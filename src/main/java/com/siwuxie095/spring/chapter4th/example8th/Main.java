package com.siwuxie095.spring.chapter4th.example8th;

/**
 * @author Jiajing Li
 * @date 2021-01-18 21:31:10
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 注入 AspectJ 切面
     *
     * 虽然 Spring AOP 能够满足许多应用的切面需求，但是与 AspectJ 相比，Spring AOP 是一个功能比较弱的 AOP 解决方案。
     * AspectJ 提供了 Spring AOP 所不能支持的许多类型的切点。
     *
     * 例如，当需要在创建对象时应用通知，构造器切点就非常方便。不像某些其他面向对象语言中的构造器，Java 构造器不同于其他
     * 的正常方法。这使得 Spring 基于代理的 AOP 无法把通知应用于对象的创建过程。
     *
     * 对于大部分功能来讲，AspectJ 切面与 Spring 是相互独立的。虽然它们可以织入到任意的 Java 应用中，这也包括了 Spring
     * 应用，但是在应用 AspectJ 切面时几乎不会涉及到 Spring。
     *
     * 但是精心设计且有意义的切面很可能依赖其他类来完成它们的工作。如果在执行通知时，切面依赖于一个或多个类，可以在切面内
     * 部实例化这些协作的对象。但更好的方式是，可以借助 Spring 的依赖注入把 bean 装配进 AspectJ 切面中。
     *
     * 为了演示，为演出 Performance 创建一个新切面。具体来讲，以切面的方式创建一个评论员的角色，他会观看演出并且会在演
     * 出之后提供一些批评意见。下面的 CriticAspect 就是一个这样的切面。
     *
     * public aspect CriticAspect {
     *
     *     public CriticAspect() {}
     *
     *     private CriticismEngine criticismEngine;
     *
     *     public void setCriticismEngine(CriticismEngine criticismEngine) {
     *         this.criticismEngine = criticismEngine;
     *     }
     *
     *     pointcut performance() : execution(* com.siwuxie095.spring.chapter4th.example8th.Performance.perform(..));
     *
     *     after() returning : performance() {
     *         System.out.println(criticismEngine.getCriticism());
     *     }
     *
     * }
     *
     * CriticAspect 的主要职责是在表演结束后为表演发表评论。其中的 performance() 切点匹配 perform() 方法。当它与
     * afterReturning() 通知一起配合使用时，可以让该切面在表演结束时起作用。
     *
     * PS：可能因为版本原因，这里 afterReturning() 不可用，可以使用 after() returning 代替。
     *
     * 这里有趣的地方在于并不是评论员自己发表评论，实际上，CriticAspect 与一个 CriticismEngine 对象相协作，在表演结
     * 束时，调用该对象的 getCriticism() 方法来发表一个苛刻的评论。为了避免 CriticAspect 和 CriticismEngine 之间
     * 产生不必要的耦合，这里通过 Setter 依赖注入为 CriticAspect 设置 CriticismEngine。
     *
     * PS：切面也需要注入。像其他的 bean 一样，Spring 可以为 AspectJ 切面注入依赖。
     *
     * CriticismEngine 自身是声明了一个简单 getCriticism() 方法的接口：
     *
     * public interface CriticismEngine {
     *
     *     String getCriticism();
     *
     * }
     *
     * 如下是 CriticismEngine 的实现：
     *
     * public class CriticismEngineImpl implements CriticismEngine {
     *
     *     private String[] criticismPool;
     *
     *     public void setCriticismPool(String[] criticismPool) {
     *         this.criticismPool = criticismPool;
     *     }
     *
     *     @Override
     *     public String getCriticism() {
     *         int i = (int) (Math.random() * criticismPool.length);
     *         return criticismPool[i];
     *     }
     *
     * }
     *
     * CriticismEngineImpl 实现了 CriticismEngine 接口，通过从注入的评论池中随机选择一个苛刻的评论。这个类可以使用
     * 如下的 XML 声明为一个 Spring bean。
     *
     *     <bean id="criticismEngine"
     *           class="com.siwuxie095.spring.chapter4th.example8th.CriticismEngineImpl">
     *         <property name="criticismPool">
     *             <list>
     *                 <value>Worst performance ever!</value>
     *                 <value>I laughed, I cried, then I realized I was at the wrong show.</value>
     *                 <value>A must see show!</value>
     *             </list>
     *         </property>
     *     </bean>
     *
     * 到目前为止，一切顺利。现在有了一个要赋予 CriticAspect 的 Criticism-Engine 实现。剩下的就是为 CriticAspect
     * 装配 CriticismEngineImple。
     *
     * 在展示如何实现注入之前，必须清楚 AspectJ 切面根本不需要 Spring 就可以织入到应用中。如果想使用 Spring 的依赖注
     * 入为 AspectJ 切面注入协作者，那就需要在 Spring 配置中把切面声明为一个 Spring 配置中的 <bean>。如下的 <bean>
     * 声明会把 criticismEngine bean 注入到 CriticAspect 中：
     *
     *     <bean class="com.siwuxie095.spring.chapter4th.example8th.CriticAspect"
     *           factory-method="aspectOf">
     *         <property name="criticismEngine" ref="criticismEngine" />
     *     </bean>
     *
     * 在很大程度上，这里的 <bean> 的声明与在 Spring 中所看到的其他 <bean> 配置并没有太多的区别，但是最大的不同在于使
     * 用了 factory-method 属性。通常情况下，Spring bean 由 Spring 容器初始化，但是 AspectJ 切面是由 AspectJ 在
     * 运行期创建的。等到 Spring 有机会为 CriticAspect 注入 CriticismEngine 时，CriticAspect 已经被实例化了。
     *
     * 因为 Spring 不能负责创建 CriticAspect，那就不能在 Spring 中简单地把 CriticAspect 声明为一个 bean。相反，需
     * 要一种方式为 Spring 获得已经由 AspectJ 创建的 CriticAspect 实例的句柄，从而可以注入 CriticismEngine。幸好，
     * 所有的 AspectJ 切面都提供了一个静态的 aspectOf() 方法，该方法返回切面的一个单例。所以为了获得切面的实例，必须使
     * 用 factory-method 来调用 asepctOf() 方法而不是调用 CriticAspect 的构造器方法。
     *
     * 简而言之，Spring 不能像之前那样使用 <bean> 声明来创建一个 CriticAspect 实例 —— 它已经在运行时由 AspectJ 创建
     * 完成了。Spring 需要通过 aspectOf() 工厂方法获得切面的引用，然后像 <bean> 元素规定的那样在该对象上执行依赖注入。
     */
    public static void main(String[] args) {

    }

}
