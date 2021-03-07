package com.siwuxie095.spring.chapter14th.example3rd;

/**
 * @author Jiajing Li
 * @date 2021-03-07 16:16:29
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 使用 @Secured 注解限制方法调用
     *
     * 在 Spring 中，如果要启用基于注解的方法安全性，关键之处在于要在配置类上使用 @EnableGlobalMethodSecurity，
     * 如下所示：
     *
     * @Configuration
     * @EnableGlobalMethodSecurity(securedEnabled = true)
     * public class SecuredConfig extends GlobalMethodSecurityConfiguration {
     *
     * }
     *
     * 除了使用 @EnableGlobalMethodSecurity 注解，同时配置类也扩展了 GlobalMethodSecurityConfiguration。
     *
     * 在 之前的 Web 安全的配置类扩展了 WebSecurityConfigurerAdapter，与之类似，这个类能够为方法级别的安全性
     * 提供更精细的配置。
     *
     * 例如，如果在 Web 层的安全配置中设置认证，那么可以通过重载 GlobalMethodSecurityConfiguration 的
     * configure() 方法实现该功能：
     *
     *     @Override
     *     protected void configure(AuthenticationManagerBuilder auth) throws Exception {
     *         auth.inMemoryAuthentication()
     *                 .withUser("user").password("password").roles("USER");
     *     }
     *
     * 后续将会看到如何重载 GlobalMethodSecurityConfiguration 的 createExpressionHandler() 方法，提供一
     * 些自定义的安全表达式处理行为。
     *
     * 回到 @EnableGlobalMethodSecurity 注解，注意这里的 securedEnabled 属性设置成了 true。
     *
     * 如果 securedEnabled 属性的值为 true 的话，将会创建一个切点，这样的话 Spring Security 切面就会包装带有
     * @Secured 注解的方法。例如，考虑如下这个带有 @Secured 注解的 addSpittle() 方法：
     *
     *     @Secured("ROLE_SPITTER")
     *     public void addSpittle(Spittle spittle) {
     *         System.out.println("Method was called successfully");
     *     }
     *
     * @Secured 注解会使用一个 String 数组作为参数。每个 String 值是一个权限，调用这个方法至少需要具备其中的一
     * 个权限。通过传递进来 ROLE_SPITTER，告诉 Spring Security 只允许具有 ROLE_SPITTER 权限的认证用户才能调
     * 用 addSpittle ()方法。
     *
     * 如果传递给 @Secured 多个权限值，认证用户必须至少具备其中的一个才能进行方法的调用。例如，下面使用 @Secured
     * 的方式表明用户必须具备 ROLE_SPITTER 或 ROLE_ADMIN 权限才能触发这个方法：
     *
     *     @Secured({"ROLE_SPITTER", "ROLE_ADMIN"})
     *     public void addSpittle(Spittle spittle) {
     *         System.out.println("Method was called successfully");
     *     }
     *
     * 如果方法被没有认证的用户或没有所需权限的用户调用，保护这个方法的切面将抛出一个 Spring Security 异常。它们
     * 是非检查型异常，但这个异常最终必须要被捕获和处理。如果被保护的方法是在 Web 请求中调用的，这个异常会被 Spring
     * Security 的过滤器自动处理。否则的话，你需要编写代码来处理这个异常。
     *
     * PS：可能是 AuthenticationException 或 AccessDeniedException 的子类。
     *
     * @Secured 注解的不足之处在于它是 Spring 特定的注解。如果更倾向于使用 Java 标准定义的注解，那么你应该考虑
     * 使用 @RolesAllowed 注解。
     */
    public static void main(String[] args) {

    }

}
