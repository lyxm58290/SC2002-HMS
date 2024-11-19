
/**
 * Represents an outcome record for a medical appointment.
 * 
 * This class stores information related to a patient's appointment outcome, including
 * the patient ID, doctor ID, date of the appointment, diagnosis, service provided,
 * medication prescribed, amount of medication, status of the appointment, and the 
 * treatment plan. It provides getter and setter methods for each of these attributes,
 * allowing for easy access and modification of the outcome record details.
 */

public class OutcomeRecord {
    private String patientID;
    private String doctorID;
    private String dateOfAppointment;
    private String diagnosis;
    private String service;
    private String med;
    private String amount;
    private String status;
    private String treatmentPlan;

    /**
     * Sets the patient ID for this outcome record.
     * 
     * @param patientID The ID of the patient.
     */
    public void setPatientID(String patientID){
        this.patientID = patientID;
    }

    /**
     * Returns the patient ID of this outcome record.
     * 
     * @return The patient ID.
     */
    public String getPatientID(){
        return this.patientID;
    }

    /**
     * Sets the doctor ID for this outcome record.
     * 
     * @param doctorID The ID of the doctor.
     */
    public void setDoctorID(String doctorID){
        this.doctorID = doctorID;
    }

    /**
     * Returns the doctor ID of this outcome record.
     * 
     * @return The doctor ID.
     */
    public String getDoctorID(){
        return this.doctorID;
    }

    /**
     * Sets the date of the appointment for this outcome record.
     * 
     * @param dateOfAppointment The date of the appointment.
     */
    public void setDateOfAppointment(String dateOfAppointment){
        this.dateOfAppointment = dateOfAppointment;
    }

    /**
     * Returns the date of the appointment of this outcome record.
     * 
     * @return The date of the appointment.
     */
    public String getDateOfAppointment(){
        return this.dateOfAppointment;
    }

    /**
     * Sets the diagnosis for this outcome record.
     * 
     * @param diagnosis The diagnosis made during the appointment.
     */
    public void setDiagnosis(String diagnosis){
        this.diagnosis = diagnosis;
    }

    /**
     * Returns the diagnosis of this outcome record.
     * 
     * @return The diagnosis.
     */
    public String getDiagnosis(){
        return this.diagnosis;
    }

    /**
     * Sets the service provided during the appointment.
     * 
     * @param service The type of service provided.
     */
    public void setService(String service){
        this.service = service;
    }

    /**
     * Returns the service provided during this outcome record.
     * 
     * @return The service provided.
     */
    public String getService(){
        return this.service;
    }

    /**
     * Sets the medication prescribed in this outcome record.
     * 
     * @param med The name of the medication.
     */
    public void setMed(String med){
        this.med = med;
    }

    /**
     * Returns the medication prescribed in this outcome record.
     * 
     * @return The medication name.
     */
    public String getMed(){
        return this.med;
    }

    /**
     * Sets the amount of medication prescribed in this outcome record.
     * 
     * @param amount The amount of medication.
     */
    public void setAmount(String amount){
        this.amount = amount;
    }

    /**
     * Returns the amount of medication prescribed in this outcome record.
     * 
     * @return The amount of medication.
     */
    public String getAmount(){
        return this.amount;
    }

    /**
     * Sets the status of this outcome record.
     * 
     * @param status The status of the appointment outcome.
     */
    public void setStatus(String status){
        this.status = status;
    }

    /**
     * Returns the status of this outcome record.
     * 
     * @return The status.
     */
    public String getStatus(){
        return this.status;
    }

    /**
     * Sets the treatment plan for this outcome record.
     * 
     * @param treatmentPlan The treatment plan provided.
     */
    public void setTreatmentPlan(String treatmentPlan){
        this.treatmentPlan = treatmentPlan;
    }

    /**
     * Returns the treatment plan of this outcome record.
     * 
     * @return The treatment plan.
     */
    public String getTreatmentPlan(){
        return this.treatmentPlan;
    }

    /**
     * Default constructor for OutcomeRecord.
     * Initializes an empty outcome record.
     */
    public OutcomeRecord(){};

    /**
     * Constructs a new {@link OutcomeRecord} with the specified details.
     * 
     * This constructor initializes an outcome record with the provided information 
     * regarding a patient's appointment outcome, including patient ID, doctor ID, 
     * date of the appointment, diagnosis, service provided, medication prescribed, 
     * amount of medication, status of the appointment, and the treatment plan.
     *
     * @param patientID The ID of the patient associated with this outcome record.
     * @param doctorID The ID of the doctor associated with this outcome record.
     * @param dateOfAppointment The date and time of the appointment.
     * @param diagnosis The diagnosis made during the appointment.
     * @param service The type of service provided during the appointment.
     * @param med The name of the medication prescribed.
     * @param amount The amount of medication prescribed.
     * @param status The status of the appointment outcome.
     * @param treatmentPlan The treatment plan provided to the patient.
     */
    public OutcomeRecord(String patientID, String doctorID, String dateOfAppointment, String diagnosis,
                         String service, String med, String amount, String status, String treatmentPlan) {
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.dateOfAppointment = dateOfAppointment;
        this.diagnosis = diagnosis;
        this.service = service;
        this.med = med;
        this.amount = amount;
        this.status = status;
        this.treatmentPlan = treatmentPlan;
    }
}   

