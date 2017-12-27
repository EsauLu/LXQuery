package esau.lxq.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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
        System.out.println(request.toMsgText());
//        System.out.println(request.toString());
        System.out.println();
        System.out.println("---------------------------------");

        switch (code) {
        case LxqRequest.CHUNK:

            int pid=0;
            try {
                pid=Integer.parseInt(msg);
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            worker.setPid(pid);
            response.setMsg("CHUNK");

            break;

        case LxqRequest.RECEIVE_CHUNK:{

            StringBuffer fix = receiveChunk(request, response);
            
            List<String> res=new ArrayList<>();
            res.add(fix.toString());
            
            response.setMsg("RECEIVE_CHUNK");
            response.setResultList(res);

            break;


        }

        case LxqRequest.FILL_CHUNK:{
            
            String chunk=request.getChunk();
            
            fixChunk(chunk, "./tem.xml");
            
            worker.buildSubtrees(msg, "./tem.xml");
            
            response.setMsg("FILL_CHUNK");

            break;
        }

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

        case LxqRequest.GET_ROOT: {
            response.setMsg("GET_ROOT");
            response.setResultList(worker.getRoot());

            break;
        }

        case LxqRequest.FIND_CHILD_NODES: {
            PartialTree pt = worker.getPartialTree();

            List<String> inputList = request.getInputList();
            List<Node> resultList = pt.findChildNodes(ListUtils.recoverNodeList(inputList), msg);

            response.setMsg("FIND_CHILD_NODES");
            response.setResultList(ListUtils.convertNodeList(resultList));

            break;
        }

        case LxqRequest.FIND_DESCENDANT_NODES: {
            PartialTree pt = worker.getPartialTree();

            List<String> inputList = request.getInputList();
            List<Node> resultList = pt.findDescendantNodes(ListUtils.recoverNodeList(inputList), msg);

            response.setMsg("FIND_DESCENDANT_NODES");
            response.setResultList(ListUtils.convertNodeList(resultList));

            break;
        }

        case LxqRequest.FIND_PARENT_NODES: {
            PartialTree pt = worker.getPartialTree();

            List<String> inputList = request.getInputList();
            List<Node> resultList = pt.findParentNodes(ListUtils.recoverNodeList(inputList), msg);

            response.setMsg("FIND_PARENT_NODES");
            response.setResultList(ListUtils.convertNodeList(resultList));

            break;
        }

        case LxqRequest.SHARE_NODES: {
            PartialTree pt = worker.getPartialTree();

            List<String> inputList = request.getInputList();
            List<Node> resultList = pt.findCorrespondingNodes(ListUtils.recoverNodeList(inputList));

            response.setMsg("SHARE_NODES");
            response.setResultList(ListUtils.convertNodeList(resultList));

            break;
        }

        case LxqRequest.FIND_FOLSIB_NODES: {
            PartialTree pt = worker.getPartialTree();

            List<String> inputList = request.getInputList();
            List<Node> resultList = pt.findFolSibNodes(ListUtils.recoverNodeList(inputList), msg);

            response.setMsg("FIND_FOLSIB_NODES");
            response.setResultList(ListUtils.convertNodeList(resultList));

            break;
        }

        case LxqRequest.FIND_NODES_BY_UID: {
            PartialTree pt = worker.getPartialTree();

            List<String> inputList = request.getInputList();

            List<Node> resultList = pt.findNodesByUid(ListUtils.recoverNodeList(inputList));

            response.setMsg("FIND_NODES_BY_UID");
            response.setResultList(ListUtils.convertNodeList(resultList));

            break;
        }

        case LxqRequest.FIND_CHILD_PNODES: {
            PartialTree pt = worker.getPartialTree();

            List<String> inputList = request.getInputList();
            List<PNode> resultList = pt.findChildPNodes(ListUtils.recoverPNodeList(inputList), msg);

            response.setMsg("FIND_CHILD_PNODES");
            response.setResultList(ListUtils.convertPNodeList(resultList));

            break;
        }

        case LxqRequest.FIND_DESCENDANT_PNODES: {

            PartialTree pt = worker.getPartialTree();

            List<String> inputList = request.getInputList();
            List<PNode> resultList = pt.findDescendantPNodes(ListUtils.recoverPNodeList(inputList), msg);

            response.setMsg("FIND_DESCENDANT_PNODES");
            response.setResultList(ListUtils.convertPNodeList(resultList));

            break;
        }

        case LxqRequest.FIND_PARENT_PNODES: {
            PartialTree pt = worker.getPartialTree();

            List<String> inputList = request.getInputList();
            List<PNode> resultList = pt.findParentPNodes(ListUtils.recoverPNodeList(inputList), msg);

            response.setMsg("FIND_PARENT_PNODES");
            response.setResultList(ListUtils.convertPNodeList(resultList));

            break;
        }

        case LxqRequest.SHARE_PNODES: {
            PartialTree pt = worker.getPartialTree();

            List<String> inputList = request.getInputList();
            List<PNode> resultList = pt.findCorrespondingPNodes(ListUtils.recoverPNodeList(inputList));

            response.setMsg("SHARE_PNODES");
            response.setResultList(ListUtils.convertPNodeList(resultList));

            break;
        }

        case LxqRequest.FIND_FOLSIB_PNODES: {
            PartialTree pt = worker.getPartialTree();

            List<String> inputList = request.getInputList();

            List<PNode> resultList = pt.findFolSibPNodes(ListUtils.recoverPNodeList(inputList), msg);

            response.setMsg("FIND_FOLSIB_PNODES");
            response.setResultList(ListUtils.convertPNodeList(resultList));

            break;
        }

        case LxqRequest.FIND_PNODES_BY_UID: {
            break;
        }

        default:
            break;
        }

        System.out.println();
        System.out.println(response.toMsgText());
//        System.out.println(response.toString());
        System.out.println();

        System.out.println("======================================");

    }

    private StringBuffer receiveChunk(LxqRequest request, LxqResponse response) {

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        StringBuffer fixStr = new StringBuffer();
        try {

            File temFile = new File("./tem.xml");
            if (temFile.exists()) {
                temFile.delete();
            }
            temFile.createNewFile();

            bis = new BufferedInputStream(request.getInputStream());
            bos = new BufferedOutputStream(new FileOutputStream(temFile));

            int len = 0;
            byte[] buff = new byte[8192];

            while ((len = bis.read(buff)) != -1) {
                String s = new String(buff, 0, len);
                int k = s.indexOf('<');
                if (k != -1) {
                    fixStr.append(s.substring(0, k));
                    bos.write(s.substring(k).getBytes());
                    break;
                } else {
                    fixStr.append(s);
                }
            }

            while ((len = bis.read(buff)) != -1) {
                bos.write(buff, 0, len);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {

                if (bos != null) {
                    bos.close();
                }

            } catch (Exception e2) {
                // TODO: handle exception
            }
        }

        return fixStr;
    }
    
    public void fixChunk(String chunk, String path) {
        
        File file=new File(path);
        if(!file.exists()||!file.isFile()) {
            return ;
        }
        
        BufferedOutputStream bos=null;
        
        try {
            
            bos=new BufferedOutputStream(new FileOutputStream(file, true));
            
            bos.write(chunk.getBytes());
            
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            try {
                if(bos!=null) {
                    bos.close();
                }
            } catch (Exception e2) {
                // TODO: handle exception
            }
        }
        
    }

}



























