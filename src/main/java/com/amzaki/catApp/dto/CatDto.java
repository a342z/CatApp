package com.amzaki.catApp.dto;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class CatDto {

    private String name;

    public CatDto() {}

    public CatDto(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
