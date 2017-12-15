package esau.lxq.utils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import esau.lxq.entry.Node;
import esau.lxq.entry.NodeType;
import esau.lxq.entry.PNode;

public class Utils {

    public static void bfs(Node root) {

        if (root == null) {
            return;
        }

        List<Node> list = root.getChildList();

        for (Node ch : list) {

            Deque<Node> que = new ArrayDeque<>();
            que.addLast(ch);

            while (!que.isEmpty()) {

                Node node = que.removeFirst();

                if (NodeType.CLOSED_NODE.equals(node.getType())) {
                    System.out.print(node);
                } else {
                    String s = node.toString();
                    s = s.substring(0, s.length() - 1);
                    System.out.print(s + " ");
                }

                for (Node nd : node.getChildList()) {
                    que.addLast(nd);
                }

            }

        }

        System.out.println();

    }

    public static void bfsWithRoot(Node root) {

        if (root == null) {
            return;
        }

        List<Node> list = new ArrayList<>();
        list.add(root);

        for (Node ch : list) {

            Deque<Node> que = new ArrayDeque<>();
            que.addLast(ch);

            while (!que.isEmpty()) {

                Node node = que.removeFirst();

                if (NodeType.CLOSED_NODE.equals(node.getType())) {
                    System.out.print(node);
                } else {
                    String s = node.toString();
                    s = s.substring(0, s.length() - 1);
                    System.out.print(s + " ");
                }

                for (Node nd : node.getChildList()) {
                    que.addLast(nd);
                }

            }

        }

        System.out.println();

    }

    public static void bfsWithRanges(Node root) {

        if (root == null) {
            return;
        }

        List<Node> list = root.getChildList();

        for (Node ch : list) {

            Deque<Node> que = new ArrayDeque<>();
            que.addLast(ch);

            while (!que.isEmpty()) {

                Node node = que.removeFirst();

                if (NodeType.CLOSED_NODE.equals(node.getType())) {
                    System.out.print(node);
                } else {
                    String s = node.toString();
                    s = s.substring(0, s.length() - 1);
                    System.out.print(s + "(" + node.getStart() + ", " + node.getEnd() + ") ");
                }

                for (Node nd : node.getChildList()) {
                    que.addLast(nd);
                }

            }

        }

        System.out.println();

    }

    public static void bfsWithDepth(Node root) {

        if (root == null) {
            return;
        }

        List<Node> list = root.getChildList();

        for (Node ch : list) {

            Deque<Node> que = new ArrayDeque<>();
            que.addLast(ch);

            while (!que.isEmpty()) {

                Node node = que.removeFirst();

                String s = node.toString();
                s = s.substring(0, s.length() - 1);
                System.out.print(s + "(" + node.getDepth() + ") ");

                for (Node nd : node.getChildList()) {
                    que.addLast(nd);
                }

            }

        }

        System.out.println();

    }

    public static void dfsWithDepth(Node root) {

        List<Node> list = root.getChildList();

        for (Node ch : list) {

            Deque<Node> stack = new ArrayDeque<Node>();
            stack.push(ch);

            while (!stack.isEmpty()) {
                Node node = stack.pop();

                String s = node.toString();
                s = s.substring(0, s.length() - 1);
                System.out.print(s + "(" + node.getDepth() + ") ");

                List<Node> childList = node.getChildList();
                for (int i = childList.size() - 1; i >= 0; i--) {
                    stack.push(childList.get(i));
                }

            }
        }

        System.out.println();

    }

    public static void dfs(Node root) {

        List<Node> list = root.getChildList();

        for (Node ch : list) {

            Deque<Node> stack = new ArrayDeque<Node>();

            stack.push(ch);

            while (!stack.isEmpty()) {
                Node node = stack.pop();

                String s = node.toString();
                s = s.substring(0, s.length() - 1);
                System.out.print(s + " ");

                List<Node> childList = node.getChildList();
                for (int i = childList.size() - 1; i >= 0; i--) {
                    stack.push(childList.get(i));
                }

            }
        }

        System.out.println();

    }
    
    public static void print(List<List<Node>> results) {

        if(results==null) {
            System.out.println("null list");
            return;
        }
        
        System.out.println();
        int p = results.size();
        for (int j = 0; j < p; j++) {
            List<Node> result = results.get(j);
            System.out.print("  pt" + j + " : ");
            if (result==null) {
                System.out.println("null pt");
                continue;
            }
            for (Node node : result) {
                System.out.print(node);
            }

            System.out.println();
        }
        System.out.println();
        System.out.println("---------------------------------------------------------------------");

    }

    
    public static void printPNodeList(List<List<PNode>> results) {

        if(results==null) {
            System.out.println("null list");
            return;
        }
        
        System.out.println();
        int p = results.size();
        for (int j = 0; j < p; j++) {
            List<PNode> result = results.get(j);
            System.out.print("  pt" + j + " : ");
            if (result==null) {
                System.out.println("null pt");
                continue;
            }
            for (PNode node : result) {
                System.out.print(node);
            }

            System.out.println();
        }
        System.out.println();
        System.out.println("----------------------------------------------------------");

    }


}
