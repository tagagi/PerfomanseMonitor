package api;

import FileHander.StaticFile;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.*;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

public class StaticInfo {
    private Map<String, String> cpuInfo;  //cpu信息
    private Map<String, String> memInfo;  //内存信息
    private Map<String, String> diskInfo;  //硬盘信息
    private String mainboardSN;           //主板序列号
    private Map<String, String> osInfo;  //操作系统信息
    private String netInfo;                     //网卡型号

    public StaticInfo() {
        try {
            CPUInfo();
            memInfo();
            mainboardSN();
            osInfo();
            netInfo();
            diskInfo();
        } catch (IOException | SigarException e) {
            e.printStackTrace();
        }


    }


    public Map<String, String> CPUInfo() throws SigarException {
        String vendor;    //生产商
        String name;      //名称(类别)
        String cores; //核心数

        Sigar sigar = new Sigar();
        CpuInfo[] infos = sigar.getCpuInfoList();
        CpuPerc[] cpuList = sigar.getCpuPercList();
        name = infos[0].getModel();
        vendor = infos[0].getVendor();
        cores = String.valueOf(infos[0].getTotalCores());

        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("vendor", vendor);
        map.put("cores", cores);

        //获取序列号
        try {
            Process process = Runtime.getRuntime().exec(
                    new String[]{"wmic", "cpu", "get", "ProcessorId"});
            process.getOutputStream().close();
            Scanner sc = new Scanner(process.getInputStream());
            String property = sc.next();
            String serial = sc.next();
            map.put("serial", serial);
        } catch (IOException e) {
            e.printStackTrace();
        }

        cpuInfo = map;
        return map;
    }

    public void memInfo() throws SigarException {
        Sigar sigar = new Sigar();
        Mem mem = sigar.getMem();
        String tatal = (int) Math.ceil(mem.getTotal() / 1000 / 1000 / 1000) + "GB";
        Map<String, String> map = new HashMap<>();
        map.put("total", tatal);
        memInfo = map;
    }

    public void diskInfo() throws SigarException, IOException {
        Sigar sigar = new Sigar();
        FileSystem[] fslist = sigar.getFileSystemList();
        int total = 0, use = 0, usable = 0;
        String serial;
        Map<String, String> map = new HashMap<>();

        try {
            for (int i = 0; i < fslist.length; i++) {
//                System.out.println("分区的盘符名称" + i);
                FileSystem fs = fslist[i];
                FileSystemUsage usage;
                usage = sigar.getFileSystemUsage(fs.getDirName());
                if (fs.getType() == 2) { // TYPE_LOCAL_DISK : 本地硬盘
                    total += usage.getTotal() / 1024 / 1024;
                    use += usage.getUsed() / 1024 / 1024;
                    usable += usage.getFree() / 1024 / 1024;
                }
            }
            map.put("total", total + "GB");
            map.put("use", use + "GB");
            map.put("free", usable + "GB");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //获取磁盘序列号
        Process process = Runtime.getRuntime().exec(new String[]{"wmic", "path", "win32_physicalmedia", "get", "serialnumber"});
        process.getOutputStream().close();
        Scanner sc = new Scanner(process.getInputStream());
        String property = sc.next();
        serial = sc.next();
        while (sc.hasNext()) {
            serial += "\n              " + sc.next();
        }
        map.put("serial", serial);

        diskInfo = map;
    }

    public void osInfo() {
        String name;        //名称
        String arch;        //架构
        String bits;        //位数

        OperatingSystem OS = OperatingSystem.getInstance();
        Properties props = System.getProperties();
        Map<String, String> map = System.getenv();
        name = props.getProperty("os.name");
        arch = props.getProperty("os.arch");
        bits = OS.getDataModel();

        Map<String, String> ans = new HashMap<>();
        ans.put("name", name);
        ans.put("arch", arch);
        ans.put("bits", bits);

        osInfo = ans;
    }

    /**
     * 获取主板序列号
     */
    public void mainboardSN() {
        StringBuilder result = new StringBuilder();
        try {
            File file = File.createTempFile("realhowto", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new java.io.FileWriter(file);

            String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                    + "Set colItems = objWMIService.ExecQuery _ \n"
                    + "   (\"Select * from Win32_BaseBoard\") \n"
                    + "For Each objItem in colItems \n"
                    + "    Wscript.Echo objItem.SerialNumber \n"
                    + "    exit for  ' do the first cpu only! \n" + "Next \n";

            fw.write(vbs);
            fw.close();
            String path = file.getPath().replace("%20", " ");
            Process p = Runtime.getRuntime().exec(
                    "cscript //NoLogo " + path);
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result.append(line);
            }
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mainboardSN = result.toString().trim();
    }

    void netInfo() throws SocketException {
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();

        for (NetworkInterface netint : Collections.list(nets)) {
            Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
            for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                if (inetAddress.toString().substring(0, 8).equals("/192.168")) {
                    netInfo = netint.getDisplayName();
                }
            }
        }
    }

    public boolean save() {
        return StaticFile.save(cpuInfo, osInfo, memInfo, diskInfo, netInfo, mainboardSN);
    }

//    public static void main(String[] args) throws SigarException, IOException {
//        for (Map.Entry<String, String> entry : CPUInfo().entrySet()) {
//            System.out.println(entry.getKey() + "  " + entry.getValue());
//        }
//
//        for (Map.Entry<String, String> entry : memInfo().entrySet()) {
//            System.out.println(entry.getKey() + "  " + entry.getValue());
//        }
//
//        for (Map.Entry<String, String> entry : diskInfo().entrySet()) {
//            System.out.println(entry.getKey() + "  " + entry.getValue());
//        }
//
//        for (Map.Entry<String, String> entry : osInfo().entrySet()) {
//            System.out.println(entry.getKey() + "  " + entry.getValue());
//        }
//
//        System.out.println(mainboardSN());
//
//        System.out.println(netInfo());
//
//    }

    public Map<String, String> getCpuInfo() {
        return cpuInfo;
    }

    public Map<String, String> getMemInfo() {
        return memInfo;
    }

    public Map<String, String> getDiskInfo() {
        return diskInfo;
    }

    public String getMainboardSN() {
        return mainboardSN;
    }

    public Map<String, String> getOsInfo() {
        return osInfo;
    }

    public String getNetInfo() {
        return netInfo;
    }
}

