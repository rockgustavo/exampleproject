package com.example.exampleproject.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.exampleproject.model.entity.Pedido;
import com.example.exampleproject.model.repository.ClienteRepository;
import com.example.exampleproject.model.repository.ItemsPedidoRepository;
import com.example.exampleproject.model.repository.PedidoRepository;
import com.example.exampleproject.model.repository.ProdutoRepository;
import com.example.exampleproject.service.impl.PedidoServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class PedidoServicePaginationTest {

    @MockBean
    private PedidoRepository pedidoRepository;

    @MockBean
    private ClienteRepository clienteRepository;

    @MockBean
    private ProdutoRepository produtoRepository;

    @MockBean
    private ItemsPedidoRepository itemsPedidoRepository;

    private PedidoServiceImpl pedidoService;

    @BeforeEach
    public void setUp() {
        this.pedidoService = new PedidoServiceImpl(pedidoRepository, clienteRepository, produtoRepository,
                itemsPedidoRepository);
    }

    @Test
    @DisplayName("Deve retornar uma lista de pedidos paginados")
    public void listarPedidosPaginadosTest() {
        // Cenário
        Pageable pageable = PageRequest.of(0, 5); // Primeira página com 5 elementos
        Pedido pedido1 = new Pedido();
        pedido1.setId(1);

        Pedido pedido2 = new Pedido();
        pedido2.setId(2);

        Page<Pedido> page = new PageImpl<>(List.of(pedido1, pedido2), pageable, 2);

        when(pedidoRepository.findAll(pageable)).thenReturn(page);

        // Execução
        Page<Pedido> result = pedidoService.listarPedidos(pageable);

        // Verificação
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        verify(pedidoRepository, times(1)).findAll(pageable);
    }
}