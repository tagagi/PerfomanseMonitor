package FileHander;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;

public class UserFile {
    /**
     * 将添加的用户名与密码用AES加密之后存盘
     * @param name  用户名
     * @param password  密码
     * @return 是否成功
     */
    public static boolean register(String name, String password){
        String u = name + password;
        byte[] nameAES = encrypt(u, "password");
        File user = new File("verification.usr");
        try {
            FileWriter fw = new FileWriter(user);
            fw.write(Arrays.toString(nameAES));
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 将用户给的用户名与密码与盘中的比较，如果相同，登录成功
     * @param name  用户名
     * @param password  密码
     * @return 是否相符
     */
    public static boolean login(String name, String password){
        File user = new File("verification.usr");
        try {
            BufferedReader in = new BufferedReader(new FileReader(user));
            String line = in.readLine();
            in.close();
            byte[] nameAES = encrypt(name + password, "password");
            return line.equals(Arrays.toString(nameAES));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * AES加密字符串
     * @param content    需要被加密的字符串
     * @param password  加密需要的密码
     * @return 密文
     */
    private static byte[] encrypt(String content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");// 创建AES的Key生产者
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );
            secureRandom.setSeed(password.getBytes());
            kgen.init(128, secureRandom);// 利用用户密码作为随机数初始化出
            // 128位的key生产者
            //加密没关系，SecureRandom是生成安全随机数序列，password.getBytes()是种子，只要种子相同，序列就一样，所以解密只要有password就行
            SecretKey secretKey = kgen.generateKey();// 根据用户密码，生成一个密钥
            byte[] enCodeFormat = secretKey.getEncoded();// 返回基本编码格式的密钥，如果此密钥不支持编码，则返回null
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");// 转换为AES专用密钥
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化为加密模式的密码器
            return cipher.doFinal(byteContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
