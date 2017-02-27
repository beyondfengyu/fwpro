package com.cus.cms.service.system;


import com.cus.cms.common.model.system.AdminDict;
import com.cus.cms.dao.system.AdminDictDao;
import com.cus.cms.service.BaseService;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Andy
 * @description
 */
@Service("adminDictService")
public class AdminDictService extends BaseService {

    @Autowired
    private AdminDictDao adminDictDao;


    /**
     * 分页查询Dict
     *
     * @param text Dict名称
     * @param page
     * @param size
     * @return
     */
    public List<AdminDict> getDictWithPage(String text, int page, int size) {
        if (page < 1 || size < 0) {
            throw new IllegalArgumentException("page cann't less than 1 or size cann't less than 0");
        }
        int offset = (page - 1) * size;
        return adminDictDao.queryDictWithPage(text, offset, size);
    }

    public long getCountWithPage(String text) {
        return adminDictDao.queryDictWithCount(text);
    }

    public int saveDict(AdminDict adminDict, boolean isEdit) {
        if (isEdit) {
            adminDictDao.updateByKey(adminDict);
        } else {
            adminDict.setId(snowFlake.nextId());
            adminDictDao.save(adminDict, WriteConcern.ACKNOWLEDGED);
        }
        return 1;
    }

    public AdminDict getAdminDictById(long id) {
        return adminDictDao.get(id);
    }

    public WriteResult delDictById(long id) {
        return adminDictDao.deleteById(id);
    }

    public List<AdminDict> getAllDict() {
        return adminDictDao.find().asList();
    }

    public List<AdminDict> getDictByCode(String code) {
        return adminDictDao.queryDictByCode(code);
    }
}
