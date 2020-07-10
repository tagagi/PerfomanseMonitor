package FileHander;

import api.Global;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class StaticFile {
    public static boolean save(Map<String, String> cpuMap, Map<String, String> osMap, Map<String, String> menMap, Map<String, String> diskMap,
                        String netInfo, String mainBordSN) {
        //cpu
        String cpuString =
                "--------------------CPU--------------------\n" +
                        "型号：" + cpuMap.get("vendor") + " " + cpuMap.get("name") + "\n" +
                        "核心数： " + cpuMap.get("cores") + "核\n" +
                        "序列号：" + cpuMap.get("serial") + "\n";

        //os
        String osString =
                "--------------------OS--------------------\n" +
                        "版本：" + osMap.get("name") + "\n" +
                        "架构：" + osMap.get("arch") + "\n" +
                        "位数：" + osMap.get("bits") + "\n";

        //内存
        String menString =
                "--------------------内存--------------------\n" +
                        "总容量：" + menMap.get("total") + "\n";

        //硬盘
        String diskString =
                "--------------------硬盘--------------------\n" +
                        "总容量：" + diskMap.get("total") + "\n" +
                        "已使用：" + diskMap.get("use") + "\n" +
                        "序列号：" + diskMap.get("serial") + "\n";
        //网卡
        String netString = "--------------------网卡--------------------\n型号：" + netInfo + "\n";

        //主板
        String mbString = "--------------------主板--------------------\n序列号：" + mainBordSN + "\n";

        File file = new File(Global.staticFile);
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(cpuString);
            fw.write(osString);
            fw.write(menString);
            fw.write(diskString);
            fw.write(netString);
            fw.write(mbString);
            fw.close();

        } catch (IOException e) {
            return false;
        }
        return true;
    }

}
