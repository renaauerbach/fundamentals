package labs;

/* Rena Auerbach - rma453
 * Prof. Shvartzshnaider
 * February 7, 2019
 * Lab 1 - Two-Pass Linker
 */

import java.util.*;

public class TwoPassLinker {

	static LinkedHashMap<String, Integer> symTable = new LinkedHashMap<String, Integer>();				//Final symbol table
	static LinkedHashMap<Integer, Integer> memMap = new LinkedHashMap<Integer, Integer>();				//Final memory map
	static ArrayList<Module> modules = new ArrayList<Module>();								//ArrayList of all modules
	static ArrayList<String> symUsed = new ArrayList<String>();								//ArrayList of all symbols used
	static ArrayList<Symbol> symDefn = new ArrayList<Symbol>();								//ArrayList of all symbols used
	static HashMap<String, String> errorMsgSym = new HashMap<String, String>();				//Symbol errors
	static HashMap<Integer, String> errorMsgMem = new HashMap<Integer, String>();			//Memory error
	static ArrayList<String> errorMsgWarning = new ArrayList<String>();						//Warnings (go at end)

	
	public static void main(String[] args) { 					

		System.out.println("Enter your input: ");
		Scanner sc = new Scanner(System.in);
		int totalMods = Integer.parseInt(sc.next());
		
		int prevModSize = 0;
		int prevModBase = 0;
        
		for (int modCounter = 0; modCounter < totalMods; modCounter++){
			
			Module mod = new Module(modCounter);
				
			//Definition Handling
			int numDef = sc.nextInt();
			for (int d = 0; d < numDef; d++) {
				String var = sc.next();
				int addr = sc.nextInt() + prevModSize + prevModBase;

				if (symTable.containsKey(var) == false) {
					Symbol symbol = new Symbol(var, addr);
					symbol.setDefnLocal(modCounter);
					mod.defnList.add(symbol);					//Add def to defn list
					symTable.put(var, addr);				//Add symbol to symTable
					symDefn.add(symbol);
				}
				else { 
					errorMsgSym.put(var, "Error: " + var + " is multiply defined. Last value " + addr + " is used.");
					symTable.replace(var, addr);
					break;
				}	
			}
	
			//Use Handling		
			int numUsed = sc.nextInt();
			mod.setNumUsed(numUsed);

			for (int u = 0; u < numUsed; u++) {
				String var = sc.next();
				int addr = sc.nextInt();
					
				Symbol symbol = new Symbol(var, addr);
				mod.useList.add(symbol);					//Add use to use list
				symUsed.add(var);							//Add symbol to symUsed	
				
			}
			
			//Program Handling
			int modSize = sc.nextInt();
			mod.setBase(prevModSize + prevModBase);
			mod.setSize(modSize);
			prevModBase = mod.getBase();
			prevModSize	= mod.getSize();
			
			for (int i = 0; i < modSize; i++) {
				Character letter = sc.next().charAt(0);
				int instr = sc.nextInt();
				Word w = new Word(letter, instr);
				mod.prgmList.add(w);					//Add word to program list
			}
			
			modules.add(mod);
		}
		
		printTable();
		passTwo(totalMods);
		checkDefn();
		printMap();
		
		sc.close();
	}
	
		private static class Module {
			
			private Integer base;		//Base address
			private int size;			//Size
			private int numUsed;		//Number of symbols used
			
			public ArrayList<Symbol> defnList;
			public ArrayList<Symbol> useList;
			public ArrayList<Word> prgmList = new ArrayList<Word>();

			public Module(int num) {
				this.base = 0;
				this.size = 0;

				this.defnList = new ArrayList<Symbol>();
				this.useList = new ArrayList<Symbol>();
				this.prgmList = new ArrayList<Word>();
			}
			
			public void setBase(int base) {
				this.base = base;
			}
			
			public Integer getBase() {
				return this.base;
			}
			
			public void setSize(int size) {
				this.size = size;
			}
			
			public Integer getSize() {
				return this.size;
			}
			
			public void setNumUsed(int numUsed) {
				this.numUsed = numUsed;
			}
			
			public int getNumUsed() {
				return this.numUsed;
			}
		}
		
		
		private static class Symbol {
			
			private String symbol;			//Symbol name
			private Integer relAddr;		//Relative address
			private Integer defnLocal;		//Which module this symbol is defined in (base addr)

			public Symbol(String sym, Integer addr) {
				this.symbol = sym;
				this.relAddr = addr;
			}
			
			public String getSymbol() {
				return this.symbol;
			}
			
			public Integer getRelAddr() {
				return this.relAddr;
			}
			
			public void setDefnLocal(Integer defnLocal) {
				this.defnLocal = defnLocal;
			}

			public Integer getDefnLocal() {
				return this.defnLocal;
			}
		}
		
		private static class Word {
			
			private Character letter;			
			private Integer instr;	

			public Word(Character letter, Integer instr) {
				this.letter = letter;
				this.instr = instr;
			}
			
			public Character getLetter() {
				return this.letter;
			}
			
			public Integer getInstr() {
				return this.instr;
			}
		}
		
		/* Pass two uses the base addresses and the symbol table computed in pass one to 
		 * generate the actual output by relocating relative addresses and resolving external 
		 * references.
		*/
		public static void passTwo(int totalMods) {
						
			int memPosition = 0;
			
			for (int modCounter = 0; modCounter < totalMods; modCounter++){
			
				Module mod = modules.get(modCounter);
				
				//Get array of symbol uses
				ArrayList<Integer> reverseUse = new ArrayList<Integer>();
				for (int g = 0; g < mod.getNumUsed(); g++) {
					reverseUse.add(mod.useList.get(g).getRelAddr());
				}
				
				//Get array of stoppers
				ArrayList<Integer> stoppers = new ArrayList<Integer>();
				for(int p = 0; p < mod.prgmList.size(); p++) {
					Integer temp = mod.prgmList.get(p).getInstr();
					Integer tempRightDigs = Integer.parseInt(Integer.toString(temp).substring(1, 4));
					if (tempRightDigs > 300) {
						stoppers.add(p);
					}
				}
				
				for(int p = 0; p < mod.prgmList.size(); p++) {
					  
					Character letter = mod.prgmList.get(p).getLetter();
					Integer instr = mod.prgmList.get(p).getInstr();
					Integer rightDigits = Integer.parseInt(Integer.toString(instr).substring(1, 4));	//3 right-most digits in address
					Integer leftDigit = Integer.parseInt(Integer.toString(instr).substring(0, 1));
					
					int absAddr;
					int lowest = 0;

					//I: Immediate Operand (unchanged)
					if (letter == 'I') {
						memMap.put(memPosition, instr);
					}
				
					//A: Absolute Address (unchanged)
					else if (letter == 'A') {
						if (rightDigits > 300) {
							absAddr = (leftDigit * 1000) + 299;
							errorMsgMem.put(memPosition, "Error: Absolute address " + instr + " exceeds the machine size. Max length " + absAddr + " will be used.");
							memMap.put(memPosition, absAddr);
						}
						else {
							memMap.put(memPosition, instr);
						}
					}
				
					//R: Relative Address (relocated)
					else if (letter == 'R') {
						if (rightDigits > mod.getSize()) {
							errorMsgMem.put(memPosition, "Error: Relative address " + instr + " exceeds the module size. Relative address 0 used.");
							absAddr = leftDigit * 1000;
							memMap.put(memPosition, absAddr + mod.getBase());
						}
						else {
							absAddr = instr + mod.getBase();			//Relative Addr --> Abs Addr = relative addr + start
							memMap.put(memPosition, absAddr);
						}
					}
				
					//E: External Address (resolved)
					else if (letter == 'E') {
						int addNum = 0;
						String key = mod.useList.get(0).getSymbol();
						
						//Multiple symbols used
						if (mod.getNumUsed() > 1) {
							
							//Check if multiple used symbols have same rel address
							if (stoppers.size() == 1) {
								String lastSym;
								for (int g = 0; g < mod.getNumUsed(); g++) {
	
									for (int h = g + 1; h < mod.getNumUsed(); h++) {
										if (mod.useList.get(g).getRelAddr() == mod.useList.get(h).getRelAddr()) {
											lastSym = mod.useList.get(h).getSymbol();
											addNum = symTable.get(lastSym);
											absAddr = leftDigit * 1000 + addNum;

											memMap.put(memPosition, absAddr);
											System.out.println(absAddr);		//PROOF: THIS FUNCTION WORKS
											errorMsgMem.put(memPosition, "Error: Multiple symbols used here. Last one (" + lastSym + ") used.");
											break;
										}
									}
								}	
							}
						
							else {
//							
								if (stoppers.contains(p)) {
									Collections.sort(stoppers, Collections.reverseOrder());		//Sort descending
									
									for(int i = 0; i < stoppers.size(); i++) {
										if (stoppers.get(i) == p) {
										
										addNum = symTable.get(mod.useList.get(i).getSymbol());
										absAddr = leftDigit * 1000;
										memMap.put(memPosition, absAddr + addNum);
							            }
									}
								}
							
								for (int z = 0; z < mod.getNumUsed(); z++) {
									if (p < reverseUse.get(z)) {

										if (lowest == 0) {
											lowest = reverseUse.get(z);
										}
										int d = p + 1;
										if (d <= mod.prgmList.size()) {
											
											if (stoppers.contains(d)) {

												if (z != mod.getNumUsed() - 1) {		
													System.out.println(memPosition);
													lowest = reverseUse.get(z + 1);
												}
												else if (reverseUse.get(z) < lowest) {	
													lowest = reverseUse.get(z);
												}
												else {
													lowest = reverseUse.get(z - 1);
												}
												break;
											}
											break;
										}
										break;
									}
								}
								
								for (int x = 0; x < mod.getNumUsed(); x++) {
									if (lowest == mod.useList.get(x).getRelAddr()) {
										
										key = mod.useList.get(x).getSymbol();
										addNum = symTable.get(key);
										absAddr = leftDigit * 1000;
										memMap.put(memPosition, absAddr + addNum);
										break;
									}
								}
							}

							for (int x = 0; x < mod.getNumUsed(); x++) {

								if (p == mod.useList.get(x).getRelAddr()) {		
									key = mod.useList.get(x).getSymbol();
									addNum = symTable.get(key);
									absAddr = leftDigit * 1000;
									memMap.put(memPosition, absAddr + addNum);
									break;
								}
							}
						}
						
						//Only one symbol used
						else if (mod.useList.size() == 1) {
							
							//Check if the symbol is defined
							if (checkUsed(memPosition, mod.useList.get(0).getSymbol()) == false) {
								key = mod.useList.get(0).getSymbol();
								addNum = symTable.get(key);
								absAddr = leftDigit * 1000;
								memMap.put(memPosition, absAddr + addNum);
							}
							//Change undefined symbols to 111
							else {
								addNum = 111;
								absAddr = leftDigit * 1000;
								memMap.put(memPosition, absAddr + addNum);
							}
						}
						
						//No symbols used --> stay the same
						else {
							absAddr = leftDigit * 1000;
							memMap.put(memPosition, absAddr);
						}
					}
					memPosition++;
				}
			}
		}
				
		//Print Symbol Table
		public static void printTable() {

			System.out.println("Symbol Table");
					
			for (Map.Entry<String, Integer> entry : symTable.entrySet()) {
				if (errorMsgSym.containsKey(entry.getKey())) {
					System.out.print(entry.getKey() + "=" + entry.getValue());
					System.out.println("  " + errorMsgSym.get(entry.getKey()));
				}
				else {
					System.out.println(entry.getKey() + "=" + entry.getValue());
				}
			}
		
			System.out.println();
		}
		
		public static boolean checkUsed(int position, String key) {
			
			//Check if symbol is used but not defined
			if (symTable.containsKey(key) == false) {
				errorMsgMem.put(position, "Error: " + key + " is used but not defined. Value 111 is used.");
				return true;
			}
			return false;
		}
		
		//Print Error Messages
		public static void checkDefn() {
			
			//Check if symbol is defined but not used
			for (int y = 0; y < symDefn.size(); y++) {
				String sym = symDefn.get(y).getSymbol();
				if (symUsed.contains(sym) == false) {
					errorMsgWarning.add("Warning: " + sym + " was defined in module " + Integer.toString(symDefn.get(y).getDefnLocal()) + " but never utilized.");
				}	
			}
		}
		
		//Print Memory Map
		public static void printMap() {
			System.out.println("Memory Map");
			
			for (Map.Entry<Integer, Integer> entry : memMap.entrySet()) {
				if (errorMsgMem.containsKey(entry.getKey())) {
					System.out.print(entry.getKey() + ":  " + entry.getValue());
					System.out.println("  " + errorMsgMem.get(entry.getKey()));
				}
				else {
					System.out.println(entry.getKey() + ":  " + entry.getValue());
				}
			}
			System.out.println();
			for (int w = 0; w < errorMsgWarning.size(); w++) {
				System.out.println(errorMsgWarning.get(w));
			}
		}

}