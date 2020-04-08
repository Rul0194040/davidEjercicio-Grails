package api.category

class Articulo {

    Categoria categoria
    String nombre
    Date fechaAlta
    Boolean activo = true
    String clave
    Tarifa tarifas

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
        tarifas(
            blank: false,
            nullable: false
        )
    }

    static hasMany = [tarifas: Tarifa]

    Map ObtenerArticulo() {[
        id         : id,
        nombre     : nombre,
        activo     : activo,
        fechaAlta  : fechaAlta,
        tarifas    : tarifas.ObtenerTarifa(),
        categoria  : categoria.Obtener(),
    ]}


}
