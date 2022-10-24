package nl.dentro.OrderSystem.controllers;

import nl.dentro.OrderSystem.dtos.ProductDto;
import nl.dentro.OrderSystem.security.JwtService;
import nl.dentro.OrderSystem.services.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
@WithMockUser(username = "testuser")
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    JwtService jwtService;

    @MockBean
    ImageController imageController;

    @MockBean
    private ProductServiceImpl productService;

    ProductDto productDto1;

    ProductDto productDto2;

    ProductDto productDto3;

    @BeforeEach
    void setUp() {
        productDto1 = new ProductDto(1001L, "APTITLIG", 17.99, "cooking", "The chopping board collects meat and fruit juice in the milled groove and prevents it from spilling on to your worktop. You can easily turn the chopping board and use both sides when you prepare food, because it has easy-to-grip slanted edges. Made of bamboo, which is an easy-care, hardwearing natural material that is also gentle on your knives.");
        productDto2 = new ProductDto(1002L, "KAVALKAD", 4.99, "cooking", "The pan s low weight makes it easy to handle when filled with food. Made from aluminium, which spreads heat evenly and energy efficiently, and makes it easier to regulate heat so the food does not burn and stick. With Teflon® Classic non-stick coating that makes cooking and cleaning easy. Easy grip handle makes the pan easy to lift.");
        productDto3 = new ProductDto(1004L, "HEMMABAK", 9.99, "baking", "This muffin tin is perfect for muffins, cupcakes, quiches or even bread. Made of durable steel with non-stick coating to make your pastries and food easy to loosen from the tin. The steel distributes the heat evenly, which makes your baking soft and scrumptious on the inside and gives it a nice golden-brown surface.");
    }


    @Test
    void shouldReturnAllProductWhenRequested() throws Exception {
        given(productService.getAllProducts()).willReturn(List.of(productDto1, productDto2, productDto3));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1001))
                .andExpect(jsonPath("$[0].name").value("APTITLIG"))
                .andExpect(jsonPath("$[0].price").value(17.99))
                .andExpect(jsonPath("$[0].category").value("cooking"))
                .andExpect(jsonPath("$[0].description").value("The chopping board collects meat and fruit juice in the milled groove and prevents it from spilling on to your worktop. You can easily turn the chopping board and use both sides when you prepare food, because it has easy-to-grip slanted edges. Made of bamboo, which is an easy-care, hardwearing natural material that is also gentle on your knives."))
                .andExpect(jsonPath("$[1].id").value(1002))
                .andExpect(jsonPath("$[1].name").value("KAVALKAD"))
                .andExpect(jsonPath("$[1].price").value(4.99))
                .andExpect(jsonPath("$[1].category").value("cooking"))
                .andExpect(jsonPath("$[1].description").value("The pan s low weight makes it easy to handle when filled with food. Made from aluminium, which spreads heat evenly and energy efficiently, and makes it easier to regulate heat so the food does not burn and stick. With Teflon® Classic non-stick coating that makes cooking and cleaning easy. Easy grip handle makes the pan easy to lift."))
                .andExpect(jsonPath("$[2].id").value(1004))
                .andExpect(jsonPath("$[2].name").value("HEMMABAK"))
                .andExpect(jsonPath("$[2].price").value(9.99))
                .andExpect(jsonPath("$[2].category").value("baking"))
                .andExpect(jsonPath("$[2].description").value("This muffin tin is perfect for muffins, cupcakes, quiches or even bread. Made of durable steel with non-stick coating to make your pastries and food easy to loosen from the tin. The steel distributes the heat evenly, which makes your baking soft and scrumptious on the inside and gives it a nice golden-brown surface."));
    }

    @Test
    void shouldReturnAllProductBelongingToCategoryWhenCategoryIsGiven() throws Exception {
        given(productService.getAllProductsByCategory("cooking")).willReturn(List.of(productDto1, productDto2));

        mockMvc.perform(get("/products?category=cooking"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1001))
                .andExpect(jsonPath("$[0].name").value("APTITLIG"))
                .andExpect(jsonPath("$[0].price").value(17.99))
                .andExpect(jsonPath("$[0].category").value("cooking"))
                .andExpect(jsonPath("$[0].description").value("The chopping board collects meat and fruit juice in the milled groove and prevents it from spilling on to your worktop. You can easily turn the chopping board and use both sides when you prepare food, because it has easy-to-grip slanted edges. Made of bamboo, which is an easy-care, hardwearing natural material that is also gentle on your knives."))
                .andExpect(jsonPath("$[1].id").value(1002))
                .andExpect(jsonPath("$[1].name").value("KAVALKAD"))
                .andExpect(jsonPath("$[1].price").value(4.99))
                .andExpect(jsonPath("$[1].category").value("cooking"))
                .andExpect(jsonPath("$[1].description").value("The pan s low weight makes it easy to handle when filled with food. Made from aluminium, which spreads heat evenly and energy efficiently, and makes it easier to regulate heat so the food does not burn and stick. With Teflon® Classic non-stick coating that makes cooking and cleaning easy. Easy grip handle makes the pan easy to lift."));
    }

    @Test
    void shouldReturnProductWhenIdIsGiven() throws Exception {

        Mockito.when(productService.getProductById(1001L)).thenReturn(productDto1);

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
    void shouldReturnFormbiddenWhenTryingToDeleteAProductWithNoRightCredentials() throws Exception {
        mockMvc.perform(delete("/products/1006"))
                .andExpect(status().isForbidden());
    }


}