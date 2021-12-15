package me.p3074098.stringingmanager.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.NamedArg;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FancyInput extends Pane {

    @FXML private Pane anchor;
    @FXML private HBox fieldBox;
    @FXML private Label label;
    @FXML private TextField textField;
    
    private StringProperty labelText;
    private StringProperty promptText;
    private ObjectProperty<Class<?>> inputType;
    
    private Node nextTarget;
    private Node previousTarget;
    
    private Predicate<String> stringValidate;
    private Predicate<Double> doubleValidate;

    private BiFunction<String, KeyCode, String> mapInput;
    
    public FancyInput(@NamedArg("labelDefault") String labelDefault,
                      @NamedArg("prefText") String preferredText,
                      @NamedArg("validateType") Class<?> type) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("fancyInput.fxml"));

            loader.setController(this);

            Pane pane = loader.load();

            this.getChildren().add(pane);

            labelText = new SimpleStringProperty(this, labelDefault);
            promptText =  new SimpleStringProperty(this, preferredText);
            inputType = new SimpleObjectProperty<>(type);


            fieldBox.prefWidthProperty().bind(pane.prefWidthProperty().subtract(20));

        } catch (IOException e) {
            e.printStackTrace();
        }

        label.textProperty().bind(labelText);
        textField.promptTextProperty().bind(promptText);
        textField.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");

        setLabelText(labelDefault);

        
        anchor.setOnMouseClicked(e -> textField.requestFocus());
        
        textField.setOnKeyPressed(e -> {
            if (nextTarget != null)
                if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.DOWN)
                    nextTarget.requestFocus();
            if (previousTarget != null)
                if (e.getCode() == KeyCode.UP)
                    previousTarget.requestFocus();
        });

        // Set color to blue if the text field is selected
        textField.focusedProperty().addListener(object -> {
            positionLabel();
            if(((ObservableBooleanValue) object).get())
                select();
            else {
                if (getInputType().equals(String.class)) {
                    validateString();
                } else if (getInputType().equals(Double.class)) {
                    validateDouble();
                }
            }
        });

        textField.setOnKeyTyped(e -> {
            if (mapInput != null)
                textField.setText(mapInput.apply(textField.getText(), e.getCode()));
        });
    }

    private void positionLabel() {
        boolean raise = textField.isFocused() || !textField.getText().isEmpty();

        double newY = raise ? 10 : 30;
        double newTextSize = raise ? 10 : 15;
        double fieldOpacity = raise ? 100 : 0;

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(100),
                            new KeyValue(label.fontProperty(), Font.font(newTextSize)),
                            new KeyValue(label.layoutYProperty(), newY),
                            new KeyValue(textField.opacityProperty(), fieldOpacity))
            );

            timeline.play();
    }
    
    public void setNextTarget(Node nextTarget) {
        this.nextTarget = nextTarget;
    }
    
    public void setPreviousTarget(Node previousTarget) {
        this.previousTarget = previousTarget;
    }
    
    public void setValidateString(Predicate<String> predicate) {
        this.stringValidate = predicate;
    }
    
    public void setValidateDouble(Predicate<Double> predicate) {
        this.doubleValidate = predicate;
    }

    public void setMapInput(BiFunction<String, KeyCode, String> mapInput) {
        this.mapInput = mapInput;
    }

    protected void select() {
        setColor("selected");
    }

    protected void clearColor() {
        setColor("normal");
    }

    protected void clearText() {
        textField.clear();
    }

    protected void warn() {
        setColor("warn");
    }

    private void setColor(String className) {
        label.getStyleClass().removeIf(s -> s.contains("fancy-input-"));
        fieldBox.getStyleClass().removeIf(s -> s.contains("fancy-input-"));
        
        label.getStyleClass().add("fancy-input-" + className + "-text-fill");
        fieldBox.getStyleClass().add("fancy-input-" + className + "-border-fill");
    }

    public String validateString() {
        if (textField.getText().isEmpty()) {
            warn();
            return null;
        }
        
        if (stringValidate != null && !stringValidate.test(textField.getText())) {
            warn();
            return null;
        }

        clearColor();
        return textField.getText();
    }
    
    protected void adjustVisibility(boolean show) {
        fieldBox.setVisible(show);
        fieldBox.setManaged(show);
        label.setVisible(show);
        label.setManaged(show);
    }

    public Double validateDouble() {
        if (textField.getText().isEmpty()) {
            warn();
            return null;
        }

        try {
            Double d = Double.parseDouble(textField.getText());
    
            if (doubleValidate != null && !doubleValidate.test(d)) {
                warn();
                return null;
            }
            
            clearColor();
            return d;
        } catch (NumberFormatException e) {
            warn();
            return null;
        }
    }
    
    public TextField getTextField() {
        return textField;
    }
    
    public StringProperty labelTextProperty() {
        return labelText;
    }

    public StringProperty promptTextProperty() {
        return promptText;
    }

    public String getLabelText() {
        return labelText.get();
    }

    public String getPromptText() {
        return promptText.get();
    }

    public void setPromptText(String promptText) {
        this.promptText.set(promptText);
    }

    public void setLabelText(String text) {
        labelText.set(text);
        label.setPrefWidth(Region.USE_COMPUTED_SIZE);
        positionLabel();
    }

    public ObjectProperty<Class<?>> inputTypeProperty() {
        return inputType;
    }

    public void setInputType(Class<?> inputType) {
        this.inputType.set(inputType);
    }

    public Class<?> getInputType() {
        return inputType.get();
    }
}
