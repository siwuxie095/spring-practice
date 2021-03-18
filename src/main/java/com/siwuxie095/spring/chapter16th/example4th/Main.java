package com.siwuxie095.spring.chapter16th.example4th;

/**
 * @author Jiajing Li
 * @date 2021-03-16 22:24:47
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 协商资源表述
     *
     * 当控制器的处理方法完成时，通常会返回一个逻辑视图名。如果方法不直接返回逻辑视图名（例如方法返回 void），那么
     * 逻辑视图名会根据请求的 URL 判断得出。DispatcherServlet 接下来会将视图的名字传递给一个视图解析器，要求它
     * 来帮助确定应该用哪个视图来渲染请求结果。
     *
     * 在面向人类访问的 Web 应用程序中，选择的视图通常来讲都会渲染为 HTML。视图解析方案是个简单的一维活动。如果根
     * 据视图名匹配上了视图，那这就是要用的视图了。
     *
     * 当要将视图名解析为能够产生资源表述的视图时，就有另外一个维度需要考虑了。视图不仅要匹配视图名，而且所选择的视
     * 图要适合客户端。如果客户端想要 JSON，那么渲染 HTML 的视图就不行了 —— 尽管视图名可能匹配。
     *
     * Spring 的 ContentNegotiatingViewResolver 是一个特殊的视图解析器，它考虑到了客户端所需要的内容类型。按
     * 照其最简单的形式，ContentNegotiatingViewResolver 可以按照下述形式进行配置：
     *
     *         @Bean
     *         public ViewResolver cnViewResolver() {
     *             return new ContentNegotiatingViewResolver();
     *         }
     *
     * 在这个简单的 bean 声明背后会涉及到很多事情。要理解 ContentNegotiatingViewResolver 是如何工作的，这涉及
     * 内容协商的两个步骤：
     * （1）确定请求的媒体类型；
     * （2）找到适合请求媒体类型的最佳视图。
     *
     * 下面来深入了解每个步骤来了解 ContentNegotiatingViewResolver 是如何完成其任务的，首先从弄明白客户端需要
     * 什么类型的内容开始。
     *
     *
     *
     * 1、确定请求的媒体类型
     *
     * 在内容协商两步骤中，第一步是确定客户端想要什么类型的内容表述。表面上看，这似乎是一个很简单的事情。难道请求的
     * Accept 头部信息不是已经很清楚地表明要发送什么样的表述给客户端吗？
     *
     * 遗憾的是，Accept 头部信息并不总是可靠的。如果客户端是 Web 浏览器，那并不能保证客户端需要的类型就是浏览器在
     * Accept 头部所发送的值。Web 浏览器一般只接受对人类用户友好的内容类型（如 text/html），所以没有办法（除了
     * 面向开发人员的浏览器插件）指定不同的内容类型。
     *
     * ContentNegotiatingViewResolver 将会考虑到 Accept 头部信息并使用它所请求的媒体类型，但是它会首先查看
     * URL 的文件扩展名。如果 URL 在结尾处有文件扩展名的话，ContentNegotiatingViewResolver 将会基于该扩展名
     * 确定所需的类型。如果扩展名是 ".json" 的话，那么所需的内容类型必须是 "application/json"。如果扩展名是
     * ".xml"，那么客户端请求的就是 "application/xml"。当然，".html" 扩展名表明客户端所需的资源表述为 HTML
     * （text/html）。
     *
     * 如果根据文件扩展名不能得到任何媒体类型的话，那就会考虑请求中的 Accept 头部信息。在这种情况下，Accept 头部
     * 信息中的值就表明了客户端想要的 MIME 类型，没有必要再去查找了。
     *
     * 最后，如果没有 Accept 头部信息，并且扩展名也无法提供帮助的话，ContentNegotiatingViewResolver 将会使用
     * "/" 作为默认的内容类型，这就意味着客户端必须要接收服务器发送的任何形式的表述。
     *
     * 一旦内容类型确定之后，ContentNegotiatingViewResolver 就该将逻辑视图名解析为渲染模型的 View。与 Spring
     * 的其他视图解析器不同，ContentNegotiatingViewResolver 本身不会解析视图。而是委托给其他的视图解析器，让它
     * 们来解析视图。
     *
     * ContentNegotiatingViewResolver 要求其他的视图解析器将逻辑视图名解析为视图。解析得到的每个视图都会放到一
     * 个列表中。这个列表装配完成后，ContentNegotiatingViewResolver 会循环客户端请求的所有媒体类型，在候选的视
     * 图中查找能够产生对应内容类型的视图。第一个匹配的视图会用来渲染模型。
     *
     *
     *
     * 2、影响媒体类型的选择
     *
     * 在上述的选择过程中，阐述了确定所请求媒体类型的默认策略。但是通过为其设置一个 ContentNegotiationManager，
     * 就能够改变它的行为。 借助 ContentNegotiationManager 所能做到的事情如下所示：
     * （1）指定默认的内容类型，如果根据请求无法得到内容类型的话，将会使用默认值；
     * （2）通过请求参数指定内容类型；
     * （3）忽视请求的 Accept 头部信息；
     * （4）将请求的扩展名映射为特定的媒体类型；
     * （5）将JAF（Java Activation Framework）作为根据扩展名查找媒体类型的备用方案。
     *
     * 有三种配置 ContentNegotiationManager 的方法：
     * （1）直接声明一个 ContentNegotiationManager 类型的 bean；
     * （2）通过 ContentNegotiationManagerFactoryBean 间接创建 bean；
     * （3）重载 WebMvcConfigurerAdapter 的 configureContentNegotiation() 方法。
     *
     * 直接创建 ContentNegotiationManager 有一些复杂，除非有充分的原因，否则不要这样做。后两种方案能够让创建
     * ContentNegotiationManager 更加简单。
     *
     * PS：ContentNegotiationManager 是在 Spring 3.2 中加入的：
     * ContentNegotiationManager 是 Spring 中相对比较新的功能，是在 Spring 3.2 中引入的。在 Spring 3.2
     * 之前，ContentNegotiatingViewResolver 的很多行为都是通过直接设置 ContentNegotiatingViewResolver
     * 的属性进行配置的。从 Spring 3.2 开始，ContentNegotiatingViewResolver 的大多数 Setter 方法都废弃了，
     * 鼓励通过 ContentNegotiationManager 来进行配置。
     *
     * 尽管这里不会介绍配置 ContentNegotiatingViewResolver 的旧方法，但是在创建 ContentNegotiationManager
     * 所设置的很多属性，在 ContentNegotiatingViewResolver 中都有对应的属性。如果你使用较早版本的 Spring 的话，
     * 应该能够很容易地将新的配置方式对应到旧配置方式中。
     *
     * 一般而言，如果使用 XML 配置 ContentNegotiationManager 的话，那最有用的将会是
     * ContentNegotiationManagerFactoryBean。
     *
     * 例如，可能希望在 XML 中配置 ContentNegotiationManager 使用 "application/json" 作为默认的内容类型：
     *
     * <bean id="contentNegotiationManager"
     *      class="org.springframework.web.accept.ContentNegotiationManagerFactoryBean"
     *      p:defaultContentType="application/json" />
     *
     * 因为 ContentNegotiationManagerFactoryBean 是 FactoryBean 的实现，所以它会创建一个
     * ContentNegotiationManager bean。这个 ContentNegotiationManager 能够注入到
     * ContentNegotiatingViewResolver 的 contentNegotiationManager 属性中。
     *
     * 如果使用 Java 配置的话，获得 ContentNegotiationManager 的最简便方法就是扩展 WebMvcConfigurerAdapter
     * 并重载 configureContentNegotiation() 方法。在创建 Spring MVC 应用的时候，很可能已经扩展了
     * WebMvcConfigurerAdapter。例如，在 Spittr 应用中，已经有了 WebMvcConfigurerAdapter 的扩展类，名为
     * WebConfig，所以需要做的就是重载 configureContentNegotiation() 方法。如下就是
     * configureContentNegotiation() 的一个实现，它设置了默认的内容类型：
     *
     *         @Override
     *         public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
     *             configurer.defaultContentType(MediaType.APPLICATION_JSON);
     *         }
     *
     * 可以看到，configureContentNegotiation() 方法给定了一个 ContentNegotiationConfigurer 对象。
     * ContentNegotiationConfigurer 中的一些方法对应于 ContentNegotiationManager 的 Setter 方法，
     * 这样就能在 ContentNegotiationManager 创建时，设置任意内容协商相关的属性。在本例中，调用
     * defaultContentType() 方法将默认的内容类型设置为 "application/json"。
     *
     * 现在，已经有了 ContentNegotiationManager bean，接下来就需要将它注入到 ContentNegotiatingViewResolver
     * 的 contentNegotiationManager 属性中。这需要稍微修改一下之前声明 ContentNegotiatingViewResolver 的
     * @Bean 方法：
     *
     *         @Bean
     *         public ViewResolver cnViewResolver(ContentNegotiationManager cnm) {
     *             ContentNegotiatingViewResolver cnvr =
     *                     new ContentNegotiatingViewResolver();
     *             cnvr.setContentNegotiationManager(cnm);
     *             return cnvr;
     *         }
     *
     * 这个 @Bean 方法注入了 ContentNegotiationManager，并使用它调用了 setContentNegotiationManager()。
     * 这样的结果就是 ContentNegotiatingViewResolver 将会使用 ContentNegotiationManager 所定义的行为。
     *
     * 配置 ContentNegotiationManager 有很多的细节，在这里无法对它们进行一一介绍。如下的程序清单是一个非常简单
     * 的配置样例，一般当使用 ContentNegotiatingViewResolver 的时候，通常会采用这种用法：它默认会使用 HTML
     * 视图，但是对特定的视图名称将会渲染为 JSON 输出。
     *
     *         @Bean
     *         public ViewResolver cnViewResolver(ContentNegotiationManager cnm) {
     *             ContentNegotiatingViewResolver cnvr =
     *                     new ContentNegotiatingViewResolver();
     *             cnvr.setContentNegotiationManager(cnm);
     *             return cnvr;
     *         }
     *
     *         @Override
     *         public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
     *             configurer.defaultContentType(MediaType.TEXT_HTML);
     *         }
     *
     *         @Bean
     *         public ViewResolver beanNameViewResolver() {
     *             return new BeanNameViewResolver();
     *         }
     *
     *         @Bean
     *         public View spittles() {
     *             return new MappingJackson2JsonView();
     *         }
     *
     * 除了以上代码中的内容以外，还应该有一个能够处理 HTML 的视图解析器（如 InternalResourceViewResolver 或
     * TilesViewResolver）。在大多数场景下，ContentNegotiatingViewResolver 会假设客户端需要 HTML，如
     * ContentNegotiationManager 配置所示。但是，如果客户端指定了它想要 JSON（通过在请求路径上使用 ".json"
     * 扩展名或 Accept 头部信息）的话，那么 ContentNegotiatingViewResolver 将会查找能够处理 JSON 视图的视
     * 图解析器。
     *
     * 如果逻辑视图的名称为 "spittles"，那么所配置的 BeanNameViewResolver 将会解析 spittles() 方法中所声明
     * 的 View。这是因为 bean 名称匹配逻辑视图的名称。如果没有匹配的 View 的话，ContentNegotiatingViewResolver
     * 将会采用默认的行为，将其输出为 HTML。
     *
     * ContentNegotiatingViewResolver 一旦能够确定客户端想要什么样的媒体类型，接下来就是查找渲染这种内容的视
     * 图。
     *
     *
     *
     * 3、ContentNegotiatingViewResolver 的优势与限制
     *
     * ContentNegotiatingViewResolver 最大的优势在于，它在 Spring MVC 之上构建了 REST 资源表述层，控制器
     * 代码无需修改。相同的一套控制器方法能够为面向人类的用户产生 HTML 内容，也能针对不是人类的客户端产生 JSON
     * 或 XML。
     *
     * 如果面向人类用户的接口与面向非人类客户端的接口之间有很多重叠的话，那么内容协商是一种很便利的方案。在实践中，
     * 面向人类用户的视图与 REST API 在细节上很少能够处于相同的级别。如果面向人类用户的接口与面向非人类客户端的
     * 接口之间没有太多重叠的话，那么 ContentNegotiatingViewResolver 的优势就体现不出来了。
     *
     * ContentNegotiatingViewResolver 还有一个严重的限制。作为 ViewResolver 的实现，它只能决定资源该如何
     * 渲染到客户端，并没有涉及到客户端要发送什么样的表述给控制器使用。如果客户端发送 JSON 或 XML 的话，那么
     * ContentNegotiatingViewResolver 就无法提供帮助了。
     *
     * ContentNegotiatingViewResolver 还有一个相关的小问题，所选中的 View 会渲染模型给客户端，而不是资源。
     * 这里有个细微但很重要的区别。当客户端请求 JSON 格式的 Spittle 对象列表时，客户端希望得到的响应可能如下
     * 所示：
     *
     * [
     *  {
     *      "id" : 42,
     *      "latitude" : 28.419489,
     *      "longitude" : -81.581184,
     *      "message" : "Hello World",
     *      "time" : 1400389200000
     *  },
     *  {
     *      "id" : 43,
     *      "latitude" : 28.419136,
     *      "longitude" : -81.577255,
     *      "message" : "Blast off",
     *      "time" : 1400475600000
     *  }
     * ]
     *
     * 而模型是 key-value 组成的 Map，那么响应可能会如下所示：
     *
     * {
     *     "spittleList" : [
     *        {
     *            "id" : 42,
     *            "latitude" : 28.419489,
     *            "longitude" : -81.581184,
     *            "message" : "Hello World",
     *            "time" : 1400389200000
     *        },
     *        {
     *            "id" : 43,
     *            "latitude" : 28.419136,
     *            "longitude" : -81.577255,
     *            "message" : "Blast off",
     *            "time" : 1400475600000
     *        }
     *       ]
     * }
     *
     * 尽管这不是很严重的问题，但确实可能不是客户端所预期的结果。
     *
     * 因为有这些限制，所以通常建议不要使用 ContentNegotiatingViewResolver。这里更加倾向于使用 Spring 的
     * 消息转换功能来生成资源表述。后续将会介绍如何在控制器代码中使用 Spring 的消息转换器。
     */
    public static void main(String[] args) {

    }

}
