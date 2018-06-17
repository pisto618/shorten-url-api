package com.bluebik.userurlapi.userurl;

import com.bluebik.userurlapi.constant.HTTPURLConstant;
import com.bluebik.userurlapi.urlshortener.URLShortenerController;
import com.bluebik.userurlapi.urlshortener.UserURLService;
import com.bluebik.userurlapi.user.ApplicationUserController;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import java.io.UnsupportedEncodingException;

@RunWith(SpringRunner.class)
@WebMvcTest(value = URLShortenerController.class, secure = false)
public class URLShortenerControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(URLShortenerController.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserURLService userURLService;

    private String httpURL = "{\"actualURL\":\"http://www.google.com\"}";
    private String httpsURL = "{\"actualURL\":\"https://www.google.com\"}";
    private String urlWithoutPrefix = "{\"actualURL\":\"www.google.com\"}";
    private String invalidURL = "{\"actualURL\":\"blablabla\"}";

    @Test
    public void saveHTTPLink() throws Exception {

        MvcResult result = executeShorteningAPI(httpURL);

        MockHttpServletResponse response = result.getResponse();

        validateURLShortenResult(response);
    }

    @Test
    public void saveHTTPSLink() throws Exception {

        MvcResult result = executeShorteningAPI(httpsURL);

        MockHttpServletResponse response = result.getResponse();

        validateURLShortenResult(response);
    }

    @Test
    public void saveLinkWithoutHTTPPrefix() throws Exception {

        MvcResult result = executeShorteningAPI(urlWithoutPrefix);

        MockHttpServletResponse response = result.getResponse();

        validateURLShortenResult(response);

    }

    @Test
    public void notSaveInvalidURL() throws Exception {

        MvcResult result = executeShorteningAPI(invalidURL);

        MockHttpServletResponse response = result.getResponse();

        validateInvalidURLResult(response);

    }

    private MvcResult executeShorteningAPI(String input) throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/saveLink")
                .accept(MediaType.APPLICATION_JSON).content(input)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        return result;
    }

    private void validateURLShortenResult(MockHttpServletResponse response) throws UnsupportedEncodingException {
        Assert.assertEquals(String.valueOf(HttpStatus.OK.value()), String.valueOf(response.getStatus()));
        String shortenURL = response.getContentAsString();
        Assert.assertTrue(shortenURL.contains(HTTPURLConstant.DOMAIN));
        String key = shortenURL.substring(shortenURL.lastIndexOf("/") + 1);
        Assert.assertEquals(String.valueOf(8),String.valueOf(key.length()));

    }

    private void validateInvalidURLResult(MockHttpServletResponse response) throws UnsupportedEncodingException {
        Assert.assertEquals(String.valueOf(HttpStatus.BAD_REQUEST.value()), String.valueOf(response.getStatus()));
        Assert.assertEquals("Not URL Try Again",response.getContentAsString());

    }

}
