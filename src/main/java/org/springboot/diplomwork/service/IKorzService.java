package org.springboot.diplomwork.service;

import org.springboot.diplomwork.entity.Korz;

import java.util.List;

public interface IKorzService {
    //public Korz saveKorz(Korz korz);
    public Korz saveKorz(Korz korz);
    public List<Korz> getAllKorz();
    public Boolean existKorz(String name);
    public Boolean deleteKorz(int id);
    public Korz getKorzById(int id);

    List<Korz> getKorzNameUs(String nameK);
    List<Korz> getKorzIdUs(int idUsKorz);
    Boolean delKorz(String name);
}
