package com.siwuxie095.spring.chapter1st.example7th;

/**
 * @author Jiajing Li
 * @date 2020-12-17 08:10:18
 */
public class Main {

    /**
     * 容纳你的 Bean
     *
     * 在基于 Spring 的应用中，你的应用对象生存于 Spring 容器（container）中。Spring 容器负责创建对象，
     * 装配它们，配置它们并管理它们的整个生命周期，从生存到死亡（在这里，可能就是 new 到 finalize()）。
     *
     * 即 在 Spring 应用中，对象由 Spring 容器创建和装配，并存在容器之中。
     *
     * 后续你将了解如何配置 Spring，从而让它知道该创建、配置和组装哪些对象。但首先，最重要的是了解容纳对象
     * 的容器。理解容器将有助于理解对象是如何被管理的。
     *
     * 容器是 Spring 框架的核心。Spring 容器使用 DI 管理构成应用的组件，它会创建相互协作的组件之间的关联。
     * 毫无疑问，这些对象更简单干净，更易于理解，更易于重用并且更易于进行单元测试。
     *
     * Spring 容器并不是只有一个。Spring 自带了多个容器实现，可以归为两种不同的类型。
     * （1）bean 工厂（由 org.springframework.beans.factory.BeanFactory 接口定义）是最简单的容器，
     * 提供基本的DI支持。
     * （2）应用上下文（由 org.springframework.context.ApplicationContext 接口定义）基于 BeanFactory
     * 构建，并提供应用框架级别的服务，例如从属性文件解析文本信息以及发布应用事件给感兴趣的事件监听者。
     *
     * 虽然可以在 bean 工厂和应用上下文之间任选一种，但 bean 工厂对大多数应用来说往往太低级了，因此，应用
     * 上下文要比 bean 工厂更受欢迎。这里会把精力集中在应用上下文的使用上，不再浪费时间讨论 bean 工厂。
     *
     *
     *
     * 使用应用上下文
     *
     * Spring 自带了多种类型的应用上下文。下面罗列的几个是你最有可能遇到的。
     *
     * （1）AnnotationConfigApplicationContext：
     * 从一个或多个基于 Java 的配置类中加载 Spring 应用上下文。
     *
     * （2）AnnotationConfigWebApplicationContext：
     * 从一个或多个基于 Java 的配置类中加载 Spring Web 应用上下文。
     *
     * （3）ClassPathXmlApplicationContext：
     * 从类路径下的一个或多个 XML 配置文件中加载上下文定义，把应用上下文的定义文件作为类资源。
     *
     * （4）FileSystemXmlApplicationContext：
     * 从文件系统下的一个或多个 XML 配置文件中加载上下文定义。
     *
     * （5）XmlWebApplicationContext：
     * 从 Web 应用下的一个或多个 XML 配置文件中加载上下文定义。
     *
     * 后续会对 AnnotationConfigWebApplicationContext 和 XmlWebApplicationContext 进行更详细的讨
     * 论，即（2）和（5）。
     *
     * 现在先简单地使用 FileSystemXmlApplicationContext 从文件系统中加载应用上下文或者使用
     * ClassPathXmlApplicationContext 从类路径中加载应用上下文。
     *
     * 无论是从文件系统中装载应用上下文还是从类路径下装载应用上下文，将 bean 加载到 bean 工厂的过程都是相
     * 似的。例如，如下代码展示了如何加载一个 FileSystemXmlApplicationContext：
     *
     *         ApplicationContext context =
     *                 new FileSystemXmlApplicationContext("c:/knight.xml");
     *
     * 类似地，你可以使用 ClassPathXmlApplicationContext 从应用的类路径下加载应用上下文：
     *
     *         ApplicationContext context =
     *                 new ClassPathXmlApplicationContext("knight.xml");
     *
     * 使用 FileSystemXmlApplicationContext 和使用 ClassPathXmlApplicationContext 的区别在于：
     * FileSystemXmlApplicationContext 在指定的文件系统路径下查找 knight.xml 文件；而
     * ClassPathXmlApplicationContext 是在所有的类路径（包含 JAR 文件）下查找 knight.xml 文件。
     *
     * 如果你想从 Java 配置中加载应用上下文，那么可以使用 AnnotationConfigApplicationContext：
     *
     *         ApplicationContext context =
     *                 new AnnotationConfigApplicationContext(KnightConfig.class);
     *
     * 在这里没有指定加载 Spring 应用上下文所需的 XML 文件，AnnotationConfigApplicationContext 通过
     * 一个配置类加载 bean。
     *
     * 应用上下文准备就绪之后，就可以调用上下文的 getBean() 方法从 Spring 容器中获取 bean。
     *
     * 现在你应该基本了解了如何创建 Spring 容器，下面对容器中 bean 的生命周期做更进一步的探究。
     *
     *
     *
     * bean 的生命周期
     *
     * 在传统的 Java 应用中，bean 的生命周期很简单。使用 Java 关键字 new 进行 bean 实例化，然后该 bean
     * 就可以使用了。一旦该 bean 不再被使用，则由 Java 自动进行垃圾回收。
     *
     * 相比之下，Spring 容器中的 bean 的生命周期就显得相对复杂多了。正确理解 Spring bean 的生命周期非常
     * 重要，因为你或许要利用 Spring 提供的扩展点来自定义 bean 的创建过程。
     *
     * bean 在 Spring 容器中从创建到销毁经历了若干阶段，每一阶段都可以针对 Spring 如何管理 bean 进行个
     * 性化定制。
     *
     * 在 bean 准备就绪之前，bean 工厂执行了若干启动步骤，如下：
     *
     * （1）实例化：
     * Spring 对 bean 进行实例化；
     *
     * （2）填充属性：
     * Spring 将值和 bean 的引用注入到 bean 对应的属性中；
     *
     * （3）调用 BeanNameAware 的 setBeanName() 方法：
     * 如果 bean 实现了 BeanNameAware 接口，Spring 将 bean 的 ID 传递给 setBeanName() 方法；
     *
     * （4）调用 BeanFactoryAware 的 setBeanFactory() 方法：
     * 如果 bean 实现了 BeanFactoryAware 接口，Spring 将调用 setBeanFactory() 方法，将 BeanFactory
     * 容器实例传入；
     *
     * （5）调用 ApplicationContextAware 的 setApplicationContext() 方法：
     * 如果 bean 实现了 ApplicationContextAware 接口，Spring 将调用 setApplicationContext() 方法，
     * 将 bean 所在的应用上下文的引用传入进来；
     *
     * （6）调用 BeanPostProcessor 的预初始化方法：
     * 如果 bean 实现了 BeanPostProcessor 接口，Spring 将调用它们的 postProcessBeforeInitialization()
     * 方法；
     *
     * （7）调用 InitializingBean 的 afterPropertiesSet() 方法和调用自定义的初始化方法：
     * 如果 bean 实现了 InitializingBean 接口，Spring 将调用它们的 afterPropertiesSet() 方法。类似
     * 地，如果 bean 使用 init-method 声明了初始化方法，该方法也会被调用；
     *
     * （8）调用 BeanPostProcessor 的初始化后方法：
     * 如果 bean 实现了 BeanPostProcessor 接口，Spring 将调用它们的 postProcessAfterInitialization()
     * 方法；
     *
     * （9）bean 可以使用了：
     * 此时，bean 已经准备就绪，可以被应用程序使用了，它们将一直驻留在应用上下文中，直到该应用上下文被销毁；
     *
     * （10）调用 DisposableBean 的 destroy() 方法和调用自定义的销毁方法：
     * 如果 bean 实现了 DisposableBean 接口，Spring 将调用它的 destroy() 接口方法。同样，如果 bean
     * 使用 destroy-method 声明了销毁方法，该方法也会被调用；
     *
     *
     * 现在你已经了解了如何创建和加载一个 Spring 容器。但是一个空的容器并没有太大的价值，在你把东西放进去
     * 之前，它里面什么都没有。为了从 Spring 的 DI 中受益，必须将应用对象装配进 Spring 容器中。后续将对
     * bean 装配进行更详细的探讨。
     */
    public static void main(String[] args) {

    }

}
