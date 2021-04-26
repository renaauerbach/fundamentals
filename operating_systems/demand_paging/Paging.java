/* Rena Auerbach - rma453
 * Prof. Shvartzshnaider
 * April 18, 2019
 * Lab 4 - Demand Paging
 */

import java.io.*;
import java.util.*;

public class Paging {
	
	static int M;				//Machine size
	static int P;				//Page size
	static int S;				//Size of each process
	static int J;				//Job mix - determines A, B, C
	static int N;				//Number of references per process
	static String R;			//Replacement alg (FIFO, RANDOM, LRU)
	
	static int numProcess;				//Number of processes
	static int numFrames;				//Number of frames (M / P)
	static int numPages;				//Number of pages (S / P)
	
	static int totalRefs;				//Total refs made during program (total program time) 
	static int totalFaults = 0;			//Total page faults in the program
	static int totalEvicted = 0;		//Total processes evicted
	static double totalAvgRes = 0;		//Total average residency
	static int random = 1;
	
	static ArrayList<Process> processes = new ArrayList<Process>();		//ArrayList of all processes
	static ArrayList<Frame> frameTBL = new ArrayList<Frame>();			//ArrayList of all frames
	
	static ArrayList<Integer> words = new ArrayList<Integer>();		//All words used in the machine
	static ArrayList<Integer> pages = new ArrayList<Integer>();		//All pages used in the machine

	public static void main(String[] args) throws FileNotFoundException {
		
		/* Driver:
		 * 	Reads all inputs from command line
		 * 	Simulates N memory references per process
		 * 	Produces all output
		 * 	Uses RR scheduling with q = 3 (number of references)
		 */
		
		M = Integer.parseInt(args[0]);	
		P = Integer.parseInt(args[1]);	
		S = Integer.parseInt(args[2]);	
		J = Integer.parseInt(args[3]);	
		N = Integer.parseInt(args[4]);	
		R = args[5];	
		
		numFrames = M / P;
		numPages = S / P;
		
		//Possible process combinations (J = job mix)
		//Completely sequential
		if (J == 1) {
			numProcess = 1;
			Process proc = new Process(1);
			proc.setA(1);
			proc.setB(0);
			proc.setC(0);
			
			processes.add(proc);		//Add to process arrays
		}
		//Sequential
		else if (J == 2) {
			numProcess = 4;
			for (int n = 1; n <= numProcess; n++) {
				Process proc = new Process(n);
				proc.setA(1);
				proc.setB(0);
				proc.setC(0);

				processes.add(proc);	//Add to process arrays
			}
		}
		//Completely random references
		else if (J == 3) {
			numProcess = 4;
			for (int n = 1; n <= numProcess; n++) {
				Process proc = new Process(n);
				proc.setA(0);
				proc.setB(0);
				proc.setC(0);

				processes.add(proc);	//Add to process arrays
			}
		}
		else if (J == 4) {
			numProcess = 4;
			for (int n = 1; n <= numProcess; n++) {
				Process proc = new Process(n);
				if (n == 1) {
					proc.setA(0.75);
					proc.setB(0.25);
					proc.setC(0);
				}
				else if (n == 2) {
					proc.setA(0.75);
					proc.setB(0);
					proc.setC(0.25);
				}
				else if (n == 3) {
					proc.setA(0.75);
					proc.setB(0.125);
					proc.setC(0.125);
				}
				else {
					proc.setA(0.5);
					proc.setB(0.125);
					proc.setC(0.125);
				}
				processes.add(proc);	//Add to process arrays
			}
		}
		
		totalRefs = N * numProcess;		

		//Populating frames table
		for (int i = 0; i < numFrames; i++) {
			Frame f = new Frame(i);
			frameTBL.add(f);
		}

		pager();
		output();
	}

	//Process class
	private static class Process { 
		
		private int id;					//Process number
		private double A;				//Reps sequential memory ref (refs the address one higher than current)
		private double B;				//Reps backward branch (refs a nearby lower address)
		private double C;				//Reps a jump around a then/else block (refs a nearby higher address)
		
		private int quantum = 3;
		private int nextIdx;
		private int totalRefs;			//How many refs this process has left
		private boolean first = true;	//For when process makes its first ref (automatic fault)
		
		//Arrays used for LRU and FIFO
		private int[] words = new int[N];				//Stores words referenced by each process
		private int[] pages = new int[N];				//Stores pages used by each process
		private int[] loadTimes = new int[numPages];	//Stores load time of each page
		
		private int numPageFault; 		//Number of page faults
		private int numEvict;			//Number of evictions
		private double resTime;			//Residency time (time evicted - time loaded)
		
		public Process(int n) {
			this.id = n;
			this.nextIdx = 0;
			this.resTime = 0;
			this.numEvict = 0;
			this.numPageFault = 0;
			this.totalRefs = N;
		}

		public int getID() {
			return id;
		}

		public double getA() {
			return A;
		}

		public void setA(double a) {
			A = a;
		}

		public double getB() {
			return B;
		}

		public void setB(double b) {
			B = b;
		}

		public double getC() {
			return C;
		}

		public void setC(double c) {
			C = c;
		}

		public int getNumPageFault() {
			return numPageFault;
		}

		public void incNumPageFault() {
			this.numPageFault = this.numPageFault + 1;
		}

		public int getNumEvict() {
			return numEvict;
		}

		public void incNumEvict() {
			this.numEvict = numEvict + 1;
		}
		
		public int getQuantum() {
			return quantum;
		}

		public void setQuantum(int q) {
			this.quantum = q;
		}

		public int getNextIdx() {
			return nextIdx;
		}

		public void decQuantum() {
			this.quantum = this.quantum - 1;
		}

		public int getRefsLeft() {
			return totalRefs;
		}

		public void decRefsLeft() {
			this.totalRefs = this.totalRefs - 1;
		}
		
		public boolean isFirst() {
			return first;
		}

		public double getResTime() {
			return resTime;
		}
		
		public void incResTime(int t) {
			this.resTime = this.resTime + t;
		}
	}
	
	//Frame class
	private static class Frame { 
		
		private int id;				//Frame ID
		private int proc;			//Process whose page is in this frame
		private int page;			//Page in this frame
		private int used;			//Last time this frame was accessed
		
		public Frame(int id) {
			this.id = id;
			this.page = -1;			//Indicates frame is empty
		}

		public int getID() {
			return id;
		}

		public int getProc() {
			return proc;
		}

		public void setProc(int n) {
			this.proc = n;
		}

		public int getPage() {
			return page;
		}

		public void setPage(int p) {
			this.page = p;
		}

		public int getUsed() {
			return used;
		}

		public void setUsed(int t) {
			this.used = t;
		}
	}
	
	/* Pager:
	 * 	Processes each reference: if fault occurs --> page becomes resident
	 * 		If no free frames (for faulted page) --> evict resident page using replacement alg R
	 * 		(SEE NOTE 2 FOR FREE FRAMES)
	 * 	Algs are globals (victim can be any frame - not just ones used by the faulting process)
	 * 	Implement frame table - not page tables
	 * 	Each process has an associate page table -- contains its nth entry number of the frame (w each process's nth page - or indicates
	 * 		page isnt resident)
	 * 	One frame table for the whole system
	 * 		contains reverse mapping (the nth entry spefiesthe page contained in the nth frame (or idicates its empty))
	 * 		The nth entry contains the pair (P, p) where page p contains process P in frame n
	 * 	Begins w all empty frames (First ref for each process = a page fault) 
	 * 	If a run has D processes (J=1 has D=1, the others have D=4) then process k (1<=k<=D) begins by referencing word 111*k mod S (SEE NOTE 1)
	 */
	public static void pager() throws FileNotFoundException {
		
		LinkedList<Integer> FIFO = new LinkedList<Integer>();	//LinkedList to keep track of FIFO
		int time = 1;		//Time counter
		
		//Check page faults
		boolean free = false;		
		int currentWord;
		int currentPg;
		
		while (totalRefs >= 1) {
			
			for (int i = 0; i < processes.size(); i++) {
				Process proc = processes.get(i);

				//For each processes' first reference
				if (proc.isFirst()) {
				
					//If R == RANDOM
					if (R.equalsIgnoreCase("random")) {
						if (i != 0) {
							words.remove(time - 1);
							pages.remove(time - 1);
						}
						words.add((111 * proc.getID()) % S);			//Initialize word 
						pages.add(((111 * proc.getID()) % S) / P);		//Initialize page
						
					}
					//If R == FIFO or LRU
					else {
						proc.words[proc.getNextIdx()] = ((111 * proc.getID()) % S);			//Initialize word
						proc.pages[proc.getNextIdx()] = (((111 * proc.getID()) % S) / P);	//Initialize page
					}
					proc.loadTimes[0] = time;
				}
				//For each quantum cycle
				while (proc.getQuantum() > 0 && proc.getRefsLeft() > 0) {
					
					proc.decRefsLeft();		//Decrease number of references the process has left
//					
					//If R == RANDOM 
					if (R.equalsIgnoreCase("random")) {
						currentWord = words.get(time - 1);				//Get current word
						currentPg = pages.get(time - 1);				//Get current page
					}
					//If R == FIFO or LRU
					else {
						currentWord = proc.words[proc.getNextIdx()];	//Get current word
						currentPg = proc.pages[proc.getNextIdx()];		//Get current page
					}
					
					boolean fault = true;
					int hit = 0;			//Frame where the hit occurred
					
					//Check for a hit or free frame
					for (int ff = frameTBL.size()-1; ff >= 0; ff--) {
						Frame fm = frameTBL.get(ff);

						//If process's ID and page match the frame --> hit
						if (fm.getProc() == proc.getID() && fm.getPage() == currentPg) {
							fault = false;
							hit = ff;	
							
							break;
						}
						//If the frame is free --> page fault
						else if (fm.getPage() == -1) {
							fault = true;
							free = true;
							
							//Assign current page to the frame
							fm.setPage(currentPg);
							fm.setProc(proc.getID());
							fm.setUsed(time);
							proc.loadTimes[currentPg] = time;
							
							//R == FIFO --> Populate FIFO LinkedList 
							if (R.equalsIgnoreCase("fifo")) {
								FIFO.add(ff);
							}
							
							//Increment page fault count
							totalFaults++;
							proc.incNumPageFault();

//							System.out.println(time);
//							System.out.println("Process: " + proc.getID() + " Faults: " + proc.getNumPageFault());
							
							break;
						}
					}
					
					//If there's a hit --> update frame use time
					if (fault == false) {
						frameTBL.get(hit).setUsed(time);	//Update time of last reference
					}
					
					//If there's a page fault (not due to a free frame)
					else if (fault && free == false){
						
						//Increment page fault count
						totalFaults++;
						proc.incNumPageFault();
						
//						System.out.println(time);
//						System.out.println("Process: " + proc.getID() + " Faults: " + proc.getNumPageFault());
						
						int victim = 0;			//Frame of victim
						
						//If R == LRU
						if (R.equalsIgnoreCase("lru")) {
							
							//Compare time of frame's last use to current time
							int tn = time;		
							for (int f = 0; f < frameTBL.size(); f++) {
								
								int lru = frameTBL.get(f).getUsed();
								if (lru < tn) {
									tn = lru;
									victim = frameTBL.get(f).getID();
								}
							}
						}
						
						//If R == FIFO
						else if (R.equalsIgnoreCase("fifo")) {

							victim = FIFO.remove();			//Get frame of victim page & remove from head 
							FIFO.add(victim);				//Add victim frame to end
						}
						
						//If R == RANDOM
						else if (R.equalsIgnoreCase("random")){
							
							int rand = random();				
							victim = (rand % numFrames);
						}
						
						//Evict page from frame
						Process p = processes.get(frameTBL.get(victim).getProc()-1);
						int victimPg = frameTBL.get(victim).getPage();
						p.incResTime(time - p.loadTimes[victimPg]);	//Add resident time
						
						//Increment evicted count
						p.incNumEvict();						
						totalEvicted++;
						
						//Add new page to frame
						frameTBL.get(victim).setProc(proc.getID());
						frameTBL.get(victim).setPage(currentPg);
						frameTBL.get(victim).setUsed(time);
						proc.loadTimes[currentPg] = time;			//Set page load time
					}
					
					proc.nextIdx++;
						
					double A = proc.getA();
					double B = proc.getB();
					double C = proc.getC();	
				
					int r = random();
					double y = (r/ (Integer.MAX_VALUE + 1d));	//To calculate next word (LAB NOTES 4)
					
					int W = currentWord;		//Current word
					int nextWord = 0;			//Next word
						
					//Calculate next reference
					//If R == RANDOM
					if (R.equalsIgnoreCase("random") && proc.getRefsLeft() >= 0) {
						
						if (y < A) {
							nextWord = ((W + 1) % S);
							words.add(nextWord);		 
						}
						else if (y < (A + B)) {				
							nextWord = ((W - 5 + S) % S);
							words.add(nextWord);		 
						}
						else if (y < (A + B + C)) {	
							nextWord = ((W + 4) % S);
							words.add(nextWord);		 
						}
						else {	// y >= (A + B + C)												
							nextWord = (random() % S);
							words.add(nextWord);		 
						}
						pages.add(nextWord / P);
					}	
					//If R == FIFO or LRU
					else if (proc.getRefsLeft() > 0) {
						
						if (y < A) {
							nextWord = ((W + 1) % S);
							proc.words[proc.getNextIdx()] = nextWord;		
						}
						else if (y < (A + B)) {				
							nextWord = ((W - 5 + S) % S);
							proc.words[proc.getNextIdx()] = nextWord;		
						}
						else if (y < (A + B + C)) {	
							nextWord = ((W + 4) % S);
							proc.words[proc.getNextIdx()] = nextWord;		
						}
						else {	// y >= (A + B + C)												
							nextWord = (random() % S);
							proc.words[proc.getNextIdx()] = nextWord;		
						}
						proc.pages[proc.getNextIdx()] = (nextWord / P);		
					}
					
					//Set free and first to FALSE
					free = false;
					proc.first = false;
					
					proc.decQuantum(); 		//Decrement quantum
					totalRefs--;			//Decrement total references left
					time++;					//Increment time
				}
				//Reset quantum for next cycle
				proc.setQuantum(3);	
			}
		}
	}
	
	//Random Number Generator - taken from my Lab 2 (Scheduler.java) 
	public static int random() throws FileNotFoundException {			
		
		int X = 0;
		Scanner in = new Scanner(new File("random-numbers.txt"));
		
		for (int r = 0; r < random; r++) {
			X = in.nextInt();
		}
		random++;
		in.close();	
		
		return X;
	}
	
	//Function to print final data results to screen
	public static void output() {
		
		System.out.println("The machine size is " + M + ".");
		System.out.println("The page size is " + P + ".");
		System.out.println("The process size is " + S + ".");
		System.out.println("The job mix number is " + J + ".");
		System.out.println("The number of references per process is " + N + ".");
		System.out.println("The replacement algorithm is " + R + ".");
		System.out.println();
		
		for (int p = 0; p < processes.size(); p++) {
			Process proc = processes.get(p);
			
			System.out.print("Process " + proc.getID() + " had " + proc.getNumPageFault() + " faults");
			if (proc.getNumEvict() != 0) {
				double avgRes = (proc.getResTime() / proc.getNumEvict());
				System.out.println(" and " + avgRes + " average residency.");
				totalAvgRes += proc.getResTime();
			}
			else {
				System.out.println(".");
				System.out.println("	With no evictions, the average resident is undefined.");
			}
		}
		System.out.println();
		System.out.print("The total number of faults is " + totalFaults);
		if (totalEvicted != 0) {
			totalAvgRes /= totalEvicted;
			System.out.print(" and the overall average residency is " + totalAvgRes + ".");
		}
		else {
			System.out.println(".");
			System.out.println("	With no evictions, the overall average resident is undefined.");
		}
	}
}
