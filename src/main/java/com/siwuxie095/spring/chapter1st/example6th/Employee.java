package com.siwuxie095.spring.chapter1st.example6th;

import java.math.BigDecimal;

/**
 * 雇员、员工
 *
 * @author Jiajing Li
 * @date 2020-12-16 21:55:43
 */
@SuppressWarnings("all")
public class Employee {

    private long id;

    private String firstName;

    private String lastName;

    private BigDecimal salary;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

}
