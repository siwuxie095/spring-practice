package com.siwuxie095.spring.chapter15th.example6th;

import com.siwuxie095.spring.chapter15th.example5th.HttpInvokerClientConfig;
import com.siwuxie095.spring.chapter15th.example6th.service.SpitterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author Jiajing Li
 * @date 2021-03-14 21:56:47
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JaxWsClientConfig.class)
public class SpitterServiceTest {

    @Autowired
    private SpitterService spitterService;

    @Test
    public void getAllSpitters() {
        assertEquals(null, spitterService.getAllSpitters());
    }

}

