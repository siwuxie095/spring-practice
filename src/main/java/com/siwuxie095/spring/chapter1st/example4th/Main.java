package com.siwuxie095.spring.chapter1st.example4th;

/**
 * @author Jiajing Li
 * @date 2020-12-12 11:20:00
 */
public class Main {

    /**
     * 依赖注入
     *
     * 依赖注入这个词让人望而生畏，现在已经演变成一项复杂的编程技巧或设计模式理念。但事实证明，依赖注入并不像它听上去
     * 那么复杂。在项目中应用 DI，你会发现你的代码会变得异常简单并且更容易理解和测试。
     *
     *
     *
     * DI功能是如何实现的
     *
     * 任何一个有实际意义的应用（肯定比 Hello World 示例更复杂）都会由两个或者更多的类组成，这些类相互之间进行协作
     * 来完成特定的业务逻辑。按照传统的做法，每个对象负责管理与自己相互协作的对象（即 它所依赖的对象）的引用，这将会
     * 导致高度耦合和难以测试的代码。
     *
     * 以 DamselRescuingKnight 为例，DamselRescuingKnight 在它的构造函数中自行创建了 RescueDamselQuest。这
     * 使得 DamselRescuingKnight 紧密地和 RescueDamselQuest 耦合到了一起，因此极大地限制了这个骑士执行探险的
     * 能力。如果一个少女需要救援，这个骑士能够召之即来。但是如果一条恶龙需要杀掉，或者一个圆桌 ...... 额 ......
     * 需要滚起来，那么这个骑士就爱莫能助了。
     *
     * 更糟糕的是，为这个 DamselRescuingKnight 编写单元测试将出奇地困难。在这样的一个测试中，你必须保证当骑士的
     * embarkOnQuest() 方法被调用的时候，探险的 embark() 方法也要被调用。但是没有一个简单明了的方式能够实现这一点。
     * 很遗憾，DamselRescuingKnight 将无法进行测试。
     *
     * 耦合具有两面性（two-headed beast）。一方面，紧密耦合的代码难以测试、难以复用、难以理解，并且典型地表现出
     * "打地鼠" 式的 bug 特性（修复一个 bug，将会出现一个或者更多新的 bug）。另一方面，一定程度的耦合又是必须的，
     * 完全没有耦合的代码什么也做不了。为了完成有实际意义的功能，不同的类必须以适当的方式进行交互。总而言之，耦合
     * 是必须的，但应当被小心谨慎地管理。
     *
     * 通过 DI，对象的依赖关系将由系统中负责协调各对象的第三方组件在创建对象的时候进行设定。对象无需自行创建或管理
     * 它们的依赖关系，依赖关系将被自动注入到需要它们的对象当中去。
     *
     * 依赖注入会将所依赖的关系自动交给目标对象，而不是让对象自己去获取依赖。
     *
     * 以 BraveKnight 为例，这个骑士不仅勇敢，而且能挑战任何形式的探险。
     *
     * 可以看到，不同于之前的 DamselRescuingKnight，BraveKnight 没有自行创建探险任务，而是在构造的时候把探险
     * 任务作为构造器参数传入。这是依赖注入的方式之一，即构造器注入（constructor injection）。
     *
     * 更重要的是，传入的探险类型是 Quest，也就是所有探险任务都必须实现的一个接口。所以，BraveKnight 能够响应
     * RescueDamselQuest、SlayDragonQuest 等任意的 Quest 实现。
     *
     * 这里的要点是 BraveKnight 没有与任何特定的 Quest 实现发生耦合。对它来说，被要求挑战的探险任务只要实现了
     * Quest 接口，那么具体是哪种类型的探险就无关紧要了。这就是 DI 所带来的最大收益 —— 松耦合。如果一个对象只
     * 通过接口（而不是具体实现或初始化过程）来表明依赖关系，那么这种依赖就能够在对象本身毫不知情的情况下，用不同
     * 的具体实现进行替换。
     *
     * 对依赖进行替换的一个最常用方法就是在测试的时候使用 mock 实现。这里无法充分地测试 DamselRescuingKnight，
     * 因为它是紧耦合的，但是可以轻松地测试 BraveKnight，只需给它一个 Quest 的 mock 实现即可。
     *
     * 以 BraveKnightTest 为例，你可以使用 mock 框架 Mockito 去创建一个 Quest 接口的 mock 实现。通过这个
     * mock 对象，就可以创建一个新的 BraveKnight 实例，并通过构造器注入这个 mock Quest。当调用 embarkOnQuest()
     * 方法时，你可以要求 Mockito 框架验证 Quest 的 mock 实现的 embark() 方法仅仅被调用了一次。
     *
     *
     *
     * 将 Quest 注入到 Knight 中
     *
     * 现在 BraveKnight 类可以接受你传递给它的任意一种 Quest 的实现，但该怎样把特定的 Quest 实现传给它呢？
     * 假设，希望 BraveKnight 所要进行探险任务是杀死一只恶龙，那么 SlayDragonQuest 也许是挺合适的。
     *
     * 可以看到，SlayDragonQuest 实现了 Quest 接口，这样它就适合注入到 BraveKnight 中去了。与其他的 Java
     * 入门样例有所不同，SlayDragonQuest 没有使用 System.out.println()，而是在构造方法中请求一个更为通用的
     * PrintStream。这里最大的问题在于，该如何将 SlayDragonQuest 交给 BraveKnight 呢？又如何将 PrintStream
     * 交给 SlayDragonQuest 呢？
     *
     * 创建应用组件之间协作的行为通常称为装配（wiring）。Spring 有多种装配 bean 的方式，采用 XML 是很常见的
     * 一种装配方式。
     *
     * 以 knights.xml 为例，展现了一个简单的 Spring 配置文件，该配置文件将 BraveKnight、SlayDragonQuest
     * 和 PrintStream 装配到了一起。
     *
     * 在这里，BraveKnight 和 SlayDragonQuest 被声明为 Spring 中的 bean。就 BraveKnight bean 来讲，它在
     * 构造时传入了对 SlayDragonQuest bean 的引用，将其作为构造器参数。同时，SlayDragonQuest bean 的声明使
     * 用了 Spring 表达式语言（Spring Expression Language），将 System.out（这是一个 PrintStream）传入到
     * 了 SlayDragonQuest 的构造器中。
     *
     * 如果 XML 配置不符合你的喜好的话，Spring 还支持使用 Java 来描述配置。
     *
     * 以 KnightConfig 为例，展现了基于 Java 的配置，它的功能与 knights.xml 相同。
     *
     * 不管你使用的是基于 XML 的配置还是基于 Java 的配置，DI 所带来的收益都是相同的。尽管 BraveKnight 依赖于 Quest，
     * 但是它并不知道传递给它的是什么类型的 Quest，也不知道这个 Quest 来自哪里。与之类似，SlayDragonQuest 依赖于
     * PrintStream，但是在编码时它并不需要知道这个 PrintStream 是什么样子的。只有 Spring 通过它的配置，能够了解这些
     * 组成部分是如何装配起来的。这样的话，就可以在不改变所依赖的类的情况下，修改依赖关系。
     *
     * 这个样例展现了在 Spring 中装配 bean 的一种简单方法。谨记现在不要过多关注细节。后续会深入讲解 Spring 的配置文件，
     * 同时还会了解 Spring 装配 bean 的其他方式，甚至包括一种让 Spring 自动发现 bean 并在这些 bean 之间建立关联关系
     * 的方式。
     *
     * 现在已经声明了 BraveKnight 和 Quest 的关系，接下来只需要装载 XML 配置文件，并把应用启动起来。
     *
     *
     *
     * 观察它如何工作
     *
     * Spring 通过应用上下文（Application Context）装载 bean 的定义并把它们组装起来。Spring 应用上下文全权负责对象
     * 的创建和组装。Spring 自带了多种应用上下文的实现，它们之间主要的区别仅仅在于如何加载配置。
     *
     * 因为 knights.xml 中的 bean 是使用 XML 文件进行配置的，所以选择 ClassPathXmlApplicationContext 作为应用上
     * 下文相对是比较合适的。该类加载位于应用程序类路径下的一个或多个 XML 配置文件。
     *
     * 以 KnightXmlMain 为例，main() 方法调用 ClassPathXmlApplicationContext 加载 knights.xml，并获得 Knight
     * 对象的引用。
     *
     * 这里的 main() 方法基于 knights.xml 文件创建了 Spring 应用上下文。随后它调用该应用上下文获取一个 ID 为 knight
     * 的 bean。得到 Knight 对象的引用后，只需简单调用 embarkOnQuest() 方法就可以执行所赋予的探险任务了。注意这个类
     * 完全不知道这里的英雄骑士接受哪种探险任务，而且完全没有意识到这是由 BraveKnight 来执行的。只有 knights.xml 文件
     * 知道哪个骑士执行哪种探险任务。
     *
     * 而对于基于 Java 的配置，Spring 提供了 AnnotationConfigApplicationContext，具体可参见 KnightJavaMain 类。
     *
     * 后续将关注 Spring 简化 Java 开发的下一个理念：基于切面进行声明式编程。
     */
    public static void main(String[] args) {

    }

}
