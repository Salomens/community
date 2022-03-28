package com.shi.community.quartz;

import com.shi.community.entity.DiscussPost;
import com.shi.community.service.DiscussPostService;
import com.shi.community.service.LikeService;
import com.shi.community.util.CommunityConstant;
import com.shi.community.util.RedisKeyUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PostScoreRefreshJob implements Job, CommunityConstant {
    private static final Logger logger = LoggerFactory.getLogger(PostScoreRefreshJob.class);
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private LikeService likeService;

    private static final Date epoch;
    static {
        try {
            epoch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-03-28 00:00:00");
        } catch (ParseException e) {
            throw new IllegalArgumentException("初始化时间失败") ;
            //e.printStackTrace();
        }
    }
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String redisKey = RedisKeyUtil.getPostScoreKey();
        BoundSetOperations operations = redisTemplate.boundSetOps(redisKey);
        if (operations.size() == 0){
            logger.info("[没有需要刷新的帖子]");
            return;
        }
        logger.info("任务开始,刷新帖子分数" + operations.size());
        while (operations.size()>0){
            this.refresh((Integer)operations.pop());
        }
        logger.info("任务结束帖子分数完毕");

    }

    private void refresh(int postId) {
        DiscussPost post = discussPostService.findDiscussPostById(postId);
        if (post == null){
            logger.info("帖子不存在");
            return;
        }
        //是否精华
        boolean wonderful = post.getStatus() == 1;
        //评论数量
        int commentCount = post.getCommentCount();
        //点赞数量
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST,postId);
        //计算权重
        double w = (wonderful ? 75 : 0) + commentCount * 10 + likeCount * 2;
        //分数
        double score = Math.log10(Math.max(w,1)) + (post.getCreateTime().getTime() - epoch.getTime())/(1000*3600*24);
        discussPostService.updateScore(postId,score);
    }

}
