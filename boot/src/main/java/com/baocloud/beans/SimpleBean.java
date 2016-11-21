package com.baocloud.beans;

import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.ToString;

@Component("simpleBean")
@Scope(scopeName=org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST)
@Data
@ToString
public class SimpleBean {
	private String name;
	private Date createDate;

}
