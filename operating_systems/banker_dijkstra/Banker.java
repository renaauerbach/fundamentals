/* Rena Auerbach - rma453
 * Prof. Shvartzshnaider
 * April 7, 2019
 * Lab 3 - Banker
 */

import java.io.*;
import java.util.*;

public class Banker {
	
	static int T;		//Number of Tasks in the program
	static int R;		//Number of Resource types (and number of initiate activities per Task)
	
	//HashMaps of all Resources and how many units available for each
	static LinkedHashMap<Integer, Integer> resourcesF = new LinkedHashMap<Integer, Integer>();	
	static LinkedHashMap<Integer, Integer> resourcesB = new LinkedHashMap<Integer, Integer>();	

	//ArrayLists for all Tasks in the program
	static ArrayList<Task> allTasks = new ArrayList<Task>();
			
	public static void main(String[] args) throws FileNotFoundException {
		
//		System.out.println("Enter the name of your input file: ");
//		Scanner sc = new Scanner(System.in);
//		String file = sc.next();
		String file = args[0];
		Scanner in = new Scanner(new File(file));    
		
		//Handles first line of input: total-tasks total-resources units-available
		String firstLine = in.nextLine();
		String [] first = firstLine.split(" ");		//Convert to array of strings
		int[] firstArr = new int[first.length];		//Convert to array of ints
		for (int f = 0; f < first.length; f++) {
			firstArr[f] = Integer.parseInt(first[f]);
		}
		
		T = firstArr[0];	//Total number of Tasks
		R = firstArr[1];	//Total number of Resource
		
		//Add all Tasks to ArrayList
		for (int t = 1; t <= T; t++){
			Task task = new Task(t);
			allTasks.add(task);
		}
		
		//Add all Resources to HashMaps
		for (int r = 1; r <= R; r++) {
			resourcesF.put(r, firstArr[r + 1]);
			resourcesB.put(r, firstArr[r + 1]);
		}
		
		while (in.hasNext()){
			
			String a = in.next();	//Activity type
			int t = in.nextInt();	//Task number
			int d = in.nextInt();	//Delay
			int r = in.nextInt();	//Resource type
			int x = in.nextInt();	//Initial claim; number requested; number released
			
			Activity act = new Activity(a, t, d, r, x);
			
			//Add activity to its respective Task
			allTasks.get(t-1).addActivyF(act);
			allTasks.get(t-1).addActivyB(act);
		}
					
		manager();
		banker();
		output();
		
		in.close();
//		sc.close();
	}
	
	//Task Class
	private static class Task {
		
		//ArrayLists of the Task's activities 
		private ArrayList<Activity> activitiesF = new ArrayList<Activity>();
		private ArrayList<Activity> activitiesB = new ArrayList<Activity>();
		
		//Linked HashMaps for the Task's allocated Resources and initial claims
		private LinkedHashMap<Integer, Integer> resAllocatedF = new LinkedHashMap<Integer, Integer>();	
		private LinkedHashMap<Integer, Integer> resAllocatedB = new LinkedHashMap<Integer, Integer>();	
		private LinkedHashMap<Integer, Integer> initClaims = new LinkedHashMap<Integer, Integer>();			//Initial claim for each resource

		private int num;				//Task number
		
		private boolean unsafe;			//If state is unsafe (BANKER ONLY)
		private boolean FAborted;		//If aborted by FIFO (for printing purposes)
		private boolean BAborted;		//If aborted by BANKER (for printing purposes)
		
		private int FWaitTime;			//FIFO wait time (time waitingint in FIFO)
		private int FStartTime;			//FIFO start time (Cycle val at initiation)
		private int FEndTime;			//FIFO end time (Cycle val at termination)
		private int FTurnAround;		//FIFO turnaround time (endTime - startTime)
		
		private int BWaitTime;			//BANKER wait time (time waiting in BANKER)
		private int BStartTime;			//BANKER start time	(Cycle val at initiation)
		private int BEndTime;			//BANKER end time (Cycle val at termination)
		private int BTurnAround;		//BANKER turnaround time (endTime - startTime)
		
		public Task(int n) {
			this.num = n;
			this.activitiesF = new ArrayList<Activity>();
			this.activitiesB = new ArrayList<Activity>();
			
			this.unsafe = false;
			this.FAborted = false;
			this.BAborted = false;
			
			this.FWaitTime = 0;	
			this.FStartTime = 0;		
			this.FEndTime = 0;			
			this.FTurnAround = 0;	
			
			this.BWaitTime = 0;		
			this.BStartTime = 0;		
			this.BEndTime = 0;			
			this.BTurnAround = 0;		
		}
		
		public int getNum() {
			return num;
		}

		public void addActivyF(Activity a) {
			this.activitiesF.add(a);
		}
		
		public void addActivyB(Activity a) {
			this.activitiesB.add(a);
		}

		public boolean isUnsafe() {
			return unsafe;
		}

		public void setUnsafe(boolean unsafe) {
			this.unsafe = unsafe;
		}

		public boolean isFAborted() {
			return FAborted;
		}

		public void setFAborted(boolean aborted) {
			this.FAborted = aborted;
		}
		
		public boolean isBAborted() {
			return BAborted;
		}

		public void setBAborted(boolean aborted) {
			this.BAborted = aborted;
		}

		public int getFWaitTime() {
			return FWaitTime;
		}

		public void incFWaitTime() {
			this.FWaitTime = this.FWaitTime + 1;
		}

		public int getFStartTime() {
			return FStartTime;
		}

		public void setFStartTime(int fStartTime) {
			this.FStartTime = fStartTime;
		}

		public int getFEndTime() {
			return FEndTime;
		}

		public void setFEndTime(int fEndTime) {
			this.FEndTime = fEndTime;
		}

		public int getFTurnAround() {
			return FTurnAround;
		}

		public void setFTurnAround(int fTurnAround) {
			this.FTurnAround = fTurnAround;
		}

		public int getBWaitTime() {
			return BWaitTime;
		}

		public void incBWaitTime() {
			this.BWaitTime = this.BWaitTime + 1;
		}

		public int getBStartTime() {
			return BStartTime;
		}

		public void setBStartTime(int bStartTime) {
			this.BStartTime = bStartTime;
		}

		public int getBEndTime() {
			return BEndTime;
		}

		public void setBEndTime(int bEndTime) {
			this.BEndTime = bEndTime;
		}

		public int getBTurnAround() {
			return BTurnAround;
		}

		public void setBTurnAround(int bTurnAround) {
			this.BTurnAround = bTurnAround;
		}
	}
	
	//Activity Class
	private static class Activity {
	
		private String act;				//Activity (initiate, request, release, terminate)
		private int taskNum;			//Task number
		private int delayF;				//FIFO Number of cycles b/w completion of prev activity & start of current activity
		private int delayB;				//BANKER Number of cycles b/w completion of prev activity & start of current activity
		private int resType;			//Resource type
		private int resNum;				//Initial claim; number requested; number released
		
		public Activity(String a, int t, int d, int r, int x) {
			this.act = a;
			this.taskNum = t;
			this.delayF = d;
			this.delayB = d;
			this.resType = r;
			this.resNum = x;
		}

		public String getAct() {
			return act;
		}

		public int getFDelay() {
			return delayF;
		}

		public void decFDelay() {
			this.delayF = this.delayF - 1;
		}

		public int getBDelay() {
			return delayB;
		}
		
		public void decBDelay() {
			this.delayB = this.delayB - 1;
		}
		
		public int getResType() {
			return resType;
		}

		public int getResNum() {
			return resNum;
		}
	}
	
	
	/* Manager fulfills requests while ignoring initial claims (doesn't check safety)
	 * Manager processes 1 activity for each Task per cycle (not including terminate)
	 * Handling requests (checks blocked Tasks first):
	 * 	If possible --> fulfill Task's request
	 * 	If not possible --> block Task (make wait)
	 * 	Check Deadlocks (if all Tasks are blocked):
	 * 		If deadlocked --> abort lowest numbered (first) Task
	 * 		Continue until deadlock ends
	 * Handling releases:
	 *	Resource is released --> available at next cycle to fulfill pending requests in FIFO manner
	 */	
	public static void manager() {
		
		int cycle = 0;		//Cycle counter
		
		//2D array of resources released during each cycle (so they aren't available until next cycle)
		int[][] resReleased = new int[R][1];					
		for (int z = 0; z < R; z++) {
			resReleased[z][0] = 0;
		}
		
		//ArrayList of all Tasks in handling order
		ArrayList<Task> taskOrder = new ArrayList<Task>();		
		for (Task t : allTasks) {
			taskOrder.add(t);
		}
		
		//Loops until all Tasks terminate
		while (taskOrder.size() > 0) {
			
			int blocked = 0;						//Counts number of blocked Tasks per cycle 
			int orderSize = taskOrder.size();		//Ensures Task termination doesn't change total number of Tasks used during a cycle 
			
			for (int i = 0; i < orderSize; i++) {
				
				Task t = taskOrder.get(blocked);			
				Activity a = t.activitiesF.get(0);
				
				//Check activity delay
				if (a.getFDelay() == 0) {
					
					//Initiate
					if (a.getAct().equalsIgnoreCase("initiate")) {
				
						//Set start time based on first initiation
						if (cycle == 0) {
							t.setFStartTime(cycle);	
						}
						t.activitiesF.remove(0);	//Remove current activity
						
						//Move task to end of order ArrayList
						Task addT = t;
						taskOrder.remove(t);				
						taskOrder.add(addT);				
					}
					//Request
					else if (a.getAct().equalsIgnoreCase("request")) {
						
						//Check if there's enough Resource units available to fulfill request 
						if (resourcesF.get(a.getResType()) >= a.getResNum()) {
							
							//Check if Task already has units of this Resource (replace vs put method)
							if (t.resAllocatedF.containsKey(a.getResType())) {
								int has = t.resAllocatedF.get(a.getResType());
								t.resAllocatedF.replace(a.getResType(), has + a.getResNum());
							}
							else {
								t.resAllocatedF.put(a.getResType(), a.getResNum());
							}
							resourcesF.replace(a.getResType(), resourcesF.get(a.getResType()) - a.getResNum());
							t.activitiesF.remove(0); 	//Remove current activity
							
							//Move task to end of order ArrayList
							Task addT = t;
							taskOrder.remove(t);				
							taskOrder.add(addT);
						}
						//Else (not enough units available) --> Block: make task wait
						else {
							t.incFWaitTime();			
							blocked++;
							continue;
						}
					}
					//Release
					else if (a.getAct().equalsIgnoreCase("release")) {
						
						//Check if releasing all allocated units of this Resource (replace vs remove method)
						if (t.resAllocatedF.get(a.getResType()) - a.getResNum() != 0) {
							int has = t.resAllocatedF.get(a.getResType());
							t.resAllocatedF.replace(a.getResType(), has - a.getResNum());
						}
						else {
							t.resAllocatedF.remove(a.getResType(), a.getResNum());
						}
						//Add released Resource to 2D array
						resReleased[a.getResType() - 1][0] += a.getResNum();
						t.activitiesF.remove(0);	//Remove current activity
						
						//Move task to end of order ArrayList
						Task addT = t;
						taskOrder.remove(t);				
						taskOrder.add(addT);
					}
					//Terminate
					else {							
						//Set end time
						t.setFEndTime(cycle);		
		
						//Set turnaround time
						t.setFTurnAround(t.getFEndTime() - t.getFStartTime());
						
						//Clear all activities & remove Task from order
						t.activitiesF.clear(); 			
						taskOrder.remove(t);				
					}
				}
				//Delay Task
				else {
					//Decrement delay
					a.decFDelay();
					
					//Move task to end of order ArrayList
					Task addT = t;
					taskOrder.remove(t);				
					taskOrder.add(addT);
				}
			}
			
			//End of cycle 
			//If all Tasks are blocked --> Deadlock: Abort first Task
			if (blocked == taskOrder.size() && taskOrder.isEmpty() == false) {	
				
				//Loop until Deadlock ends
				while (blocked > 1) {
					
					//Order Tasks by number
					for (int t1 = 0; t1 < taskOrder.size(); t1++) {
						Task T1 = taskOrder.get(t1);
						for (int t2 = t1 + 1; t2 < taskOrder.size(); t2++) {
							Task T2 = taskOrder.get(t2);
							if (T1.getNum() < T2.getNum()) {
								continue;
							}
							else {
								Task addT = T1;
								taskOrder.remove(T1);				
								taskOrder.add(addT);
							}
						}
					}
					Task t = taskOrder.get(0);
	
					//Abort task
					t.setFAborted(true); 
					
					//Release all of Task's Resources
					for (int r = 0; r < t.resAllocatedF.size(); r++) {
						resReleased[r][0] += t.resAllocatedF.get(r + 1);
					}
					
					//Clear all activities & remove Task from order
					t.resAllocatedF.clear(); 
					taskOrder.remove(t);
					blocked--;
				}
			}
			
			//Officially release Resources for next cycle & reset 2D array
			for (int r = 0; r < R; r++) {
				int add = resReleased[r][0];
				resourcesF.replace(r + 1, resourcesF.get(r + 1) + add); 
				resReleased[r][0] = 0;			
			}
		cycle++;
		}
	}

		
	/* Banker fulfills requests while checking for safety (considers initial claims)
	 * Banker processes 1 activity for each Task per cycle (not including terminate)
	 * Handling initiations:
	 * 	If initial claim > total available units of a Resource --> aborts Task
	 * Handling requests (checks blocked Tasks first):
	 * 	If total requests > initial claim --> aborts Task
	 * 	Checks safety (considers all Tasks at every request):
	 * 		If Task is in safe state --> fulfill request
	 * 		If unsafe --> block Task (make wait)
	 * 		If all tasks blocked --> abort lowest numbered (first) Task
	 * 			Continue until a request can be fulfilled
	 * Handling releases:
	 *	Resource is released --> available at next cycle to fulfill pending requests
	 */	
	public static void banker() {
				
		int cycle = 0;		//Cycle counter
		
		//2D array of resources released during each cycle (so they aren't available until next cycle)
		int[][] resReleased = new int[R][1];					
		for (int z = 0; z < R; z++) {
			resReleased[z][0] = 0;
		}
		
		//ArrayList of all Tasks in handling order
		ArrayList<Task> taskOrder = new ArrayList<Task>();		
		for (Task t : allTasks) {
			taskOrder.add(t);
		}
		
		//Loops until all Tasks terminate
		while (taskOrder.size() > 0) {
			
			int blocked = 0;						//Counts number of blocked Tasks per cycle 
			int orderSize = taskOrder.size();		//Ensures Task termination doesn't change total number of Tasks used during a cycle 
			
			for (int i = 0; i < orderSize; i++) {
				
				Task t = taskOrder.get(blocked);			
				Activity a = t.activitiesB.get(0);

				//Check activity delay
				if (a.getBDelay() == 0) {
					
					//Initiate
					if (a.getAct().equalsIgnoreCase("initiate")) {
						
						//Set start time based on first initiation
						if (cycle == 0) {
							t.setBStartTime(cycle);	
						}
						
						//Check if initClaim < resource max --> set initial claim
						if (a.getResNum() <= resourcesB.get(a.getResType())) {
							t.initClaims.put(a.getResType(), a.getResNum());
							t.activitiesB.remove(0);	//Remove current activity
							
							//Move task to end of order ArrayList
							Task addT = t;
							taskOrder.remove(t);				
							taskOrder.add(addT);
						}
						//Else (initClaim > resource max) --> abort task & release Resources
						else {							
							//Abort task
							t.setBAborted(true); 
							
							//Release all of Task's Resources
							for (int r = 0; r < t.resAllocatedB.size(); r++) {
								resReleased[r][0] += t.resAllocatedB.get(r + 1);
							}
							
							//Clear all activities & remove Task from order
							t.resAllocatedB.clear(); 
							taskOrder.remove(t);
							
							//Print message
							System.out.println("BANKER aborts task " + t.getNum() + " before run begins:");
							System.out.println("	Claim for resource " + a.getResType() + " (" + a.getResNum() + ") exceeds number of units available (" + resourcesB.get(a.getResType()) + ")");
							System.out.println();
						}
					}
					else if (a.getAct().equalsIgnoreCase("request")) {
						
						//Check if there's enough Resource units available to fulfill request 
						if (resourcesB.get(a.getResType()) >= a.getResNum()) {
							
							//Check safe state
							for (Task tc : taskOrder) {
								if (tc != t) {			//Skips comparing against itself
									int c = a.getResType();
										
									//Check if other Task has Resources (not NULL) && state isn't already declared unsafe 
									if (tc.resAllocatedB.isEmpty() == false && t.isUnsafe() == false) {	
										
										//Check if other Task has requested Resource
										if (tc.resAllocatedB.containsKey(c)) {
											
											//Number needed to achieve max claim = initial claim - units already allocated + units released during this cycle
											int maxNeeded = tc.initClaims.get(c) - tc.resAllocatedB.get(c) + resReleased[c - 1][0];
											
											//Check if total available units - number requested < maxNeeded --> unsafe
											if (resourcesB.get(a.getResType()) - a.getResNum() < maxNeeded) {
												t.incBWaitTime();			
												t.setUnsafe(true);
												blocked++;
												break;
											}
										}
										//If other Task doesn't have the requested Resource
										else {
											//Number needed to achieve max claim = initial claim + units released during this cycle
											int maxNeeded = tc.initClaims.get(c) + resReleased[c - 1][0];
											
											//If total available units - number requested < maxNeeded --> unsafe
											if (resourcesB.get(a.getResType()) - a.getResNum() < maxNeeded) {
												t.incBWaitTime();			
												t.setUnsafe(true);
												blocked++;
												break;
											}
										}
									}
								}
							}
							
							//If state is safe --> allocate Resources
							if (t.isUnsafe() == false) {
								
								//Check if Task already has units of this Resource (replace vs put method)
								if (t.resAllocatedB.containsKey(a.getResType())) {
									
									//Check if request + units already allocated <= initial claim --> fulfill request
									if ((a.getResNum() + t.resAllocatedB.get(a.getResType())) <= t.initClaims.get(a.getResType())) {
										int has = t.resAllocatedB.get(a.getResType());
										t.resAllocatedB.replace(a.getResType(), has + a.getResNum());
										
										resourcesB.replace(a.getResType(), resourcesB.get(a.getResType()) - a.getResNum());
										t.activitiesB.remove(0); 			//Remove current activity
										
										//Move task to end of order ArrayList
										Task addT = t;
										taskOrder.remove(t);				
										taskOrder.add(addT);
									}
									//Else (request + units already allocated > initial claim) --> abort Task
									else { 
										t.setBAborted(true);
										
										//Release all of Task's Resources
										for (int r = 0; r < t.resAllocatedB.size(); r++) {
											resReleased[r][0] += t.resAllocatedB.get(r + 1);
										}
										
										//Clear all activities & remove Task from order
										t.resAllocatedB.clear(); 
										taskOrder.remove(t);
										break;
									}
								}
								else {
									//If request <= initial claim --> fulfill request
									if (a.getResNum() <= t.initClaims.get(a.getResType())) {
										t.resAllocatedB.put(a.getResType(), a.getResNum());	
										
										resourcesB.replace(a.getResType(), resourcesB.get(a.getResType()) - a.getResNum());
										t.activitiesB.remove(0); 			//Remove current activity
										
										//Move task to end of order ArrayList
										Task addT = t;
										taskOrder.remove(t);				
										taskOrder.add(addT);
									}
									//Else (request > initial claim) --> abort Task
									else {
										t.setBAborted(true);
										
										//Release all of Task's Resources
										for (int r = 0; r < t.resAllocatedB.size(); r++) {
											resReleased[r][0] += t.resAllocatedB.get(r + 1);
										}
										
										//Clear all activities & remove Task from order
										t.resAllocatedB.clear(); 
										taskOrder.remove(t);
										break;
									}
								}
							}
						}
						//Else ((not enough units available) --> Block: make task wait
						else {
							t.incBWaitTime();			
							blocked++;
							continue;
						}
					}
					//Release
					else if (a.getAct().equalsIgnoreCase("release")) {
						
						//Check if releasing all allocated units of this Resource (replace vs remove method)
						if (t.resAllocatedB.get(a.getResType()) - a.getResNum() != 0) {
							int has = t.resAllocatedB.get(a.getResType());
							t.resAllocatedB.replace(a.getResType(), has - a.getResNum());
						}
						else {
							t.resAllocatedB.remove(a.getResType(), a.getResNum());
						}
						//Add released Resource to 2D array
						resReleased[a.getResType() - 1][0] += a.getResNum();
						t.activitiesB.remove(0);	//Remove current activity
						
						//Move task to end of order ArrayList
						Task addT = t;
						taskOrder.remove(t);				
						taskOrder.add(addT);
					}
					//Terminate
					else {							
						//Set end time
						t.setBEndTime(cycle);		
		
						//Set turnaround time
						t.setBTurnAround(t.getBEndTime() - t.getBStartTime());
						
						//Clear all activities & remove Task from order
						t.activitiesB.clear(); 			
						taskOrder.remove(t);
					}
				}
				//Delay Task
				else {
					//Decrement delay
					a.decBDelay();
					
					//Move task to end of order ArrayList
					Task addT = t;
					taskOrder.remove(t);				
					taskOrder.add(addT);
				}
				//Reset unsafe state
				t.setUnsafe(false);
			}
			//End of cycle 
			//If all Tasks are blocked --> abort first Task
			if (blocked == orderSize && taskOrder.isEmpty() == false) {	
				
				//Loop until pending request can be fulfilled
				while (blocked > 1) {
				
					//Order Tasks by number
					for (int t1 = 0; t1 < taskOrder.size(); t1++) {
						Task T1 = taskOrder.get(t1);
						for (int t2 = t1 + 1; t2 < taskOrder.size(); t2++) {
							Task T2 = taskOrder.get(t2);
							if (T1.getNum() < T2.getNum()) {
								continue;
							}
							else {
								Task addT = T1;
								taskOrder.remove(T1);				
								taskOrder.add(addT);
							}
						}
					}
					Task t = taskOrder.get(0);
	
					//Abort task
					t.setBAborted(true); 
					
					//Release all of Task's Resources
					for (int r = 0; r < t.resAllocatedB.size(); r++) {
						resReleased[r][0] += t.resAllocatedB.get(r + 1);
					}
					
					//Clear all activities & remove Task from order
					t.resAllocatedB.clear(); 
					taskOrder.remove(t);
					blocked--;
				}
			}
			
			//Officially release Resources for next cycle & reset 2D array
			for (int r = 0; r < R; r++) {
				int add = resReleased[r][0];
				resourcesB.replace(r + 1, resourcesB.get(r + 1) + add); 
				resReleased[r][0] = 0;			
			}
			cycle++;
		}
	}
	
	//Function to print final data results to screen
	public static void output() {
		
		//Variables to calculate total turnaround times and wait times for FIFO
		double FTotalTime = 0;
		double FTotalWaitTime = 0;
		
		//Variables to calculate total turnaround times and wait times for BANKER
		double BTotalTime = 0;
		double BTotalWaitTime = 0;
		 
		
		System.out.println("	FIFO				BANKER'S");
		for (int i = 0; i < allTasks.size(); i++) {
			
			Task t = allTasks.get(i);
			
			//Print FIFO data
			//Print message is task was aborted
			if (t.isFAborted()) {
				System.out.print("Task " + t.getNum() + "     aborted 		");
			}
			//Else --> print data
			else {
				System.out.print("Task " + t.getNum() + "     " + t.getFTurnAround() + "   " + t.getFWaitTime() + "   " + Math.round(((double) t.getFWaitTime() * 100 / (double) t.getFEndTime())) + "% 		");
				
				FTotalTime += t.getFTurnAround();
				FTotalWaitTime += t.getFWaitTime();
			}
			
			//Print BANKER'S data
			//Print message is task was aborted
			if (t.isBAborted()) {
				System.out.println("Task " + t.getNum() + "     aborted");
			}
			//Else --> print data
			else {
				System.out.println("Task " + t.getNum() + "     " + t.getBTurnAround() + "   " + t.getBWaitTime() + "   " + Math.round(((double) t.getBWaitTime() * 100 / (double) t.getBEndTime())) + "% ");
				
				BTotalTime += t.getBTurnAround();
				BTotalWaitTime += t.getBWaitTime();
			}
		}
		
		double FAvgWait = 0;
		double BAvgWait = 0;
		
		//Conditional statement for arithmetic purposes only
		//Caluculating total average wait times for each function 
		if (FTotalTime == 0 && BTotalTime != 0) {
			BAvgWait = Math.round((BTotalWaitTime * 100) / BTotalTime);
		}
		else if (FTotalTime != 0 && BTotalTime == 0) {
			FAvgWait = Math.round((FTotalWaitTime * 100) / FTotalTime);
		}
		else {
			FAvgWait = Math.round((FTotalWaitTime * 100) / FTotalTime);
			BAvgWait = Math.round((BTotalWaitTime * 100) / BTotalTime);
		}

		System.out.print("total	   " + (int) FTotalTime + "   " + (int) FTotalWaitTime + "   " + (int) FAvgWait + "%		");
		System.out.println("total	   " + (int) BTotalTime + "   " + (int) BTotalWaitTime + "   " + (int) BAvgWait + "%");
	}
}
