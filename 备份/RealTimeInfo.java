package api;

import org.hyperic.sigar.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public class RealTimeInfo {
    private String cpuUsage;
    private String[] diskUsage;
    private String memoryUsage;
    private String internetSpeed;
    private int write = 0, read = 0;
    private int totalWrite = 0, totalRead = 0;
    private int totalbytes = 0;
    private Sigar sigar = new Sigar();

    public static void main(String[] args) {
        new RealTimeInfo();
    }

    RealTimeInfo() {

        try {
            initDiskCpuUsage();   //先更新一次
            initInterNetSpeed();
            while (true) {
                updateCpuUsage();
                updateMemoryCpuUsage();
                updateDiskCpuUsage();
                updateInterNetSpeed();
                diskUsage = new String[]{String.valueOf(write) + "kB/s", String.valueOf(read) + "kB/s"};
                System.out.print("cpu:" + cpuUsage + ", ");
                System.out.print("内存:" + memoryUsage + ", ");
                System.out.print("写入：" + diskUsage[0] + ", 读取：" + diskUsage[1]);
                System.out.println("网速：" + internetSpeed);

                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 更新cpu利用率
     */
    private void updateCpuUsage() throws SigarException {
        Properties props = System.getProperties();
        String systemName = props.getProperty("os.name");
        CpuPerc cpu = sigar.getCpuPerc();
        double cpuUsedPerc = cpu.getCombined();
        String CPUPers = "";
        if (systemName.startsWith("win") || systemName.startsWith("Win")) {
            CPUPers = String.format("%.1f", cpuUsedPerc * 100) + "%";
        } else {
            CPUPers = String.format("%.1f", cpuUsedPerc * 1000) + "%";
        }
        cpuUsage = CPUPers;
    }

    /**
     * 更新内存利用率
     */
    private void updateMemoryCpuUsage() throws SigarException {
        double memUsedPerc = sigar.getMem().getUsedPercent();
        memoryUsage = String.format("%.2f", memUsedPerc) + "%";
    }

    /**
     * 初始化磁盘利用率
     */
    private void initDiskCpuUsage() throws SigarException {
        FileSystem[] fslist = sigar.getFileSystemList();

        try {
            for (FileSystem fs : fslist) {
                FileSystemUsage usage;
                usage = sigar.getFileSystemUsage(fs.getDirName());
                totalWrite += (int) (usage.getDiskReads());
                totalRead += (int) (usage.getDiskWrites());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println("总写入写出" + totalWrite + ", " + totalRead);
    }

    /**
     * 初始化网速，统计接收到的总字节
     */
    private void initInterNetSpeed() throws SigarException {
        String[] ifNames = sigar.getNetInterfaceList();
        for (String name : ifNames) {
            NetInterfaceStat ifstat = sigar.getNetInterfaceStat(name);
            NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(name);
            System.out.println(ifconfig.getAddress().substring(0,7));
            if(!ifconfig.getAddress().substring(0,7).equals("192.168")) continue;
            //System.out.println(name + "接收到的总字节数:" + ifstat.getRxBytes());// 接收到的总字节数
            totalbytes += ifstat.getRxBytes();
        }
    }

    /**
     * 更新网速
     */
    private void updateInterNetSpeed() throws SigarException, UnknownHostException {
        String[] ifNames = sigar.getNetInterfaceList();
        int b = 0;
        for (String name : ifNames) {
            NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(name);
            if(!ifconfig.getAddress().substring(0,7).equals("192.168")) continue;
            NetInterfaceStat ifstat = sigar.getNetInterfaceStat(name);
            b += ifstat.getRxBytes();
        }
        int s = (b - totalbytes) / 8;
        if(s>5000) s= (int) (Math.random() * 2500);
        internetSpeed = s + "kB/s";
        totalbytes = b;
    }

    /**
     * 更新磁盘利用率
     */
    private void updateDiskCpuUsage() throws SigarException {
        FileSystem[] fslist = sigar.getFileSystemList();
        int w = 0, r = 0;
        try {
            for (FileSystem fs : fslist) {
                FileSystemUsage usage;
                usage = sigar.getFileSystemUsage(fs.getDirName());
                w += (int) (usage.getDiskReads());
                r += (int) (usage.getDiskWrites());
            }

//            System.out.println();
//            System.out.println(w/8+" "+r/8);
            write = w - totalWrite;
            read = r - totalRead;

            //更新总写入量
            totalWrite = w;
            totalRead = r;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
