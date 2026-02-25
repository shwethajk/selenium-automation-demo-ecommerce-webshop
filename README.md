
# Demowebshop Selenium TestNG Framework

Maven project for **TestNG + Selenium** with:
- Data-driven testing (JSON/Excel/Apache POI)
- Config via `config.properties`
- Local run or Selenium Grid (local or Docker)
- Retry on failure
- Screenshots on failure
- Extent HTML report
- Different TestNG suites: **smoke**, **sanity**, **regression**, etc


## Project Structure (high level)

    demowebshop-automation/
    ├─ pom.xml
    ├─ docker-compose.yml                 # Selenium Grid via Docker
    ├─ extent-config.xml                  # Extent report config
    ├─ testng-smoke.xml                   # Smoke suite (small)
    ├─ testng-sanity.xml                  # Sanity suite (mid)
    ├─ testng-regression.xml              # Regression suite (max)
    ├─ src/test/resources/
    │  ├─ config.properties               # All configuration here
    │  ├─ log4j2.xml                      # log4j2 configuration 
    │  └─ testdata/
    │     ├─ EXCEL              
    │     │  └─ TestData.xlsx             # Excel data
    │     └─ JSON              
    │        └─ cart.json                 # json data
    │        └─ login.json              
    │        └─ pdp.json               
    │        └─ search.json              
    │        └─ search_filter.json               
    ├─ src/test/java/com/shwetha/
    │  ├─ framework/
    │  │  ├─ base/BaseTests.java          # Shared driver lifecycle
    │  │  ├─ driver/   
    │  │  │  ├─ DriverFactory.java        # Local & Grid/Docker driver
    │  │  │  ├─ DriverRegistry.java        
    │  │  ├─ listeners/
    │  │  │  ├─ ParallelSuiteConfigurer.java  # Retry failed tests (once)
    │  │  │  ├─ RetryAnalyzer.java        
    │  │  │  ├─ RetryContext.java        # Retry failed tests (once)
    │  │  │  ├─ UnifiedAnnotatinTransformer.java        
    │  │  │  └─ TestListener.java         # Extent Report + log + screenshots on failure
    │  │  └─ utils/
    │  │     ├─ ConfigReader.java
    │  │     ├─ DataProvidersMap.java
    │  │     ├─ DataRepo.java
    │  │     ├─ ExcelUtils.java
    │  │     ├─ FileUtils.java
    │  │     ├─ JsonData.java
    │  │     └─ ScreenshotUtil.java      # Screenshot helper
    │  ├─ pageobjects/
    │  │  ├─ BasePage.java              
    │  │  ├─ CartPage.java              
    │  │  ├─ HomePage.java              
    │  │  ├─ LoginPage.java              
    │  │  ├─ ProductPage.java              
    │  │  └─ SearchResultsPage.java       
    │  └─ tests/
    │     ├─ CartTests.java              
    │     ├─ LoginTests.java              
    │     ├─ PdpTests.java              
    │     └─ SearchTests.java  
    ├─ .github/workflows/
    │  └─ CI_workflow.yml
    └─ README.md


## Prerequisites (local)
- JDK 17+
- Maven 3.9+
- Chrome browser installed

> If ChromeDriver resolution fails on your machine, set `webdriver.chrome.driver` in `src/test/resources/config.properties` to your chromedriver path.

## Run Locally
mvn clean test -Psmoke      # runs testng-smoke.xml
mvn clean test -Psanity     # runs testng-sanity.xml
mvn clean test -Pregression # runs testng-regression.xml

## Run on Local Selenium Grid (Docker)
1. Start grid: ocker compose up -d
2. Set `run.mode=grid` in `src/test/resources/config.properties` (default grid URL `http://localhost:4444/wd/hub`)
3. Run any suite:
mvn clean test -Psmoke

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
