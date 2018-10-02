import java.io.*;
import java.util.ArrayList;

public class DataBaseSavedStories {

    private static DataBaseSavedStories instance;

    private static ArrayList<String> storiesArr = new ArrayList<>();
    private static ArrayList<String> fileStoriesBase = new ArrayList<>();

    private DataBaseSavedStories() {
    }

    public static DataBaseSavedStories getInstance() {
        if (instance == null) instance = new DataBaseSavedStories();
        return instance;
    }

    public void addStories(String story) {
        storiesArr.add(story);
    }

    public void savedStoriesArr() {
        try (FileWriter writer = new FileWriter("database/stories_database.txt", true)) {
            for (String story : storiesArr) writer.write(story + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("database/stories_database.txt"))) {
            String line;
            while ((line = br.readLine()) != null) fileStoriesBase.add(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean contains(String story) {
        return fileStoriesBase.contains(story);
    }

}
