package com.amzaki.catApp.mapper;

import com.amzaki.catApp.dto.CatDto;
import com.amzaki.catApp.model.Cat;
import org.springframework.stereotype.Component;

@Component
public class CatMapper {

    public CatDto toDto(Cat cat) {
        return new CatDto(cat.getName());
    }

    public Cat toEntity(CatDto catDto) {
        Cat cat = new Cat();
        cat.setName(catDto.getName());
        return cat;
    }
}
