package esau.lxq.net;

import java.util.List;

import esau.lxq.entry.MsgItem;
import esau.lxq.entry.Node;
import esau.lxq.entry.PNode;

public interface LxqResponse extends Msg{
    
    public void setMsg(String msg);
    
    public String getMsg();

    public void setNodeList(List<Node> list);
    
    public List<Node> getNodeList();
    
    public void setPNodeList(List<PNode> list);
    
    public List<PNode> getPNodeList();

//    public StringBuilder toMsgText();
    
}
