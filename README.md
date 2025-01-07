# Servidor Teste - Prova de Conceito de Circuit Breaker

Este repositório faz parte de uma prova de conceito para implementação de um **Circuit Breaker**. Para configurar o circuito, é necessário baixar e compilar o seguinte repositório antes de utilizar este servidor:

[Repositório do Circuit Breaker](https://github.com/CarlosGRSchneider/circuit-breaker)

---

## Descrição do Projeto

Este servidor foi desenvolvido para demonstrar a utilização de um Circuit Breaker em dois endpoints diferentes. Ele inclui funcionalidades para simulação de problemas e testes do comportamento do circuito em cenários reais.

O servidor utiliza a biblioteca **HttpServer** do Java e permite interações através dos seguintes endpoints:

### Endpoints Principais

1. **`/animais`**
   - Retorna combinações de nomes de animais com adjetivos.
   - Protegido por um Circuit Breaker configurado para detectar problemas de performance ou falhas no endpoint.

2. **`/calculadora`**
   - Realiza uma soma entre dois numeros(numero1 e numero2) e devolve o valor. Aceita somente requisições do tipo **POST**.
   - Também protegido por um Circuit Breaker que monitora latências altas.

### Endpoints para Simulação de Problemas

1. **`/dev-tools/caos`**
   - Habilita ou desabilita o "Modo Caos". Quando ativado, permite simular erros ou exceções nos endpoints principais.

2. **`/dev-tools/latencia`**
   - Permite configurar a latência de resposta (em milissegundos) nos endpoints principais via uma requisição **POST** com o valor desejado no corpo da requisição.

---

## Configuração e Execução

1. **Clonar e compilar o repositório do Circuit Breaker**:
   ```bash
   git clone https://github.com/CarlosGRSchneider/circuit-breaker.git
   cd circuit-breaker
   mvn clean install
   ```

2. **Clonar e executar este repositório**:
   ```bash
   git clone https://github.com/CarlosGRSchneider/servidor-teste-circuit-breaker.git
   cd servidor-teste-circuit-breaker
   mvn clean install
   java -jar target/servidor-teste-circuit-breaker.jar
   ```

3. **Acessar os endpoints**:
   - `/animais`: `http://localhost:8080/animais`
   - `/calculadora`: `http://localhost:8080/calculadora`
   - `/caos`: `http://localhost:8080/dev-tools/caos`
   - `/latencia`: `http://localhost:8080/dev-tools/latencia`

---

## Estrutura do Código

### Classes Principais

- **`ServidorPrincipal`**: Classe principal que inicializa o servidor e configura os endpoints.
- **`CircuitBreaker`**: Importado do repositório "circuit-breaker" para proteger os endpoints.

### Handlers de Endpoints

- **`GeradorAnimalHandler`**: Gera combinações de nomes de animais com adjetivos.
- **`CalculadoraComplexaHandler`**: Responde com a soma de dois valores inteiros enviados no corpo da requisição.
- **`CaosHandler`**: Habilita ou desabilita o "Modo Caos".
- **`LatenciaHandler`**: Permite configurar a latência simulada nos endpoints principais.

### Variáveis Globais

- **`VariaveisDaAplicacao`**: Classe responsável por armazenar configurações globais como latência simulada e estado do "Modo Caos".

---

## Simulações e Testes

1. **Simular Erros nos Endpoints**:
   - Ativar o modo caos: `curl http://localhost:8080/dev-tools/caos`
   - Realizar requisições para `/animais` e observar os erros gerados.

2. **Ajustar a Latência**:
   - Configurar latência com um POST:
     ```bash
     curl -X POST -d "2000" http://localhost:8080/dev-tools/latencia
     ```
   - Realizar requisições para `/calculadora` e observar o aumento no tempo de resposta.

3. **Monitorar o Circuit Breaker**:
   - Realizar múltiplas requisições para `/animais` ou `/calculadora` com erros ou alta latência e observar o comportamento do circuito.

---

## Requisitos

- Java 17 ou superior.
- Maven 3.6 ou superior.
