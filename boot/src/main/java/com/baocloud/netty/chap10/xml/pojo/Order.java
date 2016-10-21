package com.baocloud.netty.chap10.xml.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Order {
	private long orderNumber;

	private Customer customer;

	/** Billing address information. */
	private Address billTo;

	private Shipping shipping;

	/**
	 * Shipping address information. If missing, the billing address is also
	 * used as the shipping address.
	 */
	private Address shipTo;

	private Float total;
}
