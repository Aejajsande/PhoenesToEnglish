import java.util.*;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import java.io.*;

public class EnglishTranslation {
	String sen;
	List<Integer> bool = new ArrayList<Integer>();

	int initial = 1;
	int fin = 2;

	public static void main(String[] args) {
		EnglishTranslation et = new EnglishTranslation();

		int maxLength = 0;
		String input = et.getInputString();
		Map stringMap = new HashMap();
		try {
			Scanner scan = new Scanner(new FileReader("Dictionary.txt"));

			while (scan.hasNextLine()) {
				String s = scan.nextLine();

				String[] arr = s.split(" ");
				StringBuffer buf = new StringBuffer("");
				for (int i = 1; i < arr.length; i++) {

					buf = buf.append(arr[i]);
					if (buf.length() > maxLength) {
						maxLength = buf.length();
					}

				}

				if (stringMap.containsKey(buf.toString())) {
					stringMap.put("*" + buf.toString(), arr[0]);
				} else {
					stringMap.put(buf.toString(), arr[0]);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		et.sen = " " + input.replace(" ", "");

		et.bool = et.newSubString(et.sen, stringMap);

		String ans = et.createEnglishTranslation(et.bool, stringMap, et.sen, maxLength);
		System.out.println(ans);
	}

	public String getInputString() {
		String input = "";
		try {
			Scanner scan = new Scanner(new FileReader("input.txt"));
			while (scan.hasNextLine()) {
				input = input + scan.nextLine();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return input;
	}

	public List newSubString(String sen, Map stringMap) {
		List<Integer> bool = new ArrayList<Integer>(sen.length());

		bool.add(0);

		while (fin <= sen.length()) {
			String word = sen.substring(initial, fin);

			if (stringMap.containsKey(word) && bool.contains(initial - 1)) {
				bool.add(fin - 1);
				initial = fin;
				fin++;
			}

			else {

				if (!(checkAllPrevious(bool, sen, stringMap))) {
					fin++;
				}
			}

		}
		return bool;
	}

	public Boolean checkAllPrevious(List<Integer> bool, String sen, Map stringMap) {
		for (int i = bool.size() - 1; i >= 0; i--) {

			if (stringMap.containsKey(sen.substring(bool.get(i) + 1, fin))) {
				bool.add(fin - 1);
				initial = fin;
				fin++;
				return true;
			}

		}
		return false;
	}

	public String createEnglishTranslation(List<Integer> bool, Map stringMap, String sen, int maxLength) {
		String ans = "";

		int i = bool.size() - 1;
		int initialIndex = i - 1;
		int finalIndex = i;
		while (initialIndex >= 0) {

			if (stringMap.containsKey(sen.substring(bool.get(initialIndex) + 1, bool.get(finalIndex) + 1))) {
				Map<String, Integer> isWord = checkMoreWords(initialIndex, finalIndex, stringMap, maxLength);
				if (isWord.get("result") == 1) {
					initialIndex = isWord.get("index");
					ans = stringMap.get(sen.substring(bool.get(initialIndex) + 1, bool.get(finalIndex) + 1)) + " "
							+ ans;
					finalIndex = initialIndex;
					initialIndex--;
				} else {
					ans = stringMap.get(sen.substring(bool.get(initialIndex) + 1, bool.get(finalIndex) + 1)) + " "
							+ ans;
					finalIndex = initialIndex;
					initialIndex--;
				}

			} else {
				initialIndex--;
			}
		}
		return ans;
	}

	public Map checkMoreWords(int initialIndex, int finalIndex, Map stringMap, int maxLength) {
		Map isWord = new HashMap();
		isWord.put("result", 0);
		int wordIndex = 0;
		while (((bool.get(finalIndex) - bool.get(initialIndex)) <= maxLength) && initialIndex > 0) {
			initialIndex--;
			if (stringMap.containsKey(sen.substring(bool.get(initialIndex) + 1, bool.get(finalIndex) + 1))) {
				isWord.put("result", 1);
				isWord.put("index", initialIndex);
			}

		}
		return isWord;
	}
}
