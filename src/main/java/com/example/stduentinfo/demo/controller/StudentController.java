package com.example.stduentinfo.demo.controller;

import com.example.stduentinfo.demo.entity.RegisterInfoYanzheng;
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

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class StudentController {
    //验证码宽度，验证码高度
    private static int WIDTH = 60;
    private static int HEIGHT = 20;
    @Autowired
    private StudentService studentService;

    //首页面
    @RequestMapping("/")
    public void index( HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws IOException {
        HttpSession session;
        //解决乱码问题
        httpServletResponse.setContentType( "text/html;charset=utf-8" );
        PrintWriter out = httpServletResponse.getWriter();
        //创建并且获取保存用户信息的session对象
        session = httpServletRequest.getSession();
        Studentinfo studentinfo =(Studentinfo)session.getAttribute( "studentinfo" );
        if(studentinfo==null){
             out.print( "<h1>您好</h1>\n"+"<h4>您还没登录</h4>\n"+"<a href='/login'>请登录</a>" );
        }else{
            out.print( "<h2>您已经登陆登录</h2>\n<a href=\"/person>查看信息界面</a>" );
        }
        log.info( session.getId() );
        //创建cookie存放session的标识号
        Cookie cookie = new Cookie( "JSESSIONID",session.getId() );
        cookie.setMaxAge( 60*20 );
        cookie.setPath( "/" );
        httpServletResponse.addCookie( cookie );
        log.info( cookie.getValue() );
    }
    //登录界面
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
          String checknode = httpServletRequest.getParameter( "checknode" );
          String checknode2 = (String)httpServletRequest.getSession().getAttribute( "checknode" );
          log.info(checknode2);
          Studentinfo studentinfo = studentService.findByUsernameAndPassword( username,password );
          session.setAttribute( "username",username);
          if(!checknode.equals( checknode2 )){
              map.put( "checknode","验证码错误" );
              return "login";
          }
         else if (studentinfo!=null){
            log.info("IP地址和端口号："+httpServletRequest.getRemoteAddr()+":"+ httpServletRequest.getRemotePort());
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
    @RequestMapping("/loginout")
    public String loginout(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse){
        //将session对象移除
        httpServletRequest.getSession().removeAttribute( "studentinfo" );
        return "login";
    }
    //加入验证码功能
    @RequestMapping("/checknode")
    public void checkservlet(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws IOException {

        HttpSession session = httpServletRequest.getSession();
        httpServletResponse.setContentType( "image/jpeg" );
        ServletOutputStream sos = httpServletResponse.getOutputStream();
        //设置浏览器不要缓存此图片
        httpServletResponse.setHeader( "Pragma","No-cache" );
        httpServletResponse.setHeader( "Cache-Control","no-cache" );
        httpServletResponse.setDateHeader( "Expires",0);
        //创建内存图像并获得其图形上下文
        BufferedImage image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB );
        Graphics g = image.getGraphics();
        //产生随机验证码
        char[] rands = generateChechCode();
        //产生图像
        drawBackground(g);
        drawRands(g,rands);
        //结束图像的绘制过程，完成图像
        g.dispose();
        //将图像输出到客户端
        ByteArrayOutputStream bos = new ByteArrayOutputStream(  );
        ImageIO.write( image,"JPEG",bos );
        byte[] buf =bos.toByteArray();
        httpServletResponse.setContentLength( buf.length );
        bos.writeTo( sos );
        bos.close();
        sos.close();
        //将当前验证码存入session中
        session.setAttribute( "checknode",new String(rands) );
    }
    //画布背景
    private void drawBackground( Graphics g ) {
        //画背景
        g.setColor( new Color( 0XDCDCDC ) );
        g.fillRect( 0,0,WIDTH,HEIGHT );
        //随机产生120个干扰点
        for (int i =0;i<120;i++){
            int x = (int)(Math.random()*WIDTH);
            int y = (int)(Math.random()*HEIGHT);
            int red = (int)(Math.random()*255);
            int green = (int)(Math.random()*255);
            int blue = (int)(Math.random()*255);
            g.setColor( new Color( red,green,blue ) );
            g.drawOval( x,y,1,0 );
        }
    }

    //验证字符
    private void  drawRands(Graphics g,char[] rands){
        g.setColor( Color.BLACK );
        g.setFont( new Font( null,Font.ITALIC|Font.BOLD,18 ) );
        //在不同的高度上输出验证吗的每个字符
        g.drawString( ""+rands[0],1,17 );
        g.drawString( ""+rands[1],16,15 );
        g.drawString( ""+rands[2],31,18 );
        g.drawString( ""+rands[3],46,16 );
        System.out.println(rands);
    }
    //生成一个4字符的验证码
    private char[] generateChechCode() {
        //定义验证码的字符集
        String chars = "1234567890qwertyuioplkjhgfdsazxcvbnm";
        char[] rands = new char[4];
        for (int i = 0;i<4 ;i++){
            int rand = (int)(Math.random()*36);
            rands[i]=chars.charAt( rand );
        }
        return rands;
    }

    @RequestMapping("/register")
    public String register(){
        return "register";
    }

    //注册功能
    @RequestMapping("/registering")
    public String registering( HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,Map<String,String> map ) throws IOException {
        httpServletResponse.setContentType("text/html;charset=utf-8");
        String username = httpServletRequest.getParameter( "username" );
        String password = httpServletRequest.getParameter( "password" );
        String password1 =httpServletRequest.getParameter( "password1" );
        String QQ = httpServletRequest.getParameter( "QQ" );
        String sex = httpServletRequest.getParameter( "sex" );
        String birthday = httpServletRequest.getParameter( "birthday" );
        String myself = httpServletRequest.getParameter( "myself" );
        //获取注册验证信息
        RegisterInfoYanzheng registerInfoYanzheng = new RegisterInfoYanzheng();
        registerInfoYanzheng.setUsername( username );
        registerInfoYanzheng.setPassword( password );
        registerInfoYanzheng.setPassword1( password1 );
        registerInfoYanzheng.setQQ(QQ);
        if(username.equals( studentService.selectUsername( username ) ) ){
            PrintWriter out = httpServletResponse.getWriter();
            out.print( "<p style='color=red'>用户名已注册</p>" );
            return "register";
        }
         if (!registerInfoYanzheng.panduan()){
//            httpServletRequest.setAttribute( "registerInfoYanzheng",registerInfoYanzheng );
            map.put( "Username", registerInfoYanzheng.getErrors().get("username") );
            map.put( "Password", registerInfoYanzheng.getErrors().get("password") );
            map.put( "Password1", registerInfoYanzheng.getErrors().get("password1") );
            map.put( "qq", registerInfoYanzheng.getErrors().get("QQ") );
            return "register";

        }
         else{
            studentService.save( username,password,sex,birthday,myself,QQ );

            //将信息返回
            PrintWriter out = httpServletResponse.getWriter();
            out.print( "<script type=\"text/javascript\">alert('注册成功,请您登录！！！')</script>" );
            return "login";
        }
//        else {
//             //解决乱码
//             httpServletResponse.setContentType("text/html;charset=utf-8");
//             PrintWriter out1 = httpServletResponse.getWriter();
//             out1.print( "<script type=\"text/javascript\">alert('注册失败,请检查每一项是否为空，密码是否相同!!!')</script>" );
////            out1.print ("<p style=\"color:red\">注册失败,请检查每一项是否为空，密码是否相同</p>");
//             return "register";
//        }

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
    //修改个人信息
    @RequestMapping("/change{username}")
    public String updateinfo(HttpServletRequest request, HttpServletResponse httpServletResponse,@RequestParam(value="username") String username,Map<String,Object> map){
        log.info( username );
        String sex = request.getParameter( "sex" );
        String QQ = request.getParameter( "QQ" );
        String birthday =request.getParameter( "birthday" );
        String myself =request.getParameter( "myself" );

        log.info( username );
        httpServletResponse.setContentType( "text/html;charset=utf-8 ");
        if(sex!=null&&QQ!=null&&birthday!=null&&myself!=null) {                 studentService.update( sex,QQ,birthday,myself,username);
            PrintWriter out=null;
            try {
                out=httpServletResponse.getWriter();
            } catch (IOException e) {
                e.printStackTrace();
            }
            out.print( "<script type=\"text/javascript\">alert('修改信息成功！！')</script>" );
            return "person";
        }else {
            map.put( "information","修改的信息内容不能为空");
            return "changeinfo";
        }
    }




}
