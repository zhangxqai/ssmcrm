package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.HSSFUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@Controller
public class ActivityController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityRemarkService activityRemarkService;

    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){

        List<User> userList = userService.queryAllUsers();

        //将查询到的数据存到request中
        request.setAttribute("userList",userList);

        return "workbench/activity/index";
    }

    @RequestMapping("/workbench/activity/insertActivity.do")
    //由于需要传参数，所以需要加ResponseBody
    public @ResponseBody Object insertActivity(Activity activity , HttpSession session){

        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DateUtils.formateDateTime(new Date()));

        //获取当前登录的用户，在登录之前有将用户存到session作用域中
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        activity.setCreateBy(user.getId());

        //创建ReturnObject对象，将返回的数据封装到这个对象中
        ReturnObject returnObject = new ReturnObject();

        try {
            //调用service层，操作数据库，将数据存到数据库中
            int count = activityService.insertActivity(activity);

            if ( count > 0 ){
                //大于0就说明数据已经存到数据库中了
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);

            }else {
                //失败了说明数据没有存到数据库中
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙，请稍后重试...");

            }


        }catch (Exception e){
            e.printStackTrace();
        }

        return returnObject;
    }

    /**
     * 根据传入的数据进行查询出市场活动列表
     * @param name
     * @param owner
     * @param startDate
     * @param endDate
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/workbench/activity/selectActivityAll.do")
    public @ResponseBody Object selectActivityAll(String name,String owner,String startDate,String endDate ,int pageNo , int pageSize){

        //现将这些数据封装成mop
        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("beginNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);

        //调用service层，查询市场活动列表
        List<Activity> activityList = activityService.selectActivityAll(map);
        int totalRows = activityService.selectActivityAllCount(map);

        //将返回的数据封装成mop
        Map<String,Object> retMap = new HashMap<>();
        retMap.put("activityList",activityList);
        retMap.put("totalRows",totalRows);

        return retMap;

    }


    /**
     * 根据传进来的ids数组删除市场活动
     * @param id
     * @return
     */
    @RequestMapping("/workbench/activity/deleteActivityById.do")
    public @ResponseBody Object deleteActivityById(String[] id){

        ReturnObject returnObject = new ReturnObject();
        //由于这个是对数据库进行操作的是要进行事务管理的，如果出错了就不能在修改数据库，所以需要try catch
        try {
            //直接调用service层，对数据库进行删除处理
            int ret = activityService.deleteActivityById(id);

            //在判断返回的结果
            if (ret >0){
                //大于0，说明就是正常删除了
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                //等于0，就说明是没有删除成功就要返回数据并提示信息
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙，请稍后重试...");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后重试...");
        }

        return returnObject;
    }

    /**
     * 根据id查询市场活动列表一条
     * @param id
     * @return
     */
    @RequestMapping("/workbench/activity/selectActivityById.do")
    public @ResponseBody Object selectActivityById(String id){
        //调用service层，查询市场活动
        Activity activity = activityService.selectActivityById(id);
        return activity;
    }

    /**
     * 修改市场活动消息
     * @param activity
     * @param session
     * @return
     */
    @RequestMapping("/workbench/activity/updateActivityById.do")
    public @ResponseBody Object updateActivityById(Activity activity,HttpSession session){

        //谁是修改人，当前用户
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        //先将参数补齐
        activity.setEditBy(user.getId());
        activity.setEditTime(DateUtils.formateDateTime(new Date()));

        ReturnObject returnObject = new ReturnObject();

        //这个是操作数据库很危险，需要捕捉一下

        try{
            //调用service层，删除列表
            int ret = activityService.updateActivityById(activity);

            //判断有没有删除成功
            if (ret > 0 ){
                //大于0就是修改成功
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                //这个就是修改不成功，需要返回数据提醒用户
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙，请稍后重试！");
            }
        }catch (Exception e){
            e.printStackTrace();
            //这个就是修改不成功，需要返回数据提醒用户
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后重试！");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/selectAllActivityDow.do")
    public void selectAllActivityDow(HttpServletResponse response) throws IOException {

        //下载前第一步，先调service查询有多少条市场活动
        List<Activity> activityList = activityService.selectAllActivityDow();

        //查询好了之后就要，下载，下载之前就要创建一个exel文件，将这些记录填进去
        HSSFWorkbook wb = new HSSFWorkbook();
        //创建好exel文件之后就要创建页
        HSSFSheet sheet = wb.createSheet("市场活动列表");
        //创建好页之后，就要创建行
        HSSFRow row = sheet.createRow(0);
        //创建好行之后，就要创建好列
        HSSFCell cell = row.createCell(0);
        //列创建好之后，就可以先设置好列前的名称
        cell.setCellValue("ID");
        //第一列的第一行的名称创建好之后，就创建第二列的第二行
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("名称");
        cell = row.createCell(3);
        cell.setCellValue("开始日期");
        cell = row.createCell(4);
        cell.setCellValue("结束日期");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("描述");
        cell = row.createCell(7);
        cell.setCellValue("创建时间");
        cell = row.createCell(8);
        cell.setCellValue("创建者");
        cell = row.createCell(9);
        cell.setCellValue("修改时间");
        cell = row.createCell(10);
        cell.setCellValue("修改者");

        //创建好了之第一行之后就可以将查询出来的消息填到exel表中
        Activity activity = null;
        //将查询出来的市场活动遍历出来
        if (activityList !=null && activityList.size() >0){

            for (int i = 0 ; i < activityList.size() ; i++){
                //获取数组中的每一条市场活动列表
                activity = activityList.get(i);

                //获取好了之后，就直接在exel文件中生成一行
                row = sheet.createRow(i+1);

                //行创建好之后，就创建十一列
                cell = row.createCell(0);

                cell.setCellValue(activity.getId());
                cell = row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell = row.createCell(2);
                cell.setCellValue(activity.getName());
                cell = row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell = row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell = row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell = row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell = row.createCell(7);
                cell.setCellValue(activity.getCreateTime());
                cell = row.createCell(8);
                cell.setCellValue(activity.getCreateBy());
                cell = row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(activity.getEditBy());
            }
            //上面遍历完就说明市场活动就填写到exel文件中
        }

        //上面的处理好了之后，就要将文件下载到客户段上
        //下载之前还要设置文件格式
        response.setContentType("applitation/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=activityList.xls");
        //格式设置好了之后，就要设置输出流的
        OutputStream outputStream = response.getOutputStream();

        //创建好输出流之后就要将这些文件输出
        wb.write(outputStream);

        //输出好了之后就要关闭创建的文件
        wb.close();
        //创建的输出流也要刷新
        outputStream.flush();

    }

    /**
     * 导入市场活动列表
     * @return
     */
    @RequestMapping("/workbench/activity/insertActivityByList.do")
    public Object insertActivityByList(String userName , MultipartFile activityFile , HttpSession session){

        ReturnObject returnObject = new ReturnObject();
        try {
            //这个是获取文件名称的
            String originalFile = activityFile.getOriginalFilename();
            /*//把exel文件写道磁盘中
            File file = new File("F:\\crm_ssm\\serverDir",originalFile);

            //解析exel文件，获取文件中的数据，并封装为activityList
            InputStream is = new FileInputStream("F:\\crm_ssm\\serverDir"+originalFile);
*/
            InputStream is = activityFile.getInputStream();
            //这里就要创建exel文件
            HSSFWorkbook wb = new HSSFWorkbook(is);
            //创建完之后，就要获取文件中数据,获取页数，0就是第一页
            HSSFSheet sheet = wb.getSheetAt(0);
            //获取好页数就可以获取这一页中的数据，获取行
            HSSFRow row = null;
            HSSFCell cell = null;
            Activity activity = null;
            User user = (User) session.getAttribute(Contants.SESSION_USER);

            //创建一个市场活动数组
            List<Activity> activityList = new ArrayList<>();

            for (int i =1 ; i <= sheet.getLastRowNum() ; i++){//getLastRowNum是最后一行下表

                //获取第一行数据
                row = sheet.getRow(i);

                //每遍历出来一行就要封装一个实体类对象
                activity = new Activity();
                activity.setId(UUIDUtils.getUUID());
                activity.setOwner(user.getId());
                activity.setCreateBy(session.getId());
                activity.setCreateTime(DateUtils.formateDateTime(new Date()));

                //获取好一行之后就要获取列，列中好好多数据就要循环
                for (int j = 0 ;j< row.getLastCellNum();j++){//getLastCellNum是最后一列下表+1
                    //获取列
                    cell = row.getCell(j);
                    //获取列中的数据
                    String cellValue = HSSFUtils.getCellValueForStr(cell);
                    //获取好了之后就将数据封装到activity对象中
                    if(j ==0){
                        activity.setName(cellValue);
                    }else if (j == 1){
                        activity.setStartDate(cellValue);
                    }else if (j == 2){
                        activity.setEndDate(cellValue);
                    }else if (j == 3){
                        activity.setCost(cellValue);
                    }else if (j == 4){
                        activity.setDescription(cellValue);
                    }
                }
                //到这里就说明这一行的数据封装好了到对象中了，接下来就要将封装好的对象放到数组中去
                activityList.add(activity);
            }
            //到这里就说明封装好数组了，可以调用service方法
            int count = activityService.insertActivityByList(activityList);

            //在这里不需要判断，有try catch
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(count);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试！");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/selectActivityRemarkById.do")
    public Object selectActivityRemarkById(String id, HttpServletRequest request){
        //调用service方法查询，这个id的市场活动
        Activity activity = activityService.selectActivityById(id);
        //调用service方法查询，这个市场活动中的所有市场备注
        List<ActivityRemark> activityRemarkList = activityRemarkService.selectActivityRemarkById(id);

        //查询好了之后就要将这些数据封装在request中跳转页面
        request.setAttribute("activity",activity);
        request.setAttribute("activityRemarkList",activityRemarkList);

        //跳转页面
        return "workbench/activity/detail";
    }
}
