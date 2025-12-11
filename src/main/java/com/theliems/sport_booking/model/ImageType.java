package com.theliems.sport_booking.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Image_Type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer image_type_id;

    private String type_name;
}
