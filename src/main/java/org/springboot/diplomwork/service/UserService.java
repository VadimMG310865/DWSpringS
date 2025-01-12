package org.springboot.diplomwork.service;

import jakarta.servlet.http.HttpSession;
import org.springboot.diplomwork.entity.Post;
import org.springboot.diplomwork.entity.User;
import org.springboot.diplomwork.repository.UserRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        user.setRole("ROLE_USER");
        User newUser = userRepo.save(user);
        return newUser;

    }

    @Override
    public void removeSessionMessage() {
        HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest().getSession();
        session.removeAttribute("msg");
        session.removeAttribute("succMsg");
        session.removeAttribute("errorMsg");
    }

}
