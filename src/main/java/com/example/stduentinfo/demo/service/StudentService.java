package com.example.stduentinfo.demo.service;

import com.example.stduentinfo.demo.entity.Studentinfo;
import com.example.stduentinfo.demo.mapper.StudentMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.util.List;

@org.springframework.stereotype.Service
//服务类
public class StudentService {
    @Autowired
    private StudentMapper studentMapper;

    //注册功能
    public void save( String username, String password, String sex, String birthday, String myself, String QQ){

        studentMapper.save( username, password,  sex,
                            birthday,  myself,  QQ );
    }
    //登陆功能（通过账号和密码查询数据库进行登陆）
    public Studentinfo findByUsernameAndPassword(String username,String password){
        return studentMapper.findByUsernameAndPassword( username,password );
    }
    //查询学生信息功能（通过用户名查询学生所有信息）
    public List<Studentinfo> findByUsername(String username){
        return studentMapper.findByUsername( username );
    }

    //学生修改信息功能
    public void update(String sex,String QQ,String birthday,String myself,String username){
        studentMapper.update( sex, QQ,birthday,myself,username);
    }


}
