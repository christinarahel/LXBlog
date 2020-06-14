package com.rahel.lxblog.dto;

public class ResetPasswordForm {
 private String activationCode;
 private String newPassword;
 
public String getActivationCode() {
	return activationCode;
}
public void setActivationCode(String activationCode) {
	this.activationCode = activationCode;
}
public String getNewPassword() {
	return newPassword;
}
public void setNewPassword(String newPassword) {
	this.newPassword = newPassword;
}
}
