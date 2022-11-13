package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueActivityRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ClueActivityRelationController {

    @Autowired
    private ClueActivityRelationService clueActivityRelationService;

    @Autowired
    private ActivityService activityService;

    @RequestMapping("/workbench/clue/insertClueActivityRelation.do")
    public @ResponseBody Object insertClueActivityRelation(String[] activityId, String clueId){

        ClueActivityRelation clueActivityRelation = null;
        List<ClueActivityRelation> clueActivityRelationList = new ArrayList<>();
        ReturnObject returnObject = new ReturnObject();
        //第一步封装数据
        for (String ai:activityId) {
            clueActivityRelation = new ClueActivityRelation();
            clueActivityRelation.setActivityId(ai);
            clueActivityRelation.setClueId(clueId);
            clueActivityRelation.setId(UUIDUtils.getUUID());
            //到这里就将一条数据封装好了
            //将这条数据封装到列表中
            clueActivityRelationList.add(clueActivityRelation);
        }

        try {
            //这些数据封装好了，就能调用service方法，去添加数据
            int count = clueActivityRelationService.insertClueActivityRelation(clueActivityRelationList);

            //判断有没有添加成功
            if(count >0){
                //成功
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                //之后还有查询列表
                List<Activity> activityList = activityService.selectForClueRelationByIds(activityId);
                returnObject.setRetData(activityList);
            }else {
                //失败
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试...");
            }
        }catch (Exception e){
            e.printStackTrace();
            //失败
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试...");
        }

        return returnObject;
    }

    /**
     * 根据id解除市场活动和线索的关联
     * @param clueActivityRelation
     * @return
     */
    @RequestMapping("/workbench/clue/deleteClueActivityRelationById.do")
    public @ResponseBody Object deleteClueActivityRelationById(ClueActivityRelation clueActivityRelation){

        ReturnObject returnObject = new ReturnObject();
        try {
            //收集数据，已经收集好了，就直接调用service方法
             int count = clueActivityRelationService.deleteClueActivityRelationById(clueActivityRelation);

             //判断有没有修改成功
            if (count > 0){
                //成功
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
}
