package com.siwuxie095.spring.chapter3rd.example6th;

/**
 * @author Jiajing Li
 * @date 2021-01-06 08:29:56
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 处理自动装配的歧义性
     *
     * 自动装配能够让 Spring 完全负责将 bean 引用注入到构造参数和属性中。自动装配能够提供很大的帮助，因为它会减少装配
     * 应用程序组件时所需要的显式配置的数量。
     *
     * 不过，仅有一个 bean 匹配所需的结果时，自动装配才是有效的。如果不仅有一个 bean 能够匹配结果的话，这种歧义性会阻
     * 碍 Spring 自动装配属性、构造器参数或方法参数。
     *
     * 为了阐述自动装配的歧义性，假设使用 @Autowired 注解标注了 setDessert() 方法：
     *
     *     @Autowired
     *     public void setDessert(Dessert dessert) {
     *         this.dessert = dessert;
     *     }
     *
     * 在本例中，Dessert 是一个接口，并且有三个类实现了这个接口，分别为 Cake、Cookies 和 IceCream。
     *
     * 因为这三个实现均使用了 @Component 注解，在组件扫描的时候，能够发现它们并将其创建为 Spring 应用上下文里面的
     * bean。然后，当 Spring 试图自动装配 setDessert() 中的 Dessert 参数时，它并没有唯一、无歧义的可选值。在从
     * 多种甜点中做出选择时，尽管大多数人并不会有什么困难，但是 Spring 却无法做出选择。Spring 此时别无他法，只好宣告
     * 失败并抛出异常。更精确地讲，Spring 会抛出 NoUniqueBeanDefinitionException。
     *
     * 当然，使用吃甜点的样例来阐述自动装配在遇到歧义性时所面临的问题多少有些牵强。在实际中，自动装配歧义性的问题其实比
     * 你想象中的更为罕见。就算这种歧义性确实是个问题，但更常见的情况是给定的类型只有一个实现类，因此自动装配能够很好地
     * 运行。
     *
     * 但是，当确实发生歧义性的时候，Spring 提供了多种可选方案来解决这样的问题。你可以将可选 bean 中的某一个设为首选
     * （primary）的 bean，或者使用限定符（qualifier）来帮助 Spring 将可选的 bean 的范围缩小到只有一个 bean。
     *
     *
     *
     * 1、标示首选的 bean
     *
     * 在声明 bean 的时候，通过将其中一个可选的 bean 设置为首选（primary）bean 能够避免自动装配时的歧义性。当遇到歧义
     * 性的时候，Spring 将会使用首选的 bean，而不是其他可选的 bean。实际上，你所声明就是 "最喜欢" 的 bean。
     *
     * 假设冰激凌就是你最喜欢的甜点。在 Spring 中，可以通过 @Primary 来表达最喜欢的方案。@Primary 能够与 @Component
     * 组合用在组件扫描的 bean 上，也可以与 @Bean 组合用在 Java 配置的 bean 声明中。比如，下面的代码展现了如何将
     * @Component 注解的 IceCream bean 声明为首选的 bean：
     *
     * @Component
     * @Primary
     * public class IceCream implements Dessert {
     *
     * }
     *
     * 或者，如果你通过 Java 配置显式地声明 IceCream，那么 @Bean 方法应该如下所示：
     *
     *     @Bean
     *     @Primary
     *     public Dessert iceCream() {
     *         return new IceCream();
     *     }
     *
     * 如果你使用 XML 配置 bean 的话，同样可以实现这样的功能。<bean> 元素有一个 primary 属性用来指定首选的 bean：
     *
     *     <bean id="iceCream"
     *           class="com.siwuxie095.spring.chapter3rd.example6th.IceCream"
     *           primary="true"/>
     *
     * 不管你采用什么方式来标示首选 bean，效果都是一样的，都是告诉 Spring 在遇到歧义性的时候要选择首选的 bean。
     *
     * 但是，如果你标示了两个或更多的首选 bean，那么它就无法正常工作了。比如，假设 Cake 类如下所示：
     *
     * @Component
     * @Primary
     * public class Cake implements Dessert {
     *
     * }
     *
     * 现在，有两个首选的 Dessert bean：Cake 和 IceCream。这带来了新的歧义性问题。就像 Spring 无法从多个可选的 bean
     * 中做出选择一样，它也无法从多个首选的 bean 中做出选择。显然，如果不止一个 bean 被设置成了首选 bean，那实际上也就是
     * 没有首选 bean 了。
     *
     * 就解决歧义性问题而言，限定符是一种更为强大的机制，下面将对其进行介绍。
     *
     *
     *
     * 2、限定自动装配的 bean
     *
     * 设置首选 bean 的局限性在于 @Primary 无法将可选方案的范围限定到唯一一个无歧义性的选项中。它只能标示一个优先的可选
     * 方案。当首选 bean 的数量超过一个时，并没有其他的方法进一步缩小可选范围。
     *
     * 与之相反，Spring 的限定符能够在所有可选的 bean 上进行缩小范围的操作，最终能够达到只有一个 bean 满足所规定的限制
     * 条件。如果将所有的限定符都用上后依然存在歧义性，那么你可以继续使用更多的限定符来缩小选择范围。
     *
     * @Qualifier 注解是使用限定符的主要方式。它可以与 @Autowired 和 @Inject 协同使用，在注入的时候指定想要注入进去
     * 的是哪个 bean。例如，想要确保要将 IceCream 注入到 setDessert() 之中：
     *
     *     @Autowired
     *     @Qualifier("iceCream")
     *     public void setDessert(Dessert dessert) {
     *         this.dessert = dessert;
     *     }
     *
     * 这是使用限定符的最简单的例子。为 @Qualifier 注解所设置的参数就是想要注入的 bean 的 ID。所有使用 @Component
     * 注解声明的类都会创建为 bean，并且 bean 的 ID 为首字母变为小写的类名。因此，@Qualifier("iceCream") 指向的
     * 是组件扫描时所创建的 bean，并且这个 bean 是 IceCream 类的实例。
     *
     * 实际上，更准确地讲，@Qualifier("iceCream") 所引用的 bean 要具有 String 类型的 "iceCream" 作为限定符。如果
     * 没有指定其他的限定符的话，所有的 bean 都会给定一个默认的限定符，这个限定符与 bean 的 ID 相同。因此，框架会将具有
     * "iceCream" 限定符的 bean 注入到 setDessert() 方法中。这恰巧就是 ID 为 iceCream 的 bean，它是 IceCream 类
     * 在组件扫描的时候创建的。
     *
     * 基于默认的 bean ID 作为限定符是非常简单的，但这有可能会引入一些问题。如果你重构了 IceCream 类，将其重命名为 Gelato
     * 的话，那此时会发生什么情况呢？如果这样的话，bean 的 ID 和默认的限定符会变为 gelato，这就无法匹配setDessert() 方法
     * 中的限定符，自动装配会失败。
     *
     * 这里的问题在于 setDessert() 方法上所指定的限定符与要注入的 bean 的名称是紧耦合的。对类名称的任意改动都会导致限定符
     * 失效。
     *
     *
     * 2.1、创建自定义的限定符
     *
     * 可以为 bean 设置自己的限定符，而不是依赖于将 bean ID 作为限定符。在这里所需要做的就是在 bean 声明上添加 @Qualifier
     * 注解。例如，它可以与 @Component 组合使用，如下所示：
     *
     * @Component
     * @Qualifier("cold")
     * public class IceCream implements Dessert {
     *
     * }
     *
     * 在这种情况下，cold 限定符分配给了 IceCream bean。因为它没有耦合类名，因此你可以随意重构 IceCream 的类名，而不必
     * 担心会破坏自动装配。在注入的地方，只要引用 cold 限定符就可以了：
     *
     *     @Autowired
     *     @Qualifier("cold")
     *     public void setDessert(Dessert dessert) {
     *         this.dessert = dessert;
     *     }
     *
     * 值得一提的是，当通过 Java 配置显式定义 bean 的时候，@Qualifier 也可以与 @Bean 注解一起使用：
     *
     *     @Bean
     *     @Qualifier("cold")
     *     public Dessert iceCream() {
     *         return new IceCream();
     *     }
     *
     * 当使用自定义的 @Qualifier 值时，最佳实践是为 bean 选择特征性或描述性的术语，而不是使用随意的名字。在本例中，将
     * IceCream bean 描述为 "cold" bean。在注入的时候，可以将这个需求理解为 "给我一个凉的甜点"，这其实就是描述的
     * IceCream。类似地，可以将 Cake 描述为 "soft"，将 Cookie 描述为 "crispy"。
     *
     *
     * 2.2、使用自定义的限定符注解
     *
     * 面向特性的限定符要比基于 bean ID 的限定符更好一些。但是，如果多个 bean 都具备相同特性的话，这种做法也会出现问题。
     * 例如，如果引入了这个新的 Dessert bean，会发生什么情况呢：
     *
     * @Component
     * @Qualifier("cold")
     * public class Popsicle implements Dessert {
     *
     * }
     *
     * 现在有了两个带有 "cold" 限定符的甜点。在自动装配 Dessert bean 的时候，再次遇到了歧义性的问题，需要使用更多的限
     * 定符来将可选范围限定到只有一个 bean。
     *
     * 可能想到的解决方案就是在注入点和 bean 定义的地方同时再添加另外一个 @Qualifier 注解。此时，IceCream 类大致就会
     * 如下所示：
     *
     * @Component
     * @Qualifier("cold")
     * @Qualifier("creamy")
     * public class IceCream implements Dessert {
     *
     * }
     *
     * Popsicle 类同样也可能再添加另外一个 @Qualifier 注解：
     *
     * @Component
     * @Qualifier("cold")
     * @Qualifier("fruity")
     * public class Popsicle implements Dessert {
     *
     * }
     *
     * 在注入点中，可能会使用这样的方式来将范围缩小到 IceCream：
     *
     *     @Autowired
     *     @Qualifier("cold")
     *     @Qualifier("creamy")
     *     public void setDessert(Dessert dessert) {
     *         this.dessert = dessert;
     *     }
     *
     * 这里只有一个小问题：Java 不允许在同一个条目上重复出现相同类型的多个注解。如果你试图这样做的话，编译器会提示错误。
     * 在这里，使用 @Qualifier 注解并没有办法（至少没有直接的办法）将自动装配的可选 bean 缩小范围至仅有一个可选的 bean。
     *
     * PS：Java 8 允许出现重复的注解，只要这个注解本身在定义的时候带有 @Repeatable 注解就可以。不过，Spring 的
     * @Qualifier 注解并没有在定义时添加 @Repeatable 注解。
     *
     * 但是，可以创建自定义的限定符注解，借助这样的注解来表达 bean 所希望限定的特性。这里所需要做的就是创建一个注解，它
     * 本身要使用 @Qualifier 注解来标注。这样将不再使用 @Qualifier("cold")，而是使用自定义的 @Cold 注解，该注解的
     * 定义如下所示：
     *
     * @Target({ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
     * @Retention(RetentionPolicy.RUNTIME)
     * @Qualifier
     * public @interface Cold {
     *
     * }
     *
     * 同样，你可以创建一个新的 @Creamy 注解来代替 @Qualifier("creamy")：
     *
     * @Target({ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
     * @Retention(RetentionPolicy.RUNTIME)
     * @Qualifier
     * public @interface Creamy {
     *
     * }
     *
     * 当你不想用 @Qualifier 注解的时候，可以类似地创建 @Soft、@Crispy 和 @Fruity。通过在定义时添加 @Qualifier
     * 注解，它们就具有了 @Qualifier 注解的特性。它们本身实际上就成为了限定符注解。
     *
     * 现在，可以重新看一下 IceCream，并为其添加 @Cold 和 @Creamy 注解，如下所示：
     *
     * @Component
     * @Cold
     * @Creamy
     * public class IceCream implements Dessert {
     *
     * }
     *
     * 类似地，Popsicle 类可以添加 @Cold 和 @Fruity 注解：
     *
     * @Component
     * @Cold
     * @Fruity
     * public class Popsicle implements Dessert {
     *
     * }
     *
     * 最终，在注入点，使用必要的限定符注解进行任意组合，从而将可选范围缩小到只有一个 bean 满足需求。为了得到 IceCream
     * bean，setDessert() 方法可以这样使用注解：
     *
     *     @Autowired
     *     @Cold
     *     @Creamy
     *     public void setDessert(Dessert dessert) {
     *         this.dessert = dessert;
     *     }
     *
     * 通过声明自定义的限定符注解，可以同时使用多个限定符，不会再有 Java 编译器的限制或错误。与此同时，相对于使用原始的
     * @Qualifier 并借助 String 类型来指定限定符，自定义的注解也更为类型安全。
     *
     * 不妨近距离观察一下 setDessert() 方法以及它的注解，这里并没有在任何地方明确指定要将 IceCream 自动装配到该方法中。
     * 相反，这里使用所需 bean 的特性来进行指定，即 @Cold 和 @Creamy。因此，setDessert() 方法依然能够与特定的 Dessert
     * 实现保持解耦。任意满足这些特征的 bean 都是可以的。在当前选择 Dessert 实现时，恰好 IceCream 是唯一能够与之匹配的
     * bean。
     */
    public static void main(String[] args) {

    }

}
