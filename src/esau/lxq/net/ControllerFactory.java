package esau.lxq.net;

import java.util.ResourceBundle;

public class ControllerFactory {

    private static ResourceBundle bundle = ResourceBundle.getBundle("server");

    public static Controller getController() {
        String className = bundle.getString("ctrl");
        
        try {
            
            if(className==null||className.isEmpty()){
                className="esau.lxq.net.impl.DefaultController";
            }

            Object obj = Class.forName(className).newInstance();
            
            return (Controller) obj;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            className="esau.lxq.net.impl.DefaultController";
        }

        try {

            Object obj = Class.forName(className).newInstance();
            
            return (Controller) obj;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
    }

}
