package com.siwuxie095.spring.chapter4th.example7th;

/**
 * @author Jiajing Li
 * @date 2021-01-17 17:16:27
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 在 XML 中声明切面
     *
     * 关于 Spring 的配置有这样一种原则，那就是基于注解的配置 要优于基于 Java 的配置，基于 Java 的配置要优于基于 XML 的配置。
     * 但是，如果你需要声明切面，但是又不能为通知类添加注解的时候，那么就必须转向 XML 配置了。
     *
     * 在 Spring 的 aop 命名空间中，提供了多个元素用来在 XML 中声明切面，如下：
     * （1）<aop:advisor>：定义 AOP 通知器；
     * （2）<aop:after>：定义 AOP 后置通知（不管被通知的方法是否执行成功）；
     * （3）<aop:after-returning>：定义 AOP 返回通知；
     * （4）<aop:after-throwing>：定义 AOP 异常通知；
     * （5）<aop:around>：定义 AOP 环绕通知；
     * （6）<aop:aspect>：定义一个切面；
     * （7）<aop:aspectj-autoproxy>：启用 @AspectJ 注解驱动的切面；
     * （8）<aop:before>：定义一个 AOP 前置通知；
     * （9）<aop:config>：顶层的 AOP 配置元素。大多数的 <aop:*> 元素必须包含在 <aop:config> 元素内；
     * （10）<aop:declare-parents>：以透明的方式为被通知的对象引入额外的接口；
     * （11）<aop:pointcut>：定义一个切点。
     *
     * PS：Spring 的 AOP 配置元素能够以非侵入性的方式声明切面。
     *
     * <aop:aspectj-autoproxy> 元素，它能够自动代理 AspectJ 注解的通知类。aop 命名空间的其他元素能够直接在 Spring 配置中
     * 声明切面，而不需要使用注解。
     *
     * 例如，重新看一下 Audience 类，这一次将它所有的 AspectJ 注解全部移除掉：
     *
     * public class Audience {
     *
     *
     *     public void silenceCellPhones() {
     *         System.out.println("Silencing cell phones");
     *     }
     *
     *     public void takeSeats() {
     *         System.out.println("Taking seats");
     *     }
     *
     *     public void applause() {
     *         System.out.println("CLAP CLAP CLAP!!!");
     *     }
     *
     *     public void demandRefund() {
     *         System.out.println("Demanding a refund");
     *     }
     *
     * }
     *
     * 正如你所看到的，Audience 类并没有任何特别之处，它就是有几个方法的简单 Java 类。这里可以像其他类一样把它注册为 Spring
     * 应用上下文中的 bean。
     *
     * 尽管看起来并没有什么差别，但 Audience 已经具备了成为 AOP 通知的所有条件。再稍微帮助它一把，它就能够成为预期的通知了。
     *
     *
     *
     * 1、声明前置和后置通知
     *
     * 这里会使用 Spring aop 命名空间中的一些元素，将没有注解的 Audience 类转换为切面。如下展示了所需要的 XML：
     *
     *     <aop:config>
     *         <aop:aspect ref="audience">
     *             <aop:before
     *                     pointcut="execution(* com.siwuxie095.spring.chapter4th.example7th.Performance.perform(..))"
     *                     method="silenceCellPhones" />
     *             <aop:before
     *                     pointcut="execution(* com.siwuxie095.spring.chapter4th.example7th.Performance.perform(..))"
     *                     method="takeSeats" />
     *             <aop:after-returning
     *                     pointcut="execution(* com.siwuxie095.spring.chapter4th.example7th.Performance.perform(..))"
     *                     method="applause" />
     *             <aop:after-throwing
     *                     pointcut="execution(* com.siwuxie095.spring.chapter4th.example7th.Performance.perform(..))"
     *                     method="demandRefund" />
     *         </aop:aspect>
     *     </aop:config>
     *
     * 关于 Spring AOP 配置元素，第一个需要注意的事项是大多数的 AOP 配置元素必须在 <aop:config> 元素的上下文内使用。这条规
     * 则有几种例外场景，但是把 bean 声明为一个切面时，总是从 <aop:config> 元素开始配置的。
     *
     * 在 <aop:config> 元素内，可以声明一个或多个通知器、切面或者切点。这里使用 <aop:aspect> 元素声明了一个简单的切面。ref
     * 元素引用了一个 POJO bean，该 bean 实现了切面的功能 —— 在这里就是 audience。ref 元素所引用的 bean 提供了在切面中通
     * 知所调用的方法。
     *
     * 该切面应用了四个不同的通知。两个 <aop:before> 元素定义了匹配切点的方法执行之前调用前置通知方法，也就是 Audience bean
     * 的 takeSeats() 和 silenceCellPhones() 方法（由 method 属性所声明）。<aop:after-returning> 元素定义了一个返回
     * （after-returning）通知，在切点所匹配的方法调用之后再调用 applaud() 方法。同样，<aop:after-throwing> 元素定义了
     * 异常（after-throwing）通知，如果所匹配的方法执行时抛出任何的异常，都将会调用 demandRefund() 方法。
     *
     * 在所有的通知元素中，pointcut 属性定义了通知所应用的切点，它的值是使用 AspectJ 切点表达式语法所定义的切点。
     *
     * 你或许注意到所有通知元素中的 pointcut 属性的值都是一样的，这是因为所有的通知都要应用到相同的切点上。
     *
     * 在基于 AspectJ 注解的通知中，当发现这种类型的重复时，可以使用 @Pointcut 注解消除这些重复的内容。而在基于 XML 的切面
     * 声明中，需要使用 <aop:pointcut> 元素。如下的 XML 展现了如何将通用的切点表达式抽取到一个切点声明中，这样这个声明就能
     * 在所有的通知元素中使用了。
     *
     *     <aop:config>
     *         <aop:aspect ref="audience">
     *             <aop:pointcut
     *                     id="performance"
     *                     expression="execution(* com.siwuxie095.spring.chapter4th.example7th.Performance.perform(..))" />
     *             <aop:before
     *                     pointcut-ref="performance"
     *                     method="silenceCellPhones" />
     *             <aop:before
     *                     pointcut-ref="performance"
     *                     method="takeSeats" />
     *             <aop:after-returning
     *                     pointcut-ref="performance"
     *                     method="applause" />
     *             <aop:after-throwing
     *                     pointcut-ref="performance"
     *                     method="demandRefund" />
     *         </aop:aspect>
     *     </aop:config>
     *
     * 现在切点是在一个地方定义的，并且被多个通知元素所引用。<aop:pointcut> 元素定义了一个 id 为 performance 的切点。同时
     * 修改所有的通知元素，用 pointcut-ref 属性来引用这个命名切点。
     *
     * <aop:pointcut> 元素所定义的切点可以被同一个 <aop:aspect> 元素之内的所有通知元素引用。如果想让定义的切点能够在多个切
     * 面使用，可以把 <aop:pointcut> 元素放在 <aop:config> 元素的范围内。
     *
     *
     *
     * 2、声明环绕通知
     *
     * 目前 Audience 的实现工作得非常棒，但是前置通知和后置通知有一些限制。具体来说，如果不使用成员变量存储信息的话，在前置通
     * 知和后置通知之间共享信息非常麻烦。
     *
     * 例如，假设除了进场关闭手机和表演结束后鼓掌，还希望观众确保一直关注演出，并报告每个参赛者表演了多长时间。使用前置通知和后
     * 置通知实现该功能的唯一方式是在前置通知中记录开始时间并在某个后置通知中报告表演耗费的时间。但这样的话必须在一个成员变量中
     * 保存开始时间。因为 Audience 是单例的，如果像这样保存状态的话，将会存在线程安全问题。
     *
     * 相对于前置通知和后置通知，环绕通知在这点上有明显的优势。使用环绕通知，可以完成前置通知和后置通知所实现的相同功能，而且只
     * 需要在一个方法中实现。因为整个通知逻辑是在一个方法内实现的，所以不需要使用成员变量保存状态。
     *
     * 例如，AroundAudience 类的 watchPerformance() 方法，它没有使用任何的注解。
     *
     * public class AroundAudience {
     *
     *     public void watchPerformance(ProceedingJoinPoint pjp) {
     *         try {
     *             System.out.println("Silencing cell phones");
     *             System.out.println("Taking seats");
     *             pjp.proceed();
     *             System.out.println("CLAP CLAP CLAP!!!");
     *         } catch (Throwable e) {
     *             System.out.println("Demanding a refund");
     *         }
     *     }
     *
     * }
     *
     * 在观众切面中，watchPerformance() 方法包含了之前四个通知方法的所有功能。不过，所有的功能都放在了这一个方法中，因此这个
     * 方法还要负责自身的异常处理。
     *
     * 声明环绕通知与声明其他类型的通知并没有太大区别。这里所需要做的仅仅是使用 <aop:around> 元素。
     *
     *     <aop:config>
     *         <aop:aspect ref="aroundAudience">
     *             <aop:pointcut
     *                     id="performance"
     *                     expression="execution(* com.siwuxie095.spring.chapter4th.example7th.Performance.perform(..))" />
     *             <aop:around
     *                     pointcut-ref="performance"
     *                     method="watchPerformance" />
     *         </aop:aspect>
     *     </aop:config>
     *
     * 像其他通知的 XML 元素一样，<aop:around> 指定了一个切点和一个通知方法的名字。在这里，使用跟之前一样的切点，但是为该切点
     * 所设置的 method 属性值为 watchPerformance() 方法。
     *
     *
     *
     * 3、为通知传递参数
     *
     * 这里使用 XML 来配置切面，这个切面能够记录 CompactDisc 上每个磁道播放的次数。
     *
     * 首先，要移除掉 TrackCounter 上所有的 @AspectJ 注解。
     *
     * public class TrackCounter {
     *
     *     private Map<Integer, Integer> trackCounts = new HashMap<>();
     *
     *     public void countTrack(int trackNumber) {
     *         int currentCount = getPlayCount(trackNumber);
     *         trackCounts.put(trackNumber, currentCount + 1);
     *     }
     *
     *     public int getPlayCount(int trackNumber) {
     *         return trackCounts.containsKey(trackNumber) ? trackCounts.get(trackNumber) : 0;
     *     }
     *
     * }
     *
     * 去掉 @AspectJ 注解后，TrackCounter 显得有些单薄了。现在，除非显式调用 countTrack() 方法，否则 TrackCounter 不会
     * 记录磁道播放的数量。但是，借助一点 Spring XML 配置，能够让 TrackCounter 重新变为切面。
     *
     *     <aop:config>
     *         <aop:aspect ref="trackCounter">
     *             <aop:pointcut
     *                     id="trackPlayed"
     *                     expression="execution(* com.siwuxie095.spring.chapter4th.example6th.CompactDisc.playTrack(int)) and args(trackNumber)" />
     *             <aop:before
     *                     pointcut-ref="trackPlayed"
     *                     method="countTrack" />
     *         </aop:aspect>
     *     </aop:config>
     *
     * 可以看到，这里使用了和前面相同的 aop 命名空间 XML 元素，它们会将 POJO 声明为切面。唯一明显的差别在于切点表达式中包含了
     * 一个参数，这个参数会传递到通知方法中。值得注意的是，这里使用 and 关键字而不是 "&&"（因为在 XML 中，"&" 符号会被解析为
     * 实体的开始）。
     *
     *
     *
     * 4、通过切面引入新的功能
     *
     * 借助 AspectJ 的 @DeclareParents 注解可以为被通知的方法神奇地引入新的方法。但是 AOP 引入并不是 AspectJ 特有的。使用
     * Spring aop 命名空间中的 <aop:declare-parents> 元素，可以实现相同的功能。
     *
     * 如下的 XML 代码片段与基于注解的引入功能是相同：
     *
     *     <aop:config>
     *         <aop:aspect ref="audience">
     *             <aop:declare-parents
     *                     types-matching="com.siwuxie095.spring.chapter4th.example7th.Performance+"
     *                     implement-interface="com.siwuxie095.spring.chapter4th.example7th.Encoreable"
     *                     default-impl="com.siwuxie095.spring.chapter4th.example7th.DefaultEncoreable"
     *             />
     *         </aop:aspect>
     *     </aop:config>
     *
     * 顾名思义，<aop:declare-parents> 声明了此切面所通知的 bean 要在它的对象层次结构中拥有新的父类型。具体到本例中，类型匹
     * 配 Performance 接口（由 types-matching 属性指定）的那些 bean 在父类结构中会增加 Encoreable 接口（由 implement-
     * interface 属性指定）。最后要解决的问题是 Encoreable 接口中的方法实现要来自于何处。
     *
     * 这里有两种方式标识所引入接口的实现。在本例中，使用 default-impl 属性用全限定类名来显式指定 Encoreable 的实现。或者，
     * 还可以使用 delegate-ref 属性来标识。
     *
     *     <aop:config>
     *         <aop:aspect ref="audience">
     *             <aop:declare-parents
     *                     types-matching="com.siwuxie095.spring.chapter4th.example7th.Performance+"
     *                     implement-interface="com.siwuxie095.spring.chapter4th.example7th.Encoreable"
     *                     delegate-ref="encoreableDelegate"
     *             />
     *         </aop:aspect>
     *     </aop:config>
     *
     * delegate-ref 属性引用了一个 Spring bean 作为引入的委托。这需要在 Spring 上下文中存在一个 ID 为 encoreableDelegate
     * 的 bean。
     *
     *     <bean id="encoreableDelegate"
     *           class="com.siwuxie095.spring.chapter4th.example7th.DefaultEncoreable" />
     *
     * 使用 default-impl 来直接标识委托和间接使用 delegate-ref 的区别在于后者是 Spring bean，它本身可以被注入、通知或使用
     * 其他的 Spring 配置。
     */
    public static void main(String[] args) {

    }

}
