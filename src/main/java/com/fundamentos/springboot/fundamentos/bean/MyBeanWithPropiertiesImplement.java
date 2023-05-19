package com.fundamentos.springboot.fundamentos.bean;

public class MyBeanWithPropiertiesImplement implements MyBeanWithPropierties{

    private String name;
    private String lastName;

    public MyBeanWithPropiertiesImplement(String name, String lastName) {
        this.name = name;
        this.lastName = lastName;
    }

    @Override
    public String function() {
        return name + "-" + lastName;
    }
}
