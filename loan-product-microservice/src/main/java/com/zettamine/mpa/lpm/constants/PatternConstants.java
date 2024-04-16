package com.zettamine.mpa.lpm.constants;

public interface PatternConstants {
	
	public static final String STRING_PATTERN ="^[a-zA-Z. ,'--()/]*$";
	
	public static final String ONLY_STRING_PATTERN ="^[a-zA-Z. ,']*$";
	
	public static final String INTEREST_PATTERN = "^\\d{1,2}(\\.\\d{1,2})?$";
	
	public static final String INTEGER_PATTERN ="^[0-9 ]*$";
	
	public static final String MAX_LOAN_AMOUNT_PATTERN = "^(10000.00|([1-9][0-9]{0,11}(?:\\.[0-9]{1,2})?)|(?<!\\.)[1-9][0-8]{9})$";
	
	public static final String STR_PATTERN = "^[a-zA-Z ]*$";
	
	public static final String CREDIT_RANGE_PATTERN = "^(3[0-9]{2}|[4-8][0-9]{2}|900)$";
	
	public static final String LTV_RANGE_PATTERN = "^(8[0-9]|9[0-9]|100)$";

	public static final String AMOUNT_PATTERN = "^(1[0-9]{1,3}|[2-9][0-9]{1,3}|9999)(?:\\.[0-9]{2})?$";
	
	public static final String MIN_DOWN_PAY_PATTERN = "^(?:0|[1-9]\\d*)(?:\\.\\d{1,2})?$";



}
