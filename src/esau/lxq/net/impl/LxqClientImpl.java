package esau.lxq.net.impl;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import esau.lxq.entry.MsgItem;
import esau.lxq.entry.Node;
import esau.lxq.entry.PNode;
import esau.lxq.net.LxqClient;
import esau.lxq.net.LxqRequest;
import esau.lxq.net.LxqResponse;
import esau.lxq.net.Msg;

public class LxqClientImpl implements LxqClient {

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
            socket.setSoTimeout(10*60*1000);//

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
            socket.setSoTimeout(10*60*1000);//

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

        DataOutputStream dos=new DataOutputStream(out);
        
        
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));

        bw.write("" + request.getCode());
        bw.write("\n");

        bw.write("" + request.getMsg());
        bw.write("\n");

        bw.write("" + request.getType());
        bw.write("\n");

        List<MsgItem> inputList = request.getInputList();
        if (inputList != null && inputList.size() > 0) {
            for (MsgItem item : inputList) {
                bw.write(item.toText());
                bw.write("\n");
            }
        }

        bw.write("\n");

        bw.write("" + request.getChunk());

        bw.flush();

    }

    private LxqResponse readResponse(InputStream in) throws Exception {
        LxqResponse response = new LxqResponseImpl();

        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String msg = br.readLine();
        response.setMsg(msg);

        List<MsgItem> results = new ArrayList<>();
        String type = br.readLine();
        if (Msg.NODE_TYPE.equals(type)) {
            response.setType(Msg.NODE_TYPE);
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                results.add(Node.parseNode(line.trim()));
            }
        } else if (Msg.PNODE_TYPE.equals(type)) {
            response.setType(Msg.PNODE_TYPE);
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                results.add(PNode.parsePNode(line.trim()));
            }
        }
        response.setResultList(results);

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
