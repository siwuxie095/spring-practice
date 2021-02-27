package com.siwuxie095.spring.chapter10th.example2nd;

/**
 * @author Jiajing Li
 * @date 2021-02-21 16:30:02
 */
public class Main {

    /**
     * Spring 的数据访问哲学
     *
     * Spring 的目标之一就是允许在开发应用程序时，能够遵循面向对象（OO）原则中的 "针对接口编程"。
     * Spring 对数据访问的支持也不例外。
     *
     * 像很多应用程序一样，Spittr 应用需要从某种类型的数据库中读取和写入数据。为了避免持久化的逻辑
     * 分散到应用的各个组件中，最好将数据访问的功能放到一个或多个专注于此项任务的组件中。这样的组件
     * 通常称为数据访问对象（data access object，DAO）或 Repository。
     *
     * 为了避免应用与特定的数据访问策略耦合在一起，编写良好的 Repository 应该以接口的方式暴露功能。
     *
     * PS：服务对象本身并不会处理数据访问，而是将数据访问委托给 Repository。 Repository 接口确保
     * 其与服务对象的松耦合。
     *
     * 服务对象通过接口来访问 Repository。这样做会有几个好处。第一，它使得服务对象易于测试，因为它
     * 们不再与特定的数据访问实现绑定在一起。实际上，你可以为这些数据访问接口创建 mock 实现，这样无
     * 需连接数据库就能测试服务对象，而且会显著提升单元测试的效率并排除因数据不一致所造成的测试失败。
     *
     * 此外，数据访问层是以持久化技术无关的方式来进行访问的。持久化方式的选择独立于 Repository，同
     * 时只有数据访问相关的方法才通过接口进行暴露。这可以实现灵活的设计，并且切换持久化框架对应用程序
     * 其他部分所带来的影响最小。如果将数据访问层的实现细节渗透到应用程序的其他部分中，那么整个应用程
     * 序将与数据访问层耦合在一起，从而导致僵化的设计。
     *
     * 接口与 Spring：这里更倾向于将持久层隐藏在接口之后，因为接口是实现松耦合代码的关键，并且应将其
     * 用于应用程序的各个层，而不仅仅是持久化层。还要说明一点，尽管 Spring 鼓励使用接口，但这并不是
     * 强制的 —— 你可以使用 Spring 将 bean（DAO 或其他类型）直接装配到另一个 bean 的某个属性中，
     * 而不需要一定通过接口注入。
     *
     * 为了将数据访问层与应用程序的其他部分隔离开来，Spring 采用的方式之一就是提供统一的异常体系，
     * 这个异常体系用在了它支持的所有持久化方案中。后续会介绍这个异常体系。
     */
    public static void main(String[] args) {

    }

}