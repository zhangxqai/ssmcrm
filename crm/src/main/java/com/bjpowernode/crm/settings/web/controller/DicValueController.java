package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class DicValueController {

    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private UserService userService;

    @RequestMapping("/workbench/clue/selectDicValue.do")
    public String selectDicValue(HttpServletRequest request){
        //封装数据，没有数据就能直接调用service方法
        //查询用户
        List<User> userList = userService.queryAllUsers();
        //查询下拉列表的消息
        List<DicValue> appellationList = dicValueService.selectDicValue("appellation");
        List<DicValue> clueStateList = dicValueService.selectDicValue("clueState");
        List<DicValue> sourceList = dicValueService.selectDicValue("source");
        //查询好之后就要封装这些数据到request中
        request.setAttribute("userList",userList);
        request.setAttribute("appellationList",appellationList);
        request.setAttribute("clueStateList",clueStateList);
        request.setAttribute("sourceList",sourceList);

        //跳转页面
        return "workbench/clue/index";
    }

}
