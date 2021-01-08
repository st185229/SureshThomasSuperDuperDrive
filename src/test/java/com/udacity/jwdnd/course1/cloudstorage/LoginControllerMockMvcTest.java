package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.controller.LoginController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(LoginController.class)
public class LoginControllerMockMvcTest {

    @Autowired
    public MockMvc mvc;

    @Test
    public void testLoginWithoutName() throws Exception {
        mvc.perform(get("/").accept(org.springframework.http.MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));

    }

    @Test
    public void testHelloWithName() throws Exception {
        mvc.perform(get("/login").param("name", "Dolly")
                .accept(org.springframework.http.MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("hello"))
                .andExpect(model().attribute("user", "Dolly"));
    }

}
