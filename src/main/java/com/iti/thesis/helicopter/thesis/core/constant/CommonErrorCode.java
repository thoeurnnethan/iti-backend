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
	NOT_FOUND                 ("000003", "Not Found Error"),
	DUPLICATED_DATA           ("000004", "Duplicated Data Error"),
	UPDATE_NOT_AFFECT         ("000006", "Update Not Affect Error"),
	CODE                      ("000007", "Code Error"),
	DATE                      ("000008", "Date Error"),
	CURRENCY                  ("000009", "Currency Mismatch Error"),
	TOO_MANY_ROWS             ("000010", "Too Many Rows Error"),
	UPDATE                    ("000011", "Update Error"),
	REGISTER                  ("000012", "Register Error"),
	DELETE                    ("000013", "Delete Error"),
	INQUIRY                   ("000014", "Inquiry Error");
	
	private String code;
	private String description;
	
}
