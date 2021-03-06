package jbse.val;

import static jbse.bc.Signatures.JAVA_STRICTMATH_ABS_DOUBLE;
import static jbse.bc.Signatures.JAVA_STRICTMATH_ABS_FLOAT;
import static jbse.bc.Signatures.JAVA_STRICTMATH_ABS_INT;
import static jbse.bc.Signatures.JAVA_STRICTMATH_ABS_LONG;
import static jbse.bc.Signatures.JAVA_STRICTMATH_ACOS;
import static jbse.bc.Signatures.JAVA_STRICTMATH_ASIN;
import static jbse.bc.Signatures.JAVA_STRICTMATH_ATAN;
import static jbse.bc.Signatures.JAVA_STRICTMATH_COS;
import static jbse.bc.Signatures.JAVA_STRICTMATH_EXP;
import static jbse.bc.Signatures.JAVA_STRICTMATH_MAX_DOUBLE;
import static jbse.bc.Signatures.JAVA_STRICTMATH_MAX_FLOAT;
import static jbse.bc.Signatures.JAVA_STRICTMATH_MAX_INT;
import static jbse.bc.Signatures.JAVA_STRICTMATH_MAX_LONG;
import static jbse.bc.Signatures.JAVA_STRICTMATH_MIN_DOUBLE;
import static jbse.bc.Signatures.JAVA_STRICTMATH_MIN_FLOAT;
import static jbse.bc.Signatures.JAVA_STRICTMATH_MIN_INT;
import static jbse.bc.Signatures.JAVA_STRICTMATH_MIN_LONG;
import static jbse.bc.Signatures.JAVA_STRICTMATH_POW;
import static jbse.bc.Signatures.JAVA_STRICTMATH_SQRT;
import static jbse.bc.Signatures.JAVA_STRICTMATH_SIN;
import static jbse.bc.Signatures.JAVA_STRICTMATH_TAN;

import java.util.Arrays;

import jbse.common.exc.UnexpectedInternalException;
import jbse.val.exc.InvalidOperandException;
import jbse.val.exc.InvalidTypeException;

/**
 * Class representing the {@link PrimitiveSymbolicComputed} returned by the 
 * execution of a pure method on a set of {@link Value}s.
 * 
 * @author Pietro Braione
 */
public final class PrimitiveSymbolicApply extends PrimitiveSymbolicComputed {
    //pure functions implemented in java.lang.StrictMath 
    
    /** Absolute value (double) */
    public static final String ABS_DOUBLE = JAVA_STRICTMATH_ABS_DOUBLE.toString();
    
    /** Absolute value (float) */
    public static final String ABS_FLOAT = JAVA_STRICTMATH_ABS_FLOAT.toString();
    
    /** Absolute value (int) */
    public static final String ABS_INT = JAVA_STRICTMATH_ABS_INT.toString();
    
    /** Absolute value (long) */
    public static final String ABS_LONG = JAVA_STRICTMATH_ABS_LONG.toString();
    
    /** Trigonometric sine */
    public static final String SIN = JAVA_STRICTMATH_SIN.toString();
    
    /** Trigonometric cosine */
    public static final String COS = JAVA_STRICTMATH_COS.toString();
    
    /** Trigonometric tangent */
    public static final String TAN = JAVA_STRICTMATH_TAN.toString();
    
    /** Trigonometric arc sine */
    public static final String ASIN = JAVA_STRICTMATH_ASIN.toString();
    
    /** Trigonometric arc cosine */
    public static final String ACOS = JAVA_STRICTMATH_ACOS.toString();
    
    /** Trigonometric arc tangent */
    public static final String ATAN = JAVA_STRICTMATH_ATAN.toString();
    
    /** Square root */
    public static final String SQRT = JAVA_STRICTMATH_SQRT.toString();
    
    /** Power */
    public static final String POW = JAVA_STRICTMATH_POW.toString();
    
    /** Exponential */
	public static final String EXP = JAVA_STRICTMATH_EXP.toString();
    
    /** Minimum (double) */
    public static final String MIN_DOUBLE = JAVA_STRICTMATH_MIN_DOUBLE.toString();
    
    /** Minimum (float) */
    public static final String MIN_FLOAT = JAVA_STRICTMATH_MIN_FLOAT.toString();
    
    /** Minimum (int) */
    public static final String MIN_INT = JAVA_STRICTMATH_MIN_INT.toString();
    
    /** Minimum (long) */
    public static final String MIN_LONG = JAVA_STRICTMATH_MIN_LONG.toString();
    
    /** Maximum */
    public static final String MAX = "max";
    
    /** Maximum (double) */
    public static final String MAX_DOUBLE = JAVA_STRICTMATH_MAX_DOUBLE.toString();
    
    /** Maximum (float) */
    public static final String MAX_FLOAT = JAVA_STRICTMATH_MAX_FLOAT.toString();
    
    /** Maximum (int) */
    public static final String MAX_INT = JAVA_STRICTMATH_MAX_INT.toString();
    
    /** Maximum (long) */
    public static final String MAX_LONG = JAVA_STRICTMATH_MAX_LONG.toString();
    
    /** The function name. */
	private final String operator;
	
	/** The args to which the function is applied, implicitly defining its arity. */
	private final Value[] args;
	
	/** The hash code of this object. */
    private final int hashCode;

    /** The string representation of this object. */
	private final String toString;
	
	/**
	 * Constructor. 
	 * 
     * @param type a {@code char}, the type of this {@link PrimitiveSymbolicApply}. 
     * @param historyPoint the current {@link HistoryPoint}.
     * @param calc a {@link Calculator}.
     * @param operator the name of the function.
     * @param args the {@link Value} arguments to which the function is applied.
	 * @throws InvalidOperandException if any of {@code args} is null. 
	 * @throws InvalidTypeException if {@code type} is not primitive.
	 */
	public PrimitiveSymbolicApply(char type, HistoryPoint historyPoint, Calculator calc, String operator, Value... args) 
	throws InvalidTypeException, InvalidOperandException {
		super(type, historyPoint, calc);
		this.operator = operator;
		this.args = args.clone();
		int i = 0;
		for (Value v : this.args) {
			if (v == null) {
				throw new InvalidOperandException(i + (i == 1 ? "-st" : i == 2 ? "-nd" : i == 3 ? "-rd ": "-th") + " argument is null");
			}
			++i;
		}
		
		//calculates hashCode
		final int prime = 191;
		int tmpHashCode = 1;
		tmpHashCode = prime * tmpHashCode + Arrays.hashCode(args);
		tmpHashCode = prime * tmpHashCode + ((operator == null) ? 0 : operator.hashCode());
                tmpHashCode = prime * tmpHashCode + ((historyPoint == null) ? 0 : historyPoint.hashCode());
		this.hashCode = tmpHashCode;
		
		//calculates toString
		final StringBuilder buf = new StringBuilder();
		buf.append(this.operator + "(");
		boolean first = true;
		for (Value v : this.args) {
			buf.append((first ? "" : ",") + v.toString());
			first = false;
		}
                buf.append(")");
                if (historyPoint != null) {
                    buf.append("@");
                    buf.append(historyPoint.toString());
                }
		this.toString = buf.toString();
	}
	
	public String getOperator() {
		return this.operator;
	}

	public Value[] getArgs() {
		return this.args.clone();
	}
	
	    /**
	     * {@inheritDoc}
	     */
	@Override
	public Primitive doReplace(Primitive from, Primitive to) {
	    final Value[] argsNew = new Value[this.args.length];
	    for (int i = 0; i < this.args.length; ++i) {
	        if (this.args[i].equals(from)) {
	            argsNew[i] = to;
	        } else if (this.args[i] instanceof PrimitiveSymbolicComputed) {
	            argsNew[i] = ((PrimitiveSymbolicComputed) this.args[i]).doReplace(from, to);
	        } else {
	            argsNew[i] = this.args[i];
	        }
	    }
	    
	    try {
	        return this.calc.applyFunctionPrimitive(this.getType(), historyPoint(), this.operator, argsNew); //TODO possible bug! Here rewriting is applied!
	    } catch (InvalidOperandException | InvalidTypeException e) {
                //this should never happen
                throw new UnexpectedInternalException(e);
	    } 
	}
	
	@Override
	public String asOriginString() {
            final StringBuilder buf = new StringBuilder();
            buf.append(this.operator + "(");
            boolean first = true;
            for (Value v : this.args) {
                    buf.append((first ? "" : ",") + (v.isSymbolic() ? ((Symbolic) v).asOriginString() : v.toString()));
                    first = false;
            }
            if (historyPoint() == null) {
                buf.append(")");
            } else {
                buf.append(")@");
                buf.append(historyPoint().toString());
            }
            return buf.toString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void accept(PrimitiveVisitor v) throws Exception {
		v.visitPrimitiveSymbolicApply(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.toString;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return this.hashCode;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PrimitiveSymbolicApply other = (PrimitiveSymbolicApply) obj;
		if (!Arrays.equals(this.args, other.args))
			return false;
		if (this.operator == null) {
			if (other.operator != null) {
				return false;
			}
		} else if (!this.operator.equals(other.operator)) {
			return false;
		}
		if (this.historyPoint() == null) {
		    if (other.historyPoint() != null) {
		        return false;
		    }
		} else if (!this.historyPoint().equals(other.historyPoint())) {
		    return false;
		}
		return true;
	}
}
