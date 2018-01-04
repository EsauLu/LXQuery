package esau.lxq.net.impl;

import esau.lxq.net.LxqRequest;
import esau.lxq.net.LxqResponse;

import java.util.ArrayList;
import java.util.List;

import esau.lxq.entry.MsgItem;
import esau.lxq.entry.Node;
import esau.lxq.net.Controller;

public class DefaultController implements Controller {

    @Override
    public void deal(LxqRequest request, LxqResponse response) {
        // TODO Auto-generated method stub
        
        int code=request.getCode();
        if(code==LxqRequest.CHUNK){
            dealChunk(request, response);
        }else{
            dealOther(request, response);
        }
        

    }
    
    private void dealChunk(LxqRequest request, LxqResponse response){

        System.out.println("---------------------------------------------");
        
        System.out.println("Code : "+request.getCode());
        
        System.out.println("Chunk : ");
        
        System.out.println(request.getChunk());
        
        System.out.println("------------------------");
        List<MsgItem> resList=new ArrayList<>();
        for(int i=0;i<3;i++){
            resList.add(new Node());
        }
        response.setResultList(resList);
        
        response.setMsg("Node");
        System.out.println("---------------------------------------------");
        
    }
    
    private void dealOther(LxqRequest request, LxqResponse response){


        System.out.println("---------------------------------------------");
        
        System.out.println("Code : "+request.getCode());
        
        System.out.println("Name test : "+request.getMsg());
        
        List<MsgItem> list=request.getInputList();
        
        System.out.println();
        
        for(MsgItem item: list){
            System.out.println(item);
        }
        
        System.out.println("------------------------");
        List<MsgItem> resList=new ArrayList<>();
        for(int i=0;i<5;i++){
            resList.add(new Node());
        }
        response.setResultList(resList);
        
        response.setMsg("Node");
        System.out.println("---------------------------------------------");
    }

}



















