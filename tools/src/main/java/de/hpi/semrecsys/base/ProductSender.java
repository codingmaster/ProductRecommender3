package de.hpi.semrecsys.base;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
public class ProductSender {
    private Logger log = LoggerFactory.getLogger(ProductSender.class);

    private RestTemplate restTemplate;

    private String url;

    private String customer;

    @Autowired
    public ProductSender(RestTemplateBuilder restTemplateBuilder,
                         @Value("${recommender.url}") String url,
                         @Value("${recommender.customer}") String customer
    ) {
        this.restTemplate = restTemplateBuilder.build();
        this.url = url;
        this.customer = customer;
    }

    public void sendProduct(String uid, String productJson) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        URI uri = createProductUri(uid);
        RequestEntity<String> requestEntity = new RequestEntity<>(productJson, headers, HttpMethod.POST, uri);

        try {
            this.restTemplate.exchange(requestEntity, String.class);
            log.info("POST {}", uri);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.CONFLICT) {
                uri = createProductUri(uid);
                requestEntity = new RequestEntity<>(productJson, headers, HttpMethod.POST, uri);
                this.restTemplate.exchange(requestEntity, String.class);
            } else {
                log.error("Error occurred for {} {}", uri, ex.getMessage());
                throw ex;
            }
        }
    }

    private URI createProductUri(String uid) {
        return URI.create(url + "/api/v1/products/");
    }

}
