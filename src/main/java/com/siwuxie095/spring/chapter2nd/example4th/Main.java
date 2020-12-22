package com.siwuxie095.spring.chapter2nd.example4th;

/**
 * @author Jiajing Li
 * @date 2020-12-22 21:54:58
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 通过 Java 代码装配 bean
     *
     * 尽管在很多场景下通过组件扫描和自动装配实现 Spring 的自动化配置是更为推荐的方式，但有时候自动化配置的方案行不通，
     * 因此需要明确配置 Spring。比如说，你想要将第三方库中的组件装配到你的应用中，在这种情况下，是没有办法在它的类上添
     * 加 @Component 和 @Autowired 注解的，因此就不能使用自动化装配的方案了。
     *
     * 在这种情况下，你必须要采用显式装配的方式。在进行显式配置的时候，有两种可选方案：Java 和 XML。这里将会学习如何
     * 使用 Java 配置，后续将会学习 Spring 的 XML 配置。
     *
     * 在进行显式配置时，JavaConfig 是更好的方案，因为它更为强大、类型安全并且对重构友好。因为它就是 Java 代码，就像
     * 应用程序中的其他 Java 代码一样。
     *
     * 同时，JavaConfig 与其他的 Java 代码又有所区别，在概念上，它与应用程序中的业务逻辑和领域代码是不同的。尽管它与
     * 其他的组件一样都使用相同的语言进行表述，但 JavaConfig 是配置代码。这意味着它不应该包含任何业务逻辑，JavaConfig
     * 也不应该侵入到业务逻辑代码之中。尽管不是必须的，但通常会将 JavaConfig 放到单独的包中，使它与其他的应用程序逻辑
     * 分离开来，这样对于它的意图就不会产生困惑了。
     *
     * 接下来，看一下如何通过 JavaConfig 显式配置 Spring。
     *
     *
     * 1、创建配置类
     *
     * 创建 JavaConfig 类的关键在于为其添加 @Configuration 注解，@Configuration 注解表明这个类是一个配置类，该类
     * 应该包含在 Spring 应用上下文中如何创建 bean 的细节。
     *
     * 以 CDPlayerJavaConfig 为例，注意，这里没有 @ComponentScan 注解。
     *
     *
     *
     * 2、声明简单的 bean
     *
     * 要在 JavaConfig 中声明 bean，需要编写一个方法，这个方法会创建所需类型的实例，然后给这个方法添加 @Bean 注解。
     *
     * 比方说，下面的代码声明了 CompactDisc bean：
     *
     *     @Bean
     *     public CompactDisc compactDisc() {
     *         return new SgtPeppers();
     *     }
     *
     * @Bean 注解会告诉 Spring 这个方法将会返回一个对象，该对象要注册为 Spring 应用上下文中的 bean。方法体中包含了
     * 最终产生 bean 实例的逻辑。
     *
     * 默认情况下，bean 的 ID 与带有 @Bean 注解的方法名是一样的。在本例中，bean 的名字将会是 compactDisc。如果你
     * 想为其设置成一个不同的名字的话，那么可以重命名该方法，也可以通过 name 属性指定一个不同的名字：
     *
     *     @Bean("lonelyHeartsClub")
     *     public CompactDisc compactDisc() {
     *         return new SgtPeppers();
     *     }
     *
     * 不管你采用什么方法来为 bean 命名，bean 声明都是非常简单的。方法体返回了一个新的 SgtPeppers 实例。这里是使用
     * Java 来进行描述的，因此可以发挥 Java 提供的所有功能，只要最终生成一个 CompactDisc 实例即可。
     *
     * 请稍微发挥一下你的想象力，这里可能希望做稍微疯狂的事情，比如说，在一组 CD 中随机选择一个 CompactDisc 来播放：
     *
     *     @Bean
     *     public CompactDisc randomBeatlesCD() {
     *         int choice = (int) Math.floor(Math.random() * 4);
     *         if (choice == 0) {
     *             return new SgtPeppers();
     *         } else if (choice == 1) {
     *              // non exist
     *             return new WhiteAlbum();
     *         } else if (choice == 2) {
     *              // non exist
     *             return new HardDaysNight();
     *         } else {
     *              // non exist
     *             return new Revolver();
     *         }
     *     }
     *
     * 现在，你可以自己想象一下，借助 @Bean 注解方法的形式，该如何发挥出 Java 的全部威力来产生 bean。当你想完之后，
     * 要回过头来看一下在 JavaConfig 中，如何将 CompactDisc 注入到 CDPlayer 之中。
     *
     *
     *
     * 3、借助 JavaConfig 实现注入
     *
     * 前面所声明的 CompactDisc bean 是非常简单的，它自身没有其他的依赖。但现在，需要声明 CDPlayer bean，它依赖于
     * CompactDisc。在 JavaConfig 中，要如何将它们装配在一起呢？
     *
     * 在 JavaConfig 中装配 bean 的最简单方式就是引用创建 bean 的方法。如下就是一种声明 CDPlayer 的可行方案：
     *
     *     @Bean
     *     public CDPlayer cdPlayer() {
     *         return new CDPlayer(compactDisc());
     *     }
     *
     * cdPlayer() 方法像 compactDisc() 方法一样，同样使用了 @Bean 注解，这表明这个方法会创建一个 bean 实例并将其
     * 注册到 Spring 应用上下文中。所创建的 bean ID 为 cdPlayer，与方法的名字相同。
     *
     * cdPlayer() 的方法体与 compactDisc() 稍微有些区别。在这里并没有使用默认的构造器构建实例，而是调用了需要传入
     * CompactDisc 对象的构造器来创建 CDPlayer 实例。
     *
     * 看起来，CompactDisc 是通过调用 compactDisc() 得到的，但情况并非完全如此。因为 compactDisc() 方法上添加了
     * @Bean 注解，Spring 将会拦截所有对它的调用，并确保直接返回该方法所创建的 bean，而不是每次都对其进行实际的调用。
     *
     * 比如说，假设你引入了一个其他的 CDPlayer bean，那么它和之前的那个 bean 完全一样：
     *
     *     @Bean
     *     public CDPlayer cdPlayer() {
     *         return new CDPlayer(compactDisc());
     *     }
     *
     *     @Bean
     *     public CDPlayer anotherCdPlayer() {
     *         return new CDPlayer(compactDisc());
     *     }
     *
     * 假如对 compactDisc() 的调用就像其他的 Java 方法调用一样的话，那么每个 CDPlayer 实例都会有一个自己特有的
     * SgtPeppers 实例。如果这里讨论的是实际的 CD 播放器和 CD 光盘的话，这么做是有意义的。如果你有两台 CD 播放器，
     * 在物理上并没有办法将同一张 CD 光盘放到两个 CD 播放器中。
     *
     * 但是，在软件领域中，完全可以将同一个 SgtPeppers 实例注入到任意数量的其他 bean 之中。默认情况下，Spring 中
     * 的 bean 都是单例的，这里并没有必要为第二个 CDPlayer bean 创建完全相同的 SgtPeppers 实例。所以，Spring
     * 会拦截对 compactDisc() 的调用并确保返回的是 Spring 所创建的 bean，也就是 Spring 本身在调用 compactDisc()
     * 时所创建的 CompactDisc bean。因此，两个 CDPlayer bean 会得到相同的 SgtPeppers 实例。
     *
     * 可以看到，通过调用方法来引用 bean 的方式有点令人困惑。其实还有一种理解起来更为简单的方式：
     *
     *     @Bean
     *     public CDPlayer cdPlayer(CompactDisc compactDisc) {
     *         return new CDPlayer(compactDisc);
     *     }
     *
     * 在这里，cdPlayer() 方法请求一个 CompactDisc 作为参数。当 Spring 调用 cdPlayer() 创建 CDPlayer bean
     * 的时候，它会自动装配一个 CompactDisc 到配置方法之中。然后，方法体就可以按照合适的方式来使用它。借助这种技术，
     * cdPlayer() 方法也能够将 CompactDisc 注入到 CDPlayer 的构造器中，而且不用明确引用 CompactDisc 的 @Bean
     * 方法。
     *
     * 通过这种方式引用其他的 bean 通常是最佳的选择，因为它不会要求将 CompactDisc 声明到同一个配置类之中。在这里甚至
     * 没有要求 CompactDisc 必须要在 JavaConfig 中声明，实际上它可以通过组件扫描功能自动发现或者通过 XML 来进行配置。
     * 你可以将配置分散到多个配置类、XML 文件以及自动扫描和装配 bean 之中，只要功能完整健全即可。不管 CompactDisc 是
     * 采用什么方式创建出来的，Spring 都会将其传入到配置方法中，并用来创建 CDPlayer bean。
     *
     * 另外，需要提醒的是，在这里使用 CDPlayer 的构造器实现了 DI 功能， 但是完全可以采用其他风格的 DI 配置。比如说，
     * 如果你想通过 Setter 方法注入 CompactDisc 的话，那么代码看起来应该是这样的：
     *
     *     @Bean
     *     public CDPlayer cdPlayer(CompactDisc compactDisc) {
     *         CDPlayer cdPlayer = new CDPlayer(compactDisc);
     *         cdPlayer.setCompactDisc(compactDisc);
     *         return cdPlayer;
     *     }
     *
     * 再次强调一遍，带有 @Bean 注解的方法可以采用任何必要的 Java 功能来产生 bean 实例。构造器和 Setter 方法只是
     * @Bean 方法的两个简单样例。这里所存在的可能性仅仅受到 Java 语言的限制。
     *
     * PS：关于测试可参见 CDPlayerJavaConfigTest 类。
     */
    public static void main(String[] args) {

    }

}
