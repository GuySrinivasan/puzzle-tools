package puzzletools.lm.trigrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class Trigrams {
	private Multiset<String> trigramCounts = null;
	private Multiset<String> bigramCounts = null;
	private Multiset<String> unigramCounts = null;
	private Map<Integer, Set<String>> allWords = null;

	public Map<Integer, Set<String>> readAllWords() {
		if (allWords == null) {
			try {
				InputStream stream = this.getClass().getResourceAsStream("/puzzletools/lm/count_1w.txt");
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
				Map<Integer, Set<String>> words = new HashMap<>();
				for (int i = 1; i < 50; i++) {
					words.put(i, new HashSet<>());
				}
				String line = null;
				while ((line = reader.readLine()) != null) {
					String[] lineParts = line.split("\t");
					if (lineParts.length != 2) {
						continue;
					}
					String word = "$" + lineParts[0] + "$";
					words.get(word.length() - 2).add(word);
				}
				allWords = words;
			} catch (IOException e) {
				return null;
			}
		}
		return allWords;
	}

	public Map<Integer, Map<String, Double>> computeNormalizedWordProbabilities() {
		try {
			InputStream stream = this.getClass().getResourceAsStream("/puzzletools/lm/count_1w.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			Map<Integer, Map<String, Double>> unnormalized = new HashMap<>();
			for (int i = 1; i <= 50; i++) {
				unnormalized.put(i, new HashMap<>());
			}

			Map<String, Double> trigramProb = convertToFrequencies(readTrigramCounts());
			Map<String, Double> bigramProb = convertToFrequencies(readBigramCounts());
			Map<String, Double> unigramProb = convertToFrequencies(readUnigramCounts());

			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] lineParts = line.split("\t");
				if (lineParts.length != 2) {
					continue;
				}
				String word = "$" + lineParts[0] + "$";
				long frequency = Long.parseLong(lineParts[1]);
				double storedFreq = frequency / 10000.0;
//				for (int i = 2; i < word.length(); i++) {
//					storedFreq /= trigramProb.get(word.substring(i - 2, i + 1));
//				}
//				for (int i = 2; i < word.length() - 1; i++) {
//					storedFreq *= bigramProb.get(word.substring(i - 1, i + 1));
//				}
//				for (int i = 2; i < word.length() - 2; i++) {
//					storedFreq /= unigramProb.get(word.substring(i - 0, i + 1));
//				}
				unnormalized.get(word.length() - 2).put(word, storedFreq);
			}
			for (int wordLength = 1; wordLength < 25; wordLength++) {
				Map<String, Double> unnormalizedDist = unnormalized.get(wordLength);
				if (unnormalizedDist.size() == 0) {
					continue;
				}
				double total = 0;
				for (double freq : unnormalizedDist.values()) {
					total += freq;
				}
				for (String word : unnormalizedDist.keySet()) {
					unnormalizedDist.put(word, unnormalizedDist.get(word) / total);
				}
			}
			return unnormalized;
		} catch (IOException e) {
			return null;
		}
	}

	public Map<String, Double> convertToFrequencies(Multiset<String> counts) {
		Map<String, Double> freqs = new HashMap<>();
		double total = 0;
		for (String key : counts.elementSet()) {
			freqs.put(key, ((double) counts.count(key)) / counts.size());
			total += freqs.get(key);
		}
		System.out.println(total);
		return freqs;
	}

	public Multiset<String> readTrigramCounts() {
		if (trigramCounts == null) {
			try {
				InputStream stream = this.getClass().getResourceAsStream("/puzzletools/lm/count_1w.txt");
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
				Multiset<String> trigrams = HashMultiset.create();
				String line = null;
				while ((line = reader.readLine()) != null) {
					String[] lineParts = line.split("\t");
					if (lineParts.length != 2) {
						continue;
					}
					String word = "$" + lineParts[0] + "$";
					long frequency = Long.parseLong(lineParts[1]);
					int storedFreq = (int) (frequency / 10000);
					for (int i = 2; i < word.length(); i++) {
						trigrams.add(word.substring(i - 2, i + 1), storedFreq);
					}
				}
				String characters = "$abcdefghijklmnopqrstuvwxyz";
				for (int i = 0; i < characters.length(); i++) {
					for (int j = 0; j < characters.length(); j++) {
						for (int k = 0; k < characters.length(); k++) {
							String trigram = characters.charAt(i) + "" + characters.charAt(j) + ""
									+ characters.charAt(k);
							trigrams.add(trigram, 1);
						}
					}
				}
				trigramCounts = trigrams;
			} catch (IOException e) {
				return null;
			}
		}
		return trigramCounts;
	}

	public Multiset<String> readBigramCounts() {
		if (bigramCounts == null) {
			try {
				InputStream stream = this.getClass().getResourceAsStream("/puzzletools/lm/count_1w.txt");
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
				Multiset<String> bigrams = HashMultiset.create();
				String line = null;
				while ((line = reader.readLine()) != null) {
					String[] lineParts = line.split("\t");
					if (lineParts.length != 2) {
						continue;
					}
					String word = "$" + lineParts[0] + "$";
					long frequency = Long.parseLong(lineParts[1]);
					int storedFreq = (int) (frequency / 10000);
					for (int i = 1; i < word.length(); i++) {
						bigrams.add(word.substring(i - 1, i + 1), storedFreq);
					}
				}
				String characters = "$abcdefghijklmnopqrstuvwxyz";
				for (int i = 0; i < characters.length(); i++) {
					for (int j = 0; j < characters.length(); j++) {
						String bigram = characters.charAt(i) + "" + characters.charAt(j);
						bigrams.add(bigram, 1);
					}
				}
				bigramCounts = bigrams;
			} catch (IOException e) {
				return null;
			}
		}
		return bigramCounts;
	}

	public Multiset<String> readUnigramCounts() {
		if (unigramCounts == null) {
			try {
				InputStream stream = this.getClass().getResourceAsStream("/puzzletools/lm/count_1w.txt");
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
				Multiset<String> unigrams = HashMultiset.create();
				String line = null;
				while ((line = reader.readLine()) != null) {
					String[] lineParts = line.split("\t");
					if (lineParts.length != 2) {
						continue;
					}
					String word = "$" + lineParts[0] + "$";
					long frequency = Long.parseLong(lineParts[1]);
					int storedFreq = (int) (frequency / 10000);
					for (int i = 0; i < word.length(); i++) {
						unigrams.add(word.substring(i - 0, i + 1), storedFreq);
					}
				}
				String characters = "$abcdefghijklmnopqrstuvwxyz";
				for (int i = 0; i < characters.length(); i++) {
					String unigram = characters.charAt(i) + "";
					unigrams.add(unigram, 1);
				}
				unigramCounts = unigrams;
			} catch (IOException e) {
				return null;
			}
		}
		return unigramCounts;
	}
}
