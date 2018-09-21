package sample;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class Downloader implements Constants {

    private static WebDriver browser;

    public static void main(String[] args) {
        browser = new FirefoxDriver();
        instagramLogin();
        for (String user : USERS_TARGET) {
            waitForLoad();
            usersObserver(user);
        }
    }

    private static void usersObserver(String user) {
        browser.navigate().to(STORY_URL + user);
        while (currentUser(user)) {
            waitForLoad();
            String storyURL = findStory(browser.getPageSource());
            if (storyURL != null) downloadStory(storyURL, user);
            browser.findElement(By.className(NEXT_STORY_BUTTON)).click();
        }
        waitForLoad();
    }

    private static boolean currentUser(String user) {
        return !browser.getCurrentUrl().equals(STORY_URL + user);
    }

    private static void downloadStory(String storyURL, String user) {
        String format = storyURL.contains(VIDEO_FORMAT) ? VIDEO_FORMAT : PHOTO_FORMAT;
        String fileName = user + "_" + LocalDate.now() + "(" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH.mm.ss")) + ")" + format;
        try (BufferedInputStream bis = new BufferedInputStream(new URL(storyURL).openStream());
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(OUTPUT_FOLDER + fileName)))) {
            int c;
            while ((c = bis.read()) != -1) bos.write(c);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private static String findStory(String pageSource) {
        String startIndexVideo = "source src=\"";
        String startIndexPhoto = "1080w\" src=\"";
        if (pageSource.contains(IS_VIDEO_TOKEN))
            return pageSource.substring(pageSource.indexOf(startIndexVideo) + startIndexVideo.length(), pageSource.indexOf(VIDEO_FORMAT) + VIDEO_FORMAT.length());
        else if (pageSource.contains(IS_PHOTO_TOKEN)) {
            System.out.println(pageSource.substring(pageSource.indexOf(startIndexPhoto) + startIndexPhoto.length(), pageSource.indexOf(PHOTO_FORMAT, pageSource.indexOf(startIndexPhoto)) + PHOTO_FORMAT.length()));
            return pageSource.substring(pageSource.indexOf(startIndexPhoto) + startIndexPhoto.length(), pageSource.indexOf(PHOTO_FORMAT, pageSource.indexOf(startIndexPhoto)) + PHOTO_FORMAT.length());
        }
        else{
            System.out.println("not find");
            return null;
        }
    }

    private static void instagramLogin() {
        browser.get(AUTHORIZATION_URL);
        waitForLoad();
        browser.findElement(By.name("username")).sendKeys(USER_NAME);
        browser.findElement(By.name("password")).sendKeys(PASSWORD);
        browser.findElement(By.xpath("//button")).click();
    }

    private static void waitForLoad() {
        try {
            Thread.sleep(TIME_TO_LOAD);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
