package esau.lxq.net;

import java.util.Map;

public interface LxqClient {
    
    public boolean execute(Map<String,  String> params);
    
    public LxqResponse getResponse();

}
