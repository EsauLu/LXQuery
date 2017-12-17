package esau.lxq.net;

import java.util.List;

public interface LxqResponse {

    public void setType(String type);
    
    public String getType();
    
    public LxqResponse add(String item);
    
    public void setResultList(List<String > resultList);
    
    public List<String> getResultList();

}
