package com.example;

import java.util.List;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assume.assumeThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = { SpringCloudApplication.class }, properties = {
		"spring.cloud.gcp.sql.database-name=code_samples_test_db",
		"spring.cloud.gcp.sql.instance-connection-name=spring-cloud-gcp-ci:us-central1:testpostgres",
		"spring.datasource.username=postgres",
		"spring.datasource.password=test",
		"spring.datasource.continue-on-error=true",
		"spring.datasource.initialization-mode=always"
})
public class SpringCloudApplicationTests {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@BeforeClass
	public static void checkToRun() {
		assumeThat(
				"SQL sample integration tests are disabled. Please use '-Dit.cloudsql=true' "
						+ "to enable them. ",
				System.getProperty("it.cloudsql"), is("true"));
	}

	@After
	public void clearTable() {
		this.jdbcTemplate.execute("DROP TABLE IF EXISTS users");
	}

	@Test
	public void testSqlRowsAccess() {
		ResponseEntity<List<String>> result = this.testRestTemplate.exchange(
				"/getTuples", HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {
				});

		assertThat(result.getBody()).containsExactlyInAnyOrder(
				"[luisao@example.com, Anderson, Silva]",
				"[jonas@example.com, Jonas, Goncalves]",
				"[fejsa@example.com, Ljubomir, Fejsa]");
	}
}
