package po;

import java.io.Serializable;

public class WordPo implements Serializable {
	private static final long serialVersionUID = -4391413534831845101L;

	private String word;
	// 倒序
	private String wordRv;
	private int wordLen;
	private int count;
	private String type;
	//单词是否已经在字典中
	private String isInDic;

	public WordPo(String word, int count) {
		super();
		this.word = word;
		this.count = count;
		this.wordLen = word.length();
		StringBuffer stringBuffer = new StringBuffer(word);
		this.wordRv = stringBuffer.reverse().toString();
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getWordLen() {
		return wordLen;
	}

	public void setWordLen(int wordLen) {
		this.wordLen = wordLen;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWordRv() {
		return wordRv;
	}

	public void setWordRv(String wordRv) {
		this.wordRv = wordRv;
	}

	public String getIsInDic() {
		return isInDic;
	}

	public void setIsInDic(String isInDic) {
		this.isInDic = isInDic;
	}

	@Override
	public String toString() {
		
		return this.word + "          " +this.getCount() + "          "+this.getIsInDic();
	}
	
	

}
