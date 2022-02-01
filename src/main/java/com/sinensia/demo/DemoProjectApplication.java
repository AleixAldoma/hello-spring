package com.sinensia.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoProjectApplication.class, args);
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name){
		return String.format("Hello %s!", name);
	}

	@GetMapping("/")
	public String empty(){
		return String.format("Hola");
	}

	@GetMapping("/multiply")public Object add(@RequestParam(value = "n1", defaultValue = "0") Float n1, @RequestParam(value = "n2", defaultValue = "0") Float n2) {
		Float sum = n1+n2;
		Float decimals = sum - sum.intValue();
		if(decimals!=0) return sum;
		return Integer.valueOf(sum.intValue());
	}
	@GetMapping("/multiply")public Object multiply(@RequestParam(value = "n1", defaultValue = "0") Float n1, @RequestParam(value = "n2", defaultValue = "0") Float n2) {
		Float mult = n1*n2;
		Float decimals = mult - mult.intValue();
		if(decimals!=0) return mult;
		return Integer.valueOf(mult.intValue());
	}

}
