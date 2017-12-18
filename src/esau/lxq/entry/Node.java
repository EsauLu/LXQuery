package esau.lxq.entry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Node {

    private int uid;

    private String tagName;

    private NodeType type;

    private boolean isChecked;

    private Node parent;

    private Node presib;

    private Node flosib;

    private int start;

    private int end;

    private int depth;

    private List<Node> childList;

    public Node() {
        super();
        // TODO Auto-generated constructor stub

        this.type = NodeType.CLOSED_NODE;
        this.childList = new ArrayList<Node>();

    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getPresib() {
        return presib;
    }

    public void setPresib(Node presib) {
        this.presib = presib;
    }

    public Node getFlosib() {
        return flosib;
    }

    public void setFlosib(Node flosib) {
        this.flosib = flosib;
    }

    public List<Node> getChildList() {
        return childList;
    }

    public void setChildList(List<Node> childList) {
        this.childList = childList;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    public String toString() {
        if (NodeType.CLOSED_NODE.equals(type)) {
            return " " + tagName + uid + " ";
        } else if (NodeType.LEFT_OPEN_NODE.equals(type)) {
            return " +" + tagName + uid + " ";
        }
        if (NodeType.RIGHT_OPEN_NODE.equals(type)) {
            return " " + tagName + uid + "+ ";
        } else {
            return " +" + tagName + uid + "+ ";
        }
    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub

        if (obj instanceof Node) {

            Node node = (Node) obj;

            if (node.getUid() == uid && node.tagName.equals(tagName) && node.getType().equals(type)) {
                return true;
            }

        }

        return false;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return Objects.hash(uid, tagName, type);
    }
    
    public String toBfsString(){
        String s=toString().trim();
        
        String chs="";
        if(childList.size()>0){
            for(int i=0;i<childList.size()-1;i++){
                chs+=childList.get(i).toBfsString();
                chs+=", ";
            }
            chs+=childList.get(childList.size()-1).toBfsString();
            
            s+=":{"+chs+"}";
        }
        
        return s;
    }

}
































