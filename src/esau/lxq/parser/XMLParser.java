package esau.lxq.parser;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import esau.lxq.entry.Node;
import esau.lxq.entry.NodeType;
import esau.lxq.entry.Tag;
import esau.lxq.entry.TagType;
import esau.lxq.factory.NodeFactory;

public class XMLParser {

    public static List<Node> buildSubTrees(String chunk) {
        List<Tag> tagList = getTagsByXML(chunk);
        return buildSubTreesByTags(tagList);
    }

    public static List<Node> buildSubTreesByTags(List<Tag> tagList) {

        Deque<Node> stack = new ArrayDeque<Node>();

        stack.push(NodeFactory.createNode("\0", NodeType.CLOSED_NODE, 0));

        for (int i = 0; i < tagList.size(); i++) {

            Tag tag = tagList.get(i);

            if (TagType.START.equals(tag.getType())) {

                Node node = NodeFactory.createNode(tag.getName(), NodeType.CLOSED_NODE, tag.getTid());
                stack.push(node);

            } else {

                Node node = stack.peek();

                if (node.getTagName().equals(tag.getName())) {
                    stack.pop();
                    stack.peek().getChildList().add(node);
                } else {
                    Node temNode = NodeFactory.createNode(tag.getName(), NodeType.LEFT_OPEN_NODE, tag.getTid());
                    temNode.getChildList().addAll(node.getChildList());
                    node.setChildList(new ArrayList<Node>());
                    node.getChildList().add(temNode);
                }

            }
        }

        while (stack.size() > 1) {
            Node node = stack.pop();
            node.setType(NodeType.RIGHT_OPEN_NODE);
            stack.peek().getChildList().add(node);
        }

        return stack.pop().getChildList();
    }

    public static List<Tag> getTagsByXML(String chunk) {

        List<Tag> tagList = new ArrayList<Tag>();

        StringBuffer chunkBuff = new StringBuffer(chunk);

        while (true) {

            int i = chunkBuff.indexOf("<");
            int j = chunkBuff.indexOf(">");

            if (i == -1 || j == -1) {
                break;
            }

            tagList.add(getTag(chunkBuff.substring(i + 1, j)));
            chunkBuff.delete(0, j + 1);

        }

        return tagList;

    }

    private static Tag getTag(String tagStr) {

        if (tagStr == null || tagStr.trim().equals("")) {
            return null;
        }

        Tag tag = new Tag();

        tagStr = tagStr.trim();
        if (tagStr.charAt(0) == '/') {
            tagStr = tagStr.substring(1);
            tag.setType(TagType.END);
        } else {
            tag.setType(TagType.START);
        }

        int i = tagStr.indexOf(" ");
        if (i != -1) {
            tag.setName(tagStr.substring(0, i));
        } else {
            tag.setName(tagStr);
        }

        return tag;

    }

}
