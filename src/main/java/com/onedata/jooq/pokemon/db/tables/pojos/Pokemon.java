/*
 * This file is generated by jOOQ.
 */
package com.onedata.jooq.pokemon.db.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class Pokemon implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private String type;
    private Integer hp;

    public Pokemon() {
    }

    public Pokemon(Pokemon value) {
        this.id = value.id;
        this.name = value.name;
        this.type = value.type;
        this.hp = value.hp;
    }

    public Pokemon(
            Integer id,
            String name,
            String type,
            Integer hp
    ) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.hp = hp;
    }

    /**
     * Getter for <code>public.pokemon.id</code>.
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Setter for <code>public.pokemon.id</code>.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Getter for <code>public.pokemon.name</code>.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter for <code>public.pokemon.name</code>.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for <code>public.pokemon.type</code>.
     */
    public String getType() {
        return this.type;
    }

    /**
     * Setter for <code>public.pokemon.type</code>.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter for <code>public.pokemon.hp</code>.
     */
    public Integer getHp() {
        return this.hp;
    }

    /**
     * Setter for <code>public.pokemon.hp</code>.
     */
    public void setHp(Integer hp) {
        this.hp = hp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Pokemon (");

        sb.append(id);
        sb.append(", ").append(name);
        sb.append(", ").append(type);
        sb.append(", ").append(hp);

        sb.append(")");
        return sb.toString();
    }
}
