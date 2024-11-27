package org.springboot.diplomwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springboot.diplomwork.entity.Category;

public interface CategoryRepository extends JpaRepository <Category, Integer> {
    public Boolean existsByName(String name);
}
