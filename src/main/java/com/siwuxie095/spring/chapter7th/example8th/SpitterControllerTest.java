package com.siwuxie095.spring.chapter7th.example8th;

import com.siwuxie095.spring.chapter7th.example8th.data.SpitterRepository;
import com.siwuxie095.spring.chapter7th.example8th.web.SpitterController;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * @author Jiajing Li
 * @date 2021-02-05 21:02:08
 */
@SuppressWarnings("all")
public class SpitterControllerTest {

    @Test
    public void shouldShowRegistration() throws Exception {
        SpitterRepository mockRepository = mock(SpitterRepository.class);
        SpitterController controller = new SpitterController(mockRepository);
        MockMvc mockMvc = standaloneSetup(controller).build();
        mockMvc.perform(get("/spitter/register"))
                .andExpect(view().name("registerForm"));
    }

    @Test
    @Ignore
    public void shouldProcessRegistration() throws Exception {
        SpitterRepository mockRepository = mock(SpitterRepository.class);
        Spitter unsaved = new Spitter("jbauer", "24hours", "Jack", "Bauer", "jbauer@ctu.gov");
        Spitter saved = new Spitter(24L, "jbauer", "24hours", "Jack", "Bauer", "jbauer@ctu.gov");
        when(mockRepository.save(unsaved)).thenReturn(saved);

        SpitterController controller = new SpitterController(mockRepository);
        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(post("/spitter/register")
                .param("firstName", "Jack")
                .param("lastName", "Bauer")
                .param("username", "jbauer")
                .param("password", "24hours")
                .param("email", "jbauer@ctu.gov"))
                .andExpect(redirectedUrl("/spitter/jbauer"));

        verify(mockRepository, atLeastOnce()).save(unsaved);
    }

    @Test
    @Ignore
    public void shouldFailValidationWithNoData() throws Exception {
        SpitterRepository mockRepository = mock(SpitterRepository.class);
        SpitterController controller = new SpitterController(mockRepository);
        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(post("/spitter/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("registerForm"))
                .andExpect(model().errorCount(5))
                .andExpect(model().attributeHasFieldErrors(
                        "spitter", "firstName", "lastName", "username", "password", "email"));
    }

}
