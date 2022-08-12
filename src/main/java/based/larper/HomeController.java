package based.larper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Play;
import jm.util.Read;
import jm.util.Write;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static jm.constants.Durations.*;

public class HomeController {


    double length = 0;
    int order = 3;
    boolean isPlaying = false;
    @FXML
    Label lblFile;
    @FXML
    Button btnOrder;
    @FXML
    Button btnLength;
    @FXML
    private Button btnPick;
    @FXML
    private Button btnExport;
    @FXML
    private Button btnPlay;
    @FXML
    void playMusic(ActionEvent event) {
        if (!isPlaying) {
            isPlaying = true;
            Score scr = generateMusic();
            Play.midi(scr);
        }
        else {
            System.exit(0);
            isPlaying = false;
        }
    }
    @FXML
    private TextField txtOrder;
    @FXML
    private TextField txtLength;

    @FXML
    TextField txtExport;

    File selectedFile = null;
    String exportFile = null;

    @FXML
    void pickFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        selectedFile = fileChooser.showOpenDialog(new Stage());
        lblFile.setText(String.valueOf(selectedFile));
        System.out.println(selectedFile);
    }

    @FXML
    void setOrder(ActionEvent event) {
        order = Integer.parseInt(txtOrder.getText());
    }
    @FXML
    void setLength(ActionEvent event) {
        length = Integer.parseInt(txtLength.getText());
    }

    @FXML
    Button btnExportMIDI;
    @FXML
    void exportMIDI() {

        exportFile = txtExport.getText() + ".mid";
        Score scr = generateMusic();
        Write.midi(scr, exportFile);

    }

    Score generateMusic() {
        Score scr = new Score();
        Read.midi(scr, String.valueOf(selectedFile));

        HashMap<ArrayList<String>, ArrayList<String>> notes = new HashMap<>();
        ArrayList<Double> durations = new ArrayList<>();
        durations.add(QUARTER_NOTE);
        durations.add(HALF_NOTE);
        durations.add(EIGHTH_NOTE);
        durations.add(SIXTEENTH_NOTE);
        durations.add(THIRTYSECOND_NOTE);

        order = 3;
        int score_size = scr.getSize();

        ArrayList<Note> notesList = new ArrayList<>();

        for (int i = 0; i < score_size; i++) {
            int part_count = scr.getPartList().size();
            for(int j = 0; j < part_count; j++) {
                int phrase_count = scr.getPart(j).size();
                for(int k = 0; k < phrase_count; k++) {
                    int note_count = scr.getPart(j).getPhrase(k).getNoteList().size();
                    for(int l = 0; l < note_count; l++) {
                        //                        if(scr.getPart(j).getPhrase(k).getNote(l).getPitch() < 0)
                        //                            continue;
                        notesList.add(scr.getPart(j).getPhrase(k).getNote(l));
                    }
                }
            }
        }

        for (int i = 0; i < notesList.size() - order; i++) {
            List<Note> subList = notesList.subList(i, i + order);
            ArrayList<String> seq = new ArrayList<>();
            for(int j = 0; j < subList.size(); j++) {
                seq.add(subList.get(0).getName());
            }
            if(!notes.containsKey(seq)) {
                notes.put(seq, new ArrayList<>());
            }
            String pitch = notesList.get(i + order).getName();
            ArrayList<String> temp_item = notes.get(seq);
            temp_item.add(pitch);
        }

        List<String> subSeq = new ArrayList<>();
        for(int i = 0; i < order; i++) {
            subSeq.add(notesList.get(0).getName());
        }
        double sum_length = 0;
        length = 1000;
        Phrase phrase = new Phrase();
        Part part = new Part();
        Score score = new Score("Huahahah");

        while(sum_length <= length) {

            if(notes.containsKey(subSeq)) {
                int pitch_idx = (int) Math.floor(Math.random() * notes.get(subSeq).size());
                int duration_idx = (int) Math.floor(Math.random() * durations.size());

                String pitch_val = notes.get(subSeq).get(pitch_idx);
                Note temp_note = new Note(pitch_val);
                temp_note.setDuration(QN);
                temp_note.setDynamic(100);
                temp_note.setDuration(durations.get(duration_idx));
                sum_length += durations.get(duration_idx);
                phrase.addNote(temp_note);
            }
        }
        part.addPhrase(phrase);
        score.addPart(part);
        return score;
    }
}





//package based.larper;
//
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.fxml.Initializable;
//import javafx.scene.Node;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.ListView;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//import java.sql.Connection;
//
//import java.net.URL;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ResourceBundle;
//
//public class HomeController implements Initializable {
//    private Stage stg;
//    private Scene scn;
//    private Parent root;
//
//    public void switchScene(ActionEvent event) throws IOException {
//
//        root = FXMLLoader.load(getClass().getResource("/views/addRecord.fxml"));
//        stg = (Stage)((Node)event.getSource()).getScene().getWindow();
//        scn = new Scene(root);
//        stg.setScene(scn);
//        stg.show();
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        DBUtil conne = new DBUtil();
//        Connection conn = conne.getConnection();
//        String sql = "SELECT * FROM Breako";
//        try {
//            Statement stmt = conn.createStatement();
//            ResultSet rs = stmt.executeQuery(sql);
//            while(rs.next()) {
//                int id = rs.getInt("id");
//                String name = rs.getString("name");
//                String listOut = id + " " + name;
//                tracks.getItems().add(listOut);
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public void printSelect() {
//        lblDisplay.setText("Item selected");
//        System.out.println(tracks.getSelectionModel().getSelectedIndex());
//    }
//
//
//}
