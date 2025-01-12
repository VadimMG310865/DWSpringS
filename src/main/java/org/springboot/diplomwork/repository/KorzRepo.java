package org.springboot.diplomwork.repository;

import org.springboot.diplomwork.entity.Korz;
import org.springboot.diplomwork.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KorzRepo extends JpaRepository<Korz, Integer> {


    //List<Korz> findByCategory(String category);
    List<Korz> findByUser(String user);
    List<Korz> findAllById(Integer id_user);

}
