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
    private String username;
    private String password;
    private String sex;
    private String birthday;
    private String QQ;
    private String myself;
}
