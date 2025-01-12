package org.springboot.diplomwork.repository;

import org.springboot.diplomwork.entity.Pokupki;
import org.springboot.diplomwork.entity.Pokupkifilt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PokfiltRepo extends JpaRepository<Pokupkifilt, Integer> {
    List<Pokupkifilt> findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch2);
    List<Pokupkifilt> findByUser(String name);
    List<Pokupkifilt> deleteAllByUser(String name);
    List<Pokupkifilt> findByDateAfterAndDateBefore(Date dn, Date dk);
    List<Pokupkifilt> findByDateBetween(Date dn, Date dk);
    List<Pokupkifilt> findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCaseAndDateBetween(String ch, String ch2, Date dn, Date dk);

}
