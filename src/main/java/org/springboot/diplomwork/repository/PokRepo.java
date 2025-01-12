package org.springboot.diplomwork.repository;

import org.springboot.diplomwork.entity.Korz;
import org.springboot.diplomwork.entity.Pokupki;
import org.springboot.diplomwork.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PokRepo extends JpaRepository<Pokupki, Integer> {
    List<Pokupki> findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch2);
    List<Pokupki> findByUser(String name);
}
