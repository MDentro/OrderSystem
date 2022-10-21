package nl.dentro.OrderSystem.controllers;

import nl.dentro.OrderSystem.dtos.ProductDto;
import nl.dentro.OrderSystem.dtos.ProductInputDto;
import nl.dentro.OrderSystem.models.Product;
import nl.dentro.OrderSystem.security.JwtService;
import nl.dentro.OrderSystem.services.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
@WithMockUser(username="admin", roles="ADMIN", password = "admin@Test1")
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    JwtService jwtService;

    @MockBean
    ImageController imageController;

    @MockBean
    private ProductServiceImpl productService;

    ProductDto aptitligDto;


    @BeforeEach
    void setUp() {
        aptitligDto = new ProductDto(1001L, "APTITLIG", 17.99, "cooking", "The chopping board collects meat and fruit juice in the milled groove and prevents it from spilling on to your worktop. You can easily turn the chopping board and use both sides when you prepare food, because it has easy-to-grip slanted edges. Made of bamboo, which is an easy-care, hardwearing natural material that is also gentle on your knives.");

    }

    @Test
    void shouldReturnProductWhenIdIsGiven() throws Exception {

        Mockito.when(productService.getProductById(1001L)).thenReturn(aptitligDto);

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/products/1001"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(1001)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("APTITLIG")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", is(17.99)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category", is("cooking")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is("The chopping board collects meat and fruit juice in the milled groove and prevents it from spilling on to your worktop. You can easily turn the chopping board and use both sides when you prepare food, because it has easy-to-grip slanted edges. Made of bamboo, which is an easy-care, hardwearing natural material that is also gentle on your knives.")));
    }


    @Test
    void deleteProduct() throws Exception {
        mockMvc.perform(delete("/products/1006"))
                .andExpect(status().isNoContent());
    }
}