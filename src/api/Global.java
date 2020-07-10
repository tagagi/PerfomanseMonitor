package api;

import java.io.File;
import java.util.List;

public class Global {
    public static int timeGap = 1000;  //时间间隔
    public static String path = "E:\\javacode\\PerfomanseMonitor\\data"; //默认保留路径,
    public static boolean canUseFile = false; //是否可以使用文件
    public static StaticInfo staticInfo ;      //硬件基本信息
    public static RealTimeInfo realTimeInfo = new RealTimeInfo(); //实时信息


    public static boolean saveFile = false;         //是否保存实时性能信息
    public static String rtiFile;                   //性能文件保存路径与名称
    public static String staticFile;                //硬件信息保存路径与名称

    public static List<File> fileList;             //性能文件列表

}
