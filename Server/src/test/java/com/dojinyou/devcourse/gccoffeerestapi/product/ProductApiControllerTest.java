package com.dojinyou.devcourse.gccoffeerestapi.product;

import com.dojinyou.devcourse.gccoffeerestapi.common.exception.NotFoundException;
import com.dojinyou.devcourse.gccoffeerestapi.product.domain.Category;
import com.dojinyou.devcourse.gccoffeerestapi.product.domain.Product;
import com.dojinyou.devcourse.gccoffeerestapi.product.dto.ProductResponse;
import com.dojinyou.devcourse.gccoffeerestapi.product.dto.ProductUpdateRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductApiController.class)
class ProductApiControllerTest {

    public static final String BASE_API_URL = "/api/v1/products";
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private final LocalDateTime myLocalDateTime = LocalDateTime.of(2022, 5, 8,
                                                                   1, 13,
                                                                   20, 0);

    private Product insertedProduct1 = Product.of(1L, "test1", Category.TEA,
                                                  100L, null,
                                                  myLocalDateTime, myLocalDateTime, false);
    private Product insertedProduct2 = Product.of(2L, "test2", Category.TEA,
                                                  200L, "test description",
                                                  myLocalDateTime.plusHours(1), myLocalDateTime.plusHours(1),
                                                  false);
    private List<Product> allObject = Arrays.asList(insertedProduct1, insertedProduct2);

    @Test
    @DisplayName("전체 상품 조회 시 정상 조회시 상품이 담긴 리스트를 응답한다.")
    public void 전체_상품_조회_시_정상_조회_시_상품_응답객체가_담긴_리스트를_응답한다() throws Exception {
        when(productService.findAll()).thenReturn(allObject);
        String returnJson = toJson(allObject.stream()
                                            .map(ProductResponse::from)
                                            .toList());


        var response = mvc.perform(get(BASE_API_URL));

        response.andExpect(status().isOk())
                .andExpect(content().json(returnJson));
        verify(productService, atLeastOnce()).findAll();
    }

    @Test
    @DisplayName("전체 상품 조회 시 조회될 데이터가 없을 경우 빈 리스트를 응답한다.")
    public void 전체_상품_조회_시_조회될_데이터가_없을_경우_빈_리스트를_응답한다() throws Exception {
        when(productService.findAll()).thenReturn(Arrays.asList());

        var response = mvc.perform(get(BASE_API_URL));

        response.andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("카테고리를 이용한 상품 조회 시 정상 조회될 경우 상품 응답객체가 담긴 리스트를 응답한다.")
    public void 카테고리를_이용한_상품_조회_시_정상_조회될_경우_상품_응답객체가_담긴_리스트를_응답한다() throws Exception {
        List<Product> returnObject = allObject.stream()
                                              .filter(product -> product.getCategory() == Category.TEA)
                                              .toList();
        when(productService.findAllByCategory(Category.TEA)).thenReturn(returnObject);
        String jsonObject = toJson(returnObject.stream()
                                               .map(ProductResponse::from)
                                               .toList());

        var response = mvc.perform(get(BASE_API_URL + "?category=TEA"));

        response.andExpect(status().isOk())
                .andExpect(content().json(jsonObject));
        verify(productService, atLeastOnce()).findAllByCategory(Category.TEA);
    }

    @Test
    @DisplayName("카테고리를 이용한 상품 조회 시 조회된 데이터가 없는 경우 빈 리스트를 응답한다.")
    public void 카테고리를_이용한_상품_조회_시_조회될_데이터가_없는_경우_빈_리스트를_응답한다() throws Exception {
        when(productService.findAllByCategory(Category.COFFEE)).thenReturn(Arrays.asList());

        var response = mvc.perform(get(BASE_API_URL + "?category=COFFEE"));

        response.andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(productService, atLeastOnce()).findAllByCategory(Category.COFFEE);
    }

    @Test
    @DisplayName("카테고리를 이용한 상품 조회 시 잘못된 카테고리를 입력할 경우 400 Bad Request로 응답한다.")
    public void 카테고리를_이용한_상품_조회_시_잘못된_카테고리를_입력할_경우_400_Bad_Reqeust로_응답한다() throws Exception {
        var response = mvc.perform(get("/api/v1/products?category=NotCateogry"));

        response.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이름을 이용한 상품 조회 시 정상 조회될 경우 해당 이름을 가진 객체 데이터를 응답한다.")
    public void 이름을_이용한_상품_조회_시_정상_조회될_경우_해당_이름을_가진_객체_데이터를_응답한다() throws Exception {
        String inputName = "test1";
        Product returnObject = allObject.stream()
                                        .filter(product -> product.getName() == inputName)
                                        .findFirst().get();
        when(productService.findByName(inputName)).thenReturn(returnObject);
        String jsonObject = toJson(ProductResponse.from(returnObject));

        var response = mvc.perform(get(BASE_API_URL + "?name=" + inputName));

        response.andExpect(status().isOk())
                .andExpect(content().json(jsonObject));
        verify(productService, atLeastOnce()).findByName(inputName);
    }

    @Test
    @DisplayName("이름을 이용한 상품 조회 시 조회된 데이터가 없는 경우 빈 리스트를 응답한다.")
    public void 이름을_이용한_상품_조회_시_조회될_데이터가_없는_경우_빈_리스트를_응답한다() throws Exception {
        String inputName = "NotFoundName";
        when(productService.findByName(inputName)).thenThrow(NotFoundException.class);

        var response = mvc.perform(get(BASE_API_URL + "?name=" + inputName));

        response.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("상품 추가 시 정상적인 데이터를 입력하는 경우 데이터를 생성하고 201 Create로 응답한다.")
    public void 상품_추가_시_정상적인_데이터를_입력하는_경우_데이터를_생성하고_201_Create로_응답한다() throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "test3");
        requestBody.put("category", "TEA");
        requestBody.put("price", 3000);
        requestBody.put("description", null);

        var response = mvc.perform(post(BASE_API_URL)
                                           .contentType(MediaType.APPLICATION_JSON)
                                           .content(objectMapper.writeValueAsString(requestBody)));

        response.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("상품 추가 시 description이 없는 정상적인 데이터를 입력하는 경우 데이터를 생성하고 201 Create로 응답한다.")
    public void 상품_추가_시_descipription이_없는_정상적인_데이터를_입력하는_경우_데이터를_생성하고_201_Create로_응답한다() throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "test3");
        requestBody.put("category", "TEA");
        requestBody.put("price", 3000);

        var response = mvc.perform(post(BASE_API_URL)
                                           .contentType(MediaType.APPLICATION_JSON)
                                           .content(objectMapper.writeValueAsString(requestBody)));

        response.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("상품 추가 시 상품 이름이 중복된 경우 400 Bad Request로 응답한다.")
    public void 상품_추가_시_상품_이름이_중복된_경우_400_Bad_Request로_응답한다() throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "test1");
        requestBody.put("category", "TEA");
        requestBody.put("price", 3000);
        doThrow(IllegalArgumentException.class).when(productService).insert(any());


        var response = mvc.perform(post(BASE_API_URL)
                                           .contentType(MediaType.APPLICATION_JSON)
                                           .content(objectMapper.writeValueAsString(requestBody)));

        response.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("상품 추가 시 상품 내용에 잘못된 값이 존재하는 경우 400 Bad Request로 응답한다.")
    public void 상품_추가_시_상품_내용에_잘못된_값이_존재하는_경우_400_Bad_Request로_응답한다() throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "test1");
        requestBody.put("category", "TEA");
        requestBody.put("price", -3000);

        var response = mvc.perform(post(BASE_API_URL)
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
    @DisplayName("상품 전체 삭제 시 전체 삭제 후 204 No Content로 응답한다.")
    public void 상품_전체_삭제_시_전체_삭제_후_204_No_Content로_응답한다() throws Exception {
        var response = mvc.perform(delete(BASE_API_URL));

        response.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("id를 이용한 상품 조회 시 정상 조회될 경우 200 ok와 해당 상품 데이터로 응답한다.")
    public void id를_이용한_상품_조회_시_정상_조회될_경우_200_ok와_해당_상품_데이터로_응답한다() throws Exception {
        long inputId = 1L;
        when(productService.findById(inputId)).thenReturn(insertedProduct1);

        var response = mvc.perform(get(BASE_API_URL + "/" + inputId));

        response.andExpect(status().isOk())
                .andExpect(content().json(toJson(ProductResponse.from(insertedProduct1))));
    }

    @Test
    @DisplayName("id를 이용한 상품 조회 시 비정상적인 id값을 입력할 경우 400 Bad Request로 응답한다.")
    public void id를_이용한_상품_조회_시_비정상적인_id값을_입력할_경우_400_Bad_Request로_응답한다() throws Exception {
        String inputId = "invalidId";

        var response = mvc.perform(get(BASE_API_URL + "/" + inputId));

        response.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("id를 이용한 상품 조회 시 입력된 id값을 가진 상품이 없는 경우 404 Not Found로 응답한다.")
    public void id를_이용한_상품_조회_시_입력된_id값을_가진_상품이_없는_경우_404_Not_Found로_응답한다() throws Exception {
        long inputId = 10L;
        when(productService.findById(inputId)).thenThrow(NotFoundException.class);

        var response = mvc.perform(get(BASE_API_URL + "/" + inputId));

        response.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("id를 이용한 상품 수정 시 정상적으로 수정된 경우 204 No Content로 응답한다.")
    public void id를_이용한_상품_수정_시_정상적으로_수정된_경우_204_No_Content로_응답한다() throws Exception {
        var productUpdateRequest = new ProductUpdateRequest("newName",
                                                            insertedProduct1.getCategory(),
                                                            insertedProduct1.getPrice(),
                                                            insertedProduct1.getDescription());

        var response = mvc.perform(put(BASE_API_URL + "/" + insertedProduct1.getId())
                                           .contentType(MediaType.APPLICATION_JSON)
                                           .content(objectMapper.writeValueAsString(productUpdateRequest)));

        response.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("id를 이용한 상품 수정 시 수정 시키려는 이름이 중복된 경우 400 Bad Request로 응답한다.")
    public void id를_이용한_상품_수정_시_수정_시키려는_이름이_중복된_경우_400_Bad_Request로_응답한다() throws Exception {
        String duplicatedName = "test2";
        var productUpdateRequest = new ProductUpdateRequest(duplicatedName,
                                                            insertedProduct1.getCategory(),
                                                            insertedProduct1.getPrice(),
                                                            insertedProduct1.getDescription());
        doThrow(IllegalArgumentException.class).when(productService).update(any());

        var response = mvc.perform(put(BASE_API_URL + "/" + insertedProduct1.getId())
                                           .contentType(MediaType.APPLICATION_JSON)
                                           .content(objectMapper.writeValueAsString(productUpdateRequest)));

        response.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("id를 이용한 상품 수정 시 입력이 없는 경우 400 Bad Request로 응답한다.")
    public void id를_이용한_상품_수정_시_입력이_없는_경우_400_Bad_Request로_응답한다() throws Exception {

        var response = mvc.perform(put(BASE_API_URL + "/" + insertedProduct1.getId())
                                           .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("id를 이용한 상품 수정 시 입력에 잘못된 값이 있는 경우 400 Bad Request로 응답한다.")
    public void id를_이용한_상품_수정_시_입력에_잘못된_값이_있는_경우_400_Bad_Request로_응답한다() throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "test1");
        requestBody.put("category", "notCategory");
        requestBody.put("price", 2000);

        var response = mvc.perform(put(BASE_API_URL + "/" + insertedProduct1.getId())
                                           .contentType(MediaType.APPLICATION_JSON)
                                           .content(toJson(requestBody)));

        response.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("id를 이용한 상품 수정 시 id에 해당하는 상품이 없는 경우 404 Not Found로 응답한다.")
    public void id를_이용한_상품_수정_시_id에_해당하는_상품이_없는_경우_404_Not_Found로_응답한다() throws Exception {
        var productUpdateRequest = new ProductUpdateRequest(insertedProduct1.getName(),
                                                            insertedProduct1.getCategory(),
                                                            insertedProduct1.getPrice(),
                                                            insertedProduct1.getDescription());
        doThrow(NotFoundException.class).when(productService).update(any());

        var response = mvc.perform(put(BASE_API_URL + "/10")
                                           .contentType(MediaType.APPLICATION_JSON)
                                           .content(objectMapper.writeValueAsString(productUpdateRequest)));

        response.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("id를 이용한 상품 삭제 시 정상 삭제될 경우 204 No Content로 응답한다.")
    public void id를_이용한_상품_삭제_시_정상_삭제될_경우_204_No_Content로_응답한다() throws Exception {

        var response = mvc.perform(delete(BASE_API_URL + "/" + insertedProduct1.getId()));

        response.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("id를 이용한 상품 삭제 시 비정상적인 id값을 입력할 경우 400 Bad Request로 응답한다.")
    public void id를_이용한_상품_삭제_시_비정상적인_id값을_입력할_경우_400_Bad_Request로_응답한다() throws Exception {
        String inputId = "invalidId";

        var response = mvc.perform(delete(BASE_API_URL + "/" + inputId));

        response.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("id를 이용한 상품 삭제 시 입력된 id값을 가진 상품이 없는 경우 404 Not Found로 응답한다.")
    public void id를_이용한_상품_삭제_시_입력된_id값을_가진_상품이_없는_경우_404_Not_Found로_응답한다() throws Exception {
        long inputId = 10L;
        doThrow(NotFoundException.class).when(productService).deleteById(inputId);

        var response = mvc.perform(delete(BASE_API_URL + "/" + inputId));

        response.andExpect(status().isNotFound());
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException();
        }
    }
}