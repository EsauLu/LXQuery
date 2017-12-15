package esau.lxq.entry;

import java.util.Objects;

public class PNode {

    private Node node;

    private Link link;

    public PNode() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return " (" + node + "," + link + ") ";
    }

    public PNode(Node node, Link link) {
        super();
        this.node = node;
        this.link = link;
    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        if (obj != null && obj instanceof PNode) {
            PNode pNode = (PNode) obj;
            if (node.equals(pNode.getNode()) && link.equals(pNode.getLink())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return Objects.hash(node, link);
    }

}
