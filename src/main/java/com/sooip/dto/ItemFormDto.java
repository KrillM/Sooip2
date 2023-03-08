package com.sooip.dto;

import com.sooip.constant.ItemCategory;
import com.sooip.constant.ItemSellStatus;
import com.sooip.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {
    private Long id;

    @NotBlank(message = "상품명을 적어주세요")
    private String itemName;
    @NotNull(message = "가격을 적어주세요")
    private Integer price;
    @NotBlank(message = "상품 설명을 적어주세요")
    private String itemDetail;
    @NotNull(message = "재고는 필수 입력값입니다")
    private Integer stock;

    private ItemSellStatus itemSellStatus;
    private ItemCategory itemCategory;
    private List<ItemImageDto> itemImageDtoList = new ArrayList<>(); // 상품 이미지 정보
    private List<Long> itemImageIds = new ArrayList<>(); // 상품 이미지 아이디
    private static ModelMapper modelMapper = new ModelMapper();
    
    public Item createItem(){
        return modelMapper.map(this, Item.class); // itemFormDto -> Item 연결
    }
    public static ItemFormDto of(Item item){
        return modelMapper.map(item, ItemFormDto.class); // Item -> ItemFormDto 연결
    }
}
