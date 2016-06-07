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

		String expr = convertToPostFix("-2 * (3+5)");
//		String expr = convertToPostFix(" 3   *(4+5)");
//		String expr = convertToPostFix("2*((3+5)*(3+2))");
//		String expr = convertToPostFix("6*(3+(7*8)*(5+2))");
		evaluatePostFix(expr);


	}
	
	// Takes a equation as string in infix notation and converts
	// it to post fix notation
	public static String convertToPostFix(String infix){
		
		//DEBUG: prints our received input
		System.out.println(infix);
		
		// Converts our input string into an array
		String[] infixArr = infix.split("(?!^)");
	    StringBuffer postfixString = new StringBuffer(infix.length());
		Stack<String> theStack = new Stack<String>(String.class);
		
		String[] equation = normalize(infixArr);
		
		System.out.print("Normalized: ");
		displayArray(equation);
		
		//  Loop through the infix notation
		for (int i = 0; i < equation.length; i++){
			if(!isOperator(equation[i])){
//				System.out.println("Appending " + equation[i] + " to postfix string");
				postfixString.append(equation[i] + " ");
			}else{
				// if ending paren
				if(equation[i].equals(")")){
//					System.out.println("Unwinding");
					// unwind stack
					while(!theStack.isEmpty() && !theStack.peek().equals("(")){
						postfixString.append(theStack.pop() + " ");
					}
				}
				// push operator onto the stack
				else{
//					System.out.println("Appending " + equation[i] + " to the stack");
					theStack.push(equation[i]);	
				}	
			}	
		}	
		
		// Unwind remaining stack
		if(!theStack.isEmpty()){
			while(!theStack.isEmpty()){
				if(!theStack.peek().equals("(")){
					postfixString.append(theStack.pop() + " ");	
				}else{
					theStack.pop();
				}
			}
		}
		System.out.println(postfixString);	
		return postfixString.toString();
	}
	
	public static void evaluatePostFix(String expr){
		String[] equation = expr.split("\\s+");
		Stack<String> theStack = new Stack<String>(String.class);
		
		for(int i = 0; i < equation.length; i++){
			if(!isOperator(equation[i])){
				theStack.push(equation[i]);
			}
			else{
				int x = Integer.parseInt(theStack.pop());
				int y = Integer.parseInt(theStack.pop());
				
				Integer result = operate(x,y,equation[i]);
				theStack.push(result.toString());
			}
		}
		
		while(!theStack.isEmpty()){
			System.out.print(theStack.pop());
		}
		
	}
	
	
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
	
	
	/* Utilities */
	public static boolean isOperator(String o){
		return o.equals("+") || o.equals("-") || o.equals("*") || o.equals("/") || o.equals("^") || o.equals("(") || o.equals(")");
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
