package esau.lxq.net.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
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
            socket.setSoTimeout(30000);//

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

        BufferedOutputStream bos = null;
        try {

            socket = new Socket(serverIP, port);

            bos = new BufferedOutputStream(socket.getOutputStream());

            StringBuilder text = request.toMsgText();

            int len = 1024;

            while (text.length() > 0) {

                int k = Math.min(text.length(), len);
                String s = text.substring(0, k);
                text.delete(0, k);
                
                bos.write(s.getBytes());
                
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

        StringBuilder sb = new StringBuilder();

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

    private LxqResponse parse(StringBuilder text) {
        response = new LxqResponseImpl();

        int k = text.indexOf("\n\n");

        if (k == -1) {
            response.setMsg(text.toString().trim());
            return response;
        }

        String type = text.substring(0, k);
        response.setMsg(type);
        text.delete(0, k + 2);

        List<String> resultList = new ArrayList<>();
        k = text.indexOf("\n");
        while (k != -1) {
            resultList.add(text.substring(0, k).trim());
            text.delete(0, k + 1);
        }
        if (text.length() > 0) {
            resultList.add(text.toString().trim());
        }
        response.setResultList(resultList);

        return response;
    }

}
