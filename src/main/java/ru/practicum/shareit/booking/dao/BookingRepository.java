package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Transactional
    @Modifying
    @Query("update Booking booking set booking.status = ?1  where booking.id = ?2")
    void update(Status status, Integer bookingId);

    List<Booking> findAllByBooker_IdOrderByStartDesc(Integer bookerId);

    List<Booking> findAllByBooker_IdNotAndItemInOrderByStartDesc(Integer userId, List<Item> ownerItems);

    List<Booking> findAllByItem_Id(Integer itemId);

    List<Booking> findAllByItem_IdAndBooker_Id(Integer itemId, Integer bookerId);
}

