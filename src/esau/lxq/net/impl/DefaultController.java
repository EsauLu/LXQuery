package esau.lxq.net.impl;

import esau.lxq.net.LxqRequest;
import esau.lxq.net.LxqResponse;

import java.util.Map;

import esau.lxq.net.Controller;

public class DefaultController implements Controller {

    @Override
    public void deal(LxqRequest request, LxqResponse response) {
        // TODO Auto-generated method stub

        System.out.println("---------------------------------------------");
        
        Map<String,  String> params=request.getParams();
        
        for(String key: params.keySet()){
            System.out.println(key+" : ");
            System.out.println(params.get(key));
            System.out.println();
        }
        
        System.out.println("---------------------------------------------");

    }

}



















