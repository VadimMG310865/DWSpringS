package org.springboot.diplomwork.service;

import org.springboot.diplomwork.entity.Pokupki;
import org.springboot.diplomwork.entity.Pokupkifilt;
import org.springboot.diplomwork.entity.Post;
import org.springboot.diplomwork.repository.PokRepo;
import org.springboot.diplomwork.repository.PokfiltRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PokfiltService implements IPokfiltService{

    @Autowired
    private PokfiltRepo pokfiltRepo;

    @Override
    public Pokupkifilt savePokupkiFilt(Pokupkifilt pokupkifilt) {
        return pokfiltRepo.save(pokupkifilt);
    }

    @Override
    public List<Pokupkifilt> getAllPokupkiFilt() {
        return pokfiltRepo.findAll();
    }

    @Override
    public Pokupkifilt getPokupkiFiltById(int id) {
        Pokupkifilt pokupkifilt = pokfiltRepo.findById(id).orElse(null);
        return pokupkifilt;
    }

    @Override
    public List<Pokupkifilt> searchPokFilt(String ch) {
        return pokfiltRepo.findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch, ch);

    }


    @Override
    public List<Pokupkifilt> getPokFiltNameUs(String nameP) {
        String nPok = nameP;
        List<Pokupkifilt> pokUsName = null;
        pokUsName = pokfiltRepo.findByUser(nPok);
        return pokUsName;
    }

    @Override
    public List<Pokupkifilt> delPokFiltNameUs() {
        List<Pokupkifilt> pokUsNamef = null;
        pokfiltRepo.deleteAll();
        return pokUsNamef;
    }

    @Override
    public List<Pokupkifilt> searchPokFiltDate(Date dn, Date dk) {
        return pokfiltRepo.findByDateBetween(dn, dk);
    }

    @Override
    public List<Pokupkifilt> searchPokFiltStD(String ch, Date dn, Date dk) {
        return pokfiltRepo.findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCaseAndDateBetween(ch, ch, dn, dk);
    }

//    @Override
//    public List<Pokupkifilt> searchPokFiltDate(Date dn, Date dk) {
//        return pokfiltRepo.findByDateAfterAndDateBefore(dn, dk);
//    }

}
