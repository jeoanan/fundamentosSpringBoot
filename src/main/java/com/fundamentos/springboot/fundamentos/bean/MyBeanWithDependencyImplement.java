package com.fundamentos.springboot.fundamentos.bean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MyBeanWithDependencyImplement implements  MyBeanWithDependency{

    private final Log LOGGER = LogFactory.getLog(MyBeanWithDependencyImplement.class);
    private MyOperation myOperation;

    public MyBeanWithDependencyImplement(MyOperation myOperation) {
        this.myOperation = myOperation;
    }

    @Override
    public void printWithDependency() {
        LOGGER.info("Hemos ingresado a metodo printWithDependency");
        int numero=1;
        LOGGER.debug("El numero enviado a la dependencia operacion es " + numero);
        System.out.println(myOperation.sum(numero));
        System.out.println("Hola desde un bean con dependencia");
    }
}
