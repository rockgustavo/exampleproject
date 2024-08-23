package com.example.exampleproject.rest.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.exampleproject.exception.PedidoNaoEncontradoException;
import com.example.exampleproject.exception.StatusInvalidoException;
import com.example.exampleproject.model.entity.ItemPedido;
import com.example.exampleproject.model.entity.Pedido;
import com.example.exampleproject.model.enums.StatusPedido;
import com.example.exampleproject.rest.dto.AtualizacaoStatusPedidoDTO;
import com.example.exampleproject.rest.dto.InformacaoItemPedidoDTO;
import com.example.exampleproject.rest.dto.InformacoesPedidoDTO;
import com.example.exampleproject.rest.dto.PedidoDTO;
import com.example.exampleproject.service.PedidoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService service;

    @PostMapping
    @ResponseStatus(CREATED)
    public Integer save(@Valid @RequestBody PedidoDTO dto) {
        Pedido pedido = service.salvar(dto);
        return pedido.getId();
    }

    @GetMapping("{id}")
    public InformacoesPedidoDTO getById(@PathVariable Integer id) {
        return service
                .obterPedidoCompleto(id)
                .map(p -> converter(p))
                .orElseThrow(() -> new PedidoNaoEncontradoException());
    }

    @GetMapping
    public List<Pedido> listPosts(Pageable pageable) {
        return service.listarPedidos(pageable).getContent();
    }

    @PatchMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateStatus(@PathVariable Integer id,
            @RequestBody AtualizacaoStatusPedidoDTO dto) {
        try {
            service.atualizaStatus(id, StatusPedido.valueOf(dto.getNovoStatus()));
        } catch (IllegalArgumentException e) {
            throw new StatusInvalidoException();
        }
    }

    private InformacoesPedidoDTO converter(Pedido pedido) {
        return InformacoesPedidoDTO
                .builder()
                .codigo(pedido.getId())
                .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .cpf(pedido.getCliente().getCpf())
                .nomeCliente(pedido.getCliente().getNome())
                .total(pedido.getTotal())
                .status(pedido.getStatus().name())
                .items(converter(pedido.getItens()))
                .build();
    }

    private List<InformacaoItemPedidoDTO> converter(List<ItemPedido> itens) {
        if (CollectionUtils.isEmpty(itens)) {
            return Collections.emptyList();
        }
        return itens.stream().map(
                item -> InformacaoItemPedidoDTO
                        .builder().descricaoProduto(item.getProduto().getDescricao())
                        .precoUnitario(item.getProduto().getPreco())
                        .quantidade(item.getQuantidade())
                        .build())
                .collect(Collectors.toList());
    }
}