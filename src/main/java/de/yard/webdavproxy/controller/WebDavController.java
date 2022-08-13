package de.yard.webdavproxy.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.InputStream;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class WebDavController {

    public static final String BASEURL = "/api/webdav";
    public static final String BASEURL_CONTENT = BASEURL + "/content";
    public static final String BASEURL_IMAGE = BASEURL + "/image";
    private final WebDavService service;

    @CrossOrigin
    @GetMapping(BASEURL_CONTENT)
    public ResponseEntity<String> webdavContent(@RequestParam String host, @RequestParam String user) {
        return ResponseEntity.ok(service.propfind(host, user).stream().collect(Collectors.joining("\n")));
    }

    @CrossOrigin
    @GetMapping(value = BASEURL_IMAGE, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<InputStreamResource> webdavImage(@RequestParam String host, @RequestParam String user, @RequestParam String name) {
        return service.image(host,user,name);
    }
}