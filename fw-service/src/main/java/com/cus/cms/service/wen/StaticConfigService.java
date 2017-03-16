package com.cus.cms.service.wen;


import com.cus.cms.common.model.wen.StaticConfig;
import com.cus.cms.dao.wen.StaticConfigDao;
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
@Service("staticConfigService")
public class StaticConfigService extends BaseService {

    @Autowired
    private StaticConfigDao staticConfigDao;


    /**
     * 分页查询Config
     *
     * @param text Config名称
     * @param page
     * @param size
     * @return
     */
    public List<StaticConfig> getConfigWithPage(String text, int page, int size) {
        if (page < 1 || size < 0) {
            throw new IllegalArgumentException("page cann't less than 1 or size cann't less than 0");
        }
        int offset = (page - 1) * size;
        return staticConfigDao.queryConfigWithPage(text, offset, size);
    }

    public long getCountWithPage(String text) {
        return staticConfigDao.queryConfigWithCount(text);
    }

    public int saveConfig(StaticConfig adminConfig, boolean isEdit) {
        if (isEdit) {
            staticConfigDao.updateByKey(adminConfig);
        } else {
            adminConfig.setId(snowFlake.nextId());
            staticConfigDao.save(adminConfig, WriteConcern.ACKNOWLEDGED);
        }
        return 1;
    }

    public StaticConfig getStaticConfigById(long id) {
        return staticConfigDao.get(id);
    }

    public WriteResult delConfigById(long id) {
        return staticConfigDao.deleteById(id);
    }

    public List<StaticConfig> getAllConfig() {
        return staticConfigDao.find().asList();
    }

    public List<StaticConfig> getConfigByCode(String code) {
        return staticConfigDao.queryConfigByCode(code);
    }
}
