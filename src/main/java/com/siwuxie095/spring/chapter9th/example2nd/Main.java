package com.siwuxie095.spring.chapter9th.example2nd;

/**
 * @author Jiajing Li
 * @date 2021-02-12 20:39:37
 */
public class Main {

    /**
     * Spring Security 简介
     *
     * Spring Security 是为基于 Spring 的应用程序提供声明式安全保护的安全性框架。Spring Security 提供了完整的
     * 安全性解决方案，它能够在 Web 请求级别和方法调用级别处理身份认证和授权。因为基于 Spring 框架，所以 Spring
     * Security 充分利用了依赖注入（dependency injection，DI）和面向切面的技术。
     *
     * 最初，Spring Security 被称为 Acegi Security。Acegi 是一个强大的安全框架，但是它存在一个严重的问题：那就
     * 是需要大量的 XML 配置。这里不会向你介绍这种复杂配置的细节。总之一句话，典型的 Acegi 配置有几百行 XML 是很常
     * 见的。
     *
     * 到了 2.0 版本，Acegi Security 更名为 Spring Security。但是 2.0 发布版本所带来的不仅仅是表面上名字的变化。
     * 为了在 Spring 中配置安全性，Spring Security 引入了一个全新的、与安全性相关的 XML 命名空间。这个新的命名空
     * 间连同注解和一些合理的默认设置，将典型的安全性配置从几百行 XML 减少到十几行。Spring Security 3.0 融入了
     * SpEL，这进一步简化了安全性的配置。
     *
     * 它的最新版本为 3.2，Spring Security 从两个角度来解决安全性问题。它使用 Servlet 规范中的 Filter 保护 Web
     * 请求并限制 URL 级别的访问。Spring Security 还能够使用 Spring AOP 保护方法调用 —— 借助于对象代理和使用通
     * 知，能够确保只有具备适当权限的用户才能访问安全保护的方法。
     *
     * 在这里，将会关注如何将 Spring Security 用于 Web 层的安全性之中。后续会重新学习 Spring Security，了解它
     * 如何保护方法的调用。
     */
    public static void main(String[] args) {

    }

}
