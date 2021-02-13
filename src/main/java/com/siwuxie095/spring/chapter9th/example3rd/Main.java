package com.siwuxie095.spring.chapter9th.example3rd;

/**
 * @author Jiajing Li
 * @date 2021-02-13 22:19:43
 */
public class Main {

    /**
     * 理解 Spring Security 的模块
     *
     * 不管你想使用 Spring Security 保护哪种类型的应用程序，第一件需要做的事就是将 Spring Security 模块
     * 添加到应用程序的类路径下。Spring Security 3.2 分为 11 个模块，如下所示：
     * （1）ACL：支持通过访问控制列表（access control list，ACL）为域对象提供安全性；
     * （2）切面（Aspects）：一个很小的模块，当使用 Spring Security 注解时，会使用基于 AspectJ 的切面，
     * 而不是使用标准的 Spring AOP；
     * （3）CAS 客户端（CAS Client）：提供与 Jasig 的中心认证服务（Central Authentication Service，
     * CAS）进行集成的功能；
     * （4）配置（Configuration）：包含通过 XML 和 Java 配置 Spring Security 的功能支持；
     * （5）核心（Core）：提供 Spring Security 基本库；
     * （6）加密（Cryptography）：提供了加密和密码编码的功能；
     * （7）LDAP：支持基于 LDAP 进行认证；
     * （8）OpenID：支持使用 OpenID 进行集中式认证；
     * （9）Remoting：提供了对 Spring Remoting 的支持；
     * （10）标签库（Tag Library）：Spring Security 的 JSP 标签库；
     * （11）Web：提供了 Spring Security 基于 Filter 的 Web 安全性支持。
     *
     * 应用程序的类路径下至少要包含 Core 和 Configuration 这两个模块。Spring Security 经常被用于保护
     * Web 应用，这显然也是 Spittr 应用的场景，所以还需要添加 Web 模块。同时还会用到 Spring Security
     * 的 JSP 标签库，所以需要将这个模块也添加进来。
     */
    public static void main(String[] args) {

    }

}
