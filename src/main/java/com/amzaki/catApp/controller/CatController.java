package com.amzaki.catApp.controller;

import com.amzaki.catApp.dto.CatDto;
import com.amzaki.catApp.service.CatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cats")
public class CatController {

    private final CatService catService;

    public CatController(CatService catService) {
        this.catService = catService;
    }

    // GET /api/cats — returns all cats
    @GetMapping
    public ResponseEntity<List<CatDto>> getAllCats() {
        return ResponseEntity.ok(catService.getAllCats());
    }

    // POST /api/cats — add a new cat
    @PostMapping
    public ResponseEntity<CatDto> addCat(@RequestBody CatDto catDto) {
        CatDto created = catService.addCat(catDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // DELETE /api/cats — delete all cats
    @DeleteMapping
    public ResponseEntity<Void> deleteAllCats() {
        catService.deleteAllCats();
        return ResponseEntity.noContent().build();
    }
}
