package de.yard.webdavproxy.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Util {

    public static String convertInputStreamToString(InputStreamResource inputStream) throws IOException {

        return IOUtils.toString(inputStream.getInputStream(), StandardCharsets.UTF_8);
    }
}
