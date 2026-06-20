package com.amzaki.catApp.mapper;

import com.amzaki.catApp.dto.CatDto;
import com.amzaki.catApp.model.Cat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CatMapperTest {

    private CatMapper catMapper;

    @BeforeEach
    void setUp() {
        catMapper = new CatMapper();
    }

    @Test
    void toDto_shouldMapNameFromCat() {
        Cat cat = new Cat("Whiskers");
        cat.setId(1L);

        CatDto dto = catMapper.toDto(cat);

        assertThat(dto.getName()).isEqualTo("Whiskers");
    }

    @Test
    void toEntity_shouldMapNameFromDto() {
        CatDto dto = new CatDto("Luna");

        Cat cat = catMapper.toEntity(dto);

        assertThat(cat.getName()).isEqualTo("Luna");
        assertThat(cat.getId()).isNull(); // id is not mapped from DTO
    }
}
