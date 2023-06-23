package com.jh.SomsomMarket.service;

import com.jh.SomsomMarket.dao.AccountDao;
import com.jh.SomsomMarket.dao.OrderDao;
import com.jh.SomsomMarket.dao.SomsomItemDao;
import com.jh.SomsomMarket.domain.*;
import com.jh.SomsomMarket.domain.item.SomsomItem;
import com.jh.SomsomMarket.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderDao orderDao;
    private final AccountDao accountDao;
    private final SomsomItemDao somsomItemDao;
    private final OrderRepository orderRepository;

    /**
     * 주문
     */
    @Transactional
    public Long insertOrder(String accountId, Long itemId, int count) {

        //엔티티 조회
        Account account = accountDao.findOne(accountId);
        SomsomItem item = somsomItemDao.findOne(itemId);

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(account, orderItem);

        //주문 저장
        orderDao.save(order);

        return order.getId();
    }

    @Transactional
    public Long insertOrderFromCart(String accountId, OrderItem... orderItems) {
        Account account = accountDao.findOne(accountId);

        //주문 생성
        Order order = new Order();
        order.initOrder(account, orderItems);

        orderDao.save(order);

        return order.getId();
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderDao.findOne(orderId);
        //주문 취소
        order.cancel();
    }

//    //검색
//    public List<Order> findOrders(OrderSearch orderSearch) {
//        return orderDao.findAllByString(orderSearch);
//    }
    // 사용자 PK로 구매 내역 리스트 검색
    public List<Order> findOrders(String accountId) {
        return orderRepository.findByAccountId(accountId);
    }

    public Order findOne(Long orderId) {
        return orderDao.findOne(orderId);
    }
}
