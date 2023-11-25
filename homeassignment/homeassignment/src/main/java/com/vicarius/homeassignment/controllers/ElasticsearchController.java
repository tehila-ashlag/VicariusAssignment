package com.vicarius.homeassignment.controllers;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class ElasticsearchController {

    private final ElasticsearchClient esClient;
    private String lastCreatedIndexName;

    @Autowired
    public ElasticsearchController(ElasticsearchClient esClient) {
        this.esClient = esClient;
    }


    @PostMapping("/createIndex")
    public String createIndex(@RequestBody Map<String, String> requestBody) throws IOException {
        String indexName = requestBody.get("indexName");

        if (indexName == null || indexName.isEmpty()) {
            return "Index name is required in the request body.";
        }

        esClient.indices().create(c -> c
                .index(indexName)
        );

        lastCreatedIndexName = indexName;
        return "Index created: " + indexName;
    }

    @PostMapping("/createDocument")
    public String createDocument(@RequestBody Map<String, String> documentToBeCreated) throws IOException {

        IndexResponse response = esClient.index(i -> i
                .index(lastCreatedIndexName)
                .id(documentToBeCreated.get("id"))
                .document(documentToBeCreated)
        );

        return "Document created with ID: " + response.id();
    }

    @GetMapping("/getDocument/{id}")
    public String getDocumentById(@PathVariable String id) throws IOException {

        GetResponse<Object> response = esClient.get(g -> g
                        .index(this.lastCreatedIndexName)
                        .id(id),
                        Object.class
        );

        Object document=null;
        if (response.found()) {
             document = response.source();
        }

        return "Document: " + document;
    }
}