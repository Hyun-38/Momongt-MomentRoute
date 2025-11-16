package com.Momongt.Momongt_MomentRoute.repository;

import com.Momongt.Momongt_MomentRoute.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {

    Optional<City> findByName(String name);

    List<City> findByNameIn(List<String> names);
}
