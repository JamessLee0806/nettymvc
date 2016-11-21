package com.baocloud.restful;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.baocloud.beans.SimpleBean;
import com.baocloud.dao.ReaderRepository;
import com.baocloud.entity.Reader;

@RestController
@RequestMapping("/reader")
public class ReaderController {
	@Autowired
	private ReaderRepository readerResp;

	@Autowired
	SimpleBean bean;

	@RequestMapping("deleteUsers")
	public String delteAll() {
		readerResp.deleteAll();
		return "success";
	}

	@RequestMapping("init")
	public String initReader() {
		Reader reader = new Reader();
		reader.setFullname("craig");
		reader.setPassword("craig");
		reader.setUsername("craig");
		readerResp.save(reader);
		reader = null;
		reader = new Reader();
		reader.setFullname("walt");
		reader.setPassword("walt");
		reader.setUsername("walt");
		readerResp.save(reader);
		return "success";
	}

	@RequestMapping("login")
	public String login(Reader reader, HttpServletRequest req) {
		Enumeration<String> names = req.getHeaderNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			System.out.println("name=" + name + "    value:" + req.getHeader(name));
		}
		reader = readerResp.findByUsernameAndPassword(reader.getUsername(), reader.getPassword());
		return JSON.toJSONString(reader);

	}

	@RequestMapping("simpleBean")
	public String simpleBean() {
		return bean.toString();
	}
}
