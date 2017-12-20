package esau.lxq.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import esau.lxq.entry.Axis;
import esau.lxq.entry.Node;
import esau.lxq.entry.NodeType;
import esau.lxq.entry.PNode;
import esau.lxq.entry.PartialTree;
import esau.lxq.entry.RemoteNode;
import esau.lxq.entry.Step;
import esau.lxq.net.LxqRequest;
import esau.lxq.net.LxqResponse;
import esau.lxq.net.impl.LxqRequestImpl;
import esau.lxq.utils.Utils;

public class QueryExecutor {

    
    private int partialTreesNum=0;
    
    private List<Integer> pidList;
    
    private ClientManager clientManager;

    public QueryExecutor(List<Integer> pidList, ClientManager clientManager) {
        super();
        this.pidList = pidList;
        this.clientManager = clientManager;
        this.partialTreesNum=pidList.size();
    }

    
    public List<List<Node>> query(Step steps) {

        int p = partialTreesNum;
        
        List<List<Node>> resultList = new ArrayList<List<Node>>();
        
        LxqRequest request=new LxqRequestImpl();
        request.setCode(LxqRequest.GET_ROOT);

        for (int i = 0; i < p; i++) {
            List<Node> tem = new ArrayList<>();
            int pid=pidList.get(i);
            clientManager.sendRequest(pid, request);
            LxqResponse response=clientManager.getResponse(pid);
            List<String> result=response.getResultList();
            tem.add(Node.parse(result.get(0)));
            resultList.add(tem);
        }

//        Step step=steps;
//        while (step!=null) {
//
//            resultList = queryWithAixs(step.getAxis(), pts, resultList, step.getNameTest());
//
//            Step predicate = step.getPredicate();
//            if (predicate != null) {
//                // Querying predicate. his block will be executed when a query has a predicate.
//                
//                List<List<PNode>> intermadiate=PQueryExecutor.preparePredicate(resultList);
//                
//                intermadiate=PQueryExecutor.predicateQuery(predicate, pts, intermadiate);
//                
//                resultList=PQueryExecutor.proccessPredicate(pts, intermadiate);
//                
//            }
//
//            System.out.println();
//            System.out.println("Step" + " : " + step);
//            Utils.print(resultList);
//            
//            step=step.getNext();
//
//        }

        return resultList;
    }

    /**
     * Querying a step with an axis.
     * 
     * @param axis
     *            The axis of the step.
     * @param pts
     *            An indexed set of partial trees.
     * @param inputLists
     *            An indexed set of input nodes.
     * @param test
     *            A string of nametest.
     * @return An indexed set of results.
     */
    public static List<List<Node>> queryWithAixs(Axis axis, List<PartialTree> pts, List<List<Node>> inputLists, String test) {

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
    public static List<List<Node>> queryChid(List<PartialTree> pts, List<List<Node>> inputLists, String test) {

        List<List<Node>> outputList = new ArrayList<>();

        int p = pts.size();
        for (int i = 0; i < p; i++) {

            PartialTree pt = pts.get(i);

            List<Node> result = pt.findChildNodes(inputLists.get(i), test);

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
    public static List<List<Node>> queryDescendant(List<PartialTree> pts, List<List<Node>> inputLists, String test) {
        List<List<Node>> outputList = new ArrayList<>();

        int p = pts.size();
        for (int i = 0; i < p; i++) {

            PartialTree pt = pts.get(i);

            List<Node> result = pt.findDescendantNodes(inputLists.get(i), test);

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
    public static List<List<Node>> queryParent(List<PartialTree> pts, List<List<Node>> inputLists, String test) {

        List<List<Node>> outputList = new ArrayList<>();

        int p = pts.size();
        for (int i = 0; i < p; i++) {

            PartialTree pt = pts.get(i);

            List<Node> result = pt.findParentNodes(inputLists.get(i), test);

            outputList.add(result);

        }

        return shareNodes(pts, outputList);

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
    public static List<List<Node>> queryFollowingSibling(List<PartialTree> pts, List<List<Node>> inputLists, String test) {

        List<List<Node>> outputList = new ArrayList<List<Node>>();
        int p = pts.size();

        // Local query
        for (int i = 0; i < p; i++) {
            PartialTree pt = pts.get(i);
            List<Node> result = pt.findFolSibNodes(inputLists.get(i), test);
            outputList.add(result);
        }

        // Preparing remote query
        List<RemoteNode> toBeQueried = new ArrayList<RemoteNode>();
        for (int i = 0; i < p; i++) {
            for (Node n : inputLists.get(i)) {
                Node parent = n.getParent();
                if (!NodeType.RIGHT_OPEN_NODE.equals(n.getType()) && !NodeType.PRE_NODE.equals(n.getType()) && parent != null
                        && (NodeType.RIGHT_OPEN_NODE.equals(parent.getType()) || NodeType.PRE_NODE.equals(parent.getType()))) {
                    toBeQueried.add(new RemoteNode(parent, i + 1, parent.getEnd()));
                }
            }
        }

        // Regroup nodes by partial tree id
        List<List<Long>> uidLists = new ArrayList<>();
        for (int i = 0; i < p; i++) {
            List<Long> uidList = new ArrayList<Long>();
            Set<Long> uidSet = new HashSet<Long>();
            for (RemoteNode rn : toBeQueried) {
                if (rn.st <= i && rn.ed >= i) {
                    uidSet.add(rn.node.getUid());
                }
            }
            uidList.addAll(uidSet);
            uidLists.add(uidList);
        }

        List<List<Node>> remoteInputList = new ArrayList<List<Node>>();
        for (int i = 0; i < p; i++) {
            PartialTree pt = pts.get(i);
            List<Node> remoteInput = pt.findNodesByUid(uidLists.get(i));
            remoteInputList.add(remoteInput);
        }

        // Remote query
        List<List<Node>> remoteOutputList = queryChid(pts, remoteInputList, test);

        // Merge results of local query and remote query
        for (int i = 0; i < p; i++) {
            List<Node> result = outputList.get(i);
            List<Node> remoteResult = remoteOutputList.get(i);

            Set<Node> set = new HashSet<Node>();
            set.addAll(result);
            set.addAll(remoteResult);

            result.clear();
            result.addAll(set);
        }

        return outputList;

    }

}
