package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ClueController {

    @Autowired
    private ClueService clueService;

    @RequestMapping("/workbench/clue/selectActivityAll.do")
    public @ResponseBody Object selectActivityAll(String fullname,String company,String phone,String mphone,String source,String owner,String state,int pageNo,int pageSize){

        //这里就要封装参数,将参数封装到map中
        Map<String,Object> map = new HashMap<>();
        map.put("fullname",fullname);
        map.put("company",company);
        map.put("phone",phone);
        map.put("mphone",mphone);
        map.put("source",source);
        map.put("owner",owner);
        map.put("state",state);
        map.put("beginNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);
        //这里已经将数据封装好了

        //将封装数据，调用service方法，查询符合条件的列表
        List<Clue> clueList = clueService.selectActivityAll(map);
        int totalRows = clueService.selectClueAllCount(map);

        //查询出来的数据还需要将它们封装起来
        Map<String,Object> retMap = new HashMap<>();
        retMap.put("clueList",clueList);
        retMap.put("totalRows",totalRows);

        return retMap;
    }

    /**
     * 创建线索
     * @param clue
     * @param session
     * @return
     */
    @RequestMapping("/workbench/clue/insertClue.do")
    public @ResponseBody Object insertClue(Clue clue , HttpSession session){

        User user = (User) session.getAttribute(Contants.SESSION_USER);
        //封装参数
        clue.setId(UUIDUtils.getUUID());
        clue.setCreateTime(DateUtils.formateDateTime(new Date()));
        clue.setCreateBy(user.getId());

        ReturnObject returnObject = new ReturnObject();
        try {
            //封装好了，就能调用service方法，添加线索
            int count = clueService.insertClue(clue);

            //判断有没有添加成功
            if (count > 0){
                //成功
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(count);
            }else {
                //失败
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

    /**
     * 根据传进来的id列表进行删除
     * @param
     * @return
     */
    @RequestMapping("/workbench/clue/deleteClue.do")
    public @ResponseBody Object deleteClue(String[] id){

        ReturnObject returnObject = new ReturnObject();
        try {

            //封装参数，已经封装了就能直接调用service方法
            int count = clueService.deleteClue(id);

            //判断有没有删除成功
            if (count > 0 ){
                //删除成功
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(count);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试");
            }

        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试");
        }

        return returnObject;

    }
}
