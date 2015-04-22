package jbse.algo.meta;

import static jbse.algo.Util.throwVerifyError;
import static jbse.bc.Offsets.INVOKESPECIALSTATICVIRTUAL_OFFSET;
import static jbse.bc.Signatures.JAVA_THROWABLE_STACKTRACE;

import jbse.algo.Algorithm;
import jbse.algo.ExecutionContext;
import jbse.algo.exc.InterruptException;
import jbse.mem.Instance;
import jbse.mem.State;
import jbse.mem.exc.InvalidProgramCounterException;
import jbse.mem.exc.OperandStackEmptyException;
import jbse.mem.exc.ThreadStackEmptyException;
import jbse.val.Null;
import jbse.val.Reference;

public final class Algo_JAVA_THROWABLE_FILLINSTACKTRACE implements Algorithm {
	@Override
	public void exec(State state, ExecutionContext ctx) 
	throws ThreadStackEmptyException, InterruptException {
	    try {
	        final Reference thisRef = (Reference) state.popOperand(); //pops "this"
	        final Instance exc = (Instance) state.getObject(thisRef);
	        
	        //TODO replace this dummy implementation
	        exc.setFieldValue(JAVA_THROWABLE_STACKTRACE, Null.getInstance());
	        
	        state.pushOperand(thisRef); //returns "this"
	    } catch (OperandStackEmptyException | ClassCastException e) {
	        throwVerifyError(state);
	        throw InterruptException.getInstance();
	    }
		
        try {
			state.incPC(INVOKESPECIALSTATICVIRTUAL_OFFSET);
		} catch (InvalidProgramCounterException e) {
            throwVerifyError(state);
		}
        throw InterruptException.getInstance();
	}
}
