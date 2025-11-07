package in.ashokit.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.ashokit.dto.ProductDto;
import in.ashokit.response.ApiResponse;
import in.ashokit.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @PostMapping("/product")
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(@RequestParam("productJson") String productJson,
                                                                 @RequestParam("categoryId") Integer categoryId,
                                                                 @RequestParam("productImage") MultipartFile productImage) {
        //we can't use @RequestBody because here we have json(productJson) along with image('MultipartFile') we use @RequestParam (also can use @PathVariable)
        ApiResponse<ProductDto> response = new ApiResponse<>();
        try {
            //converting json to java object
            ObjectMapper mapper = new ObjectMapper();//to convert String into java Object
            ProductDto productDto = mapper.readValue(productJson, ProductDto.class);//convert raw json into Dto
            ProductDto savedProduct = productService.createProduct(categoryId, productDto, productImage);

            if (savedProduct.getProductId() != null) {
                response.setData(savedProduct);
                response.setMsg("Product Saved");
                response.setStatus(201);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                response.setData(null);
                response.setMsg("Product Failed To Save");
                response.setStatus(500);
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
            response.setData(null);
            response.setMsg("⚠️ Exception Occurred: " + e.getMessage());
            response.setStatus(500);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
