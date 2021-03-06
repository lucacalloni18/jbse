package jbse.val;

import jbse.bc.ClassFile;
import jbse.common.Type;
import jbse.common.exc.UnexpectedInternalException;
import jbse.mem.Klass;
import jbse.mem.Objekt;
import jbse.val.exc.InvalidTypeException;

/**
 * A SymbolFactory creates symbolic values for all the possible
 * origin sources of symbols.
 * 
 * @author Pietro Braione
 */
public final class SymbolFactory implements Cloneable {
    /** The {@link Calculator}. */
    private final Calculator calc;

	/** The next available identifier for a new reference-typed symbolic value. */
	private int nextIdRefSym;

	/** The next available identifier for a new primitive-typed symbolic value. */
	private int nextIdPrimSym;
    
	public SymbolFactory(Calculator calc) {
        this.calc = calc;
		this.nextIdRefSym = 0;
		this.nextIdPrimSym = 0;
	}
	
	/**
	 * A Factory Method for creating symbolic values. The symbol
	 * has as origin a local variable in the current frame.
	 * 
	 * @param historyPoint the current {@link HistoryPoint}.
	 * @param staticType a {@link String}, the static type of the
	 *        local variable from which the symbol originates.
	 * @param variableName a {@link String}, the name of the local 
	 *        variable in the root frame the symbol originates from.
	 * @return a {@link PrimitiveSymbolic} or a {@link ReferenceSymbolic}
	 *         according to {@code staticType}.
	 */
        public Value createSymbolLocalVariable(HistoryPoint historyPoint, String staticType, String variableName) {
        try {
            final Value retVal;
            if (Type.isPrimitive(staticType)) {
                retVal = new PrimitiveSymbolicLocalVariable(variableName, getNextIdPrimitiveSymbolic(), staticType.charAt(0), historyPoint, this.calc);
            } else {
                retVal = new ReferenceSymbolicLocalVariable(variableName, getNextIdReferenceSymbolic(), staticType, historyPoint);
            }
            return retVal;
        } catch (InvalidTypeException e) {
            //this should never happen
            throw new UnexpectedInternalException(e);
        }
        }
	
        /**
         * A Factory Method for creating symbolic values. The symbol
         * is a (pseudo)reference to a {@link Klass}.
         * 
	 * @param historyPoint the current {@link HistoryPoint}.
         * @param classFile the {@link ClassFile} for the {@link Klass} to be referred.
         * @return a {@link KlassPseudoReference}.
         */
        public KlassPseudoReference createSymbolKlassPseudoReference(HistoryPoint historyPoint, ClassFile classFile) {
            final KlassPseudoReference retVal = new KlassPseudoReference(classFile, historyPoint);
            return retVal;
        }
        
        /**
         * A Factory Method for creating symbolic values. The symbol
         * has as origin a field in an object (non array). 
         * 
         * @param staticType a {@link String}, the static type of the
         *        local variable from which the symbol originates.
         * @param container a {@link ReferenceSymbolic}, the container object
         *        the symbol originates from. It must not refer an array.
         * @param fieldName a {@link String}, the name of the field in the 
         *        container object the symbol originates from.
         * @return a {@link PrimitiveSymbolic} or a {@link ReferenceSymbolic}
         *         according to {@code staticType}.
         */
	public Value createSymbolMemberField(String staticType, ReferenceSymbolic container, String fieldName) {
        try {
            final Value retVal;
            if (Type.isPrimitive(staticType)) {
                retVal = new PrimitiveSymbolicMemberField(container, fieldName, getNextIdPrimitiveSymbolic(), staticType.charAt(0), this.calc);
            } else {
                retVal = new ReferenceSymbolicMemberField(container, fieldName, getNextIdReferenceSymbolic(), staticType);
            }
            return retVal;
        } catch (InvalidTypeException e) {
            //this should never happen
            throw new UnexpectedInternalException(e);
        }
	}
        
        /**
         * A Factory Method for creating symbolic values. The symbol
         * has as origin a slot in an array.  
         * 
         * @param staticType a {@link String}, the static type of the
         *        local variable from which the symbol originates.
         * @param container a {@link ReferenceSymbolic}, the container object
         *        the symbol originates from. It must refer an array.
         * @param index a {@link Primitive}, the index of the slot in the 
         *        container array this symbol originates from.
         * @return a {@link PrimitiveSymbolic} or a {@link ReferenceSymbolic}
         *         according to {@code staticType}.
         */
        public Value createSymbolMemberArray(String staticType, ReferenceSymbolic container, Primitive index) {
        try {
            final Value retVal;
            if (Type.isPrimitive(staticType)) {
                retVal = new PrimitiveSymbolicMemberArray(container, index, getNextIdPrimitiveSymbolic(), staticType.charAt(0), this.calc);
            } else {
                retVal = new ReferenceSymbolicMemberArray(container, index, getNextIdReferenceSymbolic(), staticType);
            }
            return retVal;
        } catch (InvalidTypeException e) {
            //this should never happen
            throw new UnexpectedInternalException(e);
        }
        }
	
        /**
         * A Factory Method for creating symbolic values. The symbol
         * has as origin the length of an array.  
         * 
         * @param container a {@link ReferenceSymbolic}, the container object
         *        the symbol originates from. It must refer an array.
         * @return a {@link PrimitiveSymbolic}.
         */
        public PrimitiveSymbolic createSymbolMemberArrayLength(ReferenceSymbolic container) {
        try {
            final PrimitiveSymbolicMemberArrayLength retVal = new PrimitiveSymbolicMemberArrayLength(container, getNextIdPrimitiveSymbolic(), this.calc);
            return retVal;
        } catch (InvalidTypeException e) {
            //this should never happen
            throw new UnexpectedInternalException(e);
        }
        }
        
        /**
         * A Factory Method for creating symbolic values. The symbol
         * has as origin the identity hash code of an object.  
         * 
         * @param object an {@link Objekt}, the object whose identity hash 
         *        code is this symbol. It must refer an instance or an array.
         * @return a {@link PrimitiveSymbolic}.
         */
        public PrimitiveSymbolic createSymbolIdentityHashCode(Objekt object) {
        try {
            final PrimitiveSymbolicHashCode retVal = new PrimitiveSymbolicHashCode(null, this.getNextIdPrimitiveSymbolic(), object.historyPoint(), this.calc);
            return retVal;
        } catch (InvalidTypeException e) {
            //this should never happen
            throw new UnexpectedInternalException(e);
        }
        }
        
	private int getNextIdPrimitiveSymbolic() {
		final int retVal = this.nextIdPrimSym++;
		return retVal;
	}
	
	private int getNextIdReferenceSymbolic() {
		final int retVal = this.nextIdRefSym++;
		return retVal;
	}
	
	@Override
	public SymbolFactory clone() {
		final SymbolFactory o;
		try {
			o = (SymbolFactory) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
		return o;
	}
}
