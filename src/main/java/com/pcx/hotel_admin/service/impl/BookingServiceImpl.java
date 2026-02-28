package com.pcx.hotel_admin.service.impl;

import com.pcx.hotel_admin.dto.BookingDTO;
import com.pcx.hotel_admin.entity.Booking;
import com.pcx.hotel_admin.entity.Channel;
import com.pcx.hotel_admin.entity.RoomType;
import com.pcx.hotel_admin.mapper.BookingMapper;
import com.pcx.hotel_admin.mapper.ChannelMapper;
import com.pcx.hotel_admin.mapper.RoomTypeMapper;
import com.pcx.hotel_admin.service.BookingService;
import com.pcx.hotel_admin.vo.BookingVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private RoomTypeMapper roomTypeMapper;

    @Autowired
    private ChannelMapper channelMapper;

    @Override
    public BookingVO create(BookingDTO dto) {
        Booking booking = new Booking();
        BeanUtils.copyProperties(dto, booking);
        // Generate booking number
        booking.setBookingNo("BK" + System.currentTimeMillis());
        bookingMapper.insert(booking);
        return convertToVO(booking);
    }

    @Override
    public BookingVO update(Long id, BookingDTO dto) {
        Booking existing = bookingMapper.selectById(id);
        if (existing == null) {
            throw new RuntimeException("预订不存在");
        }
        BeanUtils.copyProperties(dto, existing);
        bookingMapper.update(existing);
        return convertToVO(bookingMapper.selectById(id));
    }

    @Override
    public void delete(Long id) {
        bookingMapper.deleteById(id);
    }

    @Override
    public BookingVO getById(Long id) {
        Booking booking = bookingMapper.selectById(id);
        if (booking == null) {
            throw new RuntimeException("预订不存在");
        }
        return convertToVO(booking);
    }

    @Override
    public List<BookingVO> listAll() {
        List<Booking> bookings = bookingMapper.selectAll();
        return bookings.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    private BookingVO convertToVO(Booking booking) {
        BookingVO vo = new BookingVO();
        BeanUtils.copyProperties(booking, vo);

        // Set room type name
        if (booking.getRoomTypeId() != null) {
            RoomType roomType = roomTypeMapper.selectById(booking.getRoomTypeId());
            if (roomType != null) {
                vo.setRoomTypeName(roomType.getTypeName());
            }
        }

        // Set channel name
        if (booking.getChannelId() != null) {
            Channel channel = channelMapper.selectById(booking.getChannelId());
            if (channel != null) {
                vo.setChannelName(channel.getChannelName());
            }
        }

        // Set status name
        vo.setStatusName(getStatusName(booking.getStatus()));

        return vo;
    }

    private String getStatusName(Integer status) {
        if (status == null) return "";
        return switch (status) {
            case 0 -> "待确认";
            case 1 -> "已确认";
            case 2 -> "已入住";
            case 3 -> "已完成";
            case 4 -> "已取消";
            default -> "未知";
        };
    }
}
