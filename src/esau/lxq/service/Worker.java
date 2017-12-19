package esau.lxq.service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import esau.lxq.entry.Node;
import esau.lxq.entry.NodeType;
import esau.lxq.entry.PartialTree;
import esau.lxq.parser.XMLParser;
import esau.lxq.utils.Utils;

public class Worker {

    private int pid;

    private PartialTree pt;

    private List<Node> subtrees;

    public Worker() {
        // TODO Auto-generated constructor stub
        pt = new PartialTree();
    }
    
    public List<String> getRoot(){
        List<String> list=new ArrayList<>();
        
        list.add(pt.getRoot().toText());
        
        return list;
    }
    
    public void computRangs(List<String> inputs){
        
        List<Node> rangsList=ListUtils.recoverNodeList(inputs);
        
        for(Node n1: rangsList){
            Node n2=pt.findNodeByUid(n1.getUid());
            n2.setStart(n1.getStart());
            n2.setEnd(n1.getEnd());
        }
        
    }

    public long computePrePath(String startUid, List<String> auxList) {

        long uid = Long.parseLong(startUid);
        List<Node> pp = ListUtils.recoverNodeList(auxList);

        Node node = subtrees.get(0);
        List<Node> ll = new ArrayList<>();
        while (node != null && node.isLeftOpenNode()) {
            node.setChecked(true);
            ll.add(node);
            node = node.getFirstChild();
        }

        for (int i = ll.size() - 1; i >= 0; i--) {
            Node rn = pp.remove(pp.size() - 1);
            Node ln = ll.get(i);
            ln.setUid(rn.getUid());
            ln.setEnd(pid);
        }

        if (pp.size() > 0) {
            Node p1 = pp.get(0);
            for (int i = 1; i < pp.size(); i++) {
                Node p2 = pp.get(i);
                p1.addFirstChild(p2);
                p1.setType(NodeType.PRE_NODE);
                p1.setChecked(true);
                p1 = p2;
            }
            p1.setType(NodeType.PRE_NODE);
            p1.setChecked(true);
            for (int i = 0; i < subtrees.size(); i++) {
                p1.addLastChild(subtrees.get(i));
            }
            pt.setRoot(pp.get(0));
        } else {
            pt.setRoot(subtrees.get(0));
        }

        uid = setUids(uid, pt.getRoot());
        pt.update();

        node = subtrees.get(subtrees.size() - 1);
        while (node != null && node.isRightOpenNode()) {
            node.setStart(pid);
            pp.add(node);
            node = node.getLastChild();
        }

        auxList.clear();
        auxList.addAll(ListUtils.convertNodeList(pp));

        System.out.println("Partial trees : ");
        Utils.dfsWithDepth(pt.getRoot());
        System.out.println();

        return uid;
    }

    private long setUids(long stUid, Node root) {

        Deque<Node> nodeStack = new ArrayDeque<>();
        Deque<Integer> countStack = new ArrayDeque<>();

        long uid = stUid;
        Node p = root;

        while (!nodeStack.isEmpty() || p != null) {

            while (p != null) {

                if (p.isChecked() == false) {
                    p.setUid(uid++);
                }

                nodeStack.push(p);
                countStack.push(1);
                p = p.getFirstChild();

            }

            if (!nodeStack.isEmpty()) {

                Node parent = nodeStack.peek();
                int i = countStack.peek();

                p = parent.getChildByIndex(i++);

                if (p == null) {
                    nodeStack.poll();
                    countStack.poll();
                } else {
                    countStack.poll();
                    countStack.push(i);
                }

            }

        }

        return uid;
    }

    public List<String> selectLeftOpenNode() {

        List<Node> ll = new ArrayList<>();

        Node root = pt.getRoot();
        Node p = root;

        while (p != null && p.isPreOpenNode()) {
            p = p.getFirstChild();
        }

        while (p != null && p.isLeftOpenNode()) {
            ll.add(p);
            p = p.getFirstChild();
        }

        return ListUtils.convertNodeList(ll);
    }

    public List<String> selectRightOpenNode() {

        List<Node> rl = new ArrayList<>();

        Node root = pt.getRoot();
        Node p = root;

        while (p != null && p.isPreOpenNode()) {
            p = p.getLastChild();
        }

        while (p != null && p.isRightOpenNode()) {
            rl.add(p);
            p = p.getLastChild();
        }

        return ListUtils.convertNodeList(rl);

    }

    public void buildSubtrees(String msg, String chunk) {
        try {
            this.pid = Integer.parseInt(msg);
        } catch (NumberFormatException e) {
        }
        this.subtrees = XMLParser.buildSubTrees(chunk);
    }

    public void printSubTrees() {
        System.out.println("--------------------");
        for (Node node : subtrees) {
            Utils.bfsWithRoot(node);
        }
        System.out.println("--------------------");
    }
    
    public List<String> getOpenNodes(){
        List<String> list = new ArrayList<>();

        Set<String > set=new TreeSet<>();
        Node root=pt.getRoot();
        
        Node p=root.getFirstChild();
        while(p!=null&&!p.isClosedNode()){
            set.add(p.toText());
            p=p.getFirstChild();
        }
        
        p=root.getLastChild();
        while(p!=null&&!p.isClosedNode()){
            set.add(p.toText());
            p=p.getLastChild();
        }
        
        list.addAll(set);

        return list;
    }

    public List<String> getSubTreesResponse() {
        List<String> list = new ArrayList<>();

        for (Node root : subtrees) {
            list.add(root.toBfsString());
        }

        if (pt.getRoot() != null) {
            list.add("-");
            list.add(pt.getRoot().toBfsString());
        }

        return list;
    }

    public List<String> getResultResponse() {
        return null;
    }

    public String responseSubTrees() {
        StringBuffer sb = new StringBuffer();

        for (Node root : subtrees) {
            sb.append(root.toBfsString());
            sb.append("\n");
        }

        return sb.toString().trim();
    }
}
