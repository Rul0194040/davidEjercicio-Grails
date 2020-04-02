package api.category


import grails.rest.*
import grails.converters.*
import javafx.scene.chart.ScatterChart
import org.springframework.http.HttpStatus
import visorus.bss.BetterMap
import visorus.bss.ExceptionStatus
import visorus.bss.Message
import visorus.bss.ObjectException

class ArticuloController {
	static responseFormats = ['json', 'xml']
    ArticuloService articuloService
	
    def index() {
        try {
            BetterMap map = new BetterMap(params)
            List<Articulo> articuloList = articuloService.list(map)
            long total = articuloService.count(map)
            respond(status: HttpStatus.OK, [data: articuloList*.ObtenerArticulo(), total: total])
        } catch (ExceptionStatus e) {
            respond(status: e.status, e.responseObject)
        } catch (Exception e) {
            e.printStackTrace()
            respond(status: HttpStatus.INTERNAL_SERVER_ERROR, [error: e.getMessage()])
        }
    } //lista todos los articulos
    def crear() {
        try {
            BetterMap map = new BetterMap(request)
            println(map)
            Articulo articulo = articuloService.create(map)
            respond(articulo)
        }catch (ObjectException e) {
            respond(status: e.status, e.responseObject)
        } catch (Exception e) {
            e.printStackTrace()
            respond(
                    [error: e.getMessage()],
                    status: HttpStatus.INTERNAL_SERVER_ERROR
            )
        }

    } // crea un articulo
    def leer(long id) {
        try {
            BetterMap map = new BetterMap(params)
            println("!!!")
            println(map)
            println("!!!")
            respond(
                    status: HttpStatus.OK,
                    articuloService.get(id)
            )
        } catch (Exception e) {
            e.printStackTrace()
            respond(status: HttpStatus.INTERNAL_SERVER_ERROR, [error: e.getMessage()])
        }
    } // lee el articulo del id proporcionado
    def actualizar() {
        try {
            BetterMap map = new BetterMap(request)
            Articulo articulo = articuloService.update(map)

            Map result = [
                    message: Message.getMensaje(codigo: 'default.updated.message',
                            parametros: [Message.getMensaje('articulo.label', 'api.category.Articulo'), articulo.id]),
                    data: articulo.ObtenerArticulo()
            ]
            respond(status: HttpStatus.OK, result)
        } catch (ObjectException e) {
            respond(status: HttpStatus.BAD_REQUEST, e.responseObject)
        } catch (Exception e) {
            e.printStackTrace()
            respond(status: HttpStatus.INTERNAL_SERVER_ERROR, [error: e.getMessage()])
        }
    } // actualiza el articulo del id proporcionado
    def borrar(long id) {
        try {
            Articulo articulo = articuloService.delete(id)
            Map result = [
                    message: Message.getMensaje(codigo: 'default.disabled.message',
                            parametros: [Message.getMensaje('articulo.label', 'api.category.Articulo'), articulo.id])
            ]
            respond(status: HttpStatus.OK, result)
        } catch (Exception e) {
            e.printStackTrace()
            respond(status: HttpStatus.INTERNAL_SERVER_ERROR, [error: e.getMessage()])

        }
    } // borra el articulo del id proporcionado

    def booking() {
        try {
            BetterMap map = new BetterMap(request)
            Articulo articulo = articuloService.create(map)
            respond(articulo.ObtenerArticulo())
        }catch (ObjectException e) {
            respond(status: HttpStatus.BAD_REQUEST, e.responseObject)
        }
    }
}
