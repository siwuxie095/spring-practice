package com.siwuxie095.spring.chapter11th.example3rd;

/**
 * @author Jiajing Li
 * @date 2021-02-25 20:48:30
 */
@SuppressWarnings("all")
public class Main {

    /**
     * Spring 与 Java 持久化 API
     *
     * Java 持久化 API（Java Persistence API，JPA）诞生在 EJB 2 实体 Bean 的废墟之上，并成为下一代 Java 持久化标准。
     * JPA 是基于 POJO 的持久化机制，它从 Hibernate 和 Java 数据对象（Java Data Object，JDO）上借鉴了很多理念并加入了
     * Java 5 注解的特性。
     *
     * 在 Spring 2.0 版本中，Spring 首次集成了 JPA 的功能。具有讽刺意味的是，很多人批评（或赞赏）Spring 颠覆了 EJB。
     * 但是，当 Spring 支持 JPA 后，很多开发人员都推荐在基于 Spring 的应用程序中使用 JPA 实现持久化。实际上，有些人还
     * 将 Spring-JPA 的组合称为 POJO 开发的梦之队。
     *
     * 在 Spring 中使用 JPA 的第一步是要在 Spring 应用上下文中将实体管理器工厂（entity manager factory）按照 bean 的
     * 形式来进行配置。
     *
     *
     *
     * 1、配置实体管理器工厂
     *
     * 简单来讲，基于 JPA 的应用程序需要使用 EntityManagerFactory 的实现类来获取 EntityManager 实例。JPA 定义了两种类
     * 型的实体管理器：
     * （1）应用程序管理类型（Application-managed）：当应用程序向实体管理器工厂直接请求实体管理器时，工厂会创建一个实体管
     * 理器。在这种模式下，程序要负责打开或关闭实体管理器并在事务中对其进行控制。这种方式的实体管理器适合于不运行在 Java EE
     * 容器中的独立应用程序。
     * （2）容器管理类型（Container-managed）：实体管理器由 Java EE 创建和管理。应用程序根本不与实体管理器工厂打交道。相
     * 反，实体管理器直接通过注入或 JNDI 来获取。容器负责配置实体管理器工厂。这种类型的实体管理器最适用于 Java EE 容器，在
     * 这种情况下会希望在 persistence.xml 指定的 JPA 配置之外保持一些自己对 JPA 的控制。
     *
     * 以上的两种实体管理器实现了同一个 EntityManager 接口。关键的区别不在于 EntityManager 本身，而是在于 EntityManager
     * 的创建和管理方式。应用程序管理类型的 EntityManager 是由 EntityManagerFactory 创建的，而 EntityManagerFactory
     * 是通过 PersistenceProvider 的 createEntityManagerFactory() 方法得到的。与此相对，容器管理类型的 EntityManager
     * 也是由 EntityManagerFactory 创建的，但对应的 EntityManagerFactory 是通过 PersistenceProvider 的
     * createContainerEntityManagerFactory() 方法获得的。
     *
     * 这对想使用 JPA 的 Spring 开发者来说又意味着什么呢？其实这并没太大的关系。不管你希望使用哪种 EntityManagerFactory，
     * Spring 都会负责管理 EntityManager。如果你使用的是应用程序管理类型的实体管理器，Spring 承担了应用程序的角色并以透明
     * 的方式处理 EntityManager。在容器管理的场景下，Spring 会担当容器的角色。
     *
     * 这两种实体管理器工厂分别由对应的 Spring 工厂 Bean 创建：
     * （1）LocalEntityManagerFactoryBean 生成应用程序管理类型的 EntityManagerFactory；
     * （2）LocalContainerEntityManagerFactoryBean 生成容器管理类型的 EntityManagerFactory。
     *
     * 需要说明的是，选择应用程序管理类型的还是容器管理类型的 EntityManagerFactory，对于基于 Spring 的应用程序来讲是完全
     * 透明的。当组合使用 Spring 和 JPA 时，处理 EntityManagerFactory 的复杂细节被隐藏了起来，数据访问代码只需关注它们
     * 的真正目标即可，也就是数据访问。
     *
     * 应用程序管理类型和容器管理类型的实体管理器工厂之间唯一值得关注的区别是在 Spring 应用上下文中如何进行配置。下面先看看
     * 如何在 Spring 中配置应用程序管理类型的 LocalEntityManagerFactoryBean，然后再看看如何配置容器管理类型的
     * LocalContainerEntityManagerFactoryBean。
     *
     *
     * 1.1、配置应用程序管理类型的 JPA
     *
     * 对于应用程序管理类型的实体管理器工厂来说，它绝大部分配置信息来源于一个名为 persistence.xml 的配置文件。这个文件必须
     * 位于类路径下的 META-INF 目录下。
     *
     * persistence.xml 的作用在于定义一个或多个持久化单元。持久化单元是同一个数据源下的一个或多个持久化类。简单来讲，
     * persistence.xml 列出了一个或多个的持久化类以及一些其他的配置如数据源和基于 XML 的配置文件。如下是一个典型的
     * persistence.xml 文件，它是用于 Spittr 应用程序的：
     *
     * <persistence xmlns="http://java.sun.com/xml/ns/persistence" version="1.0">
     *     <persistence-unit name="spitterPU">
     *         <class>com.habuma.spittr.domain.Spitter</class>
     *         <class>com.habuma.spittr.domain.Spittle</class>
     *         <properties>
     *             <property name="toplink.jdbc.driver" value="org.hsqldb.jdbcDriver" />
     *             <property name="toplink.jdbc.url" value="jdbc:hsqldb:hsql://localhost/spitter" />
     *             <property name="toplink.jdbc.user" value="sa" />
     *             <property name="toplink.jdbc.password" value="" />
     *         </properties>
     *     </persistence-unit>
     * </persistence>
     *
     * 因为在 persistence.xml 文件中包含了大量的配置信息，所以在 Spring 中需要配置的就很少了。可以通过以下的 @Bean 注解
     * 方法在 Spring 中声明 LocalEntityManagerFactoryBean：
     *
     *     @Bean
     *     public LocalEntityManagerFactoryBean entityManagerFactoryBean() {
     *         LocalEntityManagerFactoryBean emf = new LocalEntityManagerFactoryBean();
     *         emf.setPersistenceUnitName("spitterPU");
     *         return emf;
     *     }
     *
     * 赋给 persistenceUnitName 属性的值就是 persistence.xml 中持久化单元的名称。
     *
     * 创建应用程序管理类型的 EntityManagerFactory 都是在 persistence.xml 中进行的，而这正是应用程序管理的本意。在应用
     * 程序管理的场景下（不考虑 Spring 时），完全由应用程序本身来负责获取 EntityManagerFactory，这是通过 JPA 实现的
     * PersistenceProvider 做到的。如果每次请求 EntityManagerFactory 时都需要定义持久化单元，那代码将会迅速膨胀。通过
     * 将其配置在 persistence.xml 中，JPA 就能够在这个特定的位置查找持久化单元定义了。
     *
     * 但借助于 Spring 对 JPA 的支持，不再需要直接处理 PersistenceProvider 了。因此，再将配置信息放在 persistence.xml
     * 中就显得不那么明智了。实际上，这样做妨碍了在 Spring 中配置 EntityManagerFactory（如果不是这样的话，就可以提供一个
     * Spring 配置的数据源）。
     *
     * 鉴于以上的原因，下面来关注一下容器管理的 JPA。
     *
     *
     * 1.2、使用容器管理类型的 JPA
     *
     * 容器管理的 JPA 采取了一个不同的方式。当运行在容器中时，可以使用容器提供的信息来生成 EntityManagerFactory。
     *
     * PS：在这里的场景下是 Spring 容器。
     *
     * 你可以将数据源信息配置在 Spring 应用上下文中，而不是在 persistence.xml 中了。例如，如下的 @Bean 注解方法声明了在
     * Spring 中如何使用 LocalContainerEntityManagerFactoryBean 来配置容器管理类型的 JPA：
     *
     *     @Bean
     *     public LocalContainerEntityManagerFactoryBean emf(DataSource dataSource,
     *                                                       JpaVendorAdapter jpaVendorAdapter) {
     *         LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
     *         emf.setDataSource(dataSource);
     *         emf.setPersistenceUnitName("spittr");
     *         emf.setJpaVendorAdapter(jpaVendorAdapter);
     *         return emf;
     *     }
     *
     * 这里使用了 Spring 配置的数据源来设置 dataSource 属性。任何 javax.sql.DataSource 的实现都是可以的。尽管数据源还
     * 可以在 persistence.xml 中进行配置，但是这个属性指定的数据源具有更高的优先级。
     *
     * jpaVendorAdapter 属性用于指明所使用的是哪一个厂商的 JPA 实现。Spring 提供了多个 JPA 厂商适配器：
     * （1）EclipseLinkJpaVendorAdapter
     * （2）HibernateJpaVendorAdapter
     * （3）OpenJpaVendorAdapter
     * （4）TopLinkJpaVendorAdapter（在 Spring 3.1 版本中，已经将其废弃了）
     *
     * 在本例中，使用 Hibernate 作为 JPA 实现，所以将其配置为 HibernateJpaVendorAdapter：
     *
     *     @Bean
     *     public JpaVendorAdapter jpaVendorAdapter() {
     *         HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
     *         adapter.setDatabase(Database.HSQL);
     *         adapter.setShowSql(true);
     *         adapter.setGenerateDdl(false);
     *         adapter.setDatabasePlatform("org.hibernate.dialect.H2Dialect");
     *         return adapter;
     *     }
     *
     * 有多个属性需要设置到厂商适配器上，但是最重要的是 database 属性，在上面设置了要使用的数据库是 Hypersonic。这个属性
     * 支持的其他值如下（数据库平台和 database 属性的值）：
     * （1）IBM DB2：DB2
     * （2）Apache Derby：DERBY
     * （3）H2：H2
     * （4）Hypersonic：HSQL
     * （5）Informix：INFORMIX
     * （6）MySQL：MYSQL
     * （7）Oracle：ORACLE
     * （8）PostgresQL：POSTGRESQL
     * （9）Microsoft SQL Server：SQLSERVER
     * （10）Sybase：SYBASE
     *
     * 一些特定的动态持久化功能需要对持久化类按照指令（instrumentation）进行修改才能支持。在属性延迟加载（只在它们被实际访
     * 问时才从数据库中获取）的对象中，必须要包含知道如何查询未加载数据的代码。一些框架使用动态代理实现延迟加载，而有一些框架
     * 像 JDO，则是在编译时执行类指令。
     *
     * 选择哪一种实体管理器工厂主要取决于如何使用它。但是，下面的小技巧可能会让你更加倾向于使用
     * LocalContainerEntityManagerFactoryBean。
     *
     * persistence.xml 文件的主要作用就在于识别持久化单元中的实体类。但是从 Spring 3.1 开始，
     * 就能够在 LocalContainerEntityManagerFactoryBean 中直接设置 packagesToScan 属性：
     *
     *     @Bean
     *     public LocalContainerEntityManagerFactoryBean emf(DataSource dataSource,
     *                                                       JpaVendorAdapter jpaVendorAdapter) {
     *         LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
     *         emf.setDataSource(dataSource);
     *         emf.setPersistenceUnitName("spittr");
     *         emf.setJpaVendorAdapter(jpaVendorAdapter);
     *         emf.setPackagesToScan("com.habuma.spittr.domain");
     *         return emf;
     *     }
     *
     * 在这个配置中，LocalContainerEntityManagerFactoryBean 会扫描 com.habuma.spittr.domain 包，查找带有 @Entity
     * 注解的类。因此，没有必要在 persistence.xml 文件中进行声明了。同时，因为 DataSource 也是注入到
     * LocalContainerEntityManagerFactoryBean 中的，所以也没有必要在 persistence.xml 文件中配置数据库信息了。那么结
     * 论就是，persistence.xml 文件完全没有必要存在了！你尽可以将其删除，让 LocalContainerEntityManagerFactoryBean
     * 来处理这些事情。
     *
     *
     * 1.3、从 JNDI 获取实体管理器工厂
     *
     * 还有一件需要注意的事项，如果将 Spring 应用程序部署在应用服务器中，EntityManagerFactory 可能已经创建好了并且位于
     * JNDI 中等待查询使用。在这种情况下，可以使用 Spring jee 命名空间下的 <jee:jndi-lookup> 元素来获取对
     * EntityManagerFactory 的引用：
     *
     * <jee:jndi-lookup id="emf" jndi-name="persistence/spitterPU" />
     *
     * 也可以使用如下的 Java 配置来获取 EntityManagerFactory：
     *
     * @Bean
     * public JndiObjectFactoryBean entityManagerFactory() {
     *     JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
     *     bean.setJndiName("jdbc/SpittrDS");
     *     return bean;
     * }
     *
     * 尽管这种方法没有返回 EntityManagerFactory，但是它的结果就是一个 EntityManagerFactory bean。这是因为它所返回的
     * JndiObjectFactoryBean 是 FactoryBean 接口的实现，它能够创建 EntityManagerFactory。
     *
     * 不管你采用何种方式得到 EntityManagerFactory，一旦得到这样的对象，接下来就可以编写 Repository 了。
     *
     *
     *
     * 2、编写基于 JPA 的 Repository
     *
     * 正如 Spring 对其他持久化方案的集成一样，Spring 对 JPA 集成也提供了 JpaTemplate 模板以及对应的支持类
     * JpaDaoSupport。但是，为了实现更纯粹的 JPA 方式，基于模板的 JPA 已经被弃用了。
     *
     * 鉴于纯粹的 JPA 方式远胜于基于模板的 JPA，所以在这里将会重点关注如何构建不依赖 Spring 的JPA Repository。
     * 如下代码中的 JpaSpitterRepository 展现了如何开发不使用 Spring JpaTemplate 的 JPA Repository：
     *
     * @Repository
     * @Transactional
     * public class JpaSpitterRepository implements SpitterRepository {
     *
     *     @PersistenceUnit
     *     private EntityManagerFactory emf;
     *
     *     @Override
     *     public long count() {
     *         return findAll().size();
     *     }
     *
     *     @Override
     *     public Spitter save(Spitter spitter) {
     *         emf.createEntityManager().persist(spitter);
     *         return spitter;
     *     }
     *
     *     @Override
     *     public Spitter findOne(long id) {
     *         return emf.createEntityManager().find(Spitter.class, id);
     *     }
     *
     *     @Override
     *     public Spitter findByUsername(String username) {
     *         return (Spitter) emf.createEntityManager()
     *                 .createQuery("select s from Spitter s where s.username=?").setParameter(1, username)
     *                 .getSingleResult();
     *     }
     *
     *     @Override
     *     public List<Spitter> findAll() {
     *         return (List<Spitter>) emf.createEntityManager()
     *                 .createQuery("select s from Spitter s")
     *                 .getResultList();
     *     }
     *
     * }
     *
     * 这里需要注意的是 EntityManagerFactory 属性，它使用了 @PersistenceUnit 注解，因此，Spring 会将
     * EntityManagerFactory 注入到 Repository 之中。有了 EntityManagerFactory 之后，JpaSpitterRepository
     * 的方法就能使用它来创建 EntityManager 了，然后 EntityManager 可以针对数据库执行操作。
     *
     * 在 JpaSpitterRepository 中，唯一的问题在于每个方法都会调用 createEntityManager()。除了引入易出错的重复代码以外，
     * 这还意味着每次调用 Repository 的方法时，都会创建一个新的 EntityManager。这种复杂性源于事务。如果能够预先准备好
     * EntityManager，那会不会更加方便呢？
     *
     * 这里的问题在于 EntityManager 并不是线程安全的，一般来讲并不适合注入到像 Repository 这样共享的单例 bean 中。但是，
     * 这并不意味着没有办法要求注入 EntityManager。如下代码展现了如何借助 @PersistentContext 注解为 JpaSpitterRepository
     * 设置 EntityManager。
     *
     * @Repository
     * @Transactional
     * public class JpaSpitterRepository implements SpitterRepository {
     *
     *     @PersistenceContext
     *     private EntityManager entityManager;
     *
     *     @Override
     *     public long count() {
     *         return findAll().size();
     *     }
     *
     *     @Override
     *     public Spitter save(Spitter spitter) {
     *         entityManager.persist(spitter);
     *         return spitter;
     *     }
     *
     *     @Override
     *     public Spitter findOne(long id) {
     *         return entityManager.find(Spitter.class, id);
     *     }
     *
     *     @Override
     *     public Spitter findByUsername(String username) {
     *         return (Spitter) entityManager
     *                 .createQuery("select s from Spitter s where s.username=?").setParameter(1, username)
     *                 .getSingleResult();
     *     }
     *
     *     @Override
     *     public List<Spitter> findAll() {
     *         return (List<Spitter>) entityManager.createQuery("select s from Spitter s").getResultList();
     *     }
     *
     * }
     *
     * 在这个新版本的 JpaSpitterRepository 中，直接为其设置了 EntityManager，这样的话，在每个方法中就没有必要再通过
     * EntityManagerFactory 创建 EntityManager 了。尽管这种方式非常便利，但是你可能会担心注入的 EntityManager 会
     * 有线程安全性的问题。
     *
     * 这里的真相是 @PersistenceContext 并不会真正注入 EntityManager —— 至少，精确来讲不是这样的。它没有将真正的
     * EntityManager 设置给 Repository，而是给了它一个 EntityManager 的代理。真正的 EntityManager 是与当前事务
     * 相关联的那一个，如果不存在这样的 EntityManager 的话，就会创建一个新的。这样的话，就能始终以线程安全的方式使用
     * 实体管理器。
     *
     * 另外，还需要了解 @PersistenceUnit 和 @PersistenceContext 并不是 Spring 的注解，它们是由 JPA 规范提供的。
     * 为了让 Spring 理解这些注解，并注入 EntityManager Factory 或 EntityManager，必须要配置 Spring 的
     * PersistenceAnnotationBeanPostProcessor。如果你已经使用了 <context:annotation-config> 或
     * <context:component-scan>，那么你就不必再担心了，因为这些配置元素会自动注册
     * PersistenceAnnotationBeanPostProcessor bean。否则的话，需要显式地注册这个 bean：
     *
     *
     * @Bean
     * public PersistenceAnnotationBeanPostProcessor paPostProcessor() {
     *     return new PersistenceAnnotationBeanPostProcessor();
     * }
     *
     * 你可能也注意到了 JpaSpitterRepository 使用了 @Repository 和 @Transactional 注解。@Transactional 表明这个
     * Repository 中的持久化方法是在事务上下文中执行的。
     *
     * 对于 @Repository 注解，它的作用与开发 Hibernate 上下文 Session 版本的 Repository 时是一致的。由于没有使用模板类
     * 来处理异常，所以需要为 Repository 添加 @Repository 注解，这样 PersistenceExceptionTranslationPostProcessor
     * 就会知道要将这个 bean 产生的异常转换成 Spring 的统一数据访问异常。
     *
     * 既然提到了 PersistenceExceptionTranslationPostProcessor，要记住的是需要将其作为一个 bean 装配到 Spring 中：
     *
     * @Bean
     * public BeanPostProcessor persistenceTranslation() {
     *     return new PersistenceExceptionTranslationPostProcessor();
     * }
     *
     * 提醒一下，不管对于 JPA 还是Hibernate，异常转换都不是强制要求的。如果你希望在 Repository 中抛出特定的 JPA 或
     * Hibernate 异常，只需将 PersistenceExceptionTranslationPostProcessor 省略掉即可，这样原来的异常就会正常
     * 地处理。但是，如果使用了 Spring 的异常转换，你会将所有的数据访问异常置于 Spring 的体系之下，这样以后切换持久化
     * 机制的话会更容易。
     */
    public static void main(String[] args) {

    }

}
