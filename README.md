# Payment Engine API

<p align="left">
  <img src="https://img.shields.io/badge/Language-Java-orange?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java">
  <img src="https://img.shields.io/badge/Framework-Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Database-PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL">
  <img src="https://img.shields.io/badge/Messaging-RabbitMQ-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white" alt="RabbitMQ">
  <img src="https://img.shields.io/badge/Container-Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker">
  <img src="https://img.shields.io/badge/Payment-Stripe-008CDD?style=for-the-badge&logo=stripe&logoColor=white" alt="Stripe">
</p>

---

## 🚀 Tecnologias e Ferramentas

* **Linguagem Principal:** `Java 17+`
* **Framework:** `Spring Boot` (Spring Web, Spring Data JPA, Spring AMQP)
* **Banco de Dados:** `PostgreSQL` (rodando via Docker)
* **Mensageria & Filas:** `RabbitMQ` (rodando via Docker)
* **Gateway de Pagamento:** `Stripe`
* **Ferramenta de Teste de API:** `Postman`
* **IDE Recomendada:** `IntelliJ IDEA (Utilizado por mim)`, `Eclipse` ou `VS Code`


## 📌 Funcionalidades

* **Gestão de Produtos:** Cadastro de itens com controle rigoroso de estoque e valores via API.
* **Processamento de Pedidos:** Criação de pedidos com baixa automática no estoque do produto associado.
* **Arquitetura Orientada a Eventos:** Publicação automática de mensagens no `RabbitMQ` assim que um pedido é gerado.
* **Consumo Assíncrono de Mensagens:** Leitura assíncrona das filas pelo consumidor para simular o envio de notificações/e-mails de confirmação ao cliente sem travar o fluxo principal.
* **Resiliência e Tratamento de Erros:** Mecanismos de segurança para evitar loops infinitos em caso de falhas ou mensagens corrompidas no broker.


## ⚡ Diferenciais Técnicos (Boas Práticas)

* **Ambiente Padronizado com Docker:** Utilização de containers para subir a infraestrutura (`PostgreSQL` e `RabbitMQ`) de forma rápida, isolada e sem complicações locais.
* **Desacoplamento de Sistemas:** Uso de mensageria assíncrona com RabbitMQ para separar a regra de negócio principal (pedido) do fluxo secundário (notificações).
* **Persistência Relacional Avançada:** Mapeamento objeto-relacional robusto utilizando Spring Data JPA e PostgreSQL.
* **Segurança no Consumo de Filas:** Implementação de validações contra valores nulos (`null checks`) no listener para prevenir exceções não tratadas e o efeito *poison pill* (redelivery infinito).
* **Organização e Limpeza de Código:** Utilização de DTOs baseados em `Record` para transferência segura e imutável de dados.
* **Separação de Responsabilidades:** Arquitetura limpa dividida em camadas bem definidas (`Controller`, `Service`, `Repository`, `Consumer` e `DTO`).


## 🌐 Como Configurar o Projeto para iniciar o Spring Boot

Para iniciar o projeto do zero pelo navegador através do **Spring Initializr** (`start.spring.io`), utilize as seguintes configurações e dependências:

### ⚙️ Configurações Básicas (Metadata)

* **Project:** `Maven`
* **Language:** `Java`
* **Spring Boot:** `4.x.x` (versão estável mais recente, sem ser SNAPSHOT)
* **Group:** `com.system`
* **Artifact:** `payment-engine`
* **Name:** `payment-engine`
* **Description:** `Sistema de processamento de pagamentos com Spring Boot, PostgreSQL e RabbitMQ`
* **Package name:** `com.system.payment_engine`
* **Packaging:** `Jar`
* **Java:** `17` (ou superior)

---

### 📦 Dependências Essenciais (`Add Dependencies`)

Adicione exatamente estas 4 dependências fundamentais para o funcionamento do projeto:

* **Spring Web:** Essencial para a criação dos `Controllers` e manipulação das requisições HTTP REST (testadas via Postman).
* **Spring Data JPA:** Responsável pelo mapeamento objeto-relacional (ORM) e integração com o Hibernate para gerenciar o banco de dados.
* **PostgreSQL Driver:** Driver JDBC para permitir a conexão e comunicação da aplicação com o banco PostgreSQL.
* **Spring Rabbit:** Biblioteca do Spring AMQP para o gerenciamento de filas, publicação e consumo de mensagens assíncronas com o RabbitMQ.

> Após selecionar todas as opções e dependências, basta clicar em **Generate** para baixar o arquivo `.zip`, descompactá-lo e abri-lo diretamente na sua IDE (No meu caso, IntelliJ IDEA).

---

## 🗄️ Estrutura do Banco de Dados

O banco de dados relacional armazena as entidades de produtos e pedidos gerenciadas automaticamente pelo Hibernate/JPA.

```sql
CREATE DATABASE payment_engine;

-- As tabelas (ex: tb_product, tb_order) são geradas e estruturadas automaticamente pelo Spring Boot via JPA.
```

## ▶️ Como Executar
Pré-requisitos
- Java JDK 17 ou superior instalado.
- Docker e Docker Compose instalados e rodando na máquina.
- Uma IDE Java de sua preferência.

```Bash
# 1. Clone o repositório
git clone [https://github.com/kauankoenigkan/payment-engine.git](https://github.com/kauankoenigkan/payment-engine.git)

# 2. Acesse a pasta do projeto
cd payment-engine
```

## Configuração do Ambiente (Docker)
Inicie os serviços de infraestrutura (PostgreSQL e RabbitMQ) utilizando containers:

```bash
# Subindo o PostgreSQL via Docker
docker run --name postgres-db -e POSTGRES_DB=payment_engine -e POSTGRES_USER=root -e POSTGRES_PASSWORD=root -p 5432:5432 -d postgres

# Subindo o RabbitMQ (com painel de gerenciamento habilitado) via Docker
docker run --name rabbitmq-broker -p 5672:5672 -p 15672:15672 -d rabbitmq:3-management
```
*Certifique-se de que o arquivo src/main/resources/application.properties aponta para os serviços rodando nos containers:*

```Properties
spring.datasource.url=jdbc:postgresql://localhost:5432/payment_engine
spring.datasource.username=root
spring.datasource.password=root

spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672

server.port=8081
```

---

## ▶️ Execução

1. Importe o projeto na sua IDE.
2. Execute a classe principal PaymentEngineApplication.
3. Utilize o Postman para cadastrar produtos, efetuar pedidos e acompanhar os eventos assíncronos no terminal e no painel do RabbitMQ (http://localhost:15672).

## 📂 Estrutura do Projeto

```Plaintext
src/main/java/com/system/payment_engine/
├── controller/
│   ├── OrderController.java
│   └── ProductController.java
├── consumer/
│   └── NotificationConsumer.java
├── dto/
│   ├── CreateProductDTO.java
│   ├── OrderDTO.java
│   └── OrderIDMessageDTO.java
├── model/
│   ├── Order.java
│   └── Product.java
└── repository/
    ├── OrderRepository.java
    └── ProductRepository.java
```

## 👨‍💻 Autor

Desenvolvido por Kauan Koenigkan.
