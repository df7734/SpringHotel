package com.example.springhotel.controllers;

import com.example.springhotel.models.Role;
import com.example.springhotel.models.Room;
import com.example.springhotel.models.User;
import com.example.springhotel.repository.RoomRepository;
import com.example.springhotel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@Controller
public class UsersController {
    @Autowired
    private UserRepository userRepository;




    @GetMapping("/users")
    public String UsersMain(Model model) {
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "users-main";
    }

    @GetMapping("/users/add")
    public String UsersAdd(Model model) {
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "users-add";
    }

    @PostMapping("/users/add")
    public String UsersAddPost(@RequestParam String username, @RequestParam String password, @RequestParam String phone) {
        User user = new User(username, password, phone);
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);
        return "redirect:/users";
    }

    @PostMapping("/users/{id}/remove")
    public String UsersRemovePost(@PathVariable(value = "id") long id) {
        userRepository.deleteById(id);
        return "redirect:/users";
    }

    @GetMapping("/users/{id}")
    public String UsersDetails(@PathVariable(value = "id") long id, Model model) {
        if(!userRepository.existsById(id)){
            return "redirect:/users";
        }

        Optional<User> res = userRepository.findById(id);
        ArrayList<User> user = new ArrayList<>();
        res.ifPresent(user::add);
        model.addAttribute("user", user);
        return "users-details";
    }
}
