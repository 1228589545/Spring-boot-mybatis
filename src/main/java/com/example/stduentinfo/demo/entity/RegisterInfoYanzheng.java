package com.example.stduentinfo.demo.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class RegisterInfoYanzheng {
    private String username;
    private String password;
    private String password1;
    private String QQ;
    HashMap<String,String> errors = new HashMap<String , String>();

    public boolean panduan(){
        boolean flag = true;
        if (username == null||username.trim().equals( "" )){
              errors.put("username", "请输用户名" );
              flag =false;
        }
        if(password==null||password.trim().equals( "" )){
            errors.put( "password","请输入密码" );
            flag=false;
        }else if(password.length()>12||password.length()<6){
            errors.put( "password","请输入6-12个字符" );
            flag=false;
        }

        if (!password.equals( password1 )&&password!=null) {
            errors.put( "password1","两次密码不一致" );
            flag=false;
        }
        if(QQ.length()>13||QQ.length()<5){
            errors.put( "QQ","请输入正确位数的QQ号" );
            flag=false;
        }
        return flag;
    }
    //向Map集合errors中添加错误信息
//    public void setMessage(String error,String message){
//        if((error!=null)&&(message!=null)){
//            errors.put( error,message );
//        }
//    }
    //获取errors集合
    public Map<String ,String> getErrors(){
        return errors;
    }
}
