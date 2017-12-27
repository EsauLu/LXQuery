package esau.lxq.net;

import java.io.OutputStream;

public interface LxqClient {
    
    public boolean commit(LxqRequest request);
    
    public boolean commit(LxqRequest request, long size);
    
    public LxqResponse getResponse();
    
    public OutputStream getOutputStream();

}
