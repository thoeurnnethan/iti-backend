package com.iti.thesis.helicopter.thesis.core.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CommonErrorCode {

	UNCAUGHT                  ("000000", "Uncaught Error"),
	MISSSING_REQUIRED_FIELD   ("000001", "Misssing Required Field Error"),
	INVALID_DATA              ("000002", "Invalid Data Error"),
	NOT_FOUND                 ("000003", "Data not found in our Systems"),
	DUPLICATED_DATA           ("000004", "Duplicated data to Register to database"),
	UPDATE_NOT_AFFECT         ("000006", "Update Not Affect Error"),
	CODE                      ("000007", "Code Error"),
	DATE                      ("000008", "Date Time Error"),
	CURRENCY                  ("000009", "Currency Mismatch Error"),
	TOO_MANY_ROWS             ("000010", "Too Many Rows Error"),
	UPDATE                    ("000011", "There are problems while Update data in Database"),
	REGISTER                  ("000012", "There are problems while Register data to Database"),
	DELETE                    ("000013", "There are problems while Delete data from Database"),
	INQUIRY                   ("000014", "There are problems while Retrieve data from Database");
	
	private String code;
	private String description;
	
}
