package com.example.exampleproject.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.exampleproject.model.entity.Pedido;
import com.example.exampleproject.model.enums.StatusPedido;
import com.example.exampleproject.rest.dto.PedidoDTO;

public interface PedidoService {

    Pedido salvar(PedidoDTO dto);

    Optional<Pedido> obterPedidoCompleto(Integer id);

    void atualizaStatus(Integer id, StatusPedido statusPedido);

    Page<Pedido> listarPedidos(Pageable pageable);
}
