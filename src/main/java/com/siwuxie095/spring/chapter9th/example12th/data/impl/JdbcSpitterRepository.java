package com.siwuxie095.spring.chapter9th.example12th.data.impl;

import com.siwuxie095.spring.chapter9th.example12th.Spitter;
import com.siwuxie095.spring.chapter9th.example12th.data.SpitterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Jiajing Li
 * @date 2021-02-19 08:22:38
 */
@SuppressWarnings("all")
@Repository
public class JdbcSpitterRepository implements SpitterRepository {

    private JdbcOperations jdbc;

    @Autowired
    public JdbcSpitterRepository(JdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Spitter save(Spitter spitter) {
        jdbc.update(
                "insert into Spitter (username, password, first_name, last_name, email)" +
                        " values (?, ?, ?, ?, ?)",
                spitter.getUsername(),
                spitter.getPassword(),
                spitter.getFirstName(),
                spitter.getLastName(),
                spitter.getEmail());
        return spitter; // TODO: Determine value for id
    }

    @Override
    public Spitter findByUsername(String username) {
        return jdbc.queryForObject(
                "select id, username, null, first_name, last_name, email from Spitter where username=?",
                new SpitterRowMapper(),
                username);
    }

    private static class SpitterRowMapper implements RowMapper<Spitter> {
        @Override
        public Spitter mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Spitter(
                    rs.getLong("id"),
                    rs.getString("username"),
                    null,
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"));
        }
    }

}
