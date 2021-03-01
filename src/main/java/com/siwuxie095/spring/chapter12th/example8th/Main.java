package com.siwuxie095.spring.chapter12th.example8th;

/**
 * @author Jiajing Li
 * @date 2021-03-01 08:09:32
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 配置 Spring Data Neo4j
     *
     * 配置 Spring Data Neo4j 的关键在于声明 GraphDatabaseService bean 和启用 Neo4j Repository 自动生成功能。
     * 如下代码展现了 Spring Data Neo4j 所需的基本配置。
     *
     * @Configuration
     * @EnableNeo4jRepositories(basePackages = "com.siwuxie095.spring.chapter12th.example8th.db")
     * public class Neo4jConfig extends Neo4jConfiguration {
     *
     *     public Neo4jConfig() {
     *         setBasePackage("com.siwuxie095.spring.chapter12th.example8th");
     *     }
     *
     *     @Bean(destroyMethod = "shutdown")
     *     public GraphDatabaseService graphDatabaseService() {
     *         return new GraphDatabaseFactory()
     *                 .newEmbeddedDatabase("/tmp/graphdb");
     *     }
     *
     * }
     *
     * @EnableNeo4jRepositories 注解能够让 Spring Data Neo4j 自动生成 Neo4j Repository 实现。它的 basePackages
     * 属性设置为 com.siwuxie095.spring.chapter12th.example8th.db 包，这样它就会扫描这个包来查找（直接或间接）扩展
     * Repository 标记接口的其他接口。
     *
     * Neo4jConfig 扩展自 Neo4jConfiguration，后者提供了多个便利的方法来配置 Spring Data Neo4j。在这些方法中，就包
     * 括 setBasePackage()，它会在 Neo4jConfig 的构造器中调用，用来告诉 Spring Data Neo4j 要在哪个包中查找模型类。
     *
     * 这个拼图的最后一部分是定义 GraphDatabaseService bean。
     *
     * 在本例中，graphDatabaseService() 方法使用 GraphDatabaseFactory来创建嵌入式的 Neo4j 数据库。在 Neo4j 中，嵌
     * 入式数据库不要与内存数据库相混淆。在这里，"嵌入式" 指的是数据库引擎与应用运行在同一个 JVM 中，作为应用的一部分，而
     * 不是独立的服务器。数据依然会持久化到文件系统中（在本例中，也就是 "/tmp/graphdb" 中）。
     *
     * 作为另外的一种方式，你可能会希望配置 GraphDatabaseService 连接远程的 Neo4j 服务器。如果 spring-data-neo4j-rest
     * 库在应用的类路径下，那么就可以配置 SpringRestGraphDatabase，它会通过 RESTful API 来访问远程的 Neo4j 数据库：
     *
     *     @Bean(destroyMethod = "shutdown")
     *     public GraphDatabaseService graphDatabaseService() {
     *         return new SpringCypherRestGraphDatabase(
     *                 "http://graphdbserver:7474/db/data/");
     *     }
     *
     * 如上所示，SpringRestGraphDatabase 在配置时，假设远程的数据库并不需要认证。但是，在生产环境的配置中，当创建
     * SpringRestGraphDatabase 的时候，可能希望提供应用的凭证：
     *
     *     @Autowired
     *     private Environment env;
     *
     *     @Bean(destroyMethod = "shutdown")
     *     public GraphDatabaseService graphDatabaseService() {
     *         return new SpringRestGraphDatabase(
     *                 "http://graphdbserver:7474/db/data/",
     *                 env.getProperty("db.username"),
     *                 env.getProperty("db.password"));
     *     }
     *
     * 在这里，凭证是通过注入的 Environment 获取到的，避免了在配置类中的硬编码。
     *
     * Spring Data Neo4j 同时还提供了 XML 命名空间。如果你更愿意在 XML 中配置 Spring Data Neo4j 的话，那可以使用该
     * 命名空间中的 <neo4j:config> 和 <neo4j:repositories> 元素。在功能上，与上面的 Java 配置是相同的。
     *
     * <!-- 配置 Neo4j 数据库的细节 -->
     * <neo4j:config storeDirectory="/tmp/graphdb"
     *               base-package="com.siwuxie095.spring.chapter12th.example8th" />
     *
     * <!-- 启用 Repository 生成功能
     * <neo4j:repositories base-package="com.siwuxie095.spring.chapter12th.example8th.db" />
     *
     * PS：需要引入 neo4j 命名空间。
     *
     * <neo4j:config> 元素配置了如何访问数据库的细节。在本例中，它配置 Spring Data Neo4j 使用嵌入式的数据库。具体来讲，
     * storeDirectory 属性指定了数据要持久化到哪个文件系统路径中。base-package 属性设置了模型类定义在哪个包中。
     *
     * 至于 <neo4j:repositories> 元素，它启用 Spring Data Neo4j 自动生成 Repository 实现的功能，它会扫描对应的包，
     * 查找所有扩展 Repository 的接口。
     *
     * 如果要配置 Spring Data Neo4j 访问远程的 Neo4j 服务器，所需要做的就是声明 SpringRestGraphDatabase bean，并
     * 设置 <neo4j:config> 的 graphDatabaseService 属性：
     *
     * <neo4j:config base-package="com.siwuxie095.spring.chapter12th.example8th"
     *               graphDatabaseService="graphDatabaseService" />
     *
     * <bean id="graphDatabaseService"
     *       class="org.springframework.data.neo4j.rest.SpringRestGraphDatabase">
     *       <constructor-arg value="http://graphdbserver:7474/db/data" />
     *       <constructor-arg value="db.username" />
     *       <constructor-arg value="db.password" />
     * </bean>
     *
     * 不管是通过 Java 还是通过 XML 来配置 Spring Data Neo4j，都需要确保模型类位于基础包所指定的包中
     * （通过 @EnableNeo4jRepositories 的 basePackages 属性或 <neo4j:config> 的 base-package
     * 属性来进行设置)。它们都需要使用注解将其标注为节点实体或关联关系实体。而这正是后续的任务。
     */
    public static void main(String[] args) {

    }

}
