package application;

import javafx.beans.property.SimpleStringProperty;

public class _Word {
	private final SimpleStringProperty english;
	private final SimpleStringProperty chinese;
	private final SimpleStringProperty phonetic_symbols;
	
	_Word(Word word) {
		english=new SimpleStringProperty(word.getEnglish());
		chinese=new SimpleStringProperty(word.getChinese());
		phonetic_symbols=new SimpleStringProperty("/"+word.getPhonetic_symbols()+"/");
	}
	
	public SimpleStringProperty getEnglish() {
		return english;
	}
	
	public void setEnglish(String english) {
		this.english.set(english);
	}
	
	public SimpleStringProperty getChinese() {
		return chinese;
	}
	
	public void setChinese(String chinese) {
		this.chinese.set(chinese);
	}
	
	public SimpleStringProperty getPhonetic_symbols() {
		return phonetic_symbols;
	}
	
	public void setPhonetic_symbols(String phonetic_symbols) {
		this.phonetic_symbols.set("/"+phonetic_symbols+"/");
	}
	
}
