package gui.staticInfo;

import api.Global;
import api.StaticInfo;
import com.jfoenix.controls.JFXButton;
import gui.dialog.DialogBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Controller {
    @FXML
    private Label cpuLable;

    @FXML
    private Label OSLable;

    @FXML
    private Label menLabel;

    @FXML
    private Label diskLabel;

    @FXML
    private Label nicLabel;

    @FXML
    private Label mbLabel;
    @FXML
    private JFXButton btnsave;

    @FXML                           //初始化
    public void initialize() {
        Global.staticInfo = new api.StaticInfo();
        if (!Global.canUseFile) btnsave.setDisable(true);
        //cpu
        Map<String, String> cpuMap = Global.staticInfo.getCpuInfo();
        String cpuString =
                "CPU:" +
                        "型号：" + cpuMap.get("vendor") + " " + cpuMap.get("name") + "\n" +
                        "核心数： " + cpuMap.get("cores") + "核\n" +
                        "序列号：" + cpuMap.get("serial");
        cpuLable.setText(cpuString);

        //os
        Map<String, String> osMap = Global.staticInfo.getOsInfo();
        String osString =
                "OS:" +
                        "版本：" + osMap.get("name") + "\n" +
                        "架构：" + osMap.get("arch") + "\n" +
                        "位数：" + osMap.get("bits");
        OSLable.setText(osString);

        //内存
        Map<String, String> menMap = Global.staticInfo.getMemInfo();
        String menString =
                "内存:" +
                        "总容量：" + menMap.get("total");
        menLabel.setText(menString);

        //硬盘
        Map<String, String> diskMap = Global.staticInfo.getDiskInfo();
        String diskString =
                "硬盘:" +
                        "总容量：" + diskMap.get("total") + "\n" +
                        "已使用：" + diskMap.get("use") + "\n" +
                        "序列号：" + diskMap.get("serial");
        diskLabel.setText(diskString);


        //网卡
        String netString = "网卡：\n型号：" + Global.staticInfo.getNetInfo();
        nicLabel.setText(netString);

        //主板
        String mbString = "主板：\n序列号：" + Global.staticInfo.getMainboardSN();
        mbLabel.setText(mbString);

    }

    @FXML
    void saveStaticInfo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(Global.path));
        fileChooser.setTitle("请选择保存到的文件");
        fileChooser.setInitialFileName("硬件信息");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT", ".txt"));
        File file = fileChooser.showSaveDialog(btnsave.getScene().getWindow());
        if (file == null) return;

        Global.staticFile = file.getAbsolutePath();

        if(Global.staticInfo.save()){
            new DialogBuilder(btnsave).setTitle("提示").setMessage("保存成功！").setNegativeBtn("确定").create();
        }
    }

}
