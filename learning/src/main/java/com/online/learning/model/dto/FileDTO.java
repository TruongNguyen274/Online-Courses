package com.online.learning.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;
import java.io.OutputStream;

@Getter
@Setter
public class FileDTO {

    private String id;
    private String name;
    private String fileName;
    private long size;
    private String path;

    private InputStream inputStream;
    private OutputStream outputStream;

}
