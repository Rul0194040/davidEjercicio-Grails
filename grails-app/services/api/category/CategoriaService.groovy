package api.category

import grails.gorm.transactions.Transactional
import org.springframework.http.HttpStatus
import visorus.bss.*

@Transactional
class CategoriaService {

    Categoria build(BetterMap map, Categoria categoria) throws Exception {
        categoria.descripcion = map.optString('descripcion', categoria.descripcion)
        categoria.activo = map.optBoolean('activo', categoria.activo)

        return categoria
    }
    void validate(Categoria categoria) throws ExceptionStatus {
        Categoria busqueda = Categoria.find('from Categoria where activo = false and clave = ?0', [categoria.clave])
        if (busqueda != null) {
            String message = Message.getMensaje([
                    codigo    : 'default.not.unique.inactive.message',
                    parametros: ['clave', Message.getMensaje('categoria.label', 'Articulo'), busqueda.clave],
            ])
            throw new ObjectException(message, 'default.not.unique.inactive.message', HttpStatus.BAD_REQUEST, [id: busqueda.id, url: 'categoria'])
        }
    }
    Categoria save(Categoria categoria) throws Exception {
        try {
            if (categoria.validate() && categoria.save(flush: true))
                return categoria
            throw new ObjectException(
                    Message.getMensaje(
                            codigo: 'default.save.failed.message',
                            parametros: [Message.getMensaje('Categoria.label', 'categoria')]), categoria)
        } catch (e) {
            throw e
        }
    }

    Categoria create(BetterMap map) throws Exception {
        Categoria categoria = new Categoria()
        build(map, categoria)
       // validate(categoria)
        save(categoria)
    }
    Categoria get(long id) throws Exception {
        Categoria categoria = Categoria.get(id)
        if (categoria == null)
            throw new ExceptionStatus(
                    Message.getMensaje(
                    codigo: 'default.not.found.message',
                    parametros: [Message.getMensaje('Categoria.label', 'Categoria'), id]
                    )
            )
        return categoria
    }
    Categoria update(BetterMap map) throws Exception {
        Categoria categoria = get(map.optLong("id", -1))//porque menos 1 ?

        if (map.optLong("version", -1) > -1) {//Que es version?
            if (categoria.version > (map.getLong("version"))) {
                throw new ObjectException(Message.getMensaje([
                        codigo    : "default.optimistic.locking.failure",
                        parametros: [Message.getMensaje('categoria.label', 'Categoria')]
                ]), categoria, true)
            }
        }
        build(map, categoria)
        save(categoria)
    }// DOS DUDAS !!
    Categoria delete(long id) throws Exception {
        Categoria categoria = get(id)
        categoria.activo = false
        save(categoria)
    }

    Categoria saveAnidado(BetterMap map, Articulo articulo) {
        Categoria categoria = new  Categoria()
        this.build(map, categoria)
        articulo.categoria = categoria
        this.save(categoria)

        return  categoria
    }

    List<Categoria> list(BetterMap params) throws Exception {

        Categoria categoria = Categoria.list(params)
        return  categoria


    }
    long count(BetterMap params) throws Exception {

        long categoria = Categoria.count(params)
        return  categoria

    }


}
