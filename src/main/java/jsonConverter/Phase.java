package jsonConverter;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class Phase {

	
	@SerializedName("Action")
	@Expose
	private String action;
	@SerializedName("Department")
	@Expose
	private String department;
	@SerializedName("Course")
	@Expose
	private String course;
	@SerializedName("Space")
	@Expose
	private String space;
	@SerializedName("Prerequisites")
	@Expose
	private List<String> prerequisites = null;
	@SerializedName("Student")
	@Expose
	private String student;
	@SerializedName("Grade")
	@Expose
	private List<String> grade = null;
	@SerializedName("Number")
	@Expose
	private String number;
	@SerializedName("Preferences")
	@Expose
	private List<String> preferences = null;
	@SerializedName("Computer")
	@Expose
	private String computer;
	@SerializedName("Conditions")
	@Expose
	private List<String> conditions = null;
	@SerializedName("Students")
	@Expose
	private List<String> students = null;
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public String getSpace() {
		return space;
	}
	public void setSpace(String space) {
		this.space = space;
	}
	public List<String> getPrerequisites() {
		return prerequisites;
	}
	public void setPrerequisites(List<String> prerequisites) {
		this.prerequisites = prerequisites;
	}
	public String getStudent() {
		return student;
	}
	public void setStudent(String student) {
		this.student = student;
	}
	public List<String> getGrade() {
		return grade;
	}
	public void setGrade(List<String> grade) {
		this.grade = grade;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public List<String> getPreferences() {
		return preferences;
	}
	public void setPreferences(List<String> preferences) {
		this.preferences = preferences;
	}
	public String getComputer() {
		return computer;
	}
	public void setComputer(String computer) {
		this.computer = computer;
	}
	public List<String> getConditions() {
		return conditions;
	}
	public void setConditions(List<String> conditions) {
		this.conditions = conditions;
	}
	public List<String> getStudents() {
		return students;
	}
	public void setStudents(List<String> students) {
		this.students = students;
	}

}
