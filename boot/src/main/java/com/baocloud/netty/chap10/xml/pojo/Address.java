package com.baocloud.netty.chap10.xml.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Address {
	 /** First line of street information (required). */
    private String street1;

    /** Second line of street information (optional). */
    private String street2;

    private String city;

    /**
     * State abbreviation (required for the U.S. and Canada, optional
     * otherwise).
     */
    private String state;

    /** Postal code (required for the U.S. and Canada, optional otherwise). */
    private String postCode;
    
    private String country;
}
