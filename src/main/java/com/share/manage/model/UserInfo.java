package com.share.manage.model;

import com.share.manage.utils.SecurityUtil;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.logging.Logger;

public class UserInfo  implements Serializable {
    /**序列号*/
    private static final long serialVersionUID = -5081034939680527551L;
    Logger logger = (Logger) LoggerFactory.getLogger(UserInfo.class);
    /**用户名*/
    private String username;
    /**邮箱*/
    private String email;
    /**密码*/
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 加密
     */
    private void setEcodePwd(){
        String publicKey = "";
        try {
            this.password = SecurityUtil.encrypt(password.getBytes(),publicKey);
        } catch (Exception e) {
            logger.info("密码加密异常");
            e.printStackTrace();
        }
    }

    /**
     * 解密
     */
    private void getDecodePwd(){
        String privateKey = "";
        try {
            this.password = SecurityUtil.decrypt(password,privateKey);
        } catch (Exception e) {
            logger.info("密码解密异常");
            e.printStackTrace();
        }
    }
}
