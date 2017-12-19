package esau.lxq.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import esau.lxq.entry.Node;
import esau.lxq.net.LxqRequest;
import esau.lxq.net.LxqResponse;
import esau.lxq.net.impl.LxqRequestImpl;

public class PartialTreeBuilder {
    
    private int workerNum=0;
    
    private List<Integer> pidList;
    
    private ClientManager clientManager;

    public PartialTreeBuilder(int workerNum, List<Integer> pidList, ClientManager clientManager) {
        super();
        this.workerNum=workerNum;
        this.pidList=pidList;
        this.clientManager = clientManager;
    }
    

    public List<LxqResponse> build() {

        String xmlDocPath = "res/test0.xml";

        dispatchXMLDocument(xmlDocPath);

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

        Map<Integer, List<Node>> inputs=new HashMap<>();
        for (Node node : rangsMap.values()) {
            int st=node.getStart();
            int ed=node.getEnd();
            for(int pid=st; pid<=ed; pid++){
                List<Node> list=inputs.get(pid);
                if(list==null){
                    list=new ArrayList<>();
                    inputs.put(pid, list);
                }
                list.add(node);
            }
        }
        
        request.setCode(LxqRequest.COMPUTE_RANGS);
        for(int pid: pidList){
            List<Node> list=inputs.get(pid);
            if(list==null){
                continue;
            }
            request.setInputList(ListUtils.convertNodeList(list));
            clientManager.sendRequest(pid, request);
        }
        
        return clientManager.getResponseList(pidList);

    }

    private void getPrePath() {

        String startUid = "-1";
        List<String> auxList = null;
        LxqRequest request = new LxqRequestImpl();

        request.setCode(LxqRequest.COMPUTE_PREPATH);

        for (int i = 0; i < workerNum; i++) {
            int pid = pidList.get(i);

            request.setInputList(auxList);
            request.setMsg(startUid);
            LxqResponse response = clientManager.sendRequestByLock(pid, request);

            startUid = response.getMsg();
            auxList = response.getResultList();

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
            List<Node> ll = ListUtils.recoverNodeList(response.getResultList());
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
            List<Node> ll = ListUtils.recoverNodeList(response.getResultList());
            rls.add(ll);
        }

        return rls;
    }

    private void dispatchXMLDocument(String xmlDocPath) {

        StringBuffer xml = readXMLDocument(xmlDocPath);

        xml.insert(0, "<root>");
        xml.append("</root>");

        List<String> chunks = getChunks(xml);

        clientManager.sendChunks(pidList, chunks);

        // clientManager.getResponseList(pidList);

    }

    public List<String> getChunks(StringBuffer xml) {

        int[] pos = getPos(xml, workerNum);

        List<String> chunks = new ArrayList<>();
        for (int i = 0; i < workerNum; i++) {
            String chunk = xml.substring(pos[i], pos[i + 1]);
            chunks.add(chunk);
        }

        return chunks;

    }

    public StringBuffer readXMLDocument(String xmlDocPath) {

        File file = new File(xmlDocPath);
        if (!file.exists() || !file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        StringBuffer xml = new StringBuffer();

        try {

            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line = "";
            while ((line = reader.readLine()) != null) {
                xml.append(line);
                xml.append("\n");
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return xml;

    }

    private int[] getPos(StringBuffer xmlDoc, int chunkNum) {

        int[] pos = new int[chunkNum + 1];
        int t = xmlDoc.length() / chunkNum + 1;
        for (int i = 0; i < chunkNum; i++) {
            pos[i] = i * t;
        }
        pos[chunkNum] = xmlDoc.length();
        return fixPos(xmlDoc, pos);
    }

    private int[] fixPos(StringBuffer xmlDoc, int[] pos) {
        long len = xmlDoc.length();
        for (int i = 0; i < pos.length; i++) {
            if (pos[i] >= len) {
                pos[i] = (int) len;
                continue;
            }
            while (xmlDoc.charAt(pos[i]) != '<') {
                pos[i]--;
            }
        }
        return pos;
    }

    private static long[] getPos(long len, int chunkNum) {
        long[] pos = new long[chunkNum + 1];
        long t = len / chunkNum + 1;
        for (int i = 0; i < chunkNum; i++) {
            pos[i] = i * t;
        }
        pos[chunkNum] = len;
        return pos;
    }

}
