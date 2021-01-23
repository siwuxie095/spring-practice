package com.siwuxie095.spring.chapter5th.example8th;

/**
 * @author Jiajing Li
 * @date 2021-01-23 21:51:40
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 处理表单
     *
     * Web 应用的功能通常并不局限于为用户推送内容。大多数的应用允许用户填充表单并将数据提交回应用中，通过这种
     * 方式实现与用户的交互。像提供内容一样，Spring MVC 的控制器也为表单处理提供了良好的支持。
     *
     * 使用表单分为两个方面：展现表单以及处理用户通过表单提交的数据。在 Spittr 应用中，需要有个表单让新用户进
     * 行注册。SpitterController 是一个新的控制器，目前只有一个请求处理的方法来展现注册表单。如下：
     *
     * @Controller
     * @RequestMapping("/spitter")
     * public class SpitterController {
     *
     *     @RequestMapping(value = "/register", method = GET)
     *     public String showRegistrationForm() {
     *         return "registerForm";
     *     }
     *
     * }
     *
     * showRegistrationForm() 方法的 @RequestMapping 注解以及类级别上的 @RequestMapping 注解组合起来，
     * 声明了这个方法要处理的是针对 "/spitter/register" 的 GET 请求。这是一个简单的方法，没有任何输入并且
     * 只是返回名为 registerForm 的逻辑视图。按照配置 InternalResourceViewResolver的方式，这意味着将会
     * 使用 "/WEB-INF/views/registerForm.jsp" 这个 JSP 来渲染注册表单。
     *
     * 因为视图的名称为 registerForm，所以 JSP 的名称需要是 registerForm.jsp。这个 JSP 必须要包含一个
     * HTML <form> 标签，在这个标签中用户输入注册应用的信息。如下就是现在所要使用的 JSP 片段：
     *
     *     <form method="POST">
     *       First Name: <input type="text" name="firstName" /><br/>
     *       Last Name: <input type="text" name="lastName" /><br/>
     *       Email: <input type="email" name="email" /><br/>
     *       Username: <input type="text" name="username" /><br/>
     *       Password: <input type="password" name="password" /><br/>
     *       <input type="submit" value="Register" />
     *     </form>
     *
     * 可以看到，这个 JSP 非常基础。它的 HTML 表单域中记录用户的名字、姓氏、用户名以及密码，然后还包含一个提交
     * 表单的按钮。
     *
     * 需要注意的是：这里的 <form> 标签中并没有设置 action 属性。在这种情况下，当表单提交时，它会提交到与展现
     * 时相同的 URL 路径上。也就是说，它会提交到 "/spitter/register" 上。
     *
     * 这就意味着需要在服务器端处理该 HTTP POST 请求。下面将在 SpitterController 中再添加一个方法来处理这个
     * 表单提交。
     *
     *
     *
     * 编写处理表单的控制器
     *
     * 当处理注册表单的 POST 请求时，控制器需要接受表单数据并将表单数据保存为 Spitter 对象。最后，为了防止重复
     * 提交（用户点击浏览器的刷新按钮有可能会发生这种情况），应该将浏览器重定向到新创建用户的基本信息页面。
     *
     * 在处理 POST 类型的请求时，在请求处理完成后，最好进行一下重定向，这样浏览器的刷新就不会重复提交表单了。
     *
     * 如下是处理表单提交的控制器方法：
     *
     *     @RequestMapping(value = "/register", method = POST)
     *     public String processRegistration(Spitter spitter) {
     *         spitterRepository.save(spitter);
     *         return "redirect:/spitter/" + spitter.getUsername();
     *     }
     *
     * 可以看到，这个方法并没有做太多的事情。它接受一个Spitter对象作为参数。
     *
     * 这个对象有 firstName、lastName、username 和 password 属性，这些属性将会使用请求中同名的参数进行填充。
     *
     * 当使用 Spitter 对象调用 processRegistration() 方法时，它会进而调用 SpitterRepository 的 save()
     * 方法，SpitterRepository 是在 SpitterController 的构造器中注入进来的。
     *
     * processRegistration() 方法做的最后一件事就是返回一个 String 类型，用来指定视图。但是这个视图格式和以
     * 前所看到的视图有所不同。这里不仅返回了视图的名称供视图解析器查找目标视图，而且返回的值还带有重定向的格式。
     *
     * 当 InternalResourceViewResolver 看到视图格式中的 "redirect:" 前缀时，它就知道要将其解析为重定向的规
     * 则，而不是视图的名称。在本例中，它将会重定向到用户基本信息的页面。例如，如果 Spitter.username 属性的值为
     * "jbauer"，那么视图将会重定向到 "/spitter/jbauer"。
     *
     * 需要注意的是，除了 "redirect:"，InternalResourceViewResolver 还能识别 "forward:" 前缀。当它发现视
     * 图格式中以 "forward:" 作为前缀时，请求将会前往（forward）指定的 URL 路径，而不再是重定向。
     *
     * 因为重定向到了用户基本信息页面，那么应该往 SpitterController 中添加一个处理器方法，用来处理对基本信息页
     * 面的请求。如下的 showSpitterProfile() 将会完成这项任务：
     *
     *     @RequestMapping(value = "/{username}", method = GET)
     *     public String showSpitterProfile(@PathVariable String username, Model model) {
     *         Spitter spitter = spitterRepository.findByUsername(username);
     *         model.addAttribute(spitter);
     *         return "profile";
     *     }
     *
     * SpitterRepository 通过用户名获取一个 Spitter 对象，showSpitterProfile() 得到这个对象并将其添加到模
     * 型中，然后返回 profile，也就是基本信息页面的逻辑视图名。现在的基本信息视图非常简单：
     *
     *     <h1>Your Profile</h1>
     *     <c:out value="${spitter.username}" /><br/>
     *     <c:out value="${spitter.firstName}" /> <c:out value="${spitter.lastName}" /><br/>
     *     <c:out value="${spitter.email}" />
     *
     * 如果表单中没有发送 username 或 password 的话，会发生什么情况呢？或者说，如果 firstName 或 lastName
     * 的值为空或太长的话，又会怎么样呢？接下来，看一下如何为表单提交添加校验，从而避免数据呈现的不一致性。
     *
     *
     *
     * 校验表单
     *
     * 如果用户在提交表单的时候，username 或 password 文本域为空的话，那么将会导致在新建 Spitter 对象中，
     * username 或 password 是空的 String。至少这是一种怪异的行为。如果这种现象不处理的话，这将会出现安全问题，
     * 因为不管是谁只要提交一个空的表单就能登录应用。
     *
     * 同时，还应该阻止用户提交空的 firstName 和 lastName，使应用仅在一定程度上保持匿名性。有个好的办法就是限制
     * 这些输入域值的长度，保持它们的值在一个合理的长度范围，避免这些输入域的误用。
     *
     * 有种处理校验的方式非常初级，那就是在 processRegistration() 方法中添加代码来检查值的合法性，如果值不合法
     * 的话，就将注册表单重新显示给用户。这是一个很简短的方法，因此，添加一些额外的 if 语句也不是什么大问题。
     *
     * 与其让校验逻辑弄乱我们的处理器方法，还不如使用 Spring 对 Java 校验 API（Java Validation API，又称
     * JSR-303）的支持。从 Spring 3.0 开始，在 Spring MVC 中提供了对 Java 校验 API 的支持。在 Spring MVC
     * 中要使用 Java 校验 API 的话，并不需要什么额外的配置。只要保证在类路径下包含这个 Java API 的实现即可，比如
     * Hibernate Validator。Java 校验 API 定义了多个注解，这些注解可以放到属性上，从而限制这些属性的值。所有的
     * 注解都位于 javax.validation.constraints 包中。如下列出了这些校验注解：
     * （1）@AssertFalse：所注解的元素必须是 Boolean 类型，并且值为 false；
     * （2）@AssertTrue：所注解的元素必须是 Boolean 类型，并且值为 true；
     * （3）@DecimalMax：所注解的元素必须是数字，并且它的值要小于或等于给定的 BigDecimalString 值；
     * （4）@DecimalMin：所注解的元素必须是数字，并且它的值要大于或等于给定的 BigDecimalString 值；
     * （5）@Digits：所注解的元素必须是数字，并且它的值必须有指定的位数；
     * （6）@Future：所注解的元素的值必须是一个将来的日期；
     * （7）@Max：所注解的元素必须是数字，并且它的值要小于或等于给定的值；
     * （8）@Min：所注解的元素必须是数字，并且它的值要大于或等于给定的值；
     * （9）@NotNull：所注解元素的值必须不能为 null；
     * （10）@Null：所注解元素的值必须为 null；
     * （11）@Past：所注解的元素的值必须是一个已过去的日期；
     * （12）@Pattern：所注解的元素的值必须匹配给定的正则表达式；
     * （13）@Size：所注解的元素的值必须是 String、集合或数组，并且它的长度要符合给定的范围。
     *
     * 除了上面的注解，Java 校验 API 的实现可能还会提供额外的校验注解。同时，也可以定义自己的限制条件。但就这里来
     * 讲，将会关注于上面的两个核心限制条件。
     *
     * 请考虑要添加到 Spitter 域上的限制条件，似乎需要使用 @NotNull 和 @Size 注解。所要做的事情就是将这些注解
     * 添加到 Spitter 的属性上。在 Spitter 类中，它的属性已经添加了校验注解：
     *
     *     @NotNull
     *     @Size(min = 5, max = 16)
     *     private String username;
     *
     *     @NotNull
     *     @Size(min = 5, max = 25)
     *     private String password;
     *
     *     @NotNull
     *     @Size(min = 2, max = 30)
     *     private String firstName;
     *
     *     @NotNull
     *     @Size(min = 2, max = 30)
     *     private String lastName;
     *
     *     ...
     *
     * 现在，Spitter 的所有属性都添加了 @NotNull 注解，以确保它们的值不为 null。类似地，属性上也添加了 @Size
     * 注解以限制它们的长度在最大值和最小值之间。对 Spittr 应用来说，这意味着用户必须要填完注册表单，并且值的长度
     * 要在给定的范围内。
     *
     * 已经为 Spitter 添加了校验注解，接下来需要修改 processRegistration() 方法来应用校验功能。启用校验功能的
     * processRegistration() 如下所示：
     *
     *     @RequestMapping(value = "/register", method = POST)
     *     public String processRegistration(@Valid Spitter spitter,
     *                                       Errors errors) {
     *         if (errors.hasErrors()) {
     *             return "registerForm";
     *         }
     *
     *         spitterRepository.save(spitter);
     *         return "redirect:/spitter/" + spitter.getUsername();
     *     }
     *
     * 与最初的 processRegistration() 方法相比，这里有了很大的变化。Spitter 参数添加了 @Valid 注解，这会告知
     * Spring，需要确保这个对象满足校验限制。
     *
     * 在 Spitter 属性上添加校验限制并不能阻止表单提交。即便用户没有填写某个域或者某个域所给定的值超出了最大长度，
     * processRegistration() 方法依然会被调用。这样，就需要处理校验的错误，就像在 processRegistration() 方法
     * 中所看到的那样。
     *
     * 如果有校验出现错误的话，那么这些错误可以通过 Errors 对象进行访问，现在这个对象已作为 processRegistration()
     * 方法的参数（很重要一点需要注意，Errors 参数要紧跟在带有 @Valid 注解的参数后面，@Valid 注解所标注的就是要
     * 检验的参数）。processRegistration() 方法所做的第一件事就是调用 Errors.hasErrors() 来检查是否有错误。
     *
     * 如果有错误的话，Errors.hasErrors() 将会返回到 registerForm，也就是注册表单的视图。这能够让用户的浏览器
     * 重新回到注册表单页面，所以他们能够修正错误，然后重新尝试提交。现在，会显示空的表单，但是后续将在表单中显示最
     * 初提交的值并将校验错误反馈给用户。
     *
     * 如果没有错误的话，Spitter 对象将会通过 Repository 进行保存，控制器会像之前那样重定向到基本信息页面。
     *
     * PS：所有代码请参见 example6th。
     */
    public static void main(String[] args) {

    }

}
