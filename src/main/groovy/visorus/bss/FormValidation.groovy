package visorus.bss

import grails.util.GrailsNameUtils

@groovy.util.logging.Log4j
class FormValidation {

	def static errorInstanciaLog(def instance) {
		try {
			instance.validate()
			if (instance.hasErrors()) {
				instance.errors.allErrors.each { log.error instance.getClass()?.getName() + " : " + Message.getMensaje(it) }
			}
		}
		catch (e) {
			println(e.getMessage())
			return null
		}
	}
	/**
	 * @object instance*
	 * se regresa el objeto en un arreglo debido a que los errores de json generados por el messagesource de allerrors regresa el mismo objeto para  compatibilidad
	 * return array[errors:[lista de errores]]
	 * */
	def static formUnitarioMap(def instance) {
		try {

			instance.validate()
			if (instance.hasErrors()) {
				return instance.errors.allErrors.collect { [error: Message.getMensaje(it)] }
			}
		}
		catch (e) {
			println(e.getMessage())
			return null
		}
	}

	def static formUnitario(def instance) {
		try {
			instance.validate()
			if (instance.hasErrors()) {
				instance.errors.allErrors.each { log.error instance.getClass()?.getName() + " : " + Message.getMensaje(it) }


				return [error: [instance.errors]]

			}
		}
		catch (e) {
			println(e.getMessage())
			return null
		}
	}

	def static formMultiple(def instance) {
		try {
			instance.validate()
			if (instance.hasErrors()) {
				instance.errors.allErrors.each { log.error instance.getClass()?.getName() + " : " + Message.getMensaje(it) }

				def map = [
						(Message.getMensaje(
								[
										label         : (GrailsNameUtils.getPropertyName(instance.getClass()) + ".label"),
										defaultMessage: instance.getClass()?.getName()
								]
						)): [instance.errors]]
				return map
			}
		}
		catch (e) {
			println(e.getMessage())
			return null
		}
	}


	def static formMultipleList(List instances) {
		try {
			ArrayList errores = new ArrayList()
			for (instance in instances) {
				instance.validate()
				if (instance.hasErrors()) {
					instance.errors.allErrors.each { log.error instance.getClass()?.getName() + " : " + Message.getMensaje(it) }

					errores.add([
							(Message.getMensaje(
									[
											label         : (GrailsNameUtils.getPropertyName(instance.getClass()) + ".label"),
											defaultMessage: instance.getClass()?.getName()
									]
							)): [instance.errors]])

				}
			}
			def map = [error: errores]
			return map
		}
		catch (e) {
			println(e.getMessage())
			return null
		}
	}
}
