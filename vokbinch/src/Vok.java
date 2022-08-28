
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Vok {

	String object_a;
	String object_b;

	boolean learned;
	int count;

	public Vok(String container) {
		//
		System.out.println(container);
		if (container.split(" ").length > 1l) {
			object_a = container.split(" ")[0];
			object_b = container.split(" ")[1];
		} else {
			object_a = container.split(" ")[0];
			object_b = container.split(" ")[1];
			learned = Boolean.parseBoolean(container.split(" ")[2]);
			count = Integer.parseInt(container.split(" ")[1]);
		}
	}

	public static void ConvertIntoVokObject(ArrayList<Vok> fileVoks, String Name, String a, String b)
			throws IOException {
		File vokobject = new File("lists/" + Name);
		vokobject.createNewFile();
		String content = "HEADER [" + a + "][" + b + "]\n";

		for (Vok v : fileVoks) {
			content += v.object_a + " " + v.object_b + " " + v.learned + " " + v.count + "\n";
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(vokobject));
		writer.write(content);
		writer.close();

	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "[" + object_a + "] [" + object_b + "] learned:" + learned + " count:" + count;
	}

	public String toStringFormatted() {
		// TODO Auto-generated method stub
		return object_a + " " + object_b + " " + learned + " " + count + "\n";
	}

	public static ArrayList<Vok> buildLearningSet(File lernset) {
		ArrayList<Vok> ret = new ArrayList<Vok>();
		Scanner s;
		try {
			s = new Scanner(lernset);

			while (s.hasNextLine()) {
				String current = s.nextLine();
				if (!current.startsWith("HEADER"))
					ret.add(new Vok(current));
			}
			s.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;

	}

	public static void updateVokFile(ArrayList<Vok> failed, String name, String a, String b) {
		File vokobject = new File("lists/" + name);
		
		System.out.println(vokobject.getAbsolutePath());
		String content = "HEADER [" + a + "][" + b + "]\n";

		for (Vok v : failed) {
			content += v.toStringFormatted();
		}
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(vokobject));

			writer.write(content);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
