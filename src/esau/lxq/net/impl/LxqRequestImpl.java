package esau.lxq.net.impl;

import java.util.HashMap;
import java.util.Map;

import esau.lxq.net.LxqRequest;

public class LxqRequestImpl implements LxqRequest {
    
    private Map<String, String> params;
    
    private String content;
    
    public LxqRequestImpl() {
        // TODO Auto-generated constructor stub
        init();
    }
    
    private void init() {
        // TODO Auto-generated method stub
        this.params=new HashMap<>();
        params.put(CODE, String.valueOf(0));
    }

    @Override
    public void setCode(int code) {
        // TODO Auto-generated method stub
        params.put(CODE, String.valueOf(code));
    }

    @Override
    public int getCode() {
        // TODO Auto-generated method stub
        return Integer.parseInt(params.get(CODE));
    }

    @Override
    public void setParam(String key, String value) {
        // TODO Auto-generated method stub
        params.put(key, value);
    }

    @Override
    public String getParam(String key) {
        // TODO Auto-generated method stub
        return params.get(key);
    }

    @Override
    public String getContent() {
        // TODO Auto-generated method stub
        return this.content;
    }
    
    @Override
    public void setContent(String content) {
        // TODO Auto-generated method stub
        this.content=content;
    }
    
    @Override
    public Map<String, String> getParams() {
        // TODO Auto-generated method stub
        
        Map<String, String> temParams=new HashMap<>();
        
        for(String key: params.keySet()){
            temParams.put(key, params.get(key));
        }
        
        return temParams;
    }
    
}
