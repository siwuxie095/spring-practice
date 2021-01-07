package com.siwuxie095.spring.chapter3rd.example7th;

/**
 * @author Jiajing Li
 * @date 2021-01-07 08:13:46
 */
@SuppressWarnings("all")
public class Main {

    /**
     * bean 的作用域
     *
     * 在默认情况下，Spring 应用上下文中所有 bean 都是作为以单例（singleton）的形式创建的。也就是说，不管给定的
     * 一个 bean 被注入到其他 bean 多少次，每次所注入的都是同一个实例。
     *
     * 在大多数情况下，单例 bean 是很理想的方案。初始化和垃圾回收对象实例所带来的成本只留给一些小规模任务，在这些
     * 任务中，让对象保持无状态并且在应用中反复重用这些对象可能并不合理。
     *
     * 有时候，可能会发现，你所使用的类是易变的（mutable），它们会保持一些状态，因此重用是不安全的。在这种情况下，
     * 将 class 声明为单例的 bean 就不是什么好主意了，因为对象会被污染，稍后重用的时候会出现意想不到的问题。
     *
     * Spring 定义了多种作用域，可以基于这些作用域创建 bean，包括：
     * （1）单例（Singleton）：在整个应用中，只创建 bean 的一个实例；
     * （2）原型（Prototype）：每次注入或者通过Spring应用上下文获取的时候，都会创建一个新的 bean 实例。
     * （3）会话（Session）：在 Web 应用中，为每个会话创建一个 bean 实例。
     * （4）请求（Request）：在 Web 应用中，为每个请求创建一个 bean 实例。
     *
     * 单例是默认的作用域，但是正如之前所述，对于易变的类型，这并不合适。如果选择其他的作用域，要使用 @Scope 注解，
     * 它可以与 @Component 或 @Bean 一起使用。
     *
     * 例如，如果你使用组件扫描来发现和声明 bean，那么你可以在 bean 的类上使用 @Scope 注解，将其声明为原型 bean：
     *
     * @Component
     * @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
     * public class Notepad {
     *
     * }
     *
     * 这里，使用 ConfigurableBeanFactory 类的 SCOPE_PROTOTYPE 常量设置了原型作用域。
     *
     * 当然，也可以使用 @Scope("prototype")，但是使用 SCOPE_PROTOTYPE 常量更加安全并且不易出错。
     *
     * 如果你想在 Java 配置中将 Notepad 声明为原型 bean，那么可以组合使用 @Scope 和 @Bean 来指定所需的作用域：
     *
     *     @Bean
     *     @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
     *     public Notepad notepad() {
     *         return new Notepad();
     *     }
     *
     * 同样，如果你使用 XML 来配置 bean 的话，可以使用 <bean> 元素的 scope 属性来设置作用域：
     *
     *     <bean id="notepad"
     *           class="com.siwuxie095.spring.chapter3rd.example7th.Notepad"
     *           scope="prototype"/>
     *
     * 不管你使用哪种方式来声明原型作用域，每次注入或从 Spring 应用上下文中检索该 bean 的时候，都会创建新的实例。
     * 这样所导致的结果就是每次操作都能得到自己的 Notepad 实例。
     *
     *
     *
     * 1、使用会话和请求作用域
     *
     * 在 Web 应用中，如果能够实例化在会话和请求范围内共享的 bean，那将是非常有价值的事情。例如，在典型的电子商务
     * 应用中，可能会有一个 bean 代表用户的购物车。如果购物车是单例的话，那么将会导致所有的用户都会向同一个购物车
     * 中添加商品。另一方面，如果购物车是原型作用域的，那么在应用中某一个地方往购物车中添加商品，在应用的另外一个地
     * 方可能就不可用了，因为在这里注入的是另外一个原型作用域的购物车。
     *
     * 就购物车 bean 来说，会话作用域是最为合适的，因为它与给定的用户关联性最大。要指定会话作用域，可以使用 @Scope
     * 注解，它的使用方式与指定原型作用域是相同的：
     *
     * @Component
     * @Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.INTERFACES)
     * public interface ShoppingCart {
     *
     * }
     *
     * 这里，将 value 设置成了 WebApplicationContext 中的 SCOPE_SESSION 常量（它的值是 session）。这会告诉
     * Spring 为 Web 应用中的每个会话创建一个 ShoppingCart。这会创建多个 ShoppingCart bean 的实例，但是对于
     * 给定的会话只会创建一个实例，在当前会话相关的操作中，这个 bean 实际上相当于单例的。
     *
     * 要注意的是，@Scope 同时还有一个 proxyMode 属性，它被设置成了 ScopedProxyMode.INTERFACES。这个属性解决
     * 了将会话或请求作用域的 bean 注入到单例 bean 中所遇到的问题。在描述 proxyMode 属性之前，不妨先来看一下
     * proxyMode 所解决问题的场景。
     *
     * 假设要将 ShoppingCart bean 注入到单例 StoreService bean 的 Setter 方法中，如下所示：
     *
     * @Component
     * public class StoreService {
     *
     *     private ShoppingCart shoppingCart;
     *
     *     @Autowired
     *     public void setShoppingCart(ShoppingCart shoppingCart) {
     *         this.shoppingCart = shoppingCart;
     *     }
     *
     * }
     *
     * 因为 StoreService 是一个单例的 bean，会在 Spring 应用上下文加载的时候创建。当它创建的时候，Spring 会试
     * 图将 ShoppingCart bean 注入到 setShoppingCart() 方法中。但是 ShoppingCart bean 是会话作用域的，此时
     * 并不存在。直到某个用户进入系统，创建了会话之后，才会出现 ShoppingCart 实例。
     *
     * 另外，系统中将会有多个 ShoppingCart 实例：每个用户一个。这里并不想让 Spring 注入某个固定的 ShoppingCart
     * 实例到 StoreService 中。这里希望的是当 StoreService 处理购物车功能时，它所使用的 ShoppingCart 实例恰好
     * 是当前会话所对应的那一个。
     *
     * Spring 并不会将实际的 ShoppingCart bean 注入到 StoreService 中，Spring 会注入一个到 ShoppingCart bean
     * 的代理。这个代理会暴露与 ShoppingCart 相同的方法，所以 StoreService 会认为它就是一个购物车。但是，当
     * StoreService 调用 ShoppingCart 的方法时，代理会对其进行懒解析并将调用委托给会话作用域内真正的 ShoppingCart
     * bean。
     *
     * 现在，带着对这个作用域的理解，讨论一下 proxyMode 属性。如配置所示，proxyMode 属性被设置成了
     * ScopedProxyMode.INTERFACES，这表明这个代理要实现 ShoppingCart 接口，并将调用委托给实现 bean。
     *
     * 如果 ShoppingCart 是接口而不是类的话，这是可以的（也是最为理想的代理模式）。但如果 ShoppingCart 是一个
     * 具体的类的话，Spring 就没有办法创建基于接口的代理了。此时，它必须使用 CGLib 来生成基于类的代理。所以，如
     * 果 bean 类型是具体类的话，必须要将 proxyMode 属性设置为 ScopedProxyMode.TARGET_CLASS，以此来表明要
     * 以生成目标类扩展的方式创建代理。
     *
     * 尽管这里主要关注了会话作用域，但是请求作用域的 bean 会面临相同的装配问题。因此，请求作用域的 bean 应该也
     * 以作用域代理的方式进行注入。
     *
     * PS：作用域代理能够延迟注入请求和会话作用域的 bean。
     *
     *
     *
     * 2、在 XML 中声明作用域代理
     *
     * 如果你需要使用 XML 来声明会话或请求作用域的 bean，那么就不能使用 @Scope 注解及其 proxyMode 属性了。
     * <bean> 元素的 scope 属性能够设置 bean 的作用域，但是该怎样指定代理模式呢？
     *
     * 要设置代理模式，需要使用 Spring aop 命名空间的一个新元素：
     *
     *     <bean id="cart"
     *           class="com.siwuxie095.spring.chapter3rd.example7th.ShoppingCart"
     *           scope="session">
     *         <aop:scoped-proxy />
     *     </bean>
     *
     * <aop:scoped-proxy> 是与 @Scope 注解的 proxyMode 属性功能相同的 Spring XML 配置元素。它会告诉 Spring
     * 为 bean 创建一个作用域代理。默认情况下，它会使用 CGLib 创建目标类的代理。但是也可以将 proxy-target-class
     * 属性设置为 false，进而要求它生成基于接口的代理：
     *
     *     <bean id="cart"
     *           class="com.siwuxie095.spring.chapter3rd.example7th.ShoppingCart"
     *           scope="session">
     *         <aop:scoped-proxy proxy-target-class="false" />
     *     </bean>
     *
     * 为了使用 <aop:scoped-proxy> 元素，必须在 XML 配置中声明 Spring 的 aop 命名空间：
     *
     * xmlns:aop="http://www.springframework.org/schema/aop"
     */
    public static void main(String[] args) {

    }

}
