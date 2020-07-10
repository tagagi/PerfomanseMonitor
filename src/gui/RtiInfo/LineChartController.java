package gui.RtiInfo;

import api.Global;
import api.Info;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.VBox;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LineChartController {

    @FXML
    private VBox mainBox;                            //主容器
    @FXML
    private LineChart<String, Number> cpuLineChart;  //cpu折线图
    @FXML
    private LineChart<String, Number> memLineChart;  //内存折线图
    @FXML
    private LineChart<String, Number> netLineChart;  //网速折线图
    @FXML
    private LineChart<String, Number> diskLineChart;  //磁盘折线图

    //初始化
    @FXML
    public void initialize() {
        List<Info> infos = Global.realTimeInfo.readFile();      //历史文件信息
        //先要按照时间排序
        infos.sort((o1,o2)->{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date d1 = sdf.parse(o1.getCurTime());
                Date d2 = sdf.parse(o2.getCurTime());
                return (int) (d1.getTime()-d2.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        });
        //cpu
        cpuLineChart.getXAxis().setLabel("时间");
        cpuLineChart.getYAxis().setLabel("占用百分率");
        cpuLineChart.setTitle("cpu占用率");
        cpuLineChart.setCreateSymbols(false);
        cpuLineChart.setAnimated(false);
        cpuLineChart.setLegendVisible(false);
        cpuLineChart.setBlendMode(BlendMode.MULTIPLY);
        XYChart.Series<String, Number> cpuSeries = new XYChart.Series<>();
        //cpuSeries.getData().add(new XYChart.Data<>(Global.realTimeInfo.getCurTime(), Global.realTimeInfo.getCpuUsage()));
        cpuLineChart.getData().add(cpuSeries);

        //内存
        memLineChart.getXAxis().setLabel("时间");
        memLineChart.getYAxis().setLabel("占用百分率");
        memLineChart.setTitle("内存占用率");
        memLineChart.setCreateSymbols(false);
        memLineChart.setAnimated(false);
        memLineChart.setLegendVisible(false);
        memLineChart.setBlendMode(BlendMode.MULTIPLY);
        XYChart.Series<String, Number> memSeries = new XYChart.Series<>();
        //memSeries.getData().add(new XYChart.Data<>(Global.realTimeInfo.getCurTime(), Global.realTimeInfo.getMemoryUsage()));
        memLineChart.getData().add(memSeries);
        //网速
        netLineChart.getXAxis().setLabel("时间");
        netLineChart.getYAxis().setLabel("kB/s");
        netLineChart.setTitle("网速");
        netLineChart.setCreateSymbols(false);
        netLineChart.setAnimated(false);
        netLineChart.setLegendVisible(false);
        netLineChart.setBlendMode(BlendMode.MULTIPLY);
        XYChart.Series<String, Number> netSeries = new XYChart.Series<>();
        //netSeries.getData().add(new XYChart.Data<>(Global.realTimeInfo.getCurTime(), Global.realTimeInfo.getInternetSpeed()));
        netLineChart.getData().add(netSeries);
        //磁盘
        diskLineChart.getXAxis().setLabel("时间");
        diskLineChart.getYAxis().setLabel("占用百分率");
        diskLineChart.setTitle("磁盘利用率");
        diskLineChart.setCreateSymbols(false);
        diskLineChart.setAnimated(false);
        diskLineChart.setLegendVisible(false);
        diskLineChart.setBlendMode(BlendMode.MULTIPLY);
        XYChart.Series<String, Number> diskSeries = new XYChart.Series<>();
        //diskSeries.getData().add(new XYChart.Data<>(Global.realTimeInfo.getCurTime(), Global.realTimeInfo.getDiskUsage()));
        diskLineChart.getData().add(diskSeries);

        //添加数据
        for(Info info:infos){
            String time = info.getCurTime().substring(11,19);
            cpuSeries.getData().add(new XYChart.Data<>(time,info.getCpuUsage()));
            memSeries.getData().add(new XYChart.Data<>(time,info.getMemoryUsage()));
            netSeries.getData().add(new XYChart.Data<>(time,info.getInternetSpeed()));
            diskSeries.getData().add(new XYChart.Data<>(time,info.getDiskUsage()));
        }
    }
}

