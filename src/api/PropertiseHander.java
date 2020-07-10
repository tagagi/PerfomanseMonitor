package api;

import FileHander.PropertiseFile;

import java.util.HashMap;
import java.util.Map;

public class PropertiseHander {
    public static void initProperties() {
        Map<String, String> map = PropertiseFile.initProperties();
        if (map != null){
            Global.path =map.get("path");
            Global.timeGap = Integer.parseInt(map.get("timegap"));
        }
    }

    public static void updatePropertise() {
        Map<String, String> map = new HashMap<>();
        map.put("path",Global.path);
        map.put("timegap",String.valueOf(Global.timeGap));
        PropertiseFile.updatePropertise(map);
    }

}
