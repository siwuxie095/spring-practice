package com.siwuxie095.spring.chapter4th.example5th;

/**
 * @author Jiajing Li
 * @date 2021-01-15 08:23:33
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 通过切点来选择连接点
     *
     * 切点用于准确定位应该在什么地方应用切面的通知。通知和切点是切面的最基本元素。因此，了解如何编写切点非常重要。
     *
     * 在 Spring AOP 中，要使用 AspectJ 的切点表达式语言来定义切点。如果你已经很熟悉 AspectJ，那么在 Spring
     * 中定义切点就感觉非常自然。但是如果你一点都不了解 AspectJ 的话，这里将快速介绍一下如何编写 AspectJ 风格的
     * 切点。
     *
     * PS：如果你想进一步了解 AspectJ 和 AspectJ 切点表达式语言，推荐《AspectJ in Action》第二版。
     *
     * 关于 Spring AOP 的 AspectJ 切点，最重要的一点就是 Spring 仅支持 AspectJ 切点指示器的一个子集。因为
     * Spring AOP 是基于代理的，而某些切点表达式是与基于代理的 AOP 无关的。
     *
     * PS：切点指示器 即 pointcut designator。
     *
     * 如下列出了 Spring AOP 所支持的 AspectJ 切点指示器：
     * （1）arg()：限制连接点匹配参数为指定类型的执行方法；
     * （2）@args()：限制连接点匹配参数由指定注解标注的执行方法；
     * （3）execution()：用于匹配是连接点的执行方法；
     * （4）this()：限制连接点匹配AOP代理的bean引用为指定类型的类；
     * （5）target：限制连接点匹配目标对象为指定类型的类；
     * （6）@target()：限制连接点匹配特定的执行对象，这些对象对应的类要具有指定类型的注解；
     * （7）within()：限制连接点匹配指定的类型；
     * （8）@within()：限制连接点匹配指定注解所标注的类型（当使用 Spring AOP 时，方法定义在由指定的注解所标注的
     * 类里）；
     * （9）@annotation：限定匹配带有指定注解的连接点。
     *
     * PS：Spring 借助 AspectJ 的切点表达式语言来定义 Spring 切面。
     *
     * 在 Spring 中尝试使用 AspectJ 其他指示器时，将会抛出 IllegalArgumentException 异常。
     *
     * 当查看如上所展示的这些 Spring 支持的指示器时，注意只有 execution 指示器是实际执行匹配的，而其他的指示器都
     * 是用来限制匹配的。这说明 execution 指示器是在编写切点定义时最主要使用的指示器。在此基础上，使用其他指示器来
     * 限制所匹配的切点。
     *
     *
     *
     * 1、编写切点
     *
     * 为了阐述 Spring 中的切面，需要有个主题来定义切面的切点。为此，定义一个 Performance 接口：
     *
     * public interface Performance {
     *
     *     void perform();
     *
     * }
     *
     * Performance 可以代表任何类型的现场表演，如舞台剧、电影或音乐会。假设想要编写 Performance 的 perform()
     * 方法触发的通知。如下展现了一个切点表达式，这个表达式能够设置当 perform() 方法执行时触发通知的调用。
     *
     * execution(* com.siwuxie095.spring.chapter4th.example5th.Performance.perform(..))
     *
     * PS：使用 AspectJ 切点表达式来选择 Performance 的 perform() 方法。
     *
     * 其中：
     * （1）execution 表示在方法执行时触发。
     * （2）* 表示返回任意类型。
     * （3）com.siwuxie095.spring.chapter4th.example5th.Performance 表示方法所属类。
     * （4）perform 表示方法。
     * （5）.. 表示使用任意参数。
     * （6）* com.siwuxie095.spring.chapter4th.example5th.Performance.perform(..) 整体表示指定方法。
     *
     * 这里使用 execution() 指示器选择 Performance 的 perform() 方法。方法表达式以 "*" 号开始，表明了不关心
     * 方法返回值的类型。然后，指定了全限定类名和方法名。对于方法参数列表，使用两个点号 ".." 表明切点要选择任意的
     * perform() 方法，无论该方法的入参是什么。
     *
     * 现在假设需要配置的切点仅匹配 chapter4th.example5th 包。在此场景下，可以使用 within() 指示器来限制匹配。
     *
     * execution(* com.siwuxie095.spring.chapter4th.example5th.Performance.perform(..)
     *          && within(com.siwuxie095.spring.chapter4th.example5th.*))
     *
     * PS：使用 within() 指示器限制切点范围。
     *
     * 其中：
     * （1）&& 表示与（and）操作。
     * （2）within(com.siwuxie095.spring.chapter4th.example5th.*) 表示当 chapter4th.example5th 包下的
     * 任意类的方法被调用时。
     *
     * 请注意这里使用了 "&&" 操作符把 execution() 和 within() 指示器连接在一起形成与（and）关系（切点必须匹配
     * 所有的指示器）。类似地，可以使用 "||" 操作符来标识或（or）关系，而使用 "!" 操作符来标识非（not）操作。
     *
     * 因为 "&" 在 XML 中有特殊含义，所以在 Spring 的 XML 配置里面描述切点时，可以使用 and 来代替 "&&"。同样，
     * or 和 not 可以分别用来代替 "||" 和 "!"。
     *
     *
     *
     * 2、在切点中选择 bean
     *
     * 除了上面所列的指示器外，Spring 还引入了一个新的 bean() 指示器，它允许在切点表达式中使用 bean 的 ID 来标识
     * bean。bean() 使用 bean ID 或 bean 名称作为参数来限制切点只匹配特定的 bean。
     *
     * 例如，考虑如下的切点：
     *
     * execution(* com.siwuxie095.spring.chapter4th.example5th.Performance.perform(..)
     *          && bean('woodstock'))
     *
     * 在这里，希望在执行 Performance 的 perform() 方法时应用通知，但限定 bean 的 ID 为 woodstock。
     *
     * 在某些场景下，限定切点为指定的 bean 或许很有意义，同时还可以使用非操作为除了特定 ID 以外的其他 bean 应用通知：
     *
     * execution(* com.siwuxie095.spring.chapter4th.example5th.Performance.perform(..)
     *          && !bean('woodstock'))
     *
     * 在此场景下，切面的通知会被编织到所有 ID 不为 woodstock 的 bean 中。
     */
    public static void main(String[] args) {

    }

}
