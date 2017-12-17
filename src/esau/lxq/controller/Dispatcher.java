package esau.lxq.controller;

import java.net.Socket;
import java.util.Map;

public interface Dispatcher {
    
    public void init(Map<Integer, Socket> workerSockets);
    public void dispatchChunks(String filePath);

}
