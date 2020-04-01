package api.category

class Articulo {

    Categoria categoria
    String nombre
    Date fechaAlta
    Boolean activo = true
    String clave
    Tarifa tarifa

    static constraints = {

        categoria(
            blank: false,
            nullable: false
        )
        nombre(
            blank: false,
            nullable: false
        )
        fechaAlta(
            blank: false,
            nullable: false
        )
        activo(
            blank: false,
            nullable: false
        )
        clave(
            blank: false,
            nullable: false
        )
        tarifa(
            blank: false,
            nullable: false
        )
    }


    Map ObtenerArticulo() {[
        id         : id,
        nombre     : nombre,
        tarifa     : tarifa,
        categoria  : categoria,
        activo     : activo,
        fechaAlta  : fechaAlta
    ]}


}
