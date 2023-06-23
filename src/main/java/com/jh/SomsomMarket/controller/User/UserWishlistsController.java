package com.jh.SomsomMarket.controller.User;

import com.jh.SomsomMarket.domain.*;
import com.jh.SomsomMarket.domain.item.GroupItem;
import com.jh.SomsomMarket.domain.item.PersonalItem;
import com.jh.SomsomMarket.service.AccountService;
import com.jh.SomsomMarket.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/user/myPage/wishlist")
@SessionAttributes("userSession")
/* 사용자 위시리스트 */
public class UserWishlistsController {
    private static final String VIEW = "user/myPage/wishlistList";

    @Autowired
    private AccountService accountService;
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    private WishlistService wishlistService;
    public void setWishlistService(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping // 위시리스트 내역 보기
    public String view(HttpServletRequest request, Model model) {
        UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");

        if (userSession == null) {
            return "redirect:/user/loginForm";
        }

        Account account = userSession.getAccount();

        List<PersonalItem> userWishlistPersonalItem = wishlistService.getPersonalWishlist(account.getId());
        List<GroupItem> userWishlistGroupItem = wishlistService.getGroupWishlist(account.getId());

        model.addAttribute("userWishlistPersonalItem", userWishlistPersonalItem);
        model.addAttribute("userWishlistGroupItem", userWishlistGroupItem);

        return VIEW;
    }

    @ResponseBody
    @PostMapping("/add")
    public boolean add(HttpServletRequest request,
                    @RequestParam("itemId") Long itemId,
                    @RequestParam("sellerId") String sellerId) {
        UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");

        if (userSession == null) {
            return false;
        }
        if (sellerId.equals(userSession.getAccount().getId())) {
            return false;
        }

        Account account = userSession.getAccount();

        Wishlist wishlist = wishlistService.addWishlist(account.getId(), itemId);

        if (wishlist != null) {
            return true;
        } else {
            return false;
        }
    }

    @ResponseBody
    @PostMapping("/delete")
    public boolean cancel(HttpServletRequest request,
                    @RequestParam("itemId") Long itemId) {
        UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
        Account account = userSession.getAccount();

        return wishlistService.cancelWishlist(account.getId(), itemId);
    }
}
