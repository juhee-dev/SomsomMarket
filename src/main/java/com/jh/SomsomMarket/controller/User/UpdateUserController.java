package com.jh.SomsomMarket.controller.User;

import com.jh.SomsomMarket.domain.Account;
import com.jh.SomsomMarket.service.AccountFormValidator;
import com.jh.SomsomMarket.service.AccountService;
import com.jh.SomsomMarket.service.GroupItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/user/myPage/update")
@SessionAttributes("userSession")
/* 회원 수정 (수정, 비밀번호 변경, 회원 탈퇴) */
public class UpdateUserController {
    private static final String USER_MYPAGE_FORM = "user/myPage/updateForm";
    private static final String USER_PW_CHECK_FORM = "user/myPage/pwCheckForm";

    @Autowired
    private AccountService accountService;
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    private GroupItemService groupItemService;
    public void setGroupItemService(GroupItemService groupItemService) {
        this.groupItemService = groupItemService;
    }

    @Autowired
    private AccountFormValidator validator;
    public void setValidator(AccountFormValidator validator) {
        this.validator = validator;
    }

    @ModelAttribute("accountReq")
    public UserRegistRequest formBacking(HttpServletRequest request) {
        if (request.getMethod().equalsIgnoreCase("GET")) {
            UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
            Account account = userSession.getAccount();

            UserRegistRequest accountReq = new UserRegistRequest(
                    account.getName(), account.getNickName(), account.getId(),
                    account.getEmail(), account.getAddress(), account.getZipcode(),
                    account.getBankName(), account.getBankAccount(), account.getPhone()
            );

            return accountReq;
        }
        return new UserRegistRequest();
    }

    @GetMapping("/pwCheck")
    public String showPwCheckForm(Model model) {
        model.addAttribute("error", false);
        return USER_PW_CHECK_FORM;
    }

    @PostMapping("/pwCheck")
    public String pwCheckSubmit(HttpServletRequest request,
                                @RequestParam("password") String password) throws Exception {
        UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
        Account account = userSession.getAccount();

        if (account.getPassword().equals(password)) { // 비밀번호가 같으면 마이페이지 수정 페이지로
            return "redirect:/user/myPage/update";
        } else { // 틀리면 다시
            return USER_PW_CHECK_FORM;
        }
    }

    @GetMapping
    public String showForm() {
        return USER_MYPAGE_FORM;
    }

    @PostMapping
    public String update(HttpServletRequest request,
                         @Valid @ModelAttribute("accountReq") UserRegistRequest accountReq,
                         BindingResult result) {
        // 이름, 닉네임, 전화번호, 이메일, 주소, 은행명, 계좌번호 변경 가능

        validator.validate(accountReq, result);

        UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");

        Account account = userSession.getAccount();
        account.setNickName(accountReq.getNickName());
        account.setPhone(accountReq.getPhoneNumber());
        account.setEmail(accountReq.getEmail());
        account.setAddress(accountReq.getAddress());
        account.setZipcode(accountReq.getZipcode());
        account.setBankName(accountReq.getBankName());
        account.setBankAccount(accountReq.getBankAccount());

        Account newAccount = accountService.updateAccount(account);
        userSession.setAccount(newAccount);

        return "redirect:/" + "user/myPage";
    }

    @PostMapping("/password")
    @ResponseBody
    public boolean updatePassword( HttpServletRequest request,
                                  @RequestParam("currentPassword") String currentPassword,
                                  @RequestParam("newPassword") String newPassword) {

        UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
        Account account = userSession.getAccount();

        System.out.println(account.getPassword() + ", " + currentPassword + ", " + newPassword);

        if (!account.getPassword().equals(currentPassword)) { // 현재 비밀번호 틀릴 경우
            return false; // 추후 수정
        }

        accountService.updatePassword(account.getId(), newPassword);
        account.setPassword(newPassword);
        userSession.setAccount(account);

        return true;
    }

    @GetMapping("/delete")
    public String delete(HttpServletRequest request, Model model) {
        UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
        Account account = userSession.getAccount();

        if (groupItemService.isExistSellingItem(account.getId())) {
            UserRegistRequest accountReq = new UserRegistRequest(
                    account.getName(), account.getNickName(), account.getId(),
                    account.getEmail(), account.getAddress(), account.getZipcode(),
                    account.getBankName(), account.getBankAccount(), account.getPhone()
            );
            model.addAttribute("accountReq", accountReq);
            model.addAttribute("error", true);

            return USER_PW_CHECK_FORM;
        }

        accountService.deleteAccount(account);

        return "redirect:/" + "user/logout";
    }

    private static boolean isLoggedIn(UserSession userSession) {
        return (userSession != null);
    }
}
