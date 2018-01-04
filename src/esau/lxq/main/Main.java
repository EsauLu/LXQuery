package esau.lxq.main;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;

import esau.lxq.net.LxqServer;
import esau.lxq.net.impl.LxqServerImpl;
import esau.lxq.service.Master;

public class Main {

    private static Scanner scn;

    private static String[] workerIps = { 
            "172.21.52.50",
            // "172.21.52.35",
            "172.21.52.51", 
            "172.21.52.52", 
            "172.21.52.53", 
            "172.21.52.54", 
            "172.21.52.55", 
            "172.21.52.56",
            "172.21.52.57", 
            "172.21.52.58", 
            "172.21.52.59", 
            "172.21.52.60", 
            "172.21.52.61", 
            "172.21.52.62", 
            "172.21.52.63", 
            "172.21.52.64", 
            "172.21.52.65", 
            };

    private static int port = 29000;

    // private static String xmlDocPath = "/Users/imac/Desktop/standard";
    // private static String xmlDocPath = "/Users/imac/Desktop/xmark40_0.xml";
    // private static String xmlDocPath = "res/test0.xml";
     private static String xmlDocPath = "res/test2.xml";
//    private static String xmlDocPath = "C:/xml/xmark1.xml";
//     private static String xmlDocPath = "C:/xml/xmark6.xml";

    private static String[] xpaths = {

//             // Q1:
//             "/child::A/descendant::B/descendant::C/parent::B",
//            
//             // Q2:
//             "/descendant::B/following-sibling::B",
//            
//             // Q3:
//             "/descendant::B[following-sibling::B/child::C]/child::C",

            // Q4
            "/child::site/descendant::keyword/parent::text",
            // Q5
            "/child::site/child::people/child::person[child::profile/child::gender]/child::name",
            // Q6
            "/child::site/child::open_auctions/child::open_auction/child::bidder[following-sibling::bidder]",
//             Q7
//            "/child::site/child::closed_auctions/child::closed_auction/child::annotation/child::description/child::text/child::keyword",

    };

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        scn = new Scanner(System.in);

        System.out.println("1. master");
        System.out.println("2. worker");

        int k = scn.nextInt();

        if (k == 1) {
            k = 1;

            while (k == 1) {
                master();
                // k = scn.nextInt();
                break;
            }
        } else {
            worker();
        }

        scn.close();

    }

    public static void worker() {

        String serverIP = "";
        String[] ips = getAllLocalHostIP();
        for (String ip : ips) {
            if (ip.startsWith("172.21.52.")) {
                serverIP = ip;
                break;
            }
        }

        LxqServer server = new LxqServerImpl(serverIP, port);

        server.start();

    }

    public static void master() {

         int[] nums= {1};
        // int[] nums= {2};
        // int[] nums = { 5 };
//         int[] nums = { 8, 4, 2, 1 };
        // int[] nums= {8, 5, 4, 2, 1};
//       int[] nums = { 1, 2, 3, 4, 5, 6, 7, 8};
//       int[] nums = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

        Master master = new Master(workerIps, port);

        for (int i = 0; i < nums.length; i++) {

            System.out.println();
            System.out.println("Worker number : " + nums[i]);
            System.out.println("=====================");
            System.out.println();

            master.init(nums[i], xpaths, xmlDocPath);
            try {
                master.start();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            System.out.println();

            System.out.println("###############################################################");

        }

    }

    private static String[] getAllLocalHostIP() {
        List<String> res = new ArrayList<String>();
        Enumeration<NetworkInterface> netInterfaces;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                Enumeration<InetAddress> nii = ni.getInetAddresses();
                while (nii.hasMoreElements()) {
                    ip = (InetAddress) nii.nextElement();
                    if (ip.getHostAddress().indexOf(":") == -1) {
                        res.add(ip.getHostAddress().trim());
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return (String[]) res.toArray(new String[0]);
    }

}

// "/descendant::A[descendant::B/descendant::C/parent::B]"
// +
// "/following-sibling::B[descendant::B/descendant::C[descendant::G/descendant::H/parent::I]/parent::B]",

// "/child::A/descendant::B/descendant::C",
// "/descendant::B[/descendant::E/parent::C]",
// "/descendant::B[following-sibling::B/child::C]",
// "/descendant::C[following-sibling::D/parent::B/child::B]",

// "/child::A/descendant::B/descendant::C",
// "/descendant::D[parent::B[descendant::E]]" ,
// "/child::*",
// "/child::site",
// "/child::site/descendant::*",
// "/child::site/descendant::keyword",
// "/child::site/descendant::keyword/parent::*",
// "/descendant::*",
// "/descendant::*",
// "/descendant::*/child::C",
