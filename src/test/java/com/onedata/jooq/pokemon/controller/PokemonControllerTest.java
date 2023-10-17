package com.onedata.jooq.pokemon.controller;

import com.onedata.jooq.pokemon.db.tables.pojos.Pokemon;
import com.onedata.jooq.pokemon.helper.PokemonHelper;
import com.onedata.jooq.pokemon.model.PokemonType;
import com.onedata.jooq.pokemon.service.PokemonService;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PokemonControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private DSLContext dslContext;
    @Mock
    private PokemonService pokemonService;

    private List<Pokemon> pokemonList = Collections.EMPTY_LIST;

    @BeforeEach
    void setUp() {
        pokemonList = PokemonHelper.getPokemonData();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllPokemonByTypeTest() throws Exception {
        mockMvc.perform(get("/api/pokemons").param("type", "thunder"))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @CsvSource({
            // Type, Name, Page
            ",,1",
            ",,2",
            "poison,,3",
            ",,8",
            ",,7",
            ",Nido,6"
    })
    public void testPokemonListing(String type, String name, int pageNumber) throws Exception {

        Page<Pokemon> pageResponse = new PageImpl<>(pokemonList, PageRequest.of(pageNumber, 10),
                pokemonList.size());
        when(pokemonService.getPokemons(anyString(), anyString(), anyInt(), anyInt())).thenReturn(pageResponse);
        List<Pokemon> filteredByType =pokemonList.stream().filter(x -> x.getType()
                .equalsIgnoreCase(PokemonType.POISON.name())).toList();
        pageResponse = new PageImpl<>(filteredByType, PageRequest.of(pageNumber, 10),
                filteredByType.size());
        when(pokemonService.getPokemons(anyString(), eq("poison"), anyInt(), anyInt())).thenReturn(pageResponse);

        List<Pokemon> filteredByName =pokemonList.stream()
                .filter(pokemon -> pokemon.getName().toLowerCase().contains("Nido")).toList();
        pageResponse = new PageImpl<>(filteredByName, PageRequest.of(pageNumber, 10),
                filteredByType.size());
        when(pokemonService.getPokemons(eq("Nido"), anyString(), anyInt(), anyInt())).thenReturn(pageResponse);

        when(pokemonService.getPokemons(anyString(), anyString(), anyInt(), anyInt())).thenReturn(pageResponse);
        ResultActions response = mockMvc.perform(get("/api/pokemons")
                .param("type", type)
                .param("name", name)
                .param("page", String.valueOf(pageNumber))
                .param("size", "10"));


        if(type != null && !type.isEmpty()){
            response.andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].type").value("poison"))
                    .andExpect(jsonPath("$..id").exists())
                    .andExpect(jsonPath("$..name").exists())
                    .andExpect(jsonPath("$..hp").exists());
        } else if (name != null && !name.isEmpty()) {
            response.andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].name").exists())
                    .andExpect(jsonPath("$..id").exists())
                    .andExpect(jsonPath("$..name").exists())
                    .andExpect(jsonPath("$..hp").exists());
        } else{
            response.andExpect(status().isOk())
                    .andExpect(jsonPath("$..id").exists())
                    .andExpect(jsonPath("$..name").exists())
                    .andExpect(jsonPath("$..type").exists())
                    .andExpect(jsonPath("$..hp").exists());
        }


    }
}
