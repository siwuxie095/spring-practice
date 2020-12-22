package com.siwuxie095.spring.chapter2nd.example3rd;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * 测试组件扫描能够发现 CompactDisc
 *
 * @author Jiajing Li
 * @date 2020-12-21 22:39:16
 */
@SuppressWarnings("all")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CDPlayerAutoConfig.class)
public class CDPlayerAutoConfigTest {

    @Autowired
    private CompactDisc cd;

    @Test
    public void cdShouldNotBeNull() {
        assertNotNull(cd);
    }

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
