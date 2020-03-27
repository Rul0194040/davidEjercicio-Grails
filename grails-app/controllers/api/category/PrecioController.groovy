package api.category


import grails.rest.*
import grails.converters.*
import org.springframework.http.HttpStatus
import visorus.bss.BetterMap
import visorus.bss.ExceptionStatus
import visorus.bss.Message
import visorus.bss.ObjectException

class PrecioController {
	static responseFormats = ['json', 'xml']
    PrecioService precioService
	
    def index() {
        try {
            BetterMap map = new BetterMap(params)
            List<Articulo> articuloList = precioService.list(map)
            long total = precioService.count(map)
            respond(status: HttpStatus.OK, [data: articuloList*.ObtenerArticulo(), total: total])
        } catch (ExceptionStatus e) {
            respond(status: e.status, e.responseObject)
        } catch (Exception e) {
            e.printStackTrace()
            respond(status: HttpStatus.INTERNAL_SERVER_ERROR, [error: e.getMessage()])
        }
    }
    def crear() {
        try {
            BetterMap map = new BetterMap(request)
            println(map)
            Precio Precio = precioService.create(map)
            respond(Precio)
        }catch (ObjectException e) {
            respond(status: e.status, e.responseObject)
        } catch (Exception e) {
            e.printStackTrace()
            respond(
                    [error: e.getMessage()],
                    status: HttpStatus.INTERNAL_SERVER_ERROR
            )
        }

    }
    def leer(long id) {
        try {
            BetterMap map = new BetterMap(params)
            println(map)
            respond(
                    status: HttpStatus.OK,
                    precioService.get(id)
            )
        } catch (Exception e) {
            e.printStackTrace()
            respond(status: HttpStatus.INTERNAL_SERVER_ERROR, [error: e.getMessage()])
        }
    }
    def actualizar() {
        try {
            BetterMap map = new BetterMap(request)
            Precio Precio = precioService.update(map)

            Map result = [
                    message: Message.getMensaje(codigo: 'default.updated.message',
                            parametros: [Message.getMensaje('Precio.label', 'Precio'), Precio.id]),
                    data: Precio
            ]
            respond(status: HttpStatus.OK, result)
        } catch (ObjectException e) {
            respond(status: HttpStatus.BAD_REQUEST, e.responseObject)
        } catch (Exception e) {
            e.printStackTrace()
            respond(status: HttpStatus.INTERNAL_SERVER_ERROR, [error: e.getMessage()])
        }
    }
    def borrar(long id) {
        try {
            Precio Precio = precioService.delete(id)
            Map result = [
                    message: Message.getMensaje(codigo: 'default.disabled.message',
                            parametros: [Message.getMensaje('Precio.label', 'Precio'), Precio.id])
            ]
            respond(status: HttpStatus.OK, result)
        } catch (Exception e) {
            e.printStackTrace()
            respond(status: HttpStatus.INTERNAL_SERVER_ERROR, [error: e.getMessage()])

        }
    }
}
