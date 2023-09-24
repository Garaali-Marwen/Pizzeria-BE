package com.example.pizzeria.Repositories;

import com.example.pizzeria.Entities.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    List<Offer> findByBeginDateLessThanEqualAndEndDateGreaterThanEqual(Date beginDate, Date endDate);

}
