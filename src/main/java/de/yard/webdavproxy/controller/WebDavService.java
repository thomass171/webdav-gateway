package de.yard.webdavproxy.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.jackrabbit.webdav.MultiStatus;
import org.apache.jackrabbit.webdav.MultiStatusResponse;
import org.apache.jackrabbit.webdav.client.methods.HttpPropfind;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class WebDavService {

    public List<String> propfind(String host, String user) {
        log.debug("listOfFilesFromWebDav");

        try {

            URI uri = new URI(host);
            HttpPropfind propfind = new HttpPropfind(uri, 0, 1);

            HttpResponse rsp = execute(propfind, user);

            List<String> files = new ArrayList<>();
            MultiStatus multiStatus = propfind.getResponseBodyAsMultiStatus(rsp);
            MultiStatusResponse[] responses = multiStatus.getResponses();

            log.debug("{} responses", responses.length);
            for (MultiStatusResponse response : responses) {
                log.debug("response={}", response.getHref());
                String name = response.getHref();
                if (name.startsWith("/public.php/webdav/")) {
                    name = name.replace("/public.php/webdav/", "");
                }
                files.add(name);
            }
            return files;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResponseEntity<InputStreamResource> image(String host, String user, String name) {
        log.debug("image host={}, user={}, name={}", host, user, name);

        try {

            URI uri = new URI(host + "/" + name);
            HttpUriRequest getter = RequestBuilder.get()
                    .setUri(uri)
                    .build();

            HttpResponse rsp = execute(getter, user);
            log.debug("rsp={}", rsp);

            if (rsp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                return ResponseEntity.status(rsp.getStatusLine().getStatusCode()).build();
            }
            MediaType contentType = MediaType.IMAGE_JPEG;
            return ResponseEntity.ok()
                    .contentType(contentType)
                    .body(new InputStreamResource(rsp.getEntity().getContent()));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private HttpResponse execute(HttpUriRequest request, String user) throws IOException {

        String cred_user = user;
        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(cred_user, "")
        );

        CloseableHttpClient client = HttpClientBuilder.create()
                .setDefaultCredentialsProvider(provider)
                .build();

        log.debug("posting request {}", request);
        HttpResponse rsp = client.execute(request);
        return rsp;
    }
}
