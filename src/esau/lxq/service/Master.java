package esau.lxq.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Master {

    private List<Integer> pidList;

    private ClientManager clientManager = new ClientManager();

    private String[] xpaths = {
            // "/descendant::A[descendant::B/descendant::C/parent::B]/following-sibling::B[descendant::B/descendant::C[descendant::G/descendant::H/parent::I]/parent::B]",
            "/child::A/descendant::B/descendant::C",
            // "/child::A/descendant::B/descendant::C/parent::B",
            // "/descendant::B/following-sibling::B",
            // "/descendant::B[following-sibling::B/child::C]/child::C",
            // "/descendant::D[parent::B[descendant::E]]"
    };

    public Master() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void start() {
        build();
    }

    private void build() {

        String xmlDocPath = "res/test0.xml";
        dispatchXMLDocument(xmlDocPath);

    }

    private void dispatchXMLDocument(String xmlDocPath) {

        File file = new File(xmlDocPath);

        if (!file.exists() || !file.isFile()) {
            return;
        }

        BufferedReader reader = null;

        StringBuffer xml = new StringBuffer();

        try {

            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            String line = "";
            while ((line = reader.readLine()) != null) {
                xml.append(line);
                xml.append("\n");
            }

            System.out.println(xml);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private long[] getPos(long len, int chunkNum) {
        long[] pos = new long[chunkNum + 1];
        long t = len / chunkNum + 1;
        for (int i = 0; i < chunkNum; i++) {
            pos[i] = i * t;
        }
        pos[chunkNum] = len;
        return pos;
    }

}
