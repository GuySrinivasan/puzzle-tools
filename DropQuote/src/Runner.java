import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;

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
		// DropQuote dropQuote = DropQuote.create("Let the punishment be
		// proportionate to the offense.", 20);
		// DropQuote dropQuote = DropQuote.create("Let the punishment be
		// proportionate to the offense.", 12);
		DropQuote dropQuote = new DropQuote.DropQuoteBuilder()
				.withColumns("ntioi", "cnvhaq", "ieteut", "aooa", "lrntnu", "lsice", "swcey", "hea", "iiiws", "csnitm",
						"toph", "trdth", "ihdhe", "ssitai", "hntsn", "geigo", "tfv", "hatoea", "beecr", "qyoin", "ugu",
						"igtsa")
				.withWordLengths(3, 6, 5, 5, 6, 2, 4, 4, 4, 2, 1, 7, 12, 4, 3, 10, 5, 2, 5, 8, 10).build();
		// DropQuote dropQuote = new
		// DropQuote.DropQuoteBuilder().withColumn("oaooot").withColumn("ntwp")
		// .withColumn("ipoett").withColumn("skihhr").withColumn("neekoe").withColumn("mtefg").withColumn("tpnh")
		// .withColumn("eatyos").withColumn("aooth").withColumn("eruy").withColumn("yhreo").withColumn("asuot")
		// .withColumn("ukrth").withColumn("idfi").withColumn("vdhnr").withColumn("ootgas").withColumn("sodlm")
		// .withColumn("ic").withColumn("dnttga").withColumn("nteohr")
		// .withWordLengths(3, 2, 3, 6, 4, 5, 3, 4, 8, 4, 4, 4, 3, 7, 7, 2, 4,
		// 3, 3, 2, 4, 3, 4, 2, 3, 4).build();
		// dropQuote.setKnown(0, 1, 'n');
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
				if (dropQuote.isKnown(r, c)) {
					currentSolution[r][c] = new Runner.Distribution<Character>(
							Sets.newHashSet(dropQuote.getKnown(r, c)));
				} else {
					currentSolution[r][c] = new Runner.Distribution<Character>(dropQuote.getLetters().get(c));
				}
			}
		}

		Distribution<Character>[][] trigrammedSolution = applyTrigramProbabilities(freqs, dropQuote, letters, R, C,
				currentSolution);

		// now take into account full word probabilities
		// 1. find each word's "without trigram bias" probability, then
		// 2. choose a reweight? probability for unknown? no, ignore TODO
		// 3. for each column, multiply probs by new distribution
		// 4. normalize
		// 5. goto 3 and 4 a bunch of times

		Map<Integer, Set<String>> allWords = trigrams.readAllWords();
		List<Integer> wordLengths = dropQuote.getWordLengths();
		Map<Integer, Map<String, Double>> normalizedWordProbabilities = trigrams.computeNormalizedWordProbabilities();

		@SuppressWarnings("unchecked")
		Distribution<Character>[][] newSolution = new Distribution[R][C];
		@SuppressWarnings("unchecked")
		Runner.Distribution.DistributionBuilder<Character>[][] builders = new Runner.Distribution.DistributionBuilder[R][C];
		for (int r = 0; r < R; r++) {
			for (int c = 0; c < C; c++) {
				builders[r][c] = new Runner.Distribution.DistributionBuilder<>(letters);
				if (trigrammedSolution[r][c].get('$') > 1.0 - 1e-9) {
					builders[r][c].withIncrement('$', 1.0);
				}
			}
		}
		int startPos = 0;
		for (int wordIndex = 0; wordIndex < wordLengths.size(); wordIndex++) {
			int wordLength = wordLengths.get(wordIndex);
			for (String word : allWords.get(wordLength)) {
				if (word.equals("$one$")) {
					int x = 0;
					x++;
				}
				double prob = normalizedWordProbabilities.get(wordLength).get(word);
				for (int offset = 0; offset < wordLength; offset++) {
					int r = (startPos + offset) / C;
					int c = (startPos + offset) % C;
					prob *= trigrammedSolution[r][c].get(word.charAt(offset + 1));
				}
				for (int offset = 0; offset < wordLength; offset++) {
					int r = (startPos + offset) / C;
					int c = (startPos + offset) % C;
					builders[r][c].withIncrement(word.charAt(offset + 1), prob);
				}
			}
			startPos += wordLength + 1;
		}
		for (int r = 0; r < R; r++) {
			for (int c = 0; c < C; c++) {
				newSolution[r][c] = builders[r][c].build();
			}
		}

		applyConstraints(dropQuote, R, C, newSolution);

		chooseAndPrintSolution(dropQuote, R, C, newSolution);
	}

	private static void chooseAndPrintSolution(DropQuote dropQuote, int R, int C,
			Distribution<Character>[][] solution) {
		char[][] bsSolution = new char[R][C];
		for (int c = 0; c < C; c++) {
			final int column = c;
			List<IndexChoice<Character>> choices = new ArrayList<>();
			for (int r = 0; r < R; r++) {
				for (Character elem : solution[r][c].dist.keySet()) {
					if (solution[r][c].get(elem) > 1e-12) {
						IndexChoice<Character> choice = new IndexChoice<>(elem, r);
						choices.add(choice);
						System.out.println("Adding choice for column " + c + ": " + choice.elem + " at row "
								+ choice.index + " with weight " + solution[r][c].get(choice.elem));
					} else {
						System.out.println("Not adding choice for column " + c + ": " + elem + " at row " + r
								+ " with weight " + solution[r][c].get(elem));
					}
				}
			}
			Set<Integer> indices = new HashSet<>();
			for (int r = 0; r < R; r++) {
				indices.add(r);
			}
			Multiset<Character> columnLetters = HashMultiset.create(dropQuote.getLetters().get(c));
			while (!indices.isEmpty()) {
				System.out.println("column " + c + " indices " + indices);
				IndexChoice<Character> choice = choices.stream().filter(ch -> indices.contains(ch.index))
						.sorted((c1, c2) -> -Double.compare(solution[c1.index][column].get(c1.elem),
								solution[c2.index][column].get(c2.elem)))
						.findFirst().get();
				bsSolution[choice.index][c] = choice.elem;
				indices.remove(choice.index);
				columnLetters.remove(choice.elem, 1);
				choices.stream().filter(ch -> indices.contains(ch.index)).filter(ch -> ch.elem == choice.elem)
						.forEach(ch -> solution[ch.index][column].multiply(ch.elem,
								columnLetters.count(ch.elem) / ((double) columnLetters.count(ch.elem) + 1)));
			}
		}

		for (int r = 0; r < R; r++) {
			for (int c = 0; c < C; c++) {
				System.out.print(bsSolution[r][c]);
			}
			System.out.println();
		}
	}

	private static Distribution<Character>[][] applyTrigramProbabilities(Map<String, Double> freqs, DropQuote dropQuote,
			Set<Character> letters, int R, int C, Distribution<Character>[][] currentSolution) {
		@SuppressWarnings("unchecked")
		Distribution<Character>[][] newSolution = new Distribution[R][C];
		for (int r = 0; r < R; r++) {
			for (int c = 0; c < C; c++) {
				Runner.Distribution.DistributionBuilder<Character> builder = new Runner.Distribution.DistributionBuilder<>(
						letters);
				int arm2 = r - (c + C) / C + (c - 2 + C) / C;
				int acm2 = (c - 2 + C) % C;
				int arm1 = r - (c + C) / C + (c - 1 + C) / C;
				int acm1 = (c - 1 + C) % C;
				int arp1 = r - (c + C) / C + (c + 1 + C) / C;
				int acp1 = (c + 1 + C) % C;
				int arp2 = r - (c + C) / C + (c + 2 + C) / C;
				int acp2 = (c + 2 + C) % C;
				// left left
				if (c >= 2 || r >= 1) {
					for (String trigram : freqs.keySet()) {
						double prob = 1.0;
						prob *= currentSolution[arm2][acm2].get(trigram.charAt(0));
						prob *= currentSolution[arm1][acm1].get(trigram.charAt(1));
						prob *= currentSolution[r][c].get(trigram.charAt(2));
						prob *= freqs.get(trigram);
						builder.withIncrement(trigram.charAt(2), prob);
					}
				}
				// left right
				if ((c >= 1 || r >= 1) && (c < C - 1 || r < R - 1)) {
					for (String trigram : freqs.keySet()) {
						double prob = 1.0;
						prob *= currentSolution[arm1][acm1].get(trigram.charAt(0));
						prob *= currentSolution[r][c].get(trigram.charAt(1));
						prob *= currentSolution[arp1][acp1].get(trigram.charAt(2));
						prob *= freqs.get(trigram);
						builder.withIncrement(trigram.charAt(1), prob);
					}
				}
				// right right
				if (c < C - 2 || r < R - 1) {
					for (String trigram : freqs.keySet()) {
						double prob = 1.0;
						prob *= currentSolution[r][c].get(trigram.charAt(0));
						prob *= currentSolution[arp1][acp1].get(trigram.charAt(1));
						prob *= currentSolution[arp2][acp2].get(trigram.charAt(2));
						prob *= freqs.get(trigram);
						builder.withIncrement(trigram.charAt(0), prob);
					}
				}
				Distribution<Character> distribution = builder.build();
				System.out.println("Row " + r + ", column " + c + ": " + distribution);
				newSolution[r][c] = distribution;
			}
		}

		applyConstraints(dropQuote, R, C, newSolution);
		return newSolution;
	}

	private static void applyConstraints(DropQuote dropQuote, int R, int C, Distribution<Character>[][] newSolution) {
		// do some bs normalization to get probabilities for each character to
		// sum to #instances in a column
		for (int i = 0; i < 25; i++) {
			for (int c = 0; c < C; c++) {
				for (char elem : dropQuote.getLetters().get(c).elementSet()) {
					double sum = 0;
					for (int r = 0; r < R; r++) {
						sum += newSolution[r][c].get(elem);
					}
					for (int r = 0; r < R; r++) {
						newSolution[r][c].multiply(elem, dropQuote.getLetters().get(c).count(elem) / sum);
					}
				}
			}
			for (int r = 0; r < R; r++) {
				for (int c = 0; c < C; c++) {
					newSolution[r][c].normalize();
				}
			}
		}
	}

	static class IndexChoice<T> {
		public IndexChoice(T elem, int index) {
			this.elem = elem;
			this.index = index;
		}

		public T elem;
		public int index;
	}

	static class Distribution<T> {
		private final Map<T, Double> dist;

		@Override
		public String toString() {
			return dist.toString();
		}

		public void multiply(T elem, double m) {
			dist.put(elem, dist.get(elem) * m);
		}

		public void normalize() {
			double sum = 0;
			for (double value : dist.values()) {
				sum += value;
			}
			for (T elem : dist.keySet()) {
				dist.put(elem, dist.get(elem) / sum);
			}
		}

		public Distribution(Set<T> elems) {
			dist = new HashMap<>();
			for (T elem : elems) {
				dist.put(elem, 1.0 / elems.size());
			}
		}

		public Distribution(Multiset<T> elems) {
			dist = new HashMap<>();
			for (T elem : elems) {
				dist.put(elem, ((double) elems.count(elem)) / elems.size());
			}
		}

		public Distribution(Map<T, Double> dist) {
			this.dist = new HashMap<>();
			this.dist.putAll(dist);
		}

		public double get(T elem) {
			return dist.containsKey(elem) ? dist.get(elem) : 0;
		}

		public static class DistributionBuilder<T> {
			private final Map<T, Double> dist;

			public DistributionBuilder(Set<T> elems) {
				dist = new HashMap<>();
				for (T elem : elems) {
					dist.put(elem, 0.0);
				}
			}

			public DistributionBuilder<T> withIncrement(T elem, double increment) {
				dist.put(elem, dist.get(elem) + increment);
				return this;
			}

			public DistributionBuilder<T> withIncrements(Map<T, Double> increments) {
				for (T elem : increments.keySet()) {
					dist.put(elem, dist.get(elem) + increments.get(elem));
				}
				return this;
			}

			public Distribution<T> build() {
				Distribution<T> built = new Distribution<T>(dist);
				built.normalize();
				return built;
			}
		}
	}
}
