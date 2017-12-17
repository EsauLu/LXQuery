package esau.lxq.main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;


public class Main {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        try {
            File file = new File("res/test2.xml");
//            File file = new File("res/test1.xml");

            StringBuffer xmlBuff = new StringBuffer();

            int len = 0;
            
            byte[] buff = new byte[1024];

            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

            while ((len = bis.read(buff)) != -1) {
                xmlBuff.append(new String(buff, 0, len));
            }

            System.out.println(xmlBuff.toString());
            System.out.println("--------------------------------------------------");

            int[] pos = getPos(xmlBuff, 5);
            for (int i = 0; i < pos.length - 1; i++) {


                System.out.println("--------------------------------------------------");
                
            }

            bis.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
