package com.baocloud.netty.chap10.xml.pojo;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Customer {
	private long customerNumber;

    /** Personal name. */
    private String firstName;

    /** Family name. */
    private String lastName;

    /** Middle name(s), if any. */
    private List<String> middleNames;
}
