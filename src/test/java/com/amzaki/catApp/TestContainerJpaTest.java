package com.amzaki.catApp;

import com.amzaki.catApp.model.Cat;
import com.amzaki.catApp.repository.CatRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for {@link CatRepository} using a real PostgreSQL database
 * spun up via Testcontainers. The container is shared across all tests in this
 * class (static field) to keep startup costs low.
 */
@Testcontainers
@DataJpaTest
class TestContainerJpaTest {

    /** Single PostgreSQL container shared by every test in the class. */
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private CatRepository catRepository;

    @Test
    void save_shouldPersistCat() {
        Cat cat = new Cat("Whiskers");

        Cat saved = catRepository.save(cat);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Whiskers");
    }

    @Test
    void findAll_shouldReturnAllPersistedCats() {
        catRepository.save(new Cat("Tom"));
        catRepository.save(new Cat("Jerry"));

        List<Cat> cats = catRepository.findAll();

        assertThat(cats).hasSize(2)
                .extracting(Cat::getName)
                .containsExactlyInAnyOrder("Tom", "Jerry");
    }

    @Test
    void deleteAll_shouldRemoveAllCats() {
        catRepository.save(new Cat("Shadow"));
        catRepository.deleteAll();

        assertThat(catRepository.findAll()).isEmpty();
    }
}
