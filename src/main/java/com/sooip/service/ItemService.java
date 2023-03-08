package com.sooip.service;

import com.sooip.dto.ItemFormDto;
import com.sooip.dto.ItemImageDto;
import com.sooip.dto.ItemSearchDto;
import com.sooip.dto.MainItemDto;
import com.sooip.entity.Item;
import com.sooip.entity.ItemImage;
import com.sooip.repository.ItemImageRepository;
import com.sooip.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemImageService itemImageService;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList)
            throws Exception{
        //상품등록
        Item item = itemFormDto.createItem();
        itemRepository.save(item);
        //이미지 등록
        for(int i =0;i<itemImgFileList.size();i++){
            ItemImage itemImage = new ItemImage();
            itemImage.setItem(item);
            if(i==0)
                itemImage.setRepImgYn("Y");
            else
                itemImage.setRepImgYn("N");
            itemImageService.saveItemImage(itemImage,itemImgFileList.get(i));
        }
        return item.getId();
    }

    private final ItemImageRepository itemImageRepository;

    @Transactional(readOnly = true)// 읽기전용 -> 더티체킹(변경감지) -> 성능향상
    public ItemFormDto getItemDetail(Long itemId){
        List<ItemImage> itemImageList = itemImageRepository.findByItemIdOrderByIdAsc(itemId); //DB에서 데이터를 가져온다
        List<ItemImageDto> itemImageDtoList = new ArrayList<>();

        for(ItemImage itemImage: itemImageList){// ItemImg 엔티티를 ItemImgDto 객체를 만들어서 리스트에 추가
            ItemImageDto itemImageDto = ItemImageDto.of(itemImage);
            itemImageDtoList.add(itemImageDto);
        }
        //Item 엔티티 조회 -> 조회X EntityNotFoundException 실행
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImageDtoList(itemImageDtoList);
        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList)
            throws Exception{
        Item item = itemRepository.findById(itemFormDto.getId()).
                orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);

        for(int i =0;i<itemFormDto.getItemImageIds().size();i++){
            System.out.println(itemFormDto.getItemImageIds().get(i));
        }
        List<Long> itemImageIds = itemFormDto.getItemImageIds();
        System.out.println(itemImgFileList.size());
        System.out.println(itemImageIds.get(0));
        for(int i =0; i<itemImgFileList.size();i++){
            itemImageService.updateItemImage(itemImageIds.get(i), itemImgFileList.get(i));
        }
        return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDto,pageable);
    }
    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
        //itemRepository는 custom의 자식이기 때문에 getMainItemPage가 사용이 가능하다.
    }

    // 추가 항목
    @Transactional(readOnly = true)
    public Page<MainItemDto> getCateItemBeverage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getCateItemBeverage(itemSearchDto,pageable);
    }
    @Transactional(readOnly = true)
    public Page<MainItemDto> getCateItemCandy(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getCateItemCandy(itemSearchDto,pageable);
    }
    @Transactional(readOnly = true)
    public Page<MainItemDto> getCateItemCookie(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getCateItemCookie(itemSearchDto,pageable);
    }
    @Transactional(readOnly = true)
    public Page<MainItemDto> getCateItemRamen(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getCateItemRamen(itemSearchDto,pageable);
    }
    @Transactional(readOnly = true)
    public Page<MainItemDto> getCateItemRetort(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getCateItemRetort(itemSearchDto,pageable);
    }
    @Transactional(readOnly = true)
    public Page<MainItemDto> getCateItemOthers(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getCateItemOthers(itemSearchDto,pageable);
    }

}
