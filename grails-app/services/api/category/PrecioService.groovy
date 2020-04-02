package api.category

import grails.gorm.transactions.Transactional
import org.springframework.http.HttpStatus
import visorus.bss.BetterMap
import visorus.bss.ExceptionStatus
import visorus.bss.Message
import visorus.bss.ObjectException

@Transactional
class PrecioService {

   Precio build(BetterMap map,Precio precio) throws Exception {
        precio.numero = map.optInt('numero', precio.numero)
        precio.precio = map.optFloat('precio', precio.precio)


        return precio
    }
    void validate(Precio precio) throws ExceptionStatus {
        Precio busqueda = Precio.find('from Precio where activo = false and clave = ?0', [precio.clave])
        if (busqueda != null) {
            String message = Message.getMensaje([
                    codigo    : 'default.not.unique.inactive.message',
                    parametros: ['clave', Message.getMensaje('precio.label', 'Articulo'), busqueda.clave],
            ])
            throw new ObjectException(message, 'default.not.unique.inactive.message', HttpStatus.BAD_REQUEST, [id: busqueda.id, url: 'precio'])
        }
    }
    Precio save(Precio precio) throws Exception {
        try {
            if (precio.validate() && precio.save(flush: true))
                return precio
            throw new ObjectException(
                    Message.getMensaje(
                            codigo: 'default.save.failed.message',
                            parametros: [Message.getMensaje('Precio.label', 'precio')]), precio)
        } catch (e) {
            throw e
        }
    }

    Precio create(BetterMap map) throws Exception {
        Precio precio = new Precio()
        build(map, precio)
        save(precio)
    }
    Precio get(long id) throws Exception {
        Precio precio = Precio.get(id)
        if (precio == null)
            throw new ExceptionStatus(
                    Message.getMensaje(
                            codigo: 'default.not.found.message',
                            parametros: [Message.getMensaje('Precio.label', 'Precio'), id]
                    )
            )
        return precio
    }
    Precio update(BetterMap map) throws Exception {
        Precio precio = get(map.optLong("id", -1))//porque menos 1 ?

        if (map.optLong("version", -1) > -1) {//Que es version?
            if (precio.version > (map.getLong("version"))) {
                throw new ObjectException(Message.getMensaje([
                        codigo    : "default.optimistic.locking.failure",
                        parametros: [Message.getMensaje('precio.label', 'Precio')]
                ]), precio, true)
            }
        }
        build(map, precio)
        save(precio)
    }// DOS DUDAS !!
    Precio delete(long id) throws Exception {
        Precio precio = get(id)
        precio.activo = false
        save(precio)
    }

    List<Precio> list(BetterMap params) throws Exception {

        Precio precio = Precio.list(params)
        return  precio


    }
    long count(BetterMap params) throws Exception {

        long precio = Precio.count(params)
        return  precio

    }


}
