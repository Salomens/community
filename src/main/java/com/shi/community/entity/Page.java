package com.shi.community.entity;

import lombok.ToString;

/**
 * 封装分页相关的信息
 */

@ToString
public class Page {
    //当前页码
    private int current = 1;
    //显示的上限
    private int limit = 10;
    //数据总数,用于计算总的页数
    private int rows;
    //查询路径 用来复用分页链接
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取当前页的卡起始行
     */
    public int getOffset(){
        return (current - 1)*limit;
    }

    /**
     * 获取总的页数
     * @return
     */
    public int getTotal(){
        if (rows % limit == 0){
            return rows / limit;
        }else {
            return rows / limit + 1;
        }
    }

    /**
     * 获取起始页码
     * @return
     */
    public int getFrom(){
        int from = current - 2;
        return from < 1 ? 1 : from;
    }
    /**
     * 获取结束页码
     * @return
     */
    public int getTo(){
        int To = current + 2;
        int total = getTotal();
        return To > total ? total : To;
    }
}
