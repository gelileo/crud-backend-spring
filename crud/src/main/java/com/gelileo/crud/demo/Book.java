package com.gelileo.crud.demo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
}

/*
create table if not exists public.book
(
    author_id bigint
        constraint fkklnrv3weler2ftkweewlky958
            references public.author,
    id        bigserial
        primary key,
    name      varchar(255)
);
 */