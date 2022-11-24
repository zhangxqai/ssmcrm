package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Contacts;
import com.bjpowernode.crm.workbench.domain.Transaction;
import com.bjpowernode.crm.workbench.mapper.ActivityMapper;
import com.bjpowernode.crm.workbench.mapper.ContactsMapper;
import com.bjpowernode.crm.workbench.service.CustomerService;
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

}
