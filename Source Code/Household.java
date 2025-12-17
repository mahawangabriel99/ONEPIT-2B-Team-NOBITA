import java.util.Date;

public class Household {
    private String householdId;
    private String headFirstName;
    private String headLastName;
    private String headMiddleName;
    private Integer headAge;
    private String headGender;
    private String headEducationLevel;
    private String headOccupation;
    private String purok;
    private String houseNumber;
    private String contactNumber;
    private String address;
    private int membersCount;
    private String status;
    private Date registrationDate;
    private int createdBy;
    private Date createdAt;
    private Date updatedAt;
    
    // Constructors
    public Household() {}
    
    // Getters and Setters
    public String getHouseholdId() { return householdId; }
    public void setHouseholdId(String householdId) { this.householdId = householdId; }
    
    public String getHeadFirstName() { return headFirstName; }
    public void setHeadFirstName(String headFirstName) { this.headFirstName = headFirstName; }
    
    public String getHeadLastName() { return headLastName; }
    public void setHeadLastName(String headLastName) { this.headLastName = headLastName; }
    
    public String getHeadMiddleName() { return headMiddleName; }
    public void setHeadMiddleName(String headMiddleName) { this.headMiddleName = headMiddleName; }
    
    public Integer getHeadAge() { return headAge; }
    public void setHeadAge(Integer headAge) { this.headAge = headAge; }
    
    public String getHeadGender() { return headGender; }
    public void setHeadGender(String headGender) { this.headGender = headGender; }
    
    public String getHeadEducationLevel() { return headEducationLevel; }
    public void setHeadEducationLevel(String headEducationLevel) { this.headEducationLevel = headEducationLevel; }
    
    public String getHeadOccupation() { return headOccupation; }
    public void setHeadOccupation(String headOccupation) { this.headOccupation = headOccupation; }
    
    public String getPurok() { return purok; }
    public void setPurok(String purok) { this.purok = purok; }
    
    public String getHouseNumber() { return houseNumber; }
    public void setHouseNumber(String houseNumber) { this.houseNumber = houseNumber; }
    
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public int getMembersCount() { return membersCount; }
    public void setMembersCount(int membersCount) { this.membersCount = membersCount; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Date getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(Date registrationDate) { this.registrationDate = registrationDate; }
    
    public int getCreatedBy() { return createdBy; }
    public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    
    // Helper methods
    public String getHeadFullName() {
        StringBuilder name = new StringBuilder();
        if (headLastName != null && !headLastName.trim().isEmpty()) {
            name.append(headLastName.trim());
            name.append(", ");
        }
        if (headFirstName != null && !headFirstName.trim().isEmpty()) {
            name.append(headFirstName.trim());
        }
        if (headMiddleName != null && !headMiddleName.trim().isEmpty()) {
            name.append(" ");
            name.append(headMiddleName.trim());
        }
        return name.length() > 0 ? name.toString() : "N/A";
    }
}