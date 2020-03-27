package visorus.bss

import org.springframework.http.HttpStatus

/**
 * Created by Orlando
 */
class ExceptionStatus extends RuntimeException {

	HttpStatus status = HttpStatus.BAD_REQUEST
	String errorCode = ''
	boolean optimisticLockingFailure = false
	Map responseObject = [:]

	ExceptionStatus(String message, String errorCode, HttpStatus status, Map data) {
		super(message)
		this.errorCode = errorCode
		this.status = status
		this.responseObject = [error: message, errorCode: errorCode, status: status, data: data]
	}

	ExceptionStatus(String message, String errorCode, HttpStatus status) {
		super(message)
		this.errorCode = errorCode
		this.status = status
		this.responseObject = [error: message, errorCode: errorCode, status: status]
	}

	ExceptionStatus(String message) {
		super(message)
		this.responseObject = [error: message, errorCode: errorCode, status: status]
	}

	ExceptionStatus(String message, String errorCode) {
		super(message)
		this.errorCode = errorCode
		this.responseObject = [error: message, errorCode: errorCode, status: status]
	}

	ExceptionStatus(HttpStatus status) {
		this.status = status
		this.responseObject = [error: getMessage(), errorCode: errorCode, status: status]
	}
}
