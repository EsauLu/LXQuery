package esau.lxq.net;

import java.io.InputStream;
import java.util.List;

import esau.lxq.entry.MsgItem;
import esau.lxq.entry.Node;
import esau.lxq.entry.PNode;

public interface LxqRequest extends Msg{

    public static final int NONE = -1;

    public static final int CHUNK = 98;

    public static final int RECEIVE_CHUNK = 99;

    public static final int FILL_CHUNK = 100;

    public static final int LEFT_OPEN_NODES = 101;

    public static final int RIGHT_OPEN_NODES = 102;

    public static final int COMPUTE_PREPATH = 110;

    public static final int COMPUTE_RANGS = 111;

    public static final int GET_ROOT = 112;

    public static final int FIND_CHILD_NODES = 109;

    public static final int FIND_DESCENDANT_NODES = 113;

    public static final int FIND_PARENT_NODES = 114;

    public static final int FIND_FOLSIB_NODES = 115;

    public static final int FIND_NODES_BY_UID = 116;

    public static final int SHARE_NODES = 117;

    public static final int FIND_CHILD_PNODES = 118;

    public static final int FIND_DESCENDANT_PNODES = 119;

    public static final int FIND_PARENT_PNODES = 120;

    public static final int FIND_FOLSIB_PNODES = 121;

    public static final int FIND_PNODES_BY_UID = 126;

    public static final int SHARE_PNODES = 122;

    public void setCode(int code);

    public int getCode();

    public void setMsg(String msg);

    public String getMsg();

//    public void setInputList(List<MsgItem> inputList);
//
//    public List<MsgItem> getInputList();
    
    public void setNodeList(List<Node> list);
    
    public List<Node> getNodeList();
    
    public void setPNodeList(List<PNode> list);
    
    public List<PNode> getPNodeList();

    public void setChunk(String chunk);

    public String getChunk();

    // public StringBuilder toMsgText();

    public InputStream getInputStream();

    public void setInputStream(InputStream in);

}
