package api.category

class Categoria {

    String clave;
    String descripcion;
    Categoria categoria;
    Boolean activo = true;

    static constraints = {
        clave(
            blank: false,
            nullable: false
        )
        descripcion(
            blank: false,
            nullable: false
        )
        categoria(
            nullable: true
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
