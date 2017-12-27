package esau.lxq.net.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.sun.org.apache.bcel.internal.generic.NEW;

import esau.lxq.net.LxqClient;
import esau.lxq.net.LxqRequest;
import esau.lxq.net.LxqResponse;

public class LxqClientImpl implements LxqClient {

    private String serverIP;
    private int port;

    private LxqResponse response;

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

            socket = new Socket(serverIP, port);
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

        BufferedOutputStream bos = null;
        try {

            socket = new Socket(serverIP, port);

            bos = new BufferedOutputStream(socket.getOutputStream());

            String text = request.toMsgText();

            bos.write(text.getBytes());
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
            }
        }

        return true;

    }

    @Override
    public LxqResponse getResponse() {
        // TODO Auto-generated method stub

        if (socket == null || socket.isClosed()) {
            return null;
        }

        StringBuffer sb = new StringBuffer();

        BufferedInputStream bis = null;

        try {
            bis = new BufferedInputStream(socket.getInputStream());

            int len = 0;
            byte[] buff = new byte[8192];

            while ((len = bis.read(buff)) != -1) {
                String s = new String(buff, 0, len);
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
                if (socket != null) {
                    socket.close();
                    socket = null;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return parse(sb);
    }

    private LxqResponse parse(StringBuffer sb) {
        response = new LxqResponseImpl();

        int k = sb.indexOf("\n\n");

        if (k == -1) {
            response.setMsg(sb.toString().trim());
            return response;
        }

        String type = sb.substring(0, k);
        response.setMsg(type);

        String paramsStr = sb.substring(k + 2).trim();

        List<String> resultList = new ArrayList<>();
        for (String item : paramsStr.split("\n")) {
            resultList.add(item);
        }
        response.setResultList(resultList);

        return response;
    }

}
