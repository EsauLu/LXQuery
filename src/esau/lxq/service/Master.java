package esau.lxq.service;

public class Master {

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

        String xmlDocPath = "res/test0.xml";

    }
    
    private void dispatchXMLDocument(String xmlDocPath){
        
        
        
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






































