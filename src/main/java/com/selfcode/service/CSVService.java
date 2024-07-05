package com.selfcode.service;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class CSVService {

    @Inject
    RestClient restClient;

    @Transactional
    public void index(InputStream csvInputStream, char delimiter) throws IOException, CsvValidationException {
        CSVParser parser = new CSVParserBuilder().withSeparator(delimiter).build();
        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(csvInputStream)).withCSVParser(parser).build()) {
            String[] headers = reader.readNext();
            Map<String, List<String>> dataMap = new HashMap<>();
            for (String header : headers) {
                dataMap.put(header, new ArrayList<>());
            }

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                for (int i = 0; i < headers.length; i++) {
                    dataMap.get(headers[i]).add(nextLine[i]);
                }
            }

            JsonObject json = new JsonObject();
            for (Map.Entry<String, List<String>> entry : dataMap.entrySet()) {
                json.put(entry.getKey(), entry.getValue());
            }
            indexDocument(json);
        }
    }

    private void indexDocument(JsonObject json) throws IOException {
        Request request = new Request("PUT", "/csv/doc/" + 0);
        request.setJsonEntity(json.encode());
        restClient.performRequest(request);
    }

    public List<String> search(String field, String match) throws IOException {
        Request request = new Request("GET", "/csv/_search");
        JsonObject termJson = new JsonObject().put(field, match);
        JsonObject matchJson = new JsonObject().put("match", termJson);
        JsonObject queryJson = new JsonObject().put("query", matchJson);
        request.setJsonEntity(queryJson.encode());

        Response response = restClient.performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());

        JsonObject json = new JsonObject(responseBody);
        JsonArray hits = json.getJsonObject("hits").getJsonArray("hits");
        List<String> results = new ArrayList<>(hits.size());
        for (int i = 0; i < hits.size(); i++) {
            JsonObject hit = hits.getJsonObject(i);
            JsonObject source = hit.getJsonObject("_source");
            if (source.containsKey(field)) {
                results.add(source.getString(field));
            }
        }
        return results;
    }
}
