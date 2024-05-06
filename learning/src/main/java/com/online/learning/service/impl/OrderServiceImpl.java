package com.online.learning.service.impl;

import com.online.learning.model.dto.CourseDTO;
import com.online.learning.model.dto.FileDTO;
import com.online.learning.model.dto.OrderDTO;
import com.online.learning.model.entity.Account;
import com.online.learning.model.entity.Course;
import com.online.learning.model.entity.Order;
import com.online.learning.repository.AccountRepository;
import com.online.learning.repository.CourseRepository;
import com.online.learning.repository.OrderRepository;
import com.online.learning.service.FileUploadService;
import com.online.learning.service.OrderService;
import com.online.learning.utils.ConstantUtil;
import com.online.learning.utils.DateUtil;
import com.online.learning.utils.FormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    private FileUploadService fileUploadService;

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order save(OrderDTO orderDTO) {
        Order order = orderRepository.findById(orderDTO.getId()).orElse(null);
        if (order == null) {
            order = new Order();
            order.setStatus(true);
        }
        if (!StringUtils.isEmpty(orderDTO.getPrice())) {
            order.setPrice(orderDTO.getPrice());
        }
        if (!StringUtils.isEmpty(orderDTO.getTimeOrder())) {
            order.setTimeOrder(DateUtil.convertStringToDate(orderDTO.getTimeOrder(), ConstantUtil.DATE_PATTERN));
        }
        if (!StringUtils.isEmpty(orderDTO.getTotalCost())) {
            order.setTotalCost(orderDTO.getTotalCost());
        }
        //Account


        order.setCourse(courseRepository.findById(orderDTO.getCourseId()).orElse(null));
        order.setAccount(accountRepository.findById(orderDTO.getAccountId()).orElse(null));


        if (!StringUtils.isEmpty(orderDTO.isStatus())) {
            order.setStatus(orderDTO.isStatus());
        }

        return orderRepository.save(order);
    }

    @Override
    public Order checkout(OrderDTO orderDTO, CourseDTO courseDTO) {
        Date date = new Date();
        Order order = orderRepository.findById(orderDTO.getId()).orElse(null);
        if (order == null) {
            order = new Order();
            order.setStatus(true);
        }

        order.setTimeOrder(date);
        order.setPrice(FormatUtils.formatNumber(courseDTO.getNewPrice()));
        order.setTotalCost(FormatUtils.formatNumber(courseDTO.getNewPrice()));
        order.setCourse(courseRepository.findById(orderDTO.getCourseDTO().getId()).orElse(null));
        order.setAccount(accountRepository.findById(orderDTO.getAccountDTO().getId()).orElse(null));
        order.setStatus(false);

        if (orderDTO.getInvoiceMul() != null && !ObjectUtils.isEmpty(orderDTO.getInvoiceMul().getOriginalFilename())) {
            FileDTO fileDTOBack = fileUploadService.uploadFile(orderDTO.getInvoiceMul(), ConstantUtil.IMAGE_TYPE_UPLOAD);
            order.setInvoice(fileDTOBack.getPath());
        }

        return orderRepository.save(order);
    }

    @Override
    public Boolean delete(long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return false;
        }
        order.setStatus(false);
        orderRepository.save(order);
        return true;
    }

    @Override
    public Order findById(long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Order> findByAccount(Account account, Pageable pageable) {
        return orderRepository.findByAccount(account, pageable);
    }

    @Override
    public List<Order> findByAccount(Account account) {
        return orderRepository.findByAccount(account);
    }

    @Override
    public List<Order> findByCourse(Course course) {
        return orderRepository.findByCourse(course);
    }

    @Override
    public boolean findCourseInAccount(Course course, Account account) {
        List<Order> ordersByCourse = findByCourse(course);
        boolean isFound = false;
        for (Order order : ordersByCourse) {
            if (order.getAccount().equals(account)) {
                isFound = true;
                break;
            }
        }
        return isFound;
    }

    @Override
    public Order saveEntity(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<Order> findByStatusIsTrue() {
        return orderRepository.findByStatusIsTrue();
    }

}
