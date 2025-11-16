package com.Momongt.Momongt_MomentRoute.repository;

import com.Momongt.Momongt_MomentRoute.entity.Place;
import com.Momongt.Momongt_MomentRoute.entity.PlaceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    // 도시 전체 후보
    List<Place> findTop30ByCity_Id(Long cityId);

    // AiService에서 사용
    List<Place> findByCity_Id(Long cityId);

    // 타입, 카테고리로 조금 더 필터링하고 싶을 때
    List<Place> findTop30ByCity_IdAndTypeIn(Long cityId, List<PlaceType> types);

    List<Place> findTop30ByCity_IdAndTypeInAndCategoryIn(
            Long cityId,
            List<PlaceType> types,
            List<String> categories
    );
}
