package com.siwuxie095.spring.chapter7th.example5th;

/**
 * @author Jiajing Li
 * @date 2021-02-02 08:08:38
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 处理 multipart 请求
     *
     * 现在已经在 Spring 中（或 Servlet 容器中）配置好了对 multipart 请求的处理，那么接下来就可以编写控制器方法
     * 来接收上传的文件。要实现这一点，最常见的方式就是在某个控制器方法参数上添加 @RequestPart 注解。
     *
     * 假设允许用户在注册 Spittr 应用的时候上传一张图片，那么需要修改表单，以允许用户选择要上传的图片，同时还需要修
     * 改 SpitterController 中的 processRegistration() 方法来接收上传的图片。如下的代码片段来源于 Thymeleaf
     * 注册表单视图（registrationForm.html），着重强调了表单所需的修改：
     *
     *     <form enctype="multipart/form-data" method="POST" th:object="${spitter}">
     *         ...
     *         <label>Profile Picture</label>:
     *         <input accept="image/jpeg,image/png,image/gif"
     *                name="profilePicture"
     *                type="file"/><br/>
     *         ...
     *     </form>
     *
     * <form> 标签现在将 enctype 属性设置为 multipart/form-data，这会告诉浏览器以 multipart 数据的形式提交表
     * 单，而不是以表单数据的形式进行提交。在 multipart 中，每个输入域都会对应一个 part。
     *
     * 除了注册表单中已有的输入域，还添加了一个新的 <input> 域，其 type 为 file。这能够让用户选择要上传的图片文件。
     * accept 属性用来将文件类型限制为 JPEG、PNG 以及 GIF 图片。根据其 name 属性，图片数据将会发送到 multipart
     * 请求中的 profilePicture part 之中。
     *
     * 现在，需要修改 processRegistration() 方法，使其能够接受上传的图片。其中一种方式是添加 byte 数组参数，并为
     * 其添加 @RequestPart 注解。如下为示例：
     *
     *     @RequestMapping(value = "/register", method = POST)
     *     public String processRegistration(
     *             @RequestPart("profilePicture") byte[] profilePicture,
     *             @Valid Spitter spitter,
     *             Errors errors) {
     *          ...
     *     }
     *
     * 当注册表单提交的时候，profilePicture 属性将会给定一个 byte 数组，这个数组中包含了请求中对应 part 的数据
     * （通过 @RequestPart 指定）。如果用户提交表单的时候没有选择文件，那么这个数组会是空（而不是 null）。 获取
     * 到图片数据后，processRegistration() 方法剩下的任务就是将文件保存到某个位置。
     *
     * 稍后将会讨论如何保存文件。但首先，想一下，对于提交的图片数据都了解哪些信息呢。或者，更为重要的是，还不知道些
     * 什么呢？尽管已经得到了 byte 数组形式的图片数据，并且根据它能够得到图片的大小，但是对于其他内容就一无所知了。
     * 不知道文件的类型是什么，甚至不知道原始的文件名是什么。你需要判断如何将 byte 数组转换为可存储的文件。
     *
     *
     *
     * 接受 MultipartFile
     *
     * 使用上传文件的原始 byte 比较简单但是功能有限。因此，Spring 还提供了 MultipartFile 接口，它为处理
     * multipart 数据提供了内容更为丰富的对象。如下是 MultipartFile 接口的概况。
     *
     * public interface MultipartFile extends InputStreamSource {
     *
     * 	String getName();
     *
     * 	String getOriginalFilename();
     *
     * 	String getContentType();
     *
     * 	boolean isEmpty();
     *
     * 	long getSize();
     *
     * 	byte[] getBytes() throws IOException;
     *
     * 	@Override
     * 	InputStream getInputStream() throws IOException;
     *
     * 	void transferTo(File dest) throws IOException, IllegalStateException;
     *
     * }
     *
     * 可以看到，MultipartFile 提供了获取上传文件 byte 的方式，但是它所提供的功能并不仅限于此，还能获得原始的文
     * 件名、大小以及内容类型。它还提供了一个 InputStream，用来将文件数据以流的方式进行读取。
     *
     * 除此之外，MultipartFile 还提供了一个便利的 transferTo() 方法，它能够帮助将上传的文件写入到文件系统中。
     * 作为样例，可以在 processRegistration() 方法中添加如下的几行代码，从而将上传的图片文件写入到文件系统中：
     *
     *         MultipartFile profilePicture =
     *                 spitterForm.getProfilePicture();
     *         profilePicture.transferTo(
     *                 new File("/data/spittr/" + profilePicture.getOriginalFilename()));
     *
     * 将文件保存到本地文件系统中是非常简单的，但是这需要对这些文件进行管理。需要确保有足够的空间，确保当出现硬件
     * 故障时，文件进行了备份，还需要在集群的多个服务器之间处理这些图片文件的同步。
     *
     *
     *
     * 将文件保存到 Amazon S3 中
     *
     * 另外一种方案就是让别人来负责处理这些事情。多加几行代码，就能将图片保存到云端。saveImage() 方法能够将上传的
     * 文件保存到 Amazon S3 中，在 processRegistration() 中可以调用该方法。
     *
     * PS：saveImage() 方法见 PDF，可参考：https://blog.csdn.net/zhanglf02/article/details/78500015
     *
     * saveImage() 方法所做的第一件事就是构建 Amazon Web Service(AWS) 凭证。为了完成这一点，你需要有一个 S3
     * Access Key 和 S3 Secret Access Key。当注册 S3 服务的时候，Amazon 会将其提供给你。它们会通过值注入的
     * 方式提供给 SpitterController。
     *
     * AWS 凭证准备好后，saveImage() 方法创建了一个 JetS3t 的 RestS3Service 实例，可以通过它来操作 S3 文件
     * 系统。它获取 spitterImages bucket 的引用并创建用来包含图片的 S3Object 对象，接下来将图片数据填充到
     * S3Object。
     *
     * 在调用 putObject() 方法将图片数据写到 S3 之前，saveImage() 方法设置了 S3Object 的权限，从而允许所有的
     * 用户查看它。这是很重要的 —— 如果没有它的话，这些图片对应用程序的用户就是不可见的。最后，如果出现任何问题的话，
     * 将会抛出 ImageUploadException 异常。
     *
     *
     *
     * 以 Part 的形式接受上传的文件
     *
     * 如果你需要将应用部署到 Servlet 3.0 的容器中，那么会有 MultipartFile 的一个替代方案。Spring MVC 也能接
     * 受 javax.servlet.http.Part 作为控制器方法的参数。如果使用 Part 来替换 MultipartFile 的话，那么
     * processRegistration() 的方法签名将会变成如下的形式：
     *
     *     @RequestMapping(value = "/register", method = POST)
     *     public String processRegistration(
     *             @RequestPart("profilePicture") Part profilePicture,
     *             @Valid Spitter spitter,
     *             Errors errors) {
     *          ...
     *     }
     *
     * 就主体来言（不开玩笑地说），Part 接口与 MultipartFile 并没有太大的差别。在如下代码中，可以看到 Part 接口
     * 有一些方法其实是与 MultipartFile 相对应的。
     *
     * public interface Part {
     *
     *     public InputStream getInputStream() throws IOException;
     *
     *     public String getContentType();
     *
     *     public String getName();
     *
     *     public String getSubmittedFileName();
     *
     *     public long getSize();
     *
     *     public void write(String fileName) throws IOException;
     *
     *     public void delete() throws IOException;
     *
     *     public String getHeader(String name);
     *
     *     public Collection<String> getHeaders(String name);
     *
     *     public Collection<String> getHeaderNames();
     *
     * }
     *
     * 在很多情况下，Part 方法的名称与 MultipartFile 方法的名称是完全相同的。有一些比较类似，但是稍有差异，比如
     * getSubmittedFileName() 对应于 getOriginalFilename()。类似地，write() 对应于transferTo()，借助该
     * 方法能够将上传的文件写入文件系统中：
     *
     * profilePicture.write("/data/spittr/" + profilePicture.getSubmittedFileName());
     *
     * 值得一提的是，如果在编写控制器方法的时候，通过 Part 参数的形式接受文件上传，那么就没有必要配置
     * MultipartResolver 了。只有使用 MultipartFile 的时候，才需要 MultipartResolver。
     */
    public static void main(String[] args) {

    }


}
