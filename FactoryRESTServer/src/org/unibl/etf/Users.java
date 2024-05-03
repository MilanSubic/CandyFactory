package org.unibl.etf;

import java.util.ArrayList;
import java.util.List;

public class Users {
    private static List<User> userList;
 
    public Users() {
    	userList = new ArrayList<>();
	}
    public List<User> getUsersList() {
        return userList;
    }
 
    public void setUsersList(List<User> userList) {
        this.userList = userList;
    }
}