package com.example.exampleproject.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.exampleproject.model.entity.Cliente;
import com.example.exampleproject.model.entity.Pedido;
import com.example.exampleproject.model.enums.StatusPedido;
import com.example.exampleproject.model.repository.PedidoRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class PedidoRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    PedidoRepository repository;

    Cliente cliente;
    Pedido pedido;

    @BeforeEach
    public void setUp() {
        cliente = Cliente
                .builder()
                .cpf("04191877070") // CPF válido
                .nome("Cliente Teste")
                .build();
        entityManager.persist(cliente);

        pedido = Pedido
                .builder()
                .cliente(cliente)
                .dataPedido(LocalDate.now())
                .total(new BigDecimal("150.00"))
                .status(StatusPedido.REALIZADO)
                .build();
        entityManager.persist(pedido);
    }

    @Test
    @DisplayName("Deve salvar um pedido.")
    public void savePedidoTest() {
        // Cenário

        // Execução
        Pedido savedPedido = repository.save(pedido);

        // Verificação
        assertThat(savedPedido.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve buscar um pedido por ID.")
    public void findByIdTest() {
        // Cenário

        // Execução
        Optional<Pedido> foundPedido = repository.findById(pedido.getId());

        // Verificação
        assertThat(foundPedido.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Deve deletar um pedido.")
    public void deletePedidoTest() {
        // Cenário
        Pedido foundPedido = entityManager.find(Pedido.class, pedido.getId());

        // Execução
        repository.delete(foundPedido);

        // Verificação
        Pedido deletedPedido = entityManager.find(Pedido.class, pedido.getId());
        assertThat(deletedPedido).isNull();
    }
}
