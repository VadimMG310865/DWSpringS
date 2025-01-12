package org.springboot.diplomwork.service;

import org.springboot.diplomwork.entity.Korz;
import org.springboot.diplomwork.entity.Pokupki;
import org.springboot.diplomwork.entity.Post;

import java.util.Date;
import java.util.List;

public interface IPokService {
        public Pokupki savePokupki(Pokupki pokupki);
        public List<Pokupki> getAllPokupki();
        public Pokupki getPokupkiById(int id);
        public List<Pokupki> searchPok(String ch);
        List<Pokupki> getPokNameUs(String nameP);
//        public List<Pokupki> saveAllPokupki(Pokupki pokupki);
//        public List<Pokupki> getAllPokupkiByIdUser(int id_user);
//        public Pokupki getPokupkiByDate(Date date);
    }
