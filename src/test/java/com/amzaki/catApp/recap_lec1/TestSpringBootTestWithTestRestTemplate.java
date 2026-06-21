package com.amzaki.catApp.recap_lec1;

import com.amzaki.catApp.dto.CatDto;
import com.amzaki.catApp.model.Cat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
class TestSpringBootTestWithTestRestTemplate {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void testAdd1Cat(){
        CatDto catDto = new CatDto();
        catDto.setName("tom");


        testRestTemplate.postForEntity("/api/cats", catDto, CatDto.class );

        ResponseEntity<List<CatDto>> cats = testRestTemplate.exchange("/api/cats",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CatDto>>() {
                });

        assertThat(cats.getBody()).hasSize(1);

    }

    @Test
    void testAddAnotherCat(){
        CatDto catDto = new CatDto();
        catDto.setName("not tom");

        testRestTemplate.postForEntity("/api/cats", catDto, CatDto.class );

        ResponseEntity<List<CatDto>> cats = testRestTemplate.exchange("/api/cats",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CatDto>>() {
                });

        assertThat(cats.getBody()).hasSize(1);


    }

}
