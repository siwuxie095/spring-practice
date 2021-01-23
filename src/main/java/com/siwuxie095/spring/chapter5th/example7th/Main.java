package com.siwuxie095.spring.chapter5th.example7th;

/**
 * @author Jiajing Li
 * @date 2021-01-23 21:13:47
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 接受请求的输入
     *
     * 有些 Web 应用是只读的。人们只能通过浏览器在站点上闲逛，阅读服务器发送到浏览器中的内容。
     *
     * 不过，这并不是一成不变的。众多的 Web 应用允许用户参与进去，将数据发送回服务器。如果没有这项能力的话，
     * 那 Web 将完全是另一番景象。
     *
     * Spring MVC 允许以多种方式将客户端中的数据传送到控制器的处理器方法中，包括：
     * （1）查询参数（Query Parameter）
     * （2）表单参数（Form Parameter）
     * （3）路径变量（Path Variable）
     *
     * 你将会看到如何编写控制器处理这些不同机制的输入。作为开始，先看一下如何处理带有查询参数的请求，这也是
     * 客户端往服务器端发送数据时，最简单和最直接的方式。
     *
     *
     *
     * 处理查询参数
     *
     * 在 Spittr 应用中，可能需要处理的一件事就是展现分页的 Spittle 列表。在现在的 SpittleController 中，
     * 它只能展现最新的 Spittle，并没有办法向前翻页查看以前编写的 Spittle 历史记录。如果你想让用户每次都能
     * 查看某一页的 Spittle 历史，那么就需要提供一种方式让用户传递参数进来，进而确定要展现哪些 Spittle 集合。
     *
     * 在确定该如何实现时，假设要查看某一页 Spittle 列表，这个列表会按照最新的 Spittle 在前的方式进行排序。
     * 因此，下一页中第一条的 ID 肯定会早于当前页最后一条的 ID。所以，为了显示下一页的 Spittle，需要将一个
     * Spittle 的 ID 传入进来，这个 ID 要恰好小于当前页最后一条 Spittle 的 ID。另外，你还可以传入一个参数
     * 来确定要展现的 Spittle 数量。
     *
     * 为了实现这个分页的功能，所编写的处理器方法要接受如下的参数：
     * （1）max 参数（表明结果中所有 Spittle 的 ID 均应该在这个值之前）。
     * （2）count 参数（表明在结果中要包含的 Spittle 数量）。
     *
     * SpittleController 中的处理器方法要同时处理有参数和没有参数的场景，那需要对其进行修改，让它能接受参数，
     * 同时，如果这些参数在请求中不存在的话，就使用默认值 Long.MAX_VALUE 和 20。@RequestParam 注解的
     * defaultValue 属性可以完成这项任务：
     *
     *     @RequestMapping(method = RequestMethod.GET)
     *     public List<Spittle> spittles(
     *             @RequestParam(value = "max", defaultValue = MAX_LONG_AS_STRING) long max,
     *             @RequestParam(value = "count", defaultValue = "20") int count) {
     *         return spittleRepository.findSpittles(max, count);
     *     }
     *
     * 现在，如果 max 参数没有指定的话，它将会是 Long 类型的最大值。因为查询参数都是 String 类型的，因此
     * defaultValue 属性需要 String 类型的值。因此，使用 Long.MAX_VALUE 是不行的。可以将 Long.MAX_VALUE
     * 转换为名为 MAX_LONG_AS_STRING 的 String 类型常量：
     *
     * private static final String MAX_LONG_AS_STRING = "9223372036854775807";
     *
     * 尽管 defaultValue 属性给定的是 String 类型的值，但是当绑定到方法的 max 参数时，它会转换为 Long 类型。
     *
     * 如果请求中没有 count 参数的话，count 参数的默认值将会设置为 20。
     *
     * 请求中的查询参数是往控制器中传递信息的常用手段。另外一种方式也很流行，尤其是在构建面向资源的控制器时，这种
     * 方式就是将传递参数作为请求路径的一部分。下面看一下如何将路径变量作为请求路径的一部分，从而实现信息的输入。
     *
     *
     *
     * 通过路径参数接受输入
     *
     * 假设应用程序需要根据给定的 ID 来展现某一个 Spittle 记录。其中一种方案就是编写处理器方法，通过使用
     * @RequestParam 注解，让它接受 ID 作为查询参数：
     *
     *     @RequestMapping(value = "/show", method = RequestMethod.GET)
     *     public String showSpittle(@RequestParam("spittle_id") long spittleId,
     *                           Model model) {
     *         model.addAttribute(spittleRepository.findOne(spittleId));
     *         return "spittle";
     *     }
     *
     * 这个处理器方法将会处理形如 "/spittles/show?spittle_id=12345" 这样的请求。尽管这也可以正常工作，
     * 但是从面向资源的角度来看这并不理想。在理想情况下，要识别的资源（Spittle）应该通过 URL 路径进行标示，
     * 而不是通过查询参数。对 "/spittles/12345" 发起 GET 请求要优于对 "/spittles/ show?spittle_id=12345"
     * 发起请求。前者能够识别出要查询的资源，而后者描述的是带有参数的一个操作 —— 本质上是通过 HTTP 发起的 RPC。
     *
     * 到目前为止，在编写的控制器中，所有的方法都映射到了（通过 @RequestMapping）静态定义好的路径上。但是，
     * 现在要编写的 @RequestMapping 要包含变量部分，这部分代表了 Spittle ID。
     *
     * 为了实现这种路径变量，Spring MVC 允许在 @RequestMapping 路径中添加占位符。占位符的名称要用大括号
     * （"{" 和 "}"）括起来。路径中的其他部分要与所处理的请求完全匹配，但是占位符部分可以是任意的值。
     *
     * 下面的处理器方法使用了占位符，将 Spittle ID 作为路径的一部分：
     *
     *     @RequestMapping(value = "/{spittleId}", method = RequestMethod.GET)
     *     public String spittle(@PathVariable("spittleId") long spittleId,
     *                           Model model) {
     *         model.addAttribute(spittleRepository.findOne(spittleId));
     *         return "spittle";
     *     }
     *
     * 例如，它就能够处理针对 "/spittles/12345" 的请求。
     *
     * 可以看到，spittle() 方法的 spittleId 参数上添加了 @PathVariable("spittleId") 注解，这表明在请求
     * 路径中，不管占位符部分的值是什么都会传递到处理器方法的 spittleId 参数中。如果对 "/spittles/54321"
     * 发送 GET 请求，那么将会把 "54321" 传递进来，作为 spittleId 的值。
     *
     * 需要注意的是：在样例中 spittleId 这个词出现了好几次，先是在 @RequestMapping 的路径中，然后作为
     * @PathVariable 属性的值，最后又作为方法的参数名称。因为方法的参数名碰巧与占位符的名称相同，因此可以去掉
     * @PathVariable 中的 value 属性：
     *
     *     @RequestMapping(value = "/{spittleId}", method = RequestMethod.GET)
     *     public String spittle(@PathVariable long spittleId,
     *                           Model model) {
     *         model.addAttribute(spittleRepository.findOne(spittleId));
     *         return "spittle";
     *     }
     *
     * 如果 @PathVariable 中没有 value 属性的话，它会假设占位符的名称与方法的参数名相同。这能够让代码稍微
     * 简洁一些，因为不必重复写占位符的名称了。但需要注意的是，如果你想要重命名参数时，必须要同时修改占位符的
     * 名称，使其互相匹配。
     *
     * spittle() 方法会将参数传递到 SpittleRepository 的 findOne() 方法中，用来获取某个 Spittle 对象，
     * 然后将 Spittle 对象添加到模型中。模型的 key 将会是 spittle，这是根据传递到 addAttribute() 方法
     * 中的类型推断得到的。
     *
     * 这样 Spittle 对象中的数据就可以渲染到视图中了，此时需要引用请求中 key 为 spittle 的属性（与模型的
     * key 一致）。如下为渲染 Spittle 的 JSP 视图片段：
     *
     *     <div class="spittleView">
     *       <div class="spittleMessage"><c:out value="${spittle.message}" /></div>
     *       <div>
     *         <span class="spittleTime"><c:out value="${spittle.time}" /></span>
     *       </div>
     *     </div>
     *
     * 如果传递请求中少量的数据，那查询参数和路径变量是很合适的。但通常还需要传递很多的数据（也许是表单提交的
     * 数据），那查询参数显得有些笨拙和受限了。后续将介绍如何编写控制器方法来处理表单提交。
     *
     * PS：所有代码请参见 example6th。
     */
    public static void main(String[] args) {

    }

}
