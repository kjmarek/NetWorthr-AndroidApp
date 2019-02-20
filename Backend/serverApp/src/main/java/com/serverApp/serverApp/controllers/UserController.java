package com.serverApp.serverApp.controllers;

import com.serverApp.serverApp.models.User;
import com.serverApp.serverApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController{

    @Autowired
    UserRepository userRepo;

    @RequestMapping("/register")
    public String register(@RequestBody User user){
    // System.out.println("Login: Id: " + user.getId() + ": " + user.getFirstName() + " " +
    // user.getLastName());userRepo.save(user);

        userRepo.save(user);
        String rString =
        "{\"error\":\"false\","
            + "\"message\":\"user login success\","
            +  "\"user\":{"
            + "\"id\":\"" + user.getId() + "\"," +
                "\"lastName\":\"" + user.getLastName() + "\"," +
                "\"firstName\":\"" + user.getFirstName() + "\"," +
                "\"email\":\"" + user.getEmail() + "\"," +
                "\"type\":\"" + user.getType() + "\"}}";
        System.out.println(rString);
        return rString;
    }

    @RequestMapping("/login")
    public String login(@RequestBody User user){
        User retrievedUser = userRepo.getUser(user.getEmail(), user.getPassword());

        String rString =
                "{\"error\":\"false\","
                        + "\"message\":\"user login success\","
                        +  "\"user\":{"
                        + "\"id\":\"" + retrievedUser.getId() + "\"," +
                        "\"lastName\":\"" + retrievedUser.getLastName() + "\"," +
                        "\"firstName\":\"" + retrievedUser.getFirstName() + "\"," +
                        "\"email\":\"" + retrievedUser.getEmail() + "\"," +
                        "\"type\":\"" + retrievedUser.getType() + "\"}}";
        System.out.println("Login: " + retrievedUser.getEmail() + ", " + retrievedUser.getPassword());
        return rString;
    }
}
