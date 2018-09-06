package com.example.stduentinfo.demo.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.sql.Date;

@Setter
@Getter
//实体类，对应数据库信息
public class Studentinfo {
    @NotEmpty(message="用户名不能为空！")
    private String username;
    @Size (min=6,max=10,message = "密码长度必须6到10位")
    private String password;
    private String sex;
    private String birthday;
    private String QQ;
    private String myself;
}
