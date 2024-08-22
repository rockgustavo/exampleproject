package com.example.exampleproject.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.exampleproject.model.entity.Cliente;
import com.example.exampleproject.model.entity.Pedido;
import com.example.exampleproject.model.enums.StatusPedido;
import com.example.exampleproject.rest.controller.PedidoController;
import com.example.exampleproject.rest.dto.AtualizacaoStatusPedidoDTO;
import com.example.exampleproject.rest.dto.ItemPedidoDTO;
import com.example.exampleproject.rest.dto.PedidoDTO;
import com.example.exampleproject.service.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = PedidoController.class) // Somente o PedidoController será carregado
@AutoConfigureMockMvc(addFilters = false)
public class PedidoControllerTest {

        static String PEDIDO_API = "/api/pedidos";

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private PedidoService pedidoService;

        @Test
        @WithMockUser(username = "user", roles = { "ADMIN" })
        public void deveSalvarPedidoComSucesso() throws Exception {
                PedidoDTO pedidoDTO = PedidoDTO.builder()
                                .cliente(1)
                                .total(BigDecimal.valueOf(100))
                                .items(Arrays.asList(ItemPedidoDTO
                                                .builder()
                                                .produto(1) // ID do produto no item
                                                .quantidade(1)
                                                .build()))
                                .build();

                Pedido pedidoSalvo = Pedido.builder()
                                .id(1)
                                .build();

                BDDMockito.given(pedidoService.salvar(Mockito.any(PedidoDTO.class))).willReturn(pedidoSalvo);

                String JSON = new ObjectMapper().writeValueAsString(pedidoDTO);

                MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                                .post(PEDIDO_API)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(JSON);

                System.out.println(JSON);

                mockMvc
                                .perform(request)
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$").value(1)); // Espera o ID como 1

        }

        @Test
        @WithMockUser(username = "user", roles = { "ADMIN" }) // Simula um usuário autenticado
        public void deveRetornarPedidoPorId() throws Exception {
                // cenario (given)
                Pedido pedido = Pedido.builder()
                                .id(1)
                                .cliente(Cliente.builder()
                                                .id(1)
                                                .nome("Cliente Teste")
                                                .cpf("000.000.000-00")
                                                .build())
                                .total(BigDecimal.valueOf(200))
                                .dataPedido(LocalDate.now())
                                .status(StatusPedido.REALIZADO)
                                .itens(null)
                                .build();

                BDDMockito.given(pedidoService.obterPedidoCompleto(1)).willReturn(Optional.of(pedido));

                // execucao (when)
                MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                                .get(PEDIDO_API.concat("/" + 1))
                                .accept(MediaType.APPLICATION_JSON);

                mockMvc
                                .perform(request)
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("codigo").value(1))
                                .andExpect(jsonPath("nomeCliente").value("Cliente Teste"));
        }

        @Test
        @WithMockUser(username = "user", roles = { "ADMIN" })
        public void deveAtualizarStatusDoPedidoComSucesso() throws Exception {
                AtualizacaoStatusPedidoDTO dto = new AtualizacaoStatusPedidoDTO();
                dto.setNovoStatus("CANCELADO");

                // Simula o comportamento do serviço
                Mockito.doNothing().when(pedidoService).atualizaStatus(Mockito.anyInt(),
                                Mockito.any(StatusPedido.class));

                String json = new ObjectMapper().writeValueAsString(dto);

                MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                                .patch(PEDIDO_API.concat("/1"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(json);

                mockMvc
                                .perform(request)
                                .andExpect(status().isNoContent());
        }

        @Test
        @WithMockUser(username = "user", roles = { "ADMIN" })
        public void deveRetornar404QuandoPedidoNaoForEncontrado() throws Exception {
                // Simula o comportamento do serviço retornando Optional.empty()
                BDDMockito.given(pedidoService.obterPedidoCompleto(Mockito.anyInt())).willReturn(Optional.empty());

                MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                                .get(PEDIDO_API.concat("/1"))
                                .accept(MediaType.APPLICATION_JSON);

                mockMvc
                                .perform(request)
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.errors[0]").value("Pedido não encontrado."));
        }

        @Test
        @WithMockUser(username = "user", roles = { "ADMIN" })
        public void deveRetornar400QuandoDadosInvalidos() throws Exception {
                PedidoDTO pedidoDTO = PedidoDTO.builder()
                                .cliente(null) // Cliente é obrigatório
                                .total(BigDecimal.valueOf(100))
                                .build();

                String json = new ObjectMapper().writeValueAsString(pedidoDTO);

                MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                                .post(PEDIDO_API)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(json);

                mockMvc
                                .perform(request)
                                .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(username = "user", roles = { "ADMIN" })
        public void deveRetornar400QuandoStatusInvalido() throws Exception {
                AtualizacaoStatusPedidoDTO dto = new AtualizacaoStatusPedidoDTO();
                dto.setNovoStatus("INVALID_STATUS"); // Status inválido

                String json = new ObjectMapper().writeValueAsString(dto);

                MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                                .patch(PEDIDO_API.concat("/1"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(json);

                mockMvc
                                .perform(request)
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.errors[0]").value("Status inválido!"));
        }

}
