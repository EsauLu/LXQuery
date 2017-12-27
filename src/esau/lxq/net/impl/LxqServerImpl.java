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
        bos.write(response.toMsgText().getBytes());
        bos.flush();

    }

    private LxqRequest getRequest(InputStream in) throws Exception {

        BufferedInputStream bis = new BufferedInputStream(in);
        StringBuffer sb = new StringBuffer();
        int len = 0;
        byte[] buff = new byte[bufferSize];

        while ((len = bis.read(buff)) != -1) {
            sb.append(new String(buff, 0, len));
        }

        return parse(sb.toString());
    }

    private LxqRequest parse(String text) {

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
        text = text.substring(k + 2);

        // set msg
        k = text.indexOf("\n\n");
        param = text.substring(0, k);
        request.setMsg(param);
        text = text.substring(k + 2);

        // set list
        k = text.indexOf("\n\n");
        param = text.substring(0, k);
        String[] items = param.split("\n");
        List<String> list=new ArrayList<>();
        for (int i=1; i<items.length; i++) {            
            list.add(items[i]);
        }
        request.setInputList(list);
        text = text.substring(k + 2);
        
        //set chunk
        request.setChunk(text);

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
