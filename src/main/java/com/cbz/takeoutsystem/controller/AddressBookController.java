package com.cbz.takeoutsystem.controller;

import com.cbz.takeoutsystem.entity.AddressBook;
import com.cbz.takeoutsystem.entity.User;
import com.cbz.takeoutsystem.service.impl.AddressBookServiceImpl;
import com.cbz.takeoutsystem.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookServiceImpl addressBookService;

    @GetMapping("list")
    public Result<List<AddressBook>> getAddressBookList() {
        return addressBookService.queryAddressBookList();
    }

    @GetMapping("/{id}")
    public Result<AddressBook> getAddressById(@PathVariable Integer id) {
        return addressBookService.queryAddressById(id);
    }

    @PostMapping
    public Result<String> addAddress(@RequestBody AddressBook addressBook) {
        log.info(addressBook.toString());
        return addressBookService.addAddress(addressBook);
    }

    @PutMapping
    public Result<String> editAddress(@RequestBody AddressBook addressBook) {
//        log.info(addressBook.toString());
        return addressBookService.editAddress(addressBook);
    }

    @DeleteMapping
    public Result<String> deleteAddressById(@RequestParam Integer id) {
        return addressBookService.deleteAddressById(id);
    }

    @GetMapping("default")
    public Result<AddressBook> getDefaultAddress() {
        return addressBookService.queryDefaultAddress();
    }

    @PutMapping("default")
    public Result<Object> setDefaultAddress(@RequestBody User user) {
//        log.info(String.valueOf(user.getId()));
        return addressBookService.setDefaultAddress(user.getId());
//        return null;
    }
}
