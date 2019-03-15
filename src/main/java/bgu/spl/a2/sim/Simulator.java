/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import bgu.spl.a2.Action;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.callback;
import jsonConverter.*;
import bgu.spl.a2.sim.actions.*;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;



/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {

	
	public static ActorThreadPool actorThreadPool;
	private static Warehouse warehouse;
	private static List<JsonComputer> computers;
	private static List<Phase> phase1;
	private static List<Phase> phase2;
	private static List<Phase> phase3;
	
	
	/**
	* Begin the simulation Should not be called before attachActorThreadPool()
	*/
    public static void start(){
		phaseSender(phase1);
		phaseSender(phase2);
		phaseSender(phase3);
    }
	
	/**
	* attach an ActorThreadPool to the Simulator, this ActorThreadPool will be used to run the simulation
	* 
	* @param myActorThreadPool - the ActorThreadPool which will be used by the simulator
	*/
	public static void attachActorThreadPool(ActorThreadPool myActorThreadPool){
		actorThreadPool = myActorThreadPool;
	}
	
	/**
	* shut down the simulation
	* returns list of private states
	*/
	public static HashMap<String,PrivateState> end(){
		try {
			actorThreadPool.shutdown();
			
		} catch (InterruptedException e) {}
		HashMap<String, PrivateState> toReturn =  new HashMap<String, PrivateState>();
		toReturn.putAll(actorThreadPool.getActors());
		return toReturn;
	}
	
	
	public static int main(String [] args){
		Gson gson = new Gson();
		Reader reader = null;
		try {
			 reader = new FileReader(args[0]);		
		} catch (FileNotFoundException e) {
			System.out.println("file not found");
			return 0;}
		JsonConverter translation = gson.fromJson(reader, JsonConverter.class);
		int threads = translation.getThreads().intValue();
		computers = translation.getComputers();
		warehouse = new Warehouse(computers.size());
		for (int i=0; i<computers.size(); i++) {
			Computer newComp = translateComp(computers.get(i));
			warehouse.getComputers()[i] = newComp;
		}
		phase1 = translation.getPhase1();
		phase2 = translation.getPhase2();
		phase3 = translation.getPhase3();
		ActorThreadPool newPool = new ActorThreadPool(threads);
		attachActorThreadPool(newPool);
		actorThreadPool.start();
		start();
		HashMap <String, PrivateState> SimulationResult;
		SimulationResult = end();
		FileOutputStream fout=null;
		try {
			fout = new FileOutputStream ("result.ser") ;
			ObjectOutputStream	oos = new ObjectOutputStream ( fout ) ;
			oos.writeObject ( SimulationResult ) ;
		} catch (FileNotFoundException e) {System.out.println("FileNotFoundException");}
		catch (IOException e) {System.out.println("IOException");}
		return 0;
	}
	
	private static Computer translateComp(JsonComputer computer) {
		
		Computer javaComp = new Computer(computer.getType());
		long successSig = Long.parseLong(computer.getSigSuccess());
		javaComp.setSuccessSig(successSig);
		long failSig = Long.parseLong(computer.getSigFail());
		javaComp.setFailSig(failSig);
		return javaComp;
		
	}
	
	/**
	 * 
	 * @param phase 
	 * 			phase is the action received from the json file
	 * @return the translated action
	 */
	
	private static Action<Object> translateToAction(Phase phase)
	{		
		String actionName = phase.getAction();
		Action<Object> action = null;
		String courseName = null;
		String stringSpots = null; //transforms to availableSpots Integer
		List<String> prequisites = null;
		String studentName = null;
		List<String> stringGrade = null; //transforms to Integer grade or List<Integer> grades
		String stringSpaces = null; // transforms to additionalSpaces int
		List<String> preferences = null;
		String computerType = null;
		List<String> coursesList = null;
		List<String> studentsAsList = null;
		String initialActorID = null;
		
		switch (actionName) {
		case "Open Course":
			courseName = phase.getCourse();
			prequisites = phase.getPrerequisites();
			stringSpots = phase.getSpace();
			Integer availableSpots = Integer.parseInt(stringSpots);
			initialActorID = phase.getDepartment();
			action = new OpenANewCourse(courseName, prequisites, availableSpots, initialActorID);
			break;
			
		case "Add Student":
			studentName = phase.getStudent();
			initialActorID = phase.getDepartment();
			action = new AddStudent(studentName, initialActorID);
			break;
		
		case "Participate In Course":
			studentName = phase.getStudent();
			stringGrade = phase.getGrade();
			Integer grade = -1;
			if (!stringGrade.get(0).equals("-")) {
				grade = Integer.parseInt(stringGrade.get(0));
			}
			initialActorID = phase.getCourse();
			action = new ParticipatingInCourse(studentName, grade, initialActorID);
			break;
			
		case "Add Spaces":
			stringSpaces = phase.getNumber();
			int additionalSpaces = Integer.parseInt(stringSpaces);
			initialActorID = phase.getCourse();
			action = new OpenNewPlaceInACourse(additionalSpaces, initialActorID);
			break;
			
		case "Register With Preferences":
			preferences = phase.getPreferences();
			List<Integer> grades = new LinkedList<Integer>();
			stringGrade = phase.getGrade();
			for(int i=0; i<phase.getGrade().size(); i++) {
				Integer gradeToAdd = -1;
				if (!stringGrade.get(i).equals("-")) {
					gradeToAdd = Integer.parseInt(stringGrade.get(i));
				}
				grades.add(gradeToAdd);
			}
			initialActorID = phase.getStudent();
			action = new RegisterWithPreferences(preferences, grades, initialActorID);
			break;
		
		case "Unregister":
			studentName = phase.getStudent();
			initialActorID = phase.getCourse();
			action = new Unregister(studentName, initialActorID);
			break;
		
		case "Close Course":
			courseName = phase.getCourse();
			initialActorID = phase.getDepartment();
			action = new CloseACourse(courseName, initialActorID);
			break;
			
		case "Administrative Check":
			studentsAsList = phase.getStudents();
			String[] studentsList = new String[studentsAsList.size()];
			for (int i=0; i<studentsList.length; i++) {
				studentsList[i] = studentsAsList.get(i);
			}
			computerType = phase.getComputer();
			coursesList = phase.getConditions();
			initialActorID = phase.getDepartment();
			action = new CheckAdministrativeObligations(studentsList, computerType, coursesList, warehouse, initialActorID);
			break;	
		}
		return action;
	}
	
	/**
	 * 
	 * @param phases
	 * 				the list of the actions as received from the json file
	 * 
	 * sends each action to the actorThreadPool
	 */
	
	private static void phaseSender(List<Phase> phases) {
		CountDownLatch latch = new CountDownLatch(phases.size());
		for(int i = 0; i<phases.size() ; i++) {
			Action<Object> nextAction = translateToAction(phases.get(i));
			callback latchDowner = () ->{
				latch.countDown();
			};
			nextAction.getResult().subscribe(latchDowner);
			if (!actorThreadPool.getActors().containsKey(nextAction.getMyActorID())) {
				DepartmentPrivateState departmentState = new DepartmentPrivateState();
				actorThreadPool.submit(nextAction,nextAction.getMyActorID() ,departmentState );
			}
			else {
				actorThreadPool.submit(nextAction,nextAction.getMyActorID(), actorThreadPool.getPrivateState(nextAction.getMyActorID()));
			}
		}
		try {
			latch.await();
		} 
		catch (InterruptedException e) {}
	}
}
