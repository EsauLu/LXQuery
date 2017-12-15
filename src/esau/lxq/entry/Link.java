package esau.lxq.entry;

import java.util.Objects;

public class Link {

    private int pid;
    private int uid;

    public Link() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Link(int pid, int uid) {
        super();
        this.pid = pid;
        this.uid = uid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return " (" + pid + ", " + uid + ") ";
    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        if (obj != null && obj instanceof Link) {
            Link link = (Link) obj;
            if (pid==link.getPid()&&uid==link.getUid()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return Objects.hash(pid, uid);
    }

}
