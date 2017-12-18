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

        StringBuffer sb = new StringBuffer();

        sb.append(msg);

        if (resultList != null && resultList.size() > 0) {
            sb.append("\n\n");
            for (String item : resultList) {
                sb.append(item);
                sb.append("\n");
            }
        }

        return sb.toString().trim();
    }

}
