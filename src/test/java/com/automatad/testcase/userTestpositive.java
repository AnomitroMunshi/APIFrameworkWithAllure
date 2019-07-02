package com.automatad.testcase;


import com.automatad.Utilityresources.ReusableMethods;
import com.automatad.Utilityresources.payload;
import com.automatad.apis.User;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
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
import static org.hamcrest.Matchers.equalToIgnoringCase;


public class userTestpositive {
    public static Logger log= LogManager.getLogger(userTestpositive.class.getName());
    Properties prop;
    FileInputStream fis;
    String token="";
    String host="";
    String[] arr=ReusableMethods.generateDummyData();
    String email=arr[0];
    String name=arr[1];
    String pass=arr[2];
    String newname=arr[3];
    User user;

    @BeforeMethod
    public void setup(){
        try {

            prop = new Properties();
            log.info("**************************************");
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


    @Test(priority = 0,description = "Verify SignUp test")
    @Severity(SeverityLevel.BLOCKER)
    @Description("TestCase to verify signUp with unique email")
    public void signUpTest(){
        log.info("Starting SignUpTest...");
        user=new User(host);
        String message=user.signUp(email,name,pass);
        if(message.equals(prop.getProperty("signUpMessage"))){
            log.info("SignUpTest success!!");
            Assert.assertTrue(true);
        }else{
            log.error("SignUp failed!");
            Assert.fail();
        }
    }

    @Test(priority = 1,description = "Verify SignIn test")
    @Severity(SeverityLevel.CRITICAL)
    @Description("TestCase to verify signin test with correct username and password")
    public void signInTest(){
        log.info("Starting SignInTest...");
        user=new User(host);
        token=user.signIn(email,pass);
        if(token!=null) {
            if(token.equals(prop.getProperty("signInFailureMessage"))){
                log.error("signin failed");
                Assert.fail();
            }else {
                log.info("Session Token: " + token + " received.");
                log.info("SignIn Test success!");
                Assert.assertTrue(true);
            }
        }
        else{
            log.error("Login failed. No token generated");
            Assert.fail();
        }
    }


    @Test(priority = 2,description = "Verify get user test")
    @Severity(SeverityLevel.BLOCKER)
    @Description("TestCase to verify get user test")
    public void getuserTest(){
        user=new User(host);
        log.info("Starting getUserTest...");
        String message = user.getUserdetails(token,name);
        if(message.equals(prop.getProperty("getUserMessage"))){
            log.info("Get User Test success!");
            Assert.assertTrue(true);
           }
        else {
            log.error("Coudn't get user. Test case failed!");
            Assert.fail();
        }
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @Description("TestCase to modify Name")
    public void ModifyNameTest(){
        log.info("Starting Modify Name Test...");
        user = new User(host);
        String message=user.modifyName(token,newname);
        name=newname;
        if(message.equals(prop.getProperty("modifyNameMessage"))){
            log.info("Modify Name Test Success!");
            Assert.assertTrue(true);
        }
        else{
            log.error("Modify Name test failed");
            Assert.fail();
        }

    }

    @Test(priority = 4)
    @Severity(SeverityLevel.NORMAL)
    @Description("TestCase to modify password")
    public void ChangePasswordTest(){
        log.info("Starting Change password Test...");
        user = new User(host);
        String newPass=pass+"12$34";
        String message=user.changePassword(token,pass,newPass);
        pass=newPass;
        if(message.equals(prop.getProperty("changePasswordMessage"))) {
            log.info("Change password  Test Success!");
            Assert.assertTrue(true);
        }else{
            log.error("Changepassword Test failed.");
            Assert.fail();
        }
    }

    @Test(priority = 5,description = "Verify get user test after password change.")
    @Severity(SeverityLevel.CRITICAL)
    @Description("User will not be able to login as token got destroyed after passwordChange")
    public void getUserAfterPasswordChangeTest(){
        user=new User(host);
        String message=user.getUserdetails(token,name);
        if(message.equals(prop.getProperty("errorMessage"))){
            log.info("Received response code: 401 ");
            log.info("Get User after password change Test success!");
            Assert.assertTrue(true);
        }else{
            log.info("Wrong response code");
            log.info("Get User after password change Test failed!");
            Assert.fail();
        }
    }



    @Test(priority = 6,description = "Verify Logout test" )
    @Severity(SeverityLevel.NORMAL)
    @Description("TestCase to verify Logout test, token should be deleted.")
    public void logoutTest(){
        user=new User(host);
        log.info("Starting LogoutTest...");
        log.info("Signing in again with email="+email+" and password="+pass);
        token=user.signIn(email,pass);
        log.info("SignIn successfull.Token received : "+token);
        String message=user.logout(token);
        if(message.equals(prop.getProperty("logOutMessage"))){
            log.info("LogOut Test Success!");
            Assert.assertTrue(true);
        }
        else{
            log.error("Logout test failed!");
            Assert.fail();
        }

     }


    @Test(priority = 7,description = "Verify get user test after logout.")
    @Severity(SeverityLevel.CRITICAL)
    @Description("User will not be able to get user details as token got destroyed after log out")
    public void getUserAfterLogoutTest(){
        user=new User(host);
        log.info("Starting getUserAfterLogoutTest...");
        String message=user.getUserdetails(token,name);
        if(message.equals(prop.getProperty("errorMessage"))){
            log.info("Received response code: 401 ");
            log.info("Get User after logout Test success!");
            Assert.assertTrue(true);
        }else{
            log.info("Wrong response code");
            log.info("Get User after logout Test failed!");
            Assert.fail();
        }

    }


    @Test(priority = 8,description = "Verify sign-in working as expected after password and name change.")
    @Severity(SeverityLevel.CRITICAL)
    @Description("User will be able to login with new password and check the new name.")
     public void signInwithNewpassTest(){
        user=new User(host);
        token=user.signIn(email,pass);
        String message=user.getUserdetails(token,name);
        if(message.equals(prop.getProperty("getUserMessage"))){
            log.info("signInwithNewpass Test success!");
            Assert.assertTrue(true);
        }
        else {
            log.error("Coudn't get user with new token or name. Test case failed!");
            Assert.fail();
        }
     }




    @Test(priority = 9,description = "Verify forgot password test")
    @Severity(SeverityLevel.MINOR)
    @Description("TestCase to verify forgot password test")
    public void forgotPassTest() {

        user = new User(host);
        log.info("Starting forgotPassword Test...");
        String message= user.forgotPass(email);
       if(message.equals(prop.getProperty("forgetpasswordmessage"))){
           log.info("ForgotpasswordTest Success!");
           Assert.assertTrue(true);
       }else{
           log.error("Forgot Password Test failed");
           Assert.fail();
       }


    }

}
