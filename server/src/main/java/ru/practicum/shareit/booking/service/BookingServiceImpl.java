package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Primary
public class BookingServiceImpl implements BookingService  {

    private final BookingRepository bookingRepository;

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final BookingDtoMapper bookingMapper;

    private static final Logger log = LoggerFactory.getLogger(BookingController.class);

    @Override
    public BookingDto addBooking(BookingDtoInput bookingDtoInput, Integer userId) {
        if (bookingDtoInput.getEnd().isBefore(bookingDtoInput.getStart()) ||
                bookingDtoInput.getEnd().isEqual(bookingDtoInput.getStart())) {
            throw new TimelineException("end не может быть раньше start");
        }
        User booker = getUser(userId);
        Item item = getItem(bookingDtoInput.getItemId());
        if (booker.getId().equals(item.getOwner().getId())) {
            throw new BookingErrorException("Owner не может забронировать собственный item");
        }
        Booking booking = Booking.builder()
            .start(bookingDtoInput.getStart())
            .end(bookingDtoInput.getEnd())
            .item(item)
            .booker(booker)
            .status(Status.WAITING)
            .build();
        if (item.getAvailable()) {
            log.info("бронирование вещи {} создано", bookingDtoInput.getItemId());
            return bookingMapper.toBookingDto(bookingRepository.save(booking));
        } else {
            throw new ItemUnvailableException("item недоступен");
        }
    }

    @Override
    public BookingDto approveBooking(Integer bookingId, Integer userId, Boolean approved) {
        Booking booking = getBooking(bookingId);
        Integer itemId = booking.getItem().getId();
        if (booking.getItem().getOwner().getId().equals(userId) && booking.getStatus().equals(Status.APPROVED)) {
            throw new DoubleApprovalException("уже обновлено " + booking.getId());
        } else if (booking.getItem().getOwner().getId().equals(userId) && approved) {
            booking.setStatus(Status.APPROVED);
            bookingRepository.update(booking.getStatus(), bookingId);
        } else if (booking.getItem().getOwner().getId().equals(userId) && !approved) {
            booking.setStatus(Status.REJECTED);
            bookingRepository.update(booking.getStatus(), bookingId);
        } else {
            throw new BookingErrorException("User with id -" + userId + " isn't owner of item with id - " + itemId);
        }
        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getBookingById(Integer bookingId, Integer userId) {
        Booking booking = getBooking(bookingId);
        if (booking.getBooker().getId().equals(userId) || booking.getItem().getOwner().getId().equals(userId)) {
            return bookingMapper.toBookingDto(booking);
        } else {
            throw new BookingNotFoundException("бронирование" + bookingId + "не найдено");
        }
    }

    @Override
    public List<BookingDto> getAllBookingsOfUser(Integer userId, String state, Integer offset, Integer limit) {
        getUser(userId);
        if ((offset + 1) % limit == 0) {
            offset = ((offset + 1) / limit) - 1;
        } else if ((offset + 1) % limit != 0) {
            offset = ((offset + 1) / limit);
        }
        Page<Booking> allUserBookings = bookingRepository.findAllByBooker_IdOrderByStartDesc(userId, PageRequest.of(offset, limit, Sort.by("id").ascending()));
        return getBookingsList(allUserBookings, state);
    }

    @Override
    public List<BookingDto> getAllItemsBookingsOfOwner(Integer userId, String state, Integer offset, Integer limit) {
        getUser(userId);
        List<Item> userItems = itemRepository.findByOwner(getUser(userId));
        if (userItems.isEmpty()) {
            throw new ItemNotFoundException("Item не найден");
        }
        if ((offset + 1) % limit == 0) {
            offset = ((offset + 1) / limit) - 1;
        } else if ((offset + 1) % limit != 0) {
            offset = ((offset + 1) / limit);
        }

        Page<Booking> allBookings = bookingRepository.findAllByBooker_IdNotAndItemInOrderByStartDesc(userId, userItems, PageRequest.of(offset, limit, Sort.by("id").ascending()));
        return getBookingsList(allBookings, state);
    }


    private Booking getBooking(Integer bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("бронирование " + bookingId + " не найдено"));
    }

    private User getUser(Integer userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("пользователь " + userId + " не найден"));
    }

    private Item getItem(Integer itemId) {
        return itemRepository.findById(itemId)
            .orElseThrow(() -> new ItemNotFoundException("предмет " + itemId + " не найден"));
    }

    private List<BookingDto> mapListToDto(Page<Booking> bookingList) {
        return bookingList.stream()
                .map(bookingMapper:: toBookingDto)
                .collect(Collectors.toList());
    }

    private  List<BookingDto> getBookingsList(Page<Booking> bookingList, String state) {
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case "ALL":
                return mapListToDto(bookingList);
            case "CURRENT":
                return mapListToDto(bookingList).stream()
                        .filter(x -> x.getStart().isBefore(now) && x.getEnd().isAfter(now))
                        .collect(Collectors.toList());
            case "PAST":
                return mapListToDto(bookingList).stream()
                        .filter(x -> x.getEnd().isBefore(now))
                        .collect(Collectors.toList());
            case "FUTURE":
                return mapListToDto(bookingList).stream()
                        .filter(x -> x.getStart().isAfter(now))
                        .collect(Collectors.toList());
            case "WAITING":
                return mapListToDto(bookingList).stream()
                        .filter(x -> x.getStatus().equals(Status.WAITING))
                        .collect(Collectors.toList());
            case "REJECTED":
                return mapListToDto(bookingList).stream()
                        .filter(x -> x.getStatus().equals(Status.REJECTED))
                        .collect(Collectors.toList());
            default:
                throw new StatusDoesntExistException(state);
        }
    }
}
