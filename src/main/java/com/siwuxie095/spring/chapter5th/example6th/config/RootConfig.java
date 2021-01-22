package com.siwuxie095.spring.chapter5th.example6th.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.core.type.filter.RegexPatternTypeFilter;

import java.util.regex.Pattern;

/**
 * @author Jiajing Li
 * @date 2021-01-22 21:59:15
 */
@SuppressWarnings("all")
@Configuration
@Import(DataConfig.class)
@ComponentScan(basePackages = {"com.siwuxie095.spring.chapter5th.example6th"},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.CUSTOM, value = RootConfig.WebPackage.class)
        })
public class RootConfig {

    public static class WebPackage extends RegexPatternTypeFilter {
        public WebPackage() {
            super(Pattern.compile("com\\.siwuxie095\\.spring\\.chapter5th\\.example6th\\.web"));
        }
    }

}
