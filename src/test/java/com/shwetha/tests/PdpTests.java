// // package com.shwetha.tests;

// // import com.shwetha.framework.base.BaseTests;
// // import com.shwetha.framework.utils.ConfigReader;
// // import com.shwetha.framework.utils.DataProviders;
// // import com.shwetha.pageobjects.*;
// // import org.testng.Assert;
// // import org.testng.SkipException;
// // import org.testng.annotations.*;

// // public class PdpTests extends BaseTests {
// //     private HomePage home;

// //     @BeforeMethod(alwaysRun = true)
// //     public void open() { 
// //         home = new HomePage(getDriver()).goTo(ConfigReader.get("baseUrl")); 
// //         // System.out.println("\nPdpTests: HomePage initialized: " + (home != null)); // Optional: sanity log
// //     }

// //     // @Test(description="Verify add to cart functionality with no add-to-cart button, ",
// //     //     groups={"pdp","regression","negative", "master"}, 
// //     //     retryAnalyzer = com.shwetha.framework.listeners.RetryAnalyzer.class)
// //     // public void womensRunningShoeHasNoAddButton() {
// //     //     SearchResultsPage results = home.search("Shoe");
// //     //     ProductPage pdp = results.openProduct("Women's Running Shoe");
// //     //     Assert.assertNotNull(pdp);
// //     //     Assert.assertFalse(pdp.hasAddToCartButton(), "Product now has Add to cart button; update test data.");
// //     //     Assert.assertFalse(pdp.addedToCartSuccessfully(), "Out-of-stock item should not show success (not be added to cart)");
// //     // }


// //    @Test(description="Verify PDP add-to-cart availability and behavior (data-driven)",
// //       groups={"pdp","regression","negative","master"},
// //       dataProvider = "pdp-data", dataProviderClass = DataProviders.class)
// //     public void womensRunningShoeHasNoAddButton(
// //             String tcId, String execute, String searchKey, String productName,
// //             String expectedHasAddButton, String expectedAdd) 
// //     {
// //         if (!"Y".equalsIgnoreCase(execute)) {
// //             throw new SkipException("Execute!=Y for " + tcId);
// //         }
// //         SearchResultsPage results = home.search(searchKey);
// //         ProductPage pdp = results.openProduct(productName);
// //         Assert.assertNotNull(pdp, "PDP not opened for: " + productName);

// //         boolean hasBtn = pdp.hasAddToCartButton();
// //         if ("Y".equalsIgnoreCase(expectedHasAddButton)) {
// //             Assert.assertTrue(hasBtn, "Expected Add to cart button for: " + productName);
// //         } else {
// //             Assert.assertFalse(hasBtn,
// //                     "Product now has Add to cart button; update test data for: " + productName);
// //         }
// //         // Call addToCart (your page object may no-op when button is absent)
// //         pdp.addToCart();
// //         if ("Y".equalsIgnoreCase(expectedAdd)) {
// //             Assert.assertTrue(pdp.addedToCartSuccessfully(),
// //                     "Expected 'add to cart' success for: " + productName);
// //         } else {
// //             Assert.assertFalse(pdp.addedToCartSuccessfully(),
// //                     "Out-of-stock item should not show success (not be added to cart): " + productName);
// //         }
// //     }
    
// // }




// package com.shwetha.tests;

// import com.shwetha.framework.base.BaseTests;
// import com.shwetha.framework.utils.ConfigReader;
// import com.shwetha.framework.utils.DataProvidersMap;
// import com.shwetha.pageobjects.*;
// import org.testng.Assert;
// import org.testng.SkipException;
// import org.testng.annotations.*;

// import java.util.Map;

// public class PdpTests extends BaseTests {
//     private HomePage home;

//     @BeforeMethod(alwaysRun = true)
//     public void open() { 
//         home = new HomePage(getDriver()).goTo(ConfigReader.get("baseUrl")); 
//     }

//     @Test(description="Verify PDP add-to-cart availability and behavior (data-driven)",
//       groups={"pdp","regression","negative","master"},
//       dataProvider = "pdp-data-map", dataProviderClass = DataProvidersMap.class)
//     public void womensRunningShoeHasNoAddButton(Map<String,String> data) {
//         String execute = data.getOrDefault("Execute","Y");
//         if (!"Y".equalsIgnoreCase(execute)) throw new SkipException("Execute!=Y for " + data.getOrDefault("TC_ID",""));

//         SearchResultsPage results = home.search(data.get("SearchKey"));
//         ProductPage pdp = results.openProduct(data.get("ProductName"));
//         Assert.assertNotNull(pdp, "PDP not opened for: " + data.get("ProductName"));

//         boolean hasBtn = pdp.hasAddToCartButton();
//         if ("Y".equalsIgnoreCase(data.getOrDefault("ExpectedHasAddButton","N"))) {
//             Assert.assertTrue(hasBtn, "Expected Add to cart button for: " + data.get("ProductName"));
//         } else {
//             Assert.assertFalse(hasBtn, "Product now has Add to cart button; update test data for: " + data.get("ProductName"));
//         }

//         pdp.addToCart();
//         if ("Y".equalsIgnoreCase(data.getOrDefault("ExpectedAdd","N"))) {
//             Assert.assertTrue(pdp.addedToCartSuccessfully(),
//                     "Expected 'add to cart' success for: " + data.get("ProductName"));
//         } else {
//             Assert.assertFalse(pdp.addedToCartSuccessfully(),
//                     "Out-of-stock item should not show success (not be added to cart): " + data.get("ProductName"));
//         }
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

public class PdpTests extends BaseTests {
    private HomePage home;

    @BeforeMethod(alwaysRun = true)
    public void open() {
        home = new HomePage(getDriver()).goTo(ConfigReader.get("baseUrl"));
    }

    @Test(description="Verify PDP add-to-cart availability and behavior (data-driven)",
            groups={"pdp","regression","negative","master"},
            dataProvider = "pdp-data-map", dataProviderClass = DataProvidersMap.class)
    public void womensRunningShoeHasNoAddButton(Map<String,String> data) {
        SearchResultsPage results = home.search(data.get("SearchKey"));
        ProductPage pdp = results.openProduct(data.get("ProductName"));
        Assert.assertNotNull(pdp, "PDP not opened for: " + data.get("ProductName"));

        boolean hasBtn = pdp.hasAddToCartButton();
        if ("Y".equalsIgnoreCase(data.getOrDefault("ExpectedHasAddButton","N"))) {
            Assert.assertTrue(hasBtn, "Expected Add to cart button for: " + data.get("ProductName"));
        } else {
            Assert.assertFalse(hasBtn,
                    "Product now has Add to cart button; update test data for: " + data.get("ProductName"));
        }

        pdp.addToCart();
        if ("Y".equalsIgnoreCase(data.getOrDefault("ExpectedAdd","N"))) {
            Assert.assertTrue(pdp.addedToCartSuccessfully(),
                    "Expected 'add to cart' success for: " + data.get("ProductName"));
        } else {
            Assert.assertFalse(pdp.addedToCartSuccessfully(),
                    "Out-of-stock item should not show success (not be added to cart): " + data.get("ProductName"));
        }
    }
}