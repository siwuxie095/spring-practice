package com.siwuxie095.spring.chapter4th.example3rd;

/**
 * @author Jiajing Li
 * @date 2021-01-14 08:06:58
 */
public class Main {

    /**
     * 定义 AOP 术语
     *
     * 与大多数技术一样，AOP 已经形成了自己的术语。描述切面的常用术语有通知（advice）、切点（pointcut）
     * 和连接点（join point）。
     *
     * 这些概念是如何关联在一起的呢？可以这样理解，在一个或多个连接点上，可以把切面的功能（通知）织入到程序
     * 的执行过程中。
     *
     * 遗憾的是，大多数用于描述 AOP 功能的术语并不直观，尽管如此，它们现在已经是 AOP 行话的组成部分了，为
     * 了理解 AOP，必须了解这些术语。在进入某个领域之前，必须学会在这个领域该如何说话。
     *
     *
     * 通知（Advice）
     *
     * 当抄表员出现在家门口时，他们要登记用电量并回去向电力公司报告。显然，他们必须有一份需要抄表的住户清单，
     * 他们所汇报的信息也很重要，但记录用电量才是抄表员的主要工作。
     *
     * 类似地，切面也有目标 —— 它必须要完成的工作。在 AOP 术语中，切面的工作被称为通知。
     *
     * 通知定义了切面是什么以及何时使用。除了描述切面要完成的工作，通知还解决了何时执行这个工作的问题。它应
     * 该应用在某个方法被调用之前？之后？之前和之后都调用？还是只在方法抛出异常时调用？
     *
     * Spring 切面可以应用 5 种类型的通知：
     * （1）前置通知（Before）：在目标方法被调用之前调用通知功能；
     * （2）后置通知（After）：在目标方法完成之后调用通知，此时不会关心方法的输出是什么；
     * （3）返回通知（After-returning）：在目标方法成功执行之后调用通知；
     * （4）异常通知（After-throwing）：在目标方法抛出异常后调用通知；
     * （5）环绕通知（Around）：通知包裹了被通知的方法，在被通知的方法调用之前和调用之后执行自定义的行为。
     *
     *
     * 连接点（Join point）
     *
     * 电力公司为多个住户提供服务，甚至可能是整个城市。每家都有一个电表，这些电表上的数字都需要读取，因此每
     * 家都是抄表员的潜在目标。抄表员也许能够读取各种类型的设备，但是为了完成他的工作，他的目标应该房屋内所
     * 安装的电表。
     *
     * 同样，应用中可能也有数以千计的时机应用通知。这些时机被称为连接点。连接点是在应用执行过程中能够插入切
     * 面的一个点。这个点可以是调用方法时、抛出异常时、甚至修改一个字段时。切面代码可以利用这些点插入到应用
     * 的正常流程之中，并添加新的行为。
     *
     *
     * 切点（Pointcut）
     *
     * 如果让一位抄表员访问电力公司所服务的所有住户，那肯定是不现实的。实际上，电力公司为每一个抄表员都分别
     * 指定某一块区域的住户。类似地，一个切面并不需要通知应用的所有连接点。切点有助于缩小切面所通知的连接点
     * 的范围。
     *
     * 如果说通知定义了切面的 "什么" 和 "何时" 的话，那么切点就定义了 "何处"。 切点的定义会匹配通知所要织
     * 入的一个或多个连接点。通常使用明确的类和方法名称，或是利用正则表达式定义所匹配的类和方法名称来指定这
     * 些切点。有些 AOP 框架允许创建动态的切点，可以根据运行时的决策（比如方法的参数值）来决定是否应用通知。
     *
     *
     * 切面（Aspect）
     *
     * 当抄表员开始一天的工作时，他知道自己要做的事情（报告用电量）和从哪些房屋收集信息。因此，他知道要完成
     * 工作所需要的一切东西。
     *
     * 切面是通知和切点的结合。通知和切点共同定义了切面的全部内容 —— 它是什么，在何时和何处完成其功能。
     *
     *
     * 引入（Introduction）
     *
     * 引入允许向现有的类添加新方法或属性。例如，可以创建一个 Auditable 通知类，该类记录了对象最后一次修改
     * 时的状态。这很简单，只需一个方法，setLastModified(Date)，和一个实例变量来保存这个状态。然后，这个
     * 新方法和实例变量就可以被引入到现有的类中，从而可以在无需修改这些现有的类的情况下，让它们具有新的行为
     * 和状态。
     *
     *
     * 织入（Weaving）
     *
     * 织入是把切面应用到目标对象并创建新的代理对象的过程。切面在指定的连接点被织入到目标对象中。在目标对象
     * 的生命周期里有多个点可以进行织入：
     * （1）编译期：切面在目标类编译时被织入。这种方式需要特殊的编译器。AspectJ 的织入编译器就是以这种方式
     * 织入切面的。
     * （2）类加载期：切面在目标类加载到 JVM 时被织入。这种方式需要特殊的类加载器（ClassLoader），它可以
     * 在目标类被引入应用之前增强该目标类的字节码。AspectJ 5 的加载时织入（load-time weaving，LTW）就
     * 支持以这种方式织入切面。
     * （3）运行期：切面在应用运行的某个时刻被织入。一般情况下，在织入切面时，AOP 容器会为目标对象动态地创
     * 建一个代理对象。Spring AOP 就是以这种方式织入切面的。
     *
     *
     * 总结如下：
     * （1）通知包含了需要用于多个应用对象的横切行为；
     * （2）连接点是程序执行过程中能够应用通知的所有点；
     * （3）切点定义了通知被应用的具体位置（在哪些连接点）。其中关键的概念是切点定义了哪些连接点会得到通知。
     */
    public static void main(String[] args) {

    }

}