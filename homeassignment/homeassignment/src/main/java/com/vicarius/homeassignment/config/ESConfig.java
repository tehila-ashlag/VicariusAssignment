package com.vicarius.homeassignment.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ESConfig {
    @Bean
    public RestClient RestClient() {
        // URL and API key
        String serverUrl = "https://localhost:9200";
        String apiKey = "aFUxUkNJd0I5T013dEs1dmpVaTU6eXo3QkdsaXNSNm1HelBkVFlWTzRFUQ==";

        // Create the low-level client
        RestClient restClient = RestClient
                .builder(HttpHost.create(serverUrl))
                .setDefaultHeaders(new Header[]{
                        new BasicHeader("Authorization", "ApiKey " + apiKey)
                })
                .build();

        return restClient;
    }

    @Bean
    public ElasticsearchTransport ElasticsearchTransport(RestClient restClient) {
        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        return transport;
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(ElasticsearchTransport elasticsearchTransport) {
        // And create the API client
        ElasticsearchClient esClient = new ElasticsearchClient(elasticsearchTransport);
        return esClient;
    }
}

