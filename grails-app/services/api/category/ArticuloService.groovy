package api.category

import grails.gorm.transactions.Transactional
import org.springframework.http.HttpStatus
import visorus.bss.*

@Transactional
class ArticuloService {

    CategoriaService categoriaService
    TarifaService tarifaService

    Articulo get(long id) throws Exception {
        Articulo articulo = Articulo.get(id)
        if (articulo == null)
            throw new ExceptionStatus(
                    Message.getMensaje(
                            codigo: 'default.not.found.message',
                            parametros: [Message.getMensaje('api.category.Articulo', 'api.category.Articulo'), id]
                    )
            )
        return articulo
    }

    Articulo update(BetterMap map,long id) throws Exception {
        Articulo articulo = this.get(id)
        build(map, articulo)
        save(articulo)
    }
    Articulo delete(long id) throws Exception {
        Articulo articulo = get(id)
        articulo.activo = false
        save(articulo)
    }
    void validate(Articulo articulo) throws ExceptionStatus {
        Articulo a = Articulo.find('from Articulo where activo = false and clave = ?0', [articulo.clave])
        if (a != null) {
            String message = Message.getMensaje([
                    codigo    : 'default.not.unique.inactive.message',
                    parametros: ['codigo', Message.getMensaje('articulo.label', 'Articulo'), articulo.clave],
            ])
            throw new ExceptionStatus(message, 'default.not.unique.inactive.message', HttpStatus.BAD_REQUEST, [id: a.id, url: 'articulo'])
        }
    }
    Articulo save(Articulo articulo) throws Exception {
        try {
            if (/*articulo.validate() &&*/ articulo.save(flush: true))
                return articulo
            throw new ObjectException(
                    Message.getMensaje(
                            codigo: 'default.save.failed.message',
                            parametros: [Message.getMensaje('articulo.label', 'Articulo')]
                    ), articulo)
        } catch (e) {
            throw e
        }
    }


    Articulo build(BetterMap map, Articulo articulo) throws Exception {
        articulo.fechaAlta = map.optDate('fechaAlta', articulo.fechaAlta)
        articulo.nombre = map.optString('nombre', articulo.nombre)
        articulo.activo = map.optBoolean('activo', articulo.activo)
        articulo.clave = map.optString('clave', articulo.clave)
        if (articulo.id != null) {
            Categoria categoria = categoriaService.update(map.optObject('categoria'), articulo.categoria.id)
            Tarifa tarifa = tarifaService.update(map.optObject('tarifa'), articulo.tarifa.id)
            articulo.categoria = categoria
            articulo.tarifa = tarifa
            return  articulo
        }else {
            Categoria categoria = categoriaService.create(map.optObject('categoria'))
            Tarifa tarifa = tarifaService.create(map.optObject('tarifa'))
            articulo.categoria = categoria
            articulo.tarifa = tarifa
            return articulo
        }
    }

    Articulo addtarifa(BetterMap map, long id) throws Exception {
        Articulo articulo = this.get(id)
        tarifaService.add(map, articulo)
        save(articulo)
    }

    Articulo create(BetterMap map) throws Exception {
        Articulo articulo = new Articulo()
        build(map, articulo)
        validate(articulo)
        save(articulo)
    }
    List<Articulo> list(BetterMap params) throws Exception {

        Articulo articulo = Articulo.list(params)
        return  articulo


    }
    long count(BetterMap params) throws Exception {

        long articulo = Articulo.count(params)
        return  articulo

    }



}












