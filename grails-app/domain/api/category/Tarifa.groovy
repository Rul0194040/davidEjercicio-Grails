package api.category

class Tarifa {

    String nombre;
    Precio precio;

    static constraints = {
        nombre(
            blank: false,
            nullable: false
        )
        precio(
            blank: false,
            nullable: false
        )
    }

    Map ObtenerTarifa() {[
            id           : id,
            nombre  : nombre,
            precio    : precio

    ]}
}
