package com.dojinyou.devcourse.gccoffeerestapi.product;

import com.dojinyou.devcourse.gccoffeerestapi.product.ProductJdbcRepository;
import com.dojinyou.devcourse.gccoffeerestapi.product.domain.Category;
import com.dojinyou.devcourse.gccoffeerestapi.product.domain.Product;
import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.ScriptResolver;
import com.wix.mysql.distribution.Version;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.config.Charset.UTF8;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("ProductJdbcRepository 테스트")
class ProductJdbcRepositoryTest {

    @Autowired
    ProductJdbcRepository productJdbcRepository;

    static EmbeddedMysql embeddedMysql;

    @BeforeAll
    void setUp() {
        // TODO: Config value에 대한 property 처리
        final var config = aMysqldConfig(Version.v5_7_latest)
                .withCharset(UTF8)
                .withPort(2215)
                .withUser("test", "test1234!")
                .withTimeZone("Asia/Seoul")
                .build();
        embeddedMysql = anEmbeddedMysql(config)
                .addSchema("test-order-mgmt", ScriptResolver.classPathScripts("schema.sql"))
                .start();
    }

    @AfterAll
    void cleanUp() {
        embeddedMysql.stop();
    }

    Product inputProduct = new Product("testName", Category.COFFEE, 100L, null);

    @Test
    @Order(2)
    @DisplayName("insert함수는 정상적인 데이터가 들어오면 아무런 예외를 발생시키지 않는다")
    void insert함수는_정상적인_데이터가_들어오면_아무런_예외를_발생시키지_않는다() {
        Throwable throwable = catchThrowable(() -> productJdbcRepository.insert(inputProduct));

        assertThat(throwable).isNull();
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("insert함수는 Null이 들어오면 예외를 발생시킨다")
    void insert함수는_Null이_들어오면_예외를_발생시킨다(Product inputProduct) {
        Throwable throwable = catchThrowable(() -> productJdbcRepository.insert(inputProduct));

        assertThat(throwable).isNotNull()
                             .isInstanceOf(IllegalArgumentException.class)
                             .hasMessage("잘못된 입력입니다.");
    }
    @Test
    @Order(3)
    @DisplayName("insert함수는 입력된 상품의 이름이 이미 있으면 예외를 발생시킨다")
    void insert함수는_입력된_상품의_이름이_이미_있으면_예외를_발생시킨다() {
        Throwable throwable = catchThrowable(() -> productJdbcRepository.insert(inputProduct));

        assertThat(throwable).isNotNull()
                             .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Order(1)
    @DisplayName("findAll함수는 조회할 데이터가 없으면 빈 리스트를 리턴한다")
    void findAll함수는_조회할_데이터가_없으면_빈_리스트를_리턴한다() {
        List<Product> products = productJdbcRepository.findAll();

        assertThat(products).isNotNull();
        assertThat(products.size()).isEqualTo(0);
    }


    @Test
    @Order(3)
    @DisplayName("findAll함수는 조회할 데이터가 있으면 상품이 포함된 리스트를 리턴한다")
    void findAll함수는_조회할_데이터가_있으면_상품이_포함된_리스트를_리턴한다() {
        List<Product> products = productJdbcRepository.findAll();

        assertThat(products).isNotNull();
        assertThat(products.size()).isNotEqualTo(0);
        assertThat(products.get(0)).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(longs = {-1, 0, Long.MAX_VALUE})
    @DisplayName("findById함수는 입력된 id값에 해당하는 데이터가 없으면 Optional.Empty를 리턴한다")
    void findById함수는_입력된_id값에_해당하는_데이터가_없으면_Optional_Empty를_리턴한다(long inputId) {
        Optional<Product> foundOptionalProduct = productJdbcRepository.findById(inputId);

        assertThat(foundOptionalProduct).isNotNull();
        assertThat(foundOptionalProduct.isEmpty()).isTrue();
    }

    @Test
    @Order(3)
    @DisplayName("findById함수는 입력된 id값에 해당하는 데이터가 있으면 데이터를 리턴한다")
    void findById함수는_입력된_id값에_해당하는_데이터가_있으면_데이터를_리턴한다() {
        long inputId = 1;
        Optional<Product> foundOptionalProduct = productJdbcRepository.findById(inputId);

        assertThat(foundOptionalProduct).isNotNull();
        assertThat(foundOptionalProduct.isPresent()).isTrue();

        Product foundProduct = foundOptionalProduct.get();

        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.id()).isEqualTo(inputId);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"failName", "notFoundName"})
    @DisplayName("findByName함수는 입력된 name값에 해당하는 데이터가 없으면 Optional.Empty를 리턴한다.")
    void findByName함수는_입력된_name값에_해당하는_데이터가_없으면_Optional_Empty를_리턴한다(String inputName) {

        Optional<Product> foundOptionalProduct = productJdbcRepository.findByName(inputName);

        assertThat(foundOptionalProduct).isNotNull();
        assertThat(foundOptionalProduct.isEmpty()).isTrue();
    }

    @Test
    @Order(3)
    @DisplayName("findByName함수는 입력된 name값에 해당하는 데이터가 있으면 데이터를 리턴한다.")
    void findByName함수는_입력된_name값에_해당하는_데이터가_있으면_데이터를_리턴한다() {
        String inputName = inputProduct.name();

        Optional<Product> foundOptionalProduct = productJdbcRepository.findByName(inputName);

        assertThat(foundOptionalProduct).isNotNull();
        assertThat(foundOptionalProduct.isPresent()).isTrue();

        Product foundProduct = foundOptionalProduct.get();

        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.name()).isEqualTo(inputName);
    }

    @ParameterizedTest
    @EnumSource(Category.class)
    @Order(1)
    @DisplayName("findAllByCategory함수는 입력된 Category에 해당하는 데이터가 없으면 빈 리스트를 리턴한다.")
    void findAllByCategory함수는_입력된_Category에_해당하는_데이터가_없으면_빈_리스트를_리턴한다(Category category) {
        List<Product> allProductByCategory = productJdbcRepository.findAllByCategory(category);

        assertThat(allProductByCategory).isNotNull();
        assertThat(allProductByCategory.size()).isEqualTo(0);
    }

    @Test
    @Order(3)
    //@DisplayCategory("findAllByCategory함수는 입력된 Category에 해당하는 데이터가 있으면 데이터를 리턴한다.")
    void findAllByCategory함수는_입력된_Category에_해당하는_데이터가_있으면_데이터를_리턴한다() {
        Category inputCategory = inputProduct.category();

        List<Product> allProductByCategory = productJdbcRepository.findAllByCategory(inputCategory);

        assertThat(allProductByCategory).isNotNull();
        assertThat(allProductByCategory.size()).isNotEqualTo(0);

        Product foundProduct = allProductByCategory.get(0);

        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.category()).isEqualTo(inputCategory);
    }

    @Test
    @DisplayName("update함수는 입력된 상품의 id에 해당하는 제품이 없을 경우 예외를 발생시킨다.")
    void update함수는_입력된_상품의_id에_해당하는_제s품이_없을_경우_예외를_발생시킨다() {
        Product notFoundProduct = new Product(0L, "notDuplicatedName",
                                              Category.COFFEE, 100L, null,
                                              LocalDateTime.now(), LocalDateTime.now(),
                                              false);

        Throwable throwable = catchThrowable(() -> productJdbcRepository.update(notFoundProduct));

        assertThat(throwable).isNotNull()
                             .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Order(3)
    @DisplayName("update함수는 update하려는 이름이 중복일 경우 예외를 발생시킨다.")
    void update함수는_update하려는_이름이_중복일_경우_예외를_발생시킨다() {
        Product insertProduct = new Product("notDuplicatedName", Category.TEA, 100L, null);
        productJdbcRepository.insert(insertProduct);
        Product updateProduct = new Product(2L, "testName", insertProduct.category(),
                                            insertProduct.price(), insertProduct.description(),
                                            null, null,false);

        Throwable throwable = catchThrowable(() -> productJdbcRepository.update(updateProduct));

        assertThat(throwable).isNotNull()
                             .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Order(4)
    @DisplayName("update함수는 정상적인 데이터가 입력되면 예외를 발생시키지 않는다.")
    void update함수는_정상적인_데이터가_입력되면_예외를_발생시키지_않는다() {
        Product updatedProduct = new Product(1L,"testName2", inputProduct.category(),
                                             inputProduct.price(), inputProduct.description(),
                                             null,null,false);

        Throwable throwable = catchThrowable(() -> productJdbcRepository.update(updatedProduct));

        assertThat(throwable).isNull();
    }

    @ParameterizedTest
    @ValueSource(longs = {-1, 0, Long.MAX_VALUE})
    @DisplayName("deleteById함수는 입력된 id값에 해당하는 데이터가 없으면 예외를 발생시킨다.")
    void deleteById함수는_입력된_id값에_해당하는_데이터가_없으면_예외를_발생시킨다(long inputId) {
        Throwable throwable = catchThrowable(() -> productJdbcRepository.deleteById(inputId));

        assertThat(throwable).isNotNull()
                             .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Order(4)
    @DisplayName("deleteById함수는 입력된 id값에 해당하는 데이터가 있으면 예외를 발생시키지 않는다.")
    void deleteById함수는_입력된_id값에_해당하는_데이터가_있으면_예외를_발생시키지_않는다() {
        long inputId = 2L;
        Throwable throwable = catchThrowable(() -> productJdbcRepository.deleteById(inputId));

        assertThat(throwable).isNull();
    }
}