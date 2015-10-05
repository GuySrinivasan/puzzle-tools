package puzzletools.puzzletypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class DropQuote {
	private final int columns;
	private final int rows;
	private final List<Integer> wordLengths;
	private final List<Multiset<Character>> letters;
	private final Optional<Character>[][] known;

	public DropQuote(int rows, int columns, List<Integer> wordLengths, List<Multiset<Character>> letters,
			Optional<Character>[][] known) {
		this.columns = columns;
		this.rows = rows;
		this.wordLengths = wordLengths;
		this.letters = letters;
		this.known = known;
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

	public boolean isKnown(int r, int c) {
		return known[r][c].isPresent();
	}

	public char getKnown(int r, int c) {
		return known[r][c].get();
	}
	
	public void setKnown(int r, int c, Character letter) {
		known[r][c] = Optional.of(letter);
	}

	public static class DropQuoteBuilder {
		List<Multiset<Character>> letters;
		List<Integer> wordLengths;

		public DropQuoteBuilder() {
			letters = new ArrayList<>();
			wordLengths = new ArrayList<>();
		}
		
		public DropQuoteBuilder withColumn(String letters) {
			Multiset<Character> column = HashMultiset.create();
			for (char letter : letters.toCharArray()) {
				column.add(letter);
			}
			this.letters.add(column);
			return this;
		}

		public DropQuoteBuilder withColumn(Character... letters) {
			Multiset<Character> column = HashMultiset.create(Arrays.asList(letters));
			this.letters.add(column);
			return this;
		}

		public DropQuoteBuilder withWordLengths(Integer... lengths) {
			wordLengths.addAll(Arrays.asList(lengths));
			return this;
		}

		public DropQuote build() {
			// public DropQuote(int rows, int columns, List<Integer>
			// wordLengths, List<Multiset<Character>> letters,
			// Optional<Character>[][] known) {
			int columns = letters.size();
			int rows = (wordLengths.stream().reduce(0, (a, b) -> a + b) + wordLengths.size() - 1 + columns - 1)
					/ columns;
			
			@SuppressWarnings("unchecked")
			Optional<Character>[][] known = new Optional[rows][columns];
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < columns; c++) {
					known[r][c] = Optional.empty();
				}
			}
			int cumulativeLength = 0;
			for (int length : wordLengths) {
				cumulativeLength += length;
				int r = cumulativeLength / columns;
				int c = cumulativeLength % columns;
				if (r < rows && c < columns) {
					known[r][c] = Optional.of('$');
					cumulativeLength += 1;
				}
			}
			while (cumulativeLength % columns != 0) {
				known[rows - 1][cumulativeLength % columns] = Optional.of('$');
				cumulativeLength += 1;
			}
			
			return new DropQuote(rows, columns, wordLengths, letters, known);
		}

		public DropQuoteBuilder withColumns(String... columns) {
			for (String column : columns) {
				withColumn(column);
			}
			return this;
		}
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
		int rows = row + 1;
		@SuppressWarnings("unchecked")
		Optional<Character>[][] known = new Optional[rows][columns];
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				known[r][c] = Optional.empty();
			}
		}
		int cumulativeLength = 0;
		for (int length : wordLengths) {
			cumulativeLength += length;
			int r = cumulativeLength / columns;
			int c = cumulativeLength % columns;
			if (r < rows && c < columns) {
				known[r][c] = Optional.of('$');
				cumulativeLength += 1;
			}
		}
		while (cumulativeLength % columns != 0) {
			known[rows - 1][cumulativeLength % columns] = Optional.of('$');
			cumulativeLength += 1;
		}
		return new DropQuote(rows, columns, wordLengths, letters, known);
	}
}
