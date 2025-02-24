<div id="top"></div>

<br />
<div align="center">
	<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/6/6a/Logo_Positivo_Tecnologia.jpg/1200px-Logo_Positivo_Tecnologia.jpg" alt="Logo" height="90"/><img src="https://www.tupifintech.com/hubfs/Untitled%20design%284%29-Jul-11-2023-10-54-01-6671-PM.png" alt="Logo" height="90"/>
	<h1 align="center">Desafio Técnico - EMV</h1>
	<p align="center">
    	Esse sistema foi desenvolvido para um desafio técnico proposto pelas empresas <a href="https://www.positivotecnologia.com.br/">Positivo Tecnologia</a> e <a href="https://www.tupifintech.com/">Tupi Fintech</a>.
  	</p>

<br/>

[Requisitos](#requisitos) · [Estrutura](#estrutura) · [Execução](#execução) · [Requisições](#requisições) · [Referências](#referências)

</div>



## Descrição
O sistema é uma API REST desenvolvida com Spring Boot, que recebe um JSON contendo os dados EMV de um cartão, organizados no formato TLV. Ele realiza a decodificação, validação, armazenamento e simula a autorização da transação.


## Requisitos
A aplicação está preparada para a compilação e execução via container (Docker), mas também pode ser executada localmente com a instalação do Maven (para gerenciamento de dependências e compilação) e do Java 23 (para execução).

Docker: https://www.docker.com/products/docker-desktop \
Maven: https://maven.apache.org/download.cgi \
Java23: https://www.oracle.com/br/java/technologies/downloads/#jdk23-windows

## Estrutura

```
projeto
├── desafio
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── com
│   │   │   │       └── tupi
│   │   │   │           └── desafio
│   │   │   │               ├── controller   # Contém os endpoints da API REST, que recebem e respondem às requisições
│   │   │   │               ├── dto          # Objetos de Transferência de Dados, usados para transitar dados entre as camadas
│   │   │   │               ├── entity       # Modelo de dados/tabelas para comunicação com o banco de dados
│   │   │   │               │   └── model    # Estruturas que representam dados específicos
│   │   │   │               ├── enums        # Tipos enumerados, usados em várias partes do código para valores fixos
│   │   │   │               ├── exception    # Definições de exceções customizadas para o tratamento de erros específicos
│   │   │   │               ├── interfaces   # Interfaces que definem contratos para serviços, repositórios, etc.
│   │   │   │               ├── mapper       # Classes de mapeamento que convertem objetos para diferentes camadas
│   │   │   │               ├── repository   # Repositórios para acessar e manipular dados do banco (SQLite)
│   │   │   │               ├── service      # Contém a lógica de negócios da aplicação
│   │   │   │               └── validator
│   │   │   │                   ├── captura  # Classes de validações relacionadas a captura (Recebimento de Tags Mandatórias)
│   │   │   │                   └── dados    # Classes de validações relacionadas aos dados (Tamanho / Formatação / Regras)
│   │   │   └── resources
│   │   │       └── application.properties   # Arquivo de configuração do Spring
│   │   └── test
│   │       └── java
│   │           └── com
│   │               └── tupi
│   │                   └── desafio
│   │                       ├── service      # Testes dos serviços
│   │                       └── validator    # Testes das validações
│   │                           ├── captura  # Testes para as validações específicas de captura / recebimento de tags
│   │                           └── dados    # Testes para as validações específicas de dados
│   ├── docker-compose.yml   # Arquivo de configuração para orquestração de containers
│   ├── Dockerfile           # Arquivo de configuração para criar a imagem Docker da aplicação
│   └── README.md            # Esta página/arquivo
└── readme.md                # README.md original do desafio
```


## Execução
Pontos de observação:
1. _Os comandos abaixo, considera que você já está dentro da pasta inicial desse repositório chamada: `desafio-tecnico`_
2. _A aplicação irá iniciar na porta default do Tomcat: `8080`_ 

<br/>

Abaixo consta a os comandos para compilação e execução para cada modo escolhido:


* Utilizando Docker\
  Os testes automatizados estão desativados durante a compilação via `Dockerfile`.\
  Você pode mudar isso alterando trecho: `-DskipTests=true` para  `-DskipTests=false` em `projeto/Dockerfile`.
  * Compilar e executar via docker-compose (orquestrador de serviços):
    ```cmd
    > cd projeto && docker compose up
    ```

  * Compilar e rodar via docker:
    ```cmd
    > docker build -t nome-imagem projeto/
    > docker run -p 8080:8080 --name nome-container nome-imagem
    ```

<br/>

* Utilizando Maven

  * Para compilar e rodar o projeto:
    ```cmd
    > mvn -f projeto/desafio/pom.xml spring-boot:run
    ```
  * Para rodar os testes automatizados:
    ```cmd
    > mvn -f projeto/desafio/pom.xml test
    ```

<br/>

* Utilizando Java 23
  * Para rodar o projeto compilado:
    ```cmd
    > java -jar desafio-0.0.1-SNAPSHOT.jar
    ```
	O download do arquivo `desafio-0.0.1-SNAPSHOT.jar` está disponível em [desafio-tecnico/releases](https://github.com/wkricowski/desafio-tecnico/releases).

<br/>

Após a aplicação ser iniciada você poderá ver os endpoints, exemplos e a documentação da API (via swagger) pelo link:
```text
http://localhost:8080/swagger-ui/index.html#/
```

<br/>

## Requisições


Exemplo de envio `POST` para `/captura`:
```json
{
    "dadosEMV": "5A0841111111111111115F24033212209F34030200005F2A0209869A032502249C01009F1A020076"
}
```
Exemplo de retorno `201 - Created` da API:

```json
{
    "id": 3,
    "dtaTransacao": "2025/02/24T17:22:12",
    "status": "APROVADA",
    "nsu": 535595,
    "conteudoEMV": "5A0841111111111111115F24033212209F34030200005F2A0209869A032502249C01009F1A020076",
    "tags": [
        {
            "tag": "5A",
            "length": 8,
            "value": "4111111111111111"
        },
        {
            "tag": "5F24",
            "length": 3,
            "value": "321220"
        },
        {
            "tag": "9F34",
            "length": 3,
            "value": "020000"
        },
        {
            "tag": "5F2A",
            "length": 2,
            "value": "0986"
        },
        {
            "tag": "9A",
            "length": 3,
            "value": "250224"
        },
        {
            "tag": "9C",
            "length": 1,
            "value": "00"
        },
        {
            "tag": "9F1A",
            "length": 2,
            "value": "0076"
        }
    ]
}
```

Exemplo de retorno `400 - Bad Request` da API:
```
{
    "erro": "Tag EMV Mandatória: 5A (PAN), não recebida.",
    "dtaHora": "2025/02/24T17:35:23"
}
```

É possivel verificar todas as transação **processadas** fazendo uma requisição `GET` para `/historico` que retornará a lista com suporte a páginação (default: 20 registros por página) ou recuperar uma transação em especifica informando o `id` no endpoint `/historico/{id}`.


<br/>

## Referências

- Padrões de commit → [GitHub](https://github.com/iuricode/padroes-de-commits)  
  _Foi realizado um merge squash na `main`, porém a sequência de commits está visivel na branch: `dev`: [commits/dev](https://github.com/wkricowski/desafio-tecnico/commits/dev/)_. 

- Tag 5F24 - MasterCard → [EFTLab](https://www.eftlab.com/knowledge-base/complete-list-of-emv-nfc-tags)  

- Tag 9F34 - Estrutura do Byte → [StackOverflow](https://stackoverflow.com/questions/47000091/parse-cv-rule-from-cvm-list-for-emv) | [Decoder](https://paymentcardtools.com/emv-tag-decoders/cvm-results)  

- Algoritmo de Luhn → [Wikipedia](https://en.wikipedia.org/wiki/Luhn_algorithm) | [Validador](https://simplycalc.com/luhn-validate.php)

- TLV Utilities → [EMVLab](https://emvlab.org/main/)

