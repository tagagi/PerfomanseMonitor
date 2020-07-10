package gui.login;

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
import javafx.stage.Stage;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;


public class Controller {
    @FXML
    private JFXPasswordField psw;
    @FXML
    private JFXTextField user;
    @FXML
    private JFXButton comfirm;
    @FXML
    private JFXButton quit;
    @FXML
    private JFXButton skip;

    //确认键监听
    @FXML
    void comfirm(ActionEvent event) throws IOException {
        String name = user.getText().trim();
        String password = psw.getText().trim();

        //如果为空
        if (name.length() == 0 || password.length() == 0) {
            new DialogBuilder(quit).setTitle("提示").setMessage("请输入用户名与密码！").setNegativeBtn("确定").create();
            return;
        }
        //如果密码对不上
        if (!User.login(name, password)) {
            new DialogBuilder(quit).setTitle("提示").setMessage("用户名或密码输入错误，请重新输入！").setNegativeBtn("确定").create();
        } else {
            Stage stage = (Stage) user.getScene().getWindow();
            Global.canUseFile = true;

            if (stage.getTitle().equals("登录")) {
                stage.close();
                new pMonitor().start(new Stage());
            } else {
                stage.hide();
            }
        }
    }

    //退出键监听
    @FXML
    void quit(ActionEvent event) {
        Stage stage = (Stage) user.getScene().getWindow();
        if (stage.getTitle().equals("登录")) {
            System.exit(0);
        } else {
            stage.close();
        }
    }

    //跳过键监听
    @FXML
    void skip(ActionEvent event) {
        new DialogBuilder(quit).setNegativeBtn("取消", new DialogBuilder.OnClickListener() {
            @Override
            public void onClick() {
                //点击取消按钮之后执行的动作
                System.out.println("取消");
            }
        }).setPositiveBtn("确定", new DialogBuilder.OnClickListener() {
            @Override
            public void onClick() {
                Global.canUseFile = false;
                Stage stage = (Stage) comfirm.getScene().getWindow();
                stage.close();
                if (stage.getTitle().equals("登录")) {
                    try {
                        new pMonitor().start(new Stage());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //System.out.println("确认");
            }
        }).setTitle("提示").setMessage("跳过之后无法使用文件相关功能，确认跳过？").create();
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


        psw.setLabelFloat(true);
        psw.setPromptText("请输入密码");
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("密码不能为空！");
        validator.setIcon(warnIcon);
        psw.getValidators().add(validator);
        psw.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                psw.validate();
            }
        });
    }
}
