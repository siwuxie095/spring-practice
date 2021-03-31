package com.siwuxie095.spring.chapter20th.example2nd;

/**
 * @author Jiajing Li
 * @date 2021-03-31 08:14:25
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 将 Spring bean 导出为 MBean
     *
     * 这里有几种方式可以让开发者通过使用 JMX 来管理 Spittr 应用中的 bean。为了让事情尽量保持简单，这里对
     * SpittleController 只做适度的改变，增加一个新的 spittlesPerPage 属性：
     *
     *     public static final int DEFAULT_SPITTLES_PER_PAGE = 25;
     *
     *     private int spittlesPerPage = DEFAULT_SPITTLES_PER_PAGE;
     *
     *     public int getSpittlesPerPage() {
     *         return spittlesPerPage;
     *     }
     *
     *     public void setSpittlesPerPage(int spittlesPerPage) {
     *         this.spittlesPerPage = spittlesPerPage;
     *     }
     *
     * 之前，当调用 SpittleRepository 的 findSpittles() 方法时，SpittleController 传入 20 作为第二
     * 个参数，这会查询最近的 20 条 Spittle。现在，不再是在构建应用时通过硬编码进行决策，而是通过使用 JMX
     * 在运行时进行决策。新增的 spittlesPerPage 属性只是第一步而已。
     *
     * 但是 spittlesPerPage 属性本身并不能实现通过外部配置来改变页面上所显示 Spittle 的数量。它只是 bean
     * 的一个属性，跟 bean 的其他属性一样。下一步需要做的是把 SpittleController bean 暴露为 MBean，而
     * spittlePerPage 属性将成为 MBean 的托管属性（managed attribute）。这时，就可以在运行时改变该属
     * 性的值。
     *
     * Spring 的 MBeanExporter 是将 Spring Bean 转变为 MBean 的关键。MBeanExporter 可以把一个或多个
     * Spring bean 导出为 MBean 服务器（MBean server）内的模型 MBean。MBean 服务器（有时候也被称为
     * MBean 代理）是 MBean 生存的容器。对 MBean 的访问，也是通过 MBean 服务器来实现的。
     *
     * 将 Spring bean 导出为 JMX MBean 之后，可以使用基于 JMX 的管理工具（例如 JConsole 或者 VisualVM）
     * 查看正在运行的应用程序，显示 bean 的属性并调用 bean 的方法。
     *
     * PS：Spring 的 MBeanExporter 可以将 Spring bean 的属性和方法导出为 MBean 服务器中的 JMX 属性和
     * 操作。通过 JMX 服务器，JMX 管理工具（例如 JConsole）可以查看到正在运行的应用程序的内部情况。
     *
     * 下面的 @Bean 方法在 Spring 中声明了一个 MBeanExporter，它会将 spittleController bean 导出为一
     * 个模型 MBean：
     *
     *    @Bean
     *     public MBeanExporter mbeanExporter(SpittleController spittleController) {
     *         MBeanExporter exporter = new MBeanExporter();
     *         Map<String, Object> beans = new HashMap<>();
     *         beans.put("spitter:name=SpittleController", spittleController);
     *         exporter.setBeans(beans);
     *         return exporter;
     *     }
     *
     * 配置 MBeanExporter 的最简单方式是为它的 beans 属性配置一个 Map 集合，该集合中的元素是希望暴露为
     * JMX MBean 的一个或多个 bean。每个 Map 条目的 key 就是 MBean 的名称（由管理域的名字和一个 key-
     * value 对组成，在 SpittleController MBean 示例中是 spitter:name=HomeController），而 Map
     * 条目的值则是需要暴露的 Spring bean 引用。在这里，将输出 spittleController bean，以便它的属性
     * 可以通过 JMX 在运行时进行管理。
     *
     * 通过 MBeanExporter，spittleController bean 将作为模型 MBean 以 SpittleController 的名称导
     * 出到 MBean 服务器中，以实现管理功能。
     *
     * 此时，SpittleController 所有的 public 成员都被导出为 MBean 的操作或属性。这可能并不是所希望看到
     * 的结果，这里真正需要的只是可以配置 spittlesPerPage 属性。而不需要调用 spittles() 方法或
     * SpittleController 中的其他方法或属性。因此，需要一个方式来筛选所需要的属性或方法。
     *
     * 为了对 MBean 的属性和操作获得更细粒度的控制，Spring 提供了几种选择，包括：
     * （1）通过名称来声明需要暴露或忽略的 bean 方法；
     * （2）通过为 bean 增加接口来选择要暴露的方法；
     * （3）通过注解标注 bean 来标识托管的属性和操作。
     *
     * 这里会尝试每一种方式来决定哪一种最适合 SpittleController MBean。首先通过名称来选择 bean 的哪些方
     * 法需要暴露。
     *
     *
     * PS：MBean 服务器从何处而来
     *
     * 根据以上配置，MBeanExporter 会假设它正在一个应用服务器中（例如 Tomcat）或提供 MBean 服务器的其他
     * 上下文中运行。但是，如果 Spring 应用程序是独立的应用或运行的容器没有提供 MBean 服务器，就需要在
     * Spring 上下文中配置一个 MBean 服务器。
     *
     * 在XML配置中，<context:mbean-server> 元素可以实现该功能。如果使用 Java 配置的话，需要更直接的方式，
     * 也就是配置类型为 MBeanServerFactoryBean 的 bean（这也是在 XML 中 <context:mbean-server> 元素
     * 所作的事情）。
     *
     * MBeanServerFactoryBean 会创建一个 MBean 服务器，并将其作为 Spring 应用上下文中的 bean。默认情况
     * 下，这个 bean 的 ID 是 mbeanServer。了解到这一点，就可以将它装配到 MBeanExporter 的 server 属性
     * 中用来指定 MBean 要暴露到哪个 MBean 服务器中。
     *
     *
     *
     * 1、通过名称暴露方法
     *
     * MBean 信息装配器（MBean info assembler）是限制哪些方法和属性将在 MBean 上暴露的关键。其中有一个
     * MBean 信息装配器是 MethodNameBasedMBeanInfoAssembler。这个装配器指定了需要暴露为 MBean 操作的
     * 方法名称列表。对于 SpittleController bean 来说，希望把 spittlePerPage 暴露为托管属性。基于方法
     * 名的装配器如何来导出一个托管属性呢？
     *
     * 这里回顾下 JavaBean 的规则（这不是 Spring Bean 所必需的），spittlesPerPage 属性需要定义对应的
     * 存取器（accessor）方法，方法名必须为 setSpittlesPerPage() 和 getSpittlesPerPage()。为了限制
     * MBean 所暴露的内容，需要告诉 MethodNameBaseMBeanInfoAssembler 仅在 MBean 的接口中包含这两个
     * 方法。如下 MethodNameBaseMBeanInfoAssembler 的 bean 声明就配置了这些方法：
     *
     *     @Bean
     *     public MethodNameBasedMBeanInfoAssembler nameBasedAssembler() {
     *         MethodNameBasedMBeanInfoAssembler assembler =
     *                 new MethodNameBasedMBeanInfoAssembler();
     *         assembler.setManagedMethods(new String[] {
     *                 "getSpittlesPerPage", "setSpittlesPerPage"});
     *         return assembler;
     *     }
     *
     * managedMethods 属性可以接受一个方法名称的列表，指定了哪些方法将暴露为 MBean 的操作。因为本示例所
     * 配置的是 spittlesPerPage 属性的存取器方法，所以 spittlesPerPage 属性也自然成为了 MBean 的托管
     * 属性。
     *
     * 为了让这个装配器能够生效，需要将它装配进 MBeanExporter 中：
     *
     *     @Bean
     *     public MBeanExporter mbeanExporter(SpittleController spittleController,
     *                                        MBeanInfoAssembler assembler) {
     *         MBeanExporter exporter = new MBeanExporter();
     *         Map<String, Object> beans = new HashMap<>();
     *         beans.put("spitter:name=SpittleController", spittleController);
     *         exporter.setBeans(beans);
     *         exporter.setAssembler(assembler);
     *         return exporter;
     *     }
     *
     * 现在如果启动应用，SpittleController 的 spittlesPerPage 将作为有效的 MBean 托管属性，而
     * spittles() 方法并不会暴露为 MBean 的托管操作。
     *
     * 另一个基于方法名称的装配器是 MethodExclusionMBeanInfoAssembler。这个 MBean 信息装配器是
     * MethodNameBaseMBeanInfoAssembler 的反操作。它不是指定哪些方法需要暴露为 MBean 的托管操作，
     * MethodExclusionMBeanInfoAssembler 指定了不需要暴露为 MBean 托管操作的方法名称列表。例如，
     * 在这里使用 MethodExclusionMBeanInfoAssemble 指定 spittles() 作为不暴露的方法：
     *
     *     @Bean
     *     public MethodExclusionMBeanInfoAssembler exclusionAssembler() {
     *         MethodExclusionMBeanInfoAssembler assembler =
     *                 new MethodExclusionMBeanInfoAssembler();
     *         assembler.setIgnoredMethods(new String[] {"spittles"});
     *         return assembler;
     *     }
     *
     * 基于方法名称的装配器是最直接和易于使用的。但是如果需要把多个 Spring bean 导出为 MBean，能想象将出现
     * 什么样的情形吗？为装配器所配置的方法名称清单将会变得非常庞大；而且还有一种可能，希望暴露一个 bean 的
     * 某个方法，但不希望暴露另一个 bean 的同名方法。
     *
     * 很明显，在 Spring 配置方面，当导出多个 MBean 时，基于方法名称的方式并不能很好地满足此场景。下面看一下
     * 如果使用接口暴露 MBean 的操作和属性是否更为合适。
     *
     *
     *
     * 2、使用接口定义 MBean 的操作和属性
     *
     * Spring 的 InterfaceBasedMBeanInfoAssembler 是另一种 MBean 信息装配器，可以通过使用接口来选择
     * bean 的哪些方法需要暴露为 MBean 的托管操作。InterfaceBasedMBeanInfoAssembler 与基于方法名称的
     * 装配器很相似，只不过不再通过罗列方法名称来确定暴露哪些方法，而是通过列出接口来声明哪些方法需要暴露。
     *
     * 例如，假设定义了一个名为 SpittleControllerManagedOperations 的接口，如下所示：
     *
     * public interface SpittleControllerManagedOperations {
     *
     *     int getSpittlesPerPage();
     *
     *     void setSpittlesPerPage(int spittlesPerPage);
     *
     * }
     *
     * 在这里，选择了 setSpittlesPerPage() 方法和 getSpittlesPerPage() 方法作为需要暴露的方法。再次提醒，
     * 这一对存取器方法间接暴露了 spittlesPerPage 属性作为 MBean 的托管属性。为了应用此装配器，只需要使用如
     * 下的 assembler bean 替换之前基于方法名称的装配器即可：
     *
     *     @Bean
     *     public InterfaceBasedMBeanInfoAssembler interfaceBasedAssembler() {
     *         InterfaceBasedMBeanInfoAssembler assembler =
     *                 new InterfaceBasedMBeanInfoAssembler();
     *         assembler.setManagedInterfaces(
     *                 new Class<?>[] {SpittleControllerManagedOperations.class});
     *         return assembler;
     *     }
     *
     * managedInterfaces 属性接受一个或多个接口组成的列表作为 MBean 的管理接口 —— 在本示例中为
     * SpittleControllerManagedOperations 接口。
     *
     * SpittleController 并没有显式实现 SpittleControllerManagedOperations 接口，这可能并不明显，但相
     * 当有趣。这个接口只是为了标识导出的内容，但并不需要在代码中直接实现该接口。不过，SpittleController 应
     * 该实现这个接口，其实也没有其他的原因，只是在 MBean 和实现类之间应该有一个一致的协议。
     *
     * 如果通过接口来选择 MBean 操作的话，最吸引人的一点在于可以把很多方法放在少量的接口中，从而确保
     * InterfaceBasedMBeanInfoAssembler 的配置尽量简洁。在输出多个 MBean 时，基于接口的方式可以
     * 帮助保持 Spring 配置的简洁。
     *
     * 最终，这些托管操作必须在某处声明，无论是在 Spring 配置中还是在某个接口中。此外，从代码角度看，托管操作
     * 的声明是一种重复 —— 在接口中或 Spring 上下文中声明的方法名称与实现中所声明的方法名称存在重复。之所以
     * 存在这种重复，没有其他原因，仅仅是为了满足 MBeanExporter 的需要而产生的。
     *
     * Java 注解的一项工作就是帮助消除这种重复。下面看看如何通过使用注解标注 Spring 管理的 bean，从而将其导
     * 出 MBean。
     *
     *
     *
     * 3、使用注解驱动的 MBean
     *
     * 除了上面展示的 MBean 信息装配器，Spring 还提供了另一种装配器 —— MetadataMBeanInfoAssembler，这种
     * 装配器可以使用注解标识哪些 bean 的方法需要暴露为 MBean 的托管操作和属性。这里完全可以向你展示如何使用
     * 这种装配器，但不会这么做。这是因为手工装配它非常繁杂，仅仅是为了使用注解并不值得这么做。相反，这里将向你
     * 展示如何使用 Spring context 配置命名空间中的 <context:mbean-export> 元素。这个便捷的元素装配了
     * MBean 导出器以及为了在 Spring 启用注解驱动的 MBean 所需要的装配器。这里所需要做的就是使用它来替换之前
     * 所使用的 MBeanExporter bean：
     *
     * <context:mbean-export server="mbeanServer" />
     *
     * 现在，要把任意一个 Spring bean 转变为 MBean，所需要做的仅仅是使用 @ManagedResource 注解标注 bean
     * 并使用 @ManagedOperation 或 @ManagedAttribute 注解标注 bean 的方法或属性。例如，如下代码展示了如
     * 何使用注解把 SpittleController 导出为 MBean。
     *
     * @Controller
     * @ManagedResource(objectName="spitter:name=SpittleController")
     * public class SpittleController {
     *
     *     @ManagedAttribute
     *     public int getSpittlesPerPage() {
     *         return spittlesPerPage;
     *     }
     *
     *     @ManagedAttribute
     *     public void setSpittlesPerPage(int spittlesPerPage) {
     *         this.spittlesPerPage = spittlesPerPage;
     *     }
     *
     * }
     *
     * 在类级别使用了 @ManagedResource 注解来标识这个 bean 应该被导出为 MBean。objectName 属性标识了域
     * （Spitter）和 MBean 的名称（SpittleController）。
     *
     * spittlesPerPage 属性的存取器方法都使用了 @ManagedAttribute 注解来进行标注，这表示该属性应该暴露为
     * MBean 的托管属性。注意，其实并不需要使用注解同时标注这两个存取器方法。如果选择仅标注
     * setSpittlesPerPage() 方法，那仍可以通过 JMX 设置该属性，但这样的话将不能查看该属性的值。相反，如果
     * 仅仅标注 getSpittlesPerPage() 方法，那可以通过 JMX 查看该属性的值，但无法修改该属性的值。
     *
     * 同样需要提醒一下，还可以使用 @ManagedOperation 注解替换 @ManagedAttribute 注解来标注存取器方法。
     * 如下所示：
     *
     *     @ManagedOperation
     *     public int getSpittlesPerPage() {
     *         return spittlesPerPage;
     *     }
     *
     *     @ManagedOperation
     *     public void setSpittlesPerPage(int spittlesPerPage) {
     *         this.spittlesPerPage = spittlesPerPage;
     *     }
     *
     * 这会将方法暴露为 MBean 的托管操作，但是并不会把 spittlesPerPage 属性暴露为 MBean 的托管属性。这是
     * 因为在暴露 MBean 功能时，使用 @ManagedOperation 注解标注方法是严格限制方法的，并不会把它作为
     * JavaBean 的存取器方法。因此，使用 @ManagedOperation 可以用来把 bean 的方法暴露为 MBean 托管操作，
     * 而使用 @ManagedAttribute 可以把 bean 的属性暴露为 MBean 托管属性。
     *
     *
     *
     * 4、处理 MBean 冲突
     *
     * 到目前为止，已经看到可以使用多种方式在 MBean 服务器中注册 MBean。在所有的示例中，为 MBean 指定的对象
     * 名称是由管理域名和 key-value 对组成的。如果 MBean 服务器中不存在与 MBean 名字相同的已注册的 MBean，
     * 那这里的 MBean 注册时就不会有任何问题。但是如果名字冲突时，将会发生什么呢？
     *
     * 默认情况下，MBeanExporter 将抛出 InstanceAlreadyExistsException 异常，该异常表明 MBean 服务器中
     * 已经存在相同名字的 MBean。不过，可以通过 MBeanExporter 的 registrationBehaviorName 属性或者
     * <context:mbean-export> 的 registration 属性指定冲突处理机制来改变默认行为。
     *
     * Spring 提供了 3 种借助 registrationBehaviorName 属性来处理 MBean 名字冲突的机制：
     * （1）FAIL_ON_EXISTING：如果已存在相同名字的 MBean，则失败（默认行为）；
     * （2）IGNORE_EXISTING：忽略冲突，同时也不注册新的 MBean；
     * （3）REPLACE_EXISTING：用新的 MBean 覆盖已存在的 MBean。
     *
     * 例如，如果使用 MBeanExporter，可以通过设置 registrationBehaviorName 属性为 RegistrationPolicy.
     * IGNORE_EXISTING 来忽略冲突，如下所示：
     *
     *     @Bean
     *     public MBeanExporter mbeanExporter(SpittleController spittleController,
     *                                        MBeanInfoAssembler assembler) {
     *         MBeanExporter exporter = new MBeanExporter();
     *         Map<String, Object> beans = new HashMap<>();
     *         beans.put("spitter:name=SpittleController", spittleController);
     *         exporter.setBeans(beans);
     *         exporter.setAssembler(assembler);
     *         exporter.setRegistrationPolicy(RegistrationPolicy.IGNORE_EXISTING);
     *         return exporter;
     *     }
     *
     * 现在已使用 MBeanExporter 注册了 MBean，还需要一种方式来访问它们并进行管理。正如之前所看到的，可以使
     * 用诸如 JConsole 之类的工具来访问本地的 MBean 服务器，进而显示和操纵 MBean，但是像 JConsole 之类的
     * 工具并不适合在程序中对 MBean 进行管理。如何在一个应用中操纵另一个应用中的 MBean 呢？幸运的是，还存在
     * 另一种方式可以把 MBean 作为远程对象进行访问。后续将进一步研究 Spring 对远程 MBean 的支持，了解如何
     * 通过远程接口以标准的方式来访问 MBean。
     */
    public static void main(String[] args) {

    }

}
