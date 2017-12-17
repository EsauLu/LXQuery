package esau.lxq.controller;

import java.io.IOException;

public interface DataStream {
    
//    public void write();
//    public void read();
    
    public void destory();
    
    public void write(byte[] data, int off, int len) throws IOException;
    
    public int read(byte[] data) throws IOException;
    
    public void flush() throws IOException;
    
}
