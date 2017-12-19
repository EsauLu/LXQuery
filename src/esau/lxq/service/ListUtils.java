package esau.lxq.service;

import java.util.ArrayList;
import java.util.List;

import esau.lxq.entry.Node;

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
                Node node = Node.parse(item);
                if (node != null) {
                    list.add(node);
                }
            }
        }
        return list;
    }

}
