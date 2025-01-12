package org.springboot.diplomwork.service;

import org.springboot.diplomwork.entity.Card;
import org.springboot.diplomwork.entity.Korz;
import org.springboot.diplomwork.entity.Pokupki;
import org.springboot.diplomwork.entity.Post;
import org.springboot.diplomwork.repository.CardRepo;
import org.springboot.diplomwork.repository.PokRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PokService implements IPokService{

    @Autowired
    private PokRepo pokRepo;

    @Override
    public Pokupki savePokupki(Pokupki pokupki) {
        return pokRepo.save(pokupki);
    }

    @Override
    public List<Pokupki> getAllPokupki() {return pokRepo.findAll();}

    @Override
    public Pokupki getPokupkiById(int id) {
        Pokupki pokupki = pokRepo.findById(id).orElse(null);
        return pokupki;
    }

    @Override
    public List<Pokupki> searchPok(String ch) {
        return pokRepo.findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch, ch);
    }

    //======= поиск в покупках по имени пользователя =======================
    @Override
    public List<Pokupki> getPokNameUs(String nameP) {

        String nPok = nameP;
        List<Pokupki> pokUsName = null;
        pokUsName = pokRepo.findByUser(nPok);
        return pokUsName;
    }

}
