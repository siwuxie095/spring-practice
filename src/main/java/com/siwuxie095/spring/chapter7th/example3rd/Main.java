package com.siwuxie095.spring.chapter7th.example3rd;

/**
 * @author Jiajing Li
 * @date 2021-02-01 07:59:05
 */
public class Main {

    /**
     * 处理 multipart 形式的数据
     *
     * 在 Web 应用中，允许用户上传内容是很常见的需求。在 Facebook 和 Flickr 这样的网站中，用户通常会上传照片和视频，
     * 并与家人和朋友分享。还有一些服务允许用户上传照片，然后按照传统方式将其打印在纸上，或者用在 T 恤衫和咖啡杯上。
     *
     * Spittr 应用在两个地方需要文件上传。当新用户注册应用的时候，希望他们能够上传一张图片，从而与他们的个人信息相关联。
     * 当用户提交新的 Spittle 时，除了文本消息以外，他们可能还会上传一张照片。
     *
     * 一般表单提交所形成的请求结果是很简单的，就是以 "&" 符分割的多个 name-value 对。例如，当在 Spittr 应用中提交
     * 注册表单时，请求会如下所示：
     *
     * firstName=Charles&lastName=Xavier&email=professorx%40xmen.org
     * &username=professorx&password=letmein01
     *
     * 尽管这种编码形式很简单，并且对于典型的基于文本的表单提交也足够满足要求，但是对于传送二进制数据，如上传图片，就显
     * 得力不从心了。与之不同的是，multipart 格式的数据会将一个表单拆分为多个部分（part），每个部分对应一个输入域。在
     * 一般的表单输入域中，它所对应的部分中会放置文本型数据，但是如果上传文件的话，它所对应的部分可以是二进制，下面展现
     * 了 multipart 的请求体：
     *
     * ------WebKitFormBoundaryqgkaBn8IHJCuNmiw
     * Content-Disposition: form-data; name="firstName"
     *
     * Charles
     * ------WebKitFormBoundaryqgkaBn8IHJCuNmiw
     * Content-Disposition: form-data; name="lastName"
     *
     * Xavier
     * ------WebKitFormBoundaryqgkaBn8IHJCuNmiw
     * Content-Disposition: form-data; name="email"
     *
     * professorx@xmen.org
     * ------WebKitFormBoundaryqgkaBn8IHJCuNmiw
     * Content-Disposition: form-data; name="username"
     *
     * professorx
     * ------WebKitFormBoundaryqgkaBn8IHJCuNmiw
     * Content-Disposition: form-data; name="password"
     *
     * letmein01
     * ------WebKitFormBoundaryqgkaBn8IHJCuNmiw
     * Content-Disposition: form-data; name="profilePicture"; filename="me.jpg"
     * Content-Type: image/jpeg
     *
     *  [[ Binary image data goes here ]]
     *
     * ------WebKitFormBoundaryqgkaBn8IHJCuNmiw--
     *
     * 在这个 multipart 的请求中，可以看到 profilePicture 部分与其他部分明显不同。除了其他内容以外，它还有自己的
     * Content-Type 头，表明它是一个 JPEG 图片。尽管不一定那么明显，但 profilePicture 部分的请求体是二进制数据，
     * 而不是简单的文本。
     *
     * 尽管 multipart 请求看起来很复杂，但在 Spring MVC 中处理它们却很容易。在编写控制器方法处理文件上传之前，必须
     * 要配置一个 multipart 解析器，通过它来告诉 DispatcherServlet 该如何读取 multipart 请求。
     */
    public static void main(String[] args) {

    }

}
