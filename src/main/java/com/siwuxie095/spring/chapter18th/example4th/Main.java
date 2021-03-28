package com.siwuxie095.spring.chapter18th.example4th;

/**
 * @author Jiajing Li
 * @date 2021-03-28 13:59:47
 */
public class Main {

    /**
     * 使用 STOMP 消息
     *
     * 如果要求你编写一个 Web 应用程序，在讨论需求之前，你可能对于要采用的基础技术和框架就有了很好的想法。即便是简单的
     * "Hello World" Web 应用，你可能也会考虑使用 Spring MVC 控制器来处理请求，并为响应使用 JSP 或 Thymeleaf 模
     * 板。至少，你也应该会创建一个静态的 HTML 页面，并让 Web 服务器处理来自 Web 浏览器的相应请求。这里应该不会关心
     * 浏览器具体如何请求页面以及页面如何传递给浏览器这样的事情。
     *
     * 现在，假设 HTTP 协议并不存在，只能使用 TCP 套接字来编写 Web 应用。你可能认为这是疯掉了。当然，你也许能够完成这
     * 一壮举，但是这需要自行设计客户端和服务器端都认可的协议，从而实现有效的通信。简单来说，这不是一件容易的事情。
     *
     * 不过，幸好有 HTTP，它解决了 Web 浏览器发起请求以及 Web 服务器响应请求的细节。这样的话，大多数的开发人员并不需
     * 要编写低层级 TCP 套接字通信相关的代码。
     *
     * 直接使用 WebSocket（或 SockJS）就很类似于使用 TCP 套接字来编写 Web 应用。因为没有高层级的线路协议（wire
     * protocol），因此就需要定义应用之间所发送消息的语义，还需要确保连接的两端都能遵循这些语义。
     *
     * 不过，好消息是并非必须要使用原生的 WebSocket 连接。就像 HTTP 在 TCP 套接字之上添加了请求-响应模型层一样，
     * STOMP 在 WebSocket 之上提供了一个基于帧的线路格式（frame-based wire format）层，用来定义消息的语义。
     *
     * 乍看上去，STOMP 的消息格式非常类似于 HTTP 请求的结构。与 HTTP 请求和响应类似，STOMP 帧由命令、一个或多个头
     * 信息以及负载所组成。例如，如下就是发送数据的一个 STOMP 帧：
     *
     * SEND
     * destination:/app/marco
     * content-length:20
     *
     * {\"message\":\"Marco!\"}
     *
     * 在这个简单的样例中，STOMP 命令是 send，表明会发送一些内容。紧接着是两个头信息：一个用来表示消息要发送到哪里的
     * 目的地，另外一个则包含了负载的大小。然后，紧接着是一个空行，STOMP 帧的最后是负载内容，在本例中，是一个 JSON
     * 消息。
     *
     * STOMP 帧中最有意思的恐怕就是 destination 头信息了。它表明 STOMP 是一个消息协议，类似于 JMS 或 AMQP。消息
     * 会发布到某个目的地，这个目的地实际上可能真的有消息代理（message broker）作为支撑。另一方面，消息处理器
     * （message handler）也可以监听这些目的地，接收所发送过来的消息。
     *
     * 在 WebSocket 通信中，基于浏览器的 JavaScript 应用可能会发送消息到一个目的地，这个目的地由服务器端的组件来进
     * 行处理。其实，反过来是一样的，服务器端的组件也可以发布消息，由 JavaScript 客户端的目的地来接收。
     *
     * Spring 为 STOMP 消息提供了基于 Spring MVC 的编程模型。后续将会看到，在 Spring MVC 控制器中处理 STOMP 消
     * 息与处理 HTTP 请求并没有太大的差别。但首先，需要配置 Spring 启用基于 STOMP 的消息。
     */
    public static void main(String[] args) {

    }

}
