package esau.lxq.net;

public interface Msg {

    public static final String EMPTY = "EMPTY";

    public static final String NODE_TYPE = "NODE";

    public static final String PNODE_TYPE = "PNODE";
    
    public void setType(String type);

    public String getType();

}
