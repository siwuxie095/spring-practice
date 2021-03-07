package com.siwuxie095.spring.chapter14th.example8th;

/**
 * @author Jiajing Li
 * @date 2021-03-07 18:24:18
 */
public class Main {

    /**
     * 小结
     *
     * 方法级别的安全性是 Spring Security Web 级别安全性的一个重要补充。对于非 Web 应用来说，
     * 方法级别的安全性则是最前沿的防护。对于 Web 应用来讲，基于安全规则所声明的方法级别安全性能
     * 够保护 Web 请求。
     *
     * 在这里，看到了六个可以在方法上声明安全性限制的注解。对于简单场景来说，面向权限的注解，包括
     * Spring Security 的 @Secured 以及基于标准的 @RolesAllowed 都很便利。当安全规则更为复
     * 杂的时候，组合使用 @PreAuthorize、@PostAuthorize 以及 SpEL 能够发挥更强大的威力。还
     * 看到通过为 @PreFilter 和 @PostFilter 提供 SpEL 表达式，过滤方法的输入和输出。
     *
     * 最后，还看到了让安全规则更加易于维护、测试和调试的方法，那就是自定义表达式计算器，它能够用
     * 在 SpEL 表达式的 hasPermission() 函数中。
     */
    public static void main(String[] args) {

    }

}
