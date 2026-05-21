-- Tabela de filmes
CREATE TABLE IF NOT EXISTS filme (
    id            SERIAL PRIMARY KEY,
    titulo        VARCHAR(100),
    sinopse       TEXT,
    duracao       INTEGER,
    classificacao VARCHAR(5),
    nota          NUMERIC(3,1),
    imagem        VARCHAR(100),
    status        VARCHAR(10)
);

-- Tabela associativa filme x gênero
-- genero armazena o name() do Enum: "ACAO", "DRAMA", etc.
CREATE TABLE IF NOT EXISTS filme_genero (
    id_filme INTEGER REFERENCES filme(id),
    genero   VARCHAR(30),
    PRIMARY KEY (id_filme, genero)
);

-- Insere filmes apenas se a tabela estiver vazia
INSERT INTO filme (titulo, sinopse, duracao, classificacao, nota, imagem, status)
SELECT * FROM (VALUES
    ('O Diabo Veste Prada 2',
     'SINOPSE 1',
     120, '12', 7.1, 'cartazPrada.png', 'EM_CARTAZ'),

    ('Michael',
     'SINOPSE 2',
     127, '12', 7.7, 'cartazMichael.webp', 'EM_CARTAZ'),
 
    ('Mortal Kombat 2',
     'SINOPSE 3',
     116, '18', 8.1, 'cartazKombat.webp', 'EM_CARTAZ'),
 
    ('Super Mario Galaxy - O Filme',
     'SINOPSE 4',
     99, 'L', 9.0, 'cartazMario.jpeg', 'EM_CARTAZ'),
 
    ('The Amazing Digital Circus: O Último Ato',
     'SINOPSE 5',
     120, 'L', 8.6, 'cartazCircus.jpg', 'EM_CARTAZ'),
 
    ('O Mandaloriano E Grogu',
     'SINOPSE 6',
     132, '14', 9.1, 'cartazMandalorian.jpg', 'EM_CARTAZ'),
 
    ('As Ovelhas Detetives',
     'SINOPSE 7',
     109, '12', 8.4, 'cartazOvelha.webp', 'EM_CARTAZ'),
 
    ('Na Zona Cinzenta',
     'SINOPSE 8',
     98, '14', 8.3, 'cartazZona.jpg', 'EM_CARTAZ'),
 
    ('Top Gun: Ases Indomáveis - 40º Aniversário',
     'SINOPSE 9',
     110, '12', 9.3, 'cartazTOPGUN.webp', 'EM_CARTAZ'),
 
    ('Interstellar',
     'SINOPSE 10',
     169, '10', 9.5, 'cartazInterstellar.jpg', 'EM_CARTAZ'),

     ('Good Omens 3',
     'SINOPSE 11',
     90, '16', 8.2, 'breveGoodOmens.jpg', 'EM_BREVE'),
 
    ('Luigi`s Mansion - O Filme',
     'SINOPSE 12',
     90, 'L', 9.7, 'breveLuigi.jpg', 'EM_BREVE'),
 
    ('Caramelo - Um Filme Netflix',
     'SINOPSE 12',
     95, '12', 10.0, 'breveCaramelo.jpg', 'EM_BREVE'),

     ('Avengers Endgame - Reboot 2026',
     'SINOPSE 14',
     181, '12', 10.0, 'breveAvengers.webp', 'EM_BREVE'),

     ('Demon Slayer - Castelo Infinito',
     'SINOPSE 15',
     155, '18', 9.5, 'breveDemonSlayer.jpg', 'EM_BREVE'),

     ('Totoro',
     'SINOPSE 16',
     86, 'L', 9.8, 'breveTotoro.jpg', 'EM_BREVE')

) AS novos(titulo, sinopse, duracao, classificacao, nota, imagem, status)
WHERE NOT EXISTS (SELECT 1 FROM filme LIMIT 1);

-- Insere gêneros dos filmes apenas se filme_genero estiver vazia
INSERT INTO filme_genero (id_filme, genero)
SELECT * FROM (VALUES
    (1, 'COMEDIA'), (1, 'DRAMA'),
    (2, 'DRAMA'), (2, 'MUSICAL'),
    (3, 'ACAO'), (3, 'AVENTURA'),
    (4, 'ANIMACAO'), (4, 'AVENTURA'),
    (5, 'ANIMACAO'), (5, 'FICCAO_CIENTIFICA'),
    (6, 'ACAO'), (6, 'FICCAO_CIENTIFICA'),
    (7, 'COMEDIA'),
    (8, 'ACAO'),
    (9, 'ACAO'), (9, 'AVENTURA'),
    (10, 'DRAMA'), (10, 'FICCAO_CIENTIFICA'),
    (11, 'DRAMA'),
    (12, 'ANIMACAO'), (12, 'AVENTURA'),
    (13, 'COMEDIA'), (13, 'DRAMA'),
    (14, 'ACAO'), (14, 'FICCAO_CIENTIFICA'),
    (15, 'ACAO'), (15, 'ANIMACAO'),
    (16, 'FANTASIA'), (16, 'AVENTURA')
) AS fg(id_filme, genero)
WHERE NOT EXISTS (SELECT 1 FROM filme_genero LIMIT 1);