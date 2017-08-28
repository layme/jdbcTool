package com.jdbctool.test;

import com.jdbctool.dbcon.DbConnection;

import java.sql.Connection;

public class Test {
    public static void main(String[] args) {
        DbConnection c = new DbConnection();
        c.getConnection();
    }
}
