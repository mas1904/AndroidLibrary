package com.example.lukasz.wypozyczalnia.models;

import java.util.Date;

/**
 * Created by Łukasz on 2017-01-05.
 */
public class LendDates {
    Date lend;

    public Date getLend() {
        return lend;
    }

    public void setLend(Date lend) {
        this.lend = lend;
    }

    public Date getTerm() {
        return term;
    }

    public void setTerm(Date term) {
        this.term = term;
    }

    public LendDates(Date lend, Date term) {

        this.lend = lend;
        this.term = term;
    }

    Date term;
}
