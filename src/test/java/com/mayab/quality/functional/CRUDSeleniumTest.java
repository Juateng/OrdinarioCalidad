package com.mayab.quality.functional;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.time.Duration;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CRUDSeleniumTest {
  private WebDriver driver;
  //private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  JavascriptExecutor js;
  @Before
  public void setUp() throws Exception {
    //System.setProperty("webdriver.chrome.driver", "");
    driver = new ChromeDriver();
    //baseUrl = "https://www.google.com/";
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
    js = (JavascriptExecutor) driver;
  }
  
  private void takeScreenshot(String fileName) {
	    try {
	      File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	      FileUtils.copyFile(screenshot, new File("./src/screenshots/" + fileName + ".png"));
	      System.out.println("Captura guardada como: " + fileName + ".png");
	    } catch (Exception e) {
	      System.out.println("Error al tomar captura: " + e.getMessage());
	 }
  }

  @Test
  public void test1CreateNewUser() throws Exception {
    driver.get("https://mern-crud-mpfr.onrender.com/");
    driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
    driver.findElement(By.name("name")).click();
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("Juan");
    pause(1000);
    driver.findElement(By.name("email")).click();
    driver.findElement(By.name("email")).clear();
    driver.findElement(By.name("email")).sendKeys("76546546454654654687987514*@gmail.com");
    pause(1000);
    driver.findElement(By.name("age")).click();
    driver.findElement(By.name("age")).clear();
    driver.findElement(By.name("age")).sendKeys("30");
    pause(1000);
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Gender'])[2]/following::div[2]")).click();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Male'])[1]/following::div[2]")).click();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Woah!'])[1]/following::button[1]")).click();
    //driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Stop'])[1]/following::div[1]")).click();
    pause(1000);
    
    takeScreenshot("CreateNewUser");
    
    String isUserCreated = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[1]")).getText();
    assertEquals(isUserCreated, "Juan");
  }
  
  @Test
  public void test2ExisitingEmail() throws Exception {
    driver.get("https://mern-crud-mpfr.onrender.com/");
    driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
    driver.findElement(By.name("name")).click();
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("Alberto");
    pause(1000);
    driver.findElement(By.name("email")).click();
    driver.findElement(By.name("email")).clear();
    driver.findElement(By.name("email")).sendKeys("76546546454654654687987514*@gmail.com");
    pause(1000);
    driver.findElement(By.name("age")).click();
    driver.findElement(By.name("age")).clear();
    driver.findElement(By.name("age")).sendKeys("28");
    pause(1000);
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Gender'])[2]/following::div[2]")).click();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Male'])[1]/following::div[2]")).click();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Woah!'])[1]/following::button[1]")).click();
    pause(1000);   
    
    takeScreenshot("ExistingEmail");
    
    String errormessage = driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[5]/div/p")).getText();
    assertEquals(errormessage, "That email is already taken.");
  }
  
  @Test
  public void test3EditAge() throws Exception {
    driver.get("https://mern-crud-mpfr.onrender.com/");
    driver.findElement(By.xpath("//div[@id='root']/div/div[2]/table/tbody/tr/td[5]/button")).click();
    pause(1000);
    driver.findElement(By.name("age")).click();
    driver.findElement(By.name("age")).clear();
    driver.findElement(By.name("age")).sendKeys("25");
    pause(1000);
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Woah!'])[1]/following::button[1]")).click();
    pause(1000);
    
    takeScreenshot("EditAge");
    
    String ageCheck = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[3]")).getText();
    assertEquals(ageCheck, "25");
  }
  
  @Test
  public void test4Delete() throws Exception {
    driver.get("https://mern-crud-mpfr.onrender.com/");
    driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[5]/button[2]")).click();
    
    takeScreenshot("Deleteuser");
    
    pause(1000);
    driver.findElement(By.xpath("/html/body/div[3]/div/div[3]/button[1]")).click();
    pause(1000);
    
    String userErased = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[2]")).getText();
    String userExpected = "76546546454654654687987514*@gmail.com";
    assertNotEquals(userErased, userExpected);
  }
  
  @Test
  public void test5FindUserByName() throws Exception {
    driver.get("https://mern-crud-mpfr.onrender.com/");
    driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
    driver.findElement(By.name("name")).click();
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("Ramon");
    pause(1000);
    driver.findElement(By.name("email")).click();
    driver.findElement(By.name("email")).clear();
    driver.findElement(By.name("email")).sendKeys("Correoquenocreoserepita1@gmail.com");
    pause(1000);
    driver.findElement(By.name("age")).click();
    driver.findElement(By.name("age")).clear();
    driver.findElement(By.name("age")).sendKeys("17");
    pause(1000);
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Gender'])[2]/following::div[2]")).click();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Male'])[1]/following::div[2]")).click();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Woah!'])[1]/following::button[1]")).click();
    pause(1000);
    //-------------------------------------------------------------------------------------------------------------------------------
    driver.get("https://mern-crud-mpfr.onrender.com/");
    driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
    driver.findElement(By.name("name")).click();
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("Lucas");
    pause(1000);
    driver.findElement(By.name("email")).click();
    driver.findElement(By.name("email")).clear();
    driver.findElement(By.name("email")).sendKeys("Correoquenocreoserepita2@gmail.com");
    pause(1000);
    driver.findElement(By.name("age")).click();
    driver.findElement(By.name("age")).clear();
    driver.findElement(By.name("age")).sendKeys("17");
    pause(1000);
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Gender'])[2]/following::div[2]")).click();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Male'])[1]/following::div[2]")).click();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Woah!'])[1]/following::button[1]")).click();
    pause(1000);
  //-------------------------------------------------------------------------------------------------------------------------------
    driver.get("https://mern-crud-mpfr.onrender.com/");
    driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
    driver.findElement(By.name("name")).click();
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("Enrique");
    pause(1000);
    driver.findElement(By.name("email")).click();
    driver.findElement(By.name("email")).clear();
    driver.findElement(By.name("email")).sendKeys("Correoquenocreoserepita3@gmail.com");
    pause(1000);
    driver.findElement(By.name("age")).click();
    driver.findElement(By.name("age")).clear();
    driver.findElement(By.name("age")).sendKeys("17");
    pause(1000);
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Gender'])[2]/following::div[2]")).click();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Male'])[1]/following::div[2]")).click();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Woah!'])[1]/following::button[1]")).click();
    pause(1000);
  //-------------------------------------------------------------------------------------------------------------------------------
    
    takeScreenshot("FindByName");
    
    String userfind = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[2]/td[1]")).getText();
    assertEquals(userfind, "Lucas");
  }
  @Test
  public void test6FindAll() throws Exception {
    driver.get("https://mern-crud-mpfr.onrender.com/");
    driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
    driver.findElement(By.name("name")).click();
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("Alberto");
    pause(1000);
    driver.findElement(By.name("email")).click();
    driver.findElement(By.name("email")).clear();
    driver.findElement(By.name("email")).sendKeys("Correoquenocreoserepita4@gmail.com");
    pause(1000);
    driver.findElement(By.name("age")).click();
    driver.findElement(By.name("age")).clear();
    driver.findElement(By.name("age")).sendKeys("17");
    pause(1000);
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Gender'])[2]/following::div[2]")).click();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Male'])[1]/following::div[2]")).click();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Woah!'])[1]/following::button[1]")).click();
    pause(3000);
    
    takeScreenshot("FindAll");
    
    //-------------------------------------------------------------------------------------------------------------------------------
    String userfind1 = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[1]")).getText();
    String emailfind1 = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[2]")).getText();
    String userfind2 = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[2]/td[1]")).getText();
    String emailfind2 = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[2]/td[2]")).getText();
    String userfind3 = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[3]/td[1]")).getText();
    String emailfind3 = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[3]/td[2]")).getText();
    String userfind4 = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[4]/td[1]")).getText();
    String emailfind4 = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[4]/td[2]")).getText();
    assertEquals(userfind1,"Alberto");
    assertEquals(emailfind1,"correoquenocreoserepita4@gmail.com");
    assertEquals(userfind2,"Enrique");
    assertEquals(emailfind2,"correoquenocreoserepita3@gmail.com");
    assertEquals(userfind3,"Lucas");
    assertEquals(emailfind3,"correoquenocreoserepita2@gmail.com");
    assertEquals(userfind4,"Ramon");
    assertEquals(emailfind4,"correoquenocreoserepita1@gmail.com");
    
  }
  
  
  private void pause(long mils) {
	  try {
		  Thread.sleep(mils);  
	  }catch(Exception e) {
		  e.printStackTrace();
	  }
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
