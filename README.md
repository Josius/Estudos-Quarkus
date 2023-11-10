# quarkus-social
## Aula 11 - criar projeto quarkus com maven
- Em [Using our Tooling - how to use Maven as a build tool](https://quarkus.io/guides/maven-tooling) encontra-se o passo-a-passo para criar um projeto quarkus com maven.
- No caso, o comando abaixo fará o download de vários pacotes necessários para criar o projeto:
```bash
mvn io.quarkus.platform:quarkus-maven-plugin:3.5.1:create
```
- Em seguida, pedirá o groupId, artifactId, version, extensões que podemos adicionar ao projeto (enter pula essa opção) e por fim, se queremos algum código inicial (yes).
- Ao criar o projeto, haverá um executável **mvnw** que é a versão do maven ao qual foi usada para criar o projeto. Ao dar manutenção ao projeto, pode-se usar esse **mvnw**.
- Em seguida, para iniciar a aplicação em modo de desenvolvimento (hot deploy/swap) usamos o [comando](https://quarkus.io/guides/maven-tooling#dev-mode):
```bash
mvn compile quarkus:dev
```
- Acessar http://localhost:8080/ para ver a página inicial do projeto quarkus pré-configurada.
- **hot deploy/swap:** atualiza a aplicação sem precisar derrubar ela.

## Aula 12 - listando e adicionando extensões do quarkus ao projeto
- 1º modo é ir no site do [maven repository](https://mvnrepository.com/), encontrar a dependência desejada, copiar o código da dependência e colar no pom.xml, no escopo de \<dependecies>.
- 2º modo é usar o próprio quarkus, os quais são chamadas de extensões na linguagem do quarkus, usando a linha de comando. Veja em [Dealing with extensions](https://quarkus.io/guides/maven-tooling#dealing-with-extensions) os comandos necessários para listar todas as extensões e como instalar uma ou mais extensões com base no id de cada extesão:
- ./mvnw quarkus:list-extensions
- ./mvnw quarkus:add-extension -Dextensions='hibernate-validator'
- ./mvnw quarkus:add-extension -Dextensions='jdbc-h2,hibernate-orm,hibernate-orm-panache,resteasy-jsonb'

## Aula 14 - Criando POST de Users
- **@Path("/\<url>")** - torna a classe em um controlador REST.

## Aula 15 - Recebendo e devolvendo objetos JSON
- **@Consumes(MediaType.APPLICATION_JSON)** - informa que tipo de dado será recebido, no caso receberá JSON no corpo da requisição, ou seja consome JSON.
- **@Produces(MediaType.APPLICATION_JSON)** - informa que tipo de dado será retornado, no caso receberá JSON no corpo da resposta, ou seja retorna JSON.

## Aula 17 - Criando a tabela de Users e configurando a conexão
### Criando BD e tabela Users
```sql
CREATE DATABASE quarkus_social;

create table USERS(
	id bigserial PRIMARY KEY,
	name varchar(100) not null,
	age integer not null
);
```

### Configurando a conexão
Neste [link](https://quarkus.io/guides/datasource#configure-a-jdbc-datasource) temos a explicação da configuração abaixo:
```
quarkus.datasource.db-kind=postgresql 
quarkus.datasource.username=<your username>
quarkus.datasource.password=<your password>

quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/hibernate_orm_test
quarkus.datasource.jdbc.max-size=16
```

### Instalando a extensão
- ./mvnw quarkus:add-extension -Dextensions='jdbc-postgresql'




****
****
****
# Readme pré-configurado

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/quarkus-social-1.0-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Related Guides

- RESTEasy Classic ([guide](https://quarkus.io/guides/resteasy)): REST endpoint framework implementing Jakarta REST and more

## Provided Code

### RESTEasy JAX-RS

Easily start your RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)
