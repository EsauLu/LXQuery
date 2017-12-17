package esau.lxq.controller.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import esau.lxq.controller.DataStream;

public class DataStreamImpl implements DataStream {

    private BufferedInputStream bis;
    private BufferedOutputStream bos;
    
    public DataStreamImpl() {
        // TODO Auto-generated constructor stub
    }

    public DataStreamImpl(InputStream in, OutputStream out) {
        super();
        this.bis = new BufferedInputStream(in);
        this.bos = new BufferedOutputStream(out);
    }
    
    @Override
    public void write(byte[] data, int off, int len) throws IOException{
        // TODO Auto-generated method stub
        bos.write(data, off, len);
    }
    
    @Override
    public int read(byte[] data) throws IOException{
        // TODO Auto-generated method stub
        return bis.read(data);
    }
    
    @Override
    public void flush() throws IOException{
        // TODO Auto-generated method stub
        bos.flush();
    }
    
    @Override
    public void destory() {
        // TODO Auto-generated method stub
        try {
            if(bis!=null) {
                bis.close();
            }
            if(bos!=null) {
                bos.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
        }
    }
    
}
