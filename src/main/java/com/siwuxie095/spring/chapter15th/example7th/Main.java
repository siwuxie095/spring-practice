package com.siwuxie095.spring.chapter15th.example7th;

/**
 * @author Jiajing Li
 * @date 2021-03-14 22:46:22
 */
public class Main {

    /**
     * 小结
     *
     * 使用远程服务通常是一个乏味的苦差事，但是 Spring 提供了对远程服务的支持，让使用远程服务与
     * 使用普通的 JavaBean 一样简单。
     *
     * 在客户端，Spring 提供了代理工厂 bean，能在 Spring 应用中配置远程服务。不管是使用 RMI、
     * Hessian、Burlap、Spring 的 HTTP invoker，还是 Web 服务，都可以把远程服务装配进应用
     * 中，好像它们就是 POJO 一样。Spring 甚至捕获了所有的 RemoteException 异常，并在发生异
     * 常的地方重新抛出运行期异常 RemoteAccessException，使得代码可以从处理不可恢复的异常中
     * 解放出来。
     *
     * 即便 Spring 隐藏了远程服务的很多细节，让它们表现得好像是本地 JavaBean 一样，但是应该时
     * 刻谨记它们是远程服务的事实。远程服务，本质上来讲，通常比本地服务更低效。当编写访问远程服务
     * 的代码时，必须考虑到这一点，限制远程调用，以规避性能瓶颈。
     *
     * 在这里，看到了 Spring 是如何使用几种基本的远程调用技术来发布和使用服务的。尽管这些远程调
     * 用方案在分布式应用中很有价值，但这只是涉及面向服务架构（SOA）的一鳞半爪。
     *
     * 还了解了如何将 bean 导出为基于 SOAP 的 Web 服务。尽管这是开发 Web 服务的一种简单方式，
     * 但从架构角度来看，它可能不是最佳的选择。后续将学习构建分布式应用的另一种选择，把应用暴露
     * 为 RESTful 资源。
     */
    public static void main(String[] args) {

    }

}
