-- Adicionando um cliente
INSERT INTO cliente (id, cpf, nome) VALUES (1, '04191877070', 'Cliente Teste');

-- Adicionando pedidos
INSERT INTO pedido (id, cliente_id, data_pedido, total, status) 
VALUES (1, 1, '2024-08-01', 150.00, 'REALIZADO');

INSERT INTO pedido (id, cliente_id, data_pedido, total, status) 
VALUES (2, 1, '2024-08-05', 200.00, 'CANCELADO');