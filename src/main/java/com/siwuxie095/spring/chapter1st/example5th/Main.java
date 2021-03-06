package com.siwuxie095.spring.chapter1st.example5th;

/**
 * @author Jiajing Li
 * @date 2020-12-14 21:37:49
 */
public class Main {

    /**
     * 应用切面
     *
     * DI 能够让相互协作的软件组件保持松散耦合，而面向切面编程（aspect-oriented programming，AOP）允许你把
     * 遍布应用各处的功能分离出来形成可重用的组件。
     *
     * 面向切面编程往往被定义为促使软件系统实现关注点分离的一项技术。系统由许多不同的组件组成，每一个组件各负责
     * 一块特定功能。除了实现自身核心的功能之外，这些组件还经常承担着额外的职责。诸如日志、事务管理和安全这样的
     * 系统服务经常融入到自身具有核心业务逻辑的组件中去，这些系统服务通常被称为横切关注点，因为它们会跨越系统的
     * 多个组件。
     *
     * 如果将这些关注点分散到多个组件中去，你的代码将会带来双重的复杂性。
     * （1）实现系统关注点功能的代码将会重复出现在多个组件中。这意味着如果你要改变这些关注点的逻辑，必须修改各
     * 个模块中的相关实现。即使你把这些关注点抽象为一个独立的模块，其他模块只是调用它的方法，但方法的调用还是会
     * 重复出现在各个模块中。
     * （2）组件会因为那些与自身核心业务无关的代码而变得混乱。一个向地址簿增加地址条目的方法应该只关注如何添加
     * 地址，而不应该关注它是不是安全的或者是否需要支持事务。
     *
     * 假设学校的系统有这样几个业务服务：学生服务、讲师服务、内容服务、课程服务、计费服务，还有这样几个系统服务：
     * 日志模块、安全模块、事务管理。
     *
     * 每个业务服务都要去调用系统服务，就有了 M * N 个调用。业务对象与系统级服务结合得过于紧密。每个对象不但要
     * 知道它需要记日志、进行安全控制和参与事务，还要亲自执行这些服务。
     *
     * 在整个系统内，关注点（例如日志和安全）的调用经常散布到各个模块中，而这些关注点并不是模块的核心业务。
     *
     * AOP 能够使这些服务（关注点）模块化，并以声明的方式将它们应用到它们需要影响的组件中去。所造成的结果就是这
     * 些组件会具有更高的内聚性并且会更加关注自身的业务，完全不需要了解涉及系统服务所带来复杂性。总之，AOP 能够
     * 确保 POJO 的简单性。
     *
     * 可以把切面想象为覆盖在很多组件之上的一个外壳。应用是由那些实现各自业务功能的模块组成的。借助 AOP，可以使
     * 用各种功能层去包裹核心业务层。这些层以声明的方式灵活地应用到系统中，你的核心应用甚至根本不知道它们的存在。
     * 这是一个非常强大的理念，可以将安全、事务和日志关注点与核心业务逻辑相分离。
     *
     * 利用 AOP，系统范围内的关注点将覆盖在它们所影响组件之上。
     *
     *
     *
     * AOP 应用
     *
     * 为了示范在 Spring 中如何应用切面，需要重新回到 example4th 中骑士的例子，并为它添加一个切面。
     *
     * 每一个人都熟知骑士所做的任何事情，这是因为吟游诗人用诗歌记载了骑士的事迹并将其进行传唱。假设需要使用吟游
     * 诗人这个服务类来记载骑士的所有事迹。
     *
     * 以 Minstrel 为例，即为吟游诗人，是一个只有两个方法的简单类。
     *
     * 在骑士执行每一个探险任务之前，singBeforeQuest() 方法会被调用；在骑士完成探险任务之后，singAfterQuest()
     * 方法会被调用。在这两种情况下，Minstrel 都会通过一个 PrintStream 类来歌颂骑士的事迹，这个类是通过构造
     * 器注入进来的。
     *
     * 把 Minstrel 加入你的代码中并使其运行起来，这对你来说是小事一桩。适当做一下调整从而让 BraveKnight 可
     * 以使用 Minstrel。
     *
     * 以 BraveKnightWithMinstrel 为例，展示了将 BraveKnight 和 Minstrel 组合起来的第一次尝试。
     *
     * 这应该可以达到预期效果。现在，你所需要做的就是回到 Spring 配置中，声明 Minstrel bean 并将其注入到
     * BraveKnightWithMinstrel 的构造器之中。但是，请稍等 ...
     *
     * 似乎感觉有些东西不太对。管理他的吟游诗人真的是骑士职责范围内的工作吗？因为吟游诗人应该做他份内的事，根本
     * 不需要骑士命令他这么做。毕竟，用诗歌记载骑士的探险事迹，这是吟游诗人的职责。为什么骑士还需要提醒吟游诗人
     * 去做他份内的事情呢？
     *
     * 此外，因为骑士需要知道吟游诗人，所以就必须把吟游诗人注入到 BraveKnightWithMinstrel 类中。这不仅使
     * BraveKnightWithMinstrel 的代码复杂化了，而且还让人疑惑是否还需要一个不需要吟游诗人的骑士呢？如果
     * Minstrel 为 null 会发生什么呢？是否应该引入一个空值校验逻辑来覆盖该场景？
     *
     * 这样，简单的 BraveKnight 类开始变得复杂，如果你还需要应对没有吟游诗人时的场景，那代码会变得更复杂。但
     * 利用 AOP，你可以声明吟游诗人必须歌颂骑士的探险事迹，而骑士本身并不用直接访问 Minstrel 的方法。
     *
     * 要将 Minstrel 抽象为一个切面，你所需要做的事情就是在一个 Spring 配置文件中声明它。
     *
     * 以 minstrel.xml 为例，这里使用了 Spring 的 aop 配置命名空间把 Minstrel bean 声明为一个切面。首先，
     * 需要把 Minstrel 声明为一个 bean，然后在 <aop:aspect> 元素中引用该 bean。为了进一步定义切面，声明
     * （使用 <aop:before>）在 embarkOnQuest() 方法执行前调用 Minstrel 的 singBeforeQuest() 方法。这
     * 种方式被称为前置通知（before advice）。同时声明（使用 <aop:after>）在 embarkOnQuest() 方法执行后
     * 调用 singAfterQuest() 方法。这种方式被称为后置通知（after advice）。
     *
     * 在这两种方式中，pointcut-ref 属性都引用了名字为 embank 的切入点。该切入点是在前边的 <pointcut> 元
     * 素中定义的，并配置 expression 属性来选择所应用的通知。表达式的语法采用的是 AspectJ 的切点表达式语言。
     *
     * 这就是需要做的所有的事情！通过少量的 XML 配置，就可以把 Minstrel 声明为一个 Spring 切面。
     *
     * 现在，Spring 在骑士执行探险任务前后会调用 Minstrel 的 singBeforeQuest() 和 singAfterQuest() 方
     * 法。从这个示例中获得两个重要的观点：
     * （1）首先，Minstrel 仍然是一个 POJO，没有任何代码表明它要被作为一个切面使用。当按照上面那样进行配置后，
     * 在 Spring 的上下文中，Minstrel 实际上已经变成一个切面了。
     * （2）其次，也是最重要的，Minstrel 可以被应用到 BraveKnight 中，而 BraveKnight 不需要显式地调用它。
     * 实际上，BraveKnight 完全不知道 Minstrel 的存在。
     *
     * 必须还要指出的是，尽管使用 Spring 魔法把 Minstrel 转变为一个切面，但首先要把它声明为一个 Spring bean。
     * 能够为其他 Spring bean 做到的事情都可以同样应用到 Spring 切面中，例如为它们注入依赖。
     *
     * 应用切面来歌颂骑士可能只是有点好玩而已，但是 Spring AOP 可以做很多有实际意义的事情。
     */
    public static void main(String[] args) {

    }

}
