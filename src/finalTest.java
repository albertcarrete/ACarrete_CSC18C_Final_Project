import java.util.Scanner;
/*
 * Author: 		Albert Carrete
 * Class: 		CSC-18C
 * Project: 	Final Project
 */

public class finalTest {

	public static void main(String[] args) {

		// Custom Input
//		String test;
//		Scanner scan = new Scanner(System.in);
//		 
//		System.out.println("Write an expression: ");
//		test = scan.nextLine();
//		String expr = convertToPostFix(test);

		// Official Tests
		String expr = convertToPostFix("3 * (4 + 5)");
//		String expr = convertToPostFix("2 * ((3+5)*(3+2))");
//		String expr = convertToPostFix("6 * (3+(7*8)*(5+2))");
//		String expr = convertToPostFix("-2 * (3+5)");
		
		// Unofficial Tests
//		String expr = convertToPostFix("(4+8)*(4-5)/((3-2)*(2+2))");
		
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
			if(!isOperator(equation[i])){ // is not an operator
				postfixString.append(equation[i] + " ");
			}else if(isOperator(equation[i],"^")){ // is operator ^
				operatorStack.push(equation[i]);
			}else if(isOperator(equation[i], new String[]{"+","-","*","/"})){ // is a generic operator
				// append to output until stack is empty or 
				while(!operatorStack.isEmpty() && precendence(equation[i],operatorStack.peek())){
					postfixString.append(operatorStack.peek() + " ");
					operatorStack.pop();
				}
				operatorStack.push(equation[i]);
//				System.out.println("Operator Stack: " + operatorStack.peek());
			}else if(isOperator(equation[i],"(")){ // is operator (
//				System.out.println("( found");
				operatorStack.push(equation[i]);
//				System.out.println("Operator Stack: " + operatorStack.peek());
				
			}else if(isOperator(equation[i],")")){ // is operator )
//				System.out.println(") found");
				String top = operatorStack.pop();
				while(!top.equals("(")){
					postfixString.append(top + " ");
					top = operatorStack.pop();
				}
			}
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
		System.out.println("Postfix: " + postfixString);	
		return postfixString.toString();
	}
	
	public static void evaluatePostFix(String expr){
		
		String[] equation = expr.split("\\s+");
		Stack<String> resultStack = new Stack<String>(String.class);
		
		for(int i = 0; i < equation.length; i++){
			// if not an operator
			if(!isOperator(equation[i])){
				resultStack.push(equation[i]);
			}
			// if operator
			else{
				float x = Float.parseFloat(resultStack.pop());
				float y = Float.parseFloat(resultStack.pop());
				float result = operate(x,y,equation[i]);
				resultStack.push(String.valueOf(result));
			}
			// DEBUG: Print Result Array 
//			System.out.println("result stack: ");
//			String[] resultArray = resultStack.toArray();
//			for(int y = 0; y < resultArray.length; y++){
//				System.out.println(resultArray[y] + " ");
//			}
		}
		
		// Post result
		while(!resultStack.isEmpty()){
			System.out.print("Result: ");
			System.out.print(resultStack.pop());
		}
		
	}
	
	/* Utilities */
	
	/* Normalizes string input by replacing spaces and
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
		return b;
	}
	
	// Is first param op less than or equal to second param op?
	public static boolean precendence(String x, String y){
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
	public static float operate(float x,float y, String operation){
		float result;
		
		switch(operation){
		
		case "+":
			result = x + y;
			break;
		case "-":
			result = x - y;
			break;
		case "*":
			result = x * y;
			break;
		case "/":
			result = y / x;
			break;
		case "^":
			result = (float)Math.pow(y, x);
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
