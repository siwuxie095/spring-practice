package com.siwuxie095.spring.chapter7th.example8th.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Jiajing Li
 * @date 2021-02-05 20:53:58
 */
@SuppressWarnings("all")
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Spittle Not Found")
public class SpittleNotFoundException extends RuntimeException {

}
