package com.sinensia.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.web.client.RestClientException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class DemoProjectApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void rootTest(@Autowired TestRestTemplate restTemplate){
		assertThat(restTemplate.getForObject("/",String.class)).isEqualTo("Hola");
	}

	@Test
	void helloTest(@Autowired TestRestTemplate restTemplate){
		assertThat(restTemplate.getForObject("/hello",String.class)).isEqualTo("Hello World!");
	}
	@Test
	void helloNameTest(@Autowired TestRestTemplate restTemplate){
		String[] arr = {"Aleix","Aldomà","Aleix%20Aldomà"};
		for(String name:arr){
			assertThat(restTemplate.getForObject("/hello?name="+name,String.class)).isEqualTo("Hello "+name+"!");
		}
	}
	@Autowired TestRestTemplate restTemplate;
	@ParameterizedTest
	@ValueSource(strings = {"Aleix","Aldomà","Aleix%20Aldomà"})
	void helloNameParam(String name){
		assertThat(restTemplate.getForObject("/hello?name="+name,String.class)).isEqualTo("Hello "+name+"!");
	}

	@ParameterizedTest(name="  [{index}] ({arguments})")
	@CsvSource({
			"a,Hello a!",
			"b,Hello b!",
			"first+last,Hello first last!",
			",Hello null!",
			"'',Hello World!",
	})
	void helloNameParamCsv(String name, String expected){
		assertThat(restTemplate.getForObject("/hello?name="+name,String.class)).isEqualTo(expected);
	}

	@Test
    void canAdd(@Autowired TestRestTemplate restTemplate) {
            assertThat(restTemplate.getForObject("/multiply?n1=1&n2=2", String.class))
                            .isEqualTo("3");
    }
	@Test
    void canAddZero(@Autowired TestRestTemplate restTemplate) {
            assertThat(restTemplate.getForObject("/multiply?n1=0&n2=2", String.class))
                            .isEqualTo("2");
    }
	@Test
    void canAddNegative(@Autowired TestRestTemplate restTemplate) {
            assertThat(restTemplate.getForObject("/multiply?n1=1&n2=-2", String.class))
                            .isEqualTo("-1");
    }
	@Test
    void canAddNull(@Autowired TestRestTemplate restTemplate) {
            assertThat(restTemplate.getForObject("/multiply?n1=&n2=2", String.class))
                            .isEqualTo("2");
    }
	@Test
    void canAddFraction(@Autowired TestRestTemplate restTemplate) {
            assertThat(restTemplate.getForObject("/multiply?n1=1.5&n2=2", String.class))
                            .isEqualTo("3.5");
    }
	@ParameterizedTest
	@CsvSource({
			"1,2,3",
			"1,1,2",
			"1.0,1.0,2",
			"1,-2,-1"
	})
	void canAddParamCsv(String a, String b, String expected){
		assertThat(restTemplate.getForObject("/multiply?n1=" +a+"&n2="+b, String.class))
				.isEqualTo(expected);
	}
	@Test
	void canAddExceptJsonString(){
		assertThat(restTemplate.getForObject("/multiply?n1=string&n2=2", String.class).indexOf("Bad Request"))
				.isGreaterThan(-1);
	}
	@Test
	void canAddFloat(){
		assertThat(restTemplate.getForObject("/multiply?n1=1&n2=2.5", Float.class))
				.isEqualTo(3.5f);
	}
	@Test
	void canAddFloatException(){
		Exception thrown = assertThrows(RestClientException.class, ()->{
			restTemplate.getForObject("/multiply?n1=hola&n2=2", Float.class);
		});
	}
	@ParameterizedTest
	@CsvSource({
			"1,2,3",
			"1,1,2",
			"1.0,1.0,2",
			"1,-2,-1"
	})
	void canAddFloatParamCsv(String a, String b, String expected){
		assertThat(restTemplate.getForObject("/multiply?n1=" +a+"&n2="+b, Float.class))
				.isEqualTo(Float.parseFloat(expected));
	}
	/*@Test
	void canAddInteger(){
		assertThat(restTemplate.getForObject("/add?n1=1.5&n2=2",Integer.class))
				.isEqualTo(3.5f);
	}*/
	@Nested
	@DisplayName("Application tests")
	class appTests{
		@Autowired
		private DemoProjectApplication app;

		@Test
		void appCanAddReturnsInteger(){
			assertThat(app.add(1f,2f)).isEqualTo(3);
		}
		@Test
		void appCanAddReturnsFloat(){
			assertThat(app.add(1.5f,2f)).isEqualTo(3.5f);
		}

		@Test
		void appCanAddNull(){
			Exception thrown = assertThrows(NullPointerException.class, ()->{
				Float ret = (Float) app.add(null,2f);
			});
			assertTrue(thrown.toString().contains("NullPointException"));
			//thrown.getMessage().contains("");
		}

	}

	@Nested
	class MultiplicationTests {

		@DisplayName("multiplication")
		@ParameterizedTest(name="{displayName} [{index}] {0} * {1} = {2}")
		@CsvSource({
				"1,   2,   2",
				"1,   1,   1",
				"1.0, 1.0, 1",
				"1,  -2,  -2",
				"1.5, 2,   3",
				"'',  2,   0",
				"1.5, 1.5, 2.25"
		})
		void canMultiply(String a, String b, String expected) {
			assertThat(restTemplate.getForObject("/multiply?a="+a+"&b="+b, String.class))
					.isEqualTo(expected);
		}

	}
}
