package de.yard.webdavproxy;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static de.yard.webdavproxy.controller.WebDavController.BASEURL_CONTENT;
import static de.yard.webdavproxy.controller.WebDavController.BASEURL_IMAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@WebAppConfiguration
@Slf4j
class CorsTest {

    public static final String AccessControlAllowOrigin = "Access-Control-Allow-Origin";
    public static final String AccessControlAllowMethods = "Access-Control-Allow-Methods";

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testCorsForContent() throws Exception {

        MvcResult result = mockMvc.perform(options(BASEURL_CONTENT)
                        .header("Access-Control-Request-Method", "GET")
                        .header("Origin", "http://www.someurl.com"))
                .andReturn();

        assertEquals(HttpStatus.SC_OK, result.getResponse().getStatus());

        log.debug(result.getResponse().getHeaderNames().toString());
        validateCorsHeader(result.getResponse());
    }

    @Test
    public void testCorsForImage() throws Exception {

        MvcResult result = mockMvc.perform(options(BASEURL_IMAGE)
                        .header("Access-Control-Request-Method", "GET")
                        .header("Origin", "http://www.someurl.com"))
                .andReturn();

        assertEquals(HttpStatus.SC_OK, result.getResponse().getStatus());

        log.debug(result.getResponse().getHeaderNames().toString());
        validateCorsHeader(result.getResponse());
    }

    private void validateCorsHeader(MockHttpServletResponse response){
        String allowOrigin = response.getHeader(AccessControlAllowOrigin);
        assertNotNull(allowOrigin);
        assertEquals("*", allowOrigin);

        String allowMethods = response.getHeader(AccessControlAllowMethods);
        assertNotNull(allowMethods);
        assertEquals("GET", allowMethods);
    }
}
