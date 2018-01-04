package esau.lxq.net;

import java.util.List;

import esau.lxq.entry.MsgItem;

public interface LxqResponse extends Msg{
    
    public void setMsg(String msg);
    
    public String getMsg();
    
    public LxqResponse add(MsgItem item);
    
    public void setResultList(List<MsgItem > resultList);
    
    public List<MsgItem> getResultList();

//    public StringBuilder toMsgText();
    
}
