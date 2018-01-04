package esau.lxq.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import esau.lxq.entry.Link;
import esau.lxq.entry.Node;
import esau.lxq.entry.NodeType;
import esau.lxq.entry.PNode;

public class EntryTransfer {

    
    public void writeNode(DataOutputStream dos, Node node) throws IOException {
        dos.writeLong(node.getUid());
        dos.writeUTF(node.getTagName());
        dos.writeUTF(node.getType().toString());
        dos.writeInt(node.getStart());
        dos.writeInt(node.getEnd());
    }
    
    public void writeLink(DataOutputStream dos, Link link) throws IOException {
        dos.writeInt(link.getPid());
        dos.writeLong(link.getUid());
    }
    
    public void writePNode(DataOutputStream dos, PNode pnode) throws IOException {
        Node node=pnode.getNode();
        writeNode(dos, node);
        Link link=pnode.getLink();
        writeLink(dos, link);
    }
    
    public Node readNode(DataInputStream dis) throws IOException {
        Node node=new Node();
        node.setUid(dis.readLong());
        node.setTagName(dis.readUTF());
        node.setType(NodeType.parseNodeType(dis.readUTF()));
        node.setStart(dis.readInt());
        node.setEnd(dis.readInt());
        return node;
    }
    
    public Link readLink(DataInputStream dis) throws IOException {
        Link link=new Link();
        
        link.setPid(dis.readInt());
        link.setUid(dis.readLong());
        
        return link;
    }
    
    public PNode readPNode(DataInputStream dis) throws IOException {
        PNode pNode=new PNode();
        pNode.setNode(readNode(dis));
        pNode.setLink(readLink(dis));
        return pNode;
    }
}
