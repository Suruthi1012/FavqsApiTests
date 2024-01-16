package api.endpoints;

public class Routes {
	//Contains all the URLS which are being used by the test cases.
	
    public static final String BASE_URL = "https://favqs.com/api";

    //End points used in the testcases / advisable to have all CRUD operation URLs for all models in this part.
 
    public static final String getQuotes_checkStatus_Endpoint = BASE_URL + "/quotes";
    public static final String createUserEndpoint = BASE_URL + "/users";
    public static final String createUserSessionEndpoint = BASE_URL + "/session";
    public static final String testMarkQuoteAsFavoriteEndpoint = BASE_URL + "/quotes/{quoteId}";
    public static final String getQuoteByIdEndpoint = BASE_URL + "/quotes/{quote_id}";
    public static final String markAsFavoriteEndpoint = BASE_URL + "/quotes/{nonExistingQuoteId}/fav";
    public static final String unmarkAsFavoriteEndpoint = BASE_URL + "/quotes/{quoteId}/unfav";
}
