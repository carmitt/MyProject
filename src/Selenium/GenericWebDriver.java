package Selenium;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import Services.Utils.fileUtils;
import enums.ByTypes;

public class GenericWebDriver {

	RemoteWebDriver webdriver;

	// remote url=address of node/grid
	public void init(String remoteUrl) throws Exception {
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();

		URL url = new URL(remoteUrl);

		webdriver = new RemoteWebDriver(url, capabilities);

	}

	// navigates to a url
	public void openUrl(String url) {
		webdriver.get(url);
	}

	public void quit() {
		webdriver.quit();
	}

	public WebElement getElementByXpath(String xpath) throws Exception {
		return getElementBy(ByTypes.xpath, xpath);
	}

	public List<String> getComboBoxValues(String xpath) throws Exception {
		// get thr combobox element
		WebElement comboBoxElement = getElementBy(ByTypes.xpath, xpath);

		// create a combobox element from the webElement
		Select select = new Select(comboBoxElement);

		// creeate a list of webElements from the combobox values
		List<WebElement> comboValues = select.getOptions();

		// creates a list of strings that will hold the text
		List<String> listStr = new ArrayList<String>();

		// iterates over the WebElements list and gets the text from each
		// webElement

		// the same as: (int i=0;i<comboValues.size();i++)
		for (WebElement element : comboValues) {
			listStr.add(element.getText());
		}
		// return the list of strings
		return listStr;
	}

	public void selectComboBoxOptions(String optionVale, String xpath) throws Exception {

		try {
			WebElement comboBox = getElementBy(ByTypes.xpath, xpath);

			Select select = new Select(comboBox);

			select.selectByVisibleText(optionVale);
		} catch (Exception e) {
			printScreen("SelectingFromComboBoxFailed");

			e.printStackTrace();
		}

	}

	public WebElement getElementBy(ByTypes type, String value) throws Exception {
		return getElementBy(type, value, 10);
	}

	public WebElement getElementBy(ByTypes type, String value, int timeout) {
		WebDriverWait wait = new WebDriverWait(webdriver, timeout, 1000);

		WebElement element = null;

		try {
			switch (type) {
			case xpath:
				element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(value)));
				break;
			case className:
				element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(value)));
				break;

			case id:
				element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(value)));
				break;

			case link:
				element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(value)));
				break;

			case name:
				element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(value)));
				break;
			default:
				break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			printScreen("ElementNotFound_" + value);

			e.printStackTrace();
		}

		return element;

	}

	public List<WebElement> getElementsByXpath(String xpath) {
		WebDriverWait wait = new WebDriverWait(webdriver, 10, 1000);

		List<WebElement> list = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(xpath)));

		return list;

	}

	public String printScreen(String prefix) {
		File screenshot = null;

		String newPath = null;

		try {
			WebDriver augmentedDriver = new Augmenter().augment(webdriver);
			screenshot = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
			newPath = System.getProperty("user.dir") + "\\files\\scr\\" + prefix + screenshot.getName();
			fileUtils.copyFile(screenshot, newPath);

		} catch (WebDriverException e) {

			System.out.println(e.getStackTrace());

		}

		return newPath;
	}

	public void addCookie(String name, String value) {
		Cookie cookie = new Cookie(name, value);
		webdriver.manage().addCookie(cookie);
	}

	public boolean isCookiesExist(String name, String value) {
		Cookie cookie = webdriver.manage().getCookieNamed(name);

		if (cookie != null) {
			if (cookie.getValue().equals(value)) {
				return true;
			} else
				return false;
		} else
			return false;
	}

}
