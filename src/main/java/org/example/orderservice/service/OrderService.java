package org.example.orderservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.orderservice.dto.CreateOrderRequest;
import org.example.orderservice.entity.IdempotencyRecord;
import org.example.orderservice.entity.Order;
import org.example.orderservice.entity.OrderStatus;
import org.example.orderservice.exception.IdempotencyConflictException;
import org.example.orderservice.repository.IdempotencyRecordRepo;
import org.example.orderservice.repository.OrderRepo;
import org.example.orderservice.utils.HashUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo orderRepo;
    private final IdempotencyRecordRepo  idempotencyRecordRepo;

    @Transactional
    public Order create(CreateOrderRequest request, String idempotence) {
        String hash = HashUtil.hash(request);

        try {
            Optional<IdempotencyRecord> existing = idempotencyRecordRepo.findByUserIdAndIdempotencyKey(request.userId(), idempotence);
            if(existing.isPresent()) {
                if(!existing.get().getRequestHash().equals(hash)) {
                    throw new IdempotencyConflictException(
                            "Idempotency key reused with different payload"
                    );
                }
                return orderRepo.findById(existing.get().getOrderId())
                        .orElseThrow(()-> new IllegalStateException("Order not found for Idempotency Record"));
            }

//            Create new order

            Order order = new Order();
            order.setUserId(request.userId());
            order.setOrderAmount(request.orderAmount());
            order.setCustomerName(request.customerName());
            order.setStatus(OrderStatus.CREATED);

            Order savedOrder = orderRepo.save(order);

//            Save Idempotency Record
            IdempotencyRecord idempotencyRecord = new IdempotencyRecord();
            idempotencyRecord.setUserId(request.userId());
            idempotencyRecord.setIdempotencyKey(idempotence);
            idempotencyRecord.setRequestHash(hash);
            idempotencyRecord.setOrderId(savedOrder.getOrderId());

            idempotencyRecordRepo.save(idempotencyRecord);

            return order;
        }
        catch (DataIntegrityViolationException ex){

            IdempotencyRecord record = idempotencyRecordRepo
                    .findByUserIdAndIdempotencyKey(request.userId(), idempotence)
                    .orElseThrow();
            if(record.getRequestHash().equals(hash)) {
                throw new IdempotencyConflictException(
                        "Idempotency key reused with different payload"
                );
            }

            return orderRepo.findById(record.getOrderId()).orElseThrow(()-> new IllegalStateException("Order not found for Idempotency Record"));
        }

    }

}
