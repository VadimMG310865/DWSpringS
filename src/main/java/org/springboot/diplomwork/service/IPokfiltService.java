package org.springboot.diplomwork.service;

import org.springboot.diplomwork.entity.Pokupkifilt;

import java.util.Date;
import java.util.List;

public interface IPokfiltService {
        public Pokupkifilt savePokupkiFilt(Pokupkifilt pokupkifilt);
        public List<Pokupkifilt> getAllPokupkiFilt();
        public Pokupkifilt getPokupkiFiltById(int id);
        public List<Pokupkifilt> searchPokFilt(String ch);
        List<Pokupkifilt> getPokFiltNameUs(String nameP);
        List<Pokupkifilt> delPokFiltNameUs();
        List<Pokupkifilt> searchPokFiltDate(Date dn, Date dk);
        List<Pokupkifilt> searchPokFiltStD(String ch, Date dn, Date dk);

//        List<Pokupkifilt> delPokFiltNameUs(String namePf);
//        public List<Pokupkifilt> saveAllPokupki(Pokupki pokupki);
//        public List<Pokupkifilt> getAllPokupkiByIdUser(int id_user);
//        public Pokupkifilt getPokupkiFiltByDate(Date date);
    }
