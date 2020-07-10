import api.PropertiseHander;
import gui.login.Login;
import gui.register.Register;
import javafx.application.Application;

import java.io.*;

public class PM {
    public static void main(String[] args) {
        PropertiseHander.initProperties();
        File f = new File("verification.usr");
        if (!f.exists())
            Application.launch(Register.class, args);
        else Application.launch(Login.class, args);
    }
}
