package esau.lxq.main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import esau.lxq.controller.Worker;

public class Main {

    public static Scanner scanner;

    public static String serverIP = "172.21.52.35";
    public static int port = 29000;

    public static String[] workerIPs = { "172.21.52.35", "172.21.52.35", "172.21.52.35", "172.21.52.35", "172.21.52.35" };

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        scanner = new Scanner(System.in);

        solve();

        scanner.close();

        // try {
        // File file = new File("res/test2.xml");
        //// File file = new File("res/test1.xml");
        //
        // StringBuffer xmlBuff = new StringBuffer();
        //
        // int len = 0;
        // byte[] buff = new byte[1024];
        //
        // BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        //
        // while ((len = bis.read(buff)) != -1) {
        // xmlBuff.append(new String(buff, 0, len));
        // }
        //
        // System.out.println(xmlBuff.toString());
        // System.out.println("--------------------------------------------------");
        //
        // int[] pos = getPos(xmlBuff, 5);
        // for (int i = 0; i < pos.length - 1; i++) {
        //
        // Worker worker = new Worker();
        //
        // worker.buildPartialtree(xmlBuff.substring(pos[i], pos[i + 1]));
        //
        // System.out.println("--------------------------------------------------");
        //
        // }
        //
        // bis.close();
        //
        // } catch (Exception e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

    }

    public static void solve() {

        while (true) {

            System.out.println();
            System.out.println("1. Run as Master");
            System.out.println("2. Run as Worker");
            System.out.print("Please select the number:  ");

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

            server = new ServerSocket(29000, 1, InetAddress.getByName(serverIP));

            System.out.println("Server address: " + server.getInetAddress() + ":" + server.getLocalPort());
            
            while (true) {
                
                System.out.println();
                System.out.println("Wait for Master...");
                System.out.println();


                Socket socket = server.accept();

                System.out.println("Connected a Master : " + socket.getInetAddress());

//                System.out.print("continue? y/n : ");
//
//                String c = scanner.next();
//
//                if (!c.equals("y") && !c.equals("yes")) {
//                    break;
//                }

            }

//            System.out.println();
//            System.out.println("Exit!");
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

//        System.out.print("please input Workers number: ");
//        int num = scanner.nextInt();
//        System.out.println();

        Map<Integer, Socket> socketMap=new HashMap<>();

        try {
            
            for(int i=0;i<workerIPs.length;i++) {
                
                System.out.println("Connecting Worker " + serverIP + ":" + port + " ...");
                Socket socket = new Socket(workerIPs[i], port);
                System.out.println("Success!");

                socketMap.put(i, socket);
                Thread.sleep(1000);

                System.out.println();

            }
            System.out.println();
            System.out.println("Exit!");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                for(Socket socket: socketMap.values()) {
                    if(socket!=null&&!socket.isClosed()) {
                        socket.close();
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private static int[] getPos(StringBuffer xmlDoc, int chunkNum) {

        int[] pos = new int[chunkNum + 1];
        int t = xmlDoc.length() / chunkNum + 1;
        for (int i = 0; i < chunkNum; i++) {
            pos[i] = i * t;
        }
        pos[chunkNum] = xmlDoc.length();
        return fixPos(xmlDoc, pos);
    }

    private static int[] fixPos(StringBuffer xmlDoc, int[] pos) {
        long len = xmlDoc.length();
        for (int i = 0; i < pos.length; i++) {
            if (pos[i] >= len) {
                pos[i] = (int) len;
                continue;
            }
            while (xmlDoc.charAt(pos[i]) != '<') {
                pos[i]--;
            }
        }
        return pos;
    }

}
