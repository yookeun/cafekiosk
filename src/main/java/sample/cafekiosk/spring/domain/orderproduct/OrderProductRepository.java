package sample.cafekiosk.spring.domain.orderproduct;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sample.cafekiosk.spring.domain.order.OrderProduct;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
