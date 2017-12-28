package esau.lxq.net.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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
import esau.lxq.net.Controller;
import esau.lxq.net.ControllerFactory;

public class LxqServerImpl implements LxqServer {

    private ServerSocket server;

    private Controller ctrl = ControllerFactory.getController();

    private String IP;
    private int port;

    private boolean isClose = false;

    private int bufferSize = 8192;

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
                request = getRequest(in);
                if (request.getCode() == LxqRequest.CHUNK) {
                    isChunk = true;
                }
            }

            LxqResponse response = new LxqResponseImpl();

            ctrl.deal(request, response);

            print(out, response);

            in.close();
            out.close();
            socket.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void print(OutputStream out, LxqResponse response) throws Exception {

        BufferedOutputStream bos = new BufferedOutputStream(out);

        StringBuilder sb = response.toMsgText();
        
        int len=1024;

        while (sb.length() > 0) {
            
            int k=Math.min(sb.length(), len);
            String s=sb.substring(0, k);
            sb.delete(0, k);
            bos.write(s.getBytes());

        }

        bos.flush();

    }

    private LxqRequest getRequest(InputStream in) throws Exception {

        BufferedInputStream bis = new BufferedInputStream(in);
        StringBuilder sb = new StringBuilder();
        int len = 0;
        byte[] buff = new byte[bufferSize];

        while ((len = bis.read(buff)) != -1) {
            sb.append(new String(buff, 0, len));
        }

        return parse(sb);
    }

    private LxqRequest parse(StringBuilder text) {
        
//        System.out.println(text.toString());

        LxqRequest request = new LxqRequestImpl();

        // set code
        int k = text.indexOf("\n\n");
        String param = text.substring(0, k);
        int code = LxqRequest.NONE;
        try {
            code = Integer.parseInt(param.trim());
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        request.setCode(code);
        text.delete(0, k + 2);

        // set msg
        k = text.indexOf("\n\n");
        param = text.substring(0, k);
        request.setMsg(param);
        text.delete(0, k + 2);

        // set chunk
        k = text.indexOf("\n\n");
        param = text.substring(k + 2);
        request.setChunk(param);
        text.delete(k, text.length());

        // set list
        k = text.indexOf("\n");
        if(k!=-1) {
            param = text.substring(0, k);
            text.delete(0, k + 1);
        }
        
        List<String> list = new ArrayList<>();
        while (true) {
            k = text.indexOf("\n");
            if(k==-1) {
                break;
            }
            param = text.substring(0, k);
            list.add(param);
            text.delete(0, k + 1);
        }
        
        if(text.length()>0) {
            list.add(text.toString().trim());
        }
        request.setInputList(list);

        return request;

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
