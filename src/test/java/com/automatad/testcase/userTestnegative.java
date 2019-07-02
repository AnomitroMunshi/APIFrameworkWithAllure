package com.automatad.testcase;


import com.automatad.Utilityresources.ReusableMethods;
import com.automatad.Utilityresources.payload;
import com.automatad.apis.User;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class userTestnegative {
    public static Logger log= LogManager.getLogger(userTestnegative.class.getName());
    Properties prop;
    FileInputStream fis;
    String token="";
    String host="";
    String[] arr=ReusableMethods.generateDummyData();
    String email="";
    String name=arr[1];
    String pass=arr[2];
    String newname=arr[3];
    User user;

    @BeforeMethod
    public void setup(){
        try {
            prop = new Properties();
            log.info("***************************************");
            log.info("Reading property file.........");
            FileInputStream ip = new FileInputStream(System.getProperty("user.dir")+ "\\src\\main\\java\\resources\\properties.properties");
            prop.load(ip);
            log.info("Property file loaded successfully!");
            host=prop.getProperty("host");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test(priority = 10,description = "Verifying SignUp negative test")
    @Severity(SeverityLevel.BLOCKER)
    @Description("TestCase to verify signUp when email already exists.")
    public void signUpNegativeTest(){
        log.info("Starting SignUp negative Test...");
       user=new User(host);
       String message=user.signUp(prop.getProperty("hardcodeemail"),name,pass);
        if(message.equals(prop.getProperty("signUpfailureMessage"))){
            log.info("SignUp negative Test success!!");
            Assert.assertTrue(true);
        }else {
            log.info("Sign Up negative test failed");
            Assert.fail();
        }
    }


    @Test(priority = 11,description = "Verify SignIn negative test")
    @Severity(SeverityLevel.CRITICAL)
    @Description("TestCase to verify signin test when wrong credentials are given")
    public void signInNegativeTest(){
        log.info("Starting SignIn negative Test...");
        user=new User(host);
        String message=user.signIn(email,"abcdef");
        if(message.equals(prop.getProperty("signInFailureMessage"))){
            log.info("Message from signIn negative Test: "+message);
            log.info("SignIn negative Test success!");
            Assert.assertTrue(true);
        }
        else{
            log.error("SignIn negative Test failed!");
            Assert.fail();
        }

    }

    @Test(priority = 12,description = "Verify get user negative test")
    @Severity(SeverityLevel.BLOCKER)
    @Description("TestCase to verify getuser when token passed is wrong")
    public void getuserNegativeTest(){
        log.info("Starting getUser negative Test...");
        user=new User(host);
       String message=user.getUserdetails("iamawsm12345689802929",name);
      if(message.equals(prop.getProperty("getuserfailure"))) {
          log.info("Get User negative Test success!");
          Assert.assertTrue(true);
      }else {
          log.error("get user negative test failed!");
          Assert.fail();
      }
    }



    @Test(priority = 13 ,description = "Verify logout negative test")
    @Severity(SeverityLevel.NORMAL)
    @Description("TestCase to verify Logout test when token is not present/wrong")
    public void logoutNegativeTest(){
        log.info("Starting Logout negative Test...");
        user=new User(host);
        String message=user.logout("jnkadlmsldlad");
        if(message.equals(prop.getProperty("getuserfailure"))) {
            log.info("Logout negative Test success!");
            Assert.assertTrue(true);
        }else {
            log.error("logout negative test failed!");
            Assert.fail();
        }
    }

    @Test(priority = 14,description = "Verify password reset test")
    @Severity(SeverityLevel.MINOR)
    @Description("TestCase to verify reset password test when wrong reset_token passed")
    public void ResetPassNegativeTest(){
        log.info("Starting reset password negative Test...");
        user=new User(host);
        String message=user.resetpassword("dummyTokentofailTestcase","anypassword");
        if(message.equals(prop.getProperty("resetfailure"))){
            log.info("Reset password Test Success!");
            Assert.assertTrue(true);
        }else {
            log.error("Reset password Test failed!");
            Assert.fail();
        }

    }

    @Test(priority = 15 ,description = "Verify change password negative test")
    @Severity(SeverityLevel.NORMAL)
    @Description("TestCase to verify password change happening, when token is not present/wrong")
    public void changepasswordNegativeTest(){
        log.info("Starting Logout negative Test...");
        user=new User(host);
        String message=user.changePassword("abc",pass,"dnkadm");
        if(message.equals(prop.getProperty("changePasswordfailure")) || message.equals(prop.getProperty("getuserfailure")) ) {
            log.info("Change password negative Test success!");
            Assert.assertTrue(true);
        }else {
            log.error("Change pass negative test failed!");
            Assert.fail();
        }
    }



}
