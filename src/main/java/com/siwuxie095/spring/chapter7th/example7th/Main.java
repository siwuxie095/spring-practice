package com.siwuxie095.spring.chapter7th.example7th;

/**
 * @author Jiajing Li
 * @date 2021-02-03 21:11:39
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 为控制器添加通知
     *
     * 如果控制器类的特定切面能够运用到整个应用程序的所有控制器中，那么这将会便利很多。举例来说，如果要在多个控制器中处理异常，
     * 那 @ExceptionHandler 注解所标注的方法是很有用的。不过，如果多个控制器类中都会抛出某个特定的异常，那么你可能会发现要
     * 在所有的控制器方法中重复相同的 @ExceptionHandler 方法。或者，为了避免重复，会创建一个基础的控制器类，所有控制器类要
     * 扩展这个类，从而继承通用的 @ExceptionHandler 方法。
     *
     * Spring 3.2 为这类问题引入了一个新的解决方案：控制器通知。控制器通知（controller advice）是任意带有 @ControllerAdvice
     * 注解的类，这个类会包含一个或多个如下类型的方法：
     * （1）@ExceptionHandler 注解标注的方法；
     * （2）@InitBinder 注解标注的方法；
     * （3）@ModelAttribute 注解标注的方法。
     *
     * 在带有 @ControllerAdvice 注解的类中，以上所述的这些方法会运用到整个应用程序所有控制器中带有 @RequestMapping 注解
     * 的方法上。
     *
     * @ControllerAdvice 注解本身已经使用了 @Component，因此 @ControllerAdvice 注解所标注的类将会自动被组件扫描获取到，
     * 就像带有 @Component 注解的类一样。
     *
     * @ControllerAdvice 最为实用的一个场景就是将所有的 @ExceptionHandler 方法收集到一个类中，这样所有控制器的异常就能在
     * 一个地方进行一致的处理。例如，想将 DuplicateSpittleException 的处理方法用到整个应用程序的所有控制器上。如下代码展现
     * 的 AppWideExceptionHandler 就能完成这一任务，这是一个带有 @ControllerAdvice 注解的类。
     *
     * @ControllerAdvice
     * public class AppWideExceptionHandler {
     *
     *     @ExceptionHandler(DuplicateSpittleException.class)
     *     public String duplicateSpittleHandler() {
     *         return "error/duplicate";
     *     }
     *
     * }
     *
     * 现在，如果任意的控制器方法抛出了 DuplicateSpittleException，不管这个方法位于哪个控制器中，都会调用这个
     * duplicateSpittleHandler() 方法来处理异常。
     *
     * 可以像编写 @RequestMapping 注解的方法那样来编写@ExceptionHandler注解的方法。这里返回 "error/duplicate"
     * 作为逻辑视图名，因此将会为用户展现一个友好的出错页面。
     */
    public static void main(String[] args) {

    }

}
