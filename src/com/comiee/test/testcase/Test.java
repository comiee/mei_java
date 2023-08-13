package com.comiee.test.testcase;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Test {
    public static void main(String[] args) {
        try{
            throw new Exception("123");
        }catch (Exception e){
            StringWriter stringWriter=new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));
            System.out.println(stringWriter);
        }
    }
}
