package com.siwuxie095.spring.chapter14th.example5th;

/**
 * @author Jiajing Li
 * @date 2021-03-07 16:57:23
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 使用表达式实现方法级别的安全性
     *
     * 尽管 @Secured 和 @RolesAllowed 注解在拒绝未认证用户方面表现不错，但这也是它们所能做到的所有
     * 事情了。有时候，安全性约束不仅仅涉及用户是否有权限。
     *
     * Spring Security 3.0 引入了几个新注解，它们使用 SpEL 能够在方法调用上实现更有意思的安全性约束。
     * 这些新的注解如下所示。
     * （1）@PreAuthorize：在方法调用之前，基于表达式的计算结果来限制对方法的访问；
     * （2）@PostAuthorize：允许方法调用，但是如果表达式计算结果为 false，将抛出一个安全性异常；
     * （3）@PostFilter：允许方法调用，但必须按照表达式来过滤方法的结果；
     * （4）@PreFilter：允许方法调用，但必须在进入方法之前过滤输入值。
     *
     * 这些注解的值参数中都可以接受一个 SpEL 表达式。表达式可以是任意合法的 SpEL 表达式，可能会包含
     * Spring Security 对 SpEL 的扩展。如果表达式的计算结果为 true，那么安全规则通过，否则就会失败。
     * 安全规则通过或失败的结果会因为所使用注解的差异而有所不同。
     *
     * 后续将会看到每个注解的例子。但首先，需要将 @EnableGlobalMethodSecurity 注解的 prePostEnabled
     * 属性设置为 true，从而启用它们：
     *
     * @Configuration
     * @EnableGlobalMethodSecurity(prePostEnabled = true)
     * public class ExpressionSecurityConfig extends GlobalMethodSecurityConfiguration {
     *
     * }
     *
     * 现在，方法调用前后的注解都已经启用了，可以使用它们了。
     *
     * 首先看一下如何使用 @PreAuthorize 和 @PostAuthorize 注解限制对方法的调用。
     */
    public static void main(String[] args) {

    }

}
