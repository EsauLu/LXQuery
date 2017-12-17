package esau.lxq.net;

import java.util.Map;

public interface LxqRequest {

    public static final String CODE="code";
    public static final String CONTENT="content";
    
    public void setCode(int code);
    public int getCode();
    public void setParam(String key, String value);
    public String getParam(String key);
    
    public String getContent();
    public void setContent(String content);
    
    public Map<String, String> getParams();

}
