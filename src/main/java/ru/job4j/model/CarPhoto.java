package ru.job4j.model;

import javax.persistence.*;

@Entity
@Table(name = "photo")
public class CarPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
