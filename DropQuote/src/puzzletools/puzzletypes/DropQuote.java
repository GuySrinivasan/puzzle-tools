package puzzletools.puzzletypes;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class DropQuote {
	private final int columns;
	private final int rows;
	private final List<Integer> wordLengths;
	private final List<Multiset<Character>> letters;

	public DropQuote(int rows, int columns, List<Integer> wordLengths, List<Multiset<Character>> letters) {
		this.columns = columns;
		this.rows = rows;
		this.wordLengths = wordLengths;
		this.letters = letters;
	}

	public int getColumns() {
		return columns;
	}

	public int getRows() {
		return rows;
	}

	public List<Integer> getWordLengths() {
		return wordLengths;
	}

	public List<Multiset<Character>> getLetters() {
		return letters;
	}
	
	public static DropQuote create(String quote, int columns) {
		String[] words = quote.toLowerCase().replaceAll("[^a-z ]", "").split(" ");
		List<Multiset<Character>> letters = new ArrayList<>();
		for (int i = 0; i < columns; i++) {
			letters.add(HashMultiset.create());
		}
		int row = 0;
		int column = 0;
		List<Integer> wordLengths = new ArrayList<>();
		for (int i = 0; i < words.length; i++) {
			// add word
			wordLengths.add(words[i].length());
			for (int j = 0; j < words[i].length(); j++) {
				if (column >= columns) {
					row++;
					column = 0;
				}
				letters.get(column).add(words[i].charAt(j));
				column++;
			}
			if (i != words.length - 1) {
				// add space
				if (column >= columns) {
					row++;
					column = 0;
				}
				column++;
			}
		}
		return new DropQuote(row + 1, columns, wordLengths, letters);
	}
}
