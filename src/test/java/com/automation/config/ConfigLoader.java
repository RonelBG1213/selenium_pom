package com.automation.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

public class ConfigLoader {

    private static ConfigLoader instance;

    private final String baseUrl;
    private final String browser;
    private final boolean headless;
    private final int implicitWaitSeconds;
    private final int explicitWaitSeconds;
    private final String adminEmail;
    private final String adminPassword;

    private ConfigLoader() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = loadEnvironmentNode(mapper);

        // ENV-specific values first, then BROWSER/BASE_URL/HEADLESS overrides on top
        String envBaseUrl = resolve("BASE_URL");
        baseUrl = (envBaseUrl != null && !envBaseUrl.isBlank())
                ? envBaseUrl
                : root.get("baseUrl").asText();

        String envBrowser = resolve("BROWSER");
        browser = (envBrowser != null && !envBrowser.isBlank())
                ? envBrowser.toLowerCase()
                : root.get("browser").asText("chrome").toLowerCase();

        String envHeadless = resolve("HEADLESS");
        headless = (envHeadless != null && !envHeadless.isBlank())
                ? Boolean.parseBoolean(envHeadless)
                : root.get("headless").asBoolean(true);

        implicitWaitSeconds = root.get("implicitWaitSeconds").asInt(5);
        explicitWaitSeconds = root.get("explicitWaitSeconds").asInt(10);

        String envAdminEmail = resolve("ADMIN_EMAIL");
        adminEmail = (envAdminEmail != null && !envAdminEmail.isBlank())
                ? envAdminEmail
                : "admin@example.com";

        String envAdminPassword = resolve("ADMIN_PASSWORD");
        adminPassword = (envAdminPassword != null && !envAdminPassword.isBlank())
                ? envAdminPassword
                : "Admin123!";
    }

    /**
     * If ENV property is set (e.g. dev/qa/staging/prod), loads the matching block
     * from environments.json. Otherwise falls back to config.json.
     */
    private JsonNode loadEnvironmentNode(ObjectMapper mapper) {
        String env = resolve("ENV");

        if (env != null && !env.isBlank()) {
            try (InputStream is = getClass().getClassLoader()
                    .getResourceAsStream("config/environments.json")) {
                if (is == null) {
                    throw new RuntimeException("config/environments.json not found on classpath");
                }
                JsonNode envFile = mapper.readTree(is);
                JsonNode envNode = envFile.get(env.toLowerCase());
                if (envNode == null) {
                    throw new RuntimeException(
                            "Environment '" + env + "' not found in environments.json. " +
                            "Available: " + envFile.fieldNames()
                    );
                }
                return envNode;
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException("Failed to load environments.json", e);
            }
        }

        // Default: config.json
        try (InputStream is = getClass().getClassLoader()
                .getResourceAsStream("config/config.json")) {
            if (is == null) {
                throw new RuntimeException("config/config.json not found on classpath");
            }
            return mapper.readTree(is);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.json", e);
        }
    }

    /** System property takes precedence over environment variable. */
    private String resolve(String key) {
        String sysProp = System.getProperty(key);
        return (sysProp != null) ? sysProp : System.getenv(key);
    }

    public static synchronized ConfigLoader getInstance() {
        if (instance == null) {
            instance = new ConfigLoader();
        }
        return instance;
    }

    public String getBaseUrl()           { return baseUrl; }
    public String getBrowser()           { return browser; }
    public boolean isHeadless()          { return headless; }
    public int getImplicitWaitSeconds()  { return implicitWaitSeconds; }
    public int getExplicitWaitSeconds()  { return explicitWaitSeconds; }
    public String getAdminEmail()        { return adminEmail; }
    public String getAdminPassword()     { return adminPassword; }
}
