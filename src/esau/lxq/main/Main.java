package esau.lxq.main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import esau.lxq.controller.Worker;

public class Main {

    public static Scanner scanner;

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

    public static void runMaster() {

        System.out.print("please input Workers number: ");
        int num = scanner.nextInt();
        System.out.println();

        ServerSocket server = null;
        try {
            server = new ServerSocket(29000, num, InetAddress.getByName("172.21.52.20"));
            System.out.println("Server address: "+server.getInetAddress()+":"+server.getLocalPort());
            System.out.println();
            System.out.println("Wait for Workers...");
            System.out.println();
            for (int i = 0; i < num; i++) {
                
                Socket socket = server.accept();
                System.out.println("Worker"+i+" : "+socket.getInetAddress());
                
                
            }
            
            System.out.println();
            System.out.println("Exit!");
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

    public static void runWorker() {
        
        Socket socket=null;
        
        try {
            
            System.out.println("Connecting 172.21.52.20:29000 ...");
            socket=new Socket("172.21.52.20", 29000);
            System.out.println("Success!");
            
            System.out.println("Do something...");
            Thread.sleep(10000);
            
            System.out.println();
            System.out.println("Exit!");
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if(socket!=null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
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
