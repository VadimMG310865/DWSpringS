package org.springboot.diplomwork.repository;

import org.springboot.diplomwork.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepo extends JpaRepository<Card, Integer> {
    public Boolean existsByNum(String num);
}
