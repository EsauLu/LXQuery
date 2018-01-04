package esau.lxq.net.impl;

import java.io.InputStream;
import java.util.List;

import esau.lxq.entry.Node;
import esau.lxq.entry.PNode;
import esau.lxq.net.LxqRequest;

public class LxqRequestImpl implements LxqRequest {

    private int code;

    private String msg;

    private String chunk;

    private InputStream in;

    private String type = NODE_TYPE;

    public List<Node> nodeList;

    public List<PNode> pnodeList;

    @Override
    public void setCode(int code) {
        // TODO Auto-generated method stub
        this.code = code;
    }

    @Override
    public int getCode() {
        // TODO Auto-generated method stub
        return code;
    }

    @Override
    public void setMsg(String test) {
        // TODO Auto-generated method stub
        this.msg = test;
    }

    @Override
    public String getMsg() {
        // TODO Auto-generated method stub
        return msg;
    }

    @Override
    public void setChunk(String chunk) {
        // TODO Auto-generated method stub
        this.chunk = chunk;
    }

    @Override
    public String getChunk() {
        // TODO Auto-generated method stub
        return chunk;
    }

    @Override
    public InputStream getInputStream() {
        // TODO Auto-generated method stub
        return in;
    }

    @Override
    public void setInputStream(InputStream in) {
        // TODO Auto-generated method stub

        this.in = in;

    }

    @Override
    public void setNodeList(List<Node> list) {
        // TODO Auto-generated method stub
        this.nodeList = list;
    }

    @Override
    public List<Node> getNodeList() {
        // TODO Auto-generated method stub
        return nodeList;
    }

    @Override
    public void setPNodeList(List<PNode> list) {
        // TODO Auto-generated method stub
        this.pnodeList = list;
    }

    @Override
    public List<PNode> getPNodeList() {
        // TODO Auto-generated method stub
        return pnodeList;
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

    // public LxqRequest parse(String reqest) {
    // // TODO Auto-generated method stub
    // LxqRequest request = new LxqRequestImpl();
    //
    // int k = reqest.indexOf("\n\n");
    //
    // String codeStr = reqest.substring(0, k);
    //
    // int code = Integer.parseInt(codeStr);
    // request.setCode(Integer.parseInt(codeStr));
    //
    // String paramsStr = reqest.substring(k + 2);
    //
    // if (code == LxqRequest.CHUNK) {
    //
    // request.setChunk(paramsStr);
    //
    // } else {
    //
    // k = paramsStr.indexOf("\n\n");
    //
    // String nameTest = paramsStr.substring(0, k);
    // request.setMsg(nameTest);
    //
    // String inputListStr = paramsStr.substring(k + 2).trim();
    // List<String> inputList = new ArrayList<String>();
    // for (String item : inputListStr.split("\n")) {
    // inputList.add(item);
    // }
    // request.setInputList(inputList);
    //
    // }
    //
    // return request;
    //
    // }

    @Override
    public String toString() {
        // TODO Auto-generated method stub

        StringBuilder sb = new StringBuilder();
        sb.append("code :" + code);
        sb.append("\n\n");
        sb.append("msg :" + msg);
        sb.append("\n\n");

        if (nodeList != null) {
            sb.append("nodeList :" + nodeList.size());
        } else {
            sb.append("nodeList :" + "null");
        }
        sb.append("\n\n");

        if (pnodeList != null) {
            sb.append("pnodeList :" + pnodeList.size());
        } else {
            sb.append("pnodeList :" + "null");
        }
        sb.append("\n\n");

        sb.append("chunk :" + chunk);

        return sb.toString();

    }

    // @Override
    // public StringBuilder toMsgText() {
    // // TODO Auto-generated method stub
    // StringBuilder sb = new StringBuilder();
    // sb.append(code);
    // sb.append("\n\n");
    // sb.append(msg);
    // sb.append("\n\n");
    //
    // sb.append("-\n");
    // if (inputList != null && inputList.size() > 0) {
    // for (String item : inputList) {
    // sb.append(item);
    // sb.append("\n");
    // }
    // }
    // sb.append("\n");
    //
    // sb.append(chunk);
    //
    // return sb;
    //
    // }

}
