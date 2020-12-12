package com.siwuxie095.spring.chapter1st.example4th;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;

/**
 * KnightXmlMain.java 加载包含 Knight 的 Spring 上下文
 *
 * @author Jiajing Li
 * @date 2020-12-12 14:01:48
 */
@SuppressWarnings("all")
public class KnightXmlMain {

    public static void main(String[] args) {
        /*
         * file:./src/main/java/com/siwuxie095/spring/chapter1st/example4th/res/knight.xml
         *
         * or
         *
         * file:src/main/java/com/siwuxie095/spring/chapter1st/example4th/res/knight.xml
         */
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("file:src/main/java/com/siwuxie095/spring/chapter1st/example4th/res/knight.xml");
        Knight knight = context.getBean(Knight.class);
        knight.embarkOnQuest();
        context.close();
    }

}
