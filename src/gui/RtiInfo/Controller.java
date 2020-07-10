package gui.RtiInfo;

import api.Global;
import api.Info;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class Controller {

    @FXML
    private TableView<Info> rtiTable;  //表格

    @FXML
    private TableColumn<Info, String> timeCol;

    @FXML
    private TableColumn<Info, Float> cpuCol;
    @FXML
    private TableColumn<Info, Float> menCol;
    @FXML
    private TableColumn<Info, Float> netCol;
    @FXML
    private TableColumn<Info, Float> diskCol;

    @FXML
    private PieChart cpuPie;
    @FXML
    private PieChart memPie;
    @FXML
    private PieChart diskPie;
    @FXML
    private ProgressBar netbar;
    @FXML
    private Label netLable;

    @FXML                           //初始化
    public void initialize() {

        //设置表格
        List<Info> infos = Global.realTimeInfo.readFile();
        ObservableList<Info> obList = FXCollections.observableArrayList(infos);
        cpuCol.setCellValueFactory(cell -> cell.getValue().cpuUsageProperty().asObject());
        menCol.setCellValueFactory(cell -> cell.getValue().memoryUsageProperty().asObject());
        netCol.setCellValueFactory(cell -> cell.getValue().internetSpeedProperty().asObject());
        diskCol.setCellValueFactory(cell -> cell.getValue().diskUsageProperty().asObject());
        timeCol.setCellValueFactory(cell -> cell.getValue().curTimeProperty());
        rtiTable.setEditable(false);
        rtiTable.setItems(obList);

        //计算平局
        float cpu = 0, mem = 0, disk = 0, net = 0;
        for (Info info : infos) {
            cpu += info.getCpuUsage();
            mem += info.getMemoryUsage();
            disk += info.getDiskUsage();
            net += info.getInternetSpeed();
        }
        int len = infos.size();
        cpu /= len;
        mem /= len;
        net /= len;
        disk /= len;

        //cpu饼图
        ObservableList<PieChart.Data> cpuPieData =
                FXCollections.observableArrayList(
                        new PieChart.Data("占用", cpu),
                        new PieChart.Data("空闲", 100 - cpu)
                );
        cpuPie.setData(cpuPieData);
        cpuPie.setTitle("CPU：" + (int) cpu + "%");

        //内存饼图
        ObservableList<PieChart.Data> memPieData =
                FXCollections.observableArrayList(
                        new PieChart.Data("占用", mem),
                        new PieChart.Data("空闲", 100 - mem)
                );
        memPie.setData(memPieData);
        memPie.setTitle("内存：" + (int) mem + "%");

        //cpu饼图
        ObservableList<PieChart.Data> diskPieData =
                FXCollections.observableArrayList(
                        new PieChart.Data("占用", disk),
                        new PieChart.Data("空闲", 100 - disk)
                );
        diskPie.setData(diskPieData);
        diskPie.setTitle("磁盘：" + disk + "%");

        //网速
        netbar.setProgress((20 + Math.random() * 50) / 100);
        netLable.setText("平均网速" + (int) (net * 10) / 10.0 + "kB/s");
    }


    @FXML
    void showLineChart(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        stage.initOwner(memPie.getScene().getWindow());
        new RtiLineChart().start(stage);
    }
}
