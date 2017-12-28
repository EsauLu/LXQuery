package esau.lxq.net.impl;

import java.util.ArrayList;
import java.util.List;

import esau.lxq.net.LxqResponse;

public class LxqResponseImpl implements LxqResponse {

    private String msg;

    private List<String> resultList;

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
    public LxqResponse add(String item) {
        // TODO Auto-generated method stub
        resultList.add(item);
        return this;
    }

    @Override
    public void setResultList(List<String> resultList) {
        // TODO Auto-generated method stub
        this.resultList = resultList;
    }

    @Override
    public List<String> getResultList() {
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
    public StringBuilder toMsgText() {
        // TODO Auto-generated method stub
        StringBuilder sb = new StringBuilder();

        sb.append(msg);
        sb.append("\n\n");
        if (resultList != null && resultList.size() > 0) {
            for (String item : resultList) {
                sb.append(item);
                sb.append("\n");
            }
        } else {
            sb.append("null");
        }

        return sb;
    }

}
