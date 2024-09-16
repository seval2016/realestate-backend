package com.project.payload.response.business;

import com.project.entity.concretes.business.Advert;
import com.project.entity.concretes.user.User;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TourRequestResponse {
    private Long id;
    private LocalDate tourDate;
    private LocalTime tourTime;
    private Status status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private User ownerUserId;
    private User guestUserId;
    private Advert advertId;
    private String advertTitle;
    private ImagesResponse featuredImage;
    private District advertDistrict;
    private City advertCity;
    private Country advertCountry;

}
