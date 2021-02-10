package com.siwuxie095.spring.chapter8th.example5th;

/**
 * @author Jiajing Li
 * @date 2021-02-10 20:41:53
 */
public class Main {

    /**
     * 保护 Web 流程
     *
     * 将会看到如何使用 Spring Security 来保护 Spring 应用程序。但现在讨论的是 Spring Web Flow，下面
     * 快速地看一下 Spring Web Flow 是如何结合 Spring Security 支持流程级别的安全性的。
     *
     * Spring Web Flow 中的状态、转移甚至整个流程都可以借助 <secured> 元素实现安全性，该元素会作为这些
     * 元素的子元素。例如，为了保护对一个视图状态的访问，你可以这样使用 <secured>：
     *
     * <view-state id="restricted">
     *     <secured attributes="ROLE_ADMIN" match="all" />
     * </view-state>
     *
     * 按照这里的配置，只有授予 ROLE_ADMIN 访问权限（借助 attributes 属性）的用户才能访问这个视图状态。
     * attributes 属性使用逗号分隔的权限列表来表明用户要访问指定状态、转移或流程所需要的权限。match 属性
     * 可以设置为 any 或 all。如果设置为 any，那么用户必须至少具有一个 attributes 属性所列的权限。如果
     * 设置为 all，那么用户必须具有所有的权限。
     */
    public static void main(String[] args) {

    }

}
