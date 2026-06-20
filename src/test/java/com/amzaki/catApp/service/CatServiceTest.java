package com.amzaki.catApp.service;

import com.amzaki.catApp.dto.CatDto;
import com.amzaki.catApp.mapper.CatMapper;
import com.amzaki.catApp.model.Cat;
import com.amzaki.catApp.repository.CatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatServiceTest {

    @Mock
    private CatRepository catRepository;

    @Mock
    private CatMapper catMapper;

    @InjectMocks
    private CatService catService;

    private Cat cat;
    private CatDto catDto;

    @BeforeEach
    void setUp() {
        cat = new Cat("Whiskers");
        cat.setId(1L);
        catDto = new CatDto("Whiskers");
    }

    // --- getAllCats ---

    @Test
    void getAllCats_shouldReturnMappedDtoList() {
        when(catRepository.findAll()).thenReturn(List.of(cat));
        when(catMapper.toDto(cat)).thenReturn(catDto);

        List<CatDto> result = catService.getAllCats();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Whiskers");
        verify(catRepository).findAll();
        verify(catMapper).toDto(cat);
    }

    @Test
    void getAllCats_shouldReturnEmptyList_whenNoCats() {
        when(catRepository.findAll()).thenReturn(List.of());

        List<CatDto> result = catService.getAllCats();

        assertThat(result).isEmpty();
    }

    // --- addCat ---

    @Test
    void addCat_shouldSaveAndReturnDto() {
        when(catMapper.toEntity(catDto)).thenReturn(cat);
        when(catRepository.save(cat)).thenReturn(cat);
        when(catMapper.toDto(cat)).thenReturn(catDto);

        CatDto result = catService.addCat(catDto);

        assertThat(result.getName()).isEqualTo("Whiskers");
        verify(catRepository).save(cat);
    }

    @Test
    void addCat_shouldThrow_whenNameIsSherbini() {
        CatDto badDto = new CatDto("sherbini");

        assertThatThrownBy(() -> catService.addCat(badDto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Bad Cat Name");

        verify(catRepository, never()).save(any());
    }

    @Test
    void addCat_shouldThrow_whenNameIsSherbiniCaseInsensitive() {
        CatDto badDto = new CatDto("SHERBINI");

        assertThatThrownBy(() -> catService.addCat(badDto))
                .isInstanceOf(ResponseStatusException.class);

        verify(catRepository, never()).save(any());
    }

    // --- deleteAllCats ---

    @Test
    void deleteAllCats_shouldCallRepositoryDeleteAll() {
        catService.deleteAllCats();

        verify(catRepository).deleteAll();
    }
}
