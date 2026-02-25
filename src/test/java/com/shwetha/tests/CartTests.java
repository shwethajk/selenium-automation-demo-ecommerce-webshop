// // package com.shwetha.tests;

// // import com.shwetha.framework.base.BaseTests;
// // import com.shwetha.framework.utils.ConfigReader;
// // import com.shwetha.framework.utils.DataProviders;
// // import com.shwetha.pageobjects.*;
// // import org.testng.Assert;
// // import org.testng.annotations.*;

// // public class CartTests extends BaseTests {
// //     private HomePage home;

// //     @BeforeMethod(alwaysRun = true)
// //     public void open() { 
// //         home = new HomePage(getDriver()).goTo(ConfigReader.get("baseUrl")).logoutIfLoggedIn(); 
// //         // System.out.println("\nCartTests: HomePage initialized: " + (home != null)); // Optional: sanity log
// //     }

// //     // private HomePage login2() {
// //     //     return home.clickLogin().loginValid("SJKtestuser123@gmail.com","!QAZ0okm");
// //     // }

// //     private HomePage login(String email, String password) {
// //         return home.clickLogin().loginValid(email, password);
// //     }


// //     // @Test(description="Verify REMOVE from cart functionality, with only one search result (for Laptop)",
// //     //     groups = {"cart","sanity","regression","master"})
// //     // public void addLaptopAndRemove() 
// //     // {
// //     //     home = login();
// //     //     SearchResultsPage results = home.search("Laptop");
// //     //     ProductPage pdp = results.openProduct("14.1-inch Laptop");
// //     //     Assert.assertNotNull(pdp, "PDP not opened");
// //     //     Assert.assertTrue(pdp.hasAddToCartButton());
// //     //     pdp.addToCart();
// //     //     Assert.assertTrue(pdp.addedToCartSuccessfully());
// //     //     CartPage cart = pdp.openCartFromHeader();
// //     //     Assert.assertTrue(cart.containsProduct("14.1-inch Laptop"));
// //     //     cart.removeProduct("14.1-inch Laptop");
// //     // }

// //     @Test(description="Verify REMOVE from cart functionality, with only one search result (for Laptop)",
// //         groups = {"cart","sanity","regression","master"},
// //         dataProvider = "cart-addremove-data", dataProviderClass = DataProviders.class)
// //     public void addLaptopAndRemove(String email, String password, String searchTerm, String productTitle) {
// //         home = login(email, password);
// //         SearchResultsPage results = home.search(searchTerm);
// //         ProductPage pdp = results.openProduct(productTitle);
// //         Assert.assertNotNull(pdp, "PDP not opened");
// //         Assert.assertTrue(pdp.hasAddToCartButton());
// //         pdp.addToCart();
// //         Assert.assertTrue(pdp.addedToCartSuccessfully());
// //         CartPage cart = pdp.openCartFromHeader();
// //         Assert.assertTrue(cart.containsProduct(productTitle));
// //         cart.removeProduct(productTitle);
// //     }

// //     // @Test(description="Verify add to cart functionality, with MULTIPLE search results (for Phone)",
// //     //     groups = {"cart","regression","master"})
// //     // public void addSmartphone() 
// //     // {
// //     //     home = login();
// //     //     ProductPage pdp = home.search("Phone").openProduct("Smartphone");
// //     //     Assert.assertNotNull(pdp);
// //     //     pdp.addToCart();
// //     //     Assert.assertTrue(pdp.addedToCartSuccessfully());
// //     // }

// //     @Test(description="Verify add to cart functionality, with MULTIPLE search results (for Phone)",
// //         groups = {"cart","regression","master"},
// //         dataProvider = "cart-add-data", dataProviderClass = DataProviders.class)
// //     public void addSmartphone(String email, String password, String searchTerm, String productTitle) {
// //         home = login(email, password);
// //         ProductPage pdp = home.search(searchTerm).openProduct(productTitle);
// //         Assert.assertNotNull(pdp);
// //         pdp.addToCart();
// //         Assert.assertTrue(pdp.addedToCartSuccessfully());
// //     }

// //     // @Test(description="Verify add to cart functionality, with MULTIPLE search results (for Phone Cover)",
// //     //     groups = {"cart","regression","master"})
// //     // public void addPhoneCover() 
// //     // {
// //     //     home = login();
// //     //     ProductPage pdp = home.search("Phone Cover").openProduct("Phone Cover");
// //     //     Assert.assertNotNull(pdp);
// //     //     pdp.addToCart();
// //     //     Assert.assertTrue(pdp.addedToCartSuccessfully());
// //     // }

// //     @Test(description="Verify add to cart functionality, with MULTIPLE search results (for Phone Cover)",
// //         groups = {"cart","regression","master"},
// //         dataProvider = "cart-add-data", dataProviderClass = DataProviders.class)
// //     public void addPhoneCover(String email, String password, String searchTerm, String productTitle) {
// //         home = login(email, password);
// //         ProductPage pdp = home.search(searchTerm).openProduct(productTitle);
// //         Assert.assertNotNull(pdp);
// //         pdp.addToCart();
// //         Assert.assertTrue(pdp.addedToCartSuccessfully());
// //     }


// //     // @Test(description="Verify add to cart functionality, with out-of-stock product, no add-to-cart button (for Genuine Leather Handbag)",
// //     //     groups = {"cart","regression","negative", "master"})
// //     // public void outOfStockBagShouldNotAdd() 
// //     // {
// //     //     home = login();
// //     //     ProductPage pdp = home.search("Bag").openProduct("Genuine Leather Handbag with Cell Phone Holder & Many Pockets");
// //     //     Assert.assertNotNull(pdp);
// //     //     pdp.addToCart();
// //     //     Assert.assertFalse(pdp.addedToCartSuccessfully(), "Out-of-stock item should not show success (not be added to cart)");
// //     // }

// //     @Test(description="Verify add to cart functionality, with out-of-stock product, no add-to-cart button (for Genuine Leather Handbag)",
// //         groups = {"cart","regression","negative","master"},
// //         dataProvider = "cart-outofstock-data", dataProviderClass = DataProviders.class)
// //     public void outOfStockBagShouldNotAdd(String email, String password, String searchTerm, String productTitle) {
// //         home = login(email, password);
// //         ProductPage pdp = home.search(searchTerm).openProduct(productTitle);
// //         Assert.assertNotNull(pdp);
// //         pdp.addToCart();
// //         Assert.assertFalse(pdp.addedToCartSuccessfully(), "Out-of-stock item should not show success (not be added to cart)");
// //     }


// //     // @Test(description="Verify failing case: add to cart functionality, with out-of-stock product, no add-to-cart button (for Genuine Leather Handbag)",
// //     //     groups = {"cart","regression","negative", "master"}
// //     //     // retryAnalyzer = com.shwetha.framework.listeners.RetryAnalyzer.class
// //     //     )
// //     // public void FailingoutOfStockBagShouldNotAdd() 
// //     // {
// //     //     log.info("Failing test log start");
// //     //     home = login2();
// //     //     ProductPage pdp = home.search("Bag").openProduct("Genuine Leather Handbag with Cell Phone Holder & Many Pockets");
// //     //     Assert.assertNotNull(pdp);
// //     //     pdp.addToCart();
// //     //     Assert.assertTrue(pdp.addedToCartSuccessfully(), "Out-of-stock item should not show success (not be added to cart)");
// //     //     log.info("Failing test log end");
// //     // }

// //     @Test(description="Verify failing case: add to cart functionality, out-of-stock (intentional fail)",
// //         groups = {"cart","regression","negative","master"},
// //         dataProvider = "cart-failing-data", dataProviderClass = DataProviders.class)
// //     public void FailingoutOfStockBagShouldNotAdd(String email, String password, String searchTerm, String productTitle) {
// //         log.info("Failing test log start");
// //         home = login(email, password);
// //         ProductPage pdp = home.search(searchTerm).openProduct(productTitle);
// //         Assert.assertNotNull(pdp);
// //         pdp.addToCart();
// //         Assert.assertTrue(pdp.addedToCartSuccessfully(), "Out-of-stock item should not show success (not be added to cart)");
// //         log.info("Failing test log end");
// //     }
// // }




// package com.shwetha.tests;

// import com.shwetha.framework.base.BaseTests;
// import com.shwetha.framework.utils.ConfigReader;
// import com.shwetha.framework.utils.DataProvidersMap;
// import com.shwetha.pageobjects.*;
// import org.testng.Assert;
// import org.testng.annotations.*;

// import java.util.Map;

// public class CartTests extends BaseTests {
//     private HomePage home;

//     @BeforeMethod(alwaysRun = true)
//     public void open() { 
//         home = new HomePage(getDriver()).goTo(ConfigReader.get("baseUrl")).logoutIfLoggedIn(); 
//     }

//     private HomePage login(String email, String password) {
//         return home.clickLogin().loginValid(email, password);
//     }

//     @Test(description="Verify REMOVE from cart functionality, with only one search result (for Laptop)",
//         groups = {"cart","sanity","regression","master"},
//         dataProvider = "cart-addremove-data-map", dataProviderClass = DataProvidersMap.class)
//     public void addLaptopAndRemove(Map<String,String> data) {
//         if (!"Laptop".equalsIgnoreCase(data.get("SearchKey"))) return;
//         if (!data.getOrDefault("ProductName","").toLowerCase().contains("14.1-inch laptop")) return;

//         home = login(data.get("Email"), data.get("Password"));
//         SearchResultsPage results = home.search(data.get("SearchKey"));
//         ProductPage pdp = results.openProduct(data.get("ProductName"));
//         Assert.assertNotNull(pdp, "PDP not opened");
//         Assert.assertTrue(pdp.hasAddToCartButton());
//         pdp.addToCart();
//         Assert.assertTrue(pdp.addedToCartSuccessfully());
//         CartPage cart = pdp.openCartFromHeader();
//         Assert.assertTrue(cart.containsProduct(data.get("ProductName")));
//         cart.removeProduct(data.get("ProductName"));
//     }

//     @Test(description="Verify add to cart functionality, with MULTIPLE search results (for Phone)",
//         groups = {"cart","regression","master"},
//         dataProvider = "cart-add-data-map", dataProviderClass = DataProvidersMap.class)
//     public void addSmartphone(Map<String,String> data) {
//         if (!"Phone".equalsIgnoreCase(data.get("SearchKey"))) return;
//         if (!"Smartphone".equalsIgnoreCase(data.get("ProductName"))) return;

//         home = login(data.get("Email"), data.get("Password"));
//         ProductPage pdp = home.search(data.get("SearchKey")).openProduct(data.get("ProductName"));
//         Assert.assertNotNull(pdp);
//         pdp.addToCart();
//         Assert.assertTrue(pdp.addedToCartSuccessfully());
//     }

//     @Test(description="Verify add to cart functionality, with MULTIPLE search results (for Phone Cover)",
//         groups = {"cart","regression","master"},
//         dataProvider = "cart-add-data-map", dataProviderClass = DataProvidersMap.class)
//     public void addPhoneCover(Map<String,String> data) {
//         if (!"Phone".equalsIgnoreCase(data.get("SearchKey")) && 
//             !"Phone Cover".equalsIgnoreCase(data.get("SearchKey"))) return;
//         if (!"Phone Cover".equalsIgnoreCase(data.get("ProductName"))) return;

//         home = login(data.get("Email"), data.get("Password"));
//         ProductPage pdp = home.search(data.get("SearchKey")).openProduct(data.get("ProductName"));
//         Assert.assertNotNull(pdp);
//         pdp.addToCart();
//         Assert.assertTrue(pdp.addedToCartSuccessfully());
//     }

//     @Test(description="Verify add to cart functionality, with out-of-stock product, no add-to-cart button (for Genuine Leather Handbag)",
//         groups = {"cart","regression","negative","master"},
//         dataProvider = "cart-outofstock-data-map", dataProviderClass = DataProvidersMap.class)
//     public void outOfStockBagShouldNotAdd(Map<String,String> data) {
//         if (!"N".equalsIgnoreCase(data.getOrDefault("ExpectedAdd",""))) return;

//         home = login(data.get("Email"), data.get("Password"));
//         ProductPage pdp = home.search(data.get("SearchKey")).openProduct(data.get("ProductName"));
//         Assert.assertNotNull(pdp);
//         pdp.addToCart();
//         Assert.assertFalse(pdp.addedToCartSuccessfully(), "Out-of-stock item should not show success (not be added to cart)");
//     }

//     @Test(description="Verify failing case: add to cart functionality, out-of-stock (intentional fail)",
//         groups = {"cart","regression","negative","master"},
//         dataProvider = "cart-failing-data-map", dataProviderClass = DataProvidersMap.class)
//     public void FailingoutOfStockBagShouldNotAdd(Map<String,String> data) {
//         if (!"N".equalsIgnoreCase(data.getOrDefault("ExpectedAdd",""))) return;

//         log.info("Failing test log start");
//         home = login(data.get("Email"), data.get("Password"));
//         ProductPage pdp = home.search(data.get("SearchKey")).openProduct(data.get("ProductName"));
//         Assert.assertNotNull(pdp);
//         pdp.addToCart();
//         Assert.assertTrue(pdp.addedToCartSuccessfully(), "Out-of-stock item should not show success (not be added to cart)");
//         log.info("Failing test log end");
//     }
// }



package com.shwetha.tests;

import com.shwetha.framework.base.BaseTests;
import com.shwetha.framework.utils.ConfigReader;
import com.shwetha.framework.utils.DataProvidersMap;
import com.shwetha.pageobjects.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Map;

public class CartTests extends BaseTests {
    private HomePage home;

    @BeforeMethod(alwaysRun = true)
    public void open() {
        home = new HomePage(getDriver()).goTo(ConfigReader.get("baseUrl")).logoutIfLoggedIn();
    }

    private HomePage login(String email, String password) {
        return home.clickLogin().loginValid(email, password);
    }

    @Test(description="Verify REMOVE from cart functionality, with only one search result (for Laptop)",
            groups = {"cart","sanity","regression","master"},
            dataProvider = "cart-addremove-data-map", dataProviderClass = DataProvidersMap.class)
    public void addLaptopAndRemove(Map<String,String> data) {
        home = login(data.get("Email"), data.get("Password"));
        SearchResultsPage results = home.search(data.get("SearchKey"));
        ProductPage pdp = results.openProduct(data.get("ProductName"));
        Assert.assertNotNull(pdp, "PDP not opened");
        Assert.assertTrue(pdp.hasAddToCartButton());
        pdp.addToCart();
        Assert.assertTrue(pdp.addedToCartSuccessfully());
        CartPage cart = pdp.openCartFromHeader();
        Assert.assertTrue(cart.containsProduct(data.get("ProductName")));
        cart.removeProduct(data.get("ProductName"));
    }

    @Test(description="Verify add to cart functionality, with MULTIPLE search results (Smartphone)",
            groups = {"cart","regression","master"},
            dataProvider = "cart-add-smartphone-data-map", dataProviderClass = DataProvidersMap.class)
    public void addSmartphone(Map<String,String> data) {
        home = login(data.get("Email"), data.get("Password"));
        ProductPage pdp = home.search(data.get("SearchKey")).openProduct(data.get("ProductName"));
        Assert.assertNotNull(pdp);
        pdp.addToCart();
        Assert.assertTrue(pdp.addedToCartSuccessfully());
    }

    @Test(description="Verify add to cart functionality, with MULTIPLE search results (Phone Cover)",
            groups = {"cart","regression","master"},
            dataProvider = "cart-add-phonecover-data-map", dataProviderClass = DataProvidersMap.class)
    public void addPhoneCover(Map<String,String> data) {
        home = login(data.get("Email"), data.get("Password"));
        ProductPage pdp = home.search(data.get("SearchKey")).openProduct(data.get("ProductName"));
        Assert.assertNotNull(pdp);
        pdp.addToCart();
        Assert.assertTrue(pdp.addedToCartSuccessfully());
    }

    @Test(description="Verify add to cart functionality, with out-of-stock product (no add-to-cart button)",
            groups = {"cart","regression","negative","master"},
            dataProvider = "cart-outofstock-data-map", dataProviderClass = DataProvidersMap.class)
    public void outOfStockBagShouldNotAdd(Map<String,String> data) {
        home = login(data.get("Email"), data.get("Password"));
        ProductPage pdp = home.search(data.get("SearchKey")).openProduct(data.get("ProductName"));
        Assert.assertNotNull(pdp);
        pdp.addToCart();
        Assert.assertFalse(pdp.addedToCartSuccessfully(),
                "Out-of-stock item should not show success (not be added to cart)");
    }

    @Test(description="Verify failing case (intentional retry): add to cart functionality, out-of-stock (intentional fail)",
            groups = {"cart","regression","negative","master"},
            dataProvider = "cart-failing-data-map", dataProviderClass = DataProvidersMap.class)
    public void FailingoutOfStockBagShouldNotAdd(Map<String,String> data) {
        log.info("Failing test log start");
        home = login(data.get("Email"), data.get("Password"));
        ProductPage pdp = home.search(data.get("SearchKey")).openProduct(data.get("ProductName"));
        Assert.assertNotNull(pdp);
        pdp.addToCart();
        Assert.assertTrue(pdp.addedToCartSuccessfully(),
                "Out-of-stock item should not show success (not be added to cart)");
        log.info("Failing test log end");
    }
}