import java.io.*;
import java.util.ArrayList;


/**
 * The Patient class represents a patient in the hospital management application.
 * It extends the User class and includes attributes and methods specific to a patient.
 * 
 * This class contains personal information about the patient, such as their ID, name, date of birth, gender,
 * phone number, email, blood type, and assigned doctor ID. It also manages the patient's appointments.
 *
 * The Patient class provides getter and setter methods for each attribute, as well as methods to load patient
 * data from a CSV file and manage appointments associated with the patient.
 */

public class Patient implements User{

	private String ID;
    private String name;
	private String dateOfBirth;
	private String gender;
	private String phoneNo;
	private String email;
	private String bloodType;
	private String assignedDoctorID;
	private ArrayList<Appointment> patientAppointments = new ArrayList<>();

	private String file = "HospitalApp\\src\\Database\\CSV\\DB\\PATIENT_DB.csv";

	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		this.ID = iD;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	private void setBT(String bloodType){
		this.bloodType = bloodType;
	}
	public String getBloodType() {
		return bloodType;
	}
	public void setAssignedDoctorID(String assignedDoctorID) {
		this.assignedDoctorID = assignedDoctorID;
	}
	public String getAssignedDoctorID() {
		return assignedDoctorID;
	}

	/**
	 * Updates the list of appointments associated with the patient by loading all appointments
	 * from the system and filtering for those that match the patient's ID.
	 *
	 * This method ensures that only appointments belonging to the patient are added to their
	 * appointment list, and duplicate entries are avoided.
	 *
	 * @param patientAppointments an ArrayList to initialize the patient's appointments (currently unused)
	 */
	public void setPatientAppointments(ArrayList<Appointment> patientAppointments){
		ArrayList<Appointment> allAppointments = AppointmentManager.loadAppointments(new ArrayList<>());

		for (Appointment appointment : allAppointments) {
			if (appointment.getPID().equals(ID) && !patientAppointments.contains(appointment)) {
				this.patientAppointments.add(appointment);
			}
		}
	}

	/**
	 * Retrieves the list of appointments specific to the patient.
	 * 
	 * This method provides access to the patient's currently stored appointment records.
	 *
	 * @return an ArrayList containing the patient's appointments
	 */
	public ArrayList<Appointment> getPatientAppointments(){
		return this.patientAppointments;
	}

	/**
     * Constructs a Patient object and loads patient data from a CSV file based on the provided ID.
     * 
     * @param ID the unique identifier for the patient
     */
	public Patient(String ID){
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if(values[0].equals(ID)){
					setID(values[0]);
					setName(values[1]);
					setDateOfBirth(values[2]);
					setGender(values[3]);
					setBT(values[4]);
					setEmail(values[5]);
					setPhoneNo(values[6]);
					DoctorSelection doc_ID = new DoctorSelection();
					if(values[7].equals("NULL") || values[7].isEmpty() || values[7] == null ){
						values[7] = doc_ID.chooseDoctor(this.ID);
					}
					setAssignedDoctorID(values[7]);
					setPatientAppointments(this.patientAppointments);
				}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
