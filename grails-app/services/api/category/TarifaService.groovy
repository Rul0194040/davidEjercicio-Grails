package api.category

import grails.gorm.transactions.Transactional
import org.springframework.http.HttpStatus
import visorus.bss.BetterMap
import visorus.bss.ExceptionStatus
import visorus.bss.Message
import visorus.bss.ObjectException

@Transactional
class TarifaService {

    PrecioService precioService

    Tarifa build(BetterMap map, Tarifa tarifa) throws Exception {
        tarifa.nombre = map.optString('nombre', tarifa.nombre)
        Precio precio = precioService.create(map.optObject('precio'))
        tarifa.precio = precio


        return tarifa
    }


    void validate(Tarifa tarifa) throws ExceptionStatus {
        Tarifa busqueda = Tarifa.find('from Tarifa where activo = false and clave = ?0', [tarifa.clave])
        if (busqueda != null) {
            String message = Message.getMensaje([
                    codigo    : 'default.not.unique.inactive.message',
                    parametros: ['clave', Message.getMensaje('tarifa.label', 'Articulo'), busqueda.clave],
            ])
            throw new ObjectException(message, 'default.not.unique.inactive.message', HttpStatus.BAD_REQUEST, [id: busqueda.id, url: 'tarifa'])
        }
    }
    Tarifa save(Tarifa tarifa) throws Exception {
        try {
            if (tarifa.validate() && tarifa.save(flush: true))
                return tarifa
            throw new ObjectException(
                    Message.getMensaje(
                            codigo: 'default.save.failed.message',
                            parametros: [Message.getMensaje('Tarifa.label', 'tarifa')]), tarifa)
        } catch (e) {
            throw e
        }
    }

    Tarifa create(BetterMap map) throws Exception {
        Tarifa tarifa = new Tarifa()
        build(map, tarifa)

        save(tarifa)
    }
    Tarifa get(long id) throws Exception {
        Tarifa tarifa = Tarifa.get(id)
        if (tarifa == null)
            throw new ExceptionStatus(
                    Message.getMensaje(
                            codigo: 'default.not.found.message',
                            parametros: [Message.getMensaje('Tarifa.label', 'Tarifa'), id]
                    )
            )
        return tarifa
    }
    Tarifa update(BetterMap map) throws Exception {
        Tarifa tarifa = get(map.optLong("id", -1))//porque menos 1 ?

        if (map.optLong("version", -1) > -1) {//Que es version?
            if (tarifa.version > (map.getLong("version"))) {
                throw new ObjectException(Message.getMensaje([
                        codigo    : "default.optimistic.locking.failure",
                        parametros: [Message.getMensaje('tarifa.label', 'Tarifa')]
                ]), tarifa, true)
            }
        }
        build(map, tarifa)
        save(tarifa)
    }// DOS DUDAS !!
    Tarifa delete(long id) throws Exception {
        Tarifa tarifa = get(id)
        tarifa.activo = false
        save(tarifa)
    }

    List<Tarifa> list(BetterMap params) throws Exception {

        Tarifa tarifa = Tarifa.list(params)
        return  tarifa


    }
    long count(BetterMap params) throws Exception {

        long tarifa = Tarifa.count(params)
        return  tarifa

    }


}
