package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NotesTab {


    @FindBy(id = "nav-notes-tab")
    private WebElement notesTab;

    @FindBy(id = "btn-add-a-new-note")
    private WebElement addANewNoteButton;

    @FindBy(id = "note-title")
    private WebElement txtElementTitle;

    @FindBy(id = "note-description")
    private WebElement txtElementDesc;

    @FindBy(id = "noteSubmit")
    private WebElement noteModalBtn;


    public NotesTab(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void selectTab() {
        this.notesTab.click();
    }

    public void addNewNotes(String title, String Description) {

        //pop up new Notes button
        addANewNoteButton.click();
        txtElementTitle.sendKeys(title);
        txtElementDesc.sendKeys(Description);
    }

    public void confirmCreate() {
        noteModalBtn.click();
    }
}
