import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class PhysiciansHelper {
    public ArrayList<String> symptoms = new ArrayList<>();
    public static Map<String, ArrayList<String>> symptomIllnesses = new HashMap<>();

    /**
     * loads in data from the txt file
     */
    public void loadFile(){
        try {
            File myObj = new File("data.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String illness = data.split(": ")[0];
                String[] symptoms = data.split(": ")[1].split(", ");

                for (String symptom : symptoms) {
                    if (symptomIllnesses.containsKey(symptom)){
                        symptomIllnesses.get(symptom).add(illness);
                    } else {
                        ArrayList<String> arr = new ArrayList<>();
                        arr.add(illness);
                        symptomIllnesses.put(symptom, arr);
                    }
                }

            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * gets a list of related illnesses based on the patient's symptoms. Ranked highest to lowest by number of matching symptoms
     * @return An ArrayList of illnesses
     */
    public ArrayList<String> getRelatedIllnesses(){
        Map<String, Integer> relatedIllnesses = new HashMap<>();
        ArrayList<String> res = new ArrayList<>();

        for (String symptom : symptoms){
            if (symptomIllnesses.containsKey(symptom)){
                for (String illness : symptomIllnesses.get(symptom)){
                    if (!relatedIllnesses.containsKey(illness)){
                        relatedIllnesses.put(illness, 1);
                    } else {
                        relatedIllnesses.put(illness, relatedIllnesses.get(illness)+1);
                    }
                }

            }
        }

        for (String value : relatedIllnesses.keySet()){
            res.add(relatedIllnesses.get(value)+"/"+symptoms.size()+" symptoms: "+value);
        }

        res.sort(Collections.reverseOrder());
        return res;
    }

    /**
     * Add a symptom to the patient's list of symptoms.
     * @param symptom The symptom to be added
     * @return A list of related illnesses to all current symptoms
     */
    public ArrayList<String> addSymptom(String symptom){
        symptoms.add(symptom);

        return getRelatedIllnesses();
    }

    /**
     * clears all symptoms
     */
    public void clearSymptoms(){
        symptoms.clear();
    }

    /**
     * Removes the specified symptom
     * @param symptom the symptom to be removed
     */
    public void removeSymptom(String symptom){
        symptoms.remove(symptom);
    }

    public static void main(String[] args) {
        PhysiciansHelper helper = new PhysiciansHelper();
        Scanner scan = new Scanner(System.in);
        helper.loadFile();

        for (int i=0; i<5; i++){
            System.out.println("Enter patient's symptom: ");
            String symptom = scan.nextLine();
            ArrayList<String> relatedIllnesses = helper.addSymptom(symptom);

            System.out.println("Related illnesses (most to least likely): ");
            int previousNumerator = 0;
            for (String illness : relatedIllnesses){
                if (previousNumerator == Integer.parseInt(illness.split("/")[0])) {
                    System.out.print(", "+illness.split(": ")[1]);
                } else {
                    previousNumerator = Integer.parseInt(illness.split("/")[0]);
                    System.out.print("\n"+illness);
                }
            }
            System.out.println();

        }
    }
}