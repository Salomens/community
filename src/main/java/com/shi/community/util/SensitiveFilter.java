package com.shi.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 敏感词
 */
@Component
public class SensitiveFilter {

    public static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);
    //替换符
    public static final String REPLACEMENT= "***";
    //根节点
    private TrieNode rootNode = new TrieNode();

    @PostConstruct
    public void init(){
        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ){
                String keyword;
                while ((keyword = reader.readLine()) != null){
                    this.addKeyWord(keyword);
                }
        }catch (IOException e){
            logger.error("加载敏感词失败" + e.getMessage());
        }

    }

    private void addKeyWord(String keyword) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode subNoes = tempNode.getSubNoes(c);
            if (subNoes == null){
               //初始化子节点
               subNoes = new TrieNode();
               tempNode.addSubNodes(c,subNoes);
            }
            //指向子节点,进行下一轮循环
            tempNode = subNoes;
            //设置结束标志
            if (i==keyword.length()-1){
                tempNode.setKeyWordEnd(true);
            }
        }
    }
    /**
    *过滤敏感词
     */
    public String filter(String text){
        if (StringUtils.isBlank(text)){
            return null;
        }
        //指针1 指向树
        TrieNode tempNode = rootNode;
        //开始指针
        int begin = 0;
        //
        int position = 0;
        //结果
        StringBuffer sb = new StringBuffer();
        while (position <text.length()){
            char c = text.charAt(position);
            //跳过符号
            if (isSymble(c)){
                //若指针1处于根节点,将此节点计入结果,然后让指针2向后走一步
                if (tempNode == rootNode){
                    sb.append(c);
                    begin++;
                }
                //无论如何指针3走一步
                position++;
                continue;

            }
            //检查下级节点
            tempNode = tempNode.getSubNoes(c);
            if (tempNode == null){
                //以begin开头的字符串不是敏感
                sb.append(text.charAt(begin));
                position = ++begin;
                //重新指向根节点
                tempNode = rootNode;
            }else if (tempNode.isKeyWordEnd()){
                sb.append(REPLACEMENT);
                begin = ++position;
                //重新指向根节点
                tempNode = rootNode;
            }else {
                position++;
            }
        }
        sb.append(text.substring(begin));
        return sb.toString();
    }
    //判断是否为符号
    public boolean isSymble(Character c){
        return !CharUtils.isAsciiAlphanumeric(c)&&(c<0x2e80||c>0x9fff);
    }

    /**
     * 一个内部类,定义前缀树
     */
    private class TrieNode{
        //标志是否为敏感词末尾{关键词结束标志}
    private boolean isKeyWordEnd = false;
    //子节点 key为下级字符 value为下级节点
    private Map<Character,TrieNode> subNodes = new HashMap<>();


        public boolean isKeyWordEnd() {
            return isKeyWordEnd;
        }

        public void setKeyWordEnd(boolean keyWordEnd) {
            isKeyWordEnd = keyWordEnd;
        }
        //添加子节点的方法
        public void addSubNodes(Character c, TrieNode node){
            subNodes.put(c,node);
        }
        //获取子节点
        public TrieNode getSubNoes(Character c){
            return subNodes.get(c);
        }
    }

}
