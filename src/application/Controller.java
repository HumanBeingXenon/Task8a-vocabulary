package application;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;

public class Controller implements Initializable{
	@FXML private Label lblEnglish;
	@FXML private Label lblPhonetic;
	@FXML private Label lblChinese;
	@FXML private FlowPane fpButtons;
	@FXML private Button btnPrevious;
	@FXML private Button btnKnow;
	@FXML private Button btnNotSure;
	@FXML private Button btnNo;
	@FXML private MenuItem mniAbout;
	@FXML private MenuItem mniAmount;
	@FXML private VBox boxRecite;
	@FXML private VBox boxTable;
	
	@FXML private TableColumn<_Word,String> tbcEnglish;
	@FXML private TableColumn<_Word,String> tbcChinese;
	@FXML private TableColumn<_Word,String> tbcPhonetic;
	@FXML private TableView<_Word> tbvDontKnow=new TableView<_Word>();
	
	public static int number=100;
	public int count=0;
	
	List<Word> vocabularyList=new ArrayList<Word>();
	List<Word> knowList=new ArrayList<Word>();
	List<Word> dontKnowList=new ArrayList<Word>();
	private ObservableList<_Word> _dontKnowList = FXCollections.observableArrayList();
	private Word currentWord,previousWord;
	private _Word _currentWord;
	
	private boolean isFinished=false; 
	
	public void showMessage(AlertType type, String title, String head, String content) {
		Alert alert=new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(head);
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	public void hide() {
		lblChinese.setVisible(false);
		fpButtons.setVisible(false);
		fpButtons.setDisable(true);
	}
	
	public void show() {
		if(previousWord!=null&&!previousWord.getEnglish().equals(lblEnglish.getText())) {
			btnPrevious.setVisible(true);
			btnPrevious.setDisable(false);
		}
		lblChinese.setVisible(true);
		fpButtons.setVisible(true);
		fpButtons.setDisable(false);
	}
	
	public void nextWord() {
		if(!isFinished) {
			int idx=Math.abs(new Random().nextInt() % vocabularyList.size());
			currentWord=vocabularyList.get(idx);
			lblEnglish.setText(currentWord.getEnglish());
			lblPhonetic.setText("["+currentWord.getPhonetic_symbols()+"]");
			lblChinese.setText(currentWord.getChinese());
			hide();
		} else {
			boxRecite.setVisible(false);
			boxRecite.setDisable(true);
			boxTable.setVisible(true);
			boxTable.setDisable(false);
			tbcEnglish.setCellValueFactory(word->word.getValue().getEnglish());
			tbcChinese.setCellValueFactory(word->word.getValue().getChinese());
			tbcPhonetic.setCellValueFactory(word->word.getValue().getPhonetic_symbols());
			tbvDontKnow.setItems(_dontKnowList);
		}
	}
	
	public void readAll( ) throws IOException{
		String fileName = "src/application/College_Grade4.txt";
		String charset = "UTF-8";
		BufferedReader reader = new BufferedReader(
			new InputStreamReader(
				new FileInputStream(fileName), charset)); 
		String line; 
		while ((line = reader.readLine()) != null) { 
			line = line.trim();
			if( line.length() == 0 ) continue;
			int idx = line.indexOf("\t");
			int idx2 = line.indexOf("\t", idx+1);
			vocabularyList.add(new Word(line.substring(0, idx), line.substring(idx+1, idx2), line.substring(idx2+1)));
		}
		reader.close();
	}
	
	public void initialize(URL location, ResourceBundle resources) {
		try {
			readAll();
			for(Word word:vocabularyList) {
				word.show();
			}
			nextWord();
		} catch(IOException e) {
			System.out.println("Read failed!");
			e.printStackTrace();
		}
	}
	
	public void setAmount() {
		TextInputDialog getNumber=new TextInputDialog(Integer.toString(number));
		getNumber.setTitle("设置");
		getNumber.setHeaderText(null);
		getNumber.setContentText("请输入本次背诵的单词数量：");
		Optional<String> result = getNumber.showAndWait();
		try {
			if(result.isPresent()) {
				int tempNum=Integer.parseInt(result.get());
				if(tempNum<5) {
					showMessage(AlertType.ERROR, "提示", null, "不能低于5个！");
					number=5;
					return ;
				}
				if(tempNum>number) {
					isFinished=false;
					boxRecite.setVisible(true);
					boxRecite.setDisable(false);
					boxTable.setVisible(false);
					boxTable.setDisable(true);
					nextWord();
				}
				number=tempNum;
			}
		} catch(Exception e) {
			showMessage(AlertType.ERROR, "提示", null, "输入异常!");
		}
		System.out.println(number);
	}
	
	public void goBack() {
		currentWord=previousWord;
		if(knowList.indexOf(currentWord)!=-1) {
			System.out.println("Find in knowList: "+knowList.indexOf(currentWord));
			vocabularyList.add(currentWord);
			knowList.remove(currentWord);
		} else {
			if(dontKnowList.indexOf(currentWord)!=-1) {
				System.out.println("Find in dontKnowList: "+dontKnowList.indexOf(currentWord));
				vocabularyList.add(currentWord);
				dontKnowList.remove(currentWord);
				_dontKnowList.remove(_currentWord);
			}
		}
		count--;
		lblEnglish.setText(currentWord.getEnglish());
		lblPhonetic.setText("["+currentWord.getPhonetic_symbols()+"]");
		lblChinese.setText(currentWord.getChinese());
		btnPrevious.setVisible(false);
		btnPrevious.setDisable(true);
		lblChinese.setVisible(true);
		fpButtons.setVisible(true);
		fpButtons.setDisable(false);
	}
		
	public void know() {
		btnPrevious.setVisible(true);
		btnPrevious.setDisable(false);
		previousWord=currentWord;
		knowList.add(currentWord);
		vocabularyList.remove(currentWord);
		count++;
		if(count==number) isFinished=true;
		nextWord();
		hide();
	}
	
	public void notSure() {
		btnPrevious.setVisible(true);
		btnPrevious.setDisable(false);
		previousWord=currentWord;
		nextWord();
		hide();
	}
	
	public void dontKnow() {
		btnPrevious.setVisible(true);
		btnPrevious.setDisable(false);
		previousWord=currentWord;
		_currentWord=new _Word(currentWord);
		dontKnowList.add(currentWord);
		_dontKnowList.add(_currentWord);
		vocabularyList.remove(currentWord);
		count++;
		if(count==number) isFinished=true;
		nextWord();
		hide();
	}
	
	public void aboutAuthor(ActionEvent event) {
		showMessage(AlertType.INFORMATION, "关于", "背单词\nVersion 1.3.2\n", "Made by Human_Being_\nMay 21, 2018");
	}
}
