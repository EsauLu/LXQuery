package esau.lxq.net;

import java.util.List;

public interface LxqRequest {

    public static final int NONE = -1;

    public static final int CHUNK = 100;

    public static final int LEFT_OPEN_NODES = 101;

    public static final int RIGHT_OPEN_NODES = 102;

    public static final int COMPUTE_PREPATH = 110;

    public static final int COMPUTE_RANGS = 111;

    public static final int GET_ROOT = 112;

    public static final int FIND_CHILD = 109;

    public static final int FIND_DESCENDANT = 113;

    public static final int FIND_PARENT = 114;

    public static final int FIND_FOLLOWING_SIBLING=115;

    public static final int FIND_NODES_BY_UID=116;
    
    public static final int SHARE_NODES=117;

    public void setCode(int code);

    public int getCode();

    public void setMsg(String msg);

    public String getMsg();

    public void setInputList(List<String> inputList);

    public List<String> getInputList();

    public void setChunk(String chunk);

    public String getChunk();

    public String toMsgText();

}
