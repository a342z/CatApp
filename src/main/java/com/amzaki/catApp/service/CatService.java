package com.amzaki.catApp.service;

import com.amzaki.catApp.dto.CatDto;
import com.amzaki.catApp.mapper.CatMapper;
import com.amzaki.catApp.model.Cat;
import com.amzaki.catApp.repository.CatRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatService {

    private final CatRepository catRepository;
    private final CatMapper catMapper;

    public CatService(CatRepository catRepository, CatMapper catMapper) {
        this.catRepository = catRepository;
        this.catMapper = catMapper;
    }

    public List<CatDto> getAllCats() {
        return catRepository.findAll()
                .stream()
                .map(catMapper::toDto)
                .toList();
    }

    public CatDto addCat(CatDto catDto) {
        if ("sherbini".equalsIgnoreCase(catDto.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Cat Name");
        }

        Cat cat = catMapper.toEntity(catDto);
        Cat saved = catRepository.save(cat);
        return catMapper.toDto(saved);
    }

    public void deleteAllCats() {
        catRepository.deleteAll();
    }
}
