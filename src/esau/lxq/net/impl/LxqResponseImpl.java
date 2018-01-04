package esau.lxq.net.impl;

import java.util.List;

import esau.lxq.entry.Node;
import esau.lxq.entry.PNode;
import esau.lxq.net.LxqResponse;

public class LxqResponseImpl implements LxqResponse {

    private String msg;
    
    public List<Node> nodeList;

    public List<PNode> pnodeList;

    private String type = NODE_TYPE;

    public LxqResponseImpl() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public void setMsg(String msg) {
        // TODO Auto-generated method stub
        this.msg = msg;
    }

    public List<Node> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<Node> nodeList) {
        this.nodeList = nodeList;
    }

    public List<PNode> getPNodeList() {
        return pnodeList;
    }

    public void setPNodeList(List<PNode> pnodeList) {
        this.pnodeList = pnodeList;
    }

    @Override
    public String getMsg() {
        // TODO Auto-generated method stub
        return this.msg;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub

        StringBuilder sb = new StringBuilder();

        sb.append("msg : " + msg);

        sb.append("\n\n");

        sb.append("nodeList : " + nodeList == null ? "null" : nodeList.size());
        
        sb.append("\n\n");

        sb.append("pnodeList : " + pnodeList == null ? "null" : pnodeList.size());

        return sb.toString().trim();
    }

    @Override
    public void setType(String type) {
        // TODO Auto-generated method stub
        this.type = type;
    }

    @Override
    public String getType() {
        // TODO Auto-generated method stub
        return this.type;
    }

    // @Override
    // public StringBuilder toMsgText() {
    // // TODO Auto-generated method stub
    // StringBuilder sb = new StringBuilder();
    //
    // sb.append(msg);
    // sb.append("\n\n");
    // sb.append("-");
    // if (resultList != null && resultList.size() > 0) {
    // for (String item : resultList) {
    // sb.append("\n");
    // sb.append(item);
    // }
    // }
    //
    // return sb;
    // }

}
