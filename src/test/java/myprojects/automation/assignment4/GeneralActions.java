package myprojects.automation.assignment4;


import myprojects.automation.assignment4.model.ProductData;
import myprojects.automation.assignment4.utils.Properties;
import myprojects.automation.assignment4.utils.logging.CustomReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;


public class GeneralActions {
    private WebDriver driver;
    private WebDriverWait wait;

   public GeneralActions(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 15);
    }

    //logs in to Admin Panel.

    public void login(String login, String password) {
        CustomReporter.log("Login as user - " + login);
        driver.navigate().to(Properties.getBaseAdminUrl());

        driver
            .findElement(By.id("email"))
            .sendKeys(login);

        driver
            .findElement(By.id("passwd"))
            .sendKeys(password);

        driver
            .findElement(By.name("submitLogin"))
            .click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("main")));
    }

    public void createProduct(ProductData product) {
        CustomReporter.log(String.format("Product %s, Price: %s, QTY: %s, Weight: %s", product.getName(), product.getPrice(), product.getQty(), product.getWeight()));
        WebElement element;
        element = waitForContentLoad(By.id("form_step1_name_1"));
        element.sendKeys(product.getName());
        element = waitForContentLoad(By.id("form_step6_reference"));
        element.sendKeys(product.getKey() );
        element = waitForContentLoad(By.id("form_step1_qty_0_shortcut"));
        element.sendKeys(Integer.toString(product.getQty()));
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"), Integer.toString(product.getQty()));
        element = waitForContentLoad(By.id("form_step1_price_shortcut"));
        wait.until(ExpectedConditions.visibilityOfElementLocated((By.id("form_step1_price_shortcut"))));
        scrollTo(element);
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"), product.getPrice());
        element = driver.findElement(By.id("add_feature_button"));
        scrollTo(element);
        element.click();
        Select slElement = new Select(waitForContentLoad(By.id("form_step1_features_0_feature")));
        slElement.selectByValue("4");
        element = waitForContentLoad(By.id("form_step1_features_0_custom_value_1"));
        element.sendKeys(product.getWeight());
        element = driver.findElement(By.className("switch-input"));
        element.click();
        wait.until(ExpectedConditions.textToBe(By.className("growl-message"), "Настройки обновлены."));
        driver.findElement(By.cssSelector(".growl-close")).click();
        element = waitForClickable(By.id("submit")); // product_form_save_go_to_catalog_btn
        element.click();
        wait.until(ExpectedConditions.textToBe(By.className("growl-message"), "Настройки обновлены."));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".growl-close")));
        driver.findElement(By.cssSelector(".growl-close")).click();
        element = waitForClickable(By.id("product_form_save_go_to_catalog_btn"));
        element.click();
        waitForContentLoad(By.id("page-header-desc-configuration-add"));
    }

    // all-product-link

    public void checkProduct(ProductData product){
        WebElement element = driver.findElement(By.xpath("//a[@class='all-product-link pull-xs-left pull-md-right h4']"));
        scrollTo(element);
        element.click();
        element = waitForContentLoad(By.linkText(product.getName()));
        element.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='content-wrapper']//h1")));
        element = waitForContentLoad(By.xpath("//div[@id='content-wrapper']//h1"));
        CustomReporter.log("check the breadcrumb name - " + element.getText());
        Assert.assertEquals(element.getText(), product.getName());
        element = waitForContentLoad(By.cssSelector(".current-price span"));
        CustomReporter.log("check the price - " + element.getAttribute("content"));

        String price = product.getPrice();
        if(!element.getAttribute("content").contains(",")) {
            price = price.replace(',', '.');
        }
        Assert.assertEquals(element.getAttribute("content"), price);

        element = waitForContentLoad(By.cssSelector(".product-quantities span"));
        CustomReporter.log("check the QTY - " + element.getText());
        Assert.assertEquals(element.getText(), product.getQty() + " Товары");

        element = waitForContentLoad(By.cssSelector("#product-details > section > dl > dt"));
        CustomReporter.log("check the weight label - " + element.getText());
        Assert.assertEquals(element.getText(), "Weight");

        element = waitForContentLoad(By.cssSelector("#product-details > section > dl > dd"));
        CustomReporter.log("check the weight - " + element.getText());
        Assert.assertEquals(element.getText(), product.getWeight());
    }

    public void open() {
        driver.navigate().to(myprojects.automation.assignment4.utils.Properties.getBaseUrl());
        waitForContentLoad(By.id("main"));
    }
    private void goToCatalogGoods() {
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("subtab-AdminCatalog"))));
        driver.findElement(By.id("subtab-AdminCatalog")).click();
    }
    public void goToCreateNewProduct() {
        goToCatalogGoods();
        waitForContentLoad(By.id("page-header-desc-configuration-add")).click();
    }
    private WebElement waitForContentLoad(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    private WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    private void scrollTo(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }
}
