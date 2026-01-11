Проект для автоматизации тестирования мобильных приложений на Android с использованием Appium, Java и TestNG.

# Требования
## Системные требования
- Windows 10/11, macOS или Linux
- Java JDK 11+ (рекомендуется 11, 17 или 21)
- Maven 3.6+
- Node.js 16+ (для Appium)

## Мобильное окружение
- Android SDK (через Android Studio)
- Appium Server 2.x
- Android эмулятор или реальное устройство

# Быстрый старт
1. Установка зависимостей
## Клонирование проекта (если нужно)
git clone https://github.com/Misha-Glazunov/Mobile-Automation-Testing-Project
cd automation-project

## Сборка проекта
mvn clean install

# 2. Настройка окружения
## Windows:
Установите переменные среды
setx ANDROID_HOME "C:\Users\%USERNAME%\AppData\Local\Android\Sdk" /M
setx ANDROID_SDK_ROOT "C:\Users\%USERNAME%\AppData\Local\Android\Sdk" /M

# 3. Установка Appium и драйверов
## Установка Appium глобально
npm install -g appium

## Установка драйверов
appium driver install uiautomator2
appium driver install xcuitest  # для iOS (опционально)

## Проверка установки
appium --version
appium driver list --installed
# 4. Настройка эмулятора
- Установите Android Studio
- Запустите Device Manager
- Создайте эмулятор (например, Pixel 4, API 34)
- Запустите эмулятор

# 5. Проверка окружения
## Проверка Java
java --version

## Проверка Maven
mvn --version

## Проверка ADB
adb devices

## Проверка Appium
appium --version

# Запуск тестов

## Вариант 1: Через IntelliJ IDEA
- Откройте проект в IntelliJ IDEA
- Запустите RunMobileTest.java как Java Application

## Вариант 2: Через командную строку
Сначала запустите Appium Server
appium

В другом терминале запустите тест
mvn exec:java -Dexec.mainClass="org.example.RunMobileTest"

# Конфигурация тестов
Настройка capabilities в коде
UiAutomator2Options options = new UiAutomator2Options()
    .setUdid("emulator-5554")           // ID устройства
    .setPlatformName("Android")         // Платформа
    .setAutomationName("UiAutomator2")  // Драйвер автоматизации
    .setDeviceName("Android Emulator")  // Имя устройства
    .setAppPackage("org.wikipedia")     // Пакет приложения
    .setAppActivity("org.wikipedia.main.MainActivity") // Активность
    .setNoReset(true)                   // Не сбрасывать состояние
    .setAutoGrantPermissions(true);     // Автоматически давать разрешения

# Использование разных устройств
## Для реального устройства:

.setUdid("ваш_device_id")  // Получить через 'adb devices'
.setDeviceName("Real Device")

##Для разных эмуляторов:
.setUdid("emulator-5554")
.setAvd("Pixel_4_API_34")  // Имя эмулятора
