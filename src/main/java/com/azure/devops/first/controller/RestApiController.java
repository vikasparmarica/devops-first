package com.azure.devops.first.controller;

import com.azure.devops.first.model.User;
import com.azure.devops.first.service.UserService;
import com.azure.devops.first.util.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api")

public class RestApiController {

    private static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user/", method = RequestMethod.GET)
    public ResponseEntity<List<User>> listAllUsers() {
        List<User> users = userService.findAllUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getUser(@PathVariable("id") String id) {
        logger.info("Fetching User with id {}", id);
        User user = userService.findById(id);
        if (user == null) {
            logger.info("User with id {} not found.", id);
            return new ResponseEntity<>(new CustomErrorType("User with id " + id
                    + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @RequestMapping(value = "/user/", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {
        logger.info("Creating User : {}", user);

        if (userService.isUserExist(user)) {
            logger.error("Unable to create. A User with name {} already exist", user.getName());
            return new ResponseEntity<>(new CustomErrorType(
                    "Unable to create. A User with name " +
                    user.getName() + " already exist."), HttpStatus.CONFLICT);
        }
        userService.saveUser(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/user/{id}").buildAndExpand(user.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }


    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@PathVariable("id") String id, @RequestBody User user) {
        logger.info("Updating User with id {}", id);

        User currentUser = userService.findById(id);

        if (!userService.isUserExist(currentUser)) {
            logger.error("Unable to update. User with id {} not found.", id);
            return new ResponseEntity<>(new CustomErrorType("Unable to upate. User with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }

        currentUser.setName(user.getName());
        currentUser.setAge(user.getAge());

        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable("id") String id) {
        logger.info("Fetching & Deleting User with id {}", id);

        User currentUser = userService.findById(id);
        if (!userService.isUserExist(currentUser)) {
            logger.error("Unable to delete. User with id {} not found.", id);
            return new ResponseEntity<>(new CustomErrorType("Unable to delete. User with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        userService.deleteUserById(currentUser);
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    }


    @RequestMapping(value = "/user/", method = RequestMethod.DELETE)
    public ResponseEntity<User> deleteAllUsers() {
        logger.info("Deleting All Users");

        userService.deleteAllUsers();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}