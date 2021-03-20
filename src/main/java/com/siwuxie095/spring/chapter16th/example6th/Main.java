package com.siwuxie095.spring.chapter16th.example6th;

/**
 * @author Jiajing Li
 * @date 2021-03-20 15:59:48
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 提供资源之外的其他内容
     *
     * @ResponseBody 提供了一种很有用的方式，能够将控制器返回的 Java 对象转换为发送到客户端的资源表述。实际上，
     * 将资源表述发送给客户端只是整个过程的一部分。一个好的 REST API 不仅能够在客户端和服务器之间传递资源，它还
     * 能够给客户端提供额外的元数据，帮助客户端理解资源或者在请求中出现了什么情况。
     *
     *
     *
     * 1、发送错误信息到客户端
     *
     * 例如，为 SpittleController 添加一个新的处理器方法，它会提供单个 Spittle 对象：
     *
     *     @RequestMapping(value = "/{id}", method = RequestMethod.GET)
     *     public @ResponseBody Spittle spittleById(@PathVariable Long id) {
     *         return spittleRepository.findOne(id);
     *     }
     *
     * 在这里，通过 id 参数传入了一个 ID，然后根据它调用 Repository 的 findOne() 方法，查找 Spittle 对象。
     * 处理器方法会返回 findOne() 方法得到的 Spittle 对象，消息转换器会负责产生客户端所需的资源表述。
     *
     * 非常简单，对吧？它还能更好吗？
     *
     * 如果根据给定的 ID，无法找到某个 Spittle 对象的 ID 属性能够与之匹配，findOne() 方法返回 null 的时候，
     * 你觉得会发生什么呢？
     *
     * 结果就是 spittleById() 方法会返回 null，响应体为空，不会返回任何有用的数据给客户端。同时，响应中默认的
     * HTTP 状态码是 200（OK），表示所有的事情运行正常。
     *
     * 但是，所有的事情都是不对的。客户端要求 Spittle 对象，但是它什么都没有得到。它既没有收到 Spittle 对象也
     * 没有收到任何消息表明出现了错误。服务器实际上是在说："这是一个没用的响应，但是能够告诉你一切都正常！"
     *
     * 现在，考虑一下在这种场景下应该发生什么。至少，状态码不应该是 200，而应该是 404（Not Found），告诉客户端
     * 它们所要求的内容没有找到。如果响应体中能够包含错误信息而不是空的话就更好了。
     *
     * Spring 提供了多种方式来处理这样的场景：
     * （1）使用 @ResponseStatus 注解可以指定状态码；
     * （2）控制器方法可以返回 ResponseEntity 对象，该对象能够包含更多响应相关的元数据；
     * （3）异常处理器能够应对错误场景，这样处理器方法就能关注于正常的状况。
     *
     * 在这个方面，Spring 提供了很多的灵活性，其实也不存在唯一正确的方式。这里不会用某一种固定的策略来处理所有的
     * 错误或涵盖所有的场景，而是会展现多种修改 spittleById() 的方法，以应对 Spittle 无法找到的场景。
     *
     *
     * 1.1、使用 ResponseEntity
     *
     * 作为 @ResponseBody 的替代方案，控制器方法可以返回一个 ResponseEntity 对象。ResponseEntity 中可以包
     * 含响应相关的元数据（如头部信息和状态码）以及要转换成资源表述的对象。
     *
     * 因为 ResponseEntity 允许指定响应的状态码，所以当无法找到 Spittle 的时候，可以返回 HTTP 404 错误。如下
     * 是新版本的 spittleById()，它会返回 ResponseEntity：
     *
     *     @RequestMapping(value = "/{id}", method = RequestMethod.GET)
     *     public ResponseEntity<Spittle> spittleById(@PathVariable Long id) {
     *          Spittle spittle = spittleRepository.findOne(id);
     *          HttpStatus status = (spittle != null) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
     *          return new ResponseEntity<Spittle>(spittle, status);
     *     }
     *
     * 像前面一样，路径中得到的 ID 用来从 Repository 中检索 Spittle。如果找到的话，状态码设置为 HttpStatus.OK
     * （这是之前的默认值），但是如果 Repository 返回 null 的话，状态码设置为 HttpStatus.NOT_FOUND，这会转换
     * 为 HTTP 404。最后，会创建一个新的 ResponseEntity，它会把 Spittle 和状态码传送给客户端。
     *
     * 注意这个 spittleById() 方法没有使用 @ResponseBody 注解。除了包含响应头信息、状态码以及负载以外，
     * ResponseEntity 还包含了 @ResponseBody 的语义，因此负载部分将会渲染到响应体中，就像之前在方法上
     * 使用 @ResponseBody 注解一样。如果返回 ResponseEntity 的话，那就没有必要在方法上使用
     * @ResponseBody 注解了。
     *
     * 现在在正确的方向上走出了第一步，如果所要求的 Spittle 无法找到的话，客户端能够得到一个合适的状态码。但是在
     * 本例中，响应体依然为空。这里可能会希望在响应体中包含一些错误信息。
     *
     * 这里重试一次，首先定义一个包含错误信息的 Error 对象：
     *
     * public class Error {
     *
     *     private int code;
     *     private String message;
     *
     *     public Error(int code, String message) {
     *         this.code = code;
     *         this.message = message;
     *     }
     *
     *     public int getCode() {
     *         return code;
     *     }
     *
     *     public String getMessage() {
     *         return message;
     *     }
     *
     * }
     *
     * 然后，可以修改 spittleById()，让它返回 Error：
     *
     *     @RequestMapping(value = "/{id}", method = RequestMethod.GET)
     *     public ResponseEntity<?> spittleById(@PathVariable Long id) {
     *          Spittle spittle = spittleRepository.findOne(id);
     *          if (spittle == null) {
     *              Error error = new Error(4, "Spittle [" + id + "] not found");
     *              return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
     *          }
     *          return new ResponseEntity<Spittle>(spittle, HttpStatus.OK);
     *     }
     *
     * 现在，这个方法的行为已经符合预期了。如果找到 Spittle 的话，就会把返回的对象以及 200（OK）的状态码封装到
     * ResponseEntity 中。另一方面，如果 findOne() 返回 null 的话，将会创建一个 Error 对象，并将其与 404
     * （Not Found）状态码一起封装到 ResponseEntity 中，然后返回。
     *
     * 你也许觉得可以到此结束这个话题了。毕竟，方法已经按照期望的方式在运行。但是，还有一点事情让人不太舒服。
     *
     * 首先，这比开始的时候更为复杂。涉及到了更多的逻辑，包括条件语句。另外，方法返回 ResponseEntity<?> 感觉
     * 有些问题。ResponseEntity 所使用的泛型为它的解析或出现错误留下了太多的空间。
     *
     * 不过，可以借助错误处理器来修正这些问题。
     *
     *
     * 1.2、处理错误
     *
     * spittleById() 方法中的 if 代码块是处理错误的，但这是控制器中错误处理器（error handler）所擅长的领域。
     * 错误处理器能够处理导致问题的场景，这样常规的处理器方法就能只关心正常的逻辑处理路径了。
     *
     * 下面重构一下代码来使用错误处理器。首先，定义能够对应 SpittleNotFoundException 的错误处理器：
     *
     *     @ExceptionHandler(SpittleNotFoundException.class)
     *     public ResponseEntity<Error> spittleNotFound(SpittleNotFoundException e) {
     *         long spittleId = e.getSpittleId();
     *         Error error = new Error(4, "Spittle [" + spittleId + "] not found");
     *         return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
     *     }
     *
     * @ExceptionHandler 注解能够用到控制器方法中，用来处理特定的异常。这里，它表明如果在控制器的任意处理方法
     * 中抛出 SpittleNotFoundException 异常，就会调用 spittleNotFound() 方法来处理异常。
     *
     * 至于 SpittleNotFoundException，它是一个很简单异常类：
     *
     * public class SpittleNotFoundException extends RuntimeException {
     *
     *     private static final long serialVersionUID = 1L;
     *
     *     private long spittleId;
     *
     *     public SpittleNotFoundException(long spittleId) {
     *         this.spittleId = spittleId;
     *     }
     *
     *     public long getSpittleId() {
     *         return spittleId;
     *     }
     *
     * }
     *
     * 现在，可以移除掉 spittleById() 方法中大多数的错误处理代码：
     *
     *     @RequestMapping(value = "/{id}", method = RequestMethod.GET)
     *     public ResponseEntity<Spittle> spittleById(@PathVariable Long id) {
     *          Spittle spittle = spittleRepository.findOne(id);
     *          if (spittle == null) {
     *          throw new SpittleNotFoundException(id);
     *          }
     *          return new ResponseEntity<Spittle>(spittle, HttpStatus.OK);
     *     }
     *
     * 这个版本的 spittleById() 方法确实干净了很多。除了对返回值进行 null 检查，它完全关注于成功的场景，也就是
     * 能够找到请求的 Spittle。同时，在返回类型中，能移除掉奇怪的泛型了。
     *
     * 不过，其实能够让代码更加干净一些。现在已经知道 spittleById() 将会返回 Spittle 并且 HTTP 状态码始终会是
     * 200（OK），那么就可以不再使用 ResponseEntity，而是将其替换为 @ResponseBody：
     *
     *     @RequestMapping(value = "/{id}", method = RequestMethod.GET)
     *     public @ResponseBody Spittle spittleById(@PathVariable Long id) {
     *          Spittle spittle = spittleRepository.findOne(id);
     *          if (spittle == null) {
     *          throw new SpittleNotFoundException(id);
     *          }
     *          return spittle;
     *     }
     *
     * 当然，如果控制器类上使用了 @RestController，甚至不再需要 @ResponseBody：
     *
     *     @RequestMapping(value = "/{id}", method = RequestMethod.GET)
     *     public Spittle spittleById(@PathVariable Long id) {
     *          Spittle spittle = spittleRepository.findOne(id);
     *          if (spittle == null) {
     *          throw new SpittleNotFoundException(id);
     *          }
     *          return spittle;
     *     }
     *
     * 鉴于错误处理器的方法会始终返回 Error，并且 HTTP 状态码为 404（Not Found），那么现在可以对
     * spittleNotFound() 方法进行类似的清理：
     *
     *     @ExceptionHandler(SpittleNotFoundException.class)
     *     @ResponseStatus(HttpStatus.NOT_FOUND)
     *     public @ResponseBody Error spittleNotFound(SpittleNotFoundException e) {
     *         long spittleId = e.getSpittleId();
     *         return new Error(4, "Spittle [" + spittleId + "] not found");
     *     }
     *
     * 因为 spittleNotFound() 方法始终会返回 Error，所以使用 ResponseEntity 的唯一原因就是能够设置状态码。
     * 但是通过为 spittleNotFound() 方法添加 @ResponseStatus(HttpStatus.NOT_FOUND) 注解，可以达到相同
     * 的效果，而且可以不再使用 ResponseEntity 了。
     *
     * 同样，如果控制器类上使用了 @RestController，那么就可以移除掉 @ResponseBody，让代码更加干净：
     *
     *     @ExceptionHandler(SpittleNotFoundException.class)
     *     @ResponseStatus(HttpStatus.NOT_FOUND)
     *     public Error spittleNotFound(SpittleNotFoundException e) {
     *         long spittleId = e.getSpittleId();
     *         return new Error(4, "Spittle [" + spittleId + "] not found");
     *     }
     *
     * 在一定程度上，已经圆满达到了想要的效果。为了设置响应状态码，首先使用 ResponseEntity，但是稍后借助异常
     * 处理器以及 @ResponseStatus，避免使用 ResponseEntity，从而让代码更加整洁。
     *
     * 似乎，这里不再需要使用 ResponseEntity 了。但是，有一种场景 ResponseEntity 能够很好地完成，但是其他
     * 的注解或异常处理器却做不到。现在，看一下如何在响应中设置头部信息。
     *
     *
     *
     * 2、在响应中设置头部信息
     *
     * 在 saveSpittle() 方法中，在处理 POST 请求的过程中创建了一个新的 Spittle 资源。但是，按照目前的写法，
     * 无法准确地与客户端交流。
     *
     * 目前的写法：
     *
     *     @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
     *     public Spittle saveSpittle(@RequestBody Spittle spittle) {
     *         return spittleRepository.save(spittle);
     *     }
     *
     * 在 saveSpittle() 处理完请求之后，服务器在响应体中包含了 Spittle 的表述以及 HTTP 状态码 200（OK），
     * 将其返回给客户端。这里没有什么大问题，但是还不是完全准确。
     *
     * 当然，假设处理请求的过程中成功创建了资源，状态可以视为 OK。但是，应该不仅仅需要说 "OK"。这里创建了新
     * 的内容，HTTP 状态码也将这种情况告诉给了客户端。不过，HTTP 201 不仅能够表明请求成功完成，而且还能描述
     * 创建了新资源。如果希望完整准确地与客户端交流，那么响应是不是应该为 201（Created），而不仅仅是 200
     * （OK）呢？
     *
     * 这个问题解决起来很容易。需要做的就是为 saveSpittle() 方法添加 @ResponseStatus 注解，如下所示：
     *
     *     @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
     *     @ResonseStatus(HttpStatus.CREATED)
     *     public Spittle saveSpittle(@RequestBody Spittle spittle) {
     *         return spittleRepository.save(spittle);
     *     }
     *
     * 这应该能够完成任务，现在状态码能够精确反应发生了什么情况。它告诉客户端新创建了资源。问题已经得以解决。
     *
     * 但这只是问题的一部分。客户端知道新创建了资源，你觉得客户端会不会感兴趣新创建的资源在哪里呢？毕竟，这是
     * 一个新创建的资源，会有一个新的 URL 与之关联。难道客户端只能猜测新创建资源的 URL 是什么吗？能不能以某
     * 种方式将其告诉客户端？
     *
     * 当创建新资源的时候，将资源的 URL 放在响应的 Location 头部信息中，并返回给客户端是一种很好的方式。因
     * 此，需要有一种方式来填充响应头部信息，此时老朋友 ResponseEntity 就能提供帮助了。
     *
     * 如下代码展现了一个新版本的 saveSpittle()，它会返回 ResponseEntity 用来告诉客户端新创建的资源。
     *
     *     @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
     *     public ResponseEntity<Spittle> saveSpittle(@RequestBody Spittle spittle) {
     *         Spittle saved = spittleRepository.save(spittle);
     *
     *         HttpHeaders headers = new HttpHeaders();
     *         URI locationUri = URI.create(
     *         "http://localhost:8080/spittr/spittles/" + spittle.getId());
     *         headers.setLocation(locationUri);
     *
     *         ResponseEntity<Spittle> responseEntity = new ResponseEntity<Spittle>(saved,
     *         headers, HttpStatus.CREATED);
     *         return responseEntity;
     *     }
     *
     * 在这个新的版本中，创建了一个 HttpHeaders 实例，用来存放希望在响应中包含的头部信息值。HttpHeaders
     * 是 MultiValueMap<String, String> 的特殊实现，它有一些便利的 Setter 方法（如 setLocation()），
     * 用来设置常见的 HTTP 头部信息。在得到新创建 Spittle 资源的 URL 之后，接下来使用这个头部信息来创建
     * ResponseEntity。
     *
     * 原本简单的 saveSpittle() 方法瞬间变得臃肿了。但是，更值得关注的是，它使用硬编码值的方式来构建
     * Location 头部信息。URL 中 "localhost" 以及 "8080" 这两个部分尤其需要注意，因为如果将应用部
     * 署到其他地方，而不是在本地运行的话，它们就不适用了。
     *
     * 其实没有必要手动构建 URL，Spring 提供了 UriComponentsBuilder，可以有一些帮助。它是一个构建类，
     * 通过逐步指定 URL 中的各种组成部分（如 host、端口、路径以及查询），能够使用它来构建 UriComponents
     * 实例。借助 UriComponentsBuilder 所构建的 UriComponents 对象，就能获得适合设置给 Location
     * 头部信息的 URI。
     *
     * 为了使用 UriComponentsBuilder，需要做的就是在处理器方法中将其作为一个参数：
     *
     *     @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
     *     public ResponseEntity<Spittle> saveSpittle(@RequestBody Spittle spittle,
     *     UriComponentsBuilder ucb) {
     *         Spittle saved = spittleRepository.save(spittle);
     *
     *         HttpHeaders headers = new HttpHeaders();
     *         URI locationUri = ucb.path("/spittles/")
     *                 .path(String.valueOf(saved.getId()))
     *                 .build()
     *                 .toUri();
     *         headers.setLocation(locationUri);
     *
     *         ResponseEntity<Spittle> responseEntity = new ResponseEntity<Spittle>(saved,
     *         headers, HttpStatus.CREATED);
     *         return responseEntity;
     *     }
     *
     * 在处理器方法所得到的 UriComponentsBuilder 中，会预先配置已知的信息如 host、端口以及 Servlet 内容。
     * 它会从处理器方法所对应的请求中获取这些基础信息。基于这些信息，代码会通过设置路径的方式构建
     * UriComponents 其余的部分。
     *
     * 注意，路径的构建分为两步。第一步调用 path() 方法，将其设置为 "/spittles/"，也就是这个控制器所能处理
     * 的基础路径。然后，在第二次调用 path() 的时候，使用了已保存 Spittle 的 ID。可以推断出来，每次调用
     * path() 都会基于上次调用的结果。
     *
     * 在路径设置完成之后，调用 build() 方法来构建 UriComponents 对象，根据这个对象调用 toUri() 就能得到
     * 新创建 Spittle 的 URI。
     *
     * 在 REST API 中暴露资源只代表了会话的一端。如果发布的 API 没有人关心和使用的话，那也没有什么价值。通常
     * 来讲，移动或 JavaScript 应用会是 REST API 的客户端，但是 Spring 应用也完全可以使用这些资源。
     *
     * 换个方向，后续将会介绍如何编写 Spring 代码实现 RESTful 交互的客户端。
     */
    public static void main(String[] args) {

    }

}
