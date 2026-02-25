
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
mvn clean test -Psmoke      # runs testng-smoke.xml
mvn clean test -Psanity     # runs testng-sanity.xml
mvn clean test -Pregression # runs testng-regression.xml

## Run on Local Selenium Grid (Docker)
1. Start grid:
docker compose up -d
2. Set `run.mode=grid` in `src/test/resources/config.properties` (default grid URL `http://localhost:4444/wd/hub`)
3. Run any suite:
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
