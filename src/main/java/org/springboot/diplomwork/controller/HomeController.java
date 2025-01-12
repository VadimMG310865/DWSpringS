package org.springboot.diplomwork.controller;

import jakarta.servlet.http.HttpSession;
import org.springboot.diplomwork.entity.Category;
import org.springboot.diplomwork.entity.Post;
import org.springboot.diplomwork.repository.UserRepo;
import org.springboot.diplomwork.service.ICategoryService;
import org.springboot.diplomwork.service.IPostService;
import org.springboot.diplomwork.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springboot.diplomwork.entity.User;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IPostService postService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private IUserService userService;

    @ModelAttribute
    public void commonUser(Principal principal, Model model) {
        if (principal != null) {
            String email = principal.getName();
            User user = userRepo.findByEmail(email);
            model.addAttribute("user", user);
        }
    }

    @GetMapping("/")
    public String index(Model m, @RequestParam(value = "category", defaultValue = "") String category, @RequestParam(name="pageNo", defaultValue = "0") Integer pageNo, @RequestParam(name="pageSize", defaultValue = "4") Integer pageSize) {

        List<Category> categories = categoryService.getAllCategory();
        m.addAttribute("categories", categories);
        m.addAttribute("paramValue", category);

        Page<Post> page = postService.getAllPostPagination(pageNo, pageSize, category);
        List<Post> posts = page.getContent();

        m.addAttribute("posts", posts);
        m.addAttribute("postsSize", posts.size());
        m.addAttribute("pageNo", page.getNumber());
        m.addAttribute("pageSize", pageSize);
        m.addAttribute("totalElements", page.getTotalElements());
        m.addAttribute("totalPages", page.getTotalPages());
        m.addAttribute("isLast", page.isLast());
        m.addAttribute("isFirst", page.isFirst());

        return "index";
    }

    @GetMapping("/register")
    public String register() {
        return "register.html";
    }

    @GetMapping("/signin")
    public String login() {
        return "login";
    }


    @GetMapping("/onas")
    public String onas() {
        return "onas";
    }

    // ================ загрузка формы регистрации ===============
    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user, HttpSession session) {
        System.out.println(user);
        User u = userService.saveUser(user);
        if (u != null) {
            session.setAttribute("msg", "Пользователь сохранен успешно");
        }else {
            session.setAttribute("msg", "Ошибка сохранения пользователя");
        }
        return "redirect:/register";
    }

    // ========= загрузка формы с категориями и товарами на главной странице ==========
    @GetMapping("/item/{id}")
    public String item(@PathVariable int id, Model m){
        Post postById = postService.getPostById(id);
         m.addAttribute("post", postById);
        return "view_item";
    }

    // ========= загрузка формы с категориями и товарами на главной странице ==========
    @GetMapping("/search")
    public String searchItem(@RequestParam String ch, Model m) {
        List<Post> searchPosts = postService.searchPost(ch);
        m.addAttribute("posts", searchPosts);
        List<Category> categories = categoryService.getAllCategory();
        m.addAttribute("categories", categories);
        return "index";
    }
}
