package api.category

class Precio {

    Integer numero = 1
    Float precio = 1000

    static constraints = {

        numero(
            blank: false,
            nullable: false)
        precio(
            blank: false,
            nullable: false)
    }

    Map ObtenerPrecio() {[
            id          : id,
            numero      : numero,
            precio      : precio,
    ]}
}
