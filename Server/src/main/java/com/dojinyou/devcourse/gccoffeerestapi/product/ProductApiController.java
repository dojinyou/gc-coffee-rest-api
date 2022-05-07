package com.dojinyou.devcourse.gccoffeerestapi.product;

import com.dojinyou.devcourse.gccoffeerestapi.product.domain.Category;
import com.dojinyou.devcourse.gccoffeerestapi.product.domain.Product;
import com.dojinyou.devcourse.gccoffeerestapi.product.dto.ProductCreateRequest;
import com.dojinyou.devcourse.gccoffeerestapi.product.dto.ProductResponse;
import com.dojinyou.devcourse.gccoffeerestapi.product.dto.ProductUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/api/v1/products")
public class ProductApiController {
    private final ProductService productService;


    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    public ResponseEntity<?> find(@RequestParam("category") Optional<Category> category,
                                  @RequestParam("name") Optional<String> name) {
        if (name.isPresent()) {
            return new ResponseEntity<>(ProductResponse.from(productService.findByName(name.get())),
                                        HttpStatus.OK);
        }
        List<Product> products = category.map(productService::findAllByCategory)
                                         .orElseGet(productService::findAll);
        List<ProductResponse> responses = products.stream()
                                                  .map(ProductResponse::from)
                                                  .toList();

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity insert(@RequestBody ProductCreateRequest request) {
        productService.insert(Product.of(request.name(),
                                         request.category(),
                                         request.price(),
                                         request.description()));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping()
    public ResponseEntity deleteAll() {
        productService.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable("id") long id) {
        ProductResponse response = ProductResponse.from(productService.findById(id));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable long id, @RequestBody ProductUpdateRequest request) {
        productService.update(Product.of(id,
                                         request.name(),
                                         request.category(),
                                         request.price(),
                                         request.description(),
                                         null,
                                         null,
                                         false));

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable("id") long id) {
        productService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
