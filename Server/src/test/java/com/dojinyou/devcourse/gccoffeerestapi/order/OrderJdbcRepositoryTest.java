package com.dojinyou.devcourse.gccoffeerestapi.order;

import com.dojinyou.devcourse.gccoffeerestapi.order.domain.Order;
import com.dojinyou.devcourse.gccoffeerestapi.order.dto.OrderCreateRequest;
import com.dojinyou.devcourse.gccoffeerestapi.order.dto.OrderProductCreateRequest;
import com.dojinyou.devcourse.gccoffeerestapi.product.ProductJdbcRepository;
import com.dojinyou.devcourse.gccoffeerestapi.product.domain.Category;
import com.dojinyou.devcourse.gccoffeerestapi.product.domain.Product;
import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.ScriptResolver;
import com.wix.mysql.distribution.Version;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.List;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.config.Charset.UTF8;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;

@EnableAutoConfiguration
@SpringBootTest(classes = {OrderJdbcRepository.class, ProductJdbcRepository.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("OrderJdbcRepository 테스트")
class OrderJdbcRepositoryTest {

    @Autowired
    OrderJdbcRepository orderJdbcRepository;

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
                .addSchema("test-order-mgmt", ScriptResolver.classPathScripts("test-schema-order.sql"))
                .start();
        productJdbcRepository.insert(Product.of("test1", Category.TEA, 1000, null));
        productJdbcRepository.insert(Product.of("test2", Category.TEA, 2000, "description"));
    }

    @AfterAll
    void cleanUp() {
        embeddedMysql.stop();
    }

    @Test
    @DisplayName("create함수는 정상적인 데이터가 입력될 경우 아무런 예외를 발생시키지 않는다.")
    void create함수는_정상적인_데이터가_입력될_경우_아무런_예외를_발생시키지_않는다() {
        List<OrderProductCreateRequest> orderProductCreateRequests = Arrays.asList(new OrderProductCreateRequest(1L, 3),
                                                                                   new OrderProductCreateRequest(2L, 2));
        Order newOrder = Order.from(new OrderCreateRequest("test@dojin.com", "우리집주소", "12345", orderProductCreateRequests));

        Throwable throwable = catchThrowable(() -> orderJdbcRepository.create(newOrder));

        assertThat(throwable).isNull();

    }

    @Test
    @DisplayName("create함수는 입력된 productId의 상품이 존재하지 않을 경우 제약 조건 예외를 발생시킨다.")
    void create함수는_입력된_productId의_상품이_존재하지_않을_경우_제약_조건_예외를_발생시킨다() {
        List<OrderProductCreateRequest> orderProductCreateRequests = Arrays.asList(new OrderProductCreateRequest(3L, 3));
        Order newOrder = Order.from(new OrderCreateRequest("test@dojin.com", "우리집주소", "12345", orderProductCreateRequests));

        Throwable throwable = catchThrowable(() -> {
            try {
                orderJdbcRepository.create(newOrder);
            } catch (DataIntegrityViolationException e) {
                throw new SQLIntegrityConstraintViolationException();
            }
        });

        assertThat(throwable).isNotNull()
                             .isInstanceOf(SQLIntegrityConstraintViolationException.class);
    }

}