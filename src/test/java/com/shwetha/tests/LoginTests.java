// // package com.shwetha.tests;

// // import com.shwetha.framework.base.BaseTests;
// // import com.shwetha.framework.utils.ConfigReader;
// // import com.shwetha.framework.utils.DataProviders;
// // import com.shwetha.pageobjects.*;
// // import org.testng.Assert;
// // import org.testng.annotations.*;

// // public class LoginTests extends BaseTests {
// //     private HomePage home;

// //     @BeforeMethod(alwaysRun = true)
// //     public void open() {
// //         // Navigate to baseUrl and initialize HomePage
// //         // Ensure ConfigReader is already loaded in your BaseTests @BeforeSuite / @BeforeClass
// //         home = new HomePage(getDriver())
// //                     .goTo(ConfigReader.get("baseUrl"))
// //                     .logoutIfLoggedIn();
// //         // System.out.println("\nLoginTests: HomePage initialized: " + (home != null)); // Optional: sanity log
// //     }

// //     @Test(description = "Open website and return the title", 
// //         priority=1,
// //         groups = {"title", "smoke"})
// //     public void getHomePageTitle() {
// //         String title = home.title();
// //         System.out.println("Page Title: " + title);
// //         Assert.assertTrue(title != null && !title.isBlank(), "Title should not be blank");
// //     }

// //     // @Test(description="Verify successful user login with VALID credentials",
// //     //     groups = {"login","smoke","master"})
// //     // public void loginValid() {
// //     //     LoginPage login = home.clickLogin();
// //     //     home = login.loginValid("SJKtestuser123@gmail.com", "!QAZ0okm");
// //     //     Assert.assertTrue(home.isLoggedIn(), "Login should succeed");
// //     // }

// //     @Test(description="Verify successful user login with VALID credentials",
// //         groups = {"login","smoke","master"},
// //         dataProvider = "login-valid-data", dataProviderClass = DataProviders.class)
// //     public void loginValid(String username, String password) {
// //         LoginPage login = home.clickLogin();
// //         home = login.loginValid(username, password);
// //         Assert.assertTrue(home.isLoggedIn(), "Login should succeed");
// //     }


// //     // @Test(description="Verify unsuccessful user login with INVALID credentials",
// //     //     groups = {"login","sanity","regression","master"})
// //     // public void loginInvalid() {
// //     //     LoginPage login = home.clickLogin();
// //     //     login.loginInvalid("WrongSJKtestuser123@gmail.com", "!QAZ0okm");
// //     //     Assert.assertFalse(home.isLoggedIn(), "Invalid login should not log in");
// //     //     Assert.assertTrue(login.isErrorShown(), "Error message expected");
// //     // }

// //     @Test(description="Verify unsuccessful user login with INVALID credentials",
// //         groups = {"login","sanity","regression","master"},
// //         dataProvider = "login-invalid-data", dataProviderClass = DataProviders.class)
// //     public void loginInvalid(String username, String password) {
// //         LoginPage login = home.clickLogin();
// //         login.loginInvalid(username, password);
// //         Assert.assertFalse(home.isLoggedIn(), "Invalid login should not log in");
// //         Assert.assertTrue(login.isErrorShown(), "Error message expected");
// //     }


// //     // @Test(description="Verify logout functionality with logged-in precondition",
// //     //     groups = {"login","sanity","regression","master"})
// //     // public void logoutValid() {
// //     //     LoginPage login = home.clickLogin();
// //     //     home = login.loginValid("SJKtestuser123@gmail.com", "!QAZ0okm");
// //     //     Assert.assertTrue(home.isLoggedIn(), "Login should succeed");
// //     //     home.logoutIfLoggedIn();
// //     //     Assert.assertFalse(home.isLoggedIn(), "Logout should succeed");
// //     // }

// //     // Optional: make logoutValid data-driven using valid creds
// //     @Test(description="Verify logout functionality with logged-in precondition",
// //         groups = {"login","sanity","regression","master"},
// //         dataProvider = "login-valid-data", dataProviderClass = DataProviders.class)
// //     public void logoutValid(String username, String password) {
// //         LoginPage login = home.clickLogin();
// //         home = login.loginValid(username, password);
// //         Assert.assertTrue(home.isLoggedIn(), "Login should succeed");
// //         home.logoutIfLoggedIn();
// //         Assert.assertFalse(home.isLoggedIn(), "Logout should succeed");
// //     }

// //     @Test(description="Verify logout functionality with already logged-out precondition",
// //         groups = {"login","regression","negative", "master"})
// //     public void logoutInvalid() {
// //         home.clickLogin();
// //         home.logoutIfLoggedIn();
// //         Assert.assertFalse(home.isLoggedIn(), "Logout should not succeed");
// //     }


// //     @Test(description="Login scenarios (Map-based)", dataProvider="login-data-map",
// //       dataProviderClass = com.shwetha.framework.utils.DataProvidersMap.class,
// //       groups={"mapTests","login","regression","master"})
// //     public void loginScenariosMap(java.util.Map<String,String> data) {
// //         String expected = data.getOrDefault("ExpectedResult","").toUpperCase();
// //         LoginPage login = home.clickLogin();
// //         if ("SUCCESS".equals(expected)) {
// //             home = login.loginValid(data.get("Username"), data.get("Password"));
// //             Assert.assertTrue(home.isLoggedIn());
// //         } else if ("ERROR".equals(expected)) {
// //             login.loginInvalid(data.get("Username"), data.get("Password"));
// //             Assert.assertFalse(home.isLoggedIn());
// //             Assert.assertTrue(login.isErrorShown());
// //         } else {
// //             throw new IllegalArgumentException("Unknown ExpectedResult: " + expected);
// //         }
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

// public class LoginTests extends BaseTests {
//     private HomePage home;

//     @BeforeMethod(alwaysRun = true)
//     public void open() {
//         home = new HomePage(getDriver())
//                     .goTo(ConfigReader.get("baseUrl"))
//                     .logoutIfLoggedIn();
//     }

//     @Test(description = "Open website and return the title",
//         priority=1,
//         groups = {"title", "smoke"})
//     public void getHomePageTitle() {
//         String title = home.title();
//         System.out.println("Page Title: " + title);
//         Assert.assertTrue(title != null && !title.isBlank(), "Title should not be blank");
//     }

//     // @Test(description="Verify successful user login with VALID credentials",
//     //     groups = {"login","smoke","master"},
//     //     dataProvider = "login-valid-data-map", dataProviderClass = DataProvidersMap.class)
//     // public void loginValid(Map<String,String> data) {
//     //     if (!"SUCCESS".equalsIgnoreCase(data.getOrDefault("ExpectedResult",""))) return; // skip rows not meant for valid
//     //     LoginPage login = home.clickLogin();
//     //     home = login.loginValid(data.get("Username"), data.get("Password"));
//     //     Assert.assertTrue(home.isLoggedIn(), "Login should succeed");
//     // }



//     @Test(description="Verify successful user login with VALID credentials",
//         groups = {"login","smoke","master"},
//         dataProvider = "login-valid-data-map", dataProviderClass = DataProvidersMap.class)
//     public void loginValid(Map<String,String> data) {
//         LoginPage login = home.clickLogin();
//         home = login.loginValid(data.get("Username"), data.get("Password"));
//         Assert.assertTrue(home.isLoggedIn(), "Login should succeed");
//     }

//     @Test(description="Verify unsuccessful user login with INVALID credentials",
//         groups = {"login","sanity","regression","master"},
//         dataProvider = "login-invalid-data-map", dataProviderClass = DataProvidersMap.class)
//     public void loginInvalid(Map<String,String> data) {
//         LoginPage login = home.clickLogin();
//         login.loginInvalid(data.get("Username"), data.get("Password"));
//         Assert.assertFalse(home.isLoggedIn(), "Invalid login should not log in");
//         Assert.assertTrue(login.isErrorShown(), "Error message expected");
//     }


//     // @Test(description="Verify unsuccessful user login with INVALID credentials",
//     //     groups = {"login","sanity","regression","master"},
//     //     dataProvider = "login-invalid-data-map", dataProviderClass = DataProvidersMap.class)
//     // public void loginInvalid(Map<String,String> data) {
//     //     if (!"ERROR".equalsIgnoreCase(data.getOrDefault("ExpectedResult",""))) return; // skip rows not meant for invalid
//     //     LoginPage login = home.clickLogin();
//     //     login.loginInvalid(data.get("Username"), data.get("Password"));
//     //     Assert.assertFalse(home.isLoggedIn(), "Invalid login should not log in");
//     //     Assert.assertTrue(login.isErrorShown(), "Error message expected");
//     // }

//     @Test(description="Verify logout functionality with logged-in precondition",
//         groups = {"login","sanity","regression","master"},
//         dataProvider = "login-valid-data-map", dataProviderClass = DataProvidersMap.class)
//     public void logoutValid(Map<String,String> data) {
//         if (!"SUCCESS".equalsIgnoreCase(data.getOrDefault("ExpectedResult",""))) return;
//         LoginPage login = home.clickLogin();
//         home = login.loginValid(data.get("Username"), data.get("Password"));
//         Assert.assertTrue(home.isLoggedIn(), "Login should succeed");
//         home.logoutIfLoggedIn();
//         Assert.assertFalse(home.isLoggedIn(), "Logout should succeed");
//     }

//     @Test(description="Verify logout functionality with already logged-out precondition",
//         groups = {"login","regression","negative", "master"})
//     public void logoutInvalid() {
//         home.clickLogin();
//         home.logoutIfLoggedIn();
//         Assert.assertFalse(home.isLoggedIn(), "Logout should not succeed");
//     }

//     // @Test(description="Login scenarios (Map-based, header-driven)", 
//     //   dataProvider="login-data-map", dataProviderClass = DataProvidersMap.class,
//     //   groups={"mapTests","login","regression","master"})
//     // public void loginScenariosMap(Map<String,String> data) {
//     //     String expected = data.getOrDefault("ExpectedResult","").toUpperCase();
//     //     LoginPage login = home.clickLogin();
//     //     if ("SUCCESS".equals(expected)) {
//     //         home = login.loginValid(data.get("Username"), data.get("Password"));
//     //         Assert.assertTrue(home.isLoggedIn());
//     //     } else if ("ERROR".equals(expected)) {
//     //         login.loginInvalid(data.get("Username"), data.get("Password"));
//     //         Assert.assertFalse(home.isLoggedIn());
//     //         Assert.assertTrue(login.isErrorShown());
//     //     }
//     // }
// }


package com.shwetha.tests;

import com.shwetha.framework.base.BaseTests;
import com.shwetha.framework.utils.ConfigReader;
import com.shwetha.framework.utils.DataProvidersMap;
import com.shwetha.framework.utils.DataRepo;
import com.shwetha.pageobjects.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Map;

public class LoginTests extends BaseTests {
    private HomePage home;

    @BeforeMethod(alwaysRun = true)
    public void open() {
        home = new HomePage(getDriver())
                .goTo(ConfigReader.get("baseUrl"))
                .logoutIfLoggedIn();
    }

    @Test(description = "Open website and return the title",
            priority=1,
            groups = {"title", "smoke"})
    public void getHomePageTitle() {
        String title = home.title();
        System.out.println("Page Title: " + title);
        Assert.assertTrue(title != null && !title.isBlank(), "Title should not be blank");
    }

    @Test(description="Verify successful user login with VALID credentials",
            groups = {"login","smoke","master"},
            dataProvider = "login-valid-data-map", dataProviderClass = DataProvidersMap.class)
    public void loginValidCase(Map<String,String> data) {
        LoginPage login = home.clickLogin();
        home = login.loginValid(data.get("Username"), data.get("Password"));
        Assert.assertTrue(home.isLoggedIn(), "Login should succeed");
    }

    @Test(description="Verify unsuccessful user login with INVALID credentials",
            groups = {"login","sanity","regression","master"},
            dataProvider = "login-invalid-data-map", dataProviderClass = DataProvidersMap.class)
    public void loginInvalidCase(Map<String,String> data) {
        LoginPage login = home.clickLogin();
        login.loginInvalid(data.get("Username"), data.get("Password"));
        Assert.assertFalse(home.isLoggedIn(), "Invalid login should not log in");
        Assert.assertTrue(login.isErrorShown(), "Error message expected");
    }

    @Test(description="Verify logout functionality with logged-in precondition",
            groups = {"login","sanity","regression","master"},
            dataProvider = "login-valid-data-map", dataProviderClass = DataProvidersMap.class)
    public void logoutValidCase(Map<String,String> data) {
        LoginPage login = home.clickLogin();
        home = login.loginValid(data.get("Username"), data.get("Password"));
        Assert.assertTrue(home.isLoggedIn(), "Login should succeed");
        home.logoutIfLoggedIn();
        Assert.assertFalse(home.isLoggedIn(), "Logout should succeed");
    }

    @Test(description="Verify logout functionality with already logged-out precondition",
            groups = {"login","regression","negative", "master"})
    public void logoutInvalidCase() {
        home.clickLogin();
        home.logoutIfLoggedIn();
        Assert.assertFalse(home.isLoggedIn(), "Logout should not succeed");
    }

    // @Testdisk
    // public void printJsonRows() {
    //     var rows = DataRepo.load("login");
    //     System.out.println("ROWS = " + rows.size());
    //     Assert.assertTrue(rows.size() > 0, "JSON was not loaded!");
    // }
}

