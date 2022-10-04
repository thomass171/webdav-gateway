package de.yard.webdavproxy.controller;

import lombok.Data;

import java.util.List;

@Data
public class PropfindResult {
    private List<String> filelist;
    private String failureMessage;

    public PropfindResult(List<String> files) {
        filelist = files;
    }

    public PropfindResult(String message) {
        failureMessage = message;
    }
}
