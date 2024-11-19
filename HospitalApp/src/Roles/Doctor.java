
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


/**
 * The Doctor class represents a doctor in the hospital management application.
 * It implements the User interface and includes attributes and methods specific to a doctor.
 * 
 * This class contains information about the doctor, such as their ID, name, gender, age, 
 * and a list of their upcoming appointments. It also manages the doctor's schedule and appointments.
 *
 * The Doctor class provides getter and setter methods for each attribute, as well as a constructor
 * that loads doctor data from a CSV file based on the provided doctor ID.
 */

public class Doctor implements User{

    private String ID;
    private String name;
    private String gender;
    private String age;
    private ArrayList<Appointment> upcomingAppointments = new ArrayList<>();

    private String file = "HospitalApp\\src\\Database\\CSV\\DB\\DR_DB.csv";

    public String getGender(){
        return this.gender;
    }
    public void setGender(String gender){
        this.gender = gender;
    }
    public String getAge(){
        return this.age;
    }
    public void setAge(String age){
        this.age = age;
    }
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getDoctorID(){
        return this.ID;
    }
    public void setDoctorID(String doctorID){
        this.ID = doctorID;
    }

    /**
     * Sets the list of upcoming appointments for the doctor by filtering all appointments
     * to include only those assigned to this doctor.
     * 
     * This method loads all appointments from the AppointmentManager and adds to the list 
     * only those appointments where the doctor's ID matches and avoids duplicates.
     * 
     * @param upcomingAppointments an ArrayList to store the doctor's upcoming appointments
     */
    public void setDoctorAppointments(ArrayList<Appointment> upcomingAppointments){
		ArrayList<Appointment> allAppointments = AppointmentManager.loadAppointments(new ArrayList<>());
	
		for (Appointment appointment : allAppointments) {
			if (appointment.getDID().equals(ID) && !upcomingAppointments.contains(appointment)) {
				upcomingAppointments.add(appointment);
			}
		}
	}

    /**
     * Retrieves the list of the doctor's upcoming appointments.
     * 
     * @return an ArrayList containing the doctor's upcoming appointments
     */
    public ArrayList<Appointment> getDocAppointments(){
        return upcomingAppointments;
    }

    /**
     * Constructs a Doctor object and loads the doctor's data from a CSV file 
     * based on the provided doctor ID. Also initializes the doctor's availability schedule 
     * for the next 7 days with default working hours (8 AM - 6 PM).
     * 
     * @param doctorID the unique identifier for the doctor
     */
    public Doctor(String doctorID){
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if(values[0].equals(doctorID)){
					setDoctorID(values[0]);
					setName(values[1]);
					setGender(values[3]);
					setAge(values[4]);
                    setDoctorAppointments(this.upcomingAppointments);
                    DoctorSchedule.initializeAvailability(doctorID);
				}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
