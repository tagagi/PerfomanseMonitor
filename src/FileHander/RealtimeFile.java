package FileHander;

import api.Global;
import api.Info;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RealtimeFile {
    public static List<Info> readFile(List<File> fileList) {
        List<Info> jsonObjects = new ArrayList<>();
        for (File file : fileList) {
            String fileName = file.getName();
            if (!fileName.endsWith(".rti")) continue;
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String s = null;
                while ((s = br.readLine()) != null) {
                    //使用readLine方法，一次读一行
                    JSONObject object = JSON.parseObject(s);
                    jsonObjects.add(new Info(object.getString("time"),
                            object.getFloat("cpuUsage"),
                            object.getFloat("memUsage"),
                            object.getFloat("netSpeed"),
                            object.getFloat("diskUsage")));
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonObjects;
    }

    //存盘
    public static void saveFile(String curDetailTime, float cpuUsage, float memoryUsage, float internetSpeed, float diskUsage, String rtiFile) {
        JSONObject rtiJSON = new JSONObject();
        rtiJSON.put("time", curDetailTime);
        rtiJSON.put("cpuUsage", cpuUsage);
        rtiJSON.put("memUsage", memoryUsage);
        rtiJSON.put("netSpeed", internetSpeed);
        rtiJSON.put("diskUsage", diskUsage);
        File file = new File(rtiFile);
        try {
            FileWriter fw = new FileWriter(file, true);
            fw.write(rtiJSON.toJSONString() + '\n');
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
