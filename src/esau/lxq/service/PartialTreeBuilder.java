package esau.lxq.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.org.apache.bcel.internal.generic.NEW;

import esau.lxq.entry.Node;
import esau.lxq.net.LxqRequest;
import esau.lxq.net.LxqResponse;
import esau.lxq.net.impl.LxqRequestImpl;
import sun.print.resources.serviceui_fr;

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

    public List<LxqResponse> build() {

        // String xmlDocPath = "res/test0.xml";
        String xmlDocPath = "res/test2.xml";

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

        // StringBuffer xml = readXMLDocument(xmlDocPath);
        // List<String> chunks = getChunks(xml);
        //
        // clientManager.sendChunks(pidList, chunks);

        File file = new File(xmlDocPath);

        BufferedInputStream bis = null;
        try {

            bis=new BufferedInputStream(new FileInputStream(file));
            System.out.println("file length : "+file.length());
            long[] pos=getPos(file.length(), workerNum);
            dispatchXMLDocument(bis, pos);
            
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            // TODO: handle finally clause
            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private void dispatchXMLDocument(BufferedInputStream bis, long[] pos) throws IOException {
        
        int len = 0;
        int buffSize = 8192;
        byte[] buff = new byte[buffSize];

        System.out.println("dispatcher");
        
        for(long pp:pos) {
            System.out.println(pp);
        }
        
        int currPos = 0;
        for (int i = 0; i < pos.length; i++) {
            int nextlen = (int) Math.min(pos[i] - currPos, buffSize);
            StringBuffer chunk = new StringBuffer();

            System.out.println(currPos+" -- "+pos[i]+" -- "+pos.length);
            
            String s=null;
            if(currPos<pos[i]) {

                System.out.println(currPos+" -- "+pos[i]+" -- next len = "+nextlen);
                while (currPos < pos[i] && (len = bis.read(buff, 0, nextlen)) != -1) {
                    String t=new String(buff, 0, len);
                    System.out.println("> "+t);
                    chunk.append(t);
                    currPos += len;
                    nextlen = (int) Math.min(pos[i] - currPos, buffSize);
                }
                System.out.println(currPos+" -- "+pos[i]+" -2- next len = "+nextlen);

                while ((len = bis.read(buff)) != -1) {
                    currPos += len;
                    s=new String(buff, 0, len);
                    int k=s.indexOf('<');
                    if(k!=-1) {                    
                        chunk.append(s.substring(0, k));
                        s=s.substring(k);
                        break;                    
                    }
                    chunk.append(s);
                    s=null;
                }
            }

            int pid=pidList.get(i);
//            clientManager.sendChunk(pid, chunk.toString());
            
            System.out.println("chunk " + i+" :");
            System.out.println(chunk.toString());
            System.out.println();
            
            chunk=new StringBuffer();
            if(s!=null) {
                chunk.append(s);
            }            
            
        }
//        clientManager.getResponseList(pidList);

    }

    public List<String> getChunksr(StringBuffer xml) {

        int[] pos = getPos(xml, workerNum);

        List<String> chunks = new ArrayList<>();
        for (int i = 0; i < workerNum; i++) {
            String chunk = xml.substring(pos[i], pos[i + 1]);
            if (i == 0) {
                chunk = "<root>" + chunk;
            }
            if (i == workerNum - 1) {
                chunk += "</root>";
            }
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

            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            int len = 0;
            byte[] buff = new byte[8092];
            while ((len = bis.read(buff)) != -1) {
                xml.append(new String(buff, 0, len));
            }
            bis.close();

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

    private long[] getPos(long len, int chunkNum) {
        long[] pos = new long[chunkNum];
        long t = (len + chunkNum) / chunkNum;
        for (int i = 1; i < chunkNum; i++) {
            pos[i - 1] = i * t;
        }
        pos[chunkNum - 1] = len;
        return pos;
    }

}
