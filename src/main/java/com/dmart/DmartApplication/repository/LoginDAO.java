package com.dmart.DmartApplication.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LoginDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean validateUser(String username, String password) {

        String sql = "SELECT COUNT(*) FROM cashier WHERE username = ? AND password = ?";

        Integer count = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                username,
                password);

        return count != null && count > 0;
    }
}