package com.app.dev.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dev.CommonFunctions.Response;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;



@RestController
@RequestMapping("/products")
public class Products {
	@GetMapping("/all")
	public List<String>  getAll(@RequestHeader Map<String, String> headers) {
		return  List.of("Prod1" , "Prod2" , "Prod3" );
	}
	
}
