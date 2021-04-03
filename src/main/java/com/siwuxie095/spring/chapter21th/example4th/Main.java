package com.siwuxie095.spring.chapter21th.example4th;

/**
 * @author Jiajing Li
 * @date 2021-04-03 12:59:41
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 组合使用 Groovy 与 Spring Boot CLI
     *
     * Groovy 编程语言要比 Java 简单得多。它的语法允许有一些快捷方式，比如省略分号和 public 关键词。同时，
     * Groovy 类中的属性不像 Java 那样需要 Setter 和 Getter 方法。当然，Groovy 还有其他的一些属性，能
     * 够消除 Java 代码中很多的繁文缛节。
     *
     * 如果你愿意使用 Groovy 编写应用代码并通过 Spring Boot CLI 运行的话，那么 Spring Boot 能够借助
     * Groovy 的简洁性进一步简化 Spring 应用。为了阐述这一点，这里使用 Groovy 来重新编写 Contacts 应
     * 用程序。
     *
     * 为什么不呢？在初始版本的应用中，只有几个小的 Java 类，因此使用 Groovy 进行重写也没有太多的工作量。
     * 这里可以重用相同的 Thymeleaf 模板和 schema.sql 文件。既然这里宣称 Groovy 能够进一步简化 Spring，
     * 那重写应用也不是什么大事儿。
     *
     * 在这个过程中，还会移除一些代码。Spring Boot CLI 本身就是启动器，所以不再需要创建的 Application 类。
     * Maven 和 Gradle 构建文件也不再需要了，因为将会通过 CLI 运行未编译的 Groovy 文件。少了 Maven 和
     * Gradle 之后，项目的整体结构将会变得更加扁平化。
     *
     * schema.sql、style.css 和 home.html 将会保持原样，但是需要将 Java 类转换为 Groovy。这里先从使用
     * Groovy 编写 Web 层开始。
     *
     *
     *
     * 1、编写 Groovy 控制器
     *
     * 如前所述，Groovy 不像 Java 那样有很多的繁文缛节。这意味着在编写 Groovy 代码的时候，可以省略如下的
     * 内容：
     * （1）分号；
     * （2）像 public 和 private 这样的修饰符；
     * （3）属性的 Setter 和 Getter 方法；
     * （4）方法返回值的 return 关键字。
     *
     * 借助 Groovy 更加灵活的语法（以及 Spring Boot 的魔力），可以使用 Groovy 重写 ContactController
     * 类，如下所示。
     *
     * @Grab("thymeleaf-spring4")
     *
     * @Controller
     * @RequestMapping("/")
     * class ContactController {
     *
     *     @Autowired
     *     ContactRepository contactRepo
     *
     *     @RequestMapping(method = RequestMethod.GET)
     *     String home(Map<String, Object> model) {
     *         List<Contact> contacts = contactRepo.findAll()
     *         model.put("contacts", contacts)
     *         "home"
     *     }
     *
     *     @RequestMapping(method = RequestMethod.POST)
     *     String submit(Contact contact) {
     *         contactRepo.save(contact)
     *         "redirect:/"
     *     }
     *
     * }
     *
     * 可以看到，这个版本的 ContactController 要比对应的 Java 版本更加简洁。排除掉 Groovy 不需要的内容
     * 后，ContactController 更加简短也更易于阅读。
     *
     * 这段代码还移除了一些内容，你可能也发现了，这里没有 import 代码行，在 Java 代码中这是很常见的。
     * Groovy 默认会导入一些包和类，包括：
     * （1）java.io.*
     * （2）java.lang.*
     * （3）java.math.BigDecimal
     * （4）java.math.BigInteger
     * （5）java.net.*
     * （6）java.util.*
     * （7）groovy.lang.*
     * （8）groovy.util.*
     *
     * 因为有了这些默认的导入，所以 ContactController 就不需要导入 List 类了。这个类位于 java.util 包
     * 中，包含在默认的导入里面。
     *
     * 但是，像 @Controller、@RequestMapping、@Autowired 以及 @RequestMethod 这样的 Spring 类型该
     * 怎么处理呢？它们没有位于默认的导入中，该如何省略 import 代码行呢？
     *
     * 稍后，当运行应用的时候，Spring Boot CLI 将会试图使用 Groovy 编译器编译这些 Groovy 类。因为这些
     * 类型没有导入进来，所以将会导致编译失败。
     *
     * 但是，Spring Boot CLI 却不会就这样轻易放弃，在这里 CLI 将自动配置达到了一个新高度。CLI 将会识别出
     * 失败是因为缺少 Spring 类型，它会采取两个步骤来修正这个问题。首先会获取 Spring Boot Web Starter
     * 依赖并将其依赖的其他内容都添加到类路径下（这样会下载并添加 JAR 到类路径下）。然后，它会将必要的包添加
     * 到 Groovy 编译器的默认导入列表中，然后重新尝试编译代码。
     *
     * CLI 这种自动添加依赖/自动导入的结果就是控制器类不需要任何的 import 语句了，并且没有必要再手动或者通
     * 过 Maven、Gradle 来解析 Spring 库。Spring Boot CLI 将会完成所有的事情。
     *
     * 现在，后退一步，考虑一下这里都发生了什么。通过在代码中使用 Spring MVC 类型，如 @Controller 或
     * @RequestMapping 注解，CLI 将会自动解析 Spring Boot Web Starter 依赖。将 Web Starter 的依赖
     * 传递添加到类路径之后，Spring Boot 的自动配置将会发挥作用，它会自动配置 Spring MVC 功能所需的 bean。
     * 不过，在这里需要做的仅仅是使用这些类型，Spring Boot 将会处理所有的事情。
     *
     * 当然，CLI 的功能也会有一些限制。尽管它知道如何解析众多的 Spring 依赖，并且能够自动将很多 Spring 类
     * 型（以及很多其他的库）添加到导入中，但是它不能自动解析和导入所有的功能。例如，使用 Thymeleaf 模板是
     * 一个可替换的方案，所以要在代码中通过 @Grab 显示声明。
     *
     * 还要注意，很多的依赖都没有必要指定 group ID 和版本号。Spring Boot 将会在解析 @Grab 依赖的时候参与
     * 进来，将缺失的 group ID 和版本号添加上。
     *
     * 借助 @Grab 注解，这里声明了要使用 Thymeleaf，这会触发自动配置功能，将会自动配置在 Spring MVC 中支
     * 持 Thymeleaf 模板所需的 bean。
     *
     * 尽管 Contact 类与 Spring Boot 没有太大关系，但为了样例的完整性，这里还是将它的 Groovy 代码展现在
     * 了下面：
     *
     * class Contact {
     *
     *     long id
     *     String firstName
     *     String lastName
     *     String phoneNumber
     *     String emailAddress
     *
     * }
     *
     * 可以看到，Contact 也更加简洁，没有分号、存取器方法以及像 public 和 private 这样的修饰符。这完全归
     * 功于 Groovy 简单的语法，其实 Spring Boot 并没有参与简化 Contact 类。
     *
     * 接下来，看一下如何借助 Spring Boot CLI 和 Groovy 来简化 Repository 类。
     *
     *
     *
     * 2、使用 Groovy Repository 实现数据持久化
     *
     * ContactController 中所用到的 Groovy 和 Spring Boot CLI 技巧都可以应用到 ContactRepository
     * 中。如下代码展现了 Groovy 版本的 ContactRepository。
     *
     * @Grab("h2")
     *
     * import java.sql.ResultSet
     *
     * @Repository
     * public class ContactRepository {
     *
     *     @Autowired
     *     JdbcTemplate jdbc
     *
     *     List<Contact> findAll() {
     *         jdbc.query(
     *                 "select id, firstName, lastName, phoneNumber, emailAddress " +
     *                         "from contacts order by lastName",
     *                 new RowMapper<Contact>() {
     *
     *                     Contact mapRow(ResultSet rs, int rowNum) throws SQLException {
     *                         new Contact(id : rs.getLong(1), firstName : rs.getString(2),
     *                              lastName : rs.getString(3), phoneNumber : rs.getString(4),
     *                              emailAddress : rs.getString(5))
     *                     }
     *                 }
     *         );
     *     }
     *
     *     void save(Contact contact) {
     *         jdbc.update(
     *                 "insert into contacts " +
     *                         "(firstName, lastName, phoneNumber, emailAddress) " +
     *                         "values (?, ?, ?, ?)",
     *                 contact.getFirstName(), contact.getLastName(),
     *                 contact.getPhoneNumber(), contact.getEmailAddress())
     *     }
     *
     * }
     *
     * 除了 Groovy 在语法方面带来的明显改善，这个新版的 ContactRepository 类使用了 Spring Boot CLI 自
     * 动导入 JdbcTemplate 和 RowMapper。除此之外，当 CLI 发现使用这些类型的时候，将会自动解析 JDBC
     * Starter 依赖。
     *
     * 只有两件事情是 CLI 的自动导入和自动解析无法帮助的。可以看到，这里依然需要导入 ResultSet。另外，
     * Spring Boot 无法知道使用哪种数据库，因此必须要使用 @Grab 注解添加 H2 数据库。
     *
     * 已经将所有 Java 类转换成了 Groovy 并在这个过程中发挥了 Spring Boot 的魔力。现在，可以运行应用了。
     *
     *
     *
     * 3、运行 Spring Boot CLI
     *
     * 在编译完 Java 应用之后，有两种方法来运行它。可以按照可执行 JAR 或 WAR 文件的形式在命令行运行，也可
     * 以将 WAR 文件部署到 Servlet 容器中运行。Spring Boot CLI 提供了第三种可选方案。
     *
     * 从名字应该也能猜得出来，通过 Spring Boot CLI 运行应用需要使用命令行。但是，借助 CLI，不需要首先将
     * 应用构建为 JAR 或 WAR 文件。运行应用的时候，可以直接将 Groovy 源码传给 CLI。
     *
     *
     * 3.1、安装 CLI
     *
     * 为了使用 Spring Boot CLI，需要安装它。有多种方案可供选择，包括：
     * （1）Groovy 环境管理器（Groovy Environment Manager，GVM）；
     * （2）Homebrew；
     * （3）手动安装。
     *
     * 如果使用 GVM 安装 CLI 的话，输入以下命令：
     *
     * gvm install springboot
     *
     * 如果使用 OS X 的话，可以使用 Homebrew 来安装 Spring Boot CLI：
     *
     * brew tap pivotal/tap
     * brew install springboot
     *
     * 如果愿意手动安装 Spring Boot 的话，那么可以下载并按照 Spring Boot 站点的指南进行安装。
     *
     * PS：站点链接：https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/
     *
     * CLI 安装完成之后，可以使用如下的命令检查安装情况以及当前使用的是哪个版本：
     *
     * spring --version
     *
     * 假设安装没有问题的话，那就可以运行 Contacts 应用了。
     *
     *
     * 2、使用 CLI 运行 Contacts 应用
     *
     * 要使用 Spring Boot CLI 运行应用的话，需要在命令行输入 spring run，然后后面再加上要通过 CLI 运行
     * 的一个或多个 Groovy 文件。例如，如果应用只有一个 Groovy 文件的话，那么可以这样运行：
     *
     * spring run Hello.groovy
     *
     * 这样就会通过CLI运行一个名为 Hello.groovy 的 Groovy 类。
     *
     * 如果你的应用有多个 Groovy 类文件的话，那么可以通过通配符来运行：
     *
     * spring run *.groovy
     *
     * 或者，如果这些 Groovy 类文件位于同一个或多个子目录下，那么可以使用 Ant 风格的通配符递归查找 Groovy
     * 类：
     *
     * spring run **斜杠*.groovy
     *
     * 因为 Contacts 应用有三个需要读取的 Groovy 类，而且它们都位于项目的根目录下，所以上述的后两种方案都
     * 是可行的。在运行应用之后，就能够在浏览器中访问 http://localhost:8080，并且能够在浏览器中看到与之前
     * 相同的 Contacts 应用。
     *
     * 到此为止，以两种方式编写了 Spring Boot 应用：一种使用 Java，另一种使用 Groovy。在这两种情况中，
     * Spring Boot 在最小化模板配置以及构建依赖方面都发挥了很大的作用。Spring Boot 还有另外一项功能。
     * 后续将会介绍如何借助 Spring Boot Actuator 为 Web 应用引入管理端点。
     */
    public static void main(String[] args) {

    }

}
