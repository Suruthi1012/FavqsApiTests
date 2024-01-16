package QUOTES_Endpoint;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

import org.junit.BeforeClass;

import java.util.HashMap;


public class HTTPRequests {
	
	//Variables used throughout the test cases
    public static final String API_KEY = "66b2a63d68dcd9261cc5f03aeddc1d9b"; 
    private static final String API_KEY1 = "23466b2a63d68dcd9261cc5f03aeddc1d9b"; 
    private static final String BASE_URL = "https://favqs.com/api";
    public static final String getQuotes_checkStatus_Endpoint = BASE_URL + "/quotes";
    public static final String createUserEndpoint = BASE_URL + "/users";
    public static final String testMarkQuoteAsFavoriteEndpoint = (BASE_URL + "/quotes/{quoteId}");
    public static final String getQuoteByIdEndpoint = (BASE_URL + "/quotes/{quote_id}");
    public static final String quoteId = "42861";
    public static final String login = "suruthithoppeyjeeva";
    public static final String email = "suruthi2023ix@gmail.com";
    public static final String password = "Suru@1012";

    public String userToken;
    
 
	@Test
	void getQuotes_checkStatus()
	{
		given()
			.header("Authorization", "Token token=" + API_KEY)
			
		.when()
			.get(getQuotes_checkStatus_Endpoint)
		
		.then()
		 	.statusCode(200)
		 	.log().all();
	}
	
	@Test
	void getQuotes_checkBody()
	{
		given()
			.header("Authorization", "Token token=" + API_KEY)
		.when()
			.get(getQuotes_checkStatus_Endpoint)
		
		.then()
			.statusCode(200)
		 	.body("page",equalTo(1))
		 	.body("last_page",equalTo(false))
		 	.log().all();
	}
	@Test
	void getQuotes_checkStatus_invalid_apikey()
	{
		given()
			.header("Authorization", "Token token=" + API_KEY1)
			
		.when()
			.get(getQuotes_checkStatus_Endpoint)
		
		.then()
		 	.statusCode(401)
		 	.log().all();
	}

	@Test
	void getQuotes_checkStatus_no_apikey()
	{
		given()
			
		.when()
			.get(getQuotes_checkStatus_Endpoint)
		
		.then()
		 	.statusCode(401)
		 	.log().all();
	}
  
    @Test(priority=1)
    public void create_user() {
	String requestBody = "{ \"user\": { \"login\": \"" + login + "\", \"password\": \"" + password + "\" ,\"email\": \"" + email  + "\" } }";
        Response response = given()
                .header("Authorization", "Token token=" + API_KEY)
                .contentType(ContentType.JSON)
        	.accept(ContentType.JSON) 
        	.body(requestBody)
                .post(createUserEndpoint);
        response.then() 
        		.statusCode(200)
        		.log().all()
        		.body("User-Token", notNullValue())
        		.body("login", equalTo(login));

        userToken = response.jsonPath().getString("User-Token");
        System.out.println(userToken);
}
    
     @Test(priority=2)
    public void create_user_session() {
	String requestBody = "{ \"user\": { \"login\": \"" + login + "\", \"password\": \"" + password + "\"} }";
    	String createUserSessionEndpoint = BASE_URL + "/session";
        Response response = given()
                .header("Authorization", "Token token=" + API_KEY)
                .header("User-Token", "Bearer " + userToken)
                .contentType(ContentType.JSON)
        	.accept(ContentType.JSON) 
        	.body(requestBody)
                .post(createUserSessionEndpoint);
        response.then() 
        		.statusCode(200)
        		.log().all()
        		.body("User-Token", equalTo(response.jsonPath().getString("User-Token")))
        		.body("login", equalTo(login))
        		.body("email", equalTo(email));
        
        userToken = response.jsonPath().getString("User-Token");
        System.out.println(userToken);

}
     
     @Test(priority=3,dependsOnMethods = "create_user_session")
     public void testMarkQuoteAsFavorite_after_creating_user_session() {
         System.out.println("inside testMarkQuoteAsFavorite_after_creating_user_session " + userToken);
         System.out.println(testMarkQuoteAsFavoriteEndpoint);


         HashMap data = new HashMap();
         data.put("favourite", "True");
         
         given()
             .pathParam("quoteId", quoteId)
             .header("Authorization", "Token token=" + API_KEY)
             .header("User-Token", "Bearer" + userToken)
             .contentType("application/json")
             .body(data)
         .when()
         	.put(testMarkQuoteAsFavoriteEndpoint)
         .then()
         	//.statusCode(200)
     		.log().all()
             .body("favourite",equalTo("True"));

 }

     
     @Test
     public void try_creating_an_exisitng_user() {
         Response response = given()
                 .header("Authorization", "Token token=" + API_KEY)
                 .contentType(ContentType.JSON)
         	    .accept(ContentType.JSON) 
         	    .body("{ \"user\": { \"login\": \"30199\", \"email\": \"suruthi2023irl@gmail.com\", \"password\": \"Suru@1012\" } }")
                 .post(createUserEndpoint);
         response.then() 
         		.body("error_code", equalTo(32))
         		.body("message", equalTo("Username has already been taken; Username must contain at least one letter; Email has already been taken"))
         		.log().all();

 }
     
    @Test
    public void testGetQuoteById() {
        given()
            .pathParam("quoteId", quoteId)
            .header("Authorization", "Token token=" + API_KEY)
            .header("User-Token", "Bearer " + userToken)
        .when()
            .get(testMarkQuoteAsFavoriteEndpoint)
        .then()
            .statusCode(200)
    		.log().all()
            .contentType(ContentType.JSON)
            .body("id", equalTo(Integer.parseInt(quoteId)))
            .body("author", notNullValue())
            .body("body", notNullValue());
    }

  
    @Test
    public void testUnmarkQuoteAsFavorite() {

        HashMap data = new HashMap();
        data.put("favourite", "False");
        
        given()
            .pathParam("quoteId", quoteId)
            .header("Authorization", "Token token=" + API_KEY)
            .header("User-Token", "Bearer" + userToken)
            .contentType("application/json")
            .body(data)
        .when()
        	.put(testMarkQuoteAsFavoriteEndpoint)
        .then()
        	.statusCode(200)
    		.log().all();
    }

}
