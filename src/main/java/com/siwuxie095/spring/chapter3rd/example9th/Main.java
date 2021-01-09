package com.siwuxie095.spring.chapter3rd.example9th;

/**
 * @author Jiajing Li
 * @date 2021-01-09 13:17:05
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 注入外部的值
     *
     * 在 Spring 中，处理外部值的最简单方式就是声明属性源并通过 Spring 的 Environment 来检索属性。
     *
     * 以 ExpressiveConfig 为例，展现了一个基本的 Spring 配置类，它使用外部的属性来装配 BlankDisc bean。如下：
     *
     * @Configuration
     * @PropertySource("file:src/main/java/com/siwuxie095/spring/chapter3rd/example9th/res/app.properties")
     * public class ExpressiveConfig {
     *
     *     @Autowired
     *     private Environment env;
     *
     *     @Bean
     *     public BlankDisc disc() {
     *         return new BlankDisc(
     *                 env.getProperty("disc.title"),
     *                 env.getProperty("disc.artist"));
     *     }
     *
     * }
     *
     * 在本例中，@PropertySource 引用了类路径中一个名为 app.properties 的文件。它大致会如下所示：
     *
     * disc.title=Sgt. Pepper's Lonely Hearts Club Band
     * disc.artist=The Beatles
     *
     * 这个属性文件会加载到 Spring 的 Environment 中，稍后可以从这里检索属性。同时，在 disc() 方法中，会创建一个
     * 新的 BlankDisc，它的构造器参数是从属性文件中获取的，而这是通过调用 getProperty() 实现的。
     *
     *
     *
     * 1、深入学习 Spring 的 Environment
     *
     * 当了解 Environment 时会发现，上面的 getProperty() 方法并不是获取属性值的唯一方法，getProperty() 方法有
     * 四个重载的变种形式：
     *
     * String getProperty(String key);
     * String getProperty(String key, String defaultValue);
     * <T> T getProperty(String key, Class<T> targetType);
     * <T> T getProperty(String key, Class<T> targetType, T defaultValue);
     *
     * 前两种形式的 getProperty() 方法都会返回 String 类型的值。上面已经看到了如何使用第一种 getProperty() 方法。
     * 稍微对 @Bean 方法进行一下修改，这样在指定属性不存在的时候，会使用一个默认值：
     *
     *     @Bean
     *     public BlankDisc disc() {
     *         return new BlankDisc(
     *                 env.getProperty("disc.title", "Rattle and Hum"),
     *                 env.getProperty("disc.artist", "U2"));
     *     }
     *
     * 剩下的两种 getProperty() 方法与前面的两种非常类似，但是它们不会将所有的值都视为 String 类型。例如，假设你想
     * 要获取的值所代表的含义是连接池中所维持的连接数量。如果从属性文件中得到的是一个 String 类型的值，那么在使用之前
     * 还需要将其转换为 Integer 类型。但是，如果使用重载形式的 getProperty() 的话，就能非常便利地解决这个问题：
     *
     * int connectionCount = env.getProperty("db.connection.count", Integer.class);
     * 或
     * int connectionCount = env.getProperty("db.connection.count", Integer.class, 30);
     *
     * Environment 还提供了几个与属性相关的方法，如果你在使用 getProperty() 方法的时候没有指定默认值，并且这个属性
     * 没有定义的话，获取到的值是 null。如果你希望这个属性必须要定义，那么可以使用 getRequiredProperty() 方法，如
     * 下所示：
     *
     *     @Bean
     *     public BlankDisc disc() {
     *         return new BlankDisc(
     *                 env.getRequiredProperty("disc.title"),
     *                 env.getRequiredProperty("disc.artist"));
     *     }
     *
     * 在这里，如果 disc.title 或 disc.artist 属性没有定义的话，将会抛出 IllegalStateException 异常。
     *
     * 如果想检查一下某个属性是否存在的话，那么可以调用 Environment 的 containsProperty() 方法：
     *
     * boolean titleExists = env.containsProperty("disc.title");
     *
     * 最后，如果想将属性解析为类的话，可以使用 getPropertyAsClass() 方法：
     *
     * Class<CompactDisc> cdClass = env.getPropertyAsClass("disc.class", CompactDisc.class);
     *
     * 除了属性相关的功能以外，Environment 还提供了一些方法来检查哪些 profile 处于激活状态：
     * （1）String[] getActiveProfiles()：返回激活 profile 名称的数组；
     * （2）String[] getDefaultProfiles()：返回默认 profile 名称的数组；
     * （3）boolean acceptsProfiles(String... profiles)：如果 environment 支持给定 profile 的话，就返回 true。
     *
     * 在 @Profile 注解的 ProfileCondition 中就有如何使用 acceptsProfiles() 的示例。其中，Environment 是从
     * ConditionContext 中获取到的，在 bean 创建之前，使用 acceptsProfiles() 方法来确保给定 bean 所需的 profile
     * 处于激活状态。通常来讲，并不会频繁使用 Environment 相关的方法，但是知道有这些方法还是有好处的。
     *
     * 直接从 Environment 中检索属性是非常方便的，尤其是在 Java 配置中装配 bean 的时候。但是，Spring 也提供了通过
     * 占位符装配属性的方法，这些占位符的值会来源于一个属性源。
     *
     *
     *
     * 2、解析属性占位符
     *
     * Spring一直支持将属性定义到外部的属性的文件中，并使用占位符值将其插入到 Spring bean 中。在 Spring 装配中，占
     * 位符的形式为使用 "${ ... }" 包装的属性名称。作为样例，可以在 XML 中按照如下的方式解析 BlankDisc 构造器参数：
     *
     *     <bean id="disc"
     *           class="com.siwuxie095.spring.chapter2nd.example6th.BlankDisc"
     *           c:_0="${disc.title}"
     *           c:_1="${disc.artist}">
     *     </bean>
     *
     * 可以看到，title 构造器参数所给定的值是从一个属性中解析得到的，这个属性的名称为 disc.title。artist 参数装配的
     * 是名为 disc.artist 的属性值。按照这种方式，XML 配置没有使用任何硬编码的值，它的值是从配置文件以外的一个源中解
     * 析得到的（稍后会讨论这些属性是如何解析的）。
     *
     * 如果依赖于组件扫描和自动装配来创建和初始化应用组件的话，那么就没有指定占位符的配置文件或类了。在这种情况下，可以
     * 使用 @Value 注解，它的使用方式与 @Autowired 注解非常相似。比如，在 BlankDisc 类中，构造器可以改成如下所示：
     *
     *     public BlankDisc(@Value("disc.title") String title, @Value("disc.artist") String artist) {
     *         this.title = title;
     *         this.artist = artist;
     *     }
     *
     * 为了使用占位符，必须要配置一个 PropertyPlaceholderConfigurer bean 或 PropertySourcesPlaceholderConfigurer
     * bean。从 Spring 3.1 开始，推荐使用后者 PropertySourcesPlaceholderConfigurer，因为它能够基于 Spring
     * Environment 及其属性源来解析占位符。
     *
     * 如下的 @Bean 方法在 Java 中配置了 PropertySourcesPlaceholderConfigurer：
     *
     *     @Bean
     *     public PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
     *         return new PropertySourcesPlaceholderConfigurer();
     *     }
     *
     * 如果你想使用 XML 配置的话，Spring context 命名空间中的 <context:propertyplaceholder> 元素将会为你生成
     * PropertySourcesPlaceholderConfigurer bean：
     *
     *     <context:property-placeholder />
     *
     * 解析外部属性能够将值的处理推迟到运行时，但是它的关注点在于根据名称解析来自于 Spring Environment 和属性源的属性。
     * 而 Spring 表达式语言提供了一种更通用的方式在运行时计算所要注入的值。
     */
    public static void main(String[] args) {

    }

}
