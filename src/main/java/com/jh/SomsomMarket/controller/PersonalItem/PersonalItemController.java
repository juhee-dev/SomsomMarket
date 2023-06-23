package com.jh.SomsomMarket.controller.PersonalItem;

import com.jh.SomsomMarket.controller.User.UserSession;
import com.jh.SomsomMarket.domain.Account;
import com.jh.SomsomMarket.domain.Wishlist;
import com.jh.SomsomMarket.domain.item.PersonalItem;
import com.jh.SomsomMarket.service.AccountService;
import com.jh.SomsomMarket.service.PersonalItemService;
import com.jh.SomsomMarket.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@SessionAttributes("userSession")
/* 개인거래 게시글 등록, 수정, 삭제를 관리하는 컨트롤러 */
public class PersonalItemController {
    private static final String PERSONAL_REGISTRATION_FORM = "items/personal/itemRegisterForm";
    private static final String PERSONAL_DETAIL_VIEW = "items/personal/detail";
    private static final String LOGIN_FORM = "user/loginForm";

    @Autowired
    private PersonalItemService personalItemService;
    public void setPersonalItemService(PersonalItemService personalItemService) {
        this.personalItemService = personalItemService;
    }

    @Autowired
    private WishlistService wishlistService;
    public void setWishlistService(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @Autowired
    private AccountService accountService;
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/personal/register")
    public ModelAndView showRegisterForm(HttpServletRequest request) {
        UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
        ModelAndView mav = new ModelAndView();

        if (userSession != null) { // 로그인 되어 있으면
            mav.setViewName(PERSONAL_REGISTRATION_FORM);
            mav.addObject("statusString", new String[] {"거래가능", "거래중", "거래완료"});
            PersonalItemRequest personalItem = new PersonalItemRequest();
            personalItem.setItemId((long) -1);
            mav.addObject("personalItem", personalItem);
            return mav;
        } else {
            mav.setViewName(LOGIN_FORM);
            return mav;
        }
    }

    @PostMapping("/personal/register")
    public String register(HttpServletRequest request,
                                 @Valid @ModelAttribute("personalItem") PersonalItemRequest itemRegistReq,
                                 BindingResult result, Model model) throws Exception {
        UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
        Account account = userSession.getAccount();

        if (result.hasErrors()) {
            return "redirect:/personal/register";
        }

        PersonalItem personalItem = personalItemService.registerNewItem(itemRegistReq, account.getId());

        return "redirect:/personal/detail/" + personalItem.getId(); // 추후 수정
        // "redirect:/item/" + personalItem.getId();
    }

    @GetMapping("/user/myPage/sell/personal/update")
    public ModelAndView showUpdateForm(HttpServletRequest request,
                                       @RequestParam("itemId") Long itemId) {
        ModelAndView mav = new ModelAndView();

        PersonalItem personalItem = personalItemService.searchItem(itemId);

        PersonalItemRequest personalItemRequest = new PersonalItemRequest();

        // 기존에 저장해둘 내용
        personalItemRequest.setItemId(personalItem.getId());
        personalItemRequest.setSellerId(personalItem.getSellerId());
        personalItemRequest.setImgName(personalItem.getImgName());
        personalItemRequest.setImgPath(personalItem.getImgPath());

        // 변경 가능한 필드
        personalItemRequest.setTitle(personalItem.getTitle());
        personalItemRequest.setPrice(personalItem.getPrice());
        personalItemRequest.setDescription(personalItem.getDescription());
        if (personalItem.getStatus().toString().equals("INSTOCK")) {
            personalItemRequest.setStatus("거래가능");
        } else if (personalItem.getStatus().toString().equals("ING")) {
            personalItemRequest.setStatus("거래중");
        } else {
            personalItemRequest.setStatus("거래완료");
        }

        mav.setViewName(PERSONAL_REGISTRATION_FORM);
        mav.addObject("statusString", new String[] {"거래가능", "거래중", "거래완료"});
        mav.addObject("personalItem", personalItemRequest);

        return mav;
    }

    @PostMapping("/user/myPage/sell/personal/update")
    public String update(HttpServletRequest request,
                         @Valid @ModelAttribute("personalItem") PersonalItemRequest itemRegistReq,
                         BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addAttribute("itemId", itemRegistReq.getItemId());
            return "redirect:/user/myPage/sell/personal/update";
        }

        PersonalItem personalItem = personalItemService.updateItem(itemRegistReq);

        return "redirect:/personal/detail/" + personalItem.getId(); // 추후 수정
        // "redirect:/item/" + personalItem.getId();
    }

    @RequestMapping("/user/myPage/sell/personal/delete")
    public String delete(HttpServletRequest request,
                         @RequestParam("itemId") Long itemId) {

        personalItemService.deleteItem(itemId);

        return "redirect:/personal/list"; // 추후 수정
    }




    // 여기서부턴 추후 삭제 (확인해보기 위해 추가함)

    // 개인 거래 게시글 리스트
    @GetMapping("/personal/list")
    public String showList(HttpServletRequest request, Model model) {

        List<PersonalItem> personalItemList = personalItemService.personalItemList();

        model.addAttribute("personalItemList", personalItemList);

        return "items/personal/list";
    }

    // 개인 거래 게시글 상세 뷰
    @RequestMapping("/personal/detail/{itemId}")
    public String showPersonalDetail(HttpServletRequest request,
                                     @PathVariable("itemId") Long itemId, Model model) {
        PersonalItem personalItem = personalItemService.searchItem(itemId);
        Account ac = accountService.getAccount(personalItem.getSellerId());
        personalItem.setNickName(ac.getNickName());

        UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
        int isExistWish = 0;

        String userId;
        if (userSession != null) { // 로그인 사용자는 위시리스트 추가되어 있는지 아닌지 확인
            Account account = userSession.getAccount();
            Wishlist wishlist = wishlistService.getPersonalWishlistByAccountAndItem(account.getId(), itemId);
            if (wishlist != null) { // 위시리스트에 추가되어 있으면
                isExistWish = 1;
            }
            userId = account.getId();
        } else {
            userId = "false";
        }

        model.addAttribute("personalItem", personalItem);
        model.addAttribute("isExistWish", isExistWish);
        model.addAttribute("userId", userId);

        return PERSONAL_DETAIL_VIEW;
    }
}
