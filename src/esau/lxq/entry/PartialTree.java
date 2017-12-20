package esau.lxq.entry;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartialTree {

    private int pid;

    private Map<Long, Node> nodeMap;

    private Node root;

    private List<Integer> leftOpenNodes;

    private List<Integer> rightOpenNodes;

    private List<Integer> preOpenNodes;

    public PartialTree() {
        super();
        // TODO Auto-generated constructor stub
        nodeMap = new HashMap<>();
    }

    public Map<Long, Node> getNodeMap() {
        return nodeMap;
    }

    public List<Integer> getLeftOpenNodes() {
        return leftOpenNodes;
    }

    public void setLeftOpenNodes(List<Integer> leftOpenNodes) {
        this.leftOpenNodes = leftOpenNodes;
    }

    public List<Integer> getRightOpenNodes() {
        return rightOpenNodes;
    }

    public void setRightOpenNodes(List<Integer> rightOpenNodes) {
        this.rightOpenNodes = rightOpenNodes;
    }

    public List<Integer> getPreOpenNodes() {
        return preOpenNodes;
    }

    public void setPreOpenNodes(List<Integer> preOpenNodes) {
        this.preOpenNodes = preOpenNodes;
    }

    public void setNodeMap(Map<Long, Node> nodeMap) {
        this.nodeMap = nodeMap;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public Node findNodeByUid(long uid) {
        Node target = nodeMap.get(uid);
        return target;
    }

    public List<Node> findChildNodes(List<Node> inputList, String test) {

        List<Node> outputList = new ArrayList<>();

        for (int i = 0; i < inputList.size(); i++) {
            Node inputNode = inputList.get(i);
            Node originNode = nodeMap.get(inputNode.getUid());
            for (int j = 0; j < originNode.getChildNum(); j++) {
                Node ch = originNode.getChildByIndex(j);
                if (checkNameTest(test, ch)) {
                    outputList.add(ch);
                }
            }
        }

        return outputList;

    }

    public List<PNode> findChildPNodes(List<PNode> inputList, String test) {

        List<PNode> outputList = new ArrayList<PNode>();

        for (int i = 0; i < inputList.size(); i++) {
            PNode inputPNode = inputList.get(i);
            Node inputNode = inputPNode.getNode();
            Node originNode = nodeMap.get(inputNode.getUid());
            for (int j = 0; j < originNode.getChildNum(); j++) {
                Node ch = originNode.getChildByIndex(j);
                if (checkNameTest(test, ch)) {
                    PNode pNode = new PNode();
                    pNode.setNode(ch);
                    pNode.setLink(inputPNode.getLink());
                    outputList.add(pNode);
                }
            }
        }

        return outputList;

    }

    public List<Node> findDescendantNodes(List<Node> inputList, String test) {

        List<Node> outputList = new ArrayList<>();
        setIsChecked(false);

        for (int i = 0; i < inputList.size(); i++) {

            Node node = nodeMap.get(inputList.get(i).getUid());
            if (node == null) {
                continue;
            }

            Deque<Node> stack = new ArrayDeque<>();
            stack.push(node);

            while (!stack.isEmpty()) {
                Node nt = stack.pop();
                if (nt.isChecked()) {
                    continue;
                }
                nt.setChecked(true);
                for (int j = 0; j < nt.getChildNum(); j++) {
                    Node ch = nt.getChildByIndex(j);
                    if (checkNameTest(test, ch)) {
                        outputList.add(ch);
                    }
                    stack.push(ch);
                }
            }

        }

        return outputList;
    }

    public List<PNode> findDescendantPNodes(List<PNode> inputList, String test) {

        List<PNode> outputList = new ArrayList<>();
        setIsChecked(false);

        for (int i = 0; i < inputList.size(); i++) {

            PNode inputPNode = inputList.get(i);

            Node node = nodeMap.get(inputPNode.getNode().getUid());
            if (node == null) {
                continue;
            }

            Deque<Node> stack = new ArrayDeque<>();
            stack.push(node);

            while (!stack.isEmpty()) {
                Node nt = stack.pop();
                // if (nt.isChecked()) {
                // continue;
                // }
                nt.setChecked(true);
                for (int j = 0; j < nt.getChildNum(); j++) {
                    Node ch = nt.getChildByIndex(j);
                    if (checkNameTest(test, ch)) {
                        PNode pNode = new PNode();
                        pNode.setNode(ch);
                        pNode.setLink(inputPNode.getLink());
                        outputList.add(pNode);
                    }
                    stack.push(ch);
                }
            }

        }

        return outputList;
    }

    public List<Node> findParentNodes(List<Node> inputList, String test) {

        List<Node> outputList = new ArrayList<>();

        for (int i = 0; i < inputList.size(); i++) {
            Node node = nodeMap.get(inputList.get(i).getUid());
            if (node == null) {
                continue;
            }

            Node parent = node.getParent();
            if (parent != null && checkNameTest(test, parent)) {
                outputList.add(parent);
            }

        }

        return outputList;
    }

    public List<PNode> findParentPNodes(List<PNode> inputList, String test) {

        List<PNode> outputList = new ArrayList<>();

        for (int i = 0; i < inputList.size(); i++) {
            PNode inputPNode = inputList.get(i);
            Node node = nodeMap.get(inputPNode.getNode().getUid());
            if (node == null) {
                continue;
            }

            Node parent = node.getParent();
            if (parent != null && checkNameTest(test, parent)) {
                outputList.add(new PNode(parent, inputPNode.getLink()));
            }

        }

        return outputList;
    }

    public List<Node> findCorrespondingNodes(List<Node> inputList) {
        List<Node> outputList = new ArrayList<Node>();

        for (int i = 0; i < inputList.size(); i++) {
            Node node = nodeMap.get(inputList.get(i).getUid());
            if (node != null) {
                outputList.add(node);
            }
        }

        return outputList;

    }

    public List<PNode> findCorrespondingPNodes(List<PNode> inputList) {
        List<PNode> outputList = new ArrayList<PNode>();

        for (int i = 0; i < inputList.size(); i++) {
            PNode inputPNode = inputList.get(i);
            Node node = nodeMap.get(inputPNode.getNode().getUid());
            if (node != null) {
                outputList.add(new PNode(node, inputPNode.getLink()));
            }
        }

        return outputList;

    }

    public List<Node> findFolSibNodes(List<Node> inputList, String test) {

        List<Node> outputList = new ArrayList<Node>();

        setIsChecked(false);

        for (int i = 0; i < inputList.size(); i++) {

            Node tem = inputList.get(i);
            Node node = nodeMap.get(tem.getUid());

            while (!node.isChecked() && node.getFlosib() != null) {

                node.setChecked(true);
                node = node.getFlosib();

                if (checkNameTest(test, node)) {
                    outputList.add(node);
                }

            }

        }

        return outputList;

    }

    public List<PNode> findFolSibPNodes(List<PNode> inputList, String test) {

        List<PNode> outputList = new ArrayList<PNode>();

        setIsChecked(false);

        for (int i = 0; i < inputList.size(); i++) {

            PNode inputPNode = inputList.get(i);
            Node tem = inputPNode.getNode();
            Node node = nodeMap.get(tem.getUid());

            while (!node.isChecked() && node.getFlosib() != null) {

                node.setChecked(true);
                node = node.getFlosib();

                if (checkNameTest(test, node)) {
                    PNode pNode = new PNode();
                    pNode.setNode(node);
                    pNode.setLink(inputPNode.getLink());
                    outputList.add(pNode);
                }

            }

        }

        return outputList;

    }

    public List<Node> findNodesByUid(List<Node> uids) {

        List<Node> outputList = new ArrayList<Node>();

        for (int i = 0; i < uids.size(); i++) {
            Node node = nodeMap.get(uids.get(i).getUid());
            if (node != null) {
                outputList.add(node);
            }
        }

        return outputList;

    }

    public List<PNode> findPNodesByUid(List<PNode> uids) {

        List<PNode> outputList = new ArrayList<PNode>();

        for (int i = 0; i < uids.size(); i++) {
            PNode inputPNode = uids.get(i);
            Node node = nodeMap.get(inputPNode.getNode().getUid());
            if (node != null) {
                outputList.add(new PNode(node, inputPNode.getLink()));
            }
        }

        return outputList;

    }

    private void setIsChecked(boolean isChecked) {

        for (Long uid : nodeMap.keySet()) {
            Node node = nodeMap.get(uid);
            node.setChecked(isChecked);
        }

    }

    public boolean checkNameTest(String test, Node node) {
        if (test.trim().equals("*")) {
            return true;
        }
        return node.getTagName().equals(test.trim());
    }

    public void update() {

        if (this.root == null) {
            return;
        }

        nodeMap.clear();

        // root.setStart(pid);
        root.setDepth(0);
        nodeMap.put(root.getUid(), root);

        Deque<Node> que = new ArrayDeque<>();
        que.addLast(root);

        while (!que.isEmpty()) {

            Node node = que.removeFirst();

            if (!nodeMap.containsKey(node.getUid())) {
                nodeMap.put(node.getUid(), node);
            }

            Node presib = null;
            for (int i = 0; i < node.getChildNum(); i++) {
                Node nd = node.getChildByIndex(i);
                if (i > 0) {
                    nd.setPresib(node.getChildByIndex(i - 1));
                }
                if (i < node.getChildNum() - 1) {
                    nd.setFlosib(node.getChildByIndex(i + 1));
                }
                que.addLast(nd);
                nd.setDepth(node.getDepth() + 1);
                nd.setParent(node);
                if (presib != null) {
                    nd.setPresib(presib);
                    presib.setFlosib(nd);
                }
                presib = nd;
            }

        }

    }

}
