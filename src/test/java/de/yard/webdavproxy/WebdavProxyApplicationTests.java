package de.yard.webdavproxy;

import de.yard.webdavproxy.controller.WebDavController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class WebdavProxyApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WebDavController webDavController;

    @Test
    void contextLoads() {
        Assertions.assertThat(webDavController).isNotNull();
    }

}
