package com.siwuxie095.spring.chapter1st.example5th;

import com.siwuxie095.spring.chapter1st.example4th.Knight;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Jiajing Li
 * @date 2020-12-16 08:21:17
 */
@SuppressWarnings("all")
public class MinstrelAopMain {

    public static void main(String[] args) {
        /*
         * file:./src/main/java/com/siwuxie095/spring/chapter1st/example5th/res/minstrel.xml
         *
         * or
         *
         * file:src/main/java/com/siwuxie095/spring/chapter1st/example5th/res/minstrel.xml
         */
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("file:src/main/java/com/siwuxie095/spring/chapter1st/example5th/res/minstrel.xml");
        Knight knight = context.getBean(Knight.class);
        knight.embarkOnQuest();
        context.close();
    }

}
