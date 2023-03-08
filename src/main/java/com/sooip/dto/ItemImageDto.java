package com.sooip.dto;

import com.sooip.entity.ItemImage;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemImageDto {
    private Long id;
    private String imageName;
    private String originImageName;
    private String imageUrl;
    private String repImgYn;
    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImageDto of(ItemImage itemImage){
        return modelMapper.map(itemImage, ItemImageDto.class);
    }
}
