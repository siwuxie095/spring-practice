package com.siwuxie095.spring.chapter10th.example5th;

/**
 * @author Jiajing Li
 * @date 2021-02-22 08:14:26
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 配置数据源
     *
     * 无论选择 Spring 的哪种数据访问方式，你都需要配置一个数据源的引用。Spring 提供了在 Spring 上下文中
     * 配置数据源 bean 的多种方式，包括：
     * （1）通过 JDBC 驱动程序定义的数据源；
     * （2）通过 JNDI 查找的数据源；
     * （3）连接池的数据源。
     *
     * 对于即将发布到生产环境中的应用程序，建议使用从连接池获取连接的数据源。如果可能的话，这里倾向于通过应用
     * 服务器的 JNDI 来获取数据源。请记住这一点，下面首先看一下如何配置 Spring 从 JNDI 中获取数据源。
     *
     *
     *
     * 1、使用 JNDI 数据源
     *
     * Spring 应用程序经常部署在 Java EE 应用服务器中，如 WebSphere、JBoss 或甚至像 Tomcat 这样的 Web
     * 容器中。这些服务器允许你配置通过 JNDI 获取数据源。这种配置的好处在于数据源完全可以在应用程序之外进行
     * 管理，这样应用程序只需在访问数据库的时候查找数据源就可以了。另外，在应用服务器中管理的数据源通常以池的
     * 方式组织，从而具备更好的性能，并且还支持系统管理员对其进行热切换。
     *
     * 利用 Spring，可以像使用 Spring bean 那样配置 JNDI 中数据源的引用并将其装配到需要的类中。位于 jee
     * 命名空间下的 <jee:jndi-lookup> 元素可以用于检索 JNDI 中的任何对象（包括数据源）并将其作为 Spring
     * 的 bean。例如，如果应用程序的数据源配置在 JNDI 中，可以使用 <jee:jndi- lookup> 元素将其装配到
     * Spring 中，如下所示：
     *
     * <jee:jndi-lookup id="dataSource" jndi-name="/jdbc/SpittrDS" resource-ref="true" />
     *
     * 其中 jndi-name 属性用于指定 JNDI 中资源的名称。如果只设置了 jndi-name 属性，那么就会根据指定的名称
     * 查找数据源。但是，如果应用程序运行在 Java 应用服务器中，你需要将 resource-ref 属性设置为 true，这样
     * 给定的 jndi-name 将会自动添加 "java:comp/env/" 前缀。
     *
     * 如果想使用 Java 配置的话，那可以借助 JndiObjectFactoryBean 从 JNDI 中查找 DataSource：
     *
     *     @Bean
     *     public JndiObjectFactoryBean dataSource() {
     *         JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
     *         jndiObjectFactoryBean.setJndiName("jdbc/SpittrDS");
     *         jndiObjectFactoryBean.setResourceRef(true);
     *         jndiObjectFactoryBean.setProxyInterface(DataSource.class);
     *         return jndiObjectFactoryBean;
     *     }
     *
     * 显然，通过 Java 配置获取 JNDI bean 要更为复杂。大多数情况下，Java 配置要比 XML 配置简单，但是这一
     * 次需要写更多的 Java 代码。但是，很容易就能够看出 Java 代码中与 XML 相对应的配置，Java 配置的内容其
     * 实也不算多。
     *
     *
     *
     * 2、使用数据源连接池
     *
     * 如果你不能从 JNDI 中查找数据源，那么下一个选择就是直接在 Spring 中配置数据源连接池。尽管 Spring 并
     * 没有提供数据源连接池实现，但是有多项可用的方案，包括如下开源的实现：
     * （1）Apache Commons DBCP：http://commons.apache.org/proper/commons-dbcp/
     * （2）c3p0：https://sourceforge.net/projects/c3p0/
     * （3）BoneCP：https://jolbox.com/
     *
     * 这些连接池中的大多数都能配置为 Spring 的数据源，在一定程度上与 Spring 自带的 DriverManagerDataSource
     * 或 SingleConnectionDataSource 很类似。例如，如下就是配置 DBCP BasicDataSource 的方式：
     *
     * <bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource"
     *      p:driverClassName="org.h2.Driver"
     *      p:url="jdbc:h2:tcp://localhost/~/spitter"
     *      p:username="sa"
     *      p:password=""
     *      p:initialSize="5"
     *      p:maxActive="10" />
     *
     * 如果你喜欢 Java 配置的话，连接池形式的 DataSource bean 可以声明如下：
     *
     *     @Bean
     *     public BasicDataSource dataSource() {
     *         BasicDataSource basicDataSource = new BasicDataSource();
     *         basicDataSource.setDriverClassName("org.h2.Driver");
     *         basicDataSource.setUrl("jdbc:h2:tcp://localhost/~/spitter");
     *         basicDataSource.setUsername("sa");
     *         basicDataSource.setPassword("");
     *         basicDataSource.setInitialSize(5);
     *         basicDataSource.setMaxActive(10);
     *         return basicDataSource;
     *     }
     *
     * 前四个属性是配置 BasicDataSource 所必需的。属性 driverClassName 指定了 JDBC 驱动类的全限定类名。
     * 在这里配置的是 H2 数据库的数据源。属性 url 用于设置数据库的 JDBC URL。最后，username 和 password
     * 用于在连接数据库时进行认证。
     *
     * 以上四个基本属性定义了 BasicDataSource 的连接信息。除此以外，还有多个配置数据源连接池的属性。如下列
     * 出了 DBCP BasicDataSource 最有用的一些池配置属性：
     * （1）initialSize：池启动时创建的连接数量；
     * （2）maxActive：同一时间可从池中分配的最多连接数。如果设置为 0，表示无限制；
     * （3）maxIdle：池里不会被释放的最多空闲连接数。如果设置为 0，表示无限制；
     * （4）maxOpenPreparedStatements：在同一时间能够从语句池中分配的预处理语句（prepared statement）
     * 的最大数量。如果设置为 0，表示无限制；
     * （5）maxWait：在抛出异常之前，池等待连接回收的最大时间（当没有可用连接时）。如果设置为 -1，表示无限
     * 等待；
     * （6）minEvictableIdleTimeMillis：连接在池中保持空闲而不被回收的最大时间；
     * （7）minIdle：在不创建新连接的情况下，池中保持空闲的最小连接数；
     * （8）poolPreparedStatements：是否对预处理语句（prepared statement）进行池管理（布尔值）。
     *
     * 在示例中，连接池启动时会创建 5 个连接。当需要的时候，允许 BasicDataSource 创建新的连接，但最大活跃
     * 连接数为 10。
     *
     *
     *
     * 3、基于 JDBC 驱动的数据源
     *
     * 在 Spring 中，通过 JDBC 驱动定义数据源是最简单的配置方式。Spring 提供了三个这样的数据源类供选择：
     * （1）DriverManagerDataSource：在每个连接请求时都会返回一个新建的连接。与 DBCP 的 BasicDataSource
     * 不同，由 DriverManagerDataSource 提供的连接并没有进行池化管理；
     * （2）SimpleDriverDataSource：与 DriverManagerDataSource 的工作方式类似，但是它直接使用 JDBC
     * 驱动，来解决在特定环境下的类加载问题，这样的环境包括 OSGi 容器；
     * （3）SingleConnectionDataSource：在每个连接请求时都会返回同一个的连接。尽管 SingleConnectionDataSource
     * 不是严格意义上的连接池数据源，但是你可以将其视为只有一个连接的池。
     * PS：三者均位于 org.springframework.jdbc.datasource 包中。
     *
     * 以上这些数据源的配置与 DBCP BasicDataSource 的配置类似。例如，如下就是配置 DriverManagerDataSource
     * 的方法：
     *
     *     @Bean
     *     public DataSource dataSource() {
     *         DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
     *         driverManagerDataSource.setDriverClassName("org.h2.Driver");
     *         driverManagerDataSource.setUrl("jdbc:h2:tcp://localhost/~/spitter");
     *         driverManagerDataSource.setUsername("sa");
     *         driverManagerDataSource.setPassword("");
     *         return driverManagerDataSource;
     *     }
     *
     * 如果使用 XML 的话，DriverManagerDataSource 可以按照如下的方式配置：
     *
     * bean id="dataSource"
     *      class="org.springframework.jdbc.datasource.DriverManagerDataSource"
     *      p:driverClassName="org.h2.Driver"
     *      p:url="jdbc:h2:tcp://localhost/~/spitter"
     *      p:username="sa"
     *      p:password="" />
     *
     * 与具备池功能的数据源相比，唯一的区别在于这些数据源 bean 都没有提供连接池功能，所以没有可配置的池相关
     * 的属性。
     *
     * 尽管这些数据源对于小应用或开发环境来说是不错的，但是要将其用于生产环境，你还是需要慎重考虑。因为
     * SingleConnectionDataSource 有且只有一个数据库连接，所以不适合用于多线程的应用程序，最好只在
     * 测试的时候使用。而 DriverManagerDataSource 和 SimpleDriverDataSource 尽管支持多线程，但
     * 是在每次请求连接的时候都会创建新连接，这是以性能为代价的。鉴于以上的这些限制，强烈建议应该使用数
     * 据源连接池。
     *
     *
     *
     * 4、使用嵌入式的数据源
     *
     * 除此之外，还要介绍一个数据源：嵌入式数据库（embedded database）。嵌入式数据库作为应用的一部分运行，
     * 而不是应用连接的独立数据库服务器。尽管在生产环境的设置中，它并没有太大的用处，但是对于开发和测试来讲，
     * 嵌入式数据库都是很好的可选方案。这是因为每次重启应用或运行测试的时候，都能够重新填充测试数据。
     *
     * Spring 的 jdbc 命名空间能够简化嵌入式数据库的配置。例如，如下代码展现了如何使用 jdbc 命名空间来配
     * 置嵌入式的 H2 数据库，它会预先加载一组测试数据：
     *
     * <jdbc:embedded-database id="dataSource" type="H2">
     *     <jdbc:script location="spitter/db/jdbc/schema.sql"/>
     *     <jdbc:script location="spitter/db/jdbc/test-data.sql"/>
     * </jdbc:embedded-database>
     *
     * 这里将 <jdbc:embedded-database> 的 type 属性设置为 H2，表明嵌入式数据库应该是 H2 数据库（要确保
     * H2 位于应用的类路径下）。另外，还可以将 type 设置为 DERBY，以使用嵌入式的 Apache Derby 数据库。
     *
     * 在 <jdbc:embedded-database> 中，可以不配置也可以配置多个 <jdbc:script> 元素来搭建数据库。这里
     * 包含了两个 <jdbc:script> 元素：
     * （1）第一个引用了 schema.sql，它包含了在数据库中创建表的 SQL；
     * （2）第二个引用了 test-data.sql，用来将测试数据填充到数据库中。
     *
     * 除了搭建嵌入式数据库以外，<jdbc:embedded-database> 元素还会暴露一个数据源，可以像使用其他的数据源
     * 那样来使用它。在这里，id 属性被设置成了 dataSource，这也是所暴露数据源的 bean ID。因此，当需要
     * javax.sql.DataSource 的时候，就可以注入 dataSource bean。
     *
     * 如果使用 Java 来配置嵌入式数据库时，不会像 jdbc 命名空间那么简便，可以使用 EmbeddedDatabaseBuilder
     * 来构建 DataSource：
     *
     *     @Bean
     *     public DataSource dataSource() {
     *         return new EmbeddedDatabaseBuilder()
     *                 .setType(EmbeddedDatabaseType.H2)
     *                 .addScript("classpath:schema.sql")
     *                 .addScript("classpath:test-data.sql")
     *                 .build();
     *     }
     *
     * 可以看到，setType() 方法等同于 <jdbc:embedded-database> 元素中的 type 属性，此外，这里用 addScript()
     * 代替 <jdbc:script> 元素来指定初始化 SQL。
     *
     *
     *
     * 5、使用 profile 选择数据源
     *
     * 已经看到了多种在 Spring 中配置数据源的方法，相信你已经找到了一两种适合你的应用程序的配置方式。实际上，
     * 很可能面临这样一种需求，那就是在某种环境下需要其中一种数据源，而在另外的环境中需要不同的数据源。
     *
     * 例如，对于开发期来说，<jdbc:embedded-database> 元素是很合适的，而在 QA 环境中，你可能希望使用 DBCP
     * 的 BasicDataSource，在生产部署环境下，可能需要使用 <jee:jndi-lookup>。
     *
     * Spring 的 bean profile 特性恰好可以用在这里，所需要做的就是将每个数据源配置在不同的 profile 中，
     * 如下所示：
     *
     * @Configuration
     * public class DataSourceConfig {
     *
     *     @Profile("development")
     *     @Bean
     *     public DataSource embeddedDataSource() {
     *         return new EmbeddedDatabaseBuilder()
     *                 .setType(EmbeddedDatabaseType.H2)
     *                 .addScript("classpath:schemal.sql")
     *                 .addScript("classpath:test-data.sql")
     *                 .build();
     *     }
     *
     *     @Profile("qa")
     *     @Bean
     *     public DataSource dbcpDatasource() {
     *         BasicDataSource basicDataSource = new BasicDataSource();
     *         basicDataSource.setDriverClassName("org.h2.Driver");
     *         basicDataSource.setUrl("jdbc:h2:tcp://localhost/~/spitter");
     *         basicDataSource.setUsername("sa");
     *         basicDataSource.setPassword("");
     *         basicDataSource.setInitialSize(5);
     *         basicDataSource.setMaxActive(10);
     *         return basicDataSource;
     *     }
     *
     *     @Profile("production")
     *     @Bean
     *     public DataSource jndiDatasource() {
     *         JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
     *         jndiObjectFactoryBean.setJndiName("jdbc/SpittrDS");
     *         jndiObjectFactoryBean.setResourceRef(true);
     *         jndiObjectFactoryBean.setProxyInterface(DataSource.class);
     *         return (DataSource) jndiObjectFactoryBean.getObject();
     *     }
     *
     * }
     *
     * 通过使用 profile 功能，会在运行时选择数据源，这取决于哪一个 profile 处于激活状态。如这段代码配置所示，
     * 当且仅当 development profile 处于激活状态时，会创建嵌入式数据库，当且仅当 qa profile 处于激活状态时，
     * 会创建 DBCP BasicDataSource，当且仅当 production profile 处于激活状态时，会从 JNDI 获取数据源。
     *
     * 为了内容的完整性，如下的代码展现了如何使用 Spring XML 代替 Java 配置，实现相同的 profile 配置。
     *
     * <beans profile="development">
     *     <jdbc:embedded-database id="dataSource" type="H2">
     *         <jdbc:script location="spitter/db/jdbc/schema.sql"/>
     *         <jdbc:script location="spitter/db/jdbc/test-data.sql"/>
     *     </jdbc:embedded-database>
     * </beans>
     *
     * <beans profile="qa">
     *     <bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource"
     *           destroy-method="close"
     *           p:url="jdbc:h2:tcp://localhost/~/spitter"
     *           p:driverClassName="org.h2.Driver"
     *           p:username="sa"
     *           p:password=""
     *           p:initialSize="5"
     *           p:maxActive="10"/>
     * </beans>
     *
     * <beans profile="production">
     *     <jee:jndi-lookup id="dataSource"
     *                      lazy-init="true"
     *                      jndi-name="jdbc/SpitterDS"
     *                      resource-ref="true"
     *                      proxy-interface="javax.sql.DataSource"/>
     * </beans>
     *
     * 现在已经通过数据源建立了与数据库的连接，接下来要实际访问数据库了。就像在前面所提到的，Spring 提供了
     * 多种使用数据库的方式包括 JDBC、Hibernate 以及 Java 持久化 API（Java Persistence API，JPA）。
     * 后续将会看到如何使用 Spring 对 JDBC 的支持为应用程序构建持久层。
     */
    public static void main(String[] args) {

    }

}
