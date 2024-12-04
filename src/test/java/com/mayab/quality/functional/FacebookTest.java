package com.mayab.quality.functional;

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
import java.io.IOException;
import java.time.Duration;

public class FacebookTest {
  private static WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  JavascriptExecutor js;
  @Before
  public void setUp() throws Exception {
    //System.setProperty("webdriver.chrome.driver", "");
    driver = new ChromeDriver();
    baseUrl = "https://www.google.com/";
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
    js = (JavascriptExecutor) driver;
  }

  @Test
  public void testFacebook() throws Exception {
	  
	 driver.get("https://www.facebook.com/?locale=en_US");
	 pause(2000);
	 takeScreenshot("Foto1");
	 pause(4000);
	 driver.findElement(By.id("email")).clear();
	 driver.findElement(By.id("email")).sendKeys("puppies");
	 driver.findElement(By.id("pass")).clear();
	 driver.findElement(By.id("pass")).sendKeys("puppies");
	 driver.findElement(By.name("login")).click();
	
	String actualResult = driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[1]/div/div[2]/div[2]/form/div/div[1]/div[2]")).getText();
    assertEquals(actualResult,("El correo electrónico o número de celular que ingresaste no está conectado a una cuenta. Encuentra tu cuenta e inicia sesión."));

}
  
  private void pause(long mils) {
	  try {
		  Thread.sleep(mils);  
	  }catch(Exception e) {
		  e.printStackTrace();
	  }
  }
  
  //function to take a screenshot
  public static void takeScreenshot(String fileName) throws IOException{
	  //Creating instance of file 
	  File file = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	  //importante
	  FileUtils.copyFile(file, new File("src/screenshots/" + fileName + ".jpeg"));
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
