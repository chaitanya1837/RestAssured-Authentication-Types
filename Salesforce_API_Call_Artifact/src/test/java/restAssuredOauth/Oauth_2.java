package restAssuredOauth;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.equalTo;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

public class Oauth_2 {
	
	static final String USERNAME = ""; //add your salesforce username here
	static final String PASSWORD = ""; //add your salesforce password here
	static final String LOGINURL = "https://na73.salesforce.com";
	static final String GRANTSERVICE = "/services/oauth2/token?grant_type=password";
	static final String CLIENTID = ""; //salesforce client id
	static final String CLIENTSECRET = ""; //salesforce client secret
    private static String REST_ENDPOINT = "/services/data" ;
    private static String API_VERSION = "/v41.0" ;
    private static String baseUri;
    private static Header oauthHeader;
    private static Header prettyPrintHeader = new BasicHeader("X-PrettyPrint", "1");
    private static String leadId;
    private static String leadFirstName;
    private static String leadLastName;
    private static String leadCompany;
    String accessToken;
    String instanceUrl;
    String loginURL = LOGINURL +
            GRANTSERVICE +
            "&client_id=" + CLIENTID +
            "&client_secret=" + CLIENTSECRET +
            "&username=" + USERNAME +
            "&password=" + PASSWORD;
	
    @BeforeClass
	public void authenticateUser() {
				
		Response res=given()
			.when()
				.post(loginURL)
			.then()			
				.statusCode(200)
				.extract().response();
											
		JsonPath jsonPath = new JsonPath(res.asString());
		accessToken = jsonPath.getString("access_token");
		instanceUrl= jsonPath.getString("instance_url");
		baseUri = instanceUrl + REST_ENDPOINT + API_VERSION ;
        oauthHeader = new BasicHeader("Authorization", "OAuth " + accessToken) ;       
        System.out.println(baseUri);
        System.out.println(oauthHeader);
		}		
    
    @Test
	public void queryLeads()
	{
    	String query="query?q=SELECT Name,Email FROM User WHERE Email LIKE '%chaitanya%'";
    	String uri = baseUri+query;
    	Response res=given()
        .auth().oauth2(accessToken)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .when().get(uri);
    	System.out.println("SF Response "+res.asString());
	}

}
