package esau.lxq.controller.master;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.Map;

import esau.lxq.controller.Dispatcher;
import esau.lxq.controller.impl.DispatcherImpl;
import esau.lxq.controller.worker.Worker;

public class Master {
    
    private int workerNum;
    private Dispatcher dispatcher;

    private static Master instance = new Master();

    private Master() {
        workerNum=0;
        dispatcher=new DispatcherImpl();
    }

    public static Master getInstance() {
        synchronized (instance) {
            if (instance == null) {
                synchronized (instance) {
                    instance = new Master();
                }
            }
        }
        return instance;
    }
    
    public void init(Map<Integer, Socket> workerSockets) {
        
        if(dispatcher==null) {
            dispatcher=new DispatcherImpl();
        }
        System.out.println(workerSockets.size());
        workerNum=workerSockets.size();
        dispatcher.init(workerSockets);
        
    }
    
    public void run() {
//        System.out.println("");
        String filePath="res/exit.txt";
//        String filePath="res/test0.xml";
//        String filePath="res/test0.xml";
        build(filePath);
    }
    
    public void build(String filePath) {
        
        dispatcher.dispatchChunks(filePath);
        
    }

}

































