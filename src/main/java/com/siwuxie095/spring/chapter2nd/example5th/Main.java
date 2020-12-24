package com.siwuxie095.spring.chapter2nd.example5th;

/**
 * @author Jiajing Li
 * @date 2020-12-23 08:02:24
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 通过 XML 装配 bean
     *
     * 在装配 bean 的时候，还有一种可选方案，即 通过 XML 装配 bean，尽管这种方案可能不太合乎大家的心意，但是
     * 它在 Spring 中已经有很长的历史了。
     *
     * 在 Spring 刚刚出现的时候，XML 是描述配置的主要方式。在 Spring 的名义下，大家创建了无数行 XML 代码。
     * 在一定程度上，Spring 成为了 XML 配置的同义词。
     *
     * 尽管 Spring 长期以来确实与 XML 有着关联，但现在需要明确的是，XML 不再是配置 Spring 的唯一可选方案。
     * Spring 现在有了强大的自动化配置和基于 Java 的配置，XML 不应该再是你的第一选择了。
     *
     * 不过，鉴于已经存在那么多基于 XML 的 Spring 配置，所以理解如何在 Spring 中使用 XML 还是很重要的。但是，
     * 希望这里的内容只是用来帮助你维护已有的 XML 配置，在完成新的 Spring 工作时，希望你会使用 Spring 的自动
     * 化配置和 JavaConfig。
     *
     *
     *
     * 1、创建 XML 配置规范
     *
     * 在使用 XML 为 Spring 装配 bean 之前，你需要创建一个新的配置规范。在使用 JavaConfig 的时候，这意味着
     * 要创建一个带有 @Configuration 注解的类，而在 XML 配置中，这意味着要创建一个 XML 文件，并且要以 <beans>
     * 元素为根。
     *
     * 最为简单的 Spring XML 配置如下所示：
     *
     * <?xml version="1.0" encoding="UTF-8"?>
     * <beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     *        xmlns="http://www.springframework.org/schema/beans"
     *        xsi:schemaLocation="http://www.springframework.org/schema/beans
     *        http://www.springframework.org/schema/beans/spring-beans-4.0.xsd
     *         http://www.springframework.org/schema/context
     *         http://www.springframework.org/schema/context/spring-context-4.0.xsd">
     *
     *     <!-- configuration details go here -->
     *
     * </beans>
     *
     * 很容易就能看出来，这个基本的 XML 配置已经比同等功能的 JavaConfig 类复杂得多了。作为起步，在 JavaConfig
     * 中所需要的只是 @Configuration，但在使用 XML 时，需要在配置文件的顶部声明多个 XML 模式（XSD）文件，这些
     * 文件定义了配置 Spring 的 XML 元素。
     *
     * PS：借助 Spring Tool Suite 创建 XML 配置文件：
     * 创建和管理 Spring XML 配置文件的一种简便方式是使用 Spring Tool Suite(https://spring.io/tools/)。
     * 在 Spring Tool Suite 的菜单中，选择 File > New > Spring Bean Configuration File，能够创建
     * Spring XML 配置文件，并且可以选择可用的配置命名空间。
     *
     * 用来装配 bean 的最基本的 XML 元素包含在 spring-beans 模式之中，在上面这个 XML 文件中，它被定义为根命
     * 名空间。<beans> 是该模式中的一个元素，它是所有 Spring 配置文件的根元素。
     *
     * PS：在 XML 中配置 Spring 时，还有一些其他的模式。后续会提及。
     *
     * 就这样，已经有了一个合法的 Spring XML 配置。不过，它也是一个没有任何用处的配置，因为它（还）没有声明任何
     * bean。为了给予它生命力，需要重新创建一下 CD 样例，只不过这次使用 XML 配置，而不是使用 JavaConfig 和自
     * 动化装配。
     *
     *
     *
     * 2、声明一个简单的 <bean>
     *
     * 要在基于 XML 的 Spring 配置中声明一个 bean，要使用 spring-beans 模式中的另外一个元素：<bean>。
     * <bean> 元素类似于 JavaConfig 中的 @Bean 注解。可以按照如下的方式声明 CompactDisc bean：
     *
     *     <bean class="com.siwuxie095.spring.chapter2nd.example5th.SgtPeppers"/>
     *
     * 这里声明了一个很简单的 bean，创建这个 bean 的类通过 class 属性来指定的，并且要使用全限定的类名。
     *
     * 因为没有明确给定 ID，所以这个 bean 将会根据全限定类名来进行命名。在本例中，bean 的 ID 将会是
     * "com.siwuxie095.spring.chapter2nd.example5th.SgtPeppers#0"。其中，"#0" 是一个计数的形式，
     * 用来区分相同类型的其他 bean。如果你声明了另外一个 SgtPeppers，并且没有明确进行标识，那么它自动得
     * 到的 ID 将会是 "com.siwuxie095.spring.chapter2nd.example5th.SgtPeppers#1"。
     *
     * 尽管自动化的 bean 命名方式非常方便，但如果你要稍后引用它的话，那自动产生的名字就没有多大的用处了。因此，
     * 通常来讲更好的办法是借助 id 属性，为每个 bean 设置一个你自己选择的名字：
     *
     *     <bean id="compactDisc" class="com.siwuxie095.spring.chapter2nd.example5th.SgtPeppers"/>
     *
     * 稍后将这个 bean 装配到 CDPlayer bean 之中的时候，你会用到这个具体的名字。
     *
     * PS：关于减少繁琐：
     * 为了减少 XML 中繁琐的配置，只对那些需要按名字引用的 bean（比如，你需要将对它的引用注入到另外一个 bean
     * 中）进行明确地命名。
     *
     * 对于这个简单 bean 声明的一些特征，有两点需要注意：
     * （1）第一件需要注意的事情就是你不再需要直接负责创建 SgtPeppers 的实例，在基于 JavaConfig 的配置中，
     * 是需要这样做的。当 Spring 发现这个 <bean> 元素时，它将会调用 SgtPeppers 的默认构造器来创建 bean。
     * 在 XML 配置中，bean 的创建显得更加被动，不过，它并没有 JavaConfig 那样强大，在 JavaConfig 配置方
     * 式中，你可以通过任何可以想象到的方法来创建 bean 实例。
     * （2）另外一个需要注意到的事情就是，在这个简单的 <bean> 声明中，将 bean 的类型以字符串的形式设置在了
     * class 属性中。谁能保证设置给 class 属性的值是真正的类呢？Spring 的 XML 配置并不能从编译期的类型检
     * 查中受益。即便它所引用的是实际的类型，如果你重命名了类，会发生什么呢？
     *
     * PS：借助 IDE 检查 XML 的合法性：
     * 使用能够感知 Spring 功能的 IDE，如 Spring Tool Suite，能够在很大程度上帮助你确保 Spring XML 配置的
     * 合法性。
     *
     * 以上介绍的只是 JavaConfig 要优于 XML 配置的部分原因。建议在为你的应用选择配置风格时，要记住 XML 配置的
     * 这些缺点。接下来，继续 Spring XML 配置的学习进程，了解如何将 SgtPeppers bean 注入到 CDPlayer 之中。
     *
     *
     *
     * 3、借助构造器注入初始化 bean
     *
     * 在 Spring XML 配置中，只有一种声明 bean 的方式：使用 <bean> 元素并指定 class 属性。Spring 会从这里
     * 获取必要的信息来创建 bean。
     *
     * 但是，在 XML 中声明 DI 时，会有多种可选的配置方案和风格。具体到构造器注入，有两种基本的配置方案可供选择：
     * （1）<constructor-arg> 元素；
     * （2）使用 Spring 3.0 所引入的 c-命名空间。
     *
     * 两者的区别在很大程度就是是否冗长烦琐。可以看到，<constructor-arg> 元素比使用 c-命名空间会更加冗长，从
     * 而导致 XML 更加难以读懂。另外，有些事情 <constructor-arg> 可以做到，但是使用 c-命名空间却无法实现。
     *
     * 在介绍 Spring XML 的构造器注入时，将会分别介绍这两种可选方案。首先，看一下它们各自如何注入 bean 引用。
     *
     *
     * 3.1、构造器注入 bean 引用
     *
     * 按照现在的定义，CDPlayer bean 有一个接受 CompactDisc 类型的构造器。这样，就有了一个很好的场景来学习如
     * 何注入 bean 的引用。
     *
     * 现在已经声明了 SgtPeppers bean，并且 SgtPeppers 类实现了 CompactDisc 接口，所以实际上已经有了一个可
     * 以注入到 CDPlayer bean 中的 bean。所需要做的就是在 XML 中声明 CDPlayer 并通过 ID 引用 SgtPeppers：
     *
     *     <bean id="cdPlayer" class="com.siwuxie095.spring.chapter2nd.example5th.CDPlayer">
     *         <constructor-arg ref="compactDisc"/>
     *     </bean>
     *
     * 当 Spring 遇到这个 <bean> 元素时，它会创建一个 CDPlayer 实例。<constructor-arg> 元素会告知 Spring
     * 要将一个 ID 为 compactDisc 的 bean 引用传递到 CDPlayer 的构造器中。
     *
     * 作为替代的方案，你也可以使用 Spring 的 c-命名空间。c-命名空间是在 Spring 3.0 中引入的，它是在 XML 中
     * 更为简洁地描述构造器参数的方式。要使用它的话，必须要在 XML 的顶部声明其模式，如下所示：
     *
     * xmlns:c="http://www.springframework.org/schema/c"
     *
     * 在 c-命名空间和模式声明之后，就可以使用它来声明构造器参数了，如下所示：
     *
     *     <bean id="cdPlayer" class="com.siwuxie095.spring.chapter2nd.example5th.CDPlayer"
     *           c:cd-ref="compactDisc"/>
     *
     * 在这里，使用了 c-命名空间来声明构造器参数，它作为 <bean> 元素的一个属性，不过这个属性的名字有点诡异。
     *
     * 属性名以 "c:" 开头，也就是命名空间的前缀。接下来就是要装配的构造器参数名，在此之后是 "-ref"，这是一个命
     * 名的约定，它会告诉 Spring，正在装配的是一个 bean 的引用，这个 bean 的名字是 compactDisc，而不是字面量
     * "compactDisc"。
     *
     * 很显然，使用 c-命名空间属性要比使用 <constructor-arg> 元素简练得多。同时也更易读。
     *
     * 关于 c-命名空间，有一件让人感到困扰的事情，就是它直接引用了构造器参数的名称。引用参数的名称看起来有些怪异，
     * 因为这需要在编译代码的时候，将调试标志（debug symbol）保存在类代码中。如果你优化构建过程，将调试标志移除
     * 掉，那么这种方式可能就无法正常执行了。
     *
     * 替代的方案是使用参数在整个参数列表中的位置信息：
     *
     *     <bean id="cdPlayer" class="com.siwuxie095.spring.chapter2nd.example5th.CDPlayer"
     *           c:_0-ref="compactDisc"/>
     *
     * 这个 c-命名空间属性看起来似乎比上一种方法更加怪异。将参数的名称替换成了 "0"，也就是参数的索引。因为在 XML
     * 中不允许数字作为属性的第一个字符，因此必须要添加一个下画线作为前缀。
     *
     * 使用索引来识别构造器参数感觉比使用名字更好一些。即便在构建的时候移除掉了调试标志，参数却会依然保持相同的顺序。
     * 如果有多个构造器参数的话，这当然是很有用处的。在这里因为只有一个构造器参数，所以还有另外一个方案 —— 根本不用
     * 去标示参数：
     *
     *     <bean id="cdPlayer" class="com.siwuxie095.spring.chapter2nd.example5th.CDPlayer"
     *           c:_-ref="compactDisc"/>
     *
     * PS：貌似在当前版本下，不支持这种形式。
     *
     * 到目前为止，这是最为奇特的一个 c-命名空间属性，这里没有参数索引或参数名。只有一个下画线，然后就是用 "-ref"
     * 来表明正在装配的是一个引用。
     *
     * 现在已经将引用装配到了其他的 bean 之中，接下来看一下如何将字面量值（literal value）装配到构造器之中。
     *
     *
     * 3.2、将字面量注入到构造器中
     *
     * 迄今为止，所做的 DI 通常指的都是类型的装配 —— 也就是将对象的引用装配到依赖于它们的其他对象之中 —— 而有时候，
     * 需要做的只是用一个字面量值来配置对象。以 BlankDisc 为例，它是 CompactDisc 的一个新实现，以此来阐述这一点。
     *
     * 在 SgtPeppers 中，唱片名称和艺术家的名字都是硬编码的，但是这个 CompactDisc 实现与之不同，它更加灵活。像
     * 现实中的空磁盘一样，它可以设置成任意你想要的艺术家和唱片名。现在，可以将已有的 SgtPeppers 替换为这个类：
     *
     *     <bean id="compactDisc" class="com.siwuxie095.spring.chapter2nd.example5th.BlankDisc">
     *         <constructor-arg value="Sgt. Pepper's Lonely Hearts Club Band"/>
     *         <constructor-arg value="The Beatles"/>
     *     </bean>
     *
     * 这里再次使用 <constructor-arg> 元素进行构造器参数的注入。但是这一次没有使用 "ref" 属性来引用其他的 bean，
     * 而是使用了 value 属性，通过该属性表明给定的值要以字面量的形式注入到构造器之中。
     *
     * 如果要使用 c-命名空间的话，这个例子又该是什么样子呢？第一种方案是引用构造器参数的名字：
     *
     *     <bean id="compactDisc" class="com.siwuxie095.spring.chapter2nd.example5th.BlankDisc"
     *           c:title="Sgt. Pepper's Lonely Hearts Club Band"
     *           c:artist="The Beatles"/>
     *
     * PS：原来的配置是 c:_title 和 c:_artist。貌似在当前版本下，不支持这种形式。
     *
     * 可以看到，装配字面量与装配引用的区别在于属性名中去掉了 "-ref" 后缀。与之类似，也可以通过参数索引装配相同的
     * 字面量值，如下所示：
     *
     *     <bean id="compactDisc" class="com.siwuxie095.spring.chapter2nd.example5th.BlankDisc"
     *           c:_0="Sgt. Pepper's Lonely Hearts Club Band"
     *           c:_1="The Beatles"/>
     *
     * XML不允许某个元素的多个属性具有相同的名字。因此，如果有两个或更多的构造器参数的话，不能简单地使用下画线进行
     * 标示。但是如果只有一个构造器参数的话，就可以这样做了。为了完整地展现该功能，假设 BlankDisc 只有一个构造器
     * 参数，这个参数接受唱片的名称。在这种情况下，可以在 Spring 中这样声明它：
     *
     *     <bean id="compactDisc" class="com.siwuxie095.spring.chapter2nd.example5th.BlankDisc"
     *           c:_="Sgt. Pepper's Lonely Hearts Club Band"/>
     *
     * PS：貌似在当前版本下，不支持这种形式。
     *
     * 在装配 bean 引用和字面量值方面，<constructor-arg> 和 c-命名空间的功能是相同的。但是有一种情况是
     * <constructor-arg> 能够实现，c-命名空间却无法做到的。接下来，看一下如何将集合装配到构造器参数中。
     *
     *
     * 3.3、装配集合
     *
     * 到现在为止，假设 CompactDisc 在定义时只包含了唱片名称和艺术家的名字。如果现实世界中的 CD 也是这样的话，
     * 那么在技术上就不会任何的进展。CD 之所以值得购买是因为它上面所承载的音乐。大多数的 CD 都会包含十多个磁道，
     * 每个磁道上包含一首歌。
     *
     * 如果使用 CompactDisc 为真正的 CD 建模，那么它也应该有磁道列表的概念。以 MultiBlankDisc 为例，其中包
     * 含了一个集合。
     *
     * 这个变更会对 Spring 如何配置 bean 产生影响，在声明 bean 的时候，必须要提供一个磁道列表。
     *
     * 最简单的办法是将列表设置为 null。因为它是一个构造器参数，所以必须要声明它，不过你可以采用如下的方式传递
     * null 给它：
     *
     *     <bean id="compactDisc" class="com.siwuxie095.spring.chapter2nd.example5th.MultiBlankDisc">
     *         <constructor-arg value="Sgt. Pepper's Lonely Hearts Club Band"/>
     *         <constructor-arg value="The Beatles"/>
     *         <constructor-arg><null/></constructor-arg>
     *     </bean>
     *
     * <null/> 元素所做的事情与你的期望是一样的：将 null 传递给构造器。这并不是解决问题的好办法，但在注入期它能
     * 正常执行。当调用 play() 方法时，你会遇到 NullPointerException 异常，因此这并不是理想的方案。
     *
     * 更好的解决方法是提供一个磁道名称的列表。要达到这一点，可以有多个可选方案。首先，可以使用 <list> 元素将其声
     * 明为一个列表：
     *
     *     <bean id="compactDisc" class="com.siwuxie095.spring.chapter2nd.example5th.MultiBlankDisc">
     *         <constructor-arg value="Sgt. Pepper's Lonely Hearts Club Band"/>
     *         <constructor-arg value="The Beatles"/>
     *         <constructor-arg>
     *             <list>
     *                 <value>Sgt. Pepper's Lonely Hearts Club Band</value>
     *                 <value>With a Little Help from My Friends</value>
     *                 <value>Lucy in the Sky with Diamonds</value>
     *                 <value>Getting Better</value>
     *                 <value>Fixing a Hole</value>
     *                 <!-- ...other tracks omitted for brevity.. -->
     *             </list>
     *         </constructor-arg>
     *     </bean>
     *
     * 其中，<list> 元素是 <constructor-arg> 的子元素，这表明一个包含值的列表将会传递到构造器中。其中，<value>
     * 元素用来指定列表中的每个元素。
     *
     * 与之类似，也可以使用 <ref> 元素替代 <value>，实现 bean 引用列表的装配。例如，假设你有一个 Discography 类，
     * 它的构造器如下所示：
     *
     * public Discography(String artist, List<CompactDisc> cds) {}
     *
     * 那么，你可以采取如下的方式配置 Discography bean：
     *
     *     <bean id="discography" class="com.siwuxie095.spring.chapter2nd.example5th.Discography">
     *         <constructor-arg value="The Beatles"/>
     *         <constructor-arg>
     *             <list>
     *                 <ref bean="sgtPeppers"/>
     *                 <ref bean="whiteAlbum"/>
     *                 <ref bean="hardDaysNight"/>
     *                 <ref bean="revolver"/>
     *             </list>
     *         </constructor-arg>
     *     </bean>
     *
     * 当构造器参数的类型是 java.util.List 时，使用 <list> 元素是合情合理的。尽管如此，也可以按照同样的方式使用
     * <set> 元素：
     *
     *     <bean id="compactDisc" class="com.siwuxie095.spring.chapter2nd.example5th.MultiBlankDisc">
     *         <constructor-arg value="Sgt. Pepper's Lonely Hearts Club Band"/>
     *         <constructor-arg value="The Beatles"/>
     *         <constructor-arg>
     *             <set>
     *                 <value>Sgt. Pepper's Lonely Hearts Club Band</value>
     *                 <value>With a Little Help from My Friends</value>
     *                 <value>Lucy in the Sky with Diamonds</value>
     *                 <value>Getting Better</value>
     *                 <value>Fixing a Hole</value>
     *                 <!-- ...other tracks omitted for brevity... -->
     *             </set>
     *         </constructor-arg>
     *     </bean>
     *
     * <set> 和 <list> 元素的区别不大，其中最重要的不同在于当 Spring 创建要装配的集合时，所创建的是 java.util.Set
     * 还是 java.util.List。如果是 Set 的话，所有重复的值都会被忽略掉，存放顺序也不会得以保证。不过无论在哪种情况下，
     * <set> 或 <list> 都可以用来装配 List、Set 甚至数组。
     *
     * 在装配集合方面，<constructor-arg> 比 c-命名空间的属性更有优势。目前，使用 c-命名空间的属性无法实现装配集合的
     * 功能。
     *
     * 使用 <constructor-arg> 和 c-命名空间实现构造器注入时，它们之间还有一些细微的差别。但是到目前为止，所涵盖的内
     * 容已经足够了。
     *
     *
     *
     * 4、设置属性
     *
     *到目前为止，CDPlayer 和 BlankDisc 类完全是通过构造器注入的，没有使用属性的 Setter 方法。接下来，就看一下如何
     * 使用 Spring XML 实现属性注入。以 DVDPlayer 和 MultiBlankDisc 为例，进行说明。
     *
     * 该选择构造器注入还是属性注入呢？作为一个通用的规则，倾向于对强依赖使用构造器注入，而对可选性的依赖使用属性注入。
     *
     * 按照这个规则，可以说对于 MultiBlankDisc 来讲，唱片名称、艺术家以及磁道列表是强依赖，因此构造器注入是正确的方案。
     * 不过，对于 DVDPlayer 来讲，它对 CompactDisc 是强依赖还是可选性依赖可能会有些争议。虽然这里不太认同，但你可能
     * 会觉得即便没有将 CompactDisc 装入进去，DVDPlayer 依然还能具备一些有限的功能。
     *
     * 现在，DVDPlayer 没有任何的构造器（除了隐含的默认构造器），它也没有任何的强依赖。因此，你可以采用如下的方式将其
     * 声明为 Spring bean：
     *
     *     <bean id="dvdPlayer" class="com.siwuxie095.spring.chapter2nd.example5th.DVDPlayer"/>
     *
     * Spring 在创建 bean 的时候不会有任何的问题，但是后续会因为出现 NullPointerException 而导致测试失败，因为这里
     * 并没有注入 DVDPlayer 的 cd 属性。不过，按照如下的方式修改 XML，就能解决该问题：
     *
     *     <bean id="dvdPlayer" class="com.siwuxie095.spring.chapter2nd.example5th.DVDPlayer">
     *         <property name="cd" ref="compactDisc"/>
     *     </bean>
     *
     * <property> 元素为属性的 Setter 方法所提供的功能与 <constructor-arg> 元素为构造器所提供的功能是一样的。在本
     * 例中，它引用了 ID 为 compactDisc 的 bean（通过 ref 属性），并将其注入到 cd 属性中（通过setCd()方法）。这样
     * 就不会出现 NullPointerException 了。
     *
     * 已经知道，Spring 为 <constructor-arg> 元素提供了 c-命名空间作为替代方案，与之类似，Spring 提供了更加简洁的
     * p-命名空间，作为 <property> 元素的替代方案。为了启用 p-命名空间，必须要在 XML 文件中与其他的命名空间一起对其
     * 进行声明：
     *
     * xmlns:p="http://www.springframework.org/schema/p"
     *
     * 可以使用 p-命名空间，按照以下的方式装配 cd 属性：
     *
     *     <bean id="dvdPlayer" class="com.siwuxie095.spring.chapter2nd.example5th.DVDPlayer"
     *           p:cd-ref="compactDisc"/>
     *
     * p-命名空间中属性所遵循的命名约定与 c-命名空间中的属性类似。
     *
     * 首先，属性的名字使用了 "p:" 前缀，表明所设置的是一个属性。接下来就是要注入的属性名。最后，属性的名称以 "-ref"
     * 结尾，这会提示 Spring 要进行装配的是引用，而不是字面量。
     *
     *
     * 4.1、将字面量注入到属性中
     *
     * 属性也可以注入字面量，这与构造器参数非常类似。作为示例，重新看一下 MultiBlankDisc bean。不过，MultiBlankDisc
     * 这次完全通过属性注入进行配置，而不是构造器注入。
     *
     * 现在，它不再强制要求我们装配任何的属性。你可以按照如下的方式创建一个 BlankDisc bean，它的所有属性全都是空的：
     *
     *     <bean id="compactDisc" class="com.siwuxie095.spring.chapter2nd.example5th.MultiBlankDisc"/>
     *
     * 当然，如果在装配 bean 的时候不设置这些属性，那么在运行期 DVD 播放器将不能正常播放内容。play() 方法可能会遇到的
     * 输出内容是 "Playing null by null"，随之会抛出 NullPointerException 异常，这是因为没有指定任何的磁道。所以，
     * 需要装配这些属性，可以借助 <property> 元素的 value 属性实现该功能：
     *
     *     <bean id="compactDisc" class="com.siwuxie095.spring.chapter2nd.example5th.MultiBlankDisc">
     *         <property name="title" value="Sgt. Pepper's Lonely Hearts Club Band"/>
     *         <property name="artist" value="The Beatles"/>
     *         <property name="tracks">
     *         <list>
     *             <value>Sgt. Pepper's Lonely Hearts Club Band</value>
     *             <value>With a Little Help from My Friends</value>
     *             <value>Lucy in the Sky with Diamonds</value>
     *             <value>Getting Better</value>
     *             <value>Fixing a Hole</value>
     *             <!-- ...other tracks omitted for brevity... -->
     *         </list>
     *         </property>
     *     </bean>
     *
     * 在这里，除了使用 <property> 元素的 value 属性来设置 title 和 artist，还使用了内嵌的 <list> 元素来设置
     * tracks 属性，这与之前通过 <constructor-arg> 装配 tracks 是完全一样的。
     *
     * 另外一种可选方案就是使用p-命名空间的属性来完成该功能：
     *
     *     <bean id="compactDisc" class="com.siwuxie095.spring.chapter2nd.example5th.MultiBlankDisc"
     *         p:title="Sgt. Pepper's Lonely Hearts Club Band"
     *         p:artist="The Beatles">
     *         <property name="tracks">
     *             <list>
     *                 <value>Sgt. Pepper's Lonely Hearts Club Band</value>
     *                 <value>With a Little Help from My Friends</value>
     *                 <value>Lucy in the Sky with Diamonds</value>
     *                 <value>Getting Better</value>
     *                 <value>Fixing a Hole</value>
     *                 <!-- ...other tracks omitted for brevity... -->
     *             </list>
     *         </property>
     *     </bean>
     *
     * 与 c-命名空间一样，装配 bean 引用与装配字面量的唯一区别在于是否带有 "-ref" 后缀。如果没有 "-ref" 后缀的话，
     * 所装配的就是字面量。
     *
     * 但需要注意的是，不能使用 p-命名空间来装配集合，没有便利的方式使用 p-命名空间来指定一个值（或 bean 引用）的列表。
     * 但是，可以使用 Spring util-命名空间中的一些功能来简化 MultiBlankDisc bean。
     *
     * 首先，需要在 XML 中声明 util-命名空间及其模式：
     *
     * xmlns:util="http://www.springframework.org/schema/util"
     *
     * util-命名空间所提供的功能之一就是 <util:list> 元素，它会创建一个列表的 bean。借助 <util:list>，可以将磁道
     * 列表转移到 MultiBlankDisc bean 之外，并将其声明到单独的 bean 之中，如下所示：
     *
     *     <util:list id="trackList">
     *         <value>Sgt. Pepper's Lonely Hearts Club Band</value>
     *         <value>With a Little Help from My Friends</value>
     *         <value>Lucy in the Sky with Diamonds</value>
     *         <value>Getting Better</value>
     *         <value>Fixing a Hole</value>
     *         <!-- ...other tracks omitted for brevity... -->
     *     </util:list>
     *
     * 现在，就能够像使用其他的 bean 那样，将磁道列表 bean 注入到 MultiBlankDisc bean 的 tracks 属性中：
     *
     *     <bean id="compactDisc" class="com.siwuxie095.spring.chapter2nd.example5th.MultiBlankDisc"
     *           p:title="Sgt. Pepper's Lonely Hearts Club Band"
     *           p:artist="The Beatles"
     *           p:tracks-ref="trackList">
     *     </bean>
     *
     * <util:list> 只是 util-命名空间中的多个元素之一。如下列出了 util-命名空间提供的所有元素。
     * （1）<util:constant>
     *     引用某个类型的 public static 域，并将其暴露为 bean。
     * （2）<util:list>
     *     创建一个 java.util.List 类型的 bean，其中包含值或引用。
     * （3）<util:map>
     *     创建一个 java.util.Map 类型的 bean，其中包含值或引用。
     * （4）<util:properties>
     *     创建一个 java.util.Properties 类型的 bean。
     * （5）<util:property-path>
     *     引用一个 bean 的属性（或内嵌属性），将其暴露为 bean。
     * （6）<util:set>
     *     创建一个 java.util.Set 类型的 bean，其中包含值或引用。
     *
     * 在需要的时候，你可能会用到 util-命名空间中的部分成员。
     */
    public static void main(String[] args) {

    }

}
