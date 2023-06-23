package com.jh.SomsomMarket.controller.Cart;

import com.jh.SomsomMarket.controller.User.UserSession;
import com.jh.SomsomMarket.domain.CartSession.CartSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@SessionAttributes({"sessionCart", "orderForm", "userSession"})
public class ViewCartController { 
	
	@ModelAttribute("sessionCart")
	public CartSession createCart(HttpSession session) {
		CartSession cart = (CartSession)session.getAttribute("sessionCart");
		if (cart == null) cart = new CartSession();
		return cart;
	}
	
	@RequestMapping("/cart")
	public ModelAndView viewCart(
			HttpServletRequest request,
			@RequestParam(value="page", required=false) String page,
			@ModelAttribute("sessionCart") CartSession cart)
			throws Exception {
		UserSession userSession =
			(UserSession) WebUtils.getSessionAttribute(request, "userSession");
		handleRequest(page, cart, userSession);
		return new ModelAndView("items/somsom/cart/cart", "cart", cart);
	}
	
	private void handleRequest(String page, CartSession cart, UserSession userSession)
			throws Exception {
		if (userSession != null) {
			if ("next".equals(page)) {
				userSession.getMySomsomList().nextPage();
			}
			else if ("previous".equals(page)) {
				userSession.getMySomsomList().previousPage();
			}
		}
		if ("nextCart".equals(page)) {
			cart.getCartItemList().nextPage();
		}
		else if ("previousCart".equals(page)) {
			cart.getCartItemList().previousPage();
		}
	}
}
