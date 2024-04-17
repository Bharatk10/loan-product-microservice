package com.zettamine.mpa.lpm.constants;

public interface EscrowConstants {
	
		//response message constants

	public static final	String SAVE_SUCCEESS = "Escrow company saved successfully";
	public static final	String SAVE_CONFLICT = "Escrow service area already exists";
	public static final	String UPDATE_SUCCESS = "Escrow company details updated successfully";
	public static final	String ENTITY_NOT_FOUND = "Escrow not found with this ";
	public static final	String NOT_FOUND = "Resource not found with this ";
	public static final	String INTERNAL_SERVER_ERROR = "Internal server error";
	public static final	String INVALID_PHONE_NUMBER = "Please provide valid phone number";
	public static final	String INVALID_EMAIL = "Please provide valid email id";
	public static final	String INVALID_ACCOUNT_NUMBER = "Please provide valid valid account number  12-16 characters required";
	public static final	String INVALID_ESC_LIENCE_ID = "Please provide valid escrow lienece number";
	public static final String INVALID_ZIPCODE = "Please enter a valid zipcode";
	public static final String INVALID_NAME = "Please enter a valid name";
	public static final String BAD_REQUEST = "Please enter a valid name";
	public static final String INVALID_ESCROW_ID = "Please enter valid escrow id";
	public static final String INVALID_LOAN_ID = "Please enter valid loan id";

	
		//validation constants
	public static final String PHONE_REGEX = "^\\d{3}-\\d{3}-\\d{4}$";
	public static final	String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	public static final	String NAMES_REGEX = "^[a-zA-Z ]+$";
	public static final	String ZIPCODE_REGEX =  "^\\d{5}(?:-\\d{4})?$";
	public static final	String ACCOUNT_NUMBER_REGEX = "^\\d{12,16}$";
	public static final	String ESC_LINCENCE_ID_REGEX = "^[A-Za-z0-9]{5,10}$";
	public static final	String ONLY_ALPHA_REGEX = "[^a-z A-Z]";
	public static final	String ONLY_NUMBER_REGEX = "\\d+";
	

		
		//status code constants
	public static final String STATUS_201 = "201";
	public static final String STATUS_200 = "200";
	public static final String STATUS_404 = "404";
	public static final String STATUS_400 = "400";
	public static final String STATUS_409 = "409";
	
	public static final String ESCR_REQ_NOT_EXISTS_MSG = "Escrow Requiremnt not found with the given input data Requirement Name : %s";
	public static final String ESCR_REQ_ALREADY_EXISTS_MSG = "'%s' Escrow Requiremnts are already exist with loan product : %s";


}
