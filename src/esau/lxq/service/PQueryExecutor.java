package esau.lxq.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import esau.lxq.entry.Axis;
import esau.lxq.entry.Link;
import esau.lxq.entry.Node;
import esau.lxq.entry.NodeType;
import esau.lxq.entry.PNode;
import esau.lxq.entry.RemoteNode;
import esau.lxq.entry.Step;
import esau.lxq.net.LxqRequest;
import esau.lxq.net.LxqResponse;
import esau.lxq.net.impl.LxqRequestImpl;

public class PQueryExecutor {

    private int p;

    private List<Integer> pidList;

    private ClientManager clientManager;

    public PQueryExecutor(List<Integer> pidList, ClientManager clientManager) {
        super();
        this.pidList = pidList;
        this.clientManager = clientManager;
        this.p = pidList.size();
    }

    public List<List<Node>> query(Step steps) {

        List<List<Node>> resultList = new ArrayList<List<Node>>();

        LxqRequest request = new LxqRequestImpl();
        request.setCode(LxqRequest.GET_ROOT);

        for (int i = 0; i < p; i++) {
            List<Node> tem = new ArrayList<>();
            int pid = pidList.get(i);
            clientManager.sendRequest(pid, request);
            LxqResponse response = clientManager.getResponse(pid);
            List<String> result = response.getResultList();
            tem.add(Node.parseNode(result.get(0)));
            resultList.add(tem);
        }

        List<List<PNode>> intermadiate = preparePredicate(resultList);

        intermadiate = predicateQuery(steps, intermadiate);

        for (int i = 0; i < p; i++) {
            List<PNode> pList = intermadiate.get(i);
            List<Node> result = resultList.get(i);
            Set<Node> set = new HashSet<>();
            for (PNode pNode : pList) {
                set.add(pNode.getNode());
            }
            result.clear();
            result.addAll(set);
        }

        return resultList;
    }

    public List<List<PNode>> preparePredicate(List<List<Node>> inputLists) {

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

    public List<List<PNode>> predicateQuery(Step psteps, List<List<PNode>> inputLists) {

        List<List<PNode>> resultLists = inputLists;

        Step pstep = psteps;

        while (pstep != null) {

            resultLists = pQueryWithAixs(pstep.getAxis(), resultLists, pstep.getNameTest());

            Step predicate = pstep.getPredicate();
            if (predicate != null) {

                // Querying predicate. his block will be executed when a query has a predicate.

                List<List<PNode>> intermadiate = regroupResults(resultLists);

                intermadiate = predicateQuery(predicate, intermadiate);

                List<List<Node>> nodeLists = proccessPredicate(intermadiate);

                resultLists = filterResults(resultLists, nodeLists);

            }

            pstep = pstep.getNext();

        }

        return resultLists;
    }

    public List<List<PNode>> pQueryWithAixs(Axis axis, List<List<PNode>> inputLists, String test) {

        // Child axis
        if (Axis.CHILD.equals(axis)) {
            return queryChid(inputLists, test);
        }

        // Descendant axis
        if (Axis.DESCENDANT.equals(axis)) {
            return queryDescendant(inputLists, test);
        }

        // Parent axis
        if (Axis.PARENT.equals(axis)) {
            return queryParent(inputLists, test);
        }

        // Following-sibling axis
        if (Axis.FOLLOWING_SIBLING.equals(axis)) {
            return queryFollowingSibling(inputLists, test);
        }

        return null;
    }

    public List<List<PNode>> queryChid(List<List<PNode>> inputLists, String test) {

        LxqRequest request = new LxqRequestImpl();
        request.setCode(LxqRequest.FIND_CHILD_PNODES);
        request.setMsg(test);

        return sendFindRequests(request, inputLists);

    }

    public List<List<PNode>> queryDescendant(List<List<PNode>> inputLists, String test) {

        LxqRequest request = new LxqRequestImpl();
        request.setCode(LxqRequest.FIND_DESCENDANT_PNODES);
        request.setMsg(test);

        return sendFindRequests(request, inputLists);

    }

    public List<List<PNode>> queryParentIgnoreCNode(List<List<PNode>> inputLists, String test) {
        LxqRequest request = new LxqRequestImpl();
        request.setCode(LxqRequest.FIND_PARENT_PNODES);
        request.setMsg(test);
        return sendFindRequests(request, inputLists);

    }

    public List<List<PNode>> queryParent(List<List<PNode>> inputLists, String test) {
        return sharePNodes(queryParentIgnoreCNode(inputLists, test));

    }

    private List<List<PNode>> sendFindRequests(LxqRequest request, List<List<PNode>> inputLists) {

        for (int i = 0; i < p; i++) {
            int pid = pidList.get(i);
            List<PNode> input = inputLists.get(i);
            request.setInputList(ListUtils.convertPNodeList(input));
            clientManager.sendRequest(pid, request);
        }

        List<LxqResponse> resposeLists = clientManager.getResponseList(pidList);

        return ListUtils.recoverPNodeListByResponse(resposeLists);
    }

    public List<List<PNode>> sharePNodes(List<List<PNode>> nodeLists) {

        List<PNode> toBeShare = new ArrayList<PNode>();

        Set<Long> teBeShareUid = new HashSet<>();
        for (int i = 0; i < p; i++) {
            for (PNode pNode : nodeLists.get(i)) {
                Node node = pNode.getNode();
                if (!NodeType.CLOSED_NODE.equals(node.getType()) 
                        && !teBeShareUid.contains(node.getUid())) {
                    teBeShareUid.add(node.getUid());
                    toBeShare.add(pNode);
                }
            }
        }

        LxqRequest request = new LxqRequestImpl();
        request.setCode(LxqRequest.SHARE_PNODES);
        request.setInputList(ListUtils.convertPNodeList(toBeShare));
        clientManager.sendRequests(request);

        List<List<PNode>> responseLists = ListUtils.recoverPNodeListByResponse(clientManager.getResponseList(pidList));

        for (int i = 0; i < p; i++) {

            Set<PNode> set = new HashSet<PNode>();
            List<PNode> inputList = nodeLists.get(i);

            set.addAll(inputList);
            set.addAll(responseLists.get(i));

            inputList.clear();
            inputList.addAll(set);

        }

        return nodeLists;
    }

    public List<List<PNode>> queryFollowingSibling(List<List<PNode>> inputLists, String test) {

        // Local query
        LxqRequest request = new LxqRequestImpl();
        request.setCode(LxqRequest.FIND_FOLSIB_PNODES);
        request.setMsg(test);
        List<List<PNode>> outputList = sendFindRequests(request, inputLists);

        // Preparing remote query
        List<RemoteNode> toBeQueried = prepareRemoteQuery(inputLists);

        // Regroup nodes by partial tree id
        List<List<PNode>> remoteInputList = regroupNodes(toBeQueried);

        // Remote query
        List<List<PNode>> remoteOutputList = queryChid(remoteInputList, test);

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

    private List<RemoteNode> prepareRemoteQuery(List<List<PNode>> inputLists) {
        // TODO Auto-generated method stub

        List<List<PNode>> parentList = new ArrayList<>();
        List<RemoteNode> toBeQueried = new ArrayList<RemoteNode>();

        // the nodes which need to be query its parent.
        for (int i = 0; i < p; i++) {
            List<PNode> tem = new ArrayList<>();
            List<PNode> input = inputLists.get(i);
            for (PNode pNode : input) {
                Node node = pNode.getNode();
                if (!node.isRightOpenNode() && !node.isPreOpenNode()) {
                    tem.add(pNode);
                }
            }
            parentList.add(tem);
        }

        // query parent nodes
        parentList = queryParentIgnoreCNode(parentList, "*");

        // collecting toBeQueried node list
        for (int i = 0; i < p; i++) {
            for (PNode pNode : parentList.get(i)) {
                Node parent = pNode.getNode();
                Link link = pNode.getLink();
                if (parent.isRightOpenNode() || parent.isPreOpenNode()) {
                    toBeQueried.add(new RemoteNode(parent, i + 1, parent.getEnd(), link));
                }
            }
        }
        return toBeQueried;
    }

    private List<List<PNode>> regroupNodes(List<RemoteNode> toBeQueried) {
        List<List<PNode>> remoteInputList = new ArrayList<>();

        for (int i = 0; i < p; i++) {
            List<PNode> remoteInput = new ArrayList<>();
            Set<PNode> set = new HashSet<>();
            for (int j = 0; j < toBeQueried.size(); j++) {
                RemoteNode remoteNode = toBeQueried.get(j);
                if (remoteNode.st <= i && remoteNode.ed >= i) {
                    Node node = remoteNode.getNode();
                    set.add(new PNode(node, remoteNode.getLink()));
                }
            }
            remoteInput.addAll(set);
            remoteInputList.add(remoteInput);
        }
        return remoteInputList;
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

    public List<List<Node>> proccessPredicate(List<List<PNode>> inputLists) {

        List<Link> allLinks = new ArrayList<>();
        for (List<PNode> list : inputLists) {
            for (PNode pNode : list) {
                allLinks.add(pNode.getLink());
            }
        }

        Map<Integer, List<Node>> uidLists = new HashMap<>();
        for (int i = 0; i < p; i++) {
            int pid = pidList.get(i);
            uidLists.put(pid, new ArrayList<Node>());
        }

        for (Link link : allLinks) {
            List<Node> uids = uidLists.get(link.getPid());
            if (uids != null) {
                uids.add(new Node(link.getUid()));
            }
        }

        LxqRequest request = new LxqRequestImpl();
        request.setCode(LxqRequest.FIND_NODES_BY_UID);
        for (int i = 0; i < p; i++) {
            int pid = pidList.get(i);
            List<Node> uids = uidLists.get(pid);
            request.setInputList(ListUtils.convertNodeList(uids));
            clientManager.sendRequest(pid, request);
        }
        List<LxqResponse> responses = clientManager.getResponseList(pidList);
        List<List<Node>> resultLists = ListUtils.recoverNodeListByResponse(responses);

        return shareNodes(resultLists);
    }

    public List<List<Node>> shareNodes(List<List<Node>> nodeLists) {

        List<Node> toBeShare = new ArrayList<Node>();

        Set<Long> teBeShareUid = new HashSet<>();
        for (int i = 0; i < p; i++) {
            for (Node node : nodeLists.get(i)) {
                if (!NodeType.CLOSED_NODE.equals(node.getType()) && !teBeShareUid.contains(node.getUid())) {
                    teBeShareUid.add(node.getUid());
                    toBeShare.add(node);
                }
            }
        }

        LxqRequest request = new LxqRequestImpl();
        request.setCode(LxqRequest.SHARE_NODES);
        request.setInputList(ListUtils.convertNodeList(toBeShare));
        clientManager.sendRequests(request);

        List<List<Node>> responseLists = ListUtils.recoverNodeListByResponse(clientManager.getResponseList(pidList));

        for (int i = 0; i < p; i++) {

            Set<Node> set = new HashSet<Node>();
            List<Node> inputList = nodeLists.get(i);

            set.addAll(inputList);
            set.addAll(responseLists.get(i));

            inputList.clear();
            inputList.addAll(set);

        }

        return nodeLists;
    }

}
