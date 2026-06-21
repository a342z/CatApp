package com.amzaki.catApp;

import com.amzaki.catApp.dto.CatDto;
import com.amzaki.catApp.model.Cat;
import com.amzaki.catApp.repository.CatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class TestContainerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CatRepository catRepository;

    @BeforeEach
    void setUp() {
        catRepository.deleteAll();
    }

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");


    @Test
    void testAddOneCat(){
        CatDto  catDto = new CatDto();
        catDto.setName("yummi");

        restTemplate.postForEntity("/api/cats", catDto, Cat.class);

        ResponseEntity<List<CatDto>> cats = restTemplate.exchange("/api/cats",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CatDto>>() {
                });

    assertThat(cats.getBody().size()).isEqualTo(1);
    }

    @Test
    void testAddAnotherCat(){
        CatDto  catDto = new CatDto();
        catDto.setName("yummi");

        restTemplate.postForEntity("/api/cats", catDto, Cat.class);

        ResponseEntity<List<CatDto>> cats = restTemplate.exchange("/api/cats",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CatDto>>() {
                });

        assertThat(cats.getBody().size()).isEqualTo(1);
    }

}
