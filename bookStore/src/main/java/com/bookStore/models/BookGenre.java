package com.bookStore.models;

public enum BookGenre {
    FANTASY("Фантастика"),
    FICTION("Художественная литература"),
    DRAMA("Драма"),
    FABLE("Басня"),
    ACTION("Приключения"),
    DETECTIVE("Детектив"),
    MEMOIR("Мемуары"),
    POETRY("Поэзия"),
    HORROR("Ужасы"),
    ROMANCE("Роман"),
    THRILLER("Триллер");


    private final String label;
    private BookGenre(String label) {
        this.label = label;
    }

    public String toLabel() {
        return this.label;
    }
}

