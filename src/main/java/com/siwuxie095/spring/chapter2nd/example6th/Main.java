package com.siwuxie095.spring.chapter2nd.example6th;

/**
 * @author Jiajing Li
 * @date 2020-12-25 08:14:02
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 导入和混合配置
     *
     * 在典型的 Spring 应用中，我们可能会同时使用自动化和显式配置。即便你更喜欢通过 JavaConfig 实现显式配置，
     * 但有的时候 XML 却是最佳的方案。
     *
     * 幸好在 Spring 中，这些配置方案都不是互斥的。你尽可以将 JavaConfig 的组件扫描和自动装配和/或 XML 配置
     * 混合在一起。因为至少需要有一点显式配置来启用组件扫描和自动装配。
     *
     * 关于混合配置，第一件需要了解的事情就是在自动装配时，它并不在意要装配的 bean 来自哪里。自动装配的时候会
     * 考虑到 Spring 容器中所有的 bean，不管它是在 JavaConfig 或 XML 中声明的还是通过组件扫描获取到的。
     *
     * 你可能会想在显式配置时，比如在 XML 配置和 Java 配置中该如何引用 bean 呢？
     *
     * 下面先看一下如何在 JavaConfig 中引用 XML 配置的 bean。
     *
     *
     *
     * 在 JavaConfig 中引用 XML 配置
     *
     * 现在，临时假设 CDPlayerJavaConfig 已经变得有些笨重，此时想要将其进行拆分。当然，它目前只定义了两个 
     * bean，远远称不上复杂的 Spring 配置。不过，假设两个 bean 就已经太多了。
     *
     * 所能实现的一种方案就是将 BlankDisc 从 CDPlayerJavaConfig 拆分出来，定义到它自己的 CDJavaConfig 类
     * 中。
     *
     * compactDisc() 方法已经从 CDPlayerJavaConfig 中移除掉了，需要有一种方式将这两个类组合在一起。一种方
     * 法就是在 CDPlayerJavaConfig 中使用 @Import 注解导入 CDJavaConfig：
     *
     * @Configuration
     * @Import(CDJavaConfig.class)
     * public class CDPlayerJavaConfig {
     *
     *     @Bean
     *     public CDPlayer cdPlayer(CompactDisc compactDisc) {
     *         return new CDPlayer(compactDisc);
     *     }
     *
     * }
     *
     * 或者采用一个更好的办法！
     *
     * 也就是不在 CDPlayerJavaConfig 中使用 @Import，而是创建一个更高级别的 SoundSystemConfig，在这个类
     * 中使用 @Import 将两个配置类组合在一起：
     *
     * @Configuration
     * @Import({CDPlayerJavaConfig.class, CDJavaConfig.class})
     * public class SoundSystemConfig {
     *
     * }
     *
     * 不管采用哪种方式，都能将 CDPlayer 的配置与 BlankDisc 的配置分开。现在，假设（基于某些原因）希望通过
     * XML 来配置 BlankDisc，即 cd-config.xml，如下所示：
     *
     *     <bean id="compactDisc"
     *           class="com.siwuxie095.spring.chapter2nd.example6th.BlankDisc"
     *           c:_0="Sgt. Pepper's Lonely Hearts Club Band"
     *           c:_1="The Beatles">
     *     </bean>
     *
     * 现在 BlankDisc 配置在了 XML 之中，该如何让 Spring 同时加载它和其他基于 Java 的配置呢？
     *
     * 答案是 @ImportResource 注解，假设 BlankDisc 定义在名为 cd-config.xml 的文件中，该文件位于根类路径
     * 下，那么可以修改 SoundSystemConfig，让它使用 @ImportResource 注解：
     *
     * @Configuration
     * @Import(CDPlayerJavaConfig.class)
     * @ImportResource("file:src/main/java/com/siwuxie095/spring/chapter2nd/example6th/res/cd-config.xml")
     * public class SoundSystemConfig {
     *
     * }
     *
     * 两个 bean —— 配置在 JavaConfig 中的 CDPlayer 以及配置在 XML 中 BlankDisc —— 都会被加载到 Spring
     * 容器之中。因为 CDPlayer 中带有 @Bean 注解的方法接受一个 CompactDisc 作为参数，因此 BlankDisc 将会
     * 装配进来，此时与它是通过 XML 配置的没有任何关系。
     *
     * 下面继续这个练习，但是这一次，需要在 XML 中引用 JavaConfig 声明的 bean。
     *
     *
     *
     * 在 XML 配置中引用 JavaConfig
     *
     * 假设你正在使用 Spring 基于 XML 的配置并且你已经意识到 XML 逐渐变得无法控制。像前面一样，正在处理的是两
     * 个 bean，但事情实际上会变得更加糟糕。在被无数的尖括号淹没之前，决定将 XML 配置文件进行拆分。
     *
     * 在 JavaConfig 配置中，已经展现了如何使用 @Import 和 @ImportResource 来拆分 JavaConfig 类。在 XML
     * 中，可以使用 import 元素来拆分 XML 配置。
     *
     * 比如，假设希望将 BlankDisc bean 拆分到自己的配置文件中，该文件名为 cd-config.xml，内容同上。这与之前
     * 使用 @ImportResource 是一样的。可以在 XML 配置文件（即 cdplayer-config.xml）中使用 <import> 元素
     * 来引用该文件：
     *
     *     <import resource="cd-config.xml"/>
     *
     *     <bean id="cdPlayer"
     *           class="com.siwuxie095.spring.chapter2nd.example6th.CDPlayer"
     *           c:cd-ref="compactDisc"/>
     *
     * 现在，假设不再将 BlankDisc 配置在 XML 之中，而是将其配置在 JavaConfig 中，CDPlayer 则继续配置在 XML
     * 中。基于 XML 的配置该如何引用一个 JavaConfig 类呢？
     *
     * 事实上，答案并不那么直观。<import> 元素只能导入其他的 XML 配置文件，并没有 XML 元素能够导入 JavaConfig
     * 类。
     *
     * 但是，有一个你已经熟知的元素能够用来将 Java 配置导入到 XML 配置中：<bean>元素。为了将 JavaConfig 类导
     * 入到 XML 配置中，可以这样声明 bean：
     *
     *     <bean class="com.siwuxie095.spring.chapter2nd.example6th.CDJavaConfig"/>
     *
     *     <bean id="cdPlayer"
     *           class="com.siwuxie095.spring.chapter2nd.example6th.CDPlayer"
     *           c:cd-ref="compactDisc"/>
     *
     * 采用这样的方式，两种配置 —— 其中一个使用 XML 描述，另一个使用 Java 描述 —— 被组合在了一起。类似地，你可
     * 能还希望创建一个更高层次的配置文件（新的配置文件），这个文件不声明任何的 bean，只是负责将两个或更多的配置
     * 组合起来。 例如，你可以将 CDJavaConfig bean 从之前的 XML 文件中移除掉，而是使用第三个配置文件将这两个
     * 组合在一起：
     *
     *     <bean class="com.siwuxie095.spring.chapter2nd.example6th.CDJavaConfig"/>
     *
     *     <import resource="cdplayer-config.xml"/>
     *
     * 不管使用 JavaConfig 还是使用 XML 进行装配，通常都应该创建一个根配置（root configuration），也就是这里
     * 展现的这样，这个配置会将两个或更多的装配类和/或 XML 文件组合起来。同时也会在根配置中启用组件扫描（通过
     * <context:component-scan> 或 @ComponentScan）。
     *
     * PS：关于测试可参见 CDPlayerMixedConfigTest 类。
     */
    public static void main(String[] args) {

    }

}
