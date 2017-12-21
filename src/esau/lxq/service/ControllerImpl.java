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

        case LxqRequest.COMPUTE_PREPATH:

            List<String> auxList = request.getInputList();

            long uid = worker.computePrePath(msg, auxList);

            response.setMsg(String.valueOf(uid));
            response.setResultList(auxList);

            break;

        case LxqRequest.COMPUTE_RANGS:

            List<String> rangsList = request.getInputList();
            
            worker.computRangs(rangsList);
            
            response.setMsg("Open nodes");
            response.setResultList(worker.getOpenNodes());

            break;

        case LxqRequest.GET_ROOT:{            
            response.setMsg("ROOT");
            response.setResultList(worker.getRoot());

            break;
        }

        case LxqRequest.FIND_CHILD_NODES:{            
            PartialTree pt=worker.getPartialTree();
            
            List<String> inputList=request.getInputList();
            List<Node> resultList=pt.findChildNodes(ListUtils.recoverNodeList(inputList), msg);
            
            response.setMsg("childs");
            response.setResultList(ListUtils.convertNodeList(resultList));
            
            break;
        }

        case LxqRequest.FIND_DESCENDANT_NODES:{       
            PartialTree pt=worker.getPartialTree();
            
            List<String> inputList=request.getInputList();
            List<Node> resultList=pt.findDescendantNodes(ListUtils.recoverNodeList(inputList), msg);
            
            response.setMsg("descendant");
            response.setResultList(ListUtils.convertNodeList(resultList));
            
            break;
        }

        case LxqRequest.FIND_PARENT_NODES:{       
            PartialTree pt=worker.getPartialTree();
            
            List<String> inputList=request.getInputList();
            List<Node> resultList=pt.findParentNodes(ListUtils.recoverNodeList(inputList), msg);
            
            response.setMsg("parents");
            response.setResultList(ListUtils.convertNodeList(resultList));
            
            break;
        }

        case LxqRequest.SHARE_NODES:{       
            PartialTree pt=worker.getPartialTree();
            
            List<String> inputList=request.getInputList();
            List<Node> resultList=pt.findCorrespondingNodes(ListUtils.recoverNodeList(inputList));
            
            response.setMsg("Share nodes");
            response.setResultList(ListUtils.convertNodeList(resultList));
            
            break;
        }

        case LxqRequest.FIND_FOLSIB_NODES:{       
            PartialTree pt=worker.getPartialTree();
            
            List<String> inputList=request.getInputList();
            List<Node> resultList=pt.findFolSibNodes(ListUtils.recoverNodeList(inputList), msg);
            
            response.setMsg("following sibling");
            response.setResultList(ListUtils.convertNodeList(resultList));
            
            break;
        }

        case LxqRequest.FIND_NODES_BY_UID:{       
            PartialTree pt=worker.getPartialTree();
            
            List<String> inputList=request.getInputList();
            
            List<Node> resultList=pt.findNodesByUid(ListUtils.recoverNodeList(inputList));
            
            response.setMsg("Nodes");
            response.setResultList(ListUtils.convertNodeList(resultList));
            
            break;
        }

        case LxqRequest.FIND_CHILD_PNODES:{            
            PartialTree pt=worker.getPartialTree();
            
            List<String> inputList=request.getInputList();
            List<PNode> resultList=pt.findChildPNodes(ListUtils.recoverPNodeList(inputList), msg);
            
            response.setMsg("childs");
            response.setResultList(ListUtils.convertPNodeList(resultList));
            
            break;
        }

        case LxqRequest.FIND_DESCENDANT_PNODES:{   
            
            PartialTree pt=worker.getPartialTree();
            
            List<String> inputList=request.getInputList();
            List<PNode> resultList=pt.findDescendantPNodes(ListUtils.recoverPNodeList(inputList), msg);
            
            response.setMsg("descendant");
            response.setResultList(ListUtils.convertPNodeList(resultList));
               
            break;
        }

        case LxqRequest.FIND_PARENT_PNODES:{     
            PartialTree pt=worker.getPartialTree();
            
            List<String> inputList=request.getInputList();
            List<PNode> resultList=pt.findParentPNodes(ListUtils.recoverPNodeList(inputList), msg);
            
            response.setMsg("parents");
            response.setResultList(ListUtils.convertPNodeList(resultList));
            
            break;
        }

        case LxqRequest.SHARE_PNODES:{   
            PartialTree pt=worker.getPartialTree();
            
            List<String> inputList=request.getInputList();
            List<PNode> resultList=pt.findCorrespondingPNodes(ListUtils.recoverPNodeList(inputList));
            
            response.setMsg("Share nodes");
            response.setResultList(ListUtils.convertPNodeList(resultList));
              
            break;
        }

        case LxqRequest.FIND_FOLSIB_PNODES:{       
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
