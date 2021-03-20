package com.siwuxie095.spring.chapter16th.example7th;

/**
 * @author Jiajing Li
 * @date 2021-03-20 17:26:53
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 编写 REST 客户端
     *
     * 作为客户端，编写与 REST 资源交互的代码可能会比较乏味，并且所编写的代码都是样板式的。例如，假设需要
     * 借助 Facebook 的 Graph API，编写方法来获取某人的 Facebook 基本信息。不过，获取基本信息的代码会
     * 有点复杂，如下所示。
     *
     * public Profile fetchFacebookProfile(String id) {
     *     try {
     *         HttpClient client = HttpClients.createDefault();
     *         HttpGet request = new HttpGet("http://graph.facebook.com/" + id);
     *         request.setHeader("Accept", "application/json");
     *         HttpResponse response = client.execute(request);
     *
     *         HttpEntity entity = response.getEntity();
     *         ObjectMapper mapper = new ObjectMapper();
     *         return mapper.readValue(entity.getContent(), Profile.class);
     *     } catch(IOException e) {
     *         throw new RuntimeException(e);
     *     }
     * }
     *
     * 可以看到，在使用 REST 资源的时候涉及很多代码。这里甚至还偷懒使用了 Jakarta Commons HTTP Client
     * 发起请求并使用 Jackson JSON processor 解析响应。
     *
     * 仔细看一下 fetchFacebookProfile() 方法，你可能会发现方法中只有少量代码与获取 Facebook 个人信息
     * 直接相关。如果你要编写另一个方法来使用其他的 REST 资源，很可能会有很多代码是与 fetchFacebookProfile()
     * 相同的。
     *
     * 另外，还有一些地方可能会抛出的 IOException 异常。因为 IOException 是检查型异常，所以要么捕获它，
     * 要么抛出它。在本示例中，选择捕获它并在它的位置重新抛出一个非检查型异常 RuntimeException。
     *
     * 鉴于在资源使用上有如此之多的样板代码，你可能会觉得最好的方式是封装通用代码并参数化可变的部分。这正是
     * Spring 的 RestTemplate 所做的事情。就像 JdbcTemplate 处理了 JDBC 数据访问时的丑陋部分，
     * RestTemplate 让使用 RESTful 资源时免于编写那些乏味的代码。
     *
     * 稍后，将会看到如何借助 RestTemplate 重写 fetchFacebookProfile() 方法，这会戏剧性的简化该方法并
     * 消除掉样板式代码。但首先，先整体了解一下 RestTemplate 提供的所有 REST 操作。
     *
     *
     *
     * 1、了解 RestTemplate 的操作
     *
     * RestTemplate 定义了 36 个与 REST 资源交互的方法，其中的大多数都对应于 HTTP 的方法。但是，这里不
     * 会介绍所有的 36 个方法。其实，这里面只有 11 个独立的方法，其中有十个有三种重载形式，而第十一个则重
     * 载了六次，这样一共形成了 36 个方法。如下描述了 RestTemplate 所提供的 11 个独立方法。
     * （1）delete()：在特定的 URL 上对资源执行 HTTP DELETE 操作；
     * （2）exchange()：在 URL 上执行特定的 HTTP 方法，返回包含对象的 ResponseEntity，这个对象是从响
     * 应体中映射得到的；
     * （3）execute()：在 URL 上执行特定的 HTTP 方法，返回一个从响应体映射得到的对象；
     * （4）getForEntity()：发送一个 HTTP GET 请求，返回的 ResponseEntity 包含了响应体所映射成的对象；
     * （5）getForObject()：发送一个 HTTP GET 请求，返回的请求体将映射为一个对象；
     * （6）headForHeaders()：发送 HTTP HEAD 请求，返回包含特定资源 URL 的 HTTP 头；
     * （7）optionsForAllow()：发送 HTTP OPTIONS 请求，返回对特定 URL 的 Allow 头信息；
     * （8）postForEntity()：POST 数据到一个 URL，返回包含一个对象的 ResponseEntity，这个对象是从响应
     * 体中映射得到的；
     * （9）postForLocation()：POST 数据到一个 URL，返回新创建资源的 URL；
     * （10）postForObject()：POST数据到一个 URL，返回根据响应体匹配形成的对象；
     * （11）put()：PUT 资源到特定的 URL。
     *
     * PS：RestTemplate 定义了 11 个独立的操作，而每一个都有重载，这样一共是 36 个方法。
     *
     * 除了 TRACE 以外，RestTemplate 涵盖了所有的 HTTP 动作。除此之外，execute() 和 exchange() 提供
     * 了较低层次的通用方法来使用任意的 HTTP 方法。
     *
     * 上面列出的大多数操作都以三种方法的形式进行了重载：
     * （1）一个使用 java.net.URI 作为 URL 格式，不支持参数化 URL；
     * （2）一个使用 String 作为 URL 格式，并使用 Map 指明 URL 参数；
     * （3）一个使用 String 作为 URL 格式，并使用可变参数列表指明 URL 参数。
     *
     * 明确了 RestTemplate 所提供的 11 个操作以及各个变种如何工作之后，你就能以自己的方式编写使用 REST
     * 资源的客户端了。通过对四个主要 HTTP 方法的支持（也就是 GET、PUT、DELETE 和 POST）来研究
     * RestTemplate 的操作。下面将从 GET 方法的 getForObject() 和 getForEntity() 开始。
     *
     *
     *
     * 2、GET 资源
     *
     * 上面列出了两种执行 GET 请求的方法：getForObject() 和 getForEntity()。每个方法又有三种形式的重载。
     * 三个 getForObject() 方法的签名如下：
     * public <T> T getForObject(String url, Class<T> responseType, Object... uriVariables)
     * throws RestClientException;
     * public <T> T getForObject(String url, Class<T> responseType, Map<String, ?> uriVariables)
     * throws RestClientException;
     * public <T> T getForObject(URI url, Class<T> responseType) throws RestClientException;
     *
     * 类似地，getForEntity() 方法的签名如下：
     * public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType,
     * Object... uriVariables) throws RestClientException;
     * public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType,
     * Map<String, ?> uriVariables) throws RestClientException;
     * public <T> ResponseEntity<T> getForEntity(URI url, Class<T> responseType)
     * throws RestClientException;
     *
     * 除了返回类型，getForEntity() 方法就是 getForObject() 方法的镜像。实际上，它们的工作方式大同小异。
     * 它们都执行根据 URL 检索资源的 GET 请求。它们都将资源根据 responseType 参数匹配为一定的类型。唯一
     * 的区别在于 getForObject() 只返回所请求类型的对象，而 getForEntity() 方法会返回请求的对象以及响应
     * 相关的额外信息。
     *
     * 下面首先看一下稍微简单的 getForObject() 方法。然后再看看如何使用 getForEntity() 方法来从 GET
     * 响应中获取更多的信息。
     *
     *
     *
     * 3、检索资源
     *
     * getForObject() 方法是检索资源的合适选择。请求一个资源并按照所选择的 Java 类型接收该资源。作为
     * getForObject() 能够做什么的一个简单示例，下面看一下 fetchFacebookProfile() 的另一个实现：
     *
     * public Profile fetchFacebookProfile(String id) {
     *     RestTemplate reset = new TestTemplate();
     *     return reset.getForObject("http://graph.facebook.com/{id}",
     *     Profile.class, id);
     * }
     *
     * 之前的 fetchFacebookProfile() 涉及十多行代码。通过使用 RestTemplate，现在减少到了几行。
     *
     * fetchFacebookProfile() 首先构建了一个 RestTemplate 的实例（另一种可行的方式是注入实例）。接下来，
     * 它调用了 getForObject() 来得到 Facebook 个人信息。为了做到这一点，它要求结果是 Profile 对象。在
     * 接收到 Profile 对象后，该方法将其返回给调用者。
     *
     * 注意，在这个新版本的 fetchFacebookProfile() 中，没有使用字符串连接来构建 URL，而是利用了
     * RestTemplate 可以接受参数化 URL 这一功能。URL 中的 {id} 占位符最终将会用方法的 id 参数
     * 来填充。getForObject() 方法的最后一个参数是大小可变的参数列表，每个参数都会按出现顺序插入到
     * 指定 URL 的占位符中。
     *
     * 另外一种替代方案是将 id 参数放到 Map 中，并以 id 作为 key，然后将这个 Map 作为最后一个参数传递给
     * getForObject()：
     *
     * public Profile fetchFacebookProfile(String id) {
     *     Map<String, String> urlVariables = new HashMap<>();
     *     urlVariables.put("id", id);
     *     RestTemplate reset = new TestTemplate();
     *     return reset.getForObject("http://graph.facebook.com/{id}",
     *     Profile.class, urlVariables);
     * }
     *
     * 这里没有任何形式的 JSON 解析和对象映射。在表面之下，getForObject() 将响应体转换为对象。它实现这些
     * 需要依赖 HTTP 消息转换器，与带有 @ResponseBody 注解的 Spring MVC 处理方法所使用的一样。
     *
     * 这个方法也没有任何异常处理。这不是因为 getForObject() 不能抛出异常，而是因为它抛出的异常都是非检查
     * 型的。如果在 getForObject() 中有错误，将抛出非检查型 RestClientException 异常（或者它的一些子类）。
     * 如果愿意的话，你可以捕获它 —— 但编译器不会强制你捕获它。
     *
     *
     *
     * 4、抽取响应的元数据
     *
     * 作为 getForObject() 的一个替代方案，RestTemplate 还提供了 getForEntity()。getForEntity() 方
     * 法与 getForObject() 方法的工作很相似。getForObject() 只返回资源（通过 HTTP 信息转换器将其转换为
     * Java对象），getForEntity() 会在 ResponseEntity 中返回相同的对象，而且 ResponseEntity 还带有关
     * 于响应的额外信息，如 HTTP 状态码和响应头。
     *
     * 可能想使用 ResponseEntity 所做的事就是获取响应头的一个值。例如，假设除了获取资源，还想要知道资源的
     * 最后修改时间。假设服务端在 LastModified 头部信息中提供了这个信息，可以这样像这样使用 getHeaders()
     * 方法：
     *
     * Date lastModified = new Date(response.getHeaders().getLastModified());
     *
     * getHeaders() 方法返回一个 HttpHeaders 对象，该对象提供了多个便利的方法来查询响应头，包括
     * getLastModified()，它将返回从 1970 年 1 月 1 日开始的毫秒数。
     *
     * 除了 getLastModified()，HttpHeaders 还包含如下的方法来获取头信息：
     *
     * public List<MediaType> getAccept();
     * public List<Charset> getAcceptCharset();
     * public Set<HttpMethod> getAllow();
     * public String getCacheControl();
     * public List<String> getConnection();
     * public long getContentLength();
     * public MediaType getContentType();
     * public long getDate();
     * public long getExpires();
     * public long getIfUnmodifiedSince();
     * public List<String> getIfNoneMatch();
     * public long getLastModified();
     * public URI getLocation();
     * public String getOrigin();
     * public String getPragma();
     * public String getUpgrade();
     *
     * 为了实现更通用的 HTTP 头信息访问，HttpHeaders 提供了 get() 方法和 getFirst() 方法。两个方法都接
     * 受 String 参数来标识所需要的头信息。get() 将会返回一个 String 值的列表，其中的每个值都是赋给该头部
     * 信息的，而 getFirst() 方法只会返回第一个头信息的值。
     *
     * 如果你对响应的 HTTP 状态码感兴趣，那么你可以调用 getStatusCode() 方法。例如，考虑下面这个获取
     * Spittle 对象的方法：
     *
     * public Spittle fetchSpittle(long id) {
     *     RestTemplate rest = new RestTemplate();
     *     ResponseEntity<Spittle> response = reset.getForEntity(
     *     "http://localhost:8080/spittr-api/spittles/{id}", Spittle.class, id);
     *     if (response.getStatusCode() == HttpStatus.NOT_MODIFIED) {
     *         throw new NotModifiedException();
     *     }
     *     return response.getBody();
     * }
     *
     * 在这里，如果服务器响应 304 状态，这意味着服务器端的内容自从上一次请求之后再也没有修改。在这种情况下，
     * 将会抛出自定义的 NotModifiedException 异常来表明客户端应该检查它的缓存来获取 Spittle。
     *
     *
     *
     * 5、PUT 资源
     *
     * 为了对数据进行 PUT 操作，RestTemplate 提供了三个简单的 put()方法。就像其他的 RestTemplate 方法
     * 一样，put() 方法有三种形式：
     * public void put(String url, Object request, Object... uriVariables)
     * throws RestClientException;
     * public void put(String url, Object request, Map<String, ?> uriVariables)
     * throws RestClientException;
     * public void put(URI url, Object request) throws RestClientException;
     *
     * 按照它最简单的形式，put() 接受一个 java.net.URI，用来标识（及定位）要将资源发送到服务器上，另外还
     * 接受一个对象，这代表了资源的 Java 表述。
     *
     * 例如，以下展现了如何使用基于 URI 版本的 put() 方法来更新服务器上的 Spittle 资源：
     *
     * public void updateSpittle(Spittle spittle) throws SpitterException {
     *     RestTemplate rest = new RestTemplate();
     *     String url = "http://localhost:8080/spittr-api/spittles/" + spittle.getId();
     *     rest.put(URI.create(url), spittle);
     * }
     *
     * 在这里，尽管方法签名很简单，但是使用 java.net.URI 作为参数的影响很明显。为了创建所更新 Spittle
     * 对象的 URL，要进行字符串拼接。
     *
     * 从 getForObject() 和 getForEntity() 方法中也看到了，使用基于 String 的其他 put() 方法能够减
     * 少创建 URI 的不便。这些方法可以将 URI 指定为模板并对可变部分插入值。以下是使用基于 String 的 put()
     * 方法重写的 updateSpittle()：
     *
     * public void updateSpittle(Spittle spittle) throws SpitterException {
     *     RestTemplate rest = new RestTemplate();
     *     rest.put("http://localhost:8080/spittr-api/spittles/{id}", spittle, spittle.getId());
     * }
     *
     * 现在的 URI 使用简单的 String 模板来进行表示。当 RestTemplate 发送 PUT 请求时，URI 模板将 {id}
     * 部分用 spittle.getId() 方法的返回值来进行替换。就像 getForObject() 和 getForEntity() 一样，
     * 这个版本的 put() 方法最后一个参数是大小可变的参数列表，每一个值会出现按照顺序赋值给占位符变量。
     *
     * 你还可以将模板参数作为 Map 传递进来：
     *
     * public void updateSpittle(Spittle spittle) throws SpitterException {
     *     RestTemplate rest = new RestTemplate();
     *     Map<String, String> params = new HashMap<>();
     *     params.put("id", spittle.getId());
     *     rest.put("http://localhost:8080/spittr-api/spittles/{id}", spittle, params);
     * }
     *
     * 当使用 Map 来传递模板参数时，Map 条目的每个 key 值与 URI 模板中占位符变量的名字相同。
     *
     * 在所有版本的 put() 中，第二个参数都是表示资源的 Java 对象，它将按照指定的 URI 发送到服务器端。在
     * 本示例中，它是一个 Spittle 对象。RestTemplate 将使用某个 HTTP 消息转换器将 Spittle 对象转换为
     * 一种表述形式，并在请求体中将其发送到服务器端。
     *
     * 对象将被转换成什么样的内容类型很大程度上取决于传递给 put() 方法的类型。如果给定一个 String 值，那
     * 么将会使用 StringHttpMessageConverter：这个值直接被写到请求体中，内容类型设置为 "text/plain"。
     * 如果给定一个 MultiValueMap<String,String>，那么这个 Map 中的值将会被 FormHttpMessageConverter
     * 以 "application/x-www-form-urlen-coded" 的格式写到请求体中。
     *
     * 因为传递进来的是 Spittle 对象，所以需要一个能够处理任意对象的信息转换器。如果在类路径下包含 Jackson
     * 库，那么 MappingJacksonHttpMessageConverter 将以 application/json 格式将 Spittle 写到请求中。
     *
     *
     *
     * 6、DELETE 资源
     *
     * 当你不需要在服务端保留某个资源时，那么可能需要调用 RestTemplate 的 delete() 方法。就像 PUT 方法
     * 那样，delete() 方法有三个版本，它们的签名如下：
     * public void delete(String url, Object... uriVariables) throws RestClientException;
     * public void delete(String url, Map<String, ?> uriVariables) throws RestClientException;
     * public void delete(URI url) throws RestClientException;
     *
     * 很容易吧，delete() 方法是所有 RestTemplate 方法中最简单的。你唯一要提供的就是要删除资源的 URI。
     * 例如，为了删除指定 ID 的 Spittle，你可以这样调用 delete()：
     *
     * public void deleteSpittle(long id) {
     *     RestTemplate rest = new RestTemplate();
     *     String url = "http://localhost:8080/spittr-api/spittles/" + id;
     *     rest.delete(URI.create(url));
     * }
     *
     * 这很简单，但在这里还是依赖字符串连接来创建 URI 对象。所以，再看一个更简单的 delete() 方法，它能够
     * 使得免于这些麻烦：
     *
     * public void deleteSpittle(long id) {
     *     RestTemplate rest = new RestTemplate();
     *     rest.delete("http://localhost:8080/spittr-api/spittles/{id}", id);
     * }
     *
     * 现在已经展现了最简单的 RestTemplate 方法，下面看看 RestTemplate 最多样化的一组方法 —— 它们能够
     * 支持 HTTP POST 请求。
     *
     *
     *
     * 7、POST 资源数据
     *
     * RestTemplate 有三个不同类型的方法来发送 POST 请求。当你再乘上每个方法的三个不同变种，那就是有九个
     * 方法来 POST 数据到服务器端。
     *
     * 这些方法中有两个的名字看起来比较类似。postForObject() 和 postForEntity() 对 POST 请求的处理方
     * 式与发送 GET 请求的 getForObject() 和 getForEntity() 方法是类似的。另一个方法是
     * postForLocation()，它是 POST 请求所特有的。
     *
     *
     *
     * 8、在 POST 请求中获取响应对象
     *
     * 假设你正在使用 RestTemplate 来 POST 一个新的 Spitter 对象到 Spittr 应用程序的 REST API。因为
     * 这是一个全新的 Spitter，服务端并（还）不知道它。因此，它还不是真正的 REST 资源，也没有 URL。另外，
     * 在服务端创建之前，客户端并不知道 Spitter 的 ID。
     *
     * POST 资源到服务端的一种方式是使用 RestTemplate 的 postForObject() 方法。postForObject() 方
     * 法的三个变种签名如下：
     * public <T> T postForObject(String url, Object request, Class<T> responseType,
     * Object... uriVariables) throws RestClientException;
     * public <T> T postForObject(String url, Object request, Class<T> responseType,
     * Map<String, ?> uriVariables) throws RestClientException;
     * public <T> T postForObject(URI url, Object request, Class<T> responseType)
     * throws RestClientException;
     *
     * 在所有情况下，第一个参数都是资源要 POST 到的 URL，第二个参数是要发送的对象，而第三个参数是预期返回
     * 的 Java 类型。在将 URL 作为 String 类型的两个版本中，第四个参数指定了 URL 变量（要么是可变参数列
     * 表，要么是一个 Map）。
     *
     * 当 POST 新的 Spitter 资源到 Spitter REST API 时，它们应该发送到
     * http://localhost:8080/spittr-api/spitters，这里会有一个应对 POST 请求的处理方法来保存对象。
     * 因为这个 URL 不需要 URL 参数，所以可以使用任何版本的 postForObject()。但为了保持尽可能简单，
     * 可以这样调用：
     *
     * public Spitter postSpitterForObject(Spitter spitter) {
     *     RestTemplate rest = new RestTemplate();
     *     return rest.postForObject("http://localhost:8080/spittr-api/spitter",
     *     spitter, Spitter.class);
     * }
     *
     * postSpitterForObject() 方法给定了一个新创建的 Spitter 对象，并使用 postForObject() 将其发送
     * 到服务器端。在响应中，它接收到一个 Spitter 对象并将其返回给调用者。
     *
     * 就像 getForEntity() 方法一样，你可能想得到请求带回来的一些元数据。在这种情况下，postForEntity()
     * 是更合适的方法。postForEntity() 方法有着与 postForObject() 几乎相同的一组签名：
     *
     * public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType,
     * Object... uriVariables) throws RestClientException;
     * public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType,
     * Map<String, ?> uriVariables) throws RestClientException;
     * public <T> ResponseEntity<T> postForEntity(URI url, Object request, Class<T> responseType)
     * throws RestClientException;
     *
     * 假设除了要获取返回的 Spitter 资源，还要查看响应中 Location 头信息的值。在这种情况下，你可以这样调用
     * postForEntity()：
     *
     * RestTemplate rest = new RestTemplate();
     * ResponseEntity<Spittle> response = rest.postForEntity(
     * "http://localhost:8080/spittr-api/spitter", spitter, Spitter.class);
     * Spitter spitter = response.getBody();
     * URI url = response.getHeaders().getLocation();
     *
     * 与 getForEntity() 方法一样，postForEntity() 返回一个 ResponseEntity<T> 对象。你可以调用这个
     * 对象的 getBody() 方法以获取资源对象（在本示例中是 Spitter）。getHeaders() 会给你一个 HttpHeaders，
     * 通过它可以访问响应中返回的各种 HTTP 头信息。这里，调用 getLocation() 来得到 java.net.URI 形式
     * 的 Location 头信息。
     *
     *
     *
     * 9、在 POST 请求后获取资源位置
     *
     * 如果要同时接收所发送的资源和响应头，postForEntity() 方法是很便利的。但通常你并不需要将资源发送回来
     * （毕竟，将其发送到服务器端是第一位的）。如果你真正需要的是 Location 头信息的值，那么使用
     * RestTemplate 的 postForLocation() 方法会更简单。
     *
     * 类似于其他的 POST 方法，postForLocation() 会在 POST 请求的请求体中发送一个资源到服务器端。但是，
     * 响应不再是相同的资源对象，postForLocation() 的响应是新创建资源的位置。它有如下三个方法签名：
     * public URI postForLocation(String url, Object request, Object... uriVariables)
     * throws RestClientException;
     * public URI postForLocation(String url, Object request, Map<String, ?> uriVariables)
     * throws RestClientException;
     * public URI postForLocation(URI url, Object request) throws RestClientException;
     *
     * 为了展示 postForLocation()，再次 POST 一个 Spitter。这次，希望在返回中包含资源的 URL：
     *
     * public String postSpitter(Spitter spitter) {
     *     RestTemplate rest = new TestTemplate();
     *     return rest.postForLocation("http://localhost:8080/spittr-api/spitters",
     *     spitter).toString();
     * }
     *
     * 在这里，以 String 的形式将目标 URL 传递进来，还有要 POST 的 Spitter 对象（在本示例中没有 URL
     * 参数）。在创建资源后，如果服务端在响应的 Location 头信息中返回新资源的 URL，接下来
     * postForLocation() 会以 String 的格式返回该 URL。
     *
     *
     *
     * 10、交换资源
     *
     * 到目前为止，已经看到 RestTemplate 的各种方法来 GET、PUT、DELETE 以及 POST 资源。在它们之中，
     * 看到两个特殊的方法：getForEntity() 和 postForEntity()，这两个方法将结果资源包含在一个
     * ResponseEntity 对象中，通过这个对象可以得到响应头和状态码。
     *
     * 能够从响应中读取头信息是很有用的。但是如果你想在发送给服务端的请求中设置头信息的话，怎么办呢？这
     * 就是 RestTemplate 的 exchange() 的用武之地。
     *
     * 像 RestTemplate 的其他方法一样，exchange() 也重载为三个签名格式。一个使用 java.net.URI 来
     * 标识目标 URL，而其他两个以 String 的形式传入 URL 并带有 URL 变量。如下所示：
     *
     * public <T> ResponseEntity<T> exchange(String url, HttpMethod method,
     * 			HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables)
     * 			throws RestClientException;
     * public <T> ResponseEntity<T> exchange(String url, HttpMethod method,
     * 			HttpEntity<?> requestEntity, Class<T> responseType, Map<String, ?> uriVariables)
     * 			throws RestClientException;
     * public <T> ResponseEntity<T> exchange(URI url, HttpMethod method, HttpEntity<?> requestEntity,
     * 			Class<T> responseType)
     * 			throws RestClientException;
     *
     * exchange() 方法使用 HttpMethod 参数来表明要使用的 HTTP 动作。根据这个参数的值，exchange()
     * 能够执行与其他 RestTemplate 方法一样的工作。
     *
     * 例如，从服务器端获取 Spitter 资源的一种方式是使用 RestTemplate 的 getForEntity() 方法，如
     * 下所示：
     *
     * ResponseEntity<Spitter> response = rest.getForEntity(
     *      "http://localhost:8080/spittr-api/spitters/{id}", Spitter.class, spitterId);
     * Spitter spitter = response.getBody();
     *
     * 在下面的代码片段中，可以看到 exchange() 也可以完成这项任务：
     *
     * Response<Spitter> response = rest.exchange(
     *      "http://localhost:8080/spittr-api/spitters/{id}",
     *      HttpMethod.GET, null, Spitter.class, spitterId);
     * Spitter spitter = response.getBody();
     *
     * 通过传入 HttpMethod.GET 作为 HTTP 动作，会要求 exchange() 发送一个 GET 请求。第三个参数是
     * 用于在请求中发送资源的，但因为这是一个 GET 请求，它可以是 null。下一个参数表明希望将响应转换为
     * Spitter 对象。最后一个参数用于替换 URL 模板中 {id} 占位符的值。
     *
     * 按照这种方式，exchange() 与之前使用的 getForEntity() 是几乎相同的，但是，不同于 getForEntity()
     * 或 getForObject()，exchange() 方法允许在请求中设置头信息。接下来，不再给 exchange() 传递 null，
     * 而是传入带有请求头信息的 HttpEntity。
     *
     * 如果不指明头信息，exchange() 对 Spitter 的 GET 请求会带有如下的头信息：
     *
     * GET /Spitter/spitters/habuma HTTP/1.1
     * Accept: application/xml, text/xml, application/*+xml, application/json
     * Content-Length: 0
     * User-Agent: Java/1.6.0_20
     * Host: localhost:8080
     * Connection: keep-alive
     *
     * 这里 Accept 头信息表明它能够接受多种不同的 XML 内容类型以及 application/json。这样服务器端在决定
     * 采用哪种格式返回资源时，就有很大的可选空间。假设希望服务端以 JSON 格式发送资源。在这种情况下，需要将
     * "application/json" 设置为 Accept 头信息的唯一值。
     *
     * 设置请求头信息是很简单的，只需构造发送给 exchange() 方法的 HttpEntity 对象即可，HttpEntity 中包
     * 含承载头信息的 MultiValueMap：
     *
     * MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
     * headers.add("Accept", "application/json");
     * HttpEntity<Object> reqeustEntity = new HttpEntity<>(headers);
     *
     * 在这里，创建了一个 LinkedMultiValueMap 并添加值为 "application/json" 的 Accept 头信息。接下来，
     * 构建了一个 HttpEntity（使用 Object 泛型类型），将 MultiValueMap 作为构造参数传入。如果这是一个
     * PUT 或 POST 请求，需要为 HttpEntity 设置在请求体中发送的对象 —— 对于 GET 请求来说，这是没有必要
     * 的。
     *
     * 现在，可以传入 HttpEntity 来调用 exchange()：
     *
     * Response<Spitter> response = rest.exchange(
     *      "http://localhost:8080/spittr-api/spitters/{id}",
     *      HttpMethod.GET, requestEntity, Spitter.class, spitterId);
     * Spitter spitter = response.getBody();
     *
     * 表面上看，结果是一样的。都得到了请求的 Spitter 对象。但在表面之下，请求将会带有如下的头信息发送：
     *
     * GET /Spitter/spitters/habuma HTTP/1.1
     * Accept: application/json
     * Content-Length: 0
     * User-Agent: Java/1.6.0_20
     * Host: localhost:8080
     * Connection: keep-alive
     *
     * 假设服务器端能够将 Spitter 序列化为 JSON，响应体将会以 JSON 格式来进行表述。
     */
    public static void main(String[] args) {

    }

}
