/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.antlr.v4.gui.Trees;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

import expressivo.parser.ExpressionLexer;
import expressivo.parser.ExpressionListener;
import expressivo.parser.ExpressionParser;
import expressivo.parser.ExpressionParser.MultiplyContext;
import expressivo.parser.ExpressionParser.PrimitiveContext;
import expressivo.parser.ExpressionParser.RootContext;
import expressivo.parser.ExpressionParser.SumContext;

/**
 * An immutable data type representing a polynomial expression of:
 *   + and *
 *   nonnegative integers and floating-point numbers
 *   variables (case-sensitive nonempty strings of letters)
 * 
 * <p>PS3 instructions: this is a required ADT interface.
 * You MUST NOT change its name or package or the names or type signatures of existing methods.
 * You may, however, add additional methods, or strengthen the specs of existing methods.
 * Declare concrete variants of Expression in their own Java source files.
 */
public interface Expression {
    
    // Datatype definition
    // Expr = Number(number:double) + Letter(letter:String) + Plus(left:Expr, right:Expr) + Multiply(left:Expr, right:Expr)
    
    /**
     * Parse an expression.
     * @param input expression to parse, as defined in the PS3 handout.
     * @return expression AST for the input
     * @throws IllegalArgumentException if the expression is invalid
     */
    public static Expression parse(String input) {
        CharStream stream = new ANTLRInputStream(input);
        
        ExpressionLexer lexer = new ExpressionLexer(stream);
        lexer.reportErrorsAsExceptions();
        TokenStream tokens = new CommonTokenStream(lexer);
        
        ExpressionParser parser = new ExpressionParser(tokens);
        parser.reportErrorsAsExceptions();
        
        ParseTree tree = parser.root();
        
        // debugging
        Trees.inspect(tree, parser);
        
        ExpressionMaker exprMaker = new ExpressionMaker();
        new ParseTreeWalker().walk(exprMaker, tree);
        return exprMaker.getExpression();
    }
    
    /**
     * @return a parsable representation of this expression, such that
     * for all e:Expression, e.equals(Expression.parse(e.toString())).
     */
    @Override 
    public String toString();

    /**
     * @param thatObject any object
     * @return true if and only if this and thatObject are structurally-equal
     * Expressions, as defined in the PS3 handout.
     */
    @Override
    public boolean equals(Object thatObject);
    
    /**
     * @return hash code value consistent with the equals() definition of structural
     * equality, such that for all e1,e2:Expression,
     *     e1.equals(e2) implies e1.hashCode() == e2.hashCode()
     */
    @Override
    public int hashCode();
    
    // TODO more instance methods
    public Expression differentiate(Letter var);
    public Expression simplify(Map<String, Double> env);
}

class ExpressionMaker implements ExpressionListener {
	private Stack<Expression> stack = new Stack<>();
	
	public Expression getExpression() {
		return stack.get(0);
	}
	
	@Override
	public void enterEveryRule(ParserRuleContext arg0) {}

	@Override
	public void exitEveryRule(ParserRuleContext arg0) {}

	@Override
	public void visitErrorNode(ErrorNode arg0) {}

	@Override
	public void visitTerminal(TerminalNode arg0) {}

	@Override
	public void enterRoot(RootContext ctx) {}

	@Override
	public void exitRoot(RootContext ctx) {}

	@Override
	public void enterMultiply(MultiplyContext ctx) {}

	@Override
	public void exitMultiply(MultiplyContext ctx) {
		List<PrimitiveContext> multEnds = ctx.primitive();
		
		Expression mult = stack.pop();
		for (int i = 1; i < multEnds.size(); i++) {
			mult = new Multiply(stack.pop(), mult);
		}
		stack.push(mult);
	}

	@Override
	public void enterSum(SumContext ctx) {}

	@Override
	public void exitSum(SumContext ctx) {
		List<MultiplyContext> addEnds = ctx.multiply();
		
		Expression sum = stack.pop();
		for (int i = 1; i < addEnds.size(); i++) {
			sum = new Plus(stack.pop(), sum);
		}
		stack.push(sum);
	}

	@Override
	public void enterPrimitive(PrimitiveContext ctx) {}

	@Override
	public void exitPrimitive(PrimitiveContext ctx) {
		if (ctx.NUMBER() != null) {
			// matched the NUMBER terminal
			Double n = Double.valueOf(ctx.NUMBER().getText());
			Expression number = new Number(n);
			stack.push(number);
		} else if (ctx.LETTER() != null) {
			String l = ctx.LETTER().getText();
			Expression letter = new Letter(l);
			stack.push(letter);
		} else {
			 // matched the '(' sum ')' alternative
			 // do nothing, because the value is already on the stack
		}
	}
}

class Number implements Expression {
	private final double number;
	public Number(double n) {
		number = n;
	}
	@Override
	public String toString() {
		return String.valueOf(number);
	}
	@Override
	public boolean equals(Object thatObject) {
		if (thatObject instanceof Number) {
			return ((Number)thatObject).number == number;
		} else {
			return false;
		}
	}
	@Override
	public int hashCode() {
		return Double.hashCode(number);
	}
	@Override
	public Expression differentiate(Letter var) {
		return new Number(0);
	}
	@Override
	public Expression simplify(Map<String, Double> env) {
		return this;
	}
	public double value() {
		return number;
	}
}

class Letter implements Expression {
	private final String letter;
	public Letter(String l) {
		letter = l;
	}
	@Override
	public String toString() {
		return letter;
	}
	@Override
	public boolean equals(Object thatObject) {
		if (thatObject instanceof Letter) {
			return ((Letter)thatObject).letter.equals(letter);
		} else {
			return false;
		}
	}
	@Override
	public int hashCode() {
		return letter.hashCode();
	}
	@Override
	public Expression differentiate(Letter var) {
		if (var.equals(this)) {
			return new Number(1);
		}
		return new Number(0);
	}
	@Override
	public Expression simplify(Map<String, Double> env) {
		if (env.containsKey(this.value())) {
			return new Number(env.get(this.value()));
		} else {
			return this;
		}
	}
	public String value() {
		return letter;
	}
}

class Plus implements Expression {
	private final Expression left, right;
	public Plus(Expression l, Expression r) {
		left = l;
		right = r;
	}
	@Override
	public String toString() {
		String lstr, rstr;
		if (left instanceof Number || left instanceof Letter) {
			lstr = left.toString();
		} else {
			lstr = '(' + left.toString() + ')';
		}
		if (right instanceof Number || right instanceof Letter) {
			rstr = right.toString();
		} else {
			rstr = '(' + right.toString() + ')';
		}
		return lstr + '+' + rstr;
	}
	@Override
	public boolean equals(Object thatObject) {
		if (thatObject instanceof Plus) {
			Plus thatExpr = (Plus)thatObject;
			return thatExpr.left.equals(left) && thatExpr.right.equals(right);
		} else {
			return false;
		}
	}
	@Override
	public int hashCode() {
		return left.hashCode() + right.hashCode();
	}
	@Override
	public Expression differentiate(Letter var) {
		return new Plus(left.differentiate(var), right.differentiate(var));
	}
	@Override
	public Expression simplify(Map<String, Double> env) {
		Expression r = right.simplify(env);
		Expression l = left.simplify(env);
		if (r instanceof Number && l instanceof Number) {
			return new Number(((Number)r).value() + ((Number)l).value());
		} else {
			return new Plus(r, l);
		}
	}
}

class Multiply implements Expression {
	private final Expression left, right;
	public Multiply(Expression l, Expression r) {
		left = l;
		right = r;
	}
	@Override
	public String toString() {
		String lstr, rstr;
		if (left instanceof Number || left instanceof Letter) {
			lstr = left.toString();
		} else {
			lstr = '(' + left.toString() + ')';
		}
		if (right instanceof Number || right instanceof Letter) {
			rstr = right.toString();
		} else {
			rstr = '(' + right.toString() + ')';
		}
		return lstr + '*' + rstr;
	
	}
	@Override
	public boolean equals(Object thatObject) {
		if (thatObject instanceof Multiply) {
			Multiply thatExpr = (Multiply)thatObject;
			return thatExpr.left.equals(left) && thatExpr.right.equals(right);
		} else {
			return false;
		}
	}
	@Override
	public int hashCode() {
		return left.hashCode() + right.hashCode();
	}
	@Override
	public Expression differentiate(Letter var) {
		Multiply l = new Multiply(left, right.differentiate(var));
		Multiply r = new Multiply(right, left.differentiate(var));
		return new Plus(l, r);
	}
	@Override
	public Expression simplify(Map<String, Double> env) {
		Expression r = right.simplify(env);
		Expression l = left.simplify(env);
		if (r instanceof Number && l instanceof Number) {
			return new Number(((Number)r).value() * ((Number)l).value());
		} else {
			return new Multiply(r, l);
		}
	}
}



