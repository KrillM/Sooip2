package com.sooip.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sooip.constant.ItemCategory;
import com.sooip.constant.ItemSellStatus;
import com.sooip.dto.ItemSearchDto;
import com.sooip.dto.MainItemDto;
import com.sooip.dto.QMainItemDto;
import com.sooip.entity.Item;
import com.sooip.entity.QItem;
import com.sooip.entity.QItemImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{
    private JPAQueryFactory queryFactory; // 동적쿼리 사용하기 위해 JPAQueryFactory 변수 선언

    // 생성자
    public ItemRepositoryCustomImpl(EntityManager entityManager){
        this.queryFactory = new JPAQueryFactory(entityManager); // JPAQueryFactory 실질적인 객체가 만들어 집니다.
    }
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        return searchSellStatus == null ?
                null : QItem.item.itemSellStatus.eq(searchSellStatus);
        //ItemSellStatus null이면 null 리턴 null 아니면 SELL, SOLD 둘중 하나 리턴
    }
    private BooleanExpression searchCategoryEq(ItemCategory searchCategory){
        return searchCategory == null ?
                null: QItem.item.itemCategory.eq(searchCategory);
    }

    // 추가항목 1 시작
    // 카테고리에 따른 String 값
    private String CategoryEq(ItemCategory itemCategory){
        String Category;
        if(itemCategory.equals(ItemCategory.BEVERAGE)){
            Category="beverage";
        }
        else if(itemCategory.equals(ItemCategory.CANDY)){
            Category="candy";
        }
        else if(itemCategory.equals(ItemCategory.COOKIE)){
            Category="cookie";
        }
        else if(itemCategory.equals(ItemCategory.RAMEN)){
            Category="ramen";
        }
        else if(itemCategory.equals(ItemCategory.RETORT)){
            Category="retort";
        }
        else if(itemCategory.equals(ItemCategory.OTHERS)){
            Category="others";
        }
        return itemCategory.name();
    }
    // 추가항목1 끝

    private  BooleanExpression regDtsAfter(String searchDateType){ // all, 1d, 1w, 1m 6m
        LocalDateTime dateTime = LocalDateTime.now(); // 현재시간을 추출해서 변수에 대입

        if(StringUtils.equals("all",searchDateType) || searchDateType == null){
            return null;
        }else if(StringUtils.equals("1d",searchDateType)){
            dateTime = dateTime.minusDays(1);
        }else if(StringUtils.equals("1w",searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        }else if(StringUtils.equals("1m",searchDateType)){
            dateTime = dateTime.minusMonths(1);
        }else if(StringUtils.equals("6m",searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }
        return QItem.item.registerTime.after(dateTime);
        //dateTime을 시간에 맞게 세팅 후 시간에 맞는 등록된 상품이 조회하도록 조건값 반환
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if(StringUtils.equals("itemName",searchBy)){ // 상품명
            return QItem.item.itemName.like("%"+searchQuery+"%");
        }else if(StringUtils.equals("createdBy",searchBy)){ // 작성자
            return QItem.item.createdBy.like("%"+searchQuery+"%");
        }
        return null;
    }
    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        QueryResults<Item> results = queryFactory.selectFrom(QItem.item).
                where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchCategoryEq(itemSearchDto.getSearchCategory()),
                        searchByLike(itemSearchDto.getSearchBy(),itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();
        List<Item> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content,pageable,total);
    }
    private BooleanExpression itemNameLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemName.like("%"+searchQuery+"%");
    }
    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        QItem item = QItem.item;
        QItemImage itemImage = QItemImage.itemImage;

        //QMainItemDto @QueryProjection을 하용하면 DTO로 바로 조회 가능
        QueryResults<MainItemDto> results = queryFactory.select(new QMainItemDto(item.id, item.itemName,
                        item.itemDetail,itemImage.imageUrl,item.price))
                // join 내부조인 .repImgYn.eq("Y") 대표이미지만 가져온다.
                .from(itemImage).join(itemImage.item, item).where(itemImage.repImgYn.eq("Y"))
                .where(itemNameLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();
        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable,total);
    }

    // 추가 항목 2
    @Override
    public Page<MainItemDto> getCateItemBeverage(ItemSearchDto itemSearchDto, Pageable pageable){
        QItem item = QItem.item;
        QItemImage itemImage = QItemImage.itemImage;
        itemSearchDto.setSearchCategory(ItemCategory.BEVERAGE);

        //QMainItemDto @QueryProjection을 하용하면 DTO로 바로 조회 가능
        QueryResults<MainItemDto> results = queryFactory.select(new QMainItemDto(item.id, item.itemName,
                item.itemDetail, itemImage.imageUrl, item.price))
                .from(itemImage).join(itemImage.item, item).where(itemImage.repImgYn.eq("Y"))
                .where(searchCategoryEq(itemSearchDto.getSearchCategory()))
                .orderBy(item.id.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();
        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }
    @Override
    public Page<MainItemDto> getCateItemCandy(ItemSearchDto itemSearchDto, Pageable pageable){
        QItem item = QItem.item;
        QItemImage itemImage = QItemImage.itemImage;
        itemSearchDto.setSearchCategory(ItemCategory.CANDY);

        //QMainItemDto @QueryProjection을 하용하면 DTO로 바로 조회 가능
        QueryResults<MainItemDto> results = queryFactory.select(new QMainItemDto(item.id, item.itemName,
                        item.itemDetail, itemImage.imageUrl, item.price))
                .from(itemImage).join(itemImage.item, item).where(itemImage.repImgYn.eq("Y"))
                .where(searchCategoryEq(itemSearchDto.getSearchCategory()))
                .orderBy(item.id.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();
        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }
    @Override
    public Page<MainItemDto> getCateItemCookie(ItemSearchDto itemSearchDto, Pageable pageable){
        QItem item = QItem.item;
        QItemImage itemImage = QItemImage.itemImage;
        itemSearchDto.setSearchCategory(ItemCategory.COOKIE);

        //QMainItemDto @QueryProjection을 하용하면 DTO로 바로 조회 가능
        QueryResults<MainItemDto> results = queryFactory.select(new QMainItemDto(item.id, item.itemName,
                        item.itemDetail, itemImage.imageUrl, item.price))
                .from(itemImage).join(itemImage.item, item).where(itemImage.repImgYn.eq("Y"))
                .where(searchCategoryEq(itemSearchDto.getSearchCategory()))
                .orderBy(item.id.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();
        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }
    @Override
    public Page<MainItemDto> getCateItemRamen(ItemSearchDto itemSearchDto, Pageable pageable){
        QItem item = QItem.item;
        QItemImage itemImage = QItemImage.itemImage;
        itemSearchDto.setSearchCategory(ItemCategory.RAMEN);

        //QMainItemDto @QueryProjection을 하용하면 DTO로 바로 조회 가능
        QueryResults<MainItemDto> results = queryFactory.select(new QMainItemDto(item.id, item.itemName,
                        item.itemDetail, itemImage.imageUrl, item.price))
                .from(itemImage).join(itemImage.item, item).where(itemImage.repImgYn.eq("Y"))
                .where(searchCategoryEq(itemSearchDto.getSearchCategory()))
                .orderBy(item.id.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();
        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }
    @Override
    public Page<MainItemDto> getCateItemRetort(ItemSearchDto itemSearchDto, Pageable pageable){
        QItem item = QItem.item;
        QItemImage itemImage = QItemImage.itemImage;
        itemSearchDto.setSearchCategory(ItemCategory.RETORT);

        //QMainItemDto @QueryProjection을 하용하면 DTO로 바로 조회 가능
        QueryResults<MainItemDto> results = queryFactory.select(new QMainItemDto(item.id, item.itemName,
                        item.itemDetail, itemImage.imageUrl, item.price))
                .from(itemImage).join(itemImage.item, item).where(itemImage.repImgYn.eq("Y"))
                .where(searchCategoryEq(itemSearchDto.getSearchCategory()))
                .orderBy(item.id.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();
        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }
    @Override
    public Page<MainItemDto> getCateItemOthers(ItemSearchDto itemSearchDto, Pageable pageable){
        QItem item = QItem.item;
        QItemImage itemImage = QItemImage.itemImage;
        itemSearchDto.setSearchCategory(ItemCategory.OTHERS);

        //QMainItemDto @QueryProjection을 하용하면 DTO로 바로 조회 가능
        QueryResults<MainItemDto> results = queryFactory.select(new QMainItemDto(item.id, item.itemName,
                        item.itemDetail, itemImage.imageUrl, item.price))
                .from(itemImage).join(itemImage.item, item).where(itemImage.repImgYn.eq("Y"))
                .where(searchCategoryEq(itemSearchDto.getSearchCategory()))
                .orderBy(item.id.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();
        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }
}
