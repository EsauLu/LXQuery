package esau.lxq.service;

import java.util.ArrayList;
import java.util.List;

import esau.lxq.entry.Node;
import esau.lxq.entry.Step;
import esau.lxq.parser.XPathParser;
import esau.lxq.utils.Utils;

public class Master {

    private int workerNum = 0;

    private List<Integer> pidList;

    private ClientManager clientManager = new ClientManager();

    private PartialTreeBuilder builder;

    private PQueryExecutor queryExecutor;

    private String[] xpaths = {
            // "/descendant::A[descendant::B/descendant::C/parent::B]"
            // +
            // "/following-sibling::B[descendant::B/descendant::C[descendant::G/descendant::H/parent::I]/parent::B]",

            // "/child::A/descendant::B/descendant::C",
            // "/descendant::B[/descendant::E/parent::C]",
            // "/descendant::B[following-sibling::B/child::C]",
            // "/descendant::C[following-sibling::D/parent::B/child::B]",

            // "/child::A/descendant::B/descendant::C",
//             "/descendant::D[parent::B[descendant::E]]" ,
//            "/child::*", 
//            "/descendant::*/child::C",

            // // Q1:
//             "/child::A/descendant::B/descendant::C/parent::B",
            //
            // // Q2:
//             "/descendant::B/following-sibling::B",
            //
            // // Q3:
//             "/descendant::B[following-sibling::B/child::C]/child::C",

//            //Q4
//            "/child::site/descendant::keyword/parent::text",
//            //Q5
            "/child::site/child::people/child::person[child::profile/child::gender]/child::name",
//            //Q6
//            "/child::site/child::open_auctions/child::open_auction/child::bidder[following-sibling::bidder]",
//            //Q7
//            "/child::site/child::closed_auctions/child::closed_auction/child::annotation/child::description/child::text/child::keyword",

    };

    public Master() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Master(int num) {
        this.workerNum = num;
    }

    public void start() {

        pidList = new ArrayList<>();
        for (int i = 0; i < workerNum; i++) {
            pidList.add(i);
        }

        clientManager.initClients(pidList);

        // String xmlDocPath = "res/test0.xml";
//         String xmlDocPath = "res/test2.xml";
        String xmlDocPath = "C:/standard";
        
        long t1=0;
        long t2=0;

        System.out.println("Building Partial trees.");
        System.out.println("Please wait...");
        
        builder = new PartialTreeBuilder(workerNum, pidList, clientManager);
        
        t1=System.currentTimeMillis();
        builder.build(xmlDocPath);
        t2=System.currentTimeMillis();
        
        System.out.println("Complete!");
        System.out.println("Time out : "+(t2-t1)+" ms");

        queryExecutor = new PQueryExecutor(pidList, clientManager);

        for (String xpath : xpaths) {

            System.out.println("---------------------------------------------");
            System.out.println();
            System.out.println("XPath : " + xpath);
            System.out.println();
            System.out.println("------------------------------");

            Step steps = XPathParser.parseXpath(xpath);

            t1=System.currentTimeMillis();
            List<List<Node>> resultLists = queryExecutor.query(steps);
            t2=System.currentTimeMillis();

            System.out.println("==============================");
            System.out.println();
            System.out.println("Final results :");
            System.out.println();

//            Utils.print(resultLists);
            for (int i = 0; i < pidList.size(); i++) {

                System.out.println("  pt" + i + " : " + resultLists.get(i).size());

            }
            System.out.println();
            System.out.println("Time out : "+(t2-t1)+" ms");
            System.out.println();

            // Utils.print(resultLists);

            System.out.println("=============================================");

        }

    }

}
