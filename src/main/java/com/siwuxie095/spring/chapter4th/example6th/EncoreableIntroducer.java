package com.siwuxie095.spring.chapter4th.example6th;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareParents;

/**
 * @author Jiajing Li
 * @date 2021-01-16 23:31:38
 */
@SuppressWarnings("all")
@Aspect
public class EncoreableIntroducer {

    @DeclareParents(value = "com.siwuxie095.spring.chapter4th.example6th.Performance+",
            defaultImpl = DefaultEncoreable.class)
    public static Encoreable encoreable;

}
