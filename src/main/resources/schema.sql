CREATE TABLE IF NOT EXISTS tb_solicitacao_grafo(
id SERIAL primary key,
vertice_origem int,
data_solicitacao date,
representacao_grafo varchar(6)
);