package com.siwuxie095.spring.chapter21th.example3rd;

/**
 * @author Jiajing Li
 * @date 2021-04-03 10:28:43
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 使用 Spring Boot 构建应用
     *
     * 这里将会向你展现如何使用 Spring Boot 构建完整且符合现实要求（real-world）的应用程序。当然，"符合现实要求"
     * 的定义标准会有些争议，对它的讨论超出了这里的范围。因此，与其在这里说构建符合现实要求的应用，还不如后退一步，
     * 说成所构建的应用程序比现实要求稍差一点，但是它能够代表使用 Spring Boot 所构建的更大型应用。
     *
     * 这里的应用是一个简单的联系人列表。它允许用户输入联系人信息（名字、电话号码以及 Email 地址），并且能够列出用户
     * 之前输入的所有联系人信息。
     *
     * 你可以自由选择使用 Maven 还是 Gradle 来构建应用程序。对于 Maven 来说，如下是所需的 parent 依赖：
     *
     *     <parent>
     *         <groupId>org.springframework.boot</groupId>
     *         <artifactId>spring-boot-starter-parent</artifactId>
     *         <version>1.4.3.RELEASE</version>
     *         <relativePath/> <!-- lookup parent from repository -->
     *     </parent>
     *
     * 这里让项目的 Maven 构建基于 Spring Boot starter parent，这样的话，就能受益于 Maven 的依赖管理功能，对于
     * 项目中的很多依赖，就没有必要明确声明版本号了，因为版本号会从 parent 中继承得到。
     *
     *
     *
     * 2、处理请求
     *
     * 因为要使用 Spring MVC 来开发应用的 Web 层，因此需要将 Spring MVC 作为依赖添加到构建中。Spring Boot 的
     * Web Starter 能够将 Spring MVC 需要的所有内容一站式添加到构建中。如下是所需的 Maven 依赖：
     *
     *         <dependency>
     *             <groupId>org.springframework.boot</groupId>
     *             <artifactId>spring-boot-starter-web</artifactId>
     *         </dependency>
     *
     * 注意，因为 Spring Boot parent 项目已经指定了 Web Starter 依赖的版本，因此在项目的 pom.xml 文件中没有必
     * 要再显式指定版本信息。
     *
     * Web Starter 依赖就绪之后，使用 Spring MVC 需要的所有依赖都会添加到项目中。现在，就可以编写应用所需的控制器
     * 类了。
     *
     * 控制器相对会非常简单，包含展现联系人表单的 HTTP GET 请求以及处理表单提交的 POST 请求。它本身并没有做太多的
     * 事情，而是委托 ContactRepository（稍后就会创建它）来持久化联系人信息。如下的 ContactController 就能满足
     * 这些需求。
     *
     * @Controller
     * @RequestMapping("/")
     * public class ContactController {
     *
     *     private ContactRepository contactRepo;
     *
     *     @Autowired
     *     public ContactController(ContactRepository contactRepo) {
     *         this.contactRepo = contactRepo;
     *     }
     *
     *     @RequestMapping(method = RequestMethod.GET)
     *     public String home(Map<String, Object> model) {
     *         List<Contact> contacts = contactRepo.findAll();
     *         model.put("contacts", contacts);
     *         return "home";
     *     }
     *
     *     @RequestMapping(method = RequestMethod.POST)
     *     public String submit(Contact contact) {
     *         contactRepo.save(contact);
     *         return "redirect:/";
     *     }
     *
     * }
     *
     * 你首先可能会发现 ContactController 就是一个典型的 Spring MVC 控制器。尽管 Spring Boot 会管理构建依赖并
     * 最小化 Spring 配置，但是在编写应用逻辑的时候，编程模型是一致的。
     *
     * 在本例中，ContactController 遵循了 Spring MVC 控制器的典型模式，它会展现表单并处理表单的提交。其中 home()
     * 方法使用注入的 ContactRepository 来获取所有 Contact 对象的列表，并将它们放到模型中，然后把请求转交给 home
     * 视图。这个视图将会展现联系人的列表以及添加新 Contact 的表单。submit() 方法将会处理表单提交的 POST 请求，保
     * 存 Contact，并重定向到首页。
     *
     * 因为 ContactController 使用了 @Controller 注解，所以组件扫描将会找到它。因此，不需要在 Spring 应用上下文
     * 中明确将其声明为 bean。
     *
     * 而 Contact 模型类是一个简单的 POJO，具有一些属性和存取器方法，如下所示。
     *
     * public class Contact {
     *
     *     private Long id;
     *     private String firstName;
     *     private String lastName;
     *     private String phoneNumber;
     *     private String emailAddress;
     *
     *     public Long getId() {
     *         return id;
     *     }
     *
     *     public void setId(Long id) {
     *         this.id = id;
     *     }
     *
     *     public String getFirstName() {
     *         return firstName;
     *     }
     *
     *     public void setFirstName(String firstName) {
     *         this.firstName = firstName;
     *     }
     *
     *     public String getLastName() {
     *         return lastName;
     *     }
     *
     *     public void setLastName(String lastName) {
     *         this.lastName = lastName;
     *     }
     *
     *     public String getPhoneNumber() {
     *         return phoneNumber;
     *     }
     *
     *     public void setPhoneNumber(String phoneNumber) {
     *         this.phoneNumber = phoneNumber;
     *     }
     *
     *     public String getEmailAddress() {
     *         return emailAddress;
     *     }
     *
     *     public void setEmailAddress(String emailAddress) {
     *         this.emailAddress = emailAddress;
     *     }
     *
     * }
     *
     * 应用程序的 Web 层基本上已经完成了，剩下的就是创建定义 home 视图的 Thymeleaf 模板。
     *
     *
     *
     * 3、创建视图
     *
     * 按照传统的方式，Java Web 应用会使用 JSP 作为视图层的技术。但是，在这个领域有一个新的参与者。Thymeleaf 的
     * 原生模板比 JSP 更加便于使用，而且它能够以 HTML 的形式编写模板。鉴于此，这里将会使用 Thymeleaf 来定义
     * Contacts 应用的 home 视图。
     *
     * 首先，需要将 Thymeleaf 添加到项目的构建中。在本例中，使用的是 Spring 4，所以需要将 Thymeleaf 的 Spring
     * 4 模块添加到构建之中。如下是所需的 Maven 依赖：
     *
     *         <dependency>
     *             <groupId>org.thymeleaf</groupId>
     *             <artifactId>thymeleaf-spring4</artifactId>
     *         </dependency>
     *
     * 需要记住的是，只要将 Thymeleaf 添加到项目的类路径下，就启用了 Spring Boot 的自动配置。当应用运行时，Spring
     * Boot 将会探测到类路径中的 Thymeleaf，然后会自动配置视图解析器、模板解析器以及模板引擎，这些都是在 Spring MVC
     * 中使用 Thymeleaf 所需要的。因此，在这里的应用中，不需要使用显式 Spring 配置的方式来定义 Thymeleaf。
     *
     * 除了将 Thymeleaf 依赖添加到构建中，剩下所需要做的就是定义视图模板。如下展现了 home.html，这是定义 home 视图
     * 的 Thymeleaf 模板。
     *
     * <!DOCTYPE html>
     * <html xmlns:th="http://www.thymeleaf.org">
     * <head>
     *     <title>Spring Boot Contacts</title>
     *     <link rel="stylesheet" th:href="@{/style.css}"/>
     * </head>
     *
     * <body>
     * <h2>Spring Boot Contacts</h2>
     * <form method="POST">
     *     <label for="firstName">First Name:</label>
     *     <input name="firstName" type="text"></input><br/>
     *     <label for="lastName">Last Name:</label>
     *     <input name="lastName" type="text"></input><br/>
     *     <label for="phoneNumber">Phone #:</label>
     *     <input name="phoneNumber" type="text"></input><br/>
     *     <label for="emailAddress">Email:</label>
     *     <input name="emailAddress" type="text"></input><br/>
     *     <input type="submit"></input>
     * </form>
     *
     * <ul th:each="contact : ${contacts}">
     *     <li>
     *         <span th:text="${contact.firstName}">First</span>
     *         <span th:text="${contact.lastName}">Last</span> :
     *         <span th:text="${contact.phoneNumber}">phoneNumber</span>,
     *         <span th:text="${contact.emailAddress}">emailAddress</span>
     *     </li>
     * </ul>
     * </body>
     * </html>
     *
     * 它实际上是一个非常简单的 Thymeleaf 模板，分为两部分：一个表单和一个 联系人的列表。表单将会 POST 数据到
     * ContactController 的 submit() 方法上，用来创建新的 Contact。列表部分将会循环列出模型中的 Contact
     * 对象。
     *
     * 为了使用这个模板，需要对其进行慎重地命名并放在项目的正确位置下。因为 ContactController 中 home() 方法
     * 所返回的逻辑视图名为 home，因此模板文件应该命名为 home.html，自动配置的模板解析器会在指定的目录下查找
     * Thymeleaf 模板，这个目录也就是相对于根类路径下的 templates 目录下，所以在 Maven 或 Gradle 项目中，需
     * 要将 home.html 放到 "src/main/resources/templates" 中。
     *
     * 这个模板还有一点小事情需要处理，它所产生的 HTML 将会引用名为 style.css 的样式表。因此，需要将这个样式表
     * 放到项目中。
     *
     *
     *
     * 3、添加静态内容
     *
     * 正常来讲，在编写 Spring 应用时，这里会尽量避免讨论样式和图片。当然，这些内容能够在很大程度上让各种应用（包括
     * Spring 应用）变得更加美观，令用户赏心悦目。但是，对于编写服务器端的 Spring 代码来说，这些静态内容就没有那么
     * 重要了。
     *
     * 但是，在 Spring Boot 中，有必要讨论一下它是如何处理静态内容的。当采用 Spring Boot 的 Web 自动配置来定义
     * Spring MVC bean 时，这些 bean 中会包含一个资源处理器（resource handler），它会将 "/**" 映射到几个资源
     * 路径中。这些资源路径包括（相对于类路径的根）：
     * （1）/META-INF/resources/
     * （2）/resources/
     * （3）/static/
     * （4）/public/
     *
     * 在传统的基于 Maven/Gradle 构建的项目中，通常会将静态内容放在 "src/main/webapp" 目录下，这样在构建所生成
     * 的 WAR 文件里面，这些内容就会位于 WAR 文件的根目录下。如果使用 Spring Boot 构建 WAR 文件的话，这依然是可
     * 选的方案。但是，也可以将静态内容放在资源处理器所映射的上述四个路径下。
     *
     * 所以，为了满足 Thymeleaf 模板对 "/style.css" 文件的引用，需要创建一个名为 style.css 文件，并将其放到如
     * 下所示的某一个位置中：
     * （1）/META-INF/resources/style.css
     * （2）/resources/style.css
     * （3）/static/style.css
     * （4）/public/style.css
     *
     * 具体的选择完全取决于你，这里倾向于将静态内容放到 "/public" 中，不过这四个可选方案是等价的。
     *
     * 尽管 style.css 文件的内容与讨论无关，但是如下这个简单的样式表能够让应用看上去更加整洁：
     *
     * body {
     *     background-color: #eeeeee;
     *     font-family: sans-serif;
     * }
     *
     * label {
     *     display: inline-block;
     *     width: 120px;
     *     text-align: right;
     * }
     *
     * 不管你是否相信，对于这个简单的 Contacts 应用来说，已经完成了超过一半的任务！Web 层全部完成了，接下来需要创建
     * ContactRepository，用来处理 Contact 对象的持久化。
     *
     *
     *
     * 4、持久化数据
     *
     * 在Spring应用中，有多种使用数据库的方式。可以使用 JPA 或 Hibernate 将对象映射为关系型数据库中的表和列。或者，
     * 干脆放弃关系型数据库，使用其他类型的数据库，如 Mongo 或 Neo4j。
     *
     * 对于 Contacts 应用来说，关系型数据库是不错的选择。这里将会使用 H2 数据库和 JDBC（使用 Spring 的 JdbcTemplate），
     * 让这个过程尽可能地简单。
     *
     * 选择这种方案就需要在构建中添加一些依赖。JDBC Starter 依赖会将 Spring JdbcTemplate 需要的所有内容都引入进来。
     * 不过，要结合使用 H2 数据库的话，还需要添加 H2 依赖。如下是所需的 Maven 依赖：
     *
     *         <dependency>
     *             <groupId>org.springframework.boot</groupId>
     *             <artifactId>spring-boot-starter-jdbc</artifactId>
     *         </dependency>
     *         <dependency>
     *             <groupId>com.h2database</groupId>
     *             <artifactId>h2</artifactId>
     *         </dependency>
     *
     * 将这两项依赖添加到构建之中后，就可以编写 Repository 类了。如下代码中的 ContactRepository 将会使用注入的
     * JdbcTemplate 实现在数据库中读取和写入 Contact 对象。
     *
     * @Repository
     * public class ContactRepository {
     *
     *     private JdbcTemplate jdbc;
     *
     *     @Autowired
     *     public ContactRepository(JdbcTemplate jdbc) {
     *         this.jdbc = jdbc;
     *     }
     *
     *     public List<Contact> findAll() {
     *         return jdbc.query(
     *                 "select id, firstName, lastName, phoneNumber, emailAddress " +
     *                         "from contacts order by lastName",
     *                 new RowMapper<Contact>() {
     *                     @Override
     *                     public Contact mapRow(ResultSet rs, int rowNum) throws SQLException {
     *                         Contact contact = new Contact();
     *                         contact.setId(rs.getLong(1));
     *                         contact.setFirstName(rs.getString(2));
     *                         contact.setLastName(rs.getString(3));
     *                         contact.setPhoneNumber(rs.getString(4));
     *                         contact.setEmailAddress(rs.getString(5));
     *                         return contact;
     *                     }
     *                 }
     *         );
     *     }
     *
     *     public void save(Contact contact) {
     *         jdbc.update(
     *                 "insert into contacts " +
     *                         "(firstName, lastName, phoneNumber, emailAddress) " +
     *                         "values (?, ?, ?, ?)",
     *                 contact.getFirstName(), contact.getLastName(),
     *                 contact.getPhoneNumber(), contact.getEmailAddress());
     *     }
     *
     * }
     *
     * 与 ContactController 类似，这个 Repository 类非常简单。它与传统 Spring 应用中的 Repository 类并没有
     * 什么差别。从实现中，根本无法看出它要用于 Spring Boot 的应用程序中。findAll() 方法使用注入的 JdbcTemplate
     * 从数据库中获取 Contact 对象，save() 方法使用注入的 JdbcTemplate 保存新的 Contact 对象。因为
     * ContactRepository 使用了 @Repository 注解，因此在组件扫描的时候，它会被发现并创建为 Spring 应用上下文
     * 中的 bean。
     *
     * 但是，JdbcTemplate 呢？难道不需要在 Spring 应用上下文中声明 JdbcTemplate bean 吗？为了声明它，是不是
     * 还要声明一个 H2 DataSource？
     *
     * 对这两个问题的简短问答就是 "不需要"。当 Spring Boot 探测到 Spring 的 JDBC 模块和 H2 在类路径下的时候，
     * 自动配置就会发挥作用，将会自动配置 JdbcTemplate bean 和 H2 DataSource bean。Spring Boot 再一次处理
     * 了所有的 Spring 配置。
     *
     * 那数据库模式该怎么处理呢？必须要自己来定义创建 contacts 表的模式，对不对？
     *
     * 这绝对是正确的！Spring Boot 没有办法猜测 contacts 表会是什么样子。所以，需要定义模式，如下所示：
     *
     * create table contacts (
     *     id identity,
     *     firstName varchar(30) not null,
     *     lastName varchar(50) not null,
     *     phoneNumber varchar(13),
     *     emailAddress varchar(30)
     * );
     *
     * 现在，只需要有一种方式加载这个 "create table" 的 SQL 并将其在 H2 数据库中执行就可以了。幸好，Spring
     * Boot 也涵盖了这项功能。如果将这个文件命名为 schema.sql 并将其放在类路径根下（也就是 Maven 或 Gradle
     * 项目的 "src/main/resources" 目录下），当应用启动的时候，就会找到这个文件并进行数据加载。
     *
     *
     *
     * 5、尝试运行
     *
     * Contacts 应用非常简单，但是也算得上现实中的 Spring 应用。它具有 Spring MVC 控制器和 Thymeleaf 模板
     * 所定义的 Web 层，并且具有 Repository 和 Spring JdbcTemplate 所定义的持久层。
     *
     * 到此为止，已经编写完了 Contacts 所需的应用级别代码。不过，这里还没有编写任何形式的配置。没有编写任何
     * Spring 配置，也没有在 web.xml 或 Servlet 初始化类中配置 DispatcherServlet。
     *
     * 如果说不需要编写任何的配置，你会相信吗？
     *
     * 这应该做不到吧，毕竟在对 Spring 的批评中，人们都在说 Spring 全是配置，肯定有忽略掉的 XML 文件或 Java
     * 配置类。Spring 应用程序根本就不可能没有任何配置的 ...... 那么，到底能做到吗？
     *
     * 通常来讲，Spring Boot 的自动配置特性消除了绝大部分或者全部的配置。因此，完全可能编写出没有任何配置的
     * Spring 应用程序。当然，自动配置并不能涵盖所有的场景，因此典型的 Spring Boot 应用程序依然会需要一点
     * 配置。
     *
     * 具体到 Contacts 应用，不需要任何的配置。Spring 的自动配置功能已经将所有的事情都做好了。
     *
     * 但是，这里需要有个特殊的类来启动 Spring Boot 应用。Spring 本身并不知道自动配置的任何信息。Application
     * 类就是 Spring Boot 启动类的典型例子。
     *
     * @ComponentScan
     * @EnableAutoConfiguration
     * public class Application {
     *
     *     public static void main(String[] args) {
     *         SpringApplication.run(Application.class, args);
     *     }
     *
     * }
     *
     * 好吧，Application 中有那么一点配置。它使用 @ComponentScan 注解来启用组件扫描，另外它还使用了
     * @EnableAutoConfiguration 注解，这会启用 Spring Boot 的自动配置特性。但是，也就这么多了！除
     * 了这两行代码以外，Contacts 再也没有什么配置了。
     *
     * Application 类最有意思的一点在于它具有一个 main() 方法。稍后将会看到，Spring Boot 应用会以
     * 一种特殊的方法运行，正是这里的 main() 方法使这一切成为可能。在 main() 方法中，这行代码会告诉
     * Spring Boot（通过 SpringApplication 类）根据 Application 中的配置以及命令行中的参数来运行。
     *
     *
     * 现在，马上就可以运行应用了。剩下就是要进行构建。Maven 构建如下：
     *
     * mvn package
     *
     * 运行 Maven 构建后，你会在 target 文件夹下找到构建形成的结果。
     *
     * 现在，就可以运行它了。按照传统的方式，这意味着要将应用的 WA R文件部署到 Servlet 容器中，如 Tomcat 或
     * WebSphere。但是在这里，甚至没有 WAR 文件 —— 构建形成的是一个 JAR 文件。
     *
     * 这没有什么问题。可以按照如下的方式从命令行运行它：
     *
     * java -jar contacts-0.1.0.jar
     *
     * 在几秒钟后，应用应该已经启动完成并且可以访问了。打开浏览器进入 http://localhost:8080，你就应该可以输入
     * 联系人了。
     *
     *
     * 你可能觉得这并不符合 Web 应用的运行方式。像这样从命令行运行应用非常简洁和方便，但是，对于你来讲，也许这并
     * 不理想。在你所工作的环境中，有可能需要将 Web 应用作为 WAR 文件部署到 Web 容器中。如果不提交 WAR 文件的
     * 话，可能不满足公司的部署策略。
     *
     * 好的，那也没有问题。
     *
     * 即便是对于生产环境，通过命令行来运行应用也是合理的方案，但是你可能需要遵循公司的部署流程。这意味着需要构建
     * 和部署 WAR 文件。
     *
     * 好消息是，如果你需要 WAR 文件的话，并没有必要舍弃 Spring Boot 的简洁性。需要做的事情仅仅是稍微调整一下
     * 构建文件。对于 Maven 来说，只需将 packaging 从 "jar" 替换为 "war" 即可：
     *
     * <packaging>war</packaging>
     *
     * 现在，可以重新构建项目，然后将会在构建目录中找到 contacts-0.1.0.war 文件。这个 WAR 文件文件可以部署到
     * 任意支持 Servlet 3.0 的容器中。另外，依然可以在命令行中运行这个应用：
     *
     * java -jar contacts-0.1.0.war
     *
     * 没错：这是一个可运行的 WAR 文件！对于两种场景来说，这都是最佳的方案。
     *
     * 可以看到，Spring Boot 能够在很大程度上尽可能简化 Spring 应用的部署。Spring Boot Stater 简化了项目
     * 构建的依赖，自动配置消除了显式的 Spring 配置。
     */
    public static void main(String[] args) {

    }

}
