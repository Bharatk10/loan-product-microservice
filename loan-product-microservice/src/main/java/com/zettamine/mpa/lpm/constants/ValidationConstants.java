package com.zettamine.mpa.lpm.constants;

public interface ValidationConstants {
	
	public final static String BLANK_ERROR_MESSAGE = "This field cannot be null.";
	
	public final static String STRING_PATTERN_ERROR_MESSAGE = "Only Characters and special characters(.,') are allowed";
	
	public final static String INTREST_PATTERN_ERROR_MESSAGE = "Only Numbers and . are allowed eg:(xx.xx)";
	
	public final static String INTEGER_PATTERN_ERROR_MESSAGE = "Only Numbers are allowed.";
	
	public final static String AMOUNT_PATTERN_ERROR_MESSAGE = "Only Numbers and decimal from two digits to five digits are allowed in the range of(10.00 - 99999.99)";
	
	public final static String MIN_DOWN_PAYEMNT_ERROR_MESSAGE = "Only Numbers and decimal from one  digits to five digits are allowed in the range of(1.00 - 99999.99)";
	
	public final static String MAX_LOAN_AMOUNT_PATTERN_ERROR_MESSAGE = "Only Numbers and decimal upto two digits are allowed in the range of(10000.00 - 999999999999.99)";
	
	public final static String STR_PATTERN_ERROR_MESSAGE = "Only Characters are allowed";
	
	public final static String CREDIT_RANGE_PATTERN_ERROR_MESSAGE = "Only digits are allowed in the range of (300-900)";
	
	public final static String LTV_RANGE_PATTERN_ERROR_MESSAGE = "Only digits are allowed in the range of (80-100)";
	
	public final static String LOCKIN_PERIOD_GREATER_THAN_LOAN_TERM_MESSAGE ="The Lockin period value is greater than Loan Term";
	
	public final static String MINDOWM_PAYMT_GREATER_THAN_MAXLOAN_AMNT_MESSAGE ="The minimum DownPayment is greater than Maximum Loan Amount";
	
	public final static String MINDOWN_PAYMT_TYPE_MESSAGE ="It accepts only two values[Absolute,Percentage]";
	
	public final static String MINIMUM_PENALITY_AMOUNT_GREATER_THAN_MAX_LOAN_AMNT_MESSAGE = "The Miminum Penality Amount is greater than Maximum Loan Amount";

	public static final String PENALITY_PERCENT_NULL_MESSAGE = "If Minimum Penality Amount is there then Prepay Penality Percentage can't be Empty";
	
	public static final String MIN_PENALITY_AMOUNT_NULL_MESSAGE = "If Prepay Penality Percentage is there then Minimum Penality Amount can't be Empty";
	
	public final static String ORIGINATION_FEE_GREATER_THAN_MAX_LOAN_AMNT_MESSAGE = "The Origination fee is greater than Maximum Loan Amount";

	
	

}
