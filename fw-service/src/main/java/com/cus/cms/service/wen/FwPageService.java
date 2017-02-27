package com.cus.cms.service.wen;


import com.cus.cms.common.model.wen.FwContent;
import com.cus.cms.common.model.wen.FwPage;
import com.cus.cms.common.util.BlankUtil;
import com.cus.cms.common.util.DateTimeUtil;
import com.cus.cms.dao.wen.FwContentDao;
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
    @Autowired
    private FwContentDao fwContentDao;

    public long getFwPageCount(String title, String oneDir, int status) {
        return fwPageDao.queryFwPageCount(title, oneDir, status);
    }

    public List<FwPage> getFwPages(String title, String oneDir, int status, int page, int size){
        if (page < 1 || size < 0) {
            throw new IllegalArgumentException("page cann't less than 1 or size cann't less than 0");
        }
        int offset = (page - 1) * size;
        return fwPageDao.queryFwPages(title, oneDir, status, offset, size);
    }

    public int saveFwPage(FwPage fwPage, boolean isEdit){
        if(isEdit) {
            fwPage.setCreateTime(DateTimeUtil.getCurrentTime());
            fwPageDao.updateByKey(fwPage);
            //保存详情内容
            fwContentDao.updateByKey(buildFwContent(fwPage.getId(), fwPage.getContent()));
        }else{
            long id = snowFlake.nextId();
            fwPage.setStatus(1);
            fwPage.setId(id);
            fwPageDao.save(fwPage, WriteConcern.ACKNOWLEDGED);
            //保存详情内容
            fwContentDao.save(buildFwContent(id,fwPage.getContent()), WriteConcern.ACKNOWLEDGED);
        }
        return 1;
    }


    public FwPage getFwPageById(long id){
        return fwPageDao.get(id);
    }

    public FwContent getFwContentById(long id) {
        return fwContentDao.get(id);
    }

    public WriteResult delFwPageById(long id){
        fwPageDao.deleteById(id);
        return fwContentDao.deleteById(id);
    }

    public UpdateResults updateStatus(long id, int status) {
        return fwPageDao.updateStatusByKey(id, status);
    }

    public List<FwPage> getAllFwPage() {
        return fwPageDao.find().asList();
    }


    private FwContent buildFwContent(long id, String content) {
        FwContent fwContent = new FwContent();
        fwContent.setId(id);
        fwContent.setContent(content);
        return fwContent;
    }

}
