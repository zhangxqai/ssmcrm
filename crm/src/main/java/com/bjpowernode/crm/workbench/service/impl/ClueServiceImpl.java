package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.mapper.ClueMapper;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("clueService")
public class ClueServiceImpl implements ClueService {

    @Autowired
    private ClueMapper clueMapper;

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
        Clue clue = clueMapper.selectForEditById((String) map.get("id"));

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

        //到这里就说明创建客户的信息收集好了，就要调用service方法进行添加了
    }


}
