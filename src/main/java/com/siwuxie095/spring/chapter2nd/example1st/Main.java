package com.siwuxie095.spring.chapter2nd.example1st;

/**
 * @author Jiajing Li
 * @date 2020-12-21 08:04:27
 */
public class Main {

    /**
     * 装配 Bean
     *
     * 在看电影的时候，你曾经在电影结束后留在位置上继续观看片尾字幕吗？一部电影需要由这么多人齐心协力才能制作出来，
     * 这真是有点令人难以置信！除了主要的参与人员 —— 演员、编剧、导演和制片人，还有那些幕后人员 —— 音乐师、特效
     * 制作人员和艺术指导，更不用说道具师、录音师、服装师、化妆师、特技演员、广告师、第一助理摄影师、第二助理摄影
     * 师、布景师、灯光师和伙食管理员（或许是最重要的人员）了。
     *
     * 现在想象一下，如果这些人彼此之间没有任何交流，你最喜爱的电影会变成什么样子？这么说吧，他们都出现在摄影棚中，
     * 开始各做各的事情，彼此之间互不合作。如果导演保持沉默不喊 "开机"，摄影师就不会开始拍摄。或许这并没什么大不
     * 了的，因为女主角还呆在她的保姆车里，而且因为没有雇佣灯光师，一切处于黑暗之中。或许你曾经看过类似这样的电影。
     * 但是大多数电影（总之，都还是很优秀的）都是由成千上万的人一起协作来完成的，他们有着共同的目标：制作一部广受
     * 欢迎的佳作。
     *
     * 在这方面，一个优秀的软件与之相比并没有太大区别。任何一个成功的应用都是由多个为了实现某一个业务目标而相互协
     * 作的组件构成的。这些组件必须彼此了解，并且相互协作来完成工作。例如，在一个在线购物系统中，订单管理组件需要
     * 和产品管理组件以及信用卡认证组件协作。这些组件或许还需要与数据访问组件协作，从数据库读取数据以及把数据写入
     * 数据库。
     *
     * 但是，创建应用对象之间关联关系的传统方法（通过构造器或者查找）通常会导致结构复杂的代码，这些代码很难被复用
     * 也很难进行单元测试。如果情况不严重的话，这些对象所做的事情只是超出了它应该做的范围，而最坏的情况则是，这些
     * 对象彼此之间高度耦合，难以复用和测试。
     *
     * 在 Spring 中，对象无需自己查找或创建与其所关联的其他对象。相反，容器负责把需要相互协作的对象引用赋予各个
     * 对象。例如，一个订单管理组件需要信用卡认证组件，但它不需要自己创建信用卡认证组件。订单管理组件只需要表明
     * 自己两手空空，容器就会主动赋予它一个信用卡认证组件。
     *
     * 创建应用对象之间协作关系的行为通常称为装配（wiring），这也是依赖注入（DI）的本质。这里将介绍使用 Spring
     * 装配 bean 的基础知识。因为 DI 是 Spring 的最基本要素，所以在开发基于 Spring 的应用时，你随时都在使用
     * 这些技术。
     */
    public static void main(String[] args) {

    }

}
