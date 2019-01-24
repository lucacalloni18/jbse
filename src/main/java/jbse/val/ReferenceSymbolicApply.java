package jbse.val;

import java.util.Arrays;

import jbse.val.exc.InvalidOperandException;

/**
 * Class of values representing the {@link ReferenceSymbolic} returned by the 
 * execution of a pure method on a set of {@link Value}s.
 * 
 * @author Pietro Braione
 */
public final class ReferenceSymbolicApply extends ReferenceSymbolic {
    //pure functions 
    //TODO move them elsewhere? should make an enum? (no special advantage in both)
    
    /** java.lang.Object.toString */
    public static final String TO_STRING = "toString";
    public static final String EQUALS = "equals";
    
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
     * @param staticType a {@link String}, the static type of the
     *        reference (taken from bytecode).
     * @param historyPoint the current  {@link HistoryPoint}.
     * @param operator the name of the function.
     * @param args the {@link Value} arguments to which the function is applied.
	 * @throws InvalidOperandException if any of {@code args} is null. 
	 */
	public ReferenceSymbolicApply(String staticType, HistoryPoint historyPoint, String operator, Value... args) 
	throws InvalidOperandException {
		super(staticType, historyPoint);
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
		final int prime = 7043;
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
		if (historyPoint == null) {
		    buf.append(")");
		} else {
		    buf.append(")@");
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
	
	@Override
	public ReferenceSymbolic root() {
		return this;
	}
	
	@Override
	public String asOriginString() {
            final StringBuilder buf = new StringBuilder();
            buf.append(this.operator + "(");
            boolean first = true;
            for (Value v : this.args) {
                    buf.append(first ? "" : ",");
                    buf.append(v.isSymbolic() ? ((Symbolic) v).asOriginString() : v.toString());
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
		final ReferenceSymbolicApply other = (ReferenceSymbolicApply) obj;
		if (!Arrays.equals(this.args, other.args))
			return false;
		if (this.operator == null) {
			if (other.operator != null) {
				return false;
			}
		} else if (!this.operator.equals(other.operator)) {
			return false;
		}
                if (historyPoint() == null) {
                    if (other.historyPoint() != null) {
                        return false;
                    }
                } else if (!historyPoint().equals(other.historyPoint())) {
                    return false;
                }
		return true;
	}
	}
