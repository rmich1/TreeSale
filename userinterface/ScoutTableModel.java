package userinterface;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;

//==============================================================================
public class ScoutTableModel {
    private final SimpleStringProperty scoutId;
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty middleName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty dateOfBirth;
    private final SimpleStringProperty phoneNumber;
    private final SimpleStringProperty email;
    private final SimpleStringProperty status;
    private final SimpleStringProperty troopId;
   // private final SimpleStringProperty dateStatusUpdated;

    //----------------------------------------------------------------------------
    public ScoutTableModel(Vector<String> scoutData) {
        scoutId = new SimpleStringProperty(scoutData.elementAt(0));
        firstName = new SimpleStringProperty(scoutData.elementAt(1));
        middleName = new SimpleStringProperty(scoutData.elementAt(2));
        lastName = new SimpleStringProperty(scoutData.elementAt(3));
        dateOfBirth = new SimpleStringProperty(scoutData.elementAt(4));
        phoneNumber = new SimpleStringProperty(scoutData.elementAt(5));
        email = new SimpleStringProperty(scoutData.elementAt(6));
        status = new SimpleStringProperty(scoutData.elementAt(7));
        troopId = new SimpleStringProperty(scoutData.elementAt(8));
       // dateStatusUpdated = new SimpleStringProperty(scoutData.elementAt(9));


    }

    //----------------------------------------------------------------------------
    //public String getBookId() {
    // return accountNumber.get();
    // }

   public String getScoutId() {
        return scoutId.get();
    }

    //----------------------------------------------------------------------------
    public void setScoutId(String number) {
        scoutId.set(number);
    }

    //----------------------------------------------------------------------------


    public String getFirstName() {
        return firstName.get();
    }
    //----------------------------------------------------------------------------
    public void setFirstname(String fName){
        firstName.set(fName);
    }
    //----------------------------------------------------------------------------

    public String getMiddleName() {
        return middleName.get();
    }
    //----------------------------------------------------------------------------
    public void setMiddleName(String mName){
        middleName.set(mName);
    }
    //----------------------------------------------------------------------------

    public String getLastName() {
        return lastName.get();
    }
    public String getLastNameSort() {
        return lastName.get().toLowerCase();
    }
    //----------------------------------------------------------------------------
    public void setLastName(String lName){
        lastName.set(lName);
    }
    //----------------------------------------------------------------------------

    public String getDateOfBirth() {
        return dateOfBirth.get();
    }
    //----------------------------------------------------------------------------
    public void setDateOfBirth(String dob){
        dateOfBirth.set(dob);
    }
    //----------------------------------------------------------------------------

    public String getPhoneNumber() {
        return phoneNumber.get();
    }
    //----------------------------------------------------------------------------
    public void setPhoneNumber(String phone){
        phoneNumber.set(phone);
    }
    //----------------------------------------------------------------------------

    public String getEmail() {
        return email.get();
    }
    //----------------------------------------------------------------------------
    public void setEmail(String mail){
        email.set(mail);
    }
    //----------------------------------------------------------------------------

    public String getStatus() {
        return status.get();
    }
    //----------------------------------------------------------------------------


    public void setStatus(String stat){
        status.set(stat);
    }
    //----------------------------------------------------------------------------
    public String getTroopId(){
        return troopId.get();
    }
    public void setTroopId(String id){troopId.set(id);}
    //----------------------------------------------------------------------------
    /*public String getDateStatusUpdated(){
        return dateStatusUpdated.get();
    }
    //----------------------------------------------------------------------------
    public void setDateStatusUpdated(String date){
        dateStatusUpdated.set(date);
    }*/
}
