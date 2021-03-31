package com.siwuxie095.spring.chapter20th.example2nd.web;

import com.siwuxie095.spring.chapter20th.example2nd.data.SpittleRepository;
import com.siwuxie095.spring.chapter20th.example2nd.domain.Spittle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-03-31 08:25:36
 */
@SuppressWarnings("all")
@Controller
@RequestMapping("/spittles")
public class SpittleController {

    private static final String MAX_LONG_AS_STRING = "9223372036854775807";

    private SpittleRepository spittleRepository;

    public static final int DEFAULT_SPITTLES_PER_PAGE = 25;

    private int spittlesPerPage = DEFAULT_SPITTLES_PER_PAGE;

    public int getSpittlesPerPage() {
        return spittlesPerPage;
    }

    public void setSpittlesPerPage(int spittlesPerPage) {
        this.spittlesPerPage = spittlesPerPage;
    }

    @Autowired
    public SpittleController(SpittleRepository spittleRepository) {
        this.spittleRepository = spittleRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Spittle> spittles(
            @RequestParam(value = "max", defaultValue = MAX_LONG_AS_STRING) long max,
            @RequestParam(value = "count", defaultValue = "20") int count) {
        return spittleRepository.findSpittles(max, count);
    }

}


