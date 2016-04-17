package scrape;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

public class FXMLController {

    @FXML
    TextField linkField, fileNameField;
    @FXML
    Button button;
    @FXML
    Label statusLabel;
    @FXML
    ProgressBar progress;
    Task<Void> task;


    @FXML
    public void handleClick() {
        initTask(new ApplicationStatusProcess(linkField.getText()));
        progress.progressProperty().bind(task.progressProperty());
        statusLabel.textProperty().bind(task.messageProperty());
        new Thread(task).start();
        button.setDisable(true);

        task.setOnSucceeded((WorkerStateEvent event) -> {
            button.setDisable(false);
        });
    }

//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        try {
//            rp = new RationProcess(linkField.getText());
//            initTask(rp);
//        } catch (IOException ex) {
//            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    private void initTask(Processable rp) {
        task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                updateMessage("Fetching Records in page.");

                int records = rp.getRecordCount();

                rp.process(fileNameField.getText(), (FileWriter writer) -> {
                    int iterations;
                    for (iterations = 0; iterations < records; iterations++) {
                        if (isCancelled()) {
                            break;
                        }
                        String link;
                        try {
                            link = rp.getPageLink(iterations);
                            updateMessage((int) (iterations / (float) records * 100)
                                    + "% Done : " + "Writing record " + (iterations + 1));
                            rp.writeRecord(link, writer);
                            updateProgress(iterations, records);
                        } catch (IOException ex) {
                            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                updateMessage("Successfully processed " + records + " records");
                return null;
            }

        };
    }

}
