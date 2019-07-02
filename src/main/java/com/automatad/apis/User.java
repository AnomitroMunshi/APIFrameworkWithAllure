package com.automatad.apis;


import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;

public class User {
    public static Logger log= LogManager.getLogger(User.class.getName());
    static String host="";


    public User(String host) {
        this.host=host;
    }


    @Step("Getting user details with token: {0} and comparing with the name : {1}")
    public  String getUserdetails(String token, String name){
        log.info("Getting user details");
        String message="";
        log.info("Hitting URL: "+host);
            RestAssured.baseURI = host;
            Response res = given().header("Authorization", "Bearer " + token).header("Content-Type", "application/json")
                    .get("/api/v1/debug-user").then().assertThat().contentType(ContentType.JSON).extract().response();
        log.info("Received JSON response.");
        String corr_cred=res.getHeader("access-control-allow-credentials");
        String corr_origin=res.getHeader("access-control-allow-origin");
        log.info("Status code received from getuser response: "+res.getStatusCode());
        log.info("Validating statusCode..");
        if(res.getStatusCode()==201) {
            if (corr_cred.equals("true") && corr_origin.equals("*")) {
                JsonPath js = resources.ReusableMethods.rawToJSON(res);
                if(js.get("user").equals(name)){
                    message=js.get("message");
                    log.info("Message from getUserdetails: " + message);
                }else
                    log.error("Name didn't match");
            }
            else{
                log.error("Corr headers not present!");
            }
        }else if(res.getStatusCode()==401){
                message="invalid JWT! Token is mostly expired, or invalid. Login to get a new Token.";
                log.info(message);
        }
        else{
            log.error("Statuscode received is not expected,failing the testcase.");
        }
            return message;
     }


     @Step("Signing up new user with email : {0} , Name : {1} and Password : {2}")
    public  String signUp(String email,String name,String pass){
         String message="";
        log.info("Signing up...");
        RestAssured.baseURI = host;
         log.info("Hitting URL: "+host);
        Response res=given().header("Content-Type","application/json").body(resources.payload.getdata(email,name,pass)).when()
                .post("/api/v1/users").then().assertThat().contentType(ContentType.JSON).and()
                .header("access-control-allow-credentials","true").and().header("access-control-allow-origin","*").extract().response();
        log.info("Received JSON response from signup api.");
         log.info("Status code received from signUp response: "+res.getStatusCode());
        JsonPath js=resources.ReusableMethods.rawToJSON(res);
        log.info("Validating status code and status..");
        if(res.getStatusCode()==201 && js.get("status").equals("ok")){
                message=js.get("message");
                log.info("Message from signUpTest: "+message);
        }else if(res.getStatusCode()==422 && js.get("status").equals("error")){
              message=js.get("message")+" as email "+js.get("errors.email[0]");
        }else{
            log.error("Status code received is not expected. Failing the testcase.");
        }
       return message;
    }



    @Step("Signing in with email : {0} and password: {1}")
    public  String  signIn(String email,String pass){
        String message="";
        log.info("Signing in...");
        RestAssured.baseURI = host;
        log.info("Hitting URL: "+host);
        Response res=given().header("Content-Type","application/json").body(resources.payload.getdata(email,pass)).when()
                .post("/api/v1/login").then().assertThat().contentType(ContentType.JSON).and()
                .header("access-control-allow-credentials","true").and().header("access-control-allow-origin","*")
                .extract().response();
        log.info("Received JSON response from signin api.");
        log.info("Status code received from signin response: "+res.getStatusCode());
        JsonPath js=resources.ReusableMethods.rawToJSON(res);
        if(res.getStatusCode()==200){
            if(js.get("data.email").equals(email) && js.get("status").equals("ok")){
                log.info("Message from signInTest: " + js.get("message"));
                message = js.get("data.token");
            }else{
                log.info("Email didn't match");
            }
        }else if(res.getStatusCode()==401 && js.get("status").equals("error")){

            message=js.get("message");

        }else{
            log.error("Status code received is not expected. Failing the testcase.");
        }
        return message;
    }


    @Step("Hitting Logout api with token: {0}")
    public String logout(String token){
        log.info("Logging out..");
        String message="";
        log.info("Hitting URL: "+host);
        RestAssured.baseURI = host;
        Response res=given().header("Authorization","Bearer "+token).header("Content-Type","application/json").when()
                .post("/api/v1/logout").then().assertThat().contentType(ContentType.JSON).extract().response();
        log.info("Received JSON response.");
        JsonPath js = resources.ReusableMethods.rawToJSON(res);
        String corr_cred=res.getHeader("access-control-allow-credentials");
        String corr_origin=res.getHeader("access-control-allow-origin");
        log.info("Status code received from logout response: "+res.getStatusCode());
        if(res.getStatusCode()==200 && js.get("status").equals("ok")) {
            if(corr_cred.equals("true") && corr_origin.equals("*")){

                log.info("Message from LogoutTest: " + js.get("message"));
                message=js.get("message");
            }else {
                log.error("Corrs header missing in response!");
            }
        }else if(res.getStatusCode()==401 && js.get("status").equals("unauthorized")){
            log.info("Unauthorized access!");
            message=js.get("message");
        }
        else {
            log.error("Status code received is wrong");
        }
        return message;

    }

    @Step("Recovery token will be sent to registered email ( {0} ) , if user exists")
    public String forgotPass(String email){

        log.info("Sending email to :"+email);
        RestAssured.baseURI = host;
        log.info("Hitting URL: "+host);
        String message="";
        Response res=given().header("Content-Type","application/json").body(resources.payload.getdata(email)).when()
                .post("/api/v1/users/forgot-password").then().assertThat().contentType(ContentType.JSON).and()
                .header("access-control-allow-credentials","true").and().header("access-control-allow-origin","*")
                .extract().response();
        log.info("Received JSON response.");
        JsonPath js=resources.ReusableMethods.rawToJSON(res);
        log.info("Status code received from forgotPass response: "+res.getStatusCode());
        if(res.getStatusCode()==200 && js.get("status").equals("ok")){
            log.info("Message from forgotpass: " + js.get("message"));
            message=js.get("message");
        }else{
            log.error("Status code received is wrong");
        }
        return message;
    }


    @Step("Modifying current username with {0}")
    public String modifyName(String token,String newname){
        String message="";
        RestAssured.baseURI = host;
        log.info("Hitting URL: "+host);
        Response res=given().header("Authorization","Bearer "+token).header("Content-Type","application/json")
                .body(resources.payload.modifyData(newname)).when().post("/api/v1/users/modify-name")
                .then().assertThat().contentType(ContentType.JSON).and()
                .header("access-control-allow-credentials","true").and().header("access-control-allow-origin","*")
               .extract().response();
        log.info("Received JSON response.");
        JsonPath js=resources.ReusableMethods.rawToJSON(res);
        log.info("Status code received from changeName response: "+res.getStatusCode());
        if(res.getStatusCode()==200 && js.get("status").equals("ok")){
            message=js.get("message");
            log.info("Message from modify name Test: "+message);
        }else {
            log.error("Status code received is wrong");
        }
        return message;
    }



    @Step("Changing password from {1} to {2}")
    public String changePassword(String token,String oldPass,String newPass){
        String message="";
        log.info("Hitting URL: "+host);
        RestAssured.baseURI = host;
        Response res=given().header("Authorization","Bearer "+token).header("Content-Type","application/json")
                .body(resources.payload.modifyData(oldPass,newPass)).when().post("/api/v1/users/change-password")
                .then().assertThat().contentType(ContentType.JSON).and()
                .header("access-control-allow-credentials","true").and().header("access-control-allow-origin","*")
                .extract().response();
        log.info("Received JSON response.");
        JsonPath js=resources.ReusableMethods.rawToJSON(res);
        log.info("Status code received from change password response: "+res.getStatusCode());
        if(res.getStatusCode()==200 && js.get("status").equals("ok")){
            message=js.get("message");
            log.info("Message from change password Test: "+message);
        }else if(res.getStatusCode()==500 && js.get("status").equals("error")){
            log.info("Original password is wrong");
            message=js.get("message");
            log.info("Message from change password Test: "+message);
        }else if(res.getStatusCode()==401 && js.get("status").equals("unauthorized")){
            log.info("Token is wrong");
            message=js.get("message");
            log.info("Message from change password Test: "+message);
        }
        else{
            log.error("Status code received is wrong");
        }
        return message;
    }


    @Step("Reseting password with token : {0} and newPassword: {1}")
    public String resetpassword(String secretToken,String newpass) {
        String message="";
        log.info("Hitting URL: "+host);
        RestAssured.baseURI = host;
        Response res = given().header("Content-Type", "application/json").body(resources.payload.resetdata(secretToken, newpass)).when()
                .post("/api/v1/users/reset-password").then().assertThat().statusCode(422).contentType(ContentType.JSON).and().
                        body("status", equalTo("error")).extract().response();

        log.info("Received JSON response.");
        JsonPath js=resources.ReusableMethods.rawToJSON(res);
        log.info("Status code received from reset password response: "+res.getStatusCode());
        if(res.getStatusCode()==200 && js.get("status").equals("ok")){
            message=js.get("message");
            log.info(message);
        }else if(res.statusCode()==401 && js.get("status").equals("error")){
            message=js.get("message");
            log.info(message);
        }else{
            log.error("Status code received is wrong");
        }

        return message;
    }


}
