package com.onedata.jooq.pokemon.service.impl;


import com.onedata.jooq.pokemon.db.Tables;
import com.onedata.jooq.pokemon.db.tables.pojos.Pokemon;
import com.onedata.jooq.pokemon.service.PokemonService;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.onedata.jooq.pokemon.db.tables.Pokemon.POKEMON;


@Service
public class PokemonServiceImpl implements PokemonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PokemonService.class);
    @Autowired
    private DSLContext dslContext;

    public int insertPokemon(Pokemon pokemon) {
        return dslContext.insertInto(POKEMON, POKEMON.ID, POKEMON.NAME, POKEMON.TYPE, POKEMON.HP)
                .values(pokemon.getId(), pokemon.getName(), pokemon.getType(), pokemon.getHp())
                .execute();
    }


    @Override
    public Page<Pokemon> getPokemons(String name, String type, int page, int size) {

        String likeExpression = "%" + name + "%";

        Table<?> table = POKEMON;
        TableField<? extends Record, Integer> idField = POKEMON.ID;

        Condition condition = DSL.trueCondition();

        if (type != null && !type.isEmpty()) {
            condition = condition.and(POKEMON.TYPE.eq(type));
        }

        if (name != null && !name.isEmpty()) {
            condition = condition.and(POKEMON.NAME.likeIgnoreCase(likeExpression));
        }

        List<Pokemon> resultList;
        try (Cursor<Record> cursor = (Cursor<Record>) dslContext
                .selectFrom(table)
                .where(condition)
                .orderBy(idField)
                .seek(page * size)
                .fetchLazy()) {

            resultList = new ArrayList<>();
            int count = 0;

            for (Record record : cursor) {
                if (count >= size) {
                    break;
                }
                Pokemon pokemon = record.into(Pokemon.class);
                resultList.add(pokemon);
                count++;
            }

        }

        long totalCount = findCountByLikeExpression(likeExpression, type);
        return new PageImpl<>(resultList, PageRequest.of(page, size), totalCount);
    }

    private long findCountByLikeExpression(String likeExpression, String type) {
        LOGGER.debug("Finding search result count by using like expression: {}", likeExpression);
        long resultCount = 0;
        if (type != null && !type.isEmpty()) {
            resultCount = dslContext.fetchCount(
                    dslContext.select()
                            .from(POKEMON)
                            .where(Tables.POKEMON.TYPE.equalIgnoreCase(type))
                            .and(createWhereConditions(likeExpression)));
        } else {
            resultCount = dslContext.fetchCount(
                    dslContext.select()
                            .from(POKEMON)
                            .where(createWhereConditions(likeExpression)));
        }

        LOGGER.debug("Found search result count: {}", resultCount);

        return resultCount;
    }


    private Condition createWhereConditions(String likeExpression) {
        if (likeExpression == null) {
            likeExpression = "";
        }
        return POKEMON.NAME.likeIgnoreCase(likeExpression);
    }
}
