package com.dojinyou.devcourse.gccoffeerestapi.order;

import com.dojinyou.devcourse.gccoffeerestapi.order.domain.Order;
import com.dojinyou.devcourse.gccoffeerestapi.order.dto.OrderCreateRequest;
import com.dojinyou.devcourse.gccoffeerestapi.order.dto.OrderProductCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;


@DisplayName("OrderDefaultService 테스트")
@ExtendWith(MockitoExtension.class)
class OrderDefaultServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderDefaultService orderService;

    private final List<OrderProductCreateRequest> orderProductCreateRequests = Arrays.asList(new OrderProductCreateRequest(1L, 3),
                                                                                             new OrderProductCreateRequest(2L, 2));
    private final Order newOrder = Order.from(new OrderCreateRequest("test@dojin.com", "우리집주소", "12345", orderProductCreateRequests));


    @Test
    @DisplayName("create함수는 정상적인 데이터가 들어오면 예외가 발생하지 않는다.")
    void create함수는_정상적인_데이터가_들어오면_예외가_발생하지_않는다() {
        Throwable throwable = catchThrowable(() -> orderService.create(newOrder));

        assertThat(throwable).isNull();
        verify(orderRepository, atLeastOnce()).create(newOrder);
    }
}