package com.shi.community.dao;

import org.springframework.stereotype.Repository;

@Repository
public class TestDaoImpl implements TestDao{
    @Override
    public String select() {
        return "null";
    }
}
