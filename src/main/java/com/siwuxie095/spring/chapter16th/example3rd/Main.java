package com.siwuxie095.spring.chapter16th.example3rd;

/**
 * @author Jiajing Li
 * @date 2021-03-16 21:30:20
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 创建第一个 REST 端点
     *
     * 借助 Spring 的支持来实现 REST 功能有一个很有利的地方，那就是已经掌握了很多创建 RESTful 控制器的知识。也就是说，
     * 之前学到的创建 Web 应用的知识，它们可以用在通过 REST API 暴露资源上。
     *
     * 首先，这里会在名为 SpittleApiController 的新控制器中创建第一个 REST 端点。
     *
     * 如下的代码展现了这个新 REST 控制器起始的样子，它会提供 Spittle 资源。这是一个很简单的开始：
     *
     * @Controller
     * @RequestMapping("/spittles")
     * public class SpittleApiController {
     *
     *     private static final String MAX_LONG_AS_STRING = "9223372036854775807";
     *
     *     private SpittleRepository spittleRepository;
     *
     *     @Autowired
     *     public SpittleApiController(SpittleRepository spittleRepository) {
     *         this.spittleRepository = spittleRepository;
     *     }
     *
     *     @RequestMapping(method = RequestMethod.GET)
     *     public List<Spittle> spittles(
     *             @RequestParam(value = "max", defaultValue = MAX_LONG_AS_STRING) long max,
     *             @RequestParam(value = "count", defaultValue = "20") int count) {
     *         return spittleRepository.findSpittles(max, count);
     *     }
     *
     * }
     *
     * 你能够看出来这段代码服务于一个 REST 资源而不是 Web 页面吗？
     *
     * 可能看不出来！按照这个控制器的写法，并没有地方表明它是 RESTful、服务于资源的控制器。
     *
     * 在之前，当发起对 "/spittles" 的 GET 请求时，将会调用 spittles() 方法。它会查找并返回 Spittle 列表，而这个
     * 列表会通过注入的 SpittleRepository 获取到。列表会放到模型中，用于视图的渲染。对于基于浏览器的 Web 应用，这可
     * 能意味着模型数据会渲染到 HTML 页面中。
     *
     * 但是，现在讨论的是创建 REST API。在这种情况下，HTML 并不是合适的数据表述形式。
     *
     * 表述是 REST 中很重要的一个方面。它是关于客户端和服务器端针对某一资源是如何通信的。任何给定的资源都几乎可以用任意
     * 的形式来进行表述。如果资源的使用者愿意使用 JSON，那么资源就可以用 JSON 格式来表述。如果使用者喜欢尖括号，那相同
     * 的资源可以用 XML 来进行表述。同时，如果用户在浏览器中查看资源的话，可能更愿意以 HTML 的方式来展现（或者 PDF、
     * Excel及其他便于人类阅读的格式）。资源没有变化 —— 只是它的表述方式变化了。
     *
     * 注意：尽管 Spring 支持多种资源表述形式，但是在定义 REST API 的时候，不一定要全部使用它们。对于大多数客户端来说，
     * 用 JSON 和 XML 来进行表述就足够了。
     *
     * 当然，如果内容要由人类用户来使用的话，那么可能需要支持 HTML 格式的资源。根据资源的特点和应用的需求，还可能选择使
     * 用 PDF 文档或 Excel 表格来展现资源。
     *
     * 对于非人类用户的使用者，比如其他的应用或调用 REST 端点的代码，资源表述的首选应该是 XML 和 JSON。借助 Spring
     * 同时支持这两种方案非常简单，所以没有必要做一个非此即彼的选择。
     *
     * 这里推荐至少要支持 JSON。JSON 使用起来至少会像 XML 一样简单（很多人会说 JSON 会更加简单），并且如果客户端是
     * JavaScript（最近一段时间以来，这种做法越来越常见）的话，JSON 更是会成为优胜者，因为在 JavaScript 中使用 JSON
     * 数据根本就不需要编排和解排（marshaling/demarshaling）。
     *
     * 需要了解的是控制器本身通常并不关心资源如何表述。控制器以 Java 对象的方式来处理资源。控制器完成了它的工作之后，资
     * 源才会被转化成最适合客户端的形式。
     *
     * Spring 提供了两种方法将资源的 Java 表述形式转换为发送给客户端的表述形式：
     * （1）内容协商（Content negotiation）：选择一个视图，它能够将模型渲染为呈现给客户端的表述形式；
     * （2）消息转换器（Message conversion）：通过一个消息转换器将控制器所返回的对象转换为呈现给客户端的表述形式。
     *
     * 鉴于之前已经讨论过视图解析器，并且已经熟悉了基于视图的渲染，所以后续会首先看一下如何使用内容协商来选择视图或视图
     * 解析器，它们将资源渲染为客户端能够接受的形式。
     */
    public static void main(String[] args) {

    }

}
