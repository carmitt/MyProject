package Selenium;

import org.junit.After;
import org.junit.Before;

import PageObjects.AddItemPageObject;
import PageObjects.LoginPage;

public class BasicTest {
	
	GenericWebDriver webdriver;
	
	LoginPage loginPage;
	
	AddItemPageObject addItem;
	
	@Before
	public void setup() throws Exception{
		
		webdriver=new GenericWebDriver();
		
		webdriver.init("http://localhost:4444/wd/hub");
		
		loginPage=new LoginPage(webdriver);
		
		addItem=new AddItemPageObject(webdriver);
	}
	
	@After
	public void tearDown(){
		webdriver.quit();
	}

}
