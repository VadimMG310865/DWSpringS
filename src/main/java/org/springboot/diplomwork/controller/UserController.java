package org.springboot.diplomwork.controller;

import jakarta.servlet.http.HttpSession;
import org.springboot.diplomwork.entity.*;
import org.springboot.diplomwork.repository.UserRepo;
import org.springboot.diplomwork.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.support.NullValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CardService cardService;

    @Autowired
    private PostService postService;

    @Autowired
    private KorzServise korzServise;

    @Autowired
    private PokService pokService;

    @Autowired
    private UserService userService;

    @Autowired
    private PokfiltService pokfiltService;


    @ModelAttribute
    public void commonUser(Principal principal, Model model) {
        if (principal != null) {
            String email = principal.getName();
            User user = userRepo.findByEmail(email);
            model.addAttribute("user", user);
        }
    }

    //==== переход на панель пользователя ===========================
    @GetMapping("/")
    public String index(){
        return "user/index";
    }

    //==== переход на профиль пользователя ===========================
    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        String email = principal.getName();
        User user = userRepo.findByEmail(email);
        model.addAttribute("user", user);
        return "user/profile";
    }

    // ========= БАНКОВСКИЕ КАРТЫ ==============================================
    // ========= вывод всех банковских карт по данному пользователю ============
    @GetMapping("/card")

    public String card(Model m){
        m.addAttribute("cards", cardService.getAllCard());
        return "user/card";
    }

    //==== переход на добавление бановской карты ===========================
    @PostMapping("/saveCard")
    public String saveCard(@ModelAttribute Card card, HttpSession session){
        Boolean existCard = cardService.existCard(card.getNum());
        if(existCard){
            session.setAttribute("errorMsg", "Такая карта уже существует");
        } else {
            Card saveCard = cardService.saveCard(card);
            if(saveCard != null){
                session.setAttribute("succMsg", "Карта создана успешно");
            } else {
                session.setAttribute("errorMsg", "Ошибка при создании карты");
            }
        }
        return "redirect:/user/card";
    }
    //======= удаление банковской карты ===============================================
    @GetMapping("/deleteCard/{id}")
    public String deleteCategory(@PathVariable int id, HttpSession session){
        Boolean deleteCard = cardService.deleteCard(id);
        if(deleteCard){
            session.setAttribute("succMsg", "Карта удалена");
        } else {
            session.setAttribute("errorMsg", "Ошибка при удалении карты");
        }
        return "redirect:/user/card";
    }

    //======= редактирование банковской карты ===============================================
    @GetMapping("/loadEditCard/{id}")
    public String loadEditCard(@PathVariable int id, Model m) {
        System.out.println("ID карты: " + cardService.getCardById(id));
        m.addAttribute("card", cardService.getCardById(id));

        return "user/edit_card";
    }

    //======= пополнение счета банковской карты ============================================
    @GetMapping("/loadPopolnCard/{id}")
    public String loadPopolnCard(@PathVariable int id, Model m) {
        //System.out.println("ID карты: " + cardService.getCardById(id));
        m.addAttribute("card", cardService.getCardById(id));

        return "user/popoln_card";
    }


    //======= обновление данных банковской карты ===========================================
    @PostMapping("/updateCard")
    public String updateCard(@ModelAttribute Card card, HttpSession session) {
        Card oldCard = cardService.getCardById(card.getId());
        if (oldCard != null) {
            oldCard.setSumma(card.getSumma());
        }
        Card updateCard = cardService.saveCard(oldCard);
        if (!ObjectUtils.isEmpty(updateCard)) {
            session.setAttribute("succMsg", "Карта обновлена успешно");
        } else {
            session.setAttribute("errorMsg", "Ошибка обновления карты");
        }
        return "redirect:/user/card";
    }
    //====================== КОНЕЦ БАНКОВСКИЕ КАРТЫ ======================================



    //==================================== КОРЗИНЫЫ ======================================
    //======= открытие формы корзины вывод всех товаров в корзине ========================
    @GetMapping("/korz/{id}")
    public String item(@PathVariable int id, Model m){

        Post postById = postService.getPostById(id);
        m.addAttribute("post", postById);
        m.addAttribute("korzs", korzServise.getAllKorz());

        return "user/korz";
    }

    //======= сохранение товара в корзине ===============================================
    @PostMapping("/saveKorz")
    public String saveKorz(@RequestParam String user, @RequestParam int id_prod, @RequestParam int kol, @RequestParam int ost, @ModelAttribute Korz korz, HttpSession session) throws IOException {

        if (ost > kol) {
            Korz saveKorz = korzServise.saveKorz(korz);
            Post oldPost = postService.getPostById(saveKorz.getId_prod());
            oldPost = postService.getPostById(saveKorz.getId_prod());
            oldPost.setOst(oldPost.getOst() - saveKorz.getKol());
            Post updatePost = postService.savePost(oldPost);

//======= ?????????????????????????????? ==============================================
            int it = 0;
            String korzNUs = user;
            System.out.println("Имя для суммы: " + user);
            List<Korz> korzss = korzServise.getKorzNameUs(korzNUs);
            for(var nnn : korzss){
                it = it + (nnn.getKol()*nnn.getPrice());
            }
            System.out.println("Сумма корзины: " + it);
//======= ?????????????????????????????? ===============================================

            if (saveKorz != null) {
                session.setAttribute("succMsg", "Товар в корзину перемещен");
                session.setAttribute("itog", it);
            } else {
                session.setAttribute("errorMsg", "Ошибка перемещения товара в корзину");
            }
            return "redirect:/user/korz/" + saveKorz.getId_prod();

        } else {session.setAttribute("succMsg", "Выбраное количество превышает остаток.");
            return "redirect:/user/korz/" + id_prod;
        }

   }

    //======= удаление товара из корзины ===============================================
    @GetMapping("/deleteProdKorz/{id}")
    public String deleteProdKorz(@PathVariable int id, HttpSession session){
        Korz delKorz = korzServise.getKorzById(id);

        //====== возврат товара на остаток после его удаления из корзины =============
        Post oldPost = postService.getPostById(delKorz.getId_prod());
        oldPost.setOst(oldPost.getOst() + delKorz.getKol());
        Post updatePost = postService.savePost(oldPost);
        //============================================================================
        Boolean deleteProdKorz = korzServise.deleteKorz(id);
        if(deleteProdKorz){
            session.setAttribute("succMsg", "Товар из корзины удален");
        } else {
            session.setAttribute("errorMsg", "Ошибка при удалении товара из корзины");
        }
        return "redirect:/user/korz/" + delKorz.getId_prod();
    }
    //======= редактирование товара из корзиныы ===============================================
    @GetMapping("/loadEditProdKorz/{id}")
    public String loadEditProdKorz(@PathVariable int id, Model m) {

//        public String loadEditProdKorz(@PathVariable int id, Model m, @RequestParam int kol, @RequestParam int ost) {

        m.addAttribute("prkorz", korzServise.getKorzById(id));

        Korz editKorz = korzServise.getKorzById(id);
        //================== ???????????????????????? ================================
        Post ostPost = postService.getPostById(editKorz.getId_prod());
        //int ost = ostPost.getOst();
        m.addAttribute("edost", ostPost.getOst());
        //================== ???????????????????????? ================================

        //====== возврат товара на остаток при его редактировании в корзине ==========
        Post oldPost = postService.getPostById(editKorz.getId_prod());
        oldPost.setOst(oldPost.getOst() + editKorz.getKol());
        Post updatePost = postService.savePost(oldPost);
        //============================================================================
        return "user/edit_korz";
    }

    //======= обновление данных товара из корзиныы ===========================================
    @PostMapping("/updateKorz")
    public String updateKorz(@RequestParam int kol, @RequestParam int ost, @ModelAttribute Korz korz, HttpSession session) {

        int nkol = kol;
        System.out.println("Начальное количество в корзине:" + nkol);
        int razn = ost - nkol;
        System.out.println("Разница остатка в корзине:" + nkol);

//        Korz oldKolKorzN = korzServise.getKorzById(korz.getId());
//        Post oldPostN = postService.getPostById(oldKolKorzN.getId_prod());
//        if ((kol - nkol) < razn) {

            Korz oldKolKorz = korzServise.getKorzById(korz.getId());
            //====== уменьшени товара на остатоке после его редактирования в корзине =====
            Post oldPost = postService.getPostById(oldKolKorz.getId_prod());
            oldPost.setOst(oldPost.getOst() - kol);
            Post updatePost = postService.savePost(oldPost);
            //============================================================================

            if (oldKolKorz != null) {
                oldKolKorz.setKol(korz.getKol());
            }
            Korz updateKorz = korzServise.saveKorz(oldKolKorz);
            if (!ObjectUtils.isEmpty(updateKorz)) {
                session.setAttribute("succMsg", "Товар в корзине обновлен успешно");
            } else {
                session.setAttribute("errorMsg", "Ошибка обновления товара в корзине");
            }
            return "redirect:/user/korz/" + updateKorz.getId_prod();
//        }else {
//            session.setAttribute("ost", ost);
//            session.setAttribute("kol", kol);
//            session.setAttribute("succMsg", "Превышен остаток. уменьшите количество товара в корзине.");
//            return "redirect:/user/edit_korz/" + korz.getId_prod();
//        }
    }

    //======= просмотр корзины из панели пользователя ====================================
    @GetMapping("/view_korz")

    public String viewKorz(Model m){
        m.addAttribute("vkorz", korzServise.getAllKorz());
        return "user/view_korz";
    }
    //======= оплата корзины  ====================================
     @GetMapping("/view_korz_opl")
     public String korzOpl(@ModelAttribute Korz korz, Model m) throws IOException {
//     public String korzOpl(@RequestParam String nuser, @ModelAttribute Korz korz, Model m) throws IOException {
//
         m.addAttribute("vkorz", korzServise.getAllKorz());

         //List<Korz> korzss = korzServise.getKorzNameUs(nuser);

         //return "user/view_korz";
         return "redirect:/user/view_korz";
     }


    //======= открытие формы оплаты товаров в корзине ================

    @GetMapping("/oplkorz")

    public String oplKorznus(@RequestParam String knus, Model m) {

        System.out.println("knus!!!!! " + knus);
        List<Korz> korzss = korzServise.getKorzNameUs(knus);
        int it = 0;
        for(var nnn : korzss){
//            System.out.println("цикл" + nnn.getName());
//            System.out.println("цикл" + nnn.getKol());
//            System.out.println("цикл" + nnn.getPrice());
//            System.out.println("цикл" + nnn.getKol()*nnn.getPrice());
            it = it + (nnn.getKol()*nnn.getPrice());
//            System.out.println("Итог цикл: " + it);
        }

        //============================================================
        List<Card> cards = cardService.getAllCard();

        m.addAttribute("cards", cardService.getAllCard());

        m.addAttribute("itog", it);

        m.addAttribute("vkorz", korzServise.getAllKorz());

        m.addAttribute("nkorz", knus);

        return "user/korz_opl";
    }

    //======= оплата товаров в корзине =============================
    @GetMapping("/opltovkorz")
    public String opltovkorz(@RequestParam String uskorz, @RequestParam int card, @RequestParam int ito, HttpSession session) {


            Card oldcard = cardService.getCardById(card);
        if (oldcard.getSumma() >= ito ) {
            oldcard.setSumma(oldcard.getSumma() - ito);
            Card updateCard = cardService.saveCard(oldcard);

//=================== ?????????????????? ================================

            List<Korz> korzss = korzServise.getKorzNameUs(uskorz);

            int it = 0;
            for (var nnn : korzss) {
                Pokupki pokupki = new Pokupki();

                pokupki.setId_prod(nnn.getId_prod());
                pokupki.setName(nnn.getName());
                pokupki.setKol(nnn.getKol());
                pokupki.setPrice(nnn.getPrice());
                pokupki.setCategory(nnn.getCategory());
                pokupki.setSumma(nnn.getKol() * nnn.getPrice());
                pokupki.setUser(nnn.getUser());
                pokupki.setId_user(nnn.getId_user());
                Date current = new Date();
                pokupki.setDate(current);

                Pokupki upPokupki = pokService.savePokupki(pokupki);
            }

//=================== ?????????????????? ================================

            Boolean deleteKorz = korzServise.delKorz(uskorz);

            session.setAttribute("succMsg", "Товар оплачен. Корзина пустая.");
            //return "user/korz_opl";
            int iid = 44;
            return "redirect:/user/korz/" + iid;
        }else {
            session.setAttribute("succMsg", "На счету не хватает средств");
            return "user/vibprcard";
        }

    }

    //====================== КОНЕЦ КОРЗИНЫ ======================================

    // ========= загрузка формы для отображения покупок фильтр по названию категории и дате ============

    @GetMapping("/pokupki/{nm}")
    public String loadViewPost(@PathVariable String nm, Model m, @RequestParam(defaultValue = "") String ch, @RequestParam (defaultValue = "01/01/2020") String sdn, @RequestParam (defaultValue = "01/01/2028") String sdk) throws ParseException {

        List<Pokupkifilt> deletePokF = pokfiltService.delPokFiltNameUs();
        List<Pokupki> npokupkis = pokService.getPokNameUs(nm);
        for (var nnn : npokupkis) {
            Pokupkifilt pokupkif = new Pokupkifilt();

            pokupkif.setId_prod(nnn.getId_prod());
            pokupkif.setName(nnn.getName());
            pokupkif.setKol(nnn.getKol());
            pokupkif.setPrice(nnn.getPrice());
            pokupkif.setCategory(nnn.getCategory());
            pokupkif.setSumma(nnn.getKol() * nnn.getPrice());
            pokupkif.setUser(nnn.getUser());
            pokupkif.setId_user(nnn.getId_user());
            pokupkif.setDate(nnn.getDate());

            Pokupkifilt upPokupkif = pokfiltService.savePokupkiFilt(pokupkif);
        }

//=================== ?????????????????? ================================

//
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date dn = format.parse(sdn);
        Date dk = format.parse(sdk);
        System.out.println("Преобразованная начальная дата dn: " + dn);
        System.out.println("Преобразованная конечная дата dk: " + dk);


        List<Pokupkifilt> pokupkisf = null;
        int it;

        if (ch != null && ch.length() > 0) {

            pokupkisf = pokfiltService.searchPokFiltStD(ch, dn, dk);
            it = 0;
            for (var nnn : pokupkisf) {
                it = it + (nnn.getKol() * nnn.getPrice());
                System.out.println("Цикл итог c критерием: " + it);
            }
            System.out.println("Итог c критерием: " + it);
            m.addAttribute("posts", pokupkisf);
            m.addAttribute("itsum", it);
            return "user/pokupki";
        } else {
            pokupkisf = pokfiltService.searchPokFiltDate(dn, dk);
            //pokupkisf = pokfiltService.getAllPokupkiFilt();
            it = 0;
            for (var nnn : pokupkisf) {
                it = it + (nnn.getKol() * nnn.getPrice());
                System.out.println("Цикл итог без критерия: " + it);
            }
            m.addAttribute("posts", pokupkisf);
            m.addAttribute("itsum", it);
            return "user/pokupki";
       }
    }

}
