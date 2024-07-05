package com.selfcode.resource;

import com.opencsv.exceptions.CsvValidationException;
import com.selfcode.model.Data;
import com.selfcode.service.CSVService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Path("/api/v1")
public class CSVResource {

    @Inject
    CSVService csvService;

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Operation(summary = "Upload CSV file", description = "Uploads and indexes a CSV file")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "CSV file uploaded successfully"),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Response uploadCsv(@MultipartForm Data data) {
        try {
            csvService.index(new FileInputStream(data.file), data.delimiter.charAt(0));
            return Response.ok().build();
        } catch (IOException e) {
            e.getCause();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Search CSV data", description = "Searches for CSV data based on field and match criteria")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Successful retrieval of CSV data"),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public List<String> search(@QueryParam("field") String field, @QueryParam("match") String match) {
        try {
            return csvService.search(field, match);
        } catch (IOException e) {
            e.getCause();
            throw new WebApplicationException("Search failed", e);
        }
    }
}
