package com.siwuxie095.spring.chapter9th.example9th;

/**
 * @author Jiajing Li
 * @date 2021-02-17 15:54:40
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 基于 LDAP 进行认证
     *
     * 为了让 Spring Security 使用基于 LDAP 的认证，可以使用 ldapAuthentication() 方法。这个方法在功能
     * 上类似于 jdbcAuthentication()，只不过是 LDAP 版本。如下的 configure() 方法展现了 LDAP 认证的简
     * 单配置：
     *
     * @Configuration
     * @EnableWebMvcSecurity
     * public class SecurityConfig extends WebSecurityConfigurerAdapter {
     *
     *     @Override
     *     protected void configure(AuthenticationManagerBuilder auth) throws Exception {
     *         auth.ldapAuthentication()
     *                 .userSearchFilter("uid={0}")
     *                 .groupSearchFilter("member={0}");
     *     }
     *
     * }
     *
     * 方法 userSearchFilter() 和 groupSearchFilter() 用来为基础 LDAP 查询提供过滤条件，它们分别用于搜
     * 索用户和组。默认情况下，对于用户和组的基础查询都是空的，也就是表明搜索会在 LDAP 层级结构的根开始。但是
     * 可以通过指定查询基础来改变这个默认行为：
     *
     *     @Override
     *     protected void configure(AuthenticationManagerBuilder auth) throws Exception {
     *         auth.ldapAuthentication()
     *                 .userSearchBase("ou=people")
     *                 .userSearchFilter("uid={0}")
     *                 .groupSearchBase("ou=groups")
     *                 .groupSearchFilter("member={0}");
     *     }
     *
     * userSearchBase() 属性为查找用户提供了基础查询。同样，groupSearchBase() 为查找组指定了基础查询。声
     * 明用户应该在名为 people 的组织单元下搜索而不是从根开始。而组应该在名为 groups 的组织单元下搜索。
     *
     *
     *
     * 配置密码比对
     *
     * 基于 LDAP 进行认证的默认策略是进行绑定操作，直接通过 LDAP 服务器认证用户。另一种可选的方式是进行比对操
     * 作。这涉及将输入的密码发送到 LDAP 目录上，并要求服务器将这个密码和用户的密码进行比对。因为比对是在 LDAP
     * 服务器内完成的，实际的密码能保持私密。
     *
     * 如果你希望通过密码比对进行认证，可以通过声明 passwordCompare() 方法来实现：
     *
     *     @Override
     *     protected void configure(AuthenticationManagerBuilder auth) throws Exception {
     *         auth.ldapAuthentication()
     *                 .userSearchBase("ou=people")
     *                 .userSearchFilter("uid={0}")
     *                 .groupSearchBase("ou=groups")
     *                 .groupSearchFilter("member={0}")
     *                 .passwordCompare();
     *     }
     *
     * 默认情况下，在登录表单中提供的密码将会与用户的 LDAP 条目中的 userPassword 属性进行比对。如果密码被保
     * 存在不同的属性中，可以通过 passwordAttribute() 方法来声明密码属性的名称：
     *
     *     @Override
     *     protected void configure(AuthenticationManagerBuilder auth) throws Exception {
     *         auth.ldapAuthentication()
     *                 .userSearchBase("ou=people")
     *                 .userSearchFilter("uid={0}")
     *                 .groupSearchBase("ou=groups")
     *                 .groupSearchFilter("member={0}")
     *                 .passwordCompare()
     *                 .passwordEncoder(new Md5PasswordEncoder())
     *                 .passwordAttribute("passcode");
     *     }
     *
     * 在本例中，指定了要与给定密码进行比对的是 "passcode" 属性。另外，还可以指定密码转码器。在进行服务器端密
     * 码比对时，有一点非常好，那就是实际的密码在服务器端是私密的。但是进行尝试的密码还是需要通过线路传输到 LDAP
     * 服务器上，这可能会被黑客所拦截。为了避免这一点，可以通过调用 passwordEncoder() 方法指定加密策略。
     *
     * 在本示例中，密码会进行 MD5 加密。这需要 LDAP 服务器上密码也使用 MD5 进行加密。
     *
     *
     *
     * 引用远程的 LDAP 服务器
     *
     * 到目前为止，忽略的一件事就是 LDAP 和实际的数据在哪里。这里很开心地配置 Spring 使用 LDAP 服务器进行认
     * 证，但是服务器在哪里呢？
     *
     * 默认情况下，Spring Security 的 LDAP 认证假设 LDAP 服务器监听本机的 33389 端口。但是，如果你的 LDAP
     * 服务器在另一台机器上，那么可以使用 contextSource() 方法来配置这个地址：
     *
     *     @Override
     *     protected void configure(AuthenticationManagerBuilder auth) throws Exception {
     *         auth.ldapAuthentication()
     *                 .userSearchBase("ou=people")
     *                 .userSearchFilter("uid={0}")
     *                 .groupSearchBase("ou=groups")
     *                 .groupSearchFilter("member={0}")
     *                 .contextSource()
     *                 .url("ldap://habuma.com:389/dc=habuma,dc=com");
     *     }
     *
     * contextSource() 方法会返回一个 ContextSourceBuilder 对象，这个对象除了其他功能以外，还提供了 url()
     * 方法用来指定 LDAP 服务器的地址。
     *
     *
     *
     * 配置嵌入式的 LDAP 服务器
     *
     * 如果你没有现成的 LDAP 服务器供认证使用，Spring Security 还提供了嵌入式的 LDAP 服务器。这样就不再需要
     * 设置远程 LDAP 服务器的 URL，只需通过 root() 方法指定嵌入式服务器的根前缀就可以了：
     *
     *     @Override
     *     protected void configure(AuthenticationManagerBuilder auth) throws Exception {
     *         auth.ldapAuthentication()
     *                 .userSearchBase("ou=people")
     *                 .userSearchFilter("uid={0}")
     *                 .groupSearchBase("ou=groups")
     *                 .groupSearchFilter("member={0}")
     *                 .contextSource()
     *                 .root("dc=habuma,dc=com");
     *     }
     *
     * 当 LDAP 服务器启动时，它会尝试在类路径下寻找 LDIF 文件来加载数据。LDIF（LDAP Data Interchange Format，
     * LDAP 数据交换格式）是以文本文件展现 LDAP 数据的标准方式。每条记录可以有一行或多行，每项包含一个名值对。记录
     * 之间通过空行进行分割。
     *
     * 如果你不想让 Spring 从整个根路径下搜索 LDIF 文件的话，那么可以通过调用 ldif() 方法来明确指定加载哪个 LDIF
     * 文件：
     *
     *     @Override
     *     protected void configure(AuthenticationManagerBuilder auth) throws Exception {
     *         auth.ldapAuthentication()
     *                 .userSearchBase("ou=people")
     *                 .userSearchFilter("uid={0}")
     *                 .groupSearchBase("ou=groups")
     *                 .groupSearchFilter("member={0}")
     *                 .contextSource()
     *                 .root("dc=habuma,dc=com")
     *                 .ldif("classpath:users.ldif");
     *     }
     *
     * Spring Security 内置的用户存储非常便利，并且涵盖了最为常用的用户场景。但是，如果你的认证需求不是那么通用的
     * 话，那么就需要创建并配置自定义的用户详细信息服务了。
     */
    public static void main(String[] args) {

    }

}
