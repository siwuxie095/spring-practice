package com.siwuxie095.spring.chapter2nd.example3rd;

/**
 * @author Jiajing Li
 * @date 2020-12-21 21:47:14
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 自动化装配 bean
     *
     * 对于借助 Java 和 XML 来进行 Spring 装配，虽然这些显式装配技术非常有用，但是在便利性方面，最强大的还是
     * Spring 的自动化配置。如果 Spring 能够进行自动化装配的话，那何苦还要显式地将这些 bean 装配在一起呢？
     *
     * Spring 从两个角度来实现自动化装配：
     * （1）组件扫描（component scanning）：Spring 会自动发现应用上下文中所创建的 bean；
     * （2）自动装配（autowiring）：Spring 自动满足 bean 之间的依赖。
     *
     * 组件扫描和自动装配组合在一起就能发挥出强大的威力，它们能够将你的显式配置降低到最少。
     *
     * 为了阐述组件扫描和装配，需要创建几个 bean，它们代表了一个音响系统中的组件。首先，要创建 CompactDisc 类，
     * Spring 会发现它并将其创建为一个 bean。然后，会创建一个 CDPlayer 类，让 Spring 发现它，并将 CompactDisc
     * bean 注入进来。
     *
     *
     *
     * 1、创建可被发现的 bean
     *
     * 在这个 MP3 和流式媒体音乐的时代，CD（compact disc）显得有点典雅甚至陈旧。它不像卡带机、八轨磁带、塑胶
     * 唱片那么普遍，随着以物理载体进行音乐交付的方式越来越少，CD 也变得越来越稀少了。
     *
     * 尽管如此，CD 为阐述 DI 如何运行提供了一个很好的样例。如果你不将 CD 插入（注入）到 CD 播放器中，那么 CD
     * 播放器其实是没有太大用处的。所以，可以这样说，CD 播放器依赖于 CD 才能完成它的使命。
     *
     * 为了在 Spring 中阐述这个例子，首先在 Java 中建立 CD 的概念。以 CompactDisc 为例，它是定义 CD 的一个
     * 接口。
     *
     * CompactDisc 的具体内容并不重要，重要的是你将其定义为一个接口。作为接口，它定义了 CD 播放器对一盘 CD 所
     * 能进行的操作。它将 CD 播放器的任意实现与 CD 本身的耦合降低到了最小的程度。
     *
     * 还需要一个 CompactDisc 的实现，实际上，可以有 CompactDisc 接口的多个实现。在本例中，首先会创建其中的
     * 一个实现，即 SgtPeppers 类。
     *
     * 和 CompactDisc 接口一样，SgtPeppers 的具体内容并不重要。你唯一需要注意的就是 SgtPeppers 类上使用了
     * @Component 注解。这个简单的注解表明该类会作为组件类，并告知 Spring 要为这个类创建 bean。没有必要显式
     * 配置SgtPeppers bean，因为这个类使用了 @Component 注解，所以 Spring 会为你把事情处理妥当。
     *
     * 不过，组件扫描默认是不启用的。还需要显式配置一下 Spring，从而命令它去寻找带有 @Component 注解的类，并
     * 为其创建 bean。
     *
     * 以 CDPlayerAutoConfig 为例，该配置类展现了完成这项任务的最简洁配置。CDPlayerAutoConfig 类通过 Java
     * 代码定义了 Spring 的装配规则。CDPlayerAutoConfig 类并没有显式地声明任何 bean，只不过它使用了
     * @ComponentScan 注解，这个注解能够在 Spring 中启用组件扫描。
     *
     * 如果没有其他配置的话，@ComponentScan 默认会扫描与配置类相同的包。因此 Spring 将会扫描这个包以及这个包
     * 下的所有子包，查找带有 @Component 注解的类。这样的话，就能发现 CompactDisc，并且会在 Spring 中自动为
     * 其创建一个 bean。
     *
     * 如果你更倾向于使用 XML 来启用组件扫描，就可以使用 Spring context 命名空间的 <context:component-scan>
     * 元素。以 soundsystem.xml 为例，展示了启用组件扫描的最简洁 XML 配置。
     *
     * 尽管可以通过 XML 的方案来启用组件扫描，但是在后面的讨论中，更多的还是会使用基于 Java 的配置。如果你更喜
     * 欢 XML 的话，<context:component-scan> 元素会有与 @ComponentScan 注解相对应的属性和子元素。
     *
     * 可能有点让人难以置信，只创建了两个类，就能对功能进行一番尝试了。为了测试组件扫描的功能，需要创建一个简单的
     * JUnit 测试，它会创建 Spring 上下文，并判断 CompactDisc 是不是真的创建出来了。
     *
     * 以 CDPlayerAutoConfigTest 中的 cdShouldNotBeNull() 测试方法为例，就是用来完成这项任务的。
     *
     * CDPlayerAutoConfigTest 使用了 Spring 的 SpringJUnit4ClassRunner，以便在测试开始的时候自动创建 
     * Spring 的应用上下文。注解 @ContextConfiguration 会告诉它需要在 CDPlayerAutoConfig 中加载配置。因为 
     * CDPlayerAutoConfig 类中包含了 @ComponentScan，因此最终的应用上下文中应该包含 CompactDisc bean。
     *
     * 为了证明这一点，在测试代码中有一个 CompactDisc 类型的属性，并且这个属性带有 @Autowired 注解，以便于将
     * CompactDisc bean 注入到测试代码之中。最后，有一个简单的测试方法断言 cd 属性不为 null。如果它不为 null
     * 的话，就意味着 Spring 能够发现 CompactDisc 类，自动在 Spring 上下文中将其创建为 bean 并将其注入到测
     * 试代码之中。
     *
     * 这个代码应该能够通过测试，并以测试成功的颜色显示（在你的测试运行器中，或许会希望出现绿色）。你第一个简单的
     * 组件扫描练习就成功了。尽管只用它创建了一个 bean，但同样是这么少的配置能够用来发现和创建任意数量的 bean。
     * 在 com.siwuxie095.spring.chapter2nd.example3rd 包及其子包中，所有带有 @Component 注解的类都会被
     * 创建为 bean。只添加一行 @ComponentScan 注解就能自动创建无数个 bean，这种权衡还是很划算的。
     *
     * 下面会更加深入地探讨 @ComponentScan 和 @Component，看一下使用组件扫描还能做些什么。
     *
     *
     *
     * 2、为组件扫描的 bean 命名
     *
     * Spring 应用上下文中所有的 bean 都会给定一个 ID。尽管前面没有明确地为 SgtPeppers bean 设置 ID，但
     * Spring 会根据类名为其指定一个 ID。具体来讲，这个 bean 所给定的 ID 为 sgtPeppers，也就是将类名的第
     * 一个字母变为小写。
     *
     * 如果想为这个 bean 设置不同的 ID，你所要做的就是将期望的 ID 作为值传递给 @Component 注解。比如说，如果
     * 想将这个 bean 标识为 lonelyHeartsClub，那么你需要将 SgtPeppers 类的 @Component 注解配置为这样：
     *
     * @Component("lonelyHeartsClub")
     *
     * 还有另外一种为 bean 命名的方式，这种方式不使用 @Component 注解，而是使用 Java 依赖注入规范（Java
     * Dependency Injection）中所提供的 @Named 注解来为 bean 设置 ID：
     *
     * @Named("lonelyHeartsClub")
     *
     * Spring支持将 @Named 作为 @Component 注解的替代方案。两者之间有一些细微的差异，但是在大多数场景中，它们
     * 是可以互相替换的（PS：单独使用 @Named 不指定 ID，和单独使用 @Component 不指定 ID 也是一样的）。
     *
     * 话虽如此，这里更加推荐 @Component，而对于 @Named，怎么说呢，它的名字起得很不好。它并没有像 @Component
     * 那样清楚地表明它是做什么的。
     *
     *
     *
     * 3、设置组件扫描的基础包
     *
     * 到现在为止，还没有为 @ComponentScan 设置任何属性。这意味着，按照默认规则，它会以配置类所在的包作为基础
     * 包（base package）来扫描组件。但是，如果你想扫描不同的包，那该怎么办呢？或者，如果你想扫描多个基础包，
     * 那又该怎么办呢？
     *
     * 有一个原因会促使你明确地设置基础包，那就是你想要将配置类放在单独的包中，使其与其他的应用代码区分开来。如果
     * 是这样的话，那默认的基础包就不能满足要求了。
     *
     * 要满足这样的需求其实也完全没有问题！为了指定不同的基础包，你所需要做的就是在 @ComponentScan 的 value
     * 属性中指明包的名称：
     *
     * @ComponentScan("com.siwuxie095.spring.chapter2nd.example3rd")
     *
     * 如果你想更加清晰地表明你所设置的是基础包，那么你可以通过 basePackages 属性进行配置：
     *
     * @ComponentScan@ComponentScan(basePackages = "com.siwuxie095.spring.chapter2nd.example3rd")
     *
     * 可能你已经注意到了 basePackages 属性使用的是复数形式。如果你揣测这是不是意味着可以设置多个基础包，那么
     * 恭喜你猜对了。如果想要这么做的话，只需要将 basePackages 属性设置为要扫描包的一个数组即可：
     *
     * @ComponentScan@ComponentScan(basePackages = {"com.siwuxie095.spring.chapter2nd.example3rd",
     * "com.siwuxie095.spring.chapter2nd.nonexist"})
     *
     * 上面所设置的基础包是以 String 类型表示的。这是可以的，但这种方法是类型不安全（not type-safe）的。如果你
     * 重构代码的话，那么所指定的基础包可能就会出现错误了。
     *
     * 除了将包设置为简单的 String 类型之外，@ComponentScan 还提供了另外一种方法，那就是将其指定为包中所包含的
     * 类或接口：
     *
     * @ComponentScan@ComponentScan(basePackageClasses = {CDPlayer.class, NonExist.class})
     *
     * 可以看到，basePackages 属性被替换成了 basePackageClasses。同时，不是再使用 String 类型的名称来指定包，
     * 为 basePackageClasses 属性所设置的数组中包含了类。这些类所在的包将会作为组件扫描的基础包。
     *
     * 尽管在样例中，为 basePackageClasses 设置的是组件类，但是你可以考虑在包中创建一个用来进行扫描的空标记接口
     * （marker interface）。通过标记接口的方式，你依然能够保持对重构友好的接口引用，但是可以避免引用任何实际的
     * 应用程序代码（在稍后重构中，这些应用代码有可能会从想要扫描的包中移除掉）。
     *
     * 在你的应用程序中，如果所有的对象都是独立的，彼此之间没有任何依赖，就像 SgtPeppers bean 这样，那么你所需要
     * 的可能就是组件扫描而已。但是，很多对象会依赖其他的对象才能完成任务。这样的话，就需要有一种方法能够将组件扫描
     * 得到的 bean 和它们的依赖装配在一起。要完成这项任务，需要了解一下 Spring 自动化配置的另外一方面内容，那就是
     * 自动装配。
     *
     *
     *
     * 4、通过为 bean 添加注解实现自动装配
     *
     * 简单来说，自动装配就是让 Spring 自动满足 bean 依赖的一种方法，在满足依赖的过程中，会在 Spring 应用上下文
     * 中寻找匹配某个 bean 需求的其他 bean。为了声明要进行自动装配，可以借助 Spring 的 @Autowired 注解。
     *
     * 比方说，CDPlayer 类。它的构造器上添加了 @Autowired 注解，这表明当 Spring 创建 CDPlayer bean 的时候，
     * 会通过这个构造器来进行实例化并且会传入一个可设置给 CompactDisc 类型的 bean。
     *
     * @Autowired 注解不仅能够用在构造器上，还能用在属性的 Setter 方法上。比如说，如果 CDPlayer 有一个
     * setCompactDisc() 方法，那么可以采用如下的注解形式进行自动装配：
     *
     *     @Autowired
     *     public void setCompactDisc(CompactDisc cd) {
     *         this.cd = cd;
     *     }
     *
     * 在 Spring 初始化 bean 之后，它会尽可能得去满足 bean 的依赖，在本例中，依赖是通过带有 @Autowired 注解的
     * 方法进行声明的，也就是 setCompactDisc()。
     *
     * 实际上，Setter 方法并没有什么特殊之处。@Autowired 注解可以用在类的任何方法上。假设 CDPlayer 类有一个
     * insertCompactDisc() 方法，那么 @Autowired 能够像在 setCompactDisc() 上那样，发挥完全相同的作用：
     *
     *     @Autowired
     *     public void insertCompactDisc(CompactDisc cd) {
     *         this.cd = cd;
     *     }
     *
     * 不管是构造器、Setter 方法还是其他的方法，Spring 都会尝试满足方法参数上所声明的依赖。假如有且只有一个
     * bean 匹配依赖需求的话，那么这个 bean 将会被装配进来。
     *
     * 如果没有匹配的 bean，那么在应用上下文创建的时候，Spring 会抛出一个异常。为了避免异常的出现，你可以将
     * @Autowired 的 required 属性设置为 false：
     *
     *     @Autowired(required = false)
     *     public CDPlayer(CompactDisc cd) {
     *         this.cd = cd;
     *     }
     *
     * 将 required 属性设置为 false 时，Spring 会尝试执行自动装配，但是如果没有匹配的 bean 的话，Spring 将会
     * 让这个 bean 处于未装配的状态。但是，把 required 属性设置为 false 时，你需要谨慎对待。如果在你的代码中没
     * 有进行 null 检查的话，这个处于未装配状态的属性有可能会出现 NullPointerException。
     *
     * 如果有多个 bean 都能满足依赖关系的话，Spring 将会抛出一个异常，表明没有明确指定要选择哪个 bean 进行自动
     * 装配。（PS：后续会进一步讨论自动装配中的歧义性）
     *
     * @Autowired 是 Spring 特有的注解。如果你不愿意在代码中到处使用 Spring 的特定注解来完成自动装配任务的话，
     * 那么你可以考虑将其替换为 @Inject，同样是由 Java 依赖注入规范（Java Dependency Injection）所提供：
     *
     *     @Inject
     *     public CDPlayer(CompactDisc cd) {
     *         this.cd = cd;
     *     }
     *
     * PS：@Inject 注解来源于 Java 依赖注入规范（JSR-330），该规范同时还定义了 @Named 注解。
     *
     * 在自动装配中，Spring 同时支持 @Inject 和 @Autowired。尽管 @Inject 和 @Autowired 之间有着一些细微的
     * 差别，但是在大多数场景下，它们都是可以互相替换的。这里更加推荐使用 @Autowired。
     *
     *
     *
     * 5、验证自动装配
     *
     * 现在，已经在 CDPlayer 的构造器中添加了 @Autowired 注解，Spring 将把一个可分配给 CompactDisc 类型的
     * bean 自动注入进来。
     *
     * 以 CDPlayerAutoConfigTest 中的 play() 测试方法为例，就可以验证这一点，使其能够借助 CDPlayer bean 
     * 播放 CD。
     *
     * 现在，除了注入 CompactDisc，这里还将 CDPlayer bean 注入到测试代码的 player 成员变量之中（它是更为通用
     * 的 MediaPlayer 类型）。在 play() 测试方法中，可以调用 CDPlayer 的 play() 方法，并断言它的行为与你的
     * 预期一致。
     *
     * 在测试代码中使用 System.out.println() 是有点棘手的事情。因此，这里使用了 StandardOutputStreamLog，
     * 这是来源于 System Rules 库（https://stefanbirkner.github.io/system-rules/index.html）的一个
     * JUnit 规则，该规则能够基于控制台的输出编写断言。在这里，断言 SgtPeppers.play() 方法的输出被发送到了
     * 控制台上。
     * 
     * PS：关于 XML 方式进行组件扫描的测试，可参考 CDPlayerXmlConfigTest 类。
     */
    public static void main(String[] args) {

    }

}
