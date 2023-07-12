package com.cbz.takeoutsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cbz.takeoutsystem.entity.AddressBook;
import com.cbz.takeoutsystem.utils.Result;

import java.util.List;

public interface AddressBookService extends IService<AddressBook>  {
    Result<List<AddressBook>> queryAddressBookList();

    Result<AddressBook> queryAddressById(Integer id);

    Result<String> addAddress(AddressBook addressBook);

    Result<String> editAddress(AddressBook addressBook);

    Result<String> deleteAddressById(Integer id);

    Result<AddressBook> queryDefaultAddress();

    Result<Object> setDefaultAddress(Long id);
}
