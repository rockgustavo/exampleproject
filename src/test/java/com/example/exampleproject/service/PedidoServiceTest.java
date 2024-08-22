package com.example.exampleproject.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.exampleproject.exception.PedidoNaoEncontradoException;
import com.example.exampleproject.exception.RegraNegocioException;
import com.example.exampleproject.model.entity.Cliente;
import com.example.exampleproject.model.entity.Pedido;
import com.example.exampleproject.model.entity.Produto;
import com.example.exampleproject.model.enums.StatusPedido;
import com.example.exampleproject.model.repository.ClienteRepository;
import com.example.exampleproject.model.repository.ItemsPedidoRepository;
import com.example.exampleproject.model.repository.PedidoRepository;
import com.example.exampleproject.model.repository.ProdutoRepository;
import com.example.exampleproject.rest.dto.ItemPedidoDTO;
import com.example.exampleproject.rest.dto.PedidoDTO;
import com.example.exampleproject.service.impl.PedidoServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class PedidoServiceTest {

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
    @DisplayName("Deve salvar um pedido com sucesso")
    public void salvarPedidoTest() {
        // Cenário
        Cliente cliente = Cliente
                .builder()
                .id(1)
                .build();

        Produto produto = Produto
                .builder()
                .id(1)
                .descricao("Produto Teste")
                .preco(new BigDecimal("100.00"))
                .build();

        ItemPedidoDTO itemDTO = ItemPedidoDTO
                .builder()
                .produto(1) // ID do produto no item
                .quantidade(1)
                .build();

        PedidoDTO pedidoDTO = PedidoDTO
                .builder()
                .cliente(1) // ID do cliente no DTO
                .items(Arrays.asList(itemDTO)) // Adicionando a lista de itens no DTO
                .build();

        Pedido pedidoSalvo = Pedido
                .builder()
                .id(1) // Certifique-se de que o ID está sendo setado
                .cliente(cliente)
                .dataPedido(LocalDate.now())
                .total(new BigDecimal("100.00"))
                .status(StatusPedido.REALIZADO)
                .build();

        BDDMockito.given(clienteRepository.findById(1)).willReturn(Optional.of(cliente));
        BDDMockito.given(produtoRepository.findById(1)).willReturn(Optional.of(produto));
        BDDMockito.given(pedidoRepository.save(any(Pedido.class))).willReturn(pedidoSalvo);

        // Execução
        Pedido pedido = pedidoService.salvar(pedidoDTO);

        // Verificação
        assertThat(pedido.getId()).isEqualTo(1);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve lançar erro ao salvar pedido com cliente inválido")
    public void salvarPedidoClienteInvalidoTest() {
        // Cenário
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setCliente(1);

        BDDMockito.given(clienteRepository.findById(1)).willReturn(Optional.empty());

        // Execução & Verificação
        assertThrows(RegraNegocioException.class, () -> pedidoService.salvar(pedidoDTO));
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve retornar pedido completo com sucesso")
    public void obterPedidoCompletoTest() {
        // Cenário
        Pedido pedido = new Pedido();
        pedido.setId(1);

        BDDMockito.given(pedidoRepository.findByIdFetchItens(1)).willReturn(Optional.of(pedido));

        // Execução
        Optional<Pedido> pedidoCompleto = pedidoService.obterPedidoCompleto(1);

        // Verificação
        assertThat(pedidoCompleto.isPresent()).isTrue();
        assertThat(pedidoCompleto.get().getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("Deve lançar erro ao atualizar status de pedido inexistente")
    public void atualizarStatusPedidoNaoEncontradoTest() {
        // Cenário
        when(pedidoRepository.findById(1)).thenReturn(Optional.empty());

        // Execução & Verificação
        assertThrows(PedidoNaoEncontradoException.class, () -> pedidoService.atualizaStatus(1, StatusPedido.REALIZADO));
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve atualizar o status do pedido com sucesso")
    public void atualizarStatusPedidoTest() {
        // Cenário
        Pedido pedido = new Pedido();
        pedido.setId(1);
        pedido.setStatus(StatusPedido.REALIZADO);

        when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        // Execução
        pedidoService.atualizaStatus(1, StatusPedido.CANCELADO);

        // Verificação
        assertThat(pedido.getStatus()).isEqualTo(StatusPedido.CANCELADO);
        verify(pedidoRepository, times(1)).save(pedido);
    }
}