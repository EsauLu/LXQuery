package esau.lxq.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import esau.lxq.entry.Node;
import esau.lxq.net.LxqRequest;
import esau.lxq.net.LxqResponse;
import esau.lxq.net.impl.LxqClientImpl;
import esau.lxq.net.impl.LxqRequestImpl;
import esau.lxq.parser.XMLParser;
import esau.lxq.utils.Utils;

public class Master {
    
    private int workerNum=0;

    private List<Integer> pidList;

    private ClientManager clientManager = new ClientManager();

    private String[] xpaths = {
            // "/descendant::A[descendant::B/descendant::C/parent::B]/following-sibling::B[descendant::B/descendant::C[descendant::G/descendant::H/parent::I]/parent::B]",
            "/child::A/descendant::B/descendant::C",
            // "/child::A/descendant::B/descendant::C/parent::B",
            // "/descendant::B/following-sibling::B",
            // "/descendant::B[following-sibling::B/child::C]/child::C",
            // "/descendant::D[parent::B[descendant::E]]"
    };

    public Master() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Master(int num) {
        this.workerNum=num;
    }

    public void start() {
        
        pidList=new ArrayList<>();
        for(int i=0;i<workerNum;i++){
            pidList.add(i);
        }
        
        clientManager.initClients(pidList);
        
        build();
    }

    private void build() {

        String xmlDocPath = "res/test0.xml";
        
        dispatchXMLDocument(xmlDocPath);

    }
    
    private void getPrePath(){
        
        LxqRequest request=new LxqRequestImpl();        
        request.setCode(LxqRequest.LEFT_OPEN_NODES);
        
    }

    private void dispatchXMLDocument(String xmlDocPath) {        

        StringBuffer xml = readXMLDocument(xmlDocPath);        
        List<String> chunks=getChunks(xml);
        
        clientManager.sendChunks(pidList, chunks);
        
        List<LxqResponse> responses=clientManager.getResponses(pidList);
        
        System.out.println("==========");
        for(LxqResponse response: responses){
            System.out.println(response);
            System.out.println("-----");
        }
        System.out.println("==========");

    }
    
    public List<String> getChunks(StringBuffer xml){
        
        int[] pos=getPos(xml, workerNum);
        
        List<String> chunks=new ArrayList<>();
        for(int i=0;i<workerNum;i++){
            String chunk=xml.substring(pos[i], pos[i+1]);
            chunks.add(chunk);
        }
        
        return chunks;
        
    }
    
    public StringBuffer readXMLDocument(String xmlDocPath){
        
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
