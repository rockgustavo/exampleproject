CREATE DATABASE vendas;

create table cliente (
        id integer not null auto_increment,
        cpf varchar(11) not null,
        nome varchar(100) not null,
        primary key (id)
);

create table produto (
        id integer not null auto_increment,
        descricao varchar(255) not null,
        preco_unitario decimal(38,2) not null,
        primary key (id)
);

create table pedido (
    id integer not null auto_increment,
    data_pedido date,
    status enum ('CANCELADO','REALIZADO'),
    total decimal(20,2),
    cliente_id integer,
    primary key (id)
);

create table item_pedido (
    id integer not null auto_increment,
    quantidade integer,
    pedido_id integer,
    produto_id integer,
    primary key (id)
);

create table usuario (
    id varchar(255) not null,
    login varchar(255),
    nome varchar(255),
    senha varchar(255),
    primary key (id)
);

create table grupo (
    id varchar(255) not null,
    nome varchar(255),
    primary key (id)
);

create table usuario_grupo (
    id varchar(255) not null,
    id_grupo varchar(255),
    id_usuario varchar(255),
    primary key (id)
);

alter table item_pedido
    add constraint FK
    foreign key (pedido_id)
    references pedido (id);
    
alter table item_pedido
    add constraint FK
    foreign key (produto_id)
    references produto (id);
    
alter table pedido
    add constraint FK
    foreign key (cliente_id)
    references cliente (id);
    
alter table usuario_grupo
    add constraint FK
    foreign key (id_grupo)
    references grupo (id);
    
alter table usuario_grupo
    add constraint FK
    foreign key (id_usuario)
    references usuario (id);