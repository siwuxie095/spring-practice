package com.siwuxie095.spring.chapter6th.example6th;

/**
 * @author Jiajing Li
 * @date 2021-01-27 08:22:31
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 使用 Apache Tiles 视图定义布局
     *
     * 到现在为止，很少关心应用中 Web 页面的布局问题。每个 JSP 完全负责定义自身的布局，在这方面其实这些 JSP 也没有做太多
     * 工作。
     *
     * 假设想为应用中的所有页面定义一个通用的头部和底部。最原始的方式就是查找每个 JSP 模板，并为其添加头部和底部的 HTML。
     * 但是这种方法的扩展性并不好，也难以维护。为每个页面添加这些元素会有一些初始成本，而后续的每次变更都会耗费类似的成本。
     *
     * 更好的方式是使用布局引擎，如 Apache Tiles，定义适用于所有页面的通用页面布局。Spring MVC 以视图解析器的形式为
     * Apache Tiles 提供了支持，这个视图解析器能够将逻辑视图名解析为 Tile 定义。
     *
     *
     *
     * 配置 Tiles 视图解析器
     *
     * 为了在 Spring 中使用 Tiles，需要配置几个 bean。需要一个 TilesConfigurer bean，它会负责定位和加载 Tile 定义
     * 并协调生成 Tiles。除此之外，还需要 TilesViewResolver bean 将逻辑视图名称解析为 Tile 定义。
     *
     * 这两个组件又有两种形式：针对 Apache Tiles 2 和 Apache Tiles 3 分别都有这么两个组件。这两组 Tiles 组件之间最为
     * 明显的区别在于包名。针对 Apache Tiles 2 的 TilesConfigurer/TilesViewResolver 位于 org.springframework.
     * web.servlet.view.tiles2 包中，而针对 Tiles 3 的组件位于 org.springframework.web.servlet.view.tiles3 包
     * 中。对于本例来讲，假设使用的是 Tiles 3。
     *
     * 首先，配置 TilesConfigurer 来解析 Tile 定义。
     *
     *     @Bean
     *     public TilesConfigurer tilesConfigurer() {
     *         TilesConfigurer tiles = new TilesConfigurer();
     *         tiles.setDefinitions(new String[]{
     *                 "/WEB-INF/layout/tiles.xml"
     *         });
     *         tiles.setCheckRefresh(true);
     *         return tiles;
     *     }
     *
     * 当配置 TilesConfigurer 的时候，所要设置的最重要的属性就是 definitions。这个属性接受一个 String 类型的数组，
     * 其中每个条目都指定一个 Tile 定义的 XML 文件。对于 Spittr 应用来讲，让它在 "/WEB-INF/layout/" 目录下查找
     * tiles.xml。
     *
     * 其实还可以指定多个 Tile 定义文件，甚至能够在路径位置上使用通配符，当然在上例中没有使用该功能。
     *
     * 例如，要求 TilesConfigurer 加载 "/WEB-INF/" 目录下的所有名字为 tiles.xml 的文件，那么可以按照这种方式设置
     * definitions 属性：将 /WEB-INF/layout/tiles.xml 中间的 layout 替换成 **。
     *
     * 这里使用了 Ant 风格的通配符（**），所以 TilesConfigurer 会遍历 "WEB-INF/" 的所有子目录来查找 Tile 定义。
     *
     * 接下来配置 TilesViewResolver，可以看到，这是一个很基本的 bean 定义，没有什么要设置的属性：
     *
     *     @Bean
     *     public ViewResolver viewResolver() {
     *         return new TilesViewResolver();
     *     }
     *
     * 如果你更喜欢 XML 配置的话，那么可以按照如下的形式配置 TilesConfigurer 和 TilesViewResolver：
     *
     *     <bean id="tilesConfigurer" class="org.springframework.web.servlet.view.tiles3.TilesConfigurer">
     *         <property name="definitions">
     *             <list>
     *                 <value>/WEB-INF/layout/tiles.xml</value>
     *             </list>
     *         </property>
     *     </bean>
     *
     *     <bean id="viewResolver" class="org.springframework.web.servlet.view.tiles3.TilesViewResolver" />
     *
     * TilesConfigurer 会加载 Tile 定义并与 Apache Tiles 协作，而 TilesViewResolver 会将逻辑视图名称解析为引用
     * Tile 定义的视图。它是通过查找与逻辑视图名称相匹配的 Tile 定义实现该功能的。
     *
     * 这里需要创建几个 Tile 定义以了解它是如何运转的。
     *
     *
     *
     * 定义 Tiles
     *
     * Apache Tiles 提供了一个文档类型定义（document type definition，DTD），用来在 XML 文件中指定 Tile 的定义。
     * 每个定义中需要包含一个 <definition> 元素，这个元素会有一个或多个 <put-attribute> 元素。例如，tiles.xml
     * 为 Spittr 应用定义了几个 Tile。
     *
     * 每个 <definition> 元素都定义了一个 Tile，它最终引用的是一个 JSP 模板。在名为 base 的 Tile 中，模板引用的是
     * "/WEB-INF/layout/page.jsp"。某个 Tile 可能还会引用其他的 JSP 模板，使这些 JSP 模板嵌入到主模板中。对于 base
     * Tile 来讲，它引用的是一个头部 JSP 模板和一个底部 JSP 模板。
     *
     * 在 page.jsp 中，需要重点关注的事情就是如何使用 Tile 标签库中的 <t:insert Attribute> JSP 标签来插入其他的
     * 模板。在这里，用它来插入名为 header、body 和 footer 的模板。
     *
     * 在 base Tile 定义中，header 和 footer 属性分别被设置为引用 "/WEB-INF/layout/ header.jsp" 和 "/WEB-INF
     * /layout/footer.jsp"。但是 body 属性呢？它是在哪里设置的呢？
     *
     * 在这里，base Tile 不会期望单独使用。它会作为基础定义（这是其名字的来历），供其他的 Tile 定义扩展。在 tiles.xml
     * 中，可以看到其他的 Tile 定义都是扩展自 base Tile。它意味着它们会继承其 header 和 footer 属性的设置（当然，Tile
     * 定义中也可以覆盖掉这些属性），但是每一个都设置了 body 属性，用来指定每个 Tile 特有的 JSP 模板。
     *
     * 现在，关注一下 home Tile，它扩展了 base。因为它扩展了 base，因此它会继承 base 中的模板和所有的属性。尽管 home
     * Tile 定义相对来说很简单，但是它实际上包含了如下的定义：
     *
     *     <definition name="home" extends="/WEB-INF/layout/page.jsp">
     *         <put-attribute name="header" value="/WEB-INF/layout/header.jsp"/>
     *         <put-attribute name="footer" value="/WEB-INF/layout/footer.jsp"/>
     *         <put-attribute name="body" value="/WEB-INF/views/home.jsp"/>
     *     </definition>
     *
     * 属性所引用的每个模板是很简单的，如下是 header.jsp 模板：
     *
     * <%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
     * <a href="<s:url value="/" />"><img
     *     src="<s:url value="/resources" />/images/spitter_logo_50.png"
     *     border="0"/></a>
     *
     * footer.jsp模板更为简单：
     *
     * Copyright &copy; Craig Walls
     *
     * 每个扩展自 base 的 Tile 都定义了自己的主体区模板，所以每个都会与其他的有所区别。但是为了完整地了解 home Tile，
     * 如下展现了 home.jsp：
     *
     * <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
     * <%@ page session="false" %>
     * <h1>Welcome to Spitter</h1>
     *
     * <a href="<c:url value="/spittles" />">Spittles</a> |
     * <a href="<c:url value="/spitter/register" />">Register</a>
     *
     * 这里的关键点在于通用的元素放到了 page.jsp、header.jsp 以及 footer.jsp 中，其他的 Tile 模板中不再包含这部分
     * 内容。这使得它们能够跨页面重用，这些元素的维护也得以简化。
     */
    public static void main(String[] args) {

    }

}
