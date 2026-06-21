package com.amzaki.catApp.recap_lec1;

import com.amzaki.catApp.model.Cat;
import com.amzaki.catApp.repository.CatRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@ActiveProfiles("dev")
class TestSpringBootTest {

    @Autowired
    private CatRepository catRepository;

    @Test
    void testAdd1Cat(){
        Cat cat1 = new Cat();
        cat1.setName("tom");

        catRepository.save(cat1);
        List<Cat> catList = catRepository.findAll();

        assertThat(catList).hasSize(1);
    }

    @Test
    void testAddAnotherCat(){
        Cat cat1 = new Cat();
        cat1.setName("not tom");

        catRepository.save(cat1);
        List<Cat> catList = catRepository.findAll();

        assertThat(catList).hasSize(1);
    }

}
