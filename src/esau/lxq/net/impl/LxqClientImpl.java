package esau.lxq.net.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import esau.lxq.net.LxqClient;
import esau.lxq.net.LxqRequest;
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
    public boolean execute(LxqRequest request) {
        // TODO Auto-generated method stub
        
        if(socket!=null){
            return false;
        }

        BufferedOutputStream bos = null;
        try {

            socket = new Socket(serverIP, port);
            
            bos = new BufferedOutputStream(socket.getOutputStream());

            String text = getRequestText(request);

            bos.write(text.getBytes());
            bos.flush();
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } finally {
            try {
                if(socket!=null){
                    socket.shutdownOutput();
                }
            } catch (Exception e2) {
                // TODO: handle exception
            }
        }
        
        return true;

    }

    private String getRequestText(LxqRequest request) {
        StringBuffer sb = new StringBuffer();
        
        int code=request.getCode();
        
        sb.append(code);
        sb.append("\n\n");
        
        if(code==LxqRequest.CHUNK){
            sb.append(request.getChunk());
        }else{

            sb.append(request.getNameTest());
            sb.append("\n\n");
            
            for(String item: request.getInputList()){
                sb.append(item);
                sb.append("\n");
            }
            
        }

        return sb.toString().trim();
    }

    @Override
    public LxqResponse getResponse() {
        // TODO Auto-generated method stub

        if(socket==null){
            return null;
        }
        
        StringBuffer sb=new StringBuffer();

        BufferedInputStream bis = null;
        
        try {
            bis = new BufferedInputStream(socket.getInputStream());

            int len = 0;
            byte[] buff = new byte[8192];

            while((len=bis.read(buff))!=-1){
                String s=new String(buff, 0, len);
                sb.append(s);
            }
            
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

        response = new LxqResponseImpl();
        

        return parse(sb);
    }
    
    private LxqResponse parse(StringBuffer sb){
        response = new LxqResponseImpl();
        
        int k=sb.indexOf("\n\n");
        
        String type=sb.substring(0, k);
        response.setType(type);
        
        String paramsStr=sb.substring(k+2).trim();
        
        List<String> resultList=new ArrayList<>();
        for(String item: paramsStr.split("\n")){
            resultList.add(item);
        }
        response.setResultList(resultList);

        return response;
    }

}




















