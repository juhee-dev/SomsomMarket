package com.jh.SomsomMarket.service;

import com.jh.SomsomMarket.dao.GroupItemDao;
import com.jh.SomsomMarket.dao.PersonalItemDao;
import com.jh.SomsomMarket.dao.SomsomItemDao;
import com.jh.SomsomMarket.dao.WishlistDao;
import com.jh.SomsomMarket.domain.Wishlist;
import com.jh.SomsomMarket.domain.item.GroupItem;
import com.jh.SomsomMarket.domain.item.PersonalItem;
import com.jh.SomsomMarket.domain.item.SomsomItem;
import com.jh.SomsomMarket.repository.PersonalItemRepository;
import com.jh.SomsomMarket.repository.SomsomItemRepository;
import com.jh.SomsomMarket.repository.WishlistRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;
    public void setWishlistRepository(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    @Autowired
    private WishlistDao wishlistDao;
    public void setWishlistDao(WishlistDao wishlistDao) {
        this.wishlistDao = wishlistDao;
    }

    @Autowired
    private PersonalItemRepository personalItemRepository;
    public void setPersonalItemRepository(PersonalItemRepository personalItemRepository) {
        this.personalItemRepository = personalItemRepository;
    }

    @Autowired
    private PersonalItemDao personalItemDao;
    public void setPersonalItemDao(PersonalItemDao personalItemDao) {
        this.personalItemDao = personalItemDao;
    }

    @Autowired
    private GroupItemDao groupItemDao;

    private SomsomItemDao somsomItemDao;
    @Autowired
    @Setter
    private SomsomItemRepository somsomItemRepository;

    // 사용자 PK로 개인 판매 위시리스트 검색
    public List<PersonalItem> getPersonalWishlist(String id) {
        List<Wishlist> wishlist = wishlistRepository.findByAccountId(id);
        List<PersonalItem> personalItemList = new ArrayList<PersonalItem>();
        for (Wishlist wish : wishlist) {
            Long itemId = wish.getItemId();
            Optional<PersonalItem> personalItem = personalItemRepository.findById(itemId);
            if (personalItem.isPresent()) personalItemList.add(personalItem.get());
        }

        return personalItemList;
    }

    // 사용자 PK와 아이템 PK로 개인 판매 위시리스트 검색
    public Wishlist getPersonalWishlistByAccountAndItem(String id, Long itemId) {
        Optional<Wishlist> wishlist = wishlistRepository.findByAccountIdAndItemId(id, itemId);
        if (wishlist.isPresent()) return wishlist.get();
        return null;
    }


    // 사용자 PK와 아이템 PK로 공동구매 위시리스트 검색
    public Wishlist getGroupWishlistByAccountAndItem(String userId, long itemId) {
        Optional<Wishlist> wishlist = wishlistRepository.findByAccountIdAndItemId(userId, itemId);
        if (wishlist.isPresent()) return wishlist.get();
        return null;
    }

    // 사용자 PK로 학교 위시리스트 검색
    public List<SomsomItem> getSomsomWishList(String id){
        List<Wishlist> wishlist = wishlistRepository.findByAccountId(id);
        List<SomsomItem> somsomItemList = new ArrayList<SomsomItem>();
        for(Wishlist wish : wishlist){
            Long itemId = wish.getItemId();
            Optional<SomsomItem> somsomItem = somsomItemRepository.findById(itemId);
            if(somsomItem.isPresent()){
                somsomItemList.add(somsomItem.get());
            }
        }
        return somsomItemList;
    }

    // 해당 사용자에 학교 아이템 위시리스트 추가
    @Transactional
    public Wishlist addSomsomWishList(String id, Long itemId){
        Wishlist wishlist = getSomsomWishlistByAccountAndItem(id, itemId);
        if(wishlist != null){
            return wishlist;
        }
        somsomItemDao.updateAddItemWishCount(itemId);
        return wishlistDao.insertWishlist(id, itemId);
    }

    // 사용자 PK, 아이템 PK로 학교 위시리스트 검색
    public Wishlist getSomsomWishlistByAccountAndItem(String id, Long itemId) {
        Optional<Wishlist> wishlist = wishlistRepository.findByAccountIdAndItemId(id, itemId);
        if(wishlist.isPresent()){
            return wishlist.get();
        }
        return null;
    }

    // 해당 사용자에 학교 아이템 위시리스트 삭제
    @Transactional
    public boolean cancelSomsomWishlist(String id, Long itemId) {
        Wishlist wishlist = getSomsomWishlistByAccountAndItem(id, itemId);
        System.out.println(wishlist.toString());

        somsomItemDao.updateDeleteItemWishcount(itemId);
        wishlistDao.deleteWishlist(wishlist);
        if (getSomsomWishlistByAccountAndItem(id, itemId) != null) { // 삭제 실패
            return false;
        } else {
            return true;
        }
    }

    // 사용자 PK로 공동구매 위시리스트 검색
    public List<GroupItem> getGroupWishlist(String id) {
        List<Wishlist> wishlist = wishlistRepository.findByAccountId(id);
        List<GroupItem> groupItemList = new ArrayList<>();
        for (Wishlist wish : wishlist) {
            Long itemId = wish.getItemId();
            Optional<GroupItem> groupItem = Optional.ofNullable(groupItemDao.getItem(itemId));
            if (groupItem.isPresent()) groupItemList.add(groupItem.get());
        }

        return groupItemList;
    }

    // 해당 사용자에 해당 아이템 위시리스트 추가
    @Transactional
    public Wishlist addWishlist(String id, Long itemId) {
        Wishlist wishlist = getPersonalWishlistByAccountAndItem(id, itemId);
        if (wishlist != null) {
            return wishlist;
        }
        personalItemDao.updateAddItemWishcount(itemId);
        return wishlistDao.insertWishlist(id, itemId);
    }

    // 해당 사용자에 해당 아이템 위시리스트 삭제
    @Transactional
    public boolean cancelWishlist(String id, Long itemId) {
        Wishlist wishlist = getPersonalWishlistByAccountAndItem(id, itemId);
        System.out.println(wishlist.toString());

        personalItemDao.updateDeleteItemWishcount(itemId);
        wishlistDao.deleteWishlist(wishlist);
        if (getPersonalWishlistByAccountAndItem(id, itemId) != null) { // 삭제 실패
            return false;
        } else {
            return true;
        }
    }
}
