package com.siwuxie095.spring.chapter10th.example6th.domain;

/**
 * @author Jiajing Li
 * @date 2021-02-23 20:55:46
 */
@SuppressWarnings("all")
public class Spitter {

    private Long id;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private boolean updateByEmail;

    public Spitter() {
    }

    public Spitter(Long id, String username, String password, String fullName, String email, boolean updateByEmail) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.updateByEmail = updateByEmail;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isUpdateByEmail() {
        return updateByEmail;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUpdateByEmail(boolean updateByEmail) {
        this.updateByEmail = updateByEmail;
    }
}
