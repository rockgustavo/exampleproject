package com.example.exampleproject.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.exampleproject.model.entity.ItemPedido;

public interface ItemsPedidoRepository extends JpaRepository<ItemPedido, Integer> {

}
