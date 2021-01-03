package com.siwuxie095.spring.chapter3rd.example2nd;

/**
 * @author Jiajing Li
 * @date 2021-01-03 11:41:57
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 环境与 profile
     *
     * 在开发软件的时候，有一个很大的挑战就是将应用程序从一个环境迁移到另外一个环境。开发阶段中，某些环境相关做法可能并不适合
     * 迁移到生产环境中，甚至即便迁移过去也无法正常工作。数据库配置、加密算法以及与外部系统的集成是跨环境部署时会发生变化的几
     * 个典型例子。
     *
     * 比如，考虑一下数据库配置。在开发环境中，可能会使用嵌入式数据库，并预先加载测试数据。例如，在 Spring 配置类中，可能会
     * 在一个带有 @Bean 注解的方法上使用 EmbeddedDatabaseBuilder：
     *
     *     @Bean(destroyMethod = "shutdown")
     *     public DataSource dataSource() {
     *         return new EmbeddedDatabaseBuilder()
     *                 .addScript("classpath:schema.sql")
     *                 .addScript("classpath:test-data.sql")
     *                 .build();
     *     }
     *
     * 这会创建一个类型为 javax.sql.DataSource 的 bean，这个 bean 是如何创建出来的才是最有意思的。
     *
     * 使用 EmbeddedDatabaseBuilder 会搭建一个嵌入式的 Hypersonic 数据库，它的模式（schema）定义在 schema.sql 中，
     * 测试数据则是通过 test-data.sql 加载的。
     *
     * 当你在开发环境中运行集成测试或者启动应用进行手动测试的时候，这个 DataSource 是很有用的。每次启动它的时候，都能让数据
     * 库处于一个给定的状态。
     *
     * 尽管 EmbeddedDatabaseBuilder 创建的 DataSource 非常适于开发环境，但是对于生产环境来说，这会是一个糟糕的选择。在
     * 生产环境的配置中，你可能会希望使用 JNDI 从容器中获取一个 DataSource。在这样场景中，如下的 @Bean 方法会更加合适：
     *
     *     @Bean
     *     public DataSource dataSource() {
     *         JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
     *         jndiObjectFactoryBean.setJndiName("jdbc/myDS");
     *         jndiObjectFactoryBean.setResourceRef(true);
     *         jndiObjectFactoryBean.setProxyInterface(javax.sql.DataSource.class);
     *         return (DataSource) jndiObjectFactoryBean.getObject();
     *     }
     *
     * 通过 JNDI 获取 DataSource 能够让容器决定该如何创建这个 DataSource，甚至包括切换为容器管理的连接池。即便如此，JNDI
     * 管理的 DataSource 更加适合于生产环境，对于简单的集成和开发测试环境来说，这会带来不必要的复杂性。
     *
     * 同时，在 QA 环境中，你可以选择完全不同的 DataSource 配置，可以配置为 Commons DBCP 连接池，如下所示：
     *
     *     @Bean(destroyMethod = "close")
     *     public DataSource dataSource() {
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
     * 显然，这里展现的三个版本的 dataSource() 方法互不相同。虽然它们都会生成一个类型为 javax.sql.DataSource 的 bean，
     * 但它们的相似点也仅限于此了。每个方法都使用了完全不同的策略来生成 DataSource bean。
     *
     * 再次强调的是，这里的讨论并不是如何配置 DataSource。看起来很简单的 DataSource 实际上并不是那么简单。这是一个很好的
     * 例子，它表现了在不同的环境中某个 bean 会有所不同。必须要有一种方法来配置 DataSource，使其在每种环境下都会选择最为
     * 合适的配置。
     *
     * 其中一种方式就是在单独的配置类（或 XML 文件）中配置每个 bean，然后在构建阶段（可能会使用 Maven 的 profiles）确定
     * 要将哪一个配置编译到可部署的应用中。这种方式的问题在于要为每种环境重新构建应用。当从开发阶段迁移到 QA 阶段时，重新构
     * 建也许算不上什么大问题。但是，从 QA 阶段迁移到生产阶段时，重新构建可能会引入 bug 并且会在 QA 团队的成员中带来不安的
     * 情绪。
     *
     * 值得庆幸的是，Spring 所提供的解决方案 profile 并不需要重新构建。
     */
    public static void main(String[] args) {

    }

}
