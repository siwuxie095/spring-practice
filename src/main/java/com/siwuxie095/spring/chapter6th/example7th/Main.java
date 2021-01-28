package com.siwuxie095.spring.chapter6th.example7th;

/**
 * @author Jiajing Li
 * @date 2021-01-28 08:13:30
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 使用 Thymeleaf
     *
     * 尽管 JSP 已经存在了很长的时间，并且在 Java Web 服务器中无处不在，但是它却存在一些缺陷。JSP 最明显的问题在于它
     * 看起来像 HTML 或 XML，但它其实上并不是。大多数的 JSP 模板都是采用 HTML 的形式，但是又掺杂上了各种 JSP 标签库
     * 的标签，使其变得很混乱。这些标签库能够以很便利的方式为 JSP 带来动态渲染的强大功能，但是它也摧毁了想要维持一个格
     * 式良好的文档的可能性。作为一个极端的样例，如下的 JSP 标签甚至作为 HTML 参数的值：
     *
     * <input type="text" value="<c:out value="${thing.name}" />" />
     *
     * 标签库和 JSP 缺乏良好格式的一个副作用就是它很少能够与其产生的 HTML 类似。所以，在 Web 浏览器或 HTML 编辑器中
     * 查看未经渲染的 JSP 模板是非常令人困惑的，而且得到的结果看上去也非常丑陋。这个结果是不完整的 —— 在视觉上这简直
     * 就是一场灾难！因为 JSP 并不是真正的 HTML，很多浏览器和编辑器展现的效果都很难在审美上接近模板最终所渲染出来的
     * 效果。
     *
     * 同时，JSP 规范是与 Servlet 规范紧密耦合的。这意味着它只能用在基于 Servlet 的 Web 应用之中。JSP 模板不能作为
     * 通用的模板（如格式化 Email），也不能用于非 Servlet 的 Web 应用。
     *
     * 多年来，在 Java 应用中，有多个项目试图挑战 JSP 在视图领域的统治性地位。最新的挑战者是 Thymeleaf，它展现了一些
     * 切实的承诺，是一项很令人兴奋的可选方案。Thymeleaf 模板是原生的，不依赖于标签库。它能在接受原始 HTML 的地方进行
     * 编辑和渲染。因为它没有与 Servlet 规范耦合，因此 Thymeleaf 模板能够进入 JSP 所无法涉足的领域。现在，看一下如何
     * 在 Spring MVC 中使用 Thymeleaf。
     *
     *
     *
     * 配置 Thymeleaf 视图解析器
     *
     * 为了要在 Spring 中使用 Thymeleaf，需要配置三个启用 Thymeleaf 与 Spring 集成的 bean：
     * （1）ThymeleafViewResolver：将逻辑视图名称解析为 Thymeleaf 模板视图；
     * （2）SpringTemplateEngine：处理模板并渲染结果；
     * （3）TemplateResolver：加载 Thymeleaf 模板。
     *
     * 如下为声明这些 bean 的 Java 配置：
     *
     *     @Bean
     *     public ViewResolver viewResolver(SpringTemplateEngine templateEngine) {
     *         ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
     *         viewResolver.setTemplateEngine(templateEngine);
     *         return viewResolver;
     *     }
     *
     *     @Bean
     *     public SpringTemplateEngine templateEngine(TemplateResolver templateResolver) {
     *         SpringTemplateEngine templateEngine = new SpringTemplateEngine();
     *         templateEngine.setTemplateResolver(templateResolver);
     *         return templateEngine;
     *     }
     *
     *     @Bean
     *     public TemplateResolver templateResolver() {
     *         TemplateResolver templateResolver = new ServletContextTemplateResolver();
     *         templateResolver.setPrefix("/WEB-INF/views/");
     *         templateResolver.setSuffix(".html");
     *         templateResolver.setTemplateMode("HTML5");
     *         return templateResolver;
     *     }
     *
     * 如果你更愿意使用 XML 来配置 bean，那么如下的 <bean> 声明能够完成该任务：
     *
     *     <bean id="viewResolver"
     *           class="org.thymeleaf.spring4.view.ThymeleafViewResolver"
     *           p:templateEngine-ref="templateEngine" />
     *
     *     <bean id="templateEngine"
     *           class="org.thymeleaf.spring4.SpringTemplateEngine"
     *           p:templateResolver-ref="templateResolver" />
     *
     *     <bean id="templateResolver"
     *           class="org.thymeleaf.templateresolver.ServletContextTemplateResolver"
     *           p:prefix="/WEB-INF/templates/"
     *           p:suffix=".html"
     *           p:templateMode="HTML5" />
     *
     * 不管使用哪种配置方式，Thymeleaf 都已经准备就绪了，它可以将响应中的模板渲染到 Spring MVC 控制器所处理的请求中。
     *
     * ThymeleafViewResolver 是 Spring MVC 中 ViewResolver 的一个实现类。像其他的视图解析器一样，它会接受一个逻
     * 辑视图名称，并将其解析为视图。不过在该场景下，视图会是一个 Thymeleaf 模板。
     *
     * 需要注意的是，ThymeleafViewResolver bean 中注入了一个对 SpringTemplateEngine bean 的引用。
     *
     * SpringTemplateEngine 会在 Spring 中启用 Thymeleaf 引擎，用来解析模板，并基于这些模板渲染结果。可以看到，这
     * 里为其注入了一个 TemplateResolver bean 的引用。
     *
     * TemplateResolver 会最终定位和查找模板。与之前配置 InternalResourceViewResolver 类似，它使用了 prefix 和
     * suffix 属性。前缀和后缀将会与逻辑视图名组合使用，进而定位 Thymeleaf 引擎。它的 templateMode 属性被设置成了
     * HTML 5，这表明预期要解析的模板会渲染成 HTML 5 输出。
     *
     * 所有的 Thymeleaf bean 都已经配置完成了，那么接下来该创建几个视图了。
     *
     *
     *
     * 定义 Thymeleaf 模板
     *
     * Thymeleaf 在很大程度上就是 HTML 文件，与 JSP 不同，它没有什么特殊的标签或标签库。Thymeleaf 之所以能够发挥作
     * 用，是因为它通过自定义的命名空间，为标准的 HTML 标签集合添加 Thymeleaf 属性。如下代码展现了 home.html，也就
     * 是使用 Thymeleaf 命名空间的首页模板。
     *
     * <html xmlns="http://www.w3.org/1999/xhtml"
     *       xmlns:th="http://www.thymeleaf.org">
     * <head>
     *     <title>Spitter</title>
     *     <link rel="stylesheet"
     *           th:href="@{/resources/style.css}"
     *           type="text/css"></link>
     * </head>
     * <body>
     * <div id="header" th:include="page :: header"></div>
     *
     * <div id="content">
     *     <h1>Welcome to Spitter</h1>
     *
     *     <a th:href="@{/spittles}">Spittles</a> |
     *     <a th:href="@{/spitter/register}">Register</a>
     *
     *     <br/>
     *
     *     View: <span th:text="${view}">unknown</span>
     * </div>
     * <div id="footer" th:include="page :: copy"></div>
     * </body>
     * </html>
     *
     * 首页模板相对来讲很简单，只使用了 th:href 属性。这个属性与对应的原生 HTML 属性很类似，也就是 href 属性，并且可
     * 以按照相同的方式来使用。th:href 属性的特殊之处在于它的值中可以包含 Thymeleaf 表达式，用来计算动态的值。它会渲
     * 染成一个标准的 href 属性，其中会包含在渲染时动态创建得到的值。这是 Thymeleaf 命名空间中很多属性的运行方式：它
     * 们对应标准的 HTML 属性，并且具有相同的名称，但是会渲染一些计算后得到的值。在本例中，使用 th:href 属性的三个地
     * 方都用到了 "@{}" 表达式，用来计算相对于 URL 的路径（就像在 JSP 页面中，可能会使用的 JSTL <c:url> 标签或
     * Spring <s:url> 标签类似）。
     *
     * 尽管 home.html 是一个相当简单的 Thymeleaf 模板，但是它依然很有价值，这在于它与纯 HTML 模板非常接近。唯一的区
     * 别之处在于 th:href 属性，否则的话，它就是基础且功能丰富的 HTML 文件。
     *
     * 这意味着 Thymeleaf 模板与 JSP 不同，它能够按照原始的方式进行编辑甚至渲染，而不必经过任何类型的处理器。当然，这
     * 里需要 Thymeleaf 来处理模板并渲染得到最终期望的输出。即便如此，如果没有任何特殊的处理，home.html 也能够加载到
     * Web 浏览器中，并且看上去与完整渲染的效果很类似。
     *
     * 对比 home.jsp 和 home.html 在 Web 浏览器中的显示效果。在 Web 浏览器中，JSP 模板的渲染效果很糟糕。尽管可以看
     * 到一些熟悉的元素，但是 JSP 标签库的声明也显示了出来。在链接前出现了一些令人费解的未闭合标记，这是 Web 浏览器没有
     * 正常解析 <s:url> 标签的结果。与之相反，Thymeleaf 模板的渲染效果基本上没有任何错误。稍微有点问题的是链接部分，
     * Web 浏览器并不会像处理 href 属性那样处理 th:href，所以链接并没有渲染为链接的样子。除了这些细微的问题，模板的渲
     * 染效果与预期完全符合。
     *
     * 像 home.jsp 这样的模板作为 Thymeleaf 入门是很合适的。但是 Spring 的 JSP 标签所擅长的是表单绑定。如果抛弃 JSP
     * 的话，那是不是也要抛弃表单绑定呢？不必担心。Thymeleaf 提供了与之相匹敌的功能。
     *
     *
     *
     * 借助 Thymeleaf 实现表单绑定
     *
     * 表单绑定是 Spring MVC 的一项重要特性。它能够将表单提交的数据填充到命令对象中，并将其传递给控制器，而在展现表单
     * 的时候，表单中也会填充命令对象中的值。如果没有表单绑定功能的话，需要确保 HTML 表单域要映射后端命令对象中的属性，
     * 并且在校验失败后展现表单的时候，还要负责确保输入域中值要设置为命令对象的属性。
     *
     * 但是，如果有表单绑定的话，它就会负责这些事情了。为了复习一下表单绑定是如何运行的，下面展现了在 registration.jsp
     * 中的 First Name 输入域：
     *
     *   <sf:label path="firstName"
     *       cssErrorClass="error">First Name</sf:label>:
     *     <sf:input path="firstName" cssErrorClass="error" /><br/>
     *
     * 在这里，调用了 Spring 表单绑定标签库的 <sf:input> 标签，它会渲染出一个 HTML <input> 标签，并且其 value 属
     * 性设置为后端对象 firstName 属性的值。它还使用了 Spring 的 <sf:label> 标签及其 cssErrorClass 属性，如果出
     * 现校验错误的话，会将文本标记渲染为红色。
     *
     * 但是，这里讨论的并不是 JSP，而是使用 Thymeleaf 替换 JSP。因此，不能使用 Spring 的 JSP 标签实现表单绑定，而
     * 是使用 Thymeleaf 的 Spring 方言。
     *
     * 作为阐述的样例，请参考如下的 Thymeleaf 模板片段，它会渲染 First Name 输入域：
     *
     *         <label th:class="${#fields.hasErrors('firstName')}? 'error'">First Name</label>:
     *         <input th:class="${#fields.hasErrors('firstName')}? 'error'" th:field="*{firstName}"
     *                type="text"/><br/>
     *
     * 在这里，不再使用 Spring JSP 标签中的 cssClassName 属性，而是在标准的 HTML 标签上使用 th:class 属性。th:class
     * 属性会渲染为一个 class 属性，它的值是根据给定的表达式计算得到的。在上面的这两个 th:class 属性中，它会直接检查
     * firstName 域有没有校验错误。如果有的话，class 属性在渲染时的值为 error。如果这个域没有错误的话，将不会渲染
     * class 属性。
     *
     * <input> 标签使用了 th:field 属性，用来引用后端对象的 firstName 域。这可能与你的预期有点差别。在 Thymeleaf
     * 模板中，在很多情况下所使用的属性都对应于标准的 HTML 属性，因此貌似使用 th:value 属性来设置 <input> 标签的
     * value 属性才是合理的。
     *
     * 其实不然，因为这里是在将这个输入域绑定到后端对象的 firstName 属性上，因此使用 th:field 属性引用 firstName 域。
     * 通过使用 th:field，将 value 属性设置为 firstName 的值，同时还会将 name 属性设置为 firstName。
     *
     * 为了阐述 Thymeleaf 是如何实际运行的，可以参考完整的注册表单模板 registerForm.html。
     *
     * 其中使用了相同的 Thymeleaf 属性和 "*{}" 表达式，为所有的表单域绑定后端对象。这其实重复了在 First Name 域中
     * 所做的事情。
     *
     * 但是，需要注意在表单的顶部了也使用了 Thymeleaf，它会用来渲染所有的错误。<div> 元素使用 th:if 属性来检查是否有
     * 校验错误。如果有的话，会渲染 <div>，否则的话，它将不会渲染。
     *
     * 在 <div> 中，会使用一个无顺序的列表来展现每项错误。<li> 标签上的 th:each 属性将会通知 Thymeleaf 为每项错误都
     * 渲染一个 <li>，在每次迭代中会将当前错误设置到一个名为 err 的变量中。
     *
     * <li> 标签还有一个 th:text 属性。这个命令会通知 Thymeleaf 计算某一个表达式（在本例中，也就是 err 变量）并将它
     * 的值渲染为 <li> 标签的内容体。实际上的效果就是每项错误对应一个 <li> 元素，并展现错误的文本。
     *
     * 你可能会想知道 "${}" 和 "*{}" 括起来的表达式到底有什么区别。"${}" 表达式（如 ${spitter}）是变量表达式
     * （variable expression）。一般来讲，它们会是对象图导航语言（Object-Graph Navigation Language，OGNL）表达
     * 式。但在使用 Spring 的时候，它们是 SpEL 表达式。在 ${spitter} 这个例子中，它会解析为 key 为 spitter 的
     * model 属性。
     *
     * PS：OGNL 可参考 http://commons.apache.org/proper/commons-ognl/
     *
     * 而对于 "*{}" 表达式，它们是选择表达式（selection expression）。变量表达式是基于整个 SpEL 上下文计算的，而选
     * 择表达式是基于某一个选中对象计算的。在本例的表单中，选中对象就是 <form> 标签中 th:object 属性所设置的对象：模
     * 型中的 Spitter 对象。因此，"*{firstName}" 表达式就会计算为 Spitter 对象的 firstName 属性。
     */
    public static void main(String[] args) {

    }

}
