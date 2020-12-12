package com.siwuxie095.spring.chapter1st.example4th.cfg;

import com.siwuxie095.spring.chapter1st.example4th.BraveKnight;
import com.siwuxie095.spring.chapter1st.example4th.Knight;
import com.siwuxie095.spring.chapter1st.example4th.Quest;
import com.siwuxie095.spring.chapter1st.example4th.SlayDragonQuest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring 提供了基于 Java 的配置，可作为 XML 的替代方案
 *
 * @author Jiajing Li
 * @date 2020-12-12 13:51:45
 */
@SuppressWarnings("all")
@Configuration
public class KnightConfig {

    @Bean
    public Knight knight() {
        return new BraveKnight(quest());
    }

    @Bean
    public Quest quest() {
        return new SlayDragonQuest(System.out);
    }

}
