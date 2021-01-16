package com.siwuxie095.spring.chapter4th.example6th;

/**
 * @author Jiajing Li
 * @date 2021-01-16 14:51:09
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 使用注解创建切面
     *
     * 使用注解来创建切面是 AspectJ 5 所引入的关键特性。AspectJ 5 之前，编写 AspectJ 切面需要学习一种 Java 语言的扩展，
     * 但是 AspectJ 面向注解的模型可以非常简便地通过少量注解把任意类转变为切面。
     *
     * 先定义好 Performance 接口，它是切面中切点的目标对象。现在，开始使用 AspectJ 注解来定义切面。
     *
     *
     *
     * 1、定义切面
     *
     * 如果一场演出没有观众的话，那不能称之为演出。对不对？从演出的角度来看，观众是非常重要的，但是对演出本身的功能来讲，它
     * 并不是核心，这是一个单独的关注点。因此，将观众定义为一个切面，并将其应用到演出上就是较为明智的做法。
     *
     * 以 Audience 为例，它定义了所需的一个切面。
     *
     * @Aspect
     * public class Audience {
     *
     *     // 表演之前
     *     @Before("execution(* com.siwuxie095.spring.chapter4th.example6th.Performance.perform(..))")
     *     public void silenceCellPhones() {
     *         System.out.println("Silencing cell phones");
     *     }
     *
     *     // 表演之前
     *     @Before("execution(* com.siwuxie095.spring.chapter4th.example6th.Performance.perform(..))")
     *     public void takeSeats() {
     *         System.out.println("Taking seats");
     *     }
     *
     *     // 表演之后
     *     @AfterReturning("execution(* com.siwuxie095.spring.chapter4th.example6th.Performance.perform(..))")
     *     public void applause() {
     *         System.out.println("CLAP CLAP CLAP!!!");
     *     }
     *
     *     // 表演失败之后
     *     @AfterThrowing("execution(* com.siwuxie095.spring.chapter4th.example6th.Performance.perform(..))")
     *     public void demandRefund() {
     *         System.out.println("Demanding a refund");
     *     }
     *
     * }
     *
     * Audience 类使用 @AspectJ 注解进行了标注。该注解表明 Audience 不仅仅是一个 POJO，还是一个切面。Audience 类中的
     * 方法都使用注解来定义切面的具体行为。
     *
     * Audience 有四个方法，定义了一个观众在观看演出时可能会做的事情。在演出之前，观众要就坐（takeSeats()）并将手机调至
     * 静音状态（silenceCellPhones()）。如果演出很精彩的话，观众应该会鼓掌喝彩（applause()）。不过，如果演出没有达到观
     * 众预期的话，观众会要求退款（demandRefund()）。
     *
     * 可以看到，这些方法都使用了通知注解来表明它们应该在什么时候调用。AspectJ 提供了五个注解来定义通知：
     * （1）@After：通知方法会在目标方法返回或抛出异常后调用；
     * （2）@AfterReturning：通知方法会在目标方法返回后调用；
     * （3）@AfterThrowing：通知方法会在目标方法抛出异常后调用；
     * （4）@Around：通知方法会将目标方法封装起来；
     * （5）@Before：通知方法会在目标方法调用之前执行。
     *
     * PS：Spring 使用 AspectJ 注解来声明通知方法。分为前置通知、后置通知、环绕通知。
     *
     * Audience 使用到了前面五个注解中的三个。takeSeats() 和 silenceCellPhones() 方法都用到了 @Before 注解，表明它
     * 们应该在演出开始之前调用。applause() 方法使用了 @AfterReturning 注解，它会在演出成功返回后调用。demandRefund()
     * 方法上添加了 @AfterThrowing 注解，这表明它会在抛出异常以后执行。
     *
     * 所有的这些注解都给定了一个切点表达式作为它的值，同时，这四个方法的切点表达式都是相同的。其实，它们可以设置成不同的切
     * 点表达式，但是在这里，这个切点表达式就能满足所有通知方法的需求。近距离看一下这个设置给通知注解的切点表达式，会发现它
     * 在 Performance 的 perform() 方法执行时触发。
     *
     * 相同的切点表达式重复了四遍，这可真不是什么光彩的事情。这样的重复让人感觉有些不对劲。如果只定义这个切点一次，然后每次
     * 需要的时候引用它，那么这会是一个很好的方案。
     *
     * 幸好，完全可以这样做：@Pointcut 注解能够在一个 @AspectJ 切面内定义可重用的切点。
     *
     * 如下展现了新的 Audience，现在它使用了 @Pointcut。
     *
     * @Aspect
     * public class Audience {
     *
     *     // 定义命名的切点
     *     @Pointcut("execution(* com.siwuxie095.spring.chapter4th.example6th.Performance.perform(..))")
     *     public void performance() {}
     *
     *     // 表演之前
     *     @Before("performance()")
     *     public void silenceCellPhones() {
     *         System.out.println("Silencing cell phones");
     *     }
     *
     *     // 表演之前
     *     @Before("performance()")
     *     public void takeSeats() {
     *         System.out.println("Taking seats");
     *     }
     *
     *     // 表演之后
     *     @AfterReturning("performance()")
     *     public void applause() {
     *         System.out.println("CLAP CLAP CLAP!!!");
     *     }
     *
     *     // 表演失败之后
     *     @AfterThrowing("performance()")
     *     public void demandRefund() {
     *         System.out.println("Demanding a refund");
     *     }
     *
     * }
     *
     * 在 Audience 中，performance() 方法使用了 @Pointcut 注解。为 @Pointcut 注解设置的值是一个切点表达式，就像之前
     * 在通知注解上所设置的那样。通过在 performance() 方法上添加 @Pointcut 注解，实际上扩展了切点表达式语言，这样就可以
     * 在任何的切点表达式中使用 performance() 了，如果不这样做的话，你需要在这些地方使用那个更长的切点表达式。现在把所有
     * 通知注解中的长表达式都替换成了 performance()。
     *
     * performance() 方法的实际内容并不重要，在这里它实际上应该是空的。其实该方法本身只是一个标识，供 @Pointcut 注解依附。
     *
     * 需要注意的是，除了注解和没有实际操作的 performance() 方法，Audience 类依然是一个POJO。能够像使用其他的 Java 类
     * 那样调用它的方法，它的方法也能够独立地进行单元测试，这与其他的 Java 类并没有什么区别。Audience 只是一个 Java 类，
     * 只不过它通过注解表明会作为切面使用而已。
     *
     * 像其他的 Java 类一样，它可以装配为 Spring 中的 bean：
     *
     *     @Bean
     *     public Audience audience() {
     *         return new Audience();
     *     }
     *
     * 如果你就此止步的话，Audience 只会是 Spring 容器中的一个 bean。即便使用了 AspectJ 注解，但它并不会被视为切面，
     * 这些注解不会解析，也不会创建将其转换为切面的代理。
     *
     * 如果你使用 JavaConfig 的话，可以在配置类的类级别上通过使用 EnableAspectJAutoProxy 注解启用自动代理功能。
     *
     * @Configuration
     * @ComponentScan("com.siwuxie095.spring.chapter4th.example6th")
     * @EnableAspectJAutoProxy
     * public class ConcertConfig {
     *
     *     @Bean
     *     public Audience audience() {
     *         return new Audience();
     *     }
     *
     * }
     *
     * 假如你在 Spring 中要使用 XML 来装配 bean 的话，那么需要使用 Spring aop 命名空间中的 <aop:aspectj-autoproxy>
     * 元素。下面的 XML 配置展现了如何完成该功能。
     *
     *     <context:component-scan base-package="com.siwuxie095.spring.chapter4th.example6th" />
     *
     *     <!-- 启用 AspectJ 自动代理 -->
     *     <aop:aspectj-autoproxy />
     *
     *     <!-- 声明 Audience bean -->
     *     <bean id="audience" class="com.siwuxie095.spring.chapter4th.example6th.Audience" />
     *
     * 不管你是使用 JavaConfig 还是 XML，AspectJ 自动代理都会为使用 @Aspect 注解的 bean 创建一个代理，这个代理会围绕
     * 着所有该切面的切点所匹配的 bean。在这种情况下，将会为 Audience bean 创建一个代理，Audience 类中的通知方法将会在
     * perform() 调用前后执行。
     *
     * 需要记住的是，Spring 的 AspectJ 自动代理仅仅使用 @AspectJ 作为创建切面的指导，切面依然是基于代理的。在本质上，它
     * 依然是 Spring 基于代理的切面。这一点非常重要，因为这意味着尽管使用的是 @AspectJ 注解，但仍然限于代理方法的调用。
     * 如果想利用 AspectJ 的所有能力，必须在运行时使用 AspectJ 并且不依赖 Spring 来创建基于代理的切面。
     *
     * 到现在为止，切面在定义时，使用了不同的通知方法来实现前置通知和后置通知。但是上面还提到了另外的一种通知：环绕通知。环
     * 绕通知与其他类型的通知有所不同，因此值得花点时间来介绍如何进行编写。
     *
     *
     *
     * 2、创建环绕通知
     *
     * 环绕通知是最为强大的通知类型。它能够让你所编写的逻辑将被通知的目标方法完全包装起来。实际上就像在一个通知方法中同时编
     * 写前置通知和后置通知。
     *
     * 为了阐述环绕通知，这里重写 Audience 切面。这次，使用一个环绕通知来代替之前多个不同的前置通知和后置通知。
     *
     * @Aspect
     * public class AroundAudience {
     *
     *     // 定义命名的切点
     *     @Pointcut("execution(* com.siwuxie095.spring.chapter4th.example6th.Performance.perform(..))")
     *     public void performance() {}
     *
     *     // 环绕通知方法
     *     @Around("performance()")
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
     * 在这里，@Around 注解表明 watchPerformance() 方法会作为 performance() 切点的环绕通知。在这个通知中，观众在演出
     * 之前会将手机调至静音并就坐，演出结束后会鼓掌喝彩。像前面一样，如果演出失败的话，观众会要求退款。
     *
     * 可以看到，这个通知所达到的效果与之前的前置通知和后置通知是一样的。但是，现在它们位于同一个方法中，不像之前那样分散在
     * 四个不同的通知方法里面。
     *
     * 关于这个新的通知方法，你首先注意到的可能是它接受 ProceedingJoinPoint 作为参数。这个对象是必须要有的，因为你要在通
     * 知中通过它来调用被通知的方法。
     *
     * 通知方法中可以做任何的事情，当要将控制权交给被通知的方法时，它需要调用 ProceedingJoinPoint 的 proceed() 方法。
     *
     * 需要注意的是，别忘记调用 proceed() 方法。如果不调这个方法的话，那么你的通知实际上会阻塞对被通知方法的调用。有可能这
     * 就是你想要的效果，但更多的情况是你希望在某个点上执行被通知的方法。
     *
     * 有意思的是，你可以不调用 proceed() 方法，从而阻塞对被通知方法的访问，与之类似，你也可以在通知中对它进行多次调用。要
     * 这样做的一个场景就是实现重试逻辑，也就是在被通知方法失败后，进行重复尝试。
     *
     *
     *
     * 3、处理通知中的参数
     *
     * 到目前为止，切面都很简单，没有任何参数。
     *
     * 唯一的例外是为环绕通知所编写的 watchPerformance() 示例方法中使用了 ProceedingJoinPoint 作为参数。除了环绕通知，
     * 编写的其他通知不需要关注传递给被通知方法的任意参数。这很正常，因为这里所通知的 perform() 方法本身没有任何参数。
     *
     * 但是，如果切面所通知的方法确实有参数该怎么办呢？切面能访问和使用传递给被通知方法的参数吗？
     *
     * 为了阐述这个问题，不妨看一下 BlankDisc 样例。play() 方法会循环所有的磁道并调用 playTrack() 方法。但是，也可以通
     * 过 playTrack() 方法直接播放某一个磁道中的歌曲。
     *
     * 假设你想记录每个磁道被播放的次数。一种方法就是修改 playTrack() 方法，直接在每次调用的时候记录这个数量。但是，记录磁
     * 道的播放次数与播放本身是不同的关注点，因此不应该属于 playTrack() 方法。看起来，这应该是切面要完成的任务。
     *
     * 为了记录每个磁道所播放的次数，我们创建了 TrackCounter 类，它是通知 playTrack() 方法的一个切面。
     *
     * @Aspect
     * public class TrackCounter {
     *
     *     private Map<Integer, Integer> trackCounts = new HashMap<>();
     *
     *     // 通知 playTrack() 方法
     *     @Pointcut("execution(* com.siwuxie095.spring.chapter4th.example6th.CompactDisc.playTrack(int)) " +
     *             "&& args(trackNumber)")
     *     public void trackPlayed(int trackNumber) {}
     *
     *     // 在播放前，为该磁道计数
     *     @Before("trackPlayed(trackNumber)")
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
     * 像之前所创建的切面一样，这个切面使用 @Pointcut 注解定义命名的切点，并使用 @Before 将一个方法声明为前置通知。但是，
     * 这里的不同点在于切点还声明了要提供给通知方法的参数。
     *
     * 如下将切点表达式进行了分解，以展现参数是在什么地方指定的。
     *
     * execution(* com.siwuxie095.spring.chapter4th.example6th.CompactDisc.playTrack(int)) && args(trackNumber)
     *
     * PS：在切点表达式中声明参数，这个参数传入到通知方法中。
     *
     * 其中：
     * （1）* 表示返回任意类型。
     * （2）com.siwuxie095.spring.chapter4th.example6th.CompactDisc 表示方法所属的类型。
     * （3）playTrack 表示方法。
     * （4）int 表示接受 int 类型的参数。
     * （5）args(trackNumber) 表示指定参数。
     *
     * 需要关注的是切点表达式中的 args(trackNumber) 限定符。它表明传递给 playTrack() 方法的 int 类型参数也会传递到通知
     * 中去。参数的名称 trackNumber 也与切点方法签名中的参数相匹配。
     *
     * 这个参数会传递到通知方法中，这个通知方法是通过 @Before 注解和命名切点 trackPlayed(trackNumber) 定义的。切点定义
     * 中的参数与切点方法中的参数名称是一样的，这样就完成了从命名切点到通知方法的参数转移。
     *
     * 现在，可以在 Spring 配置中将 BlankDisc 和 TrackCounter 定义为 bean，并启用 AspectJ 自动代理，如下：
     *
     * @Configuration
     * @ComponentScan("com.siwuxie095.spring.chapter4th.example6th")
     * @EnableAspectJAutoProxy
     * public class TrackCounterConfig {
     *
     *     @Bean
     *     public CompactDisc compactDisc() {
     *         BlankDisc blankDisc = new BlankDisc();
     *         blankDisc.setTitle("Sgt. Pepper's Lonely Hearts Club Band");
     *         blankDisc.setArtist("The Beatles");
     *         List<String> tracks = new ArrayList<>();
     *         tracks.add("Sgt. Pepper's Lonely Hearts Club Band");
     *         tracks.add("With a Little Help from My Friends");
     *         tracks.add("Lucy in the Sky with Diamonds");
     *         tracks.add("Getting Better");
     *         tracks.add("Fixing a Hole");
     *         // ...other tracks omitted for brevity...
     *         blankDisc.setTracks(tracks);
     *         return blankDisc;
     *
     *     }
     *
     *     @Bean
     *     public TrackCounter trackCounter() {
     *         return new TrackCounter();
     *     }
     *
     * }
     *
     * 最后，为了证明它能正常工作，你可以编写如下的简单测试。它会播放几个磁道并通过 TrackCounter 断言播放的数量。
     *
     * @RunWith(SpringJUnit4ClassRunner.class)
     * @ContextConfiguration(classes = TrackCounterConfig.class)
     * public class TrackCounterTest {
     *
     *     public final StandardOutputStreamLog log = new StandardOutputStreamLog();
     *
     *     @Autowired
     *     private CompactDisc cd;
     *     @Autowired
     *     private TrackCounter counter;
     *
     *     @Test
     *     public void testTrackCounter() {
     *         cd.playTrack(1);
     *         cd.playTrack(2);
     *         cd.playTrack(3);
     *         cd.playTrack(3);
     *         cd.playTrack(3);
     *         cd.playTrack(3);
     *
     *         assertEquals(1, counter.getPlayCount(1));
     *         assertEquals(1, counter.getPlayCount(2));
     *         assertEquals(4, counter.getPlayCount(3));
     *         assertEquals(0, counter.getPlayCount(4));
     *         assertEquals(0, counter.getPlayCount(5));
     *     }
     *
     * }
     *
     * 到目前为止，在所使用的切面中，所包装的都是被通知对象的已有方法。但是，方法包装仅仅是切面所能实现的功能之一。下面看一下
     * 如何通过编写切面，为被通知的对象引入全新的功能。
     *
     *
     *
     * 4、通过注解引入新功能
     *
     * 一些编程语言，例如 Ruby 和 Groovy，有开放类的理念。它们可以不用直接修改对象或类的定义就能够为对象或类增加新的方法。
     * 不过，Java 并不是动态语言。一旦类编译完成了，就很难再为该类添加新的功能了。
     *
     * 但是如果仔细想想，AOP 不是一直在使用切面这样做吗？当然，这里还没有为对象增加任何新的方法，但是已经为对象拥有的方法添加
     * 了新功能。如果切面能够为现有的方法增加额外的功能，为什么不能为一个对象增加新的方法呢？实际上，利用被称为引入的 AOP 概
     * 念，切面可以为 Spring bean 添加新方法。
     *
     * 在 Spring 中，切面只是实现了它们所包装 bean 相同接口的代理。如果除了实现这些接口，代理也能暴露新接口的话，会怎么样呢？
     * 那样的话，切面所通知的 bean 看起来像是实现了新的接口，即便底层实现类并没有实现这些接口也无所谓。
     *
     * PS：使用 Spring AOP，可以为 bean 引入新的方法。代理拦截调用并委托给实现该方法的其他对象。
     *
     * 需要注意的是，当引入接口的方法被调用时，代理会把此调用委托给实现了新接口的某个其他对象。实际上，一个 bean 的实现被拆分
     * 到了多个类中。
     *
     * 为了验证该主意能行得通，为示例中的所有的 Performance 实现引入下面的 Encoreable 接口：
     *
     * public interface Encoreable {
     *
     *     void performEncore();
     *
     * }
     *
     * PS：Encoreable 对应的英文单词词根为 encore，指的是演唱会演出结束后应观众要求进行返场表演。
     *
     * 暂且先不管 Encoreable 是不是一个真正存在的单词，这里需要有一种方式将这个接口应用到 Performance 实现中。现在假设你能
     * 够访问 Performance 的所有实现，并对其进行修改，让它们都实现 Encoreable 接口。但是，从设计的角度来看，这并不是最好的
     * 做法，并不是所有的 Performance 都是具有 Encoreable 特性的。另外一方面，有可能无法修改所有的 Performance 实现，当
     * 使用第三方实现并且没有源码的时候更是如此。
     *
     * 值得庆幸的是，借助于 AOP 的引入功能，可以不必在设计上妥协或者侵入性地改变现有的实现。为了实现该功能，要创建一个新的切面：
     *
     * @Aspect
     * public class EncoreableIntroducer {
     *
     *     @DeclareParents(value = "com.siwuxie095.spring.chapter4th.example6th.Performance+",
     *             defaultImpl = DefaultEncoreable.class)
     *     public static Encoreable encoreable;
     *
     * }
     *
     * 可以看到，EncoreableIntroducer 是一个切面。但是，它与之前所创建的切面不同，它并没有提供前置、后置或环绕通知，而是通过
     * @DeclareParents 注解，将 Encoreable 接口引入到 Performance bean 中。
     *
     * @DeclareParents 注解由三部分组成：
     * （1）value 属性指定了哪种类型的 bean 要引入该接口。在本例中，也就是所有实现 Performance 的类型。（标记符后面的加号
     * 表示是 Performance 的所有子类型，而不是 Performance 本身。）
     * （2）defaultImpl 属性指定了为引入功能提供实现的类。在这里，指定的是 DefaultEncoreable 提供实现。
     * （3）@DeclareParents 注解所标注的静态属性指明了要引入了接口。在这里，所引入的是 Encoreable 接口。
     *
     * 和其他的切面一样，需要在 Spring 应用中将 EncoreableIntroducer 声明为一个 bean：
     *
     *     <bean class="com.siwuxie095.spring.chapter4th.example6th.EncoreableIntroducer" />
     *
     * PS：也可以使用 JavaConfig 的方式进行配置。
     *
     * Spring 的自动代理机制将会获取到它的声明，当 Spring 发现一个 bean 使用了 @Aspect 注解时，Spring 就会创建一个代理，
     * 然后将调用委托给被代理的 bean 或被引入的实现，这取决于调用的方法属于被代理的 bean 还是属于被引入的接口。
     *
     * 在 Spring 中，注解和自动代理提供了一种很便利的方式来创建切面。它非常简单，并且只涉及到最少的 Spring 配置。但是，面向
     * 注解的切面声明有一个明显的劣势：你必须能够为通知类添加注解。为了做到这一点，必须要有源码。
     *
     * 如果你没有源码的话，或者不想将 AspectJ 注解放到你的代码之中，Spring 为切面提供了另外一种可选方案。后续看一下如何在
     * Spring XML 配置文件中声明切面。
     */
    public static void main(String[] args) {

    }

}
