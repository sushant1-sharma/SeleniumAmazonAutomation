package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.*;

public class AmazonAutomation {

    public static void main(String args[]) {
        WebDriverManager.chromedriver().setup();

        WebDriver driver = new ChromeDriver();

        driver.get("https://www.amazon.in");

        WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));

        searchBox.sendKeys("lg soundbar");

        searchBox.submit();

        List<WebElement> productElements = driver.findElements(By.xpath("//div[@data-component-type=\"s-search-result\"]//div[@class=\"a-section a-spacing-small a-spacing-top-small\"]"));
        List<String> withoutPriceData = new ArrayList<>();

        Comparator<Integer> descendingComparator = Collections.reverseOrder();

        TreeMap<Integer, String> dataMap = new TreeMap<>(descendingComparator);

        for (WebElement element : productElements) {
            String[] productData = element.getText().split("\n");
            String getTitle = getTitleFromProductData(productData);
            int getPrice = getPriceFromProductData(productData);
            if (getPrice == 0) {
                withoutPriceData.add(getTitle);
            } else {
                dataMap.put(getPrice, getTitle);
            }

        }

        for (Map.Entry<Integer, String> entry : dataMap.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }

        for (String element : withoutPriceData) {
            System.out.println(0 + ":" + element);
        }
    }

    public static String getTitleFromProductData(String[] productData) {
        for (String line : productData) {
            if (line.toLowerCase().contains("sound")) {
                return line;
            }
        }
        return "null";
    }

    public static int getPriceFromProductData(String[] productData) {
        for (String line : productData) {
            if (line.contains("₹")) {
                return Integer.valueOf(line.replaceAll("[^\\d]+", ""));
            }
        }
        return 0;
    }

}
