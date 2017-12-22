package esau.lxq.service;

import java.util.ArrayList;
import java.util.List;

import esau.lxq.entry.Node;
import esau.lxq.entry.PNode;
import esau.lxq.net.LxqResponse;

public class ListUtils {
    
    public static List<String> convertNodeList(List<Node> list){
        List<String> res=new ArrayList<>();
        for(Node node: list){
            res.add(node.toText());
        }
        return res;
    }
    
    public static List<Node> recoverNodeList(List<String> originList) {
        List<Node> list = new ArrayList<>();
        if (originList != null) {
            for (String item : originList) {
                Node node = Node.parseNode(item);
                if (node != null) {
                    list.add(node);
                }
            }
        }
        return list;
    }
    
    public static List<List<Node>> recoverNodeListByResponse(List<LxqResponse> responseLists) {
        List<List<Node>> outputLists = new ArrayList<>();   
        for (int i = 0; i < responseLists.size(); i++) {
            LxqResponse response=responseLists.get(i);
            List<Node> result = ListUtils.recoverNodeList(response.getResultList());
            outputLists.add(result);
        }
        return outputLists;
    }

    
    public static List<String> convertPNodeList(List<PNode> list){
        List<String> res=new ArrayList<>();
        for(PNode node: list){
            res.add(node.toText());
        }
        return res;
    }
    
    public static List<PNode> recoverPNodeList(List<String> originList) {
        List<PNode> list = new ArrayList<>();
        if (originList != null) {
            for (String item : originList) {
                PNode node = PNode.parsePNode(item);
                if (node != null) {
                    list.add(node);
                }
            }
        }
        return list;
    }
    
    public static List<List<PNode>> recoverPNodeListByResponse(List<LxqResponse> responseLists) {
        List<List<PNode>> outputLists = new ArrayList<>();   
        for (int i = 0; i < responseLists.size(); i++) {
            LxqResponse response=responseLists.get(i);
            List<PNode> result = ListUtils.recoverPNodeList(response.getResultList());
            outputLists.add(result);
        }
        return outputLists;
    }

}
