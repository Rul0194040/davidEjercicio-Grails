package visorus.bss

class ApiError extends Throwable {

	Object invalidObject
	Object actualResponse

	ApiError(String message, Object invalidObject) {
		super(message)
		this.invalidObject = invalidObject
	}

	ApiError(Object invalidObject) {
		super("Error en servidor")
		this.invalidObject = invalidObject
	}

	ApiError(Object invalidObject, Object actualResponse) {
		super("Error en servidor.")
		this.invalidObject = invalidObject
		this.actualResponse = actualResponse
	}

	ApiError(String message) {
		super(message)
	}
}
