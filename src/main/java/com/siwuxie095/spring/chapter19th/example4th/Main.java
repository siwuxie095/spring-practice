package com.siwuxie095.spring.chapter19th.example4th;

/**
 * @author Jiajing Li
 * @date 2021-03-30 21:54:21
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 使用模板生成 Email
     *
     * 使用字符串拼接来构建 Email 消息的问题在于 Email 最终会是什么样子并不清晰。在你的大脑中解析 HTML 标签并想象
     * 它在渲染时会是什么样子是挺困难的。而将 HTML 混合在 Java 代码中又会使得这个问题更加复杂。如果能够将 Email
     * 的布局抽取到一个模板中，而这个模板可以由美术设计师（可能是很讨厌 Java 代码的人）来完成将会是很棒的一件事。
     *
     * 这里需要与最终 HTML 接近的方式来表达 Email 布局，然后将模板转换成 String 并传递给 helper 的 setText()
     * 方法。在将模板转换为 String 时，有多种模板方案可供选择，包括 Apache Velocity 和 Thymeleaf。下面看一下如
     * 何使用这两种方案创建富文本的 Email 消息，先从 Velocity 开始吧。
     *
     *
     *
     * 1、使用 Velocity 构建 Email 消息
     *
     * Apache Velocity 是由 Apache 提供的通用模板引擎。Velocity 有挺长的历史了，并且已经应用于各种任务中，包括
     * 代码生成以及代替 JSP。它还能用于格式化富文本 Email 消息，也就是在这里的用法。
     *
     * 为了使用 Velocity 对 Email 进行布局，需要将 VelocityEngine 装配到 SpitterEmailServiceImpl 中。
     * Spring 提供了一个名为 VelocityEngineFactoryBean 的工厂 bean，它能够在 Spring 应用上下文中很便利
     * 地生成 VelocityEngine。VelocityEngineFactoryBean 的声明如下：
     *
     *     @Bean
     *     public VelocityEngineFactoryBean velocityEngine() {
     *         VelocityEngineFactoryBean velocityEngine = new VelocityEngineFactoryBean();
     *         Properties props = new Properties();
     *         props.setProperty("resource.loader", "class");
     *         props.setProperty("class.resource.loader.class", ClasspathResourceLoader.class.getName());
     *         velocityEngine.setVelocityProperties(props);
     *         return velocityEngine;
     *     }
     *
     * VelocityEngineFactoryBean 唯一要设置的属性是 velocityProperties。在本例中，将其配置为从类路径下加载
     * Velocity 模板。
     *
     * 现在，我们可以将 Velocity 引擎装配到 SpitterEmailServiceImpl 中。因为 SpitterEmailServiceImpl 是使用
     * 组件扫描实现自动注册的，这里可以使用 @Autowired 来自动装配 velocityEngine 属性：
     *
     * @Autowired
     * VelocityEngine velocityEngine;
     *
     * 现在，velocityEngine 属性可用了，可以使用它将 Velocity 模板转换为 String，并作为 Email 文本进行发送。为
     * 了帮助完成这一点，Spring 自带了 VelocityEngineUtils 来简化将 Velocity 模板与模型数据合并成 String 的工
     * 作。以下是可能的使用方式：
     *
     * Map<String, String> model = new HashMap<>();
     * model.put("spitterName", spitterName);
     * model.put("spittleText", spittle.getText());
     * String emailText = VelocityEngineUtils.mergeTemplateIntoString(
     *          velocityEngine, "emailTemplate.vm", model);
     *
     * 为了给处理模板做准备，首先创建了一个 Map 用来保存模板使用的模型数据。在之前字符串拼接的代码中，需要 Spitter
     * 的全名及其 Spittle 的文本，这里也是一样。为了产生合并后的 Email 文本，只需调用 VelocityEngineUtils 的
     * mergeTemplateIntoString() 方法并将 Velocity 引擎、模板路径（相对于类路径根）以及模型 Map 传递进去。
     *
     * 在 Java 代码中剩下的事情就是得到合并后的 Email 文本，并将其传递给 helper 的 setText() 方法：
     *
     * helper.setText(emailText);
     *
     * 模板位于类路径的根目录下，是一个名为 emailTemplate.vm 的文件，它看起来可能是这样的：
     *
     * <html>
     *     <body>
     *         <img src='cid:spitterLogo'>
     *         <h4>${spitterName} says...</h4>
     *         <i>${spittleText}</i>
     *     </body>
     * </html>
     *
     * 可以看到，模板文件比之前的字符串拼接版本读起来容易多了。因此，它也更容易维护和编辑。
     *
     * Velocity 作为模板引擎已经存在好多年了，并且适用于很多种任务。但是，一种新的模板方案正在变得日益流行。接下来，
     * 看一下如何使用 Thymeleaf 来构建 Spittle Email 消息。
     *
     *
     *
     * 2、使用 Thymeleaf 构建 Email 消息
     *
     * Thymeleaf 是一种很有吸引力的 HTML 模板引擎，因为它能够创建 WYSIWYG 的模板。与 JSP 和 Velocity 不同，
     * Thymeleaf 模板不包含任何特殊的标签库和特有的标签。这样模板设计师在工作的时候，能够使用任意他们所喜欢的
     * HTML 工具，而不必担心某个工具无法处理特定的标签。
     *
     * 当将 Email 模板转换为 Thymeleaf 模板时，Thymeleaf 的 WYSIWYG 特性体现得非常明显：
     *
     * <!DOCTYPE html>
     * <html>
     *     <body>
     *         <img src="spitterLogo.png" th:src='cid:spitterLog'>
     *         <h4><span th:text="${spitterName}">Craig Walls</span> says...</h4>
     *         <i><span th:text="${spittleText}">Hello there!</span></i>
     *     </body>
     * </html>
     *
     * 注意，这里没有任何自定义的标签（在 JSP 中可能会见到这种情况）。尽管模型属性是通过 "${}" 标记的，但是它们仅用
     * 于属性的值中，不会像 Velocity 那样用在外边。这种模板可以很容易地在 Web 浏览器中打开，并且以完整的形式进行展
     * 现，不必依赖于 Thymeleaf 引擎的处理。
     *
     * 使用 Thymeleaf 来生成和发送 Email 消息的做法非常类似于 Velocity：
     *
     * Context ctx = new Context();
     * ctx.setVariable("spitterName", spitterName);
     * ctx.setVariable("spittleText", spittle.getText());
     * String emailText = thymeleaf.process("emailTemplate.html", ctx);
     * ...
     * helper.setText(emailText, true);
     * mailSender.sendMessage(message);
     *
     * 这里做的第一件事情就是创建 Thymeleaf Context 实例，并将模型数据填充进去。这与使用 Velocity 的时候，将模型
     * 数据填充到 Map 中很类似。然后，要求 Thymeleaf 处理模板，通过调用 Thymeleaf 引擎的 process() 方法，将上下
     * 文中的模型数据合并到模板中。最后，将结果形成的文本借助消息 helper 设置到 Email 消息中，并使用邮件发送器将消
     * 息发送出去。
     *
     * 这看起来很简单。但是 Thymeleaf 引擎（也就是 thymeleaf 变量）是从哪里来的呢？
     *
     * 这里的 Thymeleaf 引擎使用的是 SpringTemplateEngine bean。
     *
     * @Autowired
     * SpringTemplateEngine thymeleaf;
     *
     * 不过，这里必须要对 SpringTemplateEnginebean 做一点小修改。在之前，它配置为从 Servlet 上下文中解析模板，
     * 而这里的 Email 模板需要从类路径中解析。所以，除了 ServletContextTemplateResolver，还需要一个
     * ClassLoaderTemplateResolver：
     *
     *     @Bean
     *     public ClassLoaderTemplateResolver emailTemplateResolver() {
     *         ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
     *         resolver.setPrefix("mail/");
     *         resolver.setTemplateMode("HTML5");
     *         resolver.setCharacterEncoding("UTF-8");
     *         resolver.setOrder(1);
     *         return resolver;
     *     }
     *
     * 就大部分而言，配置 ClassLoaderTemplateResolver bean 的方式类似于 ServletContextTemplateResolver。不
     * 过，需要注意，这里将 prefix 属性设置为 "mail/"，这表明它会在类路径根的 "mail" 目录下开始查找 Thymeleaf 模
     * 板。因此，Email 模板文件的名字必须是 emailTemplate.html，并且位于类路径根的 "mail" 目录下。
     *
     * 因为现在有两个模板解析器，所以需要使用 order 属性表明优先使用哪一个。ClassLoaderTemplateResolver 的 order
     * 属性为 1，因此修改一下 ServletContextTemplateResolver，将其 order 属性设置为 2：
     *
     *     @Bean
     *     public ServletContextTemplateResolver webTemplateResolver() {
     *         ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
     *         resolver.setPrefix("/WEB-INF/templates/");
     *         resolver.setTemplateMode("HTML5");
     *         resolver.setCharacterEncoding("UTF-8");
     *         resolver.setOrder(2);
     *         return resolver;
     *     }
     *
     * 现在，剩下的任务就是修改 SpringTemplateEngine bean 的配置，让它使用这两个模板解析器：
     *
     *     @Bean
     *     public SpringTemplateEngine templateEngine(Set<ITemplateResolver> resolvers) {
     *         SpringTemplateEngine engine = new SpringTemplateEngine();
     *         engine.setTemplateResolvers(resolvers);
     *         return engine;
     *     }
     *
     * 在此之前，只有一个模板解析器，所以可以将其注入到 SpringTemplateEngine 的 templateResolver 属性中。但现在
     * 有了两个模板解析器，所以必须将它们作为 Set 的成员，然后将这个 Set 注入到 templateResolvers（复数）属性中。
     */
    public static void main(String[] args) {

    }

}
