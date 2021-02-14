package com.siwuxie095.spring.chapter9th.example5th;

/**
 * @author Jiajing Li
 * @date 2021-02-14 10:18:32
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 编写简单的安全性配置
     *
     * 在 Spring Security 的早期版本中（在其还被称为 Acegi Security 之时），为了在 Web 应用中启用简单的安全功能，
     * 需要编写上百行的 XML 配置。Spring Security 2.0 提供了安全性相关的 XML 配置命名空间，让情况有了一些好转。
     *
     * Spring 3.2 引入了新的 Java 配置方案，完全不再需要通过 XML 来配置安全性功能了。如下代码展现了 Spring Security
     * 最简单的 Java 配置。
     *
     * @Configuration
     * @EnableWebSecurity
     * public class SecurityConfig extends WebSecurityConfigurerAdapter {
     *
     * }
     *
     * 顾名思义，@EnableWebSecurity 注解将会启用 Web 安全功能。但它本身并没有什么用处，Spring Security 必须配置
     * 在一个实现了 WebSecurityConfigurer 的 bean 中，或者（简单起见）扩展 WebSecurityConfigurerAdapter。在
     * Spring 应用上下文中，任何实现了 WebSecurityConfigurer 的 bean 都可以用来配置 Spring Security，但是最为
     * 简单的方式还是像上面那样扩展 WebSecurityConfigurer 的 Adapter 类。
     *
     * @EnableWebSecurity 可以启用任意 Web 应用的安全性功能，不过，如果你的应用碰巧是使用 Spring MVC 开发的，那
     * 么就应该考虑使用 @EnableWebMvcSecurity 替代它，如下：
     *
     * @Configuration
     * @EnableWebMvcSecurity
     * public class MvcSecurityConfig extends WebSecurityConfigurerAdapter {
     *
     * }
     *
     * 除了其他的内容以外，@EnableWebMvcSecurity 注解还配置了一个 Spring MVC 参数解析解析器（argument resolver），
     * 这样的话处理器方法就能够通过带有 @AuthenticationPrincipal 注解的参数获得认证用户的 principal（或 username）。
     * 它同时还配置了一个 bean，在使用 Spring 表单绑定标签库来定义表单时，这个 bean 会自动添加一个隐藏的跨站请求伪造
     * （cross-site request forgery，CSRF）token 输入域。
     *
     * 看起来似乎并没有做太多的事情，但上面两个配置类会给应用产生很大的影响。其中任何一种配置都会将应用严格锁定，导致没有
     * 人能够进入该系统了！
     *
     * 尽管不是严格要求的，但可能希望指定 Web 安全的细节，这要通过重载 WebSecurityConfigurerAdapter 中的一个或多个
     * 方法来实现。可以通过重载 WebSecurityConfigurerAdapter 的三个 configure() 方法来配置 Web 安全性，这个过程
     * 中会使用传递进来的参数设置行为。
     *
     * 重载 WebSecurityConfigurerAdapter 的三个 configure() 方法如下：
     * （1）configure(WebSecurity)：通过重载，配置 Spring Security 的 Filter 链；
     * （2）configure(HttpSecurity)：通过重载，配置如何通过拦截器保护请求；
     * （3）configure(AuthenticationManagerBuilder)：通过重载，配置 user-detail 服务。
     *
     * 再来看一下 MvcSecurityConfig，可以看到它没有重写上述三个 configure() 方法中的任何一个，这就说明了为什么应用
     * 现在是被锁定的。尽管对于这里的需求来讲默认的 Filter 链是不错的，但是默认的 configure(HttpSecurity) 实际上
     * 等同于如下代码：
     *
     *     @Override
     *     protected void configure(HttpSecurity http) throws Exception {
     *         http.authorizeRequests()
     *                 .anyRequest().authenticated()
     *                 .and()
     *                 .formLogin()
     *                 .and()
     *                 .httpBasic();
     *     }
     *
     * 这个简单的默认配置指定了该如何保护 HTTP 请求，以及客户端认证用户的方案。通过调用 authorizeRequests() 和
     * anyRequest().authenticated() 就会要求所有进入应用的 HTTP 请求都要进行认证。它也配置 Spring Security
     * 支持基于表单的登录以及 HTTP Basic 方式的认证。
     *
     * 同时，因为这里没有重载 configure(AuthenticationManagerBuilder) 方法，所以没有用户存储支撑认证过程。没有
     * 用户存储，实际上就等于没有用户。所以，在这里所有的请求都需要认证，但是没有人能够登录成功。
     *
     * 为了让 Spring Security 满足应用的需求，还需要再添加一点配置。具体来讲，需要：
     * （1）配置用户存储；
     * （2）指定哪些请求需要认证，哪些请求不需要认证，以及所需要的权限；
     * （3）提供一个自定义的登录页面，替代原来简单的默认登录页。
     *
     * 除了 Spring Security 的这些功能，可能还希望基于安全限制，有选择性地在 Web 视图上显示特定的内容。
     */
    public static void main(String[] args) {

    }

}
