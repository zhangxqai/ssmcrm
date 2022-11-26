package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.mapper.ActivityMapper;
import com.bjpowernode.crm.workbench.mapper.ContactsMapper;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TransactionHistoryService;
import com.bjpowernode.crm.workbench.service.TransactionRemarkService;
import com.bjpowernode.crm.workbench.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private ContactsMapper contactsMapper;

    @Autowired
    private TransactionRemarkService transactionRemarkService;

    @Autowired
    private TransactionHistoryService transactionHistoryService;

    @RequestMapping("/workbench/transaction/selectTranSanction.do")
    public @ResponseBody Object selectTranSanction(String owner, String name, String customerId, String stage, String type, String source, String contactsId, int pageNo, int pageSize){
        //收集数据，并封装到map中，
        Map<String,Object> map = new HashMap<>();

        map.put("owner",owner);
        map.put("name",name);
        map.put("customerId",customerId);
        map.put("stage",stage);
        map.put("type",type);
        map.put("source",source);
        map.put("contactsId",contactsId);
        map.put("beginNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);

        //封装好了之后，就要调用service方法，查询
        List<Transaction> transactionList = transactionService.selectTranSanctionAll(map);
        int totalRows = transactionService.selectTranSanctionAllCount(map);

        //查询回来的数据就封装到map中
        Map<String,Object> retMap = new HashMap<>();
        retMap.put("transactionList",transactionList);
        retMap.put("totalRows",totalRows);

        return retMap;
    }

    @RequestMapping("/workbench/transaction/editCreateTran.do")
    public Object editCreateTran(HttpServletRequest request){

        //查询下拉列表的信息
        List<User> userList = userService.queryAllUsers();
        List<DicValue> stageList = dicValueService.selectDicValue("stage");
        List<DicValue> transactionTypeList = dicValueService.selectDicValue("transactionType");
        List<DicValue> sourceList = dicValueService.selectDicValue("source");

        //将下拉列表的信息封装到request中
        request.setAttribute("userList",userList);
        request.setAttribute("stageList",stageList);
        request.setAttribute("transactionTypeList",transactionTypeList);
        request.setAttribute("sourceList",sourceList);

        //通过转发返回页面
        return "workbench/transaction/save";
    }
    @RequestMapping("/workbench/transaction/getPossibilityByStage.do")
    public @ResponseBody Object getPossibilityByStage(String stageValue){
        //解析properties文件
       /* ResourceBundle resourceBundle = ResourceBundle.getBundle("possibility");
        String properties = resourceBundle.getString(stageValue);

        //查询到了就返回数据
        return properties;*/

        //这个不知道为什么就是一直找不到key，要将properties文件中的第一行数据空出来才行
        //可以将properties文件的第一行的空，删掉就会出现找不到key的问题
        //https://www.ngui.cc/el/1284842.html?action=onClick
        //参考了这个网站就找到这个问题了
        ResourceBundle bundle= ResourceBundle.getBundle("possibility");
        String possibility=bundle.getString(stageValue);
        return possibility;
    }

    @RequestMapping("/workbench/transaction/selectCustomerById.do")
    public @ResponseBody Object selectCustomerById(String customerName){
        //直接调用方法模糊查询客户名称就可以了
        List<String> customerNameList = customerService.selectCustomerById(customerName);

        return customerNameList;
    }

    @RequestMapping("/workbench/transaction/insertTransactionOne.do")
    //第一步封装参数，在前端会将参数传进来，可以通过实体类来接受参数，也可以一个一个收集参数，但是这样都太麻烦了
    //可以通过map直接接受参数，这个是可以的，但是需要在参数前方添加
    public @ResponseBody Object insertTransactionOne(@RequestParam Map<String,Object> map , HttpSession session){

        //第一步封装数据，由于前端传来的数据都直接封装到map中了，还有一个用户的需要封装
        map.put(Contants.SESSION_USER,session.getAttribute(Contants.SESSION_USER));

        ReturnObject returnObject = new ReturnObject();
        try{
            //封装好就要调用service层
            //这里调用之后可能会出错，就要try一下
            transactionService.insertTransactionOne(map);

            //如果走到这里就说明没有问题，就返回封装成功的数据
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);

        }catch (Exception e){
            e.printStackTrace();
            //如果出现错误就会返会错误信息
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试...");
        }

    return returnObject;
    }

   /* @RequestMapping("/workbench/transaction/detailIndex.do")
    public String detailIndex(){
        return "workbench/transaction/index";
    }*/

    /**
     * 在添加交易时，根据名称查询市场活动源
     * @param name
     * @return
     */
    @RequestMapping("/workbench/transaction/selectFroActivitySavaTranByName.do")
    public @ResponseBody Object selectFroActivitySavaTranByName(String name){
        //收集数据，已经收集好了就调用service方法
        List<Activity> activityList = activityMapper.selectFroSavaTranByNameActivity(name);
        return activityList;
    }

    @RequestMapping("/workbench/transaction/selectForContactsSaveTranByName.do")
    public @ResponseBody Object selectForContactsSaveTranByName(String fullname){
        //收集数据，已经收集好了就调用service方法
        List<Contacts> contactsList = contactsMapper.selectFroSavaTranByNameContacts(fullname);
        return contactsList;
    }

    @RequestMapping("/workbench/transaction/selectForDetailById.do")
    public String selectForDetailById(String id,HttpServletRequest request){

        //收集数据没有数据就直接调用service方法
        Transaction transaction = transactionService.selectForDetailById(id);

        //还有收集备注信息的全部内容
        List<TransactionRemark> transactionRemarkList = transactionRemarkService.selectForDetailTranRemarkById(transaction.getId());
        //还有收集交易历史的全部内容
        List<TransactionHistory> transactionHistoryList = transactionHistoryService.selectForTranHistoryById(transaction.getId());

        //还有将可能性查出来，怎么查，第一步通过 ResourceBundle.getBundle("")方法解析获取一个 ResourceBundle
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        //第二步，将返回来的阶段传进去获取阶段
        String possibility = bundle.getString(transaction.getStage());
        //第三步将这个可能性存到request中，或者存到transaction实体类中，要是存到transaction中需要在实体类中添加一个属性
        request.setAttribute("possibility",possibility);

        //查询阶段，为了显示图标
        List<DicValue> stageList = dicValueService.selectDicValue("stage");

        request.setAttribute("stageList",stageList);

        //绑定获取到的数据
        request.setAttribute("transaction",transaction);
        request.setAttribute("transactionRemarkList",transactionRemarkList);
        request.setAttribute("transactionHistoryList",transactionHistoryList);

        //跳转页面
        return "workbench/transaction/detail";
    }

    @RequestMapping("/workbench/transaction/updateTransactionRemark.do")
    public @ResponseBody Object updateTransactionRemark(String noteContent,String tranId,HttpSession session){

        User user = (User) session.getAttribute(Contants.SESSION_USER);
        //收集数据
        TransactionRemark transactionRemark = new TransactionRemark();
        transactionRemark.setId(UUIDUtils.getUUID());
        transactionRemark.setNoteContent(noteContent);
        transactionRemark.setCreateBy(user.getId());
        transactionRemark.setCreateTime(DateUtils.formateDateTime(new Date()));
        transactionRemark.setEditFlag(Contants.REMARK_EDIT_FLAG_NO_EDITED);
        transactionRemark.setTranId(tranId);

        ReturnObject returnObject = new ReturnObject();
        try{
            //收集好了数据，就调用service方法
            int count = transactionRemarkService.insertTransactionRemark(transactionRemark);

            //判断有没有添加成功
            if (count > 0){
                //封装数据
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(transactionRemark);
            }else {
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

    @RequestMapping("/workbench/transaction/deleteTransactionRemark.do")
    public @ResponseBody Object deleteTransactionRemark(String id){

        ReturnObject returnObject = new ReturnObject();
        try {
            //收集数据，没有数据就先调用service方法
            int count = transactionRemarkService.deleteTransactionRemark(id);

            //判断有没有删除成功
            if (count >0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
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

    @RequestMapping("/workbench/transaction/updateForTransactionRemark.do")
    public @ResponseBody Object updateForTransactionRemark(String id,String noteContent,HttpSession session){

        User user = (User) session.getAttribute(Contants.SESSION_USER);
        //收集数据
        TransactionRemark transactionRemark = new TransactionRemark();
        transactionRemark.setId(id);
        transactionRemark.setNoteContent(noteContent);
        transactionRemark.setEditBy(user.getId());
        transactionRemark.setEditTime(DateUtils.formateDateTime(new Date()));
        transactionRemark.setEditFlag(Contants.REMARK_EDIT_FLAG_YES_EDITED);

        ReturnObject returnObject = new ReturnObject();
        try {
            //封装好数据就要调用service方法
            int count = transactionRemarkService.updateTransactionRemark(transactionRemark);

            //判断
            if (count > 0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(transactionRemark);
            }else {
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
