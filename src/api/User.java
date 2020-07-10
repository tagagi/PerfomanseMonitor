package api;

import FileHander.UserFile;


public class User {
    //注册
    public static boolean register(String name, String password){
        return UserFile.register(name,password);
    }

    //登录
    public static boolean login(String name, String password){
        return UserFile.login(name,password);
    }
}
