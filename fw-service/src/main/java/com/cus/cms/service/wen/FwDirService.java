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
 * @author Andy  2015/12/26.
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
    public List<FwDir> getFwDirs(String name, int page, int size){
        if (page < 1 || size < 0) {
            throw new IllegalArgumentException("page cann't less than 1 or size cann't less than 0");
        }
        int offset = (page - 1) * size;
        return fwDirDao.queryFwDirs(name, offset, size);
    }

    public long getFwDirCount(String name) {
        if (!BlankUtil.isBlank(name)) {
            return fwDirDao.count("name", name);
        }
        return fwDirDao.count();
    }

    public List<FwDir> getParentFwDirs(){
        return fwDirDao.queryParentFwDirs();
    }

    public int saveFwDir(FwDir fwDir,boolean isEdit){
        if(isEdit) {
            fwDirDao.updateByKey(fwDir);
        }else{
            fwDir.setStatus(1);
            fwDir.setId(new ObjectId().toHexString());
            fwDirDao.save(fwDir, WriteConcern.ACKNOWLEDGED);
        }
        return 1;
    }

    public FwDir getFwDirById(String id){
        return fwDirDao.get(id);
    }

    public WriteResult delFwDirById(String id){
        return fwDirDao.deleteById(id);
    }


    public List<FwDir> getAllFwDir() {
        return fwDirDao.find().asList();
    }
}
