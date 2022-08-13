package de.yard.webdavproxy;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import de.yard.TestUtils;
import de.yard.webdavproxy.controller.WebDavService;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.junit.jupiter.api.Assertions.*;

/**
 * webdav endpoint is mocked.
 */
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 8085)
class WebDavServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebDavService webDavService;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setup() {
        WireMock.reset();
    }

    @Test
    public void testPropFind() throws Exception {

        String responseBody = TestUtils.loadClassPathResourceAsString(new ClassPathResource("response-propfind.xml"));

        HttpHeaders responseHeaders = buildHeaderFromTemplate("response-header-propfind.txt");

        WireMock.stubFor(WireMock.any(WireMock.urlEqualTo("/webdav"))
                .willReturn(aResponse().withStatus(207).withStatusMessage("HTTP/2 207").withHeaders(responseHeaders).withBody(responseBody)));

        List<String> fileList = this.webDavService.propfind("http://localhost:8085/webdav", "aa");
        assertEquals(6, fileList.size());
        // Blanks in file names should already be encoded
        assertEquals("Nextcloud%20community.jpg", fileList.get(5));
    }

    @Test
    public void testImage() throws Exception {

        byte[] responseBody = TestUtils.loadClassPathResource(new ClassPathResource("lake.jpg"));

        HttpHeaders responseHeaders = buildHeaderFromTemplate("response-header-jpg.txt");

        WireMock.stubFor(WireMock.any(WireMock.urlEqualTo("/backend/lake.jpg"))
                .willReturn(aResponse().withStatus(200).withStatusMessage("HTTP/2 200").withHeaders(responseHeaders).withBody(responseBody)));

        ResponseEntity<InputStreamResource> response = this.webDavService.image("http://localhost:8085/backend", "aa", "/lake.jpg");
        assertNotNull(response.getBody());
    }

    @Test
    public void testImageNotFound() throws Exception {

        HttpHeaders responseHeaders = buildHeaderFromTemplate("response-header-jpg.txt");

        WireMock.stubFor(WireMock.any(WireMock.urlEqualTo("/backend/lakeXX.jpg"))
                .willReturn(aResponse().withStatus(404).withStatusMessage("HTTP/2 404").withHeaders(responseHeaders)));

        ResponseEntity<InputStreamResource> response = this.webDavService.image("http://localhost:8085/backend", "aa", "/lakeXX.jpg");
        assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    private HttpHeaders buildHeaderFromTemplate(String file) throws Exception {
        String[] responseHeadersList = TestUtils.loadClassPathResourceAsString(new ClassPathResource(file)).split("\n");

        // Hmm, duplicate header fields not possible? TODO check
        HttpHeaders responseHeaders = new HttpHeaders();
        for (String h : responseHeadersList) {
            String[] p = h.split(": ");
            responseHeaders = responseHeaders.plus(new HttpHeader(p[0], p[1]));
        }
        return responseHeaders;
    }
}

