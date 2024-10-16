package com.project.service.business;

import com.project.entity.concretes.business.Advert;
import com.project.entity.concretes.business.AdvertType;
import com.project.entity.concretes.business.Category;
import com.project.entity.concretes.business.CategoryPropertyKey;
import com.project.entity.concretes.user.User;
import com.project.repository.business.*;
import com.project.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResetService {

    private final AdvertRepository advertRepository;
    private final ImagesRepository imageRepository;
    private final CategoryRepository categoryRepository;
    private final AdvertTypesRepository advertTypesRepository;
    private final CategoryPropertyKeyRepository categoryPropertyKeyRepository;
    private final CategoryPropertyValueRepository categoryPropertyValueRepository;
    private final FavoriteRepository favoritesRepository;
    private final UserRepository userRepository;
    private final TourRequestRepository tourRequestRepository;

    @Transactional
    public ResponseEntity<String> resetDatabase() {

        categoryPropertyValueRepository.deleteAll();
        favoritesRepository.deleteAll();
        imageRepository.deleteAll();
        tourRequestRepository.deleteAll();

        List<Advert> alladverts= advertRepository.findAll();
        for(Advert advert : alladverts){
            if(!advert.getBuiltIn()){
                advertRepository.delete(advert);
            }
        }

        List<Category> allCategory=categoryRepository.findAll();
        for (Category category:allCategory){
            if (!category.getBuiltIn()){
                categoryRepository.delete(category);
            }
        }

        List<AdvertType> allAdvertTypes = advertTypesRepository.findAll();
            // Silinmesi gereken AdvertType'leri filtreleyin
             List<AdvertType> toBeDeleted = allAdvertTypes.stream()
                .filter(advertType -> !advertType.isBuiltIn()) // Sadece false olanları seç
                .collect(Collectors.toList());
            // Toplu olarak silme işlemi
             advertTypesRepository.deleteAll(toBeDeleted);

        List<CategoryPropertyKey> allCategoryPropertyKey =categoryPropertyKeyRepository.findAll();
        for (CategoryPropertyKey categoryPropertyKey:allCategoryPropertyKey){
            if (!categoryPropertyKey.getBuiltIn()){
                categoryPropertyKeyRepository.delete(categoryPropertyKey);
            }
        }

        List<User> allUser=userRepository.findAll();
        for (User user:allUser){
            if (!user.getIsBuiltIn()){
                userRepository.delete(user);
            }
        }

        return ResponseEntity.ok("Reset DB successfully");

    }
}
