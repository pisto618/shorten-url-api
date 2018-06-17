package com.bluebik.userurlapi.user;

import com.bluebik.userurlapi.urlshortener.URLShortenerController;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ApplicationUserController.class, secure = false)
public class ApplicationUserControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(URLShortenerController.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationUserService applicationUserService;

    private String exampleApplicationUser = "{\"username\":\"admin\",\"password\":\"password\"}";
    private String invalidUser = "{\"username\":\"\",\"password\":\"\"}";

    @Test
    public void createApplicationUser() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/signUp")
                .accept(MediaType.APPLICATION_JSON).content(exampleApplicationUser)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        Assert.assertEquals(String.valueOf(HttpStatus.OK.value()), String.valueOf(response.getStatus()));
        Assert.assertEquals("Add user success",response.getContentAsString());
    }

    @Test
    public void notCreateInvalidUser() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/signUp")
                .accept(MediaType.APPLICATION_JSON).content(invalidUser)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        Assert.assertEquals(String.valueOf(HttpStatus.BAD_REQUEST.value()), String.valueOf(response.getStatus()));
        Assert.assertEquals("Invalid User",response.getContentAsString());
    }

    @Test
    public void notCreateDuplicateUser() throws Exception {
        ApplicationUser mockApplicationUser = new ApplicationUser();
        mockApplicationUser.setUsername("admin");
        mockApplicationUser.setPassword("password");

        Mockito.when(applicationUserService.findUserByUsername(Mockito.anyString()))
                .thenReturn(mockApplicationUser);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/signUp")
                .accept(MediaType.APPLICATION_JSON).content(exampleApplicationUser)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        Assert.assertEquals(String.valueOf(HttpStatus.BAD_REQUEST.value()), String.valueOf(response.getStatus()));
        Assert.assertEquals("User Existing",response.getContentAsString());
    }


}
