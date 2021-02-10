package com.siwuxie095.spring.chapter8th.example3rd;

/**
 * @author Jiajing Li
 * @date 2021-02-10 10:24:45
 */
public class Main {

    /**
     * 流程的组件
     *
     * 在 Spring Web Flow 中，流程是由三个主要元素定义的：
     * （1）状态；
     * （2）转移；
     * （3）流程数据。
     *
     * 状态（State）是流程中事件发生的地点。如果你将流程想象成公路旅行，那状态就是路途上的城镇、路边饭店以及风景点。
     * 流程中的状态是业务逻辑执行、做出决策或将页面展现给用户的地方，而不是在公路旅行中买薯片和健怡可乐的所在。
     *
     * 如果流程状态就像公路旅行中停下来的地点，那转移（transition）就是连接这些点的公路。在流程中，通过转移的方式
     * 从一个状态到另一个状态。
     *
     * 当你在城镇之间旅行的时候，你可能要买一些纪念品，留下一些记忆并在路上取一些空的零食袋。类似地，在流程处理中，
     * 它要收集一些数据：流程的当前状况。虽然很想将其称为流程的状态，但是在讨论流程的时候状态（state）已经有了另外
     * 的含义。
     *
     * 下面仔细看一下在 Spring Web Flow 中这三个元素是如何定义的。
     *
     *
     *
     * 1、状态
     *
     * Spring Web Flow 定义了五种不同类型的状态，如下所示：
     * （1）行为（Action）：行为状态是流程逻辑发生的地方；
     * （2）决策（Decision）：决策状态将流程分成两个方向，它会基于流程数据的评估结果确定流程方向；
     * （3）结束（End）：结束状态是流程的最后一站。一旦进入 End 状态，流程就会终止；
     * （4）子流程（Subflow）：子流程状态会在当前正在运行的流程上下文中启动一个新的流程；
     * （5）视图（View）：视图状态会暂停流程并邀请用户参与流程。
     *
     * 通过选择 Spring Web Flow 的状态几乎可以把任意的安排功能构造成会话式的 Web 应用。尽管并不是所有的流程都需
     * 要上面所描述的状态，但最终你可能会经常使用它们中的大多数。
     *
     * 稍后将会看到如何将这些不同类型的状态组合起来形成一个完整的流程。但首先，来了解一下这些流程元素在 Spring Web
     * Flow 定义中是如何表现的。
     *
     *
     * 1.1、视图状态
     *
     * 视图状态用于为用户展现信息并使用户在流程中发挥作用。实际的视图实现可以是 Spring 支持的任意视图类型，但通常
     * 是用 JSP 来实现的。
     *
     * 在流程定义的 XML 文件中，<view-state> 用于定义视图状态：
     *
     * <view-state id="welcome" />
     *
     * 在这个简单的示例中，id 属性有两个含义。它在流程内标示这个状态。除此以外，因为在这里没有在其他地方指定视图，
     * 所以它也指定了流程到达这个状态时要展现的逻辑视图名为 welcome。
     *
     * 如果你愿意显式指定另外一个视图名，那可以使用 view 属性做到这一点：
     *
     * <view-state id="welcome" view="greeting" />
     *
     * 如果流程为用户展现了一个表单，你可能希望指明表单所绑定的对象。为了做到这一点，可以设置 model 属性：
     *
     * <view-state id="takePayment" model="flowScope.paymentDetails" />
     *
     * 这里指定 takePayment 视图中的表单将绑定流程作用域内的 paymentDetails 对象。（稍后，将会更详细地介绍流程
     * 作用域和数据。）
     *
     *
     * 1.2、行为状态
     *
     * 视图状态会涉及到流程应用程序的用户，而行为状态则是应用程序自身在执行任务。行为状态一般会触发 Spring 所管理
     * bean 的一些方法并根据方法调用的执行结果转移到另一个状态。
     *
     * 在流程定义 XML 中，行为状态使用 <action-state> 元素来声明。这里是一个例子：
     *
     * <action-state id="saveOrder">
     *      <evaluate expression="pizzaFlowActions.saveOrder(order)" />
     *      <transition to="thankYou" />
     * </action-state>
     *
     * 尽管不是严格需要的，但是 <action-state> 元素一般都会有一个 <evaluate> 作为子元素。<evaluate> 元素给出
     * 了行为状态要做的事情。expression 属性指定了进入这个状态时要评估的表达式。在本示例中，给出的 expression
     * 是 SpEL 表达式，它表明将会找到 ID 为 pizzaFlowActions 的 bean 并调用其 saveOrder() 方法。
     *
     * PS：关于 Spring Web Flow 与表达式语言：Spring Web Flow 在选择的表达式语言方面，经过了一些变化。在 1.0
     * 版本的时候，Spring Web Flow 使用的是对象图导航语言（Object-Graph Navigation Language，OGNL）。随后
     * 的 2.0 版本又换成了统一表达式语言（Unified Expression Language，Unified EL）。在 2.1 版本中，Spring
     * Web Flow 使用的是 SpEL。
     *
     * 尽管可以使用上述的任意表达式语言来配置 Spring Web Flow，但 SpEL 是默认和推荐使用的表达式语言。因此，当
     * 这里定义流程的时候，会选择使用 SpEL，忽略掉其他的可选方案。
     *
     *
     * 1.3、决策状态
     *
     * 有可能流程会完全按照线性执行，从一个状态进入另一个状态，没有其他的替代路线。但是更常见的情况是流程在某一个点
     * 根据流程的当前情况进入不同的分支。
     *
     * 决策状态能够在流程执行时产生两个分支。决策状态将评估一个 Boolean 类型的表达式，然后在两个状态转移中选择一个，
     * 这要取决于表达式会计算出 true 还是 false。在 XML 流程定义中，决策状态通过 <decision-state> 元素进行定义。
     * 典型的决策状态示例如下所示：
     *
     * <decision-state id="checkDeliveryArea">
     *     <if test="pizzaFlowActions.checkDeliveryArea(customer.zipCode)"
     *         then="addCustomer"
     *         else="deliveryWarning" />
     * </decision-state>
     *
     * 你可以看到，<decision-state> 并不是独立完成工作的。<if> 元素是决策状态的核心。这是表达式进行评估的地方，
     * 如果表达式结果为 true，流程将转移到 then 属性指定的状态中，如果结果为 false，流程将会转移到 else 属性
     * 指定的状态中。
     *
     *
     * 1.4、子流程状态
     *
     * 你可能不会将应用程序的所有逻辑写在一个方法中，而是将其分散到多个类、方法以及其他结构中。
     *
     * 同样，将流程分成独立的部分是个不错的主意。<subflow-state> 允许在一个正在执行的流程中调用另一个流程。这类似
     * 于在一个方法中调用另一个方法。
     *
     * <subflow-state> 可以这样声明：
     *
     * <subflow-state id="order" subflow="pizza/order">
     *     <input name="order" value="order" />
     *     <transition on="orderCreated" to="payment" />
     * </subflow-state>
     *
     * 在这里，<input> 元素用于传递订单对象作为子流程的输入。如果子流程结束的 <end-state> 状态 ID 为 orderCreated，
     * 那么流程将会转移到名为 payment 的状态。
     *
     * PS：在这里，有点超出进度了，还没有讨论到 <end-state> 元素和转移。但马上就会介绍到。
     *
     *
     * 1.5、结束状态
     *
     * 最后，所有的流程都要结束。这就是当流程转移到结束状态时所做的。<end-state> 元素指定了流程的结束，它一般会是
     * 这样声明的：
     *
     * <end-state id="customerReady" />
     *
     * 当到达 <end-state> 状态，流程会结束。接下来会发生什么取决于几个因素：
     * （1）如果结束的流程是一个子流程，那调用它的流程将会从 <subflow-state> 处继续执行。<end-state> 的 ID 将
     * 会用作事件触发从 <subflow-state> 开始的转移。
     * （2）如果 <end-state> 设置了 view 属性，指定的视图将会被渲染。视图可以是相对于流程路径的视图模板，如果添
     * 加 "externalRedirect:" 前缀的话，将会重定向到流程外部的页面，如果添加 "flowRedirect:" 将重定向到另一个
     * 流程中。
     * （3）如果结束的流程不是子流程，也没有指定 view 属性，那这个流程只是会结束而已。浏览器最后将会加载流程的基本
     * URL 地址，当前已没有活动的流程，所以会开始一个新的流程实例。
     *
     * 需要意识到流程可能会有不止一个结束状态。子流程的结束状态 ID 确定了激活的事件，所以你可能会希望通过多种结束
     * 状态来结束子流程，从而能够在调用流程中触发不同的事件。即使不是在子流程中，也有可能在结束流程后，根据流程的
     * 执行情况有多个显示页面供选择。
     *
     * 现在，已经看完了流程中的各个状态，应当看一下流程是如何在状态间迁移的。下面看看如何在流程中通过定义转移来完成
     * 道路铺设的。
     *
     *
     *
     * 2、转移
     *
     * 转移连接了流程中的状态。流程中除结束状态之外的每个状态，至少都需要一个转移，这样就能够知道一旦这个状态完成时
     * 流程要去向哪里。状态可以有多个转移，分别对应于当前状态结束时可以执行的不同的路径。
     *
     * 转移使用 <transition> 元素来进行定义，它会作为各种状态元素（<action-state>、<view-state>、<subflow-state>）
     * 的子元素。最简单的形式就是 <transition> 元素在流程中指定下一个状态：
     *
     * <transition to="customerReady" />
     *
     * 属性 to 用于指定流程的下一个状态。如果 <transition> 只使用了 to 属性，那这个转移就会是当前状态的默认转移
     * 选项，如果没有其他可用转移的话，就会使用它。
     *
     * 更常见的转移定义是基于事件的触发来进行的。在视图状态，事件通常会是用户采取的动作。在行为状态，事件是评估表达
     * 式得到的结果。而在子流程状态，事件取决于子流程结束状态的 ID。在任意的事件中（这里没有任何歧义），你可以使用
     * on 属性来指定触发转移的事件：
     *
     * <transition on="phoneEntered" to="lookupCustomer" />
     *
     * 在本例中，如果触发了 phoneEntered 事件，流程将会进入 lookupCustomer 状态。
     *
     * 在抛出异常时，流程也可以进入另一个状态。例如，如果顾客的记录没有找到，你可能希望流程转移到一个展现注册表单的
     * 视图状态。以下的代码片段显示了这种类型的转移：
     *
     * <transition
     *      on-exception="com.siwuxie095.pizza.service.CustomerNotFoundException"
     *      to="registrationForm" />
     *
     * 属性 on-exception 类似于 on 属性，只不过它指定了要发生转移的异常而不是一个事件。在本示例中，
     * CustomerNotFoundException 异常将导致流程转移到 registrationForm 状态。
     *
     *
     * 2.1、全局转移
     *
     * 在创建完流程之后，你可能会发现有一些状态使用了一些通用的转移。例如，如果在整个流程中到处都有如下 <transition>
     * 的话，一点也不感觉意外。
     *
     * <transition on="cancel" to="endState" />
     *
     * 与其在多个状态中都重复通用的转移，不如将 <transition> 元素作为 <global-transitions> 的子元素，把它们定
     * 义为全局转移。例如：
     *
     * <global-transitions>
     *     <transition on="cancel" to="endState" />
     * </global-transitions>
     *
     * 定义完这个全局转移后，流程中的所有状态都会默认拥有这个 cancel 转移。
     *
     * 已经讨论过了状态和转移。下面看一下流程数据，这是 Web 流程三元素中的另一个成员。
     *
     *
     *
     * 3、流程数据
     *
     * 如果你曾经玩过那种老式的基于文字的冒险游戏的话，那么当从一个地方转移到另一个地方时，你会偶尔发现散布在周围的
     * 一些东西，你可以把它们捡起来并带上。有时候，你会马上需要一件东西。其他的时候，你会在整个游戏过程中带着这些东
     * 西而不知道它们是做什么用的 —— 直到你到达游戏结束的时候才会发现它是真正有用的。
     *
     * 在很多方面，流程与这些冒险游戏是很类似的。当流程从一个状态进行到另一个状态时，它会带走一些数据。有时候，这些
     * 数据只需要很短的时间（可能只要展现页面给用户）。有时候，这些数据会在整个流程中传递并在流程结束的时候使用。
     *
     *
     * 3.1、声明变量
     *
     * 流程数据保存在变量中，而变量可以在流程的各个地方进行引用。它能够以多种方式创建。在流程中创建变量的最简单形式
     * 是使用 <var> 元素：
     *
     * <var name="customer" class="com.siwuxie095.pizza.domain.Customer" />
     *
     * 这里，创建了一个新的 Customer 实例并将其放在名为 customer 的变量中。这个变量可以在流程的任意状态进行访问。
     *
     * 作为行为状态的一部分或者作为视图状态的入口，你有可能会使用 <evaluate> 元素来创建变量。例如：
     *
     * <evaluate result="viewScope.toppingList"
     *      expression="T(com.siwuxie095.pizza.domain.Topping).asList()" />
     *
     * 在本例中，<evaluate> 元素计算了一个表达式（SpEL 表达式）并将结果放到了名为 toppingsList 的变量中，这个
     * 变量是视图作用域的（稍后将会介绍关于作用域的更多概念）。
     *
     * 类似地，<set> 元素也可以设置变量的值：
     *
     * <set name="flowScope.pizza"
     *      value="new com.siwuxie095.pizza.domain.Pizza()" />
     *
     * <set> 元素与 <evaluate> 元素很类似，都是将变量设置为表达式计算的结果。这里，设置了一个流程作用域内的 pizza
     * 变量，它的值是 Pizza 对象的新实例。
     *
     * 下面看一下变量的流程作用域、视图作用域以及其他的一些作用域是什么意思。
     *
     *
     * 3.2、定义流程数据的作用域
     *
     * 流程中携带的数据会拥有不同的生命作用域和可见性，这取决于保存数据的变量本身的作用域。Spring Web Flow 定义
     * 了五种不同作用域，如下所示：
     * （1）Conversation：最高层级的流程开始时创建，在最高层级的流程结束时销毁。被最高层级的流程和其所有的子流程
     * 所共享；
     * （2）Flow：当流程开始时创建，在流程结束时销毁。只有在创建它的流程中是可见的；
     * （3）Request：当一个请求进入流程时创建，在流程返回时销毁；
     * （4）Flash：当流程开始时创建，在流程结束时销毁。在视图状态渲染后，它也会被清除；
     * （5）View：当进入视图状态时创建，当这个状态退出时销毁。只在视图状态内是可见的。
     *
     * 当使用 <var> 元素声明变量时，变量始终是流程作用域的，也就是在定义变量的流程内有效。当使用 <set> 或 <evaluate>
     * 的时候，作用域通过 name 或 result 属性的前缀指定。例如，将一个值赋给流程作用域的 theAnswer 变量：
     *
     * <set name="flowScope.thenAnswer" value="42" />
     *
     * 至此，已经看到了 Web 流程的所有原材料。
     */
    public static void main(String[] args) {

    }

}
