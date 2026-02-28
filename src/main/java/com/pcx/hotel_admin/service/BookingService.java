package com.pcx.hotel_admin.service;

import com.pcx.hotel_admin.dto.BookingDTO;
import com.pcx.hotel_admin.vo.BookingVO;
import java.util.List;

public interface BookingService {
    BookingVO create(BookingDTO dto);
    BookingVO update(Long id, BookingDTO dto);
    void delete(Long id);
    BookingVO getById(Long id);
    List<BookingVO> listAll();
}
