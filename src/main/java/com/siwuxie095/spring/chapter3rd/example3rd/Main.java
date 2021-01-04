package com.siwuxie095.spring.chapter3rd.example3rd;

/**
 * @author Jiajing Li
 * @date 2021-01-04 07:59:24
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 配置 profile bean
     *
     * Spring 为环境相关的 bean 所提供的解决方案其实与构建时的方案没有太大的差别。当然，在这个过程中需要根据环境决定该
     * 创建哪个 bean 和不创建哪个 bean。不过 Spring 并不是在构建的时候做出这样的决策，而是等到运行时再来确定。这样的
     * 结果就是同一个部署单元（可能会是 WAR 文件）能够适用于所有的环境，没有必要进行重新构建。
     *
     * 在 3.1 版本中，Spring 引入了 bean profile 的功能。要使用 profile，你首先要将所有不同的 bean 定义整理到一个
     * 或多个 profile 之中，在将应用部署到每个环境时，要确保对应的 profile 处于激活（active）的状态。
     *
     * 在 Java 配置中，可以使用 @Profile 注解指定某个 bean 属于哪一个 profile。例如，在配置类中，嵌入式数据库的
     * DataSource 可能会配置成如下所示：
     *
     * @Configuration
     * @Profile("dev")
     * public class DevelopmentProfileConfig {
     *
     *     @Bean(destroyMethod = "shutdown")
     *     public DataSource embeddedDataSource() {
     *         return new EmbeddedDatabaseBuilder()
     *                 .setType(EmbeddedDatabaseType.H2)
     *                 .addScript("classpath:schema.sql")
     *                 .addScript("classpath:test-data.sql")
     *                 .build();
     *     }
     *
     * }
     *
     * 值得注意的是，这里的 @Profile 注解应用在了类级别上。它会告诉 Spring 这个配置类中的 bean 只有在 dev profile
     * 激活时才会创建。如果 dev profile 没有激活的话，那么带有 @Bean 注解的方法都会被忽略掉。
     *
     * 同时，你可能还需要有一个适用于生产环境的配置，如下所示：
     *
     * @Configuration
     * @Profile("prod")
     * public class ProductionProfileConfig {
     *
     *     @Bean
     *     public DataSource jndiDataSource() {
     *         JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
     *         jndiObjectFactoryBean.setJndiName("jdbc/myDS");
     *         jndiObjectFactoryBean.setResourceRef(true);
     *         jndiObjectFactoryBean.setProxyInterface(javax.sql.DataSource.class);
     *         return (DataSource) jndiObjectFactoryBean.getObject();
     *     }
     *
     * }
     *
     * 在本例中，只有 prod profile 激活的时候，才会创建对应的 bean。
     *
     * 在 Spring 3.1 中，只能在类级别上使用 @Profile 注解。从 Spring 3.2 开始，也可以在方法级别上使用 @Profile 注
     * 解，与 @Bean 注解一同使用。这样的话，就能将这两个 bean 的声明放到同一个配置类之中，如下所示：
     *
     * @Configuration
     * public class DataSourceConfig {
     *
     *     @Bean(destroyMethod = "shutdown")
     *     @Profile("dev")
     *     public DataSource embeddedDataSource() {
     *         return new EmbeddedDatabaseBuilder()
     *                 .setType(EmbeddedDatabaseType.H2)
     *                 .addScript("classpath:schema.sql")
     *                 .addScript("classpath:test-data.sql")
     *                 .build();
     *     }
     *
     *     @Bean(destroyMethod = "close")
     *     @Profile("qa")
     *     public DataSource testDataSource() {
     *         BasicDataSource dataSource = new BasicDataSource();
     *         dataSource.setUrl("jdbc:h2:tcp://dbserver/~/test");
     *         dataSource.setDriverClassName("org.h2.Driver");
     *         dataSource.setUsername("sa");
     *         dataSource.setPassword("password");
     *         dataSource.setInitialSize(20);
     *         dataSource.setMaxActive(30);
     *         return dataSource;
     *     }
     *
     *     @Bean
     *     @Profile("prod")
     *     public DataSource jndiDataSource() {
     *         JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
     *         jndiObjectFactoryBean.setJndiName("jdbc/myDS");
     *         jndiObjectFactoryBean.setResourceRef(true);
     *         jndiObjectFactoryBean.setProxyInterface(javax.sql.DataSource.class);
     *         return (DataSource) jndiObjectFactoryBean.getObject();
     *     }
     *
     * }
     *
     * 这里有个问题需要注意，尽管每个 DataSource bean 都被声明在一个 profile 中，并且只有当规定的 profile 激活时，
     * 相应的 bean 才会被创建，但是可能会有其他的 bean 并没有声明在一个给定的 profile 范围内。没有指定 profile 的
     * bean 始终都会被创建，与激活哪个 profile 没有关系。
     *
     *
     *
     * 在 XML 中配置 profile
     *
     * 也可以通过 <beans> 元素的 profile 属性，在 XML 中配置 profile bean。 例如，为了在 XML 中定义适用于开发阶段
     * 的嵌入式数据库 DataSource bean，可以创建如下所示的 XML 文件：
     *
     * <?xml version="1.0" encoding="UTF-8"?>
     * <beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     *        xmlns:jdbc="http://www.springframework.org/schema/jdbc" xmlns:jee="http://www.springframework.org/schema/jee"
     *        xmlns="http://www.springframework.org/schema/beans"
     *        xsi:schemaLocation="http://www.springframework.org/schema/jee http://www.springframework.org/schema/jee/spring-jee-4.0.xsd
     *         http://www.springframework.org/schema/jdbc http://www.springframework.org/schema/jdbc/spring-jdbc-4.0.xsd
     *         http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.0.xsd"
     *         profile="dev">
     *
     *     <jdbc:embedded-database id="dataSource">
     *         <jdbc:script location="classpath:schema.sql"/>
     *         <jdbc:script location="classpath:test-data.sql"/>
     *     </jdbc:embedded-database>
     *
     * </beans>
     *
     * 与之类似，我们也可以将 profile 设置为 prod，创建适用于生产环境的从 JNDI 获取的 DataSource bean。同样，可以
     * 创建基于连接池定义的 DataSource bean，将其放在另外一个 XML 文件中，并标注为 qa profile。所有的配置文件都会
     * 放到部署单元之中（如 WAR 文件），但是只有 profile 属性与当前激活 profile 相匹配的配置文件才会被用到。
     *
     * 你还可以在根 <beans> 元素中嵌套定义 <beans> 元素，而不是为每个环境都创建一个 profile XML 文件。这能够将所有
     * 的 profile bean 定义放到同一个 XML 文件中，如下所示：
     *
     * <?xml version="1.0" encoding="UTF-8"?>
     * <beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     *        xmlns:jdbc="http://www.springframework.org/schema/jdbc" xmlns:jee="http://www.springframework.org/schema/jee"
     *        xmlns="http://www.springframework.org/schema/beans" xmlns:p="http://www.springframework.org/schema/p"
     *        xsi:schemaLocation="http://www.springframework.org/schema/jee http://www.springframework.org/schema/jee/spring-jee-4.0.xsd
     *         http://www.springframework.org/schema/jdbc http://www.springframework.org/schema/jdbc/spring-jdbc-4.0.xsd
     *         http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.0.xsd">
     *
     *     <beans profile="dev">
     *         <jdbc:embedded-database id="dataSource" type="H2">
     *             <jdbc:script location="classpath:schema.sql"/>
     *             <jdbc:script location="classpath:test-data.sql"/>
     *         </jdbc:embedded-database>
     *     </beans>
     *
     *     <beans profile="qa">
     *         <bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource"
     *               destroy-method="close"
     *               p:url="jdbc:h2:tcp://dbserver/~/test"
     *         p:driverClassName="org.h2.Driver"
     *         p:username="sa"
     *         p:password="password"
     *         p:initialSize="20"
     *         p:maxActive="30"/>
     *     </beans>
     *
     *     <beans profile="prod">
     *         <jee:jndi-lookup id="dataSource"
     *                          lazy-init="true"
     *                          jndi-name="jdbc/myDatabase"
     *                          resource-ref="true"
     *                          proxy-interface="javax.sql.DataSource"/>
     *     </beans>
     *
     * </beans>
     *
     * 除了所有的 bean 定义到了同一个 XML 文件之中，这种配置方式与定义在单独的 XML 文件中的实际效果是一样的。这里有三
     * 个 bean，类型都是 javax.sql.DataSource，并且 ID 都是 dataSource。但是在运行时，只会创建一个 bean，这取决于
     * 处于激活状态的是哪个 profile。
     */
    public static void main(String[] args) {

    }

}
