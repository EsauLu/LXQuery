package esau.lxq.controller.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import esau.lxq.controller.Connection;

public class ConnectionImpl implements Connection {

    private InputStream in;
    private OutputStream out;
    
    public ConnectionImpl() {
        // TODO Auto-generated constructor stub
    }

    public ConnectionImpl(InputStream in, OutputStream out) {
        super();
        this.in = in;
        this.out = out;
    }
    
    @Override
    public void destory() {
        // TODO Auto-generated method stub
        try {
            if(in!=null) {
                in.close();
            }
            if(out!=null) {
                out.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
        }
    }
    
}
