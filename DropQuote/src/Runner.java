import java.util.Map;

import com.google.common.collect.Multiset;

import puzzletools.lm.trigrams.Trigrams;

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
		
	}
}
