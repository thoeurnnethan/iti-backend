package com.iti.thesis.helicopter.thesis.common.ErrorCode;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

	INVALID_ROLE							("ER000", "Invalid user role"),
	UNCAUGHT_ERROR							("ER001", "Uncaught Error"),
	INCORRECT_PASSWORD						("ER002", "Incorrect Password"),
	USER_LOCK								("ER003", "This user is locked"),
	ROOM_NOT_FOUND							("ER004", "Room ID Information not found !"),
	CLASS_NOT_FOUND							("ER005", "Class ID Information not found !"),
	DEPARTMENT_NOT_FOUND					("ER006", "Department ID Information not found !"),
	SUBJECT_ALREADY_REGISTER				("ER007", "This class subject are already registered, Please try to update it"),
	USER_NOT_FOUND							("ER008", "User not found, Cannot find your user in our system"),
	ONLY_ADMIN_CAN_UPDATE					("ER009", "Only Admin user can reset password but not for other Admin"),
	CANNOT_RESET_ADMIN_PASS					("ER010", "Cannot reset password for Admin user"),
	INVALID_ACCESS							("ER011", "Invalid Access"),
	NEED_PERMISSION							("ER012", "You don't have enough permission for this request !");
	
	private String value;
	private String description;
	
}
