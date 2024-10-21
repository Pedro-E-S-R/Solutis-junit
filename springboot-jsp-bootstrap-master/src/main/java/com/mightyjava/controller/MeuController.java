package com.mightyjava.controller;

import com.mightyjava.model.Users;
import com.mightyjava.service.UserService;
import com.mightyjava.utils.ErrorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class MeuController {
    private UserService userService;

    @Autowired
    //better go with constructor injection
    public MeuController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/exemplo")
    public String exemplo() {
        return "Exemplo de resposta!";
    }

    @RequestMapping("/login")
    public String login(Model model, String error, String logout) {
        if (error != null && !"".equals(error))
            model.addAttribute("error", "Your username and password is invalid.");
        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");
        return "/user/login";
    }

    @GetMapping("/form")
    public String userForm(Model model) {
        model.addAttribute("isNew", true);
        model.addAttribute("userForm", new Users());
        model.addAttribute("roles", userService.roleList());
        return "user/form";
    }

    @GetMapping("/edit/{id}")
    public String userOne(@PathVariable Long id, Model model) {
        model.addAttribute("isNew", false);
        model.addAttribute("userForm", userService.findOne(id));
        model.addAttribute("roles", userService.roleList());
        return "user/form";
    }

    @GetMapping(value = "/delete/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public @ResponseBody String userDelete(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @PostMapping(value="/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public @ResponseBody String userAdd(@Valid @RequestBody Users user, BindingResult result) {
        if(result.hasErrors()) {
            return ErrorUtils.customErrors(result.getAllErrors());
        } else {
            return userService.addUser(user);
        }
    }

    @GetMapping("/list/{id}")
    public Users findOne(@PathVariable Long id) {
        return userService.findOne(id);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String userList(Model model) {
        model.addAttribute("users", userService.userList());
        return "/user/list";
    }
}