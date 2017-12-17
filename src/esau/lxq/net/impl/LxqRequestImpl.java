package esau.lxq.net.impl;

import java.util.ArrayList;
import java.util.List;

import esau.lxq.net.LxqRequest;

public class LxqRequestImpl implements LxqRequest {

    private int code;

    private String nameTest;

    private List<String> inputList;

    private String chunk;

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
    public void setNameTest(String test) {
        // TODO Auto-generated method stub
        this.nameTest = test;
    }

    @Override
    public String getNameTest() {
        // TODO Auto-generated method stub
        return nameTest;
    }

    @Override
    public void setInputList(List<String> inputList) {
        // TODO Auto-generated method stub
        this.inputList = inputList;
    }

    @Override
    public List<String> getInputList() {
        // TODO Auto-generated method stub
        return inputList;
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
    
    public LxqRequest parse(String reqest) {
        // TODO Auto-generated method stub
        LxqRequest request = new LxqRequestImpl();
        
        int k=reqest.indexOf("\n\n");
        
        String codeStr=reqest.substring(0, k);
        
        int code=Integer.parseInt(codeStr);
        request.setCode(Integer.parseInt(codeStr));
        
        String paramsStr=reqest.substring(k+2);
        
        if(code==LxqRequest.CHUNK){

            request.setChunk(paramsStr);
            
        }else{
            
            k=paramsStr.indexOf("\n\n");
            
            String nameTest=paramsStr.substring(0, k);
            request.setNameTest(nameTest);
            
            String inputListStr=paramsStr.substring(k+2).trim();
            List<String> inputList=new ArrayList<String>();
            for(String item: inputListStr.split("\n")){
                inputList.add(item);
            }
            request.setInputList(inputList);
            
        }
        
        return request;

    }
    
    @Override
    public String toString() {
        // TODO Auto-generated method stub

        StringBuffer sb=new StringBuffer();
        sb.append(code);
        sb.append("\n\n");
        
        if(code==CHUNK){
            sb.append(chunk);
        }else{
            
            sb.append(nameTest);
            sb.append("\n\n");
            
            for(String item: inputList){
                sb.append(item);
                sb.append("\n");
            }
        }
        
        return sb.toString().trim();
        
    }

}




























