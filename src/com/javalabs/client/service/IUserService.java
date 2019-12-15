package com.javalabs.client.service;

import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import com.javalabs.constants.IConstants;
import com.javalabs.dto.User;

@Path(IConstants.SERVER + IConstants.PATH_USERS)
public interface UserService extends RestService {
	
	@POST
	@Path(IConstants.PATH_ADD_USER)
	public void saveUser(@BeanParam User user, MethodCallback<User> callback);

	@GET
	@Path(IConstants.PATH_GET_ALL_USERS)
	public void getAllUsers(MethodCallback<List<User>> callback);
}
