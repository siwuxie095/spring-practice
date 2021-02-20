package com.siwuxie095.spring.chapter9th.example13th;

/**
 * @author Jiajing Li
 * @date 2021-02-20 07:56:05
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 保护视图
     *
     * 当为浏览器渲染 HTML 内容时，你可能希望视图中能够反映安全限制和相关的信息。一个简单的样例就是渲染用户的
     * 基本信息（比如显示 "您已经以 ......身份登录"）。或者你想根据用户被授予了什么权限，有条件地渲染特定的
     * 视图元素。
     *
     * 在 Spring MVC 应用中渲染视图有两个最重要的可选方案：JSP 和 Thymeleaf。不管你使用哪种方案，都有办法
     * 在视图上实现安全性。Spring Security 本身提供了一个 JSP 标签库，而 Thymeleaf 通过特定的方言实现了
     * 与 Spring Security 的集成。
     *
     * 下面看一下如何将 Spring Security 用到视图中，就从 Spring Security 的 JSP 标签库开始吧。
     *
     *
     *
     * 1、使用 Spring Security 的 JSP 标签库
     *
     * Spring Security 的 JSP 标签库很小，只包含三个标签：
     * （1）<security:accesscontrollist>：
     * 如果用户通过访问控制列表授予了指定的权限，那么渲染该标签体中的内容；
     * （2）<security:authentication>：
     * 渲染当前用户认证对象的详细信息；
     * （3）<security:authorize>：
     * 如果用户被授予了特定的权限或者 SpEL 表达式的计算结果为 true，那么渲染该标签体中的内容。
     *
     * PS：Spring Security 通过 JSP 标签库在视图层上支持安全性。
     *
     * 为了使用 JSP 标签库，需要在对应的 JSP 中声明它：
     *
     * <%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
     *
     * 只要标签库在 JSP 文件中进行了声明，就可以使用它了。下面看看 Spring Security 提供的这三个标签是如何
     * 工作的。
     *
     *
     * 1.1、访问认证信息的细节
     *
     * 借助 Spring Security JSP 标签库，所能做到的最简单的一件事情就是便利地访问用户的认证信息。例如，对于
     * Web 站点来讲，在页面顶部以用户名标示显示 "欢迎" 或 "您好" 信息是很常见的。这恰恰是
     * <security:authentication> 所做的事情。例如：
     *
     * Hello <security:authentication property="principal.username" />!
     *
     * 其中，property 用来标示用户认证对象的一个属性。可用的属性取决于用户认证的方式。但是，可以依赖几个通用
     * 的属性，在不同的认证方式下，它们都是可用的，如下：
     * （1）authorities：一组用于表示用户所授予权限的 GrantedAuthority 对象；
     * （2）Credentials：用于核实用户的凭证（通常，这会是用户的密码）；
     * （3）details：认证的附加信息（IP 地址、证件序列号、会话 ID 等）；
     * （4）principal：用户的基本信息对象。
     *
     * 在这里，实际上渲染的是 principal 属性中嵌套的 username 属性。
     *
     * 当像前面示例那样使用时，<security:authentication> 将在视图中渲染属性的值。但是如果你愿意将其赋值给
     * 一个变量，那只需要在 var 属性中指明变量的名字即可。例如，如下展现了如何将其设置给名为 loginId 的属性：
     *
     * <security:authentication property="principal.username" var="loginId" />
     *
     * 这个变量默认是定义在页面作用域内的。但是如果你愿意在其他作用域内创建它，例如请求或会话作用域（或者是能
     * 够在 javax.servlet.jsp.PageContext 中获取的其他作用域），那么可以通过 scope 属性来声明。例如，要
     * 在请求作用域内创建这个变量，那可以使用 <security:authentication> 按照如下的方式来设置：
     *
     * <security:authentication property="principal.username" var="loginId" scope="request" />
     *
     * <security:authentication> 标签非常有用，但这只是 Spring Security JSP 标签库功能的基础功能。下面
     * 来看一下如何根据用户的权限来渲染内容。
     *
     *
     * 1.2、条件性的渲染内容
     *
     * 有时候视图上的一部分内容需要根据用户被授予了什么权限来确定是否渲染。对于已经登录的用户显示登录表单，或
     * 者对还未登录的用户显示个性化的问候信息都是毫无意义的。
     *
     * Spring Security 的 <security:authorize> JSP 标签能够根据用户被授予的权限有条件地渲染页面的部分
     * 内容。例如，在 Spittr 应用中，对于没有 ROLE_SPITTER 角色的用户，不会为其显示添加新 Spitter 记录
     * 的表单。 如下代码展现了如何使用 <security:authorize> 标签来为具有 ROLE_SPITTER 角色的用户显示
     * Spitter 表单。
     *
     * <sec:authorize access="hasRole('ROLE_SPITTER')">
     *     <s:url value="/spittles" var="spittle_url" />
     *     <sf:form modelAttribute="spittle" action="${spittle_url}">
     *         <sf:label path="text"><s:message code="label.spittle" text="Enter spittle:" /></sf:label>
     *         <sf:textarea path="text" rows="2" cols="40" />
     *         <sf:errors path="text" />
     *         <br/>
     *         <div class="spitItSubmitIt">
     *             <input type="submit" value="Spit it!" class="status-btn round-btn disabled" />
     *         </div>
     *     </sf:form>
     * </sec:authorize>
     *
     * access 属性被赋值为一个 SpEL 表达式，这个表达式的值将确定 <security:authorize> 标签主体内的内容
     * 是否渲染。这里使用了 hasRole('ROLE_SPITTER') 表达式来确保用户具有 ROLE_SPITTER 角色。但是，当你
     * 设置 access 属性时，可以任意发挥 SpEL 的强大威力，包括 Spring Security 所提供的任意表达式。
     *
     * 借助于这些可用的表达式，可以构造出非常有意思的安全性约束。例如，假设应用中有一些管理功能只能对用户名为
     * habuma 的用户可用。也许你会像这样使用 isAuthenticated() 和 principal 表达式：
     *
     * <security:authorize access="isAuthenticated() and principal.username=='habuma'">
     *     <a href="/admin">Administration</a>
     * </security:authorize>
     *
     * 相信你能设计出比这个更有意思的表达式，可以尽情发挥你的想象力来构造更多的安全性约束。借助于 SpEL，选择
     * 其实是无限的。
     *
     * 但是这里构造的这个示例还有一件事让人很困惑。尽管这里想限制管理功能只能给 habuma 用户，但使用 JSP 标签
     * 表达式并不见得理想。确实，它能在视图上阻止链接的渲染。但是没有什么可以阻止别人在浏览器的地址栏手动输入
     * "/admin" 这个 URL。
     *
     * 这是一个很容易解决的问题。在安全配置中，添加一个对 antMatchers() 方法的调用将会严格限制对 "/admin"
     * 这个 URL 的访问：
     *
     * .antMatchers("/admin").access("isAuthenticated() and principal.username=='habuma'");
     *
     * 现在，管理功能已经被锁定了。URL 地址得到了保护，并且到这个 URL 的链接在用户没有授权使用的情况下不会显示。
     * 但是为了做到这一点，需要在两个地方声明 SpEL 表达式 —— 在安全配置中以及在 <security:authorize> 标签
     * 的 access 属性中。有没有办法消除这种重复性，并且还要确保只有规则条件满足的情况下才渲染管理功能的链接呢？
     *
     * 这是 <security:authorize> 的 url 属性所要做的事情。它不像 access 属性那样明确声明安全性限制，url
     * 属性对一个给定的 URL 模式会间接引用其安全性约束。鉴于已经在 Spring Security 配置中为 "/admin" 声明了
     * 安全性约束，所以可以这样使用 url 属性：
     *
     * <security:authorize url="/admin">
     *     <spring:url value="/admin" var="admin_url" />
     *     <br/>
     *     <a href="${admin_url}">Admin</a>
     * </security:authorize>
     *
     * 因为只有基本信息中用户名为 "habuma" 的已认证用户才能访问 "/admin" URL，所以只有满足以上条件，
     * <security:authorize> 标签主体中的内容才会被渲染。这样就只在一个地方配置了表达式（安全配置中），
     * 但是在两个地方进行了应用。
     *
     * Spring Security 的 JSP 标签库非常便利，尤其是只给满足条件的用户渲染特定的视图元素时更是如此。
     * 如果选择 Thymeleaf 而不是 JSP 作为视图方案的话，其实还能延续这种好运气。已经知道 Thymeleaf
     * 的 Spring 方言能够自动为表单添加隐藏的 CSRF token，下面看一下 Thymeleaf 如何支持 Spring
     * Security。
     *
     *
     *
     * 2、使用 Thymeleaf 的 Spring Security 方言
     *
     * 与 Spring Security 的 JSP 标签库类似，Thymeleaf 的安全方言提供了条件化渲染和显示认证细节的能力。
     *
     * 如下列出了安全方言所提供的属性。
     * （1）sec:authentication：
     * 渲染认证对象的属性。类似于 Spring Security 的 <sec:authentication/> JSP 标签；
     * （2）sec:authorize：
     * 基于表达式的计算结果，条件性的渲染内容。类似于 Spring Security 的 <sec:authorize/> JSP 标签；
     * （3）sec:authorize-acl：
     * 基于表达式的计算结果，条件性的渲染内容。类似于 Spring Security 的 <sec:accesscontrollist/> JSP 标签；
     * （4）sec:authorize-expr：
     * sec:authorize 属性的别名；
     * （5）sec:authorize-url：
     * 基于给定 URL 路径相关的安全规则，条件性的渲染内容。类似于 Spring Security 的 <sec:authorize/> JSP
     * 标签使用 url 属性时的场景。
     *
     * 为了使用安全方言，需要确保 Thymeleaf Extras Spring Security 已经位于应用的类路径下。然后，还需要在
     * 配置中使用 SpringTemplateEngine 来注册 SpringSecurity Dialect。如下代码所展现的 @Bean 方法声明了
     * SpringTemplateEngine bean，其中就包含了 SpringSecurityDialect：
     *
     *     @Bean
     *     public SpringTemplateEngine templateEngine(TemplateResolver templateResolver) {
     *         SpringTemplateEngine templateEngine = new SpringTemplateEngine();
     *         templateEngine.setTemplateResolver(templateResolver);
     *         templateEngine.addDialect(new SpringSecurityDialect());
     *         return templateEngine;
     *     }
     *
     * 安全方言注册完成之后，就可以在 Thymeleaf 模板中使用它的属性了。首先，需要在使用这些属性的模板中声明安全
     * 命名空间：
     *
     * <html xmlns="http://www.w3.org/1999/xhtml"
     *       xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3"
     *       xmlns:th="http://www.thymeleaf.org">
     *      ...
     * </html>
     *
     * 在这里，标准的 Thymeleaf 方法依旧与之前一样，使用 th 前缀，安全方言则设置为使用 sec 前缀。
     *
     * 这样就能在任意合适的地方使用 Thymeleaf 属性了。比如，假设想要为认证用户渲染 "Hello" 文本。如下的 Thymeleaf
     * 模板代码片段就能完成这项任务：
     *
     * <div sec:authorize="isAuthenticated()">
     *     Hello <span sec:authentication="name">someone</span>
     * </div>
     *
     * sec:authorize 属性会接受一个 SpEL 表达式。如果表达式的计算结果为 true，那么元素的主体内容就会渲染。在本例中，
     * 表达式为 isAuthenticated()，所以只有用户已经进行了认证，才会渲染 <div> 标签的主体内容。就这个标签的主体内容
     * 部分而言，它的功能是使用认证对象的 name 属性提示 "Hello" 文本。
     *
     * 你可能还记得，在 Spring Security 中，借助 <sec:authorize> JSP 标签的 url 属性能够基于给定 URL 的权限有
     * 条件地渲染内容。在 Thymeleaf 中，可以通过 sec:authorize-url 属性完成相同的功能。例如，如下 Thymeleaf 代
     * 码片段所实现的功能与之前 <sec:authorize> JSP 标签和 url 属性所实现的功能是相同的：
     *
     * <span sec:authorize-url="/admin">
     *     <br/><a th:href="@{/admin}">Admin</a>
     * </span>
     *
     * 如果用户有权限访问 "/admin" 的话，那么到管理页面的链接就会渲染，否则的话，这个链接将不会渲染。
     */
    public static void main(String[] args) {

    }

}
