package com.siwuxie095.spring.chapter3rd.example5th;

/**
 * @author Jiajing Li
 * @date 2021-01-05 07:55:42
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 条件化的 bean
     *
     * 假设你希望一个或多个 bean 只有在应用的类路径下包含特定的库时才创建。或者希望某个 bean 只有当另外某个
     * 特定的 bean 也声明了之后才会创建。还可能要求只有某个特定的环境变量设置之后，才会创建某个 bean。
     *
     * 在 Spring 4 之前，很难实现这种级别的条件化配置，但是 Spring 4 引入了一个新的 @Conditional 注解，
     * 它可以用到带有 @Bean 注解的方法上。如果给定的条件计算结果为 true，就会创建这个 bean，否则的话，这个
     * bean 会被忽略。
     *
     * 例如，假设有一个名为 MagicBean 的类，现在希望只有设置了 magic 环境属性的时候，Spring 才会实例化这
     * 个类。如果环境中没有这个属性，那么 MagicBean 将会被忽略。在 MagicConfig 所展现的配置中，使用注解
     * @Conditional 条件化地配置了 MagicBean。如下：
     *
     *     @Bean
     *     @Conditional(MagicExistsCondition.class)
     *     public MagicBean magicBean() {
     *         return new MagicBean();
     *     }
     *
     * 可以看到，@Conditional 中给定了一个 Class，它指明了条件。在本例中，也就是 MagicExistsCondition。
     * @Conditional 将会通过 Condition 接口进行条件对比：
     *
     * 	boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata);
     *
     * 设置给 @Conditional 的类可以是任意实现了 Condition 接口的类型。可以看出来，这个接口实现起来很简单
     * 直接，只需提供 matches() 方法的实现即可。如果 matches() 方法返回 true，就会创建带有 @Conditional
     * 注解的 bean。如果 matches() 方法返回 false，将不会创建这些 bean。
     *
     * 在本例中，需要创建 Condition 的实现并根据环境中是否存在 magic 属性来做出决策。
     *
     * 以 MagicExistsCondition 为例，这是完成该功能的 Condition 实现类。如下：
     *
     * public class MagicExistsCondition implements Condition {
     *
     *     @Override
     *     public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
     *         Environment env = context.getEnvironment();
     *         return env.containsProperty("magic");
     *     }
     *
     * }
     *
     * matches() 方法很简单但功能强大。它通过给定的 ConditionContext 对象进而得到 Environment 对象，并
     * 使用这个对象检查环境中是否存在名为 magic 的环境属性。在本例中，属性的值是什么无所谓，只要属性存在即可
     * 满足要求。如果满足这个条件的话，matches() 方法就会返回 true。所带来的结果就是条件能够得到满足，所有
     * @Conditional 注解上引用 MagicExistsCondition 的 bean 都会被创建。
     *
     * 但是，如果这个属性不存在的话，就无法满足条件，matches() 方法会返回 false，这些 bean 都不会被创建。
     *
     * MagicExistsCondition 中只是使用了 ConditionContext 得到的 Environment，但 Condition 实现的
     * 考量因素可能会比这更多。matches() 方法会得到 ConditionContext 和 AnnotatedTypeMetadata 对象用
     * 来做出决策。
     *
     * ConditionContext 是一个接口，大致如下所示：
     *
     * public interface ConditionContext {
     *
     * 	BeanDefinitionRegistry getRegistry();
     * 	ConfigurableListableBeanFactory getBeanFactory();
     * 	Environment getEnvironment();
     * 	ResourceLoader getResourceLoader();
     * 	ClassLoader getClassLoader();
     *
     * }
     *
     * 通过 ConditionContext，可以做到如下几点：
     * （1）借助 getRegistry() 返回的 BeanDefinitionRegistry 检查 bean 定义；
     * （2）借助 getBeanFactory() 返回的 ConfigurableListableBeanFactory 检查 bean 是否存在，甚至
     * 探查 bean 的属性；
     * （3）借助 getEnvironment() 返回的 Environment 检查环境变量是否存在以及它的值是什么；
     * （4）读取并探查 getResourceLoader() 返回的 ResourceLoader 所加载的资源；
     * （5）借助 getClassLoader() 返回的 ClassLoader 加载并检查类是否存在。
     *
     * AnnotatedTypeMetadata 则能够检查带有 @Bean 注解的方法上还有什么其他的注解。像 ConditionContext
     * 一样，AnnotatedTypeMetadata 也是一个接口。它如下所示：
     *
     * public interface AnnotatedTypeMetadata {
     *
     * 	boolean isAnnotated(String annotationName);
     * 	Map<String, Object> getAnnotationAttributes(String annotationName);
     * 	Map<String, Object> getAnnotationAttributes(String annotationName, boolean classValuesAsString);
     * 	MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationName);
     * 	MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationName, boolean classValuesAsString);
     *
     * }
     *
     * 借助 isAnnotated() 方法，能够判断带有 @Bean 注解的方法是不是还有其他特定的注解。借助其他的那些方法，
     * 能够检查 @Bean 注解的方法上其他注解的属性。
     *
     * 非常有意思的是，从 Spring 4 开始，@Profile 注解进行了重构，使其基于 @Conditional 和 Condition
     * 实现。作为如何使用 @Conditional 和 Condition 的例子，下面来看一下在 Spring 4 中，@Profile 是
     * 如何实现的。
     *
     * @Profile 注解如下所示：
     *
     * @Retention(RetentionPolicy.RUNTIME)
     * @Target({ElementType.TYPE, ElementType.METHOD})
     * @Documented
     * @Conditional(ProfileCondition.class)
     * public @interface Profile {
     *
     * 	String[] value();
     *
     * }
     *
     * 注意：@Profile 本身也使用了 @Conditional 注解，并且引用 ProfileCondition 作为 Condition 实现。
     * 如下所示，ProfileCondition 实现了 Condition 接口，并且在做出决策的过程中，考虑到了 ConditionContext
     * 和 AnnotatedTypeMetadata 中的多个因素。
     *
     * ProfileCondition 检查某个 bean profile 是否可用：
     *
     * class ProfileCondition implements Condition {
     *
     * 	@Override
     * 	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
     * 		if (context.getEnvironment() != null) {
     * 			MultiValueMap<String, Object> attrs =
     * 			metadata.getAllAnnotationAttributes(Profile.class.getName());
     * 			if (attrs != null) {
     * 				for (Object value : attrs.get("value")) {
     * 					if (context.getEnvironment().acceptsProfiles(((String[]) value))) {
     * 						return true;
     * 					}
     * 				}
     * 				return false;
     * 			}
     * 		}
     * 		return true;
     * 	}
     *
     * }
     *
     * 可以看到，ProfileCondition 通过 AnnotatedTypeMetadata 得到了用于 @Profile 注解的所有属性。借助
     * 该信息，它会明确地检查 value 属性，该属性包含了bean的profile名称。然后，它根据通过 ConditionContext
     * 得到的 Environment 来检查[借助 acceptsProfiles() 方法] 该 profile 是否处于激活状态。
     */
    public static void main(String[] args) {

    }

}
