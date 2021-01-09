package com.siwuxie095.spring.chapter3rd.example10th;

/**
 * @author Jiajing Li
 * @date 2021-01-09 15:08:05
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 使用 Spring 表达式语言进行装配
     *
     * Spring 3 引入了 Spring 表达式语言（Spring Expression Language，SpEL），它能够以一种强大和简洁的方式将值装配
     * 到 bean 属性和构造器参数中，在这个过程中所使用的表达式会在运行时计算得到值。使用 SpEL，你可以实现超乎想象的装配效果，
     * 这是使用其他的装配技术难以做到的（甚至是不可能的）。
     *
     * SpEL 拥有很多特性，包括：
     * （1）使用 bean 的 ID 来引用 bean；
     * （2）调用方法和访问对象的属性；
     * （3）对值进行算术、关系和逻辑运算；
     * （4）正则表达式匹配；
     * （5）集合操作。
     *
     * SpEL能够用在依赖注入以外的其他地方。例如，Spring Security 支持使用 SpEL 表达式定义安全限制规则。另外，如果你在
     * Spring MVC 应用中使用 Thymeleaf 模板作为视图的话，那么这些模板可以使用 SpEL 表达式引用模型数据。
     *
     * 作为起步，先看几个 SpEL 表达式的样例，以及如何将其注入到 bean 中。然后会深入学习一些 SpEL 的基础表达式，它们能够
     * 组合起来形成更为强大的表达式。
     *
     *
     *
     * 1、SpEL 样例
     *
     * SpEL 是一种非常灵活的表达式语言，所以这里不可能面面俱到地介绍它的各种用法。但是可以展示几个基本的例子，这些例子会激发
     * 你的灵感，有助于你编写自己的表达式。
     *
     * 需要了解的第一件事情就是 SpEL 表达式要放到 "#{ ... }" 之中，这与属性占位符有些类似，属性占位符需要放到 "${ ... }"
     * 之中。下面所展现的可能是最简单的 SpEL 表达式了：
     *
     * #{1}
     *
     * 除去 "#{ ... }" 标记之后，剩下的就是 SpEL 表达式体了，也就是一个数字常量。这个表达式的计算结果就是数字 1，这恐怕并
     * 不会让你感到丝毫惊讶。
     *
     * 当然，在实际的应用程序中，可能并不会使用这么简单的表达式。可能会使用更加有意思的表达式，如：
     *
     * #{T(System).currentTimeMillis()}
     *
     * 它的最终结果是计算表达式的那一刻当前时间的毫秒数。T() 表达式会将 java.lang.System 视为 Java 中对应的类型，因此可
     * 以调用其 static 修饰的 currentTimeMillis() 方法。
     *
     * SpEL 表达式也可以引用其他的 bean 或其他 bean 的属性。例如，如下的表达式会计算得到 ID 为 sgtPeppers 的 bean 的
     * artist 属性：
     *
     * #{sgtPeppers.artist}
     *
     * 还可以通过 systemProperties 对象引用系统属性：
     *
     * #{systemProperties['disc.title']}
     *
     * 这只是 SpEL 的几个基础样例。下面看一下在 bean 装配的时候如何使用这些表达式。
     *
     * 如果通过组件扫描创建 bean 的话，在注入属性和构造器参数时，可以使用 @Value 注解，这与之前看到的属性占位符非常类似。
     * 不过，在这里所使用的不是占位符表达式，而是 SpEL 表达式。例如，下面的样例展现了 BlankDisc，它会从系统属性中获取专辑
     * 名称和艺术家的名字：
     *
     *     public BlankDisc(
     *             @Value("#{systemProperties['disc.title']}") String title,
     *             @Value("#{systemProperties['disc.artist']}") String artist) {
     *         this.title = title;
     *         this.artist = artist;
     *     }
     *
     * 在 XML 配置中，你可以将 SpEL 表达式传入 <property> 或 <constructor-arg> 的 value 属性中，或者将其作为 p-命名
     * 空间或 c-命名空间条目的值。例如，在如下 BlankDisc bean 的 XML 声明中，构造器参数就是通过 SpEL 表达式设置的：
     *
     *     <bean id="disc"
     *           class="com.siwuxie095.spring.chapter3rd.example10th.BlankDisc"
     *           c:_0="#{systemProperties['disc.title']}"
     *           c:_1="#{systemProperties['disc.artist']}">
     *     </bean>
     *
     * 已经看过了几个简单的样例，也学习了如何将 SpEL 解析得到的值注入到 bean 中，那现在就来继续学习一下 SpEL 所支持的基础
     * 表达式吧。
     *
     *
     *
     * 2、表示字面值
     *
     * 在前面已经看到了一个使用 SpEL 来表示整数字面量的样例。它实际上还可以用来表示浮点数、String 值以及 Boolean 值。
     *
     * 下面的 SpEL 表达式样例所表示的就是浮点值：
     *
     * #{3.14159}
     *
     * 数值还可以使用科学记数法的方式进行表示。如下面的表达式计算得到的值就是 98,700：
     *
     * #{9.87E4}
     *
     * SpEL 表达式也可以用来计算 String 类型的字面值，如：
     *
     * #{'Hello'}
     *
     * 最后，字面值 true 和 false 的计算结果就是它们对应的 Boolean 类型的值。例如：
     *
     * #{false}
     *
     * 在 SpEL 中使用字面值其实没有太大的意思，毕竟将整型属性设置为 1，或者将 Boolean 属性设置为 false 时，并不需要使用
     * SpEL。不得不承认在 SpEL 表达式中，只包含字面值情况并没有太大的用处。但需要记住的一点是，更有意思的 SpEL 表达式是由
     * 更简单的表达式组成的，因此了解在 SpEL 中如何使用字面量还是很有用处的。当组合更为复杂的表达式时，你迟早会用到它们。
     *
     *
     *
     * 3、引用 bean、属性和方法
     *
     * SpEL 所能做的另外一件基础的事情就是通过 ID 引用其他的 bean。例如，你可以使用 SpEL 将一个 bean 装配到另外一个 bean
     * 的属性中，此时要使用 bean ID 作为 SpEL 表达式，如下：
     *
     * #{sgtPeppers}
     *
     * 假设现在想在一个表达式中引用 sgtPeppers 的 artist 属性：
     *
     * #{sgtPeppers.artist}
     *
     * 表达式主体的第一部分引用了一个 ID 为 sgtPeppers 的 bean，分割符之后是对 artist 属性的引用。
     *
     * 除了引用 bean 的属性，还可以调用 bean 上的方法。例如，假设有另外一个 bean，它的 ID 为 artistSelector，可以在
     * SpEL 表达式中按照如下的方式来调用 bean 的 selectArtist() 方法：
     *
     * #{artistSelector.selectArtist()}
     *
     * 对于被调用方法的返回值来说，同样可以调用它的方法。例如，如果 selectArtist() 方法返回的是一个 String，那么可以
     * 调用 toUpperCase() 将整个艺术家的名字改为大写字母形式：
     *
     * #{artistSelector.selectArtist().toUpperCase()}
     *
     * 如果 selectArtist() 的返回值不是 null 的话，这没有什么问题。为了避免出现 NullPointerException，可以使用类型
     * 安全的运算符：
     *
     * #{artistSelector.selectArtist()?.toUpperCase()}
     *
     * 与之前只是使用点号(.)来访问 toUpperCase() 方法不同，现在使用了问号点号(?.)运算符。这个运算符能够在访问它右边的
     * 内容之前，确保它所对应的元素不是 null。所以，如果 selectArtist() 的返回值是 null 的话，那么 SpEL 将不会调用
     * toUpperCase() 方法。表达式的返回值会是 null。
     *
     *
     *
     * 4、在表达式中使用类型
     *
     * 如果要在 SpEL 中访问类作用域的方法和常量的话，要依赖 T() 这个关键的运算符。例如，为了在 SpEL 中表达 Java 的
     * Math 类，需要按照如下的方式使用 T() 运算符：
     *
     * T(java.lang.Math)
     *
     * 这里所示的 T() 运算符的结果会是一个 Class 对象，代表了 java.lang.Math。如果需要的话，甚至可以将其装配到一个
     * Class 类型的 bean 属性中。但是 T() 运算符的真正价值在于它能够访问目标类型的静态方法和常量。
     *
     * 例如，假如你需要将 PI 值装配到 bean 属性中。如下的 SpEL 就能完成该任务：
     *
     * T(java.lang.Math).PI
     *
     * 与之类似，可以调用 T() 运算符所得到类型的静态方法。已经看到了通过 T() 调用 System.currentTimeMillis()。如下
     * 的这个样例会计算得到一个 0 到 1 之间的随机数：
     *
     * T(java.lang.Math).random()
     *
     *
     *
     * 5、SpEL 运算符
     *
     * SpEL 提供了多个运算符，这些运算符可以用在 SpEL 表达式的值上。
     *
     * 如下是用来操作表达式值的 SpEL 运算符（运算符类型和运算符）：
     *
     * （1）算术运算：+、-、 * 、/、%、^
     * （2）比较运算：<、>、==、<=、>=、lt、gt、eq、le、ge
     * （3）逻辑运算：and、or、not、│
     * （4）条件运算：?: (ternary) 、 ?: (Elvis)
     * （5）正则表达式：matches
     *
     * 作为使用上述运算符的一个简单样例，看一下下面这个 SpEL 表达式：
     *
     * #{2 * T(java.lang.Math).PI * circle.radius}
     *
     * 这不仅是使用 SpEL 中乘法运算符(*)的绝佳样例，它也为你展现了如何将简单的表达式组合为更为复杂的表达式。在这里 PI
     * 的值乘以 2，然后再乘以 radius 属性的值，这个属性来源于 ID 为 circle 的 bean。实际上，它计算了 circle bean
     * 中所定义圆的周长。
     *
     * 类似地，你还可以在表达式中使用乘方运算符(^)来计算圆的面积：
     *
     * #{T(java.lang.Math).PI * circle.radius ^ 2}
     *
     * "^" 是用于乘方计算的运算符。在本例中，使用它来计算圆半径的平方。
     *
     * 当使用 String 类型的值时，"+" 运算符执行的是连接操作，与在 Java 中是一样的：
     *
     * #{disc.title + 'by' + disc.artist}
     *
     * SpEL 同时还提供了比较运算符，用来在表达式中对值进行对比。比较运算符有两种形式：符号形式和文本形式。在大多数情况下，
     * 符号运算符与对应的文本运算符作用是相同的，使用哪一种形式均可以。
     *
     * 例如，要比较两个数字是不是相等，可以使用双等号运算符(==)：
     *
     * #{counter.total == 100}
     *
     * 或者，也可以使用文本型的 eq 运算符：
     *
     * #{counter.total eq 100}
     *
     * 两种方式的结果都是一样的。表达式的计算结果是个 Boolean 值：如果 counter.total 等于 100，为 true，否则为 false。
     *
     * SpEL 还提供了三元运算符(ternary)，它与 Java 中的三元运算符非常类似。
     *
     * 例如，如下的表达式会判断如果 scoreboard.score > 1000 的话，计算结果为 String 类型的 "Winner"，否则的话，结果为
     * "Loser"：
     *
     * #{scoreboard.score > 1000 ? "Winner" : "Loser"}
     *
     * 三元运算符的一个常见场景就是检查 null 值，并用一个默认值来替代 null。例如，如下的表达式会判断 disc.title 的值是不
     * 是 null，如果是 null 的话，那么表达式的计算结果就会是 "Rattle and Hum"：
     *
     * #{disc.title ?: 'Rattle and Hum'}
     *
     * 这种表达式通常称为 Elvis 运算符。这个奇怪名称的来历是，当使用符号来表示表情时，问号看起来很像是猫王（Elvis Presley）
     * 的头发。
     *
     *
     *
     * 6、计算正则表达式
     *
     * 当处理文本时，有时检查文本是否匹配某种模式是非常有用的。SpEL 通过 matches 运算符支持表达式中的模式匹配。matches 运
     * 算符对 String 类型的文本（作为左边参数）应用正则表达式（作为右边参数）。matches 的运算结果会返回一个 Boolean 类型
     * 的值：如果与正则表达式相匹配，则返回 true，否则返回 false。
     *
     * 为了进一步解释 matches 运算符，假设想判断一个字符串是否包含有效的邮件地址。在这个场景下，可以使用 matches 运算符，
     * 如下所示：
     *
     * #{admin.email matches '[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com'}
     *
     * 探寻正则表达式语法的秘密超出了这里所讨论的范围，同时也应该意识到这里的正则表达式还不足够健壮来涵盖所有的场景。但对于演
     * 示 matches 运算符的用法，这已经足够了。
     *
     *
     *
     * 7、计算集合
     *
     * SpEL 中最令人惊奇的一些技巧是与集合和数组相关的。最简单的事情可能就是引用列表中的一个元素了：
     *
     * #{jukebox.songs[4].title}
     *
     * 这个表达式会计算 songs 集合中第五个（基于零开始）元素的 title 属性，这个集合来源于 ID 为 jukebox bean。
     *
     * 为了让这个表达式更丰富一些，假设要从 jukebox 中随机选择一首歌：
     *
     * #{jukebox.songs[T(java.lang.Math).random() * jukebox.songs.size()].title}
     *
     * "[]" 运算符用来从集合或数组中按照索引获取元素，实际上，它还可以从 String 中获取一个字符。比如：
     *
     * #{'This is a test' [3]}
     *
     * 这个表达式引用了 String 中的第四个（基于零开始）字符，也就是 "s"。
     *
     * SpEL 还提供了查询运算符(.?[])，它会用来对集合进行过滤，得到集合的一个子集。作为阐述的样例，假设你希望得到 jukebox
     * 中 artist 属性为 Aerosmith 的所有歌曲。如下的表达式就使用查询运算符得到了 Aerosmith 的所有歌曲：
     *
     * #{jukebox.songs.?[artist eq 'Aerosmith']}
     *
     * 可以看到，选择运算符在它的方括号中接受另一个表达式。当 SpEL 迭代歌曲列表的时候，会对歌曲集合中的每一个条目计算这个表
     * 达式。如果表达式的计算结果为 true 的话，那么条目会放到新的集合中。否则的话，它就不会放到新集合中。在本例中，内部的表
     * 达式会检查歌曲的 artist 属性是不是等于 Aerosmith。
     *
     * SpEL还提供了另外两个查询运算符：".^[]" 和 ".$[]"，它们分别用来在集合中查询第一个匹配项和最后一个匹配项。例如，考虑
     * 下面的表达式，它会查找列表中第一个 artist 属性为 Aerosmith 的歌曲：
     *
     * #{jukebox.songs.^[artist eq 'Aerosmith']}
     *
     * 最后，SpEL 还提供了投影运算符(.![])，它会从集合的每个成员中选择特定的属性放到另外一个集合中。作为样例，假设不想要歌
     * 曲对象的集合，而是所有歌曲名称的集合。如下的表达式会将 title 属性投影到一个新的 String 类型的集合中：
     *
     * #{jukebox.songs.![title]}
     *
     * 实际上，投影操作可以与其他任意的 SpEL 运算符一起使用。比如，可以使用如下的表达式获得 Aerosmith 所有歌曲的名称列表：
     *
     * #{jukebox.songs.?[artist eq 'Aerosmith'].![title]}
     *
     * 这里所介绍的只是 SpEL 功能的一点皮毛。后续还有更多的机会继续介绍 SpEL，尤其是在定义安全规则的时候。
     *
     * 现在对 SpEL 的介绍要告一段落了，不过在此之前，有一个提示。在动态注入值到 Spring bean 时，SpEL 是一种很便利和强大
     * 的方式。有时会忍不住编写很复杂的表达式，但需要注意的是，不要让你的表达式太智能。你的表达式越智能，对它的测试就越重要。
     * SpEL 毕竟只是 String 类型的值，可能测试起来很困难。鉴于这一点，建议尽可能让表达式保持简洁，这样测试不会是什么大问题。
     */
    public static void main(String[] args) {

    }

}
