package com.siwuxie095.spring.chapter14th.example2nd;

/**
 * @author Jiajing Li
 * @date 2021-03-07 15:50:52
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 使用注解保护方法
     *
     * 在 Spring Security 中实现方法级安全性的最常见办法是使用特定的注解，将这些注解应用到需要保护的方法上。
     * 这样有几个好处，最重要的是在编辑器中查看给定的方法时，能够很清楚地看到它的安全规则。
     *
     * Spring Security 提供了三种不同的安全注解：
     * （1）Spring Security 自带的 @Secured 注解；
     * （2）JSR-250 的 @RolesAllowed 注解；
     * （3）表达式驱动的注解，包括 @PreAuthorize、@PostAuthorize、@PreFilter 和 @PostFilter；
     *
     * @Secured 和 @RolesAllowed 方案非常类似，能够基于用户所授予的权限限制对方法的访问。当需要在方法上定
     * 义更灵活的安全规则时，Spring Security 提供了 @PreAuthorize 和 @PostAuthorize，而 @PreFilter
     * 和 @PostFilter 能够过滤方法返回的以及传入方法的集合。
     *
     * 在这里，你将会看到如何使用这些注解。作为开始，首先会介绍 @Secured 注解，这是 Spring Security 所提
     * 供的方法级安全注解里面最简单的一个。
     */
    public static void main(String[] args) {

    }

}
