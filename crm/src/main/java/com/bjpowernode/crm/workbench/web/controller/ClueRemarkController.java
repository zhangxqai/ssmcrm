package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.service.ClueRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class ClueRemarkController {

    @Autowired
    private ClueRemarkService clueRemarkService;

    @RequestMapping("/workbench/clue/insertClueRemark.do")
    public @ResponseBody Object insertClueRemark(ClueRemark clueRemark, HttpSession session){

        User user = (User) session.getAttribute(Contants.SESSION_USER);
        //收集数据，封装数据
        //生成id
        clueRemark.setId(UUIDUtils.getUUID());
        clueRemark.setCreateBy(user.getId());
        clueRemark.setCreateTime(DateUtils.formateDateTime(new Date()));
        clueRemark.setEditFlag(Contants.REMARK_EDIT_FLAG_NO_EDITED);

        ReturnObject returnObject = new ReturnObject();
        try {
            //封装好参数就要调用service方法
            int count = clueRemarkService.insertClueRemark(clueRemark);

            //还要判断有没有添加成功
            if (count > 0){

                //成功
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(clueRemark);
            }else {
                //失败
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试。。。");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试。。。");
        }

        return returnObject;
    }

    @RequestMapping("/workbench/clue/deleteClueRemarkById.do")
    public @ResponseBody Object deleteClueRemarkById(String id){

        ReturnObject returnObject = new ReturnObject();
        try {
            //收集参数，这里已经收集好参数
            //直接调用方法
            int count = clueRemarkService.deleteClueRemarkById(id);

            //判断有没有修改成功
            if(count > 0 ){
                //成功，将信息返回去
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(count);
            }else {
                //失败
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试...");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试...");
        }

        return returnObject;

    }

    /**
     * 根据id对线索中的备注进行修改
     * @param clueRemark
     * @return
     */
    @RequestMapping("/workbench/clue/updateClueRemarkById.do")
    public @ResponseBody Object updateClueRemarkById(ClueRemark clueRemark,HttpSession session){

        User user = (User) session.getAttribute(Contants.SESSION_USER);
        ReturnObject returnObject = new ReturnObject();
        clueRemark.setEditFlag(Contants.REMARK_EDIT_FLAG_YES_EDITED);
        clueRemark.setEditBy(user.getId());
        clueRemark.setEditTime(DateUtils.formateDateTime(new Date()));
        try {
            //收集数据，封装数据，已经封装就能直接调用service方法
            int count = clueRemarkService.updateClueRemarkById(clueRemark);

            //判断有没有修改成功
            if (count > 0){
                //成功
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(clueRemark);
            }else {
                //失败
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试...");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试...");
        }
        return returnObject;
    }
}
