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
        if (map["id"] != null) {
          this.update(map)
        }else{
            build(map, categoria)
            save(categoria)
        }
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
    Categoria update(BetterMap map, long id) throws Exception {
        Categoria categoria = this.get(id)
        build(map, categoria)
        save(categoria)
    }
    Categoria delete(long id) throws Exception {
        Categoria categoria = get(id)
        categoria.activo = false
        save(categoria)
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
