package esau.lxq.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import esau.lxq.entry.Axis;
import esau.lxq.entry.Link;
import esau.lxq.entry.Node;
import esau.lxq.entry.NodeType;
import esau.lxq.entry.PNode;
import esau.lxq.entry.PartialTree;
import esau.lxq.entry.RemoteNode;
import esau.lxq.entry.Step;

public class PQueryExecutor {

    public static List<List<PNode>> preparePredicate(List<List<Node>> inputLists) {

        List<List<PNode>> outputLists = new ArrayList<List<PNode>>();

        for (int i = 0; i < inputLists.size(); i++) {
            List<Node> list = inputLists.get(i);
            List<PNode> plist = new ArrayList<>();
            for (Node node : list) {
                plist.add(new PNode(node, new Link(i, node.getUid())));
            }
            outputLists.add(plist);
        }

        return outputLists;

    }

    public static List<List<PNode>> predicateQuery(Step psteps, List<PartialTree> pts, List<List<PNode>> inputLists) {

        List<List<PNode>> resultLists = inputLists;

        Step pstep = psteps;
        
        // System.out.println();
        // System.out.println("Predicate : "+psteps.toXPath());
        // System.out.println();
        // System.out.println("----------------------------------------------------------");
        
        while (pstep != null) {

            resultLists = pQueryWithAixs(pstep.getAxis(), pts, resultLists, pstep.getNameTest());

            Step predicate = pstep.getPredicate();
            if (predicate != null) {

                // Querying predicate. his block will be executed when a query has a predicate.

                List<List<PNode>> intermadiate = regroupResults(resultLists);

                intermadiate = predicateQuery(predicate, pts, intermadiate);

                List<List<Node>> nodeLists = proccessPredicate(pts, intermadiate);

                resultLists = filterResults(resultLists, nodeLists);

            }

            // System.out.println();
            // System.out.println("Predicate Step" + " : " + pstep);
            // Utils.printPNodeList(resultLists);

            pstep = pstep.getNext();

        }

        return resultLists;
    }

    public static List<List<PNode>> pQueryWithAixs(Axis axis, List<PartialTree> pts, List<List<PNode>> inputLists, String test) {

        // Child axis
        if (Axis.CHILD.equals(axis)) {
            return queryChid(pts, inputLists, test);
        }

        // Descendant axis
        if (Axis.DESCENDANT.equals(axis)) {
            return queryDescendant(pts, inputLists, test);
        }

        // Parent axis
        if (Axis.PARENT.equals(axis)) {
            return queryParent(pts, inputLists, test);
        }

        // Following-sibling axis
        if (Axis.FOLLOWING_SIBLING.equals(axis)) {
            return queryFollowingSibling(pts, inputLists, test);
        }

        return null;
    }

    /**
     * Querying a step with a child axis.
     * 
     * @param pts
     *            An indexed set of partial trees.
     * @param inputLists
     *            An indexed set of input nodes.
     * @param test
     *            A string of nametest.
     * @return An indexed set of results.
     */
    public static List<List<PNode>> queryChid(List<PartialTree> pts, List<List<PNode>> inputLists, String test) {

        List<List<PNode>> outputList = new ArrayList<>();

        int p = pts.size();
        for (int i = 0; i < p; i++) {

            PartialTree pt = pts.get(i);

            List<PNode> result = pt.findChildPNodes(inputLists.get(i), test);

            outputList.add(result);

        }

        return outputList;
    }

    /**
     * Querying a step with a descendant axis.
     * 
     * @param pts
     *            An indexed set of partial trees.
     * @param inputLists
     *            An indexed set of input nodes.
     * @param test
     *            A string of nametest.
     * @return An indexed set of results.
     */
    public static List<List<PNode>> queryDescendant(List<PartialTree> pts, List<List<PNode>> inputLists, String test) {
        List<List<PNode>> outputList = new ArrayList<>();

        int p = pts.size();
        for (int i = 0; i < p; i++) {

            PartialTree pt = pts.get(i);

            List<PNode> result = pt.findDescendantPNodes(inputLists.get(i), test);

            outputList.add(result);

        }

        return outputList;
    }

    /**
     * Querying a step with a parent axis.
     * 
     * @param pts
     *            An indexed set of partial trees.
     * @param inputLists
     *            An indexed set of input nodes.
     * @param test
     *            A string of nametest.
     * @return An indexed set of results.
     */
    public static List<List<PNode>> queryParent(List<PartialTree> pts, List<List<PNode>> inputLists, String test) {

        List<List<PNode>> outputList = new ArrayList<>();

        int p = pts.size();
        for (int i = 0; i < p; i++) {

            PartialTree pt = pts.get(i);

            List<PNode> result = pt.findParentPNodes(inputLists.get(i), test);

            outputList.add(result);

        }

        return sharePNodes(pts, outputList);

    }

    /**
     * Sharing the selected nodes.
     * 
     * @param pts
     *            An indexed set of partial trees.
     * @param nodeLists
     *            An indexed set of input nodes.
     * @return An indexed set of results.
     */
    public static List<List<Node>> shareNodes(List<PartialTree> pts, List<List<Node>> nodeLists) {

        List<Node> toBeShare = new ArrayList<Node>();

        int p = pts.size();
        for (int i = 0; i < p; i++) {
            for (Node node : nodeLists.get(i)) {
                if (!NodeType.CLOSED_NODE.equals(node.getType())) {
                    toBeShare.add(node);
                }
            }
        }

        for (int i = 0; i < p; i++) {

            Set<Node> set = new HashSet<Node>();
            List<Node> inputList = nodeLists.get(i);
            PartialTree pt = pts.get(i);

            set.addAll(inputList);
            set.addAll(pt.findCorrespondingNodes(toBeShare));

            inputList.clear();
            inputList.addAll(set);

        }

        return nodeLists;
    }

    /**
     * Sharing the selected nodes.
     * 
     * @param pts
     *            An indexed set of partial trees.
     * @param nodeLists
     *            An indexed set of input nodes.
     * @return An indexed set of results.
     */
    public static List<List<PNode>> sharePNodes(List<PartialTree> pts, List<List<PNode>> nodeLists) {

        List<PNode> toBeShare = new ArrayList<PNode>();

        int p = pts.size();
        for (int i = 0; i < p; i++) {
            for (PNode pNode : nodeLists.get(i)) {
                Node node = pNode.getNode();
                if (!NodeType.CLOSED_NODE.equals(node.getType())) {
                    toBeShare.add(pNode);
                }
            }
        }

        for (int i = 0; i < p; i++) {

            Set<PNode> set = new HashSet<PNode>();
            List<PNode> inputList = nodeLists.get(i);
            PartialTree pt = pts.get(i);

            set.addAll(inputList);
            set.addAll(pt.findCorrespondingPNodes(toBeShare));

            inputList.clear();
            inputList.addAll(set);

        }

        return nodeLists;
    }

    /**
     * Sharing the selected nodes.
     * 
     * @param pts
     *            An indexed set of partial trees.
     * @param nodeLists
     *            An indexed set of input nodes.
     * @return An indexed set of results.
     */
    public static List<List<PNode>> queryFollowingSibling(List<PartialTree> pts, List<List<PNode>> inputLists, String test) {

        List<List<PNode>> outputList = new ArrayList<List<PNode>>();
        int p = pts.size();

        // Local query
        for (int i = 0; i < p; i++) {
            PartialTree pt = pts.get(i);
            List<PNode> result = pt.findFolSibPNodes(inputLists.get(i), test);
            outputList.add(result);
        }

        // Preparing remote query
        List<RemoteNode> toBeQueried = new ArrayList<RemoteNode>();
        for (int i = 0; i < p; i++) {
            for (PNode inputPNode : inputLists.get(i)) {
                Node node = inputPNode.getNode();
                Node parent = node.getParent();
                if (!NodeType.RIGHT_OPEN_NODE.equals(node.getType()) && !NodeType.PRE_NODE.equals(node.getType()) && parent != null
                        && (NodeType.RIGHT_OPEN_NODE.equals(parent.getType()) || NodeType.PRE_NODE.equals(parent.getType()))) {
                    toBeQueried.add(new RemoteNode(parent, i + 1, parent.getEnd(), inputPNode.getLink()));
                }
            }
        }

        // Regroup nodes by partial tree id
        List<List<PNode>> remoteInputList = new ArrayList<>();
        for (int i = 0; i < p; i++) {
            List<PNode> uidList = new ArrayList<PNode>();
            Set<PNode> uidSet = new HashSet<PNode>();
            for (RemoteNode rn : toBeQueried) {
                if (rn.st <= i && rn.ed >= i) {
                    uidSet.add(new PNode(rn.getNode(), rn.getLink()));
                }
            }
            uidList.addAll(uidSet);
            remoteInputList.add(uidList);
        }

        // List<List<PNode>> remoteInputList = new ArrayList<List<PNode>>();
        // for (int i = 0; i < p; i++) {
        // PartialTree pt = pts.get(i);
        // List<PNode> remoteInput = pt.findPNodesByUid(uidLists.get(i));
        // remoteInputList.add(remoteInput);
        // }
        // List<List<PNode>> remoteOutputList = queryChid(pts, remoteInputList, test);

        // Remote query
        List<List<PNode>> remoteOutputList = queryChid(pts, remoteInputList, test);

        // Merge results of local query and remote query
        for (int i = 0; i < p; i++) {
            List<PNode> result = outputList.get(i);
            List<PNode> remoteResult = remoteOutputList.get(i);

            Set<PNode> set = new HashSet<PNode>();
            set.addAll(result);
            set.addAll(remoteResult);

            result.clear();
            result.addAll(set);
        }

        return outputList;

    }

    public static List<List<PNode>> regroupResults(List<List<PNode>> inputLists) {

        List<List<PNode>> outputLists = new ArrayList<List<PNode>>();

        for (int i = 0; i < inputLists.size(); i++) {
            List<PNode> list = inputLists.get(i);

            Set<Node> set = new HashSet<Node>();
            for (PNode pnode : list) {
                set.add(pnode.getNode());
            }

            List<PNode> plist = new ArrayList<>();
            for (Node node : set) {
                plist.add(new PNode(node, new Link(i, node.getUid())));
            }

            outputLists.add(plist);
        }

        return outputLists;

    }

    public static List<List<PNode>> filterResults(List<List<PNode>> intermadiate, List<List<Node>> inputLists) {

        List<List<PNode>> outputLists = new ArrayList<List<PNode>>();

        int p = intermadiate.size();

        List<HashMap<Node, List<Link>>> pnodeMap = new ArrayList<HashMap<Node, List<Link>>>();
        for (int i = 0; i < p; i++) {

            HashMap<Node, List<Link>> map = new HashMap<Node, List<Link>>();
            pnodeMap.add(map);

            List<PNode> list = intermadiate.get(i);
            for (PNode pNode : list) {
                List<Link> links = map.get(pNode.getNode());

                if (links == null) {
                    links = new ArrayList<Link>();
                    map.put(pNode.getNode(), links);
                }

                links.add(pNode.getLink());
            }

        }

        for (int i = 0; i < p; i++) {
            HashMap<Node, List<Link>> map = pnodeMap.get(i);
            List<PNode> result = new ArrayList<>();
            List<Node> nodes = inputLists.get(i);

            for (Node node : nodes) {
                List<Link> links = map.get(node);
                if (links != null) {
                    for (Link link : links) {
                        result.add(new PNode(node, link));
                    }
                }
            }

            outputLists.add(result);
        }

        return outputLists;

    }

    public static List<List<Node>> proccessPredicate(List<PartialTree> pts, List<List<PNode>> inputLists) {

        List<Link> allLinks = new ArrayList<>();
        for (List<PNode> list : inputLists) {
            for (PNode pNode : list) {
                allLinks.add(pNode.getLink());
            }
        }

        int p = pts.size();
        List<List<Long>> uidLists = new ArrayList<List<Long>>();
        for (int i = 0; i < p; i++) {
            uidLists.add(new ArrayList<Long>());
        }

        for (Link link : allLinks) {
            List<Long> uids = uidLists.get(link.getPid());
            if (uids != null) {
                uids.add(link.getUid());
            }
        }

        List<List<Node>> resultLists = new ArrayList<List<Node>>();
        for (int i = 0; i < p; i++) {
            PartialTree pt = pts.get(i);
            List<Long> uids = uidLists.get(i);
//            resultLists.add(pt.findNodesByUid(uids));
        }

//        return QueryExecutor.shareNodes(pts, resultLists);
        return null;
    }

}
