package com.udacity.jwdnd.course1.cloudstorage.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

public class HomePageNoteTab {
    @FindBy(xpath = "/html/body/div/div[@id='contentDiv']/div/div[@id='nav-notes']/div[1]/table/tbody/tr/th")
    private List<WebElement> noteTitleList;

    @FindBy(xpath = "/html/body/div/div[@id='contentDiv']/div/div[@id='nav-notes']/div[1]/table/tbody/tr/td[2]")
    private List<WebElement> noteDescriptionList;

    @FindBy(id = "nav-notes-tab")
    public WebElement navNotesTab;

    public HomePageNoteTab (WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void waitForNotesTab(WebDriver driver) {
        new WebDriverWait(driver, 3000)
                .until(ExpectedConditions.elementToBeClickable(navNotesTab));
    }

    public void waitForNotes(WebDriver driver) {
        new WebDriverWait(driver, 3000)
                .until(ExpectedConditions.visibilityOfAllElements(noteTitleList));
    }

    public List<String> getNoteTitleList() {
        return noteTitleList.stream().map(item -> item.getText()).collect(Collectors.toList());
    }
}