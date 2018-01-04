package esau.lxq.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import esau.lxq.entry.Node;
import esau.lxq.entry.Step;
import esau.lxq.parser.XPathParser;
import esau.lxq.utils.Utils;

public class Master {

    private int workerNum = 0;

    private List<Integer> pidList;

    private ClientManager clientManager;

    private PartialTreeBuilder builder;

    private QueryExecutor queryExecutor;
    private String[] xpaths = null;
    private String xmlDocPath = null;

    public Master() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Master(String[] workerIps, int port) {
        this.clientManager = new ClientManager(workerIps, port);
    }

    public void init(int num, String[] xpaths, String xmlDocPath) {
        this.workerNum = num;
        this.xpaths = xpaths;
        this.xmlDocPath = xmlDocPath;
    }

    public void start() throws Exception{

        if (xpaths == null || xmlDocPath == null) {
            return;
        }

        pidList = new ArrayList<>();
        for (int i = 0; i < workerNum; i++) {
            pidList.add(i);
        }

        clientManager.initClients(pidList);

        long t1 = 0;
        long t2 = 0;

        System.out.println("Build Partial trees.");

        builder = new PartialTreeBuilder(workerNum, pidList, clientManager);

        t1 = System.currentTimeMillis();
        builder.build(xmlDocPath);
        t2 = System.currentTimeMillis();

        System.out.println("Time out : " + (t2 - t1) + " ms");

        queryExecutor = new QueryExecutor(pidList, clientManager);

        System.out.println("=============================================");
        System.out.println();
        System.out.println("Query results :");
        System.out.println();
        
        File file=new File("result"+workerNum+".txt");
        if(file.exists()) {
            file.delete();
        }
        file.createNewFile();
        BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));

        for (int i = 0; i < xpaths.length; i++) {

            String xpath = xpaths[i];

            Step steps = XPathParser.parseXpath(xpath);

            System.out.println("Q" + (i + 4) + " : " + xpath);
            bw.write(xpath);
            bw.write("\n");
            bw.write("\n");

            System.out.print("Time out : ");
            List<Long> counts=new ArrayList<>();
            for (int j = 0; j < 20; j++) {
                t1 = System.currentTimeMillis();
                List<List<Node>> resultLists = queryExecutor.query(steps);
                t2 = System.currentTimeMillis();

                long count = 0;
                for (List<Node> list : resultLists) {

                    for (Node node : list) {
                        if (node.isClosedNode() || node.isLeftOpenNode()) {
                            count++;
                        }
                    }

                }
                counts.add(count);

                System.out.print((t2 - t1) + "ms ");
                bw.write(String.valueOf(t2 - t1));
                bw.write(" ");

//                 Utils.print(resultLists);
                resultLists = null;

            }
            bw.write("\n");
            System.out.println();
            System.out.print("Number of nodes : ");
            
            for(Long count: counts) {
                System.out.print(count+" ");
            }
            System.out.println();
            System.out.println();

            bw.write("\n");
            bw.write("Number of nodes : ");
            bw.write(""+counts.get(0));
            bw.write("\n");
            bw.write("\n");
            bw.write("\n");

        }

        System.out.println("=============================================");
        bw.close();

    }

}
