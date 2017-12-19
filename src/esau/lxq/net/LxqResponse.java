package esau.lxq.net;

import java.util.List;

public interface LxqResponse {
    
    public void setMsg(String msg);
    
    public String getMsg();
    
    public LxqResponse add(String item);
    
    public void setResultList(List<String > resultList);
    
    public List<String> getResultList();

    public String toMsgText();
    
}
