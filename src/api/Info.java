package api;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

//初始化，给lineChart提供数据
public class Info {
    private StringProperty curTime;           //当前时间
    private FloatProperty cpuUsage;       //cpu利用率
    private FloatProperty memoryUsage;    //内存利用率
    private FloatProperty internetSpeed;  //网速
    private FloatProperty diskUsage;      //磁盘使用率

    public Info(String time, Float cpu, Float mem, Float net, Float disk) {
        super();
        curTime = new SimpleStringProperty(time);
        cpuUsage = new SimpleFloatProperty(cpu);
        memoryUsage = new SimpleFloatProperty(mem);
        internetSpeed = new SimpleFloatProperty(net);
        diskUsage = new SimpleFloatProperty(disk);
    }


    public String getCurTime() {
        return curTime.get();
    }

    public StringProperty curTimeProperty() {
        return curTime;
    }

    public float getCpuUsage() {
        return cpuUsage.get();
    }

    public FloatProperty cpuUsageProperty() {
        return cpuUsage;
    }

    public float getMemoryUsage() {
        return memoryUsage.get();
    }

    public FloatProperty memoryUsageProperty() {
        return memoryUsage;
    }

    public float getInternetSpeed() {
        return internetSpeed.get();
    }

    public FloatProperty internetSpeedProperty() {
        return internetSpeed;
    }

    public float getDiskUsage() {
        return diskUsage.get();
    }

    public FloatProperty diskUsageProperty() {
        return diskUsage;
    }
}
