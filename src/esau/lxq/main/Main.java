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

import esau.lxq.controller.worker.Worker;

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

}
