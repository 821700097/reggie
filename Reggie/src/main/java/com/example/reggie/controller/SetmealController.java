package com.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.common.R;
import com.example.reggie.dto.SetmealDto;
import com.example.reggie.entity.Category;
import com.example.reggie.entity.Dish;
import com.example.reggie.entity.Setmeal;
import com.example.reggie.entity.SetmealDish;
import com.example.reggie.service.CategoryService;
import com.example.reggie.service.DishService;
import com.example.reggie.service.SetmealDishService;
import com.example.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishService dishService;

    @PostMapping
    public R<String> save(@RequestBody  SetmealDto setmealDto){
        log.info("setmeal info:{}",setmealDto.toString());
        setmealService.save(setmealDto);
        return R.success("add setmeal success");
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        Page<Setmeal> pageInfo=new Page(page,pageSize);
        Page<SetmealDto> dtoPage=new Page();

        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(name!=null,Setmeal::getName,name);
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo);

        BeanUtils.copyProperties(pageInfo,dtoPage,"records");

        List<Setmeal> setmealList = pageInfo.getRecords();
        List<SetmealDto> list = setmealList.stream().map((item)->{
            SetmealDto setmealDto=new SetmealDto();
            Long categoryId = item.getCategoryId();
            Category category=categoryService.getById(categoryId);
            if(category!=null){
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
                BeanUtils.copyProperties(item,setmealDto);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(list);

        return R.success(dtoPage);
    }

    @DeleteMapping()
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids:{}",ids.toString());
        setmealService.removeWithDish(ids);
        return R.success("delete setmeal success");
    }

    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable int status,
                            @RequestParam List<Long> ids){
        log.info("get status status:{},id:{}",status,ids.toString());
        setmealService.changStatus(status,ids);
        return R.success("chang status");
    }

    @GetMapping("/{id}")
    public R<Setmeal> update(@PathVariable Long id){
        log.info("update setmeal information id:{}",id);
        SetmealDto setmealDto=new SetmealDto();
        Setmeal setmeal = setmealService.getById(id);
        BeanUtils.copyProperties(setmeal,setmealDto);
        return R.success(setmealDto);
    }


}
