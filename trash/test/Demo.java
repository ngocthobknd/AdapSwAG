package test;

import test.model.Temps;

public class Demo {

   
    
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        Class clazz = Class.forName("model.Temps");
        Temps demo = (Temps) clazz.newInstance();
    }
}