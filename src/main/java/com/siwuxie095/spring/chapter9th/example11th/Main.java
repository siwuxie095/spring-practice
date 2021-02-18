package com.siwuxie095.spring.chapter9th.example11th;

/**
 * @author Jiajing Li
 * @date 2021-02-18 22:01:52
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 拦截请求
     *
     * 在前面，看到一个特别简单的 Spring Security 配置，在这个默认的配置中，会要求所有请求都要经过认证。
     * 有些人可能会说，过多的安全性总比安全性太少要好。但也有一种说法就是要适量地应用安全性。
     *
     * 在任何应用中，并不是所有的请求都需要同等程度地保护。有些请求需要认证，而另一些可能并不需要。有些请
     * 求可能只有具备特定权限的用户才能访问，没有这些权限的用户会无法访问。
     *
     * 例如，考虑 Spittr 应用的请求。首页当然是公开的，不需要进行保护。类似地，因为所有的 Spittle 都是
     * 公开的，所以展现 Spittle 的页面不需要安全性。但是，创建 Spittle 的请求只有认证用户才能执行。同样，
     * 尽管用户基本信息页面是公开的，不需要认证，但是，如果要处理 "/spitters/me" 请求，并展现当前用户的
     * 基本信息时，那么就需要进行认证，从而确定要展现谁的信息。
     *
     * 对每个请求进行细粒度安全性控制的关键在于重载 configure(HttpSecurity) 方法。如下的代码片段展现
     * 了重载的 configure(HttpSecurity) 方法，它为不同的 URL 路径有选择地应用安全性：
     *
     * @Configuration
     * @EnableWebMvcSecurity
     * public class SecurityConfig extends WebSecurityConfigurerAdapter {
     *
     *     @Override
     *     protected void configure(HttpSecurity http) throws Exception {
     *         http.authorizeRequests()
     *                 .antMatchers("/spitters/me").authenticated()
     *                 .antMatchers(HttpMethod.POST, "/spittles").authenticated()
     *                 .anyRequest().permitAll();
     *     }
     *
     * }
     *
     * configure() 方法中得到的 HttpSecurity 对象可以在多个方面配置 HTTP 的安全性。在这里，首先调用
     * authorizeRequests()，然后调用该方法所返回的对象的方法来配置请求级别的安全性细节。其中，第一次
     * 调用 antMatchers() 指定了对 "/spitters/me" 路径的请求需要进行认证。第二次调用 antMatchers()
     * 更为具体，说明对 "/spittles" 路径的 HTTP POST 请求必须要经过认证。最后对 anyRequests() 的调
     * 用中，说明其他所有的请求都是允许的，不需要认证和任何的权限。
     *
     * antMatchers() 方法中设定的路径支持 Ant 风格的通配符。在这里并没有这样使用，但是也可以使用通配符
     * 来指定路径，如下所示：
     *
     * .antMatchers("/spitters/**").authenticated();
     *
     * 也可以在一个对 antMatchers() 方法的调用中指定多个路径：
     *
     * .antMatchers("/spitters/**", "/spitters/mine").authenticated();
     *
     * antMatchers() 方法所使用的路径可能会包括 Ant 风格的通配符，而 regexMatchers() 方法则能够接受
     * 正则表达式来定义请求路径。例如，如下代码片段所使用的正则表达式与 "/spitters/**"（Ant风格）功能
     * 是相同的：
     *
     * .regexMatchers("/spitters/.*").authenticated();
     *
     * 除了路径选择，还通过 authenticated() 和 permitAll() 来定义该如何保护路径。authenticated()
     * 要求在执行该请求时，必须已经登录了应用。如果用户没有认证的话，Spring Security 的 Filter 将会
     * 捕获该请求，并将用户重定向到应用的登录页面。同时，permitAll() 方法允许请求没有任何的安全限制。
     *
     * 除了 authenticated() 和 permitAll() 以外，还有其他的一些方法能够用来定义该如何保护请求。如下
     * 描述了所有可用的方案：
     * （1）access(String)：如果给定的 SpEL 表达式计算结果为 true，就允许访问；
     * （2）anonymous()：允许匿名用户访问；
     * （3）authenticated()：允许认证过的用户访问；
     * （4）denyAll()：无条件拒绝所有访问；
     * （5）fullyAuthenticated()：如果用户是完整认证的话（不是通过 Remember-me 功能认证的），就允许
     * 访问；
     * （6）hasAnyAuthority(String...)：如果用户具备给定权限中的某一个的话，就允许访问；
     * （7）hasAnyRole(String...)：如果用户具备给定角色中的某一个的话，就允许访问；
     * （8）hasAuthority(String)：如果用户具备给定权限的话，就允许访问；
     * （9）hasIpAddress(String)：如果请求来自给定 IP 地址的话，就允许访问；
     * （10）hasRole(String)：如果用户具备给定角色的话，就允许访问；
     * （11）not()：对其他访问方法的结果求反；
     * （12）permitAll()：无条件允许访问；
     * （13）rememberMe()：如果用户是通过 Remember-me 功能认证的，就允许访问。
     *
     * 通过使用上面的方法，所配置的安全性能够不仅仅限于认证用户。例如，可以修改之前的 configure() 方法，
     * 要求用户不仅需要认证，还要具备 ROLE_SPITTER 权限：
     *
     *     @Override
     *     protected void configure(HttpSecurity http) throws Exception {
     *         http.authorizeRequests()
     *                 .antMatchers("/spitters/me").hasAuthority("ROLE_SPITTER")
     *                 .antMatchers(HttpMethod.POST, "/spittles").hasAuthority("ROLE_SPITTER")
     *                 .anyRequest().permitAll();
     *     }
     *
     * 作为替代方案，还可以使用 hasRole() 方法，它会自动使用 "ROLE_" 前缀：
     *
     *     @Override
     *     protected void configure(HttpSecurity http) throws Exception {
     *         http.authorizeRequests()
     *                 .antMatchers("/spitters/me").hasRole("SPITTER")
     *                 .antMatchers(HttpMethod.POST, "/spittles").hasRole("SPITTER")
     *                 .anyRequest().permitAll();
     *     }
     *
     * 可以将任意数量的 antMatchers()、regexMatchers() 和 anyRequest() 连接起来，以满足 Web 应用
     * 安全规则的需要。但是，需要知道，这些规则会按照给定的顺序发挥作用。所以，很重要的一点就是将最为具体
     * 的请求路径放在前面，而最不具体的路径（如anyRequest()）放在最后面。如果不这样做的话，那不具体的路
     * 径配置将会覆盖掉更为具体的路径配置。
     *
     *
     *
     * 1、使用 Spring 表达式进行安全保护
     *
     * 上面的大多数方法都是一维的，也就是说可以使用 hasRole() 限制某个特定的角色，但是不能在相同的路径
     * 上同时通过 hasIpAddress() 限制特定的 IP 地址。
     *
     * 另外，除了上面定义的方法以外，没有办法使用其他的条件。如果希望限制某个角色只能在星期二进行访问的
     * 话，该怎么办呢？
     *
     * Spring 表达式语言（Spring Expression Language，SpEL），可以作为装配 bean 属性的高级技术。
     * 借助 access() 方法，也可以将 SpEL 作为声明访问限制的一种方式。例如，如下就是使用 SpEL 表达式
     * 来声明具有 "ROLE_SPITTER" 角色才能访问 "/spitter/me" URL：
     *
     * .antMatchers("/spitters/me").access("hasRole('ROLE_SPITTER')")
     *
     * 这个对 "/spitter/me" 的安全限制与开始时的效果是等价的，只不过这里使用了 SpEL 来描述安全规则。
     * 如果当前用户被授予了给定角色的话，那 hasRole() 表达式的计算结果就为 true。
     *
     * 让 SpEL 更强大的原因在于，hasRole() 仅是 Spring 支持的安全相关表达式中的一种，如下列出了
     * Spring Security 支持的所有 SpEL 表达式。
     * （1）authentication：用户的认证对象；
     * （2）denyAll：结果始终为 false；
     * （3）hasAnyRole(list of roles)：如果用户被授予了列表中任意的指定角色，结果为 true；
     * （4）hasRole(role)：如果用户被授予了指定的角色，结果为 true；
     * （5）hasIpAddress(IP Address)：如果请求来自指定 IP 的话，结果为 true；
     * （6）isAnonymous()：如果当前用户为匿名用户，结果为 true；
     * （7）isAuthenticated()：如果当前用户进行了认证的话，结果为 true；
     * （8）isFullyAuthenticated()：如果当前用户进行了完整认证的话（不是通过 Remember-me 功能进行的
     * 认证），结果为true；
     * （9）isRememberMe()：如果当前用户是通过 Remember-me 自动认证的，结果为 true；
     * （10）permitAll：结果始终为 true；
     * （11）principal：用户的 principal 对象。
     *
     * 在掌握了 Spring Security 的 SpEL 表达式后，就能够不再局限于基于用户的权限进行访问限制了。例如，
     * 如果你想限制 "/spitter/me" URL 的访问，不仅需要 ROLE_SPITTER，还需要来自指定的 IP 地址，那么
     * 可以按照如下的方式调用 access() 方法：
     *
     * .antMatchers("/spitters/me")
     *      .access("hasRole('ROLE_SPITTER') and hasIpAddress('192.168.1.2')")
     *
     * 可以使用 SpEL 实现各种各样的安全性限制。现在，你可能已经在想象基于 SpEL 所能实现的那些有趣的安全
     * 性限制了。
     *
     * 下面看一下 Spring Security 拦截请求的另外一种方式：强制通道的安全性。
     *
     *
     *
     * 2、强制通道的安全性
     *
     * 使用 HTTP 提交数据是一件具有风险的事情。如果使用 HTTP 发送无关紧要的信息，这可能不是什么大问题。
     * 但是如果你通过 HTTP 发送诸如密码和信用卡号这样的敏感信息的话，那你就是在找麻烦了。通过 HTTP 发
     * 送的数据没有经过加密，黑客就有机会拦截请求并且能够看到他们想看的数据。这就是为什么敏感信息要通过
     * HTTPS 来加密发送的原因。
     *
     * 使用 HTTPS 似乎很简单。你要做的事情只是在 URL 中的 HTTP 后加上一个字母 "s" 就可以了。是这样吗？
     *
     * 这是真的，但这是把使用 HTTPS 通道的责任放在了错误的地方。通过添加 "s" 就能很容易地实现页面的安
     * 全性，但是忘记添加 "s" 同样也是很容易出现的。如果应用中有多个链接需要 HTTPS，估计在其中的一两个
     * 上忘记添加 "s" 的概率还是很高的。
     *
     * 另一方面，你可能还会在原本并不需要 HTTPS 的地方，误用 HTTPS。
     *
     * 传递到 configure() 方法中的 HttpSecurity 对象，除了具有 authorizeRequests() 方法以外，还
     * 有一个 requiresChannel() 方法，借助这个方法能够为各种 URL 模式声明所要求的通道。
     *
     * 作为示例，可以参考 Spittr 应用的注册表单。尽管 Spittr 应用不需要信用卡号、社会保障号或其他特别
     * 敏感的信息，但用户有可能仍然希望信息是私密的。为了保证注册表单的数据通过 HTTPS 传送，可以在配置
     * 中添加 requiresChannel() 方法，如下所示：
     *
     *     @Override
     *     protected void configure(HttpSecurity http) throws Exception {
     *         http.authorizeRequests()
     *                 .antMatchers("/spitters/me").hasRole("SPITTER")
     *                 .antMatchers(HttpMethod.POST, "/spittles").hasRole("SPITTER")
     *                 .anyRequest().permitAll()
     *                 .and()
     *                 .requiresChannel()
     *                 .antMatchers("/spitter/form").requiresSecure();
     *     }
     *
     * 不论何时，只要是对 "/spitter/form" 的请求，Spring Security 都视为需要安全通道（通过调用
     * requiresChannel() 确定的）并自动将请求重定向到 HTTPS 上。
     *
     * 与之相反，有些页面并不需要通过 HTTPS 传送。例如，首页不包含任何敏感信息，因此并不需要通过 HTTPS
     * 传送。可以使用 requiresInsecure() 代替 requiresSecure() 方法，将首页声明为始终通过 HTTP
     * 传送：
     *
     * .antMatchers("/").requiresInsecure();
     *
     * 如果通过 HTTPS 发送了对 "/" 的请求，Spring Security 将会把请求重定向到不安全的 HTTP 通道上。
     *
     * 在强制要求通道时，路径的选取方案与 authorizeRequests() 是相同的。这里使用了 antMatches()，
     * 但也可以使用 regexMatchers() 方法，通过正则表达式选取路径模式。
     *
     *
     *
     * 3、防止跨站请求伪造
     *
     * 当一个 POST 请求提交到 "/spittles" 上时，SpittleController 将会为用户创建一个新的 Spittle
     * 对象。但是，如果这个 POST 请求来源于其他站点的话，会怎么样呢？如果在其他站点提交如下表单，这个
     * POST 请求会造成什么样的结果呢？
     *
     * <form method="POST" action="http://www.spittr.com/spittles">
     *     <input type="hidden" name="message" value="I'm stupid!" />
     *     <input type="submit" value="Click here to win a new car!" />
     * </form>
     *
     * 假设你禁不住获得一辆新汽车的诱惑，点击了按钮 —— 那么你将会提交表单到如下地址：
     *
     * http://www.spittr.com/spittles
     *
     * 如果你已经登录到了 spittr.com，那么这就会广播一条消息，让每个人都知道你做了一件蠢事。
     *
     * 这是跨站请求伪造（cross-site request forgery，CSRF）的一个简单样例。简单来讲，如果一个站点
     * 欺骗用户提交请求到其他服务器的话，就会发生 CSRF 攻击，这可能会带来消极的后果。尽管提交 "I’m
     * stupid!" 这样的信息到 Spittr 站点算不上什么 CSRF 攻击的最糟糕场景，但是你可以很容易想到更为
     * 严重的攻击情景，它可能会对你的银行账号执行难以预期的操作。
     *
     * 从 Spring Security 3.2 开始，默认就会启用 CSRF 防护。实际上，除非你采取行为处理 CSRF 防护
     * 或者将这个功能禁用，否则的话，在应用中提交表单时，你可能会遇到问题。
     *
     * Spring Security 通过一个同步 token 的方式来实现 CSRF 防护的功能。它将会拦截状态变化的请求
     * （例如，非GET、HEAD、OPTIONS和TRACE的请求）并检查 CSRF token。如果请求中不包含 CSRF token
     * 的话，或者 token 不能与服务器端的 token 相匹配，请求将会失败，并抛出 CsrfException 异常。
     *
     * 这意味着在你的应用中，所有的表单必须在一个 "_csrf" 域中提交 token，而且这个 token 必须要与
     * 服务器端计算并存储的 token 一致，这样的话当表单提交的时候，才能进行匹配。
     *
     * 好消息是，Spring Security 已经简化了将 token 放到请求的属性中这一任务。如果你使用 Thymeleaf
     * 作为页面模板的话，只要 <form> 标签的 action 属性添加了 Thymeleaf 命名空间前缀，那么就会自动
     * 生成一个 "_csrf" 隐藏域：
     *
     * <form method="POST" th:action="@{spittles}">
     *     ...
     * </form>
     *
     * 如果使用 JSP 作为页面模板的话，要做的事情非常类似：
     *
     * <input type="hidden"
     *        name="${_csrf.parameterName}"
     *        value="${_csrf.token}" />
     *
     * 更好的功能是，如果使用 Spring 的表单绑定标签的话，<sf:form> 标签会自动为添加隐藏的 CSRF token
     * 标签。
     *
     * 处理 CSRF 的另外一种方式就是根本不去处理它。可以在配置中通过调用 csrf().disable() 禁用 Spring
     * Security 的 CSRF 防护功能，如下所示：
     *
     *     @Override
     *     protected void configure(HttpSecurity http) throws Exception {
     *         http.csrf().disable();
     *     }
     *
     * 需要提醒的是，禁用 CSRF 防护功能通常来讲并不是一个好主意。如果这样做的话，那么应用就会面临 CSRF
     * 攻击的风险。只有在深思熟虑之后，才能禁用。
     */
    public static void main(String[] args) {

    }

}
