-- Tabela de filmes
CREATE TABLE IF NOT EXISTS filme (
    id            SERIAL PRIMARY KEY,
    titulo        VARCHAR(100) UNIQUE,
    sinopse       TEXT,
    duracao       INTEGER,
    classificacao VARCHAR(5),
    imagem        VARCHAR(200),   -- nome do arquivo do pôster (gerado com UUID)
    banner        VARCHAR(200),   -- nome do arquivo do banner (gerado com UUID)
    status_filme  VARCHAR(10),
    data_estreia  DATE
);

-- Tabela associativa filme x gênero
-- genero armazena o name() do Enum: "ACAO", "DRAMA", etc.
CREATE TABLE IF NOT EXISTS filme_genero (
    id_filme INTEGER REFERENCES filme(id),
    genero   VARCHAR(30),
    PRIMARY KEY (id_filme, genero)
);

-- Tabela de salas
CREATE TABLE IF NOT EXISTS sala (
    id   SERIAL PRIMARY KEY,
    nome VARCHAR(50)
);

-- Tabela de assentos físicos da sala
CREATE TABLE IF NOT EXISTS assento (
    id       SERIAL PRIMARY KEY,
    sala_id  INTEGER REFERENCES sala(id),
    fileira  VARCHAR(2),
    numero   INTEGER,
    tipo     VARCHAR(20)
);

-- Tabela de sessões (filme + sala + data + horário + idioma)
CREATE TABLE IF NOT EXISTS sessao (
    id        SERIAL PRIMARY KEY,
    filme_id  INTEGER REFERENCES filme(id),
    sala_id   INTEGER REFERENCES sala(id),
    dt_sessao DATE,
    horario   TIME,
    idioma    VARCHAR(10),
    CONSTRAINT unique_sala_tempo UNIQUE (sala_id, dt_sessao, horario)
);

-- Reserva = uma compra (agrupa vários assentos)
CREATE TABLE IF NOT EXISTS reserva (
    id SERIAL PRIMARY KEY,
    sessao_id INTEGER REFERENCES sessao(id),
    dt_compra TIMESTAMP DEFAULT NOW(),
    valor_total NUMERIC(8,2),
    forma_pagamento VARCHAR(10),
    pago BOOLEAN,
    nome_cliente VARCHAR(100)
);

-- ReservaItem = cada assento dentro de uma compra
CREATE TABLE IF NOT EXISTS reserva_item (
    id             SERIAL PRIMARY KEY,
    reserva_id     INTEGER REFERENCES reserva(id),
    assento_id     INTEGER REFERENCES assento(id),
    tipo_ingresso  VARCHAR(10),
    valor          NUMERIC(6,2)
);

-- == DADOS INICIAIS ==
-- Os filmes pré-existentes usam imagens que já estão em static/images/

-- Insere os filmes UM POR UM. Se o título já existir, ele pula o filme e vai para o próximo.
INSERT INTO filme (titulo, sinopse, duracao, classificacao, imagem, banner, status_filme, data_estreia)
VALUES
    ('BTS Arirang World Tour', 'SINOPSE 1', 180, '14', 'cartazBTS.webp', 'bannerBTS.webp', 'EM_CARTAZ', NULL),
    ('O Diabo Veste Prada 2', 'SINOPSE 2', 120, '12', 'cartazPrada.png', 'bannerPrada.webp', 'EM_CARTAZ', NULL),
    ('Michael', 'SINOPSE 3', 127, '12', 'cartazMichael.webp', 'bannerMichael.png', 'EM_CARTAZ', NULL),
    ('Mestres do Universo', 'SINOPSE 4', 123, '14', 'cartazMestres.jpg', 'bannerMestres.webp', 'EM_CARTAZ', NULL),
    ('Super Mario Galaxy - O Filme', 'SINOPSE 5', 99, 'L', 'cartazMario.jpeg', 'bannerMario.webp', 'EM_CARTAZ', NULL),
    ('KPop Demon Hunters', 'SINOPSE 6', 120, 'L', 'cartazKpop.jpg', 'bannerKpop.png', 'EM_CARTAZ', NULL),
    ('O Mandaloriano E Grogu', 'SINOPSE 7', 132, '14', 'cartazMandalorian.jpg', 'bannerMandalorian.webp', 'EM_CARTAZ', NULL),
    ('Obsessão', 'SINOPSE 8', 108, '18', 'cartazObs.jpg', NULL, 'EM_CARTAZ', NULL),
    ('Interstellar', 'SINOPSE 9', 169, '10', 'cartazInterstellar.jpg', 'bannerInterstellar.webp', 'EM_CARTAZ', NULL),
    ('Good Omens 3', 'SINOPSE 10', 90, '16', 'breveGoodOmens.jpg', NULL, 'EM_BREVE', DATE '2026-07-27'),
    ('Luigi`s Mansion - O Filme', 'SINOPSE 11', 90, 'L', 'breveLuigi.jpg', NULL, 'EM_BREVE', DATE '2026-08-27'),
    ('Caramelo - Um Filme Netflix', 'SINOPSE 12', 95, '12', 'breveCaramelo.jpg', NULL, 'EM_BREVE', DATE '2026-09-27'),
    ('Avengers Endgame - Reboot 2026', 'SINOPSE 13', 181, '12', 'breveAvengers.webp', NULL, 'EM_BREVE', DATE '2026-10-27'),
    ('Demon Slayer - Castelo Infinito', 'SINOPSE 14', 155, '18', 'breveDemonSlayer.jpg', NULL, 'EM_BREVE', DATE '2026-11-27'),
    ('Totoro', 'SINOPSE 15', 86, 'L', 'breveTotoro.jpg', NULL, 'EM_BREVE', DATE '2026-12-27')
ON CONFLICT (titulo) DO NOTHING;

-- Gêneros dos filmes 
-- ON CONFLICT aqui também para evitar duplicar os gêneros dos filmes que já existiam
INSERT INTO filme_genero (id_filme, genero)
SELECT f.id, fg.genero
FROM (VALUES
    ('BTS Arirang World Tour',                    'MUSICAL'), 
    ('O Diabo Veste Prada 2',                     'DRAMA'), ('O Diabo Veste Prada 2', 'COMEDIA'),
    ('Michael',                                   'DRAMA'), ('Michael', 'MUSICAL'),
    ('Mestres do Universo',                       'ACAO'), ('Mestres do Universo', 'FANTASIA'),
    ('Super Mario Galaxy - O Filme',              'ANIMACAO'), ('Super Mario Galaxy - O Filme', 'AVENTURA'),
    ('KPop Demon Hunters',                        'ANIMACAO'), ('KPop Demon Hunters', 'FANTASIA'),
    ('O Mandaloriano E Grogu',                    'ACAO'), ('O Mandaloriano E Grogu', 'FICCAO_CIENTIFICA'),
    ('Obsessão',                                  'TERROR'),
    ('Interstellar',                              'DRAMA'), ('Interstellar', 'FICCAO_CIENTIFICA'),
    ('Good Omens 3',                              'DRAMA'),
    ('Luigi`s Mansion - O Filme',                 'ANIMACAO'), ('Luigi`s Mansion - O Filme', 'AVENTURA'),
    ('Caramelo - Um Filme Netflix',               'COMEDIA'), ('Caramelo - Um Filme Netflix', 'DRAMA'),
    ('Avengers Endgame - Reboot 2026',            'ACAO'), ('Avengers Endgame - Reboot 2026', 'FICCAO_CIENTIFICA'),
    ('Demon Slayer - Castelo Infinito',           'ACAO'), ('Demon Slayer - Castelo Infinito', 'ANIMACAO'),
    ('Totoro',                                    'FANTASIA'), ('Totoro', 'AVENTURA'), ('Totoro', 'ANIMACAO')
) AS fg(titulo_filme, genero)
JOIN filme f ON f.titulo = fg.titulo_filme
ON CONFLICT (id_filme, genero) DO NOTHING;

-- Salas 
-- No meu cinema só tem 2 salas 
INSERT INTO sala (nome)
SELECT * FROM (VALUES 
    ('Sala 1'), ('Sala 2')
) AS s(nome)
WHERE NOT EXISTS (SELECT 1 FROM sala LIMIT 1);

-- Assentos são gerados pelo SalaService.java no startup da aplicação

-- Sessões dos filmes
-- ON CONFLICT caso o banco tente reinserir sessões idênticas que já existem
INSERT INTO sessao (filme_id, sala_id, dt_sessao, horario, idioma)
SELECT f.id, s.sala_id, s.dt_sessao, s.horario, s.idioma
FROM (VALUES
    ('BTS Arirang World Tour',                    1, DATE '2026-06-15', TIME '09:00', 'LEGENDADO'),
    ('BTS Arirang World Tour',                    1, DATE '2026-06-15', TIME '12:00', 'LEGENDADO'),
    ('O Diabo Veste Prada 2',                     1, DATE '2026-06-15', TIME '15:00', 'LEGENDADO'),
    ('Mestres do Universo',                       1, DATE '2026-06-15', TIME '18:00', 'LEGENDADO'),
    ('Super Mario Galaxy - O Filme',              1, DATE '2026-06-15', TIME '21:00', 'LEGENDADO'),
    ('BTS Arirang World Tour',                    2, DATE '2026-06-15', TIME '09:30', 'DUBLADO'),
    ('BTS Arirang World Tour',                    2, DATE '2026-06-15', TIME '12:30', 'DUBLADO'),
    ('Michael',                                   2, DATE '2026-06-15', TIME '15:30', 'DUBLADO'),
    ('Mestres do Universo',                       2, DATE '2026-06-15', TIME '18:30', 'DUBLADO'),
    ('Super Mario Galaxy - O Filme',              2, DATE '2026-06-15', TIME '21:30', 'DUBLADO'),
    ('BTS Arirang World Tour',                    1, DATE '2026-06-16', TIME '21:00', 'LEGENDADO'),
    ('BTS Arirang World Tour',                    1, DATE '2026-06-16', TIME '18:00', 'LEGENDADO'),
    ('O Diabo Veste Prada 2',                     1, DATE '2026-06-16', TIME '12:00', 'LEGENDADO'),
    ('Mestres do Universo',                       1, DATE '2026-06-16', TIME '15:00', 'LEGENDADO'),
    ('Super Mario Galaxy - O Filme',              1, DATE '2026-06-16', TIME '09:00', 'LEGENDADO'),
    ('BTS Arirang World Tour',                    2, DATE '2026-06-16', TIME '21:30', 'DUBLADO'),
    ('BTS Arirang World Tour',                    2, DATE '2026-06-16', TIME '15:30', 'DUBLADO'),
    ('Michael',                                   2, DATE '2026-06-16', TIME '18:30', 'DUBLADO'),
    ('Mestres do Universo',                       2, DATE '2026-06-16', TIME '12:30', 'DUBLADO'),
    ('Super Mario Galaxy - O Filme',              2, DATE '2026-06-16', TIME '09:30', 'DUBLADO'),
    ('BTS Arirang World Tour',                    1, DATE '2026-06-17', TIME '21:00', 'LEGENDADO'),
    ('KPop Demon Hunters',                        1, DATE '2026-06-17', TIME '16:00', 'LEGENDADO'),
    ('O Mandaloriano E Grogu',                    2, DATE '2026-06-17', TIME '14:00', 'LEGENDADO'),
    ('Obsessão',                                  1, DATE '2026-06-18', TIME '15:00', 'LEGENDADO'),
    ('Interstellar',                              2, DATE '2026-06-18', TIME '20:00', 'LEGENDADO')
) AS s(titulo_filme, sala_id, dt_sessao, horario, idioma)
JOIN filme f ON f.titulo = s.titulo_filme
ON CONFLICT (sala_id, dt_sessao, horario) DO NOTHING;