package api.category

class Precio {

    Integer numero;
    Float precio;

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
            precio      : precio
    ]}
}
