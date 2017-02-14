package com.cus.cms.crawler;

import com.cus.cms.crawler.model.FwSeed;
import com.cus.cms.crawler.model.LinkMapper;
import com.cus.cms.crawler.util.DbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Andy
 */
public class UrlTask extends TimerTask{
    private static final Logger logger = LoggerFactory.getLogger(UrlTask.class);

    private final String sql = "select * from fw_url where site_type=? and id>? and status=1 and link_type=1 order by id asc limit 0,1";
    private Map<Integer,LinkedBlockingQueue<List<LinkMapper>>> queueMap;
    private FwSeed fwSeed;
    private SiteService siteService;


    public UrlTask(FwSeed fwSeed) {
        this.fwSeed = fwSeed;
        this.siteService = new SiteService();
        this.queueMap = new HashMap<>();
    }

    @Override
    public void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(fwSeed.getThread() * 2);
        final AtomicInteger currId = new AtomicInteger(fwSeed.getStartId());

        for(int i = 0; i < fwSeed.getThread(); i++) {
            LinkedBlockingQueue<List<LinkMapper>> dbQueue = new LinkedBlockingQueue<>();
            queueMap.put(i, dbQueue);
        }

        // 启动多个线程同时工作
        for(int i = 0; i < fwSeed.getThread(); i++) {
            final LinkedBlockingQueue<List<LinkMapper>> dbQueue = queueMap.get(i);
            final int index = i;

            // 爬取网页数据
            executorService.execute(new Runnable() {
                private int mid = currId.get() + index;

                @Override
                public void run() {
                    Connection conn = DbUtil.createConn();
                    PreparedStatement preStatement = null;
                    ResultSet rs = null;
                    String linkUrl = null;
                    try {
                        while (true) {
                            // 获取当前需要爬取数据的网页链接
                            preStatement = conn.prepareStatement(sql);
                            preStatement.setInt(1, fwSeed.getSiteType());
                            preStatement.setInt(2, mid);
                            rs = preStatement.executeQuery();
                            if (!rs.next()) {
                                TimeUnit.SECONDS.sleep(5);
                                continue;
                            }

                            linkUrl = rs.getString("link_url");
                            mid = rs.getInt("id") + index;
                            logger.info("current fw_url ====>> mid is: {}, linkUrl is: {}",mid, linkUrl);

                            rs.close();
                            preStatement.close();

                            // 通过爬虫爬取当前网页的所有链接元素
                            List<LinkMapper> hrefs = CrawService.getAllATag(linkUrl, fwSeed.getSiteLink(), fwSeed.getSiteType());
                            dbQueue.put(hrefs);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        if(conn!=null){
                            DbUtil.closeConn(conn);
                        }
                    }
                }
            });


            // 执行插入DB工作
            executorService.execute(new Runnable() {

                @Override
                public void run() {
                    logger.info("invoke batchInsert method run start ======");
                    while (true) {
                        try {
                            List<LinkMapper> mappers = dbQueue.take();
                            int result = siteService.batchInsert(mappers);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }


    }
}
