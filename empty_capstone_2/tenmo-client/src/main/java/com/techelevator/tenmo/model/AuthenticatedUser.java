package com.techelevator.tenmo.model;

public class AuthenticatedUser {
	
	private static String token;
	private User user;
	
	public static String getToken() {
		return token;
	}
	public void setToken(String token) {
		AuthenticatedUser.token = token;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
