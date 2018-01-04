package esau.lxq.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import esau.lxq.entry.MsgItem;
import esau.lxq.entry.Node;
import esau.lxq.net.LxqRequest;
import esau.lxq.net.LxqResponse;
import esau.lxq.net.impl.LxqRequestImpl;

public class PartialTreeBuilder {

    private int workerNum = 0;

    private List<Integer> pidList;

    private ClientManager clientManager;

    public PartialTreeBuilder(int workerNum, List<Integer> pidList, ClientManager clientManager) {
        super();
        this.workerNum = workerNum;
        this.pidList = pidList;
        this.clientManager = clientManager;
    }

    public List<LxqResponse> build(String xmlDocPath) {

        dispatchXMLDocument(xmlDocPath);

        // Compute pre-path

        getPrePath();

        LxqRequest request = new LxqRequestImpl();

        List<List<Node>> lls = selectLeftOpenNodes();
        List<List<Node>> rls = selectRightOpenNodes();

        Map<Long, Node> leftRangsMap = new HashMap<>();
        for (List<Node> ll : lls) {
            for (Node node : ll) {
                leftRangsMap.put(node.getUid(), node);
            }
        }

        Map<Long, Node> rangsMap = new HashMap<>();
        for (List<Node> rl : rls) {
            for (Node node : rl) {
                Node tem = leftRangsMap.get(node.getUid());
                if (tem != null) {
                    tem.setStart(node.getStart());
                    rangsMap.put(tem.getUid(), tem);
                }
            }
        }

        Map<Integer, List<Node>> inputs = new HashMap<>();
        for (Node node : rangsMap.values()) {
            int st = node.getStart();
            int ed = node.getEnd();
            for (int pid = st; pid <= ed; pid++) {
                List<Node> list = inputs.get(pid);
                if (list == null) {
                    list = new ArrayList<>();
                    inputs.put(pid, list);
                }
                list.add(node);
            }
        }

        request.setCode(LxqRequest.COMPUTE_RANGS);
        for (int pid : pidList) {
            List<Node> list = inputs.get(pid);
            if (list == null) {
                continue;
            }
            request.setNodeList(list);
            clientManager.sendRequest(pid, request);
        }
        
        List<LxqResponse> responses=clientManager.getResponseList(pidList);

        return responses;

    }

    private void getPrePath() {

        String startUid = "-1";
        List<Node> auxList = null;
        LxqRequest request = new LxqRequestImpl();

        request.setCode(LxqRequest.COMPUTE_PREPATH);

        for (int i = 0; i < workerNum; i++) {
            int pid = pidList.get(i);

            request.setNodeList(auxList);
            request.setMsg(startUid);
            
            LxqResponse response = clientManager.sendRequestByLock(pid, request);

            startUid = response.getMsg();
            auxList = response.getNodeList();

        }

    }

    private List<List<Node>> selectLeftOpenNodes() {

        LxqRequest request = new LxqRequestImpl();
        request.setCode(LxqRequest.LEFT_OPEN_NODES);
        clientManager.sendRequests(request);

        Map<Integer, LxqResponse> responseMap = clientManager.getResponseMap(pidList);

        List<List<Node>> lls = new ArrayList<>();

        for (int i = 0; i < workerNum; i++) {
            int pid = pidList.get(i);
            LxqResponse response = responseMap.get(pid);
            List<Node> ll = response.getNodeList();
            lls.add(ll);
        }

        return lls;
    }

    private List<List<Node>> selectRightOpenNodes() {

        LxqRequest request = new LxqRequestImpl();
        request.setCode(LxqRequest.RIGHT_OPEN_NODES);
        clientManager.sendRequests(request);

        Map<Integer, LxqResponse> responseMap = clientManager.getResponseMap(pidList);

        List<List<Node>> rls = new ArrayList<>();

        for (int i = 0; i < workerNum; i++) {
            int pid = pidList.get(i);
            LxqResponse response = responseMap.get(pid);
            List<Node> ll = response.getNodeList();
            rls.add(ll);
        }

        return rls;
    }

    private void dispatchXMLDocument(String xmlDocPath) {

        clientManager.sendChunks(xmlDocPath, pidList);
        
        List<LxqResponse> results = clientManager.getResponseList(pidList);
        
        List<String> chunks=new ArrayList<>();
        
        for(LxqResponse response: results) {
            chunks.add(response.getMsg());
        }
        chunks.add("");

        clientManager.finxChunks(pidList, chunks);

        clientManager.getResponseList(pidList);

    }

}
