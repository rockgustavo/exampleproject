package com.example.exampleproject.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.exampleproject.model.entity.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
}