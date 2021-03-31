package com.siwuxie095.spring.chapter20th.example1st;

/**
 * @author Jiajing Li
 * @date 2021-03-31 07:55:36
 */
public class Main {

    /**
     * 使用 JMX 管理 Spring Bean
     *
     * Spring 对 DI 的支持是通过在应用中配置 bean 属性，这是一种非常不错的方法。不过，一旦应用已经部署并且正在运行，
     * 单独使用 DI 并不能帮助改变应用的配置。假设希望深入了解正在运行的应用并要在运行时改变应用的配置，此时，就可以使
     * 用 Java 管理扩展（Java Management Extensions，JMX）了。
     *
     * JMX 这项技术能够让开发者管理、监视和配置应用。这项技术最初作为 Java 的独立扩展，从 Java 5 开始，JMX 已经成
     * 为标准的组件。
     *
     * 使用 JMX 管理应用的核心组件是托管 bean（managed bean，MBean）。所谓的 MBean 就是暴露特定方法的 JavaBean，
     * 这些方法定义了管理接口。JMX 规范定义了如下 4 种类型的 MBean：
     * （1）标准 MBean：标准 MBean 的管理接口是通过在固定的接口上执行反射确定的，bean 类会实现这个接口。
     * （2）动态 MBean：动态 MBean 的管理接口是在运行时通过调用 DynamicMBean 接口的方法来确定的。因为管理接口不是
     * 通过静态接口定义的，因此可以在运行时改变。
     * （3）开放 MBean：开放 MBean 是一种特殊的动态 MBean，其属性和方法只限定于原始类型、原始类型的包装类以及可以
     * 分解为原始类型或原始类型包装类的任意类型。
     * （4）模型 MBean：模型 MBean 也是一种特殊的动态 MBean，用于充当管理接口与受管资源的中介。模型 Bean 并不像它
     * 们所声明的那样来编写。它们通常通过工厂生成，工厂会使用元信息来组装管理接口。
     *
     * Spring 的 JMX 模块可以将 Spring bean 导出为模型 MBean，这样就可以查看应用程序的内部情况并且能够更改配置
     * —— 甚至在应用的运行期。后续将会介绍如何使用 Spring 对 JMX 的支持来管理 Spring 应用上下文中的 bean。
     */
    public static void main(String[] args) {

    }

}
