package esau.lxq.controller.worker;

import java.util.List;

import esau.lxq.entry.Node;
import esau.lxq.entry.PartialTree;
import esau.lxq.parser.XMLParser;
import esau.lxq.utils.Utils;

public class Worker {

    private PartialTree partialTree;
    
    private List<Node> resultList;
    
    public void buildPartialtree(String chunk) {
        
        List<Node> subTrees=XMLParser.buildSubTrees(chunk);
        
        for(Node root: subTrees) {
            Utils.bfsWithRoot(root);
        }
        
    }

    public PartialTree getPartialTree() {
        return partialTree;
    }

    public void setPartialTree(PartialTree partialTree) {
        this.partialTree = partialTree;
    }

    public List<Node> getResultList() {
        return resultList;
    }

    public void setResultList(List<Node> resultList) {
        this.resultList = resultList;
    }
    
    
    
}
