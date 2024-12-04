package com.mayab.quality.unittest.service;

import com.mayab.quality.unittest.dao.IDAOUser;
import com.mayab.quality.unittest.model.User;

public class LoginService {
	
IDAOUser dao;
	
	public LoginService(IDAOUser d) {
		dao = d;
	}
	
	public boolean login(String email, String pass) {
		User u = dao.findByUserName(email);
		if (u != null) {
			if (u.getPassword()==pass) {
			}
			return true;
		}else {
			return false;
		}
	}

}
