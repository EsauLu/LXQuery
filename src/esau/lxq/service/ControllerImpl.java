package esau.lxq.service;

import esau.lxq.net.Controller;
import esau.lxq.net.LxqRequest;
import esau.lxq.net.LxqResponse;

public class ControllerImpl implements Controller {

    private Worker worker = new Worker();

    @Override
    public void deal(LxqRequest request, LxqResponse response) {
        // TODO Auto-generated method stub
        
    }

}
