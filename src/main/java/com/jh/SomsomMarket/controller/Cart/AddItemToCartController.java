package com.jh.SomsomMarket.controller.Cart;

import com.jh.SomsomMarket.domain.CartSession.CartSession;
import com.jh.SomsomMarket.domain.item.SomsomItem;
import com.jh.SomsomMarket.service.AccountService;
import com.jh.SomsomMarket.service.SomsomItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"sessionCart", "orderForm", "userSession"})
public class AddItemToCartController {
    @Autowired
    AccountService accountService;
    @Autowired
    SomsomItemService somsomItemService;

    //카트 객체 생성
    @ModelAttribute("sessionCart")
    public CartSession createCart() {
        return new CartSession();
    }

    //장바구니에 물건 담기
    @RequestMapping("/cart/addItemToCart")
    public ModelAndView handleRequest(
            @RequestParam("workingItemId") Long workingItemId,
            @ModelAttribute("sessionCart") CartSession cart
    ) throws Exception {
        if (cart.containsItemId(workingItemId)) {
            cart.incrementQuantityByItemId(workingItemId);
        }
        else {
            // isInStock is a "real-time" property that must be updated
            // every time an item is added to the cart, even if other
            // item details are cached.

            SomsomItem item = somsomItemService.findOne(workingItemId);
            boolean isInStock = item.isItemInStock(item.getId());
            cart.addItem(item, isInStock);
        }
        return new ModelAndView("items/somsom/cart/cart", "cart", cart);
    }
}
