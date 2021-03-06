package com.siwuxie095.spring.chapter8th.example4th;

/**
 * @author Jiajing Li
 * @date 2021-02-10 18:05:04
 */
public class Main {

    /**
     * 组合起来：披萨流程
     *
     * 这里将暂时不用 Spittr 应用程序。取而代之，被要求做一个在线的披萨订购应用，饥饿的 Web 访问者可以在这里
     * 订购他们所喜欢的意大利派。
     *
     * 实际上，订购披萨的过程可以很好地定义在一个流程中。首先从构建一个高层次的流程开始，它定义了订购披萨的整体
     * 过程。接下来，会将这个流程拆分成子流程，这些子流程在较低的层次定义了细节。
     *
     *
     *
     * 1、定义基本流程
     *
     * 一个新的披萨连锁店 Spizza 决定允许用户在线订购以减轻店面电话的压力。当顾客访问 Spizza 站点时，他们需
     * 要进行用户识别，选择一个或更多披萨添加到订单中，提供支付信息然后提交订单并等待热乎又新鲜的披萨送过来。
     *
     * 基本流程如下：
     * （1）identifyCustomer 状态：一开始是识别顾客，进行身份验证，验证通过后，通过 customerReady 转移到
     * 下一个状态；
     * （2）buildOrder 状态：顾客构建订单，构建完毕后，通过 orderCreated 转移到下一个状态；
     * （3）takePayment 状态：顾客进行支付，支付完毕后，通过 paymentTaken 转移到下一个状态；
     * （4）saveOrder 状态：后台保存订单，然后直接转移到下一个状态；
     * （5）thankCustomer 状态：跳转到感谢顾客的页面。
     *
     * 这样就将订购披萨的过程归结为一个简单的流程，订购披萨的整个流程很简单且是线性的。在 Spring Web Flow
     * 中，表示这个流程是很容易的。使这个过程变得更有意思的就是前三个流程会比图中的简单方框更复杂（前三步的
     * 状态其实也是子流程）。
     *
     * pizza 文件夹下的 pizza-flow.xml 展示了如何使用 Spring Web Flow 的 XML 流程定义来实现披萨订单的
     * 整体流程。
     *
     * 在流程定义中，看到的第一件事就是 order 变量的声明。每次流程开始的时候，都会创建一个 Order 实例。Order
     * 类会带有关于订单的所有信息，包含顾客信息、订购的披萨列表以及支付详情，具体可见 Order 类。
     *
     * 流程定义的主要组成部分是流程的状态。默认情况下，流程定义文件中的第一个状态也会是流程访问中的第一个状态。
     * 在本例中，也就是 identifyCustomer 状态（一个子流程）。但是如果你愿意的话，你可以通过 <flow> 元素的
     * start-state 属性将任意状态指定为开始状态。
     *
     * <flow start-state="identifyCustomer">
     *     ...
     * </flow>
     *
     * 识别顾客、构造披萨订单以及支付这样的活动太复杂了，并不适合将其强行塞入一个状态。这是为何在后面将其单独
     * 定义为流程的原因。但是为了更好地整体了解披萨流程，这些活动都是以 <subflow-state> 元素来进行展现的。
     *
     * 流程变量 order 将在前三个状态中进行填充并在第四个状态中进行保存。identifyCustomer 子流程状态使用了
     * <output> 元素来填充 order 的 customer 属性，将其设置为顾客子流程收到的输出。buildOrder 和
     * takePayment 状态使用了不同的方式，它们使用 <input> 将 order 流程变量作为输入，这些子流程就能在其
     * 内部填充 order 对象。
     *
     * 在订单得到顾客、一些披萨以及支付细节后，就可以对其进行保存了。saveOrder 是处理这个任务的行为状态。它
     * 使用 <evaluate> 来调用 ID 为 pizzaFlowActions 的 bean 的 saveOrder() 方法，并将保存的订单对象
     * 传递进来。订单完成保存后，它会转移到 thankCustomer。
     *
     * thankCustomer 状态是一个简单的视图状态，后台使用了 "/WEB-INF/flows/pizza/thankCustomer.jsp"
     * 这个 JSP 文件。
     *
     * 在 "感谢" 页面中，会感谢顾客的订购并为其提供一个完成流程的链接。这个链接是整个页面中最有意思的事情，
     * 因为它展示了用户与流程交互的唯一办法。
     *
     * Spring Web Flow 为视图的用户提供了一个 flowExecutionUrl 变量，它包含了流程的 URL。结束链接将一个
     * "_eventId" 参数关联到 URL 上，以便回到 Web 流程时触发 finished 事件。该事件将会让流程到达结束状态。
     *
     * 流程将会在结束状态完成。鉴于在流程结束后没有下一步做什么的具体信息，流程将会重新从 identifyCustomer
     * 状态开始，以准备接受另一个披萨订单。
     *
     * 这涵盖了订购披萨的整体流程。但是这个流程并不仅仅是目前所看到的这些。还需要定义 identifyCustomer、
     * buildOrder、takePayment 这些状态的子流程。下面从识别用户开始构建这些流程。
     *
     *
     *
     * 2、收集顾客信息
     *
     * 如果你曾经订购过披萨，你可能会知道流程。他们首先会询问你的电话号码。电话号码除了能够让送货司机在找不到
     * 你家的时候打电话给你，还可以作为你在这个披萨店的标识。如果你是回头客，他们可以使用这个电话号码来查找你
     * 的地址，这样他们就知道将你的订单派送到什么地方了。
     *
     * 对于一个新的顾客来讲，查询电话号码不会有什么结果。所以接下来，他们将询问你的地址。这样，披萨店的人就会
     * 知道你是谁以及将披萨送到哪里。
     *
     * 但是在问你要哪种披萨之前，他们要确认你的地址在他们的配送范围之内。如果不在的话，你需要自己到店里并取走
     * 披萨。
     *
     * 这个流程比整体的披萨流程更有意思。这个流程不是线性的而是在好几个地方根据不同的条件有了分支。例如，在查
     * 找顾客后，流程可能结束（如果找到了顾客），也有可能转移到注册表单（如果没有找到顾客）。同样， 在
     * checkDeliveryArea 状态，顾客有可能会被警告也有可能不被警告他们的地址在配送范围之外。具体可见
     * pizza/customer 文件夹下的 customer-flow.xml 文件。
     *
     * 这个流程包含了几个新的技巧，包括首次使用的 <decision-state> 元素。因为它是 pizza 流程的子流程，所
     * 以它也可以接受 Order 对象作为输入。
     *
     * 与前面一样，还是将这个流程的定义分解成一个个的状态，先从 welcome 状态开始。
     *
     *
     * 2.1、询问电话号码
     *
     * welcome 状态是一个很简单的视图状态，它欢迎访问 Spizza 站点的顾客并要求他们输入电话号码。这个状态并
     * 没有什么特殊的。它有两个转移：如果从视图触发 phoneEntered 事件的话，转移会将流程定向到 lookupCustomer，
     * 另外一个就是在全局转移中定义的用来响应 cancel 事件的 cancel 转移。
     *
     * welcome 状态的有趣之处在于视图本身。视图 welcome 定义在 "/WEB-INF/flows/pizza/customer/welcome.jsp"
     * 中。这个简单的表单提示用户输入其电话号码。但是表单中有两个特殊的部分来驱动流程继续。
     *
     * 首先要注意的是隐藏的 "_flowExecutionKey" 输入域。当进入视图状态时，流程暂停并等待用户采取一些行为。
     * 赋予视图的流程执行 key（flow execution key）就是一种返回流程的 "回程票"（claim ticket）。当用户
     * 提交表单时，流程执行 key 会在 "_flowExecutionKey" 输入域中返回并在流程暂停的位置进行恢复。
     *
     * 还要注意的是提交按钮的名字。按钮名字的 "_eventId_" 部分是提供给 Spring Web Flow 的一个线索，它表
     * 明了接下来要触发事件。当点击这个按钮提交表单时，会触发 phoneEntered 事件进而转移到 lookupCustomer。
     *
     *
     * 2.2、查找顾客
     *
     * 当欢迎表单提交后，顾客的电话号码将包含在请求参数中并准备用于查询顾客。lookupCustomer 状态的
     * <evaluate> 元素是查找发生的地方。它将电话号码从请求参数中抽取出来并传递到 pizzaFlowActions bean
     * 的 lookupCustomer() 方法中。
     *
     * 目前，lookupCustomer() 的实现并不重要。只需知道它要么返回 Customer 对象，要么抛出
     * CustomerNotFoundException 异常。
     *
     * 在前一种情况下，Customer 对象将会设置到 customer 变量中（通过 result 属性）并且默认的转移将把流程
     * 带到 customerReady 状态。但是如果不能找到顾客的话，将抛出 CustomerNotFoundException 并且流程被
     * 转移到 registrationForm 状态。
     *
     *
     * 2.3、注册新顾客
     *
     * registrationForm 状态是要求用户填写配送地址的。就像之前看到的其他视图状态，它将被渲染成 JSP。
     *
     * 这并非在流程中看到的第一个表单。welcome 视图状态也为顾客展现了一个表单，那个表单很简单，并且只有一个
     * 输入域，从请求参数中获得输入域的值也很简单。但是注册表单就比较复杂了。
     *
     * 在这里不是通过请求参数一个个地处理输入域，而是以更好的方式将表单绑定到 Customer 对象上 —— 让框架来
     * 做所有繁杂的工作。
     *
     *
     * 2.4、检查配送区域
     *
     * 在顾客提供其地址后，需要确认他的住址在配送范围之内。如果 Spizza 不能派送给他们，那么要让顾客知道并建
     * 议他们自己到店面里取走披萨。
     *
     * 为了做出这个判断，使用了决策状态。决策状态 checkDeliveryArea 有一个 <if> 元素，它将顾客的邮政编码
     * 传递到 pizzaFlowActions bean 的 checkDeliveryArea() 方法中。这个方法将会返回一个 Boolean 值：
     * 如果顾客在配送区域内则为 true，否则为 false。
     *
     * 如果顾客在配送区域内的话，那流程转移到 addCustomer 状态。否则，顾客被带入到 deliveryWarning 视图
     * 状态。deliveryWarning 背后的视图就是 "/WEB-INF/flows/pizza/customer/deliveryWarning.jsp"。
     *
     * 在 deliveryWarning.jsp 中与流程相关的两个关键点就是那两个链接，它们允许用户继续订单或者将其取消。
     * 通过使用与 welcome 状态相同的 flowExecutionUrl 变量，这些链接分别触发流程中的 accept 或 cancel
     * 事件。如果发送的是 accept 事件，那么流程会转移到 addCustomer 状态。否则，接下来会是全局的取消转移，
     * 子流程将会转移到 cancel 结束状态。
     *
     * 稍后将介绍结束状态。下面先来看看 addCustomer 状态。
     *
     *
     * 2.5、存储顾客数据
     *
     * 当流程抵达 addCustomer 状态时，用户已经输入了他们的地址。为了将来使用，这个地址需要以某种方式存储起
     * 来（可能会存储在数据库中）。addCustomer 状态有一个 <evaluate> 元素，它会调用 pizzaFlowActions
     * bean 的 addCustomer() 方法，并将 customer 流程参数传递进去。
     *
     * 一旦这个过程完成，会执行默认的转移，流程将会转移到 ID 为 customerReady 的结束状态。
     *
     *
     * 2.6、结束流程
     *
     * 一般来讲，流程的结束状态并不会那么有意思。但是这个流程中，它不仅仅只有一个结束状态，而是两个。当子流程
     * 完成时，它会触发一个与结束状态 ID 相同的流程事件。如果流程只有一个结束状态的话，那么它始终会触发相同
     * 的事件。但是如果有两个或更多的结束状态，流程能够影响到调用状态的执行方向。
     *
     * 当 customer 流程走完所有正常的路径后，它最终会到达 ID 为 customerReady 的结束状态。当调用它的披萨
     * 流程恢复时，它会接收到一个 customerReady 事件，这个事件将使得流程转移到 buildOrder 状态。
     *
     * 要注意的是 customerReady 结束状态包含了一个 <output> 元素。在流程中这个元素等同于 Java 中的 return
     * 语句。它从子流程中传递一些数据到调用流程。在本示例中，<output> 元素返回 customer 流程变量，这样在披
     * 萨流程中，就能够将 identifyCustomer 子流程的状态指定给订单。另一方面，如果在识别顾客流程的任意地方
     * 触发了 cancel 事件，将会通过 ID 为 cancel 的结束状态退出流程，这也会在披萨流程中触发 cancel 事件
     * 并导致转移（通过全局转移）到披萨流程的结束状态。
     *
     *
     *
     * 3、构建订单
     *
     * 在识别完顾客之后，主流程的下一件事情就是确定他们想要什么类型的披萨。订单子流程就是用于提示用户创建披萨
     * 并将其放入订单中的。具体可见 pizza/order 文件夹下的 order-flow.xml 文件。
     *
     * showOrder 状态位于订单子流程的中心位置。这是用户进入这个流程时看到的第一个状态，它也是用户在添加披萨
     * 到订单后要转移到的状态。它展现了订单的当前状态并允许用户添加其他的披萨到订单中。
     *
     * 要添加披萨到订单时，流程会转移到 createPizza 状态。这是另外一个视图状态，允许用户选择披萨的尺寸和面
     * 饼上面的配料。在这里，用户可以添加或取消披萨，两种事件都会使流程转移回 showOrder 状态。
     *
     * 从 showOrder 状态，用户可能提交订单也可能取消订单。两种选择都会结束订单子流程，但是主流程会根据选择
     * 不同进入不同的执行路径。
     *
     * 这个子流程实际上会操作主流程创建的 Order 对象。因此，需要以某种方式将 Order 从主流程传到子流程。在
     * 前面使用了 <input> 元素来将 Order 传递进流程。在这里，使用它来接收 Order 对象。如果你觉得这个流程
     * 与 Java 中的方法有些类似地话，那这里使用的 <input> 元素实际上就定义了这个子流程的签名。这个流程需要
     * 一个名为 order 的参数。
     *
     * 接下来，会看到 showOrder 状态，它是一个基本的视图状态并具有三个不同的转移，分别用于创建披萨、提交订
     * 单以及取消订单。
     *
     * createPizza 状态更有意思一些。它的视图是一个表单，这个表单可以添加新的 Pizza 对象到订单中。<on-entry>
     * 元素添加了一个新的 Pizza 对象到流程作用域内，当表单提交时，表单的内容会填充到该对象中。需要注意的是，
     * 这个视图状态引用的 model 是流程作用域内的同一个 Pizza 对象。Pizza 对象将绑定到创建披萨的表单。
     *
     * 在 createPizza.jsp 中，当通过 Continue 按钮提交订单时，尺寸和配料选择将会绑定到 Pizza 对象中并且
     * 触发 addPizza 转移。与这个转移关联的 <evaluate> 元素表明在转移到 showOrder 状态之前，流程作用域内
     * 的 Pizza 对象将会传递给订单的 addPizza() 方法中。
     *
     * 有两种方法来结束这个流程。用户可以点击 showOrder 视图中的 Cancel 按钮或者 Checkout 按钮。这两种
     * 操作都会使流程转移到一个 <end-state>。但是选择的结束状态 id 决定了退出这个流程时触发事件，进而最终
     * 确定了主流程的下一步行为。主流程要么基于 cancel 事件要么基于 orderCreated 事件进行状态转移。在前者
     * 情况下，外边的主流程会结束；在后者情况下，它将转移到 takePayment 子流程，这也是接下来要看的。
     *
     *
     *
     * 4、支付
     *
     * 吃免费披萨这事儿并不常见。如果 Spizza 披萨店让他们的顾客不提供支付信息就订购披萨的话，估计他们也维持
     * 不了多久。在披萨流程要结束的时候，最后的子流程提示用户输入他们的支付信息。
     *
     * 像订单子流程一样，支付子流程也使用 <input> 元素接收一个 Order 对象作为输入。具体可见 pizza/payment
     * 文件夹下的 payment-flow.xml 文件。
     *
     * 进入支付子流程的时候，用户会到达 takePayment 状态。这是一个视图状态，在这里用户可以选择使用信用卡、
     * 支票或现金进行支付。提交支付信息后，将进入 verifyPayment 状态。这是一个行为状态，它将校验支付信息
     * 是否可以接受。
     *
     * 在流程进入 takePayment 视图时，<on-entry> 元素将构建一个支付表单并使用 SpEL 表达式在流程作用域内
     * 创建一个 PaymentDetails 实例，这是支撑表单的对象。它也会创建视图作用域的 paymentTypeList 变量，
     * 这个变量是一个列表包含了 PaymentType 枚举的值。在这里， SpEL 的 T() 操作用于获得 PaymentType 类，
     * 这样就可以调用静态的 asList() 方法。
     *
     * 在面对支付表单的时候，用户可能提交支付也可能会取消。根据做出的选择，支付子流程将以名为 paymentTaken
     * 或 cancel 的 <end-state> 结束。就像其他的子流程一样，不论哪种 <end-state> 都会结束子流程并将控制
     * 交给主流程。但是所采用 <end-state> 的 id 将决定主流程中接下来的转移。
     *
     * 至此，就依次介绍完了披萨流程及其子流程，并看到了 Spring Web Flow 的很多功能。
     */
    public static void main(String[] args) {

    }

}
