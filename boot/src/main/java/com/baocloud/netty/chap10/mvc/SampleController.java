package com.baocloud.netty.chap10.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SampleController {
	public SampleController() {
		System.out.println("SampleController .........................");
	}
	
	@RequestMapping("/hello")
	@ResponseBody
	public String hello(){
		return "hello netty spring mvc";
	}

}
