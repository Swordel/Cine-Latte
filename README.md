# 🎬 Cine Latte
Um cinema que também é um café.

Projeto acadêmico desenvolvido para a disciplina de Programação Orientada a Objetos, seguindo a arquitetura **MVC (Model-View-Controller)**.

O Cine Latte é um sistema web de cinema que permite a visualização de filmes em cartaz e em breve, compra de ingressos e gerenciamento do conteúdo pelo administrador.

---

## 📸 Screenshots

> *Screenshots serão adicionados em breve.*

---

## ✨ Funcionalidades

### Área do Público
- Listagem de filmes **Em Cartaz** e **Em Breve** na página inicial
- Página de sessões por filme com filtro por data e idioma (Legendado / Dublado)
- Seleção de assentos com visualização em tempo real de disponibilidade
- Escolha de tipo de ingresso (Inteira ou Meia) por assento
- Finalização da compra com escolha de forma de pagamento e nome do cliente
- Página de confirmação da compra

### Área do Administrador
- Cadastro de filmes com upload de pôster e banner
- Edição de filmes (título, sinopse, gêneros, status, classificação, duração e data de estreia)
- Exclusão de filmes (com remoção em cascata de sessões e reservas vinculadas)
- Cadastro de sessões vinculadas a filme, sala, data, horário e idioma
- Listagem de filmes por status (Em Cartaz / Em Breve)
- Listagem de reservas com detalhes expandíveis de cada pedido

---

## 📋 Regras de Negócio

### Filmes
- Filmes **Em Cartaz** exigem todos os campos obrigatórios (duração, classificação, nota, pôster e banner)
- Filmes **Em Breve** permitem classificação indefinida (`?`), duração opcional e possuem campo de data de estreia
- O título do filme é único no sistema — não é permitido cadastrar dois filmes com o mesmo nome
- Ao excluir um filme, todas as sessões e reservas vinculadas são removidas automaticamente

### Sessões
- Cada sessão é única por combinação de sala, data e horário — uma sala não pode ter duas sessões ao mesmo tempo (restrição `UNIQUE` no banco)
- Sessões são exibidas agrupadas por idioma (Legendado / Dublado) na página do filme
- Sessões iniciais são vinculadas pelo título do filme (não pelo id), tornando o schema resistente a reinicializações do banco

### Compra de Ingressos
- A soma de ingressos inteiros e meias deve ser igual à quantidade de assentos selecionados
- Não é permitido selecionar o mesmo assento mais de uma vez
- Assentos já ocupados por outra reserva são bloqueados antes da finalização
- O pagamento é simulado: toda compra é marcada automaticamente como paga
- Os dados da compra em andamento (assentos, sessão, quantidades) são armazenados na sessão HTTP e validados em cada etapa do fluxo

### Imagens
- Pôster e banner são obrigatórios no cadastro de um filme
- Cada arquivo recebe um nome único gerado com UUID para evitar colisões
- O sistema busca imagens primeiro na pasta `./uploads/` (filmes cadastrados pelo admin) e, como fallback, em `static/images/` (imagens pré-existentes no projeto)

---

## ✅ Validações Implementadas

| Onde | Validação |
|------|-----------|
| Cadastro de filme | Título duplicado capturado via `DataIntegrityViolationException` — exibe mensagem de erro sem perder os dados já preenchidos |
| Edição de filme | Mesmo tratamento do cadastro: título duplicado exibe erro e recarrega o formulário com os dados originais |
| Cadastro de sessão | Sala/data/horário duplicados capturados via `DataIntegrityViolationException` — exibe mensagem de erro no formulário |
| Seleção de assentos | Assentos ocupados marcados visualmente como indisponíveis antes da escolha |
| Compra de ingressos | Validação no `ReservaService`: total de ingressos deve igualar assentos selecionados; duplicatas de assento são detectadas via `HashSet`; ocupação verificada no banco antes de inserir |
| Fluxo de compra | Cada etapa (ingressos, pagamento) verifica se a sessão HTTP é válida e pertence à sessão do filme — impede acesso direto por URL ou troca de aba |

---

## 🛠️ Tecnologias Utilizadas

| Camada | Tecnologia |
|--------|-----------|
| Linguagem | Java 21 |
| Framework | Spring Boot 4.0.6 |
| Persistência | Spring Data JDBC |
| Template Engine | Thymeleaf |
| Banco de Dados | PostgreSQL |
| Front-end | HTML, CSS, JavaScript |
| Build | Maven |
| Deploy | Render |
| Empacotamento | JAR |

---

## 🗂️ Arquitetura

O projeto segue o padrão **MVC** com a seguinte estrutura por entidade:

```
Model (POJO) → DAO → Service → Controller → View (Thymeleaf)
```

As principais entidades são: `Filme`, `Sessao`, `Sala`, `Assento`, `Reserva` e `ReservaItem`.

---

## 🗄️ Modelo de Banco de Dados

```
filme
  ├── filme_genero
  └── sessao
        └── reserva
              └── reserva_item
                    └── assento (via FK)

sala
  └── assento
```

---

## ⚙️ Como Rodar Localmente

### Pré-requisitos
- Java 21
- Maven
- PostgreSQL

### Passo a passo

**1. Clone o repositório**
```bash
git clone https://github.com/Swordel/Cine-Latte.git
cd cine-latte
```

**2. Crie o banco de dados**
```sql
CREATE DATABASE cinelatte;
```

**3. Configure o `application.yaml`**

```yaml
server:
  port: 8081

spring:
  datasource:
    platform: postgres
    url: jdbc:postgresql://127.0.0.1:5432/cinelatte
    username: [insiraSeuUser]
    password: [insiraSuaSenha]
    driverClassName: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: create-drop

  sql:
    init:
      mode: always
      schema-locations: classpath:schema-postgresql.sql
```

**4. Execute o projeto**
```bash
mvn spring-boot:run
```

**5. Acesse no navegador**
```
http://localhost:8081
```

> O arquivo `schema-postgresql.sql` é executado automaticamente na inicialização, criando as tabelas. `CREATE TABLE IF NOT EXISTS` garante que as tabelas não sejam recriadas, e `ON CONFLICT DO NOTHING` garante que os dados iniciais não sejam duplicados — permitindo que o sistema reinicie sem perder dados cadastrados.

---

## 👩‍💻 Autora

Desenvolvido por **Gaby :D**.

