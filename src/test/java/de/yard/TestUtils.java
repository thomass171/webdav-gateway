package de.yard;

import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class TestUtils {

    public static final String HOST = "https://26574635qhugrix53njirrw.nextcloud-hoster.com/public.php/webdav";
    public static final String USER = "znzugnuzgnu";

    public static String readFile(String path) throws IOException {
        return readFile(path, StandardCharsets.UTF_8);
    }

    public static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(new ClassPathResource(path).getFile().toPath());
        // no explicit closing of file is needed, it will be closed automatically, after the last bytes is read
        return new String(encoded, encoding);
    }

    public static String loadClassPathResourceAsString(ClassPathResource classPathResource) throws Exception {
        return new String(Files.readAllBytes(classPathResource.getFile().toPath()), Charset.forName("UTF-8"));
    }

    public static byte[] loadClassPathResource(ClassPathResource classPathResource) throws Exception {
        return Files.readAllBytes(classPathResource.getFile().toPath());
    }

    public static InputStream getInputStream(ClassPathResource classPathResource) throws Exception {
        return new FileInputStream(classPathResource.getFile());
    }
}
