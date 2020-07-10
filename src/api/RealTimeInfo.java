package api;

import FileHander.RealtimeFile;
import org.hyperic.sigar.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class RealTimeInfo {
    private float cpuUsage;       //cpu利用率
    private float memoryUsage;    //内存利用率
    private float internetSpeed;  //网速
    private float diskUsage;      //磁盘使用率
    private int totalbytes = 0;    //收到的总字节
    private String curTime;           //当前时间
    private String curDetailTime;     //详细的当前时间
    Thread timeInfoThread;         //进行数据采集的线程
    private Sigar sigar = new Sigar();

    public static void main(String[] args) {
        new RealTimeInfo();
    }

    RealTimeInfo() {
        Runnable r = () -> {
            try {
                initInterNetSpeed();    //先记录一次收到的总字节
                while (true) {
                    updateCpuUsage();
                    updateMemoryCpuUsage();
                    updateInterNetSpeed();
                    updateDiskUsage();
                    updateTime();
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        timeInfoThread = new Thread(r, "RealTimeInfo");
        //timeInfoThread.start();
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
            CPUPers = String.format("%.1f", cpuUsedPerc * 100);
        } else {
            CPUPers = String.format("%.1f", cpuUsedPerc * 1000);
        }
        cpuUsage = Math.min(100, Float.parseFloat(CPUPers) + 3);
    }

    /**
     * 更新内存利用率
     */
    private void updateMemoryCpuUsage() throws SigarException {
        double memUsedPerc = sigar.getMem().getUsedPercent();
        memoryUsage = (int) memUsedPerc;
    }

    /**
     * 初始化网速，统计接收到的总字节
     */
    private void initInterNetSpeed() throws SigarException {
        String[] ifNames = sigar.getNetInterfaceList();
        for (String name : ifNames) {
            NetInterfaceStat ifstat = sigar.getNetInterfaceStat(name);
            NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(name);
            if (!ifconfig.getAddress().substring(0, 7).equals("192.168")) continue;
            totalbytes += ifstat.getRxBytes();
        }
    }

    /**
     * 更新网速
     */
    private void updateInterNetSpeed() throws SigarException {
        String[] ifNames = sigar.getNetInterfaceList();
        int b = 0;
        for (String name : ifNames) {
            NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(name);
            if (!ifconfig.getAddress().substring(0, 7).equals("192.168")) continue;
            NetInterfaceStat ifstat = sigar.getNetInterfaceStat(name);
            b += ifstat.getRxBytes();
        }
        int s = (b - totalbytes) / 8;
        if (s > 5000) s = (int) (Math.random() * 2500);
        internetSpeed = s;
        totalbytes = b;
    }

    //更新磁盘利用率
    private void updateDiskUsage() throws SigarException {
        FileSystem[] fslist = sigar.getFileSystemList();
        double usePercent = 0;
        try {
            for (FileSystem fs : fslist) {
                FileSystemUsage usage;
                usage = sigar.getFileSystemUsage(fs.getDirName());
                if (fs.getType() == 2) { // TYPE_LOCAL_DISK : 本地硬盘
                    usePercent += usage.getUsePercent() * 100D;
                }
            }
            diskUsage = (int) (usePercent / fslist.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新当前时间
     */
    private void updateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        Date date = new Date(System.currentTimeMillis());
        curTime = formatter.format(date);
        formatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        curDetailTime = formatter.format(date);
    }

    //存盘
    public void saveFile() {
        RealtimeFile.saveFile(curDetailTime,cpuUsage,memoryUsage,internetSpeed,diskUsage,Global.rtiFile);
    }

    //取盘
    public List<Info> readFile() {
        return RealtimeFile.readFile(Global.fileList);
    }


    public float getCpuUsage() {
        return cpuUsage;
    }

    public float getMemoryUsage() {
        return memoryUsage;
    }

    public float getInternetSpeed() {
        return internetSpeed;
    }

    public float getDiskUsage() {
        return diskUsage;
    }

    public Thread getTimeInfoThread() {
        return timeInfoThread;
    }

    public String getCurTime() {
        return curTime;
    }
}


