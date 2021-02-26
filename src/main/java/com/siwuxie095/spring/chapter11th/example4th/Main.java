package com.siwuxie095.spring.chapter11th.example4th;

/**
 * @author Jiajing Li
 * @date 2021-02-26 08:02:21
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 借助 Spring Data 实现自动化的 JPA Repository
     *
     * 尽管在 example3rd 中的方法都很简单，但它们依然还会直接与 EntityManager 交互来查询数据库。并且，仔细
     * 看一下的话，这些代码多少还是样板式的。例如，save() 方法：
     *
     *     @Override
     *     public Spitter save(Spitter spitter) {
     *         entityManager.persist(spitter);
     *         return spitter;
     *     }
     *
     * 在任何具有一定规模的应用中，你可能会以几乎完全相同的方式多次编写这种方法。实际上，除了所持久化的 Spitter
     * 对象不同以外，你以前肯定写过类似的方法。其实，JpaSpitterRepository 中的其他方法也没有什么太大的创造性。
     * 领域对象会有所不同，但是所有 Repository 中的方法都是很通用的。
     *
     * 为什么需要一遍遍地编写相同的持久化方法呢，难道仅仅是因为要处理的领域类型不同吗？Spring Data JPA 能够终
     * 结这种样板式的愚蠢行为。不再需要一遍遍地编写相同的 Repository 实现，Spring Data 能够使得开发者只编写
     * Repository 接口就可以了。根本就不再需要实现类了。
     *
     * 例如，看一下 SpitterRepository 接口。
     *
     * public interface SpitterRepository extends JpaRepository<Spitter, Long> {
     *
     * }
     *
     * 此时，SpitterRepository 看上去并没有什么作用。但是，它的功能远超出了表面上所看到的那样。
     *
     * 编写 Spring Data JPA Repository 的关键在于要从一组接口中挑选一个进行扩展。这里，SpitterRepository
     * 扩展了 Spring Data JPA 的 JpaRepository。通过这种方式，JpaRepository 进行了参数化，所以它就能知道
     * 这是一个用来持久化 Spitter 对象的 Repository，并且 Spitter 的 ID 类型为 Long。另外，它还会继承 18
     * 个执行持久化操作的通用方法，如保存 Spitter、删除 Spitter 以及根据 ID 查询 Spitter。
     *
     * 此时，你可能会想下一步就该编写一个类实现 SpitterRepository 和它的 18 个方法了。如果真的是这样的话，那
     * 就会变得乏味无聊了。其实，根本不需要编写 SpitterRepository 的任何实现类，相反，Spring Data 自己会做
     * 这件事请。开发者所需要做的就是对它提出要求。
     *
     * 为了要求 Spring Data 创建 SpitterRepository 的实现，需要在 Spring 配置中添加一个元素。如下的代码展
     * 现了在 XML 配置中启用 Spring Data JPA 所需要添加的内容：
     *
     * <jpa:repositories base-package="com.habuma.spittr.db" />
     *
     * PS：需要引入 jpa 命名空间。
     *
     * <jpa:repositories> 元素掌握了 Spring Data JPA 的所有魔力。就像 <context:component-scan> 元素一
     * 样，<jpa:repositories> 元素也需要指定一个要进行扫描的 base-package。不过，<context:component-scan>
     * 会扫描包（及其子包）来查找带有 @Component 注解的类，而 <jpa:repositories> 会扫描它的基础包来查找扩展
     * 自 Spring Data JPA Repository 接口的所有接口。如果发现了扩展自 Repository 的接口，它会自动生成（在
     * 应用启动的时候）这个接口的实现。
     *
     * 如果要使用 Java 配置的话，那就不需要使用 <jpa:repositories> 元素了，而是要在 Java 配置类上添加
     * @EnableJpaRepositories 注解。如下就是一个 Java 配置类，它使用了 @EnableJpaRepositories 注解，并且
     * 会扫描 com.habuma.spittr.db 包：
     *
     * @Configuration
     * @EnableJpaRepositories(basePackages = "com.habuma.spittr.db")
     * public class JpaConfig {
     *
     * }
     *
     * 回到 SpitterRepository 接口，它扩展自 JpaRepository，而 JpaRepository 又扩展自 Repository 标记
     * 接口（虽然是间接的）。因此，SpitterRepository 就传递性地扩展了 Repository 接口，也就是 Repository
     * 扫描时所要查找的接口。当 Spring Data 找到它后，就会创建 SpitterRepository 的实现类，其中包含了继承
     * 自 JpaRepository、PagingAndSortingRepository 和 CrudRepository 的 18 个方法。
     *
     * 很重要的一点在于 Repository 的实现类是在应用启动的时候生成的，也就是 Spring 的应用上下文创建的时候。它
     * 并不是在构建时通过代码生成技术产生的，也不是接口方法调用时才创建的。
     *
     * 很漂亮的技术，对吧？
     *
     * Spring Data JPA 很棒的一点在于它能为 Spitter 对象提供 18 个便利的方法来进行通用的 JPA 操作，而无需
     * 你编写任何持久化代码。但是，如果你的需求超过了它所提供的这 18 个方法的话，该怎么办呢？幸好，Spring Data
     * JPA 提供了几种方式来为 Repository 添加自定义的方法。下面看一下如何为 Spring Data JPA 编写自定义的查
     * 询方法。
     *
     *
     *
     * 1、定义查询方法
     *
     * 现在，SpitterRepository 需要完成的一项功能是根据给定的 username 查找 Spitter 对象。
     *
     * 比如，将 SpitterRepository 接口修改为如下所示的样子：
     *
     * public interface SpitterRepository extends JpaRepository<Spitter, Long> {
     *
     *     Spitter findByUsername(String username);
     *
     * }
     *
     * 这个新的 findByUserName() 非常简单，但是足以满足需求。现在，该如何让 Spring Data JPA 提供这个方法的
     * 实现呢？
     *
     * 实际上，并不需要实现 findByUsername()。方法签名已经告诉 Spring Data JPA 足够的信息来创建这个方法的
     * 实现了。
     *
     * 当创建 Repository 实现的时候，Spring Data 会检查 Repository 接口的所有方法，解析方法的名称，并基于被
     * 持久化的对象来试图推测方法的目的。本质上，Spring Data 定义了一组小型的领域特定语言（domain-specific
     * language，DSL），在这里，持久化的细节都是通过 Repository 方法的签名来描述的。
     *
     * Spring Data 能够知道这个方法是要查找 Spitter 的，因为这里使用 Spitter 对 JpaRepository 进行了参数化。
     * 方法名 findByUsername 确定该方法需要根据 username 属性相匹配来查找 Spitter，而 username 是作为参数
     * 传递到方法中来的。另外，因为在方法签名中定义了该方法要返回一个 Spitter 对象，而不是一个集合，因此它只会查
     * 找一个 username 属性匹配的 Spitter。
     *
     * findByUsername() 方法非常简单，但是 Spring Data 也能处理更加有意思的方法名称。Repository 方法是由一
     * 个动词、一个可选的主题（Subject）、关键词 By 以及一个断言所组成。在 findByUsername() 这个样例中，动词
     * 是 find，断言是 Username，主题并没有指定，暗含的主题是 Spitter。
     *
     * 作为编写 Repository 方法名称的样例，参照名为 readSpitterByFirstnameOrLastname() 的方法，看一下方法
     * 中的各个部分是如何映射的。这个方法是这样拆分的：
     * （1）read：查询动词
     * （2）Spitter：主题
     * （3）By：关键词
     * （4）FirstnameOrLastname：断言
     *
     * PS：Repository 方法的命名遵循一种模式，有助于 Spring Data 生成针对数据库的查询。
     *
     * 这里的动词是 read，与之前样例中的 find 有所差别。Spring Data 允许在方法名中使用四种动词：get、read、
     * find 和 count。其中，动词 get、read 和 find 是同义的，这三个动词对应的 Repository 方法都会查询数据并
     * 返回对象。而动词 count 则会返回匹配对象的数量，而不是对象本身。
     *
     * Repository 方法的主题是可选的。它的主要目的是让你在命名方法的时候，有更多的灵活性。如果你更愿意将方法称为
     * readSpittersByFirstnameOrLastname() 而不是 readByFirstnameOrLastname() 的话，那么你尽可以这么做。
     *
     * 对于大部分场景来说，主题会被省略掉。
     *
     * readSpittersByFirstnameOrLastname() 与 readPuppiesByFirstnameOrLastname() 并没有什么差别，它们
     * 与 readThoseThingsWeWantByFirstnameOrLastname() 同样没有什么区别。要查询的对象类型是通过如何参数化
     * JpaRepository 接口来确定的，而不是方法名称中的主题。
     *
     * 在省略主题的时候，有一种例外情况。如果主题的名称以 Distinct 开头的话，那么在生成查询的时候会确保所返回结果
     * 集中不包含重复记录。
     *
     * 断言是方法名称中最为有意思的部分，它指定了限制结果集的属性。在 readByFirstnameOrLastname() 这个样例中，
     * 会通过 firstname 属性或 lastname 属性的值来限制结果。
     *
     * 在断言中，会有一个或多个限制结果的条件。每个条件必须引用一个属性，并且还可以指定一种比较操作。如果省略比较操
     * 作符的话，那么这暗指是一种相等比较操作。不过，也可以选择其他的比较操作，包括如下的种类：
     * （1）IsAfter、After、IsGreaterThan、GreaterThan
     * （2）IsGreaterThanEqual、GreaterThanEqual
     * （3）IsBefore、Before、IsLessThan、LessThan
     * （4）IsLessThanEqual、LessThanEqual
     * （5）IsBetween、Between
     * （6）IsNull、Null
     * （7）IsNotNull、NotNull
     * （8）IsIn、In
     * （9）IsNotIn、NotIn
     * （10）IsStartingWith、StartingWith、StartsWith
     * （11）IsEndingWith、EndingWith、EndsWith
     * （12）IsContaining、Containing、Contains
     * （13）IsLike、Like
     * （14）IsNotLike、NotLike
     * （15）IsTrue、True
     * （16）IsFalse、False
     * （17）Is、Equals
     * （18）IsNot、Not
     *
     * 要对比的属性值就是方法的参数。完整的方法签名如下所示：
     *
     * List<Spitter> readByFirstnameOrLastname(String first, String last);
     *
     * 要处理 String 类型的属性时，条件中可能还会包含 IgnoringCase 或 IgnoresCase，这样在执行对比的时候就会不
     * 再考虑字符是大写还是小写。例如，要在 firstname 和 lastname 属性上忽略大小写，那么可以将方法签名改成如下
     * 的形式：
     *
     * List<Spitter> readByFirstnameIgnoringCaseOrLastnameIgnoresCase(String first, String last);
     *
     * 需要注意，IgnoringCase 和 IgnoresCase 是同义的，你可以随意挑选一个最合适的。
     *
     * 作为 IgnoringCase/IgnoresCase 的替代方案，还可以在所有条件的后面添加 AllIgnoringCase 或 AllIgnoresCase，
     * 这样它就会忽略所有条件的大小写：
     *
     * List<Spitter> readByFirstnameOrLastnameAllIgnoresCase(String first, String last);
     *
     * 注意，参数的名称是无关紧要的，但是它们的顺序必须要与方法名称中的操作符相匹配。
     *
     * 最后，还可以在方法名称的结尾处添加 OrderBy，实现结果集排序。例如，可以按照 lastname 属性升序排列结果集：
     *
     * List<Spitter> readByFirstnameOrLastnameOrderByLastnameAsc(String first, String last);
     *
     * 如果要根据多个属性排序的话，只需将其依序添加到 OrderBy 中即可。例如，下面的样例中，首先会根据 lastname
     * 升序排列，然后根据 firstname 属性降序排列：
     *
     * List<Spitter> readByFirstnameOrLastnameOrderByLastnameAscFirstnameDesc(String first, String last);
     *
     * 可以看到，条件部分是通过 And 或者 Or 进行分割的。
     *
     * 这里不可能（至少很难）提供一个权威的列表，将使用 Spring Data 方法命名约定可以编写出来的方法种类全部列出来。
     * 但是，如下给出了几个符合方法命名约定的方法签名：
     * （1）List<Pet> findPetsByBreedIn(List<String> breed)
     * （2）int countProductsByDiscontinuedTrue()
     * （3）List<Order> findByShippingDateBetween(Date start, Date end)
     *
     * 这里只是初步体验了所能声明的方法种类，Spring Data JPA 会帮助实现这些方法。现在，只需知道通过使用属性名和
     * 关键字构建 Repository 方法签名，就能让 Spring Data JPA 生成方法实现，完成几乎所有能够想象到的查询。
     *
     * 不过，Spring Data 这个小型的 DSL 依旧有其局限性，有时候通过方法名称表达预期的查询很烦琐，甚至无法实现。
     * 如果遇到这种情形的话，Spring Data 能够通过 @Query 注解来解决问题。
     *
     *
     *
     * 2、声明自定义查询
     *
     * 假设想要创建一个 Repository 方法，用来查找 E-mail 地址是 Gmail 邮箱的 Spitter。有一种方式就是定义一个
     * findByEmailLike() 方法，然后每次想查找 Gmail 用户的时候就将 "%gmail.com" 传递进来。不过，更好的方案是
     * 定义一个更加便利的 findAllGmailSpitters() 方法，这样的话，就不用将 E-mail 地址的一部分传递进来了：
     *
     * List<Spitter> findAllGmailSpitters();
     *
     * 不过，这个方法并不符合 Spring Data 的方法命名约定。当 Spring Data 试图生成这个方法的实现时，无法将方法
     * 名的内容与 Spitter 元模型进行匹配，因此会抛出异常。
     *
     * 如果所需的数据无法通过方法名称进行恰当地描述，那么可以使用 @Query 注解，为 Spring Data 提供要执行的查询。
     * 对于 findAllGmailSpitters() 方法，可以按照如下的方式来使用 @Query 注解：
     *
     * @Query("select s from Spitter s where s.email like '%gmail.com'")
     * List<Spitter> findAllGmailSpitters();
     *
     * 这里依然不需要编写 findAllGmailSpitters() 方法的实现，只需提供查询即可，让 Spring Data JPA 知道如何
     * 实现这个方法。
     *
     * 可以看到，当使用方法命名约定很难表达预期的查询时，@Query 注解能够发挥作用。如果按照命名约定，方法的名称特别
     * 长的时候，也可以使用这个注解。例如，考虑如下的查询方法：
     *
     * List<Order> findByCustomerAddressZipCodeOrCustomerNameAndCustomerAddressState();
     *
     * 不得不承认这是一个有点牵强的例子。但在现实世界中，确实存在这样的需求，使用 Repository 方法所执行的查询会得
     * 到一个很长的方法名。在这种情况下，你最好使用一个较短的方法名，并使用 @Query 来指定该方法要如何查询数据库。
     *
     * 对于 Spring Data JPA 的接口来说，@Query 是一种添加自定义查询的便利方式。但是，它仅限于单个 JPA 查询。如
     * 果需要更为复杂的功能，无法在一个简单的查询中处理的话，该怎么办呢？
     *
     *
     *
     * 3、混合自定义的功能
     *
     * 有些时候，需要 Repository 所提供的功能是无法用 Spring Data 的方法命名约定来描述的，甚至无法用 @Query
     * 注解设置查询来实现。尽管 Spring Data JPA 非常棒，但是它依然有其局限性，可能需要开发者按照传统的方式来编写
     * Repository 方法：也就是直接使用 EntityManager。当遇到这种情况的时候，是不是要放弃Spring Data JPA 呢？
     *
     * 简单来说，是这样的。如果你需要做的事情无法通过 Spring Data JPA 来实现，那就必须要在一个比 Spring Data
     * JPA 更低的层级上使用 JPA。好消息是没有必要完全放弃 Spring Data JPA。只需在必须使用较低层级 JPA 的方法上，
     * 才使用这种传统的方式即可，而对于 Spring Data JPA 知道该如何处理的功能，依然可以通过它来实现。
     *
     * 当 Spring Data JPA 为 Repository 接口生成实现的时候，它还会查找名字与接口相同，并且添加了 Impl 后缀的
     * 一个类。如果这个类存在的话，Spring Data JPA 将会把它的方法与 Spring Data JPA 所生成的方法合并在一起。
     * 对于 SpitterRepository 接口而言，要查找的类名为 SpitterRepositoryImpl。
     *
     * 为了阐述该功能，假设需要在 SpitterRepository 中添加一个方法，发表 Spittle 数量在 10,000 及以上的 Spitter
     * 将会更新为 Elite 状态。使用 Spring Data JPA 的方法命名约定或使用 @Query 均没有办法声明这样的方法。最为
     * 可行的方案是使用如下的 eliteSweep() 方法。
     *
     * public class SpitterRepositoryImpl implements SpitterSweeper {
     *
     *     @PersistenceContext
     *     private EntityManager em;
     *
     *     @Override
     *     public int eliteSweep() {
     *         String update =
     *                 "UPDATE Spitter spitter " +
     *                         "SET spitter.status = 'Elite' " +
     *                         "WHERE spitter.status = 'Newbie' " +
     *                         "AND spitter.id IN (" +
     *                         "SELECT s FROM Spitter s WHERE (" +
     *                         "  SELECT COUNT(spittles) FROM s.spittles spittles) > 10000" +
     *                         ")";
     *         return em.createQuery(update).executeUpdate();
     *     }
     *
     * }
     *
     * 可以看到，SpitterRepositoryImpl 没有什么特殊之处，它使用被注入的 EntityManager 来完成预期的任务。
     *
     * 注意，SpitterRepositoryImpl 并没有实现 SpitterRepository 接口。Spring Data JPA 负责实现这个接口。
     * SpitterRepositoryImpl（将它与 Spring Data 的 Repository 关联起来的是它的名字）实现了 SpitterSweeper
     * 接口，它如下所示：
     *
     * public interface SpitterSweeper {
     *
     *     int eliteSweep();
     *
     * }
     *
     * 还需要确保 eliteSweep() 方法会被声明在 SpitterRepository 接口中。要实现这一点，避免代码重复的简单方式就
     * 是修改 SpitterRepository，让它扩展 SpitterSweeper：
     *
     * public interface SpitterRepository extends JpaRepository<Spitter, Long>, SpitterSweeper {
     *
     * ...
     *
     * }
     *
     * 如前所述，Spring Data JPA 将实现类与接口关联起来是基于接口的名称。但是，Impl 后缀只是默认的做法，如果你想
     * 使用其他后缀的话，只需在配置 @EnableJpaRepositories 的时候，设置 repositoryImplementationPostfix
     * 属性即可：
     *
     * @EnableJpaRepositories(
     *         basePackages = "com.habuma.spittr.db",
     *         repositoryImplementationPostfix = "Helper")
     *
     * 如果在XML中使用 <jpa:repositories> 元素来配置 Spring Data JPA 的话，可以借助 repository-impl-postfix
     * 属性指定后缀：
     *
     * <jpa:repositories base-package="com.habuma.spittr.db"
     *      repository-impl-postfix="Helper" />
     *
     * 将后缀设置成了 Helper 以后，Spring Data JPA 将会查找名为 SpitterRepositoryHelper 的类，用它来匹配
     * SpitterRepository 接口。
     */
    public static void main(String[] args) {

    }

}
