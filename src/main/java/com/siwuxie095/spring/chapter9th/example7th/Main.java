package com.siwuxie095.spring.chapter9th.example7th;

/**
 * @author Jiajing Li
 * @date 2021-02-16 21:44:47
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 使用基于内存的用户存储
     *
     * 因为安全配置类扩展了 WebSecurityConfigurerAdapter，因此配置用户存储的最简单方式就是重载 configure() 方法，
     * 并以 AuthenticationManagerBuilder 作为传入参数。AuthenticationManagerBuilder 有多个方法可以用来配置
     * Spring Security 对认证的支持。通过 inMemoryAuthentication() 方法，可以启用、配置并任意填充基于内存的用户
     * 存储。
     *
     * 在如下代码中，SecurityConfig 重载了 configure() 方法，并使用两个用户来配置内存用户存储。
     *
     * @Configuration
     * @EnableWebMvcSecurity
     * public class SecurityConfig extends WebSecurityConfigurerAdapter {
     *
     *     @Override
     *     protected void configure(AuthenticationManagerBuilder auth) throws Exception {
     *         auth.inMemoryAuthentication()
     *                 .withUser("user").password("password").roles("USER")
     *                 .and()
     *                 .withUser("admin").password("password").roles("USER", "ADMIN");
     *     }
     *
     * }
     *
     * 可以看到，configure() 方法中的 AuthenticationManagerBuilder 使用构造者风格的接口来构建认证配置。通过简单
     * 地调用 inMemoryAuthentication() 就能启用内存用户存储。但是还需要有一些用户，否则的话，这和没有用户并没有什
     * 么区别。
     *
     * 因此，需要调用 withUser() 方法为内存用户存储添加新的用户，这个方法的参数是 username。withUser() 方法返回的
     * 是 UserDetailsManagerConfigurer.UserDetailsBuilder，这个对象提供了多个进一步配置用户的方法，包括设置用户
     * 密码的 password() 方法以及为给定用户授予一个或多个角色权限的 roles() 方法。
     *
     * 这里添加了两个用户，"user" 和 "admin"，密码均为 "password"。"user" 用户具有 USER 角色，而 "admin" 用户
     * 具有 ADMIN 和 USER 两个角色。可以看到，and() 方法能够将多个用户的配置连接起来。
     *
     * 除了 password()、roles() 和 and() 方法以外，还有其他的几个方法可以用来配置内存用户存储中的用户信息。如下描
     * 述了 UserDetailsManagerConfigurer.UserDetailsBuilder 对象所有可用的方法。
     *
     * （1）accountExpired(boolean)：定义账号是否已经过期；
     * （2）accountLocked(boolean)：定义账号是否已经锁定；
     * （3）and()：用来连接配置；
     * （4）authorities(GrantedAuthority...)：授予某个用户一项或多项权限；
     * （5）authorities(List<? extends GrantedAuthority>)：授予某个用户一项或多项权限；
     * （6）authorities(String...)：授予某个用户一项或多项权限；
     * （7）credentialsExpired(boolean)：定义凭证是否已经过期；
     * （8）disabled(boolean)：定义账号是否已被禁用；
     * （9）password(String)：定义用户的密码；
     * （10）roles(String...)：授予某个用户一项或多项角色。
     *
     * 需要注意的是，roles() 方法是 authorities() 方法的简写形式。roles() 方法所给定的值都会添加一个 "ROLE_" 前
     * 缀，并将其作为权限授予给用户。实际上，如下的用户配置与上面是等价的：
     *
     *         auth.inMemoryAuthentication()
     *                 .withUser("user").password("password").authorities("ROLE_USER")
     *                 .and()
     *                 .withUser("admin").password("password").authorities("ROLE_USER", "ROLE_ADMIN");
     *
     * 对于调试和开发人员测试来讲，基于内存的用户存储是很有用的，但是对于生产级别的应用来讲，这就不是最理想的可选方案了。
     * 为了用于生产环境，通常最好将用户数据保存在某种类型的数据库之中。
     */
    public static void main(String[] args) {

    }

}
