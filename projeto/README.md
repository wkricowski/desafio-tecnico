<div id="top"></div>

<br />
<div align="center">
	<a href="docs/"><img src="https://upload.wikimedia.org/wikipedia/commons/thumb/6/6a/Logo_Positivo_Tecnologia.jpg/1200px-Logo_Positivo_Tecnologia.jpg" alt="Logo" height="90"/></a><a href="docs/"><img src="https://www.tupifintech.com/hubfs/Untitled%20design%284%29-Jul-11-2023-10-54-01-6671-PM.png" alt="Logo" height="90"/></a>
	<h1 align="center">Desafio Técnico - EMV</h1>
	<p align="center">
    	Esse sistema foi desenvolvido para um desafio técnico proposto pelas empresas <a href="#">Positivo Tecnologia</a> e <a href="#">Tupi Fintech</a>.
  	</p>

<br/>

[Requisitos](#requisitos) · [Estrutura](#estrutura) · [Execução](#execução) 

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
│   │   │   │                   └── captura  # Classes de validações relacionadas a captura/transação
│   │   │   └── resources
│   │   │       └── application.properties   # Arquivo de configuração do Spring
│   │   └── test
│   │       └── java
│   │           └── com
│   │               └── tupi
│   │                   └── desafio
│   │                       ├── service      # Testes dos serviços
│   │                       └── validator    # Testes das validações
│   │                           └── captura  # Testes para as validações específicas de captura de dados
│   ├── docker-compose.yml   # Arquivo de configuração para orquestração de containers
│   ├── Dockerfile           # Arquivo de configuração para criar a imagem Docker da aplicação
│   └── README.md            # Esta pagina/arquivo
└── readme.md                # README.md original do desafio
```


## Execução


