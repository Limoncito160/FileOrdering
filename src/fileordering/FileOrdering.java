package fileordering;

/* by Limoncito */
import javax.swing.JOptionPane;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileOrdering {

    public static void main(String[] args) {

        String Title = "File Ordering v1.0 by Limoncito";
        String Path = "";

        input(Title, Path);

    }

    public static int input(String Title, String Path) {

        Path = JOptionPane.showInputDialog(null, "INGRESA LA UBICACIÓN LA CARPETA QUE DESEAS ORDENAR:\nEjemplo: C:\\Users\\Axel\\Desktop", Title, 3);

        if (Path != null) {
            Path = Path.trim();

            if (Path.isEmpty()) {

                JOptionPane.showMessageDialog(null, "ERROR #01: Porfavor ingresa una ubicación.", Title, 0);
                input(Title, Path);

            } else {

                int ConfirmPath = JOptionPane.showConfirmDialog(null, "¿Esta ruta es correcta?\n" + Path + "\n\n¡No uses una carpeta que pueda afectar el funcionamiento\nde tu computadora o de algún un software!");

                if (ConfirmPath == 0) {

                    //CODE FOR 'YES' OPTION
                    search(Title, Path);

                } else if (ConfirmPath == 1) {

                    //CODE FOR 'NO' OPTION
                    input(Title, Path);

                } else if (ConfirmPath == 2) {

                    //CODE FOR 'CANCEL' OPTION
                    System.exit(0);

                }

            }
        }

        return 0;

    }

    public static String search(String Title, String Path) {

        //PATH TO SEARCH
        File CheckPath = new File(Path);

        if (CheckPath.exists()) {

            fileCheck(Title, Path);

        } else {

            JOptionPane.showMessageDialog(null, "ERROR #02: ¡La ruta que ingresaste no ha sido encontrada!\nVerifica tus datos.\n\nRuta ingresada: '" + Path, Title + "'.", 0);
            input(Title, Path);

        }
        return null;
    }

    public static void fileCheck(String Title, String Path) {

        //PATH TO CHECK IS 'Path'
        File folder = new File(Path);
        File[] archives = folder.listFiles();

        if (archives != null) {

            List<String> fileNameList = new ArrayList<>();

            for (File archive : archives) {
                if (archive.isFile()) {

                    String fileName = archive.getName();
                    fileNameList.add(fileName);

                } else if (archive.isDirectory()) {

                    String folderName = archive.getName();
                    fileNameList.add(folderName + "/");

                }
            }

            //CREATE ARRAY'S FOR THE FILE NAME'S AND EXTENSION'S
            String[] fileNameArray = fileNameList.toArray(new String[0]);
            String[] fileReverseArray = new String[fileNameArray.length];
            String[] fileRevExtensionArray = new String[fileNameArray.length];
            String[] fileExtensionArray = new String[fileNameArray.length];

            //AUXILIARY STRING TO READ THE FILE EXTENSION 
            String auxExtension = "";

            StringBuilder stringBuilderVariable = new StringBuilder();

            //BUCLE TO REVERSE THE FILE NAME
            for (int i = 0; i < fileNameArray.length; i++) {

                stringBuilderVariable.setLength(0);
                stringBuilderVariable.append(fileNameArray[i]);
                stringBuilderVariable.reverse();

                fileReverseArray[i] = stringBuilderVariable.toString();

                for (int j = 0; j < fileReverseArray[i].length(); j++) {

                    if (fileReverseArray[i].charAt(j) == '/') {

                        fileExtensionArray[i] = "FOLDER";
                        auxExtension = "";
                        break;

                    } else if (fileReverseArray[i].charAt(j) == '.') {

                        fileRevExtensionArray[i] = auxExtension.toUpperCase();
                        auxExtension = "";
                        break;

                    } else {
                        auxExtension += fileReverseArray[i].charAt(j);
                    }

                }

            }

            //BUCLE TO REVERSE THE 'RevExtension' AND GET THE REAL FILE EXTENSION
            for (int i = 0; i < fileRevExtensionArray.length; i++) {

                stringBuilderVariable.setLength(0);
                stringBuilderVariable.append(fileRevExtensionArray[i]);
                stringBuilderVariable.reverse();

                if (fileRevExtensionArray[i] == null) {
                    fileExtensionArray[i] = ".FOLDER";
                } else {

                    fileExtensionArray[i] = stringBuilderVariable.toString();

                }

            }

            System.out.println("Archivos encontrados: " + fileNameArray.length);

            if (fileNameArray.length == 0) {

                JOptionPane.showMessageDialog(null, "ERROR #02: La ruta seleccionada no contiene archivos para ordenar.", Title, 0);
                input(Title, Path);

            } else {

                createFolder(Title, Path, fileNameArray, fileExtensionArray);

            }

        } else {

            JOptionPane.showMessageDialog(null, "ERROR #02: La ruta seleccionada no contiene archivos para ordenar.", Title, 0);
            input(Title, Path);

        }

    }

    public static void createFolder(String Title, String Path, String[] fileNameArray, String[] fileExtensionArray) {

        //FOLDER CREATION FOR EACH EXTENSION
        String newFolder = Path + "\\";

        for (int i = 0; i < fileNameArray.length; i++) {

            newFolder = newFolder + fileExtensionArray[i];
            File Folder = new File(newFolder);

            if (Folder.exists()) {

                newFolder = Path + "\\";

            } else {

                try {

                    //FOLDER CREATION
                    newFolder = Path + "\\";
                    boolean flag = Folder.mkdir();

                    if (flag) {
                        System.out.println("Se ha creado la carpeta: " + fileExtensionArray[i]);
                    }

                } catch (Exception e) {

                    JOptionPane.showMessageDialog(null, "ERROR #03: No se ha podido crear la carpeta:\n" + e, Title, 0);
                    input(Title, Path);

                }

            }

        }

        moveFiles(Title, Path, fileNameArray, fileExtensionArray);

    }

    public static void moveFiles(String Title, String Path, String[] fileNameArray, String[] fileExtensionArray) {

        //TARGET FILE PATH & TARGET FOLDER
        String targetFolderPath = Path + "\\";
        String sourceFilePath = Path + "\\";

        for (int i = 0; i < fileNameArray.length; i++) {

            sourceFilePath = sourceFilePath + fileNameArray[i];
            targetFolderPath = targetFolderPath + fileExtensionArray[i];

            File targetFolder = new File(targetFolderPath);
            File sourceFile = new File(sourceFilePath);

            if (targetFolder.exists()) {

                try {

                    System.out.println("Moviendo " + fileNameArray[i] + " a la carpeta: " + targetFolderPath);
                    boolean flag = sourceFile.renameTo(new File(targetFolder, fileNameArray[i]));

                    if (flag) {
                        System.out.println("Se ha movido el archivo " + fileNameArray[i] + "a la carpeta de: " + fileExtensionArray[i]);
                    }

                } catch (Exception e) {

                    JOptionPane.showMessageDialog(null, "ERROR #04: No se ha podido mover el archivo a la carpeta destino:\n" + e, Title, 0);
                    input(Title, Path);

                }

            }

            targetFolderPath = Path + "\\";
            sourceFilePath = Path + "\\";

        }

        JOptionPane.showMessageDialog(null, "¡Carpeta ordenada con exito!\nVerifica tus archivos\n\n-By Limoncito\n11/06/2023", Title, 1);

    }
}
