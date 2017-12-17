package esau.lxq.controller.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import esau.lxq.controller.DataStream;
import esau.lxq.controller.Dispatcher;
import esau.lxq.controller.worker.Worker;

public class DispatcherImpl implements Dispatcher {

    private List<Integer> pidList;
    private Map<Integer, DataStream> workerConnections;

    public DispatcherImpl() {
        super();
        // TODO Auto-generated constructor stub
    }

    public DispatcherImpl(Map<Integer, Socket> workerSockets) {
        super();
        // TODO Auto-generated constructor stub
        init(workerSockets);
    }

    @Override
    public void init(Map<Integer, Socket> workerSockets) {
        // TODO Auto-generated method stub

        if (workerConnections == null) {
            workerConnections = new HashMap<>();
        } else {
            workerConnections.clear();
        }

        if (pidList == null) {
            pidList = new ArrayList<>();
        } else {
            pidList.clear();
        }

        try {

            for (Integer pid : workerSockets.keySet()) {

                Socket socket = workerSockets.get(pid);

                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();

                DataStream con = new DataStreamImpl(in, out);
                workerConnections.put(pid, con);
                pidList.add(pid);

            }

            pidList.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    // TODO Auto-generated method stub
                    return o1.compareTo(o2);
                }
            });

            System.out.println("sort : " + pidList.toString());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void dispatchChunks(String filePath) {
        try {
            File file = new File(filePath);
            // File file = new File("res/test1.xml");

            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);

            System.out.println("--------------------------------------------------");
            System.out.println();
            System.out.println("Send chunk...");

            int len = 0;
            byte[] buff = new byte[4];
            long fileLength = file.length();
            long currPos = 0;
            long[] pos = getPos(fileLength, 5);

            int i = 1;
            DataStream ds = workerConnections.get(pidList.get(i-1));
            StringBuffer sb = new StringBuffer();
            while ((len = bis.read(buff)) != -1) {

                if (sb.length() > 0) {
                    byte[] tem = sb.toString().getBytes();
                    ds.write(tem, 0, tem.length);
                    sb.delete(0, sb.length());
                }

                currPos += len;
                if (currPos <= pos[i]) {
                    ds.write(buff, 0, len);
                }else{
                    sb.append(new String(buff));
                    int j = -1;
                    while ((j = sb.indexOf("<")) == -1 && (len = bis.read(buff)) != -1) {
                        sb.append(new String(buff, 0, len));
                    }

                    if (j == -1) {
                        byte[] tem = sb.toString().getBytes();
                        System.out.println(">> "+new String(tem));
                        ds.write(tem, 0, tem.length);
                        sb.delete(0, sb.length());
                    } else {
                        byte[] tem = sb.toString().substring(0, j).getBytes();
                        ds.write(tem, 0, tem.length);
                        sb.delete(0, j);
                    }
                    
                }

                i++;
                if(i<=pidList.size()){
                    ds.flush();                    
                    ds = workerConnections.get(pidList.get(i-1));
                }
                
            }
            ds.flush();
            
            bis.close();
            System.out.println();
            System.out.println("Complete!");
            System.out.println();
            System.out.println("--------------------------------------------------");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static long[] getPos(long len, int chunkNum) {
        long[] pos = new long[chunkNum + 1];
        long t = len / chunkNum + 1;
        for (int i = 0; i < chunkNum; i++) {
            pos[i] = i * t;
        }
        pos[chunkNum] = len;
        return pos;
    }
}
