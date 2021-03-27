package com.siwuxie095.spring.chapter18th.example3rd;

/**
 * @author Jiajing Li
 * @date 2021-03-27 16:51:05
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 应对不支持 WebSocket 的场景
     *
     * WebSocket 是一个相对比较新的规范。虽然它早在 2011 年底就实现了规范化，但即便如此，在 Web 浏览器和应用服务器上
     * 依然没有得到一致的支持。Firefox 和 Chrome 早就已经完整支持 WebSocket 了，但是其他的一些浏览器刚刚开始支持
     * WebSocket。如下列出了几个流行的浏览器支持 WebSocket 功能的最低版本：
     * （1）Internet Explorer：10.0
     * （2）Firefox：4.0（部分支持），6.0（完整支持）
     * （3）Chrome：4.0（部分支持），13.0（完整支持）
     * （4）Safari：5.0（部分支持），6.0（完整支持）
     * （5）Opera：11.0（部分支持），12.10（完整支持）
     * （6）iOS Safari：4.2（部分支持），6.0（完整支持）
     * （7）Android Browser：4.4
     *
     * 令人遗憾的是，很多的网上冲浪者并没有认识到或理解新 Web 浏览器的特性，因此升级很慢。另外，有的公司规定使用特定版本
     * 的浏览器，这样它们的员工很难（或不可能）使用更新的浏览器。鉴于这些情况，如果你的应用程序使用 WebSocket 的话，用
     * 户可能会无法使用。
     *
     * 服务器端对 WebSocket 的支持也好不到哪里去。GlassFish 在几年前就开始支持一定形式的 WebSocket，但是很多其他的
     * 应用服务器在最近的版本中刚刚开始支持 WebSocket。例如 Tomcat 8 的发布候选构建版本。
     *
     * 即便浏览器和应用服务器的版本都符合要求，两端都支持 WebSocket，在这两者之间还有可能出现问题。防火墙代理通常会限制
     * 所有除 HTTP 以外的流量。它们有可能不支持或者（还）没有配置允许进行 WebSocket 通信。
     *
     * 在当前的 WebSocket 领域，也许描述了一个很阴暗的前景。但是，不要因为这一些不支持，你就停止使用 WebSocket 的功能。
     * 当它能够正常使用的时候，WebSocket 是一项非常棒的技术，但是如果它无法得到支持的话，这里所需要的仅仅是一种备用方案
     * （fallback plan）。
     *
     * 幸好，提到 WebSocket 的备用方案，这恰是 SockJS 所擅长的。SockJS 是 WebSocket 技术的一种模拟，在表面上，它尽
     * 可能对应 WebSocket API，但是在底层它非常智能，如果 WebSocket 技术不可用的话，就会选择另外的通信方式。SockJS
     * 会优先选用 WebSocket，但是如果 WebSocket 不可用的话，它将会从如下的方案中挑选最优的可行方案：
     * （1）XHR 流
     * （2）XDR 流
     * （3）iFrame 事件源
     * （4）iFrame HTML 文件
     * （5）XHR 轮询
     * （6）XDR 轮询
     * （7）iFrame XHR 轮询
     * （8）JSONP 轮询
     *
     * 好消息是在使用 SockJS 之前，并没有必要全部了解这些方案。SockJS 让开发者能够使用统一的编程模型，就好像在各个层面
     * 都完整支持 WebSocket 一样，SockJS 在底层会提供备用方案。
     *
     * 例如，为了在服务端启用 SockJS 通信，在 Spring 配置中可以很简单地要求添加该功能。重新回顾一下
     * registerWebSocketHandlers() 方法，稍微加一点内容就能启用 SockJS：
     *
     *     @Override
     *     public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
     *         registry.addHandler(marcoHandler(), "/marco").withSockJS();
     *     }
     *
     * addHandler() 方法会返回 WebSocketHandlerRegistration，通过简单地调用其 withSockJS() 方法就能声明想要使用
     * SockJS 功能，如果 WebSocket 不可用的话，SockJS 的备用方案就会发挥作用。
     *
     * 如果你使用 XML 来配置 Spring 的话，启用 SockJS 只需在配置中添加 <websocket:sockjs> 元素即可：
     *
     *     <websocket:handlers>
     *         <websocket:mapping handler="marcoHandler" path="/marco"/>
     *         <websocket:sockjs/>
     *     </websocket:handlers>
     *
     * 要在客户端使用 SockJS，需要确保加载了 SockJS 客户端库。具体的做法在很大程度上依赖于使用 JavaScript 模块加载器
     * （如 require.js 或 curl.js）还是简单地使用 <script> 标签加载 JavaScript 库。加载 SockJS 客户端库的最简单
     * 办法是使用 <script> 标签从 SockJS CDN 中进行加载，如下所示：
     *
     * <script src="http://cdn.sockjs.org/sockjs-0.3.min.js}"></script>
     *
     *
     * PS：用 WebJars 解析 Web 资源
     *
     * 这里使用了 WebJars 来解析 JavaScript 库，使其作为项目 Maven 或 Gradle 构建的一部分，就像其他的依赖一样。为了
     * 支持该功能，在 Spring MVC 配置中搭建了一个资源处理器，让它负责解析路径以 "/webjars/**" 开头的请求，这也是
     * WebJars 的标准路径：
     *
     *     @Override
     *     public void addResourceHandlers(ResourceHandlerRegistry registry) {
     *         registry.addResourceHandler("/webjars/**")
     *                 .addResourceLocations("classpath:/META-INF/resources/webjars/");
     *     }
     *
     * 在这个资源处理器准备就绪后，可以在 Web 页面中使用如下的 <script> 标签加载 SockJS 库：
     *
     * <script th:src="@{/webjars/sockjs-client/0.3.4/sockjs.min.js}"></script>
     *
     * 注意，这个特殊的 <script> 标签来源于一个 Thymeleaf 模板，并使用 "@{...}" 表达式来为 JavaScript 文件计算完整
     * 的相对于上下文的 URL 路径。
     *
     *
     * 除了加载 SockJS 客户端库以外，要使用 SockJS 只需修改两行代码：
     *
     * var url = 'marco';
     * var sock = new SockJS(url);
     *
     * 所做的第一个修改就是 URL。SockJS 所处理的 URL 是 "http://" 或 "https://" 模式，而不是 "ws://" 和 "wss://"。
     * 即便如此，还是可以使用相对 URL，避免书写完整的全限定 URL。在本例中，如果包含 JavaScript 的页面位于
     * "http://localhost:8080/websocket" 路径下，那么给定的 "marco" 路径将会形成到
     * "http://localhost:8080/websocket/marco" 的连接。
     *
     * 但是，这里最核心的变化是创建 SockJS 实例来代替 WebSocket。因为 SockJS 尽可能地模拟了 WebSocket，所以其他代码
     * 并不需要变化。相同的 onopen、onmessage 和 onclose 事件处理函数用来响应对应的事件，相同的 send() 方法用来发送
     * "Marco!" 到服务器端。
     *
     * 这里并没有改变很多的代码，但是客户端-服务器之间通信的运行方式却有了很大的变化。可以完全相信客户端和服务器之间能够
     * 进行类似于 WebSocket 这样的通信，即便浏览器、服务器或位于中间的代理不支持 WebSocket，也无需再担心了。
     *
     * WebSocket 提供了浏览器-服务器之间的通信方式，当运行环境不支持 WebSocket 的时候，SockJS 提供了备用方案。但是不
     * 管哪种场景，对于实际应用来说，这种通信形式都显得层级过低。后续看一下如何在 WebSocket 之上使用 STOMP（Simple
     * Text Oriented Messaging Protocol），为浏览器-服务器之间的通信增加恰当的消息语义。
     */
    public static void main(String[] args) {

    }

}
