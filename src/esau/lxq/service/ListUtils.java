package esau.lxq.service;

import java.util.ArrayList;
import java.util.List;

import esau.lxq.entry.MsgItem;
import esau.lxq.entry.Node;
import esau.lxq.entry.PNode;
import esau.lxq.net.LxqResponse;

public class ListUtils {

    public static List<MsgItem> convertNodeList(List<Node> list) {
        List<MsgItem> res = new ArrayList<>();
        res.addAll(list);
        return res;
    }

    public static List<Node> recoverNodeList(List<MsgItem> originList) {
        List<Node> list = new ArrayList<>();
        if (originList != null) {
            for (MsgItem item : originList) {
                list.add((Node) item);
            }
        }
        return list;
    }

    public static List<List<Node>> recoverNodeListByResponse(List<LxqResponse> responseLists) {
        List<List<Node>> outputLists = new ArrayList<>();
        for (int i = 0; i < responseLists.size(); i++) {
            LxqResponse response = responseLists.get(i);
            List<Node> result = recoverNodeList(response.getResultList());
            outputLists.add(result);
        }
        return outputLists;
    }

    public static List<MsgItem> convertPNodeList(List<PNode> list) {
        List<MsgItem> res = new ArrayList<>();
        res.addAll(list);
        return res;
    }

    public static List<PNode> recoverPNodeList(List<MsgItem> originList) {
        List<PNode> list = new ArrayList<>();
        if (originList != null) {
            for (MsgItem item : originList) {
                PNode node = (PNode)item;
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
            LxqResponse response = responseLists.get(i);
            List<PNode> result = ListUtils.recoverPNodeList(response.getResultList());
            outputLists.add(result);
        }
        return outputLists;
    }

}
