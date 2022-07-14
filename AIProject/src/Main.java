import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
//		Tareq Shannak  1181404
//	 Abd AlRahman Mansour 1182955
public class Main extends Application {
	public static Stage s;
	public static Scene scene;

	@Override
	public void start(Stage primaryStage) {
		try {
			//Lunch the first view
			Parent root = FXMLLoader.load(getClass().getResource("View1.fxml"));
			scene = new Scene(root, 900, 600);
			primaryStage.setScene(scene);
			primaryStage.setTitle("GPS"); // give a title to our program
			primaryStage.getIcons().add(new Image("Icon.png"));
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<Node>[] CostFunc = new ArrayList[20];
	@SuppressWarnings("unchecked")
	public static ArrayList<Integer>[] HeuresticFunc = new ArrayList[20];

	public static void main(String[] args) throws IOException {
		//Read the input file to know the aerial and drive routes between cities
		BufferedReader input = null;
		File file = new File("src//AerialDistancesAndRoutes.txt");
		Pattern notnumber = Pattern.compile("[^0-9]");
		input = new BufferedReader(new FileReader(file));
		for (int i = 0; i < 20; i++) {
			String[] result = input.readLine().split("\\s");
			HeuresticFunc[i] = new ArrayList<Integer>();
			CostFunc[i] = new ArrayList<Node>();
			for (int j = 0; j < 20; j++) {
				if (notnumber.matcher(result[j]).find()) {
					String[] temp = result[j].split("-");
					CostFunc[i].add(new Node(j, Integer.parseInt(temp[0])));
					HeuresticFunc[i].add(Integer.parseInt(temp[1]));
				} else
					HeuresticFunc[i].add(Integer.parseInt(result[j]));
			}
		}
		input.close();
		launch(args);
	}
}
