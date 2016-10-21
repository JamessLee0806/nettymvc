package com.baocloud.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baocloud.dao.ReaderRepository;
import com.baocloud.entity.Reader;

@Controller
public class LoginController {
     @Autowired
	private ReaderRepository readerReposity;
     @RequestMapping("login")
	public String login(Reader reader,Model model){
		reader=readerReposity.findByUsernameAndPassword(reader.getUsername(), reader.getPassword());
		if(reader!=null){
			model.addAttribute("reader", reader);
			return "profile";
		}else{
			return "redirect:loginPage";
		}
	}
     
     @RequestMapping("loginPage")
     public String loginPage(){
    	 return "login";
     }
}
