package org.example;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.Dimension;

import java.net.URL;
import java.time.Duration;

public class RunMobileTest {
    public static void main(String[] args) {
        System.out.println("=== ЗАПУСК МОБИЛЬНОГО ТЕСТА С APPIUM 2.x ===");

        AndroidDriver driver = null;
        try {
            // 1. Настройки для Appium 2.x
            UiAutomator2Options options = new UiAutomator2Options()
                    .setUdid("emulator-5554")
                    .setPlatformName("Android")
                    .setAutomationName("UiAutomator2")
                    .setDeviceName("Android Emulator")
                    // Не указываем appPackage/appActivity сначала - просто проверим подключение
                    .setNoReset(true)
                    .setNewCommandTimeout(Duration.ofSeconds(300))
                    .setAutoGrantPermissions(true);

            // 2. Подключаемся к Appium
            System.out.println("1. Подключаемся к Appium Server...");
            System.out.println("   URL: http://127.0.0.1:4723");
            System.out.println("   Capabilities: " + options);

            driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);

            // 3. Получаем информацию
            System.out.println("\n2. УСПЕШНО ПОДКЛЮЧИЛИСЬ!");
            System.out.println("   Session ID: " + driver.getSessionId());
            System.out.println("   Platform: " + driver.getCapabilities().getCapability("platformName"));
            System.out.println("   Device UDID: " + driver.getCapabilities().getCapability("deviceUDID"));

            // 4. Проверяем, какое приложение сейчас активно
            Thread.sleep(3000);
            String currentPackage = driver.getCurrentPackage();
            System.out.println("\n3. Текущее приложение:");
            System.out.println("   Package: " + currentPackage);
            System.out.println("   Activity: " + driver.currentActivity());

            if (currentPackage != null && currentPackage.contains("wikipedia")) {
                System.out.println("   ✓ Wikipedia запущена!");
            } else {
                System.out.println("   ℹ Запустите Wikipedia в эмуляторе вручную");
                System.out.println("   Или измените код для автоматического запуска");
            }

            // 5. Простое взаимодействие
            System.out.println("\n4. Тестируем базовые команды...");

            // Получим размер экрана
            Dimension windowSize = driver.manage().window().getSize();
            System.out.println("   Размер экрана: " + windowSize.getWidth() + "x" + windowSize.getHeight());

            // Получим информацию об устройстве через capabilities
            System.out.println("   Automation Name: " + driver.getCapabilities().getCapability("automationName"));
            System.out.println("   Device Name: " + driver.getCapabilities().getCapability("deviceName"));

            // Альтернативный способ получить device info (если нужен)
            // Object deviceApiLevel = driver.executeScript("mobile: deviceInfo");
            // System.out.println("   Device Info (API): " + deviceApiLevel);

            // Сделаем скриншот
            try {
                var screenshot = driver.getScreenshotAs(org.openqa.selenium.OutputType.FILE);
                System.out.println("   Скриншот сохранен: " + screenshot.getAbsolutePath());
            } catch (Exception e) {
                System.out.println("   Не удалось сделать скриншот: " + e.getMessage());
            }

            Thread.sleep(3000);

            System.out.println("\n✅ ТЕСТ УСПЕШНО ЗАВЕРШЕН!");

        } catch (Exception e) {
            System.err.println("\n❌ ОШИБКА: " + e.getMessage());

            // Проверка специфичных ошибок Appium 2.x
            if (e.getMessage().contains("Could not find a driver")) {
                System.err.println("\n=== РЕШЕНИЕ ДЛЯ APPIUM 2.x ===");
                System.err.println("1. Установите драйвер UiAutomator2:");
                System.err.println("   appium driver install uiautomator2");
                System.err.println("\n2. Проверьте установленные драйверы:");
                System.err.println("   appium driver list --installed");
                System.err.println("\n3. Если не работает, установите через npm:");
                System.err.println("   npm install -g appium-uiautomator2-driver");
                System.err.println("\n4. Перезапустите Appium Server");
            }

        } finally {
            if (driver != null) {
                try {
                    driver.quit();
                    System.out.println("Драйвер закрыт");
                } catch (Exception e) {
                    // игнорируем
                }
            }
        }
    }
}