package com.ttnm.chillchatting.utils;

import com.ttnm.chillchatting.dtos.SortDto;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/30/22
 * Time      : 10:10
 * Filename  : PageUtils
 */
public class PageUtils {
    public static Pageable createPageable(int page, int size, String sort, String sortColumn) {
        Sort sortable = Sort.by(getOrder(sort, sortColumn));
        return PageRequest.of(page, size, sortable);
    }

    public static Pageable createPageable(int page, int size, List<SortDto> dtos) {
        List<Order> orders = new ArrayList<>();
        dtos.forEach(dto -> getOrder(dto.getSort(), dto.getColumn()));
        Sort sort = Sort.by(orders);
        return PageRequest.of(page, size, sort);
    }

    public static <T> Page<T> convertListToPage(List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }

    public static Order getOrder(String sort, String column) {
        return new Order(getDirection(sort), column);
    }

    public static Direction getDirection(String sort) {
        if (sort.trim().equalsIgnoreCase("asc")) {
            return Direction.ASC;
        }
        return Direction.DESC;
    }
}
