package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class ActivityRemarkController {

    @Autowired
    private ActivityRemarkService activityRemarkService;

    /**
     *  添加市场活动备注
     * @param activityRemark
     * @param session
     * @return
     */
    @RequestMapping("/workbench/activity/insertRemark.do")
    public @ResponseBody Object insertRemark(ActivityRemark activityRemark, HttpSession session){

        User user = (User) session.getAttribute(Contants.SESSION_USER);
        //收集参数
        activityRemark.setId(UUIDUtils.getUUID());
        activityRemark.setCreateTime(DateUtils.formateDateTime(new Date()));
        activityRemark.setCreateBy(user.getId());
        activityRemark.setEditFlag(Contants.REMARK_EDIT_FLAG_NO_EDITED);

        ReturnObject returnObject = new ReturnObject();
        //这里说明已经完成数据封装，可以调用service层
        try {

            int count = activityRemarkService.insertRemark(activityRemark);

            if (count > 0){
                //如果没有出错就会一直执行
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(activityRemark);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试！");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试！");
        }
        return returnObject;
    }
}
