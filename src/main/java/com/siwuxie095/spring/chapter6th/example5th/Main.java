package com.siwuxie095.spring.chapter6th.example5th;

/**
 * @author Jiajing Li
 * @date 2021-01-25 08:17:18
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 使用 Spring 的 JSP 库
     *
     * 当为 JSP 添加功能时，标签库是一种很强大的方式，能够避免在脚本块中直接编写 Java 代码。Spring 提供了两个
     * JSP 标签库，用来帮助定义 Spring MVC Web 的视图。其中一个标签库会用来渲染 HTML 表单标签，这些标签可以
     * 绑定 model 中的某个属性。另外一个标签库包含了一些工具类标签，随时都可以非常便利地使用它们。
     *
     * 在这两个标签库中，你可能会发现表单绑定的标签库更加有用。所以，这里就从这个标签库开始学习 Spring 的 JSP
     * 标签。你将会看到如何将 Spittr 应用的注册表单绑定到模型上，这样表单就可以预先填充值，并且在表单提交失败
     * 后，能够展现校验错误。
     *
     *
     *
     * 1、将表单绑定到模型上
     *
     * Spring 的表单绑定 JSP 标签库包含了 14 个标签，它们中的大多数都用来渲染 HTML 中的表单标签。但是，它们
     * 与原生 HTML 标签的区别在于它们会绑定模型中的一个对象，能够根据模型中对象的属性填充值。标签库中还包含了
     * 一个为用户展现错误的标签，它会将错误信息渲染到最终的 HTML 之中。
     *
     * 为了使用表单绑定库，需要在 JSP 页面中对其进行声明：
     *
     * <%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
     *
     * 需要注意，这里将前缀指定为 "sf"，但通常也可能使用 "form" 前缀。你可以选择任意喜欢的前缀，我之所以选择
     * "sf" 是因为它很简洁、易于输入，并且还是 Spring form 的简写形式。
     *
     * 在声明完表单绑定标签库之后，你就可以使用 14 个相关的标签了。借助 Spring 表单绑定标签库中所包含的标签，
     * 能够将模型对象绑定到渲染后的 HTML 表单中。这些标签如下：
     * （1）<sf:checkbox>：渲染成一个 HTML <input> 标签，其中 type 属性设置为 checkbox；
     * （2）<sf:checkboxes>：渲染成多个 HTML <input> 标签，其中 type 属性设置为 checkbox；
     * （3）<sf:errors>：在一个 HTML <span> 中渲染输入域的错误；
     * （4）<sf:form>：渲染成一个 HTML <form> 标签，并为其内部标签暴露绑定路径，用于数据绑定；
     * （5）<sf:hidden>：渲染成一个 HTML <input> 标签，其中 type 属性设置为 hidden；
     * （6）<sf:input>：渲染成一个 HTML <input> 标签，其中 type 属性设置为 text；
     * （7）<sf:label>：渲染成一个 HTML <label> 标签；
     * （8）<sf:option>：渲染成一个 HTML <option> 标签，其 selected 属性根据所绑定的值进行设置；
     * （9）<sf:options>：按照绑定的集合、数组或 Map，渲染成一个 HTML <option> 标签的列表；
     * （10）<sf:password>：渲染成一个 HTML <input> 标签，其中 type 属性设置为 password；
     * （11）<sf:radiobutton>：渲染成一个 HTML <input> 标签，其中 type 属性设置为 radio；
     * （12）<sf:radiobuttons>：渲染成多个 HTML <input> 标签，其中 type 属性设置为 radio；
     * （13）<sf:select>：渲染为一个 HTML <select> 标签；
     * （14）<sf:textarea>：渲染为一个 HTML <textarea> 标签。
     *
     * 要在一个样例中介绍所有的这些标签是很困难的，如果一定要这样做的话，肯定也会非常牵强。就 Spittr 样例来说，
     * 只会用到适合于 Spittr 应用中注册表单的标签。具体来讲，也就是 <sf:form>、<sf:input> 和 <sf:password>。
     * 在注册 JSP 中使用这些标签后，所得到的程序如下所示：
     *
     *     <sf:form method="POST" commandName="spitter" >
     *       First Name:<sf:input path="firstName" /><br/>
     *       Last Name>:<sf:input path="lastName" /><br/>
     *       Email:<sf:input path="email" /><br/>
     *       Username>:<sf:input path="username" /><br/>
     *       Password:<sf:password path="password"" /><br/>
     *       <input type="submit" value="Register" />
     *     </sf:form>
     *
     * <sf:form> 会渲染会一个 HTML <form> 标签，但它也会通过 commandName 属性构建针对某个模型对象的上下文
     * 信息。在其他的表单绑定标签中，会引用这个模型对象的属性。
     *
     * 在之前的代码中，将 commandName 属性设置为 spitter。因此，在模型中必须要有一个 key 为 spitter 的对象，
     * 否则的话，表单不能正常渲染（会出现 JSP 错误）。这意味着需要修改一下 SpitterController，以确保模型中存
     * 在以 spitter 为 key 的 Spitter 对象：
     *
     *     @RequestMapping(value = "/register", method = GET)
     *     public String showRegistrationForm(Model model) {
     *         model.addAttribute(new Spitter());
     *         return "registerForm";
     *     }
     *
     * 修改后的 showRegistrationForm() 方法中，新增了一个 Spitter 实例到模型中。模型中的 key 是根据对象
     * 类型推断得到的，也就是 spitter，与所需要的完全一致。
     *
     * 回到这个表单中，前四个输入域将 HTML <input> 标签改成了 <sf:input>。这个标签会渲染成一个 HTML <input>
     * 标签，并且 type 属性将会设置为 text。在这里设置了 path 属性，<input> 标签的 value 属性值将会设置为
     * 模型对象中 path 属性所对应的值。例如，如果在模型中 Spitter 对象的 firstName 属性值为 Jack，那么
     * <sf:input path="firstName"/> 所渲染的 <input> 标签中，会存在 value="Jack"。
     *
     * 对于 password 输入域，使用 <sf:password> 来代替 <sf:input>。<sf:password> 与 <sf:input> 类似，
     * 但是它所渲染的 HTML <input> 标签中，会将 type 属性设置为 password，这样当输入的时候，它的值不会直接
     * 明文显示。
     *
     * 为了帮助读者了解最终的 HTML 看起来是什么样子的，假设有个用户已经提交了表单，但值都是不合法的。校验失败后，
     * 用户会被重定向到注册表单。
     *
     * 相对于标准的 HTML 标签，使用 Spring 的表单绑定标签能够带来一定的功能提升，在校验失败后，表单中会预先填
     * 充之前输入的值。但是，这依然没有告诉用户错在什么地方。为了指导用户矫正错误，需要使用 <sf:errors>。
     *
     *
     *
     * 2、展现错误
     *
     * 如果存在校验错误的话，请求中会包含错误的详细信息，这些信息是与模型数据放到一起的。所需要做的就是到模型中
     * 将这些数据抽取出来，并展现给用户。<sf:errors> 能够让这项任务变得很简单。
     *
     * 例如，看一下将 <sf:errors> 用到 registerForm.jsp 中的代码片段：
     *
     *     <sf:form method="POST" commandName="spitter" >
     *       First Name:<sf:input path="firstName" />
     *          <sf:errors path="firstName" /><br/>
     *       ...
     *     </sf:form>
     *
     * 尽管这里只展现了将 <sf:errors> 用到 First Name 输入域的场景，但是它可以按照同样简单的方式用到注册表单
     * 的其他输入域中。在这里，它的 path 属性设置成了 firstName，也就是指定了要显示 Spitter 模型对象中哪个属
     * 性的错误。如果 firstName 属性没有错误的话，那么 <sf:errors> 不会渲染任何内容。但如果有校验错误的话，
     * 那么它将会在一个 HTML <span> 标签中显示错误信息。
     *
     * 现在，已经可以为用户展现错误信息，这样他们就能修正这些错误了。可以更进一步，修改错误的样式，使其更加突出显
     * 示。为了做到这一点，可以设置 cssClass 属性：
     *
     *     <sf:form method="POST" commandName="spitter" >
     *       First Name:<sf:input path="firstName" />
     *          <sf:errors path="firstName" cssClass="error" /><br/>
     *       ...
     *     </sf:form>
     *
     * 同样，简单起见，这里只会展现如何为 firstName 输入域的 <sf:errors> 设置 cssClass 属性。你可以将其用到
     * 其他的输入域上。
     *
     * 现在 errors 的 <span> 会有一个值为 error 的 class 属性。剩下需要做的就是为这个类定义 CSS 样式。如下
     * 就是一个简单的 CSS 样式，它会将错误信息渲染为红色：
     *
     * span.error {
     *     color: red;
     * }
     *
     * 在输入域的旁边展现错误信息是一种很好的方式，这样能够引起用户的关注，提醒他们修正错误。但这样也会带来布局的
     * 问题。另外一种处理校验错误方式就是将所有的错误信息在同一个地方进行显示。为了做到这一点，可以移除每个输入域
     * 上的 <sf:errors> 元素，并将其放到表单的顶部，如下所示：
     *
     *     <sf:form method="POST" commandName="spitter" >
     *       <sf:errors path="*" element="div" cssClass="errors" />
     *       ...
     *     </sf:form>
     *
     * 这个 <sf:errors> 与之前相比，值得注意的不同之处在于它的 path 被设置成了 "*"。这是一个通配符选择器，会
     * 告诉 <sf:errors> 展现所有属性的所有错误。
     *
     * 同样需要注意的是，这里将 element 属性设置成了 div。默认情况下，错误都会渲染在一个 HTML <span> 标签中，
     * 如果只显示一个错误的话，这是不错的选择。但是，如果要渲染所有输入域的错误的话，很可能要展现不止一个错误，
     * 这时候使用 <span> 标签（行内元素）就不合适了。像 <div> 这样的块级元素会更为合适。因此，可以将 element
     * 属性设置为 div，这样的话，错误就会渲染在一个 <div> 标签中。
     *
     * 像之前一样，cssClass 属性被设置 errors，这样就能为 <div> 设置样式。如下为 <div> 的 CSS 样式，它具有
     * 红色的边框和浅红色的背景：
     *
     * div.errors {
     *     background-color: #ffcccc;
     *     border: 2px solid red;
     * }
     *
     * 现在，在表单的上方显示所有的错误，这样页面布局可能会更加容易一些。但是，还没有着重显示需要修正的输入域。
     * 通过为每个输入域设置 cssErrorClass 属性，这个问题很容易解决。也可以将每个 label 都替换为 <sf:label>，
     * 并设置它的 cssErrorClass 属性。如下就是做完必要修改后的 First Name 输入域：
     *
     *     <sf:form method="POST" commandName="spitter" >
     *       <sf:errors path="*" element="div" cssClass="errors" />
     *       <sf:label path="firstName"
     *           cssErrorClass="error">First Name</sf:label>:
     *         <sf:input path="firstName" cssErrorClass="error" /><br/>
     *         ...
     *     </sf:form>
     *
     * <sf: label> 标签像其他的表单绑定标签一样，使用 path 来指定它属于模型对象中的哪个属性。在本例中，将其设置
     * 为 firstName，因此它会绑定 Spitter 对象的 firstName 属性。
     *
     * 就其自身来说，设置 <sf:label> 的 path 属性并没有完成太多的功能。但是，这里还同时设置了 cssErrorClass
     * 属性。
     *
     * 与之类似，<sf:input> 标签的 cssErrorClass 属性被设置为 error。如果有任何校验错误的话，在渲染得到的
     * <input> 标签中，class 属性将会被设置为 error。现在已经为文本标记和输入域设置了样式，这样当出现错误的
     * 时候，会将用户的注意力转移到此处。例如，如下的 CSS 会将文本标记渲染为红色，并将输入域设置为浅红色背景：
     *
     * label.error {
     *     color: red;
     * }
     *
     * input.error {
     *     background-color: #ffcccc;
     * }
     *
     * 现在，有了很好的方式为用户展现错误信息。不过，还可以做另外一件事情，能够让这些错误信息更加易读。重新看一下
     * Spitter 类，可以在校验注解上设置 message 属性，使其引用对用户更为友好的信息，而这些信息可以定义在属性文
     * 件中：
     *
     *     @NotNull
     *     @Size(min = 5, max = 16, message = "{username.size}")
     *     private String username;
     *
     *     @NotNull
     *     @Size(min = 5, max = 25, message = "{password.size}")
     *     private String password;
     *
     *     @NotNull
     *     @Size(min = 2, max = 30, message = "{firstName.size}")
     *     private String firstName;
     *
     *     @NotNull
     *     @Size(min = 2, max = 30, message = "{lastName.size}")
     *     private String lastName;
     *
     *     ...
     *
     * 对于上面每个域，都将其 @Size 注解的 message 设置为一个字符串，这个字符串是用大括号括起来的。如果没有大括
     * 号的话，message 中的值将会作为展现给用户的错误信息。但是使用了大括号之后，使用的就是属性文件中的某一个属性，
     * 该属性包含了实际的信息。
     *
     * 接下来需要做的就是创建一个名为 ValidationMessages.properties 的文件，并将其放在根类路径之下：
     *
     * firstName.size=First name must be between {min} and {max} characters long.
     * lastName.size=Last name must be between {min} and {max} characters long.
     * username.size=Username must be between {min} and {max} characters long.
     * password.size=Password must be between {min} and {max} characters long.
     * email.valid=The email address must be valid.
     *
     * ValidationMessages.properties 文件中每条信息的 key 值对应于注解中 message 属性占位符的值。同时，最小
     * 和最大长度没有硬编码在 ValidationMessages.properties 文件中，在这个用户友好的信息中也有自己的占位符 ——
     * {min} 和 {max} —— 它们会引用 @Size 注解上所设置的 min 和 max 属性。
     *
     * 将这些错误信息抽取到属性文件中还会带来一个好处，那就是可以通过创建地域相关的属性文件，为用户展现特定语言和地
     * 域的信息。例如，如果用户的浏览器设置成了西班牙语，那么就应该用西班牙语展现错误信息，需要创建一个名为
     * ValidationErrors_es.properties 的文件，内容如下：
     *
     * firstName.size=Nombre debe ser entre {min} y {max} caracteres largo.
     * lastName.size=El apellido debe ser entre {min} y {max} caracteres largo.
     * username.size=Nombre de usuario debe ser entre {min} y {max} caracteres largo.
     * password.size=Contrasena debe estar entre {min} y {max} caracteres largo.
     * email.valid=La direccion de email no es valida
     *
     * 可以按需创建任意数量的 ValidationMessages.properties 文件，使其涵盖想支持的所有语言和地域。
     *
     *
     *
     * 3、Spring 通用的标签库
     *
     * 除了表单绑定标签库之外，Spring 还提供了更为通用的 JSP 标签库。实际上，这个标签库是 Spring 中最早的标签库。
     * 这么多年来，它有所变化，但是在最早版本的 Spring 中，它就已经存在了。
     *
     * 要使用 Spring 通用的标签库，必须要在页面上对其进行声明：
     *
     * <%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
     *
     * 与其他 JSP 标签库一样，prefix 可以是任意你所喜欢的值。在这里，通用的做法是将这个标签库的前缀设置为 spring。
     * 但是，这里将其设置为 "s"，因为它更加简洁，更易于阅读和输入。
     *
     * 标签库声明之后，就可以使用如下的十个 JSP 标签了。Spring 的 JSP 标签库中提供了多个便利的标签，还包括一些遗
     * 留的数据绑定标签。
     * （1）<s:bind>：将绑定属性的状态导出到一个名为 status 的页面作用域属性中，与 <s:path> 组合使用获取绑定属
     * 性的值；
     * （2）<s:escapeBody>：将标签体中的内容进行 HTML 和/或 JavaScript 转义；
     * （3）<s:hasBindErrors>：根据指定模型对象（在请求属性中）是否有绑定错误，有条件地渲染内容；
     * （4）<s:htmlEscape>：为当前页面设置默认的 HTML 转义值；
     * （5）<s:message>：根据给定的编码获取信息，然后要么进行渲染（默认行为），要么将其设置为页面作用域、请求作用
     * 域、会话作用域或应用作用域的变量（通过使用 var 和 scope 属性实现）；
     * （6）<s:nestedPath>：设置嵌入式的 path，用于 <s:bind> 之中；
     * （7）<s:theme>：根据给定的编码获取主题信息，然后要么进行渲染（默认行为），要么将其设置为页面作用域、请求作
     * 用域、会话作用域或应用作用域的变量（通过使用 var 和 scope 属性实现）；
     * （8）<s:transform>：使用命令对象的属性编辑器转换命令对象中不包含的属性；
     * （9）<s:url>：创建相对于上下文的 URL，支持 URI 模板变量以及 HTML/XML/JavaScript 转义。可以渲染 URL
     * （默认行为），也可以将其设置为页面作用域、请求作用域、会话作用域或应用作用域的变量（通过使用 var 和 scope
     * 属性实现）；
     * （10）<s:eval>：计算符合 Spring 表达式语言（Spring Expression Language，SpEL）语法的某个表达式的值，
     * 然后要么进行渲染（默认行为），要么将其设置为页面作用域、请求作用域、会话作用域或应用作用域的变量（通过使用
     * var 和 scope 属性实现）。
     *
     * 其中的一些标签已经被 Spring 表单绑定标签库淘汰了。例如，<s:bind> 标签就是 Spring 最初所提供的表单绑定
     * 标签，它比前面所介绍的标签复杂得多。
     *
     * 因为这些标签库的行为比表单绑定标签少得多，所以这里不会详细介绍每个标签，而是快速介绍几个最为有用的标签，其
     * 余的可以自行去学习和探索。
     *
     *
     *
     * 4、展现国际化信息
     *
     * 到现在为止，这里的 JSP 模板包含了很多硬编码的文本。这其实也算不上什么大问题，但是如果你要修改这些文本的话，
     * 就不那么容易了。而且，没有办法根据用户的语言设置国际化这些文本。
     *
     * 例如，考虑首页中的欢迎信息：
     *
     * <h1>Welcome to Spittr!</h1>
     *
     * 修改这个信息的唯一办法是打开 home.jsp，然后对其进行变更。这可能算不上什么大事，但是，应用中的文本散布到多
     * 个模板中，如果要大规模修改应用的信息时，你需要修改大量的 JSP 文件。
     *
     * 另外一个更为重要的问题在于，不管你选择什么样的欢迎信息，所有的用户都会看到同样的信息。Web 是全球性的网络，
     * 你所构建的应用很可能会有全球化用户。因此，最好能够使用用户的语言与其进行交流，而不是只使用某一种语言。
     *
     * 对于渲染文本来说，是很好的方案，文本能够位于一个或多个属性文件中。 借助 <s:message>，可以将硬编码的欢迎信息
     * 替换为如下的形式：
     *
     * <h1><s:message code="spitter.welcome" /></h1>
     *
     * 按照这里的方式，<s:message> 将会根据 key 为 spittr.welcome 的信息源来渲染文本。因此，如果希望 <s:message>
     * 能够正常完成任务的话，就需要配置一个这样的信息源。
     *
     * Spring 有多个信息源的类，它们都实现了 MessageSource 接口。在这些类中，更为常见和有用的是
     * ResourceBundleMessageSource。它会从一个属性文件中加载信息，这个属性文件的名称是根据基础名称（base name）
     * 衍生而来的。如下的 @Bean 方法配置了 ResourceBundleMessageSource：
     *
     *     @Bean
     *     public MessageSource messageSource() {
     *         ResourceBundleMessageSource messageSource =
     *                 new ResourceBundleMessageSource();
     *         messageSource.setBasename("messages");
     *         return messageSource;
     *     }
     *
     * 在这个 bean 声明中，核心在于设置 basename 属性。你可以将其设置为任意你喜欢的值，在这里，将其设置为 messages。
     * 将其设置为 message 后，ResourceBundleMessageSource 就会试图在根路径的属性文件中解析信息，这些属性文件的
     * 名称是根据这个基础名称衍生得到的。
     *
     * 另外的可选方案是使用 ReloadableResourceBundleMessageSource，它的工作方式与 ResourceBundleMessageSource
     * 非常类似，但是它能够重新加载信息属性，而不必重新编译或重启应用。如下是配置样例：
     *
     *     @Bean
     *     public MessageSource messageSource() {
     *         ReloadableResourceBundleMessageSource messageSource =
     *                 new ReloadableResourceBundleMessageSource();
     *         messageSource.setBasename("file:///Users/siwuxie095/messages");
     *         messageSource.setCacheSeconds(10);
     *         return messageSource;
     *     }
     *
     * 这里的关键区别在于 basename 属性设置为在应用的外部查找（而不是像 ResourceBundleMessageSource 那样在类路
     * 径下查找）。basename 属性可以设置为在类路径下（以 "classpath:" 作为前缀）、文件系统中（以 "file:" 作为前
     * 缀）或 Web 应用的根路径下（没有前缀）查找属性。在这里，将其配置为在服务器文件系统的 "/Users/siwuxie095"
     * 目录下的属性文件中查找信息，并且基础的文件名为 "messages"。
     *
     * 现在，来创建这些属性文件。首先，创建默认的属性文件，名为 messages. properties。它要么位于根类路径下（如果
     * 使用 ResourceBundleMessageSource 的话），要么位于 pathname 属性指定的路径下（如果使用
     * ReloadableResourceBundleMessageSource 的话）。对spittr.welcome 信息来讲，它需要如下的条目：
     *
     * spitter.welcome=Welcome to Spitter!
     *
     * 如果你不再创建其他信息文件的话，那么所做的事情就是将 JSP 中硬编码的信息抽取到了属性文件中，依然作为硬编码的信
     * 息。它能够一站式地修改应用中的所有信息，但是它所完成的任务并不限于此。
     *
     * 这里已经具备了对信息进行国际化的重要组成部分。例如，如果你想要为语言设置为西班牙语的用户展现西班牙语的欢迎信息，
     * 那么需要创建另外一个名为 messages_es.properties 的属性文件，并包含如下的条目：
     *
     * spitter.welcome=Bienvenidos a Spitter!
     *
     *
     *
     * 5、创建 URL
     *
     * <s:url> 是一个很小的标签。它主要的任务就是创建 URL，然后将其赋值给一个变量或者渲染到响应中。它是 JSTL 中
     * <c:url> 标签的替代者，但是它具备几项特殊的技巧。
     *
     * 按照其最简单的形式，<s:url> 会接受一个相对于 Servlet 上下文的 URL，并在渲染的时候，预先添加上 Servlet 上
     * 下文路径。例如，考虑如下 <s:url> 的基本用法：
     *
     * <a href="<s:url value="/spittles/register" />">Register</a>
     *
     * 这样，在创建 URL 的时候，就不必再担心 Servlet 上下文路径是什么了，<s:url> 将会负责这件事。
     *
     * 另外，还可以使用 <s:url> 创建 URL，并将其赋值给一个变量供模板在稍后使用：
     *
     * <s:url value="/spitter/register" var="registerUrl" />
     * <a href="${registerUrl}">Register</a>
     *
     * 默认情况下，URL 是在页面作用域内创建的。但是通过设置 scope 属性，可以让 <s:url> 在应用作用域内、会话作用域
     * 内或请求作用域内创建 URL：
     *
     * <s:url value="/spitter/register" var="registerUrl" scope="request" />
     *
     * 如果希望在 URL 上添加参数的话，那么你可以使用 <s:param> 标签。比如，如下的 <s:url> 使用两个内嵌的 <s:param>
     * 标签，来设置 "/spittles" 的 max 和 count 参数：
     *
     * <s:url value="/spittles" var="spittlesUrl">
     *     <s:param name="max" value="60" />
     *     <s:param name="count" value="20" />
     * </s:url>
     *
     * 到目前为止，还没有看到 <s:url> 能够实现，而 JSTL 的 <c:url> 无法实现的功能。但是，如果需要创建带有路径
     * （path）参数的 URL 该怎么办呢？该如何设置 href 属性，使其具有路径变量的占位符呢？
     *
     * 例如，假设需要为特定用户的基本信息页面创建一个 URL。那没有问题，<s:param> 标签可以承担此任：
     *
     * <s:url value="/spitter/{username}" var="spitterUrl">
     *     <s:param name="username" value="jbauer" />
     * </s:url>
     *
     * 当 href 属性中的占位符匹配 <s:param> 中所指定的参数时，这个参数将会插入到占位符的位置中。如果 <s:param>
     * 参数无法匹配 href 中的任何占位符，那么这个参数将会作为查询参数。
     *
     * <s:url> 标签还可以解决 URL 的转义需求。例如，如果你希望将渲染得到的 URL 内容展现在 Web 页面上（而不是作为
     * 超链接），那么你应该要求 <s:url> 进行 HTML 转义，这需要将 htmlEscape 属性设置为 true。例如，如下的
     * <s:url> 将会渲染 HTML 转义后的 URL：
     *
     * <s:url value="/spittles" htmlEscape="true">
     *     <s:param name="max" value="60" />
     *     <s:param name="count" value="20" />
     * </s:url>
     *
     * 另一方面，如果你希望在 JavaScript 代码中使用 URL 的话，那么应该将 javaScriptEscape 属性设置为 true：
     *
     * <s:url value="/spittles" var="spittlesJSUrl" javaScriptEscape="true">
     *     <s:param name="max" value="60" />
     *     <s:param name="count" value="20" />
     * </s:url>
     * <script>
     *     var spittlesUrl = "${spittlesJSUrl}"
     * </script>
     *
     * 既然提到了转义，有一个标签专门用来转义内容，而不是转义标签。下面来看一下。
     *
     *
     *
     * 6、转义内容
     *
     * <s:escapeBody> 标签是一个通用的转义标签。它会渲染标签体中内嵌的内容，并且在必要的时候进行转义。
     *
     * 例如，假设你希望在页面上展现一个 HTML 代码片段。为了正确显示，需要将 "<" 和 ">" 字符替换为 "&lt;" 和 "&gt;"，
     * 否则的话，浏览器将会像解析页面上其他 HTML 那样解析这段 HTML 内容。
     *
     * 当然，没有人禁止你手动将其转义为 "&lt;" 和 "&gt;"，但是这很烦琐，并且代码难以阅读。可以使用 <s:escapeBody>，
     * 并让 Spring 完成这项任务：
     *
     * <s:escapeBody htmlEscape="true">
     *     <h1>Hello</h1>
     * </s:escapeBody>
     *
     * 虽然转义后的格式看起来很难读，但浏览器会很乐意将其转换为未转义的 HTML，也就是希望用户能够看到的样子。
     *
     * 通过设置 javaScriptEscape 属性，<s:escapeBody> 标签还支持 JavaScript 转义：
     *
     * <s:escapeBody javaScriptEscape="true">
     *     <h1>Hello</h1>
     * </s:escapeBody>
     *
     * <s:escapeBody> 只完成一件事，并且完成得非常好。与 <s:url> 不同，它只会渲染内容，并不能将内容设置为变量。
     */
    public static void main(String[] args) {

    }

}
