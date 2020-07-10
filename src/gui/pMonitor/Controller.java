package gui.pMonitor;

import api.Global;
import api.PropertiseHander;
import com.jfoenix.controls.JFXButton;
import gui.RtiInfo.RtiInfo;
import gui.dialog.DialogBuilder;
import gui.login.Login;
import gui.register.Register;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Controller {

    public JFXButton btnHard;
    @FXML
    private RadioMenuItem oneItem;   // 1s菜单项
    @FXML
    private RadioMenuItem fiveItem;  // 5s菜单项
    @FXML
    private RadioMenuItem tenItem;  // 10s菜单项
    @FXML
    private ToggleGroup timegap;    //菜单组
    @FXML
    private MenuItem pathItem;      //文件默认保存目录菜单
    @FXML
    private VBox mainBox;
    @FXML
    private MenuItem loginItem;    //登录菜单栏
    @FXML
    private MenuItem changePswItem;  //修改密码菜单栏
    @FXML
    private MenuItem readITEM;       //读取性能文件菜单栏

    @FXML
    private LineChart<String, Number> cpuLineChart;  //cpu折线图
    @FXML
    private LineChart<String, Number> memLineChart;  //内存折线图
    @FXML
    private LineChart<String, Number> netLineChart;  //网速折线图
    @FXML
    private LineChart<String, Number> diskLineChart;  //磁盘折线图

    @FXML
    private JFXButton btnStart;                      //开始保存键
    @FXML
    private JFXButton btnStop;                       //停止保存键
    @FXML
    private Menu fileMenu;                           //文件菜单

    @FXML
    private CheckMenuItem onTopItem;                //是否置顶选项


    @FXML
    private Label tsetLable;

    //时间选择监测
    @FXML
    void timeMenuClick() {
        if (oneItem.isSelected()) {
            oneItem.setSelected(true);
            oneItem.setDisable(true);
            fiveItem.setDisable(false);
            tenItem.setDisable(false);
            Global.timeGap = 1000;
        } else if (fiveItem.isSelected()) {
            fiveItem.setSelected(true);
            oneItem.setDisable(false);
            fiveItem.setDisable(true);
            tenItem.setDisable(false);
            Global.timeGap = 5000;
        } else if (tenItem.isSelected()) {
            tenItem.setSelected(true);
            oneItem.setDisable(false);
            fiveItem.setDisable(false);
            tenItem.setDisable(true);
            Global.timeGap = 10000;
        }
        PropertiseHander.updatePropertise();
        //System.out.println(Global.timeGap);
    }

    //选择文件默认保存目录监听
    @FXML
    void choseItem() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("请选择默认保留路径");
        directoryChooser.setInitialDirectory(new File(Global.path));
        File newFolder = directoryChooser.showDialog(pMonitor.stage);
        if (newFolder == null) return;
        Global.path = newFolder.getAbsolutePath();
        PropertiseHander.updatePropertise();
//        System.out.println(newFolder.getAbsolutePath());
    }


    //初始化
    @FXML
    public void initialize() {
        Global.realTimeInfo.getTimeInfoThread().start();
        onTopItem.setSelected(Global.onTop);
        //设置开始停止键的录入状态
        if (!Global.canUseFile) {
            btnStart.setDisable(true);
            fileMenu.setDisable(true);
        }
        btnStop.setDisable(true);

        switch (Global.timeGap) {
            case 1000:
                oneItem.setSelected(true);
                oneItem.setDisable(true);
                break;
            case 5000:
                fiveItem.setSelected(true);
                fiveItem.setDisable(true);
                break;
            default:
                tenItem.setSelected(true);
                tenItem.setDisable(true);
        }
        if (Global.canUseFile) {
            loginItem.setDisable(true);
        } else {
            changePswItem.setDisable(true);
        }
        refreshChart();
    }

    //更新图表的线程
    private void refreshChart() {

        //等待一段时间初始化数据
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //cpu
        cpuLineChart.getXAxis().setLabel("时间");
        cpuLineChart.getYAxis().setLabel("占用百分率");
        cpuLineChart.setTitle("cpu占用率");
        cpuLineChart.setCreateSymbols(false);
        cpuLineChart.setAnimated(false);
        cpuLineChart.setLegendVisible(false);
        cpuLineChart.setBlendMode(BlendMode.MULTIPLY);
        XYChart.Series<String, Number> cpuSeries = new XYChart.Series<>();
        for (int i = 0; i < 20; i++) {
            cpuSeries.getData().add(new XYChart.Data<>(String.valueOf(i), 0));
        }
        cpuSeries.getData().add(new XYChart.Data<>(Global.realTimeInfo.getCurTime(), Global.realTimeInfo.getCpuUsage()));
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
        for (int i = 0; i < 20; i++) {
            memSeries.getData().add(new XYChart.Data<>(String.valueOf(i), 0));
        }
        memSeries.getData().add(new XYChart.Data<>(Global.realTimeInfo.getCurTime(), Global.realTimeInfo.getMemoryUsage()));
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
        for (int i = 0; i < 20; i++) {
            netSeries.getData().add(new XYChart.Data<>(String.valueOf(i), 0));
        }
        netSeries.getData().add(new XYChart.Data<>(Global.realTimeInfo.getCurTime(), Global.realTimeInfo.getInternetSpeed()));
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
        for (int i = 0; i < 20; i++) {
            diskSeries.getData().add(new XYChart.Data<>(String.valueOf(i), 0));
        }
        diskSeries.getData().add(new XYChart.Data<>(Global.realTimeInfo.getCurTime(), Global.realTimeInfo.getDiskUsage()));
        diskLineChart.getData().add(diskSeries);

        new Thread(() -> {
            while (true) {
                String time = Global.realTimeInfo.getCurTime();
                //cpu
                cpuSeries.getData().remove(0);
                cpuSeries.getData().add(new XYChart.Data<>(time, Global.realTimeInfo.getCpuUsage()));

                //内存
                memSeries.getData().remove(0);
                memSeries.getData().add(new XYChart.Data<>(time, Global.realTimeInfo.getMemoryUsage()));

                //网速
                netSeries.getData().remove(0);
                netSeries.getData().add(new XYChart.Data<>(time, Global.realTimeInfo.getInternetSpeed()));
                //磁盘
                diskSeries.getData().remove(0);
                diskSeries.getData().add(new XYChart.Data<>(time, Global.realTimeInfo.getDiskUsage()));
                if (Global.saveFile) Global.realTimeInfo.saveFile();
                try {
                    Thread.sleep(Global.timeGap);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    //监听修改密码
    @FXML
    void changePsw() {
        Stage stage = new Stage();
        stage.initOwner(mainBox.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        try {
            new Register().start(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("修改密码");
    }

    //监听登录
    @FXML
    void login() {
        Stage stage = new Stage();
        stage.initOwner(mainBox.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        try {
            new Login().start(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("重新登录");

        stage.setOnHidden(event1 -> {
            if (Global.canUseFile) {
                changePswItem.setDisable(false);
                loginItem.setDisable(true);
                btnStart.setDisable(false);
                fileMenu.setDisable(false);
            }
            stage.close();
        });
    }

    //保存文件键监听
    @FXML
    void saveFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(Global.path));
        fileChooser.setTitle("请选择保存到的文件");

        //获取当前时间
        SimpleDateFormat formatter = new SimpleDateFormat("HH-mm-ss%MM%dd%YYYY");
        Date date = new Date(System.currentTimeMillis());
        String curTime = formatter.format(date);

        fileChooser.setInitialFileName(curTime);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("rti", ".rti"));
        File file = fileChooser.showSaveDialog(btnStart.getScene().getWindow());

        if (file == null) return;

        btnStart.setDisable(true);
        btnStop.setDisable(false);

        Global.rtiFile = file.getAbsolutePath();
        Global.saveFile = true;

//        System.out.println(file.getAbsolutePath());
    }

    //停止保存键监听
    @FXML
    void stopSave() {
        btnStart.setDisable(false);
        btnStop.setDisable(true);
        Global.saveFile = false;

        //更新名字
        SimpleDateFormat formatter = new SimpleDateFormat("HH-mm-ss%MM%dd%YYYY");
        Date date = new Date(System.currentTimeMillis());
        String curTime = formatter.format(date);

        File file = new File(Global.rtiFile);
        String newName = Global.rtiFile.substring(0, Global.rtiFile.length() - 4) + "~" + curTime + ".rti";
        if (file.renameTo(new File(newName)))
            new DialogBuilder(btnStart).setTitle("提示").setMessage("保存成功！").setNegativeBtn("确定").create();
    }


    //查看性能文件监听
    @FXML
    void readRTI(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(Global.path));
        fileChooser.setTitle("请选择要读取的文件");
        //fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("rti", ".rti"));
        List<File> fileList = fileChooser.showOpenMultipleDialog(btnStart.getScene().getWindow());

        if (fileList == null) return;

        Global.fileList = fileList;
        try {
            Stage stage = new Stage();
            stage.initOwner(btnStart.getScene().getWindow());
            new RtiInfo().start(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //硬件详情监听
    @FXML
    void hardWare() throws IOException {
        Stage stage = new Stage();
        stage.initOwner(btnStart.getScene().getWindow());
        new gui.staticInfo.StaticInfo().start(stage);
    }

    //窗口置顶菜单栏监听
    @FXML
    void changeOnTop(ActionEvent event) {
        Stage stage = (Stage) btnStart.getScene().getWindow();
        stage.setAlwaysOnTop(onTopItem.isSelected());
        Global.onTop = onTopItem.isSelected();
        PropertiseHander.updatePropertise();
    }
}
