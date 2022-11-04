package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(){

        return "settings/qx/user/login";

    }

    @RequestMapping("/settings/qx/user/login.do")
    public @ResponseBody Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletResponse response,HttpSession session, HttpServletRequest request){

        //封装参数
        Map<String,Object> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        map.put("isRemPwd",isRemPwd);

        //调用service层方法，查询用户
        User user = userService.queryByLoginActAndPwd(map);

        //创建实体类，封装需要返回给前端数据
        ReturnObject returnObject = new ReturnObject();

        //根据查询结果，生成响应消息
        if(user == null){
            //登录失败，用户名和密码错误
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("用户名和密码错误");

        }else {
            //如果能查出来就进一步判断
            //获取这个账号的有效时间，与当前的时间进行比较

            String nowStr = DateUtils.formateDateTime(new Date());
            //判断账号有没有过期
            if (nowStr.compareTo(user.getExpireTime()) > 0){

                //大于0，表示登录失败，账号已到期
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("登录失败，账号已到期");

            }else if("0".equals(user.getLockState())){

                //判断状态是否为锁定状态，是否为0
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账号已锁定状态");

            }else if (!user.getAllowIps().contains(request.getRemoteAddr())){
                //判断登录的电脑中的ip地址是否包含在数据库中,没有在就登录失败
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("ip受限");

            }else {
                //能到这里说明已经能成功登录了
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);

                //前端页面中需要显示当前用户的名字
                session.setAttribute(Contants.SESSION_USER,user);
                //先判断有没有勾选上记住密码
                if ("true".equals(isRemPwd)){
                    Cookie c1 = new Cookie("loginAct",user.getLoginAct());
                    c1.setMaxAge(60*60*24*10);
                    response.addCookie(c1);

                    Cookie c2 = new Cookie("loginPwd",user.getLoginPwd());
                    c2.setMaxAge(10*24*60*60);
                    response.addCookie(c2);
                }else {
                    Cookie c1 = new Cookie("loginAct","1");
                    c1.setMaxAge(0);
                    response.addCookie(c1);

                    Cookie c2 = new Cookie("loginPwd","1");
                    c2.setMaxAge(0);
                    response.addCookie(c2);
                }
            }
        }
        return returnObject;

    }

    @RequestMapping("/settings/qx/user/logout.do")
    public String loginout(HttpServletResponse response , HttpSession session){

        //消除cookie
        Cookie c1 = new Cookie("loginAct","1");
        c1.setMaxAge(0);
        response.addCookie(c1);

        Cookie c2 = new Cookie("loginPwd","1");
        c2.setMaxAge(0);
        response.addCookie(c2);

        //消除session
        session.invalidate();

        //没有需要携带的数据，直接使用重定向的方法跳转
        return "redirect:/";
    }
}
