package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

    //@Autowired
    // public static UserService userService;

    public static WebDriver driver;
    public static String baseURL;
    @LocalServerPort
    public int port;

    @BeforeAll
    public static void beforeAll() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        // userService.deleteTestUsers();

    }

    @AfterAll
    public static void afterAll() {

        //Delete existing test users, this deletes the test*** users so that you can run the test a number of times
        //Note that this URL is permitted to cleanup. This needs to be disabled when this is moved to production by removing
        //Exceptions from Security Config
        driver.get(baseURL + "/deleteTestOnlyUsers?token=JyCn7vG8dQQ2EkumvjTJfbMvnJC2P272LNKm9TZy83eVbjC6eFX5vkn1qgZH");

        //signup

        driver.quit();
        driver = null;

    }

    @BeforeEach
    public void beforeEach() {
        baseURL = baseURL = "http://localhost:" + port;
    }

    private void RegisterAndLogin(String usr, String pwd) throws InterruptedException {

        String username = usr;
        String password = pwd;
        driver.get(baseURL + "/signup");
        SignupPage signupPage = new SignupPage(driver);
        Thread.sleep(2000);
        signupPage.signup("Peter", "Zastoupil", username, password);
        //Login
        driver.get(baseURL + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);


    }

    private void registerAndLoginAndSelectNotesTab() throws InterruptedException {
        RegisterAndLogin("userForTestingNotes", "NotesPwd");
        Thread.sleep(2000);
        Assertions.assertEquals("Home", driver.getTitle());

        //Go to notetab and click
        NotesTab notesTab = new NotesTab(driver);
        notesTab.selectTab();
        Thread.sleep(2000);
    }

    private void registerAndLoginAndSelectCredentialTab() throws InterruptedException {
        RegisterAndLogin("userForTestingNotes", "NotesPwd");
        Thread.sleep(2000);
        Assertions.assertEquals("Home", driver.getTitle());
        activateCredentialTab();

    }

    private void activateCredentialTab() throws InterruptedException {
        driver.findElement(By.id("nav-credentials-tab")).click();
        Thread.sleep(2000);

    }

    /*
      Test 1:- Write tests for user signup, login, and unauthorized access restrictions.
        Write a test that verifies that an unauthorized user can only access the login and signup pages.
        Write a test that signs up a new user, logs in, verifies that the home page is accessible, logs out, and verifies that the home page is no longer accessible.
     */
    @Test
    @Order(1)
    public void tests_for_user_signup_login_and_unauthorized_access_restrictions() throws InterruptedException {
        String username = "testusr";
        String password = "testpwd";
        driver.get(baseURL + "/signup");
        SignupPage signupPage = new SignupPage(driver);
        Thread.sleep(2000);
        signupPage.signup("Peter", "Zastoupil", username, password);
        //Login
        driver.get(baseURL + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);
        //Login success should go to Home
        Thread.sleep(2000);
        Assertions.assertEquals("Home", driver.getTitle());
        //Logout
        HomePage homePage = new HomePage(driver);
        homePage.logOut();
        Thread.sleep(2000);
        Assertions.assertEquals("Login", driver.getTitle());
        // home not accessible after logging out
        driver.get("http://localhost:" + this.port + "/");
        Thread.sleep(2000);
        Assertions.assertNotEquals("Home", driver.getTitle());
        // Try accessing credential resource , it should take to login
        driver.get("http://localhost:" + this.port + "/#nav-credentials");
        Thread.sleep(2000);
        Assertions.assertEquals("Login", driver.getTitle());
    }

    /*
    Test 2 :- Write tests for note creation, viewing, editing, and deletion.
        Write a test that creates a note, and verifies it is displayed.
        Write a test that edits an existing note and verifies that the changes are displayed.
        Write a test that deletes a note and verifies that the note is no longer displayed.
     */
    @Test
    @Order(2)
    public void test_that_creates_note_verifies_displayed() throws InterruptedException {
        registerAndLoginAndSelectNotesTab();

        driver.findElement(By.id("btn-add-a-new-note")).click();
        Thread.sleep(2000);

        //Create a note
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        String noteTitle = "Note #" + new Random().nextInt(100);
        String noteDec = "WebDriver-Created-Note Description" + formatter.format(date);

        //Add the notes
        driver.findElement(By.id("note-title")).sendKeys(noteTitle);
        driver.findElement(By.id("note-description")).sendKeys(noteDec);
        driver.findElement(By.className("btn-primary")).click();
        Thread.sleep(2000);

        // Get the alert message Check whether the note has been added correctly
        String message = driver.findElement(By.id("alert-message")).getText();
        Assertions.assertTrue(message.contains(noteTitle) && message.contains("Successfully added!"));
        Thread.sleep(2000);

        //Go back to note Tab
        activateNotesTab();

        //Check they are displayed
        Assertions.assertTrue(driver.findElement(By.className("note-title")).isDisplayed());
        Assertions.assertTrue(driver.findElement(By.className("note-description")).isDisplayed());
        // Make sure that we can find the note added in the list of notes
        var noteTitles = driver.findElements(By.className("note-title"));
        var noteDescription = driver.findElements(By.className("note-description"));

        //Check there are elements in the notes
        Assertions.assertNotNull(noteTitles);
        Assertions.assertNotNull(noteDescription);
        // Match the one we added in  the home page - titles
        var createdNoteSTitle = noteTitles.stream()
                .filter(title -> noteTitle.equals(title.getText()))
                .findAny()
                .orElse(null);

        Assertions.assertEquals(createdNoteSTitle.getText(), noteTitle);

        // Match the one we added in  the home page - titles
        var createdNoteSDesc = noteDescription.stream()
                .filter(title -> noteDec.equals(title.getText()))
                .findAny()
                .orElse(null);
        Assertions.assertEquals(createdNoteSDesc.getText(), noteDec);
    }

    private void activateNotesTab() throws InterruptedException {
        driver.get("http://localhost:" + this.port + "/");
        driver.findElement(By.id("nav-notes-tab")).click();
        Thread.sleep(2000);
    }

    //Write a test that edits an existing note and verifies that the changes are displayed.
    @Test
    @Order(3)
    public void test_that_edits_existing_note_and_verifies_that_the_changes_are_displayed() throws InterruptedException {
        registerAndLoginAndSelectNotesTab();

        //Edit the first note
        // edit note
        driver.findElement(By.cssSelector("button[class='btn btn-success']")).click();
        Thread.sleep(2000);

        //Create a note
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        String noteTitle = "Note -Edited #" + new Random().nextInt(100);
        String noteDec = "WebDriver-Created-Note Description - Edited at" + formatter.format(date);

        driver.findElement(By.id("note-title")).clear();
        driver.findElement(By.id("note-title")).sendKeys(noteTitle);
        driver.findElement(By.id("note-description")).clear();
        driver.findElement(By.id("note-description")).sendKeys(noteDec);
        driver.findElement(By.className("btn-primary")).click();

        Thread.sleep(2000);
        // Get the alert message Check whether the note has been added correctly
        String message = driver.findElement(By.id("alert-message")).getText();
        Assertions.assertTrue(message.contains(noteTitle) && message.contains("Updated"));


        //Go back to note Tab
        activateNotesTab();

        //Check they are displayed
        Assertions.assertTrue(driver.findElement(By.className("note-title")).isDisplayed());
        Assertions.assertTrue(driver.findElement(By.className("note-description")).isDisplayed());
        // Make sure that we can find the note added in the list of notes
        var noteTitles = driver.findElements(By.className("note-title"));
        var noteDescription = driver.findElements(By.className("note-description"));

        //Check there are elements in the notes
        Assertions.assertNotNull(noteTitles);
        Assertions.assertNotNull(noteDescription);
        // Match the one we added in  the home page - titles
        var updatedNoteSTitle = noteTitles.stream()
                .filter(title -> noteTitle.equals(title.getText()))
                .findAny()
                .orElse(null);

        Assertions.assertEquals(updatedNoteSTitle.getText(), noteTitle);

        // Match the one we added in  the home page - titles
        var updatedNoteSDesc = noteDescription.stream()
                .filter(title -> noteDec.equals(title.getText()))
                .findAny()
                .orElse(null);
        Assertions.assertEquals(updatedNoteSDesc.getText(), noteDec);


    }

    @Test
    @Order(4)
    void test_deletes_a_note_and_verifies_that_the_note_is_no_longer_displayed() throws InterruptedException {

        registerAndLoginAndSelectNotesTab();
        Thread.sleep(2000);
        var noteTitlesOld = driver.findElement(By.className("note-title"));
        driver.findElement(By.cssSelector("a[class='btn btn-danger']")).click();
        Thread.sleep(2000);

        // Get the alert message Check whether the note has been added correctly
        String message = driver.findElement(By.id("alert-message")).getText();
        Assertions.assertTrue(message.contains("Successfully deleted"));

        //Go back to note Tab
        activateNotesTab();
        // Its valid that it will through a no element exception

        WebElement noteTitlesNew = null;
        try {
             noteTitlesNew = driver.findElement(By.className("note-title"));
        }catch (NoSuchElementException noSuchElementException){
            ;
        }
        if(noteTitlesNew != null) {
            Assertions.assertTrue((!noteTitlesOld.equals(noteTitlesNew)));
        }

    }

    @Test
    @Order(5)
    void test_that_creates_set_of_credentials_verifies_that_they_are_displayed_and_verifies_that_the_displayed_password_is_encrypted() throws InterruptedException {

        registerAndLoginAndSelectCredentialTab();
        Thread.sleep(2000);
        driver.findElement(By.id("btn-add-new-cred")).click();
        Thread.sleep(2000);

        String url = "udacity.com";
        String userName = "alibaba";
        String password = "abracadabra";


        driver.findElement(By.id("credential-url")).sendKeys(url);
        driver.findElement(By.id("credential-username")).sendKeys(userName);
        driver.findElement(By.id("credential-password")).sendKeys(password);
        driver.findElement(By.id("btn-credentials-submit")).click();
        Thread.sleep(2000);
        // Get the alert message Check whether the note has been added correctly
        String message = driver.findElement(By.id("alert-message")).getText();

        Assertions.assertTrue(message.contains(url)
                && message.contains(userName)
                && message.contains("Added !Credentials")
        );
        // credential is visible
        driver.get("http://localhost:" + this.port + "/");
        Thread.sleep(2000);
        driver.findElement(By.id("nav-credentials-tab")).click();
        Thread.sleep(2000);


        var urls = driver.findElements(By.className("cls-credential-url"));
        var usernames = driver.findElements(By.className("cls-credential-username"));
        var passwords = driver.findElements(By.className("cls-credential-password"));

        var createdURL = urls.stream()
                .filter(urlElement -> url.equals(urlElement.getText()))
                .findAny()
                .orElse(null);
        Assertions.assertEquals(createdURL.getText(), url);

        var createdUsername = usernames.stream()
                .filter(userNameElement -> userName.equals(userNameElement.getText()))
                .findAny()
                .orElse(null);
        Assertions.assertEquals(createdUsername.getText(), userName);

        var createdPasswords = passwords.stream()
                .filter(passwordElement -> password.equals(passwordElement.getText()))
                .findAny()
                .orElse(null);
        // Password should not match with what's provided - so should return null
        Assertions.assertNull(createdPasswords);

    }

    /*

    Test 3:- Write tests for credential creation, viewing, editing, and deletion.

        Write a test that views an existing set of credentials, verifies that the viewable password is unencrypted, edits the credentials, and verifies that the changes are displayed.
        Write a test that deletes an existing set of credentials and verifies that the credentials are no longer displayed.
     */


}
