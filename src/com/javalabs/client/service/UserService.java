package com.javalabs.client.service;

import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import com.javalabs.dto.User;

@Path("http://localhost:8088/users")
public interface UserService extends RestService {
	
	@POST
	@Path("/addUser")
	public void saveUser(@BeanParam User user, MethodCallback<User> callback);

	@GET
	@Path("/fetchAllUsers")
	public void getAllUsers(MethodCallback<List<User>> callback);
}
