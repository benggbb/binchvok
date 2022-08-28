import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class binchVok {
	static ArrayList<Vok> FileVoks = new ArrayList<Vok>();
	static HashMap<String, String> Props;
	static String name = "";
	static String type_a, type_b;
	static final int LEFT = 0, FAILED = 1, STAGE_ONE = 2, STAGE_TWO = 3;
	static ArrayList<Vok> left;
	static ArrayList<Vok> failed;
	static ArrayList<Vok> stageone;
	static ArrayList<Vok> learned;

	public static void main(String[] args) {

		Scanner cin = new Scanner(System.in);

		Props = Utils.GetProps(args);
		if (Props.containsKey("-p")) {
			File file = new File(Props.get("-p"));
			try {
				Scanner filescanner = new Scanner(file);
				while (filescanner.hasNextLine())
					FileVoks.add(new Vok(filescanner.nextLine()));
				name = cin.nextLine();
				System.out.print(
						"first element of new List:[" + FileVoks.get(0) + "]\n" + "what type is the first element\n->");

				type_a = cin.nextLine();
				System.out.print("what type is the second element\n->");
				type_b = cin.nextLine();

				Vok.ConvertIntoVokObject(FileVoks, name, type_a, type_b);
			} catch (FileNotFoundException e) {
				System.out.println("[ERR]" + e);
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		File file = new File("lists");
		System.out.println("Chooce set to learn");
		for (int i = 0; i < file.list().length; i++) {
			System.out.println(i + ") " + file.list()[i]);
		}
		System.out.print("->");
		int chooce = cin.nextInt();

		File vokset = new File("lists/" + file.list()[chooce]);
		vokShell(vokset, cin);

	}

	static void vokShell(File lernset, Scanner cin) {
		if (name.isEmpty() || name.isBlank())
			name =  lernset.getName();
		System.out.println("[Name]"+name);
		left = Vok.buildLearningSet(lernset);
		failed = new ArrayList<Vok>();
		stageone = new ArrayList<Vok>();
		learned = new ArrayList<Vok>();
		while (left.size() > 0 || failed.size() > 0 || stageone.size() > 0) {

			Vok current[] = new Vok[3];
			for (int i = 0; i < current.length; i++) {
				if (left.size() > 0) {
					System.out.println(left.size());
					int rand = left.size() > 1 ? new Random().nextInt(left.size() - 1) : 0;
					if (i == 2 && failed.size() > 0)
						current[2] = failed.remove(0);
					else
						current[i] = left.remove(rand);
				} else if (failed.size() > 0) {
					int rand = failed.size() > 1 ? new Random().nextInt(failed.size() - 1) : 0;
					if (i == 2 && stageone.size() > 0)
						current[2] = stageone.remove(0);
					else
						current[i] = failed.remove(rand);
				} else if (stageone.size() > 0) {
					int rand = stageone.size() > 1 ? new Random().nextInt(stageone.size() - 1) : 0;
					if (i == 2 && learned.size() > 0)
						current[2] = learned.remove(0);
					else
						current[i] = stageone.remove(rand);
				}
			}
			System.out.println(current[0] + "\n" + current[1] + "\n" + current[2]);
			for (Vok k : current) {
				System.out.print("object A ->" + k.object_a + "\nobject b ->");
				if (cin.next().equals(k.object_b)) {
					correct(k);
				} else {
					wrong(k, cin);
				}
			}
		}
		stageone.addAll(learned);
		failed.addAll(stageone);
		ArrayList<Vok> upgraded = failed;
		Vok.updateVokFile(upgraded, name, type_a, type_b);
		System.out.println("You have succesfully learned every vokabulary of this set");
	}

	static void correct(Vok k) {
		System.out.println("congrats! \n" + k.object_a + "->" + k.object_b);
		switch (k.count) {
		case LEFT:
		case FAILED:
			k.count = STAGE_ONE;
			stageone.add(k);
			break;
		case STAGE_ONE:
			k.count = STAGE_TWO;
			learned.add(k);
			break;
		case STAGE_TWO:
			k.count = STAGE_TWO;
			k.learned = true;
			learned.add(k);
			break;
		}
	}

	static void wrong(Vok k, Scanner cin) {

		System.out.println("WRONG! \n" + k.object_a + "->" + k.object_b);
		System.out.print("Were you correct [y/n]");
		if (cin.next().toLowerCase().equals("y")) {

			correct(k);

		} else {
			System.out.print("rewrite object b->");
			String correct = cin.next();
			while (!correct.equals(k.object_b)) {
				correct = cin.next();
			}
			switch (k.count) {
			case LEFT:
			case FAILED:
				k.count = FAILED;
				failed.add(k);
				break;
			case STAGE_ONE:
				k.count = FAILED;
				failed.add(k);
				break;

			case STAGE_TWO:
				k.count = STAGE_ONE;
				stageone.add(k);
				break;
			}
		}
		cin.close();
	}
}
