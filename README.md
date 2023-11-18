# quarkus-social

## [Código fonte](https://github.com/cursodsousa/Quarkus-social/tree/9e43c0cc05f8dd7f9ac82b68bc46834f7d042c9c)

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

## Aula 12 - Listando e adicionando extensões do quarkus ao projeto
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
- Ao executar um *@PUT* não é necessário chamar dentro do escopo do método atualizador qualquer método para commitar a atualização, pois o commmit é executado automaticamente assim que o método atualizador finalizar (ver método updateUser).

## Aula 21 - Utilizando os repositórios do Panache
- Com a abordagem de usar repositórios, temos uma classe separada e específica para manipular a entidade com os métodos do Panache Entity. Assim a entidade torna-se somente uma representação da tabela do BD, impedindo que seja atribuído funções que não são das entidades (Ver classe UserRepository).
- Ao usar essa abordagem, não precisamos estender a entidade a PanacheEntityBase.
- @**ApplicationScoped** - usada na classe repository, a qual cria uma instância da classe repositório dentro do contexto da aplicação, no conteiner de injeção de dependências para poder usar onde quiser. Funciona como um **Singleton**, não importa quantos usuários estão acessando a API, haverá somente uma classe executando as operações p/todos (Ver classe UserRepository).
- **@Inject** - executa a injeção de dependência no construtor da classe (Ver classe UserResource).

## Aula 22 - Aplicando validações com Hibernate Validator (Bean Validator)
- No [link](https://quarkus.io/guides/validation) indica algumas configurações a se fazer, como instalar a extensão, atualizar o pom.xml, como acessar o validador, etc.
- Para as annotations abaixo ver record CreateUserRequest:
  - **@NotBlank(message = "Name is required.")** - verifica se o campo é nulo ou vazio, se acontecer algum dos dois, não deixa passar. 
  - **@NotNull(message = "Age is required.")** - verifica se o campo é nulo, se for, não deixa passar.

- Na classe controladora, no método createUser:
```java
Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(userRequest);
	if (!violations.isEmpty() ) {
		ConstraintViolation<CreateUserRequest> erro = violations.stream().findAny().get();
		String errorMessage = erro.getMessage();
		return Response.status(400).entity(errorMessage).build();
	}
```
- **validator.validate(userRequest)** retorna um Set de ConstraintViolation com informações sobre os campos que estão inválidos, os quais podemos tratar da forma que quisermos.
- **violations.stream().findAny().get()** temos um stream de erros, os quais podemos tratar um por um, então o método encontra qualquer erro e retorna. Em seguida armazenamos a mensagem retornada (a qual foi criada no record CreateUserRequest) e retornamos como resposta da aplicação.

## Aula 23 - Retornando um objeto de erro padrão
- Criamos uma classe para representar o erro, no qual armazenará o campo do erro e a mensagem do erro associada ao campo (ver classe FieldError).
- Criamos uma classe para representar um objeto de retorno quando temos o erro, retornando um json com a mensagem e um array dentro do json contendo cada um dos erros, com campo e mensagem (ver classe ResponseError).
- método **createFromValidaton** mapeia o **ConstraintViolation** para **FieldError**, mapeando todos (ver classe ResponseError):
```java
violations.stream()
			.map( cv -> new FieldError(cv.getPropertyPath().toString(), cv.getMessage()))
			.collect(Collectors.toList());
```
- **cv.getPropertyPath().toString()** pega a propriedade que tentou validar, que contém o erro.
- **cv.getMessage()** pega a mensagem de erro.
- **collect(Collectors.toList())** captura cada *fielError* e adiciona a uma lista.

## Aula 24 - Alterando os códigos de status para mais específicos
- Trocamos os códigos de retorno de requisições para outros:
  - **POST** - *201 Created* para sucesso na criação de um usuário e *422 Unprocessable Entity* para quando envíamos uma requisição com algum dado faltando.
  - **GET** - não foi alterado.
  - **DELETE** - *204 No Content* para sucesso em deletar um usuário e continuou *404 Not Found* para requisição com id errado.
  - **PUT** - *204 No Content* para sucesso em atualizar um usuário e  continuou *404 Not Found* para requisição com id errado.

[Link status code](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status)

## Aula 27 - Criando o Resource de Posts e os endpoints
- Criamos uma classe controladora para Post, mas ela é um subrecurso de Users, logo, a uri é *@Path("/users/{userId}/posts")*.

## Aula 28 - Implementando o método de realizar postagens
- Sem muita inovação, fizemos como antes, criamos um repository, uma entity, uma tabela no BD, etc..

## Aula 29 - Concluíndo a API de postagem
- Novamente, sem muita difereça do que já vimos. Uma nota, o método **Sort.Direction.Descending** presentes em **listPost** da classe controladora **PostResource** permite ordenar a lista de retorno do mais recente ao mais antigo.

## Aula 31 - Criando a Tabela e as classes necessárias para API
- Criamos a tabela de seguidores (followers), a entidade, o repository, o resource com as annotation necessárias e injeções de dependências. 

## Aula 32 - Implementando o método de seguir um usuário
- Vamos verificar se o follower já está seguindo o usuário, evitando assim que seja criado um novo registro com mesmos dados.

## Aula 33 - Concluindo o PUT de followers
- O código abaixo se encontra em **FollowerRepository**:
```java
public boolean follows(User follower, User user) {

//        Map<String, Object> params = new HashMap<>();
//        params.put("follower", follower);
//        params.put("user", user);

	var params = Parameters.with("follower", follower).and("user", user).map();

	PanacheQuery<Follower> query = find("follower = :follower and user = :user", params);
	Optional<Follower> result = query.firstResultOptional();

	return result.isEmpty();
}
```
- Ele cria uma query para executar uma busca na tabela Follower e verificar se há um registro com o id do follower e do user, se houver retorna true e em uma cada acima será tratado para impedir que outro registro igual seja adicionado ao BD.
- O código comentado faz o mesmo que a linha *var params*.

## Aula 34 - Evitando conflito de usuarios
- Evitando que um usuário torne-se *follower* de si próprio.
```java
public Response followUser( @PathParam("userId") Long userId, FollowerRequest followerRequest ){
	if(userId.equals(followerRequest.followerId())){
		return Response.status(Response.Status.CONFLICT)
				.entity("You can't follow yourself.")
				.build();
	}
...
}
```

## Aula 35 - Implementando o GET de Followers
- Criamos as classes *FollowerResponse* e *FollowersPerUserResponse*, o método *findByUser* para retornar todos os *followers* e o método *listFolloweres* para listar todos os *followers*.

## Aula 36 - Implementando o método para deixar de seguir um usuário
- **@QueryParam**: para passar um parâmetro pela url após o símbolo '?'. Usado com *GET* e *DELETE*.
- Exemplode de requisição: *http://localhost/users/4/followers?followerId=8*

## Aula 37 - Restrigindo o acesso aos posts
- Só poderá visualizar as postagens de um certo usuário se você for um seguidor dele.
- Faremos isso via header com a annotation **@HeaderParam**.
- Ver método *listPost* na classe **PostResource**.

## Aula 38 - Configurando o ambiente para os testes
- Neste [link](https://quarkus.io/guides/config-reference#default-profiles) podemos ver os profiles para dev, test e prod. Cada um com uma funcionalidade diferente. Também é possível criar outros profiles.
- No arquivo **application.properties** iremos realizar a configuração para o profile de test.
- Iremos usar um banco em memória para executar os testes.

## Aula 39 - Implementando o primeiro teste de API
- Nas classes de teste precisamos usar a annotation **@QuarkusTest** e **@Test** nos métodos.
- Testando a API de **Users-createUser** com dois aspectos:
  - faz validação se os dados estão presentes para salvar no BD, retornando *UNPROCESSABLE_ENTITY_STATUS* se dados estiverem errados.
  - do contrário, salva usuário e retorna *CREATED* com os dados persisitidos no BD no corpo da requisição.
- Um explicação do método:
```java
@Test
@DisplayName("should create user successfully")
public void createUserTest() {
	var user = new CreateUserRequest("Fulano", 30);

	var response = 
                given()
                    .contentType(ContentType.JSON)
                    .body(user)
                .when()
                    .post("/users")
                .then()
                    .extract().response();
	
	assertEquals(201, response.statusCode());
    assertNotNull(response.jsonPath().getString("id"));
}
```
- Criamos um usuário e dado um cenário (*given*) ao qual temos um conteúdo (*contentType*) com esse corpo (*body*), quando (*when*) eu fizer um post (*post*) então (*then*), vou extrair a resposta dessa requisição (*extract.response*).
- Por fim, verificamos se o status code da resposta é 201 e se o campo 'id' está sendo enviado, o que não será nulo.

# Aula 40 - Testando o caso de erro de validação
- Segundo caso - caso de erro:
```java
@Test
@DisplayName("should return error when json is not valid")
@Order(2)
public void createUserValidationErrorTest(){
	var user = new CreateUserRequest(null, null);

	var response = given()
					.contentType(ContentType.JSON)
					.body(user)
			.when()
				.post(apiURL)
			.then()
				.extract().response();

	assertEquals(ResponseError.UNPROCESSABLE_ENTITY_STATUS, response.statusCode());
	assertEquals("Validation Error", response.jsonPath().getString("message"));

	List<Map<String, String>> errors = response.jsonPath().getList("errors");
	assertNotNull(errors.get(0).get("message"));
	assertNotNull(errors.get(1).get("message"));
//        assertEquals("Age is Required", errors.get(0).get("message"));
//        assertEquals("Name is Required", errors.get(1).get("message"));

}
```
- Com a mesma lógica que o teste da aula 39 mas passando os valores do objeto como nulos.
- Note que o código comentado é para verificar a mensagem de erro.

# Aula 41 - Realizando Testes ordenados
- Testando a API de **Users-listAllUsers**:
```java
@Test
@DisplayName("should list all users")
public void listAllUsersTest(){
	given()
		.contentType(ContentType.JSON)
	.when()
		.get(apiURL)
	.then()
		.statusCode(200)
		.body("size()", Matchers.is(1));
} 
```
- Explicando acima, criamos um cenário para url */users* e quando executar o get, é necessário receber o código de estado 200 e o tamanho do vetor deverá ter 1 elemento.
- Da forma como está o método poderá ocorrer um erro pois estamos sem usuários no BD.
- Para usar o método da aula 39 e adicionar um usuário no BD e ter a certeza que este teste irá funcionar, podemos usar a annotation **@TestMethodOrder(MethodOrderer.OrderAnnotation.class)** na classe de teste, o qual informa que será utilizadas annotations de ordenação
- Em seguida, usamos a annotation **@Order(número_que_indica_a_ordem)** a qual determinará a ordem de execução dos métodos.
- Ver classe **UserResourceTest**.

# Aula 42 - Criando cenário com beforeEach
- Sobre a annotation **beforeEach**, na classe de teste foi criado um método chamado **setUP** que usa essa annotation. Este método executará antes de todos os testes e criará a base necessária para os teste, como criação de users, posts e followers.
- Testando a API de **PostResource-savePost**. 
  - Dois aspectos, um quando não encontra o usuário e outro quando ele encontra e faz a postagem.
- Com a annotation **@TestHTTPEndpoint(PostResource.class)** na classe de teste definimos qual recurso/API estamos testando, ao qual ele reconhecerá a url, que no caso será *"/users/{userId}/posts"*.
```java
@Test
@DisplayName("should create a post for a user")
public void createPostTest(){
	var postRequest = new CreatePostRequest();
	postRequest.setText("Some text");

	given()
		.contentType(ContentType.JSON)
		.body(postRequest)
		.pathParam("userId", userId)
	.when()
		.post()
	.then()
		.statusCode(201);
}
```

# Aula 43 - Teste de usuário inexistente ao tentar fazer uma postagem
- Agora será testado o 2º caso, quando não há o usuário no BD e retorna erro.
- Muito semelhante ao método anterior, mas com a diferença que criamos uma variável interna para passar um id inexistente e o código de estado é 404.

# Aula 44 - Definindo os casos de teste da lista de posts
- Testando a API de **PostResource-listPosts**.
- Temos 5 casos:
  - 1º para o caso em que o usuário enviado não é válido.
  - 2º para o caso em que é esquecido de enviar o *followerId* no header.
  - 3º para o caso em que é enviado o *follower* mas este *follower* não existe.
  - 4º para o caso em que o *follower* não segue o *user*.
  - 5º para o caso em que está tudo certo.
- Para cada caso ver, respectivamente, os seguintes testes da classe **PostResourceTest**:
  - **Aula 45** - *listPostUserNotFoundTest*
  - **Aula 45** - *listPostFollowerHeaderNotSendTest*
  - **Aula 45** - *listPostFollowerNotFoundTest*
  - **Aula 46** - *listPostNotAFollower*
  - **Aula 46** - *listPostsTest*

# Aula 47 Testes na API de Followers
- Semelhante ao classe de teste de **PostResource**, usamos as annotations *TestHTTPEndpoint*, *beforeEach* com um método *setUp*, 
- Testando a API de **FollowerResource-followUser**.
- Temos 3 casos:
  - 1º para o caso do id do *follower* for igual ao id do *user*.
  - 2º para o caso de um *follower* for seguir um *user* que não existe.
  - 3º para o caso de um *follower* for seguir um *user* ao qual ele não segue.
- Para cada caso ver, respectivamente, os seguintes testes da classe **FollowerResourceTest**:
  - **Aula 47** - *sameUserAsFollowerTest*
  - **Aula 47** - *userNotFoundWhenTryingToFollowTest*
  - **Aula 48** - *followUserTest*

# Aula 49 - Testes de listagem de seguidores
- Testando a API de **FollowerResource-listFollowers**.
- Temos dois casos:
  - 1º para quando não encontramos o *user* no BD, pois seu id é inválido.
  - 2º para quando é encontrado o *user* e retornamos a lista de seguidores.
- Para cada caso ver, respectivamente, os seguintes testes da classe **FollowerResourceTest**:
  - **Aula 49** - *userNotFoundWhenListingFollowersTest*
  - **Aula 49** - *listFollowersTest*
- *userNotFoundWhenListingFollowersTest* é semelhante ao teste *userNotFoundWhenTryingToFollowTest*, só que ao invés de usar um *.put()* usamos um *.get()* e não passamos o *var body*.

# Aula 50 - Finalizando o módulo de testes
- Testando a API de **FollowerResource-unfollowUser**.
- Temos dois casos:
  - 1º para quando não encontramos o *user* no BD, pois seu id é inválido.
  - 2º para quando é encontrado o *user* e deletamos a referência de seguidor.
- Para cada caso ver, respectivamente, os seguintes testes da classe **FollowerResourceTest**:
  - **Aula 50** - *userNotFoundWhenUnfollowingAUserTest*
  - **Aula 50** - *unfollowUserTest*

# Aula 51 - Documentando a API com Swagger UI
- No [link](https://quarkus.io/guides/openapi-swaggerui) indica como ativar o swagger da aplicação.
- 1º precisamos instalar uma [extensão](https://quarkus.io/guides/openapi-swaggerui).
- 2º adicionamos a seguinte propriedade no arquivo *application.properties*.
  - **quarkus.swagger-ui.always-include=true**
- 3º em seguida subimos a aplicação com *./mvnw quarkus:dev*.
- 4º acessamos o link *http://localhost:8080/q/swagger-ui/* para verificarmos a documentação da API.
- Dessa forma podemos usar o swagger para executar testes, como no Postman.
- Também há a possibilidade de melhorar o swagger.

# Aula 52 - Adicionando meta dados na documentação
- No [link](https://quarkus.io/guides/openapi-swaggerui#providing-application-level-openapi-annotations) temos a opção de ativar uma documentação mais apurada, adicionando metadados:
```java
@OpenAPIDefinition(
tags = {
		@Tag(name="widget", description="Widget operations."),
		@Tag(name="gasket", description="Operations related to gaskets")
},
info = @Info(
	title="Example API",
	version = "1.0.1",
	contact = @Contact(
		name = "Example API Support",
		url = "http://exampleurl.com/contact",
		email = "techsupport@example.com"),
	license = @License(
		name = "Apache 2.0",
		url = "https://www.apache.org/licenses/LICENSE-2.0.html"))
)
```
- Também há a opção de fazer a configuração do swagger pelo **application.properties**.

# Aula 53 - Empacotando a aplicação para produção
- Gerando o arquivo **.jar** para produção:
  - No terminal, no diretório raiz da aplicação, usar o comando
  - **./mvnw clean package -DskipTests**
  - E o arquivo **.jar** se encontra dentro do diretório */target/quarkus-app*, com o nome de **quarkus-run.jar**
  - Para executá-lo, usamos o comando:
  - **java -jar ./target/quarkus-app/quarkus-run.jar**