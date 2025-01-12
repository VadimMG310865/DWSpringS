package org.springboot.diplomwork.service;


import org.springboot.diplomwork.entity.Korz;
import org.springboot.diplomwork.repository.KorzRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KorzServise implements IKorzService{

    @Autowired
    private KorzRepo korzRepo;


    @Override
    public Korz saveKorz(Korz korz) {
        return korzRepo.save(korz);
    }

    @Override
    public List<Korz> getAllKorz() {
        return korzRepo.findAll();
    }


    @Override
    public Boolean existKorz(String name) {
        return null;
    }

    @Override
    public Boolean deleteKorz(int id) {
        Korz korz = korzRepo.findById(id).orElse(null);
        if (korz != null) {
            korzRepo.delete(korz);
            return true;
        }
        return false;
    }



    @Override
    public Korz getKorzById(int id) {
        Korz korz = korzRepo.findById(id).orElse(null);
        return korz;
    }

    //=========== ?????????????????????? ==================================
    //=========== ?????????????????????? ==================================
    //=========== ?????????????????????? ==================================
    @Override
    public List<Korz> getKorzIdUs(int idUsKorz) {
        return List.of();
    }

    //======= поиск в корзине по имени пользователя =======================
    @Override
    public List<Korz> getKorzNameUs(String nameK) {

        String nKor = nameK;
        List<Korz> korzUsName = null;
        korzUsName = korzRepo.findByUser(nKor);
        return korzUsName;
    }

    //=========== ?????????????????????? ==================================
    //=========== ?????????????????????? ==================================
    //=========== ?????????????????????? ==================================

    @Override
    public Boolean delKorz(String name) {
        List<Korz> korzsUsName = null;
        korzsUsName = korzRepo.findByUser(name);
        //Korz korz = korzRepo.findById(name).orElse(null);
        if (korzsUsName != null) {
            korzRepo.deleteAll(korzsUsName);
            return true;
        }
        return false;
    }
}
