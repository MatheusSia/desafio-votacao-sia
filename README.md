# 🗳️ Desafio Votação

Este projeto é uma aplicação Java Spring Boot desenvolvida como parte de um desafio técnico.  
O objetivo é implementar um sistema de votação com regras específicas, utilizando **Spring Boot**, **Maven** e **Banco de Dados**.

---

## ⚙️ Tecnologias Utilizadas

- Java 17
- Spring Boot
- Maven
- H2 Database (modo file, persistente)
- Bean Validation (Jakarta Validation)
- JUnit / Mockito (para testes)
- Actuator (para métricas de performance)

---

## 📖 Visão Geral da Arquitetura

A aplicação foi desenvolvida seguindo uma arquitetura em camadas, típica de projetos Spring Boot:

[ Controller ] -> [ Service ] -> [ Repository ] -> [ Database ]

- ```Controller``` → expõe os endpoints REST da aplicação e recebe as requisições HTTP.
- ```Service``` → contém as regras de negócio, como abertura de sessões, registro de votos e cálculo de resultados.
- ```Repository``` → camada de acesso ao banco de dados, utilizando Spring Data JPA.
- ```Entity``` → representa as entidades do domínio (Pauta, Sessão, Voto, Resultado).

Além disso:

- O Actuator é usado para expor métricas de performance.
- O H2 Database em modo file garante a persistência dos dados entre restarts.
- Há uma camada de integração fake (CPF Client) para simular a validação de CPFs em sistemas externos.

---

## 📂 Estrutura do Projeto

- `src/main/java` → Código da aplicação
- `src/test/java` → Testes unitários
- `resources/application.properties` → Configurações da aplicação

Classe principal para execução:
com.coop.cooperative.CooperativeApplication

---

## ▶️ Como Rodar o Projeto

### 1. Clonar o repositório
```bash
  git clone https://github.com/MatheusSia/desafio-votacao-sia.git
  cd desafio-votacao
```

### 2. Build do projeto
```bash
  mvn clean install
```

### 3. Executar a aplicação
```bash
  mvn spring-boot:run
```
Ou executar diretamente a classe principal pela sua IDE (IntelliJ/Eclipse):
```bash
  com.coop.cooperative.CooperativeApplication
```

---

## 🗄️ Banco de Dados H2

A aplicação utiliza H2 Database em modo file (persistente).

Isso significa que os dados não se perdem após reiniciar a aplicação, pois são gravados em um arquivo local (```./data/cooperative.mv.db```).

### Acessar console H2

Após iniciar a aplicação, acesse:
http://localhost:8080/h2-console

Credenciais padrão:
- JDBC URL: jdbc:h2:file:./data/cooperative
- Usuário: sa
- Senha: (em branco)

---

## 🌐 Endpoints Principais

- POST /pautas → Criar nova pauta (Exemplo de entrada)
```bash
  {
    "titulo": "Teste",
    "descricao": "Teste para ver se a tarefa bonus 2 esta com tudo correto"
  }
```

- POST /pautas/{id}/sessoes → Abrir sessão de votos para pauta (Exemplo de entrada)
```bash
  {
    "minutos": 3
  }
```
Caso não seja colocado nenhum parametro no body, ele abre como padrão por 1 minuto.
Regras de validação:
- `id` deve ser maior que zero.
- `minutos` (quando informado) deve estar entre `1` e `10080`.

- POST /votos → Registrar voto (Exemplo de entrada)
```bash
    {
      "associadoId": 1,
      "pautaId": 1,
      "opcao": "sim"
    }
```
Regras de validação:
- `associadoId` obrigatório e maior que zero.
- `pautaId` obrigatório e maior que zero.
- `opcao` obrigatória e deve ser `SIM` ou `NAO` (case-insensitive).

- GET /pautas/{id}/resultado → Obter resultado da votação (Exemplo de saída)
```bash
    {
        "tipoTela": "SELECAO",
        "mensagem": "Resultado da votação",
        "dados": {
            "pautaId": 1,
            "totalSim": 3,
            "totalNao": 2,
            "status": "ENCERRADA",
            "resultado": "APROVADA"
        }
    }
```

- GET /cpfs/{cpf}/status → Verifica se o CPF recebido é válido para votar (Exemplo de saída)

```bash
    {
        "status": "ABLE_TO_VOTE"
    }
```
Regra de validação:
- `cpf` deve conter exatamente 11 dígitos numéricos.

---

## 🧪 Rodando Testes

## Testes de Controller, Repository, Service e Integração

### 1. Rodando testes individuais
Você pode executar os testes diretamente na IDE (IntelliJ ou outra):

- #### Controller:
```PautaControllerTest``` → botão direito no arquivo → Run 'PautaControllerTest'
- #### Repository:
```PautaRepositoryTest``` → botão direito no arquivo → Run 'PautaRepositoryTest'
- #### Service:
```VotoServiceTest``` e ```PautaServiceUnitTest``` → botão direito no arquivo → Run
- #### Integração:
```ApiIntegrationTest``` e ```ConcurrentVotingTest``` → botão direito no arquivo → Run

### 2. Rodando todos os testes de uma vez
Se preferir executar todos os testes do projeto ou de um pacote específico:

- Pelo IntelliJ: botão direito no pacote ```src/test/java/com/coop/cooperative/``` → Run 'All Tests'
- Pelo terminal, usando Maven:
```./mvnw test```

## Teste de performance

### 1. Executar o teste
- No IntelliJ (ou outra IDE): botão direito no arquivo → Run 'ConcurrentVotingTest'

Observação:
- O teste concorrente já cria automaticamente uma pauta e abre a sessão antes de iniciar as requisições concorrentes. Não é necessário editar o arquivo manualmente.

### 2. Ver métricas de performance
- Após a execução, acesse: http://localhost:8080/actuator/metrics/http.server.requests
- No link estão as métricas geradas pelo Spring Actuator (latência, contagem de requisições, etc.).

---

## 🔄 Versionamento da API

Atualmente, os endpoints não estão versionados por URL (`/api/v1`).  
Se houver evolução que quebre contrato, recomenda-se adotar versionamento explícito em versões futuras.

---

## ✍️ Autor

### Matheus Sia