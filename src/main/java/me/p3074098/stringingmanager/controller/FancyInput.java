package me.p3074098.stringingmanager.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.NamedArg;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import me.p3074098.stringingmanager.api.Warnable;

import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FancyInput extends Pane implements Warnable {

    @FXML private Pane anchor;
    @FXML private HBox fieldBox;
    @FXML private Label label;
    @FXML private TextField textField;
    
    private boolean warn = true;
    
    private IntegerProperty maxChars = new SimpleIntegerProperty(Integer.MAX_VALUE);
    private IntegerProperty minChars = new SimpleIntegerProperty(0);
    private StringProperty labelText = new SimpleStringProperty("Label");
    private StringProperty promptText = new SimpleStringProperty("Prompt");
    private ObjectProperty<Class<?>> inputType = new SimpleObjectProperty<>(String.class);
    
    private Node nextTarget;
    private Node previousTarget;
    
    private Predicate<String> stringValidate;
    private Predicate<Double> doubleValidate;

    private boolean allowPeriods = false;
    
    public FancyInput(@NamedArg("labelText") String labelText,
                      @NamedArg("promptText") String promptText) {
        this(labelText, promptText, String.class);
    }
    
    public FancyInput(@NamedArg("labelText") String labelText,
                      @NamedArg("promptText") String promptText,
                      @NamedArg("inputType") Class<?> inputType) {
        this(labelText, promptText, inputType, Integer.MAX_VALUE, 0);
    }
    
    
    public FancyInput(@NamedArg("labelText") String labelText,
                      @NamedArg("promptText") String promptText,
                      @NamedArg("maxChars") int maxChars,
                      @NamedArg("minChars") int minChars) {
        this(labelText, promptText, String.class, maxChars, minChars);
    }
    
    public FancyInput(@NamedArg("labelText") String labelText,
                      @NamedArg("promptText") String promptText,
                      @NamedArg("inputType") Class<?> inputType,
                      @NamedArg("maxChars") int maxChars,
                      @NamedArg("minChars") int minChars) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("fancyInput.fxml"));

            loader.setController(this);

            Pane pane = loader.load();

            this.getChildren().add(pane);


            pane.prefWidthProperty().bind(prefWidthProperty());
            fieldBox.prefWidthProperty().bind(prefWidthProperty().subtract(20));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        this.maxChars = new SimpleIntegerProperty(maxChars);
        this.minChars = new SimpleIntegerProperty(minChars);
        this.labelText = new SimpleStringProperty(labelText);
        this.promptText = new SimpleStringProperty(promptText);
        this.inputType = new SimpleObjectProperty<>(inputType);
        
        label.textProperty().bind(this.labelText);
        textField.promptTextProperty().bind(this.promptText);
        textField.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");
        
        setLabelText(getLabelText());

        
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

        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();

            if (getInputType().equals(Double.class)) {
                if (allowPeriods && text.equals("."))
                    return change;
                if (!text.matches("[0-9]*"))
                    return null;
            }
            
            if (textField.getText().length() == this.maxChars.get())
                change.setText("");

            return change;
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        textField.setTextFormatter(textFormatter);
    }

    private void positionLabel() {
        boolean raise = textField.isFocused() || !textField.getText().isEmpty();

        double newY = raise ? 10 : 30;
        double newTextSize = raise ? 10 : 14;
        double fieldOpacity = raise ? 100 : 0;

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(100),
                            new KeyValue(label.layoutYProperty(), newY),
                            new KeyValue(label.fontProperty(), Font.font(newTextSize)),
                            new KeyValue(textField.opacityProperty(), fieldOpacity))
            );

            timeline.play();
    }

    public void setAllowPeriods(boolean allowPeriods) {
        this.allowPeriods = allowPeriods;
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
        if (isWarnable())
            setColor("warn");
        else
            setColor("normal");
    }

    private void setColor(String className) {
        label.getStyleClass().removeIf(s -> s.contains("fancy-input-"));
        fieldBox.getStyleClass().removeIf(s -> s.contains("fancy-input-"));
        
        label.getStyleClass().add("fancy-input-" + className + "-text-fill");
        fieldBox.getStyleClass().add("fancy-input-" + className + "-border-fill");
    }
    
    private boolean validateInternal() {
        if (textField.getText().isEmpty())
            return false;
        
        return textField.getText().length() >= minChars.get();
    }

    public String validateString() {
        if (!validateInternal()) {
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
        if (!validateInternal()) {
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
    
    public int getMaxChars() {
        return maxChars.get();
    }
    
    public IntegerProperty maxCharsProperty() {
        return maxChars;
    }
    
    public void setMaxChars(int maxChars) {
        this.maxChars.set(maxChars);
    }
    
    public void setMinChars(int minChars) {
        this.minChars.set(minChars);
    }
    
    public IntegerProperty minCharsProperty() {
        return minChars;
    }
    
    public int getMinChars() {
        return minChars.get();
    }
    
    @Override
    public boolean isWarnable() {
        return warn;
    }
    
    @Override
    public void setWarnable(boolean warnable) {
        this.warn = warnable;
        warn();
    }
}
