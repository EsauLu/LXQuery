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

            "/child::A/descendant::B/descendant::C",
//            "/child::A/descendant::B/descendant::C/parent::B",
//            "/descendant::B/following-sibling::B",
//             "/descendant::B[following-sibling::B/child::C]/child::C",
//            "/descendant::D[parent::B[descendant::E]]" 
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

        builder = new PartialTreeBuilder(workerNum, pidList, clientManager);
        builder.build();

        queryExecutor = new PQueryExecutor(pidList, clientManager);

        for (String xpath : xpaths) {

            System.out.println("-----------------------------------------------------");
            System.out.println();
            System.out.println("XPath : " + xpath);
            System.out.println();
            System.out.println("------------------------------");

            Step steps = XPathParser.parseXpath(xpath);

            List<List<Node>> resultLists = queryExecutor.query(steps);

            System.out.println("==============================");
            System.out.println();
            System.out.println("Final results :");
            System.out.println();

            Utils.print(resultLists);

            System.out.println("====================================================================================");

        }

    }

}
