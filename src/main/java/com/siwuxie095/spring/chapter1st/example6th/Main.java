package com.siwuxie095.spring.chapter1st.example6th;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Jiajing Li
 * @date 2020-12-16 21:49:55
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 使用模板消除样板式代码
     *
     * 你是否写过这样的代码，当编写的时候总会感觉以前曾经这么写过？这不是似曾相识。这是样板式的代码（boilerplate code）。
     * 通常为了实现通用的和简单的任务，你不得不一遍遍地重复编写这样的代码。
     *
     * 遗憾的是，它们中的很多是因为使用 Java API 而导致的样板式代码。样板式代码的一个常见范例是使用 JDBC 访问数据库查询
     * 数据。举个例子，如果你曾经用过 JDBC，那么你或许会写出类似下面的代码。
     *
     *     public Employee getEmployeeById(long id) {
     *         Connection conn = null;
     *         PreparedStatement stmt = null;
     *         ResultSet rs = null;
     *         try {
     *             conn = dataSource.getConnection();
     *             stmt = conn.prepareStatement("select id, first_name, last_name, salary from " +
     *                     "employee where id = ?");
     *             stmt.setLong(1, id);
     *             rs = stmt.executeQuery();
     *             Employee employee = null;
     *             if (rs.next()) {
     *                 employee = new Employee();
     *                 employee.setId(rs.getLong("id"));
     *                 employee.setFirstName(rs.getString("first_name"));
     *                 employee.setLastName(rs.getString("last_name"));
     *                 employee.setSalary(rs.getBigDecimal("salary"));
     *             }
     *             return employee;
     *         } catch (SQLException e) {
     *             e.printStackTrace();
     *         } finally {
     *             if (rs != null) {
     *                 try {
     *                     rs.close();
     *                 } catch (SQLException e) {
     *                     e.printStackTrace();
     *                 }
     *             }
     *             if (stmt != null) {
     *                 try {
     *                     stmt.close();
     *                 } catch (SQLException e) {
     *                     e.printStackTrace();
     *                 }
     *             }
     *             if (conn != null) {
     *                 try {
     *                     conn.close();
     *                 } catch (SQLException e) {
     *                     e.printStackTrace();
     *                 }
     *             }
     *         }
     *         return null;
     *     }
     *
     * 正如你所看到的，这段 JDBC 代码查询数据库获得员工姓名和薪水。但是你很难把上面的代码逐行看完，这是因为少量查询员工的
     * 代码淹没在一堆 JDBC 的样板式代码中。首先你需要创建一个数据库连接，然后再创建一个语句对象，最后你才能进行查询。为了
     * 平息 JDBC 可能会出现的怒火，你必须捕捉 SQLException，这是一个检查型异常，即使它抛出后你也做不了太多事情。
     *
     * 最后，毕竟该说的也说了，该做的也做了，你不得不清理战场，关闭数据库连接、语句和结果集。同样为了平息 JDBC 可能会出现
     * 的怒火，你依然要捕捉 SQLException。
     *
     * 这段代码和你实现其他 JDBC 操作时所写的代码几乎是相同的。只有少量的代码与查询员工逻辑有关系，其他的代码都是 JDBC
     * 的样板代码。
     *
     * JDBC 不是产生样板式代码的唯一场景。在许多编程场景中往往都会导致类似的样板式代码，JMS、JNDI 和使用 REST 服务通常
     * 也涉及大量的重复代码。
     *
     * Spring 旨在通过模板封装来消除样板式代码。Spring 的 JdbcTemplate 使得执行数据库操作时，避免传统的 JDBC 样板代
     * 码成为了可能。
     *
     * 举个例子，使用 Spring 的 JdbcTemplate（利用了 Java 5 特性的 JdbcTemplate 实现）重写的 getEmployeeById()
     * 方法仅仅关注于获取员工数据的核心逻辑，而不需要迎合 JDBC API 的需求。
     *
     * 如下展示了修订后的 getEmployeeByIdNew() 方法。
     *
     *     public Employee getEmployeeByIdNew(long id) {
     *         return jdbcTemplate.queryForObject("select id, first_name, last_name, salary from " +
     *                 "employee where id = ?", new RowMapper<Employee>() {
     *             @Override
     *             public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
     *                 Employee employee = new Employee();
     *                 employee.setId(rs.getLong("id"));
     *                 employee.setFirstName(rs.getString("first_name"));
     *                 employee.setLastName(rs.getString("last_name"));
     *                 employee.setSalary(rs.getBigDecimal("salary"));
     *                 return employee;
     *             }
     *         }, id);
     *     }
     *
     * 正如你所看到的，新版本的 getEmployeeByIdNew() 简单多了，而且仅仅关注于从数据库中查询员工。模板的 queryForObject()
     * 方法需要一个 SQL 查询语句，一个 RowMapper 对象（把数据映射为一个域对象），零个或多个查询参数。getEmployeeByIdNew()
     * 方法再也看不到以前的 JDBC 样板式代码了，它们全部被封装到了模板中。
     */
    public static void main(String[] args) {

    }

    private DataSource dataSource;

    /**
     * 许多 Java API，例如 JDBC，会涉及编写大量的样板式代码
     */
    public Employee getEmployeeById(long id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement("select id, first_name, last_name, salary from " +
                    "employee where id = ?");
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            Employee employee = null;
            if (rs.next()) {
                employee = new Employee();
                employee.setId(rs.getLong("id"));
                employee.setFirstName(rs.getString("first_name"));
                employee.setLastName(rs.getString("last_name"));
                employee.setSalary(rs.getBigDecimal("salary"));
            }
            return employee;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    private JdbcTemplate jdbcTemplate;

    /**
     * 模板能够让你的代码关注于自身的职责
     */
    public Employee getEmployeeByIdNew(long id) {
        return jdbcTemplate.queryForObject("select id, first_name, last_name, salary from " +
                "employee where id = ?", new RowMapper<Employee>() {
            @Override
            public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
                Employee employee = new Employee();
                employee.setId(rs.getLong("id"));
                employee.setFirstName(rs.getString("first_name"));
                employee.setLastName(rs.getString("last_name"));
                employee.setSalary(rs.getBigDecimal("salary"));
                return employee;
            }
        }, id);
    }

}
