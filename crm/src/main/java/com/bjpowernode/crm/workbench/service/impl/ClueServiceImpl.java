package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.mapper.DicValueMapper;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.mapper.*;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("clueService")
public class ClueServiceImpl implements ClueService {

    @Autowired
    private ClueMapper clueMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ContactsMapper contactsMapper;

    @Autowired
    private ClueRemarkMapper clueRemarkMapper;

    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;

    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;

    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private TransactionHistoryMapper transactionHistoryMapper;

    @Autowired
    private DicValueMapper dicValueMapper;

    /**
     * 这个是查询符合条件的所有列表
     * @param map
     * @return
     */
    @Override
    public List<Clue> selectActivityAll(Map<String, Object> map) {
        //调用service方法，查询所有符合条件的列表
        List<Clue> clueList = clueMapper.selectClueAll(map);
        return clueList;
    }

    /**
     * 这个是查询符合条件的有多少条数
     * @param map
     * @return
     */
    @Override
    public int selectClueAllCount(Map<String, Object> map) {
        //调用service方法，查询所有符合条件的列表条数
        int count = clueMapper.selectClueAllCount(map);
        return count;
    }

    /**
     * 创建线索
     * @param clue
     * @return
     */
    @Override
    public int insertClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }

    /**
     * 根据传进来的id列表进行删除
     * @param ids
     * @return
     */
    @Override
    public int deleteClue(String[] ids) {
        int count = clueMapper.deleteClue(ids);
        return count;
    }

    /**
     * 根据id对线索进行修改
     * @param clue
     * @return
     */
    @Override
    public int updateClueById(Clue clue) {
        return clueMapper.updateClueById(clue);
    }

    @Override
    public Clue selectByIdForEdit(String id) {
        return clueMapper.selectByIdForEdit(id);
    }

    /**
     * 跳转线索详细信息表
     * @param id
     * @return
     */
    @Override
    public Clue selectForEditById(String id) {
        return clueMapper.selectForEditById(id);
    }

    /**
     * 根据id为转换查询线索信息
     * @param id
     * @return
     */
    @Override
    public Clue selectForConVersionById(String id) {
        return clueMapper.selectForConVersionById(id);
    }

    @Override
    public void insertCustomerAndContacts(Map<String, Object> map) {

        //先找出有关这条id的线索
        Clue clue = clueMapper.selectForEditById((String) map.get("clueId"));

        User user = (User) map.get(Contants.SESSION_USER);
        //找到线索之后，就要创建客户
        //创建客户之前就要先收集数据
        //先创建一个客户的实体类，用来封装数据
        Customer customer = new Customer();
        customer.setId(UUIDUtils.getUUID());
        customer.setOwner(user.getId());
        customer.setName(clue.getCompany());
        customer.setWebsite(clue.getWebsite());
        customer.setPhone(clue.getPhone());
        customer.setCreateBy(user.getId());
        customer.setCreateTime(DateUtils.formateDateTime(new Date()));
        customer.setContactSummary(clue.getContactSummary());
        customer.setNextContactTime(clue.getNextContactTime());
        customer.setAddress(clue.getAddress());
        customer.setDescription(clue.getDescription());

        //到这里就说明创建客户的信息收集好了，就要调用mapper方法进行添加了
        customerMapper.insertCustomer(customer);

        //到这里就说明创建好了客户，接下来就创建联系人
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtils.getUUID());
        contacts.setOwner(user.getId());
        contacts.setSource(clue.getSource());
        contacts.setCustomerId(customer.getId());
        contacts.setFullname(clue.getFullname());
        contacts.setAppellation(clue.getAppellation());
        contacts.setEmail(clue.getEmail());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setCreateBy(user.getId());
        contacts.setCreateTime(DateUtils.formateDateTime(new Date()));
        contacts.setDescription(clue.getDescription());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setAddress(clue.getAddress());

        //封装好参数，就掉用mapper方法
        contactsMapper.insertContacts(contacts);

        //到这里就说明联系人添加成功
        //还需要添加备注信息，添加之前需要将备注信息查询出来
        List<ClueRemark> clueRemarkList = clueRemarkMapper.selectForConversionClueRemark(clue.getId());

        //对列表进行判断有没有线索，如果有就要进行遍历
        if (clueRemarkList !=null && clueRemarkList.size() > 0 ){

            ContactsRemark contactsRemark = null;
            List<ContactsRemark> contactsRemarkList = new ArrayList<>();
            //判断有数据，就要循环出来
            for (ClueRemark cr: clueRemarkList) {
                //使用超强循环就可以了，循环出来一个封装一个
                contactsRemark = new ContactsRemark();
                contactsRemark.setId(UUIDUtils.getUUID());
                contactsRemark.setNoteContent(cr.getNoteContent());
                contactsRemark.setCreateBy(cr.getCreateBy());
                contactsRemark.setCreateTime(cr.getCreateTime());
                contactsRemark.setEditFlag(cr.getEditFlag());
                contactsRemark.setContactsId(contacts.getId());

                //到这里就说明这一条数据封装完成，之后就要放到list集合中
                contactsRemarkList.add(contactsRemark);
            }
            //到这里就说明封装完成,就能调用mapper层方法
            contactsRemarkMapper.insertContactsRemark(contactsRemarkList);

        }

        //现在对联系人的备注进行关联，封装前需要先查询,关联前需要封装数据
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationMapper.selectClueActivityRemarkForContacts(clue.getId());

        //查询好了之后就要将这些循环出来封装起来
        //先判断有没有数据
        if (clueActivityRelationList != null && clueActivityRelationList.size() > 0){

            ContactsActivityRelation contactsActivityRelation =null;
            List<ContactsActivityRelation> contactsActivityRelationList = new ArrayList<>();
            //有数据就要循环出来
            for (ClueActivityRelation car: clueActivityRelationList) {
                //新创建一个对象
                contactsActivityRelation = new ContactsActivityRelation();
                //创建好了之后将activityId放进去
                contactsActivityRelation.setId(UUIDUtils.getUUID());
                contactsActivityRelation.setContactsId(contacts.getId());
                contactsActivityRelation.setActivityId(car.getActivityId());

                //将一条数据封装好了之后，就要将这条数据封装到一个集合中
                contactsActivityRelationList.add(contactsActivityRelation);
            }

            //到这里就说明已经封装好集合中了，就能调用mapper层方法
            contactsActivityRelationMapper.insertContactsActivityRelation(contactsActivityRelationList);
        }

        //到这里就说明联系人的市场活动关联已经完成
        //接下来就看看是不是需要创建交易
        //先判断一下，如果不需要创建，程序到这里就可以删除线索中的数据，然后结束，如果需要创建交易，还需要创建交易信息
        //判断前端有没有勾选上选中框
        if("true".equals(map.get("isCreateTran"))){
            //这里要创建交易的
            //创建交易先收集数据
            String money = (String) map.get("money");
            String name = (String) map.get("name");
            String expectedDate = (String) map.get("expectedDate");
            String stage = (String) map.get("stage");
            String activityId = (String) map.get("activityId");

            //将这些数据先封装起来
            //封装之前需要创建一个实体类
            Transaction transaction = new Transaction();
            transaction.setId(UUIDUtils.getUUID());
            transaction.setOwner(user.getId());
            transaction.setName(name);
            transaction.setMoney(money);
            transaction.setCustomerId(customer.getId());
            transaction.setStage(stage);

            //因为这个是就创建的，就需要填上新的业务，那么就要去数据库中查这个新业务的信息，掉mapper层方法
            List<DicValue> dicValueList = dicValueMapper.selectDicValue("transactionType");

            //传回来的数据有很多个、、需要循环
            for (DicValue dic: dicValueList) {
                //在这里循环，还要判断一下
                //因为这个是新的业务，直接判断找出类型是新业务就行了
                if("新业务".equals(dic.getValue())){
                    transaction.setType(dic.getId());
                }
            }

            //由于在上面的查询线索中查出来的来源是文字的，不是id的，需要重新在查一下
            List<DicValue> dicValueListOne = dicValueMapper.selectDicValue("source");
            //传回来的数据有很多个、、需要循环
            for (DicValue dic: dicValueListOne) {
                //在这里循环，还要判断一下
                //因为这个是新的业务，直接判断找出类型是新业务就行了
                if(clue.getSource().equals(dic.getValue())){
                    transaction.setSource(dic.getId());
                }
            }

            transaction.setContactsId(contacts.getId());
            transaction.setActivityId(activityId);
            transaction.setCreateBy(user.getId());
            transaction.setCreateTime(DateUtils.formateDateTime(new Date()));
            transaction.setExpectedDate(expectedDate);

            //封装好了之后就要调用mapper层方法
            transactionMapper.insertTransaction(transaction);

            //创建好了交易信息之后就要将交易历史记录，因为这个是转换线索没有交易备注，所有不用管
            //创建交易历史先收集数据，收集数据之前需要创建一个实体类
            TransactionHistory transactionHistory = new TransactionHistory();
            //将这条信息封装到这个实体类中
            transactionHistory.setId(UUIDUtils.getUUID());
            transactionHistory.setCreateBy(user.getId());
            transactionHistory.setCreateTime(DateUtils.formateDateTime(new Date()));
            transactionHistory.setMoney(money);
            transactionHistory.setStage(stage);
            transactionHistory.setExpectedDate(expectedDate);
            transactionHistory.setTranId(transaction.getId());

            //封装好了之后，直接调用mapper层方法
            transactionHistoryMapper.insertTransactionHistory(transactionHistory);

        }

        //封装好了之后就要对这个线索进行删除，先删除哪些需要依赖线索的数据，就是先删除市场活动关联表，也可以先删除线索备注表
        clueActivityRelationMapper.deleteClueActivityRelationForConversionByClueId((String) map.get("clueId"));

        //现在对线索备注进行删除
        clueRemarkMapper.deleteClueRemarkById((String) map.get("clueId"));

        //然后对线索进行删除
        clueMapper.deleteFroConversionBuId((String) map.get("clueId"));
    }
}
