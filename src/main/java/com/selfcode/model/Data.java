package com.selfcode.model;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import java.io.File;

public class Data {

    @FormParam("file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public File file;

    @FormParam("delimiter")
    @PartType(MediaType.TEXT_PLAIN)
    public String delimiter;
}
