package com.project.service.helper;

import com.project.entity.concretes.business.*;
import com.project.entity.concretes.user.User;
import com.project.entity.enums.AdvertStatus;
import com.project.entity.image.Images;
import com.project.exception.BadRequestException;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.mappers.AdvertMapper;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.request.business.AdvertRequest;

import com.project.payload.response.business.image.ImagesResponse;

import com.project.repository.business.AdvertRepository;
import com.project.service.business.CategoryService;
import com.project.service.business.*;
import com.project.service.validator.DateTimeValidator;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AdvertHelper {

    private final AdvertRepository advertRepository;

    private final CategoryService categoryService;
    private final MethodHelper methodHelper;
    private final CityService cityService;
    private final CountryService countryService;
    private final AdvertTypesService advertTypesService;
    private final DateTimeValidator dateTimeValidator;
    private final DistrictService districtService;






    /**
     * İlanın popülarite puanını hesaplar.
     *
     * @param advert İlgili advert entity
     * @return Popülarite puanı (int)
     */
    private int calculatePopularityPoint(Advert advert) {
        int totalTourRequests = advert.getTourRequestList().size();
        int totalViews = advert.getViewCount();
        return (3 * totalTourRequests) + totalViews;
    }


    /**
     * İlan detaylarını alır ve gerekli bilgileri harita olarak döner.
     *
     * @param advertRequest      İlan istek nesnesi
     * @param httpServletRequest HTTP isteği
     * @param detailsMap         Detaylar için harita
     * @return Detaylar haritası
     */
    public Map<String, Object> getAdvertDetails(AdvertRequest advertRequest, HttpServletRequest httpServletRequest, Map<String, Object> detailsMap) {
        if (detailsMap == null) {
            detailsMap = new HashMap<>();
        }
        Optional<Category> category = categoryService.getCategoryById(advertRequest.getCategoryId());
        City city = cityService.getCityById(advertRequest.getCityId());
        User user = methodHelper.getUserByHttpRequest(httpServletRequest);
        Country country = countryService.getCountryById(advertRequest.getCountryId());
        AdvertType advertType = advertTypesService.getAdvertTypeByIdForAdvert(advertRequest.getAdvertTypeId());
        District district = districtService.getDistrictByIdForAdvert(advertRequest.getDistrictId());

        detailsMap.put("category", category);
        detailsMap.put("city", city);
        detailsMap.put("user", user);
        detailsMap.put("country", country);
        detailsMap.put("advertType", advertType);
        detailsMap.put("district", district);

        return detailsMap;
    }

    /**
     * Belirtilen ID'ye sahip ilanı kontrol eder ve var ise döner, yoksa hata fırlatır.
     *
     * @param id İlanın ID'si
     * @return Advert entity
     */
    public Advert isAdvertExistById(Long id) {
        return advertRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_ADVERT_WITH_ID_MESSAGE, id))
        );
    }

    /**
     * Belirtilen tarih aralığı, kategori, tür ve durum bilgileri ile ilan raporu döner.
     *
     * @param date1    Başlangıç tarihi
     * @param date2    Bitiş tarihi
     * @param category Kategori adı
     * @param type     İlan türü
     * @param status   İlan durumu
     * @return İlan listesi
     */
    public List<Advert> getAdvertsReport(String date1, String date2, String category, String type, AdvertStatus status) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime begin = LocalDateTime.parse(date1, formatter);
        LocalDateTime end = LocalDateTime.parse(date2, formatter);
        dateTimeValidator.checkBeginTimeAndEndTime(begin, end);

        categoryService.getCategoryByTitle(category);
        AdvertStatus statusEnum = AdvertStatus.fromValue(status.getValue());
        advertTypesService.findByTitle(type);

        return advertRepository.findByQuery(begin, end, category, type, statusEnum).orElseThrow(
                () -> new BadRequestException(ErrorMessages.ADVERT_NOT_FOUND)
        );
    }

    /**
     * Tüm ilanları döner.
     *
     * @return İlan listesi
     */
    public List<Advert> getAllAdverts() {
        return advertRepository.findAll();
    }

    /**
     * Verilen ilanı veritabanına kaydeder.
     *
     * @param advert Kaydedilecek ilan
     */
    public void saveRunner(Advert advert) {
        advertRepository.save(advert);
    }

    /**
     * Favoriler için belirtilen ID'ye sahip ilanı döner.
     *
     * @param id İlan ID'si
     * @return Advert entity
     */
    public Advert getAdvertForFavorites(Long id) {
        return advertRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ADVERT_NOT_FOUND));
    }

    /**
     * Advert entity'sini ID ile getirir.
     *
     * @param id Advert ID
     * @return Advert entity veya null
     */
    public Advert getAdvertById(Long id) {
        return advertRepository.findById(id).orElse(null);
    }

    /**
     * Featured image'ı getirir.
     *
     * @return ImageResponse veya null
     */
    //!!! Bu metod Advert'teki resimlerden öne çıkanları döndürür


    public ImagesResponse getFeaturedImages(List<Images> images) {
        // Öne çıkan resimler bir mantığa göre seçilebilir, örneğin ilk resim öne çıkan olabilir
        return (ImagesResponse) images.stream()
                .filter(Images::getFeatured) // Eğer resimlerin öne çıkan olduğunu belirten bir alan varsa
                .map(image -> ImagesResponse.builder()
                        .id(image.getId())
                        .build())
                .collect(Collectors.toList());
    }































    /* public   ImagesResponse getFeaturedImages     (List<com.project.entity.concretes.business.Image> images) {
        if (images == null || images.isEmpty()) {
            return null;

        }
        // Featured image seçme mantığı: 'featured' flag'ine göre veya ilk resmi seç
        return images.stream()
                .filter(com.project.entity.concretes.business.Image::getFeatured)
                .findFirst()
                .map(img -> ImagesResponse.builder()
                        .id(img.getId())
                        .name(img.getName())
                        .type(img.getType())
                        .featured(img.getFeatured())
                        .build())

                .orElseGet(() -> {
                    Image firstImage = images.get(0);
                    return ImagesResponse.builder()
                            .id(firstImage.getId())
                            .name(firstImage.getName())
                            .type(firstImage.getType())
                            .featured(firstImage.getFeatured())
                            .build();
                });
    }*/







    public int updateAdvertStatus(int caseNumber, Advert advert) {
        AdvertStatus status;
        switch (caseNumber) {
            case 0:
                status = AdvertStatus.PENDING;
                advert.setIsActive(false);
                System.out.println("Advert status set to PENDING. Advert is now inactive.");
                break;
            case 1:
                status = AdvertStatus.PENDING;
                advert.setIsActive(true);
                System.out.println("Advert status set to ACTIVATED. Advert is now active.");
                break;
            case 2:
                status = AdvertStatus.REJECTED;
                advert.setIsActive(false);
                System.out.println("Advert status set to REJECTED. Advert is inactive.");
                break;
            default:
                System.out.println("Invalid case number.");
                return AdvertStatus.PENDING.getValue();
        }
        return caseNumber;
    }
    @Transactional
    public Page<Advert> getPopularAdverts(int amount, Pageable pageable) {

        return advertRepository.getMostPopularAdverts(amount,pageable);

    }



}
