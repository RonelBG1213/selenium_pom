# Selenium Java POM Framework

A production-ready, data-driven Selenium test automation framework using Java 17, TestNG, and Guice dependency injection. Runs locally or inside Docker with zero code changes.

## Tech Stack

| Layer | Tool |
|---|---|
| Language | Java 17 |
| Browser automation | Selenium 4 |
| Test runner | TestNG 7 |
| Dependency injection | Google Guice 7 |
| Driver management | WebDriverManager 5 |
| Data serialization | Jackson 2 |
| Build tool | Maven 3 |
| Containerization | Docker (selenium/standalone-chrome) |
| Target web app | Express.js API + React (Vite) |

---

## Project Structure

```
seniumjava_pom/
├── pom.xml                              # Maven build descriptor
├── testng.xml                           # Full regression suite (parallel)
├── smoke.xml                            # Smoke suite — 4 fast sanity tests
├── Dockerfile                           # Container image
├── run-tests.sh                         # Entrypoint: sources .env, runs Maven
├── .env                                 # Local defaults (not committed)
│
├── express-app/                         # Target web app
│   ├── server.js                        # Express JSON API (port 3000)
│   ├── package.json
│   ├── public/                          # Vite build output (served by Express)
│   └── client/                          # React + Vite SPA source
│       ├── src/
│       │   ├── App.jsx                  # React Router routes
│       │   ├── pages/
│       │   │   ├── LoginPage.jsx        # /  — IDs match LoginPageLocators
│       │   │   ├── RegisterPage.jsx     # /register — IDs match RegisterPageLocators
│       │   │   ├── HomePage.jsx         # /home — classes match HomePageLocators
│       │   │   └── DashboardPage.jsx    # /dashboard
│       │   └── components/
│       │       └── Layout.jsx           # Navbar + sidebar (IDs match HomePageLocators)
│       └── vite.config.js               # Proxies /api → localhost:3000
│
└── src/test/
    ├── java/com/automation/
    │   ├── base/
    │   │   └── BaseTest.java            # Abstract base — DI wiring, setup/teardown
    │   ├── config/
    │   │   └── ConfigLoader.java        # Singleton; reads JSON + env var overrides
    │   ├── di/
    │   │   ├── TestModule.java          # Single DI entry point for all tests
    │   │   ├── WebDriverModule.java     # Binds WebDriver + WebDriverProvider
    │   │   ├── WebDriverProvider.java   # ThreadLocal-backed Provider<WebDriver>
    │   │   └── AppModule.java           # Binds ConfigLoader, PageManager
    │   ├── driver/
    │   │   └── DriverFactory.java       # Static factory: Chrome / Firefox
    │   ├── listeners/
    │   │   ├── ScreenshotListener.java        # Saves PNG to target/screenshots/ on failure
    │   │   ├── RetryAnalyzer.java             # Retries failing tests up to 2×
    │   │   └── RetryAnnotationTransformer.java# Wires RetryAnalyzer globally
    │   ├── locators/
    │   │   ├── LoginPageLocators.java    # XPath-only By constants for login page
    │   │   ├── RegisterPageLocators.java # XPath-only By constants for register page
    │   │   ├── HomePageLocators.java     # XPath-only By constants for home page
    │   │   └── DashboardPageLocators.java# XPath-only By constants for dashboard page
    │   ├── pages/
    │   │   ├── BasePage.java            # WebDriverWait helpers, driver accessor
    │   │   ├── LoginPage.java           # Login POM — actions + assertions
    │   │   ├── RegisterPage.java        # Register POM — form actions + soft assertions
    │   │   ├── HomePage.java            # Home POM — nav + sign-out
    │   │   ├── DashboardPage.java       # Dashboard POM — nav back to home
    │   │   └── PageManager.java         # DI facade — one getter per page
    │   ├── tests/
    │   │   ├── LoginTest.java           # Data-driven login scenarios
    │   │   ├── RegisterTest.java        # Register link, navigation, form data tests
    │   │   └── NavigationTest.java      # Home, dashboard, sign-out flows
    │   └── utils/
    │       ├── TestConstants.java       # Shared credentials and constants
    │       ├── TestDataLoader.java      # JSON → List<T> / Object[][] loader
    │       └── model/
    │           ├── LoginData.java       # POJO for login_data.json rows
    │           └── RegisterData.java    # POJO for register_data.json rows
    └── resources/
        ├── simplelogger.properties      # SLF4J log level and format config
        ├── config/
        │   ├── config.json             # Default config (used when ENV is unset)
        │   └── environments.json       # Per-environment config blocks
        └── testdata/
            ├── login_data.json         # Login scenarios (3 rows)
            └── register_data.json      # Registration scenarios (4 rows)
```

---

## Prerequisites

| Requirement | Version |
|---|---|
| JDK | 17 or later |
| Maven | 3.8 or later |
| Chrome (local runs) | Any recent version |
| Node.js (target app) | 18 or later |
| Docker (container runs) | 20 or later |

> WebDriverManager automatically downloads the matching ChromeDriver at runtime — no manual driver setup needed.

---

## Quick Start (Local)

### 1. Build and start the target web app

```bash
cd express-app/client
npm install
npm run build          # outputs React build to express-app/public/

cd ..
npm install
node server.js         # → http://localhost:3000
```

For live development (auto-rebuilds React on save):

```bash
cd express-app
npm run dev            # runs Express + Vite dev server concurrently
```

### 2. Run the full test suite

```bash
# From the project root (seniumjava_pom/)
mvn clean test
```

**Headed mode** (visible browser window):

```bash
mvn clean test -DHEADLESS=false
```

**First-time PowerShell users** — if `mvn` is not recognized, load your profile first:

```powershell
. $PROFILE; mvn clean test -DHEADLESS=false
```

> Maven is installed at `~\tools\apache-maven-3.9.9\bin`. Your PowerShell profile adds it to `$PATH` automatically on new terminals. If a terminal was opened before Maven was installed, run `. $PROFILE` once to apply it to that session.

Or using the provided script (sources `.env` first):

```bash
chmod +x run-tests.sh
./run-tests.sh
```

Expected output:
```
[INFO] Tests run: 13, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

### 3. Run smoke tests only

```bash
mvn clean test -Dsurefire.suiteXmlFiles=smoke.xml
```

Smoke suite runs 4 tests (`testRegisterLinkVisible`, `testNavigateToRegisterPage`, `testHomePageAfterLogin`, `testSignOut`) with no data providers — fast CI sanity check.

---

## Test Suites

### `testng.xml` — full regression (10 tests)

| Suite | Tests |
|---|---|
| Login Tests | `testLogin` × 3 (data-driven) |
| Register Tests | `testRegisterLinkVisible`, `testNavigateToRegisterPage`, `testRegistration` × 4 |
| Navigation Tests | `testHomePageAfterLogin`, `testNavigateToDashboard`, `testNavigateBackToHome`, `testSignOut` |

### `smoke.xml` — smoke (4 tests)

Runs only tests tagged `groups = {"smoke"}`:

| Test | Class |
|---|---|
| `testRegisterLinkVisible` | RegisterTest |
| `testNavigateToRegisterPage` | RegisterTest |
| `testHomePageAfterLogin` | NavigationTest |
| `testSignOut` | NavigationTest |

### Test groups

| Group | Tests included |
|---|---|
| `smoke` | Critical path — link visibility, nav to register, home after login, sign-out |
| `regression` | Every test |

Run a specific group:

```bash
mvn clean test -Dgroups=smoke
mvn clean test -Dgroups=regression
```

---

## Listeners

Both listeners are registered in `testng.xml` and `smoke.xml`.

### `ScreenshotListener`

On every test failure, captures a PNG from the browser and saves it to:

```
target/screenshots/<yyyyMMdd_HHmmss>_<testName>.png
```

The path is printed to the console so it can be linked from CI logs. The `target/` directory is gitignored.

### `RetryAnnotationTransformer` + `RetryAnalyzer`

Automatically wires retry logic onto every `@Test` method — no annotation changes needed on individual tests. A failing test is retried up to **2 times** before being reported as a failure. Each retry is logged:

```
[Retry] testLogin — attempt 1 of 2
```

---

## Configuration

### Default config — `src/test/resources/config/config.json`

Used when no `ENV` is set.

```json
{
  "baseUrl": "http://localhost:3000",
  "browser": "chrome",
  "headless": true,
  "implicitWaitSeconds": 5,
  "explicitWaitSeconds": 10
}
```

### Multi-environment — `src/test/resources/config/environments.json`

Select an environment with the `ENV` property:

```json
{
  "dev":     { "baseUrl": "http://localhost:3000",       "headless": true,  ... },
  "qa":      { "baseUrl": "https://qa.example.com",      "headless": true,  ... },
  "staging": { "baseUrl": "https://staging.example.com", "headless": false, ... },
  "prod":    { "baseUrl": "https://example.com",         "headless": true,  ... }
}
```

### Override resolution order (highest wins)

```
-DBROWSER / -DBASE_URL / -DHEADLESS / -DENV  (Maven system properties)
  └── environment variable BROWSER / BASE_URL / HEADLESS / ENV
        └── environments.json block (selected by ENV)
              └── config.json (fallback when ENV is unset)
```

### `.env` — local defaults

```bash
ENV=dev
BROWSER=chrome
HEADLESS=true
BASE_URL=        # empty = resolved from environments.json
```

Override at runtime without editing files:

```bash
mvn clean test -DENV=qa
mvn clean test -DBROWSER=firefox -DHEADLESS=false
mvn clean test -DBASE_URL=https://custom.example.com
```

### Logging — `src/test/resources/simplelogger.properties`

`com.automation` classes log at DEBUG; all other libraries at WARN. Timestamps use `HH:mm:ss.SSS` format.

---

## Running in Docker

### Build

```bash
docker build -t selenium-fw .
```

### Run against the default environment

```bash
# Requires the express-app to be reachable from inside the container.
# Use host.docker.internal on Mac/Windows or --network host on Linux.
docker run -e BASE_URL=http://host.docker.internal:3000 selenium-fw
```

### Run against a remote environment

```bash
docker run \
  -e ENV=qa \
  -e BASE_URL=https://qa.example.com \
  selenium-fw
```

The Docker image is built on `selenium/standalone-chrome` — Chrome and Xvfb are pre-configured; no extra display setup is required.

---

## Adding Tests

### 1. Create a locators class

```java
// src/test/java/com/automation/locators/SearchPageLocators.java
public final class SearchPageLocators {
    private SearchPageLocators() {}

    public static final By SEARCH_INPUT  = By.xpath("//*[@id='search']");
    public static final By RESULT_HEADER = By.xpath("//*[contains(@class,'result-header')]");
}
```

All locators must use `By.xpath()` exclusively.

### 2. Create a Page Object

```java
// src/test/java/com/automation/pages/SearchPage.java
public class SearchPage extends BasePage {

    @Inject
    public SearchPage(WebDriver driver) { super(driver); }

    public SearchPage search(String query) {
        waitForVisible(SearchPageLocators.SEARCH_INPUT).sendKeys(query);
        return this;
    }

    public SearchPage assertResultHeader(String expected) {
        Assert.assertEquals(waitForVisible(SearchPageLocators.RESULT_HEADER).getText(), expected);
        return this;
    }
}
```

### 3. Register the page in `PageManager`

```java
// Add provider field
private final Provider<SearchPage> searchPageProvider;

// Add to constructor
public PageManager(..., Provider<SearchPage> searchPageProvider) {
    ...
    this.searchPageProvider = searchPageProvider;
}

// Add getter
public SearchPage getSearchPage() { return searchPageProvider.get(); }
```

### 4. Create a data model (if data-driven)

```java
// src/test/java/com/automation/utils/model/SearchData.java
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchData {
    private String query;
    private String expectedHeader;
    // getters + setters
}
```

### 5. Add a JSON data file

```json
// src/test/resources/testdata/search_data.json
[
  { "query": "selenium", "expectedHeader": "Results for selenium" }
]
```

### 6. Write the test

```java
public class SearchTest extends BaseTest {

    @DataProvider(name = "searchData")
    public Object[][] searchData() {
        return TestDataLoader.toDataProvider("search_data.json", SearchData.class);
    }

    @Test(dataProvider = "searchData", groups = {"regression"})
    public void testSearch(SearchData data) {
        pages.getSearchPage()
             .search(data.getQuery())
             .assertResultHeader(data.getExpectedHeader());
    }
}
```

### 7. Add the class to `testng.xml`

```xml
<test name="Search Tests">
    <classes>
        <class name="com.automation.tests.SearchTest"/>
    </classes>
</test>
```

Add to `smoke.xml` as well if any test should be tagged `smoke`.

---

## Parallel Execution

Configured in `testng.xml`:

```xml
<suite parallel="methods" thread-count="3" data-provider-thread-count="3">
```

`WebDriverProvider` uses a `ThreadLocal<WebDriver>` — each thread gets its own isolated browser instance. Safe to increase `thread-count` without driver bleed.

---

## Architecture

```
Test (extends BaseTest)
  │
  ├── @Guice(modules = TestModule.class)  ← declared once on BaseTest
  │     ├── WebDriverModule
  │     │     ├── WebDriverProvider (singleton, ThreadLocal-backed)
  │     │     └── WebDriver         (delegated to WebDriverProvider)
  │     └── AppModule
  │           ├── ConfigLoader      (singleton, reads JSON + env vars)
  │           └── PageManager       (singleton, holds Provider<PageObject>)
  │
  ├── @BeforeMethod: DriverFactory.createDriver() → WebDriverProvider.set()
  ├── @AfterMethod:  WebDriverProvider.quit() → ThreadLocal removed
  │
  └── pages.getLoginPage()           ← lazy, always returns driver for current thread
        .login(username, password)
        .assertOutcome(expectedResult)
```

### Key design decisions

| Decision | Choice | Reason |
|---|---|---|
| Driver scope | `ThreadLocal` in `WebDriverProvider` | Safe parallel execution — no shared state |
| DI scope | Singleton declared in module, not class | Guice controls lifecycle; avoids annotation conflicts |
| Page access | `PageManager` with `Provider<T>` | Pages constructed after driver is set; test classes have zero DI imports |
| Locators | Separate `*Locators` classes, `By.xpath()` only | No `PageFactory`; XPath is explicit and IDE-navigable |
| Fluent API | Page methods return `this` or next page type | Enables readable chained test steps |
| Failure handling | `ScreenshotListener` + `RetryAnalyzer` | Screenshots survive headless; retries absorb transient flakiness |
| Config overrides | System property → env var → JSON | Same code runs locally, in CI, and in Docker |
| Data format | JSON + Jackson | Zero annotation required; `@JsonIgnoreProperties` makes POJOs forward-compatible |
| Assertions | `SoftAssert` for multi-field forms | Reports all failures at once instead of stopping on first |

---

## Target Web App (express-app)

A React SPA (Vite) served by Express.js included as the test target. Element IDs and CSS class names are intentionally matched to the locator constants.

### API endpoints

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/login` | Returns `{success}` or `{error}` |
| `POST` | `/api/register` | Adds user to in-memory store; returns `{success}` or `{error}` |
| `GET` | `*` | Serves React's `index.html` for client-side routing |

### Locator ↔ HTML mapping

| Locator constant | XPath | HTML element |
|---|---|---|
| `LoginPageLocators.USERNAME_INPUT` | `//*[@id='username']` | `<input id="username">` |
| `LoginPageLocators.PASSWORD_INPUT` | `//*[@id='password']` | `<input id="password">` |
| `LoginPageLocators.LOGIN_BUTTON` | `//*[@id='login-btn']` | `<button id="login-btn">` |
| `LoginPageLocators.ERROR_MESSAGE` | `//*[contains(@class,'error-message')]` | `<div class="error-message">` |
| `LoginPageLocators.REGISTER_LINK` | `//a[@href='/register']` | `<a href="/register">` |
| `RegisterPageLocators.NAME_INPUT` | `//*[@id='name']` | `<input id="name">` |
| `RegisterPageLocators.EMAIL_INPUT` | `//*[@id='reg-email']` | `<input id="reg-email">` |
| `RegisterPageLocators.REGISTER_BUTTON` | `//*[@id='register-btn']` | `<button id="register-btn">` |
| `HomePageLocators.NAV_HOME` | `//*[@id='nav-home']` | `<a id="nav-home">` |
| `HomePageLocators.NAV_DASHBOARD` | `//*[@id='nav-dashboard']` | `<a id="nav-dashboard">` |
| `HomePageLocators.NAVBAR_LOGOUT` | `//button[contains(@class,'navbar-logout')]` | `<button class="navbar-logout">` |

Valid credentials (seeded on startup): `admin@example.com` / `Admin123!`

> **Note:** User registrations are stored in memory only. The success scenario in `register_data.json` (`newuser@example.com`) requires the server to be restarted between full test runs, otherwise it fails with "already exists."

---

## Test Data

### `login_data.json`

```json
[
  { "username": "admin@example.com",   "password": "Admin123!", "expectedResult": "success" },
  { "username": "invalid@example.com", "password": "wrongpass", "expectedResult": "failure" },
  { "username": "",                    "password": "",          "expectedResult": "failure" }
]
```

### `register_data.json`

```json
[
  { "name": "New User", "username": "newuser@example.com", "password": "NewPass123!", "confirmPassword": "NewPass123!", "expectedResult": "success" },
  { "name": "Admin",    "username": "admin@example.com",   "password": "Admin123!",   "confirmPassword": "Admin123!",   "expectedResult": "failure", "expectedError": "An account with this email already exists." },
  { "name": "",         "username": "user@example.com",    "password": "Pass123!",    "confirmPassword": "Pass123!",    "expectedResult": "failure", "expectedError": "All fields are required." },
  { "name": "User",     "username": "mismatch@example.com","password": "Pass123!",    "confirmPassword": "DifferentPass!", "expectedResult": "failure", "expectedError": "Passwords do not match." }
]
```

`expectedResult` values: `"success"` or `"failure"`. `expectedError` is optional — when present, the exact string must appear inside the error message element.
