package com.example.stduentinfo.demo.controller;

import com.example.stduentinfo.demo.entity.Studentinfo;
import com.example.stduentinfo.demo.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import sun.awt.geom.AreaOp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class StudentController {
    @Autowired
    private StudentService studentService;

    //主页
    @RequestMapping("/")
    public String index(){

        return "index";
    }
    //登良界面
    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/person")
    public String person(){
        return "person";
    }

    @RequestMapping("logining")
    //登录功能
    public  String logining( HttpServletRequest httpServletRequest,HttpSession session,HttpServletResponse httpServletResponse,Map<String,Object> map)throws Exception{
          String username = httpServletRequest.getParameter( "username" );
          String password = httpServletRequest.getParameter( "password" );
          Studentinfo studentinfo = studentService.findByUsernameAndPassword( username,password );
          session.setAttribute( "username",username);
//          session.setAttribute( "登陆成功","success" );
        if (studentinfo!=null){

            httpServletResponse.setContentType("text/html;charset=utf-8");
            //将信息返回
            PrintWriter out = httpServletResponse.getWriter();
//            out.print("<p style=\"color:red\">登陆成功!!!</p>");
            out.print( "<script type=\"text/javascript\">alert('登陆成功!!!')</script>" );
            return "person";
        }
        else
            {
                //两种方式一种直接显示，一种弹出窗口显示
                map.put( "information","登陆失败,请检查用户名和密码" );
//                httpServletResponse.setContentType("text/html;charset=utf-8");
//                PrintWriter out1 = httpServletResponse.getWriter();
//                out1.print( "<script type=\"text/javascript\">alert('登陆失败,请检查用户名和密码!!!')</script>" );
                return "login";
            }


    }

    @RequestMapping("/register")
    public String register(){
        return "register";
    }

    //注册功能
    @RequestMapping("/registering")
    public String registering( HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse ) throws IOException {
        String username = httpServletRequest.getParameter( "username" );
        String password = httpServletRequest.getParameter( "password" );
        String password1 =httpServletRequest.getParameter( "password1" );
        String QQ = httpServletRequest.getParameter( "QQ" );
        String sex = httpServletRequest.getParameter( "sex" );
        String birthday = httpServletRequest.getParameter( "birthday" );
        String myself = httpServletRequest.getParameter( "myself" );
        if (username!=null&password!=null&password.equals( password1 )){
            studentService.save( username,password,sex,birthday,myself,QQ );
            httpServletResponse.setContentType("text/html;charset=utf-8");
            //将信息返回
            PrintWriter out = httpServletResponse.getWriter();
            out.print( "<script type=\"text/javascript\">alert('注册成功,请您登录！！！')</script>" );
            return "login";
        }
        else {
             //解决乱码
             httpServletResponse.setContentType("text/html;charset=utf-8");
             PrintWriter out1 = httpServletResponse.getWriter();
             out1.print( "<script type=\"text/javascript\">alert('注册失败,请检查每一项是否为空，密码是否相同!!!')</script>" );
//            out1.print ("<p style=\"color:red\">注册失败,请检查每一项是否为空，密码是否相同</p>");
             return "register";
        }

    }
//    查看个人信息
    @RequestMapping("/personinfo{username}")
    public ModelAndView getPersonInfo(@RequestParam String username ){
         List<Studentinfo> studentinfo =studentService.findByUsername( username );
         ModelAndView mv = new ModelAndView();
         mv.setViewName( "personinfo" );
         //获取列表中的元素（和用javaGUI 那个项目获取信息的方式一样，有个弊端就是太麻烦，目前正在讲究简便方法）
        //thymleaf模板太复杂了，我这个菜鸟学起来好吃力
         String Username = studentinfo.get(0).getUsername();
         String Sex = studentinfo.get(0).getSex();
         String Birthday = studentinfo.get(0).getBirthday();
         String QQ = studentinfo.get(0).getQQ();
         String Myself = studentinfo.get(0).getMyself();


         mv.addObject( "Username",Username );
         mv.addObject( "Sex",Sex );
         mv.addObject( "Birthday",Birthday );
         mv.addObject( "QQ",QQ  );
         mv.addObject( "Myself",Myself  );

         return mv;
    }
    @RequestMapping("/change{username}")
    public String updateinfo(HttpServletRequest request, HttpServletResponse httpServletResponse,@RequestParam(value="username") String username){
        log.info( username );
        String sex = request.getParameter( "sex" );
        String QQ = request.getParameter( "QQ" );
        String birthday =request.getParameter( "birthday" );
        String myself =request.getParameter( "myself" );
        studentService.update( sex,QQ,birthday,myself,username);
        log.info( username );
        httpServletResponse.setContentType( "text/html;charset=utf-8 ");
        PrintWriter out =null;
        try {
                out=httpServletResponse.getWriter();
            } catch (IOException e) {
                e.printStackTrace();
            }
            out.print( "<script type=\"text/javascript\">alert('修改信息成功！！')</script>" );
            return "person";


    }



}
