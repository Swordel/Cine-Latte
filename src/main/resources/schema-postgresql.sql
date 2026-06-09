-- Tabela de filmes
CREATE TABLE IF NOT EXISTS filme (
    id            SERIAL PRIMARY KEY,
    titulo        VARCHAR(100),
    sinopse       TEXT,
    duracao       INTEGER,
    classificacao VARCHAR(5),
    nota          NUMERIC(3,1),
    imagem        VARCHAR(200),   -- nome do arquivo do pôster (gerado com UUID)
    banner        VARCHAR(200),   -- nome do arquivo do banner (gerado com UUID)
    status_filme  VARCHAR(10)
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
    idioma    VARCHAR(10)
);

-- Reserva = uma compra (agrupa vários assentos)
CREATE TABLE IF NOT EXISTS reserva (
    id          SERIAL PRIMARY KEY,
    sessao_id   INTEGER REFERENCES sessao(id),
    dt_compra   TIMESTAMP DEFAULT NOW(),
    valor_total NUMERIC(8,2)
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

-- Insere filmes apenas se a tabela estiver vazia
INSERT INTO filme (titulo, sinopse, duracao, classificacao, nota, imagem, banner, status_filme)
SELECT * FROM (VALUES

    ('BTS Arirang World Tour',
     'SINOPSE 1',
     180, '14', 8.3, 'cartazBTS.webp', 'bannerBTS.webp', 'EM_CARTAZ'),

    ('O Diabo Veste Prada 2',
     'SINOPSE 2',
     120, '12', 7.1, 'cartazPrada.png', 'bannerPrada.webp', 'EM_CARTAZ'),

    ('Michael',
     'SINOPSE 3',
     127, '12', 7.7, 'cartazMichael.webp', 'bannerMichael.png', 'EM_CARTAZ'),

    ('Mestres do Universo',
     'SINOPSE 4',
     123, '14', 9.4, 'cartazMestres.jpg', 'bannerMestres.webp', 'EM_CARTAZ'),
 
    ('Super Mario Galaxy - O Filme',
     'SINOPSE 5',
     99, 'L', 9.0, 'cartazMario.jpeg', 'bannerMario.webp', 'EM_CARTAZ'),
 
    ('The Amazing Digital Circus: O Último Ato',
     'SINOPSE 6',
     120, 'L', 8.6, 'cartazCircus.jpg', 'bannerCircus.png', 'EM_CARTAZ'),
 
    ('O Mandaloriano E Grogu',
     'SINOPSE 7',
     132, '14', 9.1, 'cartazMandalorian.jpg', 'bannerMandalorian.webp', 'EM_CARTAZ'),

    ('Obsessão',
     'SINOPSE 8',
     108, '18', 8.1, 'cartazObs.jpg', NULL, 'EM_CARTAZ'),
 
    ('Interstellar',
     'SINOPSE 9',
     169, '10', 9.5, 'cartazInterstellar.jpg', 'bannerInterstellar.webp', 'EM_CARTAZ'),

     ('Good Omens 3',
     'SINOPSE 10',
     90, '16', 8.2, 'breveGoodOmens.jpg', NULL, 'EM_BREVE'),
 
    ('Luigi`s Mansion - O Filme',
     'SINOPSE 11',
     90, 'L', 9.7, 'breveLuigi.jpg', NULL, 'EM_BREVE'),
 
    ('Caramelo - Um Filme Netflix',
     'SINOPSE 12',
     95, '12', 10.0, 'breveCaramelo.jpg', NULL, 'EM_BREVE'),

     ('Avengers Endgame - Reboot 2026',
     'SINOPSE 13',
     181, '12', 10.0, 'breveAvengers.webp', NULL, 'EM_BREVE'),

     ('Demon Slayer - Castelo Infinito',
     'SINOPSE 14',
     155, '18', 9.5, 'breveDemonSlayer.jpg', NULL, 'EM_BREVE'),

     ('Totoro',
     'SINOPSE 15',
     86, 'L', 9.8, 'breveTotoro.jpg', NULL, 'EM_BREVE')

) AS novos(titulo, sinopse, duracao, classificacao, nota, imagem, banner, status_filme)
WHERE NOT EXISTS (SELECT 1 FROM filme LIMIT 1);

-- Insere gêneros dos filmes apenas se filme_genero estiver vazia
INSERT INTO filme_genero (id_filme, genero)
SELECT * FROM (VALUES
    (1, 'MUSICAL'), 
    (2, 'DRAMA'), (2, 'COMEDIA'),
    (3, 'DRAMA'), (3, 'MUSICAL'),
    (4, 'ACAO'), (4, 'FANTASIA'),
    (5, 'ANIMACAO'), (5, 'AVENTURA'),
    (6, 'ANIMACAO'), (6, 'FICCAO_CIENTIFICA'),
    (7, 'ACAO'), (7, 'FICCAO_CIENTIFICA'),
    (8, 'TERROR'),
    (9, 'DRAMA'), (9, 'FICCAO_CIENTIFICA'),
    (10, 'DRAMA'),
    (11, 'ANIMACAO'), (11, 'AVENTURA'),
    (12, 'COMEDIA'), (12, 'DRAMA'),
    (13, 'ACAO'), (13, 'FICCAO_CIENTIFICA'),
    (14, 'ACAO'), (14, 'ANIMACAO'),
    (15, 'FANTASIA'), (15, 'AVENTURA'), (15, 'ANIMACAO')
) AS fg(id_filme, genero)
WHERE NOT EXISTS (SELECT 1 FROM filme_genero LIMIT 1);

-- Salas 
-- No meu cinema só tem 2 salas 
INSERT INTO sala (nome)
SELECT * FROM (VALUES 
    ('Sala 1'), ('Sala 2')
) AS s(nome)
WHERE NOT EXISTS (SELECT 1 FROM sala LIMIT 1);

-- Assentos são gerados pelo SalaService.java no startup da aplicação

-- Sessões (inseridas apenas se a tabela estiver vazia)
INSERT INTO sessao (filme_id, sala_id, dt_sessao, horario, idioma)
SELECT * FROM (VALUES
    (1, 1, DATE '2026-05-27', TIME '14:00', 'LEGENDADO'),
    (1, 1, DATE '2026-05-27', TIME '17:30', 'DUBLADO'),
    (1, 2, DATE '2026-05-27', TIME '20:00', 'LEGENDADO'),
    (1, 1, DATE '2026-05-28', TIME '15:00', 'DUBLADO'),
    (1, 1, DATE '2026-05-28', TIME '19:00', 'LEGENDADO'),
    (2, 1, DATE '2026-05-27', TIME '16:00', 'DUBLADO'),
    (3, 2, DATE '2026-05-27', TIME '19:00', 'LEGENDADO'),
    (6, 1, DATE '2026-05-31', TIME '18:00', 'LEGENDADO'),
    (6, 2, DATE '2026-05-28', TIME '15:00', 'DUBLADO'),
    (6, 2, DATE '2026-05-30', TIME '21:00', 'DUBLADO')
) AS s(filme_id, sala_id, dt_sessao, horario, idioma)
WHERE NOT EXISTS (SELECT 1 FROM sessao LIMIT 1);