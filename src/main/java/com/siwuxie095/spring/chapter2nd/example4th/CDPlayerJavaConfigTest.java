package com.siwuxie095.spring.chapter2nd.example4th;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author Jiajing Li
 * @date 2020-12-22 22:10:33
 */
@SuppressWarnings("all")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CDPlayerJavaConfig.class)
public class CDPlayerJavaConfigTest {

    @Rule
    public final StandardOutputStreamLog log = new StandardOutputStreamLog();

    @Autowired
    private MediaPlayer player;

    @Test
    public void play() {
        player.play();
        assertEquals(
                "Playing Sgt. Pepper's Lonely Hearts Club Band by The Beatles\n",
                log.getLog());
    }

}
