package base;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * NoteBook GUI with JAVAFX
 *
 * COMP 3021
 *
 *
 * @author valerio
 *
 */
public class NoteBookWindow extends Application {

    /**
     * TextArea containing the note
     */
    final TextArea textAreaNote = new TextArea("");
    /**
     * list view showing the titles of the current folder
     */
    final ListView<String> titleslistView = new ListView<String>();
    /**
     *
     * Combobox for selecting the folder
     *
     */
    final ComboBox<String> foldersComboBox = new ComboBox<String>();
    /**
     * This is our Notebook object
     */
    NoteBook noteBook = null;
    /**
     * current folder selected by the user
     */
    String currentFolder = "";
    /**
     * current search string
     */
    String currentSearch = "";

    String currentNote = "";

    Stage stage;

    public static void main(String[] args) {
        launch(NoteBookWindow.class, args);
    }

    @Override
    public void start(Stage stage) {
        loadNoteBook();
        // Use a border pane as the root for scene
        BorderPane border = new BorderPane();
        // add top, left and center
        border.setTop(addHBox());
        border.setLeft(addVBox());
        border.setCenter(addGridPane());

        Scene scene = new Scene(border);
        stage.setScene(scene);
        stage.setTitle("NoteBook COMP 3021");
        stage.show();
    }

    /**
     * This create the top section
     *
     * @return
     */
    private HBox addHBox() {

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10); // Gap between nodes

        Button buttonLoad = new Button("Load");
        buttonLoad.setPrefSize(100, 20);
        //buttonLoad.setDisable(true);
        buttonLoad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Please Choose An File Which Contains a Notebook Object!");

                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Serialized Object File (*.ser)", "*.ser");
                fileChooser.getExtensionFilters().add(extFilter);

                File file = fileChooser.showOpenDialog(stage);

                if(file != null){
                    //NoteBook nb = new NoteBook(file);
                    loadNotebook(file);
                }
            }
        });
        Button buttonSave = new Button("Save");
        buttonSave.setPrefSize(100, 20);
        //buttonSave.setDisable(true);
        buttonSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Please Choose An File Which Contains a Notebook Object!");

                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Serialized Object File (*.ser)", "*.ser");
                fileChooser.getExtensionFilters().add(extFilter);

                File file = fileChooser.showOpenDialog(stage);

                if(file != null){
                    //NoteBook nb = new NoteBook(file);
                    if(noteBook.save(file.getName())){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Successfully saved");
                        alert.setContentText("You file has been saved to file "
                                + file.getName());
                        alert.showAndWait().ifPresent(rs -> {
                            if (rs == ButtonType.OK) {
                                System.out.println("Pressed OK.");
                            }
                        });
                    }else
                        System.out.println("Save Failed");
                }
            }
        });

        hbox.getChildren().addAll(buttonLoad, buttonSave);

        Label s = new Label("Search :");
        TextField textSearch = new TextField();
        Button buttonSearch = new Button("Search");
        buttonSearch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currentSearch = textSearch.getText();
                textAreaNote.setText("");
                updateListView();
            }
        });


        Button buttonRemove = new Button("Clear Search");
        buttonRemove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currentSearch = "";
                textSearch.setText("");
                textAreaNote.setText("");
                updateListView();
            }
        });

        hbox.getChildren().addAll(s, textSearch, buttonSearch, buttonRemove);
        return hbox;
    }

    /**
     * this create the section on the left
     *
     * @return
     */
    private VBox addVBox() {

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10)); // Set all sides to 10
        vbox.setSpacing(8); // Gap between nodes

        // TODO: This line is a fake folder list. We should display the folders in noteBook variable! Replace this with your implementation
        ArrayList<Folder> f = noteBook.getFolders();
        ArrayList<String> n = new ArrayList<>();
        for(Folder i: f){
            n.add(i.getName());
        }

        foldersComboBox.getItems().addAll(n);

        foldersComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                currentFolder = t1.toString();
                // this contains the name of the folder selected
                // TODO update listview
                updateListView();

            }

        });

        foldersComboBox.setValue("-----");

        Button buttonAddFolder = new Button("Add a Folder");
        buttonAddFolder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TextInputDialog dialog = new TextInputDialog("Add a Folder");
                dialog.setTitle("Input");
                dialog.setHeaderText("Add a new folder for your notebook:");
                dialog.setContentText("Please enter the name you want to create:");
                Optional<String> result = dialog.showAndWait();
                if(result.isPresent()){
                    String newFolderName = result.get();
                    Boolean wrongName = false ;
                    if(newFolderName.equals("")){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning");
                        alert.setContentText("Please input a valid folder name");
                        alert.showAndWait().ifPresent(rs -> {
                            if (rs == ButtonType.OK) {
                                System.out.println("Pressed OK.");
                            }
                        });
                        wrongName = true ;
                    }else{
                        for(Folder f: noteBook.getFolders()){
                            if(newFolderName.equals(f.getName())){
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setTitle("Warning");
                                alert.setContentText("You already have a folder named with "+f.getName());
                                alert.showAndWait().ifPresent(rs -> {
                                    if (rs == ButtonType.OK) {
                                        System.out.println("Pressed OK.");
                                    }
                                });
                                wrongName = true;
                                break;
                            }
                        }
                    }
                    if(!wrongName){
                        noteBook.addFolder(newFolderName);
                        foldersComboBox.getItems().add(newFolderName);
                        updateListView();
                    }

                }
            }
        });

        HBox hBox2 = new HBox(foldersComboBox, buttonAddFolder);
        hBox2.setSpacing(8);

        titleslistView.setPrefHeight(100);

        titleslistView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                if (t1 == null)
                    return;
                String title = t1.toString();
                // This is the selected title
                // TODO load the content of the selected note in
                // textAreNote
                String content = "";
                for(Folder i: noteBook.getFolders()){
                    if(i.getName().equals(currentFolder)){
                        for(Note j: i.getNotes()){
                            if(j.getTitle().equals(title)){
                                currentNote = title;
                                content = ((TextNote)j).content;
                            }
                        }
                    }
                }
                textAreaNote.setText(content);

            }
        });

        Button buttonAddNote = new Button("Add a Note");
        buttonAddNote.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(currentFolder.equals("-----")){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setContentText("Please choose a folder first!");
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("Pressed OK.");
                        }
                    });
                }else{
                    TextInputDialog dialog = new TextInputDialog("Add a Note");
                    dialog.setTitle("Input");
                    dialog.setHeaderText("Add a new note to current folder");
                    dialog.setContentText("Please enter the name you note:");

                    Optional<String> result = dialog.showAndWait();
                    String newNoteName = result.get();
                    if(newNoteName.equals(""))
                    {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning");
                        alert.setContentText("Please input a valid note name");
                        alert.showAndWait().ifPresent(rs -> {
                            if (rs == ButtonType.OK) {
                                System.out.println("Pressed OK.");
                            }
                        });
                    }
                    else
                    {
                        Boolean wrongName = false;
                        for(Folder i: noteBook.getFolders()){
                            if(i.getName().equals(currentFolder)){
                                for(Note j: i.getNotes()){
                                    if(newNoteName.equals(j.getTitle())){
                                        Alert alert = new Alert(Alert.AlertType.WARNING);
                                        alert.setTitle("Warning");
                                        alert.setContentText("You already have a note named with "+j.getTitle());
                                        alert.showAndWait().ifPresent(rs -> {
                                            if (rs == ButtonType.OK) {
                                                System.out.println("Pressed OK.");
                                            }
                                        });
                                        wrongName = true;
                                    }
                                }
                            }
                        }
                        if(!wrongName){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Successful!");
                            alert.setHeaderText("Message");
                            alert.setContentText("Insert note " + newNoteName + " to folder " + currentFolder + " successfully!");
                            alert.showAndWait().ifPresent(rs -> {
                                if (rs == ButtonType.OK) {
                                    System.out.println("Pressed OK.");
                                }
                            });
                            noteBook.createTextNote(currentFolder, newNoteName);
                            updateListView();
                        }

                    }
                }
            }
        });

        vbox.getChildren().add(new Label("Choose folder: "));
        //vbox.getChildren().add(foldersComboBox);
        vbox.getChildren().add(hBox2);
        vbox.getChildren().add(new Label("Choose note title"));
        vbox.getChildren().add(titleslistView);
        vbox.getChildren().add(buttonAddNote);

        return vbox;
    }

    private void updateListView() {
        ArrayList<String> list = new ArrayList<String>();
        //ArrayList<Folder> i = new ArrayList<Folder>();
        if(currentSearch.equals("")){
            for(Folder i: noteBook.getFolders()){
                if(i.getName().equals(currentFolder)){
                    for(Note j: i.getNotes()){
                        list.add(j.getTitle());
                    }
                    break;
                }
            }
        }else{
            //System.out.println(currentSearch);
            for(Folder i: noteBook.getFolders()){
                if(i.getName().equals(currentFolder)){
                    //ArrayList<Note> notes = i.searchNotes(currentSearch);

                    /*
                    List<Note> j = i.searchNotes(currentSearch);
                    for(Note n: j){
                        list.add(n.getTitle());
                    }
                    */
                    i.searchNotes(currentSearch).forEach(a -> {
                        list.add(a.getTitle());
                    });
                    break;
                }
            }
        }

        // TODO populate the list object with all the TextNote titles of the
        // currentFolder

        ObservableList<String> combox2 = FXCollections.observableArrayList(list);
        titleslistView.setItems(combox2);
        textAreaNote.setText("");
    }

    /*
     * Creates a grid for the center region with four columns and three rows
     */
    private GridPane addGridPane() {

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        HBox hBox = new HBox();
        hBox.setSpacing(8);
        hBox.setPadding(new Insets(8));
        ImageView saveView = new ImageView(new Image(new File("save.png").toURI().toString()));
        saveView.setFitHeight(18);
        saveView.setFitWidth(18);
        saveView.setPreserveRatio(true);
        ImageView deleteView = new ImageView(new Image(new File("delete.png").toURI().toString()));
        deleteView.setFitHeight(18);
        deleteView.setFitWidth(18);
        deleteView.setPreserveRatio(true);
        Button buttonSave = new Button("Save Note");
        buttonSave.setPrefSize(100, 20);
        buttonSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if((!currentFolder.equals("-----")) && (!currentNote.equals(""))){
                    for(Folder i: noteBook.getFolders()){
                        if(i.getName().equals(currentFolder)){
                            for(Note j: i.getNotes()){
                                ((TextNote)j).content = textAreaNote.getText();
                            }
                        }
                    }
                }else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setContentText("Please select a folder and a note");
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("Pressed OK.");
                        }
                    });
                }
            }
        });
        Button buttonDelete = new Button("Delete Note");
        buttonDelete.setPrefSize(100, 20);
        buttonDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if((!currentFolder.equals("-----")) && (!currentNote.equals(""))){
                    for(Folder i: noteBook.getFolders()){
                        if(i.getName().equals(currentFolder)){
                            if(i.removeNotes(currentNote)){
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Succeed");
                                alert.setContentText("Your note has been successfully removed");
                                alert.showAndWait().ifPresent(rs -> {
                                    if (rs == ButtonType.OK) {
                                        System.out.println("Pressed OK.");
                                    }
                                });
                                updateListView();
                            }else{
                                System.out.println("removeNotes fail");
                            }
                        }
                    }
                }else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setContentText("Please select a folder and a note");
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("Pressed OK.");
                        }
                    });
                }
            }
        });
        hBox.getChildren().addAll(saveView, buttonSave, deleteView, buttonDelete);

        textAreaNote.setEditable(true);
        textAreaNote.setMaxSize(450, 400);
        textAreaNote.setWrapText(true);
        textAreaNote.setPrefWidth(450);
        textAreaNote.setPrefHeight(400);
        // 0 0 is the position in the grid
        grid.add(hBox, 0,0);
        grid.add(textAreaNote, 0, 1);

        return grid;
    }

    private void loadNoteBook() {
        NoteBook nb = new NoteBook();
        nb.createTextNote("COMP3021", "COMP3021 syllabus", "Be able to implement object-oriented concepts in Java.");
        nb.createTextNote("COMP3021", "course information",
                "Introduction to Java Programming. Fundamentals include language syntax, object-oriented programming, inheritance, interface, polymorphism, exception handling, multithreading and lambdas.");
        nb.createTextNote("COMP3021", "Lab requirement",
                "Each lab has 2 credits, 1 for attendence and the other is based the completeness of your lab.");

        nb.createTextNote("Books", "The Throwback Special: A Novel",
                "Here is the absorbing story of twenty-two men who gather every fall to painstakingly reenact what ESPN called “the most shocking play in NFL history” and the Washington Redskins dubbed the “Throwback Special”: the November 1985 play in which the Redskins’ Joe Theismann had his leg horribly broken by Lawrence Taylor of the New York Giants live on Monday Night Football. With wit and great empathy, Chris Bachelder introduces us to Charles, a psychologist whose expertise is in high demand; George, a garrulous public librarian; Fat Michael, envied and despised by the others for being exquisitely fit; Jeff, a recently divorced man who has become a theorist of marriage; and many more. Over the course of a weekend, the men reveal their secret hopes, fears, and passions as they choose roles, spend a long night of the soul preparing for the play, and finally enact their bizarre ritual for what may be the last time. Along the way, mishaps, misunderstandings, and grievances pile up, and the comforting traditions holding the group together threaten to give way. The Throwback Special is a moving and comic tale filled with pitch-perfect observations about manhood, marriage, middle age, and the rituals we all enact as part of being alive.");
        nb.createTextNote("Books", "Another Brooklyn: A Novel",
                "The acclaimed New York Times bestselling and National Book Award–winning author of Brown Girl Dreaming delivers her first adult novel in twenty years. Running into a long-ago friend sets memory from the 1970s in motion for August, transporting her to a time and a place where friendship was everything—until it wasn’t. For August and her girls, sharing confidences as they ambled through neighborhood streets, Brooklyn was a place where they believed that they were beautiful, talented, brilliant—a part of a future that belonged to them. But beneath the hopeful veneer, there was another Brooklyn, a dangerous place where grown men reached for innocent girls in dark hallways, where ghosts haunted the night, where mothers disappeared. A world where madness was just a sunset away and fathers found hope in religion. Like Louise Meriwether’s Daddy Was a Number Runner and Dorothy Allison’s Bastard Out of Carolina, Jacqueline Woodson’s Another Brooklyn heartbreakingly illuminates the formative time when childhood gives way to adulthood—the promise and peril of growing up—and exquisitely renders a powerful, indelible, and fleeting friendship that united four young lives.");

        nb.createTextNote("Holiday", "Vietnam",
                "What I should Bring? When I should go? Ask Romina if she wants to come");
        nb.createTextNote("Holiday", "Los Angeles", "Peter said he wants to go next Agugust");
        nb.createTextNote("Holiday", "Christmas", "Possible destinations : Home, New York or Rome");
        noteBook = nb;

    }

    private void loadNotebook(File file){
        NoteBook nb = new NoteBook(file.getName());
        noteBook = nb;
        updateListView();
    }
}
