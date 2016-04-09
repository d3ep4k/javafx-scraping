package village.user.details;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

public class FXMLController implements Initializable {

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
        progress.progressProperty().bind(task.progressProperty());
        statusLabel.textProperty().bind(task.messageProperty());
        new Thread(task).start();
        button.setDisable(true);

        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                button.setDisable(false);
                initTask();
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTask();
    }

    private void initTask() {
        task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                updateMessage("Fetching Records in page.");
                RationProcess rp = new RationProcess(linkField.getText());
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
