package de.yard.webdavproxy;

import de.yard.TestUtils;
import de.yard.webdavproxy.controller.WebDavService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static de.yard.TestUtils.HOST;
import static de.yard.TestUtils.USER;
import static de.yard.webdavproxy.controller.WebDavController.BASEURL_CONTENT;
import static de.yard.webdavproxy.controller.WebDavController.BASEURL_IMAGE;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WebDavControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WebDavService webDavService;

    @Test
    public void testContent() throws Exception {

        List<String> list = new ArrayList<>();
        list.add("entry1");
        list.add("entry2");

        ArgumentCaptor<String> argumentHost = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> argumentUser = ArgumentCaptor.forClass(String.class);

        Mockito.when(webDavService.propfind(argumentHost.capture(), argumentUser.capture())).thenReturn(list);

        mockMvc.perform(get(BASEURL_CONTENT).param("host", HOST).param("user", USER))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("entry1")));

        assertEquals(HOST, argumentHost.getValue());
        assertEquals(USER, argumentUser.getValue());
    }

    @Test
    public void testImage() throws Exception {

        ArgumentCaptor<String> argumentHost = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> argumentUser = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> argumentName = ArgumentCaptor.forClass(String.class);

        ResponseEntity<InputStreamResource> responseEntity = ResponseEntity.ok(new InputStreamResource(TestUtils.getInputStream(new ClassPathResource("lake.jpg"))));

        Mockito.when(webDavService.image(argumentHost.capture(), argumentUser.capture(), argumentName.capture()))
                .thenReturn(responseEntity);

        mockMvc.perform(get(BASEURL_IMAGE).param("host", HOST).param("user", USER).param("name", "imagename"))
                .andDo(print()).andExpect(status().isOk())
        //.andExpect(content().string(containsString("entry1")));
        ;
        assertEquals(HOST, argumentHost.getValue());
        assertEquals(USER, argumentUser.getValue());
        assertEquals("imagename", argumentName.getValue());
    }
}
