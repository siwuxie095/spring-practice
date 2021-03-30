package com.siwuxie095.spring.chapter19th.example3rd;

/**
 * @author Jiajing Li
 * @date 2021-03-30 21:15:48
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 构建丰富内容的 Email 消息
     *
     * 对于简单的事情来讲，纯文本的 Email 消息是比较合适的，比如邀请朋友去观看比赛。但是，如果你要发送照片或文档的话，
     * 这种方式就不那么理想了。如果作为市场推广 Email 的话，它也无法吸引接收者的注意。
     *
     * 幸好，Spring 的 Email 功能并不局限于纯文本的 Email。可以添加附件，甚至可以使用 HTML 来美化消息体的内容。下
     * 面首先从基本的添加附件开始，然后更进一步，借助 HTML 使 Email 消息更加美观。
     *
     *
     *
     * 1、添加附件
     *
     * 如果发送带有附件的 Email，关键技巧是创建 multipart 类型的消息 —— Email 由多个部分组成，其中一部分是 Email
     * 体，其他部分是附件。
     *
     * 对于发送附件这样的需求来说，SimpleMailMessage 过于简单了。为了发送 multipart 类型的 Email，你需要创建一个
     * MIME（Multipurpose Internet Mail Extensions）的消息，可以从邮件发送器的 createMimeMessage() 方法开始：
     *
     * MimeMessage message = mailSender.createMimeMessage();
     *
     * 就这样，已经有了要使用的 MIME 消息。看起来，所需要做的就是指定收件人和发件人地址、主题、一些内容以及一个附件。
     * 尽管确实是这样，但并不是你想的那么简单。javax.mail.internet.MimeMessage 本身的 API 有些笨重。好消息是，
     * Spring 提供的 MimeMessageHelper 可以提供帮助。
     *
     * 为了使用 MimeMessageHelper，需要实例化它并将 MimeMessage 传给其构造器：
     *
     * MimeMessageHelper helper = new MimeMessageHelper(message, true);
     *
     * 构造方法的第二个参数，在这里是个布尔值 true，表明这个消息是 multipart 类型的。
     *
     * 得到了 MimeMessageHelper 实例后，就可以组装 Email 消息了。这里最主要区别在于使用 helper 的方法来指定 Email
     * 细节，而不再是设置消息对象：
     *
     * String spitterName = spittle.getSpitter().getFullName();
     * helper.setFrom("noreply@spitter.com");
     * helper.setTo(to);
     * helper.setSubject("New spittle from " + spitterName);
     * helper.setText(spitterName + " says: " + spittle.getText());
     *
     * 在发送 Email 之前，你唯一还要做的就是添加附件：在本例中，也就是一张图标图片。为了做到这一点，你需要加载图片并
     * 将其作为资源，然后将这个资源传递给 helper 的 addAttachment 方法：
     *
     * FileSystemResource couponImage = new FileSystemResource("/collateral/coupon.png");
     * helper.addAttachment("Coupon.png", couponImage);
     *
     * 在这里，使用 Spring 的 FileSystemResource 来加载位于应用类路径下的 coupon.png。然后，调用 addAttachment()。
     * 第一个参数是要添加到 Email 中附件的名称，第二个参数是图片资源。
     *
     * multipart 类型的 Email 已经构建完成了，现在可以发送它了。完整的 sendSpittleEmailWithAttachment() 方法
     * 如下所示。
     *
     *     public void sendSpittleEmailWithAttachment(String to, Spittle spittle) throws MessagingException {
     *         MimeMessage message = mailSender.createMimeMessage();
     *         MimeMessageHelper helper = new MimeMessageHelper(message, true);
     *         String spitterName = spittle.getSpitter().getFullName();
     *         helper.setFrom("noreply@spitter.com");
     *         helper.setTo(to);
     *         helper.setSubject("New spittle from " + spitterName);
     *         helper.setText(spitterName + " says: " + spittle.getText());
     *         FileSystemResource couponImage = new FileSystemResource("/collateral/coupon.png");
     *         helper.addAttachment("Coupon.png", couponImage);
     *         mailSender.send(message);
     *     }
     *
     * multipart 类型的 Email 能够实现很多的功能，添加附件只是其中之一。除此之外，通过将 Email 体指明为 HTML，可
     * 以生成比简单文本更漂亮的 Email。接下来，看一下如何使用 MimeMessageHelper 来发送更吸引人的 Email。
     *
     *
     *
     * 2、发送富文本内容的 Email
     *
     * 发送富文本的 Email 与发送简单文本的 Email 并没有太大区别。关键是将消息的文本设置为 HTML。要做到这一点只需将
     * HTML 字符串传递给 helper 的 setText() 方法，并将第二个参数设置为 true：
     *
     * helper.setText("<html><body><img src='cid:spitterLogo'><h4>" +
     *      spitter.getSpitter().getFullName() + " says ...</h4><i>" +
     *      spittle.getText() + "</i></body></html>", true);
     *
     * 第二个参数表明传递进来的第一个参数是 HTML，所以需要对消息的内容类型进行相应的设置。
     *
     * 要注意的是，传递进来的 HTML 包含了一个 <img> 标签，用来在 Email 中展现 Spittr 应用程序的 logo。src 属性可
     * 以设置为标准的 "http:" URL，以便于从 Web 中获取 Spittr 的 logo。但在这里，将 logo 图片嵌入在了 Email 之
     * 中。值 "cid:spitterLogo" 表明在消息中会有一部分是图片并以 spitterLogo 来进行标识。
     *
     * 为消息添加嵌入式的图片与添加附件很类似。不过这次不再使用 helper 的 addAttachment() 方法，而是要调用
     * addInline() 方法：
     *
     * ClassPathResource image = new ClassPathResource("spitter_logo_50.png");
     * helper.addInline("spitterLogo", image);
     *
     * addInline 的第一个参数表明内联图片的标识符 —— 与 <img> 标签的 src 属性所指定的相同。第二个参数是图片的资源
     * 引用，这里使用 ClassPathResource 从应用程序的类路径中获取图片。
     *
     * 除了 setText() 方法稍微不同以及使用了 addInline() 方法以外，发送含有富文本内容的 Email 与发送带有附件的普
     * 通文本消息很类似。为了进行对比，以下是新的 sendRichSpitterEmail() 方法。
     *
     *     public void sendRichSpitterEmail(String to, Spittle spittle) throws MessagingException {
     *         MimeMessage message = mailSender.createMimeMessage();
     *         MimeMessageHelper helper = new MimeMessageHelper(message, true);
     *         String spitterName = spittle.getSpitter().getFullName();
     *         helper.setFrom("noreply@spitter.com");
     *         helper.setTo(to);
     *         helper.setSubject("New spittle from " + spitterName);
     *         helper.setText("<html><body><img src='cid:spitterLogo'><h4>" +
     *                 spitter.getSpitter().getFullName() + " says ...</h4><i>" +
     *                 spittle.getText() + "</i></body></html>", true);
     *         FileSystemResource couponImage = new FileSystemResource("/collateral/coupon.png");
     *         ClassPathResource image = new ClassPathResource("spitter_logo_50.png");
     *         helper.addInline("spitterLogo", image);
     *         mailSender.send(message);
     *     }
     *
     * 现在你发送的 Email 带有富文本内容和嵌入式图片了！
     */
    public static void main(String[] args) {

    }

}
