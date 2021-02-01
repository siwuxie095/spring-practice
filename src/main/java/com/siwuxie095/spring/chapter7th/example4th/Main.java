package com.siwuxie095.spring.chapter7th.example4th;

/**
 * @author Jiajing Li
 * @date 2021-02-01 21:50:50
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 配置 multipart 解析器
     *
     * DispatcherServlet 并没有实现任何解析 multipart 请求数据的功能。它将该任务委托给了 Spring 中 MultipartResolver
     * 策略接口的实现，通过这个实现类来解析 multipart 请求中的内容。
     *
     * 从 Spring 3.1 开始，Spring 内置了两个 MultipartResolver 的实现以供选择：
     * （1）CommonsMultipartResolver：使用 Jakarta Commons FileUpload 解析 multipart 请求；
     * （2）StandardServletMultipartResolver：依赖于 Servlet 3.0 对 multipart 请求的支持（始于 Spring 3.1）。
     *
     * 一般来讲，在这两者之间，StandardServletMultipartResolver 可能会是优选的方案。它使用 Servlet 所提供的功能支持，并
     * 不需要依赖任何其他的项目。如果需要将应用部署到 Servlet 3.0 之前的容器中，或者还没有使用 Spring 3.1 或更高版本，那么
     * 可能就需要 CommonsMultipartResolver 了。
     *
     *
     *
     * 使用 Servlet 3.0 解析 multipart 请求
     *
     * 兼容 Servlet 3.0 的 StandardServletMultipartResolver 没有构造器参数，也没有要设置的属性。这样，在 Spring 应用
     * 上下文中，将其声明为 bean 就会非常简单，如下所示：
     *
     *     @Bean
     *     public MultipartResolver multipartResolver() throws IOException {
     *         return new StandardServletMultipartResolver();
     *     }
     *
     * 既然这个 @Bean 方法如此简单，你可能就会怀疑到底该如何限制 StandardServletMultipartResolver 的工作方式呢。如果想要
     * 限制用户上传文件的大小，该怎么实现？如果想要指定文件在上传时，临时写入目录在什么位置的话，该如何实现？因为没有属性和构造
     * 器参数，StandardServletMultipartResolver 的功能看起来似乎有些受限。
     *
     * 其实并不是这样，这里是有办法配置 StandardServletMultipartResolver 的限制条件的。只不过不是在 Spring 中配置
     * StandardServletMultipartResolver，而是要在 Servlet 中指定 multipart 的配置。至少，必须要指定在文件上传的过程中，
     * 所写入的临时文件路径。如果不设定这个最基本配置的话，StandardServletMultipartResolver 就无法正常工作。具体来讲，必
     * 须要在 web.xml 或 Servlet 初始化类中，将 multipart 的具体细节作为 DispatcherServlet 配置的一部分。
     *
     * 如果采用 Servlet 初始化类的方式来配置 DispatcherServlet 的话，这个初始化类应该已经实现了 WebApplicationInitializer，
     * 那可以在 Servlet registration 上调用 setMultipartConfig() 方法，传入一个 MultipartConfigElement 实例。如下是
     * 最基本的 DispatcherServlet multipart 配置，它将临时路径设置为 "/tmp/spittr/uploads"：
     *
     * public class DispatcherServletInitializer implements WebApplicationInitializer {
     *
     *     @Override
     *     public void onStartup(ServletContext servletContext) throws ServletException {
     *         DispatcherServlet ds = new DispatcherServlet();
     *         ServletRegistration.Dynamic registration =
     *                 servletContext.addServlet("appServlet", ds);
     *         registration.addMapping("/");
     *         registration.setMultipartConfig(
     *                 new MultipartConfigElement("/tmp/spittr/uploads"));
     *     }
     *
     * }
     *
     * 如果配置 DispatcherServlet 的 Servlet 初始化类继承了 AbstractAnnotationConfigDispatcherServletInitializer
     * 或AbstractDispatcherServletInitializer 的话，那么这里不会直接创建 DispatcherServlet 实例并将其注册到 Servlet
     * 上下文中。这样的话，将不会有对 Dynamic Servlet registration 的引用以供使用了。但是，可以通过另一种方式来配置
     * multipart 的具体细节，即 重载 customizeRegistration() 方法（它会得到一个 Dynamic 作为参数）：
     *
     *     @Override
     *     protected void customizeRegistration(Dynamic registration) {
     *         registration.setMultipartConfig(
     *                 new MultipartConfigElement("/tmp/spittr/uploads"));
     *     }
     *
     * 到目前为止，所使用是只有一个参数的 MultipartConfigElement 构造器，这个参数指定的是文件系统中的一个绝对目录，上传文件
     * 将会临时写入该目录中。但是，还可以通过其他的构造器来限制上传文件的大小。
     *
     * 除了临时路径的位置，其他的构造器所能接受的参数如下：
     * （1）上传文件的最大容量（以字节为单位）。默认是没有限制的。
     * （2）整个 multipart 请求的最大容量（以字节为单位），不会关心有多少个 part 以及每个 part 的大小。默认是没有限制的。
     * （3）在上传的过程中，如果文件大小达到了一个指定最大容量（以字节为单位），将会写入到临时文件路径中。默认值为 0，也就是所
     * 有上传的文件都会写入到磁盘上。
     *
     * 例如，假设想限制文件的大小不超过 2 MB，整个请求不超过 4 MB，而且所有的文件都要写到磁盘中。下面的代码t设置了这些临界值：
     *
     *     @Override
     *     protected void customizeRegistration(Dynamic registration) {
     *         registration.setMultipartConfig(
     *                 new MultipartConfigElement("/tmp/spittr/uploads",
     *                         2097152, 4194304, 0));
     *     }
     *
     * 如果我们使用更为传统的 web.xml 来配置 MultipartConfigElement 的话，那么可以使用 <servlet> 中的
     * <multipart-config> 元素，如下所示：
     *
     * <servlet>
     *     <servlet-name>appServlet</servlet-name>
     *     <servlet-class>
     *         org.springframework.web.servlet.DispatcherServlet
     *     </servlet-class>
     *     <load-on-startup>1</load-on-startup>
     *     <multipart-config>
     *         <location>/tmp/spittr/uploads</location>
     *         <max-file-size>2097152</max-file-size>
     *         <max-request-size>4194304</max-request-size>
     *     </multipart-config>
     * </servlet>
     *
     * <multipart-config> 的默认值与 MultipartConfigElement 相同。与 MultipartConfigElement 一样，
     * 必须要配置的是 <location>。
     *
     *
     *
     * 配置 Jakarta Commons FileUpload multipart 解析器
     *
     * 通常来讲，StandardServletMultipartResolver 会是最佳的选择，但是如果需要将应用部署到非 Servlet 3.0 的容器中，那么
     * 就得需要替代的方案。如果喜欢的话，可以编写自己的 MultipartResolver 实现。不过，除非想要在处理 multipart 请求的时候
     * 执行特定的逻辑，否则的话，没有必要这样做。Spring 内置了 CommonsMultipartResolver，可以作为
     * StandardServletMultipartResolver 的替代方案。
     *
     * 将 CommonsMultipartResolver 声明为 Spring bean 的最简单方式如下：
     *
     *     @Bean
     *     public MultipartResolver multipartResolver() throws IOException {
     *         return new CommonsMultipartResolver();
     *     }
     *
     * 与 StandardServletMultipartResolver 有所不同，CommonsMultipartResolver 不会强制要求设置临时文件路径。默认情况
     * 下，这个路径就是 Servlet 容器的临时目录。不过，通过设置 uploadTempDir 属性，可以将其指定为一个不同的位置：
     *
     *     @Bean
     *     public MultipartResolver multipartResolver() throws IOException {
     *         CommonsMultipartResolver multipartResolver =
     *                 new CommonsMultipartResolver();
     *         multipartResolver.setUploadTempDir(
     *                 new FileSystemResource("/tmp/spittr/uploads"));
     *         return multipartResolver;
     *     }
     *
     * 实际上，可以按照相同的方式指定其他的 multipart 上传细节，也就是设置 CommonsMultipartResolver 的属性。例如，如下的
     * 配置就等价于在前面通过 MultipartConfigElement 所配置的 StandardServletMultipartResolver：
     *
     *     @Bean
     *     public MultipartResolver multipartResolver() throws IOException {
     *         CommonsMultipartResolver multipartResolver =
     *                 new CommonsMultipartResolver();
     *         multipartResolver.setUploadTempDir(
     *                 new FileSystemResource("/tmp/spittr/uploads"));
     *         multipartResolver.setMaxUploadSize(2097152);
     *         multipartResolver.setMaxInMemorySize(0);
     *         return multipartResolver;
     *     }
     *
     * 在这里，将最大的文件容量设置为 2 MB，最大的内存大小设置为 0 字节。这两个属性直接对应于 MultipartConfigElement 的第
     * 二个和第四个构造器参数，表明不能上传超过 2 MB 的文件，并且不管文件的大小如何，所有的文件都会写到磁盘中。但是与
     * MultipartConfigElement 有所不同，无法设定 multipart 请求整体的最大容量。
     */
    public static void main(String[] args) {

    }

}
