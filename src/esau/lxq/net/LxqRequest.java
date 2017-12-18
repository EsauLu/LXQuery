package esau.lxq.net;

import java.util.List;

public interface LxqRequest {

    public static final int CHUNK = 100;
    
    public static final int LEFT_OPEN_NODES=101;
    
    public static final int RIGHT_OPEN_NODES=102;
    
    public static final int FIND_CHILD=109;

    public void setCode(int code);

    public int getCode();

    public void setNameTest(String test);

    public String getNameTest();

    public void setInputList(List<String> inputList);

    public List<String> getInputList();

    public void setChunk(String chunk);

    public String getChunk();

}
