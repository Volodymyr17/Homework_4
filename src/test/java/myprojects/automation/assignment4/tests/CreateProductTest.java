package myprojects.automation.assignment4.tests;

import myprojects.automation.assignment4.BaseTest;
import myprojects.automation.assignment4.model.ProductData;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class CreateProductTest extends BaseTest {
    private ProductData product = ProductData.generate();
   @DataProvider(name = "Authentication")
    public static Object[][] credentials() {
        return new Object[][] { { "webinar.test@gmail.com", "Xcg7299bnSmMuRLp9ITw" }};
    }

    @Test(dataProvider = "Authentication")
    public void createNewProduct(String login, String password) {
        actions.login(login, password);
        actions.goToCreateNewProduct();
        actions.createProduct(product);
    }

    @Test(dependsOnMethods = "createNewProduct")
    public void checkProduct(){
        actions.open();
        actions.checkProduct(product);
    }
}
