package com.siwuxie095.spring.chapter7th.example8th;

/**
 * @author Jiajing Li
 * @date 2021-02-04 08:06:53
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 跨重定向请求传递数据
     *
     * 在处理完 POST 请求后，通常来讲一个最佳实践就是执行一下重定向。除了其他的一些因素外，这样做能够防止用户点击浏览器的
     * 刷新按钮或后退箭头时，客户端重新执行危险的 POST 请求。
     *
     * 在之前控制器方法返回的视图名称中，借助了 "redirect:" 前缀的力量。当控制器方法返回的 String 值以 "redirect:"
     * 开头的话，那么这个 String 不是用来查找视图的，而是用来指导浏览器进行重定向的路径。回头看一下之前的代码，可以看到
     * processRegistration() 方法返回的 "redirect:String" 如下所示：
     *
     *         return "redirect:/spitter/" + spitter.getUsername();
     *
     * "redirect:" 前缀能够让重定向功能变得非常简单。你可能会想 Spring 很难再让重定向功能变得更简单了。但是，请稍等：
     * Spring 为重定向功能还提供了一些其他的辅助功能。
     *
     * 具体来讲，正在发起重定向功能的方法该如何发送数据给重定向的目标方法呢？一般来讲，当一个处理器方法完成之后，该方法所
     * 指定的模型数据将会复制到请求中，并作为请求中的属性，请求会转发（forward）到视图上进行渲染。因为控制器方法和视图所
     * 处理的是同一个请求，所以在转发的过程中，请求属性能够得以保存。
     *
     * 但是，当控制器的结果是重定向的话，原始的请求就结束了，并且会发起一个新的 GET 请求。原始请求中所带有的模型数据也就
     * 随着请求一起消亡了。在新的请求属性中，没有任何的模型数据，这个请求必须要自己计算数据。
     *
     * PS：模型的属性是以请求属性的形式存放在请求中的，在重定向后无法存活。
     *
     * 显然，对于重定向来说，模型并不能用来传递数据。但是也有一些其他方案，能够从发起重定向的方法传递数据给处理重定向方法
     * 中：
     * （1）使用 URL 模板以路径变量和/或查询参数的形式传递数据；
     * （2）通过 flash 属性发送数据。
     *
     * 首先，看一下 Spring 如何通过路径变量和/或查询参数的形式传递数据。
     *
     *
     *
     * 通过 URL 模板进行重定向
     *
     * 通过路径变量和查询参数传递数据看起来非常简单。但是按照现在的写法，username 的值是直接连接到重定向 String 上的。
     * 这能够正常运行，但是还远远不能说没有问题。当构建 URL 或 SQL 查询语句的时候，使用 String 连接是很危险的。
     *
     * 除了连接 String 的方式来构建重定向 URL，Spring 还提供了使用模板的方式来定义重定向 URL。
     *
     * 例如，processRegistration() 方法的最后一行可以改写为如下的形式：
     *
     *     @RequestMapping(value = "/register", method = POST)
     *     public String processRegistration(Spitter spitter,
     *                                       Model model) {
     *         spitterRepository.save(spitter);
     *         model.addAttribute("username", spitter.getUsername());
     *         return "redirect:/spitter/{username}";
     *     }
     *
     * 现在，username 作为占位符填充到了 URL 模板中，而不是直接连接到重定向 String 中，所以 username 中所有的不安全
     * 字符都会进行转义。这样会更加安全，这里允许用户输入任何想要的内容作为 username，并会将其附加到路径上。
     *
     * 除此之外，模型中所有其他的原始类型值都可以添加到 URL 中作为查询参数。作为样例，假设除了 username 以外，模型中还
     * 要包含新创建 Spitter 对象的 id 属性，那 processRegistration() 方法可以改写为如下的形式：
     *
     *     @RequestMapping(value = "/register", method = POST)
     *     public String processRegistration(Spitter spitter,
     *                                       Model model) {
     *         spitterRepository.save(spitter);
     *         model.addAttribute("username", spitter.getUsername());
     *         model.addAttribute("spitterId", spitter.getId());
     *         return "redirect:/spitter/{username}";
     *     }
     *
     * 所返回的重定向 String 并没有太大的变化。但是，因为模型中的 spitterId 属性没有匹配重定向 URL 中的任何占位符，
     * 所以它会自动以查询参数的形式附加到重定向 URL 上。
     *
     * 如果 username 属性的值是 habuma 并且 spitterId 属性的值是 42，那么结果得到的重定向 URL 路径将会是
     * "/spitter/habuma?spitterId=42"。
     *
     * 通过路径变量和查询参数的形式跨重定向传递数据是很简单直接的方式，但它也有一定的限制。它只能用来发送简单的值，如
     * String 和数字的值。在 URL 中，并没有办法发送更为复杂的值，但这正是 flash 属性能够提供帮助的领域。
     *
     *
     *
     * 使用 flash 属性
     *
     * 假设不想在重定向中发送 username 或 ID 了，而是要发送实际的 Spitter 对象。如果只发送 ID 的话，那么处理重定向
     * 的方法还需要从数据库中查找才能得到 Spitter 对象。但是，在重定向之前，其实已经得到了 Spitter 对象。为什么不将
     * 其发送给处理重定向的方法，并将其展现出来呢？
     *
     * Spitter 对象要比 String 和 int 更为复杂。因此，不能像路径变量或查询参数那么容易地发送 Spitter 对象。它只能
     * 设置为模型中的属性。
     *
     * 但是，正如前面所讨论的那样，模型数据最终是以请求参数的形式复制到请求中的，当重定向发生的时候，这些数据就会丢失。
     * 因此，需要将 Spitter 对象放到一个位置，使其能够在重定向的过程中存活下来。
     *
     * 实际上，Spring 也认为将跨重定向存活的数据放到会话中是一个很不错的方式。但是，Spring 认为并不需要管理这些数据，
     * 相反，Spring 提供了将数据发送为 flash 属性（flash attribute）的功能。按照定义，flash 属性会一直携带这些
     * 数据直到下一次请求，然后才会消失。
     *
     * Spring 提供了通过 RedirectAttributes 设置 flash 属性的方法，这是 Spring 3.1 引入的 Model 的一个子接口。
     * RedirectAttributes 提供了 Model 的所有功能，除此之外，还有几个方法是用来设置 flash 属性的。
     *
     * 具体来讲，RedirectAttributes 提供了一组 addFlashAttribute() 方法来添加 flash 属性。重新看一下
     * processRegistration() 方法，可以使用 addFlashAttribute() 将 Spitter 对象添加到模型中：
     *
     *   @RequestMapping(value="/register", method=POST)
     *   public String processRegistration(
     *           Spitter spitter,
     *           RedirectAttributes model) {
     *     spitterRepository.save(spitter);
     *     model.addAttribute("username", spitter.getUsername());
     *     model.addFlashAttribute("spitter", spitter);
     *     return "redirect:/spitter/{username}";
     *   }
     *
     * 在这里，调用了 addFlashAttribute() 方法，并将 spitter 作为 key，Spitter 对象作为值。另外，还可以不设置
     * key 参数，让 key 根据值的类型自行推断得出：
     *
     * model.addFlashAttribute(spitter);
     *
     * 因为传递了一个 Spitter 对象给 addFlashAttribute() 方法，所以推断得到的 key 将会是 spitter。
     *
     * 在重定向执行之前，所有的 flash 属性都会复制到会话中。在重定向后，存在会话中的 flash 属性会被取出，并从会话
     * 转移到模型之中。处理重定向的方法就能从模型中访问 Spitter 对象了，就像获取其他的模型对象一样。
     *
     * PS：flash 属性保存在会话中，然后再放到模型中，因此能够在重定向的过程中存活。
     *
     * 为了完成 flash 属性的流程，如下展现了更新版本的 showSpitterProfile() 方法，在从数据库中查找之前，会首先
     * 从模型中检查 Spitter 对象：
     *
     *     @RequestMapping(value = "/{username}", method = GET)
     *     public String showSpitterProfile(@PathVariable String username, Model model) {
     *         if (!model.containsAttribute("spitter")) {
     *             model.addAttribute(
     *                     spitterRepository.findByUsername(username));
     *         }
     *         return "profile";
     *     }
     *
     * 可以看到，showSpitterProfile() 方法所做的第一件事就是检查是否存有 key 为 spitter 的 model 属性。如果
     * 模型中包含 spitter 属性，那就什么都不用做了。这里面包含的 Spitter 对象将会传递到视图中进行渲染。但是如果
     * 模型中不包含 spitter 属性的话，那么 showSpitterProfile() 将会从 Repository 中查找 Spitter，并将其
     * 存放到模型中。
     */
    public static void main(String[] args) {

    }

}
