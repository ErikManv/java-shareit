package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Primary
class BookingServiceImpl implements BookingService  {

    private final UserService userService;

    private final ItemService itemService;

    private final BookingRepository bookingRepository;

    private final ItemRepository itemRepository;

    private final BookingDtoMapper bookingMapper;

    @Override
    public BookingDto addBooking(BookingDtoShort bookingDtoShort, Integer userId) {
        if (bookingDtoShort.getEnd().isBefore(bookingDtoShort.getStart()) ||
                bookingDtoShort.getEnd().isEqual(bookingDtoShort.getStart())) {
            throw new TimelineException("end не может быть раньше start");
        }
        User booker = userService.getUser(userId);
        Item item = itemService.getItem(bookingDtoShort.getItemId());
        if (booker.getId().equals(item.getOwner().getId())) {
            throw new BookingErrorException("Owner не может забронировать собственный item");
        }
        Booking booking = newBooking(bookingDtoShort, booker, item);
        if (item.getAvailable()) {
            return bookingMapper.toBookingDto(bookingRepository.save(booking));
        } else {
            throw new ItemUnvailableException("item не доступен");
        }
    }

    @Override
    public BookingDto approveBooking(Integer bookingId, Integer userId, Boolean approved) {
        Booking booking = getBooking(bookingId);
        Integer itemId = booking.getItem().getId();
        if (booking.getItem().getOwner().getId().equals(userId) && booking.getStatus().equals(Status.APPROVED)) {
            throw new DoubleApprovalException("уже обновлен" + booking.getId());
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
    public List<BookingDto> getAllBookingsOfUser(Integer userId, String state) {
        userService.getUser(userId);
        List<Booking> allUserBookings = bookingRepository.findAllByBooker_IdOrderByStartDesc(userId);
        return getBookingsList(allUserBookings, state);
    }

    @Override
    public List<BookingDto> getAllItemsBookingsOfOwner(Integer userId, String state) {
        userService.getUser(userId);
        List<Item> userItems = itemRepository.findByOwner(userService.getUser(userId));
        if (itemService.personalItems(userId).isEmpty()) {
            throw new ItemNotFoundException("ytn");
        }
        List<Booking> allBookings = bookingRepository.findAllByBooker_IdNotAndItemInOrderByStartDesc(userId, userItems);
        return getBookingsList(allBookings, state);
    }

    @Override
    public Booking getBooking(Integer bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("бронирование не найдено"));
    }

    private Booking newBooking(BookingDtoShort bookingDtoShort, User booker, Item item) {
        return Booking.builder()
            .start(bookingDtoShort.getStart())
            .end(bookingDtoShort.getEnd())
            .item(item)
            .booker(booker)
            .status(Status.WAITING)
            .build();
    }

    private List<BookingDto> mapListToDto(List<Booking> bookingList) {
        return bookingList.stream()
                .map(bookingMapper:: toBookingDto)
                .collect(Collectors.toList());
    }

    private  List<BookingDto> getBookingsList(List<Booking> bookingList, String state) {
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
