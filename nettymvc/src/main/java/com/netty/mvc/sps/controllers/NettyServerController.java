package com.netty.mvc.sps.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class NettyServerController {
	private final SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	@RequestMapping("/")
	@ResponseBody
	public String serverInfo() throws Exception{
		return "Netty Server "+sdf.format(new Date());
	}

}
