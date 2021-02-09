package com.siwuxie095.spring.chapter8th.example2nd;

/**
 * @author Jiajing Li
 * @date 2021-02-09 16:24:08
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 在 Spring 中配置 Web Flow
     *
     * Spring Web Flow 是构建于 Spring MVC 基础之上的。这意味着所有的流程请求都需要首先经过 Spring MVC
     * 的 DispatcherServlet。需要在 Spring 应用上下文中配置一些 bean 来处理流程请求并执行流程。
     *
     * 现在，还不支持在 Java 中配置 Spring Web Flow，所以别无选择，只能在 XML 中对其进行配置。有一些 bean
     * 会使用 Spring Web Flow 的 Spring 配置文件命名空间来进行声明。因此，需要在上下文定义XML文件中添加这
     * 个命名空间声明：
     *
     * <?xml version="1.0" encoding="UTF-8"?>
     * <beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     *        xmlns:flow="http://www.springframework.org/schema/webflow-config"
     *        xmlns:context="http://www.springframework.org/schema/context"
     *        xmlns="http://www.springframework.org/schema/beans"
     *        xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.0.xsd
     *         http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.0.xsd
     *         http://www.springframework.org/schema/webflow-config http://www.springframework.org/schema/webflow-config/spring-webflow-config.xsd">
     *
     * 在声明了命名空间之后，就为装配 Web Flow 的 bean 做好了准备，下面从流程执行器（flow executor）开始吧。
     *
     *
     *
     * 1、装配流程执行器
     *
     * 正如其名字所示，流程执行器（flow executor）驱动流程的执行。当用户进入一个流程时，流程执行器会为用户创建
     * 并启动一个流程执行实例。当流程暂停的时候（如为用户展示视图时），流程执行器会在用户执行操作后恢复流程。
     *
     * 在Spring中，<flow:flow-executor> 元素会创建一个流程执行器：
     *
     * <flow:flow-executor id="flowExecutor"/>
     *
     * 尽管流程执行器负责创建和执行流程，但它并不负责加载流程定义。这个责任落在了流程注册表（flow registry）身
     * 上，接下来会创建它。
     *
     *
     *
     * 2、配置流程注册表
     *
     * 流程注册表（flow registry）的工作是加载流程定义并让流程执行器能够使用它们。可以在 Spring 中使用
     * <flow:flow-registry> 配置流程注册表，如下所示：
     *
     *     <flow:flow-registry id="flowRegistry"
     *                         base-path="/WEB-INF/flows">
     *         <flow:flow-location-pattern value="*-flow.xml"/>
     *     </flow:flow-registry>
     *
     * 在这里的声明中，流程注册表会在 "/WEB-INF/flows" 目录下查找流程定义，这是通过 base-path 属性指明的。
     * 依据 <flow:flow-location-pattern> 元素的值，任何文件名以 "-flow.xml" 结尾的 XML 文件都将视为流程
     * 定义。
     *
     * 所有的流程都是通过其 ID 来进行引用的。这里使用了 <flow:flow-location-pattern> 元素，流程的 ID 就是
     * 相对于 base-path 的路径或者双星号所代表的路径。
     *
     * 如下示例展示了流程 ID 是如何计算的：
     *
     * /WEB-INF/flows/order/order-flow.xml
     *
     * （1）WEB-INF/flows 即 流程注册表基本路径；
     * （2）order 即 流程 ID；
     * （3）order-flow.xml 即 流程定义。
     *
     * PS：在使用流程定位模式的时候，流程定义文件相对于基本路径的路径将被用作流程的 ID。
     *
     * 作为另一种方式，可以去除 base-path 属性，而显式声明流程定义文件的位置：
     *
     *     <flow:flow-registry id="flowRegistry">
     *         <flow:flow-location path="/WEB-INF/flows/springpizza.xml"/>
     *     </flow:flow-registry>
     *
     * 在这里，使用了 <flow:flow-location> 而不是 <flow:flow-location-pattern>，path 属性直接指明了
     * "/WEB-INF/flows/springpizza.xml" 作为流程定义。当这样配置的话，流程的 ID 是从流程定义文件的文件
     * 名中获得的，在这里就是 springpizza。
     *
     * 如果你希望更显式地指定流程 ID，那你可以通过 <flow:flow-location> 元素的 id 属性来进行设置。例如，
     * 要将 pizza 作为流程 ID，可以像这样配置：
     *
     *     <flow:flow-registry id="flowRegistry">
     *         <flow:flow-location id="pizza" path="/WEB-INF/flows/springpizza.xml"/>
     *     </flow:flow-registry>
     *
     *
     *
     * 3、处理流程请求
     *
     * DispatcherServlet 一般将请求分发给控制器。但是对于流程而言，需要一个 FlowHandlerMapping 来帮助
     * DispatcherServlet 将流程请求发送给 Spring Web Flow。在 Spring 应用上下文中，FlowHandlerMapping
     * 的配置如下：
     *
     *     <bean class="org.springframework.webflow.mvc.servlet.FlowHandlerMapping">
     *         <property name="flowRegistry" ref="flowRegistry"/>
     *     </bean>
     *
     * 可以看到，FlowHandlerMapping 装配了流程注册表的引用，这样它就能知道如何将请求的 URL 匹配到流程上。例如，
     * 如果有一个 ID 为 pizza 的流程，FlowHandlerMapping 就会知道如果请求的 URL 模式（相对于应用程序的上下文
     * 路径）是 "/pizza" 的话，就要将其匹配到这个流程上。
     *
     * 然而，FlowHandlerMapping 的工作仅仅是将流程请求定向到 Spring Web Flow 上，响应请求的是 FlowHandlerAdapter。
     * FlowHandlerAdapter 等同于 Spring MVC 的控制器，它会响应发送的流程请求并对其进行处理。FlowHandlerAdapter 可
     * 以像下面这样装配成一个 Spring bean，如下所示：
     *
     *     <bean class="org.springframework.webflow.mvc.servlet.FlowHandlerAdapter">
     *         <property name="flowExecutor" ref="flowExecutor"/>
     *     </bean>
     *
     * 这个处理适配器是 DispatcherServlet 和 Spring Web Flow 之间的桥梁。它会处理流程请求并管理基于这些请求的流程。
     * 在这里，它装配了流程执行器的引用，而后者是为所处理的请求执行流程的。
     *
     * 已经配置了 Spring Web Flow 所需的 bean 和组件，剩下的就是真正定义流程了。
     */
    public static void main(String[] args) {

    }

}
