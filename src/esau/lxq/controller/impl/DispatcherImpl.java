package esau.lxq.controller.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import esau.lxq.controller.Connection;
import esau.lxq.controller.Dispatcher;

public class DispatcherImpl implements Dispatcher{
    
    private List<Integer> pidList;
    private Map<Integer, Connection> workerConnections;

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
        
        if(workerSockets==null) {
            workerConnections=new HashMap<>();    
        }else {
            workerSockets.clear();
        }
        
        if(pidList==null) {
            pidList=new ArrayList<>();
        }else {
            pidList.clear();
        }
        
        try {
            
            for(Integer pid: workerSockets.keySet()) {
                
                Socket socket=workerSockets.get(pid);
                
                InputStream in=socket.getInputStream();
                OutputStream out=socket.getOutputStream();
                
                Connection con=new ConnectionImpl(in, out);
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
            
            System.out.println("sort : "+pidList.toString());
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

}




























