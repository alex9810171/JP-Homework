package jp2018;

import java.util.ArrayList;
import java.util.Set;

import sun.security.jca.GetInstance.Instance;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashSet;

import static jp2018.HW3.JExpr.*;


/**
 * A practice of arithmetic and logical expression hierarchy. Remember to put
 * this class inside the 'jp2018' package/directory to start working with it.
 * 
 * @author chencc
 *
 */
public class HW3 {

	public static void main(String[] args) {
		testHW3(args);
	}

	public static void testHW3(String[] args) {

		JVariable[] vars = { JVAR("x"), JVAR("y"), JVAR("z"), JVAR("A"), JVAR("B"), JVAR("C"), JVAR("b1"), JVAR("b2"),
				JVAR("b3") };
		System.out.println("Suppose we have variables: x, y, z ,A, B,C, whose values are given below:");
		for (JVariable v : vars) {
         System.out.println("variable " + v.name + ":" + varMap.get(v.name)) ;     
		}

		// e1 = 5
		JExpr e1 = JNUM(5);

		// e2 = x + y
		JExpr e2 = new JBinaryExpr(ADD, new JNumVariable("x"), new JNumVariable("y"));

		// e3 = B * B - 4 * A * C
		JExpr e3 = JSUB(JMUL(JVAR("B"), JVAR("B")), JMUL(JLIT(4), JMUL(JVAR("A"), JVAR("C"))));

		// e4 = x + y * 2

		JExpr e4 = JADD(JVAR("x"), JMUL(JVAR("y"), JNUM(2)));

		// e5 = !(x > y + 4 ) ? b1 : b2 XOR b3
		JExpr e5 = JIF(JNOT(JGT(JVAR("x"), JADD(JVAR("y"), JNUM(4)))), JVAR("b1"), JXOR(JVAR("b2"), JVAR("b3")));

		// e6 = !(x > y + 4 ) ? 3 : b2 XOR b3 *** not wellType!
		JExpr e6 = JIF(JNOT(JGT(JVAR("x"), JADD(JVAR("y"), JNUM(4)))), JNUM(3), JXOR(JVAR("b2"), JVAR("b3")));

		// e7 = x + b1 * true *** not wellType!

		JExpr e7 = JADD(JVAR("x"), JMUL(JVAR("b1"), JLIT(true)));

		// e8 = b1 * true ? x + z : x / false *** not wellType!

		JExpr e8 = JIF(JMUL(JVAR("b1"), JLIT(true)), JADD(JVAR("x"), JVAR("z")), JVAR("b3"));

		// e9 = e5 * e4 *** not wellType!

		JExpr e9 = JMUL(e4, e5);

		// e10 = e5? e4 : e2

		JExpr e10 = JIF(e5, e4, e2);

		JExpr[] exprs = { e1, e2, e3, e4, e5, e6, e7, e8, e9, e10 };

		System.out.println("\n*************Test GetVariables(JExpr)***********************************\n");

		for (JExpr e : exprs) {
			System.out.println("Expr " + e + " has variables:" + getVariables(e));
		}

		System.out.println("\n***************Test isWellTyped(JExpr)*********************************************\n");
		List rlt = new ArrayList();
		for (JExpr e : exprs) {
			if (isWellTyped(e)) {
				System.out.println("Expr " + e + " is well-typed.");
				rlt.add(true);
			} else {
				System.out.println("Expr " + e + " is NOT well-typed!");
				rlt.add(false);
			}
		}
		System.out.println(rlt);

		System.out.println("\n****************Test GetType(JExpr)****************************************\n");
		rlt.clear();
		for (JExpr e : exprs) {
			if (isWellTyped(e)) {
				System.out.println("Expr " + e + " has type:" + getType(e));
				rlt.add(getType(e));
			} else {
				rlt.add(null);
			}
		}
		System.out.println(rlt);

		System.out.println("\n****************Test GetValue(JExpr)****************************************\n");

		rlt.clear();
		for (JExpr e : exprs) {
			if (isWellTyped(e)) {
				System.out.println("Expr " + e + " has value:" + getValue(e));
				rlt.add(getValue(e));
			} else {
				rlt.add(null);
			}
		}
		System.out.println(rlt);

		System.out.println("\n****************Test prettyString(JExpr)****************************************\n");

		for (JExpr e : exprs) {
			System.out.println("Expr " + e + " has pretty string:" + prettyString(e));
		}

	}

	/**
	 * Abstract class for Java expression
	 * 
	 * @author chencc
	 *
	 */
	public static abstract class JExpr {

		/**
		 * Every expression has a type. However this method is lifted as a static one in
		 * HW3 and is not as an abstract method of JExpr.
		 * 
		 * @return
		 */
		// public abstract JType getType();

		/**
		 * Every Expression has a 'string' representation
		 * 
		 * @return
		 */
		public abstract String toString();

		/*
		 * Arithmetic operators. Note: all numeric types share a common set of
		 * operators.
		 * 
		 */
		public static final int ADD = 0, SUB = 1, MUL = 2, DIV = 3;

		/*
		 *
		 * There are 6 comparison operations. <, <=, >, >= are used for number
		 * comparison only while == and != can also be used for comparison of boolean
		 * values.
		 */
		public static final int LT = 4, LE = 5, GT = 6, GE = 7, EQ = 8, NE = 9;

		/*
		 * Binary Boolean operations:
		 */
		public static final int AND = 10, OR = 11, XOR = 12;

		/*
		 * Unary operations include -(negation) and !(logical NOT).
		 */
		public static final int NEG = 13, NOT = 14;

		public static final int JIF = 15;

		String[] OP_STR = new String[] { "+", "-", "*", "/", "<", "<=", ">", ">=", "==", "!=", "&", "|", "^", "-", "!",
				"?:" };

		/*
		 * (!,-)7 > (*,/)6 > (+,-)5 > (<,<=,>,>=) 4> (==,!=) 3 > & 2> (|(OR),^(XOR)) 1 >
		 * ?:
		 */
		public static final int[] PRECEDENCE = { 5, 5, 6, 6, 4, 4, 4, 4, 3, 3, 2, 1, 1, 7, 7, 0 };

		/**
		 * return the precedence of an operation op
		 * 
		 * @param op
		 */
		public static int prec(int op) {
			return PRECEDENCE[op];
		}

	}

	/**
	 * There are two kinds of variables: JNumVariable for number, and JBoolVariable
	 * for boolean. We assume boolean variable names start with 'b' while number
	 * variable names do not start with 'b'.
	 * 
	 * @author chencc
	 *
	 */
	public static abstract class JVariable extends JExpr {

		String name;

		public JVariable(String name) {
			this.name = name;
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof JVariable))
				return false;
			return name.equals(((JVariable) o).name);
		}

		/*
		 * Since we redefine equals(), we must override hashCode() as well.
		 */
		@Override
		public int hashCode() {
			return name.hashCode();
		}

		/**
		 * get the value of this variable. The value is assumed to be stored in varMap.
		 */
		public JValue getValue() {
			JValue v = (JValue) varMap.get(name);
			if (v != null)
				return v;
			if (this instanceof JBoolVariable) {
				return new JBoolValue(false);
			} else {
				return new JNumValue(0);
			}
		}
		
		/**
		 * put the value of this variable to the varMap.
		 */
		public void putValue(JValue v) {
			varMap.put(name, v) ;
		}

		@Override
		public String toString() {
			return name;
		}

	}

	public static class JBoolVariable extends JVariable {

		public JBoolVariable(String name) {
			// boolean variable must start with 'b'.
			super(name.startsWith("b") ? name : "b" + name);

		}
		
		/**
		 * put the value of this variable to varMap. 
		 */
		public void putValue(boolean b) {
			
			super.putValue(new JBoolValue(b));
		}
		
		

	}

	public static class JNumVariable extends JVariable {

		public JNumVariable(String name) {
			// number variable cannot start with 'b'.
			super(name.startsWith("b") ? "n" + name : name);
		}
		
		/**
		 * put the value of this variable to varMap. 
		 */
		public void putValue(double d) {
			super.putValue(new JNumValue(d));
		}

	}

	/**
	 * Instances of JType are used to represent types. There are only two types now:
	 * JBOOLEAN for 'boolean' and JUNMBER for (javascript) 'number' or java
	 * 'double'.
	 * 
	 * @author chencc
	 *
	 */

	public static class JType {
		String name;

		private JType(String n) {
			name = n;
		}

		public final static JType JBOOLEAN = new JType("boolean");
		public final static JType JNUMBER = new JType("number");

		public String toString() {
			return name;
		}
	}

	/**
	 * There are two kinds of literals: JNumber for number and JBoolean for boolean.
	 * 
	 * @author chencc
	 *
	 */
	public static abstract class JLiteral extends JExpr {

	}

	public static class JBoolLiteral extends JLiteral {
		boolean v;

		public JBoolLiteral(boolean v) {
			this.v = v;
		}

		public String toString() {
			return String.valueOf(v); // or ""+v
		}

	}

	public static class JNumLiteral extends JLiteral {
		double d;

		public JNumLiteral(double d) {
			this.d = d;
		}

		public String toString() {
			return String.valueOf(d); // or ""+d
		}

	}

	/**
	 * We use instances of this type to represent values of JExpr. Currently there
	 * are two kinds of values: JNumValue for number and JBoolValue for boolean.
	 * 
	 * @author chencc
	 *
	 */

	public static abstract class JValue {
	}

	public static class JBoolValue extends JValue {
		public boolean v;

		public JBoolValue(boolean v) {
			this.v = v;
		}

		public String toString() {
			return "" + v;
		}
	}

	public static class JNumValue extends JValue {
		public double v;

		public JNumValue(double v) {
			this.v = v;
		}

		public String toString() {
			return "" + v;
		}
	}

	public static class JBinaryExpr extends JExpr {

		int op;
		JExpr e1, e2;

		public JBinaryExpr(int op, JExpr e1, JExpr e2) {
			this.op = op;
			this.e1 = e1;
			this.e2 = e2;
		}

		public String toString() {

			return "(" + e1 + OP_STR[op] + e2 + ")";
		}

	}

	public static class JUnaryExpr extends JExpr {

		int op;
		JExpr e1;

		public JUnaryExpr(int op, JExpr e) {
			this.op = op;
			this.e1 = e;
		}

		public String toString() {
			return OP_STR[op] + e1;
		}

	}

	/**
	 * We admit the use of C conditional expression: (e?e1:e2) as a JExpr. Note that
	 * for this expression to be wellTyped, e musty be a boolean expression while e1
	 * and e2 must have the same type. The type of the expression is that of e1 (and
	 * e2).
	 * 
	 * @author chencc
	 *
	 */
	public static class JIf extends JExpr {
		JExpr e1, e2;
		JExpr cond;

		public JIf(JExpr cond, JExpr e1, JExpr e2) {
			this.cond = cond;
			this.e1 = e1;
			this.e2 = e2;
		}

		public String toString() {
			return "(" + cond + " ? " + e1 + ":" + e2 + ")";
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/// The following part is all methods you are required to implement. /////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Given a JExpr expr, find all variables occurring in it. Note that no
	 * duplicated variables are allowed in the result list.
	 * 
	 * @param expr
	 *            a wellTyped JExpr.
	 * @return
	 */
	public static List getVariables(JExpr expr) {

		List rlt = new ArrayList();

		// a series of case tests
		if (expr instanceof JVariable) {
			rlt.add(expr);
		} else if (expr instanceof JBinaryExpr) {
			// put your code here!
			rlt.addAll(getVariables(((JBinaryExpr)expr).e1));
			rlt.addAll(getVariables(((JBinaryExpr)expr).e2));
		} else if (expr instanceof JUnaryExpr) {
			return getVariables(((JUnaryExpr) expr).e1);
		} else if (expr instanceof JIf) {
			// put your code here
			rlt.addAll(getVariables(((JIf)expr).cond));
			rlt.addAll(getVariables(((JIf)expr).e1));
			rlt.addAll(getVariables(((JIf)expr).e2));
		} else { // expr is a literal ; need do nothing!

		}
		// deal with duplicated variables
		Set<String> hs = new LinkedHashSet<>();
		hs.addAll(rlt);
		rlt.clear();
		rlt.addAll(hs);
		return rlt;
	}

	/**
	 * Check if the input argument expression is well-typed.
	 * 
	 * A JExpr is wellTyped if is has no type errors.
	 * <ul>
	 * Well-typed examples:
	 * <li>3 + 5;
	 * <li>4 > x ? 4 : 6;
	 * <li>x == 4 ? true : 4 < 3;
	 * <li>4 != 4 | false == true ;
	 * </ul>
	 * <ul>
	 * Ill-typed examples:
	 * <li>3 + true;
	 * <li>4 > x ? 6 : false;
	 * <li>true & 5
	 * </ul>
	 *
	 * @param expr
	 * @return
	 */
	public static boolean isWellTyped(JExpr expr) {
		// put you code here!
		if(expr instanceof JLiteral || expr instanceof JVariable) return true;
		else if(expr instanceof JUnaryExpr) {
			if(isWellTyped((((JUnaryExpr)expr).e1))) {
				if(getType(((JUnaryExpr)expr).e1).toString().equals("number")) {
					if(((JUnaryExpr)expr).op == 13) return true;
					else return false;
				}
				else {
					if(((JUnaryExpr)expr).op == 14) return true;
					else return false;
				}
			}
			else return false;
		}
		else if(expr instanceof JBinaryExpr) {
			if(isWellTyped((((JBinaryExpr)expr).e1)) && isWellTyped((((JBinaryExpr)expr).e2))) {
				if(getType(((JBinaryExpr)expr).e1).toString().equals("number") && getType(((JBinaryExpr)expr).e2).toString().equals("number")){
					if(((JBinaryExpr)expr).op <= 9) return true;
					else return false;
				}
				else if(getType(((JBinaryExpr)expr).e1).toString().equals("boolean") && getType(((JBinaryExpr)expr).e2).toString().equals("boolean")) {
					if((((JBinaryExpr)expr).op > 7 && ((JBinaryExpr)expr).op <= 12) || ((JBinaryExpr)expr).op == 14) return true;
					else return false;
				}
				else return false;
			}
			else return false;
		}
		else if(expr instanceof JIf) {
			if(isWellTyped((((JIf)expr).cond)) && isWellTyped((((JIf)expr).e1)) && isWellTyped((((JIf)expr).e2))) {
				if(getType(((JIf)expr).e1).toString().equals(getType(((JIf)expr).e2).toString())) return true;
				else return false;
			}
			else return false;
		}
		else return false; 
	}

	/**
	 * Given a JExpr expr which is assumed to be well-typed, find the type of expr.
	 * 
	 * @param expr
	 *            a wellTyped JExpr.
	 * @return
	 */
	public static JType getType(JExpr expr) {
		// put you code here!
		// cases you need to check :
		// 1. expr = e1 op e2 => return JBOOLEAN if op is logical or comparison op and
		// return JNUMBER otherwie.
		// 2. expr = op e1 => return JBOOLEAN if op is ! and JNUMBER if op is -.
		// 3. expr is a literal => return JBOOLEAN if expr is a booealn and JNUMBER if
		// it is a number.
		// 4. expr is a variable => return JBOOLEAN if expr is a boolean variable or
		// JNUMBER otherwise.
		// 5. expr is e?e1:e2 => return the type of e1 (or e2).
		if(expr instanceof JBinaryExpr) {
			if(getType((((JBinaryExpr)expr).e1)).toString().equals("number") && getType((((JBinaryExpr)expr).e2)).toString().equals("number")) {
				if(((JBinaryExpr)expr).op > 3) return JType.JBOOLEAN;
				else return JType.JNUMBER;
			}
			else if(getType((((JBinaryExpr)expr).e1)).toString().equals("boolean") && getType((((JBinaryExpr)expr).e2)).toString().equals("boolean")) {
				if(((JBinaryExpr)expr).op > 3) return JType.JBOOLEAN;
				else return null;
			}
			else return null;
		}
		else if(expr instanceof JUnaryExpr) {
			if(getType((((JUnaryExpr)expr).e1)).toString().equals("number")) {
				if(((JUnaryExpr)expr).op == 14) return JType.JBOOLEAN;
				else if(((JUnaryExpr)expr).op == 13) return JType.JNUMBER;
				else return null;
			}
			else if(getType((((JUnaryExpr)expr).e1)).toString().equals("boolean")) {
				if(((JUnaryExpr)expr).op == 14) return JType.JBOOLEAN;
				else return null;
			}
		}
		else if(expr instanceof JLiteral) {
			if(expr instanceof JBoolLiteral) return JType.JBOOLEAN;
			else return JType.JNUMBER;
		}
		else if(expr instanceof JVariable) {
			if(expr instanceof JBoolVariable) return JType.JBOOLEAN;
			else return JType.JNUMBER;
		}
		else if(expr instanceof JIf) {
			if(getType((((JIf)expr).e1)).toString().equals("number")) return JType.JNUMBER;
			else if(getType((((JIf)expr).e1)).toString().equals("boolean")) return JType.JBOOLEAN;
			else return null;
		}
		return null;
	}

	/**
	 * Given a well-typed JExpr expr, we may want to find the value of this expr by
	 * evaluating it. Examples: for expr : 4 + 5, its value is 9. However, since an
	 * expression may contain variables, we thus need a table mapping variables to
	 * their values. The static variable varMap serves this role. Before the
	 * evaluation of a global JExpr, we need to first assign values to all variables
	 * occurring in it by populate (Variable, value) pairs in this varMap. Note: All
	 * variables not found in this table are assumed to have value 0 or false
	 * depending on their types.
	 */

	public static Map varMap = new HashMap();

	static { // initial contents of varMap
		varMap.put("x", JVALUE(3));
		varMap.put("y", JVALUE(4));
		varMap.put("z", JVALUE(5));
		varMap.put("A", JVALUE(2));
		varMap.put("B", JVALUE(10));
		varMap.put("C", JVALUE(5));
		varMap.put("b1", JVALUE(true));
		varMap.put("b2", JVALUE(false));
		varMap.put("b3", JVALUE(true));
	}

	/**
	 * Given a well-typed JExpr expr, find the value of this expr by evaluating it.
	 * Examples: 4 + 5 => return 9 true ? 4 : 7 => return 4 false ? false : true =>
	 * return true x + 5 => return varMap.get(x) + 5, where varMap is the the static
	 * map storing value of all variables.
	 * 
	 * @param expr
	 *            a wellTyped JExpr.
	 * @return
	 * 
	 */
	public static JValue getValue(JExpr expr) {
		// put you code here!
		if(expr instanceof JVariable) return (JValue)(varMap.get(((JVariable)expr).name));
		else if(expr instanceof JBoolLiteral) return JVALUE(((JBoolLiteral)expr).v);
		else if(expr instanceof JNumLiteral) return JVALUE(((JNumLiteral)expr).d);
		else if(expr instanceof JUnaryExpr) {
			if(((JUnaryExpr)expr).op == 13) return JVALUE(((JNumValue)getValue(((JUnaryExpr)expr).e1)).v * (-1));
			else return JVALUE(!((JBoolValue)getValue(((JUnaryExpr)expr).e1)).v);
		}
		else if(expr instanceof JBinaryExpr) {
			switch (((JBinaryExpr)expr).op) {
				case 0: return JVALUE(((JNumValue)getValue(((JBinaryExpr)expr).e1)).v + ((JNumValue)getValue(((JBinaryExpr)expr).e2)).v);
				case 1: return JVALUE(((JNumValue)getValue(((JBinaryExpr)expr).e1)).v - ((JNumValue)getValue(((JBinaryExpr)expr).e2)).v);
				case 2: return JVALUE(((JNumValue)getValue(((JBinaryExpr)expr).e1)).v * ((JNumValue)getValue(((JBinaryExpr)expr).e2)).v);
				case 3: return JVALUE(((JNumValue)getValue(((JBinaryExpr)expr).e1)).v / ((JNumValue)getValue(((JBinaryExpr)expr).e2)).v);
				case 4: return JVALUE(((JNumValue)getValue(((JBinaryExpr)expr).e1)).v < ((JNumValue)getValue(((JBinaryExpr)expr).e2)).v);
				case 5: return JVALUE(((JNumValue)getValue(((JBinaryExpr)expr).e1)).v <= ((JNumValue)getValue(((JBinaryExpr)expr).e2)).v);
				case 6: return JVALUE(((JNumValue)getValue(((JBinaryExpr)expr).e1)).v > ((JNumValue)getValue(((JBinaryExpr)expr).e2)).v);
				case 7: return JVALUE(((JNumValue)getValue(((JBinaryExpr)expr).e1)).v >= ((JNumValue)getValue(((JBinaryExpr)expr).e2)).v);
				case 8: return JVALUE(((JNumValue)getValue(((JBinaryExpr)expr).e1)).v == ((JNumValue)getValue(((JBinaryExpr)expr).e2)).v);
				case 9: return JVALUE(((JNumValue)getValue(((JBinaryExpr)expr).e1)).v != ((JNumValue)getValue(((JBinaryExpr)expr).e2)).v);
				case 10: return JVALUE(((JBoolValue)getValue(((JBinaryExpr)expr).e1)).v & ((JBoolValue)getValue(((JBinaryExpr)expr).e2)).v);
				case 11: return JVALUE(((JBoolValue)getValue(((JBinaryExpr)expr).e1)).v | ((JBoolValue)getValue(((JBinaryExpr)expr).e2)).v);
				case 12: return JVALUE(((JBoolValue)getValue(((JBinaryExpr)expr).e1)).v ^ ((JBoolValue)getValue(((JBinaryExpr)expr).e2)).v);
			}
		}
		else if(expr instanceof JIf) {
			if(((JBoolValue)getValue(((JIf)expr).cond)).v) return getValue(((JIf)expr).e1);
			else return getValue(((JIf)expr).e2);
		}
		return null;
	}

	/**
	 * We can use JExpr.toString() to get a string representation of an expression.
	 * Following are some examples output: (3 + 4), x, (3>4 ? 3 : (4+5)), ((3 + 4) *
	 * 5), ((4*5)+ 3)). It is noted that some parenthesis '(', ')' in the output are
	 * necessary like (3+4) * 5, while others like those in ((4*5)+3) are not needed
	 * [4*5+3 is our expected result]. This method require you to generate a string
	 * representation of the input JExpr like those produced by JExpr.toString() BUT
	 * the additional requirement is that all unnecessary parentheses '(',')' should
	 * not appear in the output.
	 * 
	 * Assumptions:
	 * <ul>
	 * <li>Precedences of operations are defined as follows:
	 * <ul>
	 * <li>1. unary op > binary op > ternary (?:).
	 * <li>2. (*,/) > (+,-) > (<,<=,>,>=) > (==,!=) > (OR, XOR) > AND</li>
	 * </ul>
	 * The precedence of each operation is given in the array JExpr.PRECEDENCE and
	 * can be queried by JExpr.prec(op).
	 * 
	 * <li>Assume all operations are left-associative. i.e. e1 o e2 o e3 means ((e1
	 * o e2) o e3).
	 * </ul>
	 * Note You may add additional methods/fields to HW3 for your purpose provided
	 * they do not override existing ones.
	 */
	public static String prettyString(JExpr expr) {
		String str = new String();
		if(expr instanceof JVariable || expr instanceof JLiteral) {
			if(expr instanceof JBoolVariable) return ((JBoolVariable) expr).name;
			else if(expr instanceof JNumVariable) return ((JNumVariable) expr).name;
			else if(expr instanceof JBoolLiteral) return ((JBoolLiteral) expr).toString();
			else if(expr instanceof JNumLiteral) return ((JNumLiteral) expr).toString();
			else return null;
		}
		else if(expr instanceof JUnaryExpr) {
			if(needBracket(((JUnaryExpr) expr).e1, ((JUnaryExpr) expr).op))
				return expr.OP_STR[((JUnaryExpr) expr).op] + "(" + prettyString(((JUnaryExpr) expr).e1) + ")";
			else return expr.OP_STR[((JUnaryExpr) expr).op] + prettyString(((JUnaryExpr) expr).e1);
		}
		else if(expr instanceof JBinaryExpr) {
			if(needBracket(((JBinaryExpr) expr).e1, ((JBinaryExpr) expr).op) && needBracket(((JBinaryExpr) expr).e2, ((JBinaryExpr) expr).op))
				return "(" + prettyString(((JBinaryExpr) expr).e1) + ")" + expr.OP_STR[((JBinaryExpr) expr).op] 
					+ "(" + prettyString(((JBinaryExpr) expr).e2) + ")";
			else if(needBracket(((JBinaryExpr) expr).e1, ((JBinaryExpr) expr).op) && !needBracket(((JBinaryExpr) expr).e2, ((JBinaryExpr) expr).op))
				return "(" + prettyString(((JBinaryExpr) expr).e1) + ")" + expr.OP_STR[((JBinaryExpr) expr).op] 
						+ prettyString(((JBinaryExpr) expr).e2);
			else if(!needBracket(((JBinaryExpr) expr).e1, ((JBinaryExpr) expr).op) && needBracket(((JBinaryExpr) expr).e2, ((JBinaryExpr) expr).op))
				return prettyString(((JBinaryExpr) expr).e1) + expr.OP_STR[((JBinaryExpr) expr).op] 
						+ "(" + prettyString(((JBinaryExpr) expr).e2) + ")";
			else return prettyString(((JBinaryExpr) expr).e1) + expr.OP_STR[((JBinaryExpr) expr).op] 
					 + prettyString(((JBinaryExpr) expr).e2);
		}
		else if(expr instanceof JIf)
			return prettyString(((JIf) expr).cond) + "?" + prettyString(((JIf) expr).e1) + ":" + prettyString(((JIf) expr).e2);
		return null;
	}
	
	public static boolean needBracket(JExpr exprInside, int opOutside) {
		if(exprInside instanceof JUnaryExpr) {
			if(exprInside.PRECEDENCE[((JUnaryExpr) exprInside).op] < exprInside.PRECEDENCE[opOutside])
				return true;
			else return false;
		}
		else if(exprInside instanceof JBinaryExpr) {
			if(exprInside.PRECEDENCE[((JBinaryExpr) exprInside).op] < exprInside.PRECEDENCE[opOutside])
				return true;
			else return false;
		}
		else if(exprInside instanceof JIf) {
			if(exprInside instanceof JVariable) return false;
			else return true;
		}
		return false;
	}

	///////////////////////////////////////////////////////////////////////////////////
	/// The code below are factory methods for quick generation of JExpr isstances.
	/////////////////////////////////////////////////////////////////////////////////// //
	///////////////////////////////////////////////////////////////////////////////////

	public static JBinaryExpr JADD(JExpr e1, JExpr e2) {
		return new JBinaryExpr(ADD, e1, e2);
	}

	public static JBinaryExpr JSUB(JExpr e1, JExpr e2) {
		return new JBinaryExpr(SUB, e1, e2);
	}

	public static JBinaryExpr JMUL(JExpr e1, JExpr e2) {
		return new JBinaryExpr(MUL, e1, e2);
	}

	public static JBinaryExpr JDIV(JExpr e1, JExpr e2) {
		return new JBinaryExpr(DIV, e1, e2);
	}

	public static JBinaryExpr JLT(JExpr e1, JExpr e2) {
		return new JBinaryExpr(LT, e1, e2);
	}

	public static JBinaryExpr JLE(JExpr e1, JExpr e2) {
		return new JBinaryExpr(LE, e1, e2);
	}

	public static JBinaryExpr JGT(JExpr e1, JExpr e2) {
		return new JBinaryExpr(GT, e1, e2);
	}

	public static JBinaryExpr JGE(JExpr e1, JExpr e2) {
		return new JBinaryExpr(GE, e1, e2);
	}

	public static JBinaryExpr JEQ(JExpr e1, JExpr e2) {
		return new JBinaryExpr(EQ, e1, e2);
	}

	public static JBinaryExpr JNE(JExpr e1, JExpr e2) {
		return new JBinaryExpr(NE, e1, e2);
	}

	public static JBinaryExpr JAND(JExpr e1, JExpr e2) {
		return new JBinaryExpr(AND, e1, e2);
	}

	public static JBinaryExpr JOR(JExpr e1, JExpr e2) {
		return new JBinaryExpr(OR, e1, e2);
	}

	public static JBinaryExpr JXOR(JExpr e1, JExpr e2) {
		return new JBinaryExpr(XOR, e1, e2);
	}

	public static JUnaryExpr JNEG(JExpr e1) {
		return new JUnaryExpr(NEG, e1);
	}

	public static JUnaryExpr JNOT(JExpr e1) {
		return new JUnaryExpr(NOT, e1);
	}

	public static JIf JIF(JExpr e, JExpr e1, JExpr e2) {
		return new JIf(e, e1, e2);
	}

	public static JNumLiteral JNUM(double d) {
		return new JNumLiteral(d);
	}

	public static JBoolLiteral JBOOL(boolean b) {
		return new JBoolLiteral(b);
	}

	public static JNumLiteral JLIT(double d) {
		return JNUM(d);
	}

	public static JBoolLiteral JLIT(boolean b) {
		return JBOOL(b);
	}

	public static JVariable JVAR(String s) {
		if (s.startsWith("b")) {
			return new JBoolVariable(s);
		} else {
			return new JNumVariable(s);
		}

	}

	public static JNumValue JVALUE(double d) {
		return new JNumValue(d);
	}

	public static JBoolValue JVALUE(boolean d) {
		return new JBoolValue(d);
	}

}
