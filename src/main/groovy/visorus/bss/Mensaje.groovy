package visorus.bss

class Mensaje {
String codigo
List parametros
Locale local

String defaultMessage


	String getDefaultMessage() {
	return defaultMessage
	}

	void setDefaultMessage(String defaultMessage) {
	this.defaultMessage = defaultMessage
	}

	String getCodigo() {
	return codigo
	}

	void setCodigo(String codigo) {
	this.codigo = codigo
	}

	List getParametros() {
	return parametros
	}

	void setParametros(List parametros) {
	this.parametros = parametros
	}

	Locale getLocal() {
	return local
	}

	void setLocal(Locale local) {
	this.local = local
	}
}
