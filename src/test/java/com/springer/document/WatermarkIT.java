package com.springer.document;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jayway.restassured.http.ContentType;

/**
 * Integration test for public api of watermark-service.
 *
 * Starts the web server and triggers watermarking for a document, polls for progress and finally downloads it checking the properties.
 */
public class WatermarkIT {

	private static Main main = new Main();

	private static final String URL = "http://localhost:8080/wm";

	private final int documentId = 2;

	@BeforeClass
	public static void startServer() throws Exception {
		main.start();
	}

	@AfterClass
	public static void stopServer() throws Exception {
		main.stop();
	}

	@Test(timeout = 65000)
	public void shouldWatermarkDocument() throws Exception {

		String ticket = startWatermarking(documentId);

		assertThat(ticket, notNullValue());

		while (!pollFinished(ticket)) {
			Thread.sleep(2000);
		}

		String documentString = downloadDocument(ticket);

		assertThat(documentString, notNullValue());

		assertThat(
			documentString,
			equalTo("{\"id\":2,\"author\":\"Sommer\",\"title\":\"Biologische Meereskunde\",\"watermark\":{\"present\":true},\"topic\":\"SCIENCE\",\"contentType\":\"BOOK\"}"));

	}

	private String startWatermarking(int documentId) {
		return given().log().all().when().contentType(ContentType.JSON).body("{ \"documentId\": " + documentId + " }").post(URL + "/watermarkjobs")
			.then().log().all().statusCode(HttpStatus.SC_OK).extract().path("ticket");
	}

	private Boolean pollFinished(String ticket) {
		String finished =
			given().log().all().when().contentType(ContentType.JSON).get(URL + "/watermarkjobs/" + ticket).then().log().all()
				.statusCode(HttpStatus.SC_OK).extract().path("finished");

		return Boolean.valueOf(finished);
	}

	private String downloadDocument(String ticket) {
		return given().log().all().when().contentType(ContentType.JSON).header("x-wm-ticket", ticket).get(URL + "/documents/" + documentId).then()
			.log().all().statusCode(HttpStatus.SC_OK).extract().asString();
	}

}
