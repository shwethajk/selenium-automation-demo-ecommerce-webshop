// // package com.shwetha.framework.utils;

// // import java.util.*;
// // import java.util.stream.Collectors;

// // /**
// //  * Central place to fetch rows as List<Map<String,String>> from either Excel or JSON,
// //  * based on config: testDataSource=excel|json (or legacy testDataFile aliases).
// //  */
// // public final class DataRepo {
// //     public enum Backend { EXCEL, JSON }
// //     private DataRepo(){}
// //     public static Backend backend() {
// //         // Primary switch
// //         // String src = val("testDataSource", "").toLowerCase(Locale.ROOT).trim();
// //         // if (src.equals("excel")) return Backend.EXCEL;
// //         // if (src.equals("json"))  return Backend.JSON;


// //         // String src = val("testDataFile", "").toLowerCase(Locale.ROOT).trim();
// //         // if (src.equals("excelData")) return Backend.EXCEL;
// //         // if (src.equals("jsonData")) return Backend.EXCEL;

// //         // Legacy compatibility: testDataFile can be 'excelData' or 'jsonData'
// //         String tdf = val("testDataFile", "").trim();
// //         if ("jsonData".equalsIgnoreCase(tdf))  return Backend.JSON;
// //         if ("excelData".equalsIgnoreCase(tdf)) return Backend.EXCEL;

// //         // Heuristic: if an explicit .xlsx path was given, choose Excel; else JSON
// //         if (tdf.toLowerCase(Locale.ROOT).endsWith(".xlsx")) return Backend.EXCEL;
// //         return Backend.EXCEL; // default to Excel for backward-compat
// //     }

// //     public static List<Map<String,String>> load(String dataset) {
// //         if (backend() == Backend.EXCEL) {
// //             String excelPath = resolveExcelPath();
// //             return fromExcel(excelPath, sheetName(dataset));
// //         } else {
// //             String jsonDir = resolveJsonDir();
// //             return JsonData.load(jsonDir, fileName(dataset));
// //         }
// //     }

// //     // --------------- helpers ---------------

// //     private static String val(String key, String def) {
// //         String v = ConfigReader.get(key, def);
// //         return (v == null) ? def : v.trim();
// //     }

// //     private static String resolveExcelPath() {
// //         String path = val("excelData", "");
// //         String tdf  = val("testDataFile", "");
// //         if (!tdf.isBlank() && !"excelData".equalsIgnoreCase(tdf) && !"jsonData".equalsIgnoreCase(tdf)) {
// //             // explicit override path
// //             return tdf;
// //         }
// //         if (path.isBlank()) {
// //             // final fallback: old default
// //             return "src/test/resources/testdata/EXCEL/TestData.xlsx";
// //         }
// //         return path;
// //     }

// //     private static String resolveJsonDir() {
// //         String dir = val("jsonData", "testdata/JSON/");
// //         // normalize here too (JsonData also normalizes)
// //         dir = dir.replace('\\', '/');
// //         if (!dir.endsWith("/")) dir += "/";
// //         return dir;
// //     }

// //     /** Map logical dataset name -> Excel sheet name */
// //     private static String sheetName(String dataset) {
// //         return switch (dataset.toLowerCase(Locale.ROOT)) {
// //             case "login"         -> "Login";
// //             case "search"        -> "Search";
// //             case "cart"          -> "Cart";
// //             case "search_filter" -> "Search_Filter";
// //             case "pdp"           -> "PDP";
// //             default -> dataset;
// //         };
// //     }

// //     /** Map logical dataset name -> JSON file basename */
// //     private static String fileName(String dataset) {
// //         return switch (dataset.toLowerCase(Locale.ROOT)) {
// //             case "search_filter" -> "search_filter";
// //             default -> dataset.toLowerCase(Locale.ROOT);
// //         };
// //     }

// //     /** Excel -> List<Map<String,String>> via your existing ExcelUtils */
// //     private static List<Map<String,String>> fromExcel(String excelPath, String sheet) {
// //         Object[][] arr = ExcelUtils.readSheetAsMap(excelPath, sheet, true);
// //         List<Map<String,String>> out = new ArrayList<>(arr.length);
// //         for (Object[] row : arr) {
// //             @SuppressWarnings("unchecked")
// //             Map<String,String> m = (Map<String,String>) row[0];
// //             out.add(m);
// //         }
// //         return out;
// //     }

// //     /** Convert to Object[][] for DataProvider, with default Execute=Y filtering. */
// //     public static Object[][] toDp(List<Map<String,String>> rows) {
// //         List<Object[]> filtered = rows.stream()
// //                 .filter(m -> "Y".equalsIgnoreCase(m.getOrDefault("Execute","Y")))
// //                 .map(m -> new Object[]{ m })
// //                 .collect(Collectors.toList());
// //         return filtered.toArray(new Object[0][]);
// //     }
// // }




// package com.shwetha.framework.utils;

// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;
// import org.testng.Reporter;

// import java.util.*;
// import java.util.stream.Collectors;

// /**
//  * Central place to fetch rows as List<Map<String,String>> from either Excel or JSON,
//  * based on config: testDataSource=excel|json (or legacy testDataFile aliases).
//  */
// public final class DataRepo {

//     public enum Backend { EXCEL, JSON }

//     private static final Logger LOG = LogManager.getLogger(DataRepo.class);
//     private static volatile Backend CHOSEN = null;
//     private static volatile boolean ANNOUNCED = false;

//     private DataRepo(){}

//     /** Decide, cache and return the backend. */
//     public static Backend backend() {
//         Backend b = CHOSEN;
//         if (b != null) return b;

//         synchronized (DataRepo.class) {
//             // if (CHOSEN != null) return CHOSEN;
//             // Legacy compatibility: testDataFile can be 'excelData' or 'jsonData'
//             String tdf = val("testDataFile", "").trim();
//             if ("jsonData".equalsIgnoreCase(tdf))      CHOSEN = Backend.JSON;
//             else if ("excelData".equalsIgnoreCase(tdf)) CHOSEN = Backend.EXCEL;
//             // else if (tdf.toLowerCase(Locale.ROOT).endsWith(".xlsx")) CHOSEN = Backend.EXCEL;
//             // else CHOSEN = Backend.EXCEL; // default (backward-compat)
//             return CHOSEN;
//         }
//     }

//     /** Publish system properties + print once to console/log (safe to call multiple times). */
//     public static void publishChoice() {
//         // compute once (cached by backend())
//         Backend b = backend();

//         // publish system properties for other components (Extent, listeners)
//         if (b == Backend.EXCEL) {
//             String excelPath = resolveExcelPath();
//             System.setProperty("test.data.backend", "EXCEL");
//             System.setProperty("test.data.excel.path", excelPath);
//             System.clearProperty("test.data.json.dir");
//         } else {
//             String jsonDir = resolveJsonDir();
//             System.setProperty("test.data.backend", "JSON");
//             System.setProperty("test.data.json.dir", jsonDir);
//             System.clearProperty("test.data.excel.path");
//         }

//         // announce once
//         if (!ANNOUNCED) {
//             synchronized (DataRepo.class) {
//                 if (!ANNOUNCED) {
//                     String msg = (b == Backend.EXCEL)
//                             ? "🗂 Data Source: EXCEL | excelData=" + System.getProperty("test.data.excel.path")
//                             : "🗂 Data Source: JSON  | jsonData="  + System.getProperty("test.data.json.dir");
//                     try { Reporter.log(msg, true); } catch (Throwable ignore) {}
//                     LOG.info(msg);
//                     ANNOUNCED = true;
//                 }
//             }
//         }
//     }

//     /** Load the named dataset as List<Map<String,String>> from the selected backend. */
//     public static List<Map<String,String>> load(String dataset) {
//         if (backend() == Backend.EXCEL) {
//             String excelPath = resolveExcelPath();
//             return fromExcel(excelPath, sheetName(dataset));
//         } else {
//             String jsonDir = resolveJsonDir();
//             return JsonData.load(jsonDir, fileName(dataset));
//         }
//     }

//     // --------------- helpers ---------------

//     private static String val(String key, String def) {
//         String v = ConfigReader.get(key, def);
//         return (v == null) ? def : v.trim();
//     }

//     private static String resolveExcelPath() {
//         String path = val("excelData", "");
//         String tdf  = val("testDataFile", "");
//         if (!tdf.isBlank() && !"excelData".equalsIgnoreCase(tdf) && !"jsonData".equalsIgnoreCase(tdf)) {
//             // explicit override path
//             return tdf.trim();
//         }
//         if (path.isBlank()) {
//             // final fallback: old default
//             System.out.println("path blanl=k, hence default excel");
//             return "src/test/resources/testdata/EXCEL/TestData.xlsx";
//         }
//         return path;
//     }

//     private static String resolveJsonDir() {
//         String dir = val("jsonData", "testdata/JSON/");
//         dir = dir.replace('\\', '/');
//         if (!dir.endsWith("/")) dir += "/";
//         return dir;
//     }

//     /** Map logical dataset name -> Excel sheet name */
//     private static String sheetName(String dataset) {
//         return switch (dataset.toLowerCase(Locale.ROOT)) {
//             case "login"         -> "Login";
//             case "search"        -> "Search";
//             case "cart"          -> "Cart";
//             case "search_filter" -> "Search_Filter";
//             case "pdp"           -> "PDP";
//             default -> dataset;
//         };
//     }

//     /** Map logical dataset name -> JSON file basename */
//     private static String fileName(String dataset) {
//         return switch (dataset.toLowerCase(Locale.ROOT)) {
//             case "search_filter" -> "search_filter";
//             default -> dataset.toLowerCase(Locale.ROOT);
//         };
//     }

//     /** Excel -> List<Map<String,String>> via your existing ExcelUtils */
//     private static List<Map<String,String>> fromExcel(String excelPath, String sheet) {
//         Object[][] arr = ExcelUtils.readSheetAsMap(excelPath, sheet, true);
//         List<Map<String,String>> out = new ArrayList<>(arr.length);
//         for (Object[] row : arr) {
//             @SuppressWarnings("unchecked")
//             Map<String,String> m = (Map<String,String>) row[0];
//             out.add(m);
//         }
//         return out;
//     }

//     /** Convert to Object[][] for DataProvider, with default Execute=Y filtering. */
//     public static Object[][] toDp(List<Map<String,String>> rows) {
//         List<Object[]> filtered = rows.stream()
//                 .filter(m -> "Y".equalsIgnoreCase(m.getOrDefault("Execute","Y")))
//                 .map(m -> new Object[]{ m })
//                 .collect(Collectors.toList());
//         return filtered.toArray(new Object[0][]);
//     }
// }




package com.shwetha.framework.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Reporter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Central place to fetch rows as List<Map<String,String>> from either Excel or JSON.
 * Uses ONLY config key: testDataFile
 *
 * Accepted values:
 * - "excelData"     -> uses excelData path
 * - "jsonData"      -> uses jsonData directory
 * - explicit .xlsx  -> Excel
 * - explicit folder -> JSON
 */
public final class DataRepo {
    public enum Backend { EXCEL, JSON }
    private static final Logger LOG = LogManager.getLogger(DataRepo.class);
    private static volatile Backend CHOSEN = null;
    private static volatile boolean ANNOUNCED = false;
    private DataRepo(){}
    /** Decide backend strictly based on testDataFile. */
    public static Backend backend() {
        if (CHOSEN != null) return CHOSEN;
        synchronized (DataRepo.class) {
            if (CHOSEN != null) return CHOSEN;
            String tdf = val("testDataFile", "").trim().toLowerCase(Locale.ROOT);
            String excelPath = val("excelData", "").trim();
            String jsonDir   = val("jsonData", "").trim();
            // Case 1: keyword excelData
            if (tdf.equals("exceldata"))
                return CHOSEN = Backend.EXCEL;
            // Case 2: keyword jsonData
            if (tdf.equals("jsondata"))
                return CHOSEN = Backend.JSON;
            // Case 3: explicit Excel file
            if (tdf.endsWith(".xlsx"))
                return CHOSEN = Backend.EXCEL;
            // Case 4: explicit JSON directory
            if (tdf.endsWith("/") || tdf.endsWith("\\")) {
                // If looks like folder -> JSON
                return CHOSEN = Backend.JSON;
            }
            // Final fallback: assume Excel (backward-compat)
            return CHOSEN = Backend.EXCEL;
        }
    }

    /** Publishes system props + logs choice once. */
    public static void publishChoice() {
        Backend b = backend();
        if (b == Backend.EXCEL) {
            String excelPath = resolveExcelPath();
            System.setProperty("test.data.backend", "EXCEL");
            System.setProperty("test.data.excel.path", excelPath);
            System.clearProperty("test.data.json.dir");
        } else {
            String jsonDir = resolveJsonDir();
            System.setProperty("test.data.backend", "JSON");
            System.setProperty("test.data.json.dir", jsonDir);
            System.clearProperty("test.data.excel.path");
        }
        if (!ANNOUNCED) {
            synchronized (DataRepo.class) {
                if (!ANNOUNCED) {
                    String msg = (b == Backend.EXCEL)
                            ? "🗂  Data Source: EXCEL | excelData=" + System.getProperty("test.data.excel.path")
                            : "🗂  Data Source: JSON  | jsonData="  + System.getProperty("test.data.json.dir");
                    Reporter.log(msg, true);
                    LOG.info(msg);
                    ANNOUNCED = true;
                }
            }
        }
    }

    /** Load dataset rows. */
    public static List<Map<String,String>> load(String dataset) {
        Backend b = backend();

        if (b == Backend.EXCEL) {
            String excelPath = resolveExcelPath();
            LOG.info("DataRepo.load '{}' -> EXCEL sheet='{}' ({})",
                    dataset, sheetName(dataset), excelPath);
            return fromExcel(excelPath, sheetName(dataset));
        }

        String jsonDir = resolveJsonDir();
        LOG.info("DataRepo.load '{}' -> JSON file='{}{}.json'",
                dataset, jsonDir, fileName(dataset));
        return JsonData.load(jsonDir, fileName(dataset));
    }

    // ---------------- helpers ----------------

    private static String val(String key, String def) {
        String v = ConfigReader.get(key, def);
        return (v == null) ? def : v.trim();
    }

    private static String resolveExcelPath() {
        String tdf  = val("testDataFile", "");
        String excel = val("excelData", "");
        if (tdf.equalsIgnoreCase("excelData"))
            return excel;
        if (tdf.endsWith(".xlsx"))
            return tdf;
        return excel; // fallback
    }

    private static String resolveJsonDir() {
        String tdf = val("testDataFile", "");
        String dir = val("jsonData", "testdata/JSON/");
        dir = dir.replace('\\', '/');
        if (!dir.endsWith("/"))
            dir += "/";
        // explicit override
        if (tdf.equalsIgnoreCase("jsonData"))
            return dir;
        if (tdf.endsWith("/") || tdf.endsWith("\\")) {
            String cleaned = tdf.replace('\\', '/');
            return cleaned.endsWith("/") ? cleaned : cleaned + "/";
        }
        return dir;
    }

    private static String sheetName(String dataset) {
        return switch (dataset.toLowerCase(Locale.ROOT)) {
            case "login"         -> "Login";
            case "search"        -> "Search";
            case "cart"          -> "Cart";
            case "search_filter" -> "Search_Filter";
            case "pdp"           -> "PDP";
            default -> dataset;
        };
    }

    private static String fileName(String dataset) {
        return switch (dataset.toLowerCase(Locale.ROOT)) {
            case "search_filter" -> "search_filter";
            default -> dataset.toLowerCase(Locale.ROOT);
        };
    }

    private static List<Map<String,String>> fromExcel(String excelPath, String sheet) {
        Object[][] arr = ExcelUtils.readSheetAsMap(excelPath, sheet, true);
        List<Map<String,String>> out = new ArrayList<>(arr.length);
        for (Object[] row : arr) {
            @SuppressWarnings("unchecked")
            Map<String,String> m = (Map<String,String>) row[0];
            out.add(m);
        }
        return out;
    }

    public static Object[][] toDp(List<Map<String,String>> rows) {
        List<Object[]> filtered = rows.stream()
                .filter(m -> "Y".equalsIgnoreCase(m.getOrDefault("Execute","Y")))
                .map(m -> new Object[]{ m })
                .collect(Collectors.toList());
        return filtered.toArray(new Object[0][]);
    }
}