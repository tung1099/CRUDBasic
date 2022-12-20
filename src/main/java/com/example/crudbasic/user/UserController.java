package com.example.crudbasic.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ModelAndView listUser(){
        ModelAndView modelAndView = new ModelAndView("users");
        modelAndView.addObject("user", userService.findAll());
        return modelAndView;
    }

    @GetMapping("/users/new")
    public ModelAndView showNewForm(){
        ModelAndView modelAndView = new ModelAndView("user-form");
        modelAndView.addObject("user", new User());
        modelAndView.addObject("pageTitle", "Add new User");
        return modelAndView;
    }

    @PostMapping("/users/new")
    public ModelAndView saveNewUser(@ModelAttribute("user") User user, RedirectAttributes ra){
        userService.save(user);
        ModelAndView modelAndView = new ModelAndView("redirect:/users");
        modelAndView.addObject("user", new User());
        ra.addFlashAttribute("message", "The user have been saved successfully!");
        return modelAndView;
    }

    @GetMapping("/users/edit/{id}")
    public ModelAndView showEditForm(@PathVariable Long id){
        Optional<User> user = userService.findById(id);
        if (user.isPresent()){
            ModelAndView modelAndView = new ModelAndView("user-form");
            modelAndView.addObject("user", user.get());
            modelAndView.addObject("pageTitle", "Edit User (ID: " + id + ") ");
            return modelAndView;
        } else {
            return new ModelAndView("error404");
        }
    }

    @PostMapping("users/edit")
    public ModelAndView editUser(@ModelAttribute("user") User user, RedirectAttributes ra){
        userService.save(user);
        ModelAndView modelAndView = new ModelAndView("redirect:users");
        modelAndView.addObject("user", user);
        ra.addFlashAttribute("message", "The user have been edited successfully!");
        return modelAndView;
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes ra){
        Optional<User> user = userService.findById(id);
        if (user.isPresent()){
           userService.remove(id);
           ra.addFlashAttribute("message", "The user ID " + id + " has been deleted");
        } else {
            return "redirect:/error404";
        }
        return "redirect:/users";
    }

}
