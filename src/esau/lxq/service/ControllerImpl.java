package esau.lxq.service;

import java.util.List;

import esau.lxq.entry.Node;
import esau.lxq.entry.PNode;
import esau.lxq.entry.PartialTree;
import esau.lxq.net.Controller;
import esau.lxq.net.LxqRequest;
import esau.lxq.net.LxqResponse;

public class ControllerImpl implements Controller {

    private Worker worker = new Worker();

    public ControllerImpl() {

        super();

    }

    @Override
    public void deal(LxqRequest request, LxqResponse response) {
        // TODO Auto-generated method stub

        int code = request.getCode();
        String msg = request.getMsg();

        System.out.println("---------------------------------");
        System.out.println();
        System.out.println(request);
        System.out.println();
        System.out.println("---------------------------------");

        switch (code) {
        case LxqRequest.CHUNK:

            String chunk = request.getChunk();
            worker.buildSubtrees(msg, chunk);

            response.setMsg("CHUNK");
            response.setResultList(worker.getSubTreesResponse());

            break;

        case LxqRequest.LEFT_OPEN_NODES:

            response.setMsg("LEFT_OPEN_NODES");
            response.setResultList(worker.selectLeftOpenNode());

            break;

        case LxqRequest.RIGHT_OPEN_NODES:

            response.setMsg("RIGHT_OPEN_NODES");
            response.setResultList(worker.selectRightOpenNode());

            break;

        case LxqRequest.COMPUTE_PREPATH:

            List<String> auxList = request.getInputList();

            long uid = worker.computePrePath(msg, auxList);

            response.setMsg(String.valueOf(uid));
            response.setResultList(auxList);

            break;

        case LxqRequest.COMPUTE_RANGS:

            List<String> rangsList = request.getInputList();
            
            worker.computRangs(rangsList);
            
            response.setMsg("COMPUTE_RANGS");
            response.setResultList(worker.getOpenNodes());

            break;

        case LxqRequest.GET_ROOT:{            
            response.setMsg("GET_ROOT");
            response.setResultList(worker.getRoot());

            break;
        }

        case LxqRequest.FIND_CHILD_NODES:{            
            PartialTree pt=worker.getPartialTree();
            
            List<String> inputList=request.getInputList();
            List<Node> resultList=pt.findChildNodes(ListUtils.recoverNodeList(inputList), msg);
            
            response.setMsg("FIND_CHILD_NODES");
            response.setResultList(ListUtils.convertNodeList(resultList));
            
            break;
        }

        case LxqRequest.FIND_DESCENDANT_NODES:{       
            PartialTree pt=worker.getPartialTree();
            
            List<String> inputList=request.getInputList();
            List<Node> resultList=pt.findDescendantNodes(ListUtils.recoverNodeList(inputList), msg);
            
            response.setMsg("FIND_DESCENDANT_NODES");
            response.setResultList(ListUtils.convertNodeList(resultList));
            
            break;
        }

        case LxqRequest.FIND_PARENT_NODES:{       
            PartialTree pt=worker.getPartialTree();
            
            List<String> inputList=request.getInputList();
            List<Node> resultList=pt.findParentNodes(ListUtils.recoverNodeList(inputList), msg);
            
            response.setMsg("FIND_PARENT_NODES");
            response.setResultList(ListUtils.convertNodeList(resultList));
            
            break;
        }

        case LxqRequest.SHARE_NODES:{       
            PartialTree pt=worker.getPartialTree();
            
            List<String> inputList=request.getInputList();
            List<Node> resultList=pt.findCorrespondingNodes(ListUtils.recoverNodeList(inputList));
            
            response.setMsg("SHARE_NODES");
            response.setResultList(ListUtils.convertNodeList(resultList));
            
            break;
        }

        case LxqRequest.FIND_FOLSIB_NODES:{       
            PartialTree pt=worker.getPartialTree();
            
            List<String> inputList=request.getInputList();
            List<Node> resultList=pt.findFolSibNodes(ListUtils.recoverNodeList(inputList), msg);
            
            response.setMsg("FIND_FOLSIB_NODES");
            response.setResultList(ListUtils.convertNodeList(resultList));
            
            break;
        }

        case LxqRequest.FIND_NODES_BY_UID:{       
            PartialTree pt=worker.getPartialTree();
            
            List<String> inputList=request.getInputList();
            
            List<Node> resultList=pt.findNodesByUid(ListUtils.recoverNodeList(inputList));
            
            response.setMsg("FIND_NODES_BY_UID");
            response.setResultList(ListUtils.convertNodeList(resultList));
            
            break;
        }

        case LxqRequest.FIND_CHILD_PNODES:{            
            PartialTree pt=worker.getPartialTree();
            
            List<String> inputList=request.getInputList();
            List<PNode> resultList=pt.findChildPNodes(ListUtils.recoverPNodeList(inputList), msg);
            
            response.setMsg("FIND_CHILD_PNODES");
            response.setResultList(ListUtils.convertPNodeList(resultList));
            
            break;
        }

        case LxqRequest.FIND_DESCENDANT_PNODES:{   
            
            PartialTree pt=worker.getPartialTree();
            
            List<String> inputList=request.getInputList();
            List<PNode> resultList=pt.findDescendantPNodes(ListUtils.recoverPNodeList(inputList), msg);
            
            response.setMsg("FIND_DESCENDANT_PNODES");
            response.setResultList(ListUtils.convertPNodeList(resultList));
               
            break;
        }

        case LxqRequest.FIND_PARENT_PNODES:{     
            PartialTree pt=worker.getPartialTree();
            
            List<String> inputList=request.getInputList();
            List<PNode> resultList=pt.findParentPNodes(ListUtils.recoverPNodeList(inputList), msg);
            
            response.setMsg("FIND_PARENT_PNODES");
            response.setResultList(ListUtils.convertPNodeList(resultList));
            
            break;
        }

        case LxqRequest.SHARE_PNODES:{   
            PartialTree pt=worker.getPartialTree();
            
            List<String> inputList=request.getInputList();
            List<PNode> resultList=pt.findCorrespondingPNodes(ListUtils.recoverPNodeList(inputList));
            
            response.setMsg("SHARE_PNODES");
            response.setResultList(ListUtils.convertPNodeList(resultList));
              
            break;
        }

        case LxqRequest.FIND_FOLSIB_PNODES:{       
            PartialTree pt=worker.getPartialTree();
            
            List<String> inputList=request.getInputList();
            System.out.println("findFolSibPNodes String");
            for(String item: inputList) {
                System.out.println(item);
            }
            System.out.println();
            List<PNode> resultList=pt.findFolSibPNodes(ListUtils.recoverPNodeList(inputList), msg);
            
            response.setMsg("FIND_FOLSIB_PNODES");
            response.setResultList(ListUtils.convertPNodeList(resultList));
            
            break;
        }

        case LxqRequest.FIND_PNODES_BY_UID:{     
            break;
        }

        default:
            break;
        }

        System.out.println();
        System.out.println(response);
        System.out.println();

        System.out.println("======================================");

    }

}
