package esau.lxq.service;

import java.util.ArrayList;
import java.util.List;

import esau.lxq.entry.Node;
import esau.lxq.entry.NodeType;
import esau.lxq.entry.PartialTree;
import esau.lxq.parser.XMLParser;
import esau.lxq.utils.Utils;

public class Worker {

    private PartialTree pt;

    private List<Node> subtrees;
    

    public Worker() {
        // TODO Auto-generated constructor stub
    }
    
    public List<String> selectLeftOpenNode() {
        
        if(subtrees==null||subtrees.size()==0){
            return null;
        }

        Node p = subtrees.get(0);

        List<Node> ll = new ArrayList<>();

        while (p != null && NodeType.LEFT_OPEN_NODE.equals(p.getType())) {
            ll.add(p);
            List<Node> childList = p.getChildList();
            if (childList.size() > 0) {
                p = p.getChildList().get(0);
            } else {
                p = null;
            }
        }
        
        return convertNodeList(ll);
    }

    public List<String> selectRightOpenNode() {

        if(subtrees==null||subtrees.size()==0){
            return null;
        }

        Node p = subtrees.get(subtrees.size() - 1);

        List<Node> rl = new ArrayList<>();

        while (p != null && NodeType.RIGHT_OPEN_NODE.equals(p.getType())) {
            rl.add(p);
            List<Node> childList = p.getChildList();
            if (childList.size() > 0) {
                p = childList.get(childList.size() - 1);
            } else {
                p = null;
            }
        }
        
        return convertNodeList(rl);
        
    }


    public void buildSubtrees(String chunk) {
        subtrees = XMLParser.buildSubTrees(chunk);
    }
    
    public void printSubTrees(){
        System.out.println("--------------------");
        for(Node node: subtrees){
            Utils.bfsWithRoot(node);
        }
        System.out.println("--------------------");
    }
    
    public List<String> getSubTreesResponse(){
        List<String> list=new ArrayList<>();
        
        for(Node root: subtrees){
            list.add(root.toBfsString());
        }
        
        return list;
    }
    
    public List<String> getResultResponse(){
        return null;
    }
    
    public String responseSubTrees(){
        StringBuffer sb=new StringBuffer();
        
        for(Node root: subtrees){
            sb.append(root.toBfsString());
            sb.append("\n");
        }
        
        return sb.toString().trim();
    }
    
    private List<String> convertNodeList(List<Node> list){
        List<String> res=new ArrayList<>();
        StringBuffer item=new StringBuffer();
        for(Node node: list){
            item.delete(0, item.length());
            item.append(node.getUid());
            item.append('\n');
            item.append(node.getTagName());
            item.append('\n');
            item.append(node.getType());
            res.add(item.toString().trim());
        }
        return res;
    }

}
