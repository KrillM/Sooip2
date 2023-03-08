package com.sooip.service;

import com.sooip.entity.ItemImage;
import com.sooip.repository.ItemImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemImageService {
    @Value("${itemImageLocation}") //application.properties에 itemImageLocation
    private String itemImageLocation;
    private final ItemImageRepository itemImageRepository;
    private final FileService fileService;

    public void saveItemImage(ItemImage itemImage, MultipartFile itemImgFile) throws Exception{
        String originImageName = itemImgFile.getOriginalFilename(); // 오리지날 이미지 경로
        String imageName = "";
        String imageUrl = "";
        System.out.println(originImageName);
        //파일 업로드
        if(!StringUtils.isEmpty(originImageName)){// originImageName 문자열로 비어 있지 않으면 실행
            imageName = fileService.uploadFile(itemImageLocation, originImageName,
                    itemImgFile.getBytes());
            System.out.println(imageName);
            imageUrl="/image/picture/"+imageName;
        }
        itemImage.updateItemImage(originImageName, imageName, imageUrl);
        itemImageRepository.save(itemImage);
    }

    public void updateItemImage(Long itemImageId, MultipartFile itemImgFile) throws Exception {

        if (!itemImgFile.isEmpty()) { // 상품의 이미지를 수정한 경우 상품 이미지 업데이트
            ItemImage saveItemImage = itemImageRepository.findById(itemImageId).
                    orElseThrow(EntityNotFoundException::new); // 기존 엔티티 조회
            // 기존에 등록된 상품 이미지 파일이 있는경우 파일 삭제
            if (!StringUtils.isEmpty(saveItemImage.getImageName())) {
                fileService.deleteFile(itemImageLocation + "/" + saveItemImage.getImageName());
            }
            String originImageName = itemImgFile.getOriginalFilename();
            String imageName = fileService.uploadFile(itemImageLocation, originImageName,
                    itemImgFile.getBytes()); // 파일 업로드
            String imageUrl = "/image/picture/" + imageName;
            saveItemImage.updateItemImage(originImageName, imageName, imageUrl);
        }
    }
}
