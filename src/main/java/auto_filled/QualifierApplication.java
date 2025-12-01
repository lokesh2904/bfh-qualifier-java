package auto_filled;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class QualifierApplication implements CommandLineRunner {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(QualifierApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {


		String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

		RestTemplate client = restTemplate();

		Map<String, String> body = new HashMap<>();
		body.put("name", "MAYAKUNTLA LOKESH");
		body.put("regNo", "22BCE9911");
		body.put("email", "lokelucky2004@gmail.com");

		ResponseEntity<Map> response = client.postForEntity(url, body, Map.class);

		String webhook = response.getBody().get("webhook").toString();
		String accessToken = response.getBody().get("accessToken").toString();

		System.out.println("Webhook URL: " + webhook);
		System.out.println("Access Token: " + accessToken);

		
		String finalQuery = "SELECT d.DEPARTMENT_NAME, "
				+ "ROUND(AVG(DATEDIFF(YEAR, e.DOB, GETDATE())), 2) AS AVERAGE_AGE, "
				+ "STRING_AGG(CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME), ', ') "
				+ "WITHIN GROUP (ORDER BY e.FIRST_NAME) AS EMPLOYEE_LIST "
				+ "FROM DEPARTMENT d "
				+ "JOIN EMPLOYEE e ON d.DEPARTMENT_ID = e.DEPARTMENT "
				+ "JOIN PAYMENTS p ON e.EMP_ID = p.EMP_ID "
				+ "WHERE p.AMOUNT > 70000 "
				+ "GROUP BY d.DEPARTMENT_ID, d.DEPARTMENT_NAME "
				+ "ORDER BY d.DEPARTMENT_ID DESC;";

		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", accessToken);
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, String> submitBody = new HashMap<>();
		submitBody.put("finalQuery", finalQuery);

		HttpEntity<Map<String, String>> entity = new HttpEntity<>(submitBody, headers);

		ResponseEntity<String> submitResponse = client.postForEntity(webhook, entity, String.class);

		System.out.println("SUBMISSION RESPONSE: " + submitResponse.getBody());
	}
}
