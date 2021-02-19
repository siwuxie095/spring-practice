package com.siwuxie095.spring.chapter9th.example12th;

/**
 * @author Jiajing Li
 * @date 2021-02-19 07:55:43
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 认证用户
     *
     * 如果你使用最简单的 Spring Security 配置的话，那么就能无偿地得到一个登录页。实际上，在重写
     * configure(HttpSecurity) 之前，都能使用一个简单却功能完备的登录页。但是，一旦重写了
     * configure(HttpSecurity) 方法，就失去了这个简单的登录页面。
     *
     * 不过，把这个功能找回来也很容易。所需要做的就是在 configure(HttpSecurity) 方法中，调用
     * formLogin()，如下所示：
     *
     *     @Override
     *     protected void configure(HttpSecurity http) throws Exception {
     *         http.formLogin()
     *                 .and()
     *                 .authorizeRequests()
     *                 .antMatchers("/spitter/me").hasRole("SPITTER")
     *                 .antMatchers(HttpMethod.POST, "/spittles").hasRole("SPITTER")
     *                 .anyRequest().permitAll()
     *                 .and()
     *                 .requiresChannel()
     *                 .antMatchers("/spitter").requiresSecure();
     *     }
     *
     * 请注意，和前面一样，这里调用 and() 方法来将不同的配置指令连接在一起。
     *
     * 如果访问应用的 "/login" 链接或者导航到需要认证的页面，那么将会在浏览器中展现登录页面。在审
     * 美上，这个页面它没有什么令人兴奋的，但是它却能实现所需的功能。
     *
     * 你肯定希望在自己的应用程序中能有一个比默认登录页更漂亮的登录页面。如果这个普通的登录页面破坏
     * 了原本精心设计的漂亮站点，那真的是件很令人遗憾的事情。没问题！接下来，就看一下如何为应用添加
     * 自定义的登录页面。
     *
     *
     *
     * 1、添加自定义的登录页
     *
     * 创建自定义登录页的第一步就是了解登录表单中都需要些什么。只需看一下默认登录页面的 HTML 源码，
     * 就能了解需要些什么：
     *
     * <html>
     *     <head><title>Login Page</title></head>
     *     <body onload='document.f.username.focus();'>
     *         <form name='f' action="/spittr/login" method='POST'>
     *         <table>
     *             <tr><td>User:</td><td>
     *                 <input type='text' name='username' value='' />
     *             </td></tr>
     *             <tr><td>Password:</td><td>
     *                 <input type='password' name='password' />
     *             </td></tr>
     *             <tr><td colspan='2'>
     *                 <input name="submit" type="submit" value="Login" />
     *             </td></tr>
     *             <input name="_csrf" type="hidden" vlaue="6829b1ae-0a14-4920-aac4-5abbd7eeb9ee" />
     *         </table>
     *         </form>
     *     </body>
     * </html>
     *
     * 需要注意的一个关键点是 <form> 提交到了什么地方。同时还需要注意 username 和 password 输入
     * 域，在你的登录页中，需要同样的输入域。最后，假设没有禁用 CSRF 的话，还需要保证包含了值为 CSRF
     * token 的 "_csrf" 输入域。
     *
     * 在 login.html 中所展现的 Thymeleaf 模板提供了一个与 Spittr 应用风格一致的登录页。
     *
     * 需要注意的是，在 Thymeleaf 模板中，包含了 username 和 password 输入域，就像默认的登录页
     * 一样，它也提交到了相对于上下文的 "/login" 页面上。因为这是一个 Thymeleaf 模板，因此隐藏的
     * "_csrf" 域将会自动添加到表单中。
     *
     *
     *
     * 2、启用 HTTP Basic 认证
     *
     * 对于应用程序的人类用户来说，基于表单的认证是比较理想的。后续将会看到如何将 Web 应用的页面转
     * 化为 RESTful API。当应用程序的使用者是另外一个应用程序的话，使用表单来提示登录的方式就不太
     * 适合了。
     *
     * HTTP Basic 认证（HTTP Basic Authentication）会直接通过 HTTP 请求本身，对要访问应用程
     * 序的用户进行认证。你可能在以前见过 HTTP Basic 认证。当在 Web 浏览器中使用时，它将向用户弹
     * 出一个简单的模态对话框。
     *
     * 但这只是 Web 浏览器的显示方式。本质上，这是一个 HTTP 401 响应，表明必须要在请求中包含一个
     * 用户名和密码。在 REST 客户端向它使用的服务进行认证的场景中，这种方式比较适合。
     *
     * 如果要启用 HTTP Basic 认证的话，只需在 configure() 方法所传入的 HttpSecurity 对象上调
     * 用 httpBasic() 即可。另外，还可以通过调用 realmName() 方法指定域。如下是在 Spring
     * Security 中启用 HTTP Basic 认证的典型配置：
     *
     *     @Override
     *     protected void configure(HttpSecurity http) throws Exception {
     *         http.formLogin()
     *                 .loginPage("/login")
     *                 .and()
     *                 .httpBasic()
     *                 .realmName("Spittr");
     *     }
     *
     * 注意，和前面一样，在 configure() 方法中，通过调用 and() 方法来将不同的配置指令连接在一起。
     *
     * 在 httpBasic() 方法中，并没有太多的可配置项，甚至不需要什么额外配置。HTTP Basic 认证要么
     * 开启要么关闭。所以，与其进一步研究这个话题，还不如看看如何通过 Remember-me 功能实现用户的
     * 自动认证。
     *
     *
     *
     * 3、启用 Remember-me 功能
     *
     * 对于应用程序来讲，能够对用户进行认证是非常重要的。但是站在用户的角度来讲，如果应用程序不用每
     * 次都提示他们登录是更好的。这就是为什么许多站点提供了 Remember-me 功能，你只要登录过一次，
     * 应用就会记住你，当再次回到应用的时候你就不需要登录了。
     *
     * Spring Security 使得为应用添加 Remember-me 功能变得非常容易。为了启用这项功能，只需在
     * configure() 方法所传入的 HttpSecurity 对象上调用 rememberMe() 即可。
     *
     *     @Override
     *     protected void configure(HttpSecurity http) throws Exception {
     *         http.formLogin()
     *                 .loginPage("/login")
     *                 .and()
     *                 .rememberMe()
     *                 .tokenValiditySeconds(2419200)
     *                 .key("spittrKey");
     *     }
     *
     * 在这里，通过一点特殊的配置就可以启用 Remember-me 功能。默认情况下，这个功能是通过在 cookie
     * 中存储一个 token 完成的，这个 token 最多两周内有效。但是，在这里，指定这个 token 最多四周
     * 内有效(2,419,200 秒)。
     *
     * 存储在 cookie 中的 token 包含用户名、密码、过期时间和一个私钥 —— 在写入 cookie 前都进行
     * 了 MD5 哈希。默认情况下，私钥的名为 SpringSecured，但在这里将其设置为 spitterKey，使它
     * 专门用于 Spittr 应用。
     *
     * 如此简单。既然 Remember-me 功能已经启用，需要有一种方式来让用户表明他们希望应用程序能够记住
     * 他们。为了实现这一点，登录请求必须包含一个名为 remember-me 的参数。在登录表单中，增加一个简
     * 单复选框就可以完成这件事情：
     *
     * <input id="remember_me" name="remember-me" type="checkbox" />
     * <label for="remember_me" class="inline">Remember me</label>
     *
     * 在应用中，与登录同等重要的功能就是退出。如果你启用 Remember-me 功能的话，更是如此，否则的话，
     * 用户将永远登录在这个系统中。下面将看一下如何添加退出功能。
     *
     *
     *
     * 4、退出
     *
     * 其实，按照现有的配置，退出功能已经启用了，不需要再做其他的配置了。这里需要的只是一个使用该功能
     * 的链接。
     *
     * 退出功能是通过 Servlet 容器中的 Filter 实现的（默认情况下），这个 Filter 会拦截针对 "/logout"
     * 的请求。因此，为应用添加退出功能只需添加如下的链接即可（如下以Thymeleaf代码片段的形式进行了展现）：
     *
     * <a th:href="@{/logout}">Logout</a>
     *
     * 当用户点击这个链接的时候，会发起对 "/logout" 的请求，这个请求会被 Spring Security 的 LogoutFilter
     * 所处理。用户会退出应用，所有的 Remember-me token 都会被清除掉。在退出完成后，用户浏览器将会重定向到
     * "/login?logout"，从而允许用户进行再次登录。
     *
     * 如果你希望用户被重定向到其他的页面，如应用的首页，那么可以在 configure() 中进行如下的配置：
     *
     *     @Override
     *     protected void configure(HttpSecurity http) throws Exception {
     *         http.formLogin()
     *                 .loginPage("/login")
     *                 .and()
     *                 .logout()
     *                 .logoutSuccessUrl("/");
     *     }
     *
     * 在这里，和前面一样，通过 and() 连接起了对 logout() 的调用。logout() 提供了配置退出行为的
     * 方法。在本例中，调用 logoutSuccessUrl() 表明在退出成功之后，浏览器需要重定向到 "/"。
     *
     * 除了 logoutSuccessUrl() 方法以外，你可能还希望重写默认的 LogoutFilter 拦截路径。可以通
     * 过调用 logoutUrl() 方法实现这一功能：
     *
     * .logout().logoutSuccessUrl("/").logoutUrl("/signout")
     *
     * 到目前为止，已经看到了如何在发起请求的时候保护 Web 应用。这假设安全性主要涉及阻止用户访问没
     * 有权限的 URL。但是，如果能够不给用户显示其无权访问的连接，这也是一个很好的思路。后续将会看一
     * 下如何添加视图级别的安全性。
     */
    public static void main(String[] args) {

    }

}
