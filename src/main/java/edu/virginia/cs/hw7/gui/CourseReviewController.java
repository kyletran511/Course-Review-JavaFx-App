package edu.virginia.cs.hw7.gui;

import edu.virginia.cs.hw7.courseReview.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class CourseReviewController implements Initializable {

    private CourseReview courseReview;

    private static CourseReview persistCourseReview;
    @FXML
    private Label courseReviewLabel;
    @FXML
    private TextField userName;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginButton;
    @FXML
    private Label invalidLoginMessage;
    @FXML
    private Button switchToCreateUser;
    @FXML
    private Label mainMenuLabel;
    @FXML
    private Button exitProgram;
    @FXML
    private Button createAccountButton;
    @FXML
    private TextField newUserName;
    @FXML
    private PasswordField newPassword;
    @FXML
    private PasswordField confirmNewPassword;
    @FXML
    private Label createNewUserError;
    @FXML
    private Button backToLoginButton;
    @FXML
    private Button moveSubmitReviewButton;
    @FXML
    private Button submitCourseButton;
    @FXML
    private Button submitReviewToMainMenuButton;
    @FXML
    private TextField courseNameText;
    @FXML
    private Label submitCourseInstructions;
    @FXML
    private Pane courseNameInstructions;
    @FXML
    private Button closeCourseNameInstructionsButton;
    @FXML
    private Pane submitReviewPane;
    @FXML
    private TextField rating;
    @FXML
    private TextField review;
    @FXML
    private Button submitReviewButton;
    @FXML
    private Label invalidCourseMessage;
    @FXML
    private Pane alreadyReviewedPane;
    @FXML
    private Button closeAlreadyReviewedMessage;
    @FXML
    private Pane successSubmitReviewMessage;
    @FXML
    private Button closeSuccessReviewMessage;
    @FXML
    private Button moveViewReviewButton;
    @FXML
    private Button backToMainMenuFromView;
    @FXML
    private Pane validCourseNameInstructions_viewReview;
    @FXML
    private TextField courseNameInput_view;
    @FXML
    private TableView<ReviewGUI> reviewTable;
    @FXML
    private TableColumn reviewColumn;
    @FXML
    private TableColumn ratingColumn;
    @FXML
    private Pane invalidCourseName_view;
    @FXML
    private Button closeInvalidCourseMessage_view;
    @FXML
    private Pane noReviewMessage;
    @FXML
    private Button closeNoReviewMessage;
    @FXML
    private Label submitReviewErrorMessage;
    @FXML
    private Label courseAverage;
    @FXML
    private Button closeValidCourseNameInstructions_viewReview;
    @FXML
    private Button logOut;

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle){
        if(persistCourseReview == null) {
            persistCourseReview = new CourseReviewImpl();
        }
        courseReview = persistCourseReview;
    }

    public void login(){
        try{
            courseReview.loginExistingUser(userName.getText(), password.getText());
            if (courseReview.isLoggedIn()){
                resetLoginPage();
                switchToMainMenu((Stage) userName.getScene().getWindow());
            }
        }
        catch (Exception e){
            userName.setStyle("-fx-border-color: red; -fx-border-width: 2; -fx-border-radius: 5;");
            password.setStyle("-fx-border-color: red; -fx-border-width: 2; -fx-border-radius: 5;");
            invalidLoginMessage.setText(e.getMessage());
            invalidLoginMessage.setVisible(true);
        }
    }

    private void resetLoginPage(){
        invalidLoginMessage.setVisible(false);
        userName.setStyle(null);
        password.setStyle(null);
    }

    public void moveToCreateUser(){
        switchToCreateNewUser((Stage) switchToCreateUser.getScene().getWindow());
    }

    public void createNewUser(){
        try{
            courseReview.createNewUser(newUserName.getText(), newPassword.getText(), confirmNewPassword.getText());
            switchToMainMenu((Stage) newUserName.getScene().getWindow());
        }
        catch (Exception e){
            setUpCreateNewUserError();
            createNewUserError.setVisible(true);
            createNewUserError.setText(e.getMessage());
        }
    }

    private void setUpCreateNewUserError(){
        newUserName.setStyle("-fx-border-color: red; -fx-border-width: 2; -fx-border-radius: 5;");
        newPassword.setStyle("-fx-border-color: red; -fx-border-width: 2; -fx-border-radius: 5;");
        confirmNewPassword.setStyle("-fx-border-color: red; -fx-border-width: 2; -fx-border-radius: 5;");
    }

    public void moveBackToLogin() { switchToLogin((Stage) backToLoginButton.getScene().getWindow());}
    public void moveToSubmitReview(){ switchToSubmitReview((Stage) moveSubmitReviewButton.getScene().getWindow());}

    public void submitCourse(){
        try {
            String input = courseNameText.getText().trim();
            int spaceIndex = input.indexOf(" ");

            if(spaceIndex < 0) {
                throw new InvalidCourseNameException("You need a space between the department name and course number");
            }

            String department = input.substring(0, input.indexOf(" "));
            int catalogNumber;
            catalogNumber = Integer.parseInt(courseNameText.getText().substring(courseNameText.getText().indexOf(" ") + 1));

            courseReview.submitCourseForReview(department, catalogNumber);
            setUpSubmitReview();

        } catch (NumberFormatException e) {
            invalidCourseMessage.setWrapText(true);
            invalidCourseMessage.setVisible(true);
            invalidCourseMessage.setText("Error: Unable to parse course number, make sure you're inputting a number as the second token e.g. CS 3140");
        } catch (InvalidCourseNameException e){
            invalidCourseMessage.setWrapText(true);
            invalidCourseMessage.setVisible(true);
            invalidCourseMessage.setText(e.getMessage());
        }
        catch (UserAlreadyReviewedCourseException e){
            alreadyReviewedPane.setVisible(true);
        }
    }

    public void closeAlreadyReviewedMessage(){
        switchToMainMenu((Stage) courseNameText.getScene().getWindow());
        alreadyReviewedPane.setVisible(false);
    }
    public void openInstructions_submitCourse(){ courseNameInstructions.setVisible(true);}
    public void closeInstructions_submitCourse(){ courseNameInstructions.setVisible(false); }
    public void moveBackToMenuFromSubmitCourse(){ switchToMainMenu((Stage) submitReviewToMainMenuButton.getScene().getWindow()); }

    private void setUpSubmitReview(){
        submitCourseInstructions.setText("You entered a valid course name! Now, go ahead and write a review, give a rating between 1-5, the hit submit");
        submitReviewPane.setVisible(true);
        invalidCourseMessage.setVisible(false);
    }
    public void submitReview(){
        try{
            courseReview.submitReview(review.getText(), Review.Rating.getEnumByString(rating.getText()));
            successSubmitReviewMessage.setVisible(true);
        }
        catch (InvalidRatingException | EmptyReviewException e){
            submitReviewErrorMessage.setVisible(true);
            submitReviewErrorMessage.setText(e.getMessage());
        }
    }

    private void closeSubmitReviewErrors(){
        if (submitReviewErrorMessage.isVisible()){ submitReviewErrorMessage.setVisible(false); }
    }
    public void closeSuccessSubmitReview(){
        switchToMainMenu((Stage) review.getScene().getWindow());
        successSubmitReviewMessage.setVisible(false);
        submitReviewPane.setVisible(false);
    }

    public void moveToViewReview(){ switchToViewReview((Stage) moveViewReviewButton.getScene().getWindow()); }

    public void viewReview(){
        try{
            String input = courseNameInput_view.getText().trim();
            int spaceIndex = input.indexOf(" ");
            if(spaceIndex < 0)
                throw new InvalidCourseNameException("You need a space between the department name and course number");
            String department = input.substring(0, input.indexOf(" "));
            int catalogNumber = Integer.parseInt(courseNameInput_view.getText().substring(courseNameInput_view.getText().indexOf(" ") + 1));


            List<Review> reviews = courseReview.viewReview(department, catalogNumber);
            setUpReviewTable();
            int numOfReviews = 0;
            double ratingTotal = 0.0;
            for (Review r : reviews){
                ReviewGUI reviewGUI = new ReviewGUI(r.getReview(), Review.Rating.enumToInteger(r.getRating()));
                reviewTable.getItems().add(reviewGUI);
                ratingTotal += Review.Rating.enumToInteger(r.getRating());
                numOfReviews++;
            }
            double courseAvg = (ratingTotal/numOfReviews);
            courseAverage.setVisible(true);
            if(!Double.isNaN(courseAvg))
                courseAverage.setText("Course Average: " + String.format("%.2f", courseAvg) + "/5");
            else
                courseAverage.setText("No Reviews Found");
        } catch (NumberFormatException | InvalidCourseNameException e) {
            clearTableAfterSearch();
            invalidCourseName_view.setVisible(true);
        } catch (IllegalArgumentException e){
            clearTableAfterSearch();
            noReviewMessage.setVisible(true);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setUpReviewTable(){
        reviewColumn.setCellValueFactory(new PropertyValueFactory<ReviewGUI, String>("review"));
        //URL: https://stackoverflow.com/questions/22732013/javafx-tablecolumn-text-wrapping
        //Used source to set the reviewColumn to auto adjust text to wrap around column to prevent cutting off data
        reviewColumn.setCellFactory(column -> new TableCell<ReviewGUI, String>(){
            @Override
            protected void updateItem(String item, boolean empty){
                if (item == null || empty){
                    setText(null);
                    setStyle("");
                }
                else{
                    Text text = new Text(item);
                    text.wrappingWidthProperty().bind(reviewColumn.widthProperty());
                    text.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
                    setGraphic(text);
                }
            }
        });
        ratingColumn.setCellValueFactory(new PropertyValueFactory<ReviewGUI, Integer>("Rating"));
        clearTableAfterSearch();
    }

    private void clearTableAfterSearch(){
        if (reviewTable.getItems().size() > 0){
            reviewTable.getItems().clear();
            courseAverage.setVisible(false);
        }
    }

    public void closeInvalidCourseMessage(){ invalidCourseName_view.setVisible(false); }
    public void closeNoReviewsMessage(){ noReviewMessage.setVisible(false );}
    public void moveToMainMenuFromViewReview(){ switchToMainMenu((Stage) backToMainMenuFromView.getScene().getWindow()); }
    public void openValidCourseInstructions_view(){ validCourseNameInstructions_viewReview.setVisible(true);}
    public void closeValidCourseInstructions_view(){ validCourseNameInstructions_viewReview.setVisible(false);}

    public void logOut(){
        courseReview.loginOut();
        switchToLogin((Stage) mainMenuLabel.getScene().getWindow());
    }

    public void exitProgram(){
        System.exit(0);
    }

    private void switchToLogin(Stage currentStage){
        try{
            Scene mainMenu = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/virginia/cs/hw7/login-view.fxml"))));
            currentStage.setScene(mainMenu);
            currentStage.show();
        }
        catch (IOException ignored){}
    }
    private void switchToMainMenu(Stage currentStage){
        try{
            Scene mainMenu = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/virginia/cs/hw7/mainMenu-view.fxml"))));
            currentStage.setScene(mainMenu);
            currentStage.show();
        }
        catch (IOException ignored){}
    }

    private void switchToCreateNewUser(Stage currentStage){
        try{
            Scene newUser = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/virginia/cs/hw7/createNewUser-view.fxml"))));
            currentStage.setScene(newUser);
            currentStage.show();
        }
        catch (IOException e){e.printStackTrace();}
    }

    private void switchToSubmitReview(Stage currentStage){
        try{
            Scene submitReview = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/virginia/cs/hw7/submitReview-view.fxml"))));
            currentStage.setScene(submitReview);
            currentStage.show();
        }
        catch (IOException ignored){}
    }

    private void switchToViewReview(Stage currentStage){
        try{
            Scene viewReview = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/virginia/cs/hw7/viewReview-view.fxml"))));
            currentStage.setScene(viewReview);
            currentStage.show();
        }
        catch (IOException ignored){}
    }
}