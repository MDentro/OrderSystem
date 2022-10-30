package nl.dentro.OrderSystem.controllers;

import nl.dentro.OrderSystem.dtos.IdInputDto;
import nl.dentro.OrderSystem.dtos.ImageDto;
import nl.dentro.OrderSystem.dtos.ProductDto;
import nl.dentro.OrderSystem.dtos.ProductInputDto;
import nl.dentro.OrderSystem.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import java.util.List;
import java.util.Optional;

import static nl.dentro.OrderSystem.util.UtilityMethods.getValidationErrorMessage;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    private final ImageController imageController;

    public ProductController(ProductService productService, ImageController imageController) {
        this.productService = productService;
        this.imageController = imageController;
    }

    @GetMapping("")
    public ResponseEntity<List<ProductDto>> getAllProducts(@RequestParam(value = "category", required = false)
                                                               Optional<String> category) {

        List<ProductDto> dtos;
        if (category.isEmpty()) {
            dtos = productService.getAllProducts();
        } else {
            dtos = productService.getAllProductsByCategory(category.get());
        }
        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable Long id) {
        ProductDto productDto = productService.getProductById(id);
        return ResponseEntity.ok().body(productDto);
    }

    @PostMapping("")
    public ResponseEntity<Object> createProduct(@Valid @RequestBody ProductInputDto productInputDto,
                                                BindingResult br) {

        if (br.hasErrors()) {
            return new ResponseEntity<>(getValidationErrorMessage(br), HttpStatus.BAD_REQUEST);
        } else {
            ProductDto productDto = productService.createProduct(productInputDto);
            return new ResponseEntity<>(productDto, HttpStatus.CREATED);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable Long id,
                                                @Valid @RequestBody ProductInputDto productInputDto,
                                                BindingResult br) {

        if (br.hasErrors()) {
            return new ResponseEntity<>(getValidationErrorMessage(br), HttpStatus.BAD_REQUEST);
        } else {
            productService.updateProduct(productInputDto, id);
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/stocklocation")
    public void assignStockLocationToProduct(@PathVariable("id") Long id, @RequestBody IdInputDto input) {
        productService.assignStockLocationToProduct(id, input.getId());
    }

    @PostMapping("/{id}/image")
    public void assignImageToProduct(@PathVariable("id") Long productId,
                                     @RequestBody MultipartFile file) {

        ImageDto imageDto = imageController.singleFileUpload(file);

        productService.assignImageToProduct(imageDto.getFileName(), productId);
    }

}
