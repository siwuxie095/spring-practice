package com.siwuxie095.spring.chapter1st.example9th;

/**
 * @author Jiajing Li
 * @date 2020-12-18 08:14:02
 */
public class Main {

    /**
     * Spring 模块
     *
     * 当下载 Spring 发布版本并查看其 lib 目录时，会发现里面有多个 JAR 文件。在 Spring 4.0 中，Spring 框架的发布版本
     * 包括了 20 个不同的模块，每个模块会有 3 个 JAR 文件（二进制类库、源码的 JAR 文件以及 JavaDoc 的 JAR 文件）。
     *
     * 这些模块依据其所属的功能可以划分为 6 类不同的功能：
     * （1）Spring 核心容器；
     * （2）面向切面编程（Spring 的 AOP 模块）；
     * （3）数据访问与集成；
     * （4）Web 与远程调用；
     * （5）Instrumentation；
     * （6）测试。
     *
     * 总体而言，这些模块为开发企业级应用提供了所需的一切。但是你也不必将应用建立在整个 Spring 框架之上，你可以自由地选择
     * 适合自身应用需求的 Spring 模块。当 Spring 不能满足需求时，完全可以考虑其他选择。事实上，Spring 甚至提供了与其他
     * 第三方框架和类库的集成点，这样你就不需要自己编写这样的代码了。
     *
     * 下面来逐一浏览 Spring 的模块，看看它们是如何构建起 Spring 整体蓝图的。
     *
     *
     * Spring 核心容器
     *
     * 容器是 Spring 框架最核心的部分，它管理着 Spring 应用中 bean 的创建、配置和管理。在该模块中，包括了 Spring bean
     * 工厂，它为 Spring 提供了 DI 的功能。
     *
     * 基于 bean 工厂，有多种 Spring 应用上下文的实现，每一种都提供了配置 Spring 的不同方式。
     *
     * 除了 bean 工厂和应用上下文，该模块也提供了许多企业服务，例如 E-mail、JNDI 访问、EJB 集成和调度。
     *
     * 所有的 Spring 模块都构建于核心容器之上。当你配置应用时，其实你隐式地使用了这些类。
     *
     *
     * Spring 的 AOP 模块
     *
     * 在 AOP 模块中，Spring 对面向切面编程提供了丰富的支持。这个模块是 Spring 应用系统中开发切面的基础。与 DI 一样，
     * AOP 可以帮助应用对象解耦。借助于 AOP，可以将遍布系统的关注点（例如事务和安全）从它们所应用的对象中解耦出来。
     *
     *
     * 数据访问与集成
     *
     * 使用 JDBC 编写代码通常会导致大量的样板式代码，例如获得数据库连接、创建语句、处理结果集到最后关闭数据库连接。Spring
     * 的 JDBC 和 DAO（Data Access Object）模块抽象了这些样板式代码，使数据库代码变得简单明了，还可以避免因为关闭数据
     * 库资源失败而引发的问题。该模块在多种数据库服务的错误信息之上构建了一个语义丰富的异常层，以后再也不需要解释那些隐晦
     * 专有的 SQL 错误信息了。
     *
     * 对于那些更喜欢 ORM（Object-Relational Mapping）工具而不愿意直接使用 JDBC 的开发者，Spring 提供了 ORM 模块。
     * Spring 的 ORM 模块建立在对 DAO 的支持之上，并为多个 ORM 框架提供了一种构建 DAO 的简便方式。Spring 没有尝试去
     * 创建自己的 ORM 解决方案，而是对许多流行的 ORM 框架进行了集成，包括 Hibernate、Java Persistence API、
     * Java Data Object 和 iBATIS SQL Maps（现为 MyBatis）。Spring 的事务管理支持所有的 ORM 框架以及 JDBC。
     *
     * 本模块同样包含了在 JMS（Java Message Service）之上构建的 Spring 抽象层，它会使用消息以异步的方式与其他应用集成。
     * 从 Spring 3.0 开始，本模块还包含对象到 XML 映射的特性，它最初是 Spring Web Service 项目的一部分。
     *
     * 除此之外，本模块会使用 Spring AOP 模块为 Spring 应用中的对象提供事务管理服务。
     *
     *
     * Web 与远程调用
     *
     * MVC（Model-View-Controller）模式是一种普遍被接受的构建 Web 应用的方法，它可以帮助用户将界面逻辑与应用逻辑分离。
     * Java 从来不缺少 MVC 框架，Apache 的 Struts、JSF、WebWork 和 Tapestry 都是可选的最流行的 MVC 框架。
     *
     * 虽然 Spring 能够与多种流行的 MVC 框架进行集成，但它的 Web 和远程调用模块自带了一个强大的 MVC 框架，有助于在 Web
     * 层提升应用的松耦合水平。
     *
     * 除了面向用户的 Web 应用，该模块还提供了多种构建与其他应用交互的远程调用方案。Spring 远程调用功能集成了 RMI（Remote
     * Method Invocation）、Hessian、Burlap、JAX-WS，同时 Spring 还自带了一个远程调用框架：HTTP invoker。Spring
     * 还提供了暴露和使用 REST API 的良好支持。
     *
     *
     * Instrumentation
     *
     * Spring 的 Instrumentation 模块提供了为 JVM 添加代理（agent）的功能。具体来讲，它为 Tomcat 提供了一个织入代理，
     * 能够为 Tomcat 传递类文件，就像这些文件是被类加载器加载的一样。
     *
     * 如果这听起来有点难以理解，不必对此过于担心。这个模块所提供的 Instrumentation 使用场景非常有限。
     *
     *
     * 测试
     *
     * 鉴于开发者自测的重要性，Spring 提供了测试模块以致力于 Spring 应用的测试。
     *
     * 通过该模块，你会发现 Spring 为使用 JNDI、Servlet 和 Portlet 编写单元测试提供了一系列的 mock 对象实现。对于集成
     * 测试，该模块为加载 Spring 应用上下文中的 bean 集合以及与 Spring 上下文中的 bean 进行交互提供了支持。
     *
     * 这里有很多的样例都是测试驱动的，将会使用到 Spring 所提供的测试功能。
     */
    public static void main(String[] args) {

    }

}
