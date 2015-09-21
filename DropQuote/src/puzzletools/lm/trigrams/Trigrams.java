package puzzletools.lm.trigrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class Trigrams {
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
						String trigram = characters.charAt(i) + "" + characters.charAt(j) + "" + characters.charAt(k);
						trigrams.add(trigram, 1);
					}
				}
			}
			return trigrams;
		} catch (IOException e) {
			return null;
		}
	}
}
