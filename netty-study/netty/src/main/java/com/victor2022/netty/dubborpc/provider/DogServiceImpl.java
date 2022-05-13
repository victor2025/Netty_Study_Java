package com.victor2022.netty.dubborpc.provider;

import com.victor2022.netty.dubborpc.publicinterface.DogService;

/**
 * @author victor2022
 * @creat 2022/5/13 10:09
 */
public class DogServiceImpl implements DogService {

    @Override
    public String bark(String words) {
        return "Dog is barking: "+words;
    }

    @Override
    public String name(String name) {
        return "Dog's name is: "+name;
    }
}
