package com.siwuxie095.spring.chapter3rd.example5th;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 在 Condition 中检查是否存在 magic 属性
 *
 * @author Jiajing Li
 * @date 2021-01-05 08:02:11
 */
@SuppressWarnings("all")
public class MagicExistsCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment env = context.getEnvironment();
        return env.containsProperty("magic");
    }

}
