package esau.lxq.net;

public interface LxqClient {
    
    public void execute(LxqRequest request);
    
    public LxqResponse getResponse();

}
