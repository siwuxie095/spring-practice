package com.siwuxie095.spring.chapter14th.example6th;

/**
 * @author Jiajing Li
 * @date 2021-03-07 17:07:50
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 表述方法访问规则
     *
     * 到目前为止，已经看到 @Secured 和 @RolesAllowed 能够限制只有用户具备所需的权限才能触发方法的执行。
     * 但是，这两个注解的不足在于它们只能基于用户授予的权限来做出决策。
     *
     * Spring Security 还提供了两个注解，@PreAuthorize 和 @PostAuthorize，它们能够基于表达式的计算
     * 结果来限制方法的访问。在定义安全限制方面，表达式带了极大的灵活性。通过使用表达式，只要能够想象得到，
     * 就可以定义任意允许访问或不允许访问方法的条件。
     *
     * @PreAuthorize 和 @PostAuthorize 之间的关键区别在于表达式执行的时机。@PreAuthorize 的表达式会
     * 在方法调用之前执行，如果表达式的计算结果不为 true 的话，将会阻止方法执行。与之相反，@PostAuthorize
     * 的表达式直到方法返回才会执行，然后决定是否抛出安全性的异常。
     *
     *
     *
     * 1、在方法调用前验证权限
     *
     *@PreAuthorize 乍看起来可能只是添加了 SpEL 支持的 @Secured 和 @RolesAllowed。实际上，你可以基于
     * 用户所授予的角色，使用 @PreAuthorize 来限制访问：
     *
     *     @PreAuthorize("(hasRole('ROLE_SPITTER')")
     *     public void addSpittle(Spittle spittle) {
     *         System.out.println("Method was called successfully");
     *     }
     *
     * 如果按照这种方式的话，@PreAuthorize 相对于 @Secured 和 @RolesAllowed 并没有什么优势。如果用户
     * 具有 ROLE_SPITTER 角色的话，允许方法调用。否则，将会抛出安全性异常，方法也不会执行。
     *
     * 但是，@PreAuthorize 的功能并不限于这个简单例子所展现的。@PreAuthorize 的 String 类型参数是一个
     * SpEL 表达式。借助于 SpEL 表达式来实现访问决策，能够编写出更高级的安全性约束。例如，Spittr 应用程
     * 序的一般用户只能写 140 个字以内的 Spittle，而付费用户不限制字数。
     *
     * 虽然 @Secured 和 @RolesAllowed 在这里无能为力，但是 @PreAuthorize 注解恰好能够适用于这种场景：
     *
     *     @PreAuthorize("(hasRole('ROLE_SPITTER') and #spittle.text.length() le 140) " +
     *             "or hasRole('ROLE_PREMIUM')")
     *     public void addSpittle(Spittle spittle) {
     *         System.out.println("Method was called successfully");
     *     }
     *
     * 表达式中的 #spittle 部分直接引用了方法中的同名参数。这使得 Spring Security 能够检查传入方法的参
     * 数，并将这些参数用于认证决策的制定。在这里，深入到 Spitter 的文本内容中，保证不超过 Spittr 标准用
     * 户的长度限制。如果是付费用户，那么就没有长度限制了。
     *
     *
     *
     * 2、在方法调用之后验证权限
     *
     * 在方法调用之后验证权限并不是比较常见的方式。事后验证一般需要基于安全保护方法的返回值来进行安全性决策。
     * 这种情况意味着方法必须被调用执行并且得到了返回值。
     *
     * 例如，假设我们想对 getSpittleById() 方法进行保护，确保返回的 Spittle 对象属于当前的认证用户。只
     * 有得到 Spittle 对象之后，才能判断它是否属于当前用户。因此，getSpittleById() 方法必须要先执行。在
     * 得到 Spittle 之后，如果它不属于当前用户的话，将会抛出安全性异常。
     *
     * 除了验证的时机之外，@PostAuthorize 与 @PreAuthorize 的工作方式差不多，只不过它会在方法执行之后，
     * 才会应用安全规则。此时，它才有机会在做出安全决策时，考虑到返回值的因素。
     *
     * 例如，要保护上面描述的 getSpittleById() 方法，可以按照如下的方式使用 @PostAuthorize 注解：
     *
     * @PostAuthorize("returnObject.spitter.username == principal.username")
     * public Spittle getSpittleById(long id) {
     *     ...
     * }
     *
     * 为了便利地访问受保护方法的返回对象，Spring Security 在 SpEL 中提供了名为 returnObject 的变量。
     * 已经知道返回对象是一个 Spittle 对象，所以这个表达式可以直接访问其 spittle 属性中的 username 属
     * 性。
     *
     * 在对比表达式双等号的另一侧，表达式到内置的 principal 对象中取出其 username 属性。principal 是
     * 另一个 Spring Security 内置的特殊名称，它代表了当前认证用户的主要信息（通常是用户名）。
     *
     * 在 Spittle 对象所包含 Spitter 中，如果 username 属性与 principal 的 username 属性相同，这个
     * Spittle 将返回给调用者。否则，会抛出一个 AccessDeniedException 异常，而调用者也不会得到 Spittle
     * 对象。
     *
     * 有一点需要注意，不像 @PreAuthorize 注解所标注的方法那样，@PostAuthorize 注解的方法会首先执行然
     * 后被拦截。这意味着，你需要小心以保证如果验证失败的话不会有一些负面的结果。
     */
    public static void main(String[] args) {

    }

}
