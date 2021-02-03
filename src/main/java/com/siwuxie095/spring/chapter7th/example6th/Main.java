package com.siwuxie095.spring.chapter7th.example6th;

/**
 * @author Jiajing Li
 * @date 2021-02-03 20:34:13
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 处理异常
     *
     * 到现在为止，在 Spittr 应用中，假设所有的功能都正常运行。但是如果某个地方出错的话，该怎么办呢？当处理请求的时候，
     * 抛出异常该怎么处理呢？如果发生了这样的情况，该给客户端什么响应呢？
     *
     * 不管发生什么事情，不管是好的还是坏的，Servlet 请求的输出都是一个 Servlet 响应。如果在请求处理的时候，出现了
     * 异常，那它的输出依然会是 Servlet 响应。异常必须要以某种方式转换为响应。
     *
     * Spring 提供了多种方式将异常转换为响应：
     * （1）特定的 Spring 异常将会自动映射为指定的 HTTP 状态码；
     * （2）异常上可以添加 @ResponseStatus 注解，从而将其映射为某一个 HTTP 状态码；
     * （3）在方法上可以添加 @ExceptionHandler 注解，使其用来处理异常。
     *
     * 处理异常的最简单方式就是将其映射到 HTTP 状态码上，进而放到响应之中。接下来，看一下如何将异常映射为某一个 HTTP
     * 状态码。
     *
     *
     *
     * 将异常映射为 HTTP 状态码
     *
     * 在默认情况下，Spring 会将自身的一些异常自动转换为合适的状态码。
     *
     * 如下列出了这些映射关系：
     * （1）BindException：400 - Bad Request
     * （2）ConversionNotSupportedException：500 - Internal Server Error
     * （3）HttpMediaTypeNotAcceptableException：406 - Not Acceptable
     * （4）HttpMediaTypeNotSupportedException：415 - Unsupported Media Type
     * （5）HttpMessageNotReadableException：400 - Bad Request
     * （6）HttpMessageNotWritableException：500 - Internal Server Error
     * （7）HttpRequestMethodNotSupportedException：405 - Method Not Allowed
     * （8）MethodArgumentNotValidException：400 - Bad Request
     * （9）MissingServletRequestParameterException：400 - Bad Request
     * （10）MissingServletRequestPartException：400 - Bad Request
     * （11）NoSuchRequestHandlingMethodException：404 - Not Found
     * （12）TypeMismatchException：400 - Bad Request
     *
     * 这些异常一般会由 Spring 自身抛出，作为 DispatcherServlet 处理过程中或执行校验时出现问题的结果。例如，如果
     * DispatcherServlet 无法找到适合处理请求的控制器方法，那么将会抛出 NoSuchRequestHandlingMethodException
     * 异常，最终的结果就是产生 404 状态码的响应（Not Found）。
     *
     * 尽管这些内置的映射是很有用的，但是对于应用所抛出的异常它们就无能为力了。幸好，Spring 提供了一种机制，能够通过
     * @ResponseStatus 注解将异常映射为 HTTP 状态码。
     *
     * 为了阐述这项功能，请参考 SpittleController 中如下的请求处理方法，它可能会产生 HTTP 404 状态（但目前还没有
     * 实现）：
     *
     *     @RequestMapping(value = "/{spittleId}", method = RequestMethod.GET)
     *     public String spittle(@PathVariable("spittleId") long spittleId,
     *                           Model model) {
     *         Spittle spittle = spittleRepository.findOne(spittleId);
     *         if (spittle == null) {
     *             throw new SpittleNotFoundException();
     *         }
     *         model.addAttribute(spittle);
     *         return "spittle";
     *     }
     *
     * 在这里，会从 SpittleRepository 中，通过 ID 检索 Spittle 对象。如果 findOne() 方法能够返回 Spittle 对象
     * 的话，那么会将 Spittle 放到模型中，然后名为 spittle 的视图会负责将其渲染到响应之中。但是如果 findOne() 方法
     * 返回 null 的话，那么将会抛出 SpittleNotFoundException 异常。现在 SpittleNotFoundException 就是一个简单
     * 的非检查型异常，如下所示：
     *
     * public class SpittleNotFoundException extends RuntimeException {
     *
     * }
     *
     * 如果调用 spittle() 方法来处理请求，并且给定 ID 获取到的结果为空，那么 SpittleNotFoundException （默认）
     * 将会产生 500 状态码（Internal Server Error）的响应。实际上，如果出现任何没有映射的异常，响应都会带有 500
     * 状态码，但是，可以通过映射 SpittleNotFoundException 对这种默认行为进行变更。
     *
     * 当抛出 SpittleNotFoundException 异常时，这是一种请求资源没有找到的场景。如果资源没有找到的话，HTTP 状态码
     * 404 是最为精确的响应状态码。所以，要使用 @ResponseStatus 注解将 SpittleNotFoundException 映射为 HTTP
     * 状态码 404。
     *
     * @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Spittle Not Found")
     * public class SpittleNotFoundException extends RuntimeException {
     *
     * }
     *
     * 在引入 @ResponseStatus 注解之后，如果控制器方法抛出 SpittleNotFoundException 异常的话，响应将会具有 404
     * 状态码，这是因为 Spittle Not Found。
     *
     *
     *
     * 编写异常处理的方法
     *
     * 在很多的场景下，将异常映射为状态码是很简单的方案，并且就功能来说也足够了。但是如果想在响应中不仅要包括状态码，
     * 还要包含所产生的错误，那该怎么办呢？此时的话，就不能将异常视为 HTTP 错误了，而是要按照处理请求的方式来处理
     * 异常了。
     *
     * 作为样例，假设用户试图创建的 Spittle 与已创建的 Spittle 文本完全相同，那么 SpittleRepository 的 save()
     * 方法将会抛出 DuplicateSpittleException 异常。这意味着 SpittleController 的 saveSpittle() 方法可能
     * 需要处理这个异常。如下面的代码所示，saveSpittle() 方法可以直接处理这个异常。
     *
     *     @RequestMapping(method = RequestMethod.POST)
     *     public String saveSpittle(SpittleForm form, Model model) {
     *         try {
     *             spittleRepository.save(new Spittle(null, form.getMessage(), new Date(),
     *                     form.getLongitude(), form.getLatitude()));
     *             return "redirect:/spittles";
     *         } catch (DuplicateSpittleException e) {
     *             return "error/duplicate";
     *         }
     *     }
     *
     * 这段代码并没有特别之处，它只是在 Java 中处理异常的基本样例，除此之外，也就没什么了。
     *
     * 它运行起来没什么问题，但是这个方法有些复杂。该方法可以有两个路径，每个路径会有不同的输出。如果能让
     * saveSpittle() 方法只关注正确的路径，而让其他方法处理异常的话，那么它就能简单一些。
     *
     * 首先，将 saveSpittle() 方法中的异常处理方法剥离掉：
     *
     *     @RequestMapping(method = RequestMethod.POST)
     *     public String saveSpittle(SpittleForm form, Model model) {
     *         spittleRepository.save(new Spittle(null, form.getMessage(), new Date(),
     *                 form.getLongitude(), form.getLatitude()));
     *         return "redirect:/spittles";
     *     }
     *
     * 可以看到，saveSpittle() 方法简单了许多。因为它只关注成功保存 Spittle 的情况，所以只有一个执行路径，很容易
     * 理解（和测试）。
     *
     * 现在，为 SpittleController 添加一个新的方法，它会处理抛出 DuplicateSpittleException 的情况：
     *
     *     @ExceptionHandler(DuplicateSpittleException.class)
     *     public String handleDuplicateSpittle() {
     *         return "error/duplicate";
     *     }
     *
     * handleDuplicateSpittle() 方法上添加了 @ExceptionHandler 注解，当抛出 DuplicateSpittleException
     * 异常的时候，将会委托该方法来处理。它返回的是一个 String，这与处理请求的方法是一致的，指定了要渲染的逻辑视图名，
     * 它能够告诉用户他们正在试图创建一条重复的条目。
     *
     * 对于 @ExceptionHandler 注解标注的方法来说，比较有意思的一点在于它能处理同一个控制器中所有处理器方法所抛出的
     * 异常。所以，尽管从 saveSpittle() 中抽取代码创建了 handleDuplicateSpittle() 方法，但是它能够处理
     * SpittleController 中所有方法所抛出的 DuplicateSpittleException 异常。不用在每一个可能抛出
     * DuplicateSpittleException 的方法中添加异常处理代码，这一个方法就涵盖了所有的功能。
     *
     * 既然 @ExceptionHandler 注解所标注的方法能够处理同一个控制器类中所有处理器方法的异常，那么你可能会问有没有一
     * 种方法能够处理所有控制器中处理器方法所抛出的异常呢。从 Spring 3.2 开始，这肯定是能够实现的，只需将其定义到控
     * 制器通知类中即可。
     */
    public static void main(String[] args) {

    }

}
