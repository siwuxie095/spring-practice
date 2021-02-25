package com.siwuxie095.spring.chapter11th.example2nd;

/**
 * @author Jiajing Li
 * @date 2021-02-24 21:58:12
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 在 Spring 中集成 Hibernate
     *
     * Hibernate 是在开发者社区很流行的开源持久化框架。它不仅提供了基本的对象关系映射，还提供了 ORM 工具所应具有的
     * 所有复杂功能，比如缓存、延迟加载、预先抓取以及分布式缓存。
     *
     * 在这里，主要会关注 Spring 如何与 Hibernate 集成，而不会涉及太多 Hibernate 使用时的复杂细节。
     *
     *
     *
     * 声明 Hibernate 的 Session 工厂
     *
     * 使用 Hibernate 所需的主要接口是 org.hibernate.Session。Session 接口提供了基本的数据访问功能，如保存、更
     * 新、删除以及从数据库加载对象的功能。通过 Hibernate 的 Session 接口，应用程序的 Repository 能够满足所有的
     * 持久化需求。
     *
     * 获取 Hibernate Session 对象的标准方式是借助于 Hibernate SessionFactory 接口的实现类。除了一些其他的任务，
     * SessionFactory 主要负责 Hibernate Session 的打开、关闭以及管理。
     *
     * 在 Spring 中，要通过 Spring 的某一个 Hibernate Session 工厂 bean 来获取 Hibernate SessionFactory。
     * 从 3.1 版本开始，Spring 提供了三个 Session 工厂 bean 以供选择：
     * （1）org.springframework.orm.hibernate3.LocalSessionFactoryBean
     * （2）org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean
     * （3）org.springframework.orm.hibernate4.LocalSessionFactoryBean
     *
     * 这些 Session 工厂 bean 都是 Spring FactoryBean 接口的实现，它们会产生一个 HibernateSessionFactory，
     * 它能够装配进任何 SessionFactory 类型的属性中。这样的话，就能在应用的 Spring 上下文中，与其他的 bean 一起
     * 配置 Hibernate Session 工厂。
     *
     * 至于选择使用哪一个 Session 工厂，这取决于使用哪个版本的 Hibernate 以及你使用 XML 还是使用注解来定义对象
     * - 数据库之间的映射关系。如果你使用 Hibernate 3.2 或更高版本（直到 Hibernate 4.0，但不包含这个版本）并且
     * 使用 XML 定义映射的话，那么你需要定义 Spring 的 org.springframework.orm.hibernate3 包中的
     * LocalSessionFactoryBean：
     *
     *     @Bean
     *     public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
     *         LocalSessionFactoryBean sfb = new LocalSessionFactoryBean();
     *         sfb.setDataSource(dataSource);
     *         sfb.setMappingResources(new String[] { "Spitter.hbm.xml" });
     *         Properties props = new Properties();
     *         props.setProperty("dialect", "org.hibernate.dialect.H2Dialect");
     *         sfb.setHibernateProperties(props);
     *         return sfb;
     *     }
     *
     * 在配置 LocalSessionFactory Bean 时，使用了三个属性。属性 dataSource 装配了一个 DataSource bean 的引用。
     * 属性 mappingResources 列出了一个或多个的 Hibernate 映射文件，在这些文件中定义了应用程序的持久化策略。最后，
     * hibernateProperties 属性配置了 Hibernate 如何进行操作的细节。在本示例中，配置 Hibernate 使用 H2 数据库
     * 并且要按照 H2Dialect 来构建 SQL。
     *
     * 如果你更倾向于使用注解的方式来定义持久化，并且你还没有使用 Hibernate 4 的话，那么需要使用
     * AnnotationSessionFactoryBean 来代替 LocalSessionFactoryBean：
     *
     *     @Bean
     *     public AnnotationSessionFactoryBean sessionFactory(DataSource dataSource) {
     *         AnnotationSessionFactoryBean sfb = new AnnotationSessionFactoryBean();
     *         sfb.setDataSource(dataSource);
     *         sfb.setPackagesToScan(new String[] { "com.habuma.spittr.domain" });
     *         Properties props = new Properties();
     *         props.setProperty("dialect", "org.hibernate.dialect.H2Dialect");
     *         sfb.setHibernateProperties(props);
     *         return sfb;
     *     }
     *
     * 如果你使用 Hibernate 4 的话，那么就应该使用 org.springframework.orm.hibernate4 中的
     * LocalSessionFactoryBean。尽管它与 Hibernate 3 包中的 LocalSessionFactoryBean 使用了相同的名称，但是
     * Spring 3.1 新引入的这个 Session 工厂类似于 Hibernate 3 中 LocalSessionFactoryBean 和
     * AnnotationSessionFactoryBean 的结合体。它有很多相同的属性，能够支持基于 XML 的映射和基于注解的映射。如下
     * 的代码展现了如何对它进行配置，使其支持基于注解的映射：
     *
     *     @Bean
     *     public LocalSessionFactoryBean sessionFactoryBean(DataSource dataSource) {
     *         LocalSessionFactoryBean sfb = new LocalSessionFactoryBean();
     *         sfb.setDataSource(dataSource);
     *         sfb.setPackagesToScan(new String[] { "com.habuma.spittr.domain" });
     *         Properties props = new Properties();
     *         props.setProperty("dialect", "org.hibernate.dialect.H2Dialect");
     *         sfb.setHibernateProperties(props);
     *         return sfb;
     *     }
     *
     * 在这两个配置中，dataSource 和 hibernateProperties 属性都声明了从哪里获取数据库连接以及要使用哪一种数据库。
     * 这里不再列出 Hibernate 配置文件，而是使用 packagesToScan 属性告诉 Spring 扫描一个或多个包以查找域类，这
     * 些类通过注解的方式表明要使用 Hibernate 进行持久化，这些类可以使用的注解包括 JPA 的 @Entity 或
     * @MappedSuperclass 以及 Hibernate 的 @Entity。
     *
     * 如果愿意的话，你还可以使用 annotatedClasses 属性来将应用程序中所有的持久化类以全限定名的方式明确列出：
     *
     * sfb.setAnnotatedClasses(new Class<?>[] { Spitter.class, Spittle.class });
     *
     * annotatedClasses 属性对于准确指定少量的域类是不错的选择。如果你有很多的域类并且不想将其全部列出，又或者你想
     * 自由地添加或移除域类而不想修改 Spring 配置的话，那使用 packagesToScan 属性是更合适的。
     *
     * 在 Spring 应用上下文中配置完 Hibernate 的 Session 工厂 bean 后，就可以创建自己的 Repository 类了。
     *
     *
     *
     * 构建不依赖于 Spring 的 Hibernate 代码
     *
     * 在 Spring 和 Hibernate 的早期岁月中，编写 Repository 类将会涉及到使用 Spring 的 HibernateTemplate。
     * HibernateTemplate 能够保证每个事务使用同一个 Session。但是这种方式的弊端在于 Repository 实现会直接与
     * Spring 耦合。
     *
     * 现在的最佳实践是不再使用 HibernateTemplate，而是使用上下文 Session（Contextual session）。通过这种方式，
     * 会直接将 Hibernate SessionFactory 装配到 Repository 中，并使用它来获取 Session，如下：
     *
     * @Repository
     * public class HibernateSpitterRepository implements SpitterRepository {
     *
     *     private SessionFactory sessionFactory;
     *
     *     @Inject
     *     public HibernateSpitterRepository(SessionFactory sessionFactory) {
     *         this.sessionFactory = sessionFactory;
     *     }
     *
     *     private Session currentSession() {
     *         return sessionFactory.getCurrentSession();
     *     }
     *
     *     @Override
     *     public long count() {
     *         return findAll().size();
     *     }
     *
     *     @Override
     *     public Spitter save(Spitter spitter) {
     *         Serializable id = currentSession().save(spitter);
     *         return new Spitter((Long) id,
     *                 spitter.getUsername(),
     *                 spitter.getPassword(),
     *                 spitter.getFullName(),
     *                 spitter.getEmail(),
     *                 spitter.isUpdateByEmail());
     *     }
     *
     *     @Override
     *     public Spitter findOne(long id) {
     *         return (Spitter) currentSession().get(Spitter.class, id);
     *     }
     *
     *     @Override
     *     public Spitter findByUsername(String username) {
     *         return (Spitter) currentSession()
     *                 .createCriteria(Spitter.class)
     *                 .add(Restrictions.eq("username", username))
     *                 .list().get(0);
     *     }
     *
     *     @Override
     *     public List<Spitter> findAll() {
     *         return (List<Spitter>) currentSession().createCriteria(Spitter.class).list();
     *     }
     *
     * }
     *
     * 这里有几个地方需要注意。首先，通过 @Inject 注解让 Spring 自动将一个 SessionFactory 注入到
     * HibernateSpitterRepository 的 sessionFactory 属性中。接下来，在 currentSession() 方法
     * 中，使用这个 SessionFactory 来获取当前事务的 Session。
     *
     * 另外需要注意的是，这里在类上使用了 @Repository 注解，这会帮助做两件事情。首先，@Repository 是
     * Spring 的另一种构造性注解，它能够像其他注解一样被 Spring 的组件扫描所扫描到。这样就不必明确声明
     * HibernateSpitterRepository bean 了，只要这个 Repository 类在组件扫描所涵盖的包中即可。
     *
     * 除了帮助减少显式配置以外，@Repository 还有另外一个用处。回想一下模板类，它有一项任务就是捕获平台
     * 相关的异常，然后使用 Spring 统一非检查型异常的形式重新抛出。如果使用 Hibernate 上下文 Session
     * 而不是 Hibernate 模板的话，那异常转换会怎么处理呢？
     *
     * 为了给不使用模板的 Hibernate Repository 添加异常转换功能，只需在 Spring 应用上下文中添加一个
     * PersistenceExceptionTranslationPostProcessor bean：
     *
     * @Bean
     * public BeanPostProcessor persistenceTranslation() {
     *     return new PersistenceExceptionTranslationPostProcessor();
     * }
     *
     * PersistenceExceptionTranslationPostProcessor 是一个 bean 后置处理器（bean post-processor），
     * 它会在所有拥有 @Repository 注解的类上添加一个通知器（advisor），这样就会捕获任何平台相关的异常并以
     * Spring 非检查型数据访问异常的形式重新抛出。
     *
     * 现在，Hibernate 版本的 Repository 已经完成了。在开发时，没有依赖 Spring 的特定类（除了 @Repository
     * 注解以外）。这种不使用模板的方式也适用于开发纯粹的基于 JPA 的 Repository，后续将会再尝试开发另一个
     * SpitterRepository 实现类，这次使用的是 JPA。
     */
    public static void main(String[] args) {

    }

}
