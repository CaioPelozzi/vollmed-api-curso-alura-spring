# Voll Med API 🏥

Uma API RESTful desenvolvida em Java com Spring Boot para gestão e agendamento de consultas médicas corporativas. Este projeto faz parte do escopo de estudos de desenvolvimento backend, aplicando boas práticas de mercado, segurança e qualidade de código.

## 🚀 Tecnologias e Ferramentas

* **Java 17+**
* **Spring Boot 3** (Web, Data JPA, Security, Validation)
* **Banco de Dados:** Mysql
* **Migrations:** Flyway
* **Segurança:** Autenticação e Autorização com Tokens JWT
* **Documentação:** SpringDoc OpenAPI (Swagger)
* **Testes:** JUnit 5 e Mockito
* **Gerenciador de Dependências:** Maven

## ✨ Funcionalidades

A API atende aos requisitos de uma clínica médica, oferecendo os seguintes recursos:

* **CRUD de Médicos:** Cadastro, listagem, atualização e exclusão lógica (inatividade) de médicos, incluindo validações de especialidade e CRM.
* **CRUD de Pacientes:** Gestão completa dos dados dos pacientes.
* **Agendamento de Consultas:** Lógica de agendamento com validações de regras de negócios (ex: antecedência mínima, médico disponível no horário, etc).
* **Autenticação de Usuários:** Login seguro devolvendo um token JWT para acesso aos endpoints protegidos.
* **Tratamento de Erros Customizado:** Respostas padronizadas para erros de validação (400 Bad Request) usando `ExceptionHandler` e regras de negócio.

## 📁 Estrutura e Arquitetura

O projeto foi estruturado buscando separar bem as responsabilidades, isolando o domínio da aplicação da camada de infraestrutura:

* `controller/`: Endpoints da API REST.
* `domain/`: Classes de modelo (Entidades), Repositórios, DTOs (Records) e serviços contendo as regras de negócio.
* `infra/`: Configurações globais da aplicação, como segurança (Filtros JWT), documentação (SpringDoc) e tratamento de exceções (ControllerAdvice).

## 🛠️ Como Executar o Projeto

1. Clone este repositório:
   ```git clone https://github.com/CaioPelozzi/vollmed-api-curso-alura-spring```
2. Configure as variáveis de ambiente ou os dados do banco de dados no arquivo ```src/main/resources/application.properties```
3. Execute o projeto usando Maven wrapper ```./mvnw spring-boot:run```

## 📚 Documentação da API
Com a aplicação rodando, você pode acessar a interface interativa do Swagger para visualizar todos os endpoints e testar as requisições diretamente pelo navegador:

Swagger UI: ```http://localhost:8081/swagger-ui.html```

OpenAPI JSON: ```http://localhost:8081/v3/api-docs```   
## 🧪 Testes Automatizados
A aplicação conta com testes de integração para garantir a integridade dos Controllers e Repositories. Para rodá-los:   

```./mvnw test```
