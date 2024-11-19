
/**
 * The `Appointment` class represents an appointment in the healthcare system.
 * It contains details about the patient, doctor, date, time, and status of the appointment.
 * 
 * This class provides getter and setter methods for each of these attributes, allowing easy
 * access and modification. It also includes constructors to create an appointment either with 
 * all details specified or as a default instance.
 */

public class Appointment {
	
    private String pID;
    private String dID;
    private String date;   
    private String time;
    private String status;
    
    /**
     * Gets the doctor ID associated with this appointment.
     *
     * @return the doctor ID
     */
    public String getDID() {
        return dID;
    }

    /**
     * Sets the doctor ID for this appointment.
     *
     * @param DID the doctor ID to set
     */
    public void setDID(String DID) {
        this.dID = DID;
    }

    /**
     * Gets the patient ID associated with this appointment.
     *
     * @return the patient ID
     */
    public String getPID() {
        return pID;
    }

    /**
     * Sets the patient ID for this appointment.
     *
     * @param PID the patient ID to set
     */
    public void setPID(String PID) {
        this.pID = PID;
    }

    /**
     * Gets the date of this appointment.
     *
     * @return the appointment date
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date for this appointment.
     *
     * @param date the date to set for the appointment
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gets the time of this appointment.
     *
     * @return the appointment time
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets the time for this appointment.
     *
     * @param time the time to set for the appointment
     */
    public void setTime(String Time) {
        this.time = Time;
    }

    /**
     * Gets the current status of this appointment.
     *
     * @return the appointment status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status for this appointment.
     *
     * @param status the status to set for the appointment
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Default constructor for the Appointment class.
     */
    public Appointment() {}

    /**
     * Constructor for creating an individual appointment with specified details.
     *
     * @param pID   the ID of the patient
     * @param dID   the ID of the doctor
     * @param date  the date of the appointment
     * @param time  the time of the appointment
     * @param status the status of the appointment (e.g., scheduled, completed, canceled)
     */
    public Appointment(String pID, String dID, String date, String time, String status) {
        this.pID = pID;
        this.dID = dID;
        this.date = date;
        this.time = time;
        this.status = status;
    }
}