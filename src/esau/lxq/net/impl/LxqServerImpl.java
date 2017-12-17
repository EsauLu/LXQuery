package esau.lxq.net.impl;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import esau.lxq.net.LxqRequest;
import esau.lxq.net.LxqResponse;
import esau.lxq.net.LxqServer;
import esau.lxq.net.Controller;
import esau.lxq.net.ControllerFactory;

public class LxqServerImpl implements LxqServer {

    private ServerSocket server;

    private String IP;
    private int port;

    private boolean isClose = false;

    private int bufferSize = 8192;

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

        if (isClose == false) {
            return;
        }

        try {

            server = new ServerSocket(port, 1, InetAddress.getByName(IP));

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

            LxqRequest request = getRequest(in);
            LxqResponse response = new LxqResponseImpl();

            Controller ctrl = ControllerFactory.getController();

            ctrl.deal(request, response);
            
            out.write("-- response --".getBytes());

            in.close();
            out.close();
            socket.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private LxqRequest getRequest(InputStream in) throws Exception {

        BufferedInputStream bis = new BufferedInputStream(in);
        StringBuffer sb = new StringBuffer();
        int len = 0;
        byte[] buff = new byte[bufferSize];

        while ((len = bis.read(buff)) != -1) {
            sb.append(new String(buff, 0, len));
        }

        return parse(sb);
    }

    private LxqRequest parse(StringBuffer sb) {

        LxqRequest request = new LxqRequestImpl();

        int t = sb.indexOf("\n\n");
        String head = sb.substring(0, t);
        String content = sb.substring(t + 2);

        String[] parms = head.split("\n");
        for (String param : parms) {
            int i = param.indexOf(':');
            request.setParam(param.substring(0, i), param.substring(i + 1));
        }

        request.setContent(content);

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
