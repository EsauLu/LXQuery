package esau.lxq.main;

import java.util.Scanner;

public class Main {
    
    private static Scanner scn;

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        
        scn=new Scanner(System.in);        

        System.out.println("1. master");
        System.out.println("2. worker");
        
        int k=scn.nextInt();
        
        if(k==1){
            master();
        }else{
            worker();
        }
        
        scn.close();
        
    }
    
    public static void worker(){
        System.out.println("worer");
    }
    
    public static void master(){
        System.out.println("master");
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
