import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

// Events Class for Events.txt
class Events{
    // Variables
    private String eventName;
    private char type;
    private String min;
    private String max;
    private String weight;

    // Constructor
    public Events(String eventName, char type, String min, String max, String weight) {
        this.eventName = eventName;
        this.type = type;
        this.min = min;
        this.max = max;
        this.weight = weight;
    }
    
    // Getter and Setter
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
    
    @Override
    public String toString(){
        return String.format("%s %s %s %s %s", eventName, type, min, max, weight);
    }
}

// Stats Class
class Stats{
    // Variables
    private String eventName;
    private String mean;
    private String standardDeviation;

    // Constructors
    public Stats(){
        
    }
    
    public Stats(String eventName, String mean, String standardDeviation) {
        this.eventName = eventName;
        this.mean = mean;
        this.standardDeviation = standardDeviation;
    }

    // Getter and Setter
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

    public String getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(String standardDeviation) {
        this.standardDeviation = standardDeviation;
    }
    
    @Override
    public String toString(){
        return String.format("%s:%s:%s", eventName, mean, standardDeviation);
    }
     
}

// Simulation Events Class
class simulationEvent{
    // Variables
    private String eventName;
    private String eventValue;

    // Constructor
    public simulationEvent(String eventName, String eventValue) {
        this.eventName = eventName;
        this.eventValue = eventValue;
    }

    // Getter and Setter
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventValue() {
        return eventValue;
    }

    public void setEventValue(String eventValue) {
        this.eventValue = eventValue;
    }
    
    @Override
    public String toString(){
        return String.format("%s:%s", eventName, eventValue);
    }
  
}

// Daily Events Class
class dailyEvents{
    // Variables
    private String dayName;
    private ArrayList <simulationEvent> SE_List = new ArrayList<simulationEvent>();

    // Constructor
    public dailyEvents(String dayName, ArrayList<simulationEvent> aList) {
        this.dayName = dayName;
        
        for (int i = 0; i < aList.size(); i++) {
            this.SE_List.add(aList.get(i));
        }
    }

    // Getter and Setter
    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }
    
    public ArrayList<simulationEvent> getSE_List() {
	return SE_List;
    }

    public void setSE_List(ArrayList<simulationEvent> sE_List) {
	SE_List = sE_List;
    } 
    
    // Method to calculate the daily totals
    public double getDailyTotals(){
        double dailyTotal = 0;
        
        for (int i = 0; i < this.SE_List.size(); i++) {
            double evntVal = Double.parseDouble(this.SE_List.get(i).getEventValue());
            dailyTotal = dailyTotal + evntVal;
        }
        
        return dailyTotal;
    }
    
    // Method to calculate the daily total
    public double calculateDailyTotal(){
        double dailyTotal = 0;
        
        for (simulationEvent se : SE_List) {
            dailyTotal = dailyTotal + Double.parseDouble(se.getEventValue());
        }
        
        return dailyTotal;
    }
    
    // Method to return SE_List as a String
    public String getSE_ListStr(){
        String sePrint = "";
        for (simulationEvent se : SE_List) {
            sePrint = sePrint + se.getEventName() + ":" + se.getEventValue() + "\n";
        }
        
        return sePrint;
    }
    
    @Override
    public String toString(){
        return String.format("%s%n%s", dayName, getSE_ListStr());  
    }
}

// Main Class
public class IDS {
    // Variables
    private static int Days;
    private static boolean inconsistencies = false; // variable to see if there is inconsistency
    
    private static ArrayList<Events> EventsList = new ArrayList<Events>(); // ArrayList for Events.txt
    private static ArrayList<Stats> StatsList = new ArrayList<Stats>(); // ArrayList for Stats.txt
    private static int numOfEvent_in_Event; // Total events in Events.txt
    private static int numOfEvent_in_Stats; // Total events in Stats.txt
    
    private static ArrayList<dailyEvents> dailyEvents_List = new ArrayList<dailyEvents>(); // Array List for the simulation, new baseline
    private static ArrayList<Stats> simulationStats = new ArrayList<Stats>(); // Array List for simulation stats, new baseline
    
    private static int liveDays;
    private static ArrayList<dailyEvents> liveData_List = new ArrayList<dailyEvents>(); // Array List for live log data
    private static ArrayList<Stats> liveStats = new ArrayList<Stats>(); // Array List for live data stats
    private static int Treshold;
    
    // Method to read the Event.txt and Stats.txt
    public static void readFile(String[] args){
        // Get the event and stats file name
        String eventsFilename = args[0];
        String statsFilename = args[1];
        
        // Get the Days
        String strDays = args[2];
        Days = Integer.parseInt(strDays);
        
        // Read in the Events.txt file
        readEvents(eventsFilename);
        System.out.println("Read Events File success.");
        System.out.println();
            
        // Read in the Stats.txt file
        readStats(statsFilename);
        System.out.println("Read Stats File success.");
        System.out.println();
    }
    
    // Method to read the Events File
    public static void readEvents(String eventsFilename){
        System.out.println(eventsFilename + " Opened.");
        
        Scanner scan = null;
    
        try{
            scan = new Scanner(new File(eventsFilename));
        }catch(FileNotFoundException E){
            System.out.println("Events File not found");
        }
        
        while(scan.hasNext()){
            String line = scan.nextLine();
            
            // Skip the line if it is a blank line
            if(line.isEmpty()) {
                continue;
            }
            
            if(!line.contains(":")){
                numOfEvent_in_Event = Integer.parseInt(line);
            }else{
                String[] eventDetail = line.split(":");
                
                // Input as 0 when the Max is infinite
                if(eventDetail[3].equals("")){
                    eventDetail[3] = "0";
                }
                // Input as 0 when the min is empty
                if(eventDetail[2].equals("")){
                    eventDetail[2] = "0";
                }
                
                if(eventDetail[1].length() != 1){
                	System.out.println("WARNING : UNSUPPORTED FORMAT, PLEASE ENTER 'C' OR 'D' AS A TYPE --> Event Name = " + eventDetail[0]);
                	System.err.println("Terminating Program . . .");
        			System.exit(1);
                }
                
                // catch error if the data is incomplete
                try {
                	// Create an Events Object
                    Events eventsObj = new Events
                    (eventDetail[0].trim(), eventDetail[1].charAt(0), eventDetail[2].trim(), eventDetail[3].trim(), eventDetail[4].trim());
                    
                    // Add Events Object into the ArrayList
                    EventsList.add(eventsObj);  
                } catch (ArrayIndexOutOfBoundsException dataNull) {
                	System.out.println("WARNING : INCOMPLETE DATA IN Events.txt --> Event Name = " + eventDetail[0]);
                	System.err.println("Terminating Program . . .");
        			System.exit(1);
                } catch (StringIndexOutOfBoundsException typeNull) {
                	System.out.println("WARNING : INCOMPLETE DATA IN Events.txt --> Event Name = " + eventDetail[0]);
                	System.err.println("Terminating Program . . .");
        			System.exit(1);
                } catch (Exception e) {
                	System.out.println("Error in Reading Events.txt File");
                	e.getStackTrace();
                	System.err.println("Terminating Program . . .");
        			System.exit(1);
                }
                
            }     
        } 
    }
    
    // Method to read the Stats File
    public static void readStats(String statsFilename){
        System.out.println(statsFilename + " Opened.");
        
        Scanner scan = null;
    
        try{
            scan = new Scanner(new File(statsFilename));
        }catch(FileNotFoundException E){
            System.out.println("Stats File not found");
        }
        
        while(scan.hasNext()){
            String line = scan.nextLine();
            
            // Skip the line if it is a blank line
            if(line.isEmpty()) {
                continue;
            }
            
            if(!line.contains(":")){
                numOfEvent_in_Stats = Integer.parseInt(line);
            }else{
                String[] statDetail = line.split(":");
                
             // catch error if the data is incomplete
                try {
                	// Create a Stats Object
                    Stats statsObj = new Stats(statDetail[0].trim(), statDetail[1].trim(), statDetail[2].trim());
                    
                    // Add Stats Object into the ArrayList
                    StatsList.add(statsObj); 
                } catch (ArrayIndexOutOfBoundsException dataNull) {
                	System.out.println("WARNING : INCOMPLETE DATA IN Stats.txt --> Event Name = " + statDetail[0]);
                	System.err.println("Terminating Program . . .");
        			System.exit(1);
                } catch (StringIndexOutOfBoundsException typeNull) {
                	System.out.println("WARNING : INCOMPLETE DATA IN Stats.txt --> Event Name = " + statDetail[0]);
                	System.err.println("Terminating Program . . .");
        			System.exit(1);
                } catch (Exception e) {
                	System.out.println("Error in Reading Stats.txt File");
                	e.getStackTrace();
                	System.err.println("Terminating Program . . .");
        			System.exit(1);
                }
            }    
        }
    }

    // Method to return true if it is an int
    public static boolean isStringInt(String s){
        try{
            Integer.parseInt(s);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
    
    // Method to generate the simulation
    public static ArrayList<dailyEvents> generateSimulation(ArrayList<Events> EventsList, ArrayList<Stats> StatsList, int days){
        System.out.println("Generating Data.");
        
        ArrayList<dailyEvents> de_List = new ArrayList<dailyEvents>();
        de_List.clear();
        
        for (int i = 0; i < days; i++) {
            // Create a blank ArrayList of simulationEvent objects
            ArrayList<simulationEvent> seList = new ArrayList<simulationEvent>();
            seList.clear();
            
            for(Stats s : StatsList){
                for(Events e : EventsList){
                    if(s.getEventName().equals(e.getEventName())){
                        // Check if the event type is D
                        if(e.getType() == 'D'){
                            
                            // Get the mean and standard deviation from Stats.txt
                            double mean = Double.parseDouble(s.getMean());
                            double stdDev = Double.parseDouble(s.getStandardDeviation());
                            
                            // Get the range for the simulation
                            int min = (int)(Math.floor(mean - stdDev));
                            int max = (int)(Math.ceil(mean + stdDev));
                            
                            // Generate values for the simulation
                            Random random = new Random();
                            int value = random.ints(min, max + 1).findFirst().getAsInt();
                            String strValue = "" + value;
                            
                            // Create a simulationEvent objects and add it to the ArrayList
                            simulationEvent se = new simulationEvent(s.getEventName(), strValue);
                            seList.add(se);
                        }
                        
                        if(e.getType() == 'C'){
                            
                            // Get the mean and standard deviation from Stats.txt
                            double mean = Double.parseDouble(s.getMean());
                            double stdDev = Double.parseDouble(s.getStandardDeviation());
                            
                            // Get the range for the simulation
                            double min = mean - stdDev;
                            double max = mean + stdDev;
                            
                            // Generate values for the simulation
                            Random random = new Random();
                            double value = random.doubles(min, max + 1).findFirst().getAsDouble();
                            String strValue = String.format("%.2f", value);
                            
                            // Create a simulationEvent objects and add it to the ArrayList
                            simulationEvent se = new simulationEvent(s.getEventName(), strValue);
                            seList.add(se);
                            
                        } 
                    }  
                }
            }
            
            // Create a dailyEvents object and add it into the ArrayList
            dailyEvents de = new dailyEvents("Day " + (i + 1), seList);
            de_List.add(de);
            
        }
        
        System.out.println("Log Data Generated.");
        System.out.println();
        
        return de_List;
    }
    
    // Method to calculate stats in the generated simulation
    public static ArrayList<Stats> calculateStats(ArrayList<dailyEvents> aList){
        ArrayList<Stats> simulatedStats = new ArrayList<Stats>();
        
        ArrayList<String> eventNameList = new ArrayList<>(); // to store the name of events
        ArrayList<Double> meanList = new ArrayList<>(); // to store the mean
        ArrayList<ArrayList<Double>> valueList = new ArrayList<>(); //for calculation for getting mean, and value - mean
        
        
        // retrieve the value and put it into 2D array
        
        for(simulationEvent se : aList.get(0).getSE_List()){
                 eventNameList.add(se.getEventName());
             }
        
        double[][] doubleVal = new double[eventNameList.size()][aList.size()];
        
        for (int i = 0; i < aList.size(); i++) {
            for (int j = 0; j < eventNameList.size(); j++) {
                doubleVal[j][i] = Double.parseDouble(aList.get(i).getSE_List().get(j).getEventValue());
            }
        }

        for (int i = 0; i < doubleVal.length; i++) {
            ArrayList<Double> doubleValue = new ArrayList<>();
            for (int j = 0; j < doubleVal[i].length; j++) {
                doubleValue.add(doubleVal[i][j]);
            }
            valueList.add(doubleValue);
        }
        
        // Calculate Mean
        for (int i = 0; i < valueList.size(); i++) {
        	double sum = 0;
        	for(int j = 0 ; j < valueList.get(i).size(); j++) {
        		// sum up the value of each event in days
        		sum = sum + valueList.get(i).get(j);
                        
        	}
        	// get the mean and store it in meanList
        	double mean = sum / valueList.get(0).size();
        	meanList.add(mean);
        }
        
        // Calculate Standard Deviation
        // ============================
        // Standard Deviation Calculation
        // 1. sum of (value - mean)^2
        // 2. divide it by length (get average)
        // 3. square root!!

        ArrayList<Double> standardDeviationList = new ArrayList<>();
        
        for (int i = 0; i < valueList.size(); i++) {
            double sum = 0;
            for (int j = 0; j < valueList.get(i).size(); j++) {
                double value = valueList.get(i).get(j);
                double difference = value - meanList.get(i);
                double squaredValue = Math.pow(difference, 2);
                sum += squaredValue;
            }
            
            double average = sum/valueList.size();
            double squareRoot = Math.sqrt(average);
            standardDeviationList.add(squareRoot);
                
            }
        

        // store it into simulatedStats
        for(int i = 0 ; i < eventNameList.size() ; i++){
        	String eventName = eventNameList.get(i);
        	String mean = String.format("%.2f",meanList.get(i));
        	String standardDeviation = String.format("%.2f", standardDeviationList.get(i));
        	
        	Stats stat = new Stats(eventName, mean, standardDeviation);
        	simulatedStats.add(stat);
        	
        }
        
        System.out.println("Stats Calculated.");
        System.out.println();
       
        // return an ArrayList of Stats objects
        return simulatedStats;
    }
    
    // Method to write the simulation data into a txt file
    public static void writeSimulationLog(){
        FileWriter writer;
        
        try{
            writer = new FileWriter (new File("LogData.txt"));
            System.out.println("LogData.txt Created");
            
            for(dailyEvents de : dailyEvents_List){
                String toWrite = "" + de + "\n";
                writer.write(toWrite);
            }
            
            System.out.println("Write Simulation Logs.");
            
            writer.flush();
            writer.close();
        }catch (FileNotFoundException ex){
        Logger.getLogger(IDS.class.getName()).log(Level.SEVERE, null, ex);
        }catch (IOException ex){
        Logger.getLogger(IDS.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println();
    }
    
    // Method to write the simulation statistics into a txt file
    public static void writeSimulationStats(){
        FileWriter writer;
        
        try{
            writer = new FileWriter (new File("SimulationStats.txt"));
            System.out.println("SimulationStats.txt Created");
            
            writer.write(simulationStats.size() + "\n");
            
            for(Stats s : simulationStats){
                String toWrite = "" + s;
                writer.write(toWrite + "\n");
            }
            writer.write("\n");
            
            for(dailyEvents de : dailyEvents_List){
                String writeTotal = "" + String.format("%.2f%n", de.getDailyTotals());
                writer.write(de.getDayName() + "\nDaily Total = " + writeTotal);
            }
            
            System.out.println("Write Simulation Stats.");
            
            writer.flush();
            writer.close();
        }catch (FileNotFoundException ex){
        Logger.getLogger(IDS.class.getName()).log(Level.SEVERE, null, ex);
        }catch (IOException ex){
        Logger.getLogger(IDS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println();
    }
    
    public static void liveDataChoice() {
    	Scanner input = new Scanner(System.in);
    	
    	// Ask user for a file name or input 'e' to terminate(?)
    	System.out.print("Please insert new stats file name : ");
    	String choice = input.nextLine();
        
        File newStats = new File(choice);
        boolean fileExist = newStats.exists();
        
        while(!fileExist){
            System.out.println();
            System.out.print("Please insert new stats file name : ");
            choice = input.nextLine();
        
            newStats = new File(choice);
            fileExist = newStats.exists();
        }          
        
        int inputDays = -1;
        
        while(inputDays < 0){
            // Ask user for the total days
            System.out.print("Days : ");
            inputDays = input.nextInt();
        }
        
        liveDays = inputDays;

        readLiveData(choice);
    	
    }
    
    
    // Method to read live stats and store it in ArrayList
    public static void readLiveData(String newStatsFile){
        Scanner scan = null;
        
        // Check if file exist
        // Read User Input if File exist, throw an error message if file does not exist and Ask the user again
        try{
            scan = new Scanner(new File(newStatsFile));
        }catch(FileNotFoundException E){
            System.out.println("Stats File not found");
        }
        
        System.out.println(newStatsFile + " Opened.");
        
        while(scan.hasNext()){
            String line = scan.nextLine();
            
            // Skip the line if it is a blank line
            if(line.isEmpty()) {
                continue;
            }
            
            if(!line.contains(":")){
                numOfEvent_in_Stats = Integer.parseInt(line);
            }else{
                String[] statDetail = line.split(":");
                
                try {
                    // Create a Stats Object
                    Stats statsObj = new Stats(statDetail[0].trim(), statDetail[1].trim(), statDetail[2].trim());
                    
                    // Input stats objects into liveStats ArrayList
                    liveStats.add(statsObj);  
                } catch (ArrayIndexOutOfBoundsException dataNull) {
                	System.out.println("WARNING : INCOMPLETE DATA IN Stats.txt --> Event Name = " + statDetail[0]);
                	System.err.println("Terminating Program . . .");
        			System.exit(1);
                } catch (StringIndexOutOfBoundsException typeNull) {
                	System.out.println("WARNING : INCOMPLETE DATA IN Stats.txt --> Event Name = " + statDetail[0]);
                	System.err.println("Terminating Program . . .");
        			System.exit(1);
                } catch (Exception e) {
                	System.out.println("Error in Reading Stats.txt File");
                	e.getStackTrace();
                	System.err.println("Terminating Program . . .");
        			System.exit(1);
                }
            }     
        }       
    }
    
    // Method to calculate the treshold
    public static void getTreshold(){
        int tresCalc = 0;
   
        for (Events e : EventsList) {
        	int weightInt = Integer.parseInt(e.getWeight());
        	tresCalc += weightInt;		
        }
        
        // Calculate the treshold
        // 2 * (Sums of weights) from EventsList
        tresCalc = tresCalc * 2;
        
        Treshold = tresCalc;
    }
    
    // Method to generate the simulation for the live data
    public static ArrayList<dailyEvents> generateLiveSimulation(ArrayList<Events> EventsList, ArrayList<Stats> StatsList, int days){
        System.out.println("Generating Data.");
        
        ArrayList<dailyEvents> de_List = new ArrayList<dailyEvents>();
        de_List.clear();
        
        for (int i = 0; i < days; i++) {
            // Create a blank ArrayList of simulationEvent objects
            ArrayList<simulationEvent> seList = new ArrayList<simulationEvent>();
            seList.clear();
            
            for(Stats s : StatsList){
                for(Events e : EventsList){
                    if(s.getEventName().equals(e.getEventName())){
                        // Check if the event type is D
                        if(e.getType() == 'D'){

                            // Get the mean and standard deviation from Stats.txt
                            double mean = Double.parseDouble(s.getMean());
                            double stdDev = Double.parseDouble(s.getStandardDeviation());
                            
                            // Get the range for the simulation
                            int randomData = (int)(Math.random() * 10);
                            int min, max;
                            
                            if(randomData < 3){
                                min = (int)(Math.floor(Double.parseDouble(e.getMin())));
                                max = (int)(Math.floor(Double.parseDouble(e.getMax())));
                                
                                if(max == 0)
                                    max = (int)Math.floor(mean * 2);  
                            }else{
                                min = (int)(Math.floor(mean - stdDev));
                                max = (int)(Math.ceil(mean + stdDev));
                            }
                            
                            // Generate values for the simulation
                            Random random = new Random();
                            int value = random.ints(min, max + 1).findFirst().getAsInt();
                            String strValue = "" + value;
                            
                            // Create a simulationEvent objects and add it to the ArrayList
                            simulationEvent se = new simulationEvent(s.getEventName(), strValue);
                            seList.add(se);
                        }
                        
                        if(e.getType() == 'C'){
                            
                            // Get the mean and standard deviation from Stats.txt
                            double mean = Double.parseDouble(s.getMean());
                            double stdDev = Double.parseDouble(s.getStandardDeviation());
                            
                            // Get the range for the simulation
                            double min = mean - stdDev;
                            double max = mean + stdDev;
                            
                            // Generate values for the simulation
                            Random random = new Random();
                            double value = random.doubles(min, max + 1).findFirst().getAsDouble();
                            String strValue = String.format("%.2f", value);
                            
                            // Create a simulationEvent objects and add it to the ArrayList
                            simulationEvent se = new simulationEvent(s.getEventName(), strValue);
                            seList.add(se);
                            
                        } 
                    }  
                }
            }
            
            // Create a dailyEvents object and add it into the ArrayList
            dailyEvents de = new dailyEvents("Day " + (i + 1), seList);
            de_List.add(de);
            
        }
        
        System.out.println("Log Data Generated.");
        System.out.println();
        
        return de_List;
    }
    
    // Method to calculate the anomaly counter of an event
    public static double calculateAnomaly(simulationEvent se, Stats s, Events e){
        double anomaly;
        
        // Convert needed variable to double type
        double doubleMean = Double.parseDouble(s.getMean());
        double doubleStandardDeviation = Double.parseDouble(s.getStandardDeviation());
        double doubleValue = Double.parseDouble(se.getEventValue());
        double doubleWeight = Double.parseDouble(e.getWeight());
        
        // ((Mean - x) / standardDeviation) * Weight
        anomaly = ((doubleMean - doubleValue) / doubleStandardDeviation) * doubleWeight;
        
        return Math.abs(anomaly);
    }
    
    // Alert Engine
    public static void AlertEngine(ArrayList<dailyEvents> aDailyEventsList, ArrayList<Stats> aStatsList, ArrayList<Events> anEventsList){

        for(dailyEvents de : aDailyEventsList){
            double anomalyCounter = 0;
            System.out.println(de.getDayName());
            System.out.println("Treshold : " + Treshold);
            
            for(simulationEvent se : de.getSE_List()){
                for(Stats s : aStatsList){
                    for(Events e: anEventsList){
                        
                        if(se.getEventName().equals(s.getEventName()) && s.getEventName().equals(e.getEventName())){
                            double anomaly = calculateAnomaly(se, s, e);
                            System.out.printf("%-25s(Anomaly Counter : %.2f)%n", se.getEventName(), anomaly);
                            anomalyCounter = anomalyCounter + anomaly;
                        }
                    }
                }
            }
            
            System.out.printf("Total for Anomaly Counter : %20.2f%n", anomalyCounter);
            System.out.println();
            
            if(anomalyCounter > Treshold){
                System.out.printf("%50s%n", ">>>>>>>>>>ANOMALY DETECTED<<<<<<<<<<");
                System.out.println();
            }else{
                System.out.printf("%50s%n", "NO ANOMALY DETECTED");
                System.out.println();
            }
        }
    }
    
    /*
    * ---------------------------------------INCONSISTENCIES CHECKS---------------------------------------
    */
    
    // CHECK IF THE NUMBER OF STATED EVENTS EQUAL TO ACTUAL EVENT (NUMBER OF EVENT'S EVENT CONSISTENCY)
    public static void checkNumberOfEventsConsistency() {
    	int NumberOfActualEvent = EventsList.size();
    	
    	if(NumberOfActualEvent != numOfEvent_in_Event) {
    		System.out.println("WARNING : INCONSISTENCY DETECTED --> NUMBER OF EVENTS IS NOT MATCHED Stats.txt " + NumberOfActualEvent + " != " + numOfEvent_in_Event);
                inconsistencies = true;
                System.err.println("Terminating Program . . .");
    			System.exit(1);
        }
    }
    
    // CHECK IF THE NUMBER OF STATED STATS EQUAL TO ACTUAL STAT (NUMBER OF STAT'S EVENT CONSISTENCY)
    public static void checkNumberOfStatsConsistency() {
    	int NumberOfActualStat = StatsList.size();
    	
    	if(NumberOfActualStat != numOfEvent_in_Stats) {
    		System.out.println("WARNING : INCONSISTENCY DETECTED --> NUMBER OF EVENTS IS NOT MATCHED Stats.txt " + NumberOfActualStat + " != " + numOfEvent_in_Stats);
                inconsistencies = true;
                System.err.println("Terminating Program . . .");
    			System.exit(1);
        }
    }
    
    // CROSS CHECK IF NUMBER OF EVENT STATED IN Events.txt & Stats.txt IS EQUAL (CROSS CHECK NUMBER OF EVENT CONSISTENCY)
    public static void crossCheckNumberConsistency() {
    	
    	if(numOfEvent_in_Event != numOfEvent_in_Stats) {
    		System.out.println("WARNING : INCONSISTENCY DETECTED --> NUMBER OF EVENTS IS NOT MATCHED (Event.txt->" + numOfEvent_in_Event + ") != (Stats.txt->" + numOfEvent_in_Stats + ")");
                inconsistencies = true;
                System.err.println("Terminating Program . . .");
    			System.exit(1);
    	}
    }
    
    // CROSS CHECK IF THE THE EVENT EXISTS IN BOTH Events.txt & Stats.txt (NAMING CONSISTENCY)
    public static void crossCheckNamingConsistency() {
    	
    	for(int i = 0; i < EventsList.size() ; i++) {
    		String eventName1 = EventsList.get(i).getEventName();
    		String eventName2 = StatsList.get(i).getEventName();
    		
    		if (!eventName1.equals(eventName2)) {
    			System.out.println("WARNING : INCONSISTENCY DETECTED --> FILE NAMES ARE NOT MATCHED " + eventName1 + " != " + eventName2);
                        inconsistencies = true;
                        System.err.println("Terminating Program . . .");
            			System.exit(1);
    		}
    	}
    }
    
    // CHECK DISCRETE OR CONTINUOUS CONSISTENCY (CD CONSISTENCY)
    public static void checkCdConsistency(){ 
    	for(int i = 0; i < EventsList.size() ; i++) {
    		
    		String eventName = EventsList.get(i).getEventName();
    		char eventType = EventsList.get(i).getType();
    		int eventMean = StatsList.get(i).getMean().indexOf(".");
    		
    		if (eventType != 'C' && eventType != 'D') {
    			System.out.println("WARNING : INCONSISTENCY DETECTED --> EVENT TYPE ONLY CAN BE 'C' OR 'D' " + eventName);
                inconsistencies = true;
                System.err.println("Terminating Program . . .");
    			System.exit(1);
    		}
    	}
    	
    }
    
    // CHECK IF MIN VALUE IS LESS THAN 0 OR MAX VALUE EXCEED 1440 MINUTES CONSISTENCY
    public static void checkTimeOnlineConsistency() {
    	for(int i = 0; i < EventsList.size() ; i++) {
    		
    		String eventName = EventsList.get(i).getEventName();
    		int max = Integer.parseInt(EventsList.get(i).getMax());
    		int min = Integer.parseInt(EventsList.get(i).getMin());
    		
    		if (eventName.equals("Time online")) {
    			if (max > 1440) {
        			System.out.println("WARNING : INCONSISTENCY DETECTED --> EXCEEDED 1440 MINUTES (A DAY) " + eventName);
                                inconsistencies = true;
                                
        		} else if (min < 0 || max < 0) {
        			System.out.println("WARNING : INCONSISTENCY DETECTED --> MINUTE COULD NOT BE A NEGATIVE VALUE " + eventName);
                                inconsistencies = true;
                                
        		}
    		}	
    	}
    }
    
    // eventName must not be empty
    public static void checkEmptyInputConsistency(){
    	for (int i = 0; i < EventsList.size(); i++) {
    		String eventName = EventsList.get(i).getEventName();

    		if (eventName.equals("")) {
    			inconsistencies = true;
    			System.out.println("WARNING : EVENT NAME CAN NOT BE EMPTY --> (EVENT NAME NUMBER: " + i + ")");
    			System.err.println("Terminating Program . . .");
    			System.exit(1);
    		}
    	}
    }
    
    // check if weight is integer
    public static void checkWeightConsistency() {
    	for(Events e : EventsList) {
    		String weight = e.getWeight();
    		String eventName = e.getEventName();
    		if(!isStringInt(weight)) {
    			inconsistencies = true;
    			System.out.println("WARNING : EVENT WEIGHT MUST BE AN INTEGER --> EVENT NAME: " + eventName);
    			System.err.println("Terminating Program . . .");
    			System.exit(1);
    		}
    	}
    }
    
    public static void checkNegativeNumber() {
    	for(Events e : EventsList) {
    		String eventName = e.getEventName();
    		int min = Integer.parseInt(e.getMin());
    		int max = Integer.parseInt(e.getMax());
    		int weight = Integer.parseInt(e.getWeight());
    		
    		if (min < 0) {
    			inconsistencies = true;
    			System.out.println("WARNING : EVENT MIN VALUE MUST BE A POSITIVE INTEGER --> EVENT NAME: " + eventName);
    			System.err.println("Terminating Program . . .");
    			System.exit(1);
    		}
    		
    		if (max < 0) {
    			inconsistencies = true;
    			System.out.println("WARNING : EVENT MAX VALUE MUST BE A POSITIVE INTEGER --> EVENT NAME: " + eventName);
    			System.err.println("Terminating Program . . .");
    			System.exit(1);
    		}
    		
    		if (weight < 0) {
    			inconsistencies = true;
    			System.out.println("WARNING : EVENT WEIGHT MUST BE A POSITIVE INTEGER --> EVENT NAME: " + eventName);
    			System.err.println("Terminating Program . . .");
    			System.exit(1);
    		}
    	}
    }
    
    // Method to check the inconsistencies
    public static void allConsistencyCheck() {
        System.out.println("Checking Inconsistencies.");
        
    	checkNumberOfEventsConsistency();
    	checkNumberOfStatsConsistency();
    	crossCheckNumberConsistency();
    	crossCheckNamingConsistency();
    	checkCdConsistency();
    	checkTimeOnlineConsistency();
    	checkEmptyInputConsistency();
        checkWeightConsistency();
        checkNegativeNumber();
    	
        if(!inconsistencies){
            System.out.println("No Inconsistencies.");
        }
        
        System.out.println();
    }
    /*
    * -------------------------------------------------------------------------------------------------------
    */
    
    // Main Method
    public static void main(String[] args) {
        if(args.length == 3){
            // Initial Input
            readFile(args);
            
            // Check for Inconsistencies
            allConsistencyCheck();
            
            // Simulation
            System.out.println("Simulation Engine.");
            dailyEvents_List = generateSimulation(EventsList, StatsList, Days);

            // Calculating the stats of the simulation
            simulationStats = calculateStats(dailyEvents_List);
            
            // Write Simulation Log to LogData.txt
            writeSimulationLog();
            
            // Write Simulation Stats to SimulationStats.txt
            writeSimulationStats();
            
            while(true){
                
                liveData_List.clear();
                liveStats.clear();
                
                // Read live data and generate activity log
                liveDataChoice();
                liveData_List = generateLiveSimulation(EventsList, liveStats, liveDays);

                // Calculate Treshold
                getTreshold();
            
                // Alert Engine
                AlertEngine(liveData_List, liveStats, EventsList);
                
                // Ask if the user want to terminate the program
                System.out.println("Do you want to exit the program ?(Y/N)");
                Scanner sc = new Scanner(System.in);
                String isExit = sc.nextLine();
                
                System.out.println();
                if(isExit.trim().toUpperCase().equals("Y")){
                    System.exit(0);
                }  
            }
                
        }else{
            System.out.println("Input arguments 'Events.txt Stats.txt Days'");
        }
    }
}
