// // package com.shwetha.tests;

// // import com.shwetha.framework.base.BaseTests;
// // import com.shwetha.framework.utils.ConfigReader;
// // import com.shwetha.framework.utils.DataProviders;
// // import com.shwetha.pageobjects.*;
// // import org.testng.Assert;
// // import org.testng.annotations.*;

// // public class SearchTests extends BaseTests {
// //     private HomePage home;

// //     @BeforeMethod(alwaysRun = true)
// //     public void open() { 
// //         home = new HomePage(getDriver()).goTo(ConfigReader.get("baseUrl")); 
// //         // System.out.println("\nSearchTests: HomePage initialized: " + (home != null)); // Optional: sanity log
// //     }

// //     // @Test(description="Verify product search functionality",
// //     //     groups = {"search","smoke","master"})
// //     // public void searchFound() {
// //     //     SearchResultsPage results = home.search("Laptop");
// //     //     Assert.assertTrue(results.hasAnyMatch("Laptop"), "Expected results for 'Laptop'");
// //     // }

// //     @Test(description="Verify product search functionality",
// //         groups = {"search","smoke","master"},
// //         dataProvider = "search-found-data", dataProviderClass = DataProviders.class)
// //     public void searchFound(String query, String expectedMatch) {
// //         SearchResultsPage results = home.search(query);
// //         Assert.assertTrue(results.hasAnyMatch(expectedMatch),
// //             "Expected results for '" + expectedMatch + "'");
// //     }

// //     // @Test(description="Verify search functionality with non-existent product",
// //     //     groups = {"search","sanity","regression","negative", "master"})
// //     // public void searchNotFound() {
// //     //     SearchResultsPage results = home.search("Mouse");
// //     //     Assert.assertFalse(results.hasAnyMatch("Mouse"), "Expected no 'Mouse'");
// //     // }

// //     @Test(description="Verify search functionality with non-existent product",
// //         groups = {"search","sanity","regression","negative","master"},
// //         dataProvider = "search-notfound-data", dataProviderClass = DataProviders.class)
// //     public void searchNotFound(String query, String expectedNoMatch) {
// //         SearchResultsPage results = home.search(query);
// //         Assert.assertFalse(results.hasAnyMatch(expectedNoMatch),
// //             "Expected no '" + expectedNoMatch + "'");
// //     }

// //     // @Test(description=" Verify product filter functionality",
// //     //     groups = {"search","filter","sanity","regression","master"})
// //     // public void applySearchFilter() {
// //     //     SearchResultsPage results = home.search("Phone");
// //     //     Assert.assertTrue(results.hasAnyMatch("Phone"), "Expected results for 'Phone'");
// //     //     home.applyCategoryFilter("Electronics >> Cell phones"); // advanced search - apply filters
// //     //     Assert.assertTrue(results.hasAnyMatch("Phone"), "Filter not applied successfully / Expected results for 'Electronics >> Cell phones'");
// //     // }

// //     @Test(description="Verify product filter functionality",
// //         groups = {"search","filter","sanity","regression","master"},
// //         dataProvider = "search-filter-data", dataProviderClass = DataProviders.class)
// //     public void applySearchFilter(String query, String filter, String expectedMatchAfterFilter) {
// //         SearchResultsPage results = home.search(query);
// //         Assert.assertTrue(results.hasAnyMatch(query), "Expected results for '" + query + "'");
// //         // Use >> literally (not HTML entity)
// //         home.applyCategoryFilter(filter);
// //         Assert.assertTrue(results.hasAnyMatch(expectedMatchAfterFilter),
// //             "Filter not applied successfully / Expected results for '" + filter + "'");
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

// public class SearchTests extends BaseTests {
//     private HomePage home;

//     @BeforeMethod(alwaysRun = true)
//     public void open() { 
//         home = new HomePage(getDriver()).goTo(ConfigReader.get("baseUrl")); 
//     }

//     @Test(description="Verify product search functionality",
//         groups = {"search","smoke","master"},
//         dataProvider = "search-found-data-map", dataProviderClass = DataProvidersMap.class)
//     public void searchFound(Map<String,String> data) {
//         if (!"Y".equalsIgnoreCase(data.getOrDefault("ExpectedFound",""))) return;
//         String query = data.get("Keyword");
//         SearchResultsPage results = home.search(query);
//         Assert.assertTrue(results.hasAnyMatch(query), "Expected results for '" + query + "'");
//     }

//     @Test(description="Verify search functionality with non-existent product",
//         groups = {"search","sanity","regression","negative","master"},
//         dataProvider = "search-notfound-data-map", dataProviderClass = DataProvidersMap.class)
//     public void searchNotFound(Map<String,String> data) {
//         if (!"N".equalsIgnoreCase(data.getOrDefault("ExpectedFound",""))) return;
//         String query = data.get("Keyword");
//         SearchResultsPage results = home.search(query);
//         Assert.assertFalse(results.hasAnyMatch(query), "Expected no '" + query + "'");
//     }

//     @Test(description="Verify product filter functionality",
//         groups = {"search","filter","sanity","regression","master"},
//         dataProvider = "search-filter-data-map", dataProviderClass = DataProvidersMap.class)
//     public void applySearchFilter(Map<String,String> data) {
//         String query = data.get("Keyword");
//         String filter = data.get("Filter");
//         String expectedMatchAfterFilter = data.get("ExpectedMatchAfterFilter");

//         SearchResultsPage results = home.search(query);
//         Assert.assertTrue(results.hasAnyMatch(query), "Expected results for '" + query + "'");
//         // If your sheet uses HTML entity, unescape it here:
//         if (filter != null) filter = filter.replace("&gt;&gt;", ">>");
//         home.applyCategoryFilter(filter);
//         Assert.assertTrue(results.hasAnyMatch(expectedMatchAfterFilter),
//             "Filter not applied successfully / Expected results for '" + filter + "'");
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

public class SearchTests extends BaseTests {
    private HomePage home;

    @BeforeMethod(alwaysRun = true)
    public void open() {
        home = new HomePage(getDriver()).goTo(ConfigReader.get("baseUrl"));
    }

    @Test(description="Verify product search functionality",
            groups = {"search","smoke","master"},
            dataProvider = "search-found-data-map", dataProviderClass = DataProvidersMap.class)
    public void searchFound(Map<String,String> data) {
        String query = data.get("Keyword");
        SearchResultsPage results = home.search(query);
        Assert.assertTrue(results.hasAnyMatch(query), "Expected results for '" + query + "'");
    }

    @Test(description="Verify search functionality with non-existent product",
            groups = {"search","sanity","regression","negative","master"},
            dataProvider = "search-notfound-data-map", dataProviderClass = DataProvidersMap.class)
    public void searchNotFound(Map<String,String> data) {
        String query = data.get("Keyword");
        SearchResultsPage results = home.search(query);
        Assert.assertFalse(results.hasAnyMatch(query), "Expected no '" + query + "'");
    }

    @Test(description="Verify product filter functionality",
            groups = {"search","filter","sanity","regression","master"},
            dataProvider = "search-filter-data-map", dataProviderClass = DataProvidersMap.class)
    public void applySearchFilter(Map<String,String> data) {
        String query = data.get("Keyword");
        String filter = data.get("Filter");
        String expected = data.get("ExpectedMatchAfterFilter");

        // If your sheet has HTML entity for >>, normalize it:
        if (filter != null) filter = filter.replace("&gt;&gt;", ">>").replace("&amp;gt;&amp;gt;", ">>");

        SearchResultsPage results = home.search(query);
        Assert.assertTrue(results.hasAnyMatch(query), "Expected results for '" + query + "'");
        home.applyCategoryFilter(filter);
        Assert.assertTrue(results.hasAnyMatch(expected),
                "Filter not applied successfully / Expected results for '" + filter + "'");
    }
}