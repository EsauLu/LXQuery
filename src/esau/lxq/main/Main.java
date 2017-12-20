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

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        
        scn=new Scanner(System.in);        

        System.out.println("1. master");
        System.out.println("2. worker");
        
        int k=scn.nextInt();
        
        if(k==1){
            k=1;
            
            while(k==1){
                master();
                System.out.println("Exit? 1/0");
                k=scn.nextInt();
            }
        }else{
            worker();
        }
        
        scn.close();
        
    }
    
    public static void worker(){
        
        System.out.println("worker");
        
        int port=29000;
        String serverIP="";
        String[] ips=getAllLocalHostIP();
        for(String ip: ips){
            if(ip.startsWith("172.21.52.")){
                serverIP=ip;
                break;
            }
        }
        
        LxqServer server=new LxqServerImpl(serverIP, port);
        
        server.start();
        
    }
    
    public static void master(){
        
        System.out.println("master");
        
        Master master=new Master(5);
        master.start();
                
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
