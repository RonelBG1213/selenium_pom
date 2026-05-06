# How to Run — Step-by-Step Guide

---

## Part 1: Start the Express Server

The Express server is the web app that Selenium tests run against. It must be running before you execute any tests.

### Step 1 — Install dependencies (first time only)

Open a terminal in VS Code (`` Ctrl+` ``) and run:

```powershell
cd express-app/client
npm install
npm run build
```

Then install the server dependencies:

```powershell
cd ..
npm install
```

You only need to do this once. Skip it on subsequent runs.

### Step 2 — Start the server

```powershell
cd express-app
node server.js
```

You should see:

```
Test app running at http://localhost:3000
```

Leave this terminal open. The server must stay running while tests execute.

### Step 3 — Verify it's working

Open your browser and go to `http://localhost:3000`.
You should see the login page.

Credentials are set in `.env` (`ADMIN_EMAIL` / `ADMIN_PASSWORD`). Defaults: `admin@example.com` / `Admin123!`.

### Stopping the server

Press `Ctrl+C` in the terminal where `node server.js` is running.

> **Important:** Registered users are stored in memory only. Stopping and restarting the server resets all registered accounts (except the seeded admin). This is expected — restart the server before each full test run.

---

## Part 2: Run the Selenium Tests

Open a **second terminal** (click `+` in the terminal panel). Keep the server terminal open.

### Step 1 — Make sure Maven is available

```powershell
mvn -version
```

If you see `command not found`, load your profile first:

```powershell
. $PROFILE
mvn -version
```

Expected output:
```
Apache Maven 3.9.9
Java version: 24.0.2
```

### Step 2 — Navigate to the project root

```powershell
cd C:\Users\test\Documents\Active_Projects\baseWebapp\seniumjava_pom
```

---

## Running Tests

### Run all tests (headless — no browser window)

```powershell
mvn clean test
```

### Run all tests (headed — browser window visible)

```powershell
mvn clean test -DHEADLESS=false
```

### Run smoke tests only (4 fast tests)

```powershell
mvn clean test -Dsurefire.suiteXmlFiles=smoke.xml -DHEADLESS=false
```

### Run a single test class

```powershell
mvn clean test -Dtest=LoginTest -DHEADLESS=false
mvn clean test -Dtest=RegisterTest -DHEADLESS=false
mvn clean test -Dtest=NavigationTest -DHEADLESS=false
```

### Run a single test method

```powershell
mvn clean test -Dtest=NavigationTest#testSignOut -DHEADLESS=false
```

### Run by group

```powershell
mvn clean test -Dgroups=smoke -DHEADLESS=false
mvn clean test -Dgroups=regression -DHEADLESS=false
```

---

## What to Expect

### Successful run

```
[INFO] Tests run: 13, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

### Failed test

When a test fails, a screenshot is automatically saved:

```
[Screenshot] target/screenshots/20260430_071542_testLogin.png
```

Open the PNG to see what the browser looked like at the moment of failure.

### Flaky test (retry)

If a test fails due to timing, it retries automatically up to 2 times:

```
[Retry] testLogin — attempt 1 of 2
```

If it passes on retry it is not reported as a failure.

---

## Full Workflow (Start to Finish)

```
Terminal 1                          Terminal 2
──────────────────────────────      ──────────────────────────────
cd express-app
node server.js                 →    # wait for "running at :3000"
                                    cd seniumjava_pom
                                    mvn clean test -DHEADLESS=false
                                    # watch browser windows open/close
                                    # BUILD SUCCESS
Ctrl+C  (stop server)
```

---

## Troubleshooting

| Problem | Fix |
|---|---|
| `mvn: command not found` | Run `. $PROFILE` then retry |
| `ERR_CONNECTION_REFUSED` | Server isn't running — start `node server.js` first |
| `newuser@example.com already exists` | Restart the server to clear in-memory users |
| Browser opens but tests fail immediately | Check server is on port 3000: `http://localhost:3000` |
| `ChromeDriver` error | WebDriverManager auto-downloads it — check your internet connection |
| Tests pass locally but fail in CI | Set `HEADLESS=true` and ensure server is started before Maven runs |
