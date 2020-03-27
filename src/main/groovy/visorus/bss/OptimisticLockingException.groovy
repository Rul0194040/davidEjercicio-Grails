package visorus.bss

/**
 * Created by Orlando
 */
class OptimisticLockingException extends RuntimeException{
	Object invalidObject

	OptimisticLockingException(String message, Object invalidObject){
		super(message)
		this.invalidObject = invalidObject
	}

	OptimisticLockingException(String message){
		super(message)
	}
}
