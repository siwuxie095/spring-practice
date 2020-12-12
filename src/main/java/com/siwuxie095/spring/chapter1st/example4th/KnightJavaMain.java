package com.siwuxie095.spring.chapter1st.example4th;

import com.siwuxie095.spring.chapter1st.example4th.cfg.KnightConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * KnightJavaMain.java 加载包含 Knight 的 Spring 上下文
 *
 * @author Jiajing Li
 * @date 2020-12-12 14:06:07
 */
@SuppressWarnings("all")
public class KnightJavaMain {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();
        context.register(KnightConfig.class);
        context.refresh();

        Knight knight = context.getBean(Knight.class);
        knight.embarkOnQuest();
        context.close();
    }

}
