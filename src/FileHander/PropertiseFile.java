package FileHander;

import api.Global;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiseFile {
    public static Map<String, String> initProperties() {
        Map<String, String> map = new HashMap<>();
        Properties prop = new Properties();
        try {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(new File("pm.properties")));
            prop.load(inputStream);
            map.put("path", prop.getProperty("path"));
            map.put("timegap", prop.getProperty("timegap"));
            map.put("onTop", prop.getProperty("onTop"));
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updatePropertise(Map<String, String> map) {
        Properties properties = new Properties();
        properties.setProperty("timegap", String.valueOf(map.get("timegap")));
        properties.setProperty("path", map.get("path"));
        properties.setProperty("onTop", map.get("onTop"));


        String path = "pm.properties";
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path);
            properties.store(fileOutputStream,null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fileOutputStream) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                System.out.println("pm.properties文件流关闭出现异常");
            }
        }
    }
}
