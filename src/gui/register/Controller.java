package gui.register;

import api.Global;
import api.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import gui.dialog.DialogBuilder;
import gui.pMonitor.pMonitor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;


public class Controller {

    @FXML
    private JFXPasswordField psw1;   //密码1
    @FXML
    private JFXPasswordField psw2;   //密码2
    @FXML
    private JFXTextField user;      //用户名
    @FXML
    private JFXButton btnComfirm;  //确认键
    @FXML
    private JFXButton btnQuit;     //取消键

    //确认键监听
    @FXML
    void comfirm(ActionEvent event) {
        String name = user.getText().trim();
        String password1 = psw1.getText().trim();
        String password2 = psw2.getText().trim();
        //三个没有填
        if (name.length() == 0 || password1.length() == 0 || password2.length() == 0) {
            new DialogBuilder(btnComfirm).setTitle("提示").setMessage("用户名与密码不能为空！").setNegativeBtn("确定").create();
            return;
        }
        //两次密码对不上
        if (!password1.equals(password2)) {
            new DialogBuilder(btnComfirm).setTitle("提示").setMessage("两次密码输入不一致，请重新输入！").setNegativeBtn("确定").create();
            return;
        }
        //注册成功
        if (User.register(name, password1)) {
            Stage stage = (Stage) btnComfirm.getScene().getWindow();
            if (stage.getTitle().equals("注册")) {
                new DialogBuilder(btnComfirm).setTitle("成功").setMessage("注册成功！").setNegativeBtn("确定").create();
                Global.canUseFile = true;
                try {
                    stage.close();
                    new pMonitor().start(new Stage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                new DialogBuilder(btnComfirm).setTitle("成功").setMessage("修改密码成功！").setNegativeBtn("确定").create();
                stage.close();
            }
        } else {  //失败
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.titleProperty().set("警告！");
            alert.headerTextProperty().set("未知错误，注册失败！");
            alert.showAndWait();
        }
    }

    //返回键监听
    @FXML
    void quit(ActionEvent event) {
        Stage stage = (Stage) btnComfirm.getScene().getWindow();
        if (stage.getTitle().equals("注册")) {
            System.exit(0);
        } else stage.close();
    }


    //初始化
    @FXML
    public void initialize() throws InterruptedException {
        FontIcon warnIcon = new FontIcon(FontAwesomeSolid.EXCLAMATION_TRIANGLE);
        warnIcon.getStyleClass().add("error");
        //初始化
        RequiredFieldValidator validator3 = new RequiredFieldValidator();
        validator3.setMessage("用户名不能为空！");
        validator3.setIcon(warnIcon);
        user.getValidators().add(validator3);
        user.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                user.validate();
            }
        });
        user.setLabelFloat(true);
        user.setPromptText("请输入用户名");


        psw1.setLabelFloat(true);
        psw1.setPromptText("请输入密码");
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("密码不能为空！");
        validator.setIcon(warnIcon);
        psw1.getValidators().add(validator);
        psw1.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                psw1.validate();
            }
        });
        psw1.getValidators().add(validator);

        psw2.setLabelFloat(true);
//        psw2.setStyle("-fx-label-float:true");
        psw2.getValidators().add(validator);
        psw2.setPromptText("确认输入密码");
        psw2.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                psw2.validate();
            }
        });
    }
}
