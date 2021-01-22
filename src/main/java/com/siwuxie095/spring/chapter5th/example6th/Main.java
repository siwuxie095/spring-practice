package com.siwuxie095.spring.chapter5th.example6th;

/**
 * @author Jiajing Li
 * @date 2021-01-22 08:10:46
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 编写基本的控制器
     *
     * 在 Spring MVC 中，控制器只是方法上添加了 @RequestMapping 注解的类，这个注解声明了它们所要处理的请求。
     *
     * 开始的时候，尽可能简单，假设控制器类要处理对 "/" 的请求，并渲染应用的首页。如下所示的 HomeController
     * 可能是最简单的 Spring MVC 控制器类了。
     *
     * @Controller
     * public class HomeController {
     *
     *     @RequestMapping(value = "/", method = RequestMethod.GET)
     *     public String home(Model model) {
     *         return "home";
     *     }
     *
     * }
     *
     * 你可能注意到的第一件事情就是 HomeController 带有 @Controller 注解。很显然这个注解是用来声明控制器的，
     * 但实际上这个注解对 Spring MVC 本身的影响并不大。
     *
     * HomeController 是一个构造型（stereotype）的注解，它基于 @Component 注解。在这里，它的目的就是辅助
     * 实现组件扫描。因为 HomeController 带有 @Controller 注解，因此组件扫描器会自动找到 HomeController，
     * 并将其声明为 Spring 应用上下文中的一个 bean。
     *
     * 其实，你也可以让 HomeController 带有 @Component 注解，它所实现的效果是一样的，但是在表意性上可能会
     * 差一些，无法确定 HomeController 是什么组件类型。
     *
     * HomeController 唯一的一个方法，也就是 home() 方法，带有 @RequestMapping 注解。它的 value 属性指
     * 定了这个方法所要处理的请求路径，method 属性细化了它所处理的 HTTP 方法。在本例中，当收到对 "/" 的 HTTP
     * GET 请求时，就会调用 home() 方法。
     *
     * 你可以看到，home() 方法其实并没有做太多的事情：它返回了一个 String 类型的 "home"。这个 String 将会
     * 被 Spring MVC 解读为要渲染的视图名称。DispatcherServlet 会要求视图解析器将这个逻辑名称解析为实际的
     * 视图。
     *
     * 鉴于配置 InternalResourceViewResolver 的方式，视图名 "home" 将会解析为 "/WEB-INF/views/home.jsp"
     * 路径的 JSP。先来看一个简单的 Spittr 应用的首页。
     *
     * 这个 JSP 并没有太多需要注意的地方。它只是欢迎应用的用户，并提供了两个链接：一个是查看Spittle列表，另一
     * 个是在应用中进行注册。
     *
     *
     *
     * 定义类级别的请求处理
     *
     * 这里要拆分 @RequestMapping，并将其路径映射部分放到类级别上。
     *
     * @Controller
     * @RequestMapping("/")
     * public class HomeController {
     *
     *     @RequestMapping(method = RequestMethod.GET)
     *     public String home(Model model) {
     *         return "home";
     *     }
     *
     * }
     *
     * 在这个新版本的 HomeController 中，路径现在被转移到类级别的 @RequestMapping 上，而 HTTP 方法依然映
     * 射在方法级别上。当控制器在类级别上添加 @RequestMapping 注解时，这个注解会应用到控制器的所有处理器方法
     * 上。处理器方法上的 @RequestMapping 注解会对类级别上的 @RequestMapping 的声明进行补充。
     *
     * 就 HomeController 而言，这里只有一个控制器方法。与类级别的 @Request-Mapping 合并之后，这个方法的
     * @RequestMapping 表明 home() 将会处理对 "/" 路径的 GET 请求。
     *
     * 换言之，其实没有改变任何功能，只是将一些代码换了个地方，但是 HomeController 所做的事情和以前是一样的。
     *
     * 当在修改 @RequestMapping 时，还可以对 HomeController 做另外一个变更。@RequestMapping 的 value
     * 属性能够接受一个 String 类型的数组。到目前为止，给它设置的都是一个 String 类型的 "/"。但是，还可以将
     * 它映射到对 "/homepage" 的请求，只需将类级别的 @RequestMapping 改为如下所示：
     *
     * @Controller
     * @RequestMapping({"/", "/homepage"})
     * public class HomeController {
     *
     *     @RequestMapping(method = RequestMethod.GET)
     *     public String home(Model model) {
     *         return "home";
     *     }
     *
     * }
     *
     * 现在，HomeController 的 home() 方法能够映射到对 "/" 和 "/homepage" 的 GET 请求。
     *
     *
     *
     * 传递模型数据到视图中
     *
     * 到现在为止，就编写超级简单的控制器来说，HomeController 已经是一个不错的样例了。但是大多数的控制器并不
     * 是这么简单。在 Spittr 应用中，需要有一个页面展现最近提交的 Spittle 列表。因此，需要一个新的方法来处理
     * 这个页面。
     *
     * 首先，需要定义一个数据访问的 Repository。为了实现解耦以及避免陷入数据库访问的细节之中，将 Repository
     * 定义为一个接口。此时，只需要一个能够获取 Spittle 列表的 Repository，具体见 SpittleRepository。
     *
     * 其中，findSpittles() 方法接受两个参数。其中 max 参数代表所返回的 Spittle 中，Spittle ID 属性的最大
     * 值，而 count 参数表明要返回多少个 Spittle 对象。为了获得最新的 20 个 Spittle 对象，可以这样进行调用。
     *
     * List<Spittle> recent = spittleRepository.findSpittles(Long.MAX_VALUE, 20);
     *
     * 现在，让 Spittle 类尽可能的简单，它的属性包括消息内容、时间戳以及 Spittle 发布时对应的经纬度。
     *
     * 就大部分内容来看，Spittle 就是一个基本的 POJO 数据对象 —— 没有什么复杂的。唯一要注意的是，使用 Apache
     * Common Lang 包来实现 equals() 和 hashCode() 方法。
     *
     * 现在，创建 SpittleController，可以看到 SpittleController 有一个构造器，这个构造器使用了 @Autowired
     * 注解，用来注入 SpittleRepository。这个 SpittleRepository 随后又用在 spittles() 方法中，用来获取
     * 最新的 spittle 列表。
     *
     * 需要注意的是，在 spittles() 方法中给定了一个 Model 作为参数。这样，spittles() 方法就能将 Repository
     * 中获取到的 Spittle 列表填充到模型中。Model 实际上就是一个 Map（也就是 key-value 对的集合），它会传递
     * 给视图，这样数据就能渲染到客户端了。当调用 addAttribute() 方法并且不指定 key 的时候，那么 key 会根据
     * 值的对象类型推断确定。在本例中，因为它是一个 List<Spittle>，因此，键将会推断为 spittleList。
     *
     * spittles() 方法所做的最后一件事是返回 spittles 作为视图的名字，这个视图会渲染模型。
     *
     * 如果你希望显式声明模型的 key 的话，那也尽可以进行指定。如下对比：
     *
     * model.addAttribute(spittleRepository.findSpittles(Long.MAX_VALUE, 20));
     *
     * 或
     *
     * model.addAttribute("spittleList",spittleRepository.findSpittles(Long.MAX_VALUE, 20));
     *
     * 如果你希望使用非 Spring 类型的话，那么可以用 java.util.Map 来代替 Model。即 将 Model 替换为 Map，
     * 将 addAttribute() 替换为 put()。
     *
     * 既然现在提到了各种可替代的方案，那下面还有另外一种方式来进行编写。即 直接调用后进行返回，如下：
     *
     * return spittleRepository.findSpittles(Long.MAX_VALUE, 20);
     *
     * 无需 addAttribute() 或 put()。
     *
     * 这个版本与其他的版本有些差别。它并没有返回视图名称，也没有显式地设定模型，这个方法返回的是 Spittle 列表。
     * 当处理器方法像这样返回对象或集合时，这个值会放到模型中，模型的 key 会根据其类型推断得出（在本例中，也就是
     * spittleList）。
     *
     * 而逻辑视图的名称将会根据请求路径推断得出。因为这个方法处理针对 "/spittles" 的 GET 请求，因此视图的名称
     * 将会是 spittles（去掉开头的斜线）。
     *
     * 不管你选择哪种方式来编写 spittles() 方法，所达成的结果都是相同的。模型中会存储一个 Spittle 列表，key
     * 为 spittleList，然后这个列表会发送到名为 spittles 的视图中。按照配置 InternalResourceViewResolver
     * 的方式，视图的 JSP 将会是 "/WEB-INF/views/spittles.jsp"。
     *
     * 现在，数据已经放到了模型中，在 JSP 中该如何访问它呢？实际上，当视图是 JSP 的时候，模型数据会作为请求属性
     * 放到请求（request）之中。因此，在 spittles.jsp 文件中可以使用JSTL（JavaServer Pages Standard Tag
     * Library）的 <c:forEach> 标签渲染 spittle 列表：
     *
     *         <c:forEach items="${spittleList}" var="spittle" >
     *           <li id="spittle_<c:out value="spittle.id"/>">
     *             <div class="spittleMessage"><c:out value="${spittle.message}" /></div>
     *             <div>
     *               <span class="spittleTime"><c:out value="${spittle.time}" /></span>
     *               <span class="spittleLocation">
     *                   (<c:out value="${spittle.latitude}" />, <c:out value="${spittle.longitude}" />)
     *                   </span>
     *             </div>
     *           </li>
     *         </c:forEach>
     *
     * 尽管 SpittleController 很简单，但是它依然比 HomeController 更进一步了。不过，SpittleController 和
     * HomeController 都没有处理任何形式的输入。后续将扩展 SpittleController，让它从客户端接受一些输入。
     */
    public static void main(String[] args) {

    }

}
