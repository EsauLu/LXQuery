package esau.lxq.net;

public interface LxqRequest {
    
    public void setCode(int code);
    public int getCode();
    public void setParam(String key, Object obj);
    public Object getParam(String key);

}
