package com.example.exampleproject.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.exampleproject.exception.PedidoNaoEncontradoException;
import com.example.exampleproject.exception.RegraNegocioException;
import com.example.exampleproject.model.entity.Cliente;
import com.example.exampleproject.model.entity.ItemPedido;
import com.example.exampleproject.model.entity.Pedido;
import com.example.exampleproject.model.entity.Produto;
import com.example.exampleproject.model.enums.StatusPedido;
import com.example.exampleproject.model.repository.ClienteRepository;
import com.example.exampleproject.model.repository.ItemsPedidoRepository;
import com.example.exampleproject.model.repository.PedidoRepository;
import com.example.exampleproject.model.repository.ProdutoRepository;
import com.example.exampleproject.rest.dto.ItemPedidoDTO;
import com.example.exampleproject.rest.dto.PedidoDTO;
import com.example.exampleproject.service.PedidoService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository repository;
    private final ClienteRepository clientesRepository;
    private final ProdutoRepository produtosRepository;
    private final ItemsPedidoRepository itemsPedidoRepository;

    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {
        Integer idCliente = dto.getCliente();
        Cliente cliente = clientesRepository
                .findById(idCliente)
                .orElseThrow(() -> new RegraNegocioException("Código de cliente inválido."));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itemsPedido = converterItems(pedido, dto.getItems());

        // Salvar pedido antes de salvar os itens
        pedido = repository.save(pedido);

        // Definir os itens do pedido após o pedido ser salvo e ter o ID
        itemsPedidoRepository.saveAll(itemsPedido);
        pedido.setItens(itemsPedido);

        // Retornar o pedido salvo
        return pedido;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return repository.findByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void atualizaStatus(Integer id, StatusPedido statusPedido) {
        repository
                .findById(id)
                .map(pedido -> {
                    pedido.setStatus(statusPedido);
                    return repository.save(pedido);
                }).orElseThrow(() -> new PedidoNaoEncontradoException());
    }

    private List<ItemPedido> converterItems(Pedido pedido, List<ItemPedidoDTO> items) {
        if (items == null || items.isEmpty()) {
            throw new RegraNegocioException("Não é possível realizar um pedido sem items.");
        }

        return items
                .stream()
                .map(dto -> {
                    Integer idProduto = dto.getProduto();
                    Produto produto = produtosRepository
                            .findById(idProduto)
                            .orElseThrow(
                                    () -> new RegraNegocioException(
                                            "Código de produto inválido: " + idProduto));

                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setQuantidade(dto.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setProduto(produto);
                    return itemPedido;
                }).collect(Collectors.toList());

    }

    public Page<Pedido> listarPedidos(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
