package visorus.bss

import org.springframework.http.HttpStatus


class ObjectException extends RuntimeException {


	HttpStatus status = HttpStatus.BAD_REQUEST
	String errorCode = ''
	boolean optimisticLockingFailure = false
	Map responseObject = [:]
	Object invalidObject

	// usados para formularios
	ObjectException(String message, Object invalidObject) {
		super(message)
		this.invalidObject = invalidObject
		this.responseObject = [error: message, errores: FormValidation.formUnitarioMap(invalidObject)]
	}

	ObjectException(String message, Object invalidObject, boolean optimisticLockingFailure) {
		super(message)
		this.invalidObject = invalidObject
		this.optimisticLockingFailure = optimisticLockingFailure
		this.responseObject = [error: message, latest: invalidObject.obtieneDatos()]
	}

	// Excepciones generales
	ObjectException(String message, String errorCode, HttpStatus status, Map data) {
		super(message)
		this.errorCode = errorCode
		this.status = status
		this.responseObject = [error: message, errorCode: errorCode, status: status.value(), data: data]
	}

	ObjectException(String message, String errorCode, HttpStatus status) {
		super(message)
		this.errorCode = errorCode
		this.status = status
		this.responseObject = [error: message, errorCode: errorCode, status: status.value()]
	}

	ObjectException(String message, String errorCode) {
		super(message)
		this.errorCode = errorCode
		this.responseObject = [error: message, errorCode: errorCode, status: status.value()]
	}

	//@Deprecated
	ObjectException(String message) {
		super(message)
	}
}
