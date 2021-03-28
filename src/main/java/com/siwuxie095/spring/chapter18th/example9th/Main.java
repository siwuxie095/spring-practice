package com.siwuxie095.spring.chapter18th.example9th;

/**
 * @author Jiajing Li
 * @date 2021-03-28 17:28:11
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 处理消息异常
     *
     * 有时候，事情并不会按照预期的那样发展。在处理消息的时候，有可能会出错并抛出异常。因为 STOMP 消息异步的特点，
     * 发送者可能永远也不会知道出现了错误。除了 Spring 的日志记录以外，异常有可能会丢失，没有资源或机会恢复。
     *
     * 在 Spring MVC 中，如果在请求处理中，出现异常的话，@ExceptionHandler 方法将有机会处理异常。与之类似，也
     * 可以在某个控制器方法上添加 @MessageExceptionHandler 注解，让它来处理 @MessageMapping 方法所抛出的异常。
     *
     * 例如，考虑如下的方法，它会处理消息方法所抛出的异常：
     *
     * @MessageExceptionHandler
     * public void handleExceptions(Throwable t) {
     *     logger.error("Error handling message: " + t.getMessage());
     * }
     *
     * 按照最简单的形式，@MessageExceptionHandler 标注的方法能够处理消息方法中所抛出的异常。但是，也可以以参
     * 数的形式声明它所能处理的异常：
     *
     * @MessageExceptionHandler(SpittleException.class)
     * public void handleExceptions(Throwable t) {
     *     logger.error("Error handling message: " + t.getMessage());
     * }
     *
     * 或者，以数组参数的形式指定多个异常类型：
     *
     * @MessageExceptionHandler({SpittleException.class,DatabaseException.class})
     * public void handleExceptions(Throwable t) {
     *     logger.error("Error handling message: " + t.getMessage());
     * }
     *
     * 尽管它只是以日志的方式记录了所发生的错误，但是这个方法可以做更多的事情。例如，它可以回应一个错误：
     *
     * @MessageExceptionHandler(SpittleException.class)
     * @SendToUser("/queue/errors")
     * public SpittleException handleExceptions(SpittleException e) {
     *     logger.error("Error handling message: " + e.getMessage());
     *     return e;
     * }
     *
     * 在这里，如果抛出 SpittleException 的话，将会记录这个异常，然后将其返回。UserDestinationMessageHandler
     * 会重新路由这个消息到特定用户所对应的唯一路径。
     */
    public static void main(String[] args) {

    }

}
