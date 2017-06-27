package com.example.lukasz.wypozyczalnia.models;

import java.io.Serializable;

/**
 * Created by Łukasz on 2016-12-29.
 */

public class AuthorModel implements Serializable {
    String name;
    String surname;

    public AuthorModel(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
