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
	NEED_PERMISSION							("ER012", "You don't have permission for this request!"),
	DUPLICATE_TEACHER_ID					("ER013", "You have added duplicate Teacher"),
	ALREADY_ADDED_TEACHER					("ER014", "Teacher Already added to Department"),
	DUPLICATE_MANAGER_ID					("ER015", "You have added duplicate Manager"),
	DUPLICATE_STUDENT_ID					("ER016", "You have added duplicate Student"),
	CLASS_ALREADY_EXIST						("ER017", "New Class Information record found in our systems"),
	CLASS_INVALID_UPGRADE					("ER018", "New Class and Old class Information are the same"),
	STUDENT_NOT_IN_CLASS					("ER019", "Some students do not exist in Class"),
	USER_NOT_ACTIVE							("ER020", "User Status not Active"),
	ALREADY_LOGIN							("ER021", "Already Login and Change password"),
	OLD_PASSWORD_NOT_MATCH					("ER022", "Old Password not match"),
	DUPLICATE_TEACHER						("ER023", "Duplicate Teacher to register schedule"),
	DUPLICATE_TIME							("ER024", "Duplicate Time to register schedule"),
	SCHEDULE_DETAIL_NOT_FOUND				("ER025", "Schedule detail not found !"),
	
	// Http Custom error
	UNCAUGHT								("ER400","Uncauge Error while authorize request, Please contact your Administrator!"),
	UNAUTHORIZE								("ER401","Unauthorized Request!"),
	JWT_UNCAUGHT_ERROR						("ER402","Uncauge Error while authorize request (JWT)!"),
	BAD_CREDENTIALS							("ER403","Bad Credential to Authorize Request!");
	
	private String value;
	private String description;
	
}
