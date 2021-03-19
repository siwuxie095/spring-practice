package com.siwuxie095.spring.chapter16th.example5th.api;

import com.siwuxie095.spring.chapter16th.example5th.data.SpittleNotFoundException;
import com.siwuxie095.spring.chapter16th.example5th.data.SpittleRepository;
import com.siwuxie095.spring.chapter16th.example5th.domain.Spittle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-03-18 22:49:56
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/spittles")
public class SpittleApiController {

    private static final String MAX_LONG_AS_STRING = "9223372036854775807";

    private SpittleRepository spittleRepository;

    @Autowired
    public SpittleApiController(SpittleRepository spittleRepository) {
        this.spittleRepository = spittleRepository;
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public List<Spittle> spittles(
            @RequestParam(value = "max", defaultValue = MAX_LONG_AS_STRING) long max,
            @RequestParam(value = "count", defaultValue = "20") int count) {
        return spittleRepository.findSpittles(max, count);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public Spittle spittleById(@PathVariable Long id) {
        return spittleRepository.findOne(id);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Spittle> saveSpittle(@RequestBody Spittle spittle, UriComponentsBuilder ucb) {
        Spittle saved = spittleRepository.save(spittle);

        HttpHeaders headers = new HttpHeaders();
        URI locationUri = ucb.path("/spittles/")
                .path(String.valueOf(saved.getId()))
                .build()
                .toUri();
        headers.setLocation(locationUri);

        ResponseEntity<Spittle> responseEntity = new ResponseEntity<Spittle>(saved, headers, HttpStatus.CREATED);
        return responseEntity;
    }

    @ExceptionHandler(SpittleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody
    Error spittleNotFound(SpittleNotFoundException e) {
        long spittleId = e.getSpittleId();
        return new Error(4, "Spittle [" + spittleId + "] not found");
    }

}

