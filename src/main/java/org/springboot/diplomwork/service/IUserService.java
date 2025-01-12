package org.springboot.diplomwork.service;


import org.springboot.diplomwork.entity.User;

public interface IUserService {
    public User saveUser(User user);

    public void removeSessionMessage();

//    public User getUsById(int id);

}
