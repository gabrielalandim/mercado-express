# 🛒 Mercado Express API

API REST desenvolvida em **Spring Boot** para gerenciamento de produtos de um mercado *express* (meias, produtos de limpeza, frutas, etc.), com persistência em banco de dados **Oracle**, utilizando **Lombok** e o padrão **HATEOAS (nível de maturidade 3 de Richardson)**.

Projeto desenvolvido para o **Checkpoint 4 – Parte 1 (API e Deploy)** da disciplina de **Java Advanced**, do curso de **Tecnologia em Análise e Desenvolvimento de Sistemas (TDS)** — FIAP.

> *"Quem ouve, esquece. Quem vê, lembra. Quem faz, aprende."*

---

## 📌 Sobre o Projeto

O sistema simula o backend de um mercado *express*, permitindo o cadastro, consulta, atualização e remoção de produtos através de uma API RESTful. Toda a comunicação é feita via HTTP, seguindo os princípios REST e retornando links de navegação (HATEOAS) junto às respostas, o que permite ao cliente descobrir dinamicamente as ações disponíveis a partir dos recursos retornados.

Os dados são persistidos em um banco de dados **Oracle (ORACLE_FIAP / SQL Developer)**, na tabela `TDS_TB_mercado`.

---

## 🧰 Tecnologias Utilizadas

| Tecnologia | Finalidade |
|---|---|
| **Java 21** | Linguagem de programação |
| **Spring Boot 4.1.0** | Framework principal da aplicação |
| **Spring Web (MVC)** | Criação dos endpoints REST |
| **Spring Data JPA** | Persistência e abstração de acesso ao banco de dados |
| **Spring HATEOAS** | Geração de links hipermídia nas respostas (maturidade nível 3) |
| **Lombok** | Redução de código boilerplate (getters, setters, etc.) |
| **Oracle JDBC (ojdbc11)** | Driver de conexão com o banco Oracle |
| **Banco de Dados Oracle** | Ambiente `ORACLE_FIAP` (SQL Developer) |
| **Maven** | Gerenciador de dependências e build |
| **Docker** | Containerização da aplicação para deploy |
| **Postman / Insomnia** | Ferramentas utilizadas para testar os endpoints |

---

## 🗂️ Estrutura do Projeto

```
mercado-express/
├── src/
│   ├── main/
│   │   ├── java/br/com/fiap/mercadoexpress/
│   │   │   ├── controllers/
│   │   │   │   └── ProdutoController.java     # Camada de endpoints REST
│   │   │   ├── dtos/
│   │   │   │   └── ProdutoRequestDTO.java     # DTO para entrada de dados (POST/PUT/PATCH)
│   │   │   ├── models/
│   │   │   │   └── Produto.java               # Entidade JPA (mapeada para TDS_TB_mercado)
│   │   │   ├── repositories/
│   │   │   │   └── ProdutoRepository.java     # Interface JpaRepository
│   │   │   ├── services/
│   │   │   │   └── ProdutoService.java        # Regras de negócio e persistência
│   │   │   └── MercadoexpressApplication.java # Classe principal (main)
│   │   └── resources/
│   │       └── application.properties         # Configuração do datasource Oracle
│   └── test/
│       └── java/br/com/fiap/mercadoexpress/
│           └── MercadoexpressApplicationTests.java
├── Dockerfile                                  # Build multi-stage para deploy
├── pom.xml                                     # Dependências Maven
└── README.md
```

### Arquitetura em camadas

```
Postman/Insomnia (JSON)  <--- HTTP --->  Controller  --->  Service  --->  Repository  --->  Banco Oracle (TDS_TB_mercado)
```

- **Controller**: recebe as requisições HTTP, aplica os status codes corretos e monta os links HATEOAS.
- **Service**: contém a regra de negócio; recebe o produto, envia para uma lista intermediária em memória (conforme solicitado no enunciado) e depois delega ao Repository o *commit* no banco.
- **Repository**: interface `JpaRepository`, responsável pela comunicação direta com o banco Oracle via JPA/Hibernate.
- **Model**: entidade `Produto`, anotada com `@Entity` e mapeada para a tabela `TDS_TB_mercado`, estendendo `RepresentationModel<Produto>` do Spring HATEOAS para permitir a adição de links.

---

## 🗄️ Modelagem do Banco de Dados

**Tabela:** `TDS_TB_mercado`

| Coluna | Tipo (Java) | Descrição |
|---|---|---|
| `id` | `Long` | Identificador único, gerado automaticamente (`IDENTITY`) |
| `nome` | `String` | Nome do produto |
| `tipo` | `String` | Tipo/categoria do produto |
| `setor` | `String` | Setor do mercado onde o produto se encontra |
| `tamanho` | `String` | Tamanho/porção do produto |
| `preco` | `Double` | Preço do produto |

A tabela é criada/atualizada automaticamente pelo Hibernate através da configuração `spring.jpa.hibernate.ddl-auto=update`.

---

## ⚙️ Configuração (application.properties)

```properties
server.port=8082

spring.datasource.url=jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.OracleDialect
```

> ⚠️ As credenciais de acesso ao Oracle (`username`/`password`) devem ser substituídas pelas credenciais fornecidas pela FIAP (RM e senha padrão do ambiente `ORACLE_FIAP`).

A aplicação sobe, por padrão, na porta **8082**, conforme exigido no enunciado.

---

## ▶️ Como Executar Localmente

### Pré-requisitos
- Java 21 instalado
- Maven (ou usar o wrapper `mvnw` incluso no projeto)
- Acesso ao banco Oracle `ORACLE_FIAP`

### Passos

```bash
# Clonar o repositório
git clone <link-do-repositorio>
cd mercado-express

# Configurar usuário e senha do Oracle em src/main/resources/application.properties

# Rodar a aplicação com o Maven Wrapper
./mvnw spring-boot:run
```

A API ficará disponível em:
```
http://localhost:8082/mercado
```

### Executando via Docker

O projeto já possui um `Dockerfile` multi-stage (build com Maven + execução com JRE 21 Alpine), utilizado inclusive para o deploy em nuvem:

```bash
docker build -t mercado-express .
docker run -p 8082:8080 mercado-express
```

---

## 🌐 Deploy

A aplicação está publicada e disponível em produção através da plataforma **Render**, utilizando o `Dockerfile` do projeto:

🔗 **URL de acesso:** [https://mercado-express-xta8.onrender.com](https://mercado-express-xta8.onrender.com)

Endpoint principal em produção:
```
https://mercado-express-xta8.onrender.com/mercado
```

> ℹ️ Por se tratar do plano gratuito do Render, a instância pode "dormir" após um período de inatividade — a primeira requisição após um tempo ocioso pode demorar alguns segundos a mais para responder enquanto o serviço é reativado.

---

## 📡 Endpoints da API

Base URL (local): `http://localhost:8082/mercado`
Base URL (produção): `https://mercado-express-xta8.onrender.com/mercado`

| Método | Endpoint | Descrição | Status de sucesso |
|---|---|---|---|
| `GET` | `/mercado` | Lista todos os produtos cadastrados | `200 OK` / `204 No Content` |
| `GET` | `/mercado/{id}` | Busca um produto específico pelo ID | `200 OK` / `404 Not Found` |
| `POST` | `/mercado` | Cadastra um novo produto | `201 Created` |
| `PUT` | `/mercado/{id}` | Atualiza **todos** os campos de um produto existente | `200 OK` / `404 Not Found` |
| `PATCH` | `/mercado/{id}` | Atualiza **parcialmente** os campos de um produto existente | `200 OK` / `404 Not Found` |
| `DELETE` | `/mercado/{id}` | Remove um produto pelo ID | `204 No Content` / `404 Not Found` |

---

### 🔹 GET `/mercado` — Listar todos os produtos

**Requisição:**
```http
GET /mercado HTTP/1.1
Host: localhost:8082
```

**Resposta (200 OK):**
```json
[
  {
    "id": 1,
    "nome": "Maçã Gala",
    "tipo": "Fruta",
    "setor": "Hortifruti",
    "tamanho": "Unidade",
    "preco": 1.99,
    "links": [
      {
        "rel": "self",
        "href": "http://localhost:8082/mercado/1"
      }
    ]
  },
  {
    "id": 2,
    "nome": "Detergente Neutro",
    "tipo": "Limpeza",
    "setor": "Higiene",
    "tamanho": "500ml",
    "preco": 3.49,
    "links": [
      {
        "rel": "self",
        "href": "http://localhost:8082/mercado/2"
      }
    ]
  }
]
```

Caso não haja nenhum produto cadastrado, a API retorna `204 No Content`.

---

### 🔹 GET `/mercado/{id}` — Buscar produto por ID

**Requisição:**
```http
GET /mercado/1 HTTP/1.1
Host: localhost:8082
```

**Resposta (200 OK):**
```json
{
  "id": 1,
  "nome": "Maçã Gala",
  "tipo": "Fruta",
  "setor": "Hortifruti",
  "tamanho": "Unidade",
  "preco": 1.99,
  "links": [
    {
      "rel": "self",
      "href": "http://localhost:8082/mercado/1"
    },
    {
      "rel": "Lista de Produtos",
      "href": "http://localhost:8082/mercado"
    }
  ]
}
```

Este é o exemplo de consulta citado no enunciado: ao consultar o `endpoint` `/mercado` via GET, a API consulta a tabela `TDS_TB_mercado` no Oracle e retorna as informações do produto solicitado.

Caso o ID não exista, a resposta é `404 Not Found`.

---

### 🔹 POST `/mercado` — Cadastrar novo produto

**Requisição:**
```http
POST /mercado HTTP/1.1
Host: localhost:8082
Content-Type: application/json
```

```json
{
  "nome": "Sabonete Líquido",
  "tipo": "Higiene",
  "setor": "Limpeza",
  "tamanho": "250ml",
  "preco": 7.90
}
```

**Resposta (201 Created):**
```json
{
  "id": 3,
  "nome": "Sabonete Líquido",
  "tipo": "Higiene",
  "setor": "Limpeza",
  "tamanho": "250ml",
  "preco": 7.90,
  "links": [
    {
      "rel": "self",
      "href": "http://localhost:8082/mercado/3"
    }
  ]
}
```

> Observação: internamente, o `ProdutoService` recebe o objeto, adiciona a uma lista temporária em memória e, em seguida, envia ao repositório para o *commit* no banco Oracle, conforme solicitado no enunciado.

---

### 🔹 PUT `/mercado/{id}` — Atualizar produto (todos os campos)

**Requisição:**
```http
PUT /mercado/3 HTTP/1.1
Host: localhost:8082
Content-Type: application/json
```

```json
{
  "nome": "Sabonete Líquido Erva Doce",
  "tipo": "Higiene",
  "setor": "Limpeza",
  "tamanho": "250ml",
  "preco": 8.50
}
```

**Resposta (200 OK):**
```json
{
  "id": 3,
  "nome": "Sabonete Líquido Erva Doce",
  "tipo": "Higiene",
  "setor": "Limpeza",
  "tamanho": "250ml",
  "preco": 8.50,
  "links": [
    {
      "rel": "self",
      "href": "http://localhost:8082/mercado/3"
    }
  ]
}
```

O `PUT` exige o envio de **todos os campos**, pois substitui integralmente o recurso.

---

### 🔹 PATCH `/mercado/{id}` — Atualizar produto (parcial)

**Requisição:**
```http
PATCH /mercado/3 HTTP/1.1
Host: localhost:8082
Content-Type: application/json
```

```json
{
  "preco": 6.99
}
```

**Resposta (200 OK):**
```json
{
  "id": 3,
  "nome": "Sabonete Líquido Erva Doce",
  "tipo": "Higiene",
  "setor": "Limpeza",
  "tamanho": "250ml",
  "preco": 6.99,
  "links": [
    {
      "rel": "self",
      "href": "http://localhost:8082/mercado/3"
    }
  ]
}
```

Diferente do `PUT`, o `PATCH` atualiza **apenas os campos enviados** no corpo da requisição, mantendo os demais valores já existentes.

---

### 🔹 DELETE `/mercado/{id}` — Remover produto

**Requisição:**
```http
DELETE /mercado/3 HTTP/1.1
Host: localhost:8082
```

**Resposta:** `204 No Content`

Caso o ID informado não exista no banco, a API retorna `404 Not Found`.

---

## 🔗 HATEOAS (Maturidade Nível 3)

A API implementa o modelo de maturidade REST **nível 3**, no qual, além dos dados do recurso, cada resposta traz **links de navegação** (`self`, relações para outros recursos, etc.), permitindo que o cliente descubra as ações disponíveis dinamicamente, sem depender de documentação externa fixa.

Isso é feito através da entidade `Produto` estendendo `RepresentationModel<Produto>` (Spring HATEOAS) e da construção dos links no `ProdutoController`, utilizando `linkTo(methodOn(...))` para gerar URLs de forma segura e desacoplada.

---

## 🧪 Testando com Postman / Insomnia

1. Importe/crie uma nova requisição para cada endpoint listado acima.
2. Configure o endereço base como `http://localhost:8082/mercado` (ambiente local) ou `https://mercado-express-xta8.onrender.com/mercado` (produção).
3. Para os métodos `POST`, `PUT` e `PATCH`, defina o header `Content-Type: application/json` e envie o corpo (`Body > raw > JSON`) conforme os exemplos acima.
4. Verifique o `status code` retornado e o corpo da resposta, incluindo os links HATEOAS.

---

## 👤 Autores

Projeto desenvolvido por alunas do curso de **Tecnologia em Análise e Desenvolvimento de Sistemas (TDS)** — FIAP, turma 2TDS:

| Nome | RM |
|---|---|
| Maria Gabriela Landim Severo | 565146 |
| Samara Porto Souza | 559072 |

**IDE utilizada:** IntelliJ IDEA

---

## 📄 Licença

Projeto acadêmico desenvolvido para fins educacionais na disciplina de Java Advanced — FIAP.
