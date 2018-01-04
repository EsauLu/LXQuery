package esau.lxq.net.impl;

import java.util.ArrayList;
import java.util.List;

import esau.lxq.entry.MsgItem;
import esau.lxq.net.LxqResponse;

public class LxqResponseImpl implements LxqResponse {

    private String msg;

    private List<MsgItem> resultList;

    private String type = NODE_TYPE;

    public LxqResponseImpl() {
        super();
        // TODO Auto-generated constructor stub
        resultList = new ArrayList<>();
    }

    @Override
    public void setMsg(String msg) {
        // TODO Auto-generated method stub
        this.msg = msg;
    }

    @Override
    public String getMsg() {
        // TODO Auto-generated method stub
        return this.msg;
    }

    @Override
    public LxqResponse add(MsgItem item) {
        // TODO Auto-generated method stub
        resultList.add(item);
        return this;
    }

    @Override
    public void setResultList(List<MsgItem> resultList) {
        // TODO Auto-generated method stub
        this.resultList = resultList;
    }

    @Override
    public List<MsgItem> getResultList() {
        // TODO Auto-generated method stub
        return this.resultList;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub

        StringBuilder sb = new StringBuilder();

        sb.append("msg : " + msg);

        sb.append("\n\n");

        sb.append("resultList : " + resultList == null ? "null" : resultList.size());

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
