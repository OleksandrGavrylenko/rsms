package com.evry.controller;

import com.evry.RSMSApplicationion;
import com.evry.model.Item;
import com.evry.repository.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = RSMSApplicationion.class)
@AutoConfigureMockMvc
@Transactional
public class ItemControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockRestServiceServer mockServer;

    private MockMvc mockMvc;

    private RestTemplate restTemplate;

    @MockBean
    private ItemRepository itemRepository;

    private MediaType applicationJsonMediaType =
            new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Before
    public void setUp() throws Exception {
        this.restTemplate = new RestTemplate();
        this.mockServer = MockRestServiceServer.createServer(this.restTemplate);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void testGetAllItems() throws Exception {

        List<Item> items = new ArrayList<>();
        items.add(new Item(1L,"Milk", BigDecimal.valueOf(1.15), 5L, "Dairy"));
        items.add(new Item(2L,"Cheese", BigDecimal.valueOf(4.0), 3L, "Dairy"));
        items.add(new Item(3L,"Cottage cheese", BigDecimal.valueOf(3.48), 15L, "Dairy"));
        given(itemRepository.findAll()).willReturn(items);

        this.mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
        .andExpect(content().contentType(applicationJsonMediaType))
        .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void testAddItem() throws Exception {
        long id = 1L;
        when(itemRepository.save(any(Item.class)))
                .thenReturn(new Item(id,"Milk", BigDecimal.valueOf(1.15), 5L, "Dairy"));

        String jsonOfItem = "{\"name\" : \"Milk\", \"price\" : 1.10,\"quantity\" : 15, \"category\" : \"Dairy\"}";

        MvcResult mvcResult = mockMvc.perform(post("/items")
                .accept(applicationJsonMediaType)
                .content(jsonOfItem)
                .contentType(this.applicationJsonMediaType))
                .andExpect(status().isCreated())
                .andReturn();

        mockServer.verify();

        String locationUri = mvcResult.getResponse().getHeader("Location");
        assertTrue(locationUri.contains("/items/" + id));
    }

    @Test
    public void testGetItem() throws Exception {
        given(itemRepository.findById(1L)).willReturn(
                java.util.Optional.of( new Item(1L,"Milk", BigDecimal.valueOf(1.15), 5L, "Dairy")));

        this.mockMvc.perform(get("/items/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(applicationJsonMediaType))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Milk")))
                .andExpect(jsonPath("$.price", is(1.15)))
                .andExpect(jsonPath("$.quantity", is(5)))
                .andExpect(jsonPath("$.category", is("Dairy")));
    }

    @Test
    public void testGetItemNotExists() throws Exception {
        given(itemRepository.findById(1L)).willReturn(Optional.empty());

        this.mockMvc.perform(get("/items/1"))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testUpdateItemQuantity() throws Exception {
        long id = 1L;
        given(itemRepository.findById(id))
                .willReturn(Optional.ofNullable(new Item(id,"Milk", BigDecimal.valueOf(1.15), 5L, "Dairy")));
        when(itemRepository.save(any(Item.class)))
                .thenReturn(new Item(id,"Milk", BigDecimal.valueOf(1.15), 5L, "Dairy"));

        String jsonOfItem = "{\"name\" : \"Milk\", \"price\" : 1.10,\"quantity\" : 15, \"category\" : \"Dairy\"}";


        mockMvc.perform(put("/items/1/")
                .accept(applicationJsonMediaType)
                .content(jsonOfItem)
                .contentType(this.applicationJsonMediaType))
                .andExpect(status().isNoContent())
                .andReturn();
    }


}
