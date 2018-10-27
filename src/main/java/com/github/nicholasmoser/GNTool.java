package com.github.nicholasmoser;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * A tool that allows you to modify files in a Naruto GNT ISO file.
 * 
 * @author Nicholas Moser
 */
public class GNTool extends Application
{
    private static final Logger LOGGER = Logger.getLogger(GNTool.class.getName());

    @Override
    public void start(Stage primaryStage)
    {
        LOGGER.info("Application has started.");
        setLoggingProperties();
        createGUI(primaryStage);
    }

    /**
     * Creates the GUI for the application.
     * 
     * @param primaryStage The stage to use.
     */
    private void createGUI(Stage primaryStage)
    {
        setIcons(primaryStage);
        GridPane buttonPane = createButtonGrid();
        Scene scene = new Scene(buttonPane);

        primaryStage.setTitle("GNTool");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Sets the application icons on the stage.
     * 
     * @param primaryStage The primary stage to set the icons for.
     */
    private void setIcons(Stage primaryStage)
    {
        ObservableList<Image> icons = primaryStage.getIcons();
        icons.add(new Image(getClass().getResourceAsStream("naru16.gif")));
        icons.add(new Image(getClass().getResourceAsStream("naru32.gif")));
        icons.add(new Image(getClass().getResourceAsStream("naru64.gif")));
        icons.add(new Image(getClass().getResourceAsStream("naru128.gif")));
    }

    /**
     * Creates the grid of buttons for the application.
     * 
     * @return The created button grid.
     */
    private GridPane createButtonGrid()
    {
        Font buttonTextFont = new Font(20);

        Button isoExtractButton = new Button();
        isoExtractButton.setText("Export Files from ISO");
        isoExtractButton.setFont(buttonTextFont);
        isoExtractButton.setTooltip(new Tooltip("From a GameCube ISO, extract all root files to a given directory."));
        isoExtractButton.setOnAction(new EventHandler<ActionEvent>()
        {

            @Override
            public void handle(ActionEvent event)
            {
                LOGGER.info("Export Files from ISO button pressed.");
                ISOUtils.exportFiles();
            }
        });

        Button unpackFPKButton = new Button();
        unpackFPKButton.setText("Unpack FPKs in Directory");
        unpackFPKButton.setFont(buttonTextFont);
        isoExtractButton.setTooltip(new Tooltip("Decompress and extract files from FPK files."));
        unpackFPKButton.setOnAction(new EventHandler<ActionEvent>()
        {

            @Override
            public void handle(ActionEvent event)
            {
                LOGGER.info("Unpack FPKs in Directory button pressed.");
                FPKUnpacker.unpack();
            }
        });

        Button repackFPKButton = new Button();
        repackFPKButton.setText("Repack FPKs in Directory");
        repackFPKButton.setFont(buttonTextFont);
        isoExtractButton.setTooltip(new Tooltip("Re-compress and insert files back into respective FPK files."));
        repackFPKButton.setOnAction(new EventHandler<ActionEvent>()
        {

            @Override
            public void handle(ActionEvent event)
            {
                LOGGER.info("Repack FPKs in Directory button pressed.");
                FPKPacker.pack();
            }
        });

        Button isoImportButton = new Button();
        isoImportButton.setText("Import Files into ISO");
        isoImportButton.setFont(buttonTextFont);
        isoExtractButton.setTooltip(new Tooltip("Select root directory of game files and create an ISO from it."));
        isoImportButton.setOnAction(new EventHandler<ActionEvent>()
        {

            @Override
            public void handle(ActionEvent event)
            {
                LOGGER.info("Import Files into ISO button pressed.");
                ISOUtils.importFiles();
            }
        });

        GridPane buttonPane = new GridPane();
        buttonPane.setAlignment(Pos.CENTER);
        buttonPane.setVgap(10);
        buttonPane.setPadding(new Insets(12, 12, 12, 12));
        buttonPane.add(isoExtractButton, 0, 0);
        buttonPane.add(unpackFPKButton, 0, 1);
        buttonPane.add(repackFPKButton, 0, 2);
        buttonPane.add(isoImportButton, 0, 3);

        return buttonPane;
    }

    /**
     * Sets the custom logging properties from the logging.properties included resource file.
     */
    private void setLoggingProperties()
    {
        try (InputStream properties = getClass().getResourceAsStream("logging.properties"))
        {
            LogManager.getLogManager().readConfiguration(properties);
        }
        catch (SecurityException | IOException e)
        {
            String errorMessage = String.format("Unable to load logging.properties, fatal error: %s", e.toString());
            LOGGER.log(Level.SEVERE, e.toString(), e);
            Alert alert = new Alert(AlertType.ERROR, errorMessage);
            alert.setHeaderText("Logging Error");
            alert.setTitle("Logging Error");
            alert.showAndWait();
        }
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
