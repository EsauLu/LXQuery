package esau.lxq.main;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import esau.lxq.controller.master.Master;

public class Main {

    public static Scanner scanner;

//    public static String serverIP = "172.21.52.35";
//    public static String[] workerIPs = { "172.21.52.35", "172.21.52.35", "172.21.52.35", "172.21.52.35", "172.21.52.35" };

    public static String masterIP = "192.168.118.1";
    public static String[] workerIPs = { 
            "192.168.118.128",
//            "192.168.118.129",
//            "192.168.118.130",
//            "192.168.118.131",
//            "192.168.118.132",
//            "192.168.118.133",
//            "192.168.118.134",
//            "192.168.118.135",
            };

    public static int port = 29000;

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        scanner = new Scanner(System.in);

        solve();

        scanner.close();

    }

    public static void solve() {

        while (true) {

            System.out.println();
            System.out.println("1. Run as Master");
            System.out.println("2. Run as Worker");
            System.out.print("Please select option:  ");

            int k = scanner.nextInt();

            System.out.println();
            if (k == 1) {
                System.out.println("Run as Master");
                runMaster();
                System.out.println();
            } else if (k == 2) {
                System.out.println("Run as Worker");
                runWorker();
                System.out.println();
            } else {
                System.out.println("No such option!");
                System.out.println();
                continue;
            }

            break;

        }

    }

    public static void runWorker() {

        ServerSocket server = null;
        try {
            
            String[] ips=getAllLocalHostIP();
            for(String ip: ips){
                System.out.println("local ip : " + ip);
                if(ip.startsWith("192.168.118.")){
                    masterIP=ip;
//                    break;
                }
            }

            if(masterIP==null||masterIP.isEmpty()){
                System.out.println("Fail to get local address!");
                System.exit(0);
            }

            server = new ServerSocket(port, 1, InetAddress.getByName(masterIP));

            System.out.println("Address: " + server.getInetAddress() + ":" + server.getLocalPort());

            int t=5;
            while (t-->0) {

                System.out.println();
                System.out.println("Wait for Master...");
                System.out.println();

                Socket socket = server.accept();

                System.out.println("Connected a Master : " + socket.getInetAddress());
                
                int len=0;
                byte[] buff=new byte[4];
                
                InputStream in=socket.getInputStream();
                
                StringBuffer sb=new StringBuffer();
                while((len=in.read(buff))!=-1){
                    System.out.println("read : "+len);
                    sb.append(new String(buff, 0, len));
                }
                
                
                System.out.println(len+"Content : "+sb.toString());
                
                in.close();
                
                if(sb.toString().startsWith("exit!")){
                    System.out.println("break");
                    break;
                }else{
                    System.out.println("Continue");
                }
                
//                break;
            }

            // System.out.println();
            // System.out.println("Exit!");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    public static void runMaster() {

        // System.out.print("please input Workers number: ");
        // int num = scanner.nextInt();
        // System.out.println();

        Map<Integer, Socket> socketMap = new HashMap<>();

        try {

            for (int i = 0; i < workerIPs.length; i++) {

                System.out.println("Connecting Worker " + masterIP + ":" + port + " ...");
                Socket socket = new Socket(workerIPs[i], port);
                System.out.println("Success!");

                socketMap.put(i, socket);

                System.out.println();

            }

            Master master=Master.getInstance();
            System.out.println("Initialize...");
            master.init(socketMap);
            System.out.println("Runing...");
            
            master.run();
            
            Thread.sleep(10000);
            
            System.out.println();
            System.out.println("Exit!");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                for (Socket socket : socketMap.values()) {
                    if (socket != null && !socket.isClosed()) {
                        socket.close();
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }    
    


    private static String[] getAllLocalHostIP() {  
        List<String> res = new ArrayList<String>();  
        Enumeration<NetworkInterface> netInterfaces;  
        try {  
            netInterfaces = NetworkInterface.getNetworkInterfaces();  
            InetAddress ip = null;  
            while (netInterfaces.hasMoreElements()) {  
                NetworkInterface ni = (NetworkInterface) netInterfaces  
                        .nextElement();  
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
