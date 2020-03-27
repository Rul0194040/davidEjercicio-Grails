package visorus.bss
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import grails.util.Holders

class Message {

	/**
	 *
	 * @param mensajeMap
	 * @return msg
	 */
static String getMensaje(Map mensajeMap){
	Object[] objArgs = mensajeMap.parametros != null ? mensajeMap.parametros.toArray() : null
	Locale locale=mensajeMap.local!=null ? mensajeMap.local: new java.util.Locale("ES")

	def msg = Holders.grailsApplication.mainContext.getMessage(mensajeMap.codigo, objArgs,mensajeMap.defaultMessage?:'',locale )
	return msg
}
		/**
		 *
		 * @param mensajeObject
		 * @return msg
		 */
	static String getMensaje(Mensaje mensajeObject){
		Object[] objArgs = mensajeObject.parametros != null ? mensajeObject.parametros.toArray() : null
		Locale locale=mensajeObject.local!=null ? mensajeObject.local: new java.util.Locale("ES")

		def msg = Holders.grailsApplication.mainContext.getMessage(mensajeObject.codigo, objArgs,mensajeObject.defaultMessage?:'',locale )
		return msg
   }
	/**
	 *
	 * @param codigo
	 * @param parametros
	 * @param defaultMessage
	 * @return msg
	 */
	static String getMensaje(String codigo, List parametros, String defaultMessage, Locale locale=new java.util.Locale("ES")){
		Object[] objArgs = parametros != null ? parametros.toArray() : null
		def msg = Holders.grailsApplication.mainContext.getMessage(codigo, objArgs,defaultMessage,locale )
		return msg
	}
	/**
	 *
	 * @param codigo
	 * @param parametros
	 * @return msg
	 */
	static String getMensaje(String codigo,List parametros,Locale locale=new java.util.Locale("ES")){
		Object[] objArgs = parametros != null ? parametros.toArray() : null


		def msg = Holders.grailsApplication.mainContext.getMessage(codigo, objArgs,locale )
		return msg
   }
	/**
	 *
	 * @param codigo
	 * @param defaultMessage
	 * @return msg
	 */
	static String getMensaje(String codigo,String defaultMessage,Locale locale=new java.util.Locale("ES")){

				def msg = Holders.grailsApplication.mainContext.getMessage(codigo,null,defaultMessage,locale )
				return msg
		   }
	/**
	 *
	 * @param codigo
	 * @param locale
	 * @return msg
	 */
	static String getMensaje(String codigo,Locale locale=new java.util.Locale("ES")){

		def msg = Holders.grailsApplication.mainContext.getMessage(codigo,null,locale )
		return msg
   }
	/**
	 *
	 * @param org.springframework.validation.FieldError codigo
	 * @param locale
	 * @return msg
	 */
	static String getMensaje(org.springframework.validation.FieldError codigo,Locale locale=new java.util.Locale("ES")){

				def msg = Holders.grailsApplication.mainContext.getMessage(codigo,locale )
				return msg
		   }

}
