package com.siwuxie095.spring.chapter9th.example8th;

/**
 * @author Jiajing Li
 * @date 2021-02-16 22:41:55
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 基于数据库表进行认证
     *
     * 用户数据通常会存储在关系型数据库中，并通过 JDBC 进行访问。为了配置 Spring Security 使用以 JDBC 为支撑
     * 的用户存储，可以使用 jdbcAuthentication() 方法，所需的最少配置如下所示：
     *
     * @Configuration
     * @EnableWebMvcSecurity
     * public class SecurityConfig extends WebSecurityConfigurerAdapter {
     *
     *     @Autowired
     *     private DataSource dataSource;
     *
     *     @Override
     *     protected void configure(AuthenticationManagerBuilder auth) throws Exception {
     *         auth.jdbcAuthentication()
     *                 .dataSource(dataSource);
     *     }
     *
     * }
     *
     * 必须要配置的只是一个 DataSource，这样的话，就能访问关系型数据库了。在这里，DataSource 是通过自动装配的
     * 技巧得到的。
     *
     *
     *
     * 重写默认的用户查询功能
     *
     * 尽管默认的最少配置能够让一切运转起来，但是它对数据库模式有一些要求。它预期存在某些存储用户数据的表。更具体
     * 来说，下面的代码片段来源于 Spring Security 内部，这块代码展现了当查找用户信息时所执行的 SQL 查询语句：
     *
     *     public static final String DEF_USERS_BY_USERNAME_QUERY =
     *             "select username,password,enabled " +
     *             "from users " +
     *             "where username = ?";
     *     public static final String DEF_AUTHORITIES_BY_USERNAME_QUERY =
     *             "select username,authority " +
     *             "from authorities " +
     *             "where username = ?";
     *     public static final String DEF_GROUP_AUTHORITIES_BY_USERNAME_QUERY =
     *             "select g.id, g.group_name, ga.authority " +
     *             "from groups g, group_members gm, group_authorities ga " +
     *             "where gm.username = ? " +
     *             "and g.id = ga.group_id " +
     *             "and g.id = gm.group_id";
     *
     * PS：来源 org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl
     *
     * 在第一个查询中，获取了用户的用户名、密码以及是否启用的信息，这些信息会用来进行用户认证。接下来的查询查找了
     * 用户所授予的权限，用来进行鉴权，最后一个查询中，查找了用户作为群组的成员所授予的权限。
     *
     * 如果你能够在数据库中定义和填充满足这些查询的表，那么基本上就不需要你再做什么额外的事情了。但是，也有可能你
     * 的数据库与上面所述并不一致，那么你就会希望在查询上有更多的控制权。如果是这样的话，可以按照如下的方式配置自
     * 己的查询：
     *
     *     @Override
     *     protected void configure(AuthenticationManagerBuilder auth) throws Exception {
     *         auth.jdbcAuthentication()
     *                 .dataSource(dataSource)
     *                 .usersByUsernameQuery("select username, password, true " +
     *                         "from Spitter where username=?")
     *                 .authoritiesByUsernameQuery("select username, 'ROLE_USER' " +
     *                         "from Spitter where username=?");
     *     }
     *
     * 在这里，只重写了认证和基本权限的查询语句，但是通过调用 groupAuthoritiesByUsername() 方法，也能够将群
     * 组权限重写为自定义的查询语句。
     *
     * 将默认的 SQL 查询替换为自定义的设计时，很重要的一点就是要遵循查询的基本协议。所有查询都将用户名作为唯一
     * 的参数。认证查询会选取用户名、密码以及启用状态信息。权限查询会选取零行或多行包含该用户名及其权限信息的数据。
     * 群组权限查询会选取零行或多行数据，每行数据中都会包含群组 ID、群组名称以及权限。
     *
     *
     *
     * 使用转码后的密码
     *
     * 看一下上面的认证查询，它会预期用户密码存储在了数据库之中。这里唯一的问题在于如果密码明文存储的话，会很容易
     * 受到黑客的窃取。但是，如果数据库中的密码进行了转码的话，那么认证就会失败，因为它与用户提交的明文密码并不匹
     * 配。
     *
     * 为了解决这个问题，需要借助 passwordEncoder() 方法指定一个密码转码器（encoder）：
     *
     *     @Override
     *     protected void configure(AuthenticationManagerBuilder auth) throws Exception {
     *         auth.jdbcAuthentication()
     *                 .dataSource(dataSource)
     *                 .usersByUsernameQuery("select username, password, true " +
     *                         "from Spitter where username=?")
     *                 .authoritiesByUsernameQuery("select username, 'ROLE_USER' " +
     *                         "from Spitter where username=?")
     *                 .passwordEncoder(new StandardPasswordEncoder("53cr3t"));
     *     }
     *
     * passwordEncoder() 方法可以接受 Spring Security 中 PasswordEncoder 接口的任意实现。Spring Security
     * 的加密模块包括了三个这样的实现：
     * （1）BCryptPasswordEncoder；
     * （2）NoOpPasswordEncoder；
     * （3）StandardPasswordEncoder。
     *
     * 上述的代码中使用了 StandardPasswordEncoder，但是如果内置的实现无法满足需求时，你可以提供自定义的实现。
     * PasswordEncoder 接口非常简单：
     *
     * public interface PasswordEncoder {
     *
     *     String encode(CharSequence rawPassword);
     *
     *     boolean matches(CharSequence rawPassword, String encodedPassword);
     *
     * }
     *
     * 不管你使用哪一个密码转码器，都需要理解的一点是，数据库中的密码是永远不会解码的。所采取的策略与之相反，用户
     * 在登录时输入的密码会按照相同的算法进行转码，然后再与数据库中已经转码过的密码进行对比。这个对比是在
     * PasswordEncoder 的 matches() 方法中进行的。
     */
    public static void main(String[] args) {

    }

}
