package br.ce.wcaquino.tasks.apitest;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.*;

public class APITest {
	
	@BeforeClass
	public static void setup() {
		baseURI="http://localhost/";
		port = 8001;
		basePath = "tasks-backend/";		
	}
	
	@Test
	public void deveRetornarTarefas() {		
		given()
			.contentType(ContentType.JSON)
		.when()
			.get("todo")
		.then()
			.statusCode(200);
	}
	
	@Test
	public void deveAdicionarTarefaComSucesso() {
		given()
		.contentType(ContentType.JSON)
		.body("{\"task\":\"Tarefa 002\",\"dueDate\":\"2022-01-10\"}")
	.when()
		.post("todo")
	.then()
		.statusCode(201);
	}
	
	@Test
	public void naoDevePermitirAlterarTarefa() {
		given()
		.contentType(ContentType.JSON)
		.body("{\"id\":1,\"task\":\"Tarefa 001 editada\",\"dueDate\":\"2021-12-31\"}")
	.when()
		.put("todo")
	.then()
		.statusCode(405)
		.body("message", CoreMatchers.is("Request method 'PUT' not supported"));
	}
	
	@Test
	public void naoDeveAdicionarTarefaComDataInvalida() {
		given()
		.contentType(ContentType.JSON)
		.body("{\"task\":\"Tarefa 002\",\"dueDate\":\"2010-01-10\"}")
	.when()
		.post("todo")
	.then()
		.statusCode(400)
		.body("message", CoreMatchers.is("Due date must not be in past"));
	}
	
	@Test
	public void naoDeveAdicionarTarefaSemData() {
		given()
		.contentType(ContentType.JSON)
		.body("{\"task\":\"Tarefa 002\"}")
	.when()
		.post("todo")
	.then()
		.statusCode(400)
		.body("message", CoreMatchers.is("Fill the due date"));
	}
	
	@Test
	public void naoDeveAdicionarTarefaSemDescricao() {
		given()
		.contentType(ContentType.JSON)
		.body("{\"task\":\"\",\"dueDate\":\"2010-01-10\"}")
	.when()
		.post("todo")
	.then()
		.statusCode(400)
		.body("message", CoreMatchers.is("Fill the task description"));
	}	
		

}
