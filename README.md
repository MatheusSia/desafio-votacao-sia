# 🗳️ Desafio Votação

Este projeto é uma aplicação Java Spring Boot desenvolvida como parte de um desafio técnico.  
O objetivo é implementar um sistema de votação com regras específicas, utilizando **Spring Boot**, **Maven** e **Banco de Dados**.

---

## ⚙️ Tecnologias Utilizadas

- Java 17
- Spring Boot
- Maven
- H2 Database (memória)
- JUnit / Mockito (para testes)
- Actuator (para métricas de performance)

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

A aplicação utiliza H2 Database em memória.

### Acessar console H2

Após iniciar a aplicação, acesse:
http://localhost:8080/h2-console

Credenciais padrão:
- JDBC URL: jdbc:h2:mem:testdb
- Usuário: sa
- Senha: (em branco)

---

## 🌐 Endpoints Principais

- POST /pautas/cadastrar → Criar nova pauta (Exemplo de entrada)
```bash
  {
    "titulo": "Teste",
    "descricao": "Teste para ver se a tarefa bonus 2 esta com tudo correto"
  }
```

- POST /pautas/{id}/abrir-sessao → Abrir sessão de votos para pauta (Exemplo de entrada)
```bash
  {
    "minutos": 3
  }
```

- POST /votos/registrar → Registrar voto (Exemplo de entrada)
```bash
    {
      "associadoId": 1,
      "pautaId": 1,
      "opcao": "sim"
    }
```

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

- GET /cpf/{cpf} → Verifica se o CPF recebido é valido ou não para votar (Exemplo de saída)

```bash
    {
        "status": "ABLE_TO_VOTE"
    }
```

---

## 🧪 Rodando Testes

## Testes de Controller, Repository e Service

### 1. Rodando testes individuais
Você pode executar os testes diretamente na IDE (IntelliJ ou outra):

- #### Controller:
```PautaControllerTest``` → botão direito no arquivo → Run 'PautaControllerTest'
- #### Repository:
```PautaRepositoryTest``` → botão direito no arquivo → Run 'PautaRepositoryTest'
- #### Service:
```VotoServiceTest``` → botão direito no arquivo → Run 'VotoServiceTest'

### 2. Rodando todos os testes de uma vez
Se preferir executar todos os testes do projeto ou de um pacote específico:

- Pelo IntelliJ: botão direito no pacote ```src/test/java/com/coop/cooperative/``` → Run 'All Tests'
- Pelo terminal, usando Maven:
```./mvnw test```

## Teste de performance

### 1. Criar uma pauta
- Anote o id retornado (exemplo: 1).

### 2. Abrir a sessão da pauta
- Substitua {id} pelo id da pauta criada.

### 3. Configurar o teste
- Abra o arquivo: ```src/test/java/com/coop/cooperative/ConcurrentVotingTest```
- Localize na linha 40: "pautaId", 34L,
- Troque 33L pelo id da pauta criada (exemplo: 1L).

### 4. Executar o teste
- No IntelliJ (ou outra IDE): botão direito no arquivo → Run 'ConcurrentVotingTest'

### 5. Ver métricas de performance
- Após a execução, acesse: http://localhost:8080/actuator/metrics/http.server.requests
- No link estão as métricas geradas pelo Spring Actuator (latência, contagem de requisições, etc.).

---

## ✍️ Autor

### Matheus Sia