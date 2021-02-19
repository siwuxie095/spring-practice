package com.siwuxie095.spring.chapter9th.example12th;

import com.siwuxie095.spring.chapter9th.example12th.web.HomeController;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * @author Jiajing Li
 * @date 2021-02-19 08:27:49
 */
@SuppressWarnings("all")
public class HomeControllerTest {

    @Test
    public void testHomePage() throws Exception {
        HomeController controller = new HomeController();
        MockMvc mockMvc = standaloneSetup(controller).build();
        mockMvc.perform(get("/"))
                .andExpect(view().name("home"));
    }

}
