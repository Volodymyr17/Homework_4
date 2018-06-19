package myprojects.automation.assignment4;


import myprojects.automation.assignment4.utils.logging.EventHandler;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public abstract class BaseTest {
    protected EventFiringWebDriver driver;
    protected GeneralActions actions;

    private WebDriver getDriver(String browser) {
        switch (browser) {
            case "firefox":
                System.setProperty(
                        "webdriver.gecko.driver",
                        getResource("/geckodriver.exe"));
                return new FirefoxDriver();
            case "edge":
                System.setProperty(
                        "webdriver.edge.driver",
                        getResource("/MicrosoftWebDriver.exe"));
                return new EdgeDriver();
            case "chrome":
            default:
                System.setProperty(
                        "webdriver.chrome.driver",
                        getResource("/chromedriver.exe"));
                return new ChromeDriver();
        }
    }

    private String getResource(String resourceName) {
        try {
           return Paths.get(BaseTest.class.getResource(resourceName).toURI()).toFile().getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return resourceName;
    }

    @BeforeClass
    // use parameters from pom.xml to pass required browser type
    @Parameters("browser")
    public void setUp(String browser ) {
        driver = new EventFiringWebDriver(getDriver(browser));
        driver.register(new EventHandler());
        driver.manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        actions = new GeneralActions(driver);
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
