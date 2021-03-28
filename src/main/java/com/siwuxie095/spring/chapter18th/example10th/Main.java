package com.siwuxie095.spring.chapter18th.example10th;

/**
 * @author Jiajing Li
 * @date 2021-03-28 17:41:21
 */
public class Main {

    /**
     * 小结
     *
     * 如果在应用间发送消息的话，那 WebSocket 是一种令人兴奋的通信方式，尤其是如果其中某个应用运行在 Web 浏览器中
     * 更是如此。当编写存在大量交互的 Web 应用程序时，它是很重要的，能够实现从服务器无缝的发送和接收数据。
     *
     * Spring 对 WebSocket 的支持包括低层级的 API，它能够让开发者使用原始的 WebSocket 连接。但是，WebSocket
     * 并没有在 Web 浏览器、服务器以及网络代理上得到广泛支持。因此，Spring 同时还支持 SockJS，这个协议能够在
     * WebSocket 不可用的时候提供备用的通信模式。
     *
     * Spring 还提供了高级的编程模型，也就是使用 STOMP 线路级协议来处理 WebSocket 消息。在这个更高级的模型中，
     * 能够在 Spring MVC 控制器中处理 STOMP 消息，类似于处理 HTTP 消息的方式。
     */
    public static void main(String[] args) {

    }

}
