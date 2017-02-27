package com.cus.cms.service.wen;


import com.cus.cms.common.model.wen.FwDir;
import com.cus.cms.common.util.BlankUtil;
import com.cus.cms.dao.wen.FwDirDao;
import com.cus.cms.service.BaseService;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Andy
 * @description
 */
@Service("fwDirService")
public class FwDirService extends BaseService {
    
    @Autowired
    private FwDirDao fwDirDao;


    /**
     *
     * @param name
     * @return
     */
    public List<FwDir> getFwDirs(String name, String oneDir, int dirType, int page, int size){
        if (page < 1 || size < 0) {
            throw new IllegalArgumentException("page cann't less than 1 or size cann't less than 0");
        }
        int offset = (page - 1) * size;
        return fwDirDao.queryFwDirs(name, oneDir, dirType,  offset, size);
    }

    public long getFwDirCount(String name, String oneDir, int dirType) {
        return fwDirDao.queryFwDirCount(name, oneDir, dirType);
    }

    public List<FwDir> getParentFwDirs(){
        return fwDirDao.queryParentFwDirs();
    }

    public int saveFwDir(FwDir fwDir,boolean isEdit){
        if(isEdit) {
            fwDir.setId(fwDir.getId());
            fwDirDao.updateByKey(fwDir);
        }else{
            fwDir.setStatus(1);
            fwDir.setId(snowFlake.nextId());
            fwDirDao.save(fwDir, WriteConcern.ACKNOWLEDGED);
        }
        return 1;
    }

    public FwDir getFwDirById(long id){
        return fwDirDao.get(id);
    }

    public WriteResult delFwDirById(long id){
        return fwDirDao.deleteById(id);
    }


    public List<FwDir> getOneDirs() {
        return fwDirDao.queryParentFwDirs();
    }

    public List<FwDir> getTwoDirByOne(String oneDir) {
        return fwDirDao.queryFwDirByParent(oneDir);
    }
}
