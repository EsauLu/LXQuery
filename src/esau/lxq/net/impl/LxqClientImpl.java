package esau.lxq.net.impl;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import esau.lxq.entry.Node;
import esau.lxq.entry.PNode;
import esau.lxq.net.EntryTransfer;
import esau.lxq.net.LxqClient;
import esau.lxq.net.LxqRequest;
import esau.lxq.net.LxqResponse;
import esau.lxq.net.Msg;

public class LxqClientImpl extends EntryTransfer implements LxqClient {

    private String serverIP;
    private int port;

    private Socket socket = null;

    public LxqClientImpl() {
        // TODO Auto-generated constructor stub
    }

    public LxqClientImpl(String serverIP, int port) {
        super();
        this.serverIP = serverIP;
        this.port = port;
    }

    @Override
    public boolean commit(LxqRequest request, long size) {
        // TODO Auto-generated method stub

        if (!commit(request)) {
            return false;
        }

        getResponse();

        BufferedOutputStream bos = null;
        try {

            socket = new Socket();
            socket.connect(new InetSocketAddress(serverIP, port), 60 * 1000);//
            socket.setSoTimeout(10 * 60 * 1000);//

            // socket = new Socket(serverIP, port);
            bos = new BufferedOutputStream(socket.getOutputStream());

            InputStream inputStream = request.getInputStream();

            int len = 0, nextLen = 0;
            byte[] buff = new byte[8192];
            long curr = 0;

            String chunk = request.getChunk();
            if (chunk != null && !chunk.contains("/")) {
                bos.write(chunk.getBytes());
            }

            nextLen = Math.min(8192, (int) (size - curr));
            while (curr < size && (len = inputStream.read(buff, 0, nextLen)) != -1) {
                curr += len;
                nextLen = Math.min(8192, (int) (size - curr));
                bos.write(buff, 0, len);
            }

            if (chunk != null && chunk.contains("/")) {
                bos.write(chunk.getBytes());
            }

            bos.flush();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (socket != null) {
                    socket.shutdownOutput();
                }
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
                return false;
            }
        }

        return true;

    }

    @Override
    public OutputStream getOutputStream() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean commit(LxqRequest request) {
        // TODO Auto-generated method stub

        if (socket != null) {
            return false;
        }

        try {

            socket = new Socket();
            socket.connect(new InetSocketAddress(serverIP, port), 60 * 1000);//
            socket.setSoTimeout(10 * 60 * 1000);//

            OutputStream out = socket.getOutputStream();

            writeRequest(out, request);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (socket != null) {
                    socket.shutdownOutput();
                }
            } catch (Exception e2) {
                // TODO: handle exception
            }
        }

        return true;

    }

    private void writeRequest(OutputStream out, LxqRequest request) throws Exception {
        // TODO Auto-generated method stub

        DataOutputStream dos = new DataOutputStream(out);

        // Code
        dos.writeInt(request.getCode());

        // Msg
        dos.writeUTF(request.getMsg());

        // Type
        String type = request.getType();
        dos.writeUTF(type);

        // list
        if (Msg.NODE_TYPE.equals(type)) {
            List<Node> list = request.getNodeList();
            int size = list.size();
            dos.write(size);
            for (int i = 0; i < size; i++) {
                Node node = list.get(i);
                writeNode(dos, node);
            }
        } else if (Msg.PNODE_TYPE.equals(type)) {
            List<PNode> list = request.getPNodeList();
            int size = list.size();
            dos.write(size);
            for (int i = 0; i < size; i++) {
                PNode pNode = list.get(i);
                writePNode(dos, pNode);
            }
        }

        // chunk
        dos.writeUTF(request.getChunk());

        dos.flush();

    }

    private LxqResponse readResponse(InputStream in) throws Exception {
        LxqResponse response = new LxqResponseImpl();

        DataInputStream dis = new DataInputStream(in);

        //Msg
        response.setMsg(dis.readUTF());
        
        //Type
        String type=dis.readUTF();
        response.setType(type);
        
        // list
        int size = dis.readInt();        
        if (Msg.NODE_TYPE.equals(type)) {
            List<Node> list = new ArrayList<>();        
            for (int i = 0; i < size; i++) {
                list.add(readNode(dis));
            }
            response.setNodeList(list);
        } else if (Msg.PNODE_TYPE.equals(type)) {
            List<PNode> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                list.add(readPNode(dis));
            }
            response.setPNodeList(list);
        }

        return response;
    }

    @Override
    public LxqResponse getResponse() {
        // TODO Auto-generated method stub//

        LxqResponse response = null;

        if (socket == null || socket.isClosed()) {
            return null;
        }

        InputStream inputStream = null;

        try {

            inputStream = socket.getInputStream();
            response = readResponse(inputStream);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (socket != null) {
                    socket.close();
                    socket = null;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return response;
    }

}
