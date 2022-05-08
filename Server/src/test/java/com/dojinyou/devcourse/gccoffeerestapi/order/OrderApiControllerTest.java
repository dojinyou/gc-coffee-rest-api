package com.dojinyou.devcourse.gccoffeerestapi.order;

import com.dojinyou.devcourse.gccoffeerestapi.order.dto.OrderProductCreateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(OrderApiController.class)
@DisplayName("OrderApiController 테스트")
class OrderApiControllerTest {
    public static final String BASE_API_URL = "/api/v1/orders";
    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("주문 추가 시 정상적인 데이터를 입력하는 경우 데이터를 생성하고 201 Create로 응답한다.")
    public void 주문_추가_시_정상적인_데이터를_입력하는_경우_데이터를_생성하고_201_Create로_응답한다() throws Exception {
        List<OrderProductCreateRequest> orderProducts = Arrays.asList(new OrderProductCreateRequest(3, 2),
                                                                      new OrderProductCreateRequest(9, 3));
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", "order@test.com");
        requestBody.put("address", "test address");
        requestBody.put("postcode", "12345");
        requestBody.put("orderProducts", orderProducts);

        var response = mvc.perform(post(BASE_API_URL)
                                           .contentType(MediaType.APPLICATION_JSON)
                                           .content(objectMapper.writeValueAsString(requestBody)));

        response.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("주문 추가 시 주문 상품이 없는 경우 400 Bad Request로 응답한다.")
    public void 주문_추가_시_주문_상품이_없는_경우_400_Bad_Reqeust로_응답한다() throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "test3");
        requestBody.put("category", "TEA");
        requestBody.put("price", 3000);
        requestBody.put("orderProducts", List.of());

        var response = mvc.perform(post("/api/v1/orders")
                                           .contentType(MediaType.APPLICATION_JSON)
                                           .content(objectMapper.writeValueAsString(requestBody)));

        response.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("상품 추가 시 요청 내용이 없는 경우 400 Bad Request로 응답한다.")
    public void 상품_추가_시_요청_내용이_없는_경우_400_Bad_Request로_응답한다() throws Exception {

        var response = mvc.perform(post(BASE_API_URL)
                                           .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("주문 추가 시 주문 상품을 찾을 수 없는 경우 404 Not Found로 응답한다.")
    public void 주문_추가_시_주문_상품을_찾을_수_없는_경우_404_Not_Found로_응답한다() throws Exception {
        List<OrderProductCreateRequest> orderProducts = Arrays.asList(new OrderProductCreateRequest(Long.MAX_VALUE, 2));
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "test1");
        requestBody.put("category", "TEA");
        requestBody.put("price", 3000);
        requestBody.put("orderProducts", orderProducts);

        doThrow(IllegalArgumentException.class).when(orderService).create(any());


        var response = mvc.perform(post(BASE_API_URL)
                                           .contentType(MediaType.APPLICATION_JSON)
                                           .content(objectMapper.writeValueAsString(requestBody)));

        response.andExpect(status().isBadRequest());
    }
}