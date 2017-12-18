package esau.lxq.service;

import esau.lxq.net.Controller;
import esau.lxq.net.LxqRequest;
import esau.lxq.net.LxqResponse;

public class ControllerImpl implements Controller {

    private Worker worker = new Worker();
    
    public ControllerImpl() {
        
        super();
        
        System.out.println("-- ControllerImpl --");
        
    }

    @Override
    public void deal(LxqRequest request, LxqResponse response) {
        // TODO Auto-generated method stub

        int code = request.getCode();

        System.out.println("---------------------------------");
        System.out.println();
        System.out.println(request);
        System.out.println();
        System.out.println("---------------------------------");

        switch (code) {
        case LxqRequest.CHUNK:
            
            String chunk=request.getChunk();
            worker.buildSubtrees(chunk);
            
            response.setMsg("Subtrees");
            response.setResultList(worker.getSubTreesResponse());

            break;
            
        case LxqRequest.LEFT_OPEN_NODES:
            
            response.setMsg("Left open nodes");
            response.setResultList(worker.selectLeftOpenNode());

            break;
            
        case LxqRequest.RIGHT_OPEN_NODES:
            
            response.setMsg("Right open nodes");
            response.setResultList(worker.selectRightOpenNode());

            break;

        case LxqRequest.FIND_CHILD:

            break;

        default:
            break;
        }
        
        System.out.println();
        System.out.println(response);
        System.out.println();
        
        System.out.println("======================================");

    }

}
