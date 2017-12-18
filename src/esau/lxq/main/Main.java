package esau.lxq.main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;

import esau.lxq.net.ControllerFactory;
import esau.lxq.net.LxqClient;
import esau.lxq.net.LxqRequest;
import esau.lxq.net.LxqResponse;
import esau.lxq.net.LxqServer;
import esau.lxq.net.impl.LxqClientImpl;
import esau.lxq.net.impl.LxqRequestImpl;
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
                System.out.println("continue? 1/0");
                k=scn.nextInt();
            }
        }else{
            worker();
        }
        
        scn.close();
        
    }
    
    public static void worker(){
        System.out.println("worer");
                
        System.out.println(ControllerFactory.getController());
        
        int port=29000;
        String serverIP="";
        String[] ips=getAllLocalHostIP();
        for(String ip: ips){
            System.out.println("local ip : " + ip);
            if(ip.startsWith("192.168.118.")){
                serverIP=ip;
//                break;
            }
        }
        
        LxqServer server=new LxqServerImpl(serverIP, port);
        
        server.start();

        
    }
    
    public static void master(){
        
        System.out.println("master");
        
        Master master=new Master();
        master.start();
        
//        int port=29000;
////        String serverIP="192.168.118.1";     
//        String serverIP="192.168.118.128";     
//        
//        LxqClient client=new LxqClientImpl(serverIP, port);
//
//        System.out.println("==========================================");
//        
//        LxqRequest request=new LxqRequestImpl();
//        
//        request.setCode(LxqRequest.FIND_CHILD);
//        request.setNameTest("name test");
//        
//        List<String> list=new ArrayList<>();
//        for(int i=0;i<10;i++){
//            list.add("item"+i);
//        }
//        
//        request.setInputList(list);
//        
//        System.out.println(request);
//        
//        client.execute(request);
//        
//        LxqResponse response=client.getResponse();
//
//        System.out.println("======================");
//        
//        System.out.println(response);
//        
//        System.out.println("==========================================");
//        
//        try {
//            
//            File file=new File("res/test0.xml");
//            BufferedInputStream bis=new BufferedInputStream(new FileInputStream(file));
//            
//            int len=0;
//            byte[] buff=new byte[1024];
//            StringBuffer sb=new StringBuffer();
//            while((len=bis.read(buff))!=-1){
//                sb.append(new String(buff, 0, len));
//            }
//            
//            bis.close();
//
//            System.out.println("==========================================");
//            
//            request.setCode(LxqRequest.CHUNK);
//            request.setChunk(sb.toString());
//
//            System.out.println(request);
//            
//            client.execute(request);
//            
//            response=client.getResponse();
//            
//            System.out.println("======================");
//
//            System.out.println(response);
//            
//            System.out.println("==========================================");
//            
//            
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        
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
