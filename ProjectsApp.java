package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import projects.entity.Project;
import projects.exception.DbException;
import projects.service.projectService;

//import projects.dao.DbConnection;

public class ProjectsApp {
	
	// @formatter:off
	private List<String> operations = List.of(
			"1) Add a Project",
			"2) List Projects",
			"3) Select a Project"
			
			);
	// @formatter:on
	private Project curProject;
	
	
	Scanner scanner = new Scanner (System.in);
	
	private projectService projectService = new projectService();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//DbConnection.getConnection();
		
		
	
		
		new ProjectsApp().processUserSelections();
		
		
	}

	private void processUserSelections() {
		
		boolean done = false;
		
		while (!done) {
			try {
				int selection = getUserSelection();
				
				switch (selection){
				
				case -1:
					done = exitMenu();
					break;
				case 1:
					createProject();
					break;
				case 2:
					listProjects();
					break;
				case 3:
					selectProject();
					break;
				default:
					System.out.println("/n" + selection + " is not a valid selection. Try again.");
				}
				
			}catch(Exception e) {
				System.out.println("\nError: " + e + " Try Again.");
			}
		}
		
	}

	private void selectProject() {
		listProjects();
        Integer projectId = getIntInput("Enter a project ID to select a project");

        curProject = null; 

        curProject = projectService.fetchProjectById(projectId);
		
	}

	private void listProjects() {
		List<Project> projects = projectService.fetchAllProjects();
		
		System.out.println("\nProjects");
		
		projects.forEach(project -> System.out.println("   " + project.getProjectId()+ ": " + project.getProjectName()));
		
	}

	private boolean exitMenu() {
		// TODO Auto-generated method stub
		System.out.println("Exiting menu.");
		
	return true;
	}

	private void createProject() {
		String projectName = getStringInput ("Enter the project name");
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
		BigDecimal actualHours = getDecimalInput("Enter the actual hours");
		Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
		String notes = getStringInput ("Enter the project notes");
		
		Project project = new Project();
		
		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);
		
		Project dbProject = projectService.addProject(project);
		System.out.println("You have succesfully created a project: " + dbProject);
		
		
	}

	private BigDecimal getDecimalInput(String prompt) {
		String input = getStringInput(prompt);
		
		if(Objects.isNull(input)) {
			return null;
		}
		
		try {
			return new BigDecimal(input).setScale(2);
		}catch(NumberFormatException e) {
			throw new DbException (input + " is not a valid decimal number.");
		}
		
		
	}

	private int getUserSelection() {
		printOperations();
		
		Integer input = getIntInput("Enter a menu selection");
		
		return Objects.isNull(input) ? -1 : input;
	}

	private Integer getIntInput(String prompt) {
		String input = getStringInput(prompt);
		
		if(Objects.isNull(input)) {
			return null;
		}
		
		try {
			return Integer.valueOf(input);
		}catch(NumberFormatException e) {
			throw new DbException (input + " is not a valid number.");
		}
		
		
	}

	private String getStringInput(String prompt) {
		//Scanner scanner = new Scanner(System.in);
		System.out.print(prompt + ": ");
		String input = scanner.nextLine();
		
		return input.isBlank() ? null : input.trim();
	}

	private void printOperations() {
		
		System.out.println("\nThese are the available selections. Press Enter key to quit:");
		
		operations.forEach(line -> System.out.println("   " + line));
		
		if(Objects.isNull(curProject)) {
			System.out.println("\nYou are not working with a project.");
		}else {
			System.out.println("\nYou are working with project: " + curProject);
		}
		
	}

}
