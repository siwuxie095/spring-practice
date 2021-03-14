package com.siwuxie095.spring.chapter15th.example6th.cfg;

import com.siwuxie095.spring.chapter15th.example6th.domain.Spitter;
import com.siwuxie095.spring.chapter15th.example6th.domain.Spittle;
import com.siwuxie095.spring.chapter15th.example6th.service.SpitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-03-14 21:54:13
 */
@SuppressWarnings("all")
@Component
@WebService(serviceName = "SpitterService")
public class SpitterServiceEndpoint {

    @Autowired
    private SpitterService spitterService;

    @WebMethod
    public void addSpittle(Spittle spittle) {
        spitterService.saveSpittle(spittle);
    }

    @WebMethod
    public void deleteSpittle(long spittleId) {
        spitterService.deleteSpittle(spittleId);
    }

    @WebMethod
    public List<Spittle> getRecentSpittles(int spittleCount) {
        return spitterService.getRecentSpittles(spittleCount);
    }

    @WebMethod
    public List<Spittle> getSpittlesForSpitter(Spitter spitter) {
        return spitterService.getSpittlesForSpitter(spitter);
    }

}

