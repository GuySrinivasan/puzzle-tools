import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Multiset;

import puzzletools.lm.trigrams.Trigrams;
import puzzletools.puzzletypes.DropQuote;

public class Runner {
	public static void main(String[] args) throws Exception {
		Trigrams trigrams = new Trigrams();
		Multiset<String> trigramFrequencies = trigrams.readTrigramCounts();
		String characters = "$abcdefghijklmnopqrstuvwxyz";
		for (int i = 0; i < characters.length(); i++) {
			for (int j = 0; j < characters.length(); j++) {
				for (int k = 0; k < characters.length(); k++) {
					String trigram = characters.charAt(i) + "" + characters.charAt(j) + "" + characters.charAt(k);
					int freq = trigramFrequencies.count(trigram);
					if (freq > 0) {
						System.out.println(trigram + "\t" + freq);
					}
				}
			}
		}
		Map<String, Double> freqs = trigrams.convertToFrequencies(trigramFrequencies);
		DropQuote dropQuote = DropQuote.create("Let the punishment be proportionate to the offense.", 20);
		System.out.println(dropQuote.getRows());
		System.out.println(dropQuote.getWordLengths());
		System.out.println(dropQuote.getLetters());

		// in a random order but once for each: choose a square, choose a
		// trigram position, for each possible fill-in of that trigram,
		// multiply current probability by prior frequency, sum up possibles for
		// each letter, normalize, rewrite probability, normalize
		// column. what's normalize column mean? say it started L=0.3333,
		// E=0.3333, H=0.3333 for each space.
		// Now the top becomes L=0.9, E=0.1, T=0.0. Others retain ratio but are
		// normalized to 1-p, fine

		String allLetters = "abcdefghijklmnopqrstuvwxyz$";
		Set<Character> letters = new HashSet<>();
		for (Character c : allLetters.toCharArray()) {
			letters.add(c);
		}
		int R = dropQuote.getRows();
		int C = dropQuote.getColumns();
		@SuppressWarnings("unchecked")
		Distribution<Character>[][] currentSolution = new Distribution[dropQuote.getRows()][dropQuote.getColumns()];
		for (int c = 0; c < C; c++) {
			for (int r = 0; r < R; r++) {
				currentSolution[r][c] = new Runner.Distribution<Character>(dropQuote.getLetters().get(c));
			}
		}

		for (int r = 0; r < R; r++) {
			for (int c = 0; c < C; c++) {
				// left left
				if (c > 1) {
					// TODO: need a wrapper over a DropQuote and a Distribution<Character>[R][C] that lets us get probabilities
					// and maybe holds new ones, like a builder, then normalizes nicely all at once?
					for (String trigram : freqs.keySet()) {
						double prob = 1;
						prob *= currentSolution[r][c-2].get(trigram.charAt(0));
						prob *= currentSolution[r][c-1].get(trigram.charAt(1));
						prob *= currentSolution[r][c-0].get(trigram.charAt(2));
						prob *= freqs.get(trigram);
					}
				}
				// left right
				if (c > 0 && c < c - 1) {

				}
				// right right
				if (c < c - 2) {

				}
			}
		}
	}

	static class Distribution<T> {
		private final Map<T, Double> dist;

		public Distribution(Set<T> elems) {
			dist = new HashMap<>();
			for (T elem : elems) {
				dist.put(elem, 1.0 / elems.size());
			}
		}

		public double get(T elem) {
			return dist.containsKey(elem) ? dist.get(elem) : 0;
		}

		public Distribution(Multiset<T> elems) {
			dist = new HashMap<>();
			for (T elem : elems) {
				dist.put(elem, ((double) elems.count(elem)) / elems.size());
			}
		}
	}
}
