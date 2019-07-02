package com.automatad.Utilityresources;

import com.github.javafaker.Faker;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;


public class ReusableMethods {
    public static JsonPath rawToJSON(Response r) {

        String response=r.asString();
        JsonPath jspath=new JsonPath(response);
        return jspath;
    }

    public static String[] generateDummyData(){
        String[] arr=new String[4];
        Faker faker=new Faker();
        arr[0]=faker.name().firstName()+"@automatad.com";
        arr[1]=faker.ancient().god();
        arr[2]=faker.food().spice()+faker.number().numberBetween(0,9999);
        arr[3]=faker.gameOfThrones().character()+faker.gameOfThrones().city()+faker.number().randomDigit();

        return arr;
    }
}
