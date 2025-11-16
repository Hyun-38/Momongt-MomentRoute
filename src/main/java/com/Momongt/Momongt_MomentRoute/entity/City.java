package com.Momongt.Momongt_MomentRoute.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "city")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;    // 수원, 성남, 하남 ...

    @Column
    private Double latitude;

    @Column
    private Double longitude;
}
