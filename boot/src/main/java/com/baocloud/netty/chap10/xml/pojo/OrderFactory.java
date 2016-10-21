package com.baocloud.netty.chap10.xml.pojo;

public class OrderFactory {
	public static Order create(long orderID) {
		Order order = new Order();
		order.setOrderNumber(orderID);
		order.setTotal(9999.999f);
		Address address = new Address();
		address.setCity("南京市");
		address.setCountry("中国");
		address.setPostCode("123321");
		address.setState("江苏省");
		address.setStreet1("将军大道");
		order.setBillTo(address);
		Customer customer = new Customer();
		customer.setCustomerNumber(orderID);
		customer.setFirstName("李");
		customer.setLastName("井华");
		order.setCustomer(customer);
		order.setShipping(Shipping.INTERNATIONAL_MAIL);
		order.setShipTo(address);
		return order;
	}
}
