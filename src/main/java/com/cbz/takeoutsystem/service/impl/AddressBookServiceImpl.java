package com.cbz.takeoutsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cbz.takeoutsystem.entity.AddressBook;
import com.cbz.takeoutsystem.mapper.AddressBookMapper;
import com.cbz.takeoutsystem.service.AddressBookService;
import com.cbz.takeoutsystem.utils.Result;
import com.cbz.takeoutsystem.utils.UserHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

    /**
     * 查询所有地址
     *
     * @return 地址数据列表
     */
    @Override
    public Result<List<AddressBook>> queryAddressBookList() {
        //返回
        return Result.success(list());
    }

    /**
     * 根据id 查询地址数据
     *
     * @param id 地址数据id
     * @return 相应id的地址数据
     */
    @Override
    public Result<AddressBook> queryAddressById(Integer id) {
        AddressBook addressBook = getById(id);
        return Result.success(addressBook);
    }

    /**
     * 添加地址
     *
     * @param addressBook 封装了地址相关信息的Bean
     * @return 处理结果集
     */
    @Override
    public Result<String> addAddress(AddressBook addressBook) {
        //添加user_id
        addressBook.setUserId(UserHolder.getUser().getId());
        if (save(addressBook)) {
            return Result.success(null);
        }
        return Result.error("添加失败！");
    }

    /**
     * 修改地址信息
     *
     * @param addressBook 修改后的地址信息
     * @return 处理结果集
     */
    @Override
    public Result<String> editAddress(AddressBook addressBook) {
        addressBook.setUpdateTime(null);
        addressBook.setUserId(null);
        if (updateById(addressBook)) {
            return Result.success(null);
        }
        return Result.error("修改失败！");
    }

    /**
     * 根据id删除地址
     *
     * @param id 被删除地址的id
     * @return 处理结果集
     */
    @Override
    public Result<String> deleteAddressById(Integer id) {
        if (removeById(id)) {
            return Result.success(null);
        }
        return Result.error("删除失败！");
    }

    /**
     * 查询默认地址
     *
     * @return 默认地址信息
     */
    @Override
    public Result<AddressBook> queryDefaultAddress() {

        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getIsDefault, 1);

        AddressBook addressBook = getOne(wrapper);

        return Result.success(addressBook);
    }

    /**
     * 设置默认地址
     *
     * @param id 默认地址id
     * @return 处理结果集
     */
    @Transactional
    @Override
    public Result<Object> setDefaultAddress(Long id) {

        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getIsDefault, 1);
        //查询默认地址
        AddressBook addressBook = getOne(wrapper);

        if (addressBook != null) {
            addressBook.setIsDeleted(0);
            updateById(addressBook);
        }
        //设置默认地址
        addressBook = getById(id);
        addressBook.setIsDefault(1);
        updateById(addressBook);
        return Result.success(null);
    }
}
