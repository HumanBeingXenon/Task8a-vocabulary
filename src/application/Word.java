package application;

public class Word {
	private String english;
	private String chinese;
	private String phonetic_symbols;
	
	Word(String english, String chinese, String phonetic_symbols){
		this.english=english;
		this.chinese=chinese;
		this.phonetic_symbols=phonetic_symbols;
	}

	public String getEnglish() {
		return english;
	}

	public void setEnglish(String english) {
		this.english = english;
	}

	public String getChinese() {
		return chinese;
	}

	public void setChinese(String chinese) {
		this.chinese = chinese;
	}

	public String getPhonetic_symbols() {
		return phonetic_symbols;
	}

	public void setPhonetic_symbols(String phonetic_symbols) {
		this.phonetic_symbols = phonetic_symbols;
	}
	
	public void show() {
		System.out.println(english+"\t"+chinese+"\t"+phonetic_symbols);
	}
}
