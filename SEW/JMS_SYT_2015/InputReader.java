import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputReader {

	public static String readLine(String input) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println(input + ": ");
		String s = null;

		try {
			input = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return input;
	}
}
