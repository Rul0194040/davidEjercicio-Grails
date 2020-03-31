package api.category

import org.springframework.http.HttpStatus
import visorus.bss.*

class CategoriaController {
    CategoriaService categoriaService
	static responseFormats = ['json', 'xml']
	
    def index() {
        try {
            BetterMap map = new BetterMap(params)
            List<Articulo> articuloList = categoriaService.list(map)
            long total = categoriaService.count(map)
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
            println("*******REQUEST********")
            println(map)
            println("*******PARAMS********")
            Categoria categoria = categoriaService.create(map)
            respond(categoria)
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
                    categoriaService.get(id).Obtener()
            )
        } catch (Exception e) {
            e.printStackTrace()
            respond(status: HttpStatus.INTERNAL_SERVER_ERROR, [error: e.getMessage()])
        }
    }
    def actualizar() {
        try {
            BetterMap map = new BetterMap(request)
            Categoria categoria = categoriaService.update(map)

            Map result = [
                    message: Message.getMensaje(codigo: 'default.updated.message',
                    parametros: [Message.getMensaje('categoria.label', 'Categoria'), categoria.id]),
                    data: categoria.Obtener()
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
            Categoria categoria = categoriaService.delete(id)
            Map result = [
                    message: Message.getMensaje(codigo: 'default.disabled.message',
                    parametros: [Message.getMensaje('categoria.label', 'Categoria'), categoria.id])
            ]
            respond(status: HttpStatus.OK, result)
        } catch (Exception e) {
            e.printStackTrace()
            respond(status: HttpStatus.INTERNAL_SERVER_ERROR, [error: e.getMessage()])

        }
    }
}
