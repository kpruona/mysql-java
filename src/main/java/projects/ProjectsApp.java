package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;


public class ProjectsApp {

	private Scanner scanner = new Scanner(System.in);
	private ProjectService projectService = new ProjectService();
	private Project curProject;

	// @formatter:off
	private List<String> operations = List.of(
			"1) Add a project",
			"2) List projects",
			"3) Select a project",
			//vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
			"4) Update project details",
			"5) Delete project"
			//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
			);
	// @formatter:on


	public static void main(String[] args) {

		new ProjectsApp().processUserSelections();

	}


	private void processUserSelections() {
		boolean done = false;

		while (!done) {
	        try {
	            int selection = getUserSelection();
	            switch (selection) {
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
	                case 4:
	                    updateProjectDetails();
	                    break; 
	                case 5:
	                    deleteProject();
	                    break; 
	                default:
	                    System.out.println("\n" + selection + " is not a valid selection. Try again");
	                    break;
	            }
	        } catch (DbException e) {
	            System.out.println("\nError: " + e.getMessage() + " Try again.");
	        } catch (Exception e) {
	            System.out.println("\nError: " + e + " Try again.");
	        }
	    }
	}
//vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

	private void updateProjectDetails() {

	    if(Objects.isNull(curProject)) {
	      System.out.println("\nPlease select a project.");
	      return;
	    }

	    String projectName =
	        getStringInput("Enter the project name [" + curProject.getProjectName() + "]");

	    BigDecimal estimatedHours =
	        getDecimalInput("Enter the estimated hours [" + curProject.getEstimatedHours() + "]");

	    BigDecimal actualHours =
	        getDecimalInput("Enter the actual hours + [" + curProject.getActualHours() + "]");

	    Integer difficulty =
	        getIntInput("Enter the project difficulty (1-5) [" + curProject.getDifficulty() + "]");

	    String notes = getStringInput("Enter the project notes [" + curProject.getNotes() + "]");

	    
	    Project project = new Project();

	    project.setProjectId(curProject.getProjectId());
	    project.setProjectName(Objects.isNull(projectName) ? curProject.getProjectName() : projectName);

	    project.setEstimatedHours(
	        Objects.isNull(estimatedHours) ? curProject.getEstimatedHours() : estimatedHours);

	    project.setActualHours(Objects.isNull(actualHours) ? curProject.getActualHours() : actualHours);
	    project.setDifficulty(Objects.isNull(difficulty) ? curProject.getDifficulty() : difficulty);
	    project.setNotes(Objects.isNull(notes) ? curProject.getNotes() : notes);

	    projectService.modifyProjectDetails(project);

	    curProject = projectService.fetchProjectById(curProject.getProjectId());
	  }
	
	
	
	
	
	
	private void deleteProject() {
		listProjects();

	    Integer projectId = getIntInput("Enter the ID of the project to delete");
// create deleteProject in ProjectService
	    projectService.deleteProject(projectId);
	    System.out.println("Project " + projectId + " was deleted successfully.");
	  
	    if(Objects.nonNull(curProject) && curProject.getProjectId().equals(projectId)) {
	      curProject = null;
	    }
	  
		
	}

//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^


	private void selectProject() {
		listProjects();
		Integer projectId = getIntInput("Enter a project ID to select a project");
		curProject = null;
		curProject = projectService.fetchProjectById(projectId);


		if(Objects.isNull(curProject)) {
			System.out.println("\nYou are not working with a project.");
		} else {
			System.out.println("You are working with project: " + curProject);
		}
	}

	private void listProjects() {
		List<Project> projects = projectService.fetchAllProjects();

		System.out.println("\nProjects:");

		projects.forEach(project -> System.out.println("   " + project.getProjectId() 
		+ ": " + project.getProjectName()));


	}


	// This will get user input for the project 

	private void createProject() {
		String projectName = getStringInput("Enter the project name");
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
		BigDecimal actualHours = getDecimalInput("Enter the actual hours");
		Integer difficulty = getIntInput("Enter the difficulty of the project (1-5)");
		String notes = getStringInput("Enter the project notes");

		Project project = new Project();

		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);

		Project dbProject = projectService.addProject(project);
		System.out.println("You have successfully created the project: " + dbProject);	
	}



	// gets user input and converts it to BigDecimal:

	private BigDecimal getDecimalInput(String prompt) {
		String input = getStringInput(prompt);

		if(Objects.isNull(input)) {
			return null;
		}

		try {
			// creates BigDecimal with two decimal places
			return new BigDecimal(input).setScale(2);
		}
		catch(NumberFormatException e) {
			throw new DbException(input + " is not a valid decimal number.");
		}

	}



	// lets the user exit the menu:
	private boolean exitMenu() {
		System.out.println("Exiting the menu.");
		return true;
	}

	// print the available menu selections:
	private int getUserSelection() {
		printOperations();
		Integer input = getIntInput("Enter a menu selection");
		return Objects.isNull(input) ? -1 : input;
	}

	// prints a prompt on the console and gets user input

	private Integer getIntInput(String prompt) {
		String input = getStringInput(prompt);

		if(Objects.isNull(input)) {
			return null;
		}
		try {
			return Integer.valueOf(input);
		}
		catch(NumberFormatException e) {
			throw new DbException(input + " is not a valid number");
		}
	}

	// print a prompt to get user input:

	private String getStringInput(String prompt) {
		System.out.println(prompt + ": ");
		String input = scanner.nextLine();

		return input.isBlank() ? null : input.trim();
	}


	// prints the menu selections:
	private void printOperations() {
		System.out.println("\nThese are the available selections. Press Enter to quit:");

		operations.forEach(line -> System.out.println("  " + line));
	}

}
