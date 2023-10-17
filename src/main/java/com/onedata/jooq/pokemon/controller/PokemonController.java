package com.onedata.jooq.pokemon.controller;

import com.onedata.jooq.pokemon.db.tables.pojos.Pokemon;
import com.onedata.jooq.pokemon.model.PokemonType;
import com.onedata.jooq.pokemon.service.PokemonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PokemonController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PokemonController.class);
    @Autowired
    private PokemonService pokemonService;

    @GetMapping("/healthcheck")
    public String healthCheck() {
        return "green";
    }


    @PostMapping("/pokemon")
    public ResponseEntity<?> insertPokemon(@RequestBody Pokemon pokemon) {
        try {
            PokemonType enumType = PokemonType.valueOf(pokemon.getType().toUpperCase());
            int status = pokemonService.insertPokemon(pokemon);
            LOGGER.info("insert service status {}", status);
            if (status == 1) {
                return ResponseEntity.status(HttpStatus.OK).body("Pokemon added...");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Insert failed...");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid pokemon type: " + pokemon.getType());
        }

    }

    @GetMapping("/pokemons")
    public ResponseEntity<?> getAllPokemon(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            if (type != null && !type.isEmpty()) {
                PokemonType enumType = PokemonType.valueOf(type.toUpperCase());
            }
            return ResponseEntity.status(HttpStatus.OK).body(pokemonService.getPokemons(name, type, page, size));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid pokemon type: " + type);
        }
    }

    private List<Pokemon> createTestPokemonList() {
        List<Pokemon> pokemonList = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            Pokemon pokemon = new Pokemon();
            pokemon.setName("Pokemon" + i);
            pokemon.setType(PokemonType.FIRE.name());
            pokemonList.add(pokemon);
        }
        return pokemonList;
    }
}