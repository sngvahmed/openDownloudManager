package utills;

import java.io.File;

/**
 * Created by sngv on 02/03/15.
 */
public class Service {
    static public String nameFile(File file) {
        if (!file.exists())
            return file.getAbsolutePath();

        StringBuilder newName = new StringBuilder(file.getAbsolutePath());
        int count = 0;

        String path = file.getAbsolutePath();
        String name = path.substring(path.lastIndexOf('/') + 1, path.length());
        path = path.substring(0, path.lastIndexOf('/') + 1);

        while ((new File(newName.toString())).exists()) {
            count++;
            newName = new StringBuilder(path);
            newName.append(name + "(" + count + ")");
        }
        return newName.toString();
    }
}
