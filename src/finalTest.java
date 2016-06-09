import java.util.Scanner;
/*
 * Author: 		Albert Carrete
 * Class: 		CSC-18C
 * Project: 	Final Project
 */

public class finalTest {

	public static void main(String[] args) {

//		String test;
//		Scanner scan = new Scanner(System.in);
//		 
//		System.out.println("Testing Scanner, write something: ");
//		test = scan.nextLine();

//		String expr = convertToPostFix("(a + b) * (c – d) / ((e – f) * (g + h))");
//		String expr = convertToPostFix(" 3   *(4+5)");
		String expr = convertToPostFix("2*((8+5)*(1+2))");
//		String expr = convertToPostFix("6*(3+(7*8)*(5+2))");
//		String expr = convertToPostFix("(4+8)*(6-5)/((3-2)*(2+2))");
		evaluatePostFix(expr);


	}
	
	/* Takes a equation as string in infix notation and converts
	 * it to post fix notation 
	 * */
	public static String convertToPostFix(String infix){
		
		//DEBUG: prints our received input
		System.out.println(infix);
		
		// Converts our input string into an array
		String[] infixArr = infix.split("(?!^)");
	    StringBuffer postfixString = new StringBuffer(infix.length());
		Stack<String> operatorStack = new Stack<String>(String.class);
		
		String[] equation = normalize(infixArr);
		
		System.out.print("Normalized: ");
		displayArray(equation);
		
		Boolean parsing = true;
		int i = 0;
		
		while(parsing){
			System.out.println("----> Pass " + (i+1));
			System.out.println("Working: " + equation[i]);
			if(!isOperator(equation[i])){ // is not an operator
				postfixString.append(equation[i] + " ");
			}else if(isOperator(equation[i],"^")){ // is operator ^
				operatorStack.push(equation[i]);
			}else if(isOperator(equation[i], new String[]{"+","-","*","/"})){ // is a generic operator
				// append to output until stack is empty or 
				while(!operatorStack.isEmpty() && precendence(equation[i],operatorStack.peek())){
					postfixString.append(operatorStack.peek() + " ");
					operatorStack.pop();
					System.out.println("appending");
				}
				operatorStack.push(equation[i]);
				System.out.println("Operator Stack: " + operatorStack.peek());
			}else if(isOperator(equation[i],"(")){ // is operator (
				System.out.println("( found");
				operatorStack.push(equation[i]);
				System.out.println("Operator Stack: " + operatorStack.peek());
				
			}else if(isOperator(equation[i],")")){ // is operator )
				System.out.println(") found");
				String topOperator = operatorStack.pop();
				while(!topOperator.equals("(")){
					postfixString.append(topOperator + " ");
					topOperator = operatorStack.pop();
				}
			}
			
			System.out.println("Output:" + postfixString.toString());
			System.out.println("=====================");
			i++;
			if(i == equation.length){
				parsing = false;
			}
		}
		
	
		
		// Unwind remaining stack
		while(!operatorStack.isEmpty()){
			if(!operatorStack.peek().equals("(")){
				postfixString.append(operatorStack.pop() + " ");	
			}else{
				operatorStack.pop();
			}
		}
		System.out.println(postfixString);	
		return postfixString.toString();
	}
	
	public static void evaluatePostFix(String expr){
		
		System.out.println("Equation");
		String[] equation = expr.split("\\s+");
		Stack<String> theStack = new Stack<String>(String.class);
		
		for(int i = 0; i < equation.length; i++){
			// if not an operator
			if(!isOperator(equation[i])){
				theStack.push(equation[i]);
			}
			// if operator
			else{
				int x = Integer.parseInt(theStack.pop());
				int y = Integer.parseInt(theStack.pop());
				
				Integer result = operate(x,y,equation[i]);
				theStack.push(result.toString());
			}
		}
		
		// Post result
		while(!theStack.isEmpty()){
			System.out.println("Result:");
			System.out.print(theStack.pop());
		}
		
	}
	
	/* Utilities */
	
	/*
	 * Normalizes string input by replacing spaces and
	 * identifying and grouping negatives. 
	 */
	public static String[] normalize(String a[]){
		
		// Copy
		String[] temp = new String[a.length];
		for(int i = 0; i < a.length; i++){
			temp[i] = a[i];
		}
		System.out.print("Pre-Normalized: ");
		displayArray(temp);

		// Flag 		
		int deletions = 0; // calculates decreased size of new array
		
		for(int i = 0; i<temp.length; i++){
			if(temp[i].equals(" ")){
				temp[i] = "s";
				deletions++;
			}
			if(temp[i].equals("-")){
				if(i-1 >= 0){
					if(isOperator(temp[i-1])){
						temp[i] = "n";
						deletions++;
					}
				}else{
					temp[i] = "n";
					deletions++;
				}
			}
		}
		
		// Consolidates all/any alterations
		Boolean negativeNext = false;
		String b[] = new String[temp.length-deletions];
		
		for(int i = 0, j = 0; i < temp.length; i++){
			
			if(!isFlag(temp[i])){
				if(negativeNext){
					System.out.println("Negative next " + temp[i]);
					negativeNext = false;
					b[j] = "-" + temp[i];
					j++;
				}else{
					b[j] = temp[i];
					j++;
				}
			}else{				
				if(temp[i].equals("n")){
					negativeNext = true;
				}	
			}
		}
	
		System.out.print("Cleaned: ");
		displayArray(b);
		
		return b;
	}
	
	// Is first param op less than or equal to second param op?
	public static boolean precendence(String x, String y){
		System.out.println("precendence check");
		String[] ops = new String[2];
		ops[0] = x;
		ops[1] = y;
		
		int[] opVals = new int[2];
		opVals[0] = -1;
		opVals[1] = -1;
		
		for(int i = 0; i < ops.length; i++){
			switch(ops[i]){
//				case "(":
//				case ")":
//					opVals[i] = 5;
//					break;
				case "*":
					opVals[i] = 4;
					break;
				case "/":
					opVals[i] = 3;
					break;
				case "+":
					opVals[i] = 2;
					break;
				case "-":
					opVals[i] = 1;
					break;
			}
		}
		System.out.println("Comparing " + x + ":" + opVals[0] + " <= " + y + ":" + opVals[1]);
		return opVals[0] <= opVals[1];

		
	}
	public static boolean isOperator(String val, String[] key){
		for(int i = 0; i < key.length; i++){
//			System.out.println("checking if " + val + " == " + key[i]);

			if(val.equals(key[i])){
				return true;
			}
		}
		return false;
	}	
	public static boolean isOperator(String o){
		return o.equals("+") || o.equals("-") || o.equals("*") || o.equals("/") || o.equals("^") || o.equals("(") || o.equals(")");
	}
	public static boolean isOperator(String o, String type){
		return o.equals(type);
	}
	public static boolean isFlag(String o){
		return o.equals("s") || o.equals("n");
	}
	public static int operate(int x,int y, String operation){
		int result;
		
		switch(operation){
		
		case "+":
			result = x + y;
			break;
		case "-":
			result = x - y;
			break;
		case "*":
			result = x*y;
			break;
		case "/":
			result = x / y;
			break;
		case "^":
			result = (int)Math.pow(y, x);
			break;
		default:
			result = 0;
		}
			
		return result;
	}
	
	public static void displayArray(String[] a){
		for(int i = 0; i < a.length; i++){
			System.out.print(a[i] + "_");
		}
		System.out.println("");
	}
}
