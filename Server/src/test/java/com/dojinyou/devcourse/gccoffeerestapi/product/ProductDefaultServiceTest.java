package com.dojinyou.devcourse.gccoffeerestapi.product;

import com.dojinyou.devcourse.gccoffeerestapi.common.exception.NotFoundException;
import com.dojinyou.devcourse.gccoffeerestapi.product.domain.Category;
import com.dojinyou.devcourse.gccoffeerestapi.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.Mockito.*;

@DisplayName("ProductDefaultService 테스트")
@ExtendWith(MockitoExtension.class)
class ProductDefaultServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductDefaultService productService;

    private final Product inputProduct = Product.of("inputName", Category.COFFEE,
                                                    100L, null);
    private final LocalDateTime sampleLocalDateTime = LocalDateTime.of(2022, 05, 06,
                                                                       11, 32, 30);
    private final Product insertedProduct = Product.of(1L, "insertedName", Category.COFFEE,
                                                       100L, null,
                                                       sampleLocalDateTime, sampleLocalDateTime,
                                                       false);

    @Test
    @DisplayName("insert함수는 정상적인 데이터가 들어오면 예외가 발생하지 않는다.")
    void insert함수는_정상적인_데이터가_들어오면_예외가_발생하지_않는다() {
        Throwable throwable = catchThrowable(() -> productService.insert(inputProduct));

        assertThat(throwable).isNull();
    }

    @Test
    @DisplayName("insert함수는 정상적인 데이터가 들어오면 repository의 insert함수를 호출한다.")
    void insert함수는_정상적인_데이터가_들어오면_repository의_insert함수를_호출한다() {
        productService.insert(inputProduct);

        verify(productRepository, atLeastOnce()).insert(inputProduct);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, Long.MAX_VALUE})
    @DisplayName("insert함수는 상품의 id가 0이 아닌 경우 예외를 발생시킨다.")
    void insert함수는_상품의_id가_0이_아닌_경우_예외를_발생시킨다(long inputId) {
        Product productHasId = Product.of(inputId, "testName", Category.TEA,
                                          100L, null,
                                          null, null, false);
        Throwable throwable = catchThrowable(() -> productService.insert(productHasId));

        assertThat(throwable).isNotNull()
                             .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("findAll함수는 repository의 findAll 함수를 호출한다.")
    void findAll함수는_repository의_findAll_함수를_호출한다() {
        productService.findAll();

        verify(productRepository, atLeastOnce()).findAll();
    }

    @ParameterizedTest
    @ValueSource(longs = {Long.MIN_VALUE, -1, 0})
    @DisplayName("findById함수는 입력된 상품의 id가 양의 정수가 아니면 예외를 발생시킨다.")
    void findById함수는_입력된_상품의_id가_양의_정수가_아니면_예외를_발생시킨다(long inputId) {
        Throwable throwable = catchThrowable(() -> productService.findById(inputId));

        assertThat(throwable).isNotNull()
                             .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("findById함수는 입력된 id값에 해당하는 데이터가 있으면 데이터를 리턴한다")
    void findById함수는_입력된_id값에_해당하는_데이터가_있으면_데이터를_리턴한다() {
        long existId = insertedProduct.getId();
        when(productRepository.findById(existId)).thenReturn(Optional.of(insertedProduct));


        Product foundProduct = productService.findById(existId);

        verify(productRepository, atLeastOnce()).findById(existId);
        assertThat(foundProduct).isNotNull()
                                .isEqualTo(insertedProduct);
    }

    @Test
    @DisplayName("findById함수는 입력된 id값에 해당하는 데이터가 없으면 NotFoundException을 발생시킨다")
    void findById함수는_입력된_id값에_해당하는_데이터가_없으면_NotFoundException을_발생시킨다() {
        long notExistId = 10;
        when(productRepository.findById(notExistId)).thenReturn(Optional.empty());


        Throwable throwable = catchThrowable(() -> productService.findById(notExistId));

        assertThat(throwable).isNotNull()
                             .isInstanceOf(NotFoundException.class);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("findByName함수는 입력된 데이터가 null이면 예외를 발생시킨다.")
    void findByName함수는_입력된_데이터가_null이면_예외를_발생시킨다(String inputName) {
        Throwable throwable = catchThrowable(() -> productService.findByName(inputName));

        assertThat(throwable).isNotNull()
                             .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("findByName함수는 입력된 id값에 해당하는 데이터가 있으면 데이터를 리턴한다")
    void findByName함수는_입력된_id값에_해당하는_데이터가_있으면_데이터를_리턴한다() {
        String existName = insertedProduct.getName();
        when(productRepository.findByName(existName)).thenReturn(Optional.of(insertedProduct));


        Product foundProduct = productService.findByName(existName);

        verify(productRepository, atLeastOnce()).findByName(existName);
        assertThat(foundProduct).isNotNull()
                                .isEqualTo(insertedProduct);
    }

    @Test
    @DisplayName("findByName함수는 입력된 id값에 해당하는 데이터가 없으면 NotFoundException을 발생시킨다.")
    void findByName함수는_입력된_id값에_해당하는_데이터가_없으면_NotFoundException을_발생시킨다() {
        String notExistName = "notExistName";
        when(productRepository.findByName(notExistName)).thenReturn(Optional.empty());


        Throwable throwable = catchThrowable(() -> productService.findByName(notExistName));

        assertThat(throwable).isNotNull()
                             .isInstanceOf(NotFoundException.class);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("findAllByCategory함수는 입력된 데이터가 null이면 예외를 발생시킨다.")
    void findAllByCategory함수는_입력된_데이터가_null이면_예외를_발생시킨다(Category category) {
        Throwable throwable = catchThrowable(() -> productService.findAllByCategory(category));

        assertThat(throwable).isNotNull()
                             .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @EnumSource(Category.class)
    @DisplayName("findAllByCategory함수는 입력된 Category에 해당하는 데이터가 없으면 빈 리스트를 리턴한다.")
    void findAllByCategory함수는_입력된_Category에_해당하는_데이터가_없으면_빈_리스트를_리턴한다(Category category) {
        when(productRepository.findAllByCategory(any())).thenReturn(new ArrayList<>());

        List<Product> allProductByCategory = productService.findAllByCategory(category);

        assertThat(allProductByCategory).isNotNull();
        assertThat(allProductByCategory.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("findAllByCategory함수는 입력된 Category에 해당하는 데이터가 있으면 데이터를 리턴한다.")
    void findAllByCategory함수는_입력된_Category에_해당하는_데이터가_있으면_데이터를_리턴한다() {
        Category insertedCategory = insertedProduct.getCategory();
        when(productRepository.findAllByCategory(any())).thenReturn(Arrays.asList(new Product[]{insertedProduct}));

        List<Product> allProductByCategory = productService.findAllByCategory(insertedCategory);

        assertThat(allProductByCategory).isNotNull();
        assertThat(allProductByCategory.size()).isNotEqualTo(0);

        Product foundProduct = allProductByCategory.get(0);

        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getCategory()).isEqualTo(insertedCategory);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("update함수는 입력된 상품이 Null이면 예외를 발생시킨다.")
    void update함수는_입력된_상품이_Null이면_예외를_발생시킨다(Product nullProduct) {
        Throwable throwable = catchThrowable(() -> productService.update(nullProduct));

        assertThat(throwable).isNotNull()
                             .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("update함수는 정상적인 데이터가 입력되면 repository의 update 함수를 호출한다.")
    void update함수는_repository의_update_함수를_호출한다() {
        productService.update(insertedProduct);

        verify(productRepository, atLeastOnce()).update(insertedProduct);
    }

    @ParameterizedTest
    @ValueSource(longs = {Long.MIN_VALUE, -1, 0})
    @DisplayName("deleteById함수는 입력된 상품의 id가 양의 정수가 아니면 예외를 발생시킨다.")
    void deleteById함수는_입력된_상품의_id가_양의_정수가_아니면_예외를_발생시킨다(long inputId) {
        Throwable throwable = catchThrowable(() -> productService.deleteById(inputId));

        assertThat(throwable).isNotNull()
                             .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("deleteById함수는 repository의 deleteById 함수를 호출한다.")
    void deleteById함수는_repository의_deleteById_함수를_호출한다() {
        long sampleId = 1;
        productService.deleteById(sampleId);

        verify(productRepository, atLeastOnce()).deleteById(sampleId);
    }
}