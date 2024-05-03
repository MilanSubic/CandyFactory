package org.unibl.etf.model;


import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.PrintWriter;



public class UsersService {
	
	public final static String USERS_DATA_FILE="C:\\Users\\HP\\eclipse-workspace\\FactoryRESTServer\\src\\org\\unibl\\etf\\users.json";
	public final static String FACTORY_USERS_DATA_FILE="C:\\Users\\HP\\eclipse-workspace\\FactoryRESTServer\\src\\org\\unibl\\etf\\factory_users.json";

	 List<User> users = userList();


	public User getById(int id) {
		for (User s :users) {
			if (s.getId() == id) {
				return s;
			}
		}
		return new User();
	}
	
	public User getByUsername(String username) {
		for (User s :users) {
			if (s.getUsername().equals(username)) {
				return s;
			}
		}
		return new User();
	}

	

	public boolean update(User student, int id) {
		int index = users.indexOf(new User("ABC Inc","123 Main Street, City, Country","+1234567890","user1","password1","registrovan",false));
		if (index >= 0) {
			users.set(index, student);
			return true;
		} else {
			return false;
		}
	}

	public boolean remove(int id) {
		int index =users.indexOf(new User( "ABC Inc","123 Main Street, City, Country","+1234567890","user1","password1","registrovan",false));
		if (index >= 0) {
			users.remove(index);
			return true;
		} else {
			return false;
		}
	}
	

	public synchronized boolean validateLogin(LoginRequest request) {
		List<User> myObjects = userList();
	    for(User u :myObjects)
	    {
	    	if (u.getUsername().equals(request.username) && u.getPassword().equals(request.password)) {
				System.out.println("logovan5");
				return true;
	    	}
	    }
	    return false;
	}

	public synchronized List<User> userList() {
		ObjectMapper mapper = new ObjectMapper();
		File file = new File(USERS_DATA_FILE);
		List<User> myObjects=null;
		try {
			
			myObjects = Arrays.asList(mapper.readValue(file, User[].class));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myObjects;
	}
	
	public synchronized List<FactoryUser> factoryUserList() {
		ObjectMapper mapper = new ObjectMapper();
		File file = new File(FACTORY_USERS_DATA_FILE);
		List<FactoryUser> myObjects=null;
		try {
			
			myObjects = Arrays.asList(mapper.readValue(file, FactoryUser[].class));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myObjects;
	}
	
	public void writeUserData(User user) {
		ObjectMapper mapper = new ObjectMapper();
		File file = new File(USERS_DATA_FILE);
		try {
			mapper.writeValue(file, user);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void appendWriteToJson(User user) {
		try {
            // Step 1: Read the existing JSON data
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            File jsonFile = new File(USERS_DATA_FILE);

            if (!jsonFile.exists()) {
                throw new IOException("JSON file not found.");
            }

            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, User.class);

            List<User> existingDataList = objectMapper.readValue(jsonFile, listType);

            // Step 3: Append the new data
            existingDataList.add(user);
           

            // Step 4: Write the updated JSON data back to the file
            objectMapper.writeValue(jsonFile, existingDataList);

        } catch (IOException e) {
            e.printStackTrace();
        }

		 
	}
}
