package com.siwuxie095.spring.chapter14th.example7th;

/**
 * @author Jiajing Li
 * @date 2021-03-07 17:33:35
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 过滤方法的输入和输出
     *
     * 如果希望使用表达式来保护方法的话，那使用 @PreAuthorize 和 @PostAuthorize 是非常好的方案。但是，
     * 有时候限制方法调用太严格了。有时，需要保护的并不是对方法的调用，需要保护的是传入方法的数据和方法返
     * 回的数据。
     *
     * 例如，我们有一个名为 getOffensiveSpittles() 的方法，这个方法会返回标记为具有攻击性的 Spittle
     * 列表。这个方法主要会给管理员使用，以保证 Spittr 应用中内容的和谐。但是，普通用户也可以使用这个方
     * 法，用来查看他们所发布的 Spittle 有没有被标记为具有攻击性。这个方法的签名大致如下所示：
     *
     * public List<Spittle> getOffensiveSpittles() { ... }
     *
     * 按照这种方法的定义，getOffensiveSpittles() 方法与具体的用户并没有关联。它只会返回攻击性 Spittle
     * 的一个列表，并不关心它们属于哪个用户。对于管理员使用来说，这是一个很好的方法，但是它无法限制列表中
     * 的 Spittle 都属于当前用户。
     *
     * 当然，也可以重载 getOffensiveSpittles()，实现另一个版本，让它接受一个用户 ID 作为参数，查询给
     * 定用户的 Spittle。但是，正如前面所讲的那样，始终会有这样的可能性，那就是将较为宽松限制的版本用在
     * 具有一定安全限制的场景中。
     *
     * 所以需要有一种方式过滤 getOffensiveSpittles() 方法返回的 Spittle 集合，将结果限制为允许当前
     * 用户看到的内容，而这就是 Spring Security 的 @PostFilter 所能做的事情。
     *
     *
     *
     * 1、事后对方法的返回值进行过滤
     *
     * 与 @PreAuthorize 和 @PostAuthorize 类似，@PostFilter 也使用一个 SpEL 作为值参数。但是，这
     * 个表达式不是用来限制方法访问的，@PostFilter 会使用这个表达式计算该方法所返回集合的每个成员，将计
     * 算结果为 false 的成员移除掉。
     *
     * 为了阐述该功能，将 @PostFilter 应用在 getOffensiveSpittles() 方法上：
     *
     * @PreAuthorize("hasAnyRole({'ROLE_SPITTER', "ROLE_ADMIN"})")
     * @PostFilter("hasRole('ROLE_ADMIN') || "
     *              + "filterObject.spitter.username == principal.username")
     * public getOffensiveSpittles() {
     *     ...
     * }
     *
     * 在这里，@PreAuthorize 限制只有具备 ROLE_SPITTER 或 ROLE_ADMIN 权限的用户才能访问该方法。如
     * 果用户能够通过这个检查点，那么方法将会执行，并且会返回 Spittle 所组成的一个 List。
     *
     * 但是，@PostFilter 注解将会过滤这个列表，确保用户只能看到允许的 Spittle。具体来讲，管理员能够看
     * 到所有攻击性的 Spittle，非管理员只能看到属于自己的 Spittle。
     *
     * 表达式中的 filterObject 对象引用的是这个方法所返回 List 中的某一个元素（知道它是一个Spittle）。
     * 在这个 Spittle 对象中，如果 Spitter 的用户名与认证用户（表达式中的 principal.name）相同或者
     * 用户具有 ROLE_ADMIN 角色，那这个元素将会最终包含在过滤后的列表中。否则，它将被过滤掉。
     *
     *
     *
     * 2、事先对方法的参数进行过滤
     *
     * 除了事后过滤方法的返回值，还可以预先过滤传入到方法中的值。这项技术不太常用，但是在有些场景下可能会
     * 很便利。
     *
     * 例如，假设希望以批处理的方式删除 Spittle 组成的列表。为了完成该功能，可能会编写一个方法，其签名
     * 大致如下所示：
     *
     * public void deleteSpittles(List<Spittle> spittles) { ... }
     *
     * 看起来很简单，对吧？但是，如果想在它上面应用一些安全规则的话， 比如 Spittle 只能由其所有者或管理
     * 员删除，那该怎么做呢？如果是这样的话，可以将逻辑放在 deleteSpittles() 方法中，在这里循环列表中
     * 的 Spittle，只删除属于当前用户的那一部分对象（如果当前用户是管理员的话，则会全部删除）。
     *
     * 这能够运行正常，但是这意味着需要将安全逻辑直接嵌入到方法之中。相对于删除 Spittle 来讲，安全逻辑是
     * 独立的关注点（当然，它们也有所关联）。如果列表中能够只包含实际要删除的 Spittle，这样会更好一些，
     * 因为这能帮助 deleteSpittles() 方法中的逻辑更加简单，只关注于删除 Spittle 的任务。
     *
     * Spring Security 的 @PreFilter 注解能够很好地解决这个问题。与 @PostFilter 非常类似，@PreFilter
     * 也使用 SpEL 来过滤集合，只有满足 SpEL 表达式的元素才会留在集合中。但是它所过滤的不是方法的返回值，
     * @PreFilter 过滤的是要进入方法中的集合成员。
     *
     * @PreFilter 的使用非常简单。如下的 deleteSpittles() 方法使用了 @PreFilter 注解：
     *
     * @PreAuthorize("hasAnyRole({'ROLE_SPITTER', "ROLE_ADMIN"})")
     * @PreFilter("hasRole('ROLE_ADMIN') || "
     *              + "targetObject.spitter.username == principal.username")
     * public void deleteSpittles() {
     *      ...
     * }
     *
     * 与前面一样，对于没有 ROLE_SPITTER 或 ROLE_ADMIN 权限的用户，@PreAuthorize 注解会阻止对这个方
     * 法的调用。但同时，@PreFilter 注解能够保证传递给 deleteSpittles() 方法的列表中，只包含当前用户
     * 有权限删除的 Spittle。这个表达式会针对集合中的每个元素进行计算，只有表达式计算结果为 true 的元素
     * 才会保留在列表中。targetObject 是 Spring Security 提供的另外一个值，它代表了要进行计算的当前列
     * 表元素。
     *
     * Spring Security 提供了注解驱动的功能，这是通过一系列注解来实现的，到此为止，已经对这些注解进行了
     * 介绍。相对于判断用户所授予的权限，使用表达式来定义安全限制是一种更为强大的方式。
     *
     * 即便如此，也不应该让表达式过于聪明智能。应该避免编写非常复杂的安全表达式，或者在表达式中嵌入太多与
     * 安全无关的业务逻辑。而且，表达式最终只是一个设置给注解的 String 值，因此它很难测试和调试。
     *
     * 如果你觉得自己的安全表达式难以控制了，那么就应该看一下如何编写自定义的许可计算器，以简化你的 SpEL
     * 表达式。下面看一下如何编写自定义的许可计算器，用它来简化之前用于过滤的表达式。
     *
     * PS：许可计算器，即 permission evaluator。
     *
     *
     *
     * 3、定义许可计算器
     *
     * 在 @PreFilter 和 @PostFilter 中所使用的表达式还算不上太复杂。但是，它也并不简单，可以很容易地
     * 想象如果还要实现其他的安全规则，这个表达式会不断膨胀。在变得很长之前，表达式就会笨重、复杂且难以测
     * 试。
     *
     * 其实能够将整个表达式替换为更加简单的版本，如下所示：
     *
     * @PreAuthorize("hasAnyRole({'ROLE_SPITTER', "ROLE_ADMIN"})")
     * @PreFilter("hasPermission(targetObject, delete)")
     * public void deleteSpittles() {
     *      ...
     * }
     *
     * 现在，设置给 @PreFilter 的表达式更加紧凑。它实际上只是在问一个问题 "用户有权限删除目标对象吗？"。
     * 如果有的话，表达式的计算结果为 true，Spittle 会保存在列表中，并传递给 deleteSpittles() 方法。
     * 如果没有权限的话，它将会被移除掉。
     *
     * 但是，hasPermission() 是哪来的呢？它的意思是什么？更为重要的是，它如何知道用户有没有权限删除
     * targetObject 所对应的 Spittle 呢？
     *
     * hasPermission() 函数是 Spring Security 为 SpEL 提供的扩展，它为开发者提供了一个时机，能够在
     * 执行计算的时候插入任意的逻辑。所需要做的就是编写并注册一个自定义的许可计算器。
     *
     * 具体可见 SpittlePermissionEvaluator 类，它就是一个自定义的许可计算器，包含了表达式逻辑。
     *
     * SpittlePermissionEvaluator 实现了 Spring Security 的 PermissionEvaluator 接口，它需要实
     * 现两个不同的 hasPermission() 方法。其中的一个 hasPermission() 方法把要评估的对象作为第二个参数。
     * 第二个 hasPermission() 方法在只有目标对象的 ID 可以得到的时候才有用，并将 ID 作为 Serializable
     * 传入第二个参数。
     *
     * 为了满足需求，假设使用 Spittle 对象来评估权限，所以第二个方法只是简单地抛出
     * UnsupportedOperationException。
     *
     * 对于第一个 hasPermission() 方法，要检查所评估的对象是否为一个 Spittle，并判断所检查的是否为删除
     * 权限。如果是这样，它将对比 Spitter 的用户名是否与认证用户的名称相等，或者当前用户是否具有 ROLE_ADMIN
     * 权限。
     *
     * 许可计算器已经准备就绪，接下来需要将其注册到 Spring Security 中，以便在使用 @PreFilter 表达式的
     * 时候支持 hasPermission() 操作。为了实现该功能，需要替换原有的表达式处理器，换成使用自定义许可计算
     * 器的处理器。
     *
     * 默认情况下，Spring Security 会配置为使用 DefaultMethodSecurityExpression-Handler，它会使用
     * 一个 DenyAllPermissionEvaluator 实例。顾名思义，DenyAllPermissionEvaluator 将会在
     * hasPermission() 方法中始终返回 false，拒绝所有的方法访问。但是，这里可以为 Spring Security 提
     * 供另外一个 DefaultMethodSecurityExpressionHandler，让它使用自定义的 SpittlePermissionEvaluator，
     * 这需要重载 GlobalMethodSecurityConfiguration 的 createExpressionHandler 方法：
     *
     *     @Override
     *     protected MethodSecurityExpressionHandler createExpressionHandler() {
     *         DefaultMethodSecurityExpressionHandler expressionHandler =
     *                 new DefaultMethodSecurityExpressionHandler();
     *         expressionHandler.setPermissionEvaluator(
     *                 new SpittlePermissionEvaluator());
     *         return expressionHandler;
     *     }
     *
     * 现在，不管在任何地方的表达式中使用 hasPermission() 来保护方法，都会调用 SpittlePermissionEvaluator
     * 来决定用户是否有权限调用方法。
     */
    public static void main(String[] args) {

    }

}
