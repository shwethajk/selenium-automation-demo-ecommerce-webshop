
📑 E-commerce Test Cases

| **TC ID** | **Test Case Description**                | **Pre-Conditions**            | **Test Steps**                                                                                | **Test Data**                               | **Expected Result**                                                 | **Actual Result** | **Status (Pass/Fail)** |
| --------- | ---------------------------------------- | ----------------------------- | --------------------------------------------------------------------------------------------- | ------------------------------------------- | ------------------------------------------------------------------- | ----------------- | ---------------------- |
| TC001     | Verify user login with valid credentials | User is registered            | 1. Navigate to login page <br> 2. Enter valid username & password <br> 3. Click Login         | Username: testuser <br> Password: Test\@123 | User should be successfully logged in and redirected to dashboard   | –                 | –                      |
| TC002     | Verify login with invalid credentials    | User is registered            | 1. Navigate to login page <br> 2. Enter invalid username & password <br> 3. Click Login       | Username: wrong <br> Password: wrong123     | Error message should be displayed for invalid credentials           | –                 | –                      |
| TC003     | Verify product search functionality      | User logged in / not required | 1. Navigate to homepage <br> 2. Enter product name in search bar <br> 3. Click search         | Product: Laptop                             | Search results should display matching products                     | –                 | –                      |
| TC004     | Verify product filter functionality      | User logged in / not required | 1. Navigate to category <br> 2. Apply filter (e.g., price range) <br> 3. Check results        | Filter: Price range 20k-40k                 | Filtered products should be displayed correctly                     | –                 | –                      |
| TC005     | Verify add to cart functionality         | User logged in                | 1. Search a product <br> 2. Click on product <br> 3. Click Add to Cart                        | Product: Laptop                             | Product should be added to cart, cart icon should update            | –                 | –                      |
| TC006     | Verify remove from cart functionality    | Product added to cart         | 1. Navigate to cart <br> 2. Remove product from cart                                          | Product: Laptop                             | Product should be removed from cart, cart count should update       | –                 | –                      |
| TC007     | Verify checkout process without payment  | Item added to cart            | 1. Navigate to cart <br> 2. Click Checkout <br> 3. Enter shipping details <br> 4. Place order | Name: John Doe <br> Address: Bangalore      | Order should be placed successfully, confirmation message displayed | –                 | –                      |
| TC008     | Verify logout functionality              | User logged in                | 1. Click on Logout button                                                                     | N/A                                         | User should be logged out and redirected to homepage                | –                 | –                      |

---
---

## 🛒 Project Context

Our team is responsible for automating the testing of an **E-commerce web application** 
(e.g., [https://demowebshop.tricentis.com/] or 
[https://www.saucedemo.com/]).

The focus is on:

* **Smoke tests** (basic flows)
* **Regression tests** (functional flows like login, add to cart, checkout)

You’ll create **test cases in Excel**, then automate them in **Selenium with Java + TestNG**.

---

## 📋 Tasks 

1. **Requirement Understanding**:
   Read the application’s features: Login, Register, Product Search, Add to Cart, Checkout.

2. **Test Case Design (Manual)**:
   Create an **Excel sheet** with the following columns:

   * **TC ID**
   * **Test Case Description**
   * **Pre-Conditions**
   * **Test Steps**
   * **Test Data**
   * **Expected Result**
   * **Actual Result**
   * **Status (Pass/Fail)**

3. **Automation Scope** (first sprint):

   * Verify Login functionality
   * Verify Product Search
   * Verify Add to Cart
   * Verify Checkout (without payment gateway)

4. **Automation Execution**:
   Implement the above test cases using Selenium + Java + TestNG.

---
*****************************************************************************************************************

TC ID	
Test Case Description	
Pre-Conditions	
Test Steps	
Test Data	
Expected Result	
Actual Result	
Status (Pass/Fail)
–	–

TC001	
Verify user login with valid credentials	
User is registered	"
1. Navigate to login page 
2. Enter valid username & password 
3. Click Login
Username: SJKtestuser123@gmail.com
Password: !QAZ0okm
User should be successfully logged in and redirected to dashboard	
Passed
–	–

TC002	
Verify login with invalid credentials	
User is registered	
1. Navigate to login page 
2. Enter invalid username & password 
3. Click Login
Username: wrong 
Password: wrong123
Error message should be displayed for invalid credentials	
–	–

TC003
Verify product search functionality	
User logged in / not required	
1. Navigate to homepage 
2. Enter product name in search bar 
3. Click search
Product: Laptop	
Search results should display matching products	
–	–

TC004	
Verify product filter functionality	
User logged in / not required	
1. Navigate to category  
2. Apply filter (e.g., price range) 
3. Check results"	
Filter: Price range 20k-40k	Filtered products should be displayed correctly	
–	–

TC005	
Verify add to cart functionality	
User logged in	
1. Search a product 
2. Click on product
3. Click Add to Cart"	
Product: Laptop	Product should be added to cart, cart icon should update	
–	–

TC006	
Verify remove from cart functionality	
Product added to cart	
1. Navigate to cart 
2. Remove product from cart"	
Product: Laptop	Product should be removed from cart, cart count should update	
–	–

TC007	
Verify checkout process without payment	Item added to cart	
1. Navigate to cart 
2. Click Checkout  
3. Enter shipping details 
4. Place order"	
Name: John Doe 
Address: Bangalore	
Order should be placed successfully, confirmation message displayed	
–	–

TC008	
Verify logout functionality	
User logged in	
1. Click on Logout button	
N/A	
User should be logged out and redirected to homepage	
–	-
---




Great question, Shwetha!  
You want to know:

> **How the test cases (TC001–TC008) were separated into Smoke, Sanity, and Regression suites in your automation project.**

I’ll explain **exactly how your tests were grouped**, **why**, and **where (suite XML + Java classes)** the segregation happens.

***

# ✅ **1. Mapping Manual Test Cases → Test Suites**

Based on your TC001–TC008 table, here is the categorization:

## ⭐ **Smoke Suite (Very basic health check)**

These tests verify that the *most essential* features are working.

| Test ID   | Description    |
| --------- | -------------- |
| **TC001** | Valid login    |
| **TC003** | Product search |

➡ **Why only these?**  
Smoke suite should be *fast*, *minimal*, and catch *critical downtime*.

***

## ⭐ **Sanity Suite (Medium depth, stable flows)**

These tests verify that main application functionalities work after minor code changes.

| Test ID   | Description      |
| --------- | ---------------- |
| **TC002** | Invalid login    |
| **TC005** | Add to cart      |
| **TC006** | Remove from cart |

➡ **Why these?**  
They validate basic business logic but are not as fast as smoke tests.

***

## ⭐ **Regression Suite (Complete system check)**

Full functional flow tests + edge cases.

| Test ID   | Description                              |
| --------- | ---------------------------------------- |
| **TC003** | Product search (with multiple scenarios) |
| **TC004** | Filter functionality                     |
| **TC005** | Add to cart (more variations)            |
| **TC006** | Remove from cart                         |
| **TC007** | Checkout process                         |
| **TC008** | Logout                                   |

➡ **Why?**  
Regression covers all detailed flows and combinations.

***

# ✅ **2. How this segregation is implemented in your framework**

Your framework segregates tests in **two ways**:

***

# **A) By Java Test Classes**

📁 `src/test/java/com/shwetha/tests/`

### ✔ **SmokeTests.java**

Contains:

*   testValidLogin → TC001
*   testSearchProductFound → TC003

***

### ✔ **SanityTests.java**

Contains:

*   testInvalidLogin → TC002
*   addAndRemoveFromCart → TC005 + TC006

***

### ✔ **RegressionTests.java**

Contains:

*   product-not-found → TC003 variation
*   filter test → TC004
*   multiple add to cart tests → TC005 variations
*   checkout test → TC007
*   logout test → TC008

***

# **B) By TestNG Suite XMLs**

### **1️⃣ testng-smoke.xml**

```xml
<suite name="Smoke Suite">
    <test name="Smoke">
        <classes>
            <class name="com.shwetha.tests.SmokeTests"/>
        </classes>
    </test>
</suite>
```

***

### **2️⃣ testng-sanity.xml**

```xml
<suite name="Sanity Suite">
    <test name="Sanity">
        <classes>
            <class name="com.shwetha.tests.SanityTests"/>
        </classes>
    </test>
</suite>
```

***

### **3️⃣ testng-regression.xml**

```xml
<suite name="Regression Suite">
    <test name="Regression">
        <classes>
            <class name="com.shwetha.tests.RegressionTests"/>
        </classes>
    </test>
</suite>
```

***

# ⚡ How Maven runs the correct suite

You run test suites using **profiles** in `pom.xml`:

### Smoke

```bash
mvn clean test -Psmoke
```

### Sanity

```bash
mvn clean test -Psanity
```

### Regression

```bash
mvn clean test -Pregression
```

Each profile sets:

```xml
<suiteXmlFile>testng-smoke.xml</suiteXmlFile>
```

***

# 📌 Summary – FINAL ANSWER

| Manual TC | Java Test Class               | Suite XML             | Suite Type          |
| --------- | ----------------------------- | --------------------- | ------------------- |
| TC001     | SmokeTests                    | testng-smoke.xml      | Smoke               |
| TC003     | SmokeTests                    | testng-smoke.xml      | Smoke               |
| TC002     | SanityTests                   | testng-sanity.xml     | Sanity              |
| TC005     | SanityTests / RegressionTests | sanity + regression   | Sanity + Regression |
| TC006     | SanityTests / RegressionTests | sanity + regression   | Sanity + Regression |
| TC004     | RegressionTests               | testng-regression.xml | Regression          |
| TC007     | RegressionTests               | testng-regression.xml | Regression          |
| TC008     | RegressionTests               | testng-regression.xml | Regression          |

***

# ✨ If you want…

I can generate an **Excel test case sheet**, **traceability matrix**, or a **visual suite breakdown diagram**.  
Just say **"generate Excel"** or **"generate diagram"**!




















# Demowebshop Selenium TestNG Framework

A drop-in, working Maven project for **TestNG + Selenium** with:
- Data-driven testing (Excel/Apache POI)
- Config via `config.properties`
- Local run or Selenium Grid (local or Docker)
- Retry on failure
- Screenshots on failure
- Extent HTML report
- Three TestNG suites: **smoke**, **sanity**, **regression**

## Prerequisites
- JDK 17+
- Maven 3.9+
- Chrome browser installed

> If ChromeDriver resolution fails on your machine, set `webdriver.chrome.driver` in `src/test/resources/config.properties` to your chromedriver path.

## Run Locally
```bash
mvn clean test -Psmoke      # runs testng-smoke.xml
mvn clean test -Psanity     # runs testng-sanity.xml
mvn clean test -Pregression # runs testng-regression.xml
```

## Run on Local Selenium Grid (Docker)
1. Start grid:
```bash
docker compose up -d
```
2. Set `run.mode=grid` in `src/test/resources/config.properties` (default grid URL `http://localhost:4444/wd/hub`)
3. Run any suite:
```bash
mvn clean test -Psmoke
```

## Reports & Screenshots
- Extent HTML report: `target/extent/extent.html`
- Screenshots on failure: `target/screenshots/`

## Data-driven
- Example Excel file: `src/test/resources/testdata/LoginData.xlsx` (sheet: `Login`)
- DataProvider class: `com.shwetha.framework.utils.DataProviders` (example provider `loginData`)

## Suites
- `testng-smoke.xml` → `com.shwetha.tests.SmokeTests`
- `testng-sanity.xml` → `com.shwetha.tests.SanityTests`
- `testng-regression.xml` → `com.shwetha.tests.RegressionTests`

## Notes
- The framework uses **WebDriverManager** to auto-resolve ChromeDriver. If it cannot, Selenium Manager built into Selenium will still try. As a last resort, set `webdriver.chrome.driver` property.
- The test URLs and product names align with **https://demowebshop.tricentis.com**.
