package com.avengers.hawkeye.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by 鸿敏 on 2017/2/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = com.avengers.hawkeye.SpringBootTraceApplication.class)
@WebAppConfiguration
public class UserControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setupMockMvc() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testView() throws Exception {
        User user = new User();
        user.setId("9");
        user.setName("zhonghongmin");

        mockMvc.perform(get("/trace/demo/9")).andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(user)));
    }

    @Test
    public void testError() throws Exception {
        User user = new User();
        user.setId("99");
        user.setName("zhonghongmin");

        try {
            mockMvc.perform(get("/trace/demo/99"));
        }catch (Exception e){

        }
    }
}
