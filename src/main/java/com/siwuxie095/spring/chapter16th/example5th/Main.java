package com.siwuxie095.spring.chapter16th.example5th;

/**
 * @author Jiajing Li
 * @date 2021-03-18 22:36:39
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 使用 HTTP 信息转换器
     *
     * 消息转换（message conversion）提供了一种更为直接的方式，它能够将控制器产生的数据转换为服务于客户端的表述形式。
     * 当使用消息转换功能时，DispatcherServlet 不再需要那么麻烦地将模型数据传送到视图中。实际上，这里根本就没有模型，
     * 也没有视图，只有控制器产生的数据，以及消息转换器（message converter）转换数据之后所产生的资源表述。
     *
     * Spring自带了各种各样的转换器，这些转换器满足了最常见的将对象转换为表述的需要。
     *
     * 例如，假设客户端通过请求的 Accept 头信息表明它能接受 "application/json"，并且 Jackson JSON 在类路径下，那
     * 么处理方法返回的对象将交给 MappingJacksonHttpMessageConverter，并由它转换为返回客户端的 JSON 表述形式。另
     * 一方面，如果请求的头信息表明客户端想要 "text/xml" 格式，那么 Jaxb2RootElementHttpMessageConverter 将会为
     * 客户端产生 XML 响应。
     *
     * 这些转换器如下所示：
     * （1）AtomFeedHttpMessageConverter：Rome Feed 对象和 Atom feed（媒体类型 application/atom+xml）之间的
     * 互相转换。如果 Rome 包在类路径下将会进行注册。
     * （2）BufferedImageHttpMessageConverter：BufferedImages 与图片二进制数据之间互相转换。
     * （3）ByteArrayHttpMessageConverter：读取/写入字节数组。从所有媒体类型中读取，并以
     * application/octet-stream 格式写入。
     * （4）FormHttpMessageConverter：将application/x-www-form-urlencoded 内容读入到
     * MultiValueMap<String,String> 中，也会将 MultiValueMap<String,String> 写入到
     * application/x-www-form-urlencoded 中或将 MultiValueMap<String, Object> 写入
     * 到 multipart/form-data 中。
     * （5）Jaxb2RootElementHttpMessageConverter：在XML（text/xml或application/xml）和使用 JAXB2 注解的对象
     * 间互相读取和写入。如果 JAXB v2 库在类路径下，将进行注册。
     * （6）MappingJacksonHttpMessageConverter：在 JSON 和类型化的对象或非类型化的 HashMap 间互相读取和写入。如
     * 果 Jackson JSON 库在类路径下，将进行注册。
     * （7）MappingJackson2HttpMessageConverter：在 JSON 和类型化的对象或非类型化的 HashMap 间互相读取和写入。如
     * 果 Jackson 2 JSON 库在类路径下，将进行注册。
     * （8）MarshallingHttpMessageConverter：使用注入的编排器和解排器（marshaller 和 unmarshaller）来读入和写入
     * XML。支持的编排器和解排器包括 Castor、JAXB2、JIBX、XMLBeans 以及 Xstream。
     * （9）ResourceHttpMessageConverter：读取或写入 Resource。
     * （10）RssChannelHttpMessageConverter：在 RSS feed 和 Rome Channel 对象间互相读取或写入。如果 Rome 库在
     * 类路径下，将进行注册。
     * （11）SourceHttpMessageConverter：在 XML 和 javax.xml.transform.Source 对象间互相读取和写入。默认注册。
     * （12）StringHttpMessageConverter：将所有媒体类型读取为 String。将 String 写入为 text/plain。
     * （13）XmlAwareFormHttpMessageConverter：FormHttpMessageConverter 的扩展，使用
     * SourceHttpMessageConverter 来支持基于 XML 的部分。
     *
     * PS：Spring 提供了多个 HTTP 信息转换器，用于实现资源表述与各种 Java 类型之间的互相转换。
     *
     * 注意，上面列出的 HTTP 信息转换器除了其中的五个以外都是自动注册的，所以要使用它们的话，不需要 Spring 配置。但是
     * 为了支持它们，你需要添加一些库到应用程序的类路径下。例如，如果你想使用 MappingJacksonHttpMessageConverter
     * 来实现 JSON 消息和 Java 对象的互相转换，那么需要将 Jackson JSON Processor 库添加到类路径中。类似地，如果你
     * 想使用 Jaxb2RootElementHttpMessageConverter 来实现 XML 消息和 Java 对象的互相转换，那么需要 JAXB 库。如
     * 果信息是 Atom 或 RSS 格式的话，那么 AtomFeedHttpMessageConverter 和 RssChannelHttpMessageConverter
     * 会需要 Rome 库。
     *
     * 你可能已经猜到了，为了支持消息转换，需要对 Spring MVC 的编程模型进行一些小调整。
     *
     *
     *
     * 1、在响应体中返回资源状态
     *
     * 正常情况下，当处理方法返回 Java 对象（除 String 外或 View 的实现以外）时，这个对象会放在模型中并在视图中渲染
     * 使用。但是，如果使用了消息转换功能的话，需要告诉 Spring 跳过正常的模型/视图流程，并使用消息转换器。有不少方式
     * 都能做到这一点，但是最简单的方法是为控制器方法添加 @ResponseBody 注解。
     *
     * 重新看一下 spittles() 方法，可以为其添加 @ResponseBody 注解，这样就能让 Spring 将方法返回的 List<Spittle>
     * 转换为响应体：
     *
     *     @RequestMapping(method = RequestMethod.GET, produces = "application/json")
     *     public @ResponseBody List<Spittle> spittles(
     *             @RequestParam(value = "max", defaultValue = MAX_LONG_AS_STRING) long max,
     *             @RequestParam(value = "count", defaultValue = "20") int count) {
     *         return spittleRepository.findSpittles(max, count);
     *     }
     *
     * @ResponseBody 注解会告知 Spring，要将返回的对象作为资源发送给客户端，并将其转换为客户端可接受的表述形式。更
     * 具体地讲，DispatcherServlet 将会考虑到请求中 Accept 头部信息，并查找能够为客户端提供所需表述形式的消息转换
     * 器。
     *
     * 举例来讲，假设客户端的 Accept 头部信息表明它接受 "application/json"，并且 Jackson JSON 库位于应用的类路
     * 径下，那么将会选择 MappingJacksonHttpMessageConverter 或 MappingJackson2HttpMessageConverter（这取
     * 决于类路径下是哪个版本的 Jackson）。消息转换器会将控制器返回的 Spittle 列表转换为 JSON 文档，并将其写入到响
     * 应体中。响应大致会如下所示：
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
     * PS：Jackson 默认会使用反射：
     * 注意在默认情况下，Jackson JSON 库在将返回的对象转换为 JSON 资源表述时，会使用反射。对于简单的表述内容来讲，
     * 这没有什么问题。但是如果你重构了 Java 类型，比如添加、移除或重命名属性，那么所产生的 JSON 也将会发生变化
     * （如果客户端依赖这些属性的话，那客户端有可能会出错）。
     *
     * 但是，可以在 Java 类型上使用 Jackson 的映射注解，从而改变产生 JSON 的行为。这样就能更多地控制所产生的
     * JSON，从而防止它影响到 API 或客户端。
     *
     *
     * 谈及 Accept 头部信息，请注意 getSpitter() 的 @RequestMapping 注解。在这里，使用了 produces 属性表明
     * 这个方法只处理预期输出为 JSON 的请求。也就是说，这个方法只会处理 Accept 头部信息包含 "application/json"
     * 的请求。其他任何类型的请求，即使它的 URL 匹配指定的路径并且是 GET 请求也不会被这个方法处理。这样的请求会被
     * 其他的方法来进行处理（如果存在适当方法的话），或者返回客户端 HTTP 406（Not Acceptable）响应。
     *
     *
     *
     * 2、在请求体中接收资源状态
     *
     * 到目前为止，只关注了 REST 端点如何为客户端提供资源。但是 REST 并不是只读的，REST API 也可以接受来自客户端
     * 的资源表述。如果要让控制器将客户端发送的 JSON 和 XML 转换为它所使用的 Java 对象，那是非常不方便的。在处理
     * 逻辑离开控制器的时候，Spring 的消息转换器能够将对象转换为表述 —— 它们能不能在表述传入的时候完成相同的任务呢？
     *
     * @ResponseBody 能够告诉 Spring 在把数据发送给客户端的时候，要使用某一个消息器，与之类似，@RequestBody 也
     * 能告诉 Spring 查找一个消息转换器，将来自客户端的资源表述转换为对象。例如，假设需要一种方式将客户端提交的新
     * Spittle 保存起来。可以按照如下的方式编写控制器方法来处理这种请求：
     *
     *     @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
     *     @ResponseStatus(HttpStatus.CREATED)
     *     public @ResponseBody Spittle saveSpittle(@RequestBody Spittle spittle) {
     *         return spittleRepository.save(spittle);
     *     }
     *
     * 如果忽略掉注解的话，那 saveSpittle() 是一个非常简单的方法。它接受一个 Spittle 对象作为参数，并使用
     * SpittleRepository 进行保存，最终返回 spittleRepository.save() 方法所得到的 Spittle 对象。
     *
     * 但是，通过使用注解，它会变得更加有意思也更加强大。@RequestMapping 表明它只能处理 "/spittles"（在类
     * 级别的 @RequestMapping 中进行了声明）的 POST 请求。POST 请求体中预期要包含一个 Spittle 的资源表述。
     * 因为 Spittle 参数上使用了 @RequestBody，所以 Spring 将会查看请求中的 Content-Type 头部信息，并查
     * 找能够将请求体转换为 Spittle 的消息转换器。
     *
     * 例如，如果客户端发送的 Spittle 数据是 JSON 表述形式，那么 Content-Type 头部信息可能就会是
     * "application/json"。在这种情况下，DispatcherServlet 会查找能够将 JSON 转换为 Java 对象的消息转换器。
     * 如果 Jackson 2 库在类路径中，那么 MappingJackson2HttpMessageConverter 将会担此重任，将 JSON 表述转
     * 换为 Spittle，然后传递到 saveSpittle() 方法中。这个方法还使用了 @ResponseBody 注解，因此方法返回的
     * Spittle 对象将会转换为某种资源表述，发送给客户端。
     *
     * 注意，@RequestMapping 有一个 consumes 属性，将其设置为 "application/json"。consumes 属性的工作方式
     * 类似于 produces，不过它会关注请求的 Content-Type 头部信息。它会告诉 Spring 这个方法只会处理对 "/spittles"
     * 的 POST 请求，并且要求请求的 Content-Type 头部信息为 "application/json"。如果无法满足这些条件的话，会
     * 由其他方法（如果存在合适的方法的话）来处理请求。
     *
     *
     *
     * 3、为控制器默认设置消息转换
     *
     * 当处理请求时，@ResponseBody 和 @RequestBody 是启用消息转换的一种简洁和强大方式。但是，如果你所编写的控
     * 制器有多个方法，并且每个方法都需要信息转换功能的话，那么这些注解就会带来一定程度的重复性。
     *
     * Spring 4.0 引入了 @RestController 注解，能够在这个方面提供帮助。如果在控制器类上使用 @RestController
     * 来代替 @Controller 的话，Spring 将会为该控制器的所有处理方法应用消息转换功能。不必为每个方法都添加
     * @ResponseBody 了。所定义的 SpittleController 可能就会如下所示：
     *
     * @RestController
     * @RequestMapping("/spittles")
     * public class SpittleController {
     *
     *     private static final String MAX_LONG_AS_STRING = "9223372036854775807";
     *
     *     private SpittleRepository spittleRepository;
     *
     *     @Autowired
     *     public SpittleController(SpittleRepository spittleRepository) {
     *         this.spittleRepository = spittleRepository;
     *     }
     *
     *     @RequestMapping(method = RequestMethod.GET, produces = "application/json")
     *     public List<Spittle> spittles(
     *             @RequestParam(value = "max", defaultValue = MAX_LONG_AS_STRING) long max,
     *             @RequestParam(value = "count", defaultValue = "20") int count) {
     *         return spittleRepository.findSpittles(max, count);
     *     }
     *
     *     @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
     *     public Spittle saveSpittle(@RequestBody Spittle spittle) {
     *         return spittleRepository.save(spittle);
     *     }
     *
     * }
     *
     * 这段代码的关键点在于代码中此时不包含什么。这两个处理器方法都没有使用 @ResponseBody 注解，因为控制器使用了
     * @RestController 注解，所以它的方法所返回的对象将会通过消息转换机制，产生客户端所需的资源表述。
     *
     * 到目前为止，看到了如何使用 Spring MVC 编程模型将 RESTful 资源发布到响应体之中。但是响应除了负载以外还会
     * 有其他的内容。头部信息和状态码也能够为客户端提供响应的有用信息。后续将会介绍在提供资源的时候，如何填充头部
     * 信息和设置状态码。
     */
    public static void main(String[] args) {

    }

}
