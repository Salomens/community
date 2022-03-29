package com.shi.community.service;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.shi.community.dao.DiscussPostMapper;
import com.shi.community.entity.DiscussPost;
import com.shi.community.util.SensitiveFilter;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class DiscussPostService {
    //service层调用数据库层面查询字段
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;
    @Value("${caffeine.posts.max-size}")
    private int maxSize;
    @Value("${caffeine.posts.expire-seconds}")
    private int expireSeconds;
    //caffeine核心接口 cache

    //帖子列表缓存
    private LoadingCache<String ,List<DiscussPost>>  postListCache;

    //帖子总数的缓存
    private LoadingCache<Integer,Integer> postRowsCache;
    @PostConstruct
    public void init(){
        //初始化帖子列表缓存
        postListCache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<String, List<DiscussPost>>() {
                    @Override
                    public @Nullable List<DiscussPost> load(@NonNull String key) throws Exception {
                        if (key == null || key.length() == 0){
                            throw new IllegalArgumentException("参数为空");
                        }
                        String[] params = key.split(":");
                        if (params == null || params.length != 2){
                            throw new IllegalArgumentException("参数为空");
                        }
                        Integer offset = Integer.valueOf(params[0]);
                        Integer limit = Integer.valueOf(params[1]);

                        //或者改成先访问redis后访问数据库,即redis作为一个二级缓存
                        return discussPostMapper.selectDiscussPost(0,offset,limit,1);
                    }
                });
        //初始化帖子总数缓存
        postRowsCache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<Integer, Integer>() {
                    @Override
                    public @Nullable Integer load(@NonNull Integer key) throws Exception {

                        return discussPostMapper.selectDiscussPostRows(key);
                    }
                });
    }

    private static final Logger logger = LoggerFactory.getLogger(DiscussPostService.class);


    public List<DiscussPost> findDiscussPost(int id, int offset, int limit,int orderMode){
        if (id == 0 && orderMode == 1){
            //直接从缓存中去
             return postListCache.get(offset + ":" + limit);
        }
        logger.debug("从数据库查数据了");
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPost(id, offset, limit,orderMode);
        return discussPosts;
    }

    public int findDiscussPostRows(int userId){
        if (userId == 0){
            return postRowsCache.get(userId);
        }
        logger.debug("数据库取rows");
        return discussPostMapper.selectDiscussPostRows(userId);
    }
    //添加帖子
    public int addDiscussPost(DiscussPost post){
        if (post == null){
            throw new IllegalArgumentException("参数不能为空");
        }
        //转义html标记
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        //过滤敏感词
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));
        return discussPostMapper.insertDiscussPost(post);
    }
    public DiscussPost findDiscussPostById(int id){
        return discussPostMapper.selectDiscussPostNById(id);
    }

    public int updateCommentCount(int id, int commentCount){
        return discussPostMapper.updateCommentCount(id,commentCount);
    }
    public int updateType(int id,int type){
        return discussPostMapper.updateType(id, type);
    }
    public int updateStatus(int id,int status){
        return discussPostMapper.updateStatus(id, status);
    }
    public int updateScore(int id,double score){
        return discussPostMapper.updateScore(id, score);
    }

}
