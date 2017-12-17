package esau.lxq.net.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import esau.lxq.net.LxqClient;
import esau.lxq.net.LxqResponse;

public class LxqClientImpl implements LxqClient {

    private String serverIP;
    private int port;

    private LxqResponse response;

    private Socket socket=null;

    public LxqClientImpl() {
        // TODO Auto-generated constructor stub
    }

    public LxqClientImpl(String serverIP, int port) {
        super();
        this.serverIP = serverIP;
        this.port = port;
    }

    @Override
    public boolean execute(Map<String, String> params) {
        // TODO Auto-generated method stub
        
        if(socket!=null){
            return false;
        }

        BufferedOutputStream bos = null;
        try {

            socket = new Socket(serverIP, port);
            bos = new BufferedOutputStream(socket.getOutputStream());

            String text = getRequestText(params);

            bos.write(text.getBytes());
            bos.flush();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } finally {
            try {
                if(bos!=null){
                    bos.close();
                }
            } catch (Exception e2) {
                // TODO: handle exception
            }
        }
        
        return true;

    }

    private String getRequestText(Map<String, String> params) {
        StringBuffer sb = new StringBuffer();

        for (String key : params.keySet()) {
            sb.append(key + ":" + params.get(key));
            sb.append("\n\n");
        }

        return sb.toString();
    }

    @Override
    public LxqResponse getResponse() {
        // TODO Auto-generated method stub

        if(socket==null){
            return null;
        }
        
        response = null;
        BufferedInputStream bis = null;

        try {
            bis = new BufferedInputStream(socket.getInputStream());

            int len = 0;
            byte[] buff = new byte[8192];
            
            StringBuffer sb=new StringBuffer();
            while((len=bis.read(buff))!=-1){
                String s=new String(buff, 0, len);
                sb.append(s);
            }
            
            System.out.println("Response:");
            System.out.println(sb);
            System.out.println("===========================================");
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if(socket!=null){
                    socket.close();
                    socket=null;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return response;
    }

}
