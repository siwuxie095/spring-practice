package com.siwuxie095.spring.chapter16th.example2nd;

/**
 * @author Jiajing Li
 * @date 2021-03-15 21:09:02
 */
public class Main {

    /**
     * 了解 REST
     *
     * 可能这并不是你第一次听到或读到 REST 这个词。近些年来，关于 REST 已经有了许多讨论，在软件开发中你可能会发现
     * 有一种很流行的做法，那就是在推动 REST 替换 SOAP Web 服务的时候，会谈论到 SOAP 的不足。
     *
     * 诚然，对于许多应用程序而言，使用 SOAP 可能会有些大材小用了，而 REST 提供了一个更简单的可选方案。另外，很多
     * 的现代化应用都会有移动或富 JavaScript 客户端，它们都会使用运行在服务器上 REST API。
     *
     * 问题在于并不是每个人都清楚 REST 到底是什么。结果就出现了许多误解。有很多打着 REST 幌子的事情其实并不符合
     * REST 真正的本意。在谈论 Spring 如何支持 REST 之前，需要对 REST 是什么达成共识。
     *
     *
     *
     * 1、REST 的基础知识
     *
     * 当谈论 REST 时，有一种常见的错误就是将其视为 "基于 URL 的 Web 服务" —— 将 REST 作为另一种类型的远程过程
     * 调用（remote procedure call，RPC）机制，就像 SOAP 一样，只不过是通过简单的 HTTP URL 来触发，而不是使用
     * SOAP 大量的 XML 命名空间。
     *
     * 恰好相反，REST 与 RPC 几乎没有任何关系。RPC 是面向服务的，并关注于行为和动作；而 REST 是面向资源的，强调
     * 描述应用程序的事物和名词。
     *
     * 为了理解 REST 是什么，这里将它的首字母缩写拆分为不同的构成部分：
     * （1）表述性（Representational）：REST 资源实际上可以用各种形式来进行表述，包括 XML、JSON（JavaScript
     * Object Notation）甚至 HTML —— 最适合资源使用者的任意形式；
     * （2）状态（State）：当使用 REST 的时候，更关注资源的状态而不是对资源采取的行为；
     * （3）转移（Transfer）：REST 涉及到转移资源数据，它以某种表述性形式从一个应用转移到另一个应用。
     *
     * 更简洁地讲，REST 就是将资源的状态以最适合客户端或服务端的形式从服务器端转移到客户端（或者反过来）。
     *
     * 在 REST 中，资源通过 URL 进行识别和定位。至于 RESTful URL 的结构并没有严格的规则，但是 URL 应该能够识别
     * 资源，而不是简单的发一条命令到服务器上。再次强调，关注的核心是事物，而不是行为。
     *
     * REST 中会有行为，它们是通过 HTTP 方法来定义的。具体来讲，也就是 GET、POST、PUT、DELETE、PATCH 以及其他
     * 的 HTTP 方法构成了 REST 中的动作。这些 HTTP 方法通常会匹配为如下的 CRUD 动作：
     * （1）Create：POST
     * （2）Read：GET
     * （3）Update：PUT 或 PATCH
     * （4）Delete：DELETE
     *
     * 尽管通常来讲，HTTP 方法会映射为 CRUD 动作，但这并不是严格的限制。有时候，PUT 可以用来创建新资源，POST 可
     * 以用来更新资源。实际上，POST 请求非幂等性（non-idempotent）的特点使其成为一个非常灵活的方法，对于无法适应
     * 其他 HTTP 方法语义的操作，它都能够胜任。
     *
     * 基于对 REST 的这种观点，所以这里尽量避免使用诸如 REST 服务、REST Web 服务或类似的术语，这些术语会不恰当
     * 地强调行为。相反，更这里愿意强调 REST 面向资源的本质，并讨论 RESTful 资源。
     *
     *
     *
     * 2、Spring 是如何支持 REST 的
     *
     * Spring 很早就有导出 REST 资源的需求。从 3.0 版本开始，Spring 针对 Spring MVC 的一些增强功能对 REST
     * 提供了良好的支持。当前的 4.0 版本中，Spring 支持以下方式来创建 REST 资源：
     * （1）控制器可以处理所有的 HTTP 方法，包含四个主要的 REST 方法：GET、PUT、DELETE 以及 POST。Spring 3.2
     * 及以上版本还支持 PATCH 方法；
     * （2）借助 @PathVariable 注解，控制器能够处理参数化的 URL（将变量输入作为 URL 的一部分）；
     * （3）借助 Spring 的视图和视图解析器，资源能够以多种方式进行表述，包括将模型数据渲染为 XML、JSON、Atom 以
     * 及 RSS 的 View 实现；
     * （4）可以使用 ContentNegotiatingViewResolver 来选择最适合客户端的表述；
     * （5）借助 @ResponseBody 注解和各种 HttpMethodConverter 实现，能够替换基于视图的渲染方式；
     * （6）类似地，@RequestBody 注解以及 HttpMethodConverter 实现可以将传入的 HTTP 数据转化为传入控制器处理
     * 方法的 Java 对象；
     * （7）借助 RestTemplate，Spring 应用能够方便地使用 REST 资源。
     */
    public static void main(String[] args) {

    }

}
