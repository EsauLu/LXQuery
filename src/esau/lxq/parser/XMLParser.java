package esau.lxq.parser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    public static List<Node> buildSubTrees(String xmlDocPath) {
        File file = new File(xmlDocPath);
        List<Tag> tagList = getTagsByXML(file);
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
                    stack.peek().addLastChild(node);
                } else {
                    Node temNode = NodeFactory.createNode(tag.getName(), NodeType.LEFT_OPEN_NODE, tag.getTid());
                    temNode.addChilds(node.getAllChilds());
                    node.clearChilds();
                    node.addLastChild(temNode);
                }

            }
        }

        while (stack.size() > 1) {
            Node node = stack.pop();
            node.setType(NodeType.RIGHT_OPEN_NODE);
            stack.peek().addLastChild(node);
        }

        return stack.pop().getAllChilds();
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
            
            Tag tag=getTag(chunkBuff.substring(i + 1, j));
            if(tag!=null) {
                tagList.add(tag);
            }

            chunkBuff.delete(0, j + 1);

        }

        return tagList;

    }

    public static List<Tag> getTagsByXML(File file) {

        List<Tag> tagList = new ArrayList<Tag>();

        int len = 0;
        byte[] buff = new byte[8192];
        BufferedInputStream bis = null;
        StringBuffer chunkBuff = new StringBuffer();

        try {

            bis = new BufferedInputStream(new FileInputStream(file));
            int i = -1, j = -1;
            while (true) {

                i = chunkBuff.indexOf("<");
                j = chunkBuff.indexOf(">");
                if (i == -1 || j == -1) {

                    len = bis.read(buff);
                    if (len == -1) {
                        break;
                    }
                    String string = new String(buff, 0, len);
                    chunkBuff.append(string);
                    continue;

                }
                                
                Tag tag=getTag(chunkBuff.substring(i + 1, j));
                if(tag!=null) {
                    tagList.add(tag);
                }
                
                chunkBuff.delete(0, j + 1);

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (Exception e2) {
                // TODO: handle exception
            }
        }

        return tagList;

    }

    private static Tag getTag(String tagStr) {

        if (tagStr == null || tagStr.trim().equals("")) {
            return null;
        }

        Tag tag = new Tag();

        tagStr = tagStr.trim();
        char ch = tagStr.charAt(0);
        if (ch == '/') {
            tagStr = tagStr.substring(1);
            tag.setType(TagType.END);
        } else if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
            tag.setType(TagType.START);
        } else {
            return null;
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
