package api.category

class Categoria {
    String descripcion
    Boolean activo = true

    static constraints = {
        descripcion(
            blank: false,
            nullable: false
        )
        activo(
            blank: false,
            nullable: false
        )
    }

    static mapping = {
        id generator: 'sequence',
        params: [sequence_name: 'seq_categoria_id']
    }

    Map Obtener() {[
            id           : id,
            descripcion  : descripcion,
            categoria    : categoria,
            activo       : activo,
            clave        : clave
    ]}

}
