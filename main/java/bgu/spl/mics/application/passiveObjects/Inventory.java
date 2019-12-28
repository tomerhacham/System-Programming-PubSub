package bgu.spl.mics.application.passiveObjects;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *  That's where Q holds his gadget (e.g. an explosive pen was used in GoldenEye, a geiger counter in Dr. No, etc).
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Inventory {

	//Fields:
	private List<String> gadgets = new LinkedList<String>();
	private static Inventory inventory=null;
	private static class SingeltonHolder{
		private static Inventory instance = new Inventory();
	}

	//Constructor:
	/**
	 * this is empty private constructor for the Singelton,
	 * the only function that can access the constructor and
	 * make new instance of the class is getInstance()
	 */
	private Inventory()
	{}

	//Methods:
	/**
     * Retrieves the single instance of this class.
     */
	public static Inventory getInstance() {
		return SingeltonHolder.instance;
	}

	/**
     * Initializes the inventory. This method adds all the items given to the gadget
     * inventory.
     * <p>
     * @param inventory 	Data structure containing all data necessary for initialization
     * 						of the inventory.
     */
	public void load (String[] inventory) {
		for (String gadget:inventory){gadgets.add(gadget);}
	}
	
	/**
     * acquires a gadget and returns 'true' if it exists.
     * <p>
     * @param gadget 		Name of the gadget to check if available
     * @return 	‘false’ if the gadget is missing, and ‘true’ otherwise
     */
	public boolean getItem(String gadget){
		if(gadgets.contains(gadget)){
			gadgets.remove(gadget);
			return true;
		}
		else{return false;}
	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<Gadget> which is a
	 * List of all the reports in the diary.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printToFile(String filename){
		JSONArray array = new JSONArray();
		array.addAll(gadgets);
		//Write JSON file
		try (FileWriter file = new FileWriter(filename+".json")) {

			file.write(array.toJSONString());
			file.flush();
			file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
