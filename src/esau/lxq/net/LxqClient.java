package esau.lxq.net;

public interface LxqClient {
    
    public boolean execute(LxqRequest request);
    
    public LxqResponse getResponse();

}
