package com.siwuxie095.spring.chapter6th.example7th.cfg;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.core.type.filter.RegexPatternTypeFilter;

import java.util.regex.Pattern;

/**
 * @author Jiajing Li
 * @date 2021-01-28 22:03:08
 */
@SuppressWarnings("all")
@Configuration
@Import(DataConfig.class)
@ComponentScan(basePackages = {"com.siwuxie095.spring.chapter6th.example7th"},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.CUSTOM, value = RootConfig.WebPackage.class)
        })
public class RootConfig {

    public static class WebPackage extends RegexPatternTypeFilter {
        public WebPackage() {
            super(Pattern.compile("com\\.siwuxie095\\.spring\\.chapter6th\\.example7th\\.web"));
        }
    }

}

