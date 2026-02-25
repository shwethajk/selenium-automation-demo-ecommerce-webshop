
// // /**
// //  * Map-based DataProviders that return Object[][] where each row is a Map<String,String>.
// //  * - Column order in Excel does NOT matter; keys are the header names.
// //  * - Only rows with Execute=Y are returned (handled inside readSheetAsMap).
// //  *
// //  * Sheets expected:
// //  *  - Login         : TC_ID | Execute | Username | Password | ExpectedResult
// //  *  - Search        : TC_ID | Execute | Keyword  | ExpectedFound
// //  *  - Search_Filter : TC_ID | Execute | Keyword  | Filter   | ExpectedMatchAfterFilter
// //  *  - Cart          : TC_ID | Execute | Email | Password | SearchKey | ProductName | ExpectedAdd
// //  *  - PDP           : TC_ID | Execute | SearchKey | ProductName | ExpectedHasAddButton | ExpectedAdd
// //  */



// // // package com.shwetha.framework.utils;

// // // import org.testng.annotations.DataProvider;
// // // import java.util.ArrayList;
// // // import java.util.List;
// // // import java.util.Map;

// // // public class DataProvidersMap {

// // //     private static String dataFile() {
// // //         String p = ConfigReader.get("testDataFile");
// // //         return (p == null || p.isBlank())
// // //                 ? "src/test/resources/testdata/TestData.xlsx"
// // //                 : p;
// // //     }

// // //     /** Keep only rows whose map satisfies the predicate. */
// // //     private static Object[][] filter(Object[][] all, java.util.function.Predicate<Map<String,String>> keep) {
// // //         List<Object[]> out = new ArrayList<>();
// // //         for (Object[] row : all) {
// // //             @SuppressWarnings("unchecked")
// // //             Map<String,String> m = (Map<String,String>) row[0];
// // //             if (keep.test(m)) out.add(new Object[]{ m });
// // //         }
// // //         return out.toArray(new Object[0][]);
// // //     }

// // //     // ======================
// // //     // LOGIN (sheet: Login)
// // //     // ======================

// // //     /** All Execute=Y rows — use only for scenario tests that branch by ExpectedResult. */
// // //     // parallel = false will be overridden by config property, recommended to omit 'parallel'
// // //     @DataProvider(name = "login-data-map", parallel = false)
// // //     public Object[][] loginDataMap() {
// // //         return ExcelUtils.readSheetAsMap(dataFile(), "Login", true);
// // //     }

// // //     /** Only SUCCESS rows (Execute=Y & ExpectedResult=SUCCESS). */
// // //     @DataProvider(name = "login-valid-data-map", parallel = false)
// // //     public Object[][] loginValidDataMap() {
// // //         Object[][] all = ExcelUtils.readSheetAsMap(dataFile(), "Login", true);
// // //         return filter(all, m -> "SUCCESS".equalsIgnoreCase(m.getOrDefault("ExpectedResult","")));
// // //     }

// // //     /** Only ERROR rows (Execute=Y & ExpectedResult=ERROR). */
// // //     @DataProvider(name = "login-invalid-data-map", parallel = false)
// // //     public Object[][] loginInvalidDataMap() {
// // //         Object[][] all = ExcelUtils.readSheetAsMap(dataFile(), "Login", true);
// // //         return filter(all, m -> "ERROR".equalsIgnoreCase(m.getOrDefault("ExpectedResult","")));
// // //     }

// // //     // ======================
// // //     // SEARCH (sheet: Search)
// // //     // ======================

// // //     /** Only ExpectedFound=Y rows. */
// // //     @DataProvider(name = "search-found-data-map", parallel = true)
// // //     public Object[][] searchFoundDataMap() {
// // //         Object[][] all = ExcelUtils.readSheetAsMap(dataFile(), "Search", true);
// // //         return filter(all, m -> "Y".equalsIgnoreCase(m.getOrDefault("ExpectedFound","")));
// // //     }

// // //     /** Only ExpectedFound=N rows. */
// // //     @DataProvider(name = "search-notfound-data-map", parallel = true)
// // //     public Object[][] searchNotFoundDataMap() {
// // //         Object[][] all = ExcelUtils.readSheetAsMap(dataFile(), "Search", true);
// // //         return filter(all, m -> "N".equalsIgnoreCase(m.getOrDefault("ExpectedFound","")));
// // //     }

// // //     // ============================
// // //     // SEARCH FILTER (Search_Filter)
// // //     // ============================

// // //     @DataProvider(name = "search-filter-data-map", parallel = true)
// // //     public Object[][] searchFilterDataMap() {
// // //         return ExcelUtils.readSheetAsMap(dataFile(), "Search_Filter", true);
// // //     }

// // //     // ======================
// // //     // CART (sheet: Cart)
// // //     // ======================

// // //     /** C001: Laptop add+remove (14.1-inch Laptop) */
// // //     @DataProvider(name = "cart-addremove-data-map", parallel = false)
// // //     public Object[][] cartAddRemoveDataMap() {
// // //         Object[][] all = ExcelUtils.readSheetAsMap(dataFile(), "Cart", true);
// // //         return filter(all, m ->
// // //             "Laptop".equalsIgnoreCase(m.getOrDefault("SearchKey","")) &&
// // //             m.getOrDefault("ProductName","").toLowerCase().contains("14.1-inch laptop")
// // //         );
// // //     }

// // //     /** C002–C003: Phone + Phone Cover additions */
// // //     @DataProvider(name = "cart-add-data-map", parallel = false)
// // //     public Object[][] cartAddDataMap() {
// // //         Object[][] all = ExcelUtils.readSheetAsMap(dataFile(), "Cart", true);
// // //         return filter(all, m -> {
// // //             String sk = m.getOrDefault("SearchKey","");
// // //             return "Phone".equalsIgnoreCase(sk) || "Phone Cover".equalsIgnoreCase(sk);
// // //         });
// // //     }

// // //     /** C004: Out-of-stock negative (ExpectedAdd=N) */
// // //     @DataProvider(name = "cart-outofstock-data-map", parallel = false)
// // //     public Object[][] cartOutOfStockDataMap() {
// // //         Object[][] all = ExcelUtils.readSheetAsMap(dataFile(), "Cart", true);
// // //         return filter(all, m -> "N".equalsIgnoreCase(m.getOrDefault("ExpectedAdd","")));
// // //     }

// // //     /** Intentional failing slice — same as out-of-stock (test asserts opposite). */
// // //     @DataProvider(name = "cart-failing-data-map", parallel = false)
// // //     public Object[][] cartFailingDataMap() {
// // //         return cartOutOfStockDataMap();
// // //     }

// // //     // ======================
// // //     // PDP (sheet: PDP)
// // //     // ======================

// // //     @DataProvider(name = "pdp-data-map", parallel = true)
// // //     public Object[][] pdpDataMap() {
// // //         return ExcelUtils.readSheetAsMap(dataFile(), "PDP", true);
// // //     }
// // // }


// /*
// EXCEL data providers
// */

// package com.shwetha.framework.utils;

// import org.testng.annotations.DataProvider;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.Map;

// public class DataProvidersMap {

//     private static String dataFile() {
//         String p = ConfigReader.get("testDataFile");
//         return (p == null || p.isBlank())
//                 ? "src/test/resources/testdata/EXCELTestData.xlsx"
//                 : p;
//     }

//     /** Keep only rows whose map satisfies the predicate. */
//     private static Object[][] filter(Object[][] all, java.util.function.Predicate<Map<String,String>> keep) {
//         List<Object[]> out = new ArrayList<>();
//         for (Object[] row : all) {
//             @SuppressWarnings("unchecked")
//             Map<String,String> m = (Map<String,String>) row[0];
//             if (keep.test(m)) out.add(new Object[]{ m });
//         }
//         return out.toArray(new Object[0][]);
//     }

//     // ======================
//     // LOGIN (sheet: Login)
//     // ======================

//     /** All Execute=Y rows — use only for scenario tests that branch by ExpectedResult. */
//     @DataProvider(name = "login-data-map")
//     public Object[][] loginDataMap() {
//         return ExcelUtils.readSheetAsMap(dataFile(), "Login", true);
//     }

//     /** Only SUCCESS rows (Execute=Y & ExpectedResult=SUCCESS). */
//     @DataProvider(name = "login-valid-data-map")
//     public Object[][] loginValidDataMap() {
//         Object[][] all = ExcelUtils.readSheetAsMap(dataFile(), "Login", true);
//         return filter(all, m -> "SUCCESS".equalsIgnoreCase(m.getOrDefault("ExpectedResult","")));
//     }

//     /** Only ERROR rows (Execute=Y & ExpectedResult=ERROR). */
//     @DataProvider(name = "login-invalid-data-map")
//     public Object[][] loginInvalidDataMap() {
//         Object[][] all = ExcelUtils.readSheetAsMap(dataFile(), "Login", true);
//         return filter(all, m -> "ERROR".equalsIgnoreCase(m.getOrDefault("ExpectedResult","")));
//     }

//     // ======================
//     // SEARCH (sheet: Search)
//     // ======================

//     /** Only ExpectedFound=Y rows. */
//     @DataProvider(name = "search-found-data-map")
//     public Object[][] searchFoundDataMap() {
//         Object[][] all = ExcelUtils.readSheetAsMap(dataFile(), "Search", true);
//         return filter(all, m -> "Y".equalsIgnoreCase(m.getOrDefault("ExpectedFound","")));
//     }

//     /** Only ExpectedFound=N rows. */
//     @DataProvider(name = "search-notfound-data-map")
//     public Object[][] searchNotFoundDataMap() {
//         Object[][] all = ExcelUtils.readSheetAsMap(dataFile(), "Search", true);
//         return filter(all, m -> "N".equalsIgnoreCase(m.getOrDefault("ExpectedFound","")));
//     }

//     // ============================
//     // SEARCH FILTER (Search_Filter)
//     // ============================

//     @DataProvider(name = "search-filter-data-map")
//     public Object[][] searchFilterDataMap() {
//         return ExcelUtils.readSheetAsMap(dataFile(), "Search_Filter", true);
//     }

//     // ======================
//     // CART (sheet: Cart)
//     // ======================

//     /** C001: Laptop add+remove (14.1-inch Laptop). */
//     @DataProvider(name = "cart-addremove-data-map")
//     public Object[][] cartAddRemoveDataMap() {
//         Object[][] all = ExcelUtils.readSheetAsMap(dataFile(), "Cart", true);
//         return filter(all, m ->
//              "Laptop".equalsIgnoreCase(m.getOrDefault("SearchKey","")) &&
//              m.getOrDefault("ProductName","").equalsIgnoreCase("14.1-inch Laptop")
//         );
//     }

//     /** C002: Add Smartphone (SearchKey=Phone, ProductName=Smartphone). */
//     @DataProvider(name = "cart-add-smartphone-data-map")
//     public Object[][] cartAddSmartphoneDataMap() {
//         Object[][] all = ExcelUtils.readSheetAsMap(dataFile(), "Cart", true);
//         return filter(all, m ->
//             "Phone".equalsIgnoreCase(m.getOrDefault("SearchKey","")) &&
//             "Smartphone".equalsIgnoreCase(m.getOrDefault("ProductName",""))
//         );
//     }

//     /** C003: Add Phone Cover. */
//     @DataProvider(name = "cart-add-phonecover-data-map")
//     public Object[][] cartAddPhoneCoverDataMap() {
//         Object[][] all = ExcelUtils.readSheetAsMap(dataFile(), "Cart", true);
//         return filter(all, m ->
//             ("Phone".equalsIgnoreCase(m.getOrDefault("SearchKey","")) ||
//              "Phone Cover".equalsIgnoreCase(m.getOrDefault("SearchKey",""))) &&
//             "Phone Cover".equalsIgnoreCase(m.getOrDefault("ProductName",""))
//         );
//     }

//     /** C004: Out-of-stock negative (ExpectedAdd=N). */
//     @DataProvider(name = "cart-outofstock-data-map")
//     public Object[][] cartOutOfStockDataMap() {
//         Object[][] all = ExcelUtils.readSheetAsMap(dataFile(), "Cart", true);
//         return filter(all, m -> "N".equalsIgnoreCase(m.getOrDefault("ExpectedAdd","")));
//     }

//     /** Intentional failing slice — same as out-of-stock (test asserts opposite). */
//     @DataProvider(name = "cart-failing-data-map")
//     public Object[][] cartFailingDataMap() {
//         return cartOutOfStockDataMap();
//     }

//     // ======================
//     // PDP (sheet: PDP)
//     // ======================

//     /** All Execute=Y rows; assertions drive expectations via ExpectedHasAddButton/ExpectedAdd. */
//     @DataProvider(name = "pdp-data-map")
//     public Object[][] pdpDataMap() {
//         return ExcelUtils.readSheetAsMap(dataFile(), "PDP", true);
//     }
// }



// // /*
// //     JSON data providers
// // */

// // package com.shwetha.framework.utils;

// // import org.testng.annotations.DataProvider;

// // import java.util.ArrayList;
// // import java.util.List;
// // import java.util.Map;
// // import java.util.function.Predicate;

// // public class DataProvidersMap {

// //     /** Optionally allow overriding the folder via config.properties: testDataDir=testdata */
// //     private static String dataDir() {
// //         String d = ConfigReader.get("testDataDir", "testdata").trim();
// //         return d.endsWith("/") ? d.substring(0, d.length()-1) : d;
// //     }

// //     /** Build classpath resource path like "testdata/login.json". */
// //     private static String res(String simpleName) {
// //         return dataDir() + "/" + simpleName + ".json";
// //     }

// //     /** Keep only rows whose map satisfies the predicate. */
// //     private static Object[][] filter(Object[][] all, Predicate<Map<String,String>> keep) {
// //         List<Object[]> out = new ArrayList<>();
// //         for (Object[] row : all) {
// //             @SuppressWarnings("unchecked")
// //             Map<String,String> m = (Map<String,String>) row[0];
// //             if (keep.test(m)) out.add(new Object[]{ m });
// //         }
// //         return out.toArray(new Object[0][]);
// //     }

// //     // ======================
// //     // LOGIN (sheet: Login) -> login.json
// //     // ======================

// //     /** All Execute=Y rows — use only for scenario tests that branch by ExpectedResult. */
// //     @DataProvider(name = "login-data-map")
// //     public Object[][] loginDataMap() {
// //         var rows = JsonData.load(res("login"));
// //         return JsonData.asDataProviderArray(rows);
// //     }

// //     /** Only SUCCESS rows (Execute=Y & ExpectedResult=SUCCESS). */
// //     @DataProvider(name = "login-valid-data-map")
// //     public Object[][] loginValidDataMap() {
// //         var rows = JsonData.load(res("login"));
// //         rows.removeIf(m -> !"SUCCESS".equalsIgnoreCase(m.getOrDefault("ExpectedResult","")));
// //         return JsonData.asDataProviderArray(rows);
// //     }

// //     /** Only ERROR rows (Execute=Y & ExpectedResult=ERROR). */
// //     @DataProvider(name = "login-invalid-data-map")
// //     public Object[][] loginInvalidDataMap() {
// //         var rows = JsonData.load(res("login"));
// //         rows.removeIf(m -> !"ERROR".equalsIgnoreCase(m.getOrDefault("ExpectedResult","")));
// //         return JsonData.asDataProviderArray(rows);
// //     }

// //     // ======================
// //     // SEARCH (sheet: Search) -> search.json
// //     // ======================

// //     /** Only ExpectedFound=Y rows. */
// //     @DataProvider(name = "search-found-data-map")
// //     public Object[][] searchFoundDataMap() {
// //         var rows = JsonData.load(res("search"));
// //         rows.removeIf(m -> !"Y".equalsIgnoreCase(m.getOrDefault("ExpectedFound","")));
// //         return JsonData.asDataProviderArray(rows);
// //     }

// //     /** Only ExpectedFound=N rows. */
// //     @DataProvider(name = "search-notfound-data-map")
// //     public Object[][] searchNotFoundDataMap() {
// //         var rows = JsonData.load(res("search"));
// //         rows.removeIf(m -> !"N".equalsIgnoreCase(m.getOrDefault("ExpectedFound","")));
// //         return JsonData.asDataProviderArray(rows);
// //     }

// //     // ============================
// //     // SEARCH FILTER (Search_Filter) -> search_filter.json
// //     // ============================

// //     @DataProvider(name = "search-filter-data-map")
// //     public Object[][] searchFilterDataMap() {
// //         var rows = JsonData.load(res("search_filter"));
// //         return JsonData.asDataProviderArray(rows);
// //     }

// //     // ======================
// //     // CART (sheet: Cart) -> cart.json
// //     // ======================

// //     /** C001: Laptop add+remove (14.1-inch Laptop). */
// //     @DataProvider(name = "cart-addremove-data-map")
// //     public Object[][] cartAddRemoveDataMap() {
// //         var rows = JsonData.load(res("cart"));
// //         var all = JsonData.asDataProviderArray(rows);
// //         return filter(all, m ->
// //             "Laptop".equalsIgnoreCase(m.getOrDefault("SearchKey","")) &&
// //             "14.1-inch Laptop".equalsIgnoreCase(m.getOrDefault("ProductName",""))
// //         );
// //     }

// //     /** C002: Add Smartphone (SearchKey=Phone, ProductName=Smartphone). */
// //     @DataProvider(name = "cart-add-smartphone-data-map")
// //     public Object[][] cartAddSmartphoneDataMap() {
// //         var rows = JsonData.load(res("cart"));
// //         var all = JsonData.asDataProviderArray(rows);
// //         return filter(all, m ->
// //             "Phone".equalsIgnoreCase(m.getOrDefault("SearchKey","")) &&
// //             "Smartphone".equalsIgnoreCase(m.getOrDefault("ProductName",""))
// //         );
// //     }

// //     /** C003: Add Phone Cover. */
// //     @DataProvider(name = "cart-add-phonecover-data-map")
// //     public Object[][] cartAddPhoneCoverDataMap() {
// //         var rows = JsonData.load(res("cart"));
// //         var all = JsonData.asDataProviderArray(rows);
// //         return filter(all, m ->
// //             ("Phone".equalsIgnoreCase(m.getOrDefault("SearchKey","")) ||
// //              "Phone Cover".equalsIgnoreCase(m.getOrDefault("SearchKey",""))) &&
// //             "Phone Cover".equalsIgnoreCase(m.getOrDefault("ProductName",""))
// //         );
// //     }

// //     /** C004: Out-of-stock negative (ExpectedAdd=N). */
// //     @DataProvider(name = "cart-outofstock-data-map")
// //     public Object[][] cartOutOfStockDataMap() {
// //         var rows = JsonData.load(res("cart"));
// //         var all = JsonData.asDataProviderArray(rows);
// //         return filter(all, m -> "N".equalsIgnoreCase(m.getOrDefault("ExpectedAdd","")));
// //     }

// //     /** Intentional failing slice — same as out-of-stock (test asserts opposite). */
// //     @DataProvider(name = "cart-failing-data-map")
// //     public Object[][] cartFailingDataMap() {
// //         return cartOutOfStockDataMap();
// //     }

// //     // ======================
// //     // PDP (sheet: PDP) -> pdp.json
// //     // ======================

// //     /** All Execute=Y rows; assertions via ExpectedHasAddButton/ExpectedAdd. */
// //     @DataProvider(name = "pdp-data-map")
// //     public Object[][] pdpDataMap() {
// //         var rows = JsonData.load(res("pdp"));
// //         return JsonData.asDataProviderArray(rows);
// //     }
// // }



package com.shwetha.framework.utils;

import org.testng.annotations.DataProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static com.shwetha.framework.utils.DataRepo.load;
import static com.shwetha.framework.utils.DataRepo.toDp;

public class DataProvidersMap {

    /** Keep only rows whose map satisfies the predicate. */
    private static Object[][] filter(Object[][] all, Predicate<Map<String,String>> keep) {
        List<Object[]> out = new ArrayList<>();
        for (Object[] row : all) {
            @SuppressWarnings("unchecked")
            Map<String,String> m = (Map<String,String>) row[0];
            if (keep.test(m)) out.add(new Object[]{ m });
        }
        return out.toArray(new Object[0][]);
    }

    // ======================
    // LOGIN (sheet/file: Login / login.json)
    // ======================

    @DataProvider(name = "login-data-map")
    public Object[][] loginDataMap() {
        return toDp(load("login"));
    }

    @DataProvider(name = "login-valid-data-map")
    public Object[][] loginValidDataMap() {
        var rows = new ArrayList<>(load("login"));
        rows.removeIf(m -> !"SUCCESS".equalsIgnoreCase(m.getOrDefault("ExpectedResult","")));
        return toDp(rows);
    }

    @DataProvider(name = "login-invalid-data-map")
    public Object[][] loginInvalidDataMap() {
        var rows = new ArrayList<>(load("login"));
        rows.removeIf(m -> !"ERROR".equalsIgnoreCase(m.getOrDefault("ExpectedResult","")));
        return toDp(rows);
    }

    // ======================
    // SEARCH (sheet/file: Search / search.json)
    // ======================

    @DataProvider(name = "search-found-data-map")
    public Object[][] searchFoundDataMap() {
        var rows = new ArrayList<>(load("search"));
        rows.removeIf(m -> !"Y".equalsIgnoreCase(m.getOrDefault("ExpectedFound","")));
        return toDp(rows);
    }

    @DataProvider(name = "search-notfound-data-map")
    public Object[][] searchNotFoundDataMap() {
        var rows = new ArrayList<>(load("search"));
        rows.removeIf(m -> !"N".equalsIgnoreCase(m.getOrDefault("ExpectedFound","")));
        return toDp(rows);
    }

    // ============================
    // SEARCH FILTER (Search_Filter / search_filter.json)
    // ============================

    @DataProvider(name = "search-filter-data-map")
    public Object[][] searchFilterDataMap() {
        return toDp(load("search_filter"));
    }

    // ======================
    // CART (sheet/file: Cart / cart.json)
    // ======================

    @DataProvider(name = "cart-addremove-data-map")
    public Object[][] cartAddRemoveDataMap() {
        var all = toDp(load("cart"));
        return filter(all, m ->
            "Laptop".equalsIgnoreCase(m.getOrDefault("SearchKey","")) &&
            "14.1-inch Laptop".equalsIgnoreCase(m.getOrDefault("ProductName",""))
        );
    }

    @DataProvider(name = "cart-add-smartphone-data-map")
    public Object[][] cartAddSmartphoneDataMap() {
        var all = toDp(load("cart"));
        return filter(all, m ->
            "Phone".equalsIgnoreCase(m.getOrDefault("SearchKey","")) &&
            "Smartphone".equalsIgnoreCase(m.getOrDefault("ProductName",""))
        );
    }

    @DataProvider(name = "cart-add-phonecover-data-map")
    public Object[][] cartAddPhoneCoverDataMap() {
        var all = toDp(load("cart"));
        return filter(all, m ->
            ("Phone".equalsIgnoreCase(m.getOrDefault("SearchKey","")) ||
             "Phone Cover".equalsIgnoreCase(m.getOrDefault("SearchKey",""))) &&
            "Phone Cover".equalsIgnoreCase(m.getOrDefault("ProductName",""))
        );
    }

    @DataProvider(name = "cart-outofstock-data-map")
    public Object[][] cartOutOfStockDataMap() {
        var all = toDp(load("cart"));
        return filter(all, m -> "N".equalsIgnoreCase(m.getOrDefault("ExpectedAdd","")));
    }

    @DataProvider(name = "cart-failing-data-map")
    public Object[][] cartFailingDataMap() {
        return cartOutOfStockDataMap();
    }

    // ======================
    // PDP (sheet/file: PDP / pdp.json)
    // ======================

    @DataProvider(name = "pdp-data-map")
    public Object[][] pdpDataMap() {
        return toDp(load("pdp"));
    }
}