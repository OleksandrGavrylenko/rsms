package com.evry.controller;

import com.evry.exceptions.ResourceNotFoundException;
import com.evry.model.Item;
import com.evry.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController("itemControllerV1")
@Api(description =  "Operations with Items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @ApiOperation(value = "Get list of all Items")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
    })
    @RequestMapping(value = "/items", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getAllItems() {
        Iterable<Item> allItems = itemService.findAll();

        return new ResponseEntity<>(allItems, HttpStatus.OK);
    }

    @ApiOperation(value = "Add new Item")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created Item"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
    })
    @RequestMapping(value = "/items", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> addItem(@Valid @RequestBody Item item) {

        item = itemService.addItem(item);

        URI newItemUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/items/{itemId}")
                .buildAndExpand(item.getId())
                .toUri();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(newItemUri);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get Item by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved Item"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
    })
    @RequestMapping(value = "/items/{itemId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getItem(@PathVariable Long itemId) {
        Item item = itemService.findById(itemId);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete existing Item by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved Quantity"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
    })
    @RequestMapping(value = "/items/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        verifyItem(id);
        itemService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Update existing Item")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved Quantity"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
    })
    @RequestMapping(value = "/items/{itemId}", method = RequestMethod.PUT)
    public ResponseEntity<?> changeQuantity(@PathVariable Long itemId, @Valid @RequestBody Item item) {
        verifyItem(itemId);
        itemService.updateItem(itemId, item);

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    private void verifyItem(Long itemId) throws ResourceNotFoundException {
        itemService.findById(itemId);
    }
}

