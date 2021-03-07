package com.siwuxie095.spring.chapter14th.example4th;

/**
 * @author Jiajing Li
 * @date 2021-03-07 16:39:57
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 在 Spring Security 中使用 JSR-250 的 @RolesAllowed 注解
     *
     * @RolesAllowed 注解和 @Secured 注解在各个方面基本上都是一致的。唯一显著的区别在于 @RolesAllowed 是
     * JSR-250 定义的 Java 标准注解。
     *
     * 差异更多在于政治考量而非技术因素。但是，当使用其他框架或 API 来处理注解的话，使用标准的 @RolesAllowed
     * 注解会更有意义。
     *
     * 如果选择使用 @RolesAllowed 的话，需要将 @EnableGlobalMethodSecurity 的 jsr250Enabled 属性设置
     * 为 true，以开启此功能：
     *
     * @Configuration
     * @EnableGlobalMethodSecurity(jsr250Enabled = true)
     * public class JSR250Config extends GlobalMethodSecurityConfiguration {
     *
     * }
     *
     * 尽管这里只是启用了 jsr250Enabled，但需要说明的一点是这与 securedEnabled 并不冲突。这两种注解风格可
     * 以同时启用。
     *
     * 在将 jsr250Enabled 设置为 true 之后，将会启用一个切点，这样带有 @RolesAllowed 注解的方法都会被
     * Spring Security 的切面包装起来。因此，在方法上使用 @RolesAllowed 的方式与使用 @Secured 类似。例如，
     * 如下的 addSpittle() 方法使用了 @RolesAllowed 注解来代替 @Secured：
     *
     *     @RolesAllowed("ROLE_SPITTER")
     *     public void addSpittle(Spittle spittle) {
     *         System.out.println("Method was called successfully");
     *     }
     *
     * 尽管 @RolesAllowed 比 @Secured 在政治上稍微有点优势，它是实现方法安全的标准注解，但是这两个注解有
     * 一个共同的不足。它们只能根据用户有没有授予特定的权限来限制方法的调用。在判断方式是否执行方面，无法使用
     * 其他的因素。之前曾经看到过，在保护 URL 方面，能够使用 SpEL 表达式克服这一限制。接下来，看一下如何组
     * 合使用 SpEL 与 Spring Security 所提供的方法调用前后注解，实现基于表达式的方法安全性。
     */
    public static void main(String[] args) {

    }

}
