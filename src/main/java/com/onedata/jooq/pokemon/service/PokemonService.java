package com.onedata.jooq.pokemon.service;

import com.onedata.jooq.pokemon.db.tables.pojos.Pokemon;
import org.springframework.data.domain.Page;

public interface PokemonService {

    int insertPokemon(Pokemon pokemon);

    Page<Pokemon> getPokemons(String name, String type, int page, int size);
}
