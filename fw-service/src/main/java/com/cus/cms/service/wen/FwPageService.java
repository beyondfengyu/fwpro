package com.cus.cms.service.wen;


import com.cus.cms.common.model.wen.FwPage;
import com.cus.cms.common.util.BlankUtil;
import com.cus.cms.common.util.DateTimeUtil;
import com.cus.cms.dao.wen.FwPageDao;
import com.cus.cms.service.BaseService;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.UpdateResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Andy  2015/12/26.
 * @description
 */
@Service("fwPageService")
public class FwPageService extends BaseService {
    
    @Autowired
    private FwPageDao fwPageDao;

    /**
     *
     * @param title
     * @return
     */
    public List<FwPage> getFwPages(String title, int status, int page, int size){
        if (page < 1 || size < 0) {
            throw new IllegalArgumentException("page cann't less than 1 or size cann't less than 0");
        }
        int offset = (page - 1) * size;
        return fwPageDao.queryFwPages(title, status, offset, size);
    }

    public long getFwPageCount(String title, int status) {
        return fwPageDao.queryFwPageCount(title, status);
    }

    public int saveFwPage(FwPage fwPage,boolean isEdit){
        if(isEdit) {
            fwPage.setCreateTime(DateTimeUtil.getCurrentTime());
            fwPageDao.updateByKey(fwPage);
        }else{
            fwPage.setStatus(1);
            fwPage.setId(snowFlake.nextId());
            fwPageDao.save(fwPage, WriteConcern.ACKNOWLEDGED);
        }
        return 1;
    }

    public FwPage getFwPageById(long id){
        return fwPageDao.get(id);
    }

    public WriteResult delFwPageById(long id){
        return fwPageDao.deleteById(id);
    }

    public UpdateResults updateStatus(long id, int status) {
        return fwPageDao.updateStatusByKey(id, status);
    }

    public List<FwPage> getAllFwPage() {
        return fwPageDao.find().asList();
    }

    public long getFwPageCount(String title, String oneDir) {
        if (BlankUtil.isBlank(oneDir)) {
            throw new IllegalArgumentException("param oneDir cann't be null");
        }
        if (!BlankUtil.isBlank(title)) {
            return fwPageDao.queryFwPageCount(FwPage.TITLE, title);
        }
        return fwPageDao.count(FwPage.ONE_DIR, oneDir);
    }

    public List<FwPage> getFwPages(String title, String oneDir, int page, int size){
        if (BlankUtil.isBlank(oneDir)) {
            throw new IllegalArgumentException("param oneDir cann't be null");
        }
        if (page < 1 || size < 0) {
            throw new IllegalArgumentException("page cann't less than 1 or size cann't less than 0");
        }
        int offset = (page - 1) * size;
        return fwPageDao.queryFwPages(title, oneDir, offset, size);
    }
}
