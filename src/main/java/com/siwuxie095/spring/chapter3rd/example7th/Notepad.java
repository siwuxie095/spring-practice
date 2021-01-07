package com.siwuxie095.spring.chapter3rd.example7th;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Jiajing Li
 * @date 2021-01-07 08:23:06
 */
@SuppressWarnings("all")
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Notepad {

}
