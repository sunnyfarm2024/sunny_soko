package com.sunny.sunnyfarm.repository;

import com.sunny.sunnyfarm.entity.UserPlant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserPlantRepository extends JpaRepository<UserPlant, Integer> {
    @Query("SELECT up FROM UserPlant up WHERE up.userPlantId = :userPlantId")
    UserPlant findByUserPlantId(int userPlantId);

    @Query("""
    SELECT up FROM UserPlant up
    JOIN FETCH up.farm f
    JOIN FETCH f.user u
    JOIN FETCH up.plant p
    WHERE up.userPlantId = :userPlantId
""")
    UserPlant getUserPlants(@Param("userPlantId") int userPlantId);

}
