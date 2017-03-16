package com.cus.cms.service.wen;

import com.cus.cms.common.util.DateTimeUtil;
import com.cus.cms.dao.wen.FwContentDao;
import com.cus.cms.dao.wen.FwDirDao;
import com.cus.cms.dao.wen.FwPageDao;
import com.cus.cms.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Andy
 */
@Service("generHtmlService")
public class GenerHtmlService extends BaseService {

    @Autowired
    private FwDirDao fwDirDao;
    @Autowired
    private FwPageDao fwPageDao;
    @Autowired
    private FwContentDao fwContentDao;


    public String test() {
        return DateTimeUtil.getCurrentTime();
    }


}
