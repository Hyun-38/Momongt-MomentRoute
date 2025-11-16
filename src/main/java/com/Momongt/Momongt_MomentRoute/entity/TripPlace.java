package com.Momongt.Momongt_MomentRoute.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TripPlace {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Plan trip;

    private String name;
    private String type;
    private Double lat;
    private Double lng;

    private Integer orderIndex;
}
