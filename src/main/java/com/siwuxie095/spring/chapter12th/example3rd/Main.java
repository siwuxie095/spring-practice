package com.siwuxie095.spring.chapter12th.example3rd;

/**
 * @author Jiajing Li
 * @date 2021-02-28 18:19:21
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 启用 MongoDB
     *
     * 为了有效地使用 Spring Data MongoDB，需要在 Spring 配置中添加几个必要的 bean。首先，需要配置 MongoClient，
     * 以便于访问 MongoDB 数据库。同时，还需要有一个 MongoTemplate bean，实现基于模板的数据库访问。此外，不是必须，
     * 但是强烈推荐启用 Spring Data MongoDB 的自动化 Repository 生成功能。
     *
     * 如下代码展现了如何编写简单的 Spring Data MongoDB 配置类，它包含了上述的几个 bean：
     *
     * @Configuration
     * @EnableMongoRepositories(basePackages = "com.siwuxie095.spring.chapter12th.example3rd.db")
     * public class MongoConfig {
     *
     *     @Bean
     *     public MongoFactoryBean mongo() {
     *         MongoFactoryBean mongo = new MongoFactoryBean();
     *         mongo.setHost("localhost");
     *         return mongo;
     *     }
     *
     *     @Bean
     *     public MongoOperations mongoTemplete(Mongo mongo) {
     *         return new MongoTemplate(mongo, "OrdersDB");
     *     }
     *
     * }
     *
     * 通过 @EnableJpaRepositories 注解，可以启用 Spring Data 的自动化 JPA Repository 生成功能。
     *
     * 与之类似，@EnableMongoRepositories 为 MongoDB 实现了相同的功能。
     *
     * 除了 @EnableMongoRepositories 之外，其中还包含了两个带有 @Bean 注解的方法。第一个 @Bean 方法使用
     * MongoFactoryBean 声明了一个 Mongo 实例。这个 bean 将 Spring Data MongoDB 与数据库本身连接了起来（与使用
     * 关系型数据时 DataSource 所做的事情并没有什么区别）。尽管可以使用 MongoClient 直接创建 Mongo 实例，但如果这
     * 样做的话，就必须要处理 MongoClient 构造器所抛出的 UnknownHostException 异常。在这里，使用 Spring Data
     * MongoDB 的 MongoFactoryBean 更加简单。因为它是一个工厂 bean，因此 MongoFactoryBean 会负责构建 Mongo 实
     * 例，不必再担心 UnknownHostException 异常。
     *
     * 另外一个 @Bean 方法声明了 MongoTemplate bean，在它构造时，使用了其他 @Bean 方法所创建的 Mongo 实例的引用
     * 以及数据库的名称。后续你将会看到如何使用 MongoTemplate 来查询数据库。即便不直接使用 MongoTemplate，也会需要
     * 这个 bean，因为 Repository 的自动化生成功能在底层使用了它。
     *
     * 除了直接声明这些 bean，还可以让配置类扩展 AbstractMongoConfiguration 并重载 getDatabaseName() 和 mongo()
     * 方法。如下代码展现了如何使用这种配置方式。
     *
     * @Configuration
     * @EnableMongoRepositories(basePackages = "com.siwuxie095.spring.chapter12th.example3rd.db")
     * public class MongoConfig extends AbstractMongoConfiguration {
     *
     *     @Override
     *     protected String getDatabaseName() {
     *         return "OrdersDB";
     *     }
     *
     *     @Override
     *     public Mongo mongo() throws Exception {
     *         return new MongoClient();
     *     }
     *
     * }
     *
     * 这个新的配置类与之前的功能是相同的，只不过在篇幅上更加简洁。最为显著的区别在于这个配置中没有直接声明 MongoTemplate
     * bean，当然它还是会被隐式地创建。在这里重载了 getDatabaseName() 方法来提供数据库的名称。mongo() 方法依然会创建一
     * 个 MongoClient 的实例，因为它会抛出 Exception，所以可以直接使用 MongoClient，而不必再使用 MongoFactoryBean
     * 了。
     *
     * 到目前为止，不管是使用哪个配置类，都为 Spring Data MongoDB 提供了一个运行配置，也就是说，只要 MongoDB 服务器
     * 运行在本地即可。如果 MongoDB 服务器运行在其他的机器上，那么可以在创建 MongoClient 的时候进行指定：
     *
     *     public Mongo mongo() throws Exception {
     *         return new MongoClient("mongodbserver");
     *     }
     *
     * 另外，MongoDB 服务器有可能监听的端口并不是默认的 27017。如果是这样的话，在创建 MongoClient 的时候，还需要指定
     * 端口：
     *
     *     public Mongo mongo() throws Exception {
     *         return new MongoClient("mongodbserver", 37017);
     *     }
     *
     * 如果 MongoDB 服务器运行在生产配置上，你可能还启用了认证功能。在这种情况下，为了访问数据库，还需要提供应用的凭证。
     * 访问需要认证的 MongoDB 服务器稍微有些复杂，如下所示。
     *
     * @Configuration
     * @EnableMongoRepositories(basePackages = "com.siwuxie095.spring.chapter12th.example3rd.db")
     * public class MongoConfig extends AbstractMongoConfiguration {
     *
     *     @Autowired
     *     private Environment env;
     *
     *     @Override
     *     protected String getDatabaseName() {
     *         return "OrdersDB";
     *     }
     *
     *     @Override
     *     public Mongo mongo() throws Exception {
     *         MongoCredential credential =
     *                 MongoCredential.createMongoCRCredential(
     *                         env.getProperty("mongo.username"),
     *                         getDatabaseName(),
     *                         env.getProperty("mongo.password").toCharArray());
     *         return new MongoClient(
     *                 new ServerAddress("localhost", 37017),
     *                 Arrays.asList(credential));
     *     }
     *
     * }
     *
     * 为了访问需要认证的 MongoDB 服务器，MongoClient 在实例化的时候必须要有一个 MongoCredential 的列表。在这里，
     * 为此创建了一个 MongoCredential。为了将凭证信息的细节放在配置类外边，它们是通过注入的 Environment 对象解析
     * 得到的。
     *
     * 为了使这个讨论更加完整，Spring Data MongoDB 还支持通过 XML 来进行配置。这里更喜欢 Java 配置的方案。但是，
     * 如果你喜欢 XML 配置的话，如下代码展现了如何使用 mongo 配置命名空间来配置 Spring Data MongoDB。
     *
     * <!-- 启用 Repository 生成功能 -->
     * <mongo:repositories base-package="com.siwuxie095.spring.chapter12th.example3rd.db" />
     *
     * <!-- 声明 Mongo Client -->
     * <mongo:mongo />
     *
     * <!-- 创建 MongoTemplete bean -->
     * <bean id="mongoTemplete"
     *      class="org.springframework.data.mongodb.core.MongoTemplate">
     *      <constructor-arg ref="mongo" />
     *      <constructor-arg value="OrdersDB" />
     * </bean>
     *
     * PS：需要引入 mongo 命名空间。
     *
     * 现在 Spring Data MongoDB 已经配置完成了，很快就可以使用它来保存和查询文档了。但首先，需要使用 Spring Data
     * MongoDB 的对象-文档映射注解为 Java 领域对象建立到持久化文档的映射关系。后续将会对其进行介绍。
     */
    public static void main(String[] args) {

    }

}
