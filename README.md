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
- **@POST** - informa que íremos salvar um registro no BD. NÃO é idempotente, ou seja, sempre que criar um registro, a resposta será diferente.

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

## Aula 18 - Mapeamento da entidade de Users
- Criamos a classe User com annotations do JPA e Lombok (*@Data, @EqualsAndHashCode, @Entity, @Table, @Id, @GeneratedValue, @EqualsAndHashCode, @Column*)

## Aula 19 - Manipulando entidades com o Panache Entity
- Panache possuí o EntityManager em sua implementação.
- Possuí métodos prontos para utilizar.
  
### Há duas formas de utilizar 
- A 1ª é com *extends PanacheEntity*, e desta forma, não precisamos usar o atributo id, pois este é mapeado pela superclasse PanacheEntity. 
- Libera vários métodos prontos para utilizar, pois estende da PanacheEntityBase, a qual é possuídora dos métodos.
```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@EqualsAndHashCode.Include
private Long id;
```
- Entretanto usar da forma acima pode gerar algum erro, pois estamos delegando a geração da id para o BD, por exemplo, PostgreSQL usará um BigSerial, MySQL usará o auto_increment, etc.. Ao extender a PanacheEntity, se delegar ao BD a geração do id ocorrerá o erro: **Caused by: org.postgresql.util.PSQLException: ERROR: relation "alguma_coisa_seq" does not exist
  Posição: 16** que ocorre pela superclasse PanacheEntity não ter uma indicação de delegar ao BD a geração do id.

- A 2ª é com **extends PanacheEntityBase** a qual permite o uso das annotation para id, como exemplo acima, evitando assim o erro descrito.
- Libera vários métodos prontos para utilizar, pois é a possuídora dos métodos.

### Observações ao usar Panache Entity
- Na classe controladora UserResource precisamos usar a annotation **@Transactional** sobre o método que irá fazer alterações no BD para que seja aberta uma transação com BD (Ver o método createUser).
- Há vários métodos para se usar para a classe que estendeu a Panache Entity, como o método *.persist()* para salvar no BD o user (ver método createUser) ou mesmo *.findAll()* que recupera todos os registros da tabela Users (ver método listAllUsers).
- **@GET** - informa que a operação será para recuperar registro(s) no BD.

## Aula 20 - Concluíndo as operações
- **@DELETE** - informa que a operação será de exclusão do registro. Método que usa *@DELETE* precisa de **@Transactional**.
- **@Path("{id}")** - quando executar um DELETE para a url passa mais um parâmetro com ela indicando o id do registro que será excluído do BD (ver método deleteUser).
- **@PUT** - informa que iremos alterar um registro no BD. É idempotente, ou seja, sempre que atualizar um registro com os mesmo dados, sempre haverá a mesma resposta. É usado em conjunto com **@Path("{id}")** (ver método updateUser). Método que usa *@PUT* precisa de **@Transactional**.
- 









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
