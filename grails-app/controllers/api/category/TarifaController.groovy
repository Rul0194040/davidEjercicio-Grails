package api.category


import grails.rest.*
import grails.converters.*
import org.springframework.http.HttpStatus
import visorus.bss.BetterMap
import visorus.bss.ExceptionStatus
import visorus.bss.Message
import visorus.bss.ObjectException

class TarifaController {
	static responseFormats = ['json', 'xml']
    TarifaService tarifaService
	
    def index() {
        try {
            BetterMap map = new BetterMap(params)
            List<Articulo> articuloList = tarifaService.list(map)
            long total = tarifaService.count(map)
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
            Tarifa tarifa = tarifaService.create(map)
            respond(tarifa)
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
                    tarifaService.get(id)
            )
        } catch (Exception e) {
            e.printStackTrace()
            respond(status: HttpStatus.INTERNAL_SERVER_ERROR, [error: e.getMessage()])
        }
    }
    def actualizar() {
        try {
            BetterMap map = new BetterMap(request)
            Tarifa tarifa = tarifaService.update(map)

            Map result = [
                    message: Message.getMensaje(codigo: 'default.updated.message',
                            parametros: [Message.getMensaje('tarifa.label', 'Tarifa'), tarifa.id]),
                    data: tarifa.Obtener()
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
            Tarifa tarifa = tarifaService.delete(id)
            Map result = [
                    message: Message.getMensaje(codigo: 'default.disabled.message',
                            parametros: [Message.getMensaje('tarifa.label', 'Tarifa'), tarifa.id])
            ]
            respond(status: HttpStatus.OK, result)
        } catch (Exception e) {
            e.printStackTrace()
            respond(status: HttpStatus.INTERNAL_SERVER_ERROR, [error: e.getMessage()])

        }
    }
}
