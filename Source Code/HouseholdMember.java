import java.sql.Date;

public class HouseholdMember {
    private int memberId;
    private String householdId;
    private String firstName;
    private String lastName;
    private String middleName;
    private int age;
    private String gender;
    private String relationship;
    private String occupation;
    private String educationLevel;
    private Date birthDate;
    private boolean isHead;
    
    // Constructors
    public HouseholdMember() {}
    
    // Getters and Setters
    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }
    
    public String getHouseholdId() { return householdId; }
    public void setHouseholdId(String householdId) { this.householdId = householdId; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }
    
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getRelationship() { return relationship; }
    public void setRelationship(String relationship) { this.relationship = relationship; }
    
    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }
    
    public String getEducationLevel() { return educationLevel; }
    public void setEducationLevel(String educationLevel) { this.educationLevel = educationLevel; }
    
    public Date getBirthDate() { return birthDate; }
    public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }
    
    public boolean isHead() { return isHead; }
    public void setHead(boolean head) { isHead = head; }
    
    // Helper method
    public String getFullName() {
        return lastName + ", " + firstName + 
               (middleName != null && !middleName.isEmpty() ? " " + middleName : "");
    }
}