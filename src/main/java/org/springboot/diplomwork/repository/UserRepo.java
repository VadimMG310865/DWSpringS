package org.springboot.diplomwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springboot.diplomwork.entity.User;

public interface UserRepo extends JpaRepository<User, Long> {

    public User findByEmail(String email);
    //public User findByName(String name);

}
