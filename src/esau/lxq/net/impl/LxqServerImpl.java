package esau.lxq.net.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import esau.lxq.net.LxqRequest;
import esau.lxq.net.LxqResponse;
import esau.lxq.net.LxqServer;
import esau.lxq.net.Msg;
import esau.lxq.entry.Node;
import esau.lxq.entry.PNode;
import esau.lxq.net.Controller;
import esau.lxq.net.ControllerFactory;
import esau.lxq.net.EntryTransfer;

public class LxqServerImpl extends EntryTransfer implements LxqServer {

    private ServerSocket server;

    private Controller ctrl = ControllerFactory.getController();

    private String IP;
    private int port;

    private boolean isClose = false;

    private boolean isChunk = false;

    public LxqServerImpl() {
        // TODO Auto-generated constructor stub
    }

    public LxqServerImpl(String iP, int port) {
        super();
        this.IP = iP;
        this.port = port;
    }

    @Override
    public void start() {
        // TODO Auto-generated method stub

        if (isClose == true) {
            return;
        }

        try {

            server = new ServerSocket(port, 1, InetAddress.getByName(IP));

            System.out.println("Local : " + IP + ":" + port);

            while (isClose == false) {

                Socket socket = server.accept();

                solve(socket);

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void solve(Socket socket) {
        // TODO Auto-generated method stub

        try {

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            LxqRequest request = null;
            if (isChunk) {
                request = new LxqRequestImpl();
                request.setCode(LxqRequest.RECEIVE_CHUNK);
                request.setInputStream(in);
                isChunk = false;
            } else {
                request = readRequest(in);
                if (request.getCode() == LxqRequest.CHUNK) {
                    isChunk = true;
                }
            }

            LxqResponse response = new LxqResponseImpl();

            ctrl.deal(request, response);

            writeResponse(out, response);

            in.close();
            out.close();
            socket.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private LxqRequest readRequest(InputStream in) throws Exception {

        LxqRequest request = new LxqRequestImpl();

        DataInputStream dis=new DataInputStream(in);

        // code
        request.setCode(dis.readInt());

        // msg
        request.setMsg(dis.readUTF());

        // type
        String type = dis.readUTF();
        request.setType(type);

        // list
        int size=dis.readInt();  
        if (Msg.NODE_TYPE.equals(type)) {
            List<Node> list = new ArrayList<>();        
            for (int i = 0; i < size; i++) {
                list.add(readNode(dis));
            }
            request.setNodeList(list);
        } else if (Msg.PNODE_TYPE.equals(type)) {
            List<PNode> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                list.add(readPNode(dis));
            }
            request.setPNodeList(list);
        }       

        // chunk
        request.setChunk(dis.readUTF());
        
        return request;

    }

    private void writeResponse(OutputStream out, LxqResponse response) throws Exception {

        DataOutputStream dos=new DataOutputStream(out);

        // msg
        dos.writeUTF(response.getMsg());

        // type
        String type=response.getType();
        dos.writeUTF(type);

        //list
        if (Msg.NODE_TYPE.equals(type)) {
            List<Node> list = response.getNodeList();
            int size = list.size();
            dos.write(size);
            for (int i = 0; i < size; i++) {
                Node node = list.get(i);
                writeNode(dos, node);
            }
        } else if (Msg.PNODE_TYPE.equals(type)) {
            List<PNode> list = response.getPNodeList();
            int size = list.size();
            dos.write(size);
            for (int i = 0; i < size; i++) {
                PNode pNode = list.get(i);
                writePNode(dos, pNode);
            }
        }

        dos.flush();

    }

    public String getIP() {
        return IP;
    }

    public void setIP(String iP) {
        IP = iP;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isClose() {
        return isClose;
    }

    public void close() {
        this.isClose = true;
    }

}
