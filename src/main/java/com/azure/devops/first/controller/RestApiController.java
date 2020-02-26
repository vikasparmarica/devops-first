package com.azure.devops.first.controller;

import com.azure.devops.first.util.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api")

public class RestApiController {

    private static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

    @Autowired
    private com.azure.devops.first.service.CartService cartService;

    @RequestMapping(value = "/cart/", method = RequestMethod.GET)
    public ResponseEntity<List<com.azure.devops.first.model.Item>> listAllItems() {
        List<com.azure.devops.first.model.Item> items = cartService.findAllItems();
        if (items.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(items, HttpStatus.OK);
    }


    @RequestMapping(value = "/cart/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getCart(@PathVariable("id") String id) {
        logger.info("Fetching Cart with id {}", id);
        com.azure.devops.first.model.Item item = cartService.findById(id);
        if (item == null) {
            logger.info("Cart with id {} not found.", id);
            return new ResponseEntity<>(new CustomErrorType("Cart with id " + id
                    + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(item, HttpStatus.OK);
    }


    @RequestMapping(value = "/cart/", method = RequestMethod.POST)
    public ResponseEntity<?> createCart(@RequestBody com.azure.devops.first.model.Item item, UriComponentsBuilder ucBuilder) {

        if (cartService.isItemExist(item)) {
            logger.info("A Cart with name {} already exist. Updating the quantity", item.getName());
            com.azure.devops.first.model.Item currentItem = cartService.findById(item.getId());
            currentItem.setQuantity(currentItem.getQuantity() + item.getQuantity());
            currentItem.setTotalPrice(item.getPrice() * currentItem.getQuantity());
            logger.info("Updating  Cart : {}", currentItem);
            return new ResponseEntity<>(currentItem, HttpStatus.OK);
        }

        item.setTotalPrice(item.getPrice() * item.getQuantity());
        logger.info("Creating Cart : {}", item);

        cartService.saveCart(item);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/cart/{id}").buildAndExpand(item.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }


    @RequestMapping(value = "/cart/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateCart(@PathVariable("id") String id, @RequestBody com.azure.devops.first.model.Item item) {
        logger.info("Updating Cart with id {}", id);

        com.azure.devops.first.model.Item currentItem = cartService.findById(id);

        if (!cartService.isItemExist(currentItem)) {
            logger.error("Unable to update. Cart with id {} not found.", id);
            return new ResponseEntity<>(new CustomErrorType("Unable to update. Cart with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }

        currentItem.setName(item.getName());
        currentItem.setQuantity(item.getQuantity());
        currentItem.setPrice(item.getPrice());
        currentItem.setImgURL(item.getImgURL());
        currentItem.setTotalPrice(item.getPrice() * item.getQuantity());

        return new ResponseEntity<>(currentItem, HttpStatus.OK);
    }

    @RequestMapping(value = "/cart/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCart(@PathVariable("id") String id) {
        logger.info("Fetching & Deleting Cart with id {}", id);

        com.azure.devops.first.model.Item currentItem = cartService.findById(id);
        if (!cartService.isItemExist(currentItem)) {
            logger.error("Unable to delete. Cart with id {} not found.", id);
            return new ResponseEntity<>(new CustomErrorType("Unable to delete. Cart with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        cartService.deleteCartById(currentItem);
        return new ResponseEntity<com.azure.devops.first.model.Item>(HttpStatus.NO_CONTENT);
    }


    @RequestMapping(value = "/cart/", method = RequestMethod.DELETE)
    public ResponseEntity<com.azure.devops.first.model.Item> clearCart() {
        logger.info("Deleting All Carts");

        cartService.ClearAllItems();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}